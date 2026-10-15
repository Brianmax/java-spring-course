# Comprobar la presentación de POO

La lección es `lessons/java-poo.html`, un archivo HTML independiente que funciona sin conexión. Las herramientas siguientes son de desarrollo; no añaden dependencias a las diapositivas.

## Estructura

Desde la raíz del repositorio:

```sh
python3 .pi/skills/java-course-slide-generator/scripts/validate_deck.py lessons/java-poo.html
```

Una comprobación estática no demuestra que el contenido sea legible ni que los controles funcionen.

## Ejemplos reales de Java

Con JDK 21 o posterior:

```sh
python3 scripts/check_poo_examples.py
```

El script extrae bloques de código del HTML, añade únicamente el contexto de ejecución necesario y compila para Java 21 en un directorio temporal. Comprueba objetos y alias, herencia, sobrescritura, sobrecarga, igualdad, validación del inventario, genéricos, fábrica, estrategia y observadores. No modifica los proyectos del curso. No compila todos los fragmentos ni ejecuta los ejemplos de JUnit.

## Navegador sin conexión

Con Node 22 o posterior y un ejecutable de Chromium:

```sh
node scripts/check_poo_deck.mjs lessons/java-poo.html /ruta/fuera-del-repositorio/evidencia --strict
```

En macOS usa Google Chrome en su ubicación habitual. En otro sistema, establece `CHROME_BIN` con la ruta del ejecutable. Usa un directorio de evidencia nuevo para cada ejecución.

La prueba abre `file://` con la red desactivada, recorre todas las diapositivas a 1366×768, 1280×720 y 390×844, y escribe capturas seleccionadas e incidencias en `report.json`. Verifica navegación, límites, menú, foco de ayuda, hashes, progreso y pantalla completa. `--strict` también falla si detecta desbordamiento de escritorio, código recortado en escritorio o desbordamiento horizontal de la diapositiva en móvil. El desplazamiento dentro de bloques de código y diagramas puede ser necesario en móvil.

El perfil aislado de Chromium queda en el directorio de evidencia para su limpieza posterior. El script desactiva el límite de frecuencia de navegación de ese perfil para recorrer muchas diapositivas; no cambia el navegador habitual del usuario.

## Revisión antes de publicar

- Inspecciona visualmente las capturas; las medidas DOM no certifican legibilidad.
- Comprueba valores, unidades y continuidad entre ejemplos. A-17 es el pedido original de 2 cuadernos de $5; A-18 es la ampliación con varias líneas.
- Distingue declaraciones de clases, imports y fragmentos dentro de `main`.
- Revisa la precisión pedagógica, etiquetas y conectores.
- Repite las comprobaciones afectadas después de corregir hallazgos.
- Construye el sitio desde el commit que se va a publicar, sin incorporar cambios locales de Java no incluidos en él.

Estas comprobaciones no equivalen a una certificación de accesibilidad ni a pruebas de aprendizaje con estudiantes.
