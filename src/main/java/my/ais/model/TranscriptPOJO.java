package my.ais.model;

public class TranscriptPOJO {
	private String remarks;
	private String content;
	
	public TranscriptPOJO(){
		
	}
	
	public TranscriptPOJO(String remarks, String content){
		this.remarks = remarks;
		this.content = content;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
