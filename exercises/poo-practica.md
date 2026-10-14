# Práctica adicional de POO en Java

Estos ejercicios complementan los [11 ejercicios principales](poo.md). Cada reto practica conceptos parecidos en un contexto nuevo. Resuélvelos en paquetes separados y usa la estructura de clases que has trabajado en clase.

## Ejercicio 12: modela gatos

### Clases

#### `Cat`

- **Descripción:** Representa un gato.
- **Atributos privados:**
  - `name: String`
  - `age: int`
  - `color: String`
- **Métodos:**
  - Constructor con los tres atributos.
  - Getters para consultar los atributos.
  - `showDetails()` para imprimir los datos del gato.
- **Reglas:** El nombre y el color no pueden estar vacíos; la edad no puede ser negativa.

#### `Main`

- **Descripción:** Crea gatos y muestra sus datos.
- **Métodos:**
  - Crea dos gatos diferentes e imprime sus detalles.

**Comprueba:** crea un gato llamado Nube, de 2 años y color gris, y otro gato con datos distintos. Cada objeto debe mostrar sus propios atributos.

## Ejercicio 13: amplía un teléfono

### Clases

#### `Phone`

- **Descripción:** Representa un teléfono cuya memoria puede ampliarse y cuya batería puede cargarse.
- **Atributos privados:**
  - `brand: String`
  - `model: String`
  - `storageGb: int`
  - `batteryPercent: int`
- **Métodos:**
  - Constructor con marca, modelo, almacenamiento y porcentaje de batería.
  - Getters.
  - `showDetails()`.
  - `increaseStorage(int additionalGb)` suma espacio al almacenamiento.
  - `charge(int percentagePoints)` suma carga a la batería hasta un máximo de 100 %.
- **Reglas:** El almacenamiento inicial debe ser mayor que cero; la batería inicial debe estar entre 0 y 100. Los incrementos deben ser mayores que cero. Rechaza la operación si sumar almacenamiento desborda el rango de `int`.

#### `Main`

- **Descripción:** Muestra el teléfono antes y después de actualizarlo.
- **Métodos:**
  - Crea un teléfono, amplía su almacenamiento, carga su batería y muestra los resultados.

**Comprueba:** un teléfono con 128 GB y 40 % de batería, al recibir 64 GB y 75 puntos de carga, queda con 192 GB y 100 %. Una ampliación de 0 se rechaza; una batería al 100 % no puede superar ese límite.

## Ejercicio 14: administra una librería

### Clases

#### `Book`

- **Descripción:** Representa un libro disponible para la venta.
- **Atributos privados:**
  - `isbn: String` (identificador único e inmutable)
  - `title: String`
  - `author: String`
  - `pages: int`
- **Métodos:**
  - Constructor.
  - Getters.
  - `showDetails()` para mostrar título, autor, ISBN y número de páginas.
- **Reglas:** El ISBN, título y autor son obligatorios; el número de páginas debe ser positivo.

#### `BookStore`

- **Descripción:** Administra los libros disponibles en una librería.
- **Atributos privados:**
  - `name: String`
  - `books: List<Book>` (inicialmente vacía)
- **Métodos:**
  - Constructor con el nombre de la librería.
  - `boolean addBook(Book book)` agrega un libro si su ISBN no está registrado.
  - `boolean sellBook(String isbn)` retira el libro vendido; devuelve `false` si no existe.
  - `int getQuantity()` devuelve el número de libros registrados.
  - `List<Book> filterByMaximumPages(int maximumPages)` devuelve libros con páginas menores o iguales al límite.
  - `showDetails()` muestra la librería y sus libros.
- **Reglas:** La cantidad se calcula con el tamaño de la lista, no se almacena en otro atributo. El filtro no modifica el inventario y requiere un límite positivo.

#### `Main`

- **Descripción:** Demuestra el alta, filtrado y venta de libros.
- **Métodos:**
  - Registra tres libros, muestra la cantidad, filtra por páginas y vende un libro.

