package my.ais.controller.userManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.ais.business.service.UserService;
import my.ais.domain.User;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class ManageUserController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window winManageUser;
	@Wire
	private Listbox userListBox;
	
	@WireVariable
	UserService userService;
	
	private List<User> result;	
	private User selectedUser;
	
	public ManageUserController(){
		
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {		
		super.doAfterCompose(comp);
		
		result = userService.queryAll();		
		this.userListBox.setModel(new ListModelList<User>(result));
	}
	
	@Listen("onClick = #createUserBtn")
	public void doCreate(Event e){		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", null);
		map.put("recordMode", "NEW");
		map.put("parentWindow",winManageUser);	
		Window window = (Window)Executions.createComponents(
				"/stad_02_system_administration/stad_02_create_user.zul", null, map);
		window.doModal();
	}
	
	@Listen("onEdit = #userListBox")
	public void doEdit(ForwardEvent e){
		Button btn = (Button)e.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		selectedUser = (User) litem.getValue();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", selectedUser);
		map.put("recordMode", "EDIT");
		map.put("parentWindow", winManageUser);	
		Window window = (Window)Executions.createComponents(
				"/stad_02_system_administration/stad_02_edit_user.zul", null, map);
		window.doModal();	
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onDelete = #userListBox")
	public void doDelete(ForwardEvent e){
		Button btn = (Button)e.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		selectedUser = (User) litem.getValue();
		
		Messagebox.show("Are you sure to delete this user '"+selectedUser.getUsername()+"'?", "Question",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {
							userService.deleteUser(selectedUser);
							result.remove(selectedUser);							
							Clients.showNotification("User '"+selectedUser.getUsername()+"' deleted." );
							refreshViewDetail();

						} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
							// Cancel is clicked
						}
					}
				});			
	}
	
	/**
	 * This method is actually an event handler triggered only from the edit
	 * dialog and it is responsible to reflect the data changes made to the
	 * list.
	 */
	@SuppressWarnings({ "unchecked" })	
	@Listen("onSaved = #winManageUser")
	public void onSaved(Event event) {
		Map<String, Object> args = (Map<String, Object>) event.getData();
		String recordMode = (String) args.get("recordMode");
		User curUser = (User) args.get("selectedRecord");
		if (recordMode.equals("NEW")) {
			result.add(curUser);
			refreshViewDetail();
			Clients.showNotification("User '"+curUser.getUsername()+"' added." );
		}		
		if (recordMode.equals("EDIT")) {
			result.set(result.indexOf(curUser),curUser);
			refreshViewDetail();
			Clients.showNotification("User '"+curUser.getUsername()+"' updated." );
		}		
	}
	
	private void refreshViewDetail(){
		this.userListBox.setModel(new ListModelList<User>(result));
	}
}
