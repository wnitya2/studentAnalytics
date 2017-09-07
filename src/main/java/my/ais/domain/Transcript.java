package my.ais.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transcript")
public class Transcript implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
	
	private TranscriptId transcriptId;
	private Student student;
	private int creditExempted;
	private int creditTransferred;
	private String courseGrade;
	private int courseCredit;
	private String courseDesc;
	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "matrixId", column = @Column(name = "matrix_id", nullable = false)),
		@AttributeOverride(name = "courseId", column = @Column(name = "course_id", nullable = false)),
		@AttributeOverride(name = "sem", column = @Column(name = "sem", nullable = false))
	})	
	public TranscriptId getTranscriptId() {
		return transcriptId;
	}
	public void setTranscriptId(TranscriptId transcriptId) {
		this.transcriptId = transcriptId;
	}
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "matrix_id", nullable = false, insertable = false, updatable = false)
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Column(name="credit_exempted")
	public int getCreditExempted() {
		return creditExempted;
	}
	public void setCreditExempted(int creditExempted) {
		this.creditExempted = creditExempted;
	}
	
	@Column(name="credit_transferred")
	public int getCreditTransferred() {
		return creditTransferred;
	}
	public void setCreditTransferred(int creditTransferred) {
		this.creditTransferred = creditTransferred;
	}
	
	@Column(name="course_grade")
	public String getCourseGrade() {
		return courseGrade;
	}
	public void setCourseGrade(String courseGrade) {
		this.courseGrade = courseGrade;
	}
	
	@Column(name="course_credit")
	public int getCourseCredit() {
		return courseCredit;
	}
	public void setCourseCredit(int courseCredit) {
		this.courseCredit = courseCredit;
	}
	
	@Column(name="course_desc")
	public String getCourseDesc() {
		return courseDesc;
	}
	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}
	
}
