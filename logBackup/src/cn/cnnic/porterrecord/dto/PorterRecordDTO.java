package cn.cnnic.porterrecord.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
 * @author zhangtao
 * @since 2017-05-22
 */
public class PorterRecordDTO {
	private Integer id;
	private String serviceName;
	private List<Date> uploadTimes=new ArrayList<Date>();
	private List<Date> cleanTimes=new ArrayList<Date>();
	private List<Date> backupTimes=new ArrayList<Date>();
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
	public List<Date> getUploadTimes() {
		return uploadTimes;
	}
	public void setUploadTimes(List<Date> uploadTimes) {
		this.uploadTimes = uploadTimes;
	}
	public List<Date> getCleanTimes() {
		return cleanTimes;
	}
	public void setCleanTimes(List<Date> cleanTimes) {
		this.cleanTimes = cleanTimes;
	}
	public List<Date> getBackupTimes() {
		return backupTimes;
	}
	public void setBackupTimes(List<Date> backupTimes) {
		this.backupTimes = backupTimes;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	@Override
	public String toString() {
		return "PorterRecordDTO [id=" + id + ", serviceName=" + serviceName + ", uploadTimes=" + uploadTimes
				+ ", cleanTimes=" + cleanTimes + ", backupTimes=" + backupTimes + ", execDate=" + execDate + "]";
	}
}
