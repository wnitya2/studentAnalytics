/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.controller.authentication;

import my.ais.business.service.AuthenticationService;
import my.ais.business.service.AuthenticationServiceImpl;
import my.ais.business.service.UserService;
import my.ais.domain.UserCredential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class LoginController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	Textbox username;
	@Wire
	Textbox password;
	@Wire
	Label message;
	
	@WireVariable
	UserService userService;	
	
	AuthenticationService authService = new AuthenticationServiceImpl();

	
	@Listen("onClick=#login; onOK=#loginWin")
	public void doLogin(){
		String nm = username.getValue();
		String pd = password.getValue();		
		
		if(!userService.login(nm,pd)){
			message.setValue("username or password are not correct.");
			return;
		}
		
		UserCredential cre= authService.getUserCredential();
		message.setValue("Welcome, "+cre.getName());
		message.setSclass("");
		
		Executions.sendRedirect("/index.zul");
		
	}
}
