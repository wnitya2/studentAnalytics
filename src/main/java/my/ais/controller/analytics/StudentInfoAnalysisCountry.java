package my.ais.controller.analytics;

import java.io.IOException;

import my.ais.business.service.ChartService;
import my.ais.business.service.StudentService;
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
public class StudentInfoAnalysisCountry extends SelectorComposer <Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Image chartImage1;
	@Wire
	private Image chartImage2;
	@Wire
	private Component exportBlock;
	@Wire	
	private Component chartBlock1;
	@Wire
	private Component chartBlock2;
	@Wire
	private Radiogroup rg1;
	@Wire
	private Radiogroup rg2;
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
	
	@WireVariable
	ChartService chartService;	
	
	@WireVariable
	StudentService studentService;
	
	private JFreeChart chart1;
	private JFreeChart chart2;
	private String chartTitle;	
	
	private boolean chart1_status;
	private boolean chart2_status;
		
	private ListModel<String> countryModel = new ListModelList<String>(StudentSearchCategoryEntity.getCountry());
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}
	
	/*Listeners action start*/
	@Listen("onClick = #generateChartButton")
	public void generateChart() throws IOException{		
		/*init*/
		chart1_status = false;
		chart2_status = false;
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		
		if (allCountries.isChecked() && allYear.isChecked()){createChartAllCountryAllYear();}
		if (localInter.isChecked() && allYear.isChecked()){createChartLocalInterAllYear();}
		if (country.isChecked() && allYear.isChecked()){createChartCountryAllYear();}
		
		if (allCountries.isChecked() && specificYear.isChecked()){createChartAllCountrySpecificYear();}
		if (localInter.isChecked() && specificYear.isChecked()){createChartLocalInterSpecificYear();}
		
		if (allCountries.isChecked() && specificSemester.isChecked()){createChartAllCountrySpecificSemester();}
		if (localInter.isChecked() && specificSemester.isChecked()){createChartLocalInterSpecificSemester();}
		
		if (localInter.isChecked() && yearRange.isChecked()){createChartLocalInterYearRange();}
		if (country.isChecked() && yearRange.isChecked()){createChartCountryYearRange();}
		
		if (localInter.isChecked() && semesterRange.isChecked()){createChartLocalInterSemesterRange();}
		if (country.isChecked() && semesterRange.isChecked()){createChartCountrySemesterRange();}
		
		/*component visibility*/			
		if (chart1_status|| chart2_status){exportBlock.setVisible(true);}
		if (chart1_status){chartBlock1.setVisible(true);}
		if (chart2_status){chartBlock2.setVisible(true);}
	}
	
	@Listen("onClick = #resetButton")
	public void reset() throws IOException{
		exportBlock.setVisible(false);
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		rg1.getItemAtIndex(0).setChecked(true);
		rg2.getItemAtIndex(0).setChecked(true);	
		yearRange.setDisabled(true);
		semesterRange.setDisabled(true);
		countryCombobox.setDisabled(true);
		countryCombobox.setValue("");
		resetValDisabled();
	}	
	
	@Listen("onClick = #pngButton")
	public void exportPng() throws IOException{	
		if(chart2_status){
			chartService.setPng(chartTitle, chart1, chart2);
		}
		else{
			 chartService.setPng(chartTitle, chart1);
		}       
	}	
	
	@Listen("onClick = #pdfButton")
	public void exportPdf(){
		if(chart2_status){
			chartService.setPdf(chartTitle, chart1, chart2);
		}
		else{
			chartService.setPdf(chartTitle, chart1);
		}		 
	}
	
	@Listen("onCheck = #allCountries")
	public void allCountriesChecked(){
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);
		
		/*not applicable*/
		yearRange.setDisabled(true);
		semesterRange.setDisabled(true);
		
		/*enable back after being disabled by country*/
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);		
	}	
	
	@Listen("onCheck = #localInter")
	public void localInterChecked(){
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);
		
		/*enable back after being disabled by country*/
		specificYear.setDisabled(false);
		specificSemester.setDisabled(false);
		
		/*enable back after being disabled by allCountries*/
		yearRange.setDisabled(false);
		semesterRange.setDisabled(false);
	}	
	
	@Listen("onCheck = #country")
	public void countryChecked(){
		countryCombobox.setDisabled(false);
		
		/*not applicable*/
		specificYear.setDisabled(true);
		specificSemester.setDisabled(true);
		
		/*enable back after being disabled by allCountries*/
		yearRange.setDisabled(false);
		semesterRange.setDisabled(false);
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
	private void createChartAllCountryAllYear() throws IOException {		
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getAllCountriesUntilLatestYear());				
		if (dataset.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students of different countries from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
	}
	
	private void createChartLocalInterAllYear() throws IOException {
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getLocalInterUntilLatestYear());		
		if (dataset.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of Local and International students from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}
		
		DefaultCategoryDataset dataset2 = chartService.createDatasetBarChart2Rows(chartService.getLocalAllYear(),"LOCAL", chartService.getInterAllYear(),"INTERNATIONAL");
		if (dataset2.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local and International students from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart2 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset2,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart2);			      
			chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
			chart2_status = true;
		}			        
	}
	
	private void createChartCountryAllYear() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(chartService.getCountryAllYear(countryCombobox.getValue()), "Year");		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() + " from "
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}		
	}
	
	private void createChartAllCountrySpecificYear() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getAllCountriesSpecificYear(specificYearTextBox.getValue()));		
		if (dataset.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students of different countries in year "
							+ specificYearTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
	}
	
	private void createChartLocalInterSpecificYear() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getLocalInterSpecificYear(specificYearTextBox.getValue()));				
		if (dataset.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of Local and International students in year "
							+ specificYearTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
	}
	
	private void createChartAllCountrySpecificSemester() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getAllCountriesSpecificSemester(specificSemesterTextBox.getValue()));		
		if (dataset.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Distribution of students of different countries in semester "
							+ specificSemesterTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
	}
	
	private void createChartLocalInterSpecificSemester() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getLocalInterSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of Local and International students in semester "
							+ specificSemesterTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
	}
	
	private void createChartLocalInterYearRange() throws IOException {		
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart2Rows(
				chartService.getLocalYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()),"LOCAL", 
				chartService.getInterYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()),"INTERNATIONAL");
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local and International students from year " 
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}			        
	}
	
	private void createChartCountryYearRange() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(
				chartService.getCountryYearRange(countryCombobox.getValue(), fromYearTextBox.getValue(), toYearTextBox.getValue()), 
				"Year");		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() +" from year "
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}		
	}
	
	private void createChartLocalInterSemesterRange() throws IOException {		
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart2Rows(
				chartService.getLocalSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()),"LOCAL", 
				chartService.getInterSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()),"INTERNATIONAL");
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local and International students from semester " 
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Semester",
			        "Total Students",
			        dataset,			        
			        PlotOrientation.HORIZONTAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}			        
	}
	
	private void createChartCountrySemesterRange() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(
				chartService.getCountrySemesterRange(countryCombobox.getValue(), fromSemTextBox.getValue(), toSemTextBox.getValue()), 
				"Semester");		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() +" from semester "
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Semester",
			        "Total Students",
			        dataset,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
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
