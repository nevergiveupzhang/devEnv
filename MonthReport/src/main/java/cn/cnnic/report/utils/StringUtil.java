package cn.cnnic.report.utils;

public abstract class StringUtil {
	public static String excludeSuffix(String original,String suffix) {
		return (original.endsWith(suffix)?original.substring(0,original.indexOf(suffix)):original);
	}
	public static String includeSuffix(String original,String suffix) {
		return (original.endsWith(suffix)?original:original+suffix);
	}
}
