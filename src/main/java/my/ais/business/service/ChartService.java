package my.ais.business.service;

import java.io.IOException;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.zkoss.zul.Image;

public interface ChartService {
	
	/*UTIL*/
	DefaultCategoryDataset createDatasetBarChart(List<Object[]> objArray, String rowKey);
	DefaultCategoryDataset createDatasetBarChart2Rows(List<Object[]> objArray1, String rowKey1, List<Object[]> objArray2, String rowKey2);
	DefaultCategoryDataset createDatasetBarChart2Rows(List<Object[]> objArray);
	DefaultPieDataset createDatasetPieChart(List<Object[]> objArray);
	DefaultCategoryDataset createDatasetMultiplePieChart(List<Object[]> objArray);
	void setPiePlot(JFreeChart chart, boolean singlePie);
	void setBarChartRenderer(JFreeChart chart);
	void setChartDisplay(JFreeChart chart, Image chartImage, int width, int height) throws IOException;	
	String getErrNoData();
	void setPng(String chartTitle, JFreeChart chart) throws IOException;
	void setPdf(String chartTitle, JFreeChart chart);
	void setPng(String chartTitle, JFreeChart chart1, JFreeChart chart2) throws IOException;
	void setPdf(String chartTitle, JFreeChart chart1, JFreeChart chart2);
	void setPng(String chartTitle, JFreeChart chart1, JFreeChart chart2, JFreeChart chart3) throws IOException;
	void setPdf(String chartTitle, JFreeChart chart1, JFreeChart chart2, JFreeChart chart3);
	void setPngWithProgramDesc(String chartTitle, JFreeChart chart) throws IOException;
	void setPdfWithProgramDesc(String chartTitle, JFreeChart chart) throws IOException;
	String getLatestYear();
	String getEarliestYear();
	String getLatestSemester();
	
	
	/*GENERAL CUSTOMIZATION*/
	List<Object[]> getGeneralUntilLatestSemester();		
	List<Object[]> getGeneralUntilLatestYear();	
	List<Object[]> getGeneralYearRange(String min, String max);
	List<Object[]> getGeneralSemesterRange(String min, String max);
	
	/*COUNTRY CUSTOMIZATION*/
	List<Object[]> getAllCountriesUntilLatestYear();
	List<Object[]> getLocalInterUntilLatestYear();
	List<Object[]> getLocalAllYear();
	List<Object[]> getInterAllYear();
	List<Object[]> getCountryAllYear(String country);
	
	List<Object[]> getAllCountriesSpecificYear(String year);
	List<Object[]> getLocalInterSpecificYear(String year);
	
	List<Object[]> getAllCountriesSpecificSemester(String semester);
	List<Object[]> getLocalInterSpecificSemester(String semester);
	
	List<Object[]> getLocalYearRange(String min, String max);
	List<Object[]> getInterYearRange(String min, String max);
	List<Object[]> getCountryYearRange(String country, String min, String max);
	
	List<Object[]> getLocalSemesterRange(String min, String max);
	List<Object[]> getInterSemesterRange(String min, String max);
	List<Object[]> getCountrySemesterRange(String country, String min, String max);	
	
	/*LEVEL CUSTOMIZATION*/
	List<Object[]> getLevelAllYear();
	List<Object[]> getLevelMasterAllCountriesAllYear();
	List<Object[]> getLevelPhdAllCountriesAllYear();
	List<Object[]> getLevelLocalAllYear();
	List<Object[]> getLevelInterAllYear();
	List<Object[]> getLevelCountryAllYear(String country);
	
	List<Object[]> getLevelSpecificYear(String year);
	List<Object[]> getLevelMasterAllCountriesSpecificYear(String year);
	List<Object[]> getLevelPhdAllCountriesSpecificYear(String year);
	List<Object[]> getLevelLocalSpecificYear(String year);
	List<Object[]> getLevelInterSpecificYear(String year);
	
