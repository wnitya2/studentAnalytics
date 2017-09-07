package my.ais.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TranscriptId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String matrixId;
	private String courseId;
	private String sem;
	
	@Column(name = "matrix_id", nullable = false)
	public String getMatrixId() {
		return matrixId;
	}
	public void setMatrixId(String matrixId) {
		this.matrixId = matrixId;
	}
	
	@Column(name = "course_id", nullable = false)
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Column(name = "sem", nullable = false)
	public String getSem() {
		return sem;
	}
	public void setSem(String sem) {
		this.sem = sem;
	}
	
	public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof TranscriptId))
            return false;
        TranscriptId castOther = (TranscriptId) other;
 
        return (this.getCourseId() == castOther.getCourseId())
                && (this.getSem() == castOther.getSem())
                && (this.getMatrixId() == castOther.getMatrixId());
    } 
	
	public int hashCode(){
		int result = 17;
		
		result = 37 * result + this.getCourseId().hashCode();
		result = 37 * result + this.getSem().hashCode();
		result = 37 * result + this.getMatrixId().hashCode();
		
		return result;
		
	}

}
