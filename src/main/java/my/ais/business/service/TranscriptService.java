package my.ais.business.service;

import java.util.List;

import my.ais.domain.Transcript;

public interface TranscriptService {	
	
	List<Transcript> getAllResult(String matrixId);
	List<Transcript> getResultSem(String matrixId);
	List<Transcript> getCreditExempted(String matrixId);
	List<Transcript> getCreditTransferred(String matrixId);
	int getTotalCredits(String matrixId);
	double getCGPA(String matrixId);
	
}
