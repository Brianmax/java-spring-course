---
name: grill-me
description: Clarifies the scope, constraints, and acceptance criteria of a substantial request through focused questions before planning or implementation. Use when the user asks to be interviewed, says to grill them, or when a topic or project has important unresolved choices such as desired complexity, audience, features, or boundaries. Do not use for straightforward tasks where reasonable assumptions are sufficient.
---

# Grill Me

Help the user make the decisions that materially shape the requested work. This skill is an interactive discovery phase, not a plan, implementation, or approval to proceed.

## Principles

- Ask questions that change the eventual scope, complexity, approach, or acceptance criteria. Do not ask for details that can safely be decided later.
- Use the user's language and vocabulary. Explain unfamiliar choices briefly and neutrally; do not assume the user knows framework or security terminology.
- Ask a small number of related questions at a time, usually one to three. Prefer short, concrete choices with an “Other” option, and allow the user to answer in their own words.
- Include a recommended default when it helps the user decide, and explain its consequence in one sentence. Do not steer toward unnecessary complexity.
- Reuse information the user has already provided. State reasonable assumptions instead of asking the same thing twice.
- Keep confirmed decisions, assumptions, and unresolved questions distinct. Never present an assumption as a user decision.
- Stop when the important decisions are clear enough to produce a useful plan. Do not prolong the interview to achieve certainty about minor details.
- Do not begin planning or implementation in the interview response unless the user asked for that combined workflow. Summarize the agreed scope and the next step instead.

## Interview workflow

1. **Restate the goal briefly.** Identify the requested outcome and the main uncertainty that could change it.
2. **Prioritize scope.** Ask about the decisions with the largest impact first: intended audience and prerequisites, learning or product outcome, desired depth, and explicit in-scope/out-of-scope items.
3. **Resolve constraints.** Ask about platform, tools, language, runtime, time, format, or environment only when these affect the result. For software projects, distinguish the requested teaching topic from supporting infrastructure.
4. **Set complexity through observable boundaries.** Prefer asking what learners should be able to do and which features to include or exclude, rather than asking only “how advanced?” Translate levels into concrete capabilities.
5. **Confirm completion criteria.** Ask what evidence or behavior would make the result useful, and whether the user wants a review/approval checkpoint before implementation.
6. **Summarize and stop.** Give a compact scope summary with confirmed choices, explicit assumptions, exclusions, and any remaining material question. State the logical next step, such as preparing a plan, without treating the summary as permission to implement.

## Choosing questions for a teaching project

When the request is to create a project that teaches a topic, consider these dimensions and ask only those that remain consequential:

- Learner profile and prerequisites.
- What learners should explain or build by the end.
- Desired depth, expressed as concrete milestones or capabilities.
- Required and optional features, plus explicit exclusions.
- Stack and environment constraints, including database and local setup where relevant.
- Whether the deliverable should include a complete runnable project, a step-by-step recreation guide, exercises, tests, or instructor notes.
- How the guide and project should be checked for consistency.
- Whether to pause for approval after the scope or plan.

Do not silently expand a teaching example into production architecture. Flag security-sensitive or infrastructure choices, offer a proportionate default, and ask when the choice changes what students will learn.

### Example: scoping a JWT project

Avoid asking only “How complex should JWT be?” Explain the practical levels, then ask the user to choose or adjust:

- **Core:** issue and validate access tokens for a small API; no refresh tokens or roles.
- **Classroom application:** add login, password hashing, protected routes, and one authorization rule; use a local relational database.
- **Extended:** add refresh-token rotation/revocation, more roles, and security-focused tests, with the additional concepts and setup this requires.

Make clear that the labels are teaching-scope choices, not claims that the smaller example is production-ready. Ask separately about a technology constraint only if it is not already established.

## Output while interviewing

Keep each turn focused. When useful, present numbered questions with concise options and a recommended choice. After the user answers, acknowledge the decisions and ask only the next unresolved, high-impact questions. When enough is known, finish with:

### Agreed scope: <topic>

- **Audience and prerequisites:**
- **Outcome:**
- **Included:**
- **Excluded:**
- **Constraints:**
- **Deliverables and acceptance criteria:**
- **Assumptions or open questions:**
- **Next step:**

If there are no meaningful open questions, say so and offer the planning step. Do not claim that implementation has started or been approved unless the user explicitly requested it.
