package Constraints;

import java.util.ArrayList;
import java.util.List;

public class adminAnswer {
	    private Boolean ctopt1;
	    private Boolean ctopt2;
	    private Boolean ctopt3;
	    private Boolean ctopt4;

	    // Static list to store all questions
	    public static List<adminAnswer> questionList = new ArrayList<>();

	   public adminAnswer(Boolean correctopt1, Boolean correctopt2, Boolean correctopt3, Boolean correctopt4) {
	        this.ctopt1 = correctopt1;
	        this.ctopt2 = correctopt2;
	        this.ctopt3 = correctopt3;
	        this.ctopt4 = correctopt4;

	        // Add this question to the list automatically
	        questionList.add(this);
	    }

	     //Getters
	    public Boolean getcorrectopt1() { return ctopt1; }
	    public Boolean getcorrectopt2() { return ctopt2; }
	    public Boolean getcorrectopt3() { return ctopt3; }
	    public Boolean getcorrectopt4() { return ctopt4; }

}
