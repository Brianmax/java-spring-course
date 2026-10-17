package poo_teoria;

public class Persona {
    private String nombre;
    private String apellido;
    private int edad;
    private String email;

    public Persona(String nombre, String apellido, int edad, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
    }

    // getter y setter
   public String getNombre() {
        return nombre;
   }
   public String getApellido() {
        return apellido;
   }
   public int getEdad() {
        return edad;
   }
   public String getEmail() {
        return email;
   }

   public void setNombre(String nombre) {
        this.nombre = nombre;
   }

   public void setApellido(String apellido){
        this.apellido = apellido;
   }

   public void setEdad(int edad){
        if(edad < 0) {
            return;
        }
        this.edad = edad;
   }

   public void setEmail(String email) {
        this.email = email;
   }

   public void imprimirDetalles() {
       System.out.println("Nombre: " + nombre);
       System.out.println("Apellido: " + apellido);
       System.out.println("Edad: " + edad);
       System.out.println("Email: " + email);
   }
}
