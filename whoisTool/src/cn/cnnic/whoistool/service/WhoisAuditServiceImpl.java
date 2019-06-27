package cn.cnnic.whoistool.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cnnic.whoistool.common.mybatis.DataSourceContextHolder;
import cn.cnnic.whoistool.dao.IWhoisAuditDao;
import cn.cnnic.whoistool.po.WhoisAuditPO;

@Service(value="whoisAuditService")
public class WhoisAuditServiceImpl implements IWhoisAuditService{
	@Resource
	IWhoisAuditDao whoisAuditdao;
	
	@Override
	public String insert(String ipAddr, String[] domains) {
		DataSourceContextHolder.setDbType("dataSourceAudit");
		WhoisAuditPO po=new WhoisAuditPO();
		po.setIpAddr(ipAddr);
		StringBuilder sb=new StringBuilder();
		int i=0,len=domains.length;
		for(String domain:domains){
			i++;
			if(i==len){
				sb.append(domain);
			}else{
				sb.append(domain+",");
			}
		}
		po.setDomainInfo(sb.toString());
		po.setQueryDate(new Date());
		int result=whoisAuditdao.insert(po);
		if(result<=0){
			return "fail";
		}else{
			return "success";
		}
	}

}
