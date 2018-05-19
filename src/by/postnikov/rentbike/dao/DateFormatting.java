package by.postnikov.rentbike.dao;

import java.util.Calendar;

public class DateFormatting {
	
	private final static int DAY_VIEW = 0;
	private final static int MONTH_VIEW = 1;
	private final static int YEAR_VIEW = 2;	

	private final static int DAY_DB = 2;
	private final static int MONTH_DB= 1;
	private final static int YEAR_DB = 0;
	
	
	public static String getCurrentDate() {
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		return year + "-" + month + "-" + day;
		
	}

	public static String modifyDateToDB(String dateView) {
		
		String[] DateArrey = dateView.split("\\.");
		String day = DateArrey[DAY_VIEW];
		String month = DateArrey[MONTH_VIEW];
		String year = DateArrey[YEAR_VIEW];
		String dateDB = year + "-" + month + "-" + day;

		return dateDB;
	}
	
	public static String modifyDateToView(String dateDB) {
		String[] DateArrey = dateDB.split("\\-");
		String day = DateArrey[DAY_DB];
		String month = DateArrey[MONTH_DB];
		String year = DateArrey[YEAR_DB];
		String dateView = day + "." + month + "." + year;

		return dateView;
	}
	
}
