/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import java.io.Serializable;

import my.ais.domain.UserCredential;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class AuthenticationServiceImpl implements AuthenticationService,Serializable{
	private static final long serialVersionUID = 1L;
	
	public UserCredential getUserCredential(){
		Session sess = Sessions.getCurrent();
		UserCredential cre = (UserCredential)sess.getAttribute("userCredential");
		if(cre==null){
			cre = new UserCredential();//new a anonymous user and set to session
			sess.setAttribute("userCredential",cre);
		}
		return cre;
	}
}
