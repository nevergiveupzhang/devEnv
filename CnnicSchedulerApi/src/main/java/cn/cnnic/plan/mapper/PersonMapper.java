package cn.cnnic.plan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cnnic.plan.model.PersonModel;

public interface PersonMapper {
	List<PersonModel> queryPersons(@Param("userId") String[] userId, @Param("userName") String[] userName);
}
