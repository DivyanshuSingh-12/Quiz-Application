package Constraints;

public class Student {

    private int id;
    private String username;
    private String password; 
    private String firstName;
    private String lastName;
    private String contact;
    private String gender;

    // Constructor for admin-added student or student registration
    // If password is null or empty, set password = username
    public Student(int id, String username, String password, String firstName, String lastName, String contact, String gender) {
        this.id = id;
        this.username = username;
        this.password = password; // default logic
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.gender = gender;
    }

   

    // ---------------- Getters ----------------
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContact() { return contact; }
    public String getGender() { return gender; }

    // ---------------- Setters ----------------
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password;}
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setContact(String contact) { this.contact = contact; }
    public void setGender(String gender) { this.gender = gender; }
}
