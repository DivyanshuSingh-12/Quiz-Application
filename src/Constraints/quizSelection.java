package Constraints;

public class quizSelection {
    private String createdBy;
    private String title;
    private String status;

    public quizSelection(String createdBy, String title, String status) {
        this.createdBy = createdBy;
        this.title = title;
        this.status = status;
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

}
