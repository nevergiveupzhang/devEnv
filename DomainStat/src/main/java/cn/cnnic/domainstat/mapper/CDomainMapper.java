package cn.cnnic.domainstat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.domainstat.po.CDomainPO;

public interface CDomainMapper {
	void query(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("domainName")String domainName);

	int queryCount(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("domainName")String domainName);
}