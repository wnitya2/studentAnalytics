package my.ais.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="status")
public class Status implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
		
	private int statusId;
	private String statusDesc;
	
	@Id
	@Column(name="status_id")
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Column(name="status_desc")
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
