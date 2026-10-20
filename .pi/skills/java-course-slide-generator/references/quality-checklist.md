# Lesson Quality Checklist

Apply this checklist manually after generating the deck. Do not claim an item passed unless it was actually inspected.

## Pedagogy

- [ ] Defines what the topic is in beginner-appropriate language.
- [ ] Establishes a realistic problem before syntax.
- [ ] Opens each new major topic with a concrete, plain-language example that makes the problem visible before naming the technical concept or showing code; a solution-only title or definition does not count.
- [ ] Defines the familiar thing in the example (such as what a purchase order records) before asking learners to reason about it.
- [ ] Shows what learners can already accomplish with familiar tools, including a correct result, before explaining the limits or trade-offs that motivate the new concept.
- [ ] Makes the motivation specific and traceable: show the actual values, action, and difficulty; do not imply a possible mistake is inevitable or manufacture a failure to justify the concept.
- [ ] For organizational concepts such as OOP, explains what the new organization helps keep together and acknowledges that a procedural approach can also solve the task correctly.
- [ ] Keeps one example and its names, values, units, event, and result consistent across the situation, baseline, motivating difficulty, conceptual model, and code comparison.
- [ ] Uses role- or domain-based variable names (rather than unexplained ordinals) consistently in code, diagrams, accessible descriptions, and adjacent slides; distinguishes variables from the objects they reference.
- [ ] Places the course roadmap after learners understand the example and the reason for the topic.
- [ ] Explains why the concept exists and what developers would do without it.
- [ ] Provides a simple mental model before implementation details.
- [ ] Progresses from simple to complex without relying on unexplained concepts.
- [ ] When teaching Java classes, shows a valid declaration and identifies the class name, braces/body, fields, methods, and per-instance field state.
- [ ] When teaching Java methods, explains the method in plain language and labels return type (`void` where relevant), name, parameters, body, `return` where present, and a concrete call with its argument values and result.
- [ ] Clearly labels code fragments versus complete programs, and keeps the class, method, and call examples consistent with the running scenario.
- [ ] Introduces a topic in its relevant course section instead of moving a later unit to the opening without being asked.
- [ ] Defines unfamiliar ideas in plain language before introducing jargon.
- [ ] Breaks broad topics into logically ordered subtopics.
- [ ] Grounds abstractions in a realistic, progressively developed example.
- [ ] Carries the same example, names, values, and cause/effect through its conceptual and technical explanation, or clearly signals why the example changes.
- [ ] When adjacent slides change examples or subtopics, explicitly explains how the previous idea connects to the next one and why the sequence advances.
- [ ] States the relevant rule before describing a result as correct or incorrect.
- [ ] Identifies common mistakes, limitations, or misconceptions where useful.
- [ ] Clearly distinguishes language, library, protocol, database, and framework concerns.
- [ ] Ends with retrieval/summary and a practical exercise.

## Visual communication

- [ ] The deck is visual rather than a sequence of text-heavy pages.
- [ ] Processes are shown as flows or sequences.
- [ ] For decisions, each labeled outcome visibly connects to its action or terminal node; no route is implied only by card placement or prose.
- [ ] For repetition or return-based processes, the diagram visibly connects the repeated step back to its actual condition/start and shows a separate stopping path; labels or inline arrow characters alone are insufficient.
- [ ] Loop diagrams match the construct's execution order (for example, `while`: check → body → check; counted `for`: initialize → check → body → update → check).
- [ ] Relationships are shown spatially with labeled connections.
- [ ] State changes show meaningful before/after states.
- [ ] In object diagrams, each visual boundary has a clear identity: one object can show multiple moments within one group; separate objects have separate groups, with unchanged values identified. Separate snapshots of one object are explicitly connected and labeled as the same object.
- [ ] State values appear inside the visual itself, and the event causing a change is labeled.
- [ ] The chosen representation fits the topic rather than forcing a generic template.
- [ ] Animation is used only when it improves understanding.
- [ ] Animated concepts remain understandable with reduced motion.
- [ ] Every diagram is legible, labeled, and technically accurate.
- [ ] Slide compositions vary appropriately while retaining one design system.
- [ ] Flow and diagrams use the slide's available area without oversized boxes, blank code panels, or unused outer gutters.
- [ ] Adjacent inline labels and values have visible separation after rendering; HTML does not insert a space between adjacent tags.
- [ ] The main content fits a typical laptop screen without unnecessary scrolling; narrow layouts preserve reading order.
- [ ] Essential diagram nodes, labels, and definitions remain above the controls at 1366×768 and 1280×720; narrow layouts may scroll without hiding the route logic.

## Examples and technical accuracy

- [ ] Java examples are syntactically valid for the project's Java version.
- [ ] APIs are current unless deprecation is the lesson topic.
- [ ] Code is focused, incremental, and free of irrelevant boilerplate.
- [ ] Each code block directly supports the current teaching point.
- [ ] HTML special characters in displayed code are escaped.
- [ ] Simplifications that might mislead are explicitly identified.
- [ ] The example demonstrates the problem being solved, not syntax alone.
- [ ] No advanced prerequisite appears without explanation.

## Artifact and behavior

- [ ] The deliverable is one HTML file with inline CSS and JavaScript.
- [ ] It has no external libraries, frameworks, packages, fonts, media, or network requests.
- [ ] It opens through `file://` without a build step or server.
- [ ] Previous/next buttons and all required keyboard controls work.
- [ ] Slide number, progress, boundary states, hash, and fullscreen state are correct.
- [ ] Interactive elements reset predictably and do not conflict with deck navigation.
- [ ] No normal action produces a browser-console error.
- [ ] Layout remains usable on laptop and narrow/mobile viewports.
- [ ] Focus states, control labels, contrast, and reduced-motion behavior are present.

## Course consistency

- [ ] Uses the shared typography, colors, spacing, code, diagram, and motion language.
- [ ] Title placement and recurring course identity match other lessons.
- [ ] Semantic color meanings remain consistent.
- [ ] The deck does not invent an isolated visual theme.

## Exercise quality

- [ ] The exercise uses a realistic scenario.
- [ ] Objective and expected outcome are clear.
- [ ] Starting point and constraints are stated.
- [ ] An optional challenge is included when appropriate.
- [ ] A complete solution is omitted unless the user requested it.
