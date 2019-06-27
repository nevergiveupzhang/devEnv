package cn.cnnic.report.exception;

import cn.cnnic.report.exception.model.ExceptionStatus;

public class ErrPageException extends RuntimeException{
	private static final long serialVersionUID = 3495481773080060861L;
	private final int code=ExceptionStatus.ERR_PAGE.value();
	private String message;
	
	public ErrPageException(String message) {
		super(message);
		this.message=message;
	}
	
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
