package cn.cnnic.whoistool.dao;

import java.util.List;

import cn.cnnic.whoistool.po.CDNDomainPO;
import cn.cnnic.whoistool.po.CNDomainPO;

/*
 * @author zhangtao 
 * @since 2017-6-24
 */

public interface IDomainDao {
	public CNDomainPO queryCN(String domainName);
	public CDNDomainPO queryCDN(String domainName);
	public List<String> queryCNDomainStatus(String domainName);
	public List<String> queryCDNDomainStatus(String domainName);
	
}
