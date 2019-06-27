package cn.cnnic.report.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataUtil {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String toJsonStr(Object value) throws JsonProcessingException {
		return mapper.writeValueAsString(value);
	}
}