**Comprueba:** registra libros de 120, 240 y 400 páginas. Filtrar por 250 devuelve los dos primeros. Vender un ISBN registrado reduce la cantidad en uno; vender el mismo ISBN otra vez devuelve `false`. Un ISBN duplicado se rechaza.

## Ejercicio 15: calcula una beca

### Clases


#### `Scholarship`

- **Descripción:** Calcula el descuento mensual de matrícula según la categoría y el promedio del estudiante.
- **Atributos privados:**
  - `category: String`
  - `average: double`
- **Métodos:**
  - Constructor.
  - Getters.
  - `calculateTuition()` devuelve el monto base.
  - `calculateDiscountRate()` devuelve la tasa aplicable.
  - `calculateDiscount()` devuelve el descuento en dinero.
  - `calculateFinalTuition()` devuelve el pago luego del descuento.
- **Reglas:** La categoría debe ser `A`, `B` o `C`; el promedio debe estar entre 0 y 20 inclusive. No redondees el promedio antes de elegir la tasa.

| Categoría | Matrícula mensual ($) |
| --- | ---: |
| A | 600.00 |
| B | 500.00 |
| C | 400.00 |

| Promedio | Descuento |
| --- | ---: |
| 0 ≤ promedio < 14 | 0 % |
| 14 ≤ promedio < 16 | 5 % |
| 16 ≤ promedio < 18 | 10 % |
| 18 ≤ promedio ≤ 20 | 20 % |

#### `Main`

- **Descripción:** Muestra el monto base, la beca y el pago final de estudiantes distintos.
- **Métodos:**
  - Crea objetos `Scholarship` para varias categorías y promedios y muestra todos los cálculos.

**Comprueba:** categoría B con promedio 18 produce matrícula de $500.00, descuento de $100.00 y pago final de $400.00. Con promedio 15.99, la matrícula B queda en $475.00; con 16, queda en $450.00. Rechaza categoría inválida y promedios fuera del rango.

## Ejercicio 16: calcula comisiones de ventas

### Clases


#### `Salesperson`

- **Descripción:** Representa a una persona que vende y recibe una comisión mensual.
- **Atributos privados:**
  - `id: String`
  - `name: String`
  - `level: String`
  - `monthlySales: double`
  - `baseSalary: double`
- **Métodos:**
  - Constructor.
  - Getters.
  - `calculateCommissionRate()` devuelve la tasa según el nivel.
  - `calculateCommission()` calcula las ventas del mes por la tasa.
  - `calculateTotalPay()` suma el salario base y la comisión.
- **Reglas:** Nivel válido: `Junior`, `Senior` o `Lead`. Ventas y salario base deben ser valores finitos mayores o iguales que cero.

| Nivel | Comisión sobre ventas |
| --- | ---: |
| Junior | 3 % |
| Senior | 5 % |
| Lead | 7 % |

#### `SalesTeam`

- **Descripción:** Registra vendedores y resume sus pagos.
- **Atributos privados:**
  - `salespeople: List<Salesperson>` (inicialmente vacía)
- **Métodos:**
  - `boolean addSalesperson(Salesperson salesperson)` rechaza IDs repetidos.
  - `int countSalespeople()` devuelve el total registrado.
  - `int countByLevel(String level)` devuelve el total de un nivel.
  - `double calculatePayroll()` suma el pago total de cada vendedor.

#### `Main`

- **Descripción:** Muestra comisiones, pagos y resumen del equipo.
- **Métodos:**
  - Registra vendedores, imprime el pago de cada uno y muestra el conteo por nivel y el total de pagos.

**Comprueba:** una persona Junior con salario base de $900 y ventas de $1000 recibe $30 de comisión y cobra $930. Una persona Senior con base de $1200 y ventas de $2000 recibe $100 de comisión y cobra $1300. El equipo registra 2 personas y suma $2230 en pagos.

## Ejercicio 17: presta equipos de laboratorio

### Clases


#### `LabItem`

