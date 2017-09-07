package my.ais.controller.studentManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.ais.business.service.ProgramService;
import my.ais.business.service.StatusService;
import my.ais.business.service.StudentService;
import my.ais.domain.Program;
import my.ais.domain.Student;
import my.ais.model.StudentSearchCategoryEntity;
import my.ais.util.UtilDate;

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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class AddStudentController extends SelectorComposer <Component> {
	 private static final long serialVersionUID = 1L;
	 
	 @Wire
	 Window modalDialog; 	 
	 @Wire
	 private Combobox statusCombobox;
	 @Wire
	 private Textbox gradYearBox;
	 @Wire
	 private Row gradYearRow;
	 @Wire
	 private Combobox countryCombobox;
	 @Wire
	 private Combobox courseTypeCombobox;
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
	 private Textbox icBox;
	 @Wire
	 private Textbox enrolSemBox;
	 @Wire
	 private Datebox enrolDateBox;
	 @Wire
	 private Textbox currentSem1Box;
	 @Wire
	 private Textbox currentSem2Box;
	 @Wire
	 private Textbox emailBox;
	 @Wire
	 private Textbox telBox;
	 @Wire
	 private Listbox studentListBox;
	 
	 @WireVariable
	 StudentService studentService;
		
	 @WireVariable
	 ProgramService programService;
		
	 @WireVariable
	 StatusService statusService;
	 
	 private String recordMode;
	 private Window parentWindow;
	 	 
	 private ListModel<String> statusModel;
	 private ListModel<Program> programModel;
	 private ListModel<String> countryModel = new ListModelList<String>(StudentSearchCategoryEntity.getCountry());
	 private ListModel<String> modeModel = new ListModelList<String>(StudentSearchCategoryEntity.getMode());
	 private ListModel<String> courseTypeModel = new ListModelList<String>(StudentSearchCategoryEntity.getCourseType());
	 private ListModel<String> levelModel = new ListModelList<String>(StudentSearchCategoryEntity.getLevel());
	 
	 @Override
   	 public void doAfterCompose(Component comp) throws Exception {
		 super.doAfterCompose(comp);
		 
		 List<Program> programList = programService.getProgramListNoDuplicate();
		 programModel = new ListModelList<Program>(programList);
		 programCombobox.setModel(programModel);
		
		 List<String> stringDescList = statusService.getStatusDescListAll();
		 statusModel = new ListModelList<String>(stringDescList);
		 statusCombobox.setModel(statusModel);	 		 
		 
		 final Execution execution = Executions.getCurrent();		 
		 setRecordMode((String) execution.getArg().get("recordMode"));
		 setParentWindow((Window) execution.getArg().get("parentWindow"));
	 }
     
	 @Listen("onClick = #resetBtn")
	 public void resetForm() {
		 matrixBox.setValue(null);
		 icBox.setValue(null);
		 studentNameBox.setValue(null);
		 statusCombobox.setValue(null);	
		 enrolDateBox.setValue(null);
		 enrolSemBox.setValue(null);
		 currentSem1Box.setValue(null);
		 currentSem2Box.setValue(null);
		 courseTypeCombobox.setValue(null);
		 modeCombobox.setValue(null);
		 programCombobox.setValue(null);
		 countryCombobox.setValue(null);
		 emailBox.setValue(null);
		 telBox.setValue(null);
		 gradYearBox.setValue(null);
		 gradYearRow.setVisible(false);
	 }
	 
	 @Listen("onClick = #saveBtn")
	 public void doSave(){		
		 Student s = new Student();
		 s.setMatrixId(matrixBox.getValue());
		 s.setIc(icBox.getValue());
		 s.setFullName(studentNameBox.getValue());		 
		 s.setStatus(statusService.getStatusByDesc(statusCombobox.getValue()));
		 s.setEnrolmentDate(UtilDate.getFormattedDate_yyyyMMdd(enrolDateBox.getValue()));
		 s.setEnrolmentSem(enrolSemBox.getValue());
		 s.setCurrentSem(currentSem1Box.getValue()+"/"+currentSem2Box.getValue());
		 s.setCourseType(courseTypeCombobox.getValue());
		 s.setMode(modeCombobox.getValue());
		 s.setLevel(matrixBox.getValue().substring(0,1).equalsIgnoreCase("M")? "MASTER" : "PHD");
		 s.setProgram(programService.getProgramById(programCombobox.getValue().split("-")[0]));		 
		 s.setCountry(countryCombobox.getValue());
		 s.setEmail(emailBox.getValue());
		 s.setTel(telBox.getValue());
		 s.setGradYear(gradYearBox.getValue());
		 
		 try{
			 studentService.saveStudent(s);			 
			 modalDialog.detach();		 
			 Map<String, Object> args = new HashMap<String, Object>();	
			 args.put("selectedRecord", s);
			 args.put("recordMode", this.recordMode);
			 Events.sendEvent(new Event("onSaved", parentWindow, args));
		 } 
		 catch (Exception e){
			 e.printStackTrace();
			 Messagebox.show("Matrix ID already exists!");
		 }	 	 
	 }	 
	 
	 @Listen("onChange = #statusCombobox")
	 public void statusChanged(){
		 if (statusCombobox.getValue().equalsIgnoreCase("GRADUATED")){
			 gradYearRow.setVisible(true);
		 }
		 else{
			 gradYearBox.setValue("");
			 gradYearRow.setVisible(false);
		 }		 
	 }
	 
	 public ListModel<String> getCountryModel() {
		 return countryModel;
	 }
	 public ListModel<String> getModeModel() {
		 return modeModel;
	 }
	
	 public ListModel<String> getCourseTypeModel() {
		 return courseTypeModel;
	 }

	 public ListModel<String> getStatusModel() {
		 return statusModel;
	 }

	 public ListModel<String> getLevelModel() {
		 return levelModel;
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
