package my.ais.controller.userManagement;

import java.util.HashMap;
import java.util.Map;

import my.ais.business.service.UserService;
import my.ais.domain.User;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateUserController extends SelectorComposer <Component> {
	 private static final long serialVersionUID = 1L;
	 
	 @Wire
	 Window modalDialog; 	 
	 @Wire
	 Textbox username;
	 @Wire
	 Textbox fullName;
	 @Wire
	 Textbox password;
	 @Wire
	 Combobox role;
	 
	 @WireVariable
	 UserService userService;
	 
	 private String recordMode;
	 private User selectedUser;
	 private Window parentWindow;
	 
	 @Override
   	 public void doAfterCompose(Component comp) throws Exception {
		 super.doAfterCompose(comp);
		 
		 final Execution execution = Executions.getCurrent();
		 setSelectedUser((User) execution.getArg().get("selectedRecord"));
		 setRecordMode((String) execution.getArg().get("recordMode"));
		 setParentWindow((Window) execution.getArg().get("parentWindow"));		 
	 }
     
	 @Listen("onClick = #resetBtn")
	 public void resetForm() {
		 username.setValue(null);
		 fullName.setValue(null);
		 password.setValue(null);
		 role.setValue(null);		 
	 }
	 
	 @Listen("onClick = #saveBtn")
	 public void doSave(){
		 User u = new User();
		 u.setUsername(username.getValue());
		 u.setFullName(fullName.getValue());
		 u.setRole(role.getValue());
		 u.setPassword(password.getValue());	
		 
		 try{
			 userService.saveUser(u);
			 Map<String, Object> args = new HashMap<String, Object>();
			 modalDialog.detach();		 
			 args.put("selectedRecord", u);
			 args.put("recordMode", this.recordMode);
			 Events.sendEvent(new Event("onSaved", parentWindow, args));			 
		 } 
		 catch (Exception e){
			 Messagebox.show("Username already exists!");
		 }	 	 
	 }
	 
	 public User getSelectedUser() {
		 return selectedUser;
	 }

	 public void setSelectedUser(User selectedUser) {
		 this.selectedUser = selectedUser;
	 }

	 public String getRecordMode() {
		 return recordMode;
	 }

	 public void setRecordMode(String recordMode) {
		 this.recordMode = recordMode;
	 }

	 public Window getParentWindow() {
		 return parentWindow;
	 }

	 public void setParentWindow(Window parentWindow) {
		 this.parentWindow = parentWindow;
	 }
	 
	 
}
