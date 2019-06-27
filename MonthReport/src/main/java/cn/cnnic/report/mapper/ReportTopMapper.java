package cn.cnnic.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.report.po.ReportTopPO;

public interface ReportTopMapper {
	List<ReportTopPO> query(@Param("serviceName") String serviceName,@Param("reportType") String reportType,@Param("reportDate")  String reportDate, @Param("fieldName") String fieldName,@Param("fieldValue") String fieldValue);
	void delete(String serviceName, String reportDate);
	void batchInsert(@Param("topToBeInsertedList") List<ReportTopPO> topToBeInsertedList);
}
