# pedidos-hexagonal

Proyecto didáctico de **arquitectura hexagonal (puertos y adaptadores)** en **Java 21 y Spring Boot 4.1.2**, organizado con **submódulos Maven** para hacer cumplir físicamente la dirección de dependencias en tiempo de compilación.

Complementa la lección [Arquitectura hexagonal](../../lessons/hexagonal-architecture.html).

## Estructura de submódulos Maven

```text
pedidos-hexagonal/
├── pom.xml                  # POM padre (Java 21, Spring Boot 4.1.2)
├── domain/                  # Java puro: cero dependencias en compilación
├── application/             # Puertos y casos de uso: depende solo de domain
├── infrastructure/          # Adaptadores (Web, @Scheduled, Memoria, JPA, Log, Stripe/Culqi) y arranque Spring Boot
├── docker-compose.yml       # PostgreSQL 16 opcional para probar el perfil postgres
├── scripts/check_api.sh     # Verificación automatizada de endpoints con curl
└── GUIDE.md                 # Guía paso a paso para recrear el proyecto desde cero
```

### Por qué usar 3 submódulos

- `domain` no declara ninguna dependencia de Spring ni JPA: si alguien intenta importar `@Entity` o `@Service`, **Maven detiene la compilación**.
- `application` solo depende de `domain`: los casos de uso y puertos permanecen aislados de HTTP, SQL y SDKs externos.
- `infrastructure` depende de `application` (y transitivamente de `domain`) y aloja todos los adaptadores y `@Configuration`.

## Reglas de negocio implementadas

- Un pedido nuevo inicia en estado `EN_PREPARACION` con `totalSoles > 0` e `id` único.
- Solo se puede cancelar (`PATCH /api/v1/pedidos/{id}/cancelar`) un pedido en `EN_PREPARACION`; si está `ENTREGADO`, `CANCELADO` o `EXPIRADO`, responde `409 Conflict`.
- Si el pedido cancelado tiene `totalSoles >= 100`, invoca `ProcesarReembolsoPort` y marca `reembolsado = true`.
- Solo se puede entregar (`PATCH /api/v1/pedidos/{id}/entregar`) un pedido en `EN_PREPARACION`.
- Un segundo adaptador de entrada (`PedidosExpiradosJob`, `@Scheduled`) expira pedidos en `EN_PREPARACION` con más de 24 horas usando `ExpirarPedidosVencidosUseCase` sin pasar por HTTP.

## Prerrequisitos

- **Java 21** (`java -version`)
- **curl** (para `scripts/check_api.sh`)
- **Docker** o **PostgreSQL local** (solo si quieres ejecutar el perfil `postgres`; el perfil por defecto `inmemory` no los necesita)

El proyecto incluye Maven Wrapper (`./mvnw` / `mvnw.cmd`), por lo que no hace falta tener Maven instalado globalmente.

## Comandos rápidos

### 1. Ejecutar todas las pruebas (núcleo + infraestructura)

```bash
./mvnw test
```

- `domain`: pruebas de reglas puras en milisegundos (sin Spring).
- `application`: pruebas de casos de uso con dobles en memoria (sin Spring ni BD).
- `infrastructure`: pruebas del mapper JPA y pruebas HTTP con `MockMvc`.

### 2. Empaquetar y arrancar en modo en memoria (cero Docker / cero BD)

```bash
./mvnw package -DskipTests
java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
```

Al iniciar con el perfil por defecto (`inmemory`), se cargan dos pedidos de demostración:
- `ORD-101` (`S/ 120`, `EN_PREPARACION`)
- `ORD-102` (`S/ 85`, `ENTREGADO`)

### 3. Probar el intercambio de adaptadores en vivo con perfiles de Spring

- **Cambiar pasarela de reembolso de Stripe a Culqi** (sin editar `domain` ni `application`):
  ```bash
  java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=inmemory,culqi
  ```
- **Cambiar persistencia de memoria a PostgreSQL (JPA):**
  ```bash
  docker compose up -d
  java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres
  ```
- **Combinar PostgreSQL + Culqi:**
  ```bash
  java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres,culqi
  ```

### 4. Verificar los endpoints REST

Con la aplicación en ejecución en otra terminal:

```bash
./scripts/check_api.sh
```

También puedes indicar otra URL base:

```bash
./scripts/check_api.sh http://localhost:8080
```

## Endpoints disponibles

| Método | Ruta | Descripción | Códigos esperados |
| --- | --- | --- | --- |
| `POST` | `/api/v1/pedidos` | Crea un pedido (`{"id":"ORD-200","totalSoles":120}`) | `201`, `400`, `409` |
| `GET` | `/api/v1/pedidos` | Lista todos los pedidos | `200` |
| `GET` | `/api/v1/pedidos/{id}` | Consulta un pedido por ID | `200`, `404` |
| `PATCH` | `/api/v1/pedidos/{id}/cancelar` | Cancela pedido, reembolsa si `>= 100` y publica evento | `200`, `404`, `409` |
| `PATCH` | `/api/v1/pedidos/{id}/entregar` | Marca pedido como `ENTREGADO` | `200`, `404`, `409` |

## Recreación paso a paso

Consulta [GUIDE.md](./GUIDE.md) para construir este proyecto desde cero paso a paso.
