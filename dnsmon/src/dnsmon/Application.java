package dnsmon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * 统计每个分组下各个类型的告警数和告警对象详情
 * 
 * @author	zhangtao
 * @since	2018-11-21	v1.0
 */
public class Application {
	private final static String[] TYPES = { "网址DNS可用性", "网址HTTP可用性", "网址正确性", "域名可用性", "域名正确性" };
	private final static String[] URLS = { "http://218.241.116.92/dal/failrate/arecord/", "http://218.241.116.92/dal/failrate/http/",
			"http://218.241.116.92/dal/correctness/website/", "http://218.241.116.92/dal/failrate/",
			"http://218.241.116.92/dal/correctness/domain/" };
	
	public static void main(String[] args) {
		//Map<分组,Map<类型,对象列表>
		Map<String,Map<String,List<String>>> resultMap=new HashMap<String,Map<String,List<String>>>();
		InputStream inputStream=Application.class.getClassLoader().getResourceAsStream("dnsmon.xml");
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> groups = root.elements();
		for (Element group : groups) {
			String groupName = group.attributeValue("name");
			for (Element item : group.elements()) {
				String itemName = item.attributeValue("name");
				String type = item.attributeValue("type");
				float threshold = Float.valueOf(item.attributeValue("threshold"));
				float successRate=0;
				if (TYPES[0].equals(type)) {
					String schema=(null==item.attributeValue("schema"))?"http":item.attributeValue("schema");
					successRate=handle(itemName,type,URLS[0],schema,null);
				}else if(TYPES[1].equals(type)) {
					String schema=(null==item.attributeValue("schema"))?"http":item.attributeValue("schema");
					successRate=handle(itemName,type,URLS[1],schema,null);
				}else if(TYPES[2].equals(type)) {
					String queryType=(null==item.attributeValue("queryType"))?"1":item.attributeValue("queryType");
					successRate=handle(itemName,type,URLS[2],null,queryType);
				}else if(TYPES[3].equals(type)) {
					successRate=handle(itemName,type,URLS[3],null,null);
				}else if(TYPES[4].equals(type)) {
					successRate=handle(itemName,type,URLS[4],null,null);
				}
				if(successRate<threshold) {
					Map<String,List<String>> groupMap =new HashMap<String,List<String>>();
					List<String> itemList=new ArrayList<String>();
					if(resultMap.containsKey(groupName)) {
						groupMap=resultMap.get(groupName);
						if(null!=groupMap.get(type)) {
							itemList=groupMap.get(type);
						}
					}
					itemList.add(itemName);
					groupMap.put(type, itemList);
					resultMap.put(groupName, groupMap);
				}
			}
		}
		
		if(resultMap.isEmpty()) {
			System.out.println("result_code=0");
			System.out.println("status_text='everything goes well!'");
		}else {
			StringBuilder statusText=new StringBuilder();
			for(Entry<String, Map<String, List<String>>> groupEntry:resultMap.entrySet()) {
				String groupNameKey=groupEntry.getKey();
				statusText.append("分组["+groupNameKey+"]有异常,其中");
				Map<String, List<String>> groupItemMap=groupEntry.getValue();
				for(Entry<String, List<String>> itemEntry:groupItemMap.entrySet()) {
					String typeKey=itemEntry.getKey();
					List<String> itemList=itemEntry.getValue();
					statusText.append("["+typeKey+"]类型有"+itemList.size()+"个监控对象存在异常，它们是[");
					int i=0;
					for(String itemValue:itemList) {
						if(i==0) {
							statusText.append(itemValue);
						}else {
							statusText.append(","+itemValue);
						}
					}
					statusText.append("];");
				}
			}
			System.out.println("result_code=1");
			System.out.println("status_text="+statusText.toString());
		}
	}
	/*
	 * 调用dnsmon接口，获取监控对象的成功率
	 * 
	 * @param	itemName	监控对象
	 * @param	type	监控对象的类型
	 * @param	url	dnsmon接口地址
	 * @param	schema	dnsmon接口需要的参数，http和https
	 * @param	queryType	dnsmon需要的参数，1（isp递归），2（本地递归），3（权威查询）
	 */
	private static float handle(String itemName, String type, String url, String schema, String queryType) {
		StringBuilder completeUrl=new StringBuilder();
		completeUrl.append(url);
		completeUrl.append(itemName);
		completeUrl.append("?");
		if(null!=schema) {
			completeUrl.append("schema=");
			completeUrl.append(schema);
			completeUrl.append("?");
		}
		if(null!=queryType) {
			completeUrl.append("query_type=");
			completeUrl.append(queryType);
			completeUrl.append("?");
		}
		completeUrl.deleteCharAt(completeUrl.length()-1);
		HttpClient httpCient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(completeUrl.toString());
		try {
			HttpResponse response =httpCient.execute(httpget);
			HttpEntity entity = response.getEntity();
			JSONObject json=JSONObject.fromObject(EntityUtils.toString(entity));
			JSONArray jsonArray =json.getJSONArray("result");
			JSONObject jsonItem=jsonArray.getJSONObject(0);
			if(jsonItem.containsKey("success_rate")) {
				String successRate=jsonItem.getString("success_rate");
				return new Float(successRate.substring(0, successRate.indexOf("%")))/100;
			}else {
				int totalCount=Integer.valueOf(jsonItem.getString("total_count"));
				int successCount=Integer.valueOf(jsonItem.getString("success_count"));
				return successCount/totalCount;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
