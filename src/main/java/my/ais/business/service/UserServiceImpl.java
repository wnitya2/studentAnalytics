/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import java.util.List;

import my.ais.business.dao.UserDao;
import my.ais.domain.User;
import my.ais.domain.UserCredential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

@Service("userService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserDao userDao;
	
	public boolean login(String username, String password) {
		User user = userDao.get(username);		
		if(user==null || !user.getPassword().equals(password)){
			return false;
		}
		
		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getUsername(), user.getFullName(), user.getRole());
		if(cre.isAnonymous()){
			return false;
		}		
		sess.setAttribute("userCredential", cre);
		
		//TODO handle the role here for authorization
		return true;		
	}
	
	public void logout() {		
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("userCredential");
	}	
	
	public User findUser(String username) {
		return userDao.get(username);
	}

	public User updateUser(User user) {
		return userDao.update(user);
	}

	public void saveUser(User user) {
		userDao.save(user);		
	}

	public void deleteUser(User user) {
		userDao.delete(user);
		
	}

	public List<User> queryAll() {		
		return userDao.queryAll();
	}

	

	

		
	
}
