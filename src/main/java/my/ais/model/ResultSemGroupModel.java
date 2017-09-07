package my.ais.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import my.ais.domain.Transcript;
import my.ais.util.Grading;

import org.zkoss.zul.GroupsModelArray;

public class ResultSemGroupModel extends GroupsModelArray<Transcript, String, String, Object>{

	private static final long serialVersionUID = 1L;
	
	private boolean showGroup;	
	
	public ResultSemGroupModel(List<Transcript> data, Comparator<Transcript> cmpr, boolean showGroup) {
		super(data.toArray(new Transcript[0]), cmpr);
		
		this.showGroup = showGroup;
	}
	
	protected String createGroupHead(Transcript[] groupdata, int index, int col) {      
        StringBuilder sb = new StringBuilder();
        if (groupdata.length > 0) {
        	sb.append("SESSION ")
        		.append(groupdata[0].getTranscriptId().getSem().substring(0, 4))
        		.append("/")
        		.append(groupdata[0].getTranscriptId().getSem().substring(4, 8))
        		.append(" SEMESTER ")
        		.append(groupdata[0].getTranscriptId().getSem().substring(8));
        }
 
        return sb.toString();
    }
     
    protected String createGroupFoot(Transcript[] groupdata, int index, int col) {    	
    	int totalCredit = 0;    	
    	double gpa = 0.00;
    	StringBuffer sb = new StringBuffer();
    	
    	List<Transcript> transcripts = Arrays.asList(groupdata);    
    	Grading grading = new Grading();
    	gpa = grading.calculateGPA(transcripts);   	    	
    	totalCredit = grading.calculateTotalCredit(transcripts);   	 	
    	
    	sb.append("Total credits: ").append(String.valueOf(totalCredit)).append("\n")
    		.append(",GPA: ").append(String.valueOf(gpa));   	
    	
    	return sb.toString();
    }   
    
    public boolean hasGroupfoot(int groupIndex) {
        boolean retBool = false;
         
        if(showGroup) {
            retBool = super.hasGroupfoot(groupIndex);
        }
         
        return retBool;
    }

}
