package cn.cnnic.whoistool.dto;


public class DomainDTO {
	private String domainName;
	private String domainStatus;
	private String registrantId;
	private String sponsorRegId;
	private String domainRegDate;
	private String domainExpirationDate;
	private String org;
	private String contactName;
	private String contactEmail;
	private String contactCreateDate;
	private String serverName;
	private String registrarName;
	
	public DomainDTO(){}
	public DomainDTO(String domainName) {
		super();
		this.domainName = domainName;
		this.domainStatus = "not exist";
		this.registrantId = "not exist";
		this.sponsorRegId = "not exist";
		this.domainRegDate ="not exist";
		this.domainExpirationDate = "not exist";
		this.org = "not exist";
		this.contactName = "not exist";
		this.contactEmail = "not exist";
		this.contactCreateDate ="not exist";
		this.serverName = "not exist";
		this.registrarName = "not exist";
	}
	public DomainDTO(String domainName,String label) {
		super();
		this.domainName = domainName;
		this.domainStatus = "illegal domain";
		this.registrantId = "illegal domain";
		this.sponsorRegId = "illegal domain";
		this.domainRegDate = "illegal domain";
		this.domainExpirationDate = "illegal domain";
		this.org = "illegal domain";
		this.contactName = "illegal domain";
		this.contactEmail = "illegal domain";
		this.contactCreateDate = "illegal domain";
		this.serverName = "illegal domain";
		this.registrarName = "illegal domain";
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainStatus() {
		return domainStatus;
	}
	public void setDomainStatus(String domainStatus) {
		this.domainStatus = domainStatus;
	}
	public String getRegistrantId() {
		return registrantId;
	}
	public void setRegistrantId(String registrantId) {
		this.registrantId = registrantId;
	}
	public String getSponsorRegId() {
		return sponsorRegId;
	}
	public void setSponsorRegId(String sponsorRegId) {
		this.sponsorRegId = sponsorRegId;
	}
	public String getDomainRegDate() {
		return domainRegDate;
	}
	public void setDomainRegDate(String domainRegDate) {
		this.domainRegDate = domainRegDate;
	}
	public String getDomainExpirationDate() {
		return domainExpirationDate;
	}
	public void setDomainExpirationDate(String domainExpirationDate) {
		this.domainExpirationDate = domainExpirationDate;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactCreateDate() {
		return contactCreateDate;
	}
	public void setContactCreateDate(String contactCreateDate) {
		this.contactCreateDate = contactCreateDate;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getRegistrarName() {
		return registrarName;
	}
	public void setRegistrarName(String registrarName) {
		this.registrarName = registrarName;
	}
}
