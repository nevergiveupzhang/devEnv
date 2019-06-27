package cn.cnnic.plan.exception;

import cn.cnnic.plan.exception.model.ExceptionStatus;

public class InvalidParamException extends RuntimeException{
	private static final long serialVersionUID = -681106310175062083L;
	private final int code=ExceptionStatus.INVALID_PARAM.value();
	private String message;
	
	public InvalidParamException(String message) {
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
