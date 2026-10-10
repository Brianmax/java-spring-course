# Java Course Slide Design System

Use this shared visual language for every generated lesson unless the repository contains a deliberately updated version of it. Consistency matters more than novelty.

## Visual identity

The course style is **Tecsup-inspired technical clarity**: a bright, confident white canvas, Tecsup cyan accents, charcoal typography, spacious layouts, crisp geometric diagrams, and restrained motion. The visual language should feel modern, practical, and technology-oriented without copying site assets or relying on remote brand resources.

### Color tokens

These tokens are sampled from the current Tecsup website palette. Define them on `:root` and reuse them rather than introducing per-lesson palettes:

```css
:root {
  --bg: #ffffff;
  --surface: #f5f5f5;
  --surface-2: #dcf3ff;
  --text: #111111;
  --muted: #555555;
  --tecsup-blue: #0097d0;
  --tecsup-cyan: #00b3e0;
  --tecsup-pale: #dcf3ff;
  --success: #16835b;
  --warning: #9a6700;
  --danger: #c62828;
  --line: #dddddd;
  --ink-panel: #111111;
  --shadow: 0 14px 36px rgb(17 17 17 / 0.12);
}
```

Use `--tecsup-blue` for primary headings, actions, and selected diagram nodes; use `--tecsup-cyan` for flow arrows, progress, links, and interaction states; use `--tecsup-pale` for highlighted regions and explanatory panels. Keep `--text` for essential text and `--muted` only for supporting text. Use semantic colors only for their meanings, and never communicate meaning by color alone. White text is appropriate on `--tecsup-blue`, `--tecsup-cyan`, or `--ink-panel` only after checking contrast.

### Typography

Tecsup's site uses N27 for headings and Open Sans for body text. Because lessons must work offline with no external fonts, approximate that pairing with robust local/system stacks:

```css
--font-sans: "Arial Nova", Arial, Helvetica, ui-sans-serif, system-ui, sans-serif;
--font-heading: "Arial Nova", Arial, Helvetica, ui-sans-serif, system-ui, sans-serif;
--font-mono: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, monospace;
```

Use bold, compact headings with sentence case. Prefer a cyan rule, eyebrow, or geometric mark for Tecsup identity rather than decorative gradients or a copied logo.

Recommended hierarchy (responsive with `clamp`):

- deck/section title: 3–4.5rem, weight 750–800
- slide title: 2–3rem, weight 700–800
- body: 1.1–1.45rem, line height 1.45–1.6
- label/eyebrow: 0.75–0.9rem, uppercase, letter spacing 0.12em
- code: 0.9–1.15rem, line height 1.5

Keep line lengths short. Avoid shrinking text to fit; split an overcrowded slide.

## Stage and spacing

- Use a 16:9 presentation stage (`aspect-ratio: 16 / 9`) sized to the available viewport. When the deck is designed for full-screen use, let the stage fill the viewport; do not place it inside a second card or leave wide unused page gutters.
- Slides occupy the same stage and use a consistent safe area, approximately `clamp(1.5rem, 4vw, 4rem)`.
- Use an 8px spacing rhythm: 8, 16, 24, 32, 48, 64.
- Keep slide titles and their eyebrows at the upper left of the safe area, including title and section-divider slides.
- Group the explanation, diagrams, examples, and callouts below the title and vertically center that group in the remaining stage space. Keep panels at their natural height. When the group is taller than the available space, let it start below the title and scroll in reading order without clipping.
- Set body and diagram text for the actual slide's reading needs. When a short slide leaves room and its supporting text looks small, enlarge that text while keeping the title scale consistent. When content is crowded, simplify or rearrange it and split the teaching point across slides if needed; reduce font size only when the text remains comfortably readable.
- Keep navigation/progress chrome quiet and outside the core content when possible.
- Use rounded corners of 12–18px and subtle borders; avoid excessive cards.
- Size panels to their content. Do not stretch short code examples into tall empty blocks; align code and explanation panels to their natural height and use the remaining stage space for the diagram or example that advances the lesson.
- At a typical laptop viewport, keep each slide's main explanation and visual on screen without unnecessary scrolling. Stack diagrams in reading order on narrow screens.

