package my.ais.controller.studentManagement;

import my.ais.domain.Student;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class StudentViewDetailController extends SelectorComposer <Component> {
    private static final long serialVersionUID = 1L;
     
    @Wire
    Window modalDialog;
    
    private Student student;
    
	@Override
   	public void doAfterCompose(Component comp) throws Exception {
   		super.doAfterCompose(comp);

   		final Execution execution = Executions.getCurrent();
        setStudent((Student) execution.getArg().get("studentObj"));
        comp.setAttribute("cp", this);
    }
     
    @Listen("onClick = #closeBtn")
    public void showModal(Event e) {
        modalDialog.detach();
    }

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
    
    
}
