package cn.cnnic.porterrecord.dao;

import java.util.List;

import cn.cnnic.porterrecord.po.ServicePO;

public interface IServDao {
	public Integer getIdByNameAndIp(String serviceName, String ipAddr);

	public List<ServicePO> getAll();

	public Integer update(ServicePO service);

	public Integer delete(Integer serviceId);

	public Integer addService(ServicePO service);
}
