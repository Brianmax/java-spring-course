---
name: java-course-slide-planner
description: Plans the instructional structure of a lesson deck for this course before implementation. Use when asked to plan, outline, sequence, or decide what each slide should teach for a specific Java, Spring, database, or software-development topic, in English or Spanish. Produces a reviewable slide-by-slide blueprint; does not create or edit HTML decks. For implementation, use java-course-slide-generator after the plan is accepted. Not for visual-only redesign of an existing deck, which belongs to java-course-slide-design.
---

# Java Course Slide Planner

Create a concrete instructional blueprint that a learner-facing deck can be implemented from. This skill owns lesson planning, not HTML authoring, visual polishing of existing decks, or deck runtime. Do not edit lesson files. The user can review or revise the plan before handing it to `java-course-slide-generator`.

## Scope and handoff

- Plan a new lesson, a new section, or a substantial content reorganization when specifically requested.
- Return the plan in the conversation unless the user explicitly asks to save it. If asked to save, use a suitable planning document path and do not create the HTML deck.
- Do not treat a plan as approval to implement. Wait for an explicit implementation request; then the generator skill owns the finished deck.
- For a small wording/content change to an existing deck, use the generator directly. For a visual-only review or redesign, use `java-course-slide-design`.
- If the request combines planning and implementation, first provide the plan and seek approval before implementation unless the user explicitly asks for both in the same request.

## Planning workflow

1. **Understand the request.** Identify topic, audience, expected prior knowledge, course language, lesson scope, and any constraints. Default to beginner learners and clear Spanish for this course. Read relevant neighboring lesson files and course context when they exist; do not invent prerequisites or claim to have inspected material you did not read.
2. **Resolve material uncertainty.** Ask one concise question only if an unknown would materially change the content or sequence (for example, whether students already know classes). Otherwise state a reasonable assumption and proceed. Keep assumptions visible in the plan.
3. **Sequence instruction.** Start with a concrete, ordinary-language situation, establish what learners can already do, motivate the new concept with a plausible need, then explain the concept before syntax. Keep the topic in its correct curriculum position. Do not manufacture failure or imply one approach is inherently wrong when both work.
4. **Design each slide's information.** Give each slide one main teaching purpose. Specify the learner-facing message and the actual information/examples it should contain, not just a title. Track one concrete example consistently through the lesson. Add transitions where the example or subtopic changes.
5. **Suggest a visual by instructional purpose.** Describe what the visual communicates (such as a sequence, relationship, state change, comparison, or decision flow). This is an information-design suggestion, not pixel-level styling or a replacement for the course design system.
6. **Audit teaching dependencies.** Walk the proposed sequence in order and identify every technical concept, system, domain term, and notation that a later slide relies on. Check titles, labels, diagrams, examples, and code—not only explanatory paragraphs. Ensure each prerequisite is introduced and defined in learner-friendly language before it is used as known information. For example, do not show MongoDB documents before explaining what MongoDB is and what a document means; do not introduce collections in a diagram without defining them first or at that point. Add an introductory slide/step, define the term at first use, or explicitly flag the unresolved dependency. A passing mention or unexplained preview does not count as an introduction.
7. **Check coverage and load.** Include prerequisites only where needed, a recap tied to the opening problem, and realistic practice with an objective, starting point, expected outcome, and constraints. Split slides that would carry multiple teaching points; do not pad to a target slide count.
8. **Return a handoff-ready plan.** Include assumptions, open questions if any, slide order, content, visual direction, and acceptance checks. Mark the plan as proposed, not implemented.

## Teaching requirements

- Distinguish Java language features from framework features. Keep technical claims accurate and proportionate to the learner's level.
- For process concepts such as loops, specify the actual order of steps, the return path, and where the process stops; do not prescribe a generic misleading diagram.
- Put a topic's beginner explanation at the start of its section rather than front-loading later topics.
- Exercises should not include full solutions unless requested.

