package cn.cnnic.whoistool.po;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cnnic.whoistool.dto.DomainDTO;

public class CNDomainPO {
	private String serial;
	private String registrantId;
	private String sponsorRegId;
	private Date regDate;
	private Date expirationDate;
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
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
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	@Override
	public String toString() {
		return "CNDomainPO [serial=" + serial + ", registrantId=" + registrantId + ", sponsorRegId=" + sponsorRegId
				+ ", regDate=" + regDate + ", expirationDate=" + expirationDate + "]";
	}
	public DomainDTO toDomainDTO(){
		DomainDTO domainDto=new DomainDTO();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		domainDto.setRegistrantId(this.registrantId);
		domainDto.setSponsorRegId(this.sponsorRegId);
		domainDto.setDomainRegDate(sdf.format(this.regDate));
		domainDto.setDomainExpirationDate(sdf.format(this.expirationDate));
		return domainDto;
	}
}
