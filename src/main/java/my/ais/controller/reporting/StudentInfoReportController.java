package my.ais.controller.reporting;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import my.ais.business.service.ProgramService;
import my.ais.business.service.StatusService;
import my.ais.business.service.StudentService;
import my.ais.domain.Program;
import my.ais.domain.Student;
import my.ais.model.SearchCategory;
import my.ais.model.StudentSearchCategoryEntity;
import my.ais.report.SearchCategoryDataSource;
import my.ais.util.UtilDate;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@VariableResolver(DelegatingVariableResolver.class)
public class StudentInfoReportController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window win;
	@Wire
    private Combobox statusCombobox;
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
	private Textbox enrolBox;
	@Wire
	private Textbox gradYearBox;
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
	
	@WireVariable
	StatusService statusService;
	
	private List<Student> result;
	private HashMap<String, String> categoriesMap;
	
	public StudentInfoReportController(){		
				
	}
		
	//private ListModel<String> statusModel = new ListModelList<String>(StudentSearchCategoryEntity.getStatus());
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
	};
	
	@Listen("onClick = #searchButton")
	public void search(){	
		
		searchResultBlock.setVisible(true);		
		String[] program = programCombobox.getValue().split("-");
		
		categoriesMap = new LinkedHashMap<String, String>();
		if(!statusCombobox.getValue().isEmpty()){categoriesMap.put("statusKey", statusCombobox.getValue());}
		if(!levelCombobox.getValue().isEmpty()){categoriesMap.put("levelKey", levelCombobox.getValue());}
		if(!modeCombobox.getValue().isEmpty()){categoriesMap.put("modeKey", modeCombobox.getValue());}
		if(!courseTypeCombobox.getValue().isEmpty()){categoriesMap.put("courseTypeKey", courseTypeCombobox.getValue());}
		if(!programCombobox.getValue().isEmpty()){categoriesMap.put("programKey", program[0]);} //only the code
		if(!countryCombobox.getValue().isEmpty()){categoriesMap.put("countryKey", countryCombobox.getValue());}
		if(!matrixBox.getValue().isEmpty()){categoriesMap.put("matrixKey", matrixBox.getValue());}
		if(!studentNameBox.getValue().isEmpty()){categoriesMap.put("studentKey", studentNameBox.getValue());}
		if(!enrolBox.getValue().isEmpty()){categoriesMap.put("enrolKey", enrolBox.getValue());}
		if(!gradYearBox.getValue().isEmpty()){categoriesMap.put("gradYearKey", gradYearBox.getValue());}
		
		//set the model
		result = studentService.getStudentListByCategory(categoriesMap);	
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
		statusCombobox.setValue("");
		levelCombobox.setValue("");
		courseTypeCombobox.setValue("");
		modeCombobox.setValue("");
		programCombobox.setValue("");
		countryCombobox.setValue("");
		matrixBox.setValue("");
		studentNameBox.setValue("");
		enrolBox.setValue("");		
		gradYearBox.setValue("");
		searchResultBlock.setVisible(false);
		exportBlock.setVisible(false);
	}
	
	@Listen("onClick = #pdfButton")
	public void exportPdf(){
		System.out.println("export to pdf: "+ Executions.getCurrent().getDesktop().getWebApp().getResourcePaths("/")+" path jasper");		
		JRDataSource ds = new JRBeanCollectionDataSource(result);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		Map<String, Object> params = setReportParam();
	   		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reports") +
					"/studentInfoReport_pdf.jasper", params, ds);
			JasperExportManager.exportReportToPdfStream(jasperPrint,output);
		 
			AMedia amedia = new AMedia("studentInfoReport", "pdf", "application/pdf", output.toByteArray());
			Filedownload.save(amedia);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}	
	
	@Listen("onClick = #xlsButton")
	public void exportXls(){
		System.out.println("export to excel: " +Executions.getCurrent().getDesktop().getWebApp().getResourcePaths("/")+" path jasper");		
		JRDataSource ds = new JRBeanCollectionDataSource(result);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		Map<String, Object> params = setReportParam();
	   		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reports") +
					"/studentInfoReport_xls.jasper", params, ds);
			
		    // coding For Excel:
		    JRXlsExporter exporterXLS = new JRXlsExporter();
		    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
		    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		    exporterXLS.exportReport();		    
		    
		    AMedia amedia = new AMedia("studentInfoReport", "xls", "application/vnd.ms-excel", output.toByteArray());			
			Filedownload.save(amedia);		         
		 } catch (Exception e) {
			 e.printStackTrace();
		 } 
	}
	
	@Listen("onClick = #csvButton")
	public void exportCsv(){
		System.out.println("Save as csv");
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
	
	private Map<String, Object> setReportParam() {
		LinkedList<SearchCategory> reportFilterList = new LinkedList<SearchCategory>();
		
		for (String s : categoriesMap.keySet()){			
			if (s.equals("countryKey")){
				reportFilterList.add(new SearchCategory("Country", categoriesMap.get(s)));
			}
			else if (s.equals("programKey")){
				reportFilterList.add(new SearchCategory("Program", categoriesMap.get(s)));
			}			
			else if (s.equals("studentKey")){
				reportFilterList.add(new SearchCategory("Student Name", categoriesMap.get(s)));
			}
			else if (s.equals("statusKey")){
				reportFilterList.add(new SearchCategory("Status", categoriesMap.get(s)));
			}
			else if (s.equals("levelKey")){
				reportFilterList.add(new SearchCategory("Level", categoriesMap.get(s)));
			}
			else if (s.equals("courseTypeKey")){
				reportFilterList.add(new SearchCategory("Course Type", categoriesMap.get(s)));
			}
			else if (s.equals("modeKey")){
				reportFilterList.add(new SearchCategory("Mode", categoriesMap.get(s)));
			}
			else if (s.equals("matrixKey")){
				reportFilterList.add(new SearchCategory("Matrix No", categoriesMap.get(s)));
			}
			else if (s.equals("enrolKey")){
				reportFilterList.add(new SearchCategory("Enrolment Sem", categoriesMap.get(s)));
			}
			else if (s.equals("gradYearKey")){
				reportFilterList.add(new SearchCategory("Graduated Year", categoriesMap.get(s)));
			}	
		}		
		
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("reportTitle", "Student Data Report");
	    params.put("totalRecords", result.size());
	    params.put("currentDate", UtilDate.getCurrentDate_ddMMyyyy());
	    params.put("categoryDataSource", new SearchCategoryDataSource(reportFilterList));
		return params;
	}
}
