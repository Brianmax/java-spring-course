# Planning and review examples

These examples specify teaching intent without forcing every topic into the same composition.

## Single-slide plan

- **Place:** methods section, after multiplication with variables, before parameters.
- **Objective:** connect a method call with the result it returns.
- **Visible message:** “Ana quiere 3 cuadernos de S/ 12 cada uno. Una acción con nombre puede calcular el total: S/ 36.”
- **Continuity:** previous slide already defined a pedido as a record of what someone wants to buy. Reuse Ana, 3, 12, and 36.
- **Composition:** title above; code left and a concrete call → calculation → returned value diagram right. Natural-height panels; no second outer card.
- **Code scope:** label this as a class fragment, not a complete runnable program:

```java
double calcularTotal() {
    return 3 * 12;
}
```

- **Reading order:** explain the named action, identify `double`, `calcularTotal`, empty parameter list, body, and `return`; trace `calcularTotal()` to `36.0` (S/ 36).
- **Labels:** “llamada”, “3 × 12”, “valor devuelto: 36.0”; connect each stage explicitly.
- **Simplification:** fixed values isolate the return mechanism; the next slide makes quantity and unit price parameters. Do not silently switch domains.
- **Acceptance:** declaration and call agree; units are visible; each label fits at both laptop sizes; no constructors, access modifiers, or `static` before prerequisites.

## Weak opening → stronger sequence

**Weak:** “POO evita errores porque agrupa datos.” This invents a correctness guarantee and does not show a problem.

**Stronger, spread over slides as needed:**
1. Ana wants 3 notebooks at S/ 12; explain what the purchase record contains.
2. Show variables and a method correctly producing S/ 36.
3. Luis wants 2 folders at S/ 5. Show that a caller *could* accidentally combine Ana's 3 with Luis's 5, producing S/ 15. This is a possible mistake, not an inevitable procedural failure.
4. Show a labeled order object holding Ana's own quantity and unit price with an action that uses them. Name this organization only after explaining it.
5. Contrast the same values in both approaches. Grouping helps maintain associations; validation rules still have to be written.

## Loop diagram acceptance example

Situation: fulfill three pending orders, one per action.

- Show initial `pendientes = 3`, then a decision diamond labeled `pendientes > 0`.
- Connect “sí” to “atender un pedido”, then to `pendientes--`, then draw an arrow returning to the actual decision.
- Connect “no” to a separate terminal “terminado: pendientes = 0”. Keep exit and return paths apart.
- Trace the condition values 3, 2, 1, 0; the body runs three times. The diagram remains understandable when motion is disabled.
- Do not use a linear row of cards or an arrow character as a substitute for the returning connector. Do not reuse this geometry unchanged for `do-while` or counted `for`.

## Delegation handoff

```text
Task: implement the approved methods slide plan above.
Target: lessons/<lesson>.html; one writer only.
Allowed scope: this slide, its immediate transition, necessary styles.
Preserve: running example, curriculum order, existing runtime.
Read: canonical SKILL.md and its route-applicable references.
Checks: complete-deck static validation; changed slide at both laptop
sizes and a narrow viewport when browser tools are available.
Return: changed paths, checks with evidence, unresolved limitations.
If the plan is inaccurate, report the issue before changing its intent.
```

## Reviewer finding

> **High — slide 7, rendered at 1280×720:** the “no” connector ends at the body instead of the terminal, contradicting the `while` condition. Route “no” directly to “terminado” and keep the return arrow connected to the decision. Recheck both laptop layouts.

If only source is available, say “source inspection suggests…” and leave the rendered result NOT VERIFIED. No findings means no blocking issue found within the inspected scope, not universal approval.
