# Curso de Java · Tecsup

Sitio estático para que los estudiantes consulten las diapositivas y los proyectos del curso.

## Estructura

- `index.html`: página de inicio.
- `lessons/`: catálogo y archivos de las lecciones.
- `projects/`: catálogo de proyectos del curso.
- `assets/site.css`: estilos compartidos de las páginas del sitio.

Las diapositivas de cada lección son archivos HTML independientes y se pueden abrir directamente en un navegador.

## Agregar contenido

Para publicar una lección, agrega su archivo HTML en `lessons/` y enlázalo desde `lessons/index.html` y la página de inicio. Para publicar un proyecto, agrega una página o carpeta dentro de `projects/` y enlázala desde `projects/index.html`.

El sitio no requiere compilación ni dependencias. GitHub Pages puede servir estos archivos estáticos directamente desde la rama `main`.
