package cn.cnnic.whoistool.po;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cnnic.whoistool.dto.DomainDTO;

public class CDNDomainPO {
	private String serial;
	private String registrantId;
	private String sponsorRegId;
	private Date regDate;
	private Date expirationDate;
	private String nameServer1;
	private String nameServer2;
	private String nameServer3;
	private String nameServer4;
	private String nameServer5;
	private String nameServer6;
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
	public String getNameServer1() {
		return nameServer1;
	}
	public void setNameServer1(String nameServer1) {
		this.nameServer1 = nameServer1;
	}
	public String getNameServer2() {
		return nameServer2;
	}
	public void setNameServer2(String nameServer2) {
		this.nameServer2 = nameServer2;
	}
	public String getNameServer3() {
		return nameServer3;
	}
	public void setNameServer3(String nameServer3) {
		this.nameServer3 = nameServer3;
	}
	public String getNameServer4() {
		return nameServer4;
	}
	public void setNameServer4(String nameServer4) {
		this.nameServer4 = nameServer4;
	}
	public String getNameServer5() {
		return nameServer5;
	}
	public void setNameServer5(String nameServer5) {
		this.nameServer5 = nameServer5;
	}
	public String getNameServer6() {
		return nameServer6;
	}
	public void setNameServer6(String nameServer6) {
		this.nameServer6 = nameServer6;
	}
	@Override
	public String toString() {
		return "CDNDomainPO [serial=" + serial + ", registrantId=" + registrantId + ", sponsorRegId=" + sponsorRegId
				+ ", regDate=" + regDate + ", expirationDate=" + expirationDate + ", nameServer1=" + nameServer1
				+ ", nameServer2=" + nameServer2 + ", nameServer3=" + nameServer3 + ", nameServer4=" + nameServer4
				+ ", nameServer5=" + nameServer5 + ", nameServer6=" + nameServer6 + "]";
	}
	public DomainDTO toDomainDTO(){
		DomainDTO domainDto=new DomainDTO();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		domainDto.setRegistrantId(this.registrantId);
		domainDto.setSponsorRegId(this.sponsorRegId);
		domainDto.setDomainRegDate(sdf.format(this.regDate));
		domainDto.setDomainExpirationDate(sdf.format(this.expirationDate));
		String ns="";
		if(null!=nameServer1){
			ns+=nameServer1+"<br/>";
		}
		if(null!=nameServer2){
			ns+=nameServer2+"<br/>";
		}
		if(null!=nameServer3){
			ns+=nameServer3+"<br/>";
		}
		if(null!=nameServer4){
			ns+=nameServer4+"<br/>";
		}
		if(null!=nameServer5){
			ns+=nameServer5+"<br/>";
		}
		if(null!=nameServer6){
			ns+=nameServer6+"<br/>";
		}
		domainDto.setServerName(ns);
		return domainDto;
	}
}