	List<Object[]> getLevelSpecificSemester(String sem);
	List<Object[]> getLevelMasterAllCountriesSpecificSemester(String sem);
	List<Object[]> getLevelPhdAllCountriesSpecificSemester(String sem);
	List<Object[]> getLevelLocalSpecificSemester(String sem);
	List<Object[]> getLevelInterSpecificSemester(String sem);
	
	List<Object[]> getLevelAllCountriesYearRange(String min, String max);
	List<Object[]> getLevelLocalYearRange(String min, String max);
	List<Object[]> getLevelInterYearRange(String min, String max);
	List<Object[]> getLevelCountyYearRange(String country, String min, String max);
	
	List<Object[]> getLevelAllCountriesSemesterRange(String min, String max);
	List<Object[]> getLevelLocalSemesterRange(String min, String max);
	List<Object[]> getLevelInterSemesterRange(String min, String max);
	List<Object[]> getLevelCountrySemesterRange(String country, String min, String max);
	
	
	/*PROGRAM CUSTOMIZATION*/
	List<Object[]> getAllProgramsAllCountriesAllYear();
	List<Object[]> getAllProgramsAllCountriesSpecificYear(String year);
	List<Object[]> getAllProgramsAllCountriesSpecificSemester(String sem);
	List<Object[]> getAllProgramsAllCountriesYearRange(String min, String max);
	List<Object[]> getAllProgramsAllCountriesSemesterRange(String min, String max);
	
	List<Object[]> getAllProgramsLocalAllYear();
	List<Object[]> getAllProgramsInterAllYear();
	List<Object[]> getAllProgramsLocalSpecificYear(String year);
	List<Object[]> getAllProgramsInterSpecificYear(String year);
	List<Object[]> getAllProgramsLocalSpecificSemester(String sem);
	List<Object[]> getAllProgramsInterSpecificSemester(String sem);
	List<Object[]> getAllProgramsLocalYearRange(String min, String max);
	List<Object[]> getAllProgramsInterYearRange(String min, String max);
	List<Object[]> getAllProgramsLocalSemesterRange(String min, String max);
	List<Object[]> getAllProgramsInterSemesterRange(String min, String max);
	
	List<Object[]> getAllProgramsSpecificCountryAllYear(String country);
	List<Object[]> getAllProgramsSpecificCountrySpecificYear(String country, String year);
	List<Object[]> getAllProgramsSpecificCountrySpecificSemester(String country, String sem);
	List<Object[]> getAllProgramsSpecificCountryYearRange(String country, String min, String max);
	List<Object[]> getAllProgramsSpecificCountrySemesterRange(String country, String min, String max);
	
	List<Object[]> getSpecificProgramAllCountriesAllYear(String program);
	List<Object[]> getSpecificProgramAllCountriesSpecificYear(String program, String year);
	List<Object[]> getSpecificProgramAllCountriesSpecificSemester(String program, String sem);
	List<Object[]> getSpecificProgramAllCountriesYearRange(String program, String min, String max);
	List<Object[]> getSpecificProgramAllCountriesSemesterRange(String program, String min, String max);
	
	List<Object[]> getSpecificProgramLocalInterAllYear(String program);
	List<Object[]> getSpecificProgramLocalInterSpecificYear(String program, String year);
	List<Object[]> getSpecificProgramLocalInterSpecificSemester(String program, String sem);
	List<Object[]> getSpecificProgramLocalYearRange(String program, String min, String max);
	List<Object[]> getSpecificProgramInterYearRange(String program, String min, String max);
	List<Object[]> getSpecificProgramLocalSemesterRange(String program, String min, String max);
	List<Object[]> getSpecificProgramInterSemesterRange(String program, String min, String max);
	
	List<Object[]> getSpecificProgramSpecificCountryAllYear(String program, String country);
	List<Object[]> getSpecificProgramSpecificCountryYearRange(String program, String country, String min, String max);
	List<Object[]> getSpecificProgramSpecificCountrySemesterRange(String program, String country, String min, String max);
	
}
