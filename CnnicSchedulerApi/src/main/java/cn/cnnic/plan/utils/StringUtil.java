package cn.cnnic.plan.utils;

import java.util.regex.Pattern;


import org.springframework.lang.NonNull;

public class StringUtil {
	public static boolean isNull(Object value) {
		return (null==value);
	}
	public static boolean isNotNull(Object value) {
		return !isNull(value);
	}
	public static boolean isEmpty(Object value) {
		return isNull(value)||"".equals(value);
	}
	public static boolean isNotEmpty(Object value) {
		return !isEmpty(value);
	}
	public static boolean isNumberic(String value) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");  
        return pattern.matcher(value).matches();  
}
}
