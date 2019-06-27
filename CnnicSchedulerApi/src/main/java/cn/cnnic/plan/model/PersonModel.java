package cn.cnnic.plan.model;

public class PersonModel {
	private int id;
	private String name;
	private String sex;
	private String cellphone;
	private String email;
	private int planStatus;
	private int lock;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}
	public int getLock() {
		return lock;
	}
	public void setLock(int lock) {
		this.lock = lock;
	}
	@Override
	public String toString() {
		return "PersonModel [id=" + id + ", name=" + name + ", sex=" + sex + ", cellphone=" + cellphone + ", email="
				+ email + ", planStatus=" + planStatus + ", lock=" + lock + "]";
	}
}
