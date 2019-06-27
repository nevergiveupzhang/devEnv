<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript"
	src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="resources/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="resources/js/highcharts.js"></script>
<link rel="stylesheet" href="resources/css/main.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/css/bootstrap.min.css" type="text/css" />
<title>porter record</title>
</head>
<body>
	<div id="container" style="min-width:800px;height:400px"></div>
	<script type="text/javascript">
		var model=${model};
		var categories=[];
		var series=[];
		var backup={};
		var upload={};
		var clean={};
		var backupData=[];
		var uploadData=[];
		var cleanData=[];
		for(var key in model.data.backup){
			categories.push(key);
			backup.name="backup";
			backupData.push(model.data.backup[key]);
		}
		backup.data=backupData;
		for(var key in model.data.upload){
			upload.name="upload";
			uploadData.push(model.data.upload[key]);
		}
		upload.data=uploadData;
		for(var key in model.data.clean){
			clean.name="clean";
			cleanData.push(model.data.clean[key]);
		}
		clean.data=cleanData;
		series.push(backup);
		series.push(upload);
		series.push(clean);
		$('#container').highcharts({                   //图表展示容器，与div的id保持一致
	        chart: {
	            type: 'line'                         //指定图表的类型，默认是折线图（line）
	        },
	        title: {
	            text: model.serviceName      //指定图表标题
	        },
	        xAxis: {
	            categories:categories   //指定x轴分组
	        },
	        yAxis: {
	            title: {
	                text: '时间'                  //指定y轴的标题
	            },
	        },
	        series: series,
	        credits:{
	        	enabled:false,
	        }
	    });
	</script>
</body>
</html>