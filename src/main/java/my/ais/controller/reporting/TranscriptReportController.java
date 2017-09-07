package my.ais.controller.reporting;

import java.util.LinkedList;
import java.util.List;

import my.ais.business.service.StudentService;
import my.ais.business.service.TranscriptService;
import my.ais.domain.Student;
import my.ais.domain.Transcript;
import my.ais.model.ResultCreditExemptedGroupModel;
import my.ais.model.ResultCreditTransferredGroupModel;
import my.ais.model.ResultSemGroupModel;
import my.ais.model.TranscriptComparator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class TranscriptReportController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Textbox matrixBox;
	@Wire
	private Listbox creditTransferredListBox;
	@Wire
	private Listbox creditExemptedListBox;
	@Wire
	private Listbox semResultListBox;
	@Wire
	private Component searchResultBlock;
	@Wire
	private Component noResultBlock;	
	@Wire 
	private Grid studentInfoGridCourseWork;
	@Wire 
	private Grid studentInfoGridResearch;
	
	@WireVariable
	TranscriptService transcriptService;
	
	@WireVariable
	StudentService studentService;
	
	private List<Transcript> resultSem;
	private List<Transcript> resultCreditExempted;
	private List<Transcript> resultCreditTransferred;

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);	
	};
	
	@Listen("onClick = #searchButton")
	public void search(){		
		
		String matrixId = matrixBox.getValue();
		matrixBox.setValue(matrixId.toUpperCase());		
		Student student = studentService.getStudentById(matrixId);			
		
		if (student != null){		
			//get result
			resultSem = transcriptService.getResultSem(matrixId);
			resultCreditExempted = transcriptService.getCreditExempted(matrixId);
			resultCreditTransferred = transcriptService.getCreditTransferred(matrixId);			
			
			//set student info for total credits and cgpa
			student.setTotalCredits(transcriptService.getTotalCredits(matrixId));
			student.setCgpa(String.valueOf(transcriptService.getCGPA(matrixId)));
			List<Student> studentList = new LinkedList<Student>();
			studentList.add(studentService.getStudentById(matrixId));		
			
			//set model			
			if (student.getCourseType().toUpperCase().contains("COURSEWORK")){
				studentInfoGridCourseWork.setModel(new ListModelList<Student>(studentList));
				studentInfoGridCourseWork.setVisible(true);
			}
			else{
				studentInfoGridResearch.setModel(new ListModelList<Student>(studentList));	
				studentInfoGridResearch.setVisible(true);
			}
								
			creditExemptedListBox.setModel(new ResultCreditExemptedGroupModel(resultCreditExempted, new TranscriptComparator(), true));
			creditTransferredListBox.setModel(new ResultCreditTransferredGroupModel(resultCreditTransferred, new TranscriptComparator(), true));
			semResultListBox.setModel(new ResultSemGroupModel(resultSem, new TranscriptComparator(), true));		
			
			//set component visibility
			searchResultBlock.setVisible(true);				
			noResultBlock.setVisible(false);					
		}
		else{
			noResultBlock.setVisible(true);
			searchResultBlock.setVisible(false);
			studentInfoGridCourseWork.setVisible(false);
			studentInfoGridResearch.setVisible(false);
		}			
	}	
	
	@Listen("onClick = #resetButton")
	public void resetForm(){		
		matrixBox.setValue("");		
		noResultBlock.setVisible(false);
		searchResultBlock.setVisible(false);
		studentInfoGridCourseWork.setVisible(false);
		studentInfoGridResearch.setVisible(false);
	}	
}
