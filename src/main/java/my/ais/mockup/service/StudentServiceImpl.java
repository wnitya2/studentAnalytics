package my.ais.mockup.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import my.ais.mockup.domain.Student;

public class StudentServiceImpl implements StudentService{
	
	private List<Student> studentList = new LinkedList<Student>();
	
	public StudentServiceImpl()
	{
		studentList.add(
				new Student("ACTIVE", "MASTER", "COURSEWORK - FT", "MANP - Software Engineering", "Indonesia", "MAN141034", "Nitya Wijayanti", "201420151"));
		studentList.add(
				new Student("ACTIVE", "MASTER", "COURSEWORK - FT", "MANP - Software Engineering", "Malaysia", "MAN141036", "Yee Zen Ming", "201420151"));
	}
	

	public List<Student> findAll() {
		return studentList;
	}
	
	public List<Student> search(LinkedHashMap<String, String> categories) {
		List <Student> result = new LinkedList<Student>();			
		
		if (isCategoryEmpty(categories)){
			result = studentList;
		}else{
			for (Student s: studentList){
				int countMatch = 0;				
					
				if (s.getStatus().equalsIgnoreCase(categories.get("statusKey"))){countMatch++;}
				if (s.getLevel().equalsIgnoreCase(categories.get("levelKey"))){countMatch++;}
				if (s.getMode().equalsIgnoreCase(categories.get("modeKey"))){countMatch++;}
				if (s.getProgram().equalsIgnoreCase(categories.get("programKey"))){countMatch++;}
				if (s.getCountry().equalsIgnoreCase(categories.get("countryKey"))){countMatch++;}
				if (categories.get("matrixKey") != null){
					if (s.getMatrixNo().equalsIgnoreCase(categories.get("matrixKey").toLowerCase().trim())){countMatch++;}
				}								
				if (categories.get("studentKey") != null){
					if (s.getStudentName().toLowerCase().contains(categories.get("studentKey").toLowerCase().trim())){countMatch++;}
				}				
				if (categories.get("enrolKey") != null){
					if (s.getEnrolment().equalsIgnoreCase(categories.get("enrolKey").toLowerCase().trim())){countMatch++;}
				}			
				
				if (countMatch == categories.size()){
					result.add(s);
				}			
			}			
		}		
		return result;	
	}
	
	private boolean isCategoryEmpty(HashMap<String, String> categories)
	{
		for (String key: categories.keySet())
		{
			if (!key.isEmpty()){
				return false;
			}
		}
		return true;
	}	
}
