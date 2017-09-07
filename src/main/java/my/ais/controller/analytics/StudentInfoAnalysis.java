package my.ais.controller.analytics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Toolbarbutton;

public class StudentInfoAnalysis extends SelectorComposer <Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Image chartImage;
	@Wire
	private Component exportBlock;
	@Wire
	private Component chartBlock;
	@Wire
	private Component defaultBlock;	
	
	@Wire
	private Toolbarbutton countryToolButton;
	@Wire
	private Toolbarbutton levelToolButton;
	@Wire
	private Toolbarbutton programToolButton;
	@Wire
	private Toolbarbutton modeToolButton;
	
	@Wire
	private Grid countryCategoryBlock;
	@Wire
	private Grid levelCategoryBlock;
	@Wire
	private Grid programCategoryBlock;
	@Wire
	private Grid modeCategoryBlock;
	
	private ArrayList<Toolbarbutton> toolButtonArray;
		
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		toolButtonArray = new ArrayList<Toolbarbutton>();
		toolButtonArray.add(countryToolButton);
		toolButtonArray.add(levelToolButton);
		toolButtonArray.add(programToolButton);
		toolButtonArray.add(modeToolButton);

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Local", new Double(70));
		pieDataset.setValue("International", new Double(30));
		
		JFreeChart chart = ChartFactory.createPieChart3D("AIS Student Distribution TEST", pieDataset,true,true,true);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5f);
		BufferedImage bi = chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT , null);
		byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
		
		AImage image = new AImage("Pie Chart", bytes);
		chartImage.setContent(image);
	}
	
	@Listen("onClick = #countryToolButton")
	public void analyseByCountry(){
		countryCategoryBlock.setVisible(true);
		
		//reset other block
		defaultBlock.setVisible(false);
		resetOtherToolButton(countryToolButton);
	}
	
	@Listen("onClick = #levelToolButton")
	public void analyseByLevel(){
		countryCategoryBlock.setVisible(true);
		
		//reset other block
		defaultBlock.setVisible(false);
		resetOtherToolButton(countryToolButton);
	}
	
	@Listen("onClick = #programToolButton")
	public void analyseByProgram(){
		programCategoryBlock.setVisible(true);
		
		//reset other block
		defaultBlock.setVisible(false);
		resetOtherToolButton(countryToolButton);
	}
	
	@Listen("onClick = #modeToolButton")
	public void analyseByMode(){
		modeCategoryBlock.setVisible(true);
		
		//reset other block
		defaultBlock.setVisible(false);
		resetOtherToolButton(countryToolButton);
	}
	
	@Listen("onClick = #generateChartButton")
	public void generateChart(){
		exportBlock.setVisible(true);
		chartBlock.setVisible(true);
	}
	
	private void resetOtherToolButton(Toolbarbutton currentButton){
		for (Toolbarbutton tb : toolButtonArray){
			if (tb != currentButton){
				tb.setChecked(false);
			}
		}
	}
	
	
}
