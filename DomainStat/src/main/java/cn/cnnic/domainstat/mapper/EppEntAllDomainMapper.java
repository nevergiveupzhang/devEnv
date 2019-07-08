package cn.cnnic.domainstat.mapper;


import org.apache.ibatis.annotations.Param;


public interface EppEntAllDomainMapper {
	void query(@Param("startDate") String startDate,@Param("endDate") String endDate);
	int queryCount(@Param("startDate") String startDate,@Param("endDate") String endDate);
}