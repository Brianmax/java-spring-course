#!/usr/bin/env python3
"""Structural checks for a Java Course self-contained HTML slide deck."""

from __future__ import annotations

import argparse
import re
import sys
from html.parser import HTMLParser
from pathlib import Path
from urllib.parse import urlparse


REMOTE_SCHEMES = {"http", "https", "ftp", "file", "ws", "wss"}
NETWORK_PATTERNS = {
    "fetch()": r"\bfetch\s*\(",
    "XMLHttpRequest": r"\bXMLHttpRequest\b",
    "WebSocket": r"\bWebSocket\s*\(",
    "EventSource": r"\bEventSource\s*\(",
    "dynamic import": r"\bimport\s*\(",
}


class DeckParser(HTMLParser):
    def __init__(self) -> None:
        super().__init__(convert_charrefs=True)
        self.styles = 0
        self.scripts = 0
        self.slide_count = 0
        self.title_text: list[str] = []
        self._in_title = False
        self.external_issues: list[str] = []
        self.ids: set[str] = set()
        self.duplicate_ids: set[str] = set()
        self.id_references: list[tuple[str, str]] = []
        self.lang = ""
        self.charset = ""
        self.viewport = ""
        self.css: list[str] = []
        self.javascript: list[str] = []
        self._in_style = False
        self._in_script = False
        self._svg_depth = 0
        self.untyped_buttons = 0

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        values = {name.lower(): (value or "") for name, value in attrs}
        tag = tag.lower()

        if tag == "svg":
            self._svg_depth += 1
        if tag == "html":
            self.lang = values.get("lang", "")
        if tag == "meta":
            if "charset" in values:
                self.charset = values["charset"]
            if values.get("name", "").lower() == "viewport":
                self.viewport = values.get("content", "")
        if "id" in values and values["id"]:
            identity = values["id"]
            if identity in self.ids:
                self.duplicate_ids.add(identity)
            self.ids.add(identity)
        for attr in ("aria-labelledby", "aria-describedby", "aria-controls", "for"):
            for identity in values.get(attr, "").split():
                self.id_references.append((attr, identity))
        if tag == "button" and values.get("type") not in {"button", "submit", "reset"}:
            self.untyped_buttons += 1
        if tag in {"iframe", "embed", "object"}:
            self.external_issues.append(f"embedded document element <{tag}> is not allowed")
        if values.get("srcset"):
            self.external_issues.append("srcset is not supported; use inline SVG or a single data URI")

        if tag == "style":
            self.styles += 1
            self._in_style = True
        elif tag == "script":
            self.scripts += 1
            self._in_script = True
            if "src" in values:
                self.external_issues.append("script element has a src attribute")
        elif tag == "title" and not self._svg_depth:
            self._in_title = True

        classes = set(values.get("class", "").split())
        if tag == "section" and "slide" in classes:
            self.slide_count += 1

        if tag == "link" and values.get("rel", "").lower() == "stylesheet":
            self.external_issues.append("external stylesheet link found")

        if "style" in values:
            self.css.append(values["style"])
        for attr, value in values.items():
            if attr.startswith("on"):
                self.javascript.append(value)

        for attr in ("src", "href", "xlink:href", "poster", "data"):
            if attr not in values:
                continue
            value = values[attr].strip()
            if not value or value.startswith("#") or value.startswith("data:"):
                continue
            # A lesson may link back to the course landing page. This is navigation,
            # not a runtime dependency on adjacent lesson content or an external asset.
            if tag == "a" and attr == "href" and value == "../":
                continue
            # SVG namespace declarations are not href attributes and are ignored.
            parsed = urlparse(value)
            if parsed.scheme in REMOTE_SCHEMES or value.startswith("//"):
                self.external_issues.append(f"{tag}[{attr}] references {value!r}")
            else:
                self.external_issues.append(
                    f"{tag}[{attr}] references adjacent resource {value!r}"
                )

    def handle_endtag(self, tag: str) -> None:
        if tag.lower() == "svg":
            self._svg_depth = max(0, self._svg_depth - 1)
        if tag.lower() == "title":
            self._in_title = False
        if tag.lower() == "style":
            self._in_style = False
        if tag.lower() == "script":
            self._in_script = False

    def handle_startendtag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        self.handle_starttag(tag, attrs)
        self.handle_endtag(tag)

    def handle_data(self, data: str) -> None:
        if self._in_title:
            self.title_text.append(data.strip())
        if self._in_style:
            self.css.append(data)
        if self._in_script:
            self.javascript.append(data)


def fail(message: str, errors: list[str]) -> None:
    errors.append(message)


