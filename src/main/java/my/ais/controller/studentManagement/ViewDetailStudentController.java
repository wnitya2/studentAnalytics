package my.ais.controller.studentManagement;

import my.ais.domain.Student;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class ViewDetailStudentController extends SelectorComposer <Component> {
	 private static final long serialVersionUID = 1L;
	 
	 @Wire
	 Window modalDialog; 		 
	 @Wire 
	 Label enrolmentDateLabel;
	 
	 private Student student;
	 
	 
	 @Override
   	 public void doAfterCompose(Component comp) throws Exception {
		 super.doAfterCompose(comp);
		 
		 final Execution execution = Executions.getCurrent();   		
		 setStudent((Student) execution.getArg().get("selectedRecord"));
		 comp.setAttribute("cp", this);		 
		 
		 //formatting
		 if (student.getEnrolmentDate().length()==8){
			 enrolmentDateLabel.setValue(student.getEnrolmentDate().substring(0,4)+"/"+
 					 student.getEnrolmentDate().substring(4,6)+"/"+
 					 student.getEnrolmentDate().substring(6,8));
		 }else{
			 enrolmentDateLabel.setValue(student.getEnrolmentDate());
		 }		 	
	 }	 
	 
	 public Student getStudent() {
		 return student;
	 }

	 public void setStudent(Student student) {
		this.student = student;
	 }
}
