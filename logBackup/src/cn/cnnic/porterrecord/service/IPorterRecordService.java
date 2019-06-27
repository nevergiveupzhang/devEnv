package cn.cnnic.porterrecord.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.cnnic.porterrecord.dto.PorterRecordDTO;
import cn.cnnic.porterrecord.po.ServicePO;
import cn.cnnic.porterrecord.vo.PorterRecordVO;

/*
 * @author zhangtao
 * @since 2017-05-22
 */
public interface IPorterRecordService {
	public String addPorterRecord(PorterRecordVO  porterRecord);
	public List<PorterRecordDTO> getPorterRecordByDate(Date startDate,Date endDate);
	public List<ServicePO> getAllServices();
	public Integer updateService(ServicePO service);
	public Integer deleteService(Integer serviceId);
	public Map<String, Map<String, Integer>> getPorterRecordByServiceId(Integer serviceId, Date startDate, Date endDate);
	public String addService(ServicePO service);
}
