package my.ais.report;

import java.util.List;

import my.ais.domain.Student;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class StudentDataSource implements JRDataSource {
	
	private List<Student> students;
	private int index = -1;
	
	public StudentDataSource(List<Student> students){
		super();
		this.students = students;
	}

	public Object getFieldValue(JRField field) throws JRException {
		String fieldName = field.getName();
		Student student = students.get(index);
		
		if("ic".equals(fieldName)){
			return student.getIc();
		}
		else if ("matrixId".equals(fieldName)){
			return student.getMatrixId();
		}
		else if ("fullName".equals(fieldName)){
			return student.getFullName();
		}
		else if ("country".equals(fieldName)){
			return student.getCountry();
		}
		return "";
	}

	public boolean next() throws JRException {
		return ++index < students.size();
	}

}
