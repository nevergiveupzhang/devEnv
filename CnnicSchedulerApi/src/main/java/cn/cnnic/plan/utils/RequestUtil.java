package cn.cnnic.plan.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtil {
	public static String getQueryParam(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		if (request.getMethod().equalsIgnoreCase("POST")) {
			return transform(request.getParameter(name));
		}
		String s = request.getQueryString();
		if (StringUtil.isEmpty(s)) {
			return null;
		}
		try {
			s = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("encoding UTF-8 not support?");
		}
		String[] values = (String[]) parseQueryString(s).get(name);
		if ((values != null) && (values.length > 0)) {
			return transform(values[(values.length - 1)]);
		}
		return null;
	}

	public static Map<String, String[]> parseQueryString(String s) {
		String[] valArray = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = st.nextToken();
			int pos = pair.indexOf('=');
			if (pos != -1) {
				String key = pair.substring(0, pos);
				String val = pair.substring(pos + 1, pair.length());
				if (ht.containsKey(key)) {
					String[] oldVals = (String[]) ht.get(key);
					valArray = new String[oldVals.length + 1];
					for (int i = 0; i < oldVals.length; i++) {
						valArray[i] = oldVals[i];
					}
					valArray[oldVals.length] = val;
				} else {
					valArray = new String[1];
					valArray[0] = val;
				}
				ht.put(key, valArray);
			}
		}
		return ht;
	}

	public static String transform(String content) {
		if (StringUtil.isEmpty(content)) {
			return content;
		}
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("'", "&#039;");
		content = content.replaceAll("\"", "&quot;");
		content = content.replaceAll("%3C", "&lt;");
		content = content.replaceAll("%3E", "&gt;");
		return content;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
