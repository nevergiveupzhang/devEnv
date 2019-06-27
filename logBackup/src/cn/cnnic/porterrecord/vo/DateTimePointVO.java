package cn.cnnic.porterrecord.vo;

public class DateTimePointVO {
	private String date;
	private Integer hour;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	@Override
	public String toString() {
		return "DateTimePointVO [date=" + date + ", hour=" + hour + "]";
	}
	
}