def validate(text: str) -> tuple[DeckParser, list[str], list[str]]:
    """Return static findings only; this does not execute JavaScript or render CSS."""
    parser = DeckParser()
    errors: list[str] = []
    warnings: list[str] = []

    try:
        parser.feed(text)
    except Exception as exc:  # HTMLParser is lenient, but report unexpected failures.
        fail(f"could not parse HTML: {exc}", errors)

    if not re.match(r"\s*<!doctype\s+html", text, re.IGNORECASE):
        fail("missing <!doctype html>", errors)
    if not parser.lang.strip():
        fail("missing non-empty html lang attribute", errors)
    if parser.charset.lower().replace("-", "") != "utf8":
        fail("missing UTF-8 charset declaration", errors)
    if not re.search(r"width\s*=\s*device-width", parser.viewport, re.IGNORECASE):
        fail("missing responsive viewport metadata", errors)
    if not parser.title_text or not "".join(parser.title_text).strip():
        fail("missing non-empty <title>", errors)
    if parser.styles < 1:
        fail("missing inline <style>", errors)
    if parser.scripts < 1:
        fail("missing inline <script>", errors)
    if parser.slide_count < 2:
        fail("expected at least two <section class=\"slide\"> elements", errors)

    for issue in parser.external_issues:
        fail(issue, errors)

    for identity in sorted(parser.duplicate_ids):
        fail(f"duplicate id: {identity!r}", errors)
    for attr, identity in parser.id_references:
        if identity not in parser.ids:
            fail(f"{attr} references missing id {identity!r}", errors)

    css = "\n".join(parser.css)
    javascript = "\n".join(parser.javascript)
    if re.search(r"@import\b", css, re.IGNORECASE):
        fail("CSS @import is not allowed", errors)
    if re.search(r"(?:^|[;\n])\s*(?:import|export)\b", javascript):
        fail("static module import/export is not allowed", errors)

    css_urls = re.findall(r"url\(\s*(['\"]?)(.*?)\1\s*\)", css, re.IGNORECASE)
    for _, value in css_urls:
        value = value.strip()
        if value and not value.startswith(("data:", "#")):
            fail(f"CSS url() references non-inline resource {value!r}", errors)

    for label, pattern in NETWORK_PATTERNS.items():
        if re.search(pattern, javascript):
            fail(f"network/module API found: {label}", errors)

    required_behavior_hints = {
        "keyboard handler": r"(?:keydown|keyup)",
        "fullscreen support": r"requestFullscreen",
        "progress UI": r"progress",
        "slide indicator": r"(?:slide-count|slide-indicator|current-slide|aria-valuenow)",
        "reduced-motion support": r"prefers-reduced-motion",
    }
    for label, pattern in required_behavior_hints.items():
        haystack = javascript if label in {"keyboard handler", "fullscreen support"} else text
        if not re.search(pattern, haystack, re.IGNORECASE):
            warnings.append(f"could not detect {label}; verify manually")

    if not re.search(r":focus-visible", css):
        warnings.append("could not detect :focus-visible styling")
    for key in ("Home", "End", "ArrowLeft", "ArrowRight", "ArrowUp", "ArrowDown"):
        if not re.search(rf"\b{key}\b", javascript):
            warnings.append(f"could not detect navigation key {key}; verify manually")
    if parser.untyped_buttons:
        warnings.append(f"{parser.untyped_buttons} button(s) lack an explicit valid type")

    return parser, errors, warnings


def main() -> int:
    arguments = argparse.ArgumentParser(description=__doc__)
    arguments.add_argument("deck", type=Path, help="Standalone lesson HTML")
    arguments.add_argument("--strict", action="store_true", help="Fail on warnings as well as errors")
    args = arguments.parse_args()
    path = args.deck
    if not path.is_file() or path.suffix.lower() != ".html":
        print(f"ERROR: expected an existing .html file: {path}", file=sys.stderr)
        return 2
    try:
        text = path.read_text(encoding="utf-8")
    except (OSError, UnicodeError) as exc:
        print(f"ERROR: cannot read UTF-8 HTML: {exc}", file=sys.stderr)
        return 2
    parser, errors, warnings = validate(text)

    print(f"Deck: {path}")
    print(f"Slides: {parser.slide_count}")
    for message in warnings:
        print(f"WARNING: {message}")
    for message in errors:
        print(f"ERROR: {message}")

    if errors or (args.strict and warnings):
        print(f"FAILED: {len(errors)} error(s), {len(warnings)} warning(s)")
        return 1

    print(f"PASSED: structural checks ({len(warnings)} warning(s))")
    print("Manual pedagogy, visual, accessibility, and technical review is still required.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
