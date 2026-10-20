# Skill evaluations

These cases evaluate agent behavior, not just HTML structure. Run them in disposable copies of the repository; never run editing evaluations against a user's working tree.

## Comparison procedure

1. Preserve the pre-change skill package and host adapter files from the repository revision being compared. Record its commit and any uncommitted skill changes; do not assume HEAD includes them.
2. Give each old/new run the same repository inputs, prompt, model, tool access, and output location. Use fresh sessions. Existing fixture defects are intentional.
3. Run the two content-generation cases in `evals.json`. Run the separate visual repair case in `.pi/skills/java-course-slide-design/evals/evals.json` for design changes. Resolve course paths from the repository root and fixture paths from the skill directories. Copy the crowded fixture to the same relative path in each design-evaluation sandbox.
4. Capture outputs, diffs, execution traces, validation output, and available duration/token measurements outside the skill package. Record unavailable measurements as null, not zero.
5. Grade each assertion with PASS, FAIL, or NOT VERIFIED and concrete evidence. Mechanical checks use scripts; browser claims require browser evidence. Treat NOT VERIFIED as incomplete, not a pass.
6. Review old/new screenshots blind where practical. Have a human judge readability, visual explanation, and teaching continuity; do not equate an LLM score with learner outcomes.
7. Compare raw pass/fail/incomplete counts and cost. Repeat on the actual coordinator and implementer models before claiming improved reliability. Keep a held-out prompt when tuning instructions.

Stop when meaningful gains plateau. Do not add narrow rules merely to satisfy one fixture.

## Trigger evaluation

Use each skill's `trigger-queries.json` as a small smoke set, not an accuracy benchmark. First confirm the host discovers both skills. In fresh sessions, observe whether the correct skill is loaded; do not execute negative prompts against live files. Repeat queries to reveal inconsistent activation. Expand with held-out Spanish/English near-misses before tuning the descriptions.

## Verification levels

- Static: `python3 .pi/skills/java-course-slide-generator/scripts/validate_deck.py <deck>`.
- Validator regression tests: `python3 -B -m unittest discover -s .pi/skills/java-course-slide-generator/scripts/tests -v`.
- Browser: follow `references/browser-checks.md`; this is not performed by the static validator.
- Starter smoke test (optional Node 22+ and a Chromium executable, no npm install): `node .pi/skills/java-course-slide-generator/scripts/check_starter.mjs /path/to/chromium /path/to/evidence-directory`. Uses an isolated browser profile, writes screenshots and a JSON report outside the repository, and tests only the bundled starter. It does not grade teaching or validate arbitrary decks. Choose a fresh evidence directory; browser profile files are retained there for scoped cleanup.
- Pedagogy/visuals: apply `references/quality-checklist.md` to outputs and rendered views.

See `baseline.md` for the initial structural baseline. It is deliberately not a completed behavioral benchmark.

## Authoring sources

These inform skill maintenance, not lesson-generation runtime dependencies:

- [Agent Skills best practices](https://agentskills.io/skill-creation/best-practices)
- [Evaluating skill quality](https://agentskills.io/skill-creation/evaluating-skills)
- [Optimizing descriptions](https://agentskills.io/skill-creation/optimizing-descriptions)
- [Anthropic authoring guidance](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/best-practices)
- [OpenAI skills guidance](https://developers.openai.com/codex/build-skills)
