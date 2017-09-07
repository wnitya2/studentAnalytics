package my.ais.model;

import java.math.BigInteger;

public class ProgramCount implements Comparable<ProgramCount> {
	private String programId;
	private BigInteger count;
	
	public ProgramCount (String programId, BigInteger count){
		super();
		this.programId = programId;
		this.count = count;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public int compareTo(ProgramCount compareProgramCount) {
		BigInteger compareCount = compareProgramCount.getCount();
		
		//descending order
		return compareCount.intValue() - this.count.intValue();
	}

}
