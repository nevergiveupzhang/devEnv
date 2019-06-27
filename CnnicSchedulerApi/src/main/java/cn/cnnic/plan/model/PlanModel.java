package cn.cnnic.plan.model;

public class PlanModel {
	private int id;
	private int pid;
	private String name;
	private String day;
	private String events;
	private int status;
	private String createDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		this.events = events;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "PlanModel [id=" + id + ", pid=" + pid + ", name=" + name + ", day=" + day + ", events=" + events
				+ ", status=" + status + ", createDate=" + createDate + "]";
	}
	
}
