package my.ais.model;

public class SearchCategory {
	
	private String filter;
	private String desc;
	
	public SearchCategory(String filter, String desc){
		this.filter = filter;
		this.desc = desc;
	}
	
	public SearchCategory() {
		
	}

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
