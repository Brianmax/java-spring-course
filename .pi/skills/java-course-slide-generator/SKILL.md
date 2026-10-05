---
name: java-course-slide-generator
description: Creates complete, self-contained HTML/CSS/vanilla-JavaScript educational slide decks for Java, Spring, databases, and related software-development topics in this course. Use when the user asks to create, generate, revise, or extend course slides, lessons, or a technical presentation.
---

# Java Course Slide Generator

Create the finished lesson, not merely an outline. The primary artifact is one browser-ready HTML file with inline CSS and JavaScript and no external runtime dependencies.

## Non-negotiable teaching sequence

Keep the requested topic in its proper place in the lesson. Do not move a later topic to the opening of the full deck just to simplify its introduction. Unless the user explicitly asks to reorder the course, put its plain-language introduction at the start of that topic's section and preserve the surrounding curriculum sequence.

For a beginner-facing abstract concept, use this progression when it fits:

1. **Plain-language idea** — explain it without unexplained technical terms.
2. **Familiar use** — show a recognizable real-world example.
3. **Technical model** — name the concept and describe its relevant parts.
4. **Mechanics and implementation** — trace what changes and show a small code example.
5. **Application** — reuse the same example or clearly explain why the context changes.

For every major concept, teach in this order:

1. **Problem** — the concrete difficulty developers face.
2. **Purpose** — why the concept exists and what it improves.
3. **Mental model** — a simple visual explanation.
4. **Mechanics** — how the parts interact.
5. **Implementation** — focused, incremental Java or relevant technical syntax.
6. **Application** — a realistic scenario and student practice.

Never lead with syntax. Move from conceptual understanding to motivation, mechanics, implementation, and practical application. Explicitly distinguish Java language features from framework features.

### Language and continuity

- Match the lesson's established language. In this course, teach and label slides in clear Spanish.
- Define an idea in ordinary words before introducing its technical name. Keep jargon off the first explanatory slide; introduce technical terms on the next step and define them in place.
- Carry one concrete example through the conceptual and technical explanations. Preserve the same names, values, units, and event. For example, if an unread-message count goes from 3 to 4, make the Java variable, arithmetic, and printed result follow that same change.
- Place a topic's beginner explanation beside that topic's section. Do not front-load variables, loops, or another later unit before the lesson has reached it.
- Use concrete names for code artifacts: say “programa Java que imprime un mensaje en consola” when that is what the example does. Distinguish a small program's instructions from a complete software product when the word “programa” could be ambiguous.
- Explain the rule before judging the result. If rules are missing, say that the program may produce an incorrect result; avoid vague claims such as “no hay un único resultado esperado” unless multiple outcomes are the actual learning point.
- Avoid unexplained abstractions such as “estructura” or “responsabilidad visible.” Replace them with the actual thing students can observe or change.

### Technical explanations that must stay precise

- For Java, distinguish compilation from execution: `javac` compiles source into bytecode before the program runs; at runtime, the JVM interprets bytecode and may JIT-compile frequently executed code. Do not describe Java as compiling source and interpreting it simultaneously.
- When comparing C, describe the native executable as built for a selected operating-system/architecture target. Do not imply that one generated executable automatically works on Windows, macOS, and Linux.
- Keep simplifications explicit and proportional to the learner's current knowledge. Do not introduce implementation details before the concept that makes them understandable.

## Workflow

### 1. Interpret the request

Identify the topic, likely learner level, scope, and requested output path from the user's prompt and repository context. Inspect the repository for:

- README/course structure and Java version indicators
- existing lesson HTML files
- an established design system or conventions
- prerequisites already taught

Do not ask routine questions. Ask one concise clarification only when the answer materially changes the lesson: learner depth is genuinely ambiguous, two substantially different teaching approaches are equally suitable, or no reliable visualization can be selected. Otherwise, make a pedagogically sound choice.

Default assumptions when unspecified:

- audience: beginners with only the prerequisites required by the topic
- duration: enough slides to teach the concept well; never pad to a fixed count
- filename: `lessons/<topic-slug>.html`
- language level: use the Java version configured by the project; if absent, use conservative modern Java syntax and avoid version-sensitive features

Never overwrite an existing lesson without first reading it and preserving intentional project conventions. If a filename collision appears accidental, choose a clear alternative or ask.

### 2. Load the course standards

Read these files before planning or generating a deck:

- [Design system](references/design-system.md)
- [Deck contract](references/deck-contract.md)
- [Quality checklist](references/quality-checklist.md)

These references are requirements, not suggestions. Existing project lessons take precedence only when they clearly establish a newer shared convention. If changing the shared visual language is explicitly requested, update the design-system reference so future lessons remain consistent.

