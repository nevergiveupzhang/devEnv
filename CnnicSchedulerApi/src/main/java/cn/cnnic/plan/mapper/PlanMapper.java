package cn.cnnic.plan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.plan.model.PlanModel;

public interface PlanMapper {
	List<PlanModel> queryPlans(@Param("personId") String personId,@Param("personName") String personName,@Param("fromDay")  String fromDay, @Param("toDay") String toDay, @Param("planType") String []planType);
}
