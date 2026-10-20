---
name: java-course-slide-generator
description: Creates, extends, revises, and reviews teaching content and behavior in this course's standalone HTML slide decks for Java, Spring, databases, and software development, including Spanish requests for diapositivas. Use for new lessons, explanations, examples, lesson sequence, or deck controls. For visual-only layout, style, or readability work on existing decks, use java-course-slide-design instead. Not for application-code changes, site catalog edits, or chat-only explanations.
---

# Java Course Slide Generator

For creation requests, deliver the finished lesson, not merely an outline. The artifact is one offline HTML file with inline CSS and JavaScript. For review-only requests, report findings without editing. Preserve existing course conventions and unrelated working-tree changes.

For visual-only design changes or reviews of an existing deck, use `java-course-slide-design` instead. Continue to apply the shared design system when creating or changing teaching content; the design skill does not replace visual quality work in a complete lesson.

## Choose the smallest complete workflow

| Request | Plan and execution | Verification scope |
| --- | --- | --- |
| New deck | Map prerequisites, objectives, motivating example, slide sequence, visuals, summary, exercise; implement one file | Complete deck, navigation, all target viewports |
| Extend a section | Read adjacent slides; plan the addition in place, retaining names, values, and curriculum order | Changed section, transitions, full-deck static/navigation regression |
| Redesign a slide | Specify its position, one objective, exact message, composition, reading order, labels, and acceptance checks | Changed slide and affected layouts; full-deck static check |
| Minor wording fix | Read context and edit directly; no delegation or full lesson map | Changed wording, local rendering if wrapping changes, static check |
| Review only | Read the relevant content; report severity, slide, evidence, correction | Separate source findings from rendered observations; no edits |

Default to beginners, clear Spanish, `lessons/<topic-slug>.html`, and the project's configured Java version. If none is configured, avoid version-sensitive features. Do not pad to a slide count. Ask one concise clarification only when ambiguity materially changes the lesson. Read an existing target before changing it.

## Load references at the step that needs them

Paths below are relative to this skill directory. Load each applicable reference completely; do not follow a chain of partially read references.

