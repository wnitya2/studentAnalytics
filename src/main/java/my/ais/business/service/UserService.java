/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import java.util.List;

import my.ais.domain.User;

public interface UserService {

	boolean login (String username, String password);
	void logout();	
	List<User> queryAll();
	User findUser(String username);	
	User updateUser(User user);
	void saveUser(User user);
	void deleteUser(User user);
}
