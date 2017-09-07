package my.ais.business.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import my.ais.business.dao.StudentDao;
import my.ais.model.ChartPOJO;
import my.ais.model.ProgramCount;
import my.ais.util.UtilDate;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Image;

@Service("chartService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ChartServiceImpl implements ChartService {
	
	@Autowired
	StudentDao studentDao;
		
	private final String NO_DATA = "No data found. Chart cannot be generated.";
	
	/*UTIL start*/
	public DefaultCategoryDataset createDatasetBarChart(List<Object[]> objArray, String rowKey) {
		DefaultCategoryDataset dataset =  new DefaultCategoryDataset();		
		for (Object[] o : objArray)
		{
			dataset.addValue((BigInteger)o[1], rowKey, (String)o[0]);			
		}
		return dataset;
	}
	
	public DefaultCategoryDataset createDatasetBarChart2Rows(List<Object[]> objArray1, String rowKey1, List<Object[]> objArray2, String rowKey2) {
		
		DefaultCategoryDataset dataset =  new DefaultCategoryDataset();
		//e.g:
		//o[1]: count
		//o[0]: year
		//rowKey: Local/International
		for (Object[] o : objArray1)
		{
			dataset.addValue((BigInteger)o[1], rowKey1, (String)o[0]);			
		}
		
		for (Object[] o : objArray2)
		{
			dataset.addValue((BigInteger)o[1], rowKey2, (String)o[0]);			
		}		
	
		return dataset;
	}
	
	public DefaultCategoryDataset createDatasetBarChart2Rows(List<Object[]> objArray) {
		
		DefaultCategoryDataset dataset =  new DefaultCategoryDataset();
		//e.g:
		//o[2]: count
		//o[0]: year
		//o[1]: level (master/phd)
		for (Object[] o : objArray)
		{
			dataset.addValue((BigInteger)o[2], (String)o[1], (String)o[0]);			
		}	
		return dataset;
	}
	
	
	public DefaultPieDataset createDatasetPieChart(List<Object[]> objArray) {
		DefaultPieDataset dataset =  new DefaultPieDataset();		
		BigInteger bi;
		double d;
		for (Object[] o : objArray)
		{			
			bi = (BigInteger) o[1];
			d = Double.valueOf(bi.toString());
			dataset.setValue((String)o[0], new Double(d));		
		}
		return dataset;
	}
	
	public DefaultCategoryDataset createDatasetMultiplePieChart(List<Object[]> objArray){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Object[] o : objArray)
		{
			dataset.addValue((BigInteger)o[2], (String)o[0], (String)o[1]);			
		}
		return dataset;
	}
	
	public void setPiePlot(JFreeChart chart, boolean singlePie) {		
		PiePlot plot;
		StandardPieSectionLabelGenerator labelGenerator	= new StandardPieSectionLabelGenerator("{0} = {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%"));
		if(singlePie){
			plot = (PiePlot) chart.getPlot();
			plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		}
		else{
			MultiplePiePlot multiplePlot = (MultiplePiePlot) chart.getPlot();
			JFreeChart subchart = multiplePlot.getPieChart();
			plot = (PiePlot) subchart.getPlot();
			plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		}		
		plot.setLabelGenerator(labelGenerator);
		plot.setLegendLabelGenerator(labelGenerator);		
		plot.setNoDataMessage("No data available");
		plot.setLabelGap(0.02);		
	}

	public void setBarChartRenderer(JFreeChart chart) {
		StackedBarRenderer renderer = new StackedBarRenderer(false);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		chart.getCategoryPlot().setRenderer(renderer);		
	}

	public void setChartDisplay(JFreeChart chart, Image chartImage, int width, int height) throws IOException {
		BufferedImage bufferedImage = chart.createBufferedImage(width, height, BufferedImage.TRANSLUCENT , null);
		byte[] chartBytes = EncoderUtil.encode(bufferedImage, ImageFormat.PNG, true);		
		AImage image = new AImage("Chart", chartBytes);
		chartImage.setContent(image);		
	}
	
	public String getErrNoData() {
		return NO_DATA;
	}
	
	public void setPng(String chartTitle, JFreeChart chart) throws IOException {
		File file1 = new File("chart1.png"); 
	    ChartUtilities.saveChartAsJPEG(file1, chart, 1000, 500);		
	    BufferedImage bufferedImage = chart.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);	    
	    byte[] chartBytes = EncoderUtil.encode(bufferedImage, ImageFormat.PNG, true);
		AMedia amedia = new AMedia("chart_" + chartTitle, "png", "image/png", chartBytes);
		Filedownload.save(amedia);
	}	
	
	public void setPdf(String chartTitle, JFreeChart chart) {
		BufferedImage bufferedImage1 = chart.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
		List<ChartPOJO> chartList = new LinkedList<ChartPOJO>();
		chartList.add(new ChartPOJO(bufferedImage1));			
		setJasperPdf(chartList, chartTitle);
	}
	
	public void setPng(String chartTitle, JFreeChart chart1, JFreeChart chart2) throws IOException {
        File file1 = new File("chart1.png"); 
	    ChartUtilities.saveChartAsJPEG(file1, chart1, 1000, 500);		
	    BufferedImage bufferedImage1 = chart1.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    File file2 = new File("chart2.png"); 
	    ChartUtilities.saveChartAsJPEG(file2, chart2, 1000, 500);		
	    BufferedImage bufferedImage2 = chart2.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    BufferedImage joinedImg = joinBufferedImage(bufferedImage1,bufferedImage2);	    	        
	    byte[] chartBytes = EncoderUtil.encode(joinedImg, ImageFormat.PNG, true);
		AMedia amedia = new AMedia("chart_" + chartTitle, "png", "image/png", chartBytes);
		Filedownload.save(amedia);
	}	
	
	public void setPdf(String chartTitle, JFreeChart chart1, JFreeChart chart2) {		
		List<ChartPOJO> chartList = new LinkedList<ChartPOJO>();
		BufferedImage bufferedImage1 = chart1.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);		
		BufferedImage bufferedImage2 = chart2.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
		chartList.add(new ChartPOJO(bufferedImage1));	
		chartList.add(new ChartPOJO(bufferedImage2));
		setJasperPdf(chartList, chartTitle);
	}
	
	public void setPng(String chartTitle, JFreeChart chart1, JFreeChart chart2, JFreeChart chart3) throws IOException {	
        File file1 = new File("chart1.png"); 
	    ChartUtilities.saveChartAsJPEG(file1, chart1, 1000, 500);		
	    BufferedImage bufferedImage1 = chart1.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    File file2 = new File("chart2.png"); 
	    ChartUtilities.saveChartAsJPEG(file2, chart2, 1000, 500);		
	    BufferedImage bufferedImage2 = chart2.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    File file3 = new File("chart3.png"); 
	    ChartUtilities.saveChartAsJPEG(file3, chart3, 1000, 500);		
	    BufferedImage bufferedImage3 = chart3.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    BufferedImage joinedImg = joinBufferedImage(bufferedImage1,bufferedImage2,bufferedImage3);	   	    
	    byte[] chartBytes = EncoderUtil.encode(joinedImg, ImageFormat.PNG, true);
		AMedia amedia = new AMedia("chart_" + chartTitle, "png", "image/png", chartBytes);
		Filedownload.save(amedia);
	}	
	
	public void setPdf(String chartTitle, JFreeChart chart1, JFreeChart chart2, JFreeChart chart3) {
		List<ChartPOJO> chartList = new LinkedList<ChartPOJO>();
		BufferedImage bufferedImage1 = chart1.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
		BufferedImage bufferedImage2 = chart2.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
		BufferedImage bufferedImage3 = chart3.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
		chartList.add(new ChartPOJO(bufferedImage1));	
		chartList.add(new ChartPOJO(bufferedImage2));
		chartList.add(new ChartPOJO(bufferedImage3));
		setJasperPdf(chartList, chartTitle);
	}
	
	public void setPngWithProgramDesc(String chartTitle, JFreeChart chart) throws IOException{
		File file1 = new File("chart1.png"); 
	    ChartUtilities.saveChartAsJPEG(file1, chart, 1000, 500);		
	    BufferedImage bufferedImage = chart.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);
	    
	    File programDescImg = new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/img") +"/program_desc.png");	    
	    BufferedImage bufferedProgramDesc = ImageIO.read(programDescImg);
	    
	    BufferedImage joinedImg = joinBufferedImage(bufferedImage,bufferedProgramDesc);	    	        
	    byte[] chartBytes = EncoderUtil.encode(joinedImg, ImageFormat.PNG, true);
		AMedia amedia = new AMedia("chart_" + chartTitle, "png", "image/png", chartBytes);
		Filedownload.save(amedia);
	}
	
	public void setPdfWithProgramDesc(String chartTitle, JFreeChart chart) throws IOException {		
		List<ChartPOJO> chartList = new LinkedList<ChartPOJO>();
		BufferedImage bufferedImage = chart.createBufferedImage(1000, 500, BufferedImage.TRANSLUCENT , null);		
		
		File programDescImg = new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/img") +"/program_desc.png");	    
	    BufferedImage bufferedProgramDesc = ImageIO.read(programDescImg);
	    
		chartList.add(new ChartPOJO(bufferedImage));	
		chartList.add(new ChartPOJO(bufferedProgramDesc));
		setJasperPdf(chartList, chartTitle);
	}
	
	private BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {
        //do some calculate first
        int offset  = 5;
        int wid = Math.max(img1.getWidth(),img2.getWidth())+offset;
        int height = img1.getHeight()+img2.getHeight()+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight()+offset);
        g2.dispose();
        return newImage;
    }
	
	private BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2, BufferedImage img3) {
        //do some calculate first
        int offset  = 5;
        int wid = Math.max(img1.getWidth(),img2.getWidth());
        wid = Math.max(wid, img3.getWidth())+offset;
        int height = img1.getHeight()+img2.getHeight()+img3.getHeight()+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight()+offset);
        g2.drawImage(img2, null, 0, img1.getHeight()+offset+img2.getHeight()+offset);
        g2.dispose();
        return newImage;
    }
	
	private void setJasperPdf(List<ChartPOJO> chartList, String chartTitle) {
		System.out.println("export to pdf: "+ Executions.getCurrent().getDesktop().getWebApp().getResourcePaths("/")+" path jasper");	
		JRDataSource ds = new JRBeanCollectionDataSource(chartList);
		ByteArrayOutputStream output = new ByteArrayOutputStream();		
		
		Map<String, Object> params = new LinkedHashMap<String, Object>();	   		
		params.put("currentDate", UtilDate.getCurrentDate_ddMMyyyy());
		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reports") +
					"/stadChart_pdf.jasper", params, ds);
			JasperExportManager.exportReportToPdfStream(jasperPrint,output);
		 
			AMedia amedia = new AMedia("chart_" + chartTitle, "pdf", "application/pdf", output.toByteArray());
			Filedownload.save(amedia);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*UTIL end*/
	
	/*GENERAL CUSTOMIZATION start*/
	public List<Object[]> getGeneralUntilLatestSemester() {		
		return studentDao.chartAllSemester();
	}

	public List<Object[]> getGeneralUntilLatestYear() {		
		return studentDao.chartAllYear();
	}

	public String getLatestYear() {		
		int count = 1;
		for (Object[] o : studentDao.chartAllYear())
		{			
			if (count == studentDao.chartAllYear().size()){
				return (String)o[0];
			}			
			count++;				
		}
		return null;
	}
	
	public String getEarliestYear() {
		for (Object[] o : studentDao.chartAllYear())
		{			
			return (String)o[0];				
		}
		return null;
	}

	public String getLatestSemester() {
		int count = 1;
		for (Object[] o : studentDao.chartAllSemester())
		{
			if (count == studentDao.chartAllSemester().size()){
				return (String)o[0];
			}			
			count++;				
		}
		return null;
	}

	public List<Object[]> getGeneralYearRange(String min, String max) {
		return studentDao.chartCountYearRange(min, max);
	}

	public List<Object[]> getGeneralSemesterRange(String min, String max) {	
		return studentDao.chartCountSemesterRange(min, max);
	}
	/*GENERAL CUSTOMIZATION end*/

	/*COUNTRY CUSTOMIZATION start*/
	public List<Object[]> getAllCountriesUntilLatestYear() {
		return studentDao.chartCountAllCountryAllYear();
	}

	public List<Object[]> getLocalInterUntilLatestYear() {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountLocalAllYear();
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountInterAllYear();
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getLocalAllYear() {
		return studentDao.chartCountLocalAllYear();
	}

	public List<Object[]> getInterAllYear() {
		return studentDao.chartCountInterAllYear();
	}

	public List<Object[]> getCountryAllYear(String country) {
		return studentDao.chartCountSpecificCountryAllYear(country);
	}

	public List<Object[]> getAllCountriesSpecificYear(String year) {
		return studentDao.chartCountAllCountrySpecificYear(year);
	}

	public List<Object[]> getLocalInterSpecificYear(String year) {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountLocalSpecificYear(year);
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountInterSpecificYear(year);
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getAllCountriesSpecificSemester(String semester) {
		return studentDao.chartCountAllCountrySpecificSemester(semester);
	}

	public List<Object[]> getLocalInterSpecificSemester(String semester) {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountLocalSpecificSemester(semester);
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountInterSpecificSemester(semester);
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getLocalYearRange(String min, String max) {
		return studentDao.chartCountLocalYearRange(min, max);
	}
	
	public List<Object[]> getInterYearRange(String min, String max) {
		return studentDao.chartCountInterYearRange(min, max);
	}

	public List<Object[]> getCountryYearRange(String country, String min, String max) {
		return studentDao.chartCountSpecificCountryYearRange(country, min, max);
	}

	public List<Object[]> getLocalSemesterRange(String min, String max) {
		return studentDao.chartCountLocalSemesterRange(min, max);
	}
	
	public List<Object[]> getInterSemesterRange(String min, String max) {
		return studentDao.chartCountInterSemesterRange(min, max);
	}

	public List<Object[]> getCountrySemesterRange(String country, String min, String max) {
		return studentDao.chartCountSpecificCountrySemesterRange(country, min, max);
	}
	/*COUNTRY CUSTOMIZATION end*/

	/*LEVEL CUSTOMIZATION start*/
	public List<Object[]> getLevelMasterAllCountriesAllYear() {
		return studentDao.chartCountLevelMasterAllCountryAllYear();
	}
	public List<Object[]> getLevelPhdAllCountriesAllYear() {
		return studentDao.chartCountLevelPhdAllCountryAllYear();
	}

	public List<Object[]> getLevelAllYear() {
		return studentDao.chartCountLevelAllYear();
	}

	public List<Object[]> getLevelLocalAllYear() {
		return studentDao.chartCountLevelLocalAllYear();
	}

	public List<Object[]> getLevelInterAllYear() {
		return studentDao.chartCountLevelInterAllYear();
	}

	public List<Object[]> getLevelCountryAllYear(String country) {
		return studentDao.chartCountLevelSpecificCountryAllYear(country);
	}

	public List<Object[]> getLevelSpecificYear(String year) {
		return studentDao.chartCountLevelSpecificYear(year);
	}

	public List<Object[]> getLevelMasterAllCountriesSpecificYear(String year) {
		return studentDao.chartCountLevelMasterAllCountrySpecificYear(year);
	}

	public List<Object[]> getLevelPhdAllCountriesSpecificYear(String year) {
		return studentDao.chartCountLevelPhdAllCountrySpecificYear(year);
	}

	public List<Object[]> getLevelLocalSpecificYear(String year) {
		return studentDao.chartCountLevelLocalSpecificYear(year);
	}

	public List<Object[]> getLevelInterSpecificYear(String year) {
		return studentDao.chartCountLevelInterSpecificYear(year);
	}

	public List<Object[]> getLevelSpecificSemester(String sem) {
		return studentDao.chartCountLevelSpecificSemester(sem);
	}

	public List<Object[]> getLevelMasterAllCountriesSpecificSemester(String sem) {
		return studentDao.chartCountLevelMasterAllCountrySpecificSemester(sem);
	}

	public List<Object[]> getLevelPhdAllCountriesSpecificSemester(String sem) {
		return studentDao.chartCountLevelPhdAllCountrySpecificSemester(sem);
	}

	public List<Object[]> getLevelLocalSpecificSemester(String sem) {
		return studentDao.chartCountLevelLocalSpecificSemester(sem);
	}

	public List<Object[]> getLevelInterSpecificSemester(String sem) {
		return studentDao.chartCountLevelInterSpecificSemester(sem);
	}

	public List<Object[]> getLevelAllCountriesYearRange(String min, String max) {
		return studentDao.chartCountLevelAllCountriesYearRange(min, max);
	}

	public List<Object[]> getLevelLocalYearRange(String min, String max) {
		return studentDao.chartCountLevelLocalYearRange(min, max);
	}

	public List<Object[]> getLevelInterYearRange(String min, String max) {
		return studentDao.chartCountLevelInterYearRange(min, max);
	}

	public List<Object[]> getLevelCountyYearRange(String country, String min, String max) {
		return studentDao.chartCountLevelSpecificCountryYearRange(country, min, max);
	}

	public List<Object[]> getLevelAllCountriesSemesterRange(String min,	String max) {
		return studentDao.chartCountLevelAllCountriesSemesterRange(min, max);
	}

	public List<Object[]> getLevelLocalSemesterRange(String min, String max) {
		return studentDao.chartCountLevelLocalSemesterRange(min, max);
	}

	public List<Object[]> getLevelInterSemesterRange(String min, String max) {
		return studentDao.chartCountLevelInterSemesterRange(min, max);
	}

	public List<Object[]> getLevelCountrySemesterRange(String country, String min, String max) {
		return studentDao.chartCountLevelSpecificCountrySemesterRange(country, min, max);
	}

	/*PROGRAM CUSTOMIZATION start*/
	private List<Object[]> removeProgramRedundancyAndMakeDescending(List<Object[]> originChartResult) {	
		List<Object[]> list = originChartResult;
		
		//avoid ConcurrentModificationException, create new List<Object[]> for iteration
		List<Object[]> cc = new ArrayList<Object[]>();
		for (Object[] temp: originChartResult){
			Object[] objTemp = new Object[2];
			objTemp[0] = temp[0];
			objTemp[1] = temp[1];
			cc.add(objTemp);
		}
		
		Object[] objArrayMANP = new Object[2];
		Object[] objArrayMANA = new Object[2];
		Object[] objArrayMANN = new Object[2];
		int indexCurrent=0;
		int countMANP=0;
		int countMANA=0;
		int countMANN=0;
						
		for (Object[] o : cc)
		{			
			if(o[0].equals("MANP") || o[0].equals("MNP")){
				countMANP += ((BigInteger)o[1]).intValue();	
				list.remove(indexCurrent);
				indexCurrent -= 1; //adjust index after remove an object
			}
			if(o[0].equals("MANA") || o[0].equals("MNA")){
				countMANA += ((BigInteger)o[1]).intValue();	
				list.remove(indexCurrent);
				indexCurrent -= 1;
			}
			if(o[0].equals("MANN") || o[0].equals("MNS")){
				countMANN += ((BigInteger)o[1]).intValue();	
				list.remove(indexCurrent);
				indexCurrent -= 1;
			}	
			indexCurrent += 1;
		}
		
		//add to the list if the count is more than zero
		if(countMANP>0){
			objArrayMANP[0] = "MANP";
			objArrayMANP[1] = BigInteger.valueOf(countMANP);		
			list.add(objArrayMANP);
		}		
		
		if(countMANA>0){
			objArrayMANA[0] = "MANA";
			objArrayMANA[1] = BigInteger.valueOf(countMANA);
			list.add(objArrayMANA);
		}
		
		if(countMANN>0){
			objArrayMANN[0] = "MANN";
			objArrayMANN[1] = BigInteger.valueOf(countMANN);	
			list.add(objArrayMANN);	
		}
		
		//transform to bean for comparison of count --> make it descending
		ProgramCount[] programCounts = new ProgramCount[list.size()];
		int i = 0;
		for (Object[] o : list){
			programCounts[i]=new ProgramCount((String)o[0], (BigInteger)o[1]);
			i++;
		}		
		Arrays.sort(programCounts);
		
		//transform back to List<Object[]>
		List<Object[]> newList = new ArrayList<Object[]>();
		for (ProgramCount temp: programCounts){
			Object[] objTemp = new Object[2];
			objTemp[0] = temp.getProgramId();
			objTemp[1] = temp.getCount();
			newList.add(objTemp);
		}
		return newList;
	}	
	
	public List<Object[]> getAllProgramsAllCountriesAllYear() {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramAllCountriesAllYear());
	}
	
	public List<Object[]> getAllProgramsAllCountriesSpecificYear(String year) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramAllCountriesSpecificYear(year));
	}

	public List<Object[]> getAllProgramsAllCountriesSpecificSemester(String sem) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramAllCountriesSpecificSemester(sem));
	}

	public List<Object[]> getAllProgramsAllCountriesYearRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramAllCountriesYearRange(min, max));
	}

	public List<Object[]> getAllProgramsAllCountriesSemesterRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramAllCountriesSemesterRange(min, max));
	}

	public List<Object[]> getAllProgramsLocalAllYear() {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramLocalAllYear());
	}

	public List<Object[]> getAllProgramsInterAllYear() {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramInterAllYear());
	}

	public List<Object[]> getAllProgramsLocalSpecificYear(String year) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramLocalSpecificYear(year));
	}

	public List<Object[]> getAllProgramsInterSpecificYear(String year) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramInterSpecificYear(year));
	}

	public List<Object[]> getAllProgramsLocalSpecificSemester(String sem) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramLocalSpecificSemester(sem));
	}

	public List<Object[]> getAllProgramsInterSpecificSemester(String sem) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramInterSpecificSemester(sem));
	}

	public List<Object[]> getAllProgramsLocalYearRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramLocalYearRange(min, max));
	}

	public List<Object[]> getAllProgramsInterYearRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramInterYearRange(min, max));
	}

	public List<Object[]> getAllProgramsLocalSemesterRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramLocalSemesterRange(min, max));
	}

	public List<Object[]> getAllProgramsInterSemesterRange(String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramInterSemesterRange(min, max));
	}

	public List<Object[]> getAllProgramsSpecificCountryAllYear(String country) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramSpecificCountryAllYear(country));
	}

	public List<Object[]> getAllProgramsSpecificCountrySpecificYear(String country, String year) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramSpecificCountrySpecificYear(country, year));
	}

	public List<Object[]> getAllProgramsSpecificCountrySpecificSemester(String country, String sem) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramSpecificCountrySpecificSemester(country, sem));
	}

	public List<Object[]> getAllProgramsSpecificCountryYearRange(String country, String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramSpecificCountryYearRange(country, min, max));
	}

	public List<Object[]> getAllProgramsSpecificCountrySemesterRange(String country, String min, String max) {
		return removeProgramRedundancyAndMakeDescending(
				studentDao.chartCountAllProgramSpecificCountrySemesterRange(country, min, max));
	}

	public List<Object[]> getSpecificProgramAllCountriesAllYear(String program) {
		return studentDao.chartCountSpecificProgramAllCountryAllYear(program);
	}

	public List<Object[]> getSpecificProgramAllCountriesSpecificYear(String program, String year) {
		return studentDao.chartCountSpecificProgramAllCountrySpecificYear(program, year);
	}

	public List<Object[]> getSpecificProgramAllCountriesSpecificSemester(String program, String sem) {
		return studentDao.chartCountSpecificProgramAllCountrySpecificSemester(program, sem);
	}

	public List<Object[]> getSpecificProgramAllCountriesYearRange(String program, String min, String max) {
		return studentDao.chartCountSpecificProgramAllCountryYearRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramAllCountriesSemesterRange(String program, String min, String max) {
		return studentDao.chartCountSpecificProgramAllCountrySemesterRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramLocalInterAllYear(String program) {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramLocalAllYear(program);
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramInterAllYear(program);
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getSpecificProgramLocalInterSpecificYear(String program, String year) {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramLocalSpecificYear(program, year);
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramInterSpecificYear(program, year);
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getSpecificProgramLocalInterSpecificSemester(String program, String sem) {
		List <Object[]> result = new LinkedList<Object[]>();
		Object[] objArray1 = new Object[2];		
		Object[] objArray2 = new Object[2];	
		
		objArray1[0] = "LOCAL";
		objArray1[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramLocalSpecificSemester(program, sem);
		
		objArray2[0] = "INTERNATIONAL";
		objArray2[1] = (BigInteger)studentDao.chartSingleCountSpecificProgramInterSpecificSemester(program, sem);
		
		result.add(objArray1);
		result.add(objArray2);
		
		return result;
	}

	public List<Object[]> getSpecificProgramLocalYearRange(String program,	String min, String max) {
		return studentDao.chartCountSpecificProgramLocalYearRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramInterYearRange(String program, String min, String max) {
		return studentDao.chartCountSpecificProgramInterYearRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramLocalSemesterRange(String program, String min, String max) {
		return studentDao.chartCountSpecificProgramLocalSemesterRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramInterSemesterRange(String program, String min, String max) {
		return studentDao.chartCountSpecificProgramInterSemesterRange(program, min, max);
	}

	public List<Object[]> getSpecificProgramSpecificCountryAllYear(String program, String country) {
		return studentDao.chartCountSpecificProgramSpecificCountryAllYear(program, country);
	}

	public List<Object[]> getSpecificProgramSpecificCountryYearRange(String program, String country, String min, String max) {
		return studentDao.chartCountSpecificProgramSpecificCountryYearRange(program, country, min, max);
	}

	public List<Object[]> getSpecificProgramSpecificCountrySemesterRange(String program, String country, String min, String max) {
		return studentDao.chartCountSpecificProgramSpecificCountrySemesterRange(program, country, min, max);
	}	
}
