package cn.cnnic.domainstat.utils;

import java.util.regex.Pattern;

public abstract class StringUtil {
	public static String excludeSuffix(String original,String suffix) {
		return (original.endsWith(suffix)?original.substring(0,original.indexOf(suffix)):original);
	}
	public static String includeSuffix(String original,String suffix) {
		return (original.endsWith(suffix)?original:original+suffix);
	}
	public static boolean isNumber(String data) {
		Pattern pattern=Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(data).matches();
	}
}
