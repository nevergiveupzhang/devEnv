package cn.cnnic.whoistool.service;

public interface IWhoisAuditService {
	public String insert(String ipAddr, String[] domains);
}
