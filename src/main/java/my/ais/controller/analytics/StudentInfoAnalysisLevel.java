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
public class StudentInfoAnalysisLevel extends SelectorComposer <Component> {

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
	private JFreeChart chart3;
	private String chartTitle;	
	
	private boolean chart1_status;
	private boolean chart2_status;
	private boolean chart3_status;
		
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
		chart3_status = false;
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		chartBlock3.setVisible(false);
		
		if (allCountries.isChecked() && allYear.isChecked()){createChartLevelAllCountryAllYear();}
		if (localInter.isChecked() && allYear.isChecked()){createChartLevelLocalInterAllYear();}
		if (country.isChecked() && allYear.isChecked()){createChartLevelCountryAllYear();}
		
		if (allCountries.isChecked() && specificYear.isChecked()){createChartLevelAllCountrySpecificYear();}
		if (localInter.isChecked() && specificYear.isChecked()){createChartLevelLocalInterSpecificYear();}
		
		if (allCountries.isChecked() && specificSemester.isChecked()){createChartLevelAllCountrySpecificSemester();}
		if (localInter.isChecked() && specificSemester.isChecked()){createChartLevelLocalInterSpecificSemester();}
		
		if (allCountries.isChecked() && yearRange.isChecked()){createChartLevelAllCountryYearRange();}
		if (localInter.isChecked() && yearRange.isChecked()){createChartLevelLocalInterYearRange();}
		if (country.isChecked() && yearRange.isChecked()){createChartLevelCountryYearRange();}
		
		if (allCountries.isChecked() && semesterRange.isChecked()){createChartLevelAllCountrySemesterRange();}
		if (localInter.isChecked() && semesterRange.isChecked()){createChartLevelLocalInterSemesterRange();}
		if (country.isChecked() && semesterRange.isChecked()){createChartLevelCountrySemesterRange();}
		
