# Guía paso a paso: `pedidos-hexagonal` con submódulos Maven

Esta guía permite recrear desde cero un proyecto completo y ejecutable de **arquitectura hexagonal (puertos y adaptadores)** en **Java 21** y **Spring Boot 4.1.2**, dividido en tres submódulos Maven (`domain`, `application` e `infrastructure`).

## Qué aprenderás al construirlo

1. Usar **submódulos Maven** para impedir en compilación que el núcleo importe Spring, JPA o SDKs externos.
2. Separar el **Model de dominio** (`Pedido`) de la **Entity JPA** (`PedidoEntity`) y proteger reglas sin `setters` públicos.
3. Definir **puertos de entrada y salida** para conectar múltiples canales (HTTP REST y `@Scheduled`) y múltiples proveedores (Memoria ↔ PostgreSQL/JPA, Stripe ↔ Culqi, eventos por log).
4. Probar las reglas de negocio en milisegundos con **dobles en memoria** y verificar la API con un script `curl`.

---

## Prerrequisitos y entorno

- **Java 21** (`java -version` debe mostrar `21.x`).
- **Maven 3.9+** (`mvn -version`) o **Maven Wrapper** (`./mvnw` / `mvnw.cmd`).
- **curl** para ejecutar el script de verificación HTTP.
- **Opcional:** **Docker** o **PostgreSQL 16** local si deseas probar el perfil `postgres`.

> **Nota sobre Maven:** En los comandos de esta guía usamos `./mvnw` (Maven Wrapper). Si empiezas desde una carpeta vacía y tienes Maven instalado globalmente, usa `mvn` o genera el wrapper con `mvn wrapper:wrapper -Dmaven=3.10.0`.

---

## Hito 1 · Crear el proyecto multimódulo Maven

**Qué enseña este paso:** la arquitectura no depende solo de carpetas; declarar dependencias explícitas en cada `pom.xml` garantiza que `domain` y `application` no puedan acoplarse a Spring ni a JPA.

### 1.1 Crear la estructura base

```bash
mkdir -p pedidos-hexagonal/{domain,application,infrastructure,scripts}
cd pedidos-hexagonal
```

### 1.2 Crear `.gitignore`

Archivo: `.gitignore`

```gitignore
target/
**/target/
.idea/
*.iml
.DS_Store
*.log
```

### 1.3 Crear el `pom.xml` padre

Archivo: `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.1.2</version>
        <relativePath/>
    </parent>

    <groupId>com.tienda</groupId>
    <artifactId>pedidos-hexagonal</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>pedidos-hexagonal</name>
    <description>Proyecto didáctico de arquitectura hexagonal en Java 21 y Spring Boot con submódulos Maven</description>

    <modules>
        <module>domain</module>
        <module>application</module>
        <module>infrastructure</module>
    </modules>

    <properties>
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.tienda</groupId>
                <artifactId>domain</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.tienda</groupId>
                <artifactId>application</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### 1.4 Crear los `pom.xml` de los tres submódulos

Archivo: `domain/pom.xml` *(cero dependencias de compilación)*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.tienda</groupId>
        <artifactId>pedidos-hexagonal</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>domain</artifactId>
    <name>pedidos-hexagonal :: domain</name>
    <description>Módulo de dominio en Java puro: sin dependencias de Spring, JPA ni infraestructura</description>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Archivo: `application/pom.xml` *(depende solo de `domain`)*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.tienda</groupId>
        <artifactId>pedidos-hexagonal</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>application</artifactId>
    <name>pedidos-hexagonal :: application</name>
    <description>Puertos de entrada/salida y casos de uso: depende únicamente del módulo domain</description>

    <dependencies>
        <dependency>
            <groupId>com.tienda</groupId>
            <artifactId>domain</artifactId>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Archivo: `infrastructure/pom.xml` *(Spring Boot Web, JPA, PostgreSQL y pruebas)*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.tienda</groupId>
        <artifactId>pedidos-hexagonal</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>infrastructure</artifactId>
    <name>pedidos-hexagonal :: infrastructure</name>
    <description>Adaptadores de entrada y salida, configuración Spring Boot y punto de arranque</description>

    <dependencies>
        <dependency>
            <groupId>com.tienda</groupId>
            <artifactId>application</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

Si usas Maven instalado globalmente y deseas generar el Maven Wrapper (`./mvnw`), hazlo ahora que `pom.xml` ya existe:

```bash
mvn wrapper:wrapper -Dmaven=3.10.0
```

---

## Hito 2 · Implementar y probar el módulo `domain` (Java puro)

**Qué enseña este paso:** las reglas de negocio viven dentro del modelo `Pedido` sin anotaciones de persistencia ni `setters` abiertos.

Crea las carpetas:

```bash
mkdir -p domain/src/main/java/com/tienda/pedidos/domain/{model,exception}
mkdir -p domain/src/test/java/com/tienda/pedidos/domain/model
```

### 2.1 `EstadoPedido.java`

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/model/EstadoPedido.java`

```java
package com.tienda.pedidos.domain.model;

public enum EstadoPedido {
    EN_PREPARACION,
    ENTREGADO,
    CANCELADO,
    EXPIRADO
}
```

### 2.2 Excepciones de dominio

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/exception/PedidoInvalidoException.java`

```java
package com.tienda.pedidos.domain.exception;

public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String message) {
        super(message);
    }
}
```

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/exception/PedidoNoCancelableException.java`

```java
package com.tienda.pedidos.domain.exception;

import com.tienda.pedidos.domain.model.EstadoPedido;

public class PedidoNoCancelableException extends RuntimeException {

    private final String pedidoId;
    private final EstadoPedido estadoActual;

    public PedidoNoCancelableException(String pedidoId, EstadoPedido estadoActual) {
        super("El pedido " + pedidoId + " no puede cancelarse porque está en estado " + estadoActual);
        this.pedidoId = pedidoId;
        this.estadoActual = estadoActual;
    }

    public String pedidoId() {
        return pedidoId;
    }

    public EstadoPedido estadoActual() {
        return estadoActual;
    }
}
```

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/exception/PedidoEstadoInvalidoException.java`

```java
package com.tienda.pedidos.domain.exception;

import com.tienda.pedidos.domain.model.EstadoPedido;

public class PedidoEstadoInvalidoException extends RuntimeException {

    private final String pedidoId;
    private final EstadoPedido estadoActual;

