package cn.cnnic.plan.exception;

import cn.cnnic.plan.exception.model.ExceptionStatus;

public class ErrPageException extends RuntimeException{

	private static final long serialVersionUID = 999533799111829462L;
	
	private final int code=ExceptionStatus.ERR_PAGE.value();
	private String message;
	public ErrPageException(String message) {
		super();
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
}
