define(function(require, exports, module){
	var FESTIVALS={
			"traditional":[
				"2018-09-22","2018-09-23","2018-09-24",//中秋节
				"2019-02-05",//春节
				"2019-04-05","2019-04-06","2019-04-07"
				],
			"untraditional":[
				"01-01",//元旦节
				"05-01",//劳动节
				"10-01","10-02","10-03","10-04","10-05","10-06","10-07"//国庆节
				]
	};
	//检查节假日
	exports.isFestival=function(date){
		var ym=date.split("-")[1]+"-"+date.split("-")[2];
		for(var key in FESTIVALS.untraditional){
			if(FESTIVALS.untraditional[key] == ym){
				return true;
			}
		}
		for(var key in FESTIVALS.traditional){
			if(FESTIVALS.traditional[key] == date){
				return true;
			}
		}
		return false;
	}
});