- **Descripción:** Clase base para equipos que se pueden prestar.
- **Atributos privados:**
  - `itemId: String` (identificador único e inmutable)
  - `name: String`
  - `loanedTo: Borrower` (inicialmente `null`)
- **Métodos:**
  - Constructor.
  - Getters.
  - `isAvailable()` devuelve `true` cuando no existe prestatario.
  - `showDetails()` abstracto.
- **Reglas:** El estado del préstamo se obtiene de `loanedTo`. No expongas un setter público para cambiar el prestatario.

#### `Laptop` (hereda de `LabItem`)

- **Descripción:** Representa una laptop de laboratorio.
- **Atributos privados:**
  - `ramGb: int`
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `showDetails()` para incluir la memoria RAM. La RAM debe ser mayor que cero.

#### `Projector` (hereda de `LabItem`)

- **Descripción:** Representa un proyector de laboratorio.
- **Atributos privados:**
  - `lumens: int`
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `showDetails()` para incluir el brillo. Los lúmenes deben ser mayores que cero.

#### `Borrower`

- **Descripción:** Representa a un estudiante que puede solicitar equipos.
- **Atributos privados:**
  - `studentId: String`
  - `name: String`
- **Métodos:**
  - Constructor.
  - Getters.
  - `showDetails()`.

#### `Lab`

- **Descripción:** Administra los equipos, prestatarios y préstamos del laboratorio.
- **Atributos privados:**
  - `items: List<LabItem>`
  - `borrowers: List<Borrower>`
- **Métodos:**
  - `boolean addItem(LabItem item)` agrega un equipo si su ID no existe.
  - `boolean registerBorrower(Borrower borrower)` registra al estudiante si su ID no existe.
  - `boolean loanItem(String itemId, String studentId)` presta un equipo disponible a un estudiante registrado.
  - `boolean returnItem(String itemId, String studentId)` acepta la devolución solo de su prestatario.
  - `List<LabItem> getItemsLoanedTo(String studentId)` consulta los préstamos actuales.
  - `showAllItems()` muestra el estado y los detalles polimórficos de los equipos.
- **Reglas:** Conserva el estado solo en `loanedTo`; usa operaciones internas del paquete para actualizarlo. No guardes una segunda lista de préstamos en `Borrower`. Si el estudiante o el equipo no existen, la operación devuelve `false`.

#### `Main`

- **Descripción:** Demuestra el préstamo de laptops y proyectores.
- **Métodos:**
  - Registra al menos dos equipos de cada tipo y dos estudiantes. Presta y devuelve equipos y muestra los préstamos de cada estudiante.

**Comprueba:** el mismo método presta una laptop y un proyector. Un equipo prestado no puede volver a prestarse; otro estudiante no puede devolverlo. Al devolverlo su prestatario, vuelve a estar disponible.

## Ejercicio 18: alquila instrumentos musicales

### Clases


#### `RentalItem`

- **Descripción:** Clase base para los instrumentos que se alquilan.
- **Atributos privados:**
  - `itemId: String` (identificador único e inmutable)
  - `name: String`
  - `rentedTo: Customer` (inicialmente `null`)
- **Métodos:**
  - Constructor y getters
  - `isAvailable()`
  - `showDetails()` abstracto.
- **Reglas:** Un instrumento está disponible si `rentedTo` es `null`. Solo el sistema de alquiler modifica al cliente asociado.

#### `Guitar` (hereda de `RentalItem`)

- **Descripción:** Representa una guitarra.
- **Atributos privados:**
  - `stringCount: int`
  - `acoustic: boolean`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para mostrar el número de cuerdas y si es acústica. El número de cuerdas debe ser positivo.

#### `Keyboard` (hereda de `RentalItem`)

- **Descripción:** Representa un teclado musical.
- **Atributos privados:**
  - `keyCount: int`
  - `hasStand: boolean`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para mostrar el número de teclas y si incluye soporte. El número de teclas debe ser positivo.

#### `Customer`

