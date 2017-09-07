package my.ais.controller.studentManagement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import my.ais.business.service.ProgramService;
import my.ais.business.service.StudentService;
import my.ais.domain.Program;
import my.ais.domain.Student;
import my.ais.model.StudentSearchCategoryEntity;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class ManageStudentController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window win;
	@Wire
    private Combobox statusCombobox;
	@Wire
    private Combobox countryCombobox;
	@Wire
    private Combobox modeCombobox;
	@Wire
    private Combobox programCombobox;
	@Wire
	private Combobox levelCombobox;
	@Wire
	private Textbox matrixBox;
	@Wire
	private Textbox studentNameBox;
	@Wire
	private Textbox enrolBox;
	@Wire
	private Textbox gradYearBox;
	@Wire
	private Listbox studentListBox;
	@Wire
	private Component searchResultBlock;	
	
	@WireVariable
	StudentService studentService;
	
	@WireVariable
	ProgramService programService;
	
	private List<Student> result;
	private HashMap<String, String> categoriesMap;
	private Student selectedStudent;
	
	public ManageStudentController(){		
				
	}
		
	private ListModel<String> statusModel = new ListModelList<String>(StudentSearchCategoryEntity.getStatus());
	private ListModel<Program> programModel;
	private ListModel<String> countryModel = new ListModelList<String>(StudentSearchCategoryEntity.getCountry());
	private ListModel<String> modeModel = new ListModelList<String>(StudentSearchCategoryEntity.getMode());
	private ListModel<String> levelModel = new ListModelList<String>(StudentSearchCategoryEntity.getLevel());
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		List<Program> programList = programService.getProgramListNoDuplicate();
		programModel = new ListModelList<Program>(programList);
		programCombobox.setModel(programModel);
	};
	
	@Listen("onClick = #addStudentBtn")
	public void doCreate(Event e){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", null);
		map.put("recordMode", "NEW");
		map.put("parentWindow", win);
		Window window = (Window)Executions.createComponents(
				"/stad_03_data_entry/stad_03_add_student.zul", null, map);
		window.doModal();
	}
	
	@Listen("onClick = #searchButton")
	public void search(){	
		
		searchResultBlock.setVisible(true);		
		String[] program = programCombobox.getValue().split("-");
		
		categoriesMap = new LinkedHashMap<String, String>();
		if(!statusCombobox.getValue().isEmpty()){categoriesMap.put("statusKey", statusCombobox.getValue());}
		if(!levelCombobox.getValue().isEmpty()){categoriesMap.put("levelKey", levelCombobox.getValue());}
		if(!modeCombobox.getValue().isEmpty()){categoriesMap.put("modeKey", modeCombobox.getValue());}
		if(!programCombobox.getValue().isEmpty()){categoriesMap.put("programKey", program[0]);} //only the code
		if(!countryCombobox.getValue().isEmpty()){categoriesMap.put("countryKey", countryCombobox.getValue());}
		if(!matrixBox.getValue().isEmpty()){categoriesMap.put("matrixKey", matrixBox.getValue());}
		if(!studentNameBox.getValue().isEmpty()){categoriesMap.put("studentKey", studentNameBox.getValue());}
		if(!enrolBox.getValue().isEmpty()){categoriesMap.put("enrolKey", enrolBox.getValue());}
		if(!gradYearBox.getValue().isEmpty()){categoriesMap.put("gradYearKey", gradYearBox.getValue());}
		
		//set the model
		result = studentService.getStudentListByCategory(categoriesMap);	
		studentListBox.setModel(new ListModelList<Student>(result));						
	}		

	@Listen("onDetail = #studentListBox")
	public void doDetail(ForwardEvent e){
		Button btn = (Button)e.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		selectedStudent = (Student) litem.getValue();
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("selectedRecord", selectedStudent);
		
		Window window = (Window)Executions.createComponents(
				"/stad_03_data_entry/stad_03_view_detail.zul", null, args);
		window.doModal();		
	}
	
	@Listen("onUpdate = #studentListBox")
	public void doUpdate(ForwardEvent e){
		Button btn = (Button)e.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		selectedStudent = (Student) litem.getValue();		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", selectedStudent);
		map.put("recordMode", "EDIT");
		map.put("parentWindow", win);
		Window window = (Window)Executions.createComponents(
				"/stad_03_data_entry/stad_03_update_detail.zul", null, map);
		window.doModal();		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onDelete = #studentListBox")
	public void doDelete(ForwardEvent e){
		Button btn = (Button)e.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		selectedStudent = (Student) litem.getValue();
		
		Messagebox.show("Are you sure to delete this student '"+selectedStudent.getFullName()+"'?", "Question",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {
							studentService.deleteStudent(selectedStudent);	
							result.remove(selectedStudent);
							Clients.showNotification("Student '"+selectedStudent.getFullName()+"' deleted." );
							refreshViewDetail();
						} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
							// Cancel is clicked
						}
					}
				});			
	}
	
	@Listen("onClick = #resetButton")
	public void resetForm(){
		statusCombobox.setValue("");
		levelCombobox.setValue("");
		modeCombobox.setValue("");
		programCombobox.setValue("");
		countryCombobox.setValue("");
		matrixBox.setValue("");
		studentNameBox.setValue("");
		enrolBox.setValue("");		
		searchResultBlock.setVisible(false);
	}	
	
	/**
	 * This method is actually an event handler triggered only from the edit
	 * dialog and it is responsible to reflect the data changes made to the
	 * list.
	 */
	@SuppressWarnings({ "unchecked" })	
	@Listen("onSaved = #win")
	public void onSaved(Event event) {
		Map<String, Object> args = (Map<String, Object>) event.getData();
		String recordMode = (String) args.get("recordMode");
		Student curStudent = (Student) args.get("selectedRecord");
		if (recordMode.equals("NEW")) {
			if (result!= null){
				result.add(curStudent);
			}
			else{
				result = new LinkedList<Student>();
				result.add(curStudent);
			}
			refreshViewDetail();
			Clients.showNotification("Student '"+curStudent.getFullName()+"' added." );
		}		
		if (recordMode.equals("EDIT")) {
			result.set(result.indexOf(curStudent),curStudent);			
			Clients.showNotification("Student '"+curStudent.getFullName()+"' updated." );
			refreshViewDetail();
		}		
	}
	
	public ListModel<String> getCountryModel() {
        return countryModel;
    }
	public ListModel<String> getModeModel() {
        return modeModel;
    }

	public ListModel<String> getStatusModel() {
		return statusModel;
	}

	public ListModel<String> getLevelModel() {
		return levelModel;
	}			
	
	private void refreshViewDetail(){		
		searchResultBlock.setVisible(true);
		this.studentListBox.setModel(new ListModelList<Student>(result));		
	}
}
