# Ejercicios de POO en Java

> Adaptación corregida de los [ejercicios originales de Brianmax/javaG7](https://github.com/Brianmax/javaG7/blob/main/ejercicios/poo.md). Se conservan los once temas y su numeración.

## Indicaciones generales

- Resuelve cada ejercicio en un paquete independiente, por ejemplo, `ejercicio01`. Así puedes reutilizar nombres como `Main` o `Empleado` sin conflictos.
- Salvo indicación contraria, los atributos deben ser privados. Usa constructores para crear objetos válidos y getters para consultarlos. Agrega setters solo cuando el cambio esté permitido.
- Rechaza valores obligatorios nulos o vacíos y valores fuera de los límites indicados. Puedes lanzar `IllegalArgumentException` en los constructores y setters.
- Los identificadores son únicos e inmutables. Las operaciones de agregar, eliminar, prestar, devolver o matricular devuelven `true` si se realizan y `false` si una regla de negocio las impide.
- Devuelve copias o vistas no modificables de las colecciones. La clase responsable es la única que debe cambiar su colección.
- En los ejercicios con dinero, usa `double` y muestra los montos con dos decimales. No redondees resultados intermedios.
- Incluye un `Main` que demuestre los casos indicados. Solo el ejercicio 7 requiere un menú interactivo.
- Recorrido sugerido: 1 → 2 → 3 → 4 → 5 → 10 → 6 → 7 → 11 → 8 → 9.

## Ejercicio 1: clases y constructores

### Clases


#### `Dog`

- **Descripción:** Representa un perro.
- **Atributos privados:**
  - `breed: String`
  - `age: int`
  - `color: String`.
- **Métodos:**
  - Constructor que recibe los tres atributos y getters para consultarlos.

#### `Main`

- **Descripción:** Crea perros y muestra sus datos.
- **Métodos:**
  - En `main`, crea dos objetos `Dog` con datos diferentes e imprime sus atributos mediante los getters.

**Comprueba:** un perro con raza `Beagle`, edad `3` y color `Tricolor` debe mostrar esos datos. El segundo perro debe conservar sus propios valores.

## Ejercicio 2: encapsulación y métodos

### Clases


#### `Computer`

- **Descripción:** Representa una computadora. RAM y almacenamiento se expresan en GB.
- **Atributos privados:**
  - `brand: String`
  - `model: String`
  - `ram: int`
  - `storage: int`.
- **Métodos:**
  - Constructor con los cuatro atributos
  - getters
  - `showDetails()` para imprimir marca, modelo, RAM y almacenamiento
  - `increaseRam(int additionalGb)` y `increaseStorage(int additionalGb)` para ampliar las capacidades existentes.
- **Reglas:** RAM y almacenamiento iniciales deben ser mayores que cero. Cada ampliación debe ser mayor que cero y se suma al valor actual.

#### `Main`

- **Descripción:** Crea una computadora y muestra sus cambios.
- **Métodos:**
  - En `main`, muestra los datos antes y después de ampliar RAM y almacenamiento.

**Comprueba:** una computadora con 8 GB de RAM y 256 GB de almacenamiento, al recibir incrementos de 8 y 256, queda con 16 GB y 512 GB. Un incremento de 0 o −4 se rechaza sin modificarla.

## Ejercicio 3: colecciones y composición

### Clases


#### `Computer`

- **Descripción:** Representa un equipo físico que puede vender una tienda.
- **Atributos privados:**
  - `serialNumber: String` (identificador único e inmutable)
  - `brand: String`
  - `model: String`
  - `ram: int` (GB)
  - `storage: int` (GB).
- **Métodos:**
  - Constructor con número de serie y los cuatro atributos del ejercicio 2
  - getters
  - `showDetails()`
  - `increaseRam(int additionalGb)` e `increaseStorage(int additionalGb)`. Las ampliaciones deben ser mayores que cero.

#### `ComputerStore`

- **Descripción:** Administra el inventario de computadoras de una tienda.
- **Atributos privados:**
  - `name: String`
  - `address: String`
  - `computers: List<Computer>`.
- **Métodos:**
  - Constructor con nombre y dirección que inicializa una lista vacía
  - `boolean addComputer(Computer computer)`
  - `boolean sellComputer(String serialNumber)`
  - `int getQuantity()`
  - `List<Computer> filterByRam(int minimumGb)`
  - `showDetails()`.
- **Reglas:** Rechaza números de serie duplicados. La cantidad se obtiene de `computers.size()` y no se almacena por separado. El filtro devuelve equipos con RAM **estrictamente mayor** al límite, sin modificar el inventario. El límite debe ser mayor o igual que cero.

#### `Main`

- **Descripción:** Demuestra el registro, la búsqueda por RAM y la venta de equipos.
- **Métodos:**
  - Agrega computadoras, consulta cantidad e información, filtra por RAM y vende una computadora.

**Comprueba:** agrega equipos con series `C1`, `C2` y `C3` y RAM de 8, 16 y 32 GB. La cantidad es 3 y filtrar por 16 devuelve solo `C3`. Vender `C2` deja 2 equipos; volver a venderlo devuelve `false`. Una serie duplicada no aumenta la cantidad.

## Ejercicio 4: cálculo de pensión

### Clases


#### `Pension`

- **Descripción:** Calcula el pago mensual de un estudiante según su categoría y promedio.
- **Atributos privados:**
  - `categoria: String`
  - `promedio: double`.
- **Métodos:**
  - Constructor
  - `calcularMontoBase()`
  - `calcularTasaDescuento()`
  - `calcularDescuento()`
  - `calcularPensionFinal()`
  - getters.
- **Reglas:** La categoría debe ser `A`, `B`, `C` o `D`; el promedio debe estar entre 0 y 20, inclusive. Aplica el descuento usando el promedio sin redondear.

| Categoría | Pensión base ($) |
| --- | ---: |
| A | 550.00 |
| B | 500.00 |
| C | 460.00 |
| D | 400.00 |

| Promedio | Descuento |
| --- | ---: |
| 0 ≤ promedio < 14 | 0 % |
| 14 ≤ promedio < 16 | 10 % |
| 16 ≤ promedio < 18 | 12 % |
| 18 ≤ promedio ≤ 20 | 15 % |

#### `Main`

- **Descripción:** Muestra el cálculo de pensión de un estudiante.
- **Métodos:**
  - En `main`, imprime categoría, promedio, monto base, descuento en dinero y monto final.

**Comprueba:** categoría A y promedio 18 producen una base de $550.00, descuento de $82.50 y pago de $467.50. Para B: 13.99 → $500.00; 14 → $450.00; 16 → $440.00; 18 y 20 → $425.00. Un promedio de 13.995 no recibe descuento. Rechaza categoría E y promedios fuera de 0 a 20.

## Ejercicio 5: planilla de empleados

### Clases


#### `Empleado`

- **Descripción:** Representa a un empleado que cobra por hora.
- **Atributos privados:**
  - `id: String`
  - `nombres: String`
  - `apellidos: String`
  - `cargo: String`
  - `modalidad: String`
  - `horasTrabajadas: double`.
- **Métodos:**
  - Constructor
  - `calcularPagoBase()`
  - `calcularBonificacion()`
  - `calcularPagoTotal()`
  - getters.
- **Reglas:** Las horas del mismo mes pueden ser cero, pero no negativas. Los cargos y modalidades solo pueden tomar los valores indicados en las tablas.

| Cargo | Pago por hora ($) |
| --- | ---: |
| Gerente | 20.00 |
| Administrativo | 10.00 |
| Jefe | 8.50 |
| Operario | 3.50 |

| Modalidad | Bonificación |
| --- | ---: |
| Tiempo Completo | 20 % |
| Tiempo Parcial | 5 % |

El pago base es horas × tarifa. La bonificación es pago base × tasa de modalidad. El pago total es pago base + bonificación. No se consideran horas extra ni descuentos.

#### `Planilla`

- **Descripción:** Registra empleados y resume sus pagos.
- **Atributos privados:**
  - `empleados: List<Empleado>`
  - inicialmente vacía.
- **Métodos:**
  - `boolean registrarEmpleado(Empleado empleado)`
  - `int contarEmpleados()`
  - `int contarPorCargo(String cargo)`
  - `double calcularTotalPlanilla()`.
- **Reglas:** Rechaza IDs repetidos. El total es la suma del pago total de cada empleado.

#### `Main`

- **Descripción:** Muestra pagos individuales y un resumen de la planilla.
- **Métodos:**
  - Registra empleados y muestra el pago de cada uno, el total de empleados, el conteo por cada cargo y la suma de pagos.

**Comprueba:** un gerente a tiempo completo con 10 horas cobra $240.00; un operario a tiempo parcial con 10 horas cobra $36.75. Juntos suman $276.75. Los conteos son 1 gerente, 0 administrativos, 0 jefes y 1 operario. Una planilla vacía tiene conteos y total en cero.

## Ejercicio 6: biblioteca con préstamos

### Clases


#### `LibraryItem`

- **Descripción:** Clase base para los elementos prestables de la biblioteca.
- **Atributos privados:**
  - `title: String`
  - `itemID: String`
  - `loanedTo: LibraryUser` (inicialmente `null`).
- **Métodos:**
  - Constructor
  - getters
  - `isLoaned()`
  - `showDetails()` abstracto. `isLoaned()` devuelve `true` cuando existe un prestatario.

#### `Book` (hereda de `LibraryItem`)

- **Descripción:** Representa un libro.
- **Atributos privados:**
  - `author: String`.
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `showDetails()` para incluir el autor.

#### `DVD` (hereda de `LibraryItem`)

- **Descripción:** Representa un DVD.
- **Atributos privados:**
  - `director: String`
  - `duration: int` (minutos).
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para incluir director y duración.
- **Reglas:** La duración debe ser mayor que cero.

#### `LibraryUser`

- **Descripción:** Representa a un usuario de la biblioteca.
- **Atributos privados:**
  - `userName: String`
  - `userID: String`.
- **Métodos:**
  - Constructor
  - getters
  - `showUserDetails()`.

#### `Library`

- **Descripción:** Administra los elementos, usuarios y préstamos.
- **Atributos privados:**
  - `libraryItems: List<LibraryItem>`
  - `users: List<LibraryUser>`; ambas listas empiezan vacías.
- **Métodos:**
  - `boolean addItem(LibraryItem item)`
  - `boolean registerUser(LibraryUser user)`
  - `boolean loanItem(String itemID, String userID)`
  - `boolean returnItem(String itemID, String userID)`
  - `List<LibraryItem> getLoanedItems(String userID)`
  - `showAllItems()`.
- **Reglas:** Rechaza IDs duplicados en cada colección. Solo presta un elemento disponible a un usuario registrado. Solo acepta la devolución del usuario que tiene el préstamo. Un usuario inexistente o sin préstamos obtiene una lista vacía. El estado del préstamo se guarda en `loanedTo`; no mantengas otra lista mutable de préstamos en `LibraryUser`. Cambia el prestatario mediante métodos internos del paquete, sin exponer un setter público.

#### `Main`

- **Descripción:** Demuestra préstamos y devoluciones de libros y DVDs.
- **Métodos:**
  - Crea varios libros, DVDs y usuarios
  - agrega los elementos a la biblioteca
  - presta y devuelve elementos
  - muestra sus detalles y consulta los préstamos de cada usuario. Usa polimorfismo para operar con ambos tipos sin condicionales por subtipo.

**Comprueba:** prestar el libro `B1` a `U1` devuelve `true`. Prestarlo después a `U2` o devolverlo como `U2` devuelve `false`. Devolverlo como `U1` lo deja disponible y lo quita de la consulta de préstamos de `U1`. Repetir la devolución devuelve `false`. Demuestra también un préstamo de DVD.

## Ejercicio 7: alquiler de películas y videojuegos

### Clases


#### `RentalItem`

- **Descripción:** Clase base para los artículos del videoclub.
- **Atributos privados:**
  - `itemName: String`
  - `itemID: String`
  - `rentedTo: Customer` (inicialmente `null`).
- **Métodos:**
  - Constructor
  - getters
  - `isAvailable()`
  - `showDetails()` abstracto. El artículo está disponible cuando `rentedTo` es `null`.

#### `Movie` (hereda de `RentalItem`)

- **Descripción:** Representa una película.
- **Atributos privados:**
  - `director: String`
  - `duration: int` (minutos)
  - `ageRating: String`
  - `genre: String`.
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para incluir los datos de la película. La duración debe ser positiva.

#### `VideoGame` (hereda de `RentalItem`)

- **Descripción:** Representa un videojuego; este segundo tipo permite demostrar polimorfismo.
- **Atributos privados:**
  - `platform: String`
  - `ageRating: String`.
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para incluir plataforma y clasificación por edades.

#### `Customer`

- **Descripción:** Representa a un cliente.
- **Atributos privados:**
  - `customerName: String`
  - `customerID: String`.
- **Métodos:**
  - Constructor
  - getters
  - `showCustomerDetails()`.

#### `MovieRentalSystem`

- **Descripción:** Administra los artículos, clientes y alquileres.
- **Atributos privados:**
  - `rentalItems: List<RentalItem>`
  - `customers: List<Customer>`; ambas listas empiezan vacías.
- **Métodos:**
  - `boolean addItem(RentalItem item)`
  - `boolean registerCustomer(Customer customer)`
  - `boolean rentItemToCustomer(String itemID, String customerID)`
  - `boolean returnItem(String itemID, String customerID)`
  - `List<RentalItem> getRentedItems(String customerID)`
  - `showAllItems()`
  - `showAvailableItems()`
  - `showAllCustomers()`.
- **Reglas:** Rechaza IDs duplicados. Solo alquila artículos disponibles a clientes registrados. Solo acepta la devolución del cliente que tiene el artículo alquilado. El estado se guarda en `rentedTo`; no agregues setters públicos de disponibilidad ni otra lista mutable de alquileres en `Customer`. La clasificación por edades es informativa; no se requiere validar la edad del cliente ni cobrar alquileres.

#### `Main`

- **Descripción:** Carga datos de ejemplo y permite gestionar el videoclub desde consola.
- **Métodos:**
  - Crea al menos dos películas, un videojuego y dos clientes. Implementa un menú con `Scanner` para listar artículos, listar disponibles, alquilar, devolver, mostrar clientes y salir. Lee las opciones como texto
  - si la opción no existe, muestra un mensaje y vuelve al menú.

**Comprueba:** alquila una película y un videojuego al mismo cliente con el mismo método. Ambos desaparecen de la lista de disponibles. Rechaza otro alquiler del mismo artículo, IDs inexistentes y una devolución de otro cliente. La devolución correcta vuelve a mostrar el artículo como disponible; la opción de salir termina el programa.

## Ejercicio 8: inventario con interfaces

### Clases


#### `Priceable`

- **Descripción:** Define el comportamiento de precio de un producto.
- **Métodos:**
  - `double getPrice()`
  - `void setPrice(double price)`.

#### `InventoryManageable`

- **Descripción:** Define las operaciones de la colección de productos.
- **Métodos:**
  - `boolean addItem(Product product)`
  - `boolean deleteItem(String productId)`
  - `List<Product> listItems()`
  - `Product findItemById(String productId)`.

#### `Product` (implementa `Priceable`)

- **Descripción:** Representa un producto genérico de la tienda.
- **Atributos privados:**
  - `productId: String`
  - `price: double`.
- **Métodos:**
  - Constructor
  - getters
  - implementa `getPrice()` y `setPrice(double price)`
  - declara `showDetails()` abstracto.
- **Reglas:** El precio debe ser mayor o igual que cero.

#### `Electronics` (hereda de `Product`)

- **Descripción:** Representa un producto electrónico.
- **Atributos privados:**
  - `brand: String`
  - `model: String`.
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()`. Hereda el comportamiento de precio de `Product`.

#### `Clothing` (hereda de `Product`)

- **Descripción:** Representa una prenda de vestir.
- **Atributos privados:**
  - `size: String`
  - `material: String`.
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()`. Hereda el comportamiento de precio de `Product`.

#### `Inventory` (implementa `InventoryManageable`)

- **Descripción:** Administra los productos de la tienda.
- **Atributos privados:**
  - `products: List<Product>`
  - inicialmente vacía.
- **Métodos:**
  - Implementa las cuatro operaciones de `InventoryManageable`.
- **Reglas:** Rechaza IDs duplicados. `listItems()` devuelve una copia o vista no modificable. `findItemById()` devuelve `null` si no encuentra el producto.

#### `Main`

- **Descripción:** Demuestra el alta, listado, búsqueda, cambio de precio y eliminación de productos.
- **Métodos:**
  - Usa una referencia `InventoryManageable` para trabajar con `Inventory`. Agrega productos electrónicos y prendas
  - busca un producto y cambia su precio mediante `Priceable`
  - elimina un producto. Cada registro representa un producto identificado
  - no se requiere controlar cantidades.

**Comprueba:** registra `E1` a $1200.00 y `C1` a $50.00. Cambia `C1` a $45.00. Un precio negativo se rechaza y conserva $45.00. Agregar otro `E1` devuelve `false`. Eliminar `E1` deja un producto; buscar el eliminado devuelve `null`. Modificar la lista de `listItems()` no debe cambiar la colección interna.

## Ejercicio 9: matrícula y roles educativos

### Clases


#### `Usuario`

- **Descripción:** Define las operaciones de sesión de un usuario.
- **Métodos:**
  - `void iniciarSesion()`
  - `void cerrarSesion()`.

#### `Estudiante`

- **Descripción:** Define las acciones de un estudiante.
- **Métodos:**
  - `String getId()`
  - `String getNombre()`
  - `boolean matricularCurso(Curso curso)`
  - `boolean entregarTarea(Curso curso, String tarea)`.

#### `Profesor`

- **Descripción:** Define las acciones de un profesor.
- **Métodos:**
  - `String getId()`
  - `String getNombre()`
  - `boolean asignarTarea(Curso curso, String tarea)`
  - `boolean calificarExamen(Curso curso, Estudiante estudiante, double nota)`.

#### `Persona` (implementa `Usuario`)

- **Descripción:** Clase base para los usuarios del sistema.
- **Atributos privados:**
  - `id: String`
  - `nombre: String`.
- **Métodos:**
  - Constructor
  - getters
  - declara `iniciarSesion()` abstracto e implementa `cerrarSesion()` con un mensaje común.
- **Reglas:** La clase es abstracta porque cada rol concreto presenta un inicio de sesión distinto.

#### `SoloEstudiante` (hereda de `Persona` e implementa `Estudiante`)

- **Descripción:** Representa a una persona que solo estudia.
- **Métodos:**
  - Constructor
  - implementa `iniciarSesion()`, `matricularCurso(Curso curso)` y `entregarTarea(Curso curso, String tarea)`.

#### `SoloProfesor` (hereda de `Persona` e implementa `Profesor`)

- **Descripción:** Representa a una persona que solo enseña.
- **Métodos:**
  - Constructor
  - implementa `iniciarSesion()`, `asignarTarea(Curso curso, String tarea)` y `calificarExamen(Curso curso, Estudiante estudiante, double nota)`.

#### `EstudianteProfesor` (hereda de `Persona` e implementa `Estudiante` y `Profesor`)

- **Descripción:** Representa a una persona que estudia y enseña.
- **Métodos:**
  - Constructor
  - implementa el inicio de sesión y las operaciones de ambos roles.

Cada implementación de `iniciarSesion()` muestra el nombre y el rol correspondiente. La sesión es una demostración por consola, sin contraseña ni autenticación real. No se requiere iniciar sesión antes de ejecutar las demás operaciones.

#### `Curso`

- **Descripción:** Representa un curso, su docente y sus estudiantes matriculados.
- **Atributos privados:**
  - `nombre: String`
  - `codigo: String`
  - `docente: Profesor`
  - `estudiantes: List<Estudiante>` (inicialmente vacía).
- **Métodos:**
  - Constructor con nombre, código y docente
  - `boolean matricular(Estudiante estudiante)`
  - `boolean estaMatriculado(String estudianteId)`
  - `boolean esDocente(String profesorId)`
  - `List<Estudiante> getEstudiantes()`
  - getters.
- **Reglas:** Cada curso tiene un solo docente. Rechaza matrícula duplicada y no matricula al docente en su propio curso. Una persona puede enseñar en un curso y estudiar en otro. La matrícula solo se guarda en `Curso`, no en una segunda lista de cursos de cada persona.

### Reglas de tareas y calificaciones

- `matricularCurso(curso)` delega en `curso.matricular(this)`.
- `entregarTarea` requiere curso no nulo, texto no vacío y que el estudiante esté matriculado. Muestra estudiante, tarea y curso.
- `asignarTarea` requiere curso no nulo, texto no vacío y que el profesor sea el docente del curso. Muestra profesor, tarea y curso.
- `calificarExamen` requiere curso y estudiante no nulos, que el profesor sea docente del curso, que el estudiante esté matriculado y nota entre 0 y 20 inclusive. Muestra profesor, estudiante, curso y nota.
- Estas tres últimas operaciones validan las reglas e imprimen un mensaje; no almacenan tareas, entregas ni notas. Devuelven `false` si el rol no tiene permiso. No se requiere comprobar una asignación previa para simular una entrega.

#### `Main`

- **Descripción:** Demuestra sesiones, matrículas y acciones de los distintos roles.
- **Métodos:**
  - Crea un `SoloEstudiante`, un `SoloProfesor`, un `EstudianteProfesor` y dos cursos. Recorre las personas usando `Persona` como tipo de referencia
  - no instancies la clase abstracta. Asigna tareas, registra matrículas y demuestra una calificación.

**Comprueba:** Ana (`SoloProfesor`) enseña Java; Luis (`EstudianteProfesor`) enseña Bases de Datos y se matricula en Java; Eva (`SoloEstudiante`) se matricula en Bases de Datos. Luis puede entregar una tarea en Java y calificar a Eva con 18 en Bases de Datos. No puede calificarla en Java ni matricularse en el curso que enseña. Rechaza una matrícula duplicada y una nota de 21.

## Ejercicio 10: registro de vehículos

### Clases


#### `Vehicle`

- **Descripción:** Clase base para los vehículos registrados.
- **Atributos privados:**
  - `make: String`
  - `model: String`
  - `year: int`
  - `registrationNumber: String` (matrícula única e inmutable).
- **Métodos:**
  - Constructor
  - getters
  - `showDetails()` abstracto.
- **Reglas:** El año debe ser positivo.

#### `Car` (hereda de `Vehicle`)

- **Descripción:** Representa un automóvil.
- **Atributos privados:**
  - `seatingCapacity: int` (incluye al conductor)
  - `fuelType: String`.
- **Métodos:**
  - Constructor que llama a `super`
  - getters
  - sobrescribe `showDetails()` para incluir asientos y combustible.
- **Reglas:** La capacidad de asientos debe ser positiva.

#### `Motorcycle` (hereda de `Vehicle`)

- **Descripción:** Representa una motocicleta.
- **Atributos privados:**
  - `engineSize: int` (cilindrada en cm³)
  - `hasSidecar: boolean`.
- **Métodos:**
  - Constructor que llama a `super`
  - getters
  - sobrescribe `showDetails()` para incluir cilindrada y sidecar.
- **Reglas:** La cilindrada debe ser positiva.

#### `Truck` (hereda de `Vehicle`)

- **Descripción:** Representa un camión.
- **Atributos privados:**
  - `loadCapacity: double` (kg)
  - `numberOfAxles: int`.
- **Métodos:**
  - Constructor que llama a `super`
  - getters
  - sobrescribe `showDetails()` para incluir capacidad de carga y número de ejes.
- **Reglas:** La capacidad de carga debe ser positiva y el camión debe tener al menos 2 ejes.

#### `VehicleRegistry`

- **Descripción:** Gestiona el registro de vehículos.
- **Atributos privados:**
  - `registeredVehicles: List<Vehicle>` (inicialmente vacía).
- **Métodos:**
  - `boolean registerVehicle(Vehicle vehicle)`
  - `void showAllVehicles()`
  - `Vehicle findVehicle(String registrationNumber)`.
- **Reglas:** Rechaza matrículas duplicadas. La búsqueda devuelve `null` si no encuentra el vehículo. `showAllVehicles()` recorre la colección y llama a `showDetails()` sin usar `instanceof` ni conversiones a subtipos.

#### `Main`

- **Descripción:** Crea vehículos y demuestra el registro polimórfico.
- **Métodos:**
  - Crea varios automóviles, motocicletas y camiones
  - los registra
  - muestra sus detalles y busca una matrícula.

**Comprueba:** registra un automóvil `A1`, una motocicleta `M1` y un camión `T1`. El listado muestra los atributos específicos de cada tipo. Buscar `M1` devuelve la motocicleta; buscar `X1` devuelve `null`. Otro vehículo con matrícula `A1` es rechazado, aunque sea de otro tipo.

## Ejercicio 11: gestión de personal y salarios

### Clases


Las reglas salariales de este ejercicio se definen aquí para que cada solución tenga resultados verificables.

#### `Empleado`

- **Descripción:** Clase base para el personal de la empresa.
- **Atributos privados:**
  - `nombre: String`
  - `id: String`
  - `departamento: String`
  - `salarioBase: double`.
- **Métodos:**
  - Constructor
  - getters
  - `String mostrarInfo()`
  - `double calcularSalario()` abstracto. `mostrarInfo()` incluye el salario calculado.
- **Reglas:** El salario base debe ser mayor o igual que cero.

#### `Desarrollador` (hereda de `Empleado`)

- **Descripción:** Representa a un desarrollador.
- **Atributos privados:**
  - `lenguajes: List<String>`.
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `calcularSalario()`.
- **Reglas:** Copia la lista de lenguajes, que no puede ser nula; cada nombre debe ser no vacío y único, comparado sin distinguir mayúsculas. El salario mensual es base + $100.00 por lenguaje distinto. Una lista vacía es válida y no agrega bonificación.

#### `Gerente` (hereda de `Empleado`)

- **Descripción:** Representa a un gerente y el equipo que supervisa.
- **Atributos privados:**
  - `empleadosACargo: List<Empleado>` (inicialmente vacía).
- **Métodos:**
  - Constructor
  - consulta del equipo
  - sobrescribe `calcularSalario()`.
- **Reglas:** El salario mensual es base + $150.00 por empleado a cargo. `Empresa` administra el equipo mediante métodos internos del paquete.

#### `Disenador` (hereda de `Empleado`)

- **Descripción:** Representa a un diseñador.
- **Atributos privados:**
  - `tipoDiseno: String`.
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `calcularSalario()`.
- **Reglas:** El tipo debe ser `Grafico`, `UX` o `UI`. El salario mensual es base + $200.00 para Grafico, + $300.00 para UX o + $250.00 para UI.

#### `Empresa`

- **Descripción:** Administra empleados y equipos.
- **Atributos privados:**
  - `empleados: List<Empleado>` (inicialmente vacía).
- **Métodos:**
  - `boolean agregarEmpleado(Empleado empleado)`
  - `boolean asignarEmpleadoAGerente(String empleadoId, String gerenteId)`
  - `boolean eliminarEmpleado(String id)`
  - `void mostrarEmpleados()`
  - `double calcularTotalSalarios()`.
- **Reglas:** Rechaza IDs duplicados. Solo asigna empleados registrados a gerentes registrados. Un gerente no puede pertenecer a un equipo; cada empleado puede pertenecer como máximo a un equipo. Al eliminar a un empleado, quítalo también del equipo. Al eliminar a un gerente, deja a sus empleados en la empresa sin gerente asignado. El total se calcula sumando `calcularSalario()` de cada empleado.

#### `Main`

- **Descripción:** Registra empleados, asigna equipos y muestra salarios.
- **Métodos:**
  - Crea y registra desarrolladores, gerentes y diseñadores
  - asigna integrantes a un gerente
  - muestra información y total de salarios
  - elimina un empleado y muestra el total actualizado.

**Comprueba:** un desarrollador con base de $2000.00 y dos lenguajes cobra $2200.00; un diseñador UX con base de $1800.00 cobra $2100.00; un gerente con base de $3000.00 y esos dos empleados a cargo cobra $3300.00. El total es $7600.00. Tras eliminar al diseñador, el gerente cobra $3150.00 y el total pasa a $5350.00. Rechaza una segunda asignación, la autoasignación del gerente y un ID duplicado.
