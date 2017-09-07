package my.ais.business.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.ais.domain.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<User> queryAll() {
        Query query = em.createQuery("from User as u order by u.username");
        List<User> result = query.getResultList();          
        return result;
    }
	
	@Transactional(readOnly=true)
	public User get(String username){
		User u = em.find(User.class, username);
		return u;
	}
	
	@Transactional
	public User save(User user){
		em.persist(user);
		return user;
	}
	
	@Transactional
	public User update (User user){
		user = em.merge(user);
		return user;
	}
	
	@Transactional
	public void delete (User user){
		User u = get(user.getUsername());
		if(u!=null){
			em.remove(u);
		}
	}	
	
}
