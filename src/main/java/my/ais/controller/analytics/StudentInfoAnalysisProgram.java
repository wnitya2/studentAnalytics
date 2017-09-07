package my.ais.controller.analytics;

import java.io.IOException;
import java.util.List;

import my.ais.business.service.ChartService;
import my.ais.business.service.ProgramService;
import my.ais.business.service.StudentService;
import my.ais.domain.Program;
import my.ais.model.StudentSearchCategoryEntity;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class StudentInfoAnalysisProgram extends SelectorComposer <Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Image chartImage1;
	@Wire
	private Image chartImage2;
	@Wire
	private Image chartImage3;
	@Wire
	private Component exportBlock;
	@Wire	
	private Component chartBlock1;
	@Wire
	private Component chartBlock2;
	@Wire
	private Component chartBlock3;
	@Wire
	private Component programDescBlock;
	@Wire
	private Radiogroup rg1;
	@Wire
	private Radiogroup rg2;
	@Wire
	private Radiogroup rg3;
	@Wire
	private Radio allCountries;
	@Wire
	private Radio localInter;
	@Wire
	private Radio country;
	@Wire
	private Radio allYear;
	@Wire
	private Radio specificYear;
	@Wire
	private Radio specificSemester;	
	@Wire
	private Radio yearRange;
	@Wire
	private Radio semesterRange;
	@Wire
	private Radio allPrograms;
	@Wire
	private Radio program;
	@Wire
	private Combobox countryCombobox;
	@Wire
	private Textbox specificYearTextBox;
	@Wire
	private Textbox specificSemesterTextBox;
	@Wire
	private Textbox fromYearTextBox;
	@Wire
	private Textbox toYearTextBox;
	@Wire
	private Textbox fromSemTextBox;
	@Wire
	private Textbox toSemTextBox;	
	@Wire
    private Combobox programCombobox;
	
	@WireVariable
	ChartService chartService;	
	
	@WireVariable
	StudentService studentService;
	
	@WireVariable
	ProgramService programService;
	
	private JFreeChart chart1;
	private JFreeChart chart2;
	private JFreeChart chart3;
	private String chartTitle;	
	
	private boolean chart1_status;
	private boolean chart2_status;
	private boolean chart3_status;
		
	private ListModel<String> countryModel = new ListModelList<String>(StudentSearchCategoryEntity.getCountry());
	private ListModel<Program> programModel;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		List<Program> programList = programService.getProgramListNoDuplicate();
		programModel = new ListModelList<Program>(programList);
		programCombobox.setModel(programModel);
		
	}
	
	/*Listeners action start*/
	@Listen("onClick = #generateChartButton")
	public void generateChart() throws IOException{		
		/*init*/
		chart1_status = false;
		chart2_status = false;
		chart3_status = false;
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		chartBlock3.setVisible(false);
		
		if (allPrograms.isChecked() && allCountries.isChecked() && allYear.isChecked()){createChartAllProgramAllCountryAllYear();}
		if (allPrograms.isChecked() && allCountries.isChecked() && specificYear.isChecked()){createChartAllProgramAllCountrySpecificYear();}
		if (allPrograms.isChecked() && allCountries.isChecked() && specificSemester.isChecked()){createChartAllProgramAllCountrySpecificSemester();}
		if (allPrograms.isChecked() && allCountries.isChecked() && yearRange.isChecked()){createChartAllProgramAllCountryYearRange();}
		if (allPrograms.isChecked() && allCountries.isChecked() && semesterRange.isChecked()){createChartAllProgramAllCountrySemesterRange();}
		
		
		if (allPrograms.isChecked() && localInter.isChecked() && allYear.isChecked()){createChartAllProgramLocalInterAllYear();}
		if (allPrograms.isChecked() && localInter.isChecked() && specificYear.isChecked()){createChartAllProgramLocalInterSpecificYear();}
		if (allPrograms.isChecked() && localInter.isChecked() && specificSemester.isChecked()){createChartAllProgramLocalInterSpecificSemester();}
		if (allPrograms.isChecked() && localInter.isChecked() && yearRange.isChecked()){createChartAllProgramLocalInterYearRange();}
		if (allPrograms.isChecked() && localInter.isChecked() && semesterRange.isChecked()){createChartAllProgramLocalInterSemesterRange();}
		
		if (allPrograms.isChecked() && country.isChecked() && allYear.isChecked()){createChartAllProgramCountryAllYear();}	
		if (allPrograms.isChecked() && country.isChecked() && specificYear.isChecked()){createChartAllProgramCountrySpecificYear();}
		if (allPrograms.isChecked() && country.isChecked() && specificSemester.isChecked()){createChartAllProgramCountrySpecificSemester();}
		if (allPrograms.isChecked() && country.isChecked() && yearRange.isChecked()){createChartAllProgramSpecificCountryYearRange();}
		if (allPrograms.isChecked() && country.isChecked() && semesterRange.isChecked()){createChartAllProgramCountrySemesterRange();}
		
		if (program.isChecked() && allCountries.isChecked() && allYear.isChecked()){createChartSpecificProgramAllCountryAllYear();}	
		if (program.isChecked() && allCountries.isChecked() && specificYear.isChecked()){createChartSpecificProgramAllCountrySpecificYear();}
		if (program.isChecked() && allCountries.isChecked() && specificYear.isChecked()){createChartSpecificProgramAllCountrySpecificYear();}
		if (program.isChecked() && allCountries.isChecked() && specificSemester.isChecked()){createChartSpecificProgramAllCountrySpecificSemester();}
		if (program.isChecked() && allCountries.isChecked() && yearRange.isChecked()){createChartSpecificProgramAllCountryYearRange();}
		if (program.isChecked() && allCountries.isChecked() && semesterRange.isChecked()){createChartSpecificProgramAllCountrySemesterRange();}
		
		if (program.isChecked() && localInter.isChecked() && allYear.isChecked()){createChartSpecificProgramLocalInterAllYear();}	
		if (program.isChecked() && localInter.isChecked() && specificYear.isChecked()){createChartSpecificProgramLocalInterSpecificYear();}
		if (program.isChecked() && localInter.isChecked() && specificSemester.isChecked()){createChartSpecificProgramLocalInterSpecificSemester();}
		if (program.isChecked() && localInter.isChecked() && yearRange.isChecked()){createChartSpecificProgramLocalInterYearRange();}
		if (program.isChecked() && localInter.isChecked() && semesterRange.isChecked()){createChartSpecificProgramLocalInterSemesterRange();}
		
		if (program.isChecked() && country.isChecked() && allYear.isChecked()){createChartSpecificProgramSpecificCountryAllYear();}
		if (program.isChecked() && country.isChecked() && yearRange.isChecked()){createChartSpecificProgramSpecificCountryYearRange();}
		if (program.isChecked() && country.isChecked() && semesterRange.isChecked()){createChartSpecificProgramSpecificCountrySemesterRange();}
		
		
		
		/*component visibility*/			
		if (chart1_status|| chart2_status || chart3_status){exportBlock.setVisible(true); programDescBlock.setVisible(true);}
		if (chart1_status){chartBlock1.setVisible(true);}
		if (chart2_status){chartBlock2.setVisible(true);}
		if (chart3_status){chartBlock3.setVisible(true);}
	}
	
	@Listen("onClick = #resetButton")
	public void reset() throws IOException{
		exportBlock.setVisible(false);
		programDescBlock.setVisible(false);
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		chartBlock3.setVisible(false);		
		rg1.getItemAtIndex(0).setChecked(true);
		rg2.getItemAtIndex(0).setChecked(true);		
		rg3.getItemAtIndex(0).setChecked(true);
		programCombobox.setDisabled(true);
		programCombobox.setValue(null);
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);
		resetValDisabled();
		
		//enable back after being disabled when specific program + specific country are checked
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);
	}	
	
	@Listen("onClick = #pngButton")
	public void exportPng() throws IOException{		
		if(chart3_status){
			chartService.setPng(chartTitle, chart1, chart2, chart3);
		}
		else if(chart2_status){
			chartService.setPng(chartTitle, chart1, chart2);
		}
		else{
			 chartService.setPngWithProgramDesc(chartTitle, chart1);
		}         
	}	
	
	@Listen("onClick = #pdfButton")
	public void exportPdf() throws IOException{
		if(chart3_status){
			chartService.setPdf(chartTitle, chart1, chart2, chart3); 
		}
		else if(chart2_status){
			chartService.setPdf(chartTitle, chart1, chart2);
		}
		else{
			chartService.setPdfWithProgramDesc(chartTitle, chart1);
		}		
	}
	
	@Listen("onCheck = #allPrograms")
	public void allProgramsChecked(){
		programCombobox.setDisabled(true);
		programCombobox.setValue(null);
		
		//enable back after being disabled when specific program + specific country are checked
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);
	}
	
	@Listen("onCheck = #program")
	public void programChecked(){
		programCombobox.setDisabled(false);
		
		if(country.isChecked()){
			specificYear.setDisabled(true);
			specificSemester.setDisabled(true);
		}
		else{
			//enable back after being disabled when specific program + specific country are checked
			specificYear.setDisabled(false);
			specificSemester.setDisabled(false);
		}
	}
	
	@Listen("onCheck = #allCountries")
	public void allCountriesChecked(){
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);	
		
		//enable back after being disabled when specific program + specific country are checked
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);
	}	
	
	@Listen("onCheck = #localInter")
	public void localInterChecked(){
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);
		
		//enable back after being disabled when specific program + specific country are checked
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);
	}	
	
	@Listen("onCheck = #country")
	public void countryChecked(){
		countryCombobox.setDisabled(false);	
		
		if(program.isChecked()){
			specificYear.setDisabled(true);
			specificSemester.setDisabled(true);
		}
		else{
			//enable back after being disabled when specific program + specific country are checked
			specificYear.setDisabled(false);
			specificSemester.setDisabled(false);
		}
	}	
	
	@Listen("onCheck = #allYear" )
	public void allYearChecked(){
		resetValDisabled();		
	}	
	
	@Listen("onCheck = #allSemester" )
	public void allSemesterChecked(){
		resetValDisabled();		
	}	
	
	@Listen("onCheck = #specificYear" )
	public void specificYearChecked(){
		resetValDisabled();	
		specificYearTextBox.setDisabled(false);
	}
	
	@Listen("onCheck = #specificSemester" )
	public void specificSemesterChecked(){
		resetValDisabled();	
		specificSemesterTextBox.setDisabled(false);
	}
	
	@Listen("onCheck = #yearRange" )
	public void yearRangeChecked(){
		resetValDisabled();	
		fromYearTextBox.setDisabled(false);
		toYearTextBox.setDisabled(false);
	}
	
	@Listen("onCheck = #semesterRange" )
	public void semesterRangeChecked(){
		resetValDisabled();	
		fromSemTextBox.setDisabled(false);
		toSemTextBox.setDisabled(false);
	}	
	
	private void resetValDisabled() {
		fromYearTextBox.setValue(null);
		toYearTextBox.setValue(null);
		fromSemTextBox.setValue(null);
		toSemTextBox.setValue(null);		
		
		fromYearTextBox.setDisabled(true);
		toYearTextBox.setDisabled(true);
		fromSemTextBox.setDisabled(true);
		toSemTextBox.setDisabled(true);				
		
		specificYearTextBox.setDisabled(true);
		specificSemesterTextBox.setDisabled(true);
		specificYearTextBox.setValue(null);
		specificSemesterTextBox.setValue(null);
		
	}
	/*Listeners action end*/
	
	/*Create chart methods start*/
	private void createChartAllProgramAllCountryAllYear() throws IOException {	
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getAllProgramsAllCountriesAllYear());				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students enrolled in different programs from "
							+chartService.getEarliestYear()+"-"+chartService.getLatestYear();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}		
	}
	
	private void createChartAllProgramAllCountrySpecificYear() throws IOException{
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsAllCountriesSpecificYear(specificYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students enrolled in different programs in year "
							+specificYearTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}		
	}
	
	private void createChartAllProgramAllCountrySpecificSemester() throws IOException{
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsAllCountriesSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students enrolled in different programs in semester "
							+specificSemesterTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}		
	}
	
	private void createChartAllProgramAllCountryYearRange()throws IOException {	
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsAllCountriesYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()));
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students enrolled in different programs from year " 
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle, 
			        dataset1,
			        true, true, false);			
			chartService.setPiePlot(chart1, true);		      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
			chart1_status = true;
		}
	}
	
	private void createChartAllProgramAllCountrySemesterRange() throws IOException{
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsAllCountriesSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()));
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students enrolled in different programs from semester " 
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle, 
			        dataset1,	
			        true, true, false);			
			chartService.setPiePlot(chart1, true);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
			chart1_status = true;
		}		
	}
	
	private void createChartAllProgramLocalInterAllYear() throws IOException {	
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getAllProgramsLocalAllYear(), "LOCAL", chartService.getAllProgramsInterAllYear(), "INTERNATIONAL");
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of local/international students enrolled in different programs from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Program",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 800, 500);
			chart1_status = true;
		}			
	}
	
	private void createChartAllProgramLocalInterSpecificYear() throws IOException{
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getAllProgramsLocalSpecificYear(specificYearTextBox.getValue()), "LOCAL", 
				chartService.getAllProgramsInterSpecificYear(specificYearTextBox.getValue()), "INTERNATIONAL");
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of local/international students enrolled in different programs in year " 
							+ specificYearTextBox.getValue();			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Program",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 800, 500);
			chart1_status = true;
		}	
	}
	
	private void createChartAllProgramLocalInterSpecificSemester() throws IOException{
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getAllProgramsLocalSpecificSemester(specificSemesterTextBox.getValue()), "LOCAL", 
				chartService.getAllProgramsInterSpecificSemester(specificSemesterTextBox.getValue()), "INTERNATIONAL");
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of local/international students enrolled in different programs in semester " 
							+ specificSemesterTextBox.getValue();			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Program",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 800, 500);
			chart1_status = true;
		}	
	}
	
	private void createChartAllProgramLocalInterYearRange() throws IOException{
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getAllProgramsLocalYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()), "LOCAL", 
				chartService.getAllProgramsInterYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()), "INTERNATIONAL");
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of local/international students enrolled in different programs in from year " 
							+ fromYearTextBox.getValue()+" to " + toYearTextBox.getValue();		
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Program",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 800, 500);
			chart1_status = true;
		}	
	}
	
	private void createChartAllProgramLocalInterSemesterRange() throws IOException{
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getAllProgramsLocalSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()), "LOCAL", 
				chartService.getAllProgramsInterSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()), "INTERNATIONAL");
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of local/international students enrolled in different programs in from semester " 
							+ fromSemTextBox.getValue()+" to " + toSemTextBox.getValue();		
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Program",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 800, 500);
			chart1_status = true;
		}	
	}
	
	private void createChartAllProgramCountryAllYear() throws IOException {
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsSpecificCountryAllYear(countryCombobox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students from "+countryCombobox.getValue()+" enrolled in different programs from "
							+chartService.getEarliestYear()+"-"+chartService.getLatestYear();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}	
	
	private void createChartAllProgramCountrySpecificYear() throws IOException {
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsSpecificCountrySpecificYear(countryCombobox.getValue(), specificYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students from "+countryCombobox.getValue()+" enrolled in different programs in year "
							+specificYearTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartAllProgramCountrySpecificSemester() throws IOException {
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsSpecificCountrySpecificSemester(countryCombobox.getValue(), specificSemesterTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students from "+countryCombobox.getValue()+" enrolled in different programs in semester "
							+specificSemesterTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartAllProgramSpecificCountryYearRange() throws IOException {
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsSpecificCountryYearRange(countryCombobox.getValue(), fromYearTextBox.getValue(), toYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students from "+countryCombobox.getValue()+" enrolled in different programs from year "
							+fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}		
	}	
	
	private void createChartAllProgramCountrySemesterRange() throws IOException {
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getAllProgramsSpecificCountrySemesterRange(countryCombobox.getValue(), fromSemTextBox.getValue(), toSemTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students from "+countryCombobox.getValue()+" enrolled in different programs from semester "
							+fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}		
	}
	
	private void createChartSpecificProgramAllCountryAllYear() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getSpecificProgramAllCountriesAllYear(program_id));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Distribution of students from different countries enrolled in " + programCombobox.getValue() +" from "
							+chartService.getEarliestYear() + "-" + chartService.getLatestYear();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramAllCountrySpecificYear() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getSpecificProgramAllCountriesSpecificYear(program_id, specificYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Distribution of students from different countries enrolled in " + programCombobox.getValue() +" in year "
							+specificYearTextBox.getValue();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramAllCountrySpecificSemester() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(
				chartService.getSpecificProgramAllCountriesSpecificSemester(program_id, specificSemesterTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Distribution of students from different countries enrolled in " + programCombobox.getValue() +" in semester "
							+specificSemesterTextBox.getValue();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramAllCountryYearRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart(
				chartService.getSpecificProgramAllCountriesYearRange(program_id, fromYearTextBox.getValue(), toYearTextBox.getValue()),
						"year");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Distribution of students from different countries enrolled in " + programCombobox.getValue() +" from year "
							+fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Year",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramAllCountrySemesterRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart(
				chartService.getSpecificProgramAllCountriesSemesterRange(program_id, fromSemTextBox.getValue(), toSemTextBox.getValue()),
						"semester");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Distribution of students from different countries enrolled in " + programCombobox.getValue() +" from semester "
							+fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Semester",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramLocalInterAllYear() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getSpecificProgramLocalInterAllYear(program_id));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of Local/ International students enrolled in " + programCombobox.getValue() +" from "
							+chartService.getEarliestYear() + "-" + chartService.getLatestYear();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,  
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramLocalInterSpecificYear() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getSpecificProgramLocalInterSpecificYear(program_id, 
				specificYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of Local/ International students enrolled in " + programCombobox.getValue() +" in year "
							+specificYearTextBox.getValue();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,  
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramLocalInterSpecificSemester() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getSpecificProgramLocalInterSpecificSemester(program_id, 
				specificSemesterTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of Local/ International students enrolled in " + programCombobox.getValue() +" in semester "
							+specificSemesterTextBox.getValue();
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,  
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramLocalInterYearRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getSpecificProgramLocalYearRange(program_id, fromYearTextBox.getValue(), toYearTextBox.getValue()), "Local",
				chartService.getSpecificProgramInterYearRange(program_id, fromYearTextBox.getValue(), toYearTextBox.getValue()), "International");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of Local/ International students enrolled in " + programCombobox.getValue() +" from year "
							+fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Year",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);	
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramLocalInterSemesterRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getSpecificProgramLocalSemesterRange(program_id, fromSemTextBox.getValue(), toSemTextBox.getValue()), "Local",
				chartService.getSpecificProgramInterSemesterRange(program_id, fromSemTextBox.getValue(), toSemTextBox.getValue()), "International");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of Local/ International students enrolled in " + programCombobox.getValue() +" from semester "
					+fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Semester",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);	
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramSpecificCountryAllYear() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart(
				chartService.getSpecificProgramSpecificCountryAllYear(program_id, countryCombobox.getValue()), "Year");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of students from "+ countryCombobox.getValue()+ " enrolled in " + programCombobox.getValue() +" from "
					+chartService.getEarliestYear() + "-" + chartService.getLatestYear();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Year",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);	
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramSpecificCountryYearRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart(
				chartService.getSpecificProgramSpecificCountryYearRange(program_id, countryCombobox.getValue(), fromYearTextBox.getValue(), toYearTextBox.getValue()),
				"Year");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of students from "+ countryCombobox.getValue()+ " enrolled in " + programCombobox.getValue() +" from year "
					+fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Year",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);	
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	private void createChartSpecificProgramSpecificCountrySemesterRange() throws IOException {
		String program_id= programCombobox.getValue().split("-")[0];
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart(
				chartService.getSpecificProgramSpecificCountrySemesterRange(program_id, countryCombobox.getValue(), fromSemTextBox.getValue(), toSemTextBox.getValue()),
				"Year");				
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			
			chartTitle = "Number of students from "+ countryCombobox.getValue()+ " enrolled in " + programCombobox.getValue() +" from semester "
					+fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();
			
			chart1 = ChartFactory.createBarChart(
			        chartTitle,  
			        "Semester",
			        "Total Students",
			        dataset1,
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			 
			chartService.setBarChartRenderer(chart1);	
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}	
	}
	
	
	
	private void errNoData() throws IOException{
		reset();
		Messagebox.show(chartService.getErrNoData());
	}	
	/*Create chart methods end*/

	public ListModel<String> getCountryModel() {
		return countryModel;
	}	
}
