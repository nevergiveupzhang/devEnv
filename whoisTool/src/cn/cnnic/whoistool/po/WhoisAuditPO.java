package cn.cnnic.whoistool.po;

import java.util.Date;

public class WhoisAuditPO {
	private Integer id;
	private String ipAddr;
	private Date queryDate;
	private String domainInfo;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public Date getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}
	public String getDomainInfo() {
		return domainInfo;
	}
	public void setDomainInfo(String domainInfo) {
		this.domainInfo = domainInfo;
	}
	@Override
	public String toString() {
		return "WhoisAuditPO [id=" + id + ", ipAddr=" + ipAddr + ", queryDate=" + queryDate + ", domainInfo="
				+ domainInfo + "]";
	}
	
}