    public PedidoEstadoInvalidoException(String pedidoId, EstadoPedido estadoActual, String operacion) {
        super("No se puede " + operacion + " el pedido " + pedidoId + " en estado " + estadoActual);
        this.pedidoId = pedidoId;
        this.estadoActual = estadoActual;
    }

    public String pedidoId() {
        return pedidoId;
    }

    public EstadoPedido estadoActual() {
        return estadoActual;
    }
}
```

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/exception/PedidoDuplicadoException.java`

```java
package com.tienda.pedidos.domain.exception;

public class PedidoDuplicadoException extends RuntimeException {

    private final String pedidoId;

    public PedidoDuplicadoException(String pedidoId) {
        super("Ya existe un pedido registrado con el identificador " + pedidoId);
        this.pedidoId = pedidoId;
    }

    public String pedidoId() {
        return pedidoId;
    }
}
```

### 2.3 Modelo `Pedido.java`

Archivo: `domain/src/main/java/com/tienda/pedidos/domain/model/Pedido.java`

```java
package com.tienda.pedidos.domain.model;

import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;

import java.time.Instant;
import java.util.Objects;

public class Pedido {

    public static final int MONTO_MINIMO_REEMBOLSO_SOLES = 100;

    private final String id;
    private final int totalSoles;
    private final Instant creadoEn;
    private EstadoPedido estado;
    private boolean reembolsado;

    private Pedido(String id, int totalSoles, EstadoPedido estado, Instant creadoEn, boolean reembolsado) {
        if (id == null || id.isBlank()) {
            throw new PedidoInvalidoException("El identificador del pedido es obligatorio");
        }
        if (totalSoles <= 0) {
            throw new PedidoInvalidoException("El total del pedido debe ser mayor que cero");
        }
        if (estado == null) {
            throw new PedidoInvalidoException("El estado del pedido es obligatorio");
        }
        if (creadoEn == null) {
            throw new PedidoInvalidoException("La fecha de creación es obligatoria");
        }
        this.id = id.trim();
        this.totalSoles = totalSoles;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.reembolsado = reembolsado;
    }

    public static Pedido crear(String id, int totalSoles, Instant creadoEn) {
        return new Pedido(id, totalSoles, EstadoPedido.EN_PREPARACION, creadoEn, false);
    }

    public static Pedido reconstituir(
            String id,
            int totalSoles,
            EstadoPedido estado,
            Instant creadoEn,
            boolean reembolsado
    ) {
        return new Pedido(id, totalSoles, estado, creadoEn, reembolsado);
    }

    public void cancelar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoNoCancelableException(this.id, this.estado);
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    public void entregar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "entregar");
        }
        this.estado = EstadoPedido.ENTREGADO;
    }

    public void expirar() {
        if (this.estado != EstadoPedido.EN_PREPARACION) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "expirar");
        }
        this.estado = EstadoPedido.EXPIRADO;
    }

    public boolean requiereReembolso() {
        return this.totalSoles >= MONTO_MINIMO_REEMBOLSO_SOLES;
    }

    public void marcarReembolsado() {
        if (this.estado != EstadoPedido.CANCELADO) {
            throw new PedidoEstadoInvalidoException(this.id, this.estado, "reembolsar");
        }
        this.reembolsado = true;
    }

    public String id() {
        return id;
    }

    public int totalSoles() {
        return totalSoles;
    }

    public EstadoPedido estado() {
        return estado;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public boolean reembolsado() {
        return reembolsado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido other)) return false;
        return totalSoles == other.totalSoles
                && reembolsado == other.reembolsado
                && Objects.equals(id, other.id)
                && estado == other.estado
                && Objects.equals(creadoEn, other.creadoEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalSoles, estado, creadoEn, reembolsado);
    }
}
```

### 2.4 Pruebas unitarias de `domain`

Archivo: `domain/src/test/java/com/tienda/pedidos/domain/model/PedidoTest.java`

```java
package com.tienda.pedidos.domain.model;

import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private static final Instant FECHA_BASE = Instant.parse("2026-11-21T15:00:00Z");

    @Test
    void crearPedidoIniciaEnPreparacionYSinReembolso() {
        Pedido pedido = Pedido.crear("ORD-101", 120, FECHA_BASE);

        assertEquals("ORD-101", pedido.id());
        assertEquals(120, pedido.totalSoles());
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.estado());
        assertFalse(pedido.reembolsado());
        assertTrue(pedido.requiereReembolso());
    }

    @Test
    void cancelarPedidoEnPreparacionCambiaEstadoACancelado() {
        Pedido pedido = Pedido.crear("ORD-101", 120, FECHA_BASE);

        pedido.cancelar();

        assertEquals(EstadoPedido.CANCELADO, pedido.estado());
    }

    @Test
    void cancelarPedidoEntregadoLanzaExcepcionYNoCambiaEstado() {
        Pedido pedido = Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, FECHA_BASE, false);

        PedidoNoCancelableException ex = assertThrows(PedidoNoCancelableException.class, pedido::cancelar);

        assertEquals("ORD-102", ex.pedidoId());
        assertEquals(EstadoPedido.ENTREGADO, ex.estadoActual());
        assertEquals(EstadoPedido.ENTREGADO, pedido.estado());
    }

    @Test
    void pedidoMenorAlUmbralNoRequiereReembolso() {
        Pedido pedido = Pedido.crear("ORD-103", 50, FECHA_BASE);

        assertFalse(pedido.requiereReembolso());
    }

    @Test
    void entregarYExpirarSoloSePermitenEnPreparacion() {
        Pedido paraEntregar = Pedido.crear("ORD-104", 90, FECHA_BASE);
        paraEntregar.entregar();
        assertEquals(EstadoPedido.ENTREGADO, paraEntregar.estado());
        assertThrows(PedidoEstadoInvalidoException.class, paraEntregar::expirar);

        Pedido paraExpirar = Pedido.crear("ORD-105", 90, FECHA_BASE);
        paraExpirar.expirar();
        assertEquals(EstadoPedido.EXPIRADO, paraExpirar.estado());
        assertThrows(PedidoEstadoInvalidoException.class, paraExpirar::entregar);
    }

    @Test
    void rechazaDatosInvalidosAlCrear() {
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("   ", 100, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", 0, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", -15, FECHA_BASE));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.crear("ORD-106", 100, null));
    }
}
```

### Checkpoint 1

```bash
./mvnw -pl domain test
```

