package cn.cnnic.whoistool.dao;

import java.util.List;

import cn.cnnic.whoistool.po.CDNDomainPO;
import cn.cnnic.whoistool.po.CNDomainPO;

/*
 * @author zhangtao 
 * @since 2017-6-24
 */

public interface INameServerDao {
	public List<String> queryNameServerBySerial(String domainName);
	public String queryNameServerById(String id);
}
