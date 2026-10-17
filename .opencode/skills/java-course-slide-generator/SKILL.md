---
name: java-course-slide-generator
description: Creates, revises, and reviews this course's standalone HTML slide decks for Java, Spring, databases, and software development. Use for course presentations, slide explanations, diagrams, readability, or deck accessibility, including Spanish requests for diapositivas. Not for application-code changes, site catalog edits, or chat-only explanations.
---

# Java Course Slide Generator (OpenCode entry point)

Read the complete [canonical skill](../../../.pi/skills/java-course-slide-generator/SKILL.md), select the route matching the request, and load its applicable references. Resolve all reference, asset, and script paths relative to `.pi/skills/java-course-slide-generator/`, not this adapter directory.

Keep the canonical skill as the single source of truth. For nontrivial work, use the project `slide-implementer` and read-only `slide-reviewer` subagents when available and permitted; otherwise use the documented sequential fallback. Do not change model settings. Review-only requests do not authorize edits.

Run `.pi/skills/java-course-slide-generator/scripts/validate_deck.py` on edited decks. Distinguish static validation from browser evidence and teaching review, and report unverified checks explicitly.
