define(function(require, exports, module) {
    var Messager = require('support/messager');
    
    exports.upload = function() {
    	var xml = $('#xmlText')[0].value;
    	var startDate = $("#inputStartDate").val();
    	var endDate=$("#inputEndDate").val();
    	if (xml == null || xml == '') {
    		Messager.alert('请填写XML数据！',2000);
			return;
		}
    	if(startDate == null || startDate==''){
    		Messager.alert('开始日期不能为空！',2000);
			return;
    	}
    	if(endDate == null || endDate==''){
    		Messager.alert('结束日期不能为空！',2000);
			return;
    	}
    	if((new Date(Date.parse(startDate.replace(/-/g, "/")))-new Date(Date.parse(endDate.replace(/-/g, "/"))))>0){
    		Messager.alert('结束日期不能早于开始日期！',2000);
    		return;
    	}
    	var containsFestival=$(".festival input[type='radio']:checked").val();
    	var festivalStartDate=$("#festivalStartDate").val();
    	var festivalEndDate=$("#festivalEndDate").val();
    	if("Y"==containsFestival&&(festivalStartDate==null||festivalStartDate=="")){
    		Messager.alert('节假日开始日期不能为空！',2000);
    		return;
    	}else if("Y"==containsFestival&&(festivalEndDate==null||festivalEndDate=="")){
    		Messager.alert('节假日结束日期不能为空！',2000);
    		return;
    	}
    	
    	var containsOvertime=$(".overtime input[type='radio']:checked").val();
    	var overtimeStartDate=$("#overtimeStartDate").val();
    	var overtimeEndDate=$("#overtimeEndDate").val();
    	if("Y"==containsOvertime&&(overtimeStartDate==null||overtimeStartDate=="")){
    		Messager.alert('加班开始日期不能为空！',2000);
    		return;
    	}else if("Y"==containsOvertime&&(overtimeEndDate==null||overtimeEndDate=="")){
    		Messager.alert('加班结束日期不能为空！',2000);
    		return;
    	}
    	
    	var containsVocation=$(".vocation input[type='radio']:checked").val();
    	var vocationText=$("#vocationText").val();
    	if("Y"==containsVocation&&null==vocationText){
    		Messager.alert("请填写休假信息！",2000)
    		return;
    	}
    	var params={
    		"startDate":startDate,
    		"endDate":endDate,
    		"xml":xml,
    		"containsFestival":containsFestival,
    		"festivalStartDate":festivalStartDate,
    		"festivalEndDate":festivalEndDate,
    		"containsOvertime":containsOvertime,
    		"overtimeStartDate":overtimeStartDate,
    		"overtimeEndDate":overtimeEndDate,
    		"containsVocation":containsVocation,
    		"vocationText":vocationText
    	};
		$.ajax({
			type : 'POST',
			url : 'upload/upXml',
			data : params,
			success : function(data) {
				Messager.alert(data,5000);
			}
		});
    };
});
