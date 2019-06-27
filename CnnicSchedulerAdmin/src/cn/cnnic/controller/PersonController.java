package cn.cnnic.controller;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.Person;
import cn.cnnic.utils.DataUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

	public void index() {
		setAttr("personPage", Person.me.paginate(getParaToInt(0, Integer.valueOf(1)).intValue(), 10));
		render("person.html");
	}

	public void add() {
	}

	@Before({ POST.class })
	@ActionKey("/delPerson")
	public void delete() {
		Integer[] IDs = getParaValuesToInt("IDs[]");
		LOGGER.info("--------------------[PersonController->delete]\t\tbegin to delete");
		LOGGER.info("--------------------[PersonController->delete]\t\tperson ids to be deleted:" + IDs);

		String personSql = "delete from gc_schedule_person where pid in(?)";
		String schedulerSql="delete from gc_schedule_scheduler where pid in(?)";
		Object[][] pIDs = new Object[IDs.length][1];
		for (int i = 0; i < IDs.length; i++) {
			pIDs[i][0] = IDs[i];
		}
		Db.batch(personSql, pIDs, 200);
		Db.batch(schedulerSql, pIDs, 200);
		LOGGER.info("--------------------[PersonController->delete]\t\tdeleting end");
	}

	@ActionKey("pageSelectPerson")
	public void pageSelectPerson() throws IOException {
		int rows = Integer.parseInt(getPara("rows"));
		int page = Integer.parseInt(getPara("page"));
		LOGGER.info("--------------------[PersonController->pageSelectPerson] rows=" + rows + "&page=" + page);
		Page<Person> personPage = Person.me.paginate(page, rows);
		String result = DataUtils.pageToJson(personPage, Person.me);
		renderJson(result);
	}

	@ActionKey("/queryPerson")
	public void queryPerson() throws IOException {
		QueryFilter queryFilter = new QueryFilter();
		if (getPara() == null) {
			queryFilter.setWhereString("1=1");
		} else {
			queryFilter.setWhereString(getPara());
		}
		queryFilter.setSelectFields("*");
		queryFilter.setOrderString("pid desc");
		List<Person> dictList = Person.me.find(queryFilter);
		String result = DataUtils.listToJsonStr(dictList, Person.me);
		renderJson(result);
	}

	@Before({ POST.class })
	@ActionKey("commitPerson")
	public void commit() throws JsonParseException, JsonMappingException, IOException {
		boolean result = false;
		String insertedJson = getPara("inserted");
		String updatedJson = getPara("updated");
		if ((insertedJson != null) && (!insertedJson.equals(""))) {
			LOGGER.info("--------------------[PersonController->commit]\t\tinsertedJson=" + insertedJson);

			JSONArray jsonArray = JSONArray.fromObject(insertedJson);

			List<Map<String, Object>> mapListJson = jsonArray;

			Person person = null;
			for (int i = 0; i < mapListJson.size(); i++) {
				Map<String, Object> map = (Map) mapListJson.get(i);
				person = new Person();
				result = ((Person) person.setAttrs(map)).save();
			}
		}
		if ((updatedJson != null) && (!updatedJson.equals(""))) {
			LOGGER.info("--------------------[PersonController->commit]\t\tupdatedJson=" + updatedJson);
			JSONArray updatedJsonArray = JSONArray.fromObject(updatedJson);
			List<Map<String, Object>> updatedListJson = updatedJsonArray;
			Person updatePerson = null;
			for (int i = 0; i < updatedListJson.size(); i++) {
				Map<String, Object> map = (Map) updatedListJson.get(i);
				int pid=(int) map.get("pid");
				String newName=(String) map.get("name");
				//必须先修改分组的groupItem，再更新person
				replaceGroupItem(pid,newName);
				updatePerson = new Person();
				result = ((Person) updatePerson.setAttrs(map)).update();
				replaceScheduler(pid,newName);
			}
		}
		renderJson(Boolean.valueOf(result));
	}

	private void replaceScheduler(int pid, String newName) {
		Db.update("update gc_schedule_scheduler set name=? where pid=?",newName,pid);
	}

	private void replaceGroupItem(int pid, String newName) {
		//根据pid找到name
		Record personRecord=Db.findFirst("select name from gc_schedule_person where pid=?",pid);
		String oldName=personRecord.getStr("name");
		if(null!=oldName&&!oldName.equals(newName)) {
			//根据name找到groupName
			Record groupPersonRecord=Db.findFirst("select groupName from gc_schedule_group_person_v where pid=?",pid);
			String groupName=groupPersonRecord.getStr("groupName");
			//根据groupName找到groupItem
			Record groupRecord=Db.findFirst("select groupItem from gc_schedule_group where groupName=?",groupName);
			String groupItem=groupRecord.getStr("groupItem");
			//重新构造groupItem
			String groupItemArr[]=groupItem.split("\\|");
			for(int i=0;i<groupItemArr.length;i++) {
				if(oldName.equals(groupItemArr[i])) {
					groupItemArr[i]=newName;
					break;
				}
			}
			String groupNewItem="";
			for(int i=0;i<groupItemArr.length;i++) {
				if(i==0) {
					groupNewItem+=groupItemArr[i];
				}else {
					groupNewItem+="|"+groupItemArr[i];
				}
			}
			//替换groupItem
			Db.update("update gc_schedule_group set groupItem=? where groupName=?",groupNewItem,groupName);
		}		
	}

}
