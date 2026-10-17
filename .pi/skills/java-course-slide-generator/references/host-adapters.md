# Host adapters

The teaching standards are host-independent. Use only tools and agent roles actually exposed by the current host, subject to its permissions. Model choice belongs to host configuration, not this skill.

| Host | Entry and delegation |
| --- | --- |
| Pi | Canonical `.pi/skills/java-course-slide-generator/SKILL.md`; `/crear-diapositiva` prompt. If the project subagent extension is trusted and loaded, use project `slide-implementer` and read-only `slide-reviewer` with `agentScope: "project"`. Inspect the live tool schema first. |
| Codex | Repository entry `.codex/skills/java-course-slide-generator/SKILL.md` points here. Use configured roles in `.codex/agents/` when exposed. Verify discovery on the installed version; a file's presence alone does not prove automatic registration. |
| OpenCode V2 | Auto-discovered `.opencode/skills/java-course-slide-generator/SKILL.md` points here. Project subagents live in `.opencode/agents/`; both inherit the parent model. The reviewer has an explicit read-only tool allowlist; the primary agent supplies rendered screenshots. Use `/crear-diapositiva` from `.opencode/commands/`. |
| Other hosts | Load the canonical skill explicitly or configure discovery according to that host. If generic delegation is available, supply the role instructions and complete handoff. Do not invent named tools, model IDs, or configuration fields. |

If delegation is missing or prohibited, keep the same plan → implementation → review → validation stages in one session and report the fallback. Trivial wording edits do not need delegation. Review-only requests never authorize implementation.

Keep host-specific prompts and role definitions as thin adapters. They should refer to the canonical skill's route-specific loading instructions rather than reproducing all course standards.
