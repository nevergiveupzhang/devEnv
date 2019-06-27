package cn.cnnic.porterrecord.common.exception;

public class NullParamException extends PorterRecordException {
	private final static Integer ERR_CODE=810001;
	public NullParamException(String message) {
		super(message,ERR_CODE);
	}
}
