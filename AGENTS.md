# Project instructions

## Readable subagent handoffs and summaries

These rules apply to the parent agent writing a delegation prompt, the receiving subagent, and agents producing context summaries or continuation notes.

- Write clear sentences and bullet points with normal word spacing and punctuation.
- Do not concatenate words, omit word boundaries, or invent cryptic shorthand to reduce token use. For example, write "Review the VID38 plan" rather than "PlanVID38" and "Return actionable findings" rather than "Returnactionable".
- Reduce length by removing repetition and irrelevant background, not spaces or essential meaning.
- Preserve exact file paths, code symbols, task IDs, version strings, commands, and other literal identifiers. Do not insert spaces into legitimate identifiers.
- State permissions and approval requirements explicitly. Keep "do not edit", "do not publish", and "ask before release" as separate, readable constraints when applicable; never compress away their conditions.
- Distinguish completed work, remaining work, blockers, and unverified checks in summaries. Do not turn assumptions or earlier approvals into authorization for a new action.
- Before sending a handoff or summary, reread it for missing spaces, ambiguous abbreviations, and lost scope or approval conditions. Rewrite unclear passages.

## Handoff template

Use this structure for nontrivial delegation. Omit sections that do not apply; keep small requests brief. This template does not itself authorize delegation, edits, or publication.

```text
Task
- Describe the specific outcome and target files.

Context
- Provide the relevant plan, current state, and prerequisite information.
- Name the applicable project instructions and skill references.

Constraints
- Define the allowed scope and what must remain unchanged.
- State read-only or editing permissions and any approval gates explicitly.

Checks
- List the required checks and the evidence expected.
- Require unavailable or incomplete checks to be reported as unverified.

Expected output
- Request changed paths or concrete findings, as appropriate.
- Include evidence, blockers, and remaining limitations.
```
