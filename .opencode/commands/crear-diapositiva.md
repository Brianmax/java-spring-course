---
description: Crea, mejora o revisa diapositivas del curso con la skill y los agentes especializados
---

Para una solicitud exclusivamente visual sobre diapositivas existentes, usa `java-course-slide-design`. Para crear una lección, cambiar explicaciones o modificar el comportamiento de la presentación, usa `java-course-slide-generator`. Solicitud: $ARGUMENTS

Lee la skill canónica correspondiente en `.pi/skills/` y selecciona su ruta según el alcance. Para cambios sustanciales de contenido, lee el HTML y las referencias aplicables antes de definir el objetivo, lugar en la lección, mensaje, ejemplo, composición y criterios de aceptación. Para cambios solo visuales, conserva el contenido y aplica la ruta de diseño.

Para implementación no trivial de contenido usa el subagente `slide-implementer` si está disponible y permitido, con un único escritor por HTML. Para cambios sustanciales de contenido solicita también una revisión de solo lectura a `slide-reviewer`, proporcionando capturas cuando estén disponibles. No delegues correcciones triviales ni edites en una revisión sin cambios. Si falta delegación, sigue las mismas etapas secuencialmente e informa la limitación.

Revisa el diff, valida y distingue comprobaciones estáticas, de navegador y pedagógicas. No declares una revisión visual aprobada si no pudiste realizarla. Conserva el modelo seleccionado por el usuario.
