# Temario de Programación Orientada a Objetos en Java

El temario propone una progresión de **16 unidades**, desde representar objetos hasta diseñar y probar aplicaciones con ellos. El ejemplo conductor será una tienda: productos, pedidos, entregas y notificaciones.

Se distinguen tres categorías:

- **Lenguaje Java:** clases, constructores, herencia, interfaces y genéricos.
- **Biblioteca estándar:** `Object`, colecciones y clases de excepciones.
- **Diseño y herramientas:** SOLID, patrones, arquitectura y JUnit.

Los ejemplos son fragmentos didácticos; las instrucciones sueltas se ejecutan dentro de un método. Cada ejemplo introduce solo una parte del modelo y no constituye una aplicación completa. Las clases con nombres repetidos representan versiones sucesivas o alternativas: no deben copiarse juntas en un mismo paquete.

Como requisito de entrada basta con comprender variables, tipos, condiciones, llamadas a métodos y ejecución de un programa Java sencillo. Estos conocimientos se repasarán cuando sean necesarios para entender un objeto.

## Nivel básico — Representar objetos y proteger su comportamiento

### 1. Fundamentos de la programación orientada a objetos

**Idea central.** Cuando los datos de un pedido y las operaciones que los modifican están dispersos, resulta difícil mantener sus reglas. La POO permite reunir estado y comportamiento en objetos que colaboran.

Conceptos que se deben estudiar:

- Objeto, clase, estado, comportamiento e identidad.
- Encapsulamiento, abstracción, herencia y polimorfismo: primera visión, sin profundizar todavía.
- Enfoque procedural: organización alrededor de procedimientos que operan sobre datos.
- Enfoque orientado a objetos: organización alrededor de objetos con operaciones y reglas.
- Convivencia de ambos enfoques en Java.

```java
// Enfoque procedural: una operación recibe los datos.
int total = Calculadora.calcularTotal(precio, cantidad);

// Enfoque orientado a objetos: el pedido conoce sus datos.
int totalPedido = pedido.calcularTotal();
```

