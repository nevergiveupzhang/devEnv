<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/resources/lib/bootstrap-3.3.7/css/bootstrap.min.css"
	type="text/css" />
<script type="text/javascript" src="/resources/lib/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="/resources/lib/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/lib/highcharts.js"></script>
<title></title>
<style type="text/css">
#container{
	position:absolute;
	width:calc(100% - 100px);
	height:calc(100% - 100px);
	top:50px;
	left:50px;
}
</style>
</head>
<body>
	<div id="container"></div>
	<script type="text/javascript">
		var model=${model};
		var description=model.description;
		var categories=model.days;
		var series=model.channelDayCounts;
		$("title").text(description);
		$('#container').highcharts({                   //图表展示容器，与div的id保持一致
	        chart: {
	            type: 'line'                         //指定图表的类型，默认是折线图（line）
	        },
	        title: {
	            text: description      //指定图表标题
	        },
	        xAxis: {
	            categories:categories   //指定x轴分组
	        },
	        yAxis: {
	            title: {
	                text: '数量'                  //指定y轴的标题
	            },
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    // 开启数据标签
	                    enabled: true          
	                },
	                // 关闭鼠标跟踪，对应的提示框、点击事件会失效
	                enableMouseTracking: false
	            }
	        },
	        series: series,
	        credits:{
	        	enabled:false,
	        }
	    });
	</script>
</body>
</html>