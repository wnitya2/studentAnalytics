package my.ais.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "research_student_progress")
public class ResearchStudentProgress implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private String matrixId;
	private Student student;
	private String researchMethod;
	private String univGeneral;
	private String assessment1Status;
	private String assessment1Result;
	private String colloquiumStatus;
	private String colloquiumResult;
	private String vivaStatus;
	private String vivaResult;
	
	@Id  
	@Column(name="matrix_id")
    /*@GeneratedValue(generator="gen")  */
    @GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(value="student", name = "property")) 
	public String getMatrixId() {
		return matrixId;
	}
	public void setMatrixId(String matrixId) {
		this.matrixId = matrixId;
	}
	
	@OneToOne(cascade=CascadeType.ALL)  
	@PrimaryKeyJoinColumn
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Column(name="research_method")
	public String getResearchMethod() {
		return researchMethod;
	}
	public void setResearchMethod(String researchMethod) {
		this.researchMethod = researchMethod;
	}
	
	@Column(name="univ_general")
	public String getUnivGeneral() {
		return univGeneral;
	}
	public void setUnivGeneral(String univGeneral) {
		this.univGeneral = univGeneral;
	}
	
	@Column(name="assessment_1_status")
	public String getAssessment1Status() {
		return assessment1Status;
	}
	public void setAssessment1Status(String assessment1Status) {
		this.assessment1Status = assessment1Status;
	}
	
	@Column(name="assessment_1_result")
	public String getAssessment1Result() {
		return assessment1Result;
	}
	public void setAssessment1Result(String assessment1Result) {
		this.assessment1Result = assessment1Result;
	}
	
	@Column(name="colloquium_status")
	public String getColloquiumStatus() {
		return colloquiumStatus;
	}
	public void setColloquiumStatus(String colloquiumStatus) {
		this.colloquiumStatus = colloquiumStatus;
	}
	
	@Column(name="colloquium_result")
	public String getColloquiumResult() {
		return colloquiumResult;
	}
	public void setColloquiumResult(String colloquiumResult) {
		this.colloquiumResult = colloquiumResult;
	}
	
	@Column(name="viva_status")
	public String getVivaStatus() {
		return vivaStatus;
	}
	public void setVivaStatus(String vivaStatus) {
		this.vivaStatus = vivaStatus;
	}
	
	@Column(name="viva_result")
	public String getVivaResult() {
		return vivaResult;
	}
	public void setVivaResult(String vivaResult) {
		this.vivaResult = vivaResult;
	}

}