Estas llamadas ilustran dos diseños posibles; las clases y los datos se desarrollarían por separado. La diferencia está en cómo se distribuyen los datos y las responsabilidades, no simplemente en utilizar la palabra `class`. La documentación oficial introduce los objetos mediante su estado y comportamiento: [objetos y clases en Java](https://dev.java/learn/language/oop/classes/).

**Errores comunes:** creer que todo sustantivo necesita una clase; confundir POO con herencia; suponer que poner funciones estáticas dentro de una clase ya produce un buen diseño orientado a objetos.

**Ejercicio:** a partir de una compra con dos productos, identificar tres objetos, sus datos y sus operaciones. Explicar qué objeto debería calcular el total.

**Requisitos y conexión:** conocimientos mínimos de Java. Esta unidad da sentido a las clases que se construirán a continuación.

### 2. Clases, objetos y referencias

**Idea central.** Una clase declara qué datos y operaciones tendrán sus instancias; cada objeto mantiene su propio estado.

Conceptos que se deben estudiar:

- Declaración de clases, atributos y métodos.
- Parámetros, valores de retorno y métodos de instancia.
- Creación con `new`.
- Diferencia entre clase, objeto y variable de referencia.
- Referencias compartidas, `null` y `NullPointerException`.
- Paso por valor en Java: al pasar un objeto se copia el valor de su referencia.
- Alcance de variables locales frente a atributos.

```java
class ContadorPedido {
    private int unidades;

    public void agregarUnidad() {
        unidades++;
    }

    public int consultarUnidades() {
        return unidades;
    }
}

// Dentro de un método:
ContadorPedido primero = new ContadorPedido();
ContadorPedido segundo = new ContadorPedido();

primero.agregarUnidad();
// primero tiene 1 unidad; segundo sigue teniendo 0.
```

**Errores comunes:** pensar que asignar una referencia copia el objeto; creer que todos los objetos comparten sus atributos de instancia; llamar métodos sobre `null`.

**Ejercicio:** crear dos contadores independientes y después una tercera referencia al primero. Predecir qué consultas cambiarán al agregar una unidad mediante esa tercera referencia.

**Requisitos y conexión:** unidad 1. Convierte el modelo conceptual de objeto en código y prepara el estudio de su inicialización.

### 3. Constructores e inicialización

**Idea central.** Un objeto debe comenzar con los datos necesarios para poder utilizarse correctamente.

Conceptos que se deben estudiar:

- Constructor sin parámetros y constructor parametrizado.
- Constructor por defecto proporcionado por el compilador.
- Sobrecarga de constructores.
- `this` para referirse al objeto actual.
- `this(...)` para reutilizar otro constructor.
- Inicialización de atributos y diferencias entre constructor y método.

```java
class Pedido {
    private String cliente;

    public Pedido() {
        this("Cliente ocasional");
    }

    public Pedido(String cliente) {
        this.cliente = cliente;
    }
}
```

El constructor por defecto solo se genera si la clase no declara ningún constructor. Un constructor sin parámetros escrito por el programador no es ese constructor generado. El ejemplo se centra en la inicialización; la validación de los datos se incorpora al estudiar encapsulamiento y excepciones.

**Errores comunes:** escribir un tipo de retorno en un constructor; esperar que siga existiendo el constructor generado después de declarar otro; duplicar la inicialización en varias sobrecargas.

**Ejercicio:** crear `Producto` con código y nombre obligatorios, y una sobrecarga que permita omitir una descripción. Ambos constructores deben producir objetos utilizables.

**Requisitos y conexión:** unidad 2. Añade un punto de entrada controlado a la creación de objetos.

### 4. Encapsulamiento y reglas del estado

**Idea central.** Si cualquier parte del programa modifica directamente el stock, puede dejarlo negativo. El objeto debe controlar las operaciones que afectan sus datos.

Conceptos que se deben estudiar:

- `public`, `private`, `protected` y acceso de paquete, sin modificador.
- Paquetes e `import`, necesarios para comprender la visibilidad.
- Getters y setters: cuándo ayudan y cuándo exponen demasiado.
- Invariantes: reglas que deben mantenerse durante la vida del objeto.
- Métodos que expresan acciones del negocio.
- Exposición accidental de objetos mutables.

```java
class Inventario {
    private int stock = 5;

    public boolean retirar(int unidades) {
        if (unidades <= 0 || unidades > stock) {
            return false;
        }
        stock -= unidades;
        return true;
    }

    public int consultarStock() {
        return stock;
    }
}
```

`protected` permite acceso dentro del mismo paquete y acceso desde subclases con restricciones adicionales fuera del paquete; no significa simplemente «visible para cualquier objeto hijo».

**Errores comunes:** generar setters para todos los atributos; validar únicamente en la interfaz de usuario; devolver una colección interna modificable.

**Ejercicio:** agregar una operación para reponer stock. Debe rechazar cantidades no positivas y conservar el estado cuando se rechace una operación.

**Requisitos y conexión:** unidades 2–3. Extiende la inicialización correcta hacia la protección del objeto durante toda su vida.

### 5. Miembros estáticos, constantes e inmutabilidad

**Idea central.** Algunos datos pertenecen a cada objeto; otros pertenecen a la clase. Además, ciertos valores deberían mantenerse estables después de la construcción.

Conceptos que se deben estudiar:

- Campos y métodos `static`.
- Miembros de clase frente a miembros de instancia.
- `final` en variables, referencias, métodos y clases.
- Constantes con `static final`.
- Diseño de objetos inmutables: estado privado, ausencia de modificaciones y copias defensivas cuando corresponda.

```java
final class Producto {
    public static final int LONGITUD_MAXIMA_CODIGO = 12;

    private final String codigo;

    public Producto(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
```

El ejemplo muestra dónde se declara una constante; aplicar el límite al constructor será parte de la validación. Una referencia `final` no puede reasignarse, pero el objeto al que apunta puede seguir siendo mutable.

**Errores comunes:** convertir todo en `static`; usar estado global mutable; confundir `final` con inmutabilidad profunda.

**Ejercicio:** diseñar una dirección de entrega inmutable. Crear una dirección diferente debe requerir un nuevo objeto.

**Requisitos y conexión:** unidades 3–4. Refuerza el control del estado y prepara la comprensión de `final` en jerarquías.

### 6. Herencia y sobrescritura

**Idea central.** Algunas clases representan variantes de un mismo tipo. La herencia permite expresar esa relación y especializar operaciones.

Conceptos que se deben estudiar:

- Superclase y subclase.
- `extends` y herencia simple de clases.
- Relación «es un».
- Inicialización de la superclase con `super(...)`.
- Acceso a comportamiento heredado mediante `super`.
- Sobrescritura y `@Override`.
- Restricciones de `final`; los constructores no se heredan.
- Diferencia entre sobrescritura y ocultamiento de miembros estáticos.

```java
class Entrega {
    public int diasEstimados() {
        return 5;
    }
}

class EntregaExpress extends Entrega {
    @Override
    public int diasEstimados() {
        return 1;
    }
}
```

**Errores comunes:** heredar solo para ahorrar líneas; crear jerarquías profundas; debilitar reglas de la superclase; confundir atributos del mismo nombre con métodos sobrescritos.

**Ejercicio:** agregar una entrega programada. Justificar si realmente puede utilizarse en los mismos lugares que una entrega general.

**Requisitos y conexión:** unidades 2–5. Introduce especialización; más adelante se comparará con la composición.

### 7. Abstracción y clases abstractas

**Idea central.** Quien consulta una entrega necesita conocer su plazo, sin depender de cómo lo calcula cada modalidad.

Conceptos que se deben estudiar:

- Abstracción como selección de las operaciones relevantes.
- Contrato público y detalles de implementación.
- Clases y métodos `abstract`.
- Métodos concretos dentro de clases abstractas.
- Estado y constructores de una clase abstracta.
- Diferencia entre abstracción y encapsulamiento.

```java
abstract class Entrega {
    public abstract int diasEstimados();

    public String resumen() {
        return "Plazo de entrega (días): " + diasEstimados();
    }
}

class EntregaExpress extends Entrega {
    @Override
    public int diasEstimados() {
        return 1;
    }
}
```

La abstracción no requiere siempre una clase abstracta: una clase concreta con una API clara también oculta detalles innecesarios.

**Errores comunes:** intentar instanciar una clase abstracta; confundir abstracción con ausencia de código; crear una clase abstracta sin una necesidad real.

**Ejercicio:** agregar otra modalidad de entrega y reutilizar `resumen()` sin modificar la clase base.

**Requisitos y conexión:** unidad 6. Separa lo que todas las entregas ofrecen de lo que cada variante debe implementar.

### 8. Polimorfismo y sobrecarga

**Idea central.** Un mismo código puede trabajar con distintas modalidades de entrega a través de un tipo común.

Conceptos que se deben estudiar:

- Tipo declarado de una referencia y clase real del objeto.
- Referencia de supertipo e instancia de subtipo.
- Despacho dinámico de métodos de instancia sobrescritos.
- Sobrecarga: mismo nombre con diferentes parámetros.
- Selección de sobrecargas en compilación frente a sobrescritura en ejecución.
- Conversiones de tipo y riesgos del downcasting.

```java
// Dentro de un método, utilizando las clases de la unidad anterior:
Entrega entrega = new EntregaExpress();

System.out.println(entrega.diasEstimados()); // 1
System.out.println(entrega.resumen());       // Plazo de entrega (días): 1
```

La sobrecarga se estudia junto al polimorfismo, pero no funciona mediante el mismo mecanismo que la sobrescritura. Cambiar únicamente el tipo de retorno no crea una sobrecarga válida.

**Errores comunes:** creer que el tipo de la variable determina siempre la implementación ejecutada; llenar el código de conversiones; esperar despacho dinámico en campos o métodos estáticos.

**Ejercicio:** escribir un método que reciba `Entrega` y muestre su plazo. Debe aceptar dos modalidades sin preguntar cuál es su clase concreta.

**Requisitos y conexión:** unidades 6–7. Permite utilizar las jerarquías sin acoplar cada llamada a una implementación.

## Nivel intermedio — Colaboración, contratos y grupos de objetos

### 9. Interfaces

**Idea central.** Para notificar al cliente, un pedido necesita una operación de envío; no necesita conocer los detalles del correo o de otro canal.

Conceptos que se deben estudiar:

- Declaración con `interface` e implementación con `implements`.
- Interfaces como tipos y contratos.
- Implementación de múltiples interfaces.
- Herencia entre interfaces.
- Métodos `default`, `static` y, como ampliación, privados.
- Conflictos entre métodos `default`.
- Diferencias frente a clases abstractas.
- Interfaces funcionales; lambdas como ampliación posterior.

```java
interface Notificador {
    void enviar(String mensaje);

    default void confirmarPedido() {
        enviar("Pedido confirmado");
    }
}

class NotificadorConsola implements Notificador {
    @Override
    public void enviar(String mensaje) {
        System.out.println(mensaje);
    }
}
```

Una clase abstracta puede mantener estado de instancia y participar en la única cadena de herencia de clases. Una interfaz permite ofrecer un contrato que clases de distintas jerarquías pueden implementar. Sus métodos `static` se invocan mediante el nombre de la interfaz.

**Errores comunes:** creer que una interfaz nunca puede contener implementación; añadir operaciones que algunos implementadores no pueden cumplir; confundir múltiples interfaces con múltiples superclases.

**Ejercicio:** crear dos notificadores intercambiables y utilizarlos mediante referencias `Notificador`.

**Requisitos y conexión:** unidades 7–8. Extiende el polimorfismo a contratos independientes de una superclase compartida.

### 10. Relaciones entre objetos

**Idea central.** Una aplicación funciona mediante objetos que conocen o utilizan otros objetos. Es necesario decidir quién conserva cada referencia y quién controla cada parte.

Conceptos que se deben estudiar:

- Asociación: vínculo entre objetos.
- Agregación: relación todo–parte con partes independientes.
- Composición: propiedad y control del ciclo de vida lógico de las partes.
- Dependencia: utilización de otro tipo, por ejemplo mediante un parámetro.
- Cardinalidad y relaciones unidireccionales o bidireccionales.
- Diagramas de clases sencillos.
- Composición frente a herencia.

```java
class Cliente {
}

class Pedido {
    private final Cliente cliente; // Asociación.

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void confirmar(Notificador notificador) {
        notificador.enviar("Pedido confirmado"); // Dependencia.
    }
}
```

El ejemplo utiliza `Notificador` de la unidad 9. Agregación y composición son decisiones del modelo: Java no proporciona palabras clave que las impongan. La composición tampoco implica que el recolector de basura destruya inmediatamente las partes al desaparecer el objeto principal.

**Errores comunes:** representar «tiene un» mediante `extends`; compartir partes que debían ser exclusivas; crear relaciones bidireccionales innecesarias.

**Ejercicio:** modelar un pedido con líneas propias y un cliente independiente. Explicar qué objetos pueden compartirse entre pedidos y cuáles no.

**Requisitos y conexión:** unidades 2, 4 y 9. Une los objetos individuales y permite escoger relaciones más adecuadas que la herencia.

### 11. Object, identidad e igualdad

**Idea central.** Dos objetos diferentes pueden representar el mismo código de producto. El programa necesita distinguir identidad física e igualdad definida por el modelo.

Conceptos que se deben estudiar:

- `Object` como raíz de la jerarquía de clases.
- `toString()`, `equals()` y `hashCode()`.
- `==` frente a igualdad lógica.
- Contrato de `equals()`: reflexividad, simetría, transitividad, consistencia y comparación con `null`.
- Objetos iguales deben tener el mismo hash; hashes iguales no garantizan igualdad.
- `instanceof`, comprobación de tipos y conversiones seguras.
- Identidad de entidades frente a igualdad de objetos valor.

```java
final class CodigoProducto {
    private final int valor;

    public CodigoProducto(int valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof CodigoProducto)) {
            return false;
        }
        CodigoProducto codigo = (CodigoProducto) otro;
        return valor == codigo.valor;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(valor);
    }

    @Override
    public String toString() {
        return "Producto-" + valor;
    }
}
```

**Errores comunes:** sobrescribir `equals()` sin `hashCode()`; comparar cadenas con `==`; modificar los campos usados como identidad de una clave mientras está almacenada en un mapa.

**Ejercicio:** comparar dos códigos creados por separado con el mismo valor. Explicar por qué `==` y `equals()` pueden producir resultados diferentes.

**Requisitos y conexión:** unidades 4–8. Establece las reglas que utilizarán conjuntos y mapas.

### 12. Excepciones y contratos de error

**Idea central.** Cuando una operación no puede completarse, debe comunicarlo sin dejar el objeto en un estado inválido.

Conceptos que se deben estudiar:

- Jerarquía de excepciones.
- `try`, `catch` y `finally`.
- `throw` para lanzar y `throws` para declarar.
- Excepciones verificadas y no verificadas.
- Excepciones personalizadas.
- Conservación de la causa original.
- Validación antes de modificar el estado.
- `AutoCloseable` y `try-with-resources` para objetos que poseen recursos.

```java
class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(int solicitado) {
        super("No hay stock para " + solicitado + " unidades");
    }
}

class Inventario {
    private int stock = 5;

    public void retirar(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Cantidad no positiva");
        }
        if (unidades > stock) {
            throw new StockInsuficienteException(unidades);
        }
        stock -= unidades;
    }

    public int consultarStock() {
        return stock;
    }
}
```

Esta versión de `Inventario` reemplaza el resultado booleano de la unidad 4 por excepciones. Las excepciones verificadas deben capturarse o declararse. Las subclases de `RuntimeException` no tienen esa obligación. `finally` se utiliza para acciones de cierre, pero no debe enseñarse como una garantía absoluta ante cualquier terminación de la JVM.

**Errores comunes:** capturar y silenciar; usar `catch (Exception)` sin criterio; modificar parcialmente el objeto antes de validar; emplear excepciones para cualquier resultado esperado.

**Ejercicio:** intentar retirar más unidades de las disponibles, capturar la excepción y comprobar que el stock permanece igual.

**Requisitos y conexión:** unidades 4 y 6. Amplía el contrato de los métodos para describir también sus fallos.

### 13. Colecciones y genéricos

**Idea central.** Un pedido contiene varios objetos. Las colecciones permiten agruparlos, y los genéricos permiten indicar qué tipos contienen.

Conceptos que se deben estudiar:

- `List` y `ArrayList`: secuencia con duplicados.
- `Set` y `HashSet`: elementos sin duplicados según su igualdad.
- `Map` y `HashMap`: asociaciones entre claves y valores.
- Programar mediante interfaces.
- Uso de `equals()` y `hashCode()` en conjuntos y mapas basados en hash.
- Clases, interfaces y métodos genéricos.
- Tipos envoltorio: los argumentos genéricos no son tipos primitivos.
- `Comparable` y `Comparator` para ordenar objetos.
- Ampliación: límites de tipos, comodines `? extends` y `? super`, invariancia y borrado de tipos.

```java
import java.util.HashSet;
import java.util.Set;

// Dentro de un método, utilizando CodigoProducto de la unidad 11:
Set<CodigoProducto> codigos = new HashSet<>();

codigos.add(new CodigoProducto(10));
codigos.add(new CodigoProducto(10));

System.out.println(codigos.size()); // 1
```

Una primera clase genérica:

```java
class Caja<T> {
    private final T contenido;

    public Caja(T contenido) {
        this.contenido = contenido;
    }

    public T obtener() {
        return contenido;
    }
}
```

**Errores comunes:** usar tipos sin parámetro, como `List`; asumir que `List<Subtipo>` es un subtipo de `List<Supertipo>`; esperar orden de inserción de cualquier conjunto; exponer una colección interna mutable.

**Ejercicio:** almacenar productos en una lista, impedir códigos repetidos con un conjunto y localizar productos mediante un mapa. Añadir un método genérico sencillo que opere sin conversiones forzadas.

**Requisitos y conexión:** unidades 9–12. Integra contratos, igualdad y protección del estado cuando un objeto administra muchos otros.

## Nivel avanzado — Diseño, evolución y verificación

### 14. Principios de diseño y arquitectura orientada a objetos

**Idea central.** Si una clase calcula pedidos, envía correos y guarda datos, cualquier cambio puede afectar responsabilidades distintas. El diseño debe permitir cambiar una parte con efectos controlados.

Conceptos que se deben estudiar:

- Alta cohesión y bajo acoplamiento.
- Separación de responsabilidades.
- Composición frente a herencia.
- Inyección de dependencias mediante constructores, posible con Java sin frameworks.
- Contratos explícitos, inmutabilidad y control de dependencias entre paquetes.
- Separación entre objetos del dominio, coordinación de casos de uso y adaptadores externos.
- SOLID como principios de diseño, no como características del lenguaje.

| Principio | Pregunta práctica |
|---|---|
| Responsabilidad única — SRP | ¿La clase cambia por motivos pertenecientes a responsabilidades distintas? |
| Abierto/cerrado — OCP | ¿Puedo incorporar una variante mediante una extensión prevista? |
| Sustitución de Liskov — LSP | ¿El subtipo respeta las promesas del tipo general? |
| Segregación de interfaces — ISP | ¿Cada cliente depende solo de las operaciones que necesita? |
| Inversión de dependencias — DIP | ¿Las reglas principales dependen de contratos adecuados? |

```java
// Utiliza Notificador de la unidad 9.
class ServicioPedidos {
    private final Notificador notificador;

    public ServicioPedidos(Notificador notificador) {
        this.notificador = notificador;
    }

    public void confirmar() {
        notificador.enviar("Pedido confirmado");
    }
}
```

**Ampliaciones de modelado en Java:** `enum` para estados y comportamientos finitos; `record` para determinados objetos portadores de datos; clases `sealed` para jerarquías restringidas; clases anidadas cuando expresen una pertenencia clara. Un `record` no garantiza inmutabilidad profunda. Las ampliaciones deben utilizarse con una versión de Java que las admita.

**Errores comunes:** crear una interfaz para cada clase sin necesidad; interpretar SRP como «un solo método»; aplicar herencia violando contratos; introducir capas vacías.

**Ejercicio:** separar una clase que calcula, notifica y almacena pedidos. El resultado debe permitir cambiar el notificador sin modificar las reglas del pedido.

**Requisitos y conexión:** todo el nivel intermedio. Utiliza las herramientas del lenguaje para tomar decisiones de diseño justificadas.

### 15. Patrones de diseño

**Idea central.** Algunos problemas de colaboración y creación se repiten. Los patrones ofrecen estructuras conocidas, con ventajas y costes.

Se deben estudiar el problema, la estructura, las alternativas y las consecuencias de cada patrón.

| Patrón | Problema que resuelve | Cuándo considerarlo |
|---|---|---|
| Singleton | Controlar una instancia compartida | Cuando existe una necesidad real de unicidad |
| Factory | Concentrar decisiones de creación | Cuando crear objetos implica elegir o configurar variantes |
| Strategy | Intercambiar una forma de realizar una operación | Cuando hay reglas alternativas bajo un contrato común |
| Observer | Avisar a varios interesados de un cambio | Cuando el emisor no debe conocer cada reacción concreta |

Ejemplo de **Strategy**:

```java
interface TarifaEntrega {
    int costoEnCentimos();
}

class TarifaEstandar implements TarifaEntrega {
    @Override
    public int costoEnCentimos() {
        return 500;
    }
}

class Cotizador {
    private final TarifaEntrega tarifa;

    public Cotizador(TarifaEntrega tarifa) {
        this.tarifa = tarifa;
    }

    public int cotizar() {
        return tarifa.costoEnCentimos();
    }
}
```

Los otros patrones se implementarán progresivamente mediante:

- **Singleton:** constructor controlado o un `enum` de una instancia; análisis de estado compartido y dificultades de prueba.
- **Factory:** método que devuelve una interfaz y decide qué implementación crear. Distinguir fábrica simple, Factory Method y Abstract Factory.
- **Observer:** interfaz de observador y una colección de suscriptores; registro, eliminación y notificación. Utilizar contratos propios en lugar de las antiguas clases `Observable` y `Observer`.

**Errores comunes:** forzar patrones donde bastaría una clase sencilla; convertir Singleton en un acceso global a todo; confundir Strategy con una cadena creciente de condiciones; no retirar observadores cuando dejan de utilizarse.

**Ejercicio:** incorporar una segunda tarifa sin modificar `Cotizador`; crear una fábrica para seleccionar tarifas y un mecanismo de observación para comunicar cambios del pedido. Justificar por separado si existe alguna necesidad de Singleton.

**Requisitos y conexión:** unidades 9–14. Los patrones combinan polimorfismo, composición y principios de diseño.

### 16. Pruebas del comportamiento de objetos

**Idea central.** Una clase debe conservar sus promesas cuando cambia su implementación. Las pruebas permiten comprobarlo de forma repetible.

Conceptos que se deben estudiar:

- Pruebas unitarias y aislamiento.
- Organización preparar–actuar–comprobar.
- JUnit Jupiter, `@Test` y aserciones.
- Pruebas de constructores, métodos, invariantes y excepciones.
- Casos válidos, inválidos y valores límite.
- Pruebas parametrizadas.
- Objetos de prueba: fakes, stubs y mocks; diferencias y uso justificado.
- Pruebas de contratos compartidos por varias implementaciones.
- Diferencia entre prueba unitaria y prueba de integración.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CodigoProductoTest {
    @Test
    void mismoValorRepresentaElMismoCodigo() {
        CodigoProducto primero = new CodigoProducto(10);
        CodigoProducto segundo = new CodigoProducto(10);

        assertEquals(primero, segundo);
        assertEquals(primero.hashCode(), segundo.hashCode());
    }
}
```

El ejemplo utiliza `CodigoProducto` de la unidad 11 y requiere JUnit Jupiter configurado en el proyecto de pruebas. JUnit es una herramienta externa al lenguaje. En Jupiter, las pruebas se declaran con `@Test` y utilizan aserciones como `assertEquals`; `assertThrows` permite verificar excepciones esperadas. Véanse [pruebas con JUnit](https://docs.junit.org/6.1.0/writing-tests/intro.html) y [verificación de excepciones](https://docs.junit.org/6.1.1/writing-tests/exception-handling.html).

**Errores comunes:** probar únicamente casos felices; depender del orden de ejecución; comprobar detalles privados; sustituir todas las colaboraciones por mocks; confundir cobertura con calidad.

**Ejercicio:** probar retiradas válidas, cantidad cero, cantidad negativa y stock insuficiente. Tras cada fallo, verificar que el inventario conserva su estado anterior.

**Requisitos y conexión:** unidades 4, 11, 12 y 14. La unidad profundiza en pruebas, pero las comprobaciones sencillas deben acompañar los ejercicios desde las primeras clases.

## Qué dominar antes de avanzar

| Siguiente etapa | Dominio imprescindible | Puede profundizarse después |
|---|---|---|
| **Estructuras de datos en Java** | Clases, referencias compartidas, encapsulamiento, composición, igualdad, genéricos y contratos de colecciones | SOLID completo y catálogo de patrones |
| **Aplicaciones Java** | Nivel básico e intermedio; validación, excepciones, composición, separación de responsabilidades y pruebas básicas | Patrones complejos y arquitecturas con muchas capas |
| **Frameworks Java** | Interfaces, polimorfismo, composición, inyección por constructor, genéricos, excepciones y pruebas | Detalles internos del framework y mecanismos avanzados |

Antes de frameworks también conviene reconocer qué son las anotaciones y comprender, de forma introductoria, que la reflexión permite inspeccionar tipos en ejecución. Estos mecanismos de Java ayudan a explicar cómo algunas herramientas construyen y conectan objetos; no sustituyen el aprendizaje de POO.

## Proyecto integrador

La comprobación final será un **modelo de pedidos implementado sin frameworks**.

- **Punto de partida:** las clases y contratos desarrollados en los ejercicios anteriores.
- **Objetivo:** integrar productos con códigos comparables, pedidos con líneas protegidas, inventario validado, tarifas intercambiables y notificaciones mediante interfaces.
- **Restricciones:** conservar las invariantes, evitar estado global mutable y no exponer colecciones internas modificables. Utilizar Java y su biblioteca estándar; JUnit se incorpora para las pruebas.
- **Resultado esperado:** explicar las relaciones del modelo, añadir una variante sin romper contratos y demostrar con pruebas que las operaciones inválidas conservan el estado.
- **Reto opcional:** añadir un segundo observador del estado del pedido sin modificar las reglas de inventario ni el cálculo de tarifas.

No se incluye una solución completa: el objetivo es justificar y comprobar las decisiones del modelo.