		/*component visibility*/			
		if (chart1_status|| chart2_status || chart3_status){exportBlock.setVisible(true);}
		if (chart1_status){chartBlock1.setVisible(true);}
		if (chart2_status){chartBlock2.setVisible(true);}
		if (chart3_status){chartBlock3.setVisible(true);}
	}
	
	@Listen("onClick = #resetButton")
	public void reset() throws IOException{
		exportBlock.setVisible(false);
		chartBlock1.setVisible(false);
		chartBlock2.setVisible(false);
		chartBlock3.setVisible(false);		
		rg1.getItemAtIndex(0).setChecked(true);
		rg2.getItemAtIndex(0).setChecked(true);			
		countryCombobox.setDisabled(true);
		countryCombobox.setValue(null);
		resetValDisabled();
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
			 chartService.setPng(chartTitle, chart1);
		}         
	}	
	
	@Listen("onClick = #pdfButton")
	public void exportPdf(){
		if(chart3_status){
			chartService.setPdf(chartTitle, chart1, chart2, chart3); 
		}
		else if(chart2_status){
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
	}	
	
	@Listen("onCheck = #country")
	public void countryChecked(){
		countryCombobox.setDisabled(false);
		
		/*not applicable*/
		specificYear.setDisabled(true);
		specificSemester.setDisabled(true);
		specificYearTextBox.setValue(null);
		specificSemesterTextBox.setValue(null);
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
	private void createChartLevelAllCountryAllYear() throws IOException {	
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getLevelAllYear());				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students based on level of study from year "
							+chartService.getEarliestYear()+"-"+chartService.getLatestYear();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}
		
		DefaultPieDataset dataset2 = chartService.createDatasetPieChart(chartService.getLevelMasterAllCountriesAllYear());				
		if (dataset2.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of MASTER students from year "
							+chartService.getEarliestYear()+"-"+chartService.getLatestYear();	
			
			chart2 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset2,
			        true, true, false);			 
			chartService.setPiePlot(chart2, true);
	        chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
	        chart2_status = true;
		}
		
		DefaultPieDataset dataset3 = chartService.createDatasetPieChart(chartService.getLevelPhdAllCountriesAllYear());				
		if (dataset3.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of PhD students from year "
							+chartService.getEarliestYear()+"-"+chartService.getLatestYear();	
			
			chart3 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset3,
			        true, true, false);			 
			chartService.setPiePlot(chart3, true);
	        chartService.setChartDisplay(chart3, chartImage3, 1000, 500);
	        chart3_status = true;
		}
		
		//override chartTitle since it has different 3 charts
		//for PNG and PDF filename
		chartTitle = "Number of students based on level of study from "	+chartService.getEarliestYear()+"-"+chartService.getLatestYear();
	}
	
	private void createChartLevelLocalInterAllYear() throws IOException {		
		
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(chartService.getLevelLocalAllYear());
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local students based on level of study from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}	
		
		DefaultCategoryDataset dataset2 = chartService.createDatasetBarChart2Rows(chartService.getLevelInterAllYear());
		if (dataset2.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of International students based on level of study from " 
							+ chartService.getEarliestYear() + "-" + chartService.getLatestYear();				
			chart2 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset2,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart2);			      
			chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
			chart2_status = true;
		}	
		
		//override chartTitle since it has different 3 charts
		//for PNG and PDF filename
		chartTitle = "Number of Local and International students based on level of study from "	+chartService.getEarliestYear()+"-"+chartService.getLatestYear();
	}
	
	private void createChartLevelCountryAllYear() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart2Rows(chartService.getLevelCountryAllYear(countryCombobox.getValue()));		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() + " based on level of study from "
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
	
	private void createChartLevelAllCountrySpecificYear() throws IOException{
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getLevelSpecificYear(specificYearTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students based on level of study in year "
							+specificYearTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}
		
		DefaultPieDataset dataset2 = chartService.createDatasetPieChart(chartService.getLevelMasterAllCountriesSpecificYear(specificYearTextBox.getValue()));				
		if (dataset2.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of MASTER students in year "
							+specificYearTextBox.getValue();
			
			chart2 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset2,
			        true, true, false);			 
			chartService.setPiePlot(chart2, true);
	        chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
	        chart2_status = true;
		}
		
		DefaultPieDataset dataset3 = chartService.createDatasetPieChart(chartService.getLevelPhdAllCountriesSpecificYear(specificYearTextBox.getValue()));				
		if (dataset3.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of PhD students in year "
							+specificYearTextBox.getValue();	
			
			chart3 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset3,
			        true, true, false);			 
			chartService.setPiePlot(chart3, true);
	        chartService.setChartDisplay(chart3, chartImage3, 1000, 500);
	        chart3_status = true;
		}
		
		//override chartTitle since it has different 3 charts
		//for PNG and PDF filename
		chartTitle = "Number of students based on level of study in year " +specificYearTextBox.getValue();
	}	
	
	private void createChartLevelLocalInterSpecificYear() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getLevelLocalSpecificYear(specificYearTextBox.getValue()));				
		if (dataset.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of Local students based on level of study in year "
							+ specificYearTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
		
		DefaultPieDataset dataset2 = chartService.createDatasetPieChart(chartService.getLevelInterSpecificYear(specificYearTextBox.getValue()));				
		if (dataset2.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of International students based on level of study in year "
							+ specificYearTextBox.getValue();				
			chart2 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset2,        
			        true, true, false);		 
			chartService.setPiePlot(chart2, true);
	        chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
	        chart2_status = true;
		}
		
		//override chartTitle since it has different 2 charts
		//for PNG and PDF filename
		chartTitle = "Number of Local and International students based on level of study in year "+ specificYearTextBox.getValue();
	}
	
	private void createChartLevelAllCountrySpecificSemester() throws IOException{
		DefaultPieDataset dataset1 = chartService.createDatasetPieChart(chartService.getLevelSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset1.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students based on level of study in semester "
							+specificSemesterTextBox.getValue();	
			
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset1,
			        true, true, false);			 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 300);
	        chart1_status = true;
		}
		
		DefaultPieDataset dataset2 = chartService.createDatasetPieChart(chartService.getLevelMasterAllCountriesSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset2.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of MASTER students in semester "
							+specificSemesterTextBox.getValue();
			
			chart2 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset2,
			        true, true, false);			 
			chartService.setPiePlot(chart2, true);
	        chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
	        chart2_status = true;
		}
		
		DefaultPieDataset dataset3 = chartService.createDatasetPieChart(chartService.getLevelPhdAllCountriesSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset3.getItemCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of PhD students in semester "
							+specificSemesterTextBox.getValue();	
			
			chart3 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset3,
			        true, true, false);			 
			chartService.setPiePlot(chart3, true);
	        chartService.setChartDisplay(chart3, chartImage3, 1000, 500);
	        chart3_status = true;
		}
		
		//override chartTitle since it has different 3 charts
		//for PNG and PDF filename
		chartTitle = "Number of students based on level of study in semester " +specificSemesterTextBox.getValue();
	}
	
	private void createChartLevelLocalInterSpecificSemester() throws IOException{
		DefaultPieDataset dataset = chartService.createDatasetPieChart(chartService.getLevelLocalSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of Local students based on level of study in semester "
							+ specificSemesterTextBox.getValue();				
			chart1 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset,        
			        true, true, false);		 
			chartService.setPiePlot(chart1, true);
	        chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
	        chart1_status = true;
		}
		
		DefaultPieDataset dataset2 = chartService.createDatasetPieChart(chartService.getLevelInterSpecificSemester(specificSemesterTextBox.getValue()));				
		if (dataset2.getValue(0).equals((Number)0.0) && dataset.getValue(1).equals((Number)0.0)){errNoData();}
		else{		
			chartTitle = "Number of International students based on level of study in semester "
							+ specificSemesterTextBox.getValue();				
			chart2 = ChartFactory.createPieChart(
			        chartTitle,             
			        dataset2,        
			        true, true, false);		 
			chartService.setPiePlot(chart2, true);
	        chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
	        chart2_status = true;
		}
		
		//override chartTitle since it has different 2 charts
		//for PNG and PDF filename
		chartTitle = "Number of Local and International students based on level of study in semester "+ specificSemesterTextBox.getValue();
	}
	
	private void createChartLevelAllCountryYearRange()throws IOException {	
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelAllCountriesYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()));
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students based on level of study from year " 
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}
	}
	
	private void createChartLevelLocalInterYearRange() throws IOException {		
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelLocalYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()));
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local students based on level of study from year " 
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		} 
		
		DefaultCategoryDataset dataset2 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelInterYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()));
		if (dataset2.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of International students based on level of study from year " 
							+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();				
			chart2 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Year",
			        "Total Students",
			        dataset2,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart2);			      
			chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
			chart2_status = true;
		} 
		
		//override chartTitle since it has different 2 charts
		//for PNG and PDF filename
		chartTitle = "Number of Local and International students based on level of study from year "+ fromYearTextBox.getValue() + " to " + toYearTextBox.getValue();
	}
	
	private void createChartLevelCountryYearRange() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart2Rows(
				chartService.getLevelCountyYearRange(countryCombobox.getValue(), fromYearTextBox.getValue(), toYearTextBox.getValue()));		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() +" based on level of study from year "
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
	
	private void createChartLevelAllCountrySemesterRange() throws IOException{
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelAllCountriesSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()));
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students based on level of study from semester " 
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Semester",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		}		
	}
	
	private void createChartLevelLocalInterSemesterRange() throws IOException {		
		DefaultCategoryDataset dataset1 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelLocalSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()));
		if (dataset1.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of Local students based on level of study from semester " 
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart1 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Semester",
			        "Total Students",
			        dataset1,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart1);			      
			chartService.setChartDisplay(chart1, chartImage1, 1000, 500);
			chart1_status = true;
		} 
		
		DefaultCategoryDataset dataset2 = chartService.createDatasetBarChart2Rows(
				chartService.getLevelInterSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()));
		if (dataset2.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of International students based on level of study from semester " 
							+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart2 = ChartFactory.createBarChart(
			        chartTitle,             
			        "Semester",
			        "Total Students",
			        dataset2,			        
			        PlotOrientation.VERTICAL,
			        true, true, false);			
			chartService.setBarChartRenderer(chart2);			      
			chartService.setChartDisplay(chart2, chartImage2, 1000, 500);
			chart2_status = true;
		} 
		
		//override chartTitle since it has different 2 charts
		//for PNG and PDF filename
		chartTitle = "Number of Local and International students based on level of study from semester "+ fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();			        
	}
	
	private void createChartLevelCountrySemesterRange() throws IOException {
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart2Rows(
				chartService.getLevelCountrySemesterRange(countryCombobox.getValue(), fromSemTextBox.getValue(), toSemTextBox.getValue()));		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "Number of students from " 
							+ countryCombobox.getValue() +" based on level of study from semester "
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
