package cn.cnnic.domainstat.utils;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class UnitMap extends HashMap<String,Integer>{
	private static final long serialVersionUID = 1570876426756196814L;

	public UnitMap() {
		this.put("g", 1000000000);
		this.put("G", 1000000000);
		this.put("m", 1000000);
		this.put("M", 1000000);
		this.put("k", 1000);
		this.put("K", 1000);
	}
	public boolean isUnit(String data) {
		StringBuilder sb=new StringBuilder();
		for(String key:this.keySet()) {
			sb.append(key+"|");
		}
		sb.deleteCharAt(sb.length()-1);
		Pattern pattern=Pattern.compile("^[-\\+]?[\\d]+["+sb.toString()+"]$");
		return pattern.matcher(data).matches();
	}
}
