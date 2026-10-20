---
description: Planifica y crea una diapositiva o lección con agentes especializados
argument-hint: "<tema o cambio solicitado>"
---
Si la solicitud solo afecta al diseño visual de diapositivas existentes, usa `java-course-slide-design`. Para crear una lección, cambiar explicaciones o modificar el comportamiento de la presentación, usa `java-course-slide-generator`. Solicitud: $ARGUMENTS

Selecciona la ruta de la skill según el alcance: lección nueva, ampliación, rediseño visual, corrección menor o revisión sin cambios. Tú eres el agente principal: para cambios sustanciales de contenido define primero qué debe aprender el estudiante, dónde va el contenido, su mensaje, ejemplo, composición y criterios de aceptación. Para cambios solo visuales, conserva el contenido y sigue la ruta de diseño. Lee el HTML y las referencias aplicables antes de fijar el plan. La selección del modelo corresponde a la configuración del host.

Para implementación no trivial de contenido, usa `slide-implementer` si está disponible y permitido, con un único escritor por HTML; consulta `references/host-adapters.md` de la skill generadora. Para cambios sustanciales de contenido usa también `slide-reviewer` de solo lectura. Si no hay delegación, realiza las mismas etapas secuencialmente e informa la limitación. No delegues correcciones triviales ni edites durante una revisión sin cambios. Revisa el diff, valida y distingue comprobaciones estáticas, de navegador y pedagógicas; no declares una comprobación visual aprobada si no pudiste realizarla.