- **Descripción:** Representa a una persona que alquila instrumentos.
- **Atributos privados:**
  - `id: String`
  - `name: String`
- **Métodos:**
  - Constructor.
  - Getters.
  - `showDetails()`.

#### `InstrumentRental`

- **Descripción:** Administra el catálogo de instrumentos y los clientes.
- **Atributos privados:**
  - `items: List<RentalItem>`
  - `customers: List<Customer>`
- **Métodos:**
  - `boolean addItem(RentalItem item)` agrega un instrumento si su ID no existe.
  - `boolean addCustomer(Customer customer)` registra al cliente si su ID no existe.
  - `boolean rent(String itemId, String customerId)` alquila un instrumento disponible a un cliente registrado.
  - `boolean returnItem(String itemId, String customerId)` devuelve un instrumento solo si coincide su cliente.
  - `List<RentalItem> getAvailableItems()` devuelve los instrumentos disponibles.
  - `showAllItems()` muestra guitarras y teclados mediante polimorfismo.
- **Reglas:** No cobres tarifas ni guardes una segunda lista de alquileres en el cliente.

#### `Main`

- **Descripción:** Permite gestionar alquileres desde un menú de consola.
- **Métodos:**
  - Crea guitarras, teclados y clientes. Ofrece opciones para listar, alquilar, devolver y salir. Una opción desconocida muestra un mensaje y vuelve al menú.

**Comprueba:** alquila una guitarra y un teclado al mismo cliente usando `rent`. Ambos dejan de aparecer entre los disponibles. Rechaza un alquiler repetido y una devolución hecha por otro cliente.

## Ejercicio 19: inventario de alimentos y útiles

### Clases


#### `Priceable`

- **Descripción:** Define cómo consultar y cambiar el precio de un producto.
- **Métodos:**
  - `double getPrice()` devuelve el precio.
  - `void setPrice(double price)` actualiza el precio.

#### `InventoryManageable`

- **Descripción:** Define las operaciones del inventario.
- **Métodos:**
  - `boolean addItem(Product product)`
  - `boolean deleteItem(String id)`
  - `List<Product> listItems()`
  - `Product findItemById(String id)`.

#### `Product` (implementa `Priceable`)

- **Descripción:** Clase base para los productos.
- **Atributos privados:**
  - `id: String` (identificador único e inmutable)
  - `price: double`
- **Métodos:**
  - Constructor
  - getters
  - Implementación de `getPrice()`.
  - Implementación de `setPrice(double price)`.
  - `showDetails()` abstracto.
- **Reglas:** El precio debe ser finito y mayor o igual que cero.

#### `Food` (hereda de `Product`)

- **Descripción:** Representa un alimento empacado.
- **Atributos privados:**
  - `name: String`
  - `expirationDate: LocalDate`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para incluir el nombre y la fecha de vencimiento.
- **Reglas:** La fecha debe estar presente y el nombre no puede estar vacío.

#### `Stationery` (hereda de `Product`)

