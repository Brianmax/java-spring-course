package ejercicio6;

public class LibraryUser {
    private String username;
    private String userId;

    public LibraryUser(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void mostrarDetalles() {
        System.out.println("Username: " + username);
        System.out.println("User ID: " + userId);
    }
}
