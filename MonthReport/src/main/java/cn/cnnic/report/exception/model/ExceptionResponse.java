package cn.cnnic.report.exception.model;

import java.util.LinkedHashMap;

public class ExceptionResponse extends LinkedHashMap<String, Object> {
	 
    private static final long serialVersionUID = -364546270975223015L;
 
    public ExceptionResponse fail(int code,Object message) {
        this.put("code",code);
        this.put("success", "false");
        this.put("err", message);
        return this;
    }
}