- **Descripción:** Representa un útil escolar.
- **Atributos privados:**
  - `name: String`
  - `material: String`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()` para mostrar nombre y material.

#### `Inventory` (implementa `InventoryManageable`)

- **Descripción:** Administra una colección común de alimentos y útiles escolares.
- **Atributos privados:**
  - `products: List<Product>` (inicialmente vacía)
- **Métodos:**
  - Implementa las cuatro operaciones de `InventoryManageable`.
- **Reglas:** Rechaza IDs repetidos. Devuelve una copia o vista no modificable de la lista.

#### `Main`

- **Descripción:** Agrega, lista, busca, actualiza y elimina productos.
- **Métodos:**
  - Trabaja con una referencia `InventoryManageable`
  - busca un producto y actualiza su precio mediante `Priceable`.

**Comprueba:** registra un alimento `F1` por $4.50 y un cuaderno `S1` por $3.00. Cambia el cuaderno a $3.50. Rechaza un precio negativo, un ID repetido y la eliminación de un ID inexistente.

## Ejercicio 20: organiza talleres con varios roles

### Clases


#### `User`

- **Descripción:** Define las operaciones de sesión.
- **Métodos:**
  - `void logIn()`.
  - `void logOut()`.

#### `Participant`

- **Descripción:** Define las acciones de quien participa en talleres.
- **Métodos:**
  - `String getId()`
  - `String getName()`
  - `boolean enroll(Workshop workshop)`
  - `boolean submitActivity(Workshop workshop, String activity)`.

#### `Instructor`

- **Descripción:** Define las acciones de quien enseña talleres.
- **Métodos:**
  - `String getId()`
  - `String getName()`
  - `boolean assignActivity(Workshop workshop, String activity)`
  - `boolean gradeActivity(Workshop workshop, Participant participant, double score)`.

#### `Person` (implementa `User`)

- **Descripción:** Clase base para las personas de la plataforma.
- **Atributos privados:**
  - `id: String`
  - `name: String`
- **Métodos:**
  - Constructor
  - getters
  - `logIn()` abstracto
  - implementación común de `logOut()`.

#### `Student` (hereda de `Person` e implementa `Participant`)

- **Descripción:** Representa a una persona participante.
- **Métodos:**
  - Constructor
  - inicio de sesión
  - matrícula y entrega de actividades.

#### `Teacher` (hereda de `Person` e implementa `Instructor`)

- **Descripción:** Representa a una persona instructora.
- **Métodos:**
  - Constructor
  - inicio de sesión
  - asignación y calificación de actividades.

#### `StudentTeacher` (hereda de `Person` e implementa `Participant` e `Instructor`)

- **Descripción:** Representa a alguien que estudia y también enseña.
- **Métodos:**
  - Constructor
  - inicio de sesión
  - implementa las acciones de ambos roles.

#### `Workshop`

- **Descripción:** Representa un taller con una persona instructora y una lista de participantes.
- **Atributos privados:**
  - `code: String`
  - `title: String`
  - `instructor: Instructor`
  - `participants: List<Participant>` (inicialmente vacía)
- **Métodos:**
  - Constructor
  - `boolean addParticipant(Participant participant)`
  - `boolean containsParticipant(String participantId)`
  - `boolean isInstructor(String instructorId)`
  - getters.
- **Reglas:** Un taller tiene una persona instructora. Rechaza matrículas duplicadas y no matricula a su instructora en el mismo taller. La lista del curso es la única fuente de matrículas.

### Reglas para las actividades

- `enroll(workshop)` delega en `workshop.addParticipant(this)`.
- `submitActivity` requiere participante matriculado y una actividad no vacía.
- `assignActivity` requiere que quien asigna sea la persona instructora del taller.
- `gradeActivity` requiere que quien califica sea la persona instructora, que quien recibe la nota esté matriculado y una nota entre 0 y 20.
- Las operaciones muestran un mensaje, pero no guardan actividades ni notas. Devuelven `false` si el rol no tiene permiso.

#### `Main`

- **Descripción:** Demuestra que una persona puede tener más de un rol y que las acciones dependen del taller.
- **Métodos:**
  - Crea personas y dos talleres
  - matricula participantes
  - asigna y califica actividades
  - muestra el inicio y cierre de sesión.

**Comprueba:** una persona `StudentTeacher` puede enseñar un taller y participar en otro. No puede matricularse en el taller que enseña ni calificar en un taller ajeno. Rechaza una matrícula repetida y una nota de 21.

## Ejercicio 21: registra envíos

### Clases


#### `Shipment`

- **Descripción:** Clase base para los envíos de una empresa de mensajería.
- **Atributos privados:**
  - `trackingNumber: String` (identificador único e inmutable)
  - `origin: String`
  - `destination: String`
- **Métodos:**
  - Constructor
  - getters
  - `showDetails()` abstracto.

#### `DocumentShipment` (hereda de `Shipment`)

- **Descripción:** Representa un envío de documentos.
- **Atributos privados:**
  - `pageCount: int`
  - `certified: boolean`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()`.
