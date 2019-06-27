package cn.cnnic.report.vo;

public class ReportChannelDailyDataVO {
	private String name;
	private long []data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long[] getData() {
		return data;
	}
	public void setData(long[] data) {
		this.data = data;
	}
}
