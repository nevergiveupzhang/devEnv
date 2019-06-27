package cn.cnnic.porterrecord.po;

public class ServicePO {
	private Integer id;
	private String serviceName;
	private String ipAddr;
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
	@Override
	public String toString() {
		return "ServicePO [id=" + id + ", serviceName=" + serviceName + ", ipAddr=" + ipAddr + "]";
	}
	
}
