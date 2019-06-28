package cn.cnnic.domainstat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.domainstat.po.EppEntAllDomainPO;

public interface EppEntDomainMapper {
	List<EppEntAllDomainPO> query(@Param("startDate") String startDate,@Param("endDate") String endDate);
	int queryCount(@Param("startDate") String startDate,@Param("endDate") String endDate);

}