### 3. Plan before coding

Create an internal lesson map tailored to the topic. Include:

- prerequisite boundary and learning objectives
- motivating real-world problem and the cost of doing without the concept
- a concise mental model
- logically ordered subtopics
- a progressive realistic example
- the best visual form for each process, relationship, lifecycle, or state change
- common mistakes or misconceptions
- summary and practical exercise

Choose representations by meaning, not by template. Examples include object/reference diagrams, class hierarchies, runtime-dispatch paths, collection state changes, exception control flow, concurrency timelines, HTTP exchanges, table/result transformations, dependency graphs, and security flows.

A simple topic may need roughly 8–12 slides; a broad topic may need 25–40. Use exactly the number needed for coherent pacing. One slide should normally make one teaching point.

### 4. Build the complete artifact

Write a single `.html` file containing all markup, styles, scripts, diagrams, notes, and controls. Use only:

- semantic HTML
- inline `<style>` CSS
- inline `<script>` vanilla JavaScript
- inline SVG and native browser APIs where useful

Do not use libraries, frameworks, CDNs, packages, external fonts, external images, external CSS/JS, build tools, or network requests. The file must work through `file://` in a modern browser.

Required presentation behavior:

- previous/next controls
- `←`/`↑` previous; `→`/`↓`/`Space` next
- `Home` first; `End` last; `F` fullscreen
- current/total indicator and progress bar
- smooth but restrained transitions
- responsive 16:9-style stage that remains usable on smaller screens
- visible focus states and accessible control labels
- reduced-motion support
- URL hash slide persistence (recommended)
- presenter notes toggle with `N` when notes are included

Do not hijack keys while focus is in an interactive control. Keep navigation logic small and readable.

### 5. Make explanations visual

Prefer diagrams and progressive states to paragraphs. Keep on-slide prose concise. Use CSS transitions, keyframes, JavaScript-controlled classes, SVG, or DOM updates only when motion reveals sequence, causality, state, or runtime behavior.

For step-based visuals:

- provide explicit Replay/Next step controls when useful
- make the final state understandable without animation
- pause/reset hidden-slide animations to avoid confusing state
- respect `prefers-reduced-motion`

Do not use decorative motion that competes with instruction.

When illustrating a state change, show the values inside the diagram itself (for example, a visible unread-message badge changing from 3 to 4), and label the event that caused the change (`+1 mensaje`). Captions may reinforce the diagram but must not carry essential state that the visual omits. A simple before → event → after diagram is sufficient; animate the change only when animation makes the cause and result clearer.

Use the available stage efficiently. Keep boxes only as large as their content requires, avoid stretched empty code panels and unused blank bands, and make the main flow fit on one screen at a typical laptop size. Use the full presentation stage rather than adding a second decorative frame or wide page gutters. On narrow screens, stack the same flow in reading order and allow scrolling only where the viewport requires it.

### 6. Write focused, accurate examples

Use valid syntax compatible with the project's Java version. Prefer realistic domains such as checkout, payments, orders, inventory, accounts, notifications, or booking over `Animal`/`Dog` examples when a realistic model clarifies the problem.

Introduce code incrementally. Each code block should support one teaching point and avoid unrelated boilerplate. Use escaped HTML and static semantic `<span>` classes for code highlighting; do not implement a fragile parser or import a highlighter.

When educational simplification could become misleading, label it. Avoid deprecated APIs unless teaching them explicitly. Do not introduce advanced concepts before their prerequisites.

### 7. End with retrieval and practice

Conclude with:

- a visual summary tied back to the original problem
- one or more realistic exercises containing objective, starting point, expected outcome, constraints, and optional challenge
- no complete exercise solution unless requested

Speaker notes may add teaching prompts, caveats, and discussion questions, but must not contain essential information absent from the visible slide.

### 8. Validate before reporting completion

Run the bundled structural validator:

```bash
python3 .pi/skills/java-course-slide-generator/scripts/validate_deck.py lessons/<topic-slug>.html
```

If invoking the skill from an ancestor project configuration, resolve the validator relative to this `SKILL.md` rather than assuming the example path.

Then manually apply every item in [the quality checklist](references/quality-checklist.md). Inspect rendered slides when a browser is available; if visual inspection is unavailable, state that limit rather than claiming it passed. Also verify:

- the browser console has no obvious errors
- navigation cannot move outside slide bounds
- every visual is legible and pedagogically relevant
- no content is visibly overcrowded at a typical laptop viewport
- examples are technically accurate
- the generated artifact is exactly one self-contained HTML file

Fix failures before finalizing. In the final response, state the created file path, slide count, key controls, and validation result. Do not paste the entire HTML into chat when it has been written to the repository.
