package my.ais.business.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import my.ais.domain.Transcript;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TranscriptDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Transcript> queryResultAll(String matrixId){		
		return em.createQuery("select t from Transcript t where t.transcriptId.matrixId LIKE :matrixId")
				.setParameter("matrixId", matrixId)
				.getResultList();		
	}	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Transcript> queryResultSemByMatrix(String matrixId){		
		return em.createQuery("select t from Transcript t where t.transcriptId.matrixId LIKE :matrixId and t.creditExempted = 0 and t.creditTransferred = 0")
				.setParameter("matrixId", matrixId)
				.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Transcript> queryResultCreditExemptedByMatrix(String matrixId){		
		return em.createQuery("select t from Transcript t where t.transcriptId.matrixId LIKE :matrixId and t.creditExempted = 1 and t.creditTransferred = 0")
				.setParameter("matrixId", matrixId)
				.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Transcript> queryResultCreditTransferredByMatrix(String matrixId){		
		return em.createQuery("select t from Transcript t where t.transcriptId.matrixId LIKE :matrixId and t.creditExempted = 0 and t.creditTransferred = 1")
				.setParameter("matrixId", matrixId)
				.getResultList();		
	}
}
