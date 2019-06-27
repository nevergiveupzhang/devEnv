<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="resources/css/main.css" type="text/css" />
<link rel="stylesheet" href="resources/css/flatpickr.min.css"
	type="text/css" />
<script type="text/javascript" src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="resources/js/flatpickr.min.js"></script>
<title>porter record</title>

</head>
<body>
	<div class="container">
		<div id="header" class="header">
			<form action="queryByDate" method="get">
				<input id="start-date" required="required" name="startDate"
					class="flatpickr input" data-date-format="Y-m-d" /> <input
					id="end-date" required="required" name="endDate"
					class="flatpickr input" data-date-format="Y-m-d" /> <input
					type="submit" value="查询" /> <a class="ml50" href="edit"
					target="blank">服务列表</a>
			</form>

		</div>
		<c:choose>
			<c:when test="${fn:length(model)>0}">
				<div id="content" class="mt50"">
					<table class="fs16 wid-per-25">
						<thead>
							<tr>
								<th>服务名称</th>
								<th>backup</th>
								<th>upload</th>
								<th>clean</th>
								<!-- <th>日期</th> -->
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${model.data}" var="record" varStatus="loop">
								<tr>
									<td><a
										href="showChart?id=${record.id}&serviceName=${record.serviceName}&startDate=${ model.startDate}&endDate=${ model.endDate}"
										target="blank">${ record.serviceName}</a></td>
									<td>
										<c:choose>
											<c:when test="${(record.backupTimes)==null||fn:length(record.backupTimes) == 0}">
												<p class="red">please check!</p>
											</c:when>
											<c:otherwise> 
												<c:forEach items="${record.backupTimes}"
													var="backupTime" varStatus="loop">
													<p>
														<fmt:formatDate timeZone="Etc/GMT-8" value="${backupTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${(record.uploadTimes)==null||fn:length(record.uploadTimes) == 0}">
												<p class="red">please check!</p>
											</c:when>
											<c:otherwise> 
												<c:forEach items="${record.uploadTimes}"
													var="uploadTime" varStatus="loop">
													<p>
														<fmt:formatDate timeZone="Etc/GMT-8" value="${uploadTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${(record.cleanTimes)==null||fn:length(record.cleanTimes) == 0}">
												<p class="red">please check!</p>
											</c:when>
											<c:otherwise> 
												<c:forEach items="${record.cleanTimes}"
													var="cleanTime" varStatus="loop">
													<p>
														<fmt:formatDate timeZone="Etc/GMT-8" value="${cleanTime}"
															pattern="yyyy-MM-dd HH:mm:ss" />
													</p>
												</c:forEach>
											</c:otherwise>
										</c:choose>
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
		<div id="footer" class="footer mt10 v-center-inline">
			<p>Copyright ©2017 运行管理部</p>
		</div>
	</div>

	<script type="text/javascript">
		$('#start-date').val("${ model.startDate}");
		$('#start-date').flatpickr();
		$('#end-date').val("${ model.endDate}");
		$('#end-date').flatpickr();
	</script>
</body>
</html>