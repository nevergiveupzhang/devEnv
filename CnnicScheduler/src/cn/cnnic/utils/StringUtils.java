package cn.cnnic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static int getChineseNum(String str){
		  int count=0;
		  String regEx = "[\\u4e00-\\u9fa5]";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(str);
		  while(m.find())count++;
		  	return count;
		 }
	public static void main(String[] args) {
		System.out.println(getChineseNum("dd"));
		System.out.println("中国".length());
	}
}
