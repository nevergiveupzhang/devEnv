package cn.cnnic.report.vo;

import java.util.List;

public class ReportDailyVO {
	private String description;
	private String []days;
	private List<ReportChannelDailyDataVO> channelDayCounts;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getDays() {
		return days;
	}
	public void setDays(String[] days) {
		this.days = days;
	}
	public List<ReportChannelDailyDataVO> getChannelDayCounts() {
		return channelDayCounts;
	}
	public void setChannelDayCounts(List<ReportChannelDailyDataVO> channelDayCounts) {
		this.channelDayCounts = channelDayCounts;
	}
}
