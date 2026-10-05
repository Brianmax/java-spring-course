#!/usr/bin/env python3
"""Structural checks for a Java Course self-contained HTML slide deck."""

from __future__ import annotations

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

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        values = {name.lower(): (value or "") for name, value in attrs}
        tag = tag.lower()

        if tag == "style":
            self.styles += 1
        elif tag == "script":
            self.scripts += 1
            if "src" in values:
                self.external_issues.append("script element has a src attribute")
        elif tag == "title":
            self._in_title = True

        classes = set(values.get("class", "").split())
        if tag == "section" and "slide" in classes:
            self.slide_count += 1

        if tag == "link" and values.get("rel", "").lower() == "stylesheet":
            self.external_issues.append("external stylesheet link found")

        for attr in ("src", "href", "poster", "data"):
            if attr not in values:
                continue
            value = values[attr].strip()
            if not value or value.startswith("#") or value.startswith("data:"):
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
        if tag.lower() == "title":
            self._in_title = False

    def handle_data(self, data: str) -> None:
        if self._in_title:
            self.title_text.append(data.strip())


def fail(message: str, errors: list[str]) -> None:
    errors.append(message)


def main() -> int:
    if len(sys.argv) != 2:
        print("Usage: validate_deck.py <lesson.html>", file=sys.stderr)
        return 2

    path = Path(sys.argv[1])
    if not path.is_file():
        print(f"ERROR: file not found: {path}", file=sys.stderr)
        return 2
    if path.suffix.lower() != ".html":
        print("ERROR: lesson artifact must have an .html extension", file=sys.stderr)
        return 2

    text = path.read_text(encoding="utf-8")
    parser = DeckParser()
    errors: list[str] = []
    warnings: list[str] = []

    try:
        parser.feed(text)
    except Exception as exc:  # HTMLParser is lenient, but report unexpected failures.
        fail(f"could not parse HTML: {exc}", errors)

    if not re.match(r"\s*<!doctype\s+html", text, re.IGNORECASE):
        fail("missing <!doctype html>", errors)
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

    css_urls = re.findall(r"url\(\s*(['\"]?)(.*?)\1\s*\)", text, re.IGNORECASE)
    for _, value in css_urls:
        value = value.strip()
        if value and not value.startswith(("data:", "#")):
            fail(f"CSS url() references non-inline resource {value!r}", errors)

    for label, pattern in NETWORK_PATTERNS.items():
        if re.search(pattern, text):
            fail(f"network/module API found: {label}", errors)

    required_behavior_hints = {
        "keyboard handler": r"(?:keydown|keyup)",
        "fullscreen support": r"requestFullscreen",
        "progress UI": r"progress",
        "slide indicator": r"(?:slide-count|slide-indicator|current-slide|aria-valuenow)",
        "reduced-motion support": r"prefers-reduced-motion",
    }
    for label, pattern in required_behavior_hints.items():
        if not re.search(pattern, text, re.IGNORECASE):
            warnings.append(f"could not detect {label}; verify manually")

    if not re.search(r":focus-visible", text):
        warnings.append("could not detect :focus-visible styling")
    if not re.search(r"\b(Home|End|ArrowLeft|ArrowRight)\b", text):
        warnings.append("could not detect all expected navigation key names")

    print(f"Deck: {path}")
    print(f"Slides: {parser.slide_count}")
    for message in warnings:
        print(f"WARNING: {message}")
    for message in errors:
        print(f"ERROR: {message}")

    if errors:
        print(f"FAILED: {len(errors)} error(s), {len(warnings)} warning(s)")
        return 1

    print(f"PASSED: structural checks ({len(warnings)} warning(s))")
    print("Manual pedagogy, visual, accessibility, and technical review is still required.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
