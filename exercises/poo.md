# Ejercicios de POO en Java

> Adaptación corregida de los [ejercicios originales de Brianmax/javaG7](https://github.com/Brianmax/javaG7/blob/main/ejercicios/poo.md). Se conservan los once temas y su numeración.

## Antes de empezar

Usa las siguientes reglas para validar tus soluciones y comprobar sus resultados.

- Resuelve cada ejercicio en un proyecto o paquete independiente, por ejemplo, `ejercicio01`. Así podrás reutilizar nombres como `Main` o `Empleado` sin conflictos. El ejercicio 3 reutiliza la clase del ejercicio 2.

- Usa atributos privados, constructores para crear objetos válidos y getters cuando necesites consultar datos. Agrega setters solo para cambios permitidos; no expongas setters para identificadores, colecciones completas ni estados calculados.

- Usa nombres de métodos en `lowerCamelCase` y una clase pública por archivo. Puedes trabajar con `List<T>` y `ArrayList<T>` desde el ejercicio 3.

- Desde el ejercicio 2, rechaza textos obligatorios vacíos o nulos y valores fuera de los rangos indicados. Puedes lanzar `IllegalArgumentException` para datos inválidos y capturarla en `Main` al demostrar esos casos. Una operación rechazada debe conservar el estado anterior.

- En las colecciones, los identificadores son textos obligatorios, únicos e inmutables. Una operación de agregar, eliminar, prestar, devolver o matricular devuelve `boolean`: `true` si se realizó y `false` si incumple una regla de negocio. Las búsquedas devuelven el objeto o `null` si no existe. No agregues objetos nulos.

- Las consultas que devuelvan listas deben entregar una copia o una vista no modificable. Solo la clase responsable debe modificar su colección.

- Para estos ejercicios puedes representar montos con `double` y mostrarlos con dos decimales. Usa valores finitos y no redondees resultados intermedios. No se requiere usar bases de datos, frameworks ni archivos.

- Incluye un `Main` con casos válidos e inválidos y evidencia de sus resultados. Solo el ejercicio 7 requiere un menú interactivo; en los demás puedes crear los datos directamente en el código.

**Recorrido sugerido:** 1 → 2 → 3 → 4 → 5 → 10 → 6 → 7 → 11 → 8 → 9. El registro de vehículos permite practicar polimorfismo antes de gestionar préstamos.

## Elige un ejercicio

1. [Dog: clases y constructores](#1-crea-un-perro)

2. [Computer: métodos y encapsulación](#2-actualiza-una-computadora)

3. [ComputerStore: colecciones y composición](#3-administra-una-tienda-de-computadoras)

4. [Pensión universitaria: reglas de cálculo](#4-calcula-una-pension-universitaria)

5. [Planilla: empleados, pagos y conteos](#5-calcula-la-planilla-de-empleados)

6. [Biblioteca: herencia y préstamos](#6-gestiona-prestamos-de-una-biblioteca)

7. [Alquiler: polimorfismo y menú de consola](#7-gestiona-el-alquiler-de-peliculas-y-videojuegos)

8. [Inventario: interfaces y responsabilidades](#8-controla-un-inventario-de-productos)

9. [Matrículas: personas con varios roles](#9-gestiona-matriculas-y-roles-educativos)

10. [Vehículos: registro polimórfico](#10-registra-diferentes-vehiculos)

11. [Personal: salarios por especialidad](#11-gestiona-personal-y-salarios)

## 1. Crea un perro

Crea una clase `Dog` con los atributos privados `breed: String`, `age: int` y `color: String`.

- Implementa un constructor que reciba los tres valores y los asigne a los atributos usando `this`.

- Agrega getters para consultar los atributos desde otra clase.

- En `Main`, crea dos perros con datos diferentes e imprime sus atributos usando los getters.

**Comprueba:** un perro con raza `Beagle`, edad `3` y color `Tricolor` debe mostrar esos datos. El segundo perro debe conservar sus propios valores. En este primer ejercicio puedes asumir entradas válidas.

## 2. Actualiza una computadora

Crea `Computer` con `brand: String`, `model: String`, `ram: int` y `storage: int`. Expresa RAM y almacenamiento en GB.

- El constructor recibe los cuatro atributos. RAM y almacenamiento deben ser mayores que cero.

- Implementa getters y `void showDetails()` para imprimir marca, modelo, RAM y almacenamiento con sus unidades.

- Implementa `void increaseRam(int additionalGb)` y `void increaseStorage(int additionalGb)`. Cada argumento representa una cantidad que se suma al valor actual y debe ser mayor que cero.

- En `Main`, muestra la computadora antes y después de las ampliaciones.

**Comprueba:** una computadora con 8 GB de RAM y 256 GB de almacenamiento, al recibir incrementos de 8 y 256, debe quedar con 16 GB y 512 GB. Un incremento de 0 o −4 debe rechazarse sin modificarla.

## 3. Administra una tienda de computadoras

Reutiliza `Computer` y añade `serialNumber: String` como identificador inmutable de cada equipo físico. El constructor de esta versión recibe el número de serie y los cuatro datos del ejercicio 2.

Crea `ComputerStore` con `name: String`, `address: String` y `computers: List<Computer>`. Su constructor recibe nombre y dirección e inicializa una lista vacía.

- `boolean addComputer(Computer computer)`: agrega un equipo si no hay otro con el mismo número de serie.

- `boolean sellComputer(String serialNumber)`: elimina del inventario el equipo vendido; devuelve `false` si no existe.

- `int getQuantity()`: devuelve `computers.size()`. No almacenes un atributo `quantity` que pueda desincronizarse.

- `List<Computer> filterByRam(int minimumGb)`: devuelve los equipos con RAM **estrictamente mayor** al límite, sin modificar el inventario. El límite debe ser mayor o igual que cero.

- `void showDetails()`: muestra nombre, dirección, cantidad y detalles de los equipos disponibles.

**Comprueba:** agrega equipos con series `C1`, `C2` y `C3` y RAM de 8, 16 y 32 GB. La cantidad es 3 y filtrar por 16 devuelve solo `C3`. Vender `C2` deja 2 equipos; volver a venderlo devuelve `false`. Una serie duplicada no aumenta la cantidad.

## 4. Calcula una pensión universitaria

Crea `Pension` con los atributos privados `categoria: String` y `promedio: double`. El constructor recibe una categoría válida (`A`, `B`, `C` o `D`) y un promedio entre 0 y 20, ambos límites incluidos.

**Monto mensual por categoría**

| Categoría | Pensión base ($) |
| --- | --- |
| A | 550.00 |
| B | 500.00 |
| C | 460.00 |
| D | 400.00 |

**Descuento según el promedio, sin redondearlo**

| Promedio | Descuento |
| --- | --- |
| 0 ≤ promedio < 14 | 0 % |
| 14 ≤ promedio < 16 | 10 % |
| 16 ≤ promedio < 18 | 12 % |
| 18 ≤ promedio ≤ 20 | 15 % |

- `double calcularMontoBase()`: devuelve el monto correspondiente a la categoría.

- `double calcularTasaDescuento()`: devuelve `0.0`, `0.10`, `0.12` o `0.15`.

- `double calcularDescuento()`: monto base × tasa.

- `double calcularPensionFinal()`: monto base − descuento.

En `Main`, muestra categoría, promedio, monto base, descuento en dinero y monto final. El ejercicio calcula una mensualidad; no requiere registrar un historial de pagos.

**Comprueba:** categoría A y promedio 18 producen una base de $550.00, descuento de $82.50 y pago de $467.50. Para B: 13.99 → $500.00; 14 → $450.00; 16 → $440.00; 18 y 20 → $425.00. Un promedio de 13.995 sigue sin descuento. Rechaza la categoría E y promedios fuera de 0 a 20.

## 5. Calcula la planilla de empleados

Crea `Empleado` con `id: String`, `nombres: String`, `apellidos: String`, `cargo: String`, `modalidad: String` y `horasTrabajadas: double`. Las horas corresponden a un mismo mes y pueden ser cero, pero no negativas.

**Tarifas del ejercicio**

| Cargo | Pago por hora ($) |
| --- | --- |
| Gerente | 20.00 |
| Administrativo | 10.00 |
| Jefe | 8.50 |
| Operario | 3.50 |

Las modalidades válidas son `Tiempo Completo`, con bonificación del 20 %, y `Tiempo Parcial`, con bonificación del 5 %. Acepta únicamente los cargos y modalidades indicados; puedes representarlos con `String` o con enums si ya los conoces.

- En `Empleado`, implementa `calcularPagoBase()`, `calcularBonificacion()` y `calcularPagoTotal()`, todos con retorno `double`.

- Pago base = horas × tarifa del cargo. Bonificación = pago base × tasa de la modalidad. Pago total = base + bonificación. No incluyas horas extra ni descuentos adicionales.

- Crea `Planilla` con una lista inicialmente vacía y `boolean registrarEmpleado(Empleado empleado)`, rechazando IDs repetidos.

- Implementa `int contarEmpleados()`, `int contarPorCargo(String cargo)` y `double calcularTotalPlanilla()`. Si usas enums, adapta el tipo del parámetro de cargo.

- En `Main`, muestra el pago individual, el total registrado, el conteo de cada uno de los cuatro cargos y la suma de pagos.

**Comprueba:** un gerente a tiempo completo con 10 horas cobra $240.00; un operario a tiempo parcial con 10 horas cobra $36.75. Al registrar solo esos dos empleados, el total es 2, los conteos son 1 gerente, 0 administrativos, 0 jefes y 1 operario, y la planilla suma $276.75. Una planilla vacía tiene conteos y total en cero.

## 6. Gestiona préstamos de una biblioteca

Modela libros y DVDs que pueden prestarse a usuarios registrados.

### Clases

- `LibraryItem`: clase abstracta con `title: String`, `itemID: String` y `loanedTo: LibraryUser`, inicialmente `null`. Incluye constructor, getters, `boolean isLoaned()` (verdadero cuando `loanedTo != null`) y `abstract void showDetails()`.

- `Book extends LibraryItem`: agrega `author: String` y sobrescribe `showDetails()`.

- `DVD extends LibraryItem`: agrega `director: String` y `duration: int` en minutos, mayor que cero. Sobrescribe `showDetails()`.

- `LibraryUser`: contiene `userName: String` y `userID: String`, con constructor, getters y `void showUserDetails()`.

- `Library`: contiene `libraryItems: List<LibraryItem>` y `users: List<LibraryUser>`, inicialmente vacías.

El préstamo se guarda en `loanedTo`. No mantengas otra lista mutable de préstamos dentro del usuario: obtén sus préstamos consultando la biblioteca. El cambio de prestatario se realiza mediante métodos internos del paquete llamados por `Library`; no expongas un setter público.

### Operaciones de Library

- `boolean addItem(LibraryItem item)` y `boolean registerUser(LibraryUser user)`: rechazan IDs repetidos en su respectiva colección.

- `boolean loanItem(String itemID, String userID)`: requiere un ítem existente y disponible y un usuario registrado; asigna ese usuario como prestatario.

- `boolean returnItem(String itemID, String userID)`: requiere que el ítem esté prestado precisamente a ese usuario; establece `loanedTo` en `null`.

- `List<LibraryItem> getLoanedItems(String userID)`: devuelve los ítems prestados a ese usuario; devuelve una lista vacía si no existe o no tiene préstamos.

- `void showAllItems()`: recorre la colección y llama a `showDetails()`; cada subtipo muestra sus datos específicos y si está prestado.

En `Main`, registra libros, DVDs y dos usuarios; muestra sus detalles, realiza préstamos y devoluciones y consulta los préstamos de cada usuario. Las mismas operaciones deben aceptar ambos tipos de ítem sin distinguirlos mediante condicionales de tipo.

**Comprueba:** prestar el libro `B1` al usuario `U1` devuelve `true`. Prestarlo después a `U2` o devolverlo como `U2` devuelve `false`. Devolverlo como `U1` lo deja disponible y elimina ese resultado de la consulta de préstamos de `U1`. Repetir la devolución devuelve `false`. Demuestra también un préstamo de DVD.

## 7. Gestiona el alquiler de películas y videojuegos

Construye un videoclub con dos tipos de artículos para demostrar polimorfismo. El videojuego amplía el enunciado original, que solo definía películas.

### Clases

- `RentalItem`: clase abstracta con `itemName: String`, `itemID: String` y `rentedTo: Customer`, inicialmente `null`. Define `boolean isAvailable()` a partir de si `rentedTo` es `null` y `abstract void showDetails()`.

- `Movie extends RentalItem`: agrega `director: String`, `duration: int` (minutos positivos), `ageRating: String` y `genre: String`.

- `VideoGame extends RentalItem`: agrega `platform: String` y `ageRating: String`.

- `Customer`: contiene `customerName: String`, `customerID: String` y `void showCustomerDetails()`.

- `MovieRentalSystem`: contiene `rentalItems: List<RentalItem>` y `customers: List<Customer>`, inicialmente vacías.

Los constructores reciben los datos obligatorios de cada clase. Ambos subtipos sobrescriben `showDetails()`. La clasificación por edades es informativa; no se requiere comprobar la edad del cliente ni cobrar alquileres.

### Operaciones y reglas

- `boolean addItem(RentalItem item)` y `boolean registerCustomer(Customer customer)`: rechazan IDs duplicados en su colección.

- `boolean rentItemToCustomer(String itemID, String customerID)`: alquila un artículo existente y disponible a un cliente registrado.

- `boolean returnItem(String itemID, String customerID)`: permite devolverlo únicamente al cliente que lo tiene alquilado.

- `List<RentalItem> getRentedItems(String customerID)`: calcula los alquileres consultando `rentedTo`; devuelve una lista vacía para un cliente inexistente o sin alquileres.

- `void showAllItems()`: muestra todos los artículos y su disponibilidad; `void showAvailableItems()`: muestra solo los disponibles; `void showAllCustomers()`: muestra los clientes y sus alquileres.

Como en la biblioteca, guarda el estado del alquiler solo en `rentedTo` y permite que el sistema lo cambie mediante métodos internos del paquete. No agregues setters públicos de disponibilidad ni una segunda lista mutable en el cliente.

En `Main`, carga al menos dos películas, un videojuego y dos clientes. Implementa un menú con `Scanner` para listar artículos, listar disponibles, alquilar, devolver, mostrar clientes y salir. Lee las opciones como texto para que una opción desconocida muestre un mensaje y vuelva al menú. Toda operación debe informar si se realizó.

**Comprueba:** alquila una película y un videojuego al mismo cliente usando el mismo método. Ambos desaparecen de la lista de disponibles. Rechaza otro alquiler del mismo artículo, IDs inexistentes y una devolución por un cliente diferente. Una devolución válida vuelve a mostrar el artículo como disponible. La opción de salir debe terminar el programa.

## 8. Controla un inventario de productos

Usa una interfaz para el precio de cada producto y otra para las operaciones de la colección. Un producto tiene precio; el inventario agrega, busca y elimina productos.

### Interfaces

- `Priceable`: declara `double getPrice()` y `void setPrice(double price)`.

- `InventoryManageable`: declara `boolean addItem(Product product)`, `boolean deleteItem(String productId)`, `List<Product> listItems()` y `Product findItemById(String productId)`.

### Clases

- `Product implements Priceable`: clase abstracta con `productId: String` y `price: double`, ambos privados. Implementa la interfaz, valida un precio mayor o igual que cero y declara `abstract void showDetails()`.

- `Electronics extends Product`: agrega `brand: String` y `model: String`.

- `Clothing extends Product`: agrega `size: String` y `material: String`.

- `Inventory implements InventoryManageable`: contiene una lista privada `products: List<Product>`, inicialmente vacía, e implementa las cuatro operaciones. Rechaza IDs repetidos.

Agrega constructores y getters en las clases de productos y sobrescribe `showDetails()` en ambos subtipos. `Electronics` y `Clothing` heredan el comportamiento de precio de `Product`; no implementan `InventoryManageable`.

En `Main`, usa una referencia `InventoryManageable` para trabajar con un `Inventory`. Agrega productos de ambos tipos, lista sus detalles, busca uno por ID, comprueba que exista y cambia su precio mediante una referencia `Priceable`. Finalmente elimina un producto. Cada registro representa un producto identificado; no se requiere controlar cantidades ni elaborar reportes adicionales.

**Comprueba:** registra `E1` a $1200.00 y `C1` a $50.00. Cambia `C1` a $45.00 y verifica el nuevo precio al buscarlo. Un precio negativo se rechaza y conserva $45.00. Agregar otro `E1` devuelve `false`. Eliminar `E1` deja un producto; buscar el eliminado devuelve `null`. Modificar la lista devuelta por `listItems()` no debe modificar la colección interna.

## 9. Gestiona matrículas y roles educativos

Modela personas que pueden ser estudiantes, profesores o ambos. Java permite implementar varias interfaces y extender una sola clase. En este ejercicio las interfaces representan roles.

### Interfaces

- `Usuario`: `void iniciarSesion()` y `void cerrarSesion()`.

- `Estudiante`: `String getId()`, `String getNombre()`, `boolean matricularCurso(Curso curso)` y `boolean entregarTarea(Curso curso, String tarea)`.

- `Profesor`: `String getId()`, `String getNombre()`, `boolean asignarTarea(Curso curso, String tarea)` y `boolean calificarExamen(Curso curso, Estudiante estudiante, double nota)`.

### Personas

- `Persona implements Usuario`: clase **abstracta** con `id: String` y `nombre: String`, constructor y getters. Declara `iniciarSesion()` abstracto e implementa `cerrarSesion()` mostrando un mensaje común.

- `SoloEstudiante extends Persona implements Estudiante`: implementa las operaciones de estudiante.

- `SoloProfesor extends Persona implements Profesor`: implementa las operaciones de profesor.

- `EstudianteProfesor extends Persona implements Estudiante, Profesor`: implementa ambos roles.

Cada clase concreta implementa `iniciarSesion()` mostrando el nombre y su rol: estudiante, profesor o estudiante y profesor. Es una simulación por consola, sin contraseñas ni autenticación real. No se exige iniciar sesión para ejecutar las otras operaciones. Los getters heredados de `Persona` satisfacen los métodos comunes de ambas interfaces.

### Curso como responsable de la matrícula

Crea `Curso` con `nombre: String`, `codigo: String`, `docente: Profesor` y `estudiantes: List<Estudiante>`. Su constructor recibe nombre, código y un docente no nulo; la lista empieza vacía. Cada curso tiene un solo docente.

- `boolean matricular(Estudiante estudiante)`: agrega un estudiante si su ID no está registrado y no coincide con el del docente. Una persona puede enseñar en un curso y estudiar en otro.

- `boolean estaMatriculado(String estudianteId)`, `boolean esDocente(String profesorId)`, `List<Estudiante> getEstudiantes()` y getters para nombre, código y docente.

- `matricularCurso(curso)` delega en `curso.matricular(this)`. No mantengas listas de cursos en cada persona: para este ejercicio, la matrícula se almacena únicamente en `Curso`.

### Tareas y calificaciones

Para mantener el alcance centrado en interfaces, estas acciones solo validan las reglas y muestran un mensaje; no almacenan tareas, entregas ni notas.

- `entregarTarea`: exige un curso no nulo, un texto de tarea no vacío y que quien entrega esté matriculado. Muestra estudiante, tarea y curso.

- `asignarTarea`: exige un curso no nulo, un texto de tarea no vacío y que quien asigna sea el docente del curso. Muestra docente, tarea y curso.

- `calificarExamen`: exige un curso y estudiante no nulos, que quien califica sea el docente, que el estudiante esté matriculado y una nota entre 0 y 20, inclusive. Muestra docente, estudiante, curso y nota.

- Las acciones devuelven `false` cuando el rol no tiene permiso para el curso; para argumentos inválidos aplica la validación general. No se requiere comprobar una asignación previa para simular una entrega.

En `Main`, crea un `SoloEstudiante`, un `SoloProfesor`, un `EstudianteProfesor` y dos cursos. Usa IDs distintos para las personas y códigos distintos para los cursos. No instancies `Persona`; puedes usarla como tipo de referencia para recorrer las personas y demostrar sus diferentes mensajes de inicio de sesión.

**Comprueba:** Ana (`SoloProfesor`) enseña Java; Luis (`EstudianteProfesor`) enseña Bases de Datos y se matricula en Java; Eva (`SoloEstudiante`) se matricula en Bases de Datos. Luis puede entregar una tarea en Java y calificar a Eva con 18 en Bases de Datos. No puede calificarla en Java ni matricularse en el curso que él enseña. Rechaza una matrícula duplicada y una nota de 21.

## 10. Registra diferentes vehículos

Crea una jerarquía de vehículos y una clase que gestione su registro.

- `Vehicle`: clase abstracta con `make: String`, `model: String`, `year: int` (positivo) y `registrationNumber: String` (matrícula inmutable). Incluye constructor, getters y `abstract void showDetails()`.

- `Car extends Vehicle`: agrega `seatingCapacity: int` (positivo, incluye al conductor) y `fuelType: String`.

- `Motorcycle extends Vehicle`: agrega `engineSize: int` (cilindrada positiva en cm³) y `hasSidecar: boolean`.

- `Truck extends Vehicle`: agrega `loadCapacity: double` (capacidad positiva en kg) y `numberOfAxles: int` (al menos 2).

Cada subtipo tiene un constructor que llama a `super`, getters y una sobrescritura de `showDetails()` que muestra datos comunes, datos específicos y unidades.

Crea `VehicleRegistry` con `registeredVehicles: List<Vehicle>`, inicialmente vacía, y estos métodos:

- `boolean registerVehicle(Vehicle vehicle)`: agrega un vehículo si su matrícula no está registrada.

- `Vehicle findVehicle(String registrationNumber)`: busca por matrícula; devuelve `null` si no existe.

- `void showAllVehicles()`: recorre la lista y llama a `showDetails()` sin usar `instanceof` ni conversiones a subtipos.

**Comprueba:** registra un automóvil `A1`, una motocicleta `M1` y un camión `T1`. El listado debe mostrar los atributos específicos de los tres. Buscar `M1` devuelve la motocicleta; buscar `X1` devuelve `null`. Intentar registrar otro vehículo con matrícula `A1`, incluso de otro subtipo, devuelve `false`.

## 11. Gestiona personal y salarios

Desarrolla una empresa que administre distintos tipos de empleados. Las fórmulas siguientes son reglas didácticas añadidas para que el cálculo tenga un resultado verificable.

### Jerarquía de empleados

- `Empleado`: clase abstracta con `nombre: String`, `id: String`, `departamento: String` y `salarioBase: double` (mayor o igual que cero). Incluye constructor, getters, `String mostrarInfo()` y `abstract double calcularSalario()`. `mostrarInfo()` debe incluir el salario calculado.

- `Desarrollador extends Empleado`: añade `lenguajes: List<String>`. Recibe una lista no nula de nombres no vacíos y sin repetidos, y guarda una copia. Su salario mensual es **salario base + $100.00 por lenguaje distinto**. Compara los nombres sin distinguir mayúsculas y minúsculas.

- `Gerente extends Empleado`: añade `empleadosACargo: List<Empleado>`, inicialmente vacía. Su salario mensual es **salario base + $150.00 por empleado a cargo**. La empresa gestiona esta lista mediante métodos internos del paquete.

- `Disenador extends Empleado`: añade `tipoDiseno: String`, con valores válidos `Grafico`, `UX` o `UI`. Su salario mensual es **salario base + $200.00** para Grafico, **+ $300.00** para UX o **+ $250.00** para UI.

Las tres clases concretas sobrescriben `calcularSalario()`. Para la lista de lenguajes, puedes rechazar duplicados mediante `IllegalArgumentException`; una lista vacía es válida y no genera bonificación.

### Empresa

Crea `Empresa` con una lista privada inicialmente vacía `empleados: List<Empleado>` y estos métodos:

- `boolean agregarEmpleado(Empleado empleado)`: rechaza IDs duplicados.

- `boolean asignarEmpleadoAGerente(String empleadoId, String gerenteId)`: requiere que ambos estén registrados, que el responsable sea un gerente y que el empleado no sea gerente ni esté ya asignado a otro gerente. Cada empleado puede pertenecer como máximo a un equipo; los gerentes no forman parte de equipos en este ejercicio.

- `boolean eliminarEmpleado(String id)`: elimina al empleado y lo retira de cualquier equipo al que pertenezca. Si se elimina un gerente, vacía su equipo y sus integrantes permanecen en la empresa sin gerente asignado.

- `void mostrarEmpleados()`: imprime la información de cada empleado.

- `double calcularTotalSalarios()`: suma `calcularSalario()` recorriendo la colección. No repitas las fórmulas salariales dentro de `Empresa`.

En `Main`, registra los tres tipos, asigna empleados a un gerente, muestra los salarios y elimina un empleado para comprobar que el equipo y su bonificación se actualizan.

**Comprueba:** un desarrollador con base de $2000.00 y dos lenguajes cobra $2200.00; un diseñador UX con base de $1800.00 cobra $2100.00; un gerente con base de $3000.00 y esos dos empleados a cargo cobra $3300.00. El total es $7600.00. Tras eliminar al diseñador, el gerente cobra $3150.00 y el total pasa a $5350.00. Rechaza una segunda asignación del mismo empleado, una autoasignación del gerente y un ID duplicado.
