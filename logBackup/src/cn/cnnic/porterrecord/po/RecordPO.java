package cn.cnnic.porterrecord.po;

import java.util.Date;

public class RecordPO {
	private Integer id;
	private Integer serviceId;
	private String taskName;
	private Date execDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	@Override
	public String toString() {
		return "RecordPO [id=" + id + ", serviceId=" + serviceId + ", taskName=" + taskName + ", execDate=" + execDate
				+ "]";
	}
	
}
