package my.ais.business.service;

import java.util.HashMap;
import java.util.List;

import my.ais.domain.Student;

public interface StudentService {
	
	List<Student> getStudentListAll();
	
	List<Student> getStudentListByCategory(HashMap<String, String> categoriesMap);
	
	List<Student> getResearchStudentListAll();
	
	Student getStudentById(String matrix);

	Student saveStudent(Student student);
	
	Student updateStudent(Student student);
	
	void deleteStudent(Student student);
}
