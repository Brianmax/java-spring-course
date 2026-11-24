# cupones-unit-testing

Proyecto didáctico de **pruebas unitarias en Java 21 con Spring Boot 4.1.2 y PostgreSQL**, diseñado para acompañar la lección [Pruebas unitarias en Java](../../lessons/unit-testing.html).

El proyecto incluye **únicamente el código de producción correcto de `TICKET-14`** (sin pruebas unitarias todavía) para que los estudiantes construyan su suite con **JUnit Jupiter y Mockito** y luego implementen **`TICKET-28` a mano** viendo las pruebas unitarias atrapar la regresión en vivo.

## Reglas implementadas (`TICKET-14`)

- **`DESC20`** (activo): otorga **20%** de descuento, requiere un monto mínimo de **S/ 100 inclusive** (`subtotal >= 100`) y aplica un **tope máximo de S/ 50**.
- **`VENCIDO10`** (inactivo): debe rechazarse con `CuponInvalidoException` (`409 Conflict`) sin guardar ni notificar.
- Al arrancar la aplicación contra PostgreSQL se cargan estos datos iniciales (si no existen):
  - Cupones: `DESC20` (activo) y `VENCIDO10` (inactivo).
  - Pedidos: `ORD-205` (`S/ 200`), `ORD-206` (`S/ 400`), `ORD-207` (`S/ 100`) y `ORD-208` (`S/ 80`).

## Estructura del proyecto

```text
cupones-unit-testing/
├── pom.xml                                  # Java 21, Spring Boot 4.1.2, JPA, PostgreSQL, JUnit Jupiter y Mockito
├── docker-compose.yml                       # PostgreSQL 16 para desarrollo local
├── postman/cupones-unit-testing.postman_collection.json  # Colección Postman v2.1 (importable en Bruno)
├── scripts/check_api.sh                     # Verificación automatizada de endpoints REST con curl
└── src/
    ├── main/java/com/tienda/cupones/
    │   ├── domain/                          # Cupon, Pedido, ResultadoDescuento, CalculadoraDescuento y excepciones
    │   ├── port/                            # Interfaces PedidoRepository, CuponRepository, NotificadorCliente
    │   ├── service/                         # AplicarCuponService, PedidoService
    │   ├── persistence/                     # Entidades JPA, adaptadores PostgreSQL y LogNotificadorCliente
    │   ├── config/                          # CuponesConfig (beans y carga de datos demo)
    │   └── web/                             # PedidoController, ApiExceptionHandler y DTOs
    └── test/java/com/tienda/cupones/        # Carpeta lista para que los estudiantes escriban las pruebas unitarias
```

## Prerrequisitos

- **Java 21** (`java -version`)
- **PostgreSQL** (vía Docker con `docker compose up -d` o una instancia local)
- **curl** (para ejecutar `scripts/check_api.sh`)

El proyecto incluye **Maven Wrapper** (`./mvnw` / `mvnw.cmd`), así que no necesitas instalar Maven globalmente.

## Cómo ejecutar el proyecto

### 1. Levantar PostgreSQL

Si no tienes otro PostgreSQL escuchando en el puerto `5432`:

```bash
docker compose up -d
```

Si ya tienes un PostgreSQL local en `5432`, crea la base de datos `cupones` o levanta el contenedor en otro puerto (por ejemplo `5433`):

```bash
POSTGRES_PORT=5433 docker compose up -d
export POSTGRES_URL=jdbc:postgresql://localhost:5433/cupones
```

También puedes ajustar credenciales mediante variables de entorno (`POSTGRES_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD`).

### 2. Arrancar la aplicación Spring Boot

```bash
./mvnw spring-boot:run
```

O empaquetada:

```bash
./mvnw package -DskipTests
java -jar target/cupones-unit-testing-1.0.0-SNAPSHOT.jar
```

### 3. Probar la API manualmente, con Postman/Bruno o con `check_api.sh`

- **Postman / Bruno:** importa [postman/cupones-unit-testing.postman_collection.json](./postman/cupones-unit-testing.postman_collection.json) (en Bruno: *Import Collection → Postman Collection*; usa la variable `baseUrl = http://localhost:8080`).
- **Script automatizado:** con la aplicación en ejecución en otra terminal:

```bash
./scripts/check_api.sh
```

Ejemplo manual (caso `ORD-205` de la diapositiva 2):

```bash
curl -s -X POST http://localhost:8080/api/v1/pedidos/ORD-205/aplicar-cupon \
  -H "Content-Type: application/json" \
  -d '{"codigoCupon":"DESC20"}'
```

Respuesta esperada:

```json
{"pedidoId":"ORD-205","codigoCupon":"DESC20","subtotal":200,"descuento":40,"totalFinal":160}
```

## Dinámica en clase: proteger `TICKET-14` antes de implementar `TICKET-28`

1. **Escribir las pruebas unitarias de `TICKET-14`** en `src/test/java/com/tienda/cupones/`:
   - `CalculadoraDescuentoTest`: casos `ORD-205` (`200 → 40`), `ORD-206` (`400 → 50`), borde `ORD-207` (`100 → 20`) con `@ParameterizedTest`, y rechazo `ORD-208` (`80`) / `99` con `assertThrows`.
   - `AplicarCuponServiceTest`: usar `@ExtendWith(MockitoExtension.class)`, `@Mock` para `PedidoRepository`, `CuponRepository`, `NotificadorCliente`, instancia real de `CalculadoraDescuento`, `when(...)`, `verify(...)`, `verifyNoInteractions(...)` y `ArgumentCaptor<Pedido>`.
   - Ejecutar `./mvnw test` y confirmar que toda la batería está en **verde**.
2. **Implementar `TICKET-28` a mano** (*«envío gratis para compras mayores a S/ 100 y reutilizar la validación de monto mínimo»*):
   - En `CalculadoraDescuento.cumpleMontoMinimo(int subtotal, int minimo)`, cambiar `subtotal >= minimo` por `subtotal > minimo` y usar la condición para activar `pedido.marcarEnvioGratis(...)`.
3. **Ver la red de seguridad en acción:**
   - Si pruebas manualmente `ORD-205` (`S/ 200`) y `ORD-208` (`S/ 80`), ambos siguen respondiendo igual.
   - Al ejecutar `./mvnw test`, la prueba de borde para **`ORD-207` (`S/ 100 → 20`)** falla de inmediato en rojo antes de hacer commit, revelando que el envío gratis (`> 100`) y el mínimo del cupón (`>= 100`) son reglas distintas que no deben compartir la misma condición.

## Endpoints disponibles

| Método | Ruta | Descripción | Códigos esperados |
| --- | --- | --- | --- |
| `GET` | `/api/v1/pedidos` | Lista todos los pedidos | `200` |
| `GET` | `/api/v1/pedidos/{id}` | Consulta un pedido por ID | `200`, `404` |
| `POST` | `/api/v1/pedidos` | Crea un pedido (`{"id":"ORD-300","subtotal":200}`) | `201`, `400`, `409` |
| `POST` | `/api/v1/pedidos/{id}/aplicar-cupon` | Aplica un cupón (`{"codigoCupon":"DESC20"}`) | `200`, `400`, `404`, `409` |
| `GET` | `/api/v1/cupones` | Lista todos los cupones | `200` |
