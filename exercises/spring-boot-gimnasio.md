# Práctica de Spring Boot: Gimnasio — Clases y Reservas

Construye una API REST con **Spring Boot, Spring Data JPA y PostgreSQL** para administrar los socios de un gimnasio y sus reservas de clases grupales. El ejercicio aplica la misma arquitectura por capas del proyecto de clase (`spring-introduccion`) en un dominio nuevo.

## Lo que vas a practicar

- Arquitectura en capas: `controller`, `service`, `repository`, `entity` y `dto`.
- Endpoints REST con `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PatchMapping`, `@PathVariable`, `@RequestParam` y `@RequestBody`.
- Respuestas HTTP con `ResponseEntity` (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`).
- Persistencia en PostgreSQL con entidades JPA, claves `UUID` y relaciones `@ManyToOne` / `@OneToMany`.
- Consultas con Spring Data JPA (`existsBy...`, `findById`, `findAll` y consultas personalizadas con `@Query`).
- Reglas de negocio en la capa de servicio: validaciones, unicidad, límite de reservas activas y actualización de estado.
- **Desafío opcional:** relación `@ManyToMany` con `@JoinTable`.

---

## Configuración inicial

1. Crea un proyecto Spring Boot con **Java 21** y **Maven** (puedes usar Spring Initializr o tomar como referencia `spring-introduccion`).
2. Agrega las dependencias:
   - `spring-boot-starter-webmvc` (o `spring-boot-starter-web` según versión de Spring Boot)
   - `spring-boot-starter-data-jpa`
   - `postgresql`
   - `lombok`
3. Crea una base de datos llamada `gimnasio` en PostgreSQL y configura `src/main/resources/application.properties`:

```properties
spring.application.name=gimnasio

spring.datasource.url=jdbc:postgresql://localhost:5432/gimnasio
spring.datasource.username=postgres
spring.datasource.password=admin

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update
```

Organiza tus clases en los paquetes `controller`, `service`, `repository`, `entity` y `dto`. No devuelvas entidades directamente desde los controladores; usa siempre DTOs de entrada (`*Request`) y salida (`*Response`).

---

## Modelo de datos (Entidades JPA)

### `SocioEntity` (tabla `socio`)

Representa a un miembro registrado en el gimnasio.

| Atributo | Tipo | Restricciones / Columna |
| --- | --- | --- |
| `id` | `UUID` | `@Id`, `@GeneratedValue(strategy = GenerationType.UUID)` |
| `dni` | `String` | `nullable = false`, `unique = true`, `length = 20` |
| `email` | `String` | `nullable = false`, `unique = true`, `length = 100` |
| `nombre` | `String` | `nullable = false`, `length = 50` |
| `apellido` | `String` | `nullable = false`, `length = 50` |
| `edad` | `Integer` | Opcional (puede ser `null`) |
| `reservasActivas` | `int` | Columna `reservas_activas`, valor inicial `0` |
| `reservaEntities` | `List<ReservaEntity>` | `@OneToMany(mappedBy = "socioEntity", fetch = FetchType.LAZY)` |

### `ReservaEntity` (tabla `reserva`)

Representa la reserva de un socio para una clase grupal (por ejemplo, Spinning, Funcional o Yoga).

| Atributo | Tipo | Restricciones / Columna |
| --- | --- | --- |
| `id` | `UUID` | `@Id`, `@GeneratedValue(strategy = GenerationType.UUID)` |
| `nombreClase` | `String` | Columna `nombre_clase`, `nullable = false` |
| `horario` | `String` | `nullable = false` (por ejemplo, `"08:00"`, `"18:30"`) |
| `fechaCreacion` | `Date` (`java.sql.Date`) | Columna `fecha_creacion`, valor inicial `new Date(System.currentTimeMillis())` |
| `fechaClase` | `Date` (`java.sql.Date`) | Columna `fecha_clase`, `nullable = false` |
| `asistio` | `boolean` | Valor inicial `false` |
| `socioEntity` | `SocioEntity` | `@ManyToOne`, `@JoinColumn(name = "socio_id")` |

---

## DTOs sugeridos

- `SocioRequest`: `dni`, `email`, `nombre`, `apellido`, `edad`.
- `SocioResponse`: `id`, `dni`, `email`, `nombre`, `apellido`, `edad`, `reservasActivas`.
- `ReservaRequest`: `nombreClase`, `horario`, `fechaClase`, `socioId`.
- `ReservaResponse`: `id`, `nombreClase`, `horario`, `fechaClase`, `asistio`, `socioId`.

---

## Endpoints y reglas de negocio

### 1. Socios (`/api/v1/socio`)

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `POST` | `/api/v1/socio/save` | Registra un nuevo socio | `201 Created`, `400 Bad Request` |

**Reglas de validación en `SocioService`:**
- `dni`, `email`, `nombre` y `apellido` son obligatorios y no pueden estar vacíos o solo con espacios.
- `email` debe contener `@`.
- `edad` es opcional, pero si se envía debe ser mayor o igual a **14**.
- Limpia espacios con `.trim()` y normaliza `email` a minúsculas con `.toLowerCase()`.
- No puede existir otro socio con el mismo `dni` ni con el mismo `email` (usa `existsByDni` y `existsByEmail` en `SocioRepository`).

### 2. Reservas (`/api/v1/reserva`)

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| `POST` | `/api/v1/reserva/save` | Crea una reserva para un socio | `201 Created`, `400 Bad Request` |
| `GET` | `/api/v1/reserva/find` | Lista todas las reservas | `200 OK` |
| `GET` | `/api/v1/reserva/find/{id}` | Busca una reserva por su `UUID` | `200 OK`, `404 Not Found` |
| `GET` | `/api/v1/reserva/find-reservas-socio/{idSocio}` | Lista las reservas de un socio | `200 OK`, `404 Not Found` (o `400`) |
| `PATCH` | `/api/v1/reserva/confirmar-asistencia/{id}` | Confirma asistencia y libera un cupo activo | `200 OK`, `400 Bad Request`, `404 Not Found` |
| `PATCH` | `/api/v1/reserva/cambiar-clase/{id}?nuevoNombre=...` | Cambia el nombre de la clase de una reserva | `200 OK`, `400 Bad Request`, `404 Not Found` |

**Reglas de negocio en `ReservaService`:**

1. **Crear reserva (`saveReserva`):**
   - `nombreClase` no puede ser nulo ni vacío y solo debe contener letras, números y espacios (`^[a-zA-Z0-9 ]+$`).
   - `horario` no puede ser nulo ni vacío.
   - `fechaClase` no puede ser nula y debe ser **mayor o igual** a la fecha actual (`fechaCreacion`).
   - El socio (`socioId`) debe existir en la base de datos.
   - Un socio puede tener como máximo **4 reservas activas** (`reservasActivas >= 4` se rechaza con `400 Bad Request`).
   - Al guardar la reserva, incrementa `reservasActivas` del socio en `+1` y guarda el cambio en `SocioRepository`.
2. **Buscar por ID (`findById`):**
   - Si no existe ninguna reserva con ese `UUID`, responde `404 Not Found`.
3. **Listar reservas por socio (`findReservasSocio`):**
   - Verifica primero que el socio exista (`existsById`).
   - Agrega en `ReservaRepository` una consulta para traer las reservas por `socio_id` (por ejemplo, `@Query(nativeQuery = true, value = "select * from reserva where socio_id = :idSocio")`).
4. **Confirmar asistencia (`confirmarAsistencia`):**
   - Si la reserva no existe, responde `404 Not Found`.
   - Si la reserva **ya tiene** `asistio == true`, rechaza la operación (`400 Bad Request`) para no descontar dos veces `reservasActivas`.
   - Marca `asistio = true`, reduce en `1` las `reservasActivas` del socio asociado (sin permitir valores negativos) y guarda ambas entidades.
5. **Cambiar clase (`cambiarClase`):**
   - Valida que `nuevoNombre` no sea nulo y cumpla `^[a-zA-Z0-9 ]+$`. Si no cumple, responde `400 Bad Request`.
   - Si la reserva no existe, responde `404 Not Found`.
   - Actualiza `nombreClase`, guarda y devuelve el `ReservaResponse` actualizado.

---

## Ejemplos de peticiones JSON

### Registrar un socio (`POST /api/v1/socio/save`)

```json
{
  "dni": "72458910",
  "email": "camila.torres@correo.com",
  "nombre": "Camila",
  "apellido": "Torres",
  "edad": 24
}
```

Respuesta (`201 Created`):

```json
{
  "id": "c7c9a8d2-4d34-4b4e-8f52-91b7c54f1a20",
  "dni": "72458910",
  "email": "camila.torres@correo.com",
  "nombre": "Camila",
  "apellido": "Torres",
  "edad": 24,
  "reservasActivas": 0
}
```

### Crear una reserva (`POST /api/v1/reserva/save`)

```json
{
  "nombreClase": "Spinning Intermedio",
  "horario": "18:30",
  "fechaClase": "2026-12-10",
  "socioId": "c7c9a8d2-4d34-4b4e-8f52-91b7c54f1a20"
}
```

Respuesta (`201 Created`):

```json
{
  "id": "8a41f0b3-1c90-4a12-9b88-34e2a1d90f11",
  "nombreClase": "Spinning Intermedio",
  "horario": "18:30",
  "fechaClase": "2026-12-10",
  "asistio": false,
  "socioId": "c7c9a8d2-4d34-4b4e-8f52-91b7c54f1a20"
}
```

### Cambiar el nombre de la clase

```http
PATCH /api/v1/reserva/cambiar-clase/8a41f0b3-1c90-4a12-9b88-34e2a1d90f11?nuevoNombre=Yoga%20Matutino
```

---

## Comprueba tu solución

Prueba cada escenario con Postman, Bruno o `curl` antes de entregar:

1. **Registro de socios:** registra un socio válido (`201`). Intenta registrar otro con el mismo DNI o email (`400`). Prueba con `edad: 12` (`400`) y sin enviar `edad` (`201`).
2. **Validación de reservas:** intenta crear una reserva con `nombreClase: "CrossFit!!!"` (`400`), con una fecha anterior a hoy (`400`) o con un `socioId` inexistente (`400`).
3. **Límite de 4 reservas activas:** crea 4 reservas válidas para el mismo socio (`201` en cada una; `reservas_activas` debe quedar en `4`). Al intentar crear la quinta reserva, la API debe responder `400`.
4. **Confirmación de asistencia:** llama a `PATCH /api/v1/reserva/confirmar-asistencia/{id}` sobre una de las 4 reservas. Debe responder `200` con `asistio: true` y bajar `reservas_activas` a `3`. Si vuelves a llamar al mismo endpoint con el mismo `id`, no debe volver a restar. Después, crea una nueva reserva para ese socio y verifica que ahora sí devuelve `201`.
5. **Consultas:** verifica `GET /api/v1/reserva/find`, `GET /api/v1/reserva/find/{id}` (con un ID válido y otro inexistente) y `GET /api/v1/reserva/find-reservas-socio/{idSocio}`.
6. **Cambio de clase:** actualiza el nombre con `PATCH /api/v1/reserva/cambiar-clase/{id}?nuevoNombre=Pilates` (`200`) y prueba un nombre con símbolos especiales (`400`).

---

## Desafío opcional: beneficios del gimnasio (`@ManyToMany`)

Si terminaste la parte principal, amplía el modelo con una relación muchos a muchos similar a `RoleEntity` en el proyecto de clase:

1. Crea `BeneficioEntity` (tabla `beneficio`) con `id: UUID`, `nombre: String` (único, por ejemplo `SAUNA`, `ESTACIONAMIENTO`, `NUTRICIONISTA`) y `descripcion: String`.
2. Relaciona `BeneficioEntity` y `SocioEntity` con `@ManyToMany` usando una tabla intermedia `socio_beneficio` (`beneficio_id`, `socio_id`).
3. Agrega:
   - `POST /api/v1/beneficio/save` para registrar beneficios.
   - `POST /api/v1/socio/{idSocio}/beneficios/{idBeneficio}` para asignar un beneficio a un socio (sin duplicar la asignación si ya lo tiene).
   - `GET /api/v1/socio/{idSocio}/beneficios` para listar los beneficios asignados a un socio.
