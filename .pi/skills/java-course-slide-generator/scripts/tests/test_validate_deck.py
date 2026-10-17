"""Small generated fixtures exercise both acceptance and rejection paths."""

import importlib.util
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "validate_deck.py"
SPEC = importlib.util.spec_from_file_location("validate_deck", SCRIPT)
VALIDATOR = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(VALIDATOR)

VALID = """<!doctype html>
<html lang="es"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Curso de prueba</title><style>
button:focus-visible { outline: 2px solid black; }
@media (prefers-reduced-motion: reduce) { * { animation: none; } }
</style></head><body>
<section class="slide" id="first"><h1>Compra</h1></section>
<section class="slide" id="second" hidden><h2>Total</h2></section>
<a href="../">Inicio</a><a href="#first">Primera</a>
<button type="button" aria-controls="first">Anterior</button>
<span id="slide-count">1 / 2</span><progress value="1" max="2"></progress>
<script>
document.addEventListener('keydown', event => {
  const keys = ['Home', 'End', 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown'];
});
function fullscreen() { document.documentElement.requestFullscreen(); }
</script></body></html>"""


class DeckValidationTests(unittest.TestCase):
    def findings(self, text):
        _, errors, warnings = VALIDATOR.validate(text)
        return errors, warnings

    def test_valid_fixture(self):
        self.assertEqual(self.findings(VALID), ([], []))

    def test_metadata_is_required(self):
        for fragment in (' lang="es"', '<meta charset="utf-8">',
                         '<meta name="viewport" content="width=device-width, initial-scale=1">'):
            with self.subTest(fragment=fragment):
                self.assertTrue(self.findings(VALID.replace(fragment, ""))[0])

    def test_each_missing_key_is_reported(self):
        for key in ("Home", "End", "ArrowLeft", "ArrowRight", "ArrowUp", "ArrowDown"):
            with self.subTest(key=key):
                errors, warnings = self.findings(VALID.replace(key, ""))
                self.assertEqual(errors, [])
                self.assertTrue(any(f"key {key};" in item for item in warnings))

    def test_displayed_api_examples_are_not_executed_code(self):
        example = '<pre><code>fetch("https://example.org"); url(remote.png)</code></pre>'
        self.assertEqual(self.findings(VALID.replace("</body>", example + "</body>")), ([], []))

    def test_executable_network_apis_are_rejected(self):
        for code in ('fetch("/data")', 'new XMLHttpRequest()', 'new WebSocket("wss://x")',
                     'new EventSource("/events")', 'import("./module.js")'):
            with self.subTest(code=code):
                self.assertTrue(self.findings(VALID.replace("<script>", "<script>" + code + ";"))[0])

    def test_inline_event_network_api_is_rejected(self):
        self.assertTrue(self.findings(VALID.replace('type="button"', 'type="button" onclick="fetch(\'/x\')"'))[0])

    def test_external_resources_are_rejected(self):
        for resource in ('<script src="x.js"></script>', '<img src="x.png" alt="X">',
                         '<link rel="stylesheet" href="x.css">', '<iframe srcdoc="test"></iframe>',
                         '<img srcset="x.png 1x" alt="X">', '<svg><use xlink:href="x.svg#x"/></svg>'):
            with self.subTest(resource=resource):
                self.assertTrue(self.findings(VALID.replace("</body>", resource + "</body>"))[0])

    def test_css_import_and_urls_are_rejected(self):
        for css in ('@import "x.css";', 'body { background: url(x.png); }'):
            with self.subTest(css=css):
                self.assertTrue(self.findings(VALID.replace("<style>", "<style>" + css))[0])

    def test_inline_css_url_is_rejected(self):
        self.assertTrue(self.findings(VALID.replace('<h1>', '<h1 style="background:url(x.png)">'))[0])

    def test_inline_svg_and_data_uri_are_allowed(self):
        graphic = '<svg><title>Diagrama</title><use href="#shape"/><path id="shape"/></svg><img src="data:image/png;base64,AA==" alt="X">'
        self.assertEqual(self.findings(VALID.replace("</body>", graphic + "</body>")), ([], []))

    def test_svg_title_does_not_replace_document_title(self):
        text = VALID.replace('<title>Curso de prueba</title>', '')
        text = text.replace('</body>', '<svg><title>Diagrama</title></svg></body>')
        self.assertIn('missing non-empty <title>', self.findings(text)[0])

    def test_duplicate_ids_are_rejected(self):
        self.assertTrue(any('duplicate id' in item for item in self.findings(VALID.replace('id="second"', 'id="first"'))[0]))

    def test_missing_accessibility_target_is_rejected(self):
        self.assertTrue(any('missing id' in item for item in self.findings(VALID.replace('aria-controls="first"', 'aria-controls="missing"'))[0]))

    def test_static_module_import_is_rejected(self):
        self.assertTrue(self.findings(VALID.replace('<script>', '<script>import x from "./x.js";'))[0])

    def test_untyped_button_warns(self):
        self.assertTrue(any('button(s)' in item for item in self.findings(VALID.replace('type="button"', ''))[1]))

    def test_cli_exit_codes(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / 'deck.html'
            for text, strict, code in ((VALID, False, 0), (VALID.replace('ArrowUp', ''), True, 1), ('bad', False, 1)):
                path.write_text(text, encoding='utf-8')
                result = subprocess.run([sys.executable, '-B', str(SCRIPT), str(path)] + (['--strict'] if strict else []), capture_output=True)
                self.assertEqual(result.returncode, code, result.stdout)
            result = subprocess.run([sys.executable, '-B', str(SCRIPT), str(Path(directory) / 'missing.html')], capture_output=True)
            self.assertEqual(result.returncode, 2)


if __name__ == '__main__':
    unittest.main()
