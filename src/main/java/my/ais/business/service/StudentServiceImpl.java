package my.ais.business.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import my.ais.business.dao.StudentDao;
import my.ais.domain.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("studentService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StudentServiceImpl implements StudentService {
	
	@Autowired 
	StudentDao studentDao;	

	public List<Student> getStudentListAll() {
		return studentDao.queryAll();
	}
	
	public List<Student> getStudentListByCategory(HashMap<String, String> categoriesMap) {
		List<Student> result = new LinkedList<Student>();
		List<Student> allList = studentDao.queryAll();		
		
		if (isCategoryEmpty(categoriesMap)){
			result = allList;
		}else{
			for (Student s: allList){
				int countMatch = 0;				
					
				if (s.getStatus().getStatusDesc().equalsIgnoreCase(categoriesMap.get("statusKey"))){countMatch++;}
				if (s.getLevel().equalsIgnoreCase(categoriesMap.get("levelKey"))){countMatch++;}
				if (s.getMode().equalsIgnoreCase(categoriesMap.get("modeKey"))){countMatch++;}
				if (s.getCourseType().equalsIgnoreCase(categoriesMap.get("courseTypeKey"))){countMatch++;}
				if (s.getGradYear().equalsIgnoreCase(categoriesMap.get("gradYearKey"))){countMatch++;}
				
				//Handle program code customization
				String programKey = categoriesMap.get("programKey");
				String sProgramId = s.getProgram().getProgramId();
				if (programKey != null){
					if (programKey.equalsIgnoreCase("MANP") || programKey.equalsIgnoreCase("MNP")){
						if(sProgramId.equalsIgnoreCase("MANP")|| sProgramId.equalsIgnoreCase("MNP")){countMatch++;}
					}
					else if (programKey.equalsIgnoreCase("MANA") || programKey.equalsIgnoreCase("MNA")){
						if(sProgramId.equalsIgnoreCase("MANA")|| sProgramId.equalsIgnoreCase("MNA")){countMatch++;}
					}
					else if (programKey.equalsIgnoreCase("MANN") || programKey.equalsIgnoreCase("MNS")){
						if(sProgramId.equalsIgnoreCase("MANN")|| sProgramId.equalsIgnoreCase("MNS")){countMatch++;}
					}
					else{
						if (sProgramId.equalsIgnoreCase(programKey)){countMatch++;}
					}						
				}
				
				//Country - determine by LOCAL, INTERNATIONAL, and specific country name
				if (categoriesMap.get("countryKey") != null){
					if (categoriesMap.get("countryKey").equalsIgnoreCase("LOCAL")){
						if(s.getCountry().equalsIgnoreCase("Malaysia")){countMatch++;}
					}
					else if (categoriesMap.get("countryKey").equalsIgnoreCase("INTERNATIONAL")){
						if(!s.getCountry().equalsIgnoreCase("Malaysia")){countMatch++;}
					}
					else{
						if (s.getCountry().equalsIgnoreCase(categoriesMap.get("countryKey"))){countMatch++;}
					}				
				}				
				
				if (categoriesMap.get("matrixKey") != null){
					if (s.getMatrixId().equalsIgnoreCase(categoriesMap.get("matrixKey").toLowerCase().trim())){countMatch++;}
				}								
				if (categoriesMap.get("studentKey") != null){
					if (s.getFullName().toLowerCase().contains(categoriesMap.get("studentKey").toLowerCase().trim())){countMatch++;}
				}				
				if (categoriesMap.get("enrolKey") != null){
					if (s.getEnrolmentSem().equalsIgnoreCase(categoriesMap.get("enrolKey").toLowerCase().trim())){countMatch++;}
				}			
				
				if (countMatch == categoriesMap.size()){
					result.add(s);
				}			
			}			
		}		
		
		return result;
	}	

	public List<Student> getResearchStudentListAll() {
		return studentDao.queryResearchStudentAll();
	}

	public Student getStudentById(String matrix) {
		return studentDao.get(matrix);
	}

	public Student saveStudent(Student student) {
		studentDao.save(student);
		return student;
	}

	public Student updateStudent(Student student) {
		return studentDao.update(student);
	}

	public void deleteStudent(Student student) {
		studentDao.delete(student);
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
