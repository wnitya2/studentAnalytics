package my.ais.controller.analytics;

import java.io.IOException;

import my.ais.business.service.ChartService;
import my.ais.business.service.StudentService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

@VariableResolver(DelegatingVariableResolver.class)
public class StudentInfoAnalysisGeneral extends SelectorComposer <Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Image chartImage;
	@Wire
	private Component exportBlock;
	@Wire
	private Component chartBlock;
	@Wire
	private Radiogroup rg1;
	@Wire
	private Radio allYear;
	@Wire
	private Radio allSemester;
	@Wire
	private Radio yearRange;
	@Wire
	private Radio semesterRange;
	
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
	
	private JFreeChart chart;
	private String chartTitle;
	
	private boolean chart_status;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}
	
	/*Listeners action start*/	
	@Listen("onClick = #generateChartButton")
	public void generateChart() throws IOException{	
		
		chart_status = false;
		chartBlock.setVisible(false);
		
		if(allYear.isChecked()){createChartAllYear();}
		if(allSemester.isChecked()){createChartAllSemester();}
		if(yearRange.isChecked()){createChartYearRange();}
		if(semesterRange.isChecked()){createChartSemesterRange();}
		
		if(chart_status){
			exportBlock.setVisible(true);
			chartBlock.setVisible(true);
		}				
	}
	
	@Listen("onClick = #resetButton")
	public void reset() throws IOException{
		exportBlock.setVisible(false);
		chartBlock.setVisible(false);
		rg1.getItemAtIndex(0).setChecked(true);
		
		fromYearTextBox.setValue(null);
		toYearTextBox.setValue(null);
		fromSemTextBox.setValue(null);
		toSemTextBox.setValue(null);
		
		fromYearTextBox.setDisabled(true);
		toYearTextBox.setDisabled(true);
		fromSemTextBox.setDisabled(true);
		toSemTextBox.setDisabled(true);		
	}
	
	@Listen("onClick = #pngButton")
	public void exportPng() throws IOException{
		chartService.setPng(chartTitle, chart);
	}	
	
	@Listen("onClick = #pdfButton")
	public void exportPdf(){	
		chartService.setPdf(chartTitle, chart); 
	}
	
	@Listen("onCheck = #allYear")
	public void allYearChecked(){
		fromYearTextBox.setValue(null);
		toYearTextBox.setValue(null);
		fromSemTextBox.setValue(null);
		toSemTextBox.setValue(null);
		
		fromYearTextBox.setDisabled(true);
		toYearTextBox.setDisabled(true);
		fromSemTextBox.setDisabled(true);
		toSemTextBox.setDisabled(true);
	}
	
	@Listen("onCheck = #allSemester")
	public void allSemesterChecked(){
		fromYearTextBox.setValue(null);
		toYearTextBox.setValue(null);
		fromSemTextBox.setValue(null);
		toSemTextBox.setValue(null);
		
		fromYearTextBox.setDisabled(true);
		toYearTextBox.setDisabled(true);
		fromSemTextBox.setDisabled(true);
		toSemTextBox.setDisabled(true);
	}
	
	@Listen("onCheck = #yearRange")
	public void yearRangeChecked(){
		fromSemTextBox.setValue(null);
		toSemTextBox.setValue(null);
		
		fromYearTextBox.setDisabled(false);
		toYearTextBox.setDisabled(false);
		fromSemTextBox.setDisabled(true);
		toSemTextBox.setDisabled(true);
	}
	
	@Listen("onCheck = #semesterRange")
	public void semesterRangeChecked(){
		fromYearTextBox.setValue(null);
		toYearTextBox.setValue(null);
		
		fromYearTextBox.setDisabled(true);
		toYearTextBox.setDisabled(true);
		fromSemTextBox.setDisabled(false);
		toSemTextBox.setDisabled(false);
	}
	/*Listeners action end*/	
	
	/*Create chart methods start*/
	private void createChartAllYear() throws IOException{
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(chartService.getGeneralUntilLatestYear(),"year");
		
		if (dataset.getRowCount() < 1){errNoData();}
		else{		
			chartTitle = "AIS students enrolment until year " + chartService.getLatestYear();			
			chart = ChartFactory.createBarChart(
			        chartTitle,           
			        "Year",            
			        "Total Students",            
			        dataset,          
			        PlotOrientation.VERTICAL,           
			        true, true, false);			
			chartService.setBarChartRenderer(chart);		      
			chartService.setChartDisplay(chart, chartImage, 1000, 500);
			chart_status = true;
		}
	}	
	
	private void createChartAllSemester() throws IOException{
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(chartService.getGeneralUntilLatestSemester(),"semester");
		
		if (dataset.getRowCount() < 1){errNoData();}
		else{	
			chartTitle = "AIS students enrolment until semester " + chartService.getLatestSemester();			
			chart = ChartFactory.createBarChart(
			        chartTitle,           
			        "Semester",            
			        "Total Students",            
			        dataset,          
			        PlotOrientation.VERTICAL,           
			        true, true, false);			
			chartService.setBarChartRenderer(chart);
			chartService.setChartDisplay(chart, chartImage, 1000, 500);
			chart_status = true;
		}
	}
	
	private void createChartYearRange() throws IOException{		
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(
				chartService.getGeneralYearRange(fromYearTextBox.getValue(), toYearTextBox.getValue()),
				"year");	
		
		if (dataset.getRowCount() < 1){errNoData();}
		else{	
			chartTitle =  "AIS students enrolment from year " + fromYearTextBox.getValue() + " to " + toYearTextBox.getValue(); 				
			chart = ChartFactory.createBarChart(
			        chartTitle,           
			        "Year",            
			        "Total Students",            
			        dataset,          
			        PlotOrientation.VERTICAL,           
			        true, true, false);				
			chartService.setBarChartRenderer(chart);			      
			chartService.setChartDisplay(chart, chartImage, 1000, 500);
			chart_status = true;
		}
	}	
	
	private void createChartSemesterRange() throws IOException{		
		DefaultCategoryDataset dataset = chartService.createDatasetBarChart(
				chartService.getGeneralSemesterRange(fromSemTextBox.getValue(), toSemTextBox.getValue()),
				"semester");	
		if (dataset.getRowCount() < 1){errNoData();}
		else{	
			chartTitle = "AIS students enrolment from semester " + fromSemTextBox.getValue() + " to " + toSemTextBox.getValue();				
			chart = ChartFactory.createBarChart(
			        chartTitle,           
			        "Semester",            
			        "Total Students",            
			        dataset,          
			        PlotOrientation.VERTICAL,           
			        true, true, false);				
			chartService.setBarChartRenderer(chart);			      
			chartService.setChartDisplay(chart, chartImage, 1000, 500);
			chart_status = true;
		}		
	}
	
	private void errNoData() throws IOException{
		reset();
		Messagebox.show(chartService.getErrNoData());
	}
	/*Create chart methods end*/
}