## Reusable information-organization rules

Use slides 1–19 of `lessons/databases.html` as a positive example of coherent lesson progression and information organization, not as a mandatory sequence or visual template. Generalize its instructional practices to the requested topic:

1. **Orient learners with a concrete context.** Establish who is doing what, what information or materials matter, and what learners will follow through the lesson.
2. **Build the need before presenting the response.** Show a plausible situation, limitation, or question that makes the topic relevant. Do not manufacture a failure when the known approach also works.
3. **Move from familiar to unfamiliar in small steps.** Start from learners' known tools and ideas; introduce one new concept at a time, then connect it to the previous step.
4. **Introduce the broad idea before its varieties and named tools.** Explain the general concept and its purpose before comparing models, APIs, frameworks, products, or implementation details.
5. **Give each slide a clear local job and a place in the larger argument.** A slide should answer a learner question, establish a needed idea, demonstrate an operation, or bridge to the next step. Make the transition explicit when the connection would otherwise be implicit.
6. **Keep information and examples continuous.** Reuse the same entities, names, values, units, and scenario wherever possible. If the example must change, explain why and provide a bridge.
7. **Separate ideas beginners are likely to conflate.** Plan explicit contrasts for similar terms or roles, using concrete examples rather than relying on vocabulary alone.
8. **Demonstrate before generalizing.** Show a concrete instance or trace, then state the rule or comparison it supports. Ensure the example actually supports the claim.
9. **Compare with conditions and trade-offs.** State what each option does well and what the choice depends on; avoid presenting context-dependent tools or models as universally superior.
10. **Use misconceptions as planned checks.** Identify likely confusion and place a corrective explanation where it becomes relevant, not as a disconnected warning at the end.
11. **Close the loop.** Summarize the ideas in relation to the opening context and have learners apply them in a realistic practice task.

These are decision rules, not a required slide-by-slide recipe. Adapt, combine, or omit a step when the topic does not need it, and briefly note why if an omission could look like a gap. Do not force every lesson into the database deck's exact order, number of slides, or example domain.

## Output format

Use this compact structure, adapting it to the scope:

### Plan: <topic>

- **Audience and assumed knowledge:**
- **Learning outcome:** What learners should be able to explain or do by the end.
- **Running example:** Concrete domain, names, values, and event carried through the lesson.
- **Assumptions:** Explicit defaults or confirmed constraints.
- **Proposed slide count:** A reasoned estimate, not a quota.

| # | Slide / teaching purpose | Information and example on the slide | Suggested visual | Transition or learner check |
|---|---|---|---|---|

After the table, include:

- **Coverage check:** Prerequisites, likely misconception(s), recap, and practice.
- **Acceptance checks:** Observable criteria for deciding whether the implemented slides follow the plan.
- **Handoff:** State that `java-course-slide-generator` can implement the approved plan; identify any unresolved decision that needs user input.

Keep slide descriptions specific enough to implement, but do not draft full slide copy or complete code unless the user requests that level of detail. A concise code fragment may be named when essential to define the intended teaching point, but the planner must not produce the HTML deck.

## Existing course conventions

For course topics, follow the teaching sequence and accuracy expectations in `.pi/skills/java-course-slide-generator/SKILL.md` (especially its non-negotiable teaching sequence and technical precision sections). If planning a design-sensitive or diagram-heavy lesson, consult `.pi/skills/java-course-slide-design/references/design-system.md` for the course's visual vocabulary. These references inform the plan; implementation validation belongs to the generator workflow.

When accessible and relevant, inspect slides 1–19 of `lessons/databases.html` to ground these generalized organization rules in the course's existing positive example. Treat them as evidence of approach, not proof that every detail in that deck is exemplary or an instruction to reproduce it mechanically.

When repository materials are relevant, report which files were inspected. Do not claim browser, visual-rendering, or implementation checks for a plan-only task.
