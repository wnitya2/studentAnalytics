/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.controller.authentication;

import java.util.Map;

import my.ais.business.service.AuthenticationService;
import my.ais.business.service.AuthenticationServiceImpl;
import my.ais.domain.UserCredential;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;


public class AuthenticationInit implements Initiator {
	AuthenticationService authService = new AuthenticationServiceImpl();	
	public void doInit(Page page, Map<String, Object> args) throws Exception {		
		UserCredential cre = authService.getUserCredential();		
		if(cre==null || cre.isAnonymous()){
			Executions.sendRedirect("/login.zul");
			return;
		}
	}
}