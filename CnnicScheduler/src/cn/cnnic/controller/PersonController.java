package cn.cnnic.controller;

import java.util.List;

import com.jfinal.core.Controller;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.Person;
import cn.cnnic.utils.DataUtils;

public class PersonController extends Controller{
	public void queryPerson() {
		QueryFilter queryFilter=new QueryFilter();
		queryFilter.setOrderString("");
		queryFilter.setSelectFields("*");
		queryFilter.setWhereString("1=1");
		List<Person> personList=Person.me.find(queryFilter);
		String result=DataUtils.listToJsonStr(personList, Person.me);
		renderJson(result);
	}
}
