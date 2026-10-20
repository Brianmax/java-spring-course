---
name: java-course-slide-design
description: Designs or improves the visual presentation of this course's existing HTML slide decks: layout, typography, spacing, colors, diagrams, responsive readability, and visual accessibility. Use for design-only slide requests, crowded slides, visual polish, or design reviews in English or Spanish. Not for writing lesson content, creating complete decks, changing navigation, or editing application code.
---

# Java Course Slide Design

Own visual communication, not the lesson's teaching sequence or deck runtime. Work on an existing deck when editing; if the user asks only for a design direction, deliver a concrete design plan without changing files. For a new lesson, new teaching content, or navigation/interaction behavior, use `java-course-slide-generator` instead; that skill remains responsible for the complete artifact. A content-generation request may still include visual design without requiring a second skill.

## Load only what the task needs

The paths below are relative to the repository root:

- Read this skill's [design system](references/design-system.md) in full before any design plan, edit, or review. It is the single source of truth for the course's palette, typography, stage, diagrams, motion, and responsive layout.
- Before visual verification, read `.pi/skills/java-course-slide-generator/references/browser-checks.md`. Check the relevant viewports and changed slides; a source inspection is not a rendered check.
- Before accepting an edit or completing a review, use the applicable **Visual communication**, **Artifact and behavior**, and **Course consistency** items in `.pi/skills/java-course-slide-generator/references/quality-checklist.md`. Mark uninspected items NOT VERIFIED.
- If a requested visual change would alter deck controls or runtime, hand that part to the generator workflow and load its deck contract. Do not silently change behavior under a design-only request.

## Design-only workflow

The design rules to apply are: use the shared Tecsup-inspired palette and offline system fonts; give every slide a clear hierarchy and one meaningful composition rather than repeating cards; preserve the 16:9 stage, title placement, and generous safe area; put values and events inside diagrams and explicitly draw labeled branches and return paths; simplify or split crowded layouts before reducing type; stack content in reading order on narrow screens; use motion only when it clarifies an idea and respect reduced motion; maintain contrast, focus visibility, and readable labels above controls at both laptop sizes. The linked design system gives the exact tokens, dimensions, and component rules.

1. Read the target slide and its neighbors, relevant CSS/SVG, and any existing design constraints. Identify the slide's current message, visual hierarchy, reading order, and specific visual problem. Preserve the language, names, values, code meaning, and lesson order.
2. Choose a composition that serves that message: for example, process flow for a sequence, labeled relationship for an association, or code alongside visible runtime state. Give every important arrow, branch, and state an explicit label. Do not create a decorative diagram that misstates the concept.
3. Make a short, actionable design brief: target slide(s), intended hierarchy, composition, layout at laptop and narrow widths, and acceptance checks. For a review-only request, stop at findings with slide, severity, evidence, and suggested correction; **do not edit**.
4. When edits are requested, change only the targeted slide's visual markup, CSS, inline SVG, or presentation-only motion. Keep the deck self-contained and offline. Do not rewrite teaching copy, Java examples, navigation, progress, or shared runtime merely to achieve a new look. If clarity requires a new teaching point or slide, explain the dependency rather than adding it without permission.
5. Check the diff for scope and regressions. Run `python3 .pi/skills/java-course-slide-generator/scripts/validate_deck.py <edited-deck.html>` on any edited deck. Fix errors and investigate warnings. Static validation cannot prove visual quality.
6. Inspect the changed slide(s), neighbors, and every affected layout at 1366×768, 1280×720, and a narrow viewport (for example 390×844) when browser tooling is available. Check clipping, labels, reading order, contrast, controls, and reduced-motion state where relevant. If a shared style changes, inspect all affected slides. Report browser checks as NOT VERIFIED when unavailable; never claim a visual pass based on source alone.

Favor rearranging, simplifying visual containers, or increasing useful diagram space over shrinking text. Preserve course identity and accessible focus/contrast; do not use external assets or network dependencies. In the final response, distinguish the design outcome or findings from static checks, rendered evidence, and anything unverified.
