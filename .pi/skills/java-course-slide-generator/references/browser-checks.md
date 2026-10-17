# Browser verification protocol

Use available browser tools; do not assume a particular automation package is installed. Adding test tooling is separate from adding runtime dependencies to the deck: the final HTML must still work without any tooling.

For the bundled starter only, `scripts/check_starter.mjs` can automate a subset with Node 22+ and Chromium; see `evals/README.md` for invocation and limitations. Passing it does not certify a generated deck.

## Record evidence

Record deck path, browser, viewport, slide numbers/titles, actions taken, observations, and screenshot paths where available. Label each result PASS, FAIL, or NOT VERIFIED. Source inspection is not browser evidence.

For new decks, traverse all slides. For local changes, inspect changed slides and neighbors; inspect every affected layout if shared CSS changed. For runtime changes, execute the full navigation checks.

## Viewports and layout

- Check 1366×768 and 1280×720, plus a narrow viewport such as 390×844.
- Titles, relevant code lines, diagram labels, and definitions must remain readable and above controls on laptops. Check long labels and the densest slides explicitly.
- On narrow screens, preserve reading order and allow vertical scrolling; no clipped flow routes or controls covering content.
- Inspect actual rendered spacing between inline labels, numbers, and units.
- Observe roomy/balanced/dense layout using the design-system guidance; don't treat a numeric DOM measurement as proof of legibility.

## Behavior

- Open directly with `file://` when supported. If the tool requires a local server, report that file access is unverified; server success is not proof of offline/file compatibility.
- Check previous/next buttons, arrows (all four), Space, Home, End, and disabled boundary buttons. Repeated input must not move outside the deck.
- Check valid/invalid initial hashes and hash changes; confirm current/total and final-slide progress.
- Tab through controls. Enter/Space must activate the focused control without unintended slide movement. Deck shortcuts must not hijack inputs, selects, editable regions, or custom interactions.
- Check F and fullscreen button entry/exit when supported, synchronize state after Escape, and handle rejection without an unhandled error. Unsupported environments must disable or explain the action.
- If notes exist, verify N, notes-button state, and resetting notes between slides. Escape closes help/notes before fullscreen.
- Hidden slides must not leave tabbable controls or running educational sequences. Test replay/reset and reduced-motion rendering when present.
- Check console errors during normal actions and unexpected external requests. Offline operation must not depend on cached network assets.

## Completion

Fix failures and repeat affected checks. If a browser, fullscreen capability, or viewport control is unavailable, state exactly what remains unverified. Never infer a visual or interaction pass from the static validator.
