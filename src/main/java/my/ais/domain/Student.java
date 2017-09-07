package my.ais.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "student")
public class Student implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
		
	private String matrixId;
	private String ic;	
	private String fullName;	
	private String country;	
	private String level;	
	private String courseType;
	private String mode;	
	private String enrolmentSem;
	private String enrolmentDate;	
	private String email;	
	private String tel;	
	private String currentSem;
	private String gradYear;
	
	private Program program;
	private String programId; //transient (for jasperReport)
	private String programDesc; //transient (for jasperReport)
	
	private Status status;
	private int statusId; //transient (for jasperReport)
	private String statusDesc; //transient (for jasperReport)
	
	private int totalCredits; //transient
	private String cgpa; //transient
	
	private Set<Transcript> transcripts = new HashSet<Transcript>(0);	
	private ResearchStudentProgress researchStudentProgress;

	@Id
	@Column(name="matrix_id")
	public String getMatrixId() {
		return matrixId;
	}

	public void setMatrixId(String matrixId) {
		this.matrixId = matrixId;
	}

	@Column(name="ic")
	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	@Column(name="full_name")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="level")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name="course_type")
	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	@Column(name="mode")
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Column(name="enrolment_sem")
	public String getEnrolmentSem() {
		return enrolmentSem;
	}

	public void setEnrolmentSem(String enrolmentSem) {
		this.enrolmentSem = enrolmentSem;
	}

	@Column(name="enrolment_date")
	public String getEnrolmentDate() {
		return enrolmentDate;
	}

	public void setEnrolmentDate(String enrolmentDate) {
		this.enrolmentDate = enrolmentDate;
	}

	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="tel")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@Column(name="current_sem")
	public String getCurrentSem() {
		return currentSem;
	}

	public void setCurrentSem(String currentSem) {
		this.currentSem = currentSem;
	}

	@Column(name="grad_year")
	public String getGradYear() {
		return gradYear;
	}

	public void setGradYear(String gradYear) {
		this.gradYear = gradYear;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn( name = "program_id" )
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	@Transient
	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	@Transient
	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
	public Set<Transcript> getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(Set<Transcript> transcripts) {
		this.transcripts = transcripts;
	}

	@Transient
	public int getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(int totalCredits) {
		this.totalCredits = totalCredits;
	}

	@Transient
	public String getCgpa() {
		return cgpa;
	}

	public void setCgpa(String cgpa) {
		this.cgpa = cgpa;
	}

	@OneToOne(mappedBy="student", cascade=CascadeType.ALL)
	public ResearchStudentProgress getResearchStudentProgress() {
		return researchStudentProgress;
	}

	public void setResearchStudentProgress(
			ResearchStudentProgress researchStudentProgress) {
		this.researchStudentProgress = researchStudentProgress;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "status_id" )
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Transient
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	@Transient
	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}		
	
}
