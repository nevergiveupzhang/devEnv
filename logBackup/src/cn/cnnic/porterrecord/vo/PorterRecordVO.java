package cn.cnnic.porterrecord.vo;


import java.util.Date;

public class PorterRecordVO {
	private Integer id;
	private String serviceName;
	private String ipAddr;
	private String taskName;
	private Date execDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
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
		return "PorterRecordVO [id=" + id + ", serviceName=" + serviceName + ", ipAddr=" + ipAddr + ", taskName="
				+ taskName + ", execDate=" + execDate + "]";
	}
	
	
}
