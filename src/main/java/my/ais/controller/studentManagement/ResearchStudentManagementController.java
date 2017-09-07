package my.ais.controller.studentManagement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import my.ais.business.service.ProgramService;
import my.ais.business.service.StudentService;
import my.ais.domain.Program;
import my.ais.domain.Student;
import my.ais.model.StudentSearchCategoryEntity;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class ResearchStudentManagementController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window win;	
	@Wire
    private Combobox countryCombobox;
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
	private Listbox studentListBox;
	@Wire
	private Component searchResultBlock;	
	@Wire
	private Component exportBlock;
	
	@WireVariable
	StudentService studentService;
	
	@WireVariable
	ProgramService programService;
	
	private List<Student> result;
	private HashMap<String, String> categoriesMap;
	
	public ResearchStudentManagementController(){		
				
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
	
	@Listen("onClick = #searchButton")
	public void search(){	
		
		searchResultBlock.setVisible(true);		
		String[] program = programCombobox.getValue().split("-");
		
		categoriesMap = new LinkedHashMap<String, String>();		
		if(!levelCombobox.getValue().isEmpty()){categoriesMap.put("levelKey", levelCombobox.getValue());}		
		if(!programCombobox.getValue().isEmpty()){categoriesMap.put("programKey", program[0]);} //only the code
		if(!countryCombobox.getValue().isEmpty()){categoriesMap.put("countryKey", countryCombobox.getValue());}
		if(!matrixBox.getValue().isEmpty()){categoriesMap.put("matrixKey", matrixBox.getValue());}
		if(!studentNameBox.getValue().isEmpty()){categoriesMap.put("studentKey", studentNameBox.getValue());}
		if(!enrolBox.getValue().isEmpty()){categoriesMap.put("enrolKey", enrolBox.getValue());}
		
		//set the model
		result = studentService.getResearchStudentListAll();	
		studentListBox.setModel(new ListModelList<Student>(result));	
		
		//show-hide export options
		if(result.isEmpty()){
			exportBlock.setVisible(false);
		} else{
			exportBlock.setVisible(true);
		}	
	}	
	
	@Listen("onClick = #resetButton")
	public void resetForm(){
		levelCombobox.setValue("");
		programCombobox.setValue("");
		countryCombobox.setValue("");
		matrixBox.setValue("");
		studentNameBox.setValue("");
		enrolBox.setValue("");		
		searchResultBlock.setVisible(false);
		exportBlock.setVisible(false);
	}	
	
	@Listen("onViewDetail = #studentListBox")
	public void doDetail(ForwardEvent evt){
		Button btn = (Button)evt.getOrigin().getTarget();
		Listitem litem = (Listitem)btn.getParent().getParent();
		
		Student student = (Student)litem.getValue();	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("studentObj", student);
		
		Window window = (Window)Executions.createComponents(
					"/stad_04_research_student/stad_04_view_student_detail.zul", null, map);
	    window.doModal();
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
}
