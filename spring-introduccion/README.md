# Introducción a Spring Boot

Proyecto inicial con Spring Boot, Maven y Java 21. Incluye una aplicación y controladores REST sencillos para probar rutas HTTP `GET`.

## Ejecutar

Necesitas Java 21. Desde esta carpeta, inicia la aplicación con el Maven Wrapper:

```sh
./mvnw spring-boot:run
```

En Windows, usa `mvnw.cmd spring-boot:run`. Spring Boot inicia el servidor en `http://localhost:8080`.

## Rutas de ejemplo

- `GET /hola` y `GET /adios` — respuestas sencillas de `BasicController`.
- `GET /api/productos/findAll` — respuesta de ejemplo del controlador de productos.
- `GET /api/usuarios/findAll` y `GET /api/usuarios/delete` — respuestas de ejemplo del controlador de usuarios.

Los métodos devuelven texto de muestra; todavía no se conectan a una base de datos.
