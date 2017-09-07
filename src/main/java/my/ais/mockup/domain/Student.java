package my.ais.mockup.domain;

public class Student {
	
	private String status;
	private String level;
	private String mode;
	private String program;
	private String country;
	private String matrixNo;
	private String studentName;
	private String enrolment;
	
	public Student(){
		
	}
	
	public Student (String status, String level, String mode,
					String program, String country, String matrixNo,
					String studentName, String enrolment){
		this.status = status;
		this.level = level;
		this.mode = mode;
		this.program = program;
		this.country = country;
		this.matrixNo = matrixNo;
		this.studentName = studentName;
		this.enrolment = enrolment;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMatrixNo() {
		return matrixNo;
	}
	public void setMatrixNo(String matrixNo) {
		this.matrixNo = matrixNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getEnrolment() {
		return enrolment;
	}
	public void setEnrolment(String enrolment) {
		this.enrolment = enrolment;
	}

}
