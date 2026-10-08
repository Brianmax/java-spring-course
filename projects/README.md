# Biblioteca de proyectos

Cada proyecto vive en su propia carpeta y tiene una página de código y un ZIP independiente.

## Actualizar el código publicado

Edita los archivos originales dentro de `src/`, crea un commit y súbelo a `main`:

```sh
git add projects/java-g19-intro/src/
git commit -m "Actualizar ejercicios de Java"
git push origin main
```

El workflow **Publish course site** regenera el visor y el ZIP a partir del código de ese commit y publica el sitio. Espera a que termine en la pestaña Actions y recarga la página. No necesitas regenerar ni subir los archivos derivados para publicar cambios de código.

Para actualizar también la copia local del visor y del ZIP, ejecuta desde la raíz:

```sh
python3 scripts/package_projects.py java-g19-intro
```

El comando actualiza `project-data.js` (código que muestra el visor) y `java-g19-intro.zip` (descarga del proyecto). El despliegue siempre regenera estos archivos, incluso si las copias guardadas en Git son anteriores.

El ZIP contiene `src/**/*.java`, `README.md`, `.gitignore`, los módulos `.iml` y la configuración portátil de IntelliJ. Excluye archivos compilados, sesiones del IDE y recursos del sitio.

Para detectar recursos locales pendientes de regenerar:

```sh
python3 scripts/package_projects.py --check
```

Para generar la misma carpeta que se publica en Pages, ejecuta `python3 scripts/build_site.py`. El resultado queda en `_site/`; usa los archivos del sitio registrados en Git y genera los recursos de los proyectos desde sus fuentes actuales. Los enlaces al índice de código incluyen una versión basada en su contenido para evitar reutilizar una copia anterior del navegador.

## Agregar otro proyecto sencillo de Java

1. Crea `projects/<nombre>/` con `src/` y `README.md`; agrega los archivos de IntelliJ si los necesita.
2. Copia el `index.html` de un proyecto existente y actualiza título, descripción, instrucciones, enlaces de descarga y `data-default-file`.
3. Ejecuta `python3 scripts/package_projects.py <nombre>` para generar el índice y el ZIP.
4. Agrega una tarjeta en `projects/index.html` que enlace al `index.html` del nuevo proyecto.

El visor comparte `assets/project-code.css` y `assets/project-code.js`. Los enlaces con `#file=src%2Farrays%2FTeoria.java&line=5` abren un archivo y resaltan una línea. No necesita servicios externos para mostrar el código.

El empaquetador está pensado para estos proyectos sencillos. Si agregas recursos, dependencias o un proyecto Maven/Gradle, amplía primero la lista de archivos incluidos para que la descarga esté completa.
