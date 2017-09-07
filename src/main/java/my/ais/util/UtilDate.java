package my.ais.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {
	
	public static String getCurrentDate_ddMMyyyy()
	{		
		return (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
	}
	
	public static String getFormattedDate_yyyyMMdd(Date date)
	{		
		return (new SimpleDateFormat("yyyyMMdd").format(date));
	}
	
	public static void main (String[] args)
	{
		getCurrentDate_ddMMyyyy();
	}

}
