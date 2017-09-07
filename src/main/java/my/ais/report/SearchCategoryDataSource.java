package my.ais.report;

import java.util.List;

import my.ais.model.SearchCategory;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class SearchCategoryDataSource implements JRDataSource {
	
	private List<SearchCategory> categories;
	private int index = -1;
	
	public SearchCategoryDataSource(List<SearchCategory> categories){
		super();
		this.categories = categories;
	}

	public Object getFieldValue(JRField field) throws JRException {
		String fieldName = field.getName();
		SearchCategory cat = categories.get(index);
		
		if ("filter".equals(fieldName)){
			return cat.getFilter();
		}
		else if ("desc".equals(fieldName)){
			return cat.getDesc();
		}
		return "";
	}

	public boolean next() throws JRException {
		return ++index < categories.size();
	}

}
