package my.ais.controller.studentManagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class UpdateDetailStudentController extends SelectorComposer <Component> {
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
	 private Label matrixBox;
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
	 
	 private Student student;
	 private Window parentWindow;
	 private String recordMode;
	 
	 @WireVariable
	 StudentService studentService;
		
	 @WireVariable
	 ProgramService programService;
		
	 @WireVariable
	 StatusService statusService;
	 	 
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
		 setStudent((Student) execution.getArg().get("selectedRecord"));
		 setRecordMode((String) execution.getArg().get("recordMode"));
		 setParentWindow((Window) execution.getArg().get("parentWindow"));
		 comp.setAttribute("cp", this);	
		 
		 //formatting purpose
		 String dateDB = student.getEnrolmentDate(); //yyyyMMdd format	by default		 
		 if (dateDB.length()<8){//some of them are null or incomplete
			 enrolDateBox.setValue(null);
		 }
		 else{
			 DateFormat df = new SimpleDateFormat("yyyy/MM/dd");	 
			 enrolDateBox.setValue(df.parse(dateDB.substring(0,4)+"/"+dateDB.substring(4,6)+"/"+dateDB.substring(6,8)));
		 }		 
		 currentSem1Box.setValue(student.getCurrentSem().split("/")[0]);
		 currentSem2Box.setValue(student.getCurrentSem().split("/")[1]);
		 programCombobox.setValue(student.getProgramId()+"-"+student.getProgramDesc());	 
		 
		 //condition for status
		 if (student.getStatus().getStatusDesc().equalsIgnoreCase("GRADUATED")){
			 gradYearRow.setVisible(true);
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
	 	 
	 @Listen("onClick = #updateBtn")
	 public void doSave(){		
		 student.setIc(icBox.getValue());
		 student.setFullName(studentNameBox.getValue());		 
		 student.setStatus(statusService.getStatusByDesc(statusCombobox.getValue()));
		 student.setGradYear(gradYearBox.getValue());
		 
		 //TODO: still in DEV MODE
		 //IMPORTANT: if the updated value is null or incomplete, just stick to its ORIGINAL value!
		 //otherwise it will be updated as empty and cannot be used for data analytics module!
		 if (enrolDateBox.getValue()!=null){
			 student.setEnrolmentDate(UtilDate.getFormattedDate_yyyyMMdd(enrolDateBox.getValue()));
		 }
		  
		 student.setEnrolmentSem(enrolSemBox.getValue());
		 student.setCurrentSem(currentSem1Box.getValue()+"/"+currentSem2Box.getValue());
		 student.setCourseType(courseTypeCombobox.getValue());
		 student.setMode(modeCombobox.getValue());
		 
		 String programId = programCombobox.getValue().split("-")[0];		 
		 Program capturedProgram = programService.getProgramById(programId);		 
		 student.setProgram(capturedProgram);		 
		 student.setCountry(countryCombobox.getValue());
		 student.setEmail(emailBox.getValue());
		 student.setTel(telBox.getValue());
		 		 	 
		 try{
			 studentService.updateStudent(student);	 
			 modalDialog.detach();	
			 Map<String, Object> args = new HashMap<String, Object>();				 
			 args.put("selectedRecord", student);
			 args.put("recordMode", this.recordMode);
			 Events.sendEvent(new Event("onSaved", parentWindow, args));
		 } 
		 catch (Exception e){
			 Messagebox.show(e.toString());
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
	 
	 public Student getStudent() {
		 return student;
	 }

	 public void setStudent(Student student) {
		this.student = student;
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
