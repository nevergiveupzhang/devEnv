package cn.cnnic.domainstat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CalendarUtil {
	public final static String DEFAULT_FORMAT = "yyyy-MM-dd";
	public static final String ORIGINAL_DATE = "1970-01-01";

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
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
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
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
		return formatter.format(resultDate);
	}

	public static long getDateInterval(String startDate, String endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
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
		DateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
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
			fromDate = parse(startDate, DEFAULT_FORMAT);
			toDate = parse(endDate, DEFAULT_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long from = fromDate.getTime();
		long to = toDate.getTime();
		int day = (int) ((to - from) / (24 * 60 * 60 * 1000));
		return day;
	}

	public static Date parse(String date, String formtter) {
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
			return null;
		}
		return result;
	}

	public static String format(Date date, String formatter) {
		DateFormat df = new SimpleDateFormat(formatter);
		return df.format(date);
	}

	public static String getNextMonthFirstDay(String reportDate, String format) {
		return getIntervalMonthFirstDay(reportDate, format, 1);
	}

	public static String getIntervalMonthFirstDay(String reportDate, String format, int interval) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(reportDate, format));
		cal.add(cal.MONTH, interval);
		return convertFormat("" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-01", DEFAULT_FORMAT,
				format);

	}

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(convertFormat(date, DEFAULT_FORMAT), DEFAULT_FORMAT));
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(convertFormat(date, DEFAULT_FORMAT), DEFAULT_FORMAT));
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static String getThisMonthLastDay(String date) {
		return getIntervalDate(getNextMonthFirstDay(date, DEFAULT_FORMAT), -1);
	}
	public static String getThisMonthFirstDay(String date) {
		int year=getYear(date);
		int month=getMonth(date);
		return year+"-"+((month<10)?("0"+month):month)+"-01";
	}
	public static String getThisMonthLastDay(Date date) {
		return getIntervalDate(getNextMonthFirstDay(format(date, DEFAULT_FORMAT), DEFAULT_FORMAT), -1);
	}

	public static String calFormat(String startDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		try {
			sdf.setLenient(false);
			sdf.parse(startDate);
		} catch (ParseException e) {
			sdf = new SimpleDateFormat("yyyy.MM.dd");
			try {
				sdf.setLenient(false);
				sdf.parse(startDate);
			} catch (ParseException e1) {
				sdf = new SimpleDateFormat("yyyyMMdd");
				try {
					sdf.setLenient(false);
					sdf.parse(startDate);
				} catch (ParseException e2) {
					sdf = new SimpleDateFormat("yyyy-MM");
					try {
						sdf.setLenient(false);
						sdf.parse(startDate);
					} catch (ParseException e3) {
						sdf = new SimpleDateFormat("yyyy.MM");
						try {
							sdf.setLenient(false);
							sdf.parse(startDate);
						} catch (ParseException e4) {
							sdf = new SimpleDateFormat("yyyyMM");
							try {
								sdf.setLenient(false);
								sdf.parse(startDate);
							} catch (ParseException e5) {
								return null;
							}
							return "yyyyMM";
						}
						return "yyyy.MM";
					}
					return "yyyy-MM";
				}
				return "yyyyMMdd";
			}
			return "yyyy.MM.dd";
		}
		return DEFAULT_FORMAT;
	}

	public static String convertFormat(String date, String targetFormat) {
		String originalFormat = calFormat(date);
		if (null != targetFormat && targetFormat.equals(originalFormat)) {
			return date;
		}
		if (null != originalFormat) {
			return convertFormat(date, originalFormat, targetFormat);
		} else {
			return null;
		}
	}

	public static boolean isSameYear(String startDate, String endDate) {
		return getYear(startDate) == getYear(endDate);
	}

	public static boolean isSameMonth(String startDate, String endDate) {
		if (!isSameYear(startDate, endDate)) {
			return false;
		}
		return getMonth(startDate) == getMonth(endDate);
	}

	public static String getThisYearLastDay(String date) {
		return getYear(date) + "-12-31";
	}

	public static String getNextYearFirstDay(String date) {
		return (getYear(date) + 1) + "-01-01";
	}

	public static boolean isSameHalfYear(String startDate, String endDate) {
		if (!isSameYear(startDate, endDate)) {
			return false;
		}
		int startMonth = getMonth(startDate);
		int endMonth = getMonth(endDate);
		return ((startMonth >= 1 & startMonth <= 6) & (endMonth >= 1 & endMonth <= 6))
				|| ((startMonth >= 7 & startMonth <= 12) & (endMonth >= 7 & endMonth <= 12));
	}

	public static String getTheLastDayOfTheFirstHalfYear(String date) {
		return getYear(date) + "-06-30";
	}

	public static String getTheFirstDayOfTheSecondHalfYear(String date) {
		return getYear(date) + "-07-01";
	}

	public static boolean isSameQurter(String startDate, String endDate) {
		if (!isSameYear(startDate, endDate)) {
			return false;
		}
		int startMonth = getMonth(startDate);
		int endMonth = getMonth(endDate);
		return ((startMonth >= 1 & startMonth <= 3) & (endMonth >= 1 & endMonth <= 3))
				|| ((startMonth >= 4 & startMonth <= 6) & (endMonth >= 4 & endMonth <= 6))
				|| ((startMonth >= 7 & startMonth <= 9) & (endMonth >= 7 & endMonth <= 9))
				|| ((startMonth >= 10 & startMonth <= 12) & (endMonth >= 10 & endMonth <= 12));
	}

	public static String getTheLastDayOfThisQuarter(String date) {
		int year = getYear(date);
		int month = getMonth(date);
		if (month >= 1 & month <= 3) {
			return year + "-03-31";
		} else if (month >= 4 & month <= 6) {
			return year + "-06-30";
		} else if (month >= 7 & month <= 9) {
			return year + "-09-30";
		} else if (month >= 10 & month <= 12) {
			return year + "-12-31";
		}
		return null;
	}

	public static String getTheFirstDayOfThisQuarter(String date) {
		int year = getYear(date);
		int month = getMonth(date);
		if (month >= 1 & month <= 3) {
			return year + "-01-01";
		} else if (month >= 4 & month <= 6) {
			return year + "-04-01";
		} else if (month >= 7 & month <= 9) {
			return year + "-07-01";
		} else if (month >= 10 & month <= 12) {
			return year + "-10-01";
		}
		return null;
	}

	public static int getDiffMonths(String startDate, String endDate) {
		return (getYear(endDate)*12+getMonth(endDate))-(getYear(startDate)*12+getMonth(startDate));
	}
	
	public static void main(String[] args) {
//		System.out.println(isValidDate("2018-01.31", "yyyyMMdd"));
		System.out.println(getDiffMonths("2018-01.31", "2019-1-12"));
	}
}
