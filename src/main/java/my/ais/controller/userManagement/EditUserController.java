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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class EditUserController extends SelectorComposer <Component> {
    private static final long serialVersionUID = 1L;
     
    @Wire
    Window modalDialog;
    @Wire
    Textbox fullName;
	@Wire
	Textbox password;
	@Wire
	Combobox role;
    
    private User user;
    
    @WireVariable
	UserService userService;
	 
	private String recordMode;
	private Window parentWindow;
    
	@Override
   	public void doAfterCompose(Component comp) throws Exception {
   		super.doAfterCompose(comp);

   		final Execution execution = Executions.getCurrent();   		
		setRecordMode((String) execution.getArg().get("recordMode"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
        setUser((User) execution.getArg().get("selectedRecord"));
        comp.setAttribute("cp", this);
    }
     
    @Listen("onClick = #updateBtn")
    public void doUpdate(Event e) {
    	user.setFullName(fullName.getValue());
    	user.setRole(role.getValue());
    	user.setPassword(password.getValue());		 
		userService.updateUser(user);		
		
		modalDialog.detach();
		Map<String, Object> args = new HashMap<String, Object>();				 
		args.put("selectedRecord", user);
		args.put("recordMode", this.recordMode);
		Events.sendEvent(new Event("onSaved", parentWindow, args));
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
