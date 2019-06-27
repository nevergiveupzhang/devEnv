package cn.cnnic.whoistool.common.exception;

public class NullParamException extends WhoisException {
	private final static Integer ERR_CODE=810001;
	public NullParamException(String message) {
		super(message,ERR_CODE);
	}
}
