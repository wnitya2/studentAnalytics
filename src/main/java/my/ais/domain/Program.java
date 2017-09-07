package my.ais.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="program")
public class Program implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="program_id")
	private String programId;
	
	@Column(name="program_desc")
	private String programDesc;

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

}
