package my.ais.business.service;

import java.util.List;

import my.ais.business.dao.TranscriptDao;
import my.ais.domain.Transcript;
import my.ais.util.Grading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("transcriptService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class TranscriptServiceImpl implements TranscriptService{
	
	@Autowired
	TranscriptDao transcriptDao;
	
	public List<Transcript> getAllResult(String matrixId) {
		return transcriptDao.queryResultAll(matrixId);
	}

	public List<Transcript> getResultSem(String matrixId) {			
		return transcriptDao.queryResultSemByMatrix(matrixId);
	}
	
	public List<Transcript> getCreditExempted(String matrixId) {		
		return transcriptDao.queryResultCreditExemptedByMatrix(matrixId);
	}
	
	public List<Transcript> getCreditTransferred(String matrixId) {		
		return transcriptDao.queryResultCreditTransferredByMatrix(matrixId);
	}

	public int getTotalCredits(String matrixId) {
		int result = 0;
		result = new Grading().calculateTotalCredit(getAllResult(matrixId));		
		return result;
	}

	public double getCGPA(String matrixId) {		
    	double result = 0.00;   	
    	    	
    	if (getAllResult(matrixId).size() > 0){
    		result = new Grading().calculateGPA(getAllResult(matrixId));
    	}    	    	
		return result;
	}

}
