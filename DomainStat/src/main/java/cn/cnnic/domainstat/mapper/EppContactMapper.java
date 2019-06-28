package cn.cnnic.domainstat.mapper;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.domainstat.po.EppContactPO;

public interface EppContactMapper {
	EppContactPO query(@Param("contactId") String contactId);
}
