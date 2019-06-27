package cn.cnnic.whoistool.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.cnnic.whoistool.common.mybatis.DataSourceContextHolder;
import cn.cnnic.whoistool.controller.WhoisController;
import cn.cnnic.whoistool.dao.IContactDao;
import cn.cnnic.whoistool.dao.IDomainDao;
import cn.cnnic.whoistool.dao.INameServerDao;
import cn.cnnic.whoistool.dao.IRegistrarDao;
import cn.cnnic.whoistool.dto.DomainDTO;
import cn.cnnic.whoistool.po.CDNDomainPO;
import cn.cnnic.whoistool.po.CNDomainPO;
import cn.cnnic.whoistool.po.ContactPO;

@Service(value="whoisService")
public class WhoisServiceImpl implements IWhoisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WhoisServiceImpl.class);
	
	@Resource
	private IDomainDao domainDao;
	@Resource
	private IContactDao contactDao;
	@Resource
	private IRegistrarDao registrarDao;
	@Resource
	private INameServerDao nameServerDao;
	
	
	@Override
	public List<DomainDTO> query(String[] domains, String[] whoisDatas) {
		//DataSourceContextHolder.setDbType("dataSourceSrs");
		List<DomainDTO> resultList=new ArrayList<DomainDTO>();
		List<DomainDTO> cnResultList=new ArrayList<DomainDTO>();
		List<DomainDTO> cdnResultList=new ArrayList<DomainDTO>();
		List<String> cnDomains=new ArrayList<String>();
		List<String> cdnDomains=new ArrayList<String>();
		
		//cn域名和cdn域名分开处理，格式不正确的域名直接返回
		for(String domain:domains){
			//cn域名和cdn域名分开处理
			if(domain.endsWith("cn")){
				Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		        Matcher matcher = pattern.matcher(domain);
		        if(matcher.find()){
		        	cdnDomains.add(domain);
		        }else{
		        	cnDomains.add(domain);
		        }
			}else if(domain.endsWith("中国")){
				cdnDomains.add(domain);
			}else{
				//格式不正确的域名直接返回
				cnResultList.add(new DomainDTO(domain,""));
			}
		}
		if(0!=cnDomains.size()){
			for(String domainName:cnDomains){
				//查询域名基本信息
				CNDomainPO domainPO=domainDao.queryCN(domainName);
				//域名不存在
				if(null==domainPO){
					cnResultList.add(new DomainDTO(domainName));
				}else{
					//查询域名状态，查询注册者信息，查询注册机构名称
					DomainDTO domainDTO=domainPO.toDomainDTO();
					domainDTO.setDomainName(domainName);
					List<String> domainStatus=domainDao.queryCNDomainStatus(domainName);
					if(null==domainStatus||domainStatus.isEmpty()){
						domainDTO.setDomainStatus("ok");
					}else{
						String domainStatusRes="";
						for(String ds:domainStatus){
							domainStatusRes+=ds+"<br/>";
						}
						domainDTO.setDomainStatus(domainStatusRes);
					}
					ContactPO contactPO=contactDao.queryContact(domainPO.getRegistrantId());
					if(null!=contactPO){
						domainDTO.setContactName(contactPO.getName());
						domainDTO.setContactEmail(contactPO.getEmail());
						domainDTO.setOrg(contactPO.getOrg());
						domainDTO.setContactCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(contactPO.getCreateDate()));
					}
					List<String> nameServers=nameServerDao.queryNameServerBySerial(domainPO.getSerial());
					String nameserverRes="";
					for(String ns:nameServers){
						nameserverRes+=ns+"<br/>";
					}
					domainDTO.setServerName(nameserverRes);
					String registrarName=registrarDao.queryNameById(domainPO.getSponsorRegId());
					LOGGER.info("query->cnDomains->registrarName:"+registrarName);
					domainDTO.setRegistrarName(registrarName);
					cnResultList.add(domainDTO);
				}
			}	
		}
		if(0!=cdnDomains.size()){
			for(String domainName:cdnDomains){
				//查询域名基本信息
				CDNDomainPO domainPO=domainDao.queryCDN(domainName);
				//域名不存在
				if(null==domainPO){
					cdnResultList.add(new DomainDTO(domainName));
				}else{
					LOGGER.info(domainPO.toString());
					if(null!=domainPO.getNameServer1()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer1());
						domainPO.setNameServer1(ns);
					}
					if(null!=domainPO.getNameServer2()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer2());
						domainPO.setNameServer2(ns);
					}
					if(null!=domainPO.getNameServer3()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer3());
						domainPO.setNameServer3(ns);
					}
					if(null!=domainPO.getNameServer4()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer4());
						domainPO.setNameServer4(ns);
					}
					if(null!=domainPO.getNameServer5()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer5());
						domainPO.setNameServer5(ns);
					}
					if(null!=domainPO.getNameServer6()){
						String ns=nameServerDao.queryNameServerById(domainPO.getNameServer6());
						domainPO.setNameServer6(ns);
					}
					//查询域名状态，查询注册者信息，查询注册机构名称
					DomainDTO domainDTO=domainPO.toDomainDTO();
					domainDTO.setDomainName(domainName);
					List<String> domainStatus=domainDao.queryCDNDomainStatus(domainName);
					if(null==domainStatus||domainStatus.isEmpty()){
						domainDTO.setDomainStatus("ok");
					}else{
						String domainStatusRes="";
						for(String ds:domainStatus){
							domainStatusRes+=ds+"<br/>";
						}
						domainDTO.setDomainStatus(domainStatusRes);
					}
					ContactPO contactPO=contactDao.queryContact(domainPO.getRegistrantId());
					domainDTO.setContactName(contactPO.getName());
					domainDTO.setContactEmail(contactPO.getEmail());
					domainDTO.setOrg(contactPO.getOrg());
					domainDTO.setContactCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contactPO.getCreateDate()));
					String registrarName=registrarDao.queryNameById(domainPO.getSponsorRegId());
					domainDTO.setRegistrarName(registrarName);
					cdnResultList.add(domainDTO);
				}
			}
		}
		
		resultList.addAll(cnResultList);
		resultList.addAll(cdnResultList);
		return resultList;
	}

}