- Creating or changing content/layout: read the [design skill's design system](../java-course-slide-design/references/design-system.md) in full. It owns the palette, typography, stage, density, and diagram rules used by every course deck.
- Creating a deck or changing controls/runtime: [deck contract](references/deck-contract.md). Existing runtime need not be rebuilt for a wording edit.
- Planning or delegating substantial work: [worked examples](references/examples.md) and [host adapters](references/host-adapters.md).
- New deck: optionally copy [starter HTML](assets/deck-starter.html), then replace sample content. It is a runtime foundation, not a lesson or required layout template. Never replace an existing deck's runtime just to adopt it.
- Before visual verification: [browser checks](references/browser-checks.md).
- Before accepting work: [quality checklist](references/quality-checklist.md); apply relevant items and mark others not applicable with a reason.
- Improving this skill itself: [evaluation procedure](evals/README.md), [behavioral cases](evals/evals.json), and [trigger cases](evals/trigger-queries.json). These are not normal slide-production steps.

## Non-negotiable teaching sequence

Keep the requested topic in its proper place in the lesson. Do not move a later topic to the opening of the full deck just to simplify its introduction. Unless the user explicitly asks to reorder the course, put its plain-language introduction at the start of that topic's section and preserve the surrounding curriculum sequence.

### Earn the technical concept in the opening

Apply this sequence to the opening of the deck and to the first slides of every new major topic. Do not assume learners know the everyday meaning of a domain word used in the example. Before asking what should change in a *pedido*, for example, say that it is the record of what someone wants to buy and show a concrete purchase. A topic title, benefits statement, abstract diagram, or course roadmap cannot substitute for introducing the situation.

Build the learner's understanding in this order:

1. **Make the situation concrete.** Show who wants or does what, define any necessary domain item in ordinary language, and show the relevant data or action. Keep jargon and unexplained code off this first explanatory step.
2. **Show the starting point working.** Demonstrate what learners can already do with the known tools, using the same example and a correct, visible result. When comparing procedural and object-oriented code, show that variables and a method can calculate the purchase correctly; do not suggest that procedural code is inherently wrong or that an object is required for a valid calculation.
3. **Earn the motivation.** Introduce a specific, plausible pressure: another purchase, a changed requirement, a repeated rule, or a new collaborator. Trace the concrete values, event, and resulting difficulty. For example, show a quantity from purchase A combined with a price from purchase B. Explain that this is a possible mistake when callers gather separate values, not an inevitable result of the procedural approach. State the trade-off accurately; a new abstraction does not guarantee validation or correctness unless its rules are implemented.
4. **Introduce the idea that addresses that pressure.** Define the concept in everyday words, show its parts and how they interact, then give its technical name. Use a labeled visual relationship or state change where that makes the model easier to see.
5. **Compare and implement with continuity.** Contrast the familiar approach and the new approach with the same people, objects, names, units, event, and expected result. Add syntax only after the learner understands what each version accomplishes.
6. **Give the route afterward.** Place prerequisites, level labels, and the unit roadmap after the concrete situation and its motivation. Learners should know what problem the course addresses before seeing its route.

A title that states only a solution or benefit is not enough. Avoid vague friction such as “the data is separate” unless the slide identifies which data, where it is held, who needs to combine it, and what that costs. Do not create an artificial failure just to make the requested concept appear necessary. Explicitly state when two approaches both work and describe the real trade-off the new concept offers.

Make transitions between adjacent lessons explicit when the example or subtopic changes. Name what the previous slide established, how it relates to the new example, and why the next concept follows; do not rely on adjacency, a unit label, or an unexplained new domain noun to carry the connection.

When the topic involves repetition or a process that returns to an earlier step, draw the return path explicitly and show where the process stops. A linear sequence, a “sí/no” label, or prose that says “volver” does not make the cycle visible by itself. Put branch labels beside their connector and make the Yes/continue and No/exit routes spatially distinct. Match the connector order to the construct being taught: a `while` checks its condition before the body; a counted `for` initializes, checks, runs the body, updates, then checks again. Do not make one generic diagram imply that every loop has the same order. Keep the ordinary-language example first; define terms such as *bucle*, *vuelta*, and *condición* on the following technical step, then progress to code examples.

After the concrete opening, develop the mental model and mechanics before implementation, then give realistic practice. Never lead with syntax. Explicitly distinguish Java language features from framework features.

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

## Plan, implement, review

1. Read repository context and the target's neighboring slides. Select the route above and load the applicable references.
2. Produce its concrete plan before coding. Choose visuals by meaning (relationships, runtime state, flow, timeline), not by a repeated card template. Include misconceptions and acceptance checks. One slide normally makes one teaching point.
3. For nontrivial implementation, use a `slide-implementer` when supported and permitted by the host. Supply the plan, target file, scope, references, and checks. Keep one writer per HTML file. If agents are unavailable, perform the same stages sequentially and report that fallback; do not invent tool names or change model settings.
4. Review the diff against the plan. For substantial work or uncertain clarity, request a read-only `slide-reviewer` if available. Findings must include slide, evidence, severity, and correction. The primary agent owns integration and final acceptance.
5. Validate, fix, and repeat the affected checks. Do not enlarge scope or redesign unrelated slides in response to incidental findings; report unrelated issues separately.

## Build the complete artifact

Write a single `.html` file containing all markup, styles, scripts, diagrams, notes, and controls. Use only:

- semantic HTML
- inline `<style>` CSS
- inline `<script>` vanilla JavaScript
- inline SVG and native browser APIs where useful

Do not use libraries, frameworks, CDNs, packages, external fonts, external images, external CSS/JS, build tools, or network requests. The file must work through `file://` in a modern browser.

Implement controls, accessibility, responsive behavior, and offline restrictions exactly as specified in the deck contract. The contract is strict about behavior; slide compositions remain adaptable to the teaching point.

## Make explanations visual

Prefer diagrams and progressive states to paragraphs. Keep on-slide prose concise. Use CSS transitions, keyframes, JavaScript-controlled classes, SVG, or DOM updates only when motion reveals sequence, causality, state, or runtime behavior.

For step-based visuals:

- provide explicit Replay/Next step controls when useful
- make the final state understandable without animation
- pause/reset hidden-slide animations to avoid confusing state
- respect `prefers-reduced-motion`

Do not use decorative motion that competes with instruction.

When illustrating a state change, show the values inside the diagram itself (for example, a visible unread-message badge changing from 3 to 4), and label the event that caused the change (`+1 mensaje`). Captions may reinforce the diagram but must not carry essential state that the visual omits. A simple before → event → after diagram is sufficient; animate the change only when animation makes the cause and result clearer.

Apply the design system's connector geometry, accessible SVG, stage, and roomy/balanced/dense sizing rules. Simplify or split crowded content before shrinking text; narrow layouts preserve the same flow and may scroll. Verify labels above controls at both required laptop sizes.

Gotcha: adjacent inline tags do not insert spaces. Separate labels, values, and units with a literal space or intentional layout gap, then inspect the result.

## Write focused, accurate examples

Use valid syntax compatible with the project's Java version. Prefer realistic domains such as checkout, payments, orders, inventory, accounts, notifications, or booking over `Animal`/`Dog` examples when a realistic model clarifies the problem.

When a Java lesson teaches classes or methods, do not assume that naming the concepts or showing an object diagram is enough. Show a valid class declaration and identify its class name, braces/body, fields (state), and methods (behavior); explain that each instance has its own instance-field values. When introducing a method, define it in ordinary language and walk through one complete example that identifies its return type (including `void` when useful), name, parameters, body, `return` when present, and a call with concrete argument values and result. Distinguish a method fragment from a complete Java program, and keep class, method, and call examples consistent with the same running domain example. Place constructors, access modifiers, `static`, and other details at the point their prerequisites are taught rather than crowding this first explanation.

Introduce code incrementally. Each code block should support one teaching point and avoid unrelated boilerplate. Use escaped HTML and static semantic `<span>` classes for code highlighting; do not implement a fragile parser or import a highlighter.

When educational simplification could become misleading, label it. Avoid deprecated APIs unless teaching them explicitly. Do not introduce advanced concepts before their prerequisites.

## End with retrieval and practice

Conclude with:

- a visual summary tied back to the original problem
- one or more realistic exercises containing objective, starting point, expected outcome, constraints, and optional challenge
- no complete exercise solution unless requested

Speaker notes may add teaching prompts, caveats, and discussion questions, but must not contain essential information absent from the visible slide.

## Validate before reporting completion

Run the bundled static validator from the repository root (or resolve its path relative to this skill):

```bash
python3 .pi/skills/java-course-slide-generator/scripts/validate_deck.py lessons/<topic-slug>.html
```

Separate three evidence levels:

1. **Static:** inspect validator errors and warnings; fix errors and investigate warnings. A regex match is not proof that a control works.
2. **Browser:** execute the browser checklist for the route's scope. Capture viewport and slide evidence; check console errors and offline operation. If unavailable, state NOT VERIFIED.
3. **Teaching/visual:** apply the quality checklist to the actual content and rendered views. Source-only judgments must be labeled as such.

Never claim a complete pass when required checks are unavailable. Report unresolved limitations rather than looping indefinitely on unavailable tools. Finish with file path, slide count for deck changes, relevant controls, checks actually run, and remaining gaps. Do not paste the entire HTML into chat.
