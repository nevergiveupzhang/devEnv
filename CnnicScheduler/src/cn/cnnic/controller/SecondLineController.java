package cn.cnnic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.GroupPerson;
import cn.cnnic.model.SecondLine;
import cn.cnnic.utils.CalendarUtil;
import cn.cnnic.utils.DataUtils;


public class SecondLineController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecondLineController.class);

	public void index() {
	}

	public void getSecondLine() {
		LOGGER.info("--------------------[SecondLineController->getSecondLine]\t\tbegin to get secondline");
		String startDate=getPara("startDate");
		QueryFilter queryFilter=new QueryFilter();
		if((startDate == null) || ("".equals(startDate))||(!CalendarUtil.isValidDate(startDate,"yyyy-MM-dd"))) {
			renderJson("");
			return;
		}
		queryFilter.setSelectFields("*");
		queryFilter.setWhereString("startDate='"+startDate+"'");
		LOGGER.info("--------------------[SecondLineController->getSecondLine]	"+queryFilter.toString());
		List<SecondLine> secondLineList=SecondLine.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(secondLineList, SecondLine.me);
		renderJson(result);
		LOGGER.info("--------------------[SecondLineController->getSecondLine]	"+result);
		LOGGER.info("--------------------[SecondLineController->getSecondLine] getting secondline finished");
	}

}
