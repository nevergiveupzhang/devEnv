package cn.cnnic.porterrecord.common.exception;

public class PorterRecordException extends Exception{
	private final Integer errCode;
	
	public PorterRecordException(Integer errCode){
		this.errCode=errCode;
	}
	public PorterRecordException(String message,Integer errCode){
		super(message);
		this.errCode=errCode;
	}
	public PorterRecordException(String message,Throwable cause,Integer errCode){
		super(message,cause);
		this.errCode=errCode;
	}
}
