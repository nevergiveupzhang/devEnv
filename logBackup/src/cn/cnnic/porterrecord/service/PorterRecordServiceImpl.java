package cn.cnnic.porterrecord.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnnic.porterrecord.dao.IRecordDao;
import cn.cnnic.porterrecord.dao.IServDao;
import cn.cnnic.porterrecord.dto.PorterRecordDTO;
import cn.cnnic.porterrecord.po.RecordPO;
import cn.cnnic.porterrecord.po.ServicePO;
import cn.cnnic.porterrecord.vo.DateTimePointVO;
import cn.cnnic.porterrecord.vo.PorterRecordVO;
/*
 * @author zhangtao
 * @since 2017-05-22
 */
@Service(value="porterRecordService")
public class PorterRecordServiceImpl implements IPorterRecordService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PorterRecordServiceImpl.class);

	@Resource
	private IRecordDao recordDao;
	
	@Resource
	private IServDao servDao;
	
	@Override
	public String addPorterRecord(PorterRecordVO porterRecord) {
		String servName=porterRecord.getServiceName();
		String ipAddr=porterRecord.getIpAddr();
		ServicePO service=new ServicePO();
		service.setServiceName(servName);
		service.setIpAddr(ipAddr);
    	Integer serviceId=servDao.getIdByNameAndIp(servName,ipAddr);
    	if(null==serviceId){
    		servDao.addService(service);
    	}
    	serviceId=servDao.getIdByNameAndIp(servName,ipAddr);
    	RecordPO record=new RecordPO();
    	record.setTaskName(porterRecord.getTaskName());
    	record.setExecDate(new Date());
    	record.setServiceId(serviceId);
		int resultCode=recordDao.addRecord(record);
		
		if(resultCode==0){
			return "fail";
		}else{
			return "success";
		}
	}

	@Override
	public List<PorterRecordDTO> getPorterRecordByDate(Date startDate, Date endDate) {
		LOGGER.info("[PorterRecordServiceImpl]->startDate="+startDate+"&endDate="+endDate);
		List<PorterRecordDTO> resultList=new ArrayList<PorterRecordDTO>();
		List<PorterRecordVO> voList=recordDao.getPorterRecordByDate(startDate, endDate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<PorterRecordVO> voForCmd=new ArrayList<PorterRecordVO>();
		
		for(int i=0;i<voList.size();){
			PorterRecordVO vo=voList.get(i);
			voForCmd.add(vo);
			String serviceName=vo.getServiceName();
			String ipAddr=vo.getIpAddr();
			Date execDate=vo.getExecDate();
			PorterRecordDTO dto=new PorterRecordDTO();
			dto.setId(vo.getId());
			dto.setServiceName(serviceName+"("+ipAddr+")");
			dto.setExecDate(execDate);
			if(null!=vo.getTaskName()&&!"".equals(vo.getTaskName())){
				setTimeAndTaskName(dto,vo);				
			}
			for(int j=1;j<voList.size();j++){
				vo=voList.get(j);
				if(vo.getServiceName().equals(serviceName)&&vo.getIpAddr().equals(ipAddr)&&sdf.format(vo.getExecDate()).equals(sdf.format(execDate))){
					if(null!=vo.getTaskName()&&!"".equals(vo.getTaskName())){
						setTimeAndTaskName(dto,vo);				
					}
					voForCmd.add(vo);
				}
			}
			voList.removeAll(voForCmd);
			voForCmd=new ArrayList<PorterRecordVO>();
			resultList.add(dto);
			i=0;
		}
		return resultList;
	}

	private void setTimeAndTaskName(PorterRecordDTO dto, PorterRecordVO vo) {
		String taskName=vo.getTaskName();
		Date time=vo.getExecDate();
		if(taskName.equals("clean")){
			dto.getCleanTimes().add(time);
		}else if(taskName.equals("backup")){
			dto.getBackupTimes().add(time);
		}else if(taskName.equals("upload")){
			dto.getUploadTimes().add(time);
		}
	}

	@Override
	public List<ServicePO> getAllServices() {
		return servDao.getAll();
	}

	@Override
	public Integer updateService(ServicePO service) {
		Integer serviceId=servDao.getIdByNameAndIp(service.getServiceName(), service.getIpAddr());
		if(serviceId>0){
			return -1;
		}else{
			return servDao.update(service);				
		}
		
	}

	@Override
	public Integer deleteService(Integer serviceId) {
		return servDao.delete(serviceId);
	}

	@Override
	public Map<String, Map<String, Integer>> getPorterRecordByServiceId(Integer serviceId,Date startDate, Date endDate) {
		LOGGER.info("[PorterRecordServiceImpl]->serviceId="+serviceId);
		LinkedList<PorterRecordVO> voList=recordDao.getPorterRecordByServiceId(serviceId,startDate,endDate);
		SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime=new SimpleDateFormat("hh:mm:ss");
		Map<String,Map<String,Integer>> resultMap=new HashMap<String,Map<String,Integer>>();
		Map<String,Integer> backupMap=new TreeMap<String,Integer>();
		Map<String,Integer> uploadMap=new TreeMap<String,Integer>();
		Map<String,Integer> cleanMap=new TreeMap<String,Integer>();
		Date loopDate=(Date) startDate.clone();
		Calendar cal=Calendar.getInstance();
		
		while(!sdfDate.format(loopDate).equals(sdfDate.format(endDate))){
			backupMap.put(sdfDate.format(loopDate), 0);
			uploadMap.put(sdfDate.format(loopDate), 0);
			cleanMap.put(sdfDate.format(loopDate), 0);
			cal.setTime(loopDate);
			cal.add(cal.DATE, 1);
			loopDate=cal.getTime();
		}
		backupMap.put(sdfDate.format(endDate), 0);
		uploadMap.put(sdfDate.format(endDate), 0);
		cleanMap.put(sdfDate.format(endDate), 0);
		for(int i=0;i<voList.size();i++){
			PorterRecordVO vo=voList.get(i);
			String date=sdfDate.format(vo.getExecDate());
			String time=sdfTime.format(vo.getExecDate());
			if("backup".equals(vo.getTaskName())){
				backupMap.put(date, new Integer(vo.getExecDate().getHours()));
			}else if("upload".equals(vo.getTaskName())){
				uploadMap.put(date, vo.getExecDate().getHours());
			}else if("clean".equals(vo.getTaskName())){
				cleanMap.put(date, vo.getExecDate().getHours());
			}
		}
		resultMap.put("backup", backupMap);
		resultMap.put("upload", uploadMap);
		resultMap.put("clean", cleanMap);
		return resultMap;
	}

	@Override
	public String addService(ServicePO service) {
		Integer serviceId=servDao.addService(service);
		if(0==serviceId){
			return "fail";
		}else{
			return "success";
		}
	}

}
