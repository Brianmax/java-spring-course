# Curso de Java · Tecsup

Sitio estático para que los estudiantes consulten las diapositivas y los proyectos del curso.

## Estructura

- `index.html`: página de inicio.
- `lessons/`: catálogo y archivos de las lecciones.
- `exercises/`: catálogo por temas con enlaces a los enunciados de [Brianmax/javaG7](https://github.com/Brianmax/javaG7/tree/main/ejercicios).
- `projects/`: catálogo de proyectos del curso.
- `assets/site.css`: estilos compartidos de las páginas del sitio.

Las diapositivas de cada lección son archivos HTML independientes y se pueden abrir directamente en un navegador.

El [temario de Programación Orientada a Objetos en Java](docs/temario-poo-java.md) organiza el aprendizaje en 16 unidades, desde los fundamentos hasta diseño, patrones y pruebas, con ejemplos y ejercicios por nivel.

## Agregar contenido

Para publicar una lección, agrega su archivo HTML en `lessons/` y enlázalo desde `lessons/index.html` y la página de inicio. Para publicar un proyecto, agrega una página o carpeta dentro de `projects/` y enlázala desde `projects/index.html`.

Para agregar una colección de ejercicios, añade una tarjeta en `exercises/index.html` con su tema, descripción y enlace al enunciado original. Los enunciados se consultan directamente en GitHub, por lo que sus actualizaciones no requieren copiar el contenido a este sitio.

Para crear o mejorar una diapositiva con Pi, inicia el flujo con `/crear-diapositiva <tema o cambio>`. El agente principal define primero el objetivo, el lugar de la diapositiva en la lección, el contenido, la composición y cómo evaluar la claridad. Después asigna el HTML a `slide-implementer` y, para cambios sustanciales, solicita a `slide-reviewer` una revisión de solo lectura. En Codex se sigue el mismo proceso con la skill `java-course-slide-generator` y los roles del proyecto en `.codex/agents/`.

El coordinador usa GPT-6 Astra con razonamiento alto en Codex; los roles de implementación y revisión usan GPT-6 Luna. Pi usa el mismo modelo menor para esos roles y requiere confiar el proyecto para cargar sus agentes y extensiones locales. Un solo agente escribe cada presentación HTML para evitar conflictos.

## Publicación

Cada push a `main` ejecuta `.github/workflows/pages.yml`. El workflow usa Python 3 (sin dependencias adicionales) para construir `_site/`, regenerar el código del visor y los ZIP de los proyectos, y desplegar el resultado en GitHub Pages. La fuente de Pages debe estar configurada como **GitHub Actions**.

Para publicar cambios de Java, basta con guardar los archivos fuente, crear un commit y hacer push. Espera a que **Publish course site** finalice y recarga la página. Consulta [la guía de proyectos](projects/README.md) para los comandos de actualización y generación local.
