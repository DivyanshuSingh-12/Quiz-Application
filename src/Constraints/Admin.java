package Constraints;


public class Admin {

    private String firstName;
    private String lastName;
    private String contact;
    private String userId;
    private String password;
    private String gender;
    private String imagePath;


    public Admin(String firstName, String lastName, String contact, String userId, String password,String gender,String imagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.userId = userId;
        this.password = password;
        this.gender = gender;
        this.imagePath = imagePath;

    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContact() { return contact; }
    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getgender() { return gender; }
    public String getimagePath() { return imagePath; }
    
   public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

    public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    public void setContact(String contact) {
            this.contact = contact;
        }

    public void setUserId(String userId) {
            this.userId = userId;
        }

    public void setPassword(String password) {
            this.password = password;
        }

    public void setgender(String gender) {
            this.gender = gender;
        }

    public void setimagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }



