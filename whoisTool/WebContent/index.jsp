<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.shCircleLoader-min.js"></script>
<link rel="stylesheet" href="resources/css/jquery.shCircleLoader.css"
	type="text/css" />
<link rel="stylesheet" href="resources/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="resources/css/main.css" type="text/css" />
<title>whois query</title>
<style type="text/css">
table, td, th {
	border: #000000 1px solid;
	border-collapse: collapse;
	font-size: 13px;
}
</style>
</head>
<body>
	<div class="whois-container">
		<div class="search-wrapper">
			<div class="multi-domain-search">
				<div class="multi-domain-box">
					<textarea id="textareaBox" placeholder="请输入查询的域名；一行一个域名，不超过50个。"></textarea>
					<div class="domain-root" id="nameCheckboxContainer">
						<label>调取字段</label>
						<ul id="multiName" class="">
							<li><label> <input name="whoisData"
									value="domainStatus" data-text="域名状态" type="checkbox">
									域名状态
							</label> <label> <input name="whoisData" value="registrantId"
									data-text="注册者ID" type="checkbox"> 注册者ID
							</label> <label> <input name="whoisData" value="org"
									data-text="注册者信息（ORG）" type="checkbox"> 注册者信息（ORG）
							</label> <label> <input name="whoisData" value="contactName"
									data-text="注册者姓名" type="checkbox"> 注册者姓名
							</label> <label> <input name="whoisData" value="contactEmail"
									data-text="注册者联系人邮件" type="checkbox"> 注册者联系人邮件
							</label> <label> <input name="whoisData"
									value="contactCreateDate" data-text="注册者创建时间" type="checkbox">
									注册者创建时间
							</label> <label> <input name="whoisData" value="sponsorRegId"
									data-text="所属注册机构ID" type="checkbox"> 所属注册机构ID
							</label> <label> <input name="whoisData" value="registrarName"
									data-text="所属注册机构名称" type="checkbox"> 所属注册机构名称
							</label> <label> <input name="whoisData" value="serverName"
									data-text="域名服务器" type="checkbox"> 域名服务器
							</label> <label> <input name="whoisData" value="domainRegDate"
									data-text="注册时间" type="checkbox"> 注册时间
							</label> <label> <input name="whoisData"
									value="domainExpirationDate" data-text="到期时间" type="checkbox">
									到期时间
							</label> <label> <input id="selectAll" value="selectAll"
									data-text="全选" type="checkbox"> 全选
							</label></li>
						</ul>
					</div>
					<div class="clear"></div>
					<label class="domain-statistic"> 还可以输入<span
						class="yellow-font" id="domainKeywordLeftNum">50</span>个域名 <span
						class="blue-font" id="clean">清空</span>
					</label>
				</div>
				<a class="search-btn" id="searchMultiBtn" disabled="disabled">立即查询</a>
			</div>
		</div>
		<div id="loader"></div>
		<div class="result-wrapper mb50">
			<div class="result fl">
				<div class="result-tip">
					<ul>
						<li class="on ml90">查询结果</li>
						<li id="download" class="on fr">下载</li>
						<div class="clear"></div>
					</ul>
				</div>
				<div class="result-table-wrapper">
					<table id="resultTable" class="result-table" style="vnd.ms-excel.numberformat:@">
						<tbody id="result-tbody">
						</tbody>
					</table>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<jsp:include page="WEB-INF/pages/modal.jsp" flush="true" />
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var content = $('#textareaBox').val().replace(
									/\ +/g, "").replace(/[\r\n]+/g, "\r\n");
							if ("\r\n" == content) {
								content = "";
							}
							if ("" == content) {
								$("#searchMultiBtn").attr("disabled", true);
							} else {
								$("#searchMultiBtn").attr("disabled", false);
							}
							$('#textareaBox').val(content);
							var count = 0;
							if ("" != content) {
								count = content.replace(/[\r\n]$/g, "").split(
										"\n").length;
							}
							$("#domainKeywordLeftNum").html(50 - count);
						});

		/* 域名文本框事件监听 */
		var charCount = 0;
		$('#textareaBox').bind(
				'input propertychange',
				function() {
					if (charCount > $(this).val().length) {
						charCount = $(this).val().length;
						return;
					}
					charCount = $(this).val().length;
					//去除空格，只允许一次换行
					var content = $(this).val().replace(/\ +/g, "").replace(
							/[\r\n]+/g, "\r\n");
					if ("\r\n" == content) {
						content = "";
					}
					if ("" == content) {
						$("#searchMultiBtn").attr("disabled", true);
					} else {
						$("#searchMultiBtn").attr("disabled", false);
					}
					$(this).val(content);
					var rowCount = 0;
					if ("" != content) {
						rowCount = content.replace(/[\r\n]$/g, "")
								.split("\r\n").length;
					}
					$("#domainKeywordLeftNum").html(50 - rowCount);
				});

		$("#clean").click(function() {
			$('#textareaBox').val("");
			$("#domainKeywordLeftNum").html(50);
			$("#searchMultiBtn").attr("disabled", true);
		});
		/* 查询按钮事件监听 */
		$("#searchMultiBtn").click(
				function() {
					if ($("#searchMultiBtn").attr("disabled")) {
						return;
					}
					//保存checkbox的value值，保存需要调取哪些字段
					var fieldNameArr = [];
					//保存checkbox的value值和data-text值，以map即key-value形式保存，用于保存界面表格的表头（表头字段可能有中文）
					var fieldMapping = {};
					var domainArr = $("#textareaBox").val().split("\n");
					$('input[name="whoisData"]:checked').each(
							function() {
								fieldNameArr.push($(this).val());
								fieldMapping[$(this).val()] = $(this).attr(
										"data-text");
							});
					if (fieldNameArr.length == 0) {
						showMessage("请选择调取哪些whois信息。");
						return;
					}
					$('#loader').shCircleLoader({
					    keyframes:
					       "0%   {background:black}\
					        40%  {background:transparent}\
					        60%  {background:transparent}\
					        100% {background:black}"
					});
					$.post("query", {
						"domains" : domainArr,
						"whoisDatas" : fieldNameArr
					}, function(data) {
						var tbodyHtml = "<tr><td>域名</td>";
						for (var i = 0; i < fieldNameArr.length; i++) {
							tbodyHtml = tbodyHtml + "<td>"
									+ fieldMapping[fieldNameArr[i]] + "</td>";
						}
						tbodyHtml += "</tr>";
						if (data.length > 0) {
							tbodyHtml += "<tr>";
						}
						data = eval('(' + data + ')');
						for (var i = 0; i < data.length; i++) {
							var whoisData = data[i];
							tbodyHtml += "<td style='vnd.ms-excel.numberformat:@'>" + whoisData["domainName"]
									+ "</td>";
							for (var j = 0; j < fieldNameArr.length; j++) {
								tbodyHtml += "<td style='vnd.ms-excel.numberformat:@'>"
										+ whoisData[fieldNameArr[j]] + "</td>";
							}
							tbodyHtml += "</tr>";
						}
						$("#result-tbody").html(tbodyHtml);
						$('#loader').shCircleLoader("destroy");
					});
				});
		/* 全选按钮事件监听 */
		$("#selectAll").change(function() {
			if ($(this).is(":checked")) {
				$("[name=whoisData]:checkbox").prop("checked", true);
			} else {
				$("[name=whoisData]:checkbox").prop("checked", false);
			}
		});

		/* 下载按钮事件监听 */
		$("#download").click(function() {
			if (0 == $("#result-tbody").find("tr").length) {
				showMessage("无whois信息可供下载。");
			} else {
				exportTable("resultTable");
			}
		});
	</script>
	<script type="text/javascript" language="javascript">
		var idTmr;
		function getExplorer() {
			var explorer = window.navigator.userAgent;
			//ie
			if (explorer.indexOf("MSIE") >= 0||explorer.indexOf(".NET") >= 0) {
				return 'ie';
			}
			//firefox
			else if (explorer.indexOf("Firefox") >= 0) {
				return 'Firefox';
			}
			//Chrome
			else if (explorer.indexOf("Chrome") >= 0) {
				return 'Chrome';
			}
			//Opera
			else if (explorer.indexOf("Opera") >= 0) {
				return 'Opera';
			}
			//Safari
			else if (explorer.indexOf("Safari") >= 0) {
				return 'Safari';
			}
		}
		function exportTable(tableid) {//整个表格拷贝到EXCEL中
			if (getExplorer() == 'ie') {
				var curTbl = document.getElementById(tableid);
				var oXL = new ActiveXObject("Excel.Application");

				//创建AX对象excel
				var oWB = oXL.Workbooks.Add();
				//获取workbook对象
				var xlsheet = oWB.Worksheets(1);
				//激活当前sheet
				var sel = document.body.createTextRange();
				sel.moveToElementText(curTbl);
				//把表格中的内容移到TextRange中
				sel.select();
				//全选TextRange中内容
				sel.execCommand("Copy");
				//复制TextRange中内容 
				xlsheet.Paste();
				//粘贴到活动的EXCEL中      
				oXL.Visible = true;
				//设置excel可见属性

				try {
					var fname = oXL.Application.GetSaveAsFilename("Excel.xls",
							"Excel Spreadsheets (*.xls), *.xls");
				} catch (e) {
					print("Nested catch caught " + e);
				} finally {
					oWB.SaveAs(fname);

					oWB.Close(savechanges = false);
					//xls.visible = false;
					oXL.Quit();
					oXL = null;
					//结束excel进程，退出完成
					//window.setInterval("Cleanup();",1);
					idTmr = window.setInterval("Cleanup();", 1);

				}
			} else {
				tableToExcel(tableid)
			}
		}
		function Cleanup() {
			window.clearInterval(idTmr);
			CollectGarbage();
		}
		var tableToExcel = (function() {
			var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><meta http-equiv="Content-Type" charset=utf-8"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:Panes></x:Panes><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>', base64 = function(
					s) {
				return window.btoa(unescape(encodeURIComponent(s)))
			}, format = function(s, c) {
				return s.replace(/{(\w+)}/g, function(m, p) {
					return c[p];
				})
			}
			return function(table, name) {
				if (!table.nodeType)
					table = document.getElementById(table)
				var ctx = {
					worksheet : name || 'Worksheet',
					table : table.innerHTML
				}
				window.location.href = uri + base64(format(template, ctx))
			}
		})()
	</script>
</body>
</html>