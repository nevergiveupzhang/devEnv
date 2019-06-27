package cn.cnnic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtil {
	public static String getTomorrowDate() throws ParseException {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(5, 1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getTodayDate() throws ParseException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static boolean isValidDate(String dateStr,String formatStr) {
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			format.setLenient(false);
			format.parse(dateStr);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
	public static String getIntervalDate(String referDate, int interval) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = "";
		try {
			result = getIntervalDate(format.parse(referDate), interval);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getIntervalDate(Date referDate, int interval) {
		Date resultDate = null;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(referDate);
		calendar.add(Calendar.DAY_OF_MONTH, interval);
		resultDate = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(resultDate);
	}
	
	public static String getFirstDayOfNextMonth(String currentDate) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
			calendar.setTime(dft.parse(currentDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }
	
	public static long getDateInterval(String startDate,String endDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long result = 0;
		try {
			result = ((formatter.parse(endDate).getTime()-formatter.parse(startDate).getTime())/(1000*60*60*24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println(getFirstDayOfNextMonth("2019-02-11"));
	}
}