**Resultado esperado:** `Tests run: 6, Failures: 0, Errors: 0` en milisegundos, sin arrancar Spring ni bases de datos.

---

## Hito 3 · Implementar puertos, casos de uso y pruebas del módulo `application`

**Qué enseña este paso:** la aplicación coordina los casos de uso contra interfaces (puertos) sin importar ninguna tecnología externa, lo que permite probar todo con dobles en memoria.

Crea las carpetas:

```bash
mkdir -p application/src/main/java/com/tienda/pedidos/application/{port/in,port/out,service}
mkdir -p application/src/test/java/com/tienda/pedidos/application/{fake,service}
```

### 3.1 Puertos de entrada (`application/port/in`)

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/in/CrearPedidoUseCase.java`

```java
package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

public interface CrearPedidoUseCase {

    Pedido crear(String id, int totalSoles);
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/in/ConsultarPedidoUseCase.java`

```java
package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface ConsultarPedidoUseCase {

    Optional<Pedido> buscarPorId(String id);

    List<Pedido> listarTodos();
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/in/CancelarPedidoUseCase.java`

```java
package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

import java.util.Optional;

public interface CancelarPedidoUseCase {

    Optional<Pedido> cancelar(String id);
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/in/EntregarPedidoUseCase.java`

```java
package com.tienda.pedidos.application.port.in;

import com.tienda.pedidos.domain.model.Pedido;

import java.util.Optional;

public interface EntregarPedidoUseCase {

    Optional<Pedido> entregar(String id);
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/in/ExpirarPedidosVencidosUseCase.java`

```java
package com.tienda.pedidos.application.port.in;

import java.time.Instant;

public interface ExpirarPedidosVencidosUseCase {

    int expirarAnterioresA(Instant fechaLimite);
}
```

### 3.2 Puertos de salida (`application/port/out`)

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/out/PedidoRepositoryPort.java`

```java
package com.tienda.pedidos.application.port.out;

import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {

    Optional<Pedido> buscarPorId(String id);

    List<Pedido> listarTodos();

    List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite);

    boolean existePorId(String id);

    Pedido guardar(Pedido pedido);
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/out/NotificarEventoPedidoPort.java`

```java
package com.tienda.pedidos.application.port.out;

import com.tienda.pedidos.domain.model.Pedido;

public interface NotificarEventoPedidoPort {

    void notificarCancelacion(Pedido pedido);

    void notificarExpiracion(Pedido pedido);
}
```

Archivo: `application/src/main/java/com/tienda/pedidos/application/port/out/ProcesarReembolsoPort.java`

```java
package com.tienda.pedidos.application.port.out;

public interface ProcesarReembolsoPort {

    void reembolsar(String pedidoId, int montoSoles);
}
```

### 3.3 Servicio de aplicación (`PedidoUseCaseService.java`)

Archivo: `application/src/main/java/com/tienda/pedidos/application/service/PedidoUseCaseService.java`

```java
package com.tienda.pedidos.application.service;

import com.tienda.pedidos.application.port.in.CancelarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ConsultarPedidoUseCase;
import com.tienda.pedidos.application.port.in.CrearPedidoUseCase;
import com.tienda.pedidos.application.port.in.EntregarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ExpirarPedidosVencidosUseCase;
import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.model.Pedido;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PedidoUseCaseService implements
        CrearPedidoUseCase,
        ConsultarPedidoUseCase,
        CancelarPedidoUseCase,
        EntregarPedidoUseCase,
        ExpirarPedidosVencidosUseCase {

    private final PedidoRepositoryPort pedidos;
    private final NotificarEventoPedidoPort notificador;
    private final ProcesarReembolsoPort reembolsos;
    private final Clock clock;

    public PedidoUseCaseService(
            PedidoRepositoryPort pedidos,
            NotificarEventoPedidoPort notificador,
            ProcesarReembolsoPort reembolsos,
            Clock clock
    ) {
        this.pedidos = Objects.requireNonNull(pedidos, "pedidos es obligatorio");
        this.notificador = Objects.requireNonNull(notificador, "notificador es obligatorio");
        this.reembolsos = Objects.requireNonNull(reembolsos, "reembolsos es obligatorio");
        this.clock = Objects.requireNonNull(clock, "clock es obligatorio");
    }

    @Override
    public Pedido crear(String id, int totalSoles) {
        Pedido nuevo = Pedido.crear(id, totalSoles, Instant.now(clock));
        if (pedidos.existePorId(nuevo.id())) {
            throw new PedidoDuplicadoException(nuevo.id());
        }
        return pedidos.guardar(nuevo);
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return pedidos.buscarPorId(id);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidos.listarTodos();
    }

    @Override
    public Optional<Pedido> cancelar(String id) {
        Optional<Pedido> encontrado = pedidos.buscarPorId(id);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = encontrado.get();
        pedido.cancelar();

        if (pedido.requiereReembolso()) {
            reembolsos.reembolsar(pedido.id(), pedido.totalSoles());
            pedido.marcarReembolsado();
        }

        Pedido guardado = pedidos.guardar(pedido);
        notificador.notificarCancelacion(guardado);
        return Optional.of(guardado);
    }

    @Override
    public Optional<Pedido> entregar(String id) {
        Optional<Pedido> encontrado = pedidos.buscarPorId(id);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = encontrado.get();
        pedido.entregar();
        return Optional.of(pedidos.guardar(pedido));
    }

    @Override
    public int expirarAnterioresA(Instant fechaLimite) {
        List<Pedido> vencidos = pedidos.buscarEnPreparacionAnterioresA(fechaLimite);
        for (Pedido pedido : vencidos) {
            pedido.expirar();
            Pedido guardado = pedidos.guardar(pedido);
            notificador.notificarExpiracion(guardado);
        }
        return vencidos.size();
    }
}
```

### 3.4 Dobles de prueba en memoria y pruebas unitarias

Archivo: `application/src/test/java/com/tienda/pedidos/application/fake/RepositorioPedidosEnMemoria.java`

```java
package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioPedidosEnMemoria implements PedidoRepositoryPort {

    private final Map<String, Pedido> almacenamiento = new LinkedHashMap<>();

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id)).map(this::copiar);
    }

    @Override
    public List<Pedido> listarTodos() {
        return almacenamiento.values().stream().map(this::copiar).toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : almacenamiento.values()) {
            if (pedido.estado() == EstadoPedido.EN_PREPARACION && pedido.creadoEn().isBefore(fechaLimite)) {
                resultado.add(copiar(pedido));
            }
        }
        return resultado;
    }

    @Override
    public boolean existePorId(String id) {
        return almacenamiento.containsKey(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        Pedido copia = copiar(pedido);
        almacenamiento.put(copia.id(), copia);
        return copiar(copia);
    }

    private Pedido copiar(Pedido original) {
        return Pedido.reconstituir(
                original.id(),
                original.totalSoles(),
                original.estado(),
                original.creadoEn(),
                original.reembolsado()
        );
    }
}
```

Archivo: `application/src/test/java/com/tienda/pedidos/application/fake/NotificadorEnMemoria.java`

```java
package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.domain.model.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificadorEnMemoria implements NotificarEventoPedidoPort {

    private final List<String> cancelaciones = new ArrayList<>();
    private final List<String> expiraciones = new ArrayList<>();

    @Override
    public void notificarCancelacion(Pedido pedido) {
        cancelaciones.add(pedido.id());
    }

    @Override
    public void notificarExpiracion(Pedido pedido) {
        expiraciones.add(pedido.id());
    }

    public List<String> cancelaciones() {
        return Collections.unmodifiableList(cancelaciones);
    }

    public List<String> expiraciones() {
        return Collections.unmodifiableList(expiraciones);
    }
}
```

Archivo: `application/src/test/java/com/tienda/pedidos/application/fake/ReembolsoEnMemoria.java`

```java
package com.tienda.pedidos.application.fake;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReembolsoEnMemoria implements ProcesarReembolsoPort {

    public record RegistroReembolso(String pedidoId, int montoSoles) {}

    private final List<RegistroReembolso> registros = new ArrayList<>();

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        registros.add(new RegistroReembolso(pedidoId, montoSoles));
    }

    public List<RegistroReembolso> registros() {
        return Collections.unmodifiableList(registros);
    }
}
```

Archivo: `application/src/test/java/com/tienda/pedidos/application/service/PedidoUseCaseServiceTest.java`

```java
package com.tienda.pedidos.application.service;

import com.tienda.pedidos.application.fake.NotificadorEnMemoria;
import com.tienda.pedidos.application.fake.ReembolsoEnMemoria;
import com.tienda.pedidos.application.fake.RepositorioPedidosEnMemoria;
import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoUseCaseServiceTest {

    private static final Instant AHORA = Instant.parse("2026-11-21T18:00:00Z");

    private RepositorioPedidosEnMemoria repositorio;
    private NotificadorEnMemoria notificador;
    private ReembolsoEnMemoria reembolsos;
    private PedidoUseCaseService service;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioPedidosEnMemoria();
        notificador = new NotificadorEnMemoria();
        reembolsos = new ReembolsoEnMemoria();
        Clock relojFijo = Clock.fixed(AHORA, ZoneOffset.UTC);
        service = new PedidoUseCaseService(repositorio, notificador, reembolsos, relojFijo);
    }

    @Test
    void cancelarOrd101EnPreparacionConMontoAltoGuardaReembolsaYNotifica() {
        repositorio.guardar(Pedido.crear("ORD-101", 120, AHORA));

        Optional<Pedido> resultado = service.cancelar("ORD-101");

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPedido.CANCELADO, resultado.get().estado());
        assertTrue(resultado.get().reembolsado());
        assertEquals(EstadoPedido.CANCELADO, repositorio.buscarPorId("ORD-101").orElseThrow().estado());
        assertEquals(List.of(new ReembolsoEnMemoria.RegistroReembolso("ORD-101", 120)), reembolsos.registros());
        assertEquals(List.of("ORD-101"), notificador.cancelaciones());
    }

    @Test
    void cancelarPedidoMenorAlUmbralNoInvocaPasarelaDeReembolsos() {
        repositorio.guardar(Pedido.crear("ORD-103", 50, AHORA));

        Optional<Pedido> resultado = service.cancelar("ORD-103");

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPedido.CANCELADO, resultado.get().estado());
        assertFalse(resultado.get().reembolsado());
        assertTrue(reembolsos.registros().isEmpty());
        assertEquals(List.of("ORD-103"), notificador.cancelaciones());
    }

    @Test
    void cancelarOrd102EntregadoLanzaExcepcionSinGuardarNiReembolsarNiNotificar() {
        repositorio.guardar(Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, AHORA, false));

        assertThrows(PedidoNoCancelableException.class, () -> service.cancelar("ORD-102"));

        assertEquals(EstadoPedido.ENTREGADO, repositorio.buscarPorId("ORD-102").orElseThrow().estado());
        assertTrue(reembolsos.registros().isEmpty());
        assertTrue(notificador.cancelaciones().isEmpty());
    }

    @Test
    void cancelarOrd999AusenteDevuelveVacioSinEfectosSecundarios() {
        Optional<Pedido> resultado = service.cancelar("ORD-999");

        assertTrue(resultado.isEmpty());
        assertTrue(reembolsos.registros().isEmpty());
        assertTrue(notificador.cancelaciones().isEmpty());
    }

    @Test
    void crearRechazaIdentificadorDuplicado() {
        service.crear("ORD-200", 150);

        assertThrows(PedidoDuplicadoException.class, () -> service.crear("ORD-200", 90));
    }

    @Test
    void entregarCambiaEstadoDeEnPreparacionAEntregado() {
        service.crear("ORD-201", 75);

        Optional<Pedido> entregado = service.entregar("ORD-201");

        assertTrue(entregado.isPresent());
        assertEquals(EstadoPedido.ENTREGADO, entregado.get().estado());
    }

    @Test
    void expirarPedidosVencidosSoloAfectaEnPreparacionAnterioresAlLimite() {
        Instant hace30Horas = AHORA.minusSeconds(30 * 3600);
        Instant hace2Horas = AHORA.minusSeconds(2 * 3600);
        repositorio.guardar(Pedido.crear("ORD-OLD", 110, hace30Horas));
        repositorio.guardar(Pedido.reconstituir("ORD-OLD-DONE", 80, EstadoPedido.ENTREGADO, hace30Horas, false));
        repositorio.guardar(Pedido.crear("ORD-RECENT", 90, hace2Horas));

        int expirados = service.expirarAnterioresA(AHORA.minusSeconds(24 * 3600));

        assertEquals(1, expirados);
        assertEquals(EstadoPedido.EXPIRADO, repositorio.buscarPorId("ORD-OLD").orElseThrow().estado());
        assertEquals(EstadoPedido.ENTREGADO, repositorio.buscarPorId("ORD-OLD-DONE").orElseThrow().estado());
        assertEquals(EstadoPedido.EN_PREPARACION, repositorio.buscarPorId("ORD-RECENT").orElseThrow().estado());
        assertEquals(List.of("ORD-OLD"), notificador.expiraciones());
    }
}
```

### Checkpoint 2

```bash
./mvnw -pl application -am test
```

**Resultado esperado:** `domain` (6 pruebas) + `application` (7 pruebas) pasan en ~2 segundos totales, todavía sin haber escrito una sola clase de Spring Boot.

---

## Hito 4 · Implementar el módulo `infrastructure` (Spring Boot y adaptadores)

**Qué enseña este paso:** Spring Boot, REST, `@Scheduled`, JPA/PostgreSQL, adaptador en memoria, eventos y Stripe/Culqi viven exclusivamente fuera del núcleo y se conectan mediante `@Configuration` y `@Bean`.

Crea las carpetas:

```bash
mkdir -p infrastructure/src/main/java/com/tienda/pedidos/infrastructure/{config,adapter/in/web,adapter/in/scheduler,adapter/out/persistence/memory,adapter/out/persistence/jpa,adapter/out/messaging,adapter/out/payment}
mkdir -p infrastructure/src/main/resources
mkdir -p infrastructure/src/test/java/com/tienda/pedidos/infrastructure/{adapter/in/web,adapter/out/persistence/jpa}
```

### 4.1 Arranque y cableado (`PedidosHexagonalApplication` y `PedidoConfig`)

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/PedidosHexagonalApplication.java`

```java
package com.tienda.pedidos.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PedidosHexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidosHexagonalApplication.class, args);
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/config/PedidoConfig.java`

```java
package com.tienda.pedidos.infrastructure.config;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import com.tienda.pedidos.application.service.PedidoUseCaseService;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;

@Configuration
public class PedidoConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public PedidoUseCaseService pedidoUseCaseService(
            PedidoRepositoryPort pedidoRepositoryPort,
            NotificarEventoPedidoPort notificarEventoPedidoPort,
            ProcesarReembolsoPort procesarReembolsoPort,
            Clock systemClock
    ) {
        return new PedidoUseCaseService(
                pedidoRepositoryPort,
                notificarEventoPedidoPort,
                procesarReembolsoPort,
                systemClock
        );
    }

    @Bean
    @Profile("inmemory")
    public CommandLineRunner seedDemoPedidos(PedidoRepositoryPort pedidos, Clock clock) {
        return args -> {
            Instant ahora = Instant.now(clock);
            if (!pedidos.existePorId("ORD-101")) {
                pedidos.guardar(Pedido.crear("ORD-101", 120, ahora));
            }
            if (!pedidos.existePorId("ORD-102")) {
                pedidos.guardar(Pedido.reconstituir("ORD-102", 85, EstadoPedido.ENTREGADO, ahora, false));
            }
        };
    }
}
```

### 4.2 Adaptadores de entrada (`web` y `scheduler`)

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/web/CrearPedidoRequest.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

public record CrearPedidoRequest(String id, Integer totalSoles) {
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/web/PedidoResponse.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.domain.model.Pedido;

import java.time.Instant;

public record PedidoResponse(
        String id,
        int totalSoles,
        String estado,
        Instant creadoEn,
        boolean reembolsado
) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.id(),
                pedido.totalSoles(),
                pedido.estado().name(),
                pedido.creadoEn(),
                pedido.reembolsado()
        );
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/web/ErrorResponse.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

public record ErrorResponse(int status, String error, String message) {
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/web/PedidoController.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.application.port.in.CancelarPedidoUseCase;
import com.tienda.pedidos.application.port.in.ConsultarPedidoUseCase;
import com.tienda.pedidos.application.port.in.CrearPedidoUseCase;
import com.tienda.pedidos.application.port.in.EntregarPedidoUseCase;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedido;
    private final ConsultarPedidoUseCase consultarPedido;
    private final CancelarPedidoUseCase cancelarPedido;
    private final EntregarPedidoUseCase entregarPedido;

    public PedidoController(
            CrearPedidoUseCase crearPedido,
            ConsultarPedidoUseCase consultarPedido,
            CancelarPedidoUseCase cancelarPedido,
            EntregarPedidoUseCase entregarPedido
    ) {
        this.crearPedido = crearPedido;
        this.consultarPedido = consultarPedido;
        this.cancelarPedido = cancelarPedido;
        this.entregarPedido = entregarPedido;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
        if (request == null || request.totalSoles() == null) {
            throw new PedidoInvalidoException("Los campos id y totalSoles son obligatorios");
        }
        Pedido creado = crearPedido.crear(request.id(), request.totalSoles());
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponse.from(creado));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> respuesta = consultarPedido.listarTodos().stream()
                .map(PedidoResponse::from)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") String id) {
        return consultarPedido.buscarPorId(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable("id") String id) {
        return cancelarPedido.cancelar(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<?> entregar(@PathVariable("id") String id) {
        return entregarPedido.entregar(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(PedidoResponse.from(p)))
                .orElseGet(() -> notFound(id));
    }

    private ResponseEntity<ErrorResponse> notFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "NOT_FOUND", "No se encontró el pedido " + id));
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/web/PedidoExceptionHandler.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

import com.tienda.pedidos.domain.exception.PedidoDuplicadoException;
import com.tienda.pedidos.domain.exception.PedidoEstadoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoInvalidoException;
import com.tienda.pedidos.domain.exception.PedidoNoCancelableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PedidoExceptionHandler {

    @ExceptionHandler(PedidoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleInvalido(PedidoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler({
            PedidoNoCancelableException.class,
            PedidoEstadoInvalidoException.class,
            PedidoDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "CONFLICT", ex.getMessage()));
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/in/scheduler/PedidosExpiradosJob.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.scheduler;

import com.tienda.pedidos.application.port.in.ExpirarPedidosVencidosUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
public class PedidosExpiradosJob {

    private static final Logger log = LoggerFactory.getLogger(PedidosExpiradosJob.class);

    private final ExpirarPedidosVencidosUseCase expirarPedidos;
    private final Clock clock;
    private final long horasExpiracion;

    public PedidosExpiradosJob(
            ExpirarPedidosVencidosUseCase expirarPedidos,
            Clock clock,
            @Value("${pedidos.expiracion.horas:24}") long horasExpiracion
    ) {
        this.expirarPedidos = expirarPedidos;
        this.clock = clock;
        this.horasExpiracion = horasExpiracion;
    }

    @Scheduled(cron = "${pedidos.scheduler.cron:0 */30 * * * *}")
    public void ejecutar() {
        Instant limite = Instant.now(clock).minus(Duration.ofHours(horasExpiracion));
        int expirados = expirarPedidos.expirarAnterioresA(limite);
        if (expirados > 0) {
            log.info("[SCHEDULER] Se expiraron {} pedido(s) anteriores a {}", expirados, limite);
        }
    }
}
```

### 4.3 Adaptadores de salida (`memory`, `jpa`, `messaging` y `payment`)

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/memory/PedidoInMemoryAdapter.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.memory;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("inmemory")
public class PedidoInMemoryAdapter implements PedidoRepositoryPort {

    private final Map<String, Pedido> almacenamiento = new ConcurrentHashMap<>();

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id)).map(this::copiar);
    }

    @Override
    public List<Pedido> listarTodos() {
        return almacenamiento.values().stream()
                .sorted(Comparator.comparing(Pedido::id))
                .map(this::copiar)
                .toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        return almacenamiento.values().stream()
                .filter(p -> p.estado() == EstadoPedido.EN_PREPARACION && p.creadoEn().isBefore(fechaLimite))
                .map(this::copiar)
                .toList();
    }

    @Override
    public boolean existePorId(String id) {
        return almacenamiento.containsKey(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        Pedido copia = copiar(pedido);
        almacenamiento.put(copia.id(), copia);
        return copiar(copia);
    }

    private Pedido copiar(Pedido original) {
        return Pedido.reconstituir(
                original.id(),
                original.totalSoles(),
                original.estado(),
                original.creadoEn(),
                original.reembolsado()
        );
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/jpa/PedidoEntity.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "total_soles", nullable = false)
    private int totalSoles;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoPedido estado;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "reembolsado", nullable = false)
    private boolean reembolsado;

    public PedidoEntity() {
    }

    public PedidoEntity(String id, int totalSoles, EstadoPedido estado, Instant creadoEn, boolean reembolsado) {
        this.id = id;
        this.totalSoles = totalSoles;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.reembolsado = reembolsado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getTotalSoles() { return totalSoles; }
    public void setTotalSoles(int totalSoles) { this.totalSoles = totalSoles; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public boolean isReembolsado() { return reembolsado; }
    public void setReembolsado(boolean reembolsado) { this.reembolsado = reembolsado; }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/jpa/SpringDataPedidoRepository.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, String> {

    List<PedidoEntity> findByEstadoAndCreadoEnBefore(EstadoPedido estado, Instant fechaLimite);
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/jpa/PedidoMapper.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public Pedido toDomain(PedidoEntity entity) {
        return Pedido.reconstituir(
                entity.getId(),
                entity.getTotalSoles(),
                entity.getEstado(),
                entity.getCreadoEn(),
                entity.isReembolsado()
        );
    }

    public PedidoEntity toEntity(Pedido pedido) {
        return new PedidoEntity(
                pedido.id(),
                pedido.totalSoles(),
                pedido.estado(),
                pedido.creadoEn(),
                pedido.reembolsado()
        );
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/jpa/PedidoJpaAdapter.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("postgres")
public class PedidoJpaAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository jpaRepository;
    private final PedidoMapper mapper;

    public PedidoJpaAdapter(SpringDataPedidoRepository jpaRepository, PedidoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> listarTodos() {
        return jpaRepository.findAll(Sort.by("id")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        return jpaRepository.findByEstadoAndCreadoEnBefore(EstadoPedido.EN_PREPARACION, fechaLimite).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorId(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity guardado = jpaRepository.save(mapper.toEntity(pedido));
        return mapper.toDomain(guardado);
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/messaging/LogEventoPedidoAdapter.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.messaging;

import com.tienda.pedidos.application.port.out.NotificarEventoPedidoPort;
import com.tienda.pedidos.domain.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogEventoPedidoAdapter implements NotificarEventoPedidoPort {

    private static final Logger log = LoggerFactory.getLogger(LogEventoPedidoAdapter.class);

    @Override
    public void notificarCancelacion(Pedido pedido) {
        log.info(
                "[EVENTO · topic=pedidos-cancelados] {\"id\":\"{}\",\"totalSoles\":{},\"estado\":\"{}\",\"reembolsado\":{}}",
                pedido.id(), pedido.totalSoles(), pedido.estado(), pedido.reembolsado()
        );
    }

    @Override
    public void notificarExpiracion(Pedido pedido) {
        log.info(
                "[EVENTO · topic=pedidos-expirados] {\"id\":\"{}\",\"totalSoles\":{},\"estado\":\"{}\"}",
                pedido.id(), pedido.totalSoles(), pedido.estado()
        );
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/payment/StripeReembolsoAdapter.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.payment;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!culqi")
public class StripeReembolsoAdapter implements ProcesarReembolsoPort {

    private static final Logger log = LoggerFactory.getLogger(StripeReembolsoAdapter.class);

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        log.info("[STRIPE] Reembolso simulado aprobado para pedido {} por S/ {}", pedidoId, montoSoles);
    }
}
```

Archivo: `infrastructure/src/main/java/com/tienda/pedidos/infrastructure/adapter/out/payment/CulqiReembolsoAdapter.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.payment;

import com.tienda.pedidos.application.port.out.ProcesarReembolsoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("culqi")
public class CulqiReembolsoAdapter implements ProcesarReembolsoPort {

    private static final Logger log = LoggerFactory.getLogger(CulqiReembolsoAdapter.class);

    @Override
    public void reembolsar(String pedidoId, int montoSoles) {
        log.info("[CULQI] Reembolso simulado aprobado para pedido {} por S/ {}", pedidoId, montoSoles);
    }
}
```

### 4.4 Archivos de propiedades y `docker-compose.yml` opcional

Archivo: `infrastructure/src/main/resources/application.properties`

```properties
spring.application.name=pedidos-hexagonal
server.port=8080

spring.profiles.default=inmemory

pedidos.expiracion.horas=24
pedidos.scheduler.cron=0 */30 * * * *
```

Archivo: `infrastructure/src/main/resources/application-inmemory.properties`

```properties
spring.autoconfigure.exclude=\
  org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,\
  org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,\
  org.springframework.boot.data.jpa.autoconfigure.JpaRepositoriesAutoConfiguration
```

Archivo: `infrastructure/src/main/resources/application-postgres.properties`

```properties
# Ajusta credenciales con variables de entorno en vez de guardar contraseñas reales en Git.
spring.datasource.url=${POSTGRES_URL:jdbc:postgresql://localhost:5432/pedidos}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:postgres}

# Opción didáctica para entorno local; en producción usa migraciones (Flyway/Liquibase).
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Archivo: `docker-compose.yml` *(opcional, solo para el perfil `postgres`)*

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: pedidos-hexagonal-postgres
    environment:
      POSTGRES_DB: pedidos
      POSTGRES_USER: ${POSTGRES_USER:-postgres}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-postgres}
    ports:
      - "${POSTGRES_PORT:-5432}:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d pedidos"]
      interval: 5s
      timeout: 5s
      retries: 10
```

### 4.5 Pruebas del módulo `infrastructure`

Archivo: `infrastructure/src/test/java/com/tienda/pedidos/infrastructure/adapter/out/persistence/jpa/PedidoMapperTest.java`

```java
package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    private final PedidoMapper mapper = new PedidoMapper();

    @Test
    void traduceIdaYVueltaEntreDominioYEntidadJpa() {
        Instant fecha = Instant.parse("2026-11-21T16:30:00Z");
        Pedido original = Pedido.reconstituir("ORD-101", 120, EstadoPedido.CANCELADO, fecha, true);

        PedidoEntity entity = mapper.toEntity(original);
        Pedido reconstruido = mapper.toDomain(entity);

        assertEquals("ORD-101", entity.getId());
        assertEquals(120, entity.getTotalSoles());
        assertEquals(EstadoPedido.CANCELADO, entity.getEstado());
        assertEquals(fecha, entity.getCreadoEn());
        assertTrue(entity.isReembolsado());
        assertEquals(original, reconstruido);
    }
}
```

Archivo: `infrastructure/src/test/java/com/tienda/pedidos/infrastructure/adapter/in/web/PedidoControllerIntegrationTest.java`

```java
package com.tienda.pedidos.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"inmemory", "culqi"})
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flujoCompletoDeCreacionConsultaYCancelacionConReembolso() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-101\",\"totalSoles\":140}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("INT-101"))
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"))
                .andExpect(jsonPath("$.reembolsado").value(false));

        mockMvc.perform(get("/api/v1/pedidos/INT-101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSoles").value(140));

        mockMvc.perform(patch("/api/v1/pedidos/INT-101/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"))
                .andExpect(jsonPath("$.reembolsado").value(true));
    }

    @Test
    void rechazaCancelacionDePedidoEntregadoCon409() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-102\",\"totalSoles\":85}"))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/api/v1/pedidos/INT-102/entregar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));

        mockMvc.perform(patch("/api/v1/pedidos/INT-102/cancelar"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void devuelve404SiPedidoNoExisteY400SiMontoEsInvalido() throws Exception {
        mockMvc.perform(patch("/api/v1/pedidos/INT-999/cancelar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-BAD\",\"totalSoles\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
```

### Checkpoint 3

```bash
./mvnw test
```

**Resultado esperado:** `BUILD SUCCESS` con **17 pruebas** pasando (`domain`: 6, `application`: 7, `infrastructure`: 4).

---

## Hito 5 · Crear el script de verificación HTTP y ejecutar la API

### 5.1 Crear `scripts/check_api.sh`

Archivo: `scripts/check_api.sh`

```bash
#!/usr/bin/env bash
# Comprobación de endpoints REST de pedidos-hexagonal.
#
# Prerrequisitos:
#   - curl instalado
#   - La aplicación empaquetada y en ejecución:
#       ./mvnw package -DskipTests
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
#     Ejemplos con otros perfiles:
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=inmemory,culqi
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres
#
# Uso:
#   ./scripts/check_api.sh
#   BASE_URL=http://localhost:8080 ./scripts/check_api.sh
#   ./scripts/check_api.sh http://localhost:8080

set -euo pipefail

BASE_URL="${1:-${BASE_URL:-http://localhost:8080}}"
BASE_URL="${BASE_URL%/}"

if ! command -v curl >/dev/null 2>&1; then
  echo "ERROR: se requiere curl para ejecutar este script." >&2
  exit 1
fi

RUN_ID="$(date +%s)-$$"
ID_ALTO="CHK-ALTO-${RUN_ID}"
ID_BAJO="CHK-BAJO-${RUN_ID}"
ID_ENTREGADO="CHK-ENT-${RUN_ID}"
ID_AUSENTE="CHK-999-${RUN_ID}"

TMP_BODY="$(mktemp)"
trap 'rm -f "$TMP_BODY"' EXIT

PASSED=0

assert_request() {
  local name="$1"
  local method="$2"
  local path="$3"
  local expected_status="$4"
  local payload="$5"
  shift 5

  local status
  if [ -n "$payload" ]; then
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" -H "Content-Type: application/json" -d "$payload" \
      "${BASE_URL}${path}")"
  else
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" "${BASE_URL}${path}")"
  fi

  local body
  body="$(cat "$TMP_BODY")"

  if [ "$status" != "$expected_status" ]; then
    echo "FAIL [$name]: esperado HTTP $expected_status, recibido HTTP $status" >&2
    echo "  Respuesta: $body" >&2
    exit 1
  fi

  local expected_substring
  for expected_substring in "$@"; do
    if [ -n "$expected_substring" ] && ! printf '%s' "$body" | grep -Fq "$expected_substring"; then
      echo "FAIL [$name]: la respuesta no contiene '$expected_substring'" >&2
      echo "  Respuesta: $body" >&2
      exit 1
    fi
  done

  PASSED=$((PASSED + 1))
  echo "PASS [$name]: HTTP $status"
}

echo "Verificando API en ${BASE_URL} (lote ${RUN_ID})..."

assert_request "Crear pedido con monto alto (>= S/ 100)" "POST" "/api/v1/pedidos" "201" "{\"id\":\"${ID_ALTO}\",\"totalSoles\":120}" "\"id\":\"${ID_ALTO}\"" "\"estado\":\"EN_PREPARACION\"" "\"reembolsado\":false"
assert_request "Rechazar ID duplicado" "POST" "/api/v1/pedidos" "409" "{\"id\":\"${ID_ALTO}\",\"totalSoles\":120}" "\"status\":409"
assert_request "Rechazar monto inválido (<= 0)" "POST" "/api/v1/pedidos" "400" "{\"id\":\"CHK-BAD-${RUN_ID}\",\"totalSoles\":0}" "\"status\":400"
assert_request "Consultar pedido por ID" "GET" "/api/v1/pedidos/${ID_ALTO}" "200" "" "\"id\":\"${ID_ALTO}\"" "\"estado\":\"EN_PREPARACION\""
assert_request "Consultar pedido inexistente devuelve 404" "GET" "/api/v1/pedidos/${ID_AUSENTE}" "404" "" "\"status\":404"
assert_request "Listar pedidos incluye el pedido creado" "GET" "/api/v1/pedidos" "200" "" "\"id\":\"${ID_ALTO}\""
assert_request "Cancelar pedido >= S/ 100 aplica reembolso" "PATCH" "/api/v1/pedidos/${ID_ALTO}/cancelar" "200" "" "\"estado\":\"CANCELADO\"" "\"reembolsado\":true"
assert_request "Rechazar doble cancelación" "PATCH" "/api/v1/pedidos/${ID_ALTO}/cancelar" "409" "" "\"status\":409"
assert_request "Crear pedido con monto bajo (< S/ 100)" "POST" "/api/v1/pedidos" "201" "{\"id\":\"${ID_BAJO}\",\"totalSoles\":50}" "\"id\":\"${ID_BAJO}\"" "\"estado\":\"EN_PREPARACION\""
assert_request "Cancelar pedido < S/ 100 no aplica reembolso" "PATCH" "/api/v1/pedidos/${ID_BAJO}/cancelar" "200" "" "\"estado\":\"CANCELADO\"" "\"reembolsado\":false"
assert_request "Crear pedido para entregar" "POST" "/api/v1/pedidos" "201" "{\"id\":\"${ID_ENTREGADO}\",\"totalSoles\":85}" "\"id\":\"${ID_ENTREGADO}\""
assert_request "Entregar pedido en preparación" "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/entregar" "200" "" "\"estado\":\"ENTREGADO\""
assert_request "Rechazar entrega repetida" "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/entregar" "409" "" "\"status\":409"
assert_request "Rechazar cancelación de pedido entregado" "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/cancelar" "409" "" "\"status\":409"
assert_request "Cancelar pedido inexistente devuelve 404" "PATCH" "/api/v1/pedidos/${ID_AUSENTE}/cancelar" "404" "" "\"status\":404"

echo "OK: ${PASSED} comprobaciones superadas contra ${BASE_URL}."
```

Hazlo ejecutable:

```bash
chmod +x scripts/check_api.sh
```

### 5.2 Empaquetar, arrancar y probar en vivo

Terminal 1 (perfil por defecto `inmemory` + Stripe):

```bash
./mvnw package -DskipTests
java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
```

Terminal 2:

```bash
./scripts/check_api.sh
```

**Resultado esperado:** `OK: 15 comprobaciones superadas contra http://localhost:8080.` y en los logs del servidor verás `[STRIPE] Reembolso simulado aprobado...` seguido de `[EVENTO · topic=pedidos-cancelados]`.

### 5.3 Demostración en clase: cambiar adaptadores sin tocar el núcleo

1. **Cambiar de Stripe a Culqi:** detén la app (`Ctrl+C`) y vuelve a arrancar con:
   ```bash
   java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=inmemory,culqi
   ```
   Ejecuta `./scripts/check_api.sh` y comprueba en los logs `[CULQI] Reembolso simulado aprobado...`.

2. **Cambiar de memoria a PostgreSQL (JPA):**
   ```bash
   docker compose up -d
   java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres
   ```
   Si ya tienes un PostgreSQL local usando el puerto `5432`, levanta el contenedor en otro puerto (por ejemplo `5433`):
   ```bash
   POSTGRES_PORT=5433 docker compose up -d
   POSTGRES_URL=jdbc:postgresql://localhost:5433/pedidos java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres
   ```
   Ejecuta `./scripts/check_api.sh` en otra terminal; los registros ahora persisten en la tabla `pedidos` de PostgreSQL.

3. **Probar el límite físico de Maven:** intenta añadir temporalmente `import org.springframework.stereotype.Service;` dentro de `domain/.../Pedido.java` o `application/.../PedidoUseCaseService.java` y ejecuta `./mvnw compile`. Maven fallará de inmediato porque esos submódulos no tienen Spring en su classpath. **Recuerda eliminar ese `import` temporal al terminar la prueba.**

---

## Diagnóstico de problemas comunes

| Síntoma | Causa probable | Solución |
| --- | --- | --- |
| `Could not find artifact com.tienda:application:jar` al usar `spring-boot:run` | En proyectos multimódulo, `spring-boot:run` en un submódulo necesita artefactos hermanos construidos | Ejecuta `./mvnw package -DskipTests` y luego `java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar` (o `./mvnw install -DskipTests` antes de `./mvnw -pl infrastructure spring-boot:run`). |
| `Failed to configure a DataSource` o falta de `PedidoRepositoryPort` | Se pasó `--spring.profiles.active=culqi` solo (desactivando el perfil por defecto `inmemory`) | Combina siempre un perfil de persistencia: `--spring.profiles.active=inmemory,culqi` o `--spring.profiles.active=postgres,culqi`. |
| `Bind for 0.0.0.0:5432 failed` al ejecutar `docker compose up -d` | Ya existe un PostgreSQL local usando el puerto `5432` | Usa `POSTGRES_PORT=5433 docker compose up -d` y pasa `POSTGRES_URL=jdbc:postgresql://localhost:5433/pedidos`. |
| `Port 8080 was already in use` | Otra aplicación Spring Boot sigue abierta | Cierra el proceso anterior o arranca con `--server.port=8081` y ejecuta `./scripts/check_api.sh http://localhost:8081`. |
| `Connection refused` con perfil `postgres` | PostgreSQL no está levantado en el puerto configurado | Ejecuta `docker compose up -d` (o inicia tu PostgreSQL local con la base `pedidos`). |

## Limpieza final

```bash
# Si iniciaste PostgreSQL con Docker Compose:
docker compose down -v
```
