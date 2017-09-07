/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.controller.authentication;

import my.ais.business.service.UserService;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

@VariableResolver(DelegatingVariableResolver.class)
public class LogoutController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@WireVariable
	UserService userService;
	
	@Listen("onClick=#logout")
	public void doLogout(){
		userService.logout();		
		Executions.sendRedirect("/index.zul");
	}
}
