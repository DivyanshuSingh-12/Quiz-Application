package Constraints;

import java.util.ArrayList;

public class Question {

    private String ques;
    private String opttxt1;
    private String opttxt2;
    private String opttxt3;
    private String opttxt4;
    private int correctOpt; 
    private int questionId; 

    private final ArrayList<Question> questions = new ArrayList<>();
    
    
    public Question() {}

    
    public Question(String ques, String opttext1, String opttext2,
                    String opttext3, String opttext4, int correctOpt) {
        this.ques = ques;
        this.opttxt1 = opttext1;
        this.opttxt2 = opttext2;
        this.opttxt3 = opttext3;
        this.opttxt4 = opttext4;
        this.correctOpt = correctOpt;
    }

    
    public Question(int questionId, String ques, String opttext1, String opttext2,
                    String opttext3, String opttext4, int correctOpt) {
        this.questionId = questionId;
        this.ques = ques;
        this.opttxt1 = opttext1;
        this.opttxt2 = opttext2;
        this.opttxt3 = opttext3;
        this.opttxt4 = opttext4;
        this.correctOpt = correctOpt;
    }

    // CRUD methods for admin panel
    public void addQuestion(Question q) { questions.add(q); }
    public Question getQuestion(int index) { return questions.get(index); }
    public void updateQuestion(int index, Question q) { questions.set(index, q); }
    public int size() { return questions.size(); }
    public ArrayList<Question> getAllQuestions() { return questions; }

    // Getters
    public int getQuestionId() { return questionId; }
    public String getQues() { return ques; }
    public String getOpttext1() { return opttxt1; }
    public String getOpttext2() { return opttxt2; }
    public String getOpttext3() { return opttxt3; }
    public String getOpttext4() { return opttxt4; }
    public int getCorrectOpt() { return correctOpt; }


     // Setters
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setQues(String ques) { this.ques = ques; }
    public void setOpttext1(String opttxt1) { this.opttxt1 = opttxt1; }
    public void setOpttext2(String opttxt2) { this.opttxt2 = opttxt2; }
    public void setOpttext3(String opttxt3) { this.opttxt3 = opttxt3; }
    public void setOpttext4(String opttxt4) { this.opttxt4 = opttxt4; }
    public void setCorrectOpt(int correctOpt) { this.correctOpt = correctOpt; }
    

    
    
}
