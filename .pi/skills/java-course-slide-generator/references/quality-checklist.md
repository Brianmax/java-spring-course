# Lesson Quality Checklist

Apply this checklist manually after generating the deck. Do not claim an item passed unless it was actually inspected.

## Pedagogy

- [ ] Defines what the topic is in beginner-appropriate language.
- [ ] Establishes a realistic problem before syntax.
- [ ] Explains why the concept exists and what developers would do without it.
- [ ] Provides a simple mental model before implementation details.
- [ ] Progresses from simple to complex without relying on unexplained concepts.
- [ ] Introduces a topic in its relevant course section instead of moving a later unit to the opening without being asked.
- [ ] Defines unfamiliar ideas in plain language before introducing jargon.
- [ ] Breaks broad topics into logically ordered subtopics.
- [ ] Grounds abstractions in a realistic, progressively developed example.
- [ ] Carries the same example, names, values, and cause/effect through its conceptual and technical explanation, or clearly signals why the example changes.
- [ ] States the relevant rule before describing a result as correct or incorrect.
- [ ] Identifies common mistakes, limitations, or misconceptions where useful.
- [ ] Clearly distinguishes language, library, protocol, database, and framework concerns.
- [ ] Ends with retrieval/summary and a practical exercise.

## Visual communication

- [ ] The deck is visual rather than a sequence of text-heavy pages.
- [ ] Processes are shown as flows or sequences.
- [ ] Relationships are shown spatially with labeled connections.
- [ ] State changes show meaningful before/after states.
- [ ] State values appear inside the visual itself, and the event causing a change is labeled.
- [ ] The chosen representation fits the topic rather than forcing a generic template.
- [ ] Animation is used only when it improves understanding.
- [ ] Animated concepts remain understandable with reduced motion.
- [ ] Every diagram is legible, labeled, and technically accurate.
- [ ] Slide compositions vary appropriately while retaining one design system.
- [ ] Flow and diagrams use the slide's available area without oversized boxes, blank code panels, or unused outer gutters.
- [ ] The main content fits a typical laptop screen without unnecessary scrolling; narrow layouts preserve reading order.

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
