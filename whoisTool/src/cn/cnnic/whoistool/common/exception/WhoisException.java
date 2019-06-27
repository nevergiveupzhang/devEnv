package cn.cnnic.whoistool.common.exception;

public class WhoisException extends Exception{
	private final Integer errCode;
	
	public WhoisException(Integer errCode){
		this.errCode=errCode;
	}
	public WhoisException(String message,Integer errCode){
		super(message);
		this.errCode=errCode;
	}
	public WhoisException(String message,Throwable cause,Integer errCode){
		super(message,cause);
		this.errCode=errCode;
	}
}
