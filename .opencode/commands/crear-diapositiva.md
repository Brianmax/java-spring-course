---
description: Crea, mejora o revisa diapositivas del curso con la skill y los agentes especializados
---

Usa la skill `java-course-slide-generator` para esta solicitud: $ARGUMENTS

Lee la skill canónica en `.pi/skills/java-course-slide-generator/SKILL.md` y selecciona su ruta según el alcance. Para cambios sustanciales, lee el HTML y las referencias aplicables antes de definir el objetivo, lugar en la lección, mensaje, ejemplo, composición y criterios de aceptación.

Para implementación no trivial usa el subagente `slide-implementer` si está disponible y permitido, con un único escritor por HTML. Para cambios sustanciales solicita también una revisión de solo lectura a `slide-reviewer`, proporcionando capturas cuando estén disponibles. No delegues correcciones triviales ni edites en una revisión sin cambios. Si falta delegación, sigue las mismas etapas secuencialmente e informa la limitación.

Revisa el diff, valida y distingue comprobaciones estáticas, de navegador y pedagógicas. No declares una revisión visual aprobada si no pudiste realizarla. Conserva el modelo seleccionado por el usuario.
