package Constraints;

public class quizSelection {
    private int id;            
    private String createdBy;
    private String title;
    private String status;

    public quizSelection(int id, String createdBy, String title, String status) {
        this.id = id;
        this.createdBy = createdBy;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
