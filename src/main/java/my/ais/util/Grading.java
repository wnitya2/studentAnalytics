package my.ais.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import my.ais.domain.Transcript;

public class Grading {
	public static final double GRADE_A_PLUS 	= 4.00; 
	public static final double GRADE_A 			= 4.00;
	public static final double GRADE_A_MINUS 	= 3.67;
	public static final double GRADE_B_PLUS 	= 3.33;
	public static final double GRADE_B		 	= 3.00;
	public static final double GRADE_B_MINUS 	= 2.67;
	public static final double GRADE_C_PLUS 	= 2.33;
	public static final double GRADE_C_MINUS 	= 1.67;
	public static final double GRADE_D_PLUS 	= 1.33;
	public static final double GRADE_D		 	= 1.00;
	public static final double GRADE_D_MINUS 	= 0.67;
	public static final double GRADE_E		 	= 0.00;
	
	public double calculateGPA(List<Transcript> transcripts)
	{
		int totalCredit = 0;
    	double gradeCredit = 0.00;
    	double result = 0.00;
    	
    	for (Transcript t : transcripts){
    		gradeCredit += getGradeMultipleByCredit(t);    		
    		totalCredit += t.getCourseCredit();    		
    	}    	
    	result = gradeCredit/totalCredit; 		
    	result = round(String.valueOf(result), 2);    	
		return result;
	}
	
	public int calculateTotalCredit(List<Transcript> transcripts){
		int totalCredit = 0;
		for (Transcript t : transcripts){	
    		totalCredit += t.getCourseCredit();    		
    	}  
		return totalCredit;
	}
	
	private double getGradeMultipleByCredit(Transcript transcript) {
		
		int courseCredit = transcript.getCourseCredit();
		String courseGrade = transcript.getCourseGrade();
		
    	double d = 0.00;
    	if (courseGrade.equalsIgnoreCase("A+")){
    		d = Grading.GRADE_A_PLUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("A")){
    		d = Grading.GRADE_A * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("A-")){
    		d = Grading.GRADE_A_MINUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("B+")){
    		d = Grading.GRADE_B_PLUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("B")){
    		d = Grading.GRADE_B * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("B-")){
    		d = Grading.GRADE_B_MINUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("C+")){
    		d = Grading.GRADE_C_PLUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("C-")){
    		d = Grading.GRADE_C_MINUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("D+")){
    		d = Grading.GRADE_D_PLUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("D")){
    		d = Grading.GRADE_D * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("D-")){
    		d = Grading.GRADE_D_MINUS * courseCredit;
    	}
    	else if (courseGrade.equalsIgnoreCase("E")){
    		d = Grading.GRADE_E * courseCredit;
    	}
    	return d;
    }
	
	private double round(String value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static void main (String[] args)
	{
		System.out.println(new BigDecimal(1.03).subtract(new BigDecimal(0.41)));
		System.out.println(new BigDecimal("1.03").subtract(new BigDecimal("0.41")));
		
		Transcript t = new Transcript();
		t.setCourseCredit(3);
		t.setCourseGrade("A-");
		System.out.println(new Grading().getGradeMultipleByCredit(t));
	}
}
