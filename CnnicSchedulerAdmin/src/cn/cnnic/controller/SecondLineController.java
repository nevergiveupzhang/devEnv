package cn.cnnic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.SecondLine;

public class SecondLineController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecondLineController.class);
	private DataService dataService = new DataService();

	public void index() {
	}

	@Before({ POST.class })
	public void save() {
		LOGGER.info("--------------------[SecondLineController->save]\t\tbegin to save secondline");
		String insertedJson = getPara("inserted");
		String startDate=getPara("startDate");
		QueryFilter queryFilter=new QueryFilter();
		queryFilter.setWhereString("startDate='"+startDate+"'");
		queryFilter.setSelectFields("*");
		List<SecondLine> list=SecondLine.me.getEntityList(queryFilter);
		if(null!=list&&list.size()!=0) {
			LOGGER.info("--------------------[SecondLineController->save]	exist secondline found,begin to delete,start date is "+startDate+",total count is "+list.size());
			for(SecondLine entity:list) {
				Integer id=entity.getInt("sid");
				SecondLine.me.deleteById(id);
			}
			LOGGER.info("--------------------[SecondLineController->save]	deleting finished");
		}
		LOGGER.info("--------------------[SecondLineController->save]\t\tinsertedJson:" + insertedJson);
		boolean result = this.dataService.save(insertedJson, SecondLine.class);
		LOGGER.info("--------------------[SecondLineController->save]\t\tsaving secondline finished");
		renderJson(Boolean.valueOf(result));
	}

}
