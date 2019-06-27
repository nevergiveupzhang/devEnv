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
<link rel="stylesheet" href="resources/css/main.css"
	type="text/css" />
<link rel="stylesheet"
	href="resources/css/bootstrap.min.css" type="text/css" />
<title>porter record</title>
</head>
<body>
	<div class="container">
		<div class="mt10"></div>
		<c:choose>
			<c:when test="${fn:length(model)>0}">
				<div>
					<table class="fs16 wid-per-33">
						<thead>
							<tr>
								<th>服务名称</th>
								<th>服务IP</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${model.data}" var="service" varStatus="loop">
								<tr>
									<td>${ service.serviceName}</td>
									<td>${ service.ipAddr }</td>
									<td>
										<img alt="" serviceId="${service.id}"
										serviceName="${ service.serviceName}"
										ipAddr="${service.ipAddr}"
										src="resources/img/Edit_16px.ico"
										class="edit-icon" operation="update"/>
										<img alt="" serviceId="${service.id}"
										serviceName="${ service.serviceName}"
										ipAddr="${service.ipAddr}"
										src="resources/img/Delete_16px.ico"
										class="edit-icon" operation="delete"/>
										<img alt="" src="resources/img/Add_16px.ico"
										class="edit-icon" operation="add"/>
										</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<p class="tip-warnning">未查询到数据</p>
			</c:otherwise>
		</c:choose>
	</div>
	<jsp:include page="modal.jsp" flush="true" />
	<script type="text/javascript">
		$(".edit-icon")
				.click(
						function() {
							var operation=$(this).attr("operation");
							if("update" === operation){
								var serviceId = $(this).attr("serviceId");
								var serviceName = $(this).attr("serviceName");
								var ipAddr = $(this).attr("ipAddr");
								if(null==ipAddr){
									ipAddr="";
								}
								showDialog({
									title : "修改服务",
									htmlContent : "<div class='clear v-center-inline'><label class='col-sm-3 title tar'>服务名称：</label><div class='col-sm-7'><input type='text' name='serviceName' id='serviceName' value="+serviceName+" class='form-control'></input></div><label class='title hide col-sm-2 tal'>*必填</label></div>"
											+ "<div class='clear mt10 v-center-inline'><label class='col-sm-3 title tar'>IP：</label><div class='col-sm-7'><input type='text' name='ipAddr' id='ipAddr' value="+ipAddr+" class='form-control'></input></div><label class='title hide col-sm-2 tal'>*必填</label></div>",
									submitAction : function() {
										var serviceName = $("#serviceName").val();
										var ipAddr = $("#ipAddr").val();
										if ($.trim($("#serviceName").val().replace(
												" ", "")).length == 0) {
											$("#serviceName").parent()
													.next("label").removeClass(
															"hide");
										} else if ($.trim($("#ipAddr").val()
												.replace(" ", "")).length == 0) {
											$("#ipAddr").parent().next("label")
													.removeClass("hide");
										} else {
											$.get("updateService?id="
													+ serviceId
													+ "&serviceName="
													+ $("#serviceName").val()
													+"&ipAddr="
													+$("#ipAddr").val()
													, function(r) {
														if (r > 0) {
															location.reload();
														} else {
															showMessage("更新失败，请重试。");
														}
													});
										}
									}
								});
							}else if("delete" === operation){
								var serviceId = $(this).attr("serviceId");
								confirm({
										htmlContent:"<p>是否确认删除该服务？</p>",
										submitAction : function() {
											$.get("deleteService?id="+serviceId
													, function(r) {
														if (r > 0) {
															location.reload();
														} else {
															showMessage("更新失败，请重试。");
														}
													});
										}
									});
							}else if("add" === operation){
								showDialog({
									title : "添加服务",
									htmlContent : "<div class='clear v-center-inline'><label class='col-sm-3 title tar'>服务名称：</label><div class='col-sm-7'><input type='text' name='serviceName' id='serviceName' class='form-control'></input></div><label class='title hide col-sm-2 tal'>*必填</label></div>"
											+ "<div class='clear mt10 v-center-inline'><label class='col-sm-3 title tar'>IP：</label><div class='col-sm-7'><input type='text' name='ipAddr' id='ipAddr' class='form-control'></input></div><label class='title hide col-sm-2 tal'>*必填</label></div>",
									submitAction : function() {
										var serviceName = $("#serviceName").val();
										var ipAddr = $("#ipAddr").val();
										if ($.trim($("#serviceName").val().replace(
												" ", "")).length == 0) {
											$("#serviceName").parent()
													.next("label").removeClass(
															"hide");
										} else if ($.trim($("#ipAddr").val()
												.replace(" ", "")).length == 0) {
											$("#ipAddr").parent().next("label")
													.removeClass("hide");
										} else {
											$.get("addService?serviceName="
													+ $("#serviceName").val()
													+"&ipAddr="
													+$("#ipAddr").val()
													, function(r) {
														if (r === "success") {
															location.reload();
														} else {
															showMessage("更新失败，请重试。");
														}
													});
										}
									}
								});
							}
							
						});
	</script>
</body>
</html>