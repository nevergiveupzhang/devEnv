package cn.cnnic.whoistool.po;

import java.util.Date;

public class ContactPO {
	private String name;
	private String org;
	private String email;
	private Date createDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "ContactPO [name=" + name + ", org=" + org + ", email=" + email + ", createDate=" + createDate + "]";
	}
}
