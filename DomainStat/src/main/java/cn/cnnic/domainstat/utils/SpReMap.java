package cn.cnnic.domainstat.utils;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class SpReMap extends HashMap<String, String>{
	public SpReMap() {
		this.put("内蒙古|nm|neimenggu","nm");
		this.put("河北|he|hebei","he");
		this.put("浙江|zj|zhejiang","zj");
		this.put("北京|bj|beijing","bj");
		this.put("上海|sh|shanghai","sh");
		this.put("天津|tj|tianjin","tj");
		this.put("重庆|cq|chongqing","cq");
		this.put("山西|sx|shanxi","sx");
		this.put("辽宁|ln|liaoning","ln");
		this.put("吉林|jl|jilin","jl");
		this.put("黑龙江|hl|hlj|heilongjiang","hl");
		this.put("江苏|js|jiangsu","js");
		this.put("安徽|ah|anhui","ah");
		this.put("福建|fj|fujian","fj");
		this.put("江西|jx|jiangxi","jx");
		this.put("山东|sd|shandong","sd");
		this.put("河南|ha|henan","ha");
		this.put("湖北|hb|hubei","hb");
		this.put("湖南|hn|hunan","hn");
		this.put("广东|gd|guangdong","gd");
		this.put("广西|gx|guangxi","gx");
		this.put("海南|hi|hainan","hi");
		this.put("四川|sc|sichuan","sc");
		this.put("贵州|gz|guizhou","gz");
		this.put("云南|yn|yunnan","yn");
		this.put("西藏|xz|xizang","xz");
		this.put("陕西|sn|shanxi","sn");
		this.put("甘肃|gs|gansu","gs");
		this.put("宁夏|nx|ningxia","nx");
		this.put("青海|qh|qinghai","qh");
		this.put("新疆|xj|xinjiang","xj");
	}
		
}
