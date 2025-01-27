package cn.cnnic.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.report.po.ReportDailyPO;

public interface ReportDailyMapper {
	List<ReportDailyPO> query(@Param("serviceName") String serviceName,@Param("reportType") String reportType, @Param("startDate") String startDate,@Param("endDate")  String endDate, @Param("orderFields") String orderFields);

	void delete(String serviceName, String startDate, String endDate);

	void batchInsert(@Param("dailyToBeInsertedList") List<ReportDailyPO> dailyToBeInsertedList);
}