Every slide should use a meaningful composition selected from: centered hero, split concept/visual, comparison, process flow, diagram focus, code plus runtime state, or exercise brief. Do not repeat the same card grid on every slide.

## Components

### Code blocks

Use the Tecsup charcoal `--ink-panel` surface, one-pixel cyan or charcoal border, 12–16px radius, mono font, and optional filename/language header. Highlight only tokens or lines relevant to the explanation. Keep code text near-white for contrast.

Use stable token classes:

- `.tok-keyword` — Tecsup blue or cyan
- `.tok-type` — bright cyan
- `.tok-string` — green
- `.tok-number` — amber
- `.tok-comment` — muted
- `.code-focus` — subtle Tecsup-cyan line background/border
- `.code-dim` — reduced opacity

Always HTML-escape `<`, `>`, and `&` inside code.

### Diagrams

- Nodes: white or `--surface-2` fill, `--line` border, 12px radius.
- Primary/current node: `--tecsup-blue` border or restrained cyan emphasis.
- Flow arrows: `--tecsup-cyan`, with clear arrowheads and labels where ambiguity exists.
- Use solid lines for direct relationships and dashed lines for optional/indirect relationships.
- Use CSS/HTML for simple layouts and inline SVG for connectors or spatial diagrams.
- Include concise text labels; icon-only diagrams are not acceptable.
- For before/after diagrams, put the actual values in the visual and label the event between them. Do not make students infer the value change from a caption alone.
- Show arrows in a clear reading direction. Label an arrow when it means a concrete event or operation, such as `+1 mensaje`.

### Callouts

Use a colored left border plus a text label/icon glyph:

- info: `--tecsup-cyan`
- key idea/success: `--success`
- warning/common mistake: `--warning`
- error/anti-pattern: `--danger`

Do not rely on emoji as the sole visual language. Native Unicode symbols are acceptable when accompanied by text.

### Comparisons

Use a clear “before/problem” versus “after/solution” composition. Explain the trade-off; do not imply every abstraction is always beneficial.

### Exercises

Use a distinct final-section treatment with scenario, objective, starting point, constraints, expected outcome, and optional challenge visibly separated.

## Motion language

Motion should explain. Standard timing:

- UI/hover feedback: 120–180ms
- slide transition: 280–420ms
- diagram step: 350–650ms
- stagger between related items: 80–140ms

Use ease-out for entrances and ease-in-out for state changes. Animate opacity and transforms when possible. Avoid looping animation except a subtle active-flow indicator; stop it when the slide is hidden. Under `prefers-reduced-motion: reduce`, remove nonessential animation and reveal final states immediately.

## Recurring course identity

Include a small `TECSUP · JAVA COURSE` eyebrow on title/section slides and a subtle lesson/topic label in deck chrome. Do not use an external logo or remote brand asset. A small abstract cyan geometric mark made with CSS or inline SVG is allowed.

## Responsive and accessible behavior

- Preserve reading order in the DOM.
- Collapse multi-column compositions to one column on narrow screens.
- Ensure controls have at least 44px targets where practical.
- Use buttons for actions, not clickable generic elements.
- Add `aria-label`/`aria-pressed` where state or visible text is insufficient.
- Use visible `:focus-visible` outlines.
- Do not hide focus indicators.
- Avoid tiny fixed pixel typography and content clipped without a way to scroll.

## Design anti-patterns

Do not use:

- a unique palette or typography system per lesson
- dense walls of bullets
- more than one large code listing on a slide
- unlabeled arrows or decorative architecture lines
- gratuitous gradients, glass effects, or constant animation
- external images merely to fill space
- text embedded as raster imagery
- low-contrast muted text for essential explanations
