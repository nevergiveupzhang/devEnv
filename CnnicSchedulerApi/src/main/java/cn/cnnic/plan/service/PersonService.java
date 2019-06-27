package cn.cnnic.plan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.plan.mapper.PersonMapper;
import cn.cnnic.plan.model.PersonModel;
import cn.cnnic.plan.utils.StringUtil;

@Service
public class PersonService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	PersonMapper personMapper;

	public List<PersonModel> queryPersons(String userProfile) {
		LOGGER.info("userProfile=" + userProfile);
		if (null == userProfile) {
			return personMapper.queryPersons(null, null);
		} else if (StringUtil.isNumberic(userProfile)) {
			return personMapper.queryPersons(new String[]{userProfile}, null);
		} else {
			return personMapper.queryPersons(null, new String[] {userProfile});
		}
	}

	public List<PersonModel> queryPersons(String userIdStr, String userNameStr) {
		String []userId=null;
		String []userName=null;
		if(StringUtil.isNotEmpty(userIdStr)) {
			userId=userIdStr.split(",");
		}
		if(StringUtil.isNotEmpty(userNameStr)) {
			userName=userNameStr.split(",");
		}
		return personMapper.queryPersons(userId, userName);
	}
}
