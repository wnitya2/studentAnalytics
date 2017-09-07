package my.ais.mockup.service;

import java.util.LinkedHashMap;
import java.util.List;

public interface StudentService {
	
	@SuppressWarnings("rawtypes")
	public List findAll();
	
	@SuppressWarnings("rawtypes")
	public List search(LinkedHashMap<String, String> categories);

}