- **Reglas:** El número de páginas debe ser positivo.

#### `PackageShipment` (hereda de `Shipment`)

- **Descripción:** Representa un paquete.
- **Atributos privados:**
  - `weightKg: double`
  - `fragile: boolean`
- **Métodos:**
  - Constructor
  - getters
  - sobrescribe `showDetails()`.
- **Reglas:** El peso debe ser finito y mayor que cero.

#### `ShipmentRegistry`

- **Descripción:** Registra y consulta envíos de distintos tipos.
- **Atributos privados:**
  - `shipments: List<Shipment>` (inicialmente vacía)
- **Métodos:**
  - `boolean register(Shipment shipment)`
  - `Shipment findByTrackingNumber(String trackingNumber)`
  - `void showAll()`.
- **Reglas:** Rechaza números de seguimiento repetidos. `findByTrackingNumber()` devuelve `null` si no encuentra el envío. `showAll()` llama polimórficamente a `showDetails()` sin comprobar el subtipo.

#### `Main`

- **Descripción:** Registra documentos y paquetes y los busca por número de seguimiento.
- **Métodos:**
  - Crea al menos dos envíos de cada tipo, los registra, muestra sus detalles y busca un envío.

**Comprueba:** registra `D1` y `P1`. Los detalles muestran campos específicos de cada subtipo. Buscar `P1` devuelve el paquete; un número inexistente devuelve `null`; un número duplicado se rechaza.

## Ejercicio 22: calcula pagos del personal educativo

### Clases


#### `StaffMember`

- **Descripción:** Clase base para el personal de una institución educativa.
- **Atributos privados:**
  - `id: String`
  - `name: String`
  - `baseSalary: double`
- **Métodos:**
  - Constructor
  - getters
  - `showDetails()`
  - `calculateSalary()` abstracto.
- **Reglas:** El salario base debe ser finito y mayor o igual que cero. `showDetails()` incluye el salario calculado.

#### `Professor` (hereda de `StaffMember`)

- **Descripción:** Representa a un profesor.
- **Atributos privados:**
  - `courseCount: int`
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `calculateSalary()`.
- **Reglas:** El sueldo mensual es base + $150 por curso. El número de cursos no puede ser negativo.

#### `Assistant` (hereda de `StaffMember`)

- **Descripción:** Representa a un asistente académico.
- **Atributos privados:**
  - `supportHours: int`
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `calculateSalary()`.
- **Reglas:** El sueldo mensual es base + $25 por hora de apoyo. Las horas no pueden ser negativas.

#### `Coordinator` (hereda de `StaffMember`)

- **Descripción:** Representa a una persona coordinadora.
- **Atributos privados:**
  - `teamCount: int`
- **Métodos:**
  - Constructor
  - getter
  - sobrescribe `calculateSalary()`.
- **Reglas:** El sueldo mensual es base + $200 por equipo coordinado. La cantidad de equipos no puede ser negativa.

#### `EducationCenter`

- **Descripción:** Administra al personal y resume los pagos.
- **Atributos privados:**
  - `staff: List<StaffMember>` (inicialmente vacía)
- **Métodos:**
  - `boolean addStaff(StaffMember member)`
  - `boolean removeStaff(String id)`
  - `StaffMember findById(String id)`
  - `void showAll()`
  - `double calculatePayroll()`.
- **Reglas:** Rechaza IDs repetidos. El total de pagos suma `calculateSalary()` de cada integrante. Devuelve `null` si la búsqueda no encuentra a nadie.

#### `Main`

- **Descripción:** Demuestra cálculos salariales polimórficos y el resumen de la institución.
- **Métodos:**
  - Registra un profesor, asistente y coordinador
  - imprime sus salarios y el total de la planilla.

**Comprueba:** un profesor con base de $2000 y 2 cursos cobra $2300; un asistente con base de $1200 y 8 horas cobra $1400; un coordinador con base de $2500 y 3 equipos cobra $3100. La planilla suma $6800. Rechaza un ID duplicado.
