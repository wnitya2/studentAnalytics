/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import my.ais.domain.UserCredential;

public interface AuthenticationService {

	//get current user credential
	public UserCredential getUserCredential();
	
}
