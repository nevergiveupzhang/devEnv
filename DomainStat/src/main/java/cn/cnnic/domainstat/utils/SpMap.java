package cn.cnnic.domainstat.utils;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class SpMap extends HashMap<String, String>{
	public SpMap() {
		this.put("内蒙古","nm");
		this.put("河北","he");
		this.put("浙江","zj");
		this.put("北京","bj");
		this.put("上海","sh");
		this.put("天津","tj");
		this.put("重庆","cq");
		this.put("山西","sx");
		this.put("辽宁","ln");
		this.put("吉林","jl");
		this.put("黑龙江","hl");
		this.put("江苏","js");
		this.put("安徽","ah");
		this.put("福建","fj");
		this.put("江西","jx");
		this.put("山东","sd");
		this.put("河南","ha");
		this.put("湖北","hb");
		this.put("湖南","hn");
		this.put("广东","gd");
		this.put("广西","gx");
		this.put("海南","hi");
		this.put("四川","sc");
		this.put("贵州","gz");
		this.put("云南","yn");
		this.put("西藏","xz");
		this.put("陕西","sn");
		this.put("甘肃","gs");
		this.put("宁夏","nx");
		this.put("青海","qh");
		this.put("新疆","xj");
	}
		
}
