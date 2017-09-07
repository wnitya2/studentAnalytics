package my.ais.model;

import java.util.Comparator;
import java.util.List;

import my.ais.domain.Transcript;

import org.zkoss.zul.GroupsModelArray;

public class ResultCreditExemptedGroupModel extends GroupsModelArray<Transcript, String, String, Object>{

	private static final long serialVersionUID = 1L;
	
	private boolean showGroup;	
	
	public ResultCreditExemptedGroupModel(List<Transcript> data, Comparator<Transcript> cmpr, boolean showGroup) {
		super(data.toArray(new Transcript[0]), cmpr);
		
		this.showGroup = showGroup;
	}
	
	protected String createGroupHead(Transcript[] groupdata, int index, int col) {            
        return "CREDIT EXEMPTED";
    }  
    
    protected String createGroupFoot(Transcript[] groupdata, int index, int col) {    	
    	int totalCredit = 0;    	
    	StringBuffer sb = new StringBuffer();
    	
    	for (Transcript t : groupdata){    				
    		totalCredit += t.getCourseCredit();    		
    	}        	    	
    	
    	sb.append("Total credits exempted: ").append(String.valueOf(totalCredit));    	
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
