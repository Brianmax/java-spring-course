# Biblioteca de proyectos

Cada proyecto vive en su propia carpeta y tiene una página de código y un ZIP independiente.

## Actualizar el código publicado

Edita los archivos originales dentro de `src/` y regenera los recursos desde la raíz del repositorio:

```sh
python3 scripts/package_projects.py java-g19-intro
```

El comando actualiza `project-data.js` (código que muestra el visor) y `java-g19-intro.zip` (descarga del proyecto). Incluye ambos archivos generados en el mismo commit que los cambios al código.

El ZIP contiene `src/**/*.java`, `README.md`, `.gitignore`, los módulos `.iml` y la configuración portátil de IntelliJ. Excluye archivos compilados, sesiones del IDE y recursos del sitio.

Para detectar recursos pendientes de regenerar:

```sh
python3 scripts/package_projects.py --check
```

## Agregar otro proyecto sencillo de Java

1. Crea `projects/<nombre>/` con `src/` y `README.md`; agrega los archivos de IntelliJ si los necesita.
2. Copia el `index.html` de un proyecto existente y actualiza título, descripción, instrucciones, enlaces de descarga y `data-default-file`.
3. Ejecuta `python3 scripts/package_projects.py <nombre>` para generar el índice y el ZIP.
4. Agrega una tarjeta en `projects/index.html` que enlace al `index.html` del nuevo proyecto.

El visor comparte `assets/project-code.css` y `assets/project-code.js`. Los enlaces con `#file=src%2Farrays%2FTeoria.java&line=5` abren un archivo y resaltan una línea. No necesita servicios externos para mostrar el código.

El empaquetador está pensado para estos proyectos sencillos. Si agregas recursos, dependencias o un proyecto Maven/Gradle, amplía primero la lista de archivos incluidos para que la descarga esté completa.
