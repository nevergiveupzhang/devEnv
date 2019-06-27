package cn.cnnic.domainstat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtil {
	public static String getTomorrowDate() {
		return getIntervalDate(new Date(), 1);
	}

	public static String getTodayDate() {
		return getIntervalDate(new Date(), 0);
	}

	public static String get7DayLaterDate() {
		return getIntervalDate(new Date(), 8);
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

	public static long getDateInterval(String startDate, String endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long result = 0;
		try {
			result = ((formatter.parse(endDate).getTime() - formatter.parse(startDate).getTime())
					/ (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isValidDate(String dateStr, String formatStr) {
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

	public static void main(String[] args) {
//		System.out.println(format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		System.out.println(CalendarUtil.getIntervalDate("2019-01-04", -6));
//		String[] arr = "张衡".split("\\|");
//		System.out.println("张衡".split("|"));
		System.out.println(getDateInterval("2019-01-01", "2019-01-02"));
		System.out.println(getDiffDays("2019-01-01", "2019-01-02"));

//		System.out.println(getTomorrowDate());
//		System.out.println(getTodayDate());
//		System.out.println(get7DayLaterDate());
		
		long []ls=new long[3];
		for(long l:ls) {
			System.out.println(l);
		}
	}

	public static boolean isWeekend(String day) {
		return isSaturday(day) || isSunday(day);
	}

	public static boolean isFriday(String day) {
		return calculateDayOfWeek(day) == Calendar.FRIDAY;
	}

	public static boolean isSaturday(String day) {
		return calculateDayOfWeek(day) == Calendar.SATURDAY;
	}

	public static boolean isSunday(String day) {
		return calculateDayOfWeek(day) == Calendar.SUNDAY;
	}

	private static int calculateDayOfWeek(String day) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDiffDays(String startDate, String endDate) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = getStrToDate(startDate, "yyyy-MM-dd");
			toDate = getStrToDate(endDate, "yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long from = fromDate.getTime();
		long to = toDate.getTime();
		int day = (int) ((to - from) / (24 * 60 * 60 * 1000));
		return day;
	}

	public static Date getStrToDate(String date, String formtter) {
		DateFormat df = new SimpleDateFormat(formtter);
		Date resultDate = null;
		try {
			resultDate = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	public static String convertFormat(String date, String fromFormat, String toFormat) {
		DateFormat fromDf = new SimpleDateFormat(fromFormat);
		DateFormat toDf = new SimpleDateFormat(toFormat);
		String result = "";
		try {
			result = toDf.format(fromDf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String format(Date date, String formatter) {
		DateFormat df = new SimpleDateFormat(formatter);
		return df.format(date);
	}

	public static String getNextMonthFirstDay(String reportDate, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStrToDate(reportDate, format));
		cal.add(cal.MONTH, 1);
		String nextMonth = format(cal.getTime(), format);
		return nextMonth;
	}
	

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
}
