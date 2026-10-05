# Self-Contained Deck Contract

Each generated lesson is one standalone `.html` file that opens directly in a modern browser. It contains its own HTML, `<style>`, and `<script>`.

## Required document structure

Use semantic, maintainable markup similar to:

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Topic — Java Course</title>
  <style>/* complete design system and lesson styles */</style>
</head>
<body>
  <main class="deck" aria-live="polite">
    <section class="slide is-active" data-title="...">...</section>
    <section class="slide" data-title="..." hidden>...</section>
  </main>
  <nav class="deck-controls" aria-label="Presentation controls">...</nav>
  <div class="progress" aria-hidden="true">...</div>
  <script>/* complete presentation and lesson behavior */</script>
</body>
</html>
```

Equivalent accessible structures are acceptable. Include a short keyboard-help overlay or discoverable help button.

## Navigation behavior

Maintain one integer slide index and one render function as the source of truth. Rendering should:

1. clamp the index to valid bounds
2. mark only the current slide active and expose only it to assistive technology
3. update current/total indicators and progress
4. update the URL hash without causing a page jump
5. reset or activate the current slide's educational sequence
6. disable previous/next buttons at boundaries

Required keys:

| Key | Behavior |
|---|---|
| Left / Up | Previous slide |
| Right / Down / Space | Next slide |
| Home | First slide |
| End | Last slide |
| F | Enter/exit fullscreen |
| N | Toggle speaker notes, if present |
| Escape | Close overlays before allowing normal fullscreen behavior |

Ignore presentation shortcuts when the event target is an input, textarea, select, button activated by Space, or contenteditable region. Buttons must remain operable by keyboard.

Touch swipe navigation is optional. If included, require a clear horizontal gesture threshold so vertical page movement is not accidentally captured.

## Slide and transition model

Slides may be layered in one stage or shown/hidden. Hidden slides must not expose focusable controls or continue meaningful animations. Use opacity/transform transitions without making all slide contents simultaneously visible to screen readers.

Progress should represent the current position including the final slide (for example `(index + 1) / count`). Deep-link hashes such as `#slide-4` are recommended. Invalid hashes must safely fall back to the first slide.

## Educational interactions

Interactions must have an instructional purpose and a non-animated fallback. Good patterns:

- reveal the next state of an object diagram
- step through request/response flow
- switch between “without” and “with” views
- run/reset a small deterministic data-structure visualization
- highlight the executing code line alongside state

Keep interactions deterministic and resettable. Never require a network connection, randomness, drag precision, audio, or hover to understand essential content.

## Speaker notes

When useful, place notes inside their slide:

```html
<aside class="speaker-notes" hidden>
  Teaching prompt or caveat for the instructor.
</aside>
```

A notes mode may display them in a panel and toggle via `N`. Notes should add delivery guidance rather than compensate for an incomplete visible explanation.

## Code highlighting

Use pre-authored semantic spans and CSS. Example:

```html
<pre><code><span class="tok-keyword">class</span> <span class="tok-type">Order</span> {
    <span class="tok-keyword">private</span> Status status;
}</code></pre>
```

Do not fetch or recreate a general-purpose syntax-highlighting library.

## External-resource prohibition

The final HTML must not contain or depend on:

- `<script src="...">`
- `<link rel="stylesheet" ...>`
- remote URLs in `src`, `href`, `url(...)`, imports, fetch, XHR, or WebSockets
- external fonts, images, videos, iframes, modules, or source maps
- data loaded from adjacent files

Inline SVG, CSS artwork, and data URIs are permitted, but simple native elements are preferable. Ordinary fragment links beginning with `#` are permitted.

## Practical browser quality

- Set explicit button types.
- Guard Fullscreen API calls and synchronize fullscreen button state.
- Avoid unsupported experimental APIs when a simple DOM/CSS solution exists.
- Do not emit console errors during normal navigation.
- Keep JavaScript names and sections readable; avoid minification.
- Keep all lesson data in the HTML so copying the file preserves the presentation.
- Add print styles only if they are simple; printing must not compromise presentation mode.

## Content density

A slide is too dense when it needs unusually small text, contains multiple independent ideas, or requires sustained scrolling at a normal laptop viewport. Split it. A diagram's labels and a code block's relevant lines must be readable from presentation distance.
