# Java Course Slide Design System

## Contents

- Visual identity: colors and typography
- Stage and spacing
- Components: code, diagrams, callouts, comparisons, exercises
- Motion language
- Recurring course identity
- Responsive and accessible behavior
- Design anti-patterns

Use this shared visual language for every generated lesson unless the repository contains a deliberately updated version of it. Consistency matters more than novelty.

## Visual identity

The course style is **technical clarity**: readable typography, spacious layouts, crisp geometric diagrams, and restrained motion. The current course site and decks use a Developer Dark palette inspired by modern IDEs and documentation. Neither the course name nor its host institution dictates its colors. If a user requests another palette or light mode, adapt the tokens while preserving contrast and consistency across the whole site.

### Color tokens

The following are current course defaults, not mandatory brand colors. Use the same semantic roles for the site catalog and standalone lessons. Define theme tokens on `:root` and reuse them rather than introducing arbitrary per-lesson palettes:

```css
:root {
  color-scheme: dark;
  --bg: #101820;
  --secondary-bg: #182330;
  --surface: #202c3c;
  --surface-2: #29384a;
  --table-head: #2d3b50;
  --text: #e6edf3;
  --muted: #a8b3c5;
  --accent: #70d9e8;
  --flow: #70d9e8;
  --highlight: #b9a0ff;
  --success: #a6e3a1;
  --warning: #ffc78a;
  --danger: #ff5555;
  --line: #39495d;
  /* Existing code blocks retain their independent background and syntax palette. */
  --ink-panel: #21222c;
  --shadow: 0 14px 36px rgb(0 0 0 / 0.35);
}
```

Use `--accent` for active states and meaningful links or relationships, `--flow` for arrows and focus, and `--highlight` sparingly for section and category labels. Keep main titles and ordinary values in `--text`, and supporting text in `--muted`. Give cards subtle borders and distinguish them by surface brightness; table headers use `--table-head`. Callouts use a dark surface and a thin semantic left border. On filled accent buttons use dark text. Aim for WCAG AA contrast: 4.5:1 for normal text and 3:1 for large text and essential UI. Never communicate meaning by color alone. Existing decks may still use legacy `--tecsup-*` variable names; their names do not require brand colors. Do not recolor existing inline code, code blocks, or syntax tokens as part of a structural theme change.

### Typography

Because lessons must work offline with no external fonts, use robust local/system stacks:

```css
--font-sans: "Arial Nova", Arial, Helvetica, ui-sans-serif, system-ui, sans-serif;
--font-heading: "Arial Nova", Arial, Helvetica, ui-sans-serif, system-ui, sans-serif;
--font-mono: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, monospace;
```

Use bold, compact headings with sentence case. A small accent rule, eyebrow, or geometric mark can reinforce course identity without fixing a brand color or copying a logo.

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
- Keep titles and their sizes fixed while adapting the content below them to the available stage. Classify density by the natural height of the body group divided by the safe vertical space remaining after the title: **roomy** below 70%, **balanced** from 70% through 85%, and **dense** above 85% or when content reaches the controls.
- For roomy slides, enlarge body text, diagram labels, panels, and row spacing together in small steps toward 75–80% of the available height. Keep the hierarchy between body text, labels, and code. Balanced slides keep the baseline sizing. For dense slides, simplify or rearrange first and split the teaching point if it still crowds the safe area; reduce font size only as a last resort and keep it comfortably readable.
- On narrow screens, stack diagrams and comparisons in reading order and allow scrolling where needed. Do not reduce type just to force the desktop composition into a small viewport.
- Keep navigation/progress chrome quiet and outside the core content when possible.
- Use rounded corners of 12–18px and subtle borders; avoid excessive cards.
- Size panels to their content. Do not stretch short code examples into tall empty blocks; align code and explanation panels to their natural height and use the remaining stage space for the diagram or example that advances the lesson.
- At a typical laptop viewport, keep each slide's main explanation and visual on screen without unnecessary scrolling. Stack diagrams in reading order on narrow screens.

Every slide should use a meaningful composition selected from: centered hero, split concept/visual, comparison, process flow, diagram focus, code plus runtime state, or exercise brief. Do not repeat the same card grid on every slide.

## Components

### Code blocks

For new decks, use the `--ink-panel` surface, a subtle one-pixel border, 12–16px radius, mono font, and optional filename/language header. Highlight only tokens or lines relevant to the explanation. Keep code text near-white for contrast. When applying a new structural theme to existing decks, preserve their code backgrounds, syntax colors, inline-code colors, and formatting.

Use stable token classes:

- `.tok-keyword` — pink or purple
- `.tok-type` — cyan
- `.tok-string` — green
- `.tok-number` — purple or orange
- `.tok-comment` — muted
- `.code-focus` — subtle accent line background/border
- `.code-dim` — reduced opacity

Always HTML-escape `<`, `>`, and `&` inside code.

### Diagrams

- Nodes: `--surface` or `--surface-2` fill, subtle `--line` border, 12px radius.
- Primary/current node: `--accent` border or restrained emphasis; avoid outlining every node brightly.
- Flow arrows: `--flow`, with clear arrowheads and labels where ambiguity exists.
- Use solid lines for direct relationships and dashed lines for optional/indirect relationships.
- Use CSS/HTML for simple layouts and inline SVG for connectors or spatial diagrams.
- Include concise text labels; icon-only diagrams are not acceptable.
- For decisions, place each branch label beside its outgoing connector and route each outcome to a distinct action or terminal node. Keep connectors visible between nodes; an inline arrow character or a row of cards is not a process diagram by itself.
- For loops, draw a return connector from the repeated work to the actual condition or start point, with an arrowhead that shows the direction. Give the exit path a separate, explicit stop node. For a `while`, show condition → body → condition; for a counted `for`, show initialization → condition → body → update → condition. Keep the routes apart so return and exit cannot be confused.
- If desktop and mobile use separate SVG layouts, preserve the same route and labels in both, and add accessible `<title>` and `<desc>` text with unique IDs. At 1366×768 and 1280×720, check that all essential nodes, labels, and definitions fit above the presentation controls.
- For before/after diagrams, put the actual values in the visual and label the event between them. Do not make students infer the value change from a caption alone.
- Make visual boundaries match what they represent. When teaching objects, one outlined group should represent one identified object; show that object's before → labeled event → after states inside the group. For two independent objects, use one group per object, show each object's own values, and explicitly mark an unaffected object's unchanged state. If two separate snapshots of the same object are necessary, repeat its identity and visibly connect the snapshots so they cannot be mistaken for two objects. Do not use two identical unconnected boxes for the before/after states of one object.
- Distinguish a variable or reference from the object it points to: label the variable at the start of its connector and the object's identity inside its boundary. Keep these labels consistent with the code, alt text, and neighboring slides, including after reassignment.
- Show arrows in a clear reading direction. Label an arrow when it means a concrete event or operation, such as `+1 mensaje`.

### Callouts

Use a colored left border plus a text label/icon glyph:

- info: `--flow`
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

Include a small course/topic eyebrow on title/section slides and a subtle lesson/topic label in deck chrome. The real institution name may appear where appropriate, but is not a color or logo requirement. Do not use an external logo or remote brand asset. A small abstract geometric mark made with CSS or inline SVG is allowed.

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
