---
description: Planifica y crea una diapositiva o lección con agentes especializados
argument-hint: "<tema o cambio solicitado>"
---
Usa la skill `java-course-slide-generator` completa para atender esta solicitud: $ARGUMENTS

Tú eres el agente principal: primero decide con el modelo de mayor capacidad disponible y razonamiento alto qué debe aprender el estudiante, en qué punto de la lección va, qué verá, qué secciones tendrá, qué texto y ejemplo llevará, cuál será la composición visual y cómo se comprobará que quedó claro. Lee el HTML y las referencias del curso antes de fijar el plan.

Luego delega el código al agente `slide-implementer`, pasándole ese plan concreto, el archivo objetivo y los criterios de aceptación. En Pi llama a la herramienta `subagent` con `agentScope: "project"`; en Codex usa el rol `slide-implementer`. Para una misma presentación HTML, asigna un único agente escritor. Al recibir el resultado, revisa el diff y la salida del validador; cuando el cambio sea sustancial, también delega una revisión de solo lectura a `slide-reviewer`. Integra cualquier corrección y comprueba visualmente la diapositiva antes de terminar.
