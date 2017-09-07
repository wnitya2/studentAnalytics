/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import java.util.List;

import my.ais.model.SidebarPage;

public interface SidebarPageConfigService {
	/** get pages of this application **/
	public List<SidebarPage> getPages();
	
	/** get page **/
	public SidebarPage getPage(String name);
}