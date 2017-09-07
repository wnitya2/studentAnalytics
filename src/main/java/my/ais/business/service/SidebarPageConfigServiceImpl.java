/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package my.ais.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import my.ais.model.SidebarPage;

public class SidebarPageConfigServiceImpl implements SidebarPageConfigService{
	
	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
	public SidebarPageConfigServiceImpl(){		
		pageMap.put("fn1",new SidebarPage("fn1","Welcome Page","/img/icon/Home.png","/stad_01_welcome_page/stad_01_layout.zul"));
		pageMap.put("fn2",new SidebarPage("fn2","System Administration","/img/icon/System.png","/stad_02_system_administration/stad_02_layout.zul"));
		pageMap.put("fn3",new SidebarPage("fn3","Data Entry","/img/icon/Write.png","/stad_03_data_entry/stad_03_layout.zul"));
		pageMap.put("fn4",new SidebarPage("fn4","Research Student Management","/img/icon/Folder.png","/stad_04_research_student/stad_04_layout.zul"));
		pageMap.put("fn5",new SidebarPage("fn5","Reporting","/img/icon/Paste.png","/stad_05_reporting/stad_05_layout.zul"));
		pageMap.put("fn6",new SidebarPage("fn6","Data Analytics","/img/icon/Stats.png","/stad_06_data_analytics/stad_06_layout.zul"));
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}