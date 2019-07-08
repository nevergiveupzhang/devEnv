package cn.cnnic.domainstat.mapper;

import java.util.List;

import cn.cnnic.domainstat.po.EppContactPO;

public interface EppContactMapper {
	List<EppContactPO> queryWithCdn(String endDate, String likePattern);

	List<EppContactPO> queryWithCn(String startDate, String endDate);

	int queryCountWithCn(String startDate, String endDate);
}
