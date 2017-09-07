package my.ais.business.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.ais.domain.Status;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StatusDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Status> queryAll() {
		Query query = em.createQuery("from Status as s order by s.statusId");
		List<Status> result = query.getResultList();		
		return result;
	}
	
	@Transactional(readOnly=true)
	public Status get(int statusId){
		return em.find(Status.class, statusId);
	}
	
	@Transactional(readOnly=true)
	public Object getStatusId(String statusDesc) {
		Query query = em.createNativeQuery("SELECT status_id FROM status WHERE status_desc='"+statusDesc+"';");			
		return query.getSingleResult();
	}	
}
