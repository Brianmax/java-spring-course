---
name: java-teaching-project
description: Creates complete, runnable Java teaching projects with a step-by-step Markdown recreation guide and API endpoint checks. Use for requests to build a course project, demonstration, or guided exercise for Java, Spring, persistence, APIs, security, or related topics. Start with grill-me when material scope decisions are unresolved; coordinate planning, implementation, and independent guide-only reproduction.
---

# Java Teaching Project

Create a complete teaching project and a guide that lets an instructor and students recreate it. The guide is a first-class deliverable and must be verified by a separate agent that cannot inspect the implementation.

## Workflow

1. **Scope interview.** Read and follow `.pi/skills/grill-me/SKILL.md`. Reuse details already given; ask only material questions about learners, outcome, complexity, features and exclusions, stack, environment, deliverables, and approval gates. Do not start the plan or implementation until the scope is sufficiently clear. Summarize the agreed scope. Treat it as permission to plan, not automatic permission to implement, unless the user explicitly asked for the full workflow.
2. **Plan with a subagent.** Delegate to `teaching-project-planner` when the host exposes it. Provide the agreed scope and relevant repository context. The planner returns milestones, prerequisites, decisions, artifact layout, acceptance checks, and risks. Review the plan with the user before implementation unless the user explicitly requested scope-through-delivery in one task. Preserve user approval gates.
3. **Implement with a subagent.** After the plan is approved, delegate the project and guide to `teaching-project-implementer` as the sole writer for the project output. Give it the approved scope and plan. It must deliver both the complete runnable project and a step-by-step guide derived from the actual implementation, plus API checks when the project exposes HTTP endpoints.
4. **Verify independently with a new set of subagents.** Once implementation is stable, start fresh review work:
   - `guide-only-rebuilder` receives only the recreation guide, the approved learning scope, and a clean destination. It must not read, search, or receive implementation source, generated artifacts, or the implementer's explanation. It attempts to reproduce the project from the guide alone and reports every ambiguity, missing step, and result. Run it only in an isolated runtime that cannot access the authored project; a temporary directory or worktree alone does not establish this boundary. Do not overwrite the authored project.
   - `teaching-project-reviewer` receives the approved plan, implementation, guide, test evidence, and rebuild report. It reviews technical correctness, scope, setup, teaching sequence, guide/code consistency, and endpoint-check quality without editing files.
   These are distinct from the implementation subagent. Never present author self-review as independent reproduction.
5. **Resolve findings and validate.** Address blockers with the implementer or primary agent, then rerun affected checks. The guide-only rebuilder must use a fresh destination after guide changes. Report changed paths, exact checks and outcomes, rebuild limitations, and anything not verified. Never claim the guide reproduces the project if the rebuild did not complete.

If delegation is unavailable or prohibited, follow the same stages sequentially and clearly report that independent guide-only verification was not performed. Do not pretend a role was spawned.

When using Pi's `.pi/extensions/subagent` tool for project agents, pass `agentScope: "project"` (or `"both"` when user agents are also needed) and keep `confirmProjectAgents: true`. Its default scope is `"user"`, which does not discover `.pi/agents/`. Do not change that trust-preserving default to make project agents appear automatically.

## Deliverables and defaults

- Place the project under a topic-specific directory in `projects/`, following existing repository conventions. Inspect `projects/README.md` and neighboring projects first; preserve unrelated work.
- Include all source and configuration required for a complete, runnable final project.
- Include a Markdown guide named `GUIDE.md` alongside the project that rebuilds it from a clean starting point, not merely describes the finished code. Keep a project `README.md` for overview/setup if useful, but make `GUIDE.md` the canonical reproduction input.
- If the project exposes HTTP endpoints, include an executable shell script, normally `scripts/check_api.sh`, that queries those endpoints and asserts expected outcomes.
- Default to the repository's Java conventions where applicable. Ask before choices that materially affect the lesson. Do not turn a classroom project into a production system without explicit scope.
- For security topics, accurately label educational simplifications and avoid implying that a teaching configuration is production-ready.

## Implementation requirements

The implementer must keep the code and guide synchronized. Write the guide against actual file paths, commands, configuration, endpoint behavior, and tool versions in the finished project. Include:

- prerequisites and environment setup;
- ordered, copyable steps to create the project and files;
- explanations of what each milestone teaches and why;
- commands to run and observable expected results at each meaningful checkpoint;
- common failure diagnosis where environment or ordering makes it likely;
- a final run/test procedure and clear cleanup instructions when needed.

Do not omit files or settings needed to reproduce the implementation. A reader must not need to infer hidden setup or consult implementation source outside the guide.

### HTTP endpoint checks

For an API project, create `scripts/check_api.sh` (or a similarly discoverable `.sh` file) that:

- is executable and documents prerequisites, startup instructions, and its base URL;
- uses `curl` to call each in-scope endpoint or a deliberate representative set, including relevant success and error cases from the agreed scope;
- checks HTTP status and meaningful response content, not just that the server responds;
- uses portable shell practices, clear failure messages, and a nonzero exit code on failed expectations;
- avoids destructive or non-repeatable requests unless the script safely prepares and cleans up its own test data;
- allows the base URL to be overridden and never embeds secrets;
- states any required optional tools (for example `jq`) and handles their absence clearly.

The guide must explain how to start the API and run the script. Execute the script against the implementation when feasible and report whether the server setup, dependencies, and external services were available.

## Independent guide-only reproduction contract

The rebuilder's allowed inputs are exactly the recreation guide, the approved scope/outcome (not implementation-specific details absent from the guide), and a clean destination. Launch it in a fresh isolated runtime (for example, a container with only the guide mounted read-only and the empty destination mounted writable). Do not mount the checkout, implementation files, source-control data, agent notes, or test outputs. Supply the role's operating instructions without making the repository accessible. A worktree or a different `cwd` in the same host is not an information boundary, and agent prompts or tool permissions cannot make unrestricted shell access safe there. If isolation cannot be established, do not launch the rebuilder against the checkout; report independent reproduction as unverified and explain why.

The rebuilder must not inspect project files, implementation agent notes, source-control diffs, or test outputs before completing the attempt. It may use tools and dependencies described in the guide and ordinary language/runtime documentation. It records commands, outcomes, questions, and any reasonable assumptions it had to make. After the attempt, return the report to the primary agent for comparison with the authored project.

The purpose is to test whether the guide teaches someone to recreate the project, not to make the rebuilt copy identical by guesswork. Any inferred detail that should have been explicit is a guide defect.

## Handoff format

Summarize:

- agreed scope and approved plan;
- project and guide paths;
- endpoint script path and which assertions it covers, if applicable;
- implementation/build/test results;
- guide-only reproduction result and destination (do not retain temporary artifacts unless requested);
- independent review findings and their resolution;
- checks that remain unverified and why.
