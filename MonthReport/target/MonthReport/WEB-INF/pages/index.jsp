<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>month-report</title>
<link rel="stylesheet"
	href="/resources/lib/bootstrap-3.3.7/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="/resources/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="/resources/lib/shCirecleLoader/jquery.shCircleLoader.css"
	type="text/css" />
<link rel="stylesheet" href="/resources/lib/select2-4.0.7/dist/css/select2.min.css"/>
<script type="text/javascript" src="/resources/lib/jquery-3.4.1.min.js"></script>
<script type="text/javascript"
	src="/resources/lib/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="/resources/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript"
	src="/resources/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript"
	src="/resources/lib/shCirecleLoader/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="/resources/lib/select2-4.0.7/dist/js/select2.min.js"></script>

<style type="text/css">
#messagePanel .modal-content{
	position:absolute;
	left:calc(50% - 100px);
}
.top50 {
	margin-top: 50px;
}
#loader{
	position:absolute;
	left:calc(50% - 49px);
	top:calc(50% - 49px);
} 
.height-50{
	height:50px;
}
.well{
	background-color: rgba(242, 241, 239, 1);
}
a{
	color:#333;
}
.width-160.form-control.width-tag{
	width:160px;
}
.ml10{
	margin-left:10px;
}
</style>
</head>
<body class="">
	<div class="container">
		<div class="row top50">
			<div class="well col-md-6 col-md-offset-3">
				<form class="form-inline">
					<div class="form-group col-md-4">
						<select id="indexName" class="form-control height-50 width-160 width-tag">
							<c:forEach items="${model}" var="item">
								<option>${item.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group col-md-4">
						<div class="input-group date form_date">
							<input id="reportDate" class="form-control height-50" size="16" value="2019-03" type="text" />
							<div class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</div>
						</div>
					</div>
					<button id="startBtn" type="button"
						class="btn  btn-info col-md-4 height-50">生成报告</button>
				</form>
			</div>
		</div>
		<div class=" row">
			<div id="result" style="display: none"
				class="well col-md-4 col-md-offset-3"></div>
		</div>
	</div>
	<jsp:include page="modal.jsp" flush="true" />
	<div id="loader" data-spy="affix"></div>
	<script type="text/javascript">
		$(document).ready(
				function() {
					/* $("#indexName").select2(); */
					$('.form_date').datetimepicker({
						format : 'yyyy-mm',
						startView : "year",
						minView : "year",
						autoclose : "true",
						clearBtn : "true",
						language : "en"
					});
					$("#startBtn").click(
							function() {
								$("#startBtn").attr('disabled',true);
								$('#loader').shCircleLoader({
									 color: 'transparent'
								});
								var indexName = $("#indexName").val();
								var reportDate = $("#reportDate").val();
								$.get("/generate", {
									"indexName" : indexName,
									"reportDate" : reportDate
								}, function(result) {
									$('#loader').shCircleLoader("destroy");
									$("#startBtn").attr('disabled',false);
									if ("false" == result.success) {
										showMessage(result.err);
										return;
									} 
									if ($("#result").is(":hidden")) {
										$("#result").show();
									}
									$("#result").append(
											"<div class='row'><span class='col-sm-6' href='#'>" + result.data
													+ "</span><a class='col-sm-offset-3 col-sm-1 glyphicon glyphicon-download-alt' href='/fetch?q="
													+ result.data + "'></a><a class='col-sm-1 glyphicon glyphicon-list graph-report-file' href='#' url='/graph' data='"
													+ result.data + "'></a><a href='#' class='col-sm-1 glyphicon glyphicon-remove-circle delete-report-file' data='"
													+ result.data + "'></a></div>");
									$(".delete-report-file").off("click");
									$(".graph-report-file").off("click");
									$(".delete-report-file").on("click",function(){
										var q=$(this).attr("data");
										$.get("/deleteFile",{"q":q},function(result){
											if ("false" == result.success) {
												if(null!=result.code){
													showMessage("<span>errCode:"+result.code+",message:"+result.err+"</span>");
												}else{
													showMessage(result.err);
												}
												return;
											}else{
												if(200==result.code){
													showMessage("<span>删除成功！</span>");
												}else if(30004==result.code){
													showMessage("<span>文件不存在！</span>");
												}
											}
										});
									});
									$(".graph-report-file").click(function(){
										var url=$(this).attr("url");
										var data=$(this).attr("data");
										window.open(url+"?q="+data);
									});
									if(30001==result.code){
										showMessage({
											title:"提示",
											htmlContent:"<span>该月报告已生成过(可能非完整月)，未重新发起ES查询！</span><br/><span>可点击下方删除按钮，然后重新生成报告！</span>",
											width:"200px"
										});
										return;
									}else{
										showMessage({
											title:"提示",
											htmlContent:"took: "+result.took+"ms",
											width:"200px"
										});
									}
									
								});
							});
				});
	</script>
</body>
</html>
