package cn.cnnic.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public abstract class RequestUtil {
	public static String getIpAddr(HttpServletRequest request)
	  {
	    String ip = request.getHeader("X-Forwarded-For");
	    if ((!StringUtils.isEmpty(ip)) && (!"unknown".equalsIgnoreCase(ip)))
	    {
	      int index = ip.indexOf(',');
	      if (index != -1) {
	        return ip.substring(0, index);
	      }
	      return ip;
	    }
	    return request.getRemoteAddr();
	  }
}
