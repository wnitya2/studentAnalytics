package my.ais.business.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.ais.domain.Program;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProgramDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Program> queryAll() {
		Query query = em.createQuery("from Program as p order by p.programId");
		List<Program> result = query.getResultList();		
		return result;
	}
	
	@Transactional(readOnly=true)
	public Program get(String programId){
		return em.find(Program.class, programId);
	}
	
	
}
