package cn.cnnic.plan.exception.model;


public enum ExceptionStatus {
	INVALID_PARAM(40001),
	
	UNSUPPORTED_ENCODING(40002),
	
	UNPARSED_DATE(40003), 
	
	ERR_PAGE(40004);
	
	
	private final int value;
	
	ExceptionStatus(int value) {
		this.value=value;
	}
	
	public int value() {
		return this.value;
	}
}
