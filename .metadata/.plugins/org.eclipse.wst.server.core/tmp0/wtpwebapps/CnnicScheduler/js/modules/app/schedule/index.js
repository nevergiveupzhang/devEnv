/**
 * 排班展示业务逻辑module
 * 
 * @author zhangtao
 * @version 1.0
 * @date 2018-07-28 15:04
 */
define(function(require, exports, module) {

	var isFestival=require('support/foxUtils').isFestival;
	// 外部全局变量
	Tiger.pbglXhyCache = [];
	Tiger.pbglBcColorObj = {};
	Tiger.pbglEventsCache = {};

	initTodayDuty();
	
	function initTodayDuty(){
		var eventList=['白班','夜班','白连夜'];
		var date = new Date();
		var dayS=date.getFullYear()+"-"+(date.getMonth()<9?("0"+(date.getMonth()+1)):(date.getMonth()+1))+"-"+(date.getDate()<10?("0"+date.getDate()):date.getDate());
		date.setTime(date.getTime()+24*60*60*1000);
		var dayE=date.getFullYear()+"-"+(date.getMonth()<9?("0"+(date.getMonth()+1)):(date.getMonth()+1))+"-"+(date.getDate()<10?("0"+date.getDate()):date.getDate());
		var queryFilter = {
			"startDate" : dayS,
			"endDate" : dayE,
			"eventList" : eventList
		};
		$.ajax({
			url : hostUrl
					+ 'schedule/getSchedulerList',
			type : 'POST',
			async : false, // 同步
			data : queryFilter,
			dataType : 'json',
			success : function(result) {
				var dayDuty=[];
				var nightDuty=[];
				for(var i=0;i<result.length;i++){
					if("白班"==result[i].events){
						dayDuty.push(result[i].name);
					}else if("夜班"==result[i].events||"白连夜"==result[i].events){
						nightDuty.push(result[i].name);
					}
				}
				var htmlContent="今日白班:"+dayDuty.join("&")+"&nbsp;&nbsp;&nbsp;&nbsp;"+"今日夜班:"+nightDuty.join("&");
				$("#todayDuty").html(htmlContent);
			}
		});
	}
	/**
	 * 初始化Calender
	 */
	exports.buildWeekCalendar = function() {
		$("#exportMonthScheduler").hide();
		getPlanInfoAndColor(); // 获取班次并创建弹出层
	}
	
	exports.buildWeekGroupCalendar=function(){
		$("#exportMonthScheduler").hide();
		var groupName = $(".workingModel").attr("data-name");
		var options={};
		var queryFilter={
				"groupName":groupName
		};
		options.queryFilter=queryFilter;
		getPlanInfoAndColor(options); // 获取班次并创建弹出层
	}
	
	/**
	 * 获取并保存班次与班次的对应颜色，例如在展示时用到
	 */
	function getPlanInfoAndColor(options) {
		$.ajax({
			url : hostUrl + 'plan/getList/',
			type : 'GET',
			dataType : 'json'
		}).done(function(result) {
			Tiger.pbglBcColorObj = {};
			if (!result.length)
				return;
			for (var i = 0; i < result.length; i++) {
				var bcmc = result[i].planName;
				var color = result[i].color;
				Tiger.pbglBcColorObj[bcmc] = color;
			}
			;
			getPersonGroup(options);
		}).fail(function() {
			console.log("error");
		});
	}

	/**
	 * 获取人员的分组信息
	 * 
	 * @author zhangtao
	 * @version 1.0
	 * @date 2018-07-28 15:04
	 */
	function getPersonGroup(options) {
		$.ajax({
			url : hostUrl + 'schedule/getGroupPersonList/',
			type : 'GET',
			dataType : 'json',
			data : (typeof options ==='undefined')?'':options.queryFilter,
		}).done(function(data) {
			dealTheData(data);
		}).fail(function() {
			console.log("error");
		});
		// 处理结果数据
		function dealTheData(data) {
			Tiger.pbglXhyCache = [];
			Tiger.pbglRowCnt = data.length;
			if (data.length > 0) {
				var compareGid = data[0].gid;
				var compareGName = data[0].groupName;
				var Gid, personArr = [];
				for (var i = 0; i < data.length; i++) {
					Gid = data[i].gid;
					if (compareGid == Gid) {
						var name = data[i].pid + "|" + data[i].name;
						personArr.push(name);
					} else {
						var obj = {
							'groupName' : compareGName,
							'personArr' : personArr
						}
						Tiger.pbglXhyCache.push(obj);
						compareGid = Gid;
						compareGName = data[i].groupName || '未分组人员';
						personArr = [];
						var name = data[i].pid + "|" + data[i].name;
						personArr.push(name);
					}
				}
				;
				var obj = {
					'groupName' : compareGName,
					'personArr' : personArr
				}
				Tiger.pbglXhyCache.push(obj);
				initCalendar();
			}
		}
	}

	function initCalendar() {
		// 初始化日历
		var calendar = $('#pbgl_calendar').fullCalendar({
			header : {
				left : '',
				center : 'monthBtn,weekBtn',
				right : 'prev,next,today'
			},
			customButtons:{
				monthBtn:{
					text:"月视图",
					click:function(){
						loadUI('WEB-INF/views/schedule/schedulerMonth.html');
					}
				},
				weekBtn:{
					text:"周视图",
					click:function(){
						loadUI('WEB-INF/views/schedule/scheduler.html');
					}
				}
			},
			buttonText : {
				today : '本周',
			},
			defaultView : "basicWeek",
			firstday : 1
		});
		Tiger.calendar = calendar;
	}

	

	/**
	 * 初始化Calender
	 */
	exports.buildMonthCalendar = function() {
		$("#exportMonthScheduler").show();
		$('#pbgl_calendar')
				.fullCalendar(
						{
							defaultView : 'month',
							height : 'auto',
							header : {
								left : '',
								center : 'monthBtn,weekBtn',
								right : 'prev,next,today '
							},
							footer : {
								right : 'prev,next,today'
							},
							customButtons:{
								monthBtn:{
									text:"月视图",
								},
								weekBtn:{
									text:"周视图",
									click:function(){
										loadUI('WEB-INF/views/schedule/schedulerWeek.html');
									}
								}
							},
							dayRender:function(date,cell){
								if(isFestival(date.format())&&!cell.hasClass("fc-disabled-day")){
									cell.css("background-color", "rgba(250, 177, 160, 0.2)");
								}
							},
							eventOrder : 'order',
							eventSources : [ 
								{
									events : function(start, end, timezone,
											callback) {
										var dayS = start.format();
										var dayE =end.format();
										Tiger.currentYear=dayS.split("-")[0];
										Tiger.currentMonth=dayS.split("-")[1];
										var date=dayS.split("-")[0]+"年"+dayS.split("-")[1]+"月 排班表";
										$("#pbgl_calendar .fc-header-toolbar .fc-left").html("<span style='font-weight:bold;'>"+date+"</span>");
										$("#pbgl_calendar .fc-header-toolbar .fc-center .fc-monthBtn-button").addClass("fc-state-disabled");
										$("#pbgl_calendar .fc-header-toolbar .fc-center .fc-monthBtn-button").attr("disabled",true);
										callback([]);
									}
								},
								{
									events : function(start, end, timezone,
										callback) {
									var dayS = start.format();
									var dayE = end.format();
									Tiger.currentYear=dayS.split("-")[0];
									Tiger.currentMonth=dayS.split("-")[1];
									var eventList=['白班','夜班','白连夜'];
									var queryFilter = {
										"startDate" : dayS,
										"endDate" : dayE,
										"eventList" : eventList
									}
									$
											.ajax({
												url : hostUrl
														+ 'schedule/getSchedulerList',
												type : 'POST',
												async : false, // 同步
												data : queryFilter,
												dataType : 'json',
												success : function(result) {
													var events = [];
													var itemsDay = {};
													var itemsNight = {};
													var dateArr = [];
													for (var i = 0; i < result.length; i++) {
														if (null == itemsDay[result[i].day]
																&& null == itemsNight[result[i].day]) {
															dateArr
																	.push(result[i].day);
														}
														var pname = result[i].name;
														if ("夜班" == result[i].events
																|| "白连夜" == result[i].events) {
															if (null == itemsNight[result[i].day]) {
																itemsNight[result[i].day] = "夜："
																		+ pname;
															} else {
																itemsNight[result[i].day] = itemsNight[result[i].day]
																		+ ","
																		+ pname;
															}
														} else {
															if (null == itemsDay[result[i].day]) {
																itemsDay[result[i].day] = "白："
																		+ pname;
															} else {
																itemsDay[result[i].day] = itemsDay[result[i].day]
																		+ ","
																		+ pname;
															}
														}

													}
													for (var j = 0; j < dateArr.length; j++) {
														events
																.push({
																	title : itemsDay[dateArr[j]],
																	start : dateArr[j],
																	order : 1
																});
														events
																.push({
																	title : itemsNight[dateArr[j]],
																	start : dateArr[j],
																	order : 2
																});

													}
													callback(events);
												}
											});
								},
								// color : '#2ecc71', // an option!
								textColor : 'black', // an option!
								className : 'schedualitem-month'
							} ],
							displayEventTime : true,
							displayEventEnd : true,
							weekMode : "variable",
							weekNumbers : true,
							allDaySlot : false,
							aspectRatio : 2.0,
							timeFormat : 'HH:mm',
							showNonCurrentDates : false,
							firstDay : 6,
							buttonText : {
								today : '本月',
								month : '月',
								agendaWeek : '周',
								agendaDay : '日'
							},
							locale : 'zh-cn',
						});
	}

	exports.buildMonthSingleCalendar = function() {
		$("#exportMonthScheduler").hide();
		var pid = $(".workingModel").attr("data-id");
		var pname = $(".workingModel").attr("data-name");
		$('#pbgl_calendar')
				.fullCalendar(
						{
							defaultView : 'month',
							height : 'auto',
							header : {
								left : '',
								center : 'monthBtn,weekBtn',
								right : 'prev,next,today '
							},
							footer : {
								right : 'prev,next,today'
							},
							customButtons:{
								monthBtn:{
									text:"月视图",
									click:function(){
										loadUI('WEB-INF/views/schedule/schedulerMonth.html');
									}
								},
								weekBtn:{
									text:"周视图",
									click:function(){
										loadUI('WEB-INF/views/schedule/schedulerWeek.html');
									}
								}
							},
							dayRender:function(date,cell){
								if(isFestival(date.format())){
									cell.css("background-color", "rgba(250, 177, 160,0.2)");
								}
							},
							eventOrder : 'order',
							//夜班、白班、常白、白连夜、白班换休、夜班换休、周末休息、出差、出差换休、节日休息、休假分开处理events
							eventSources : [
									//动态更新表头信息
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var date=dayS.split("-")[0]+"年"+dayS.split("-")[1]+"月";
											$("#pbgl_calendar .fc-header-toolbar .fc-left").html("<div class='fox-month-single-title'>"+date+" "+pname + "的个人排班表"+"</div>");
											callback([]);
										}
									},
									// 夜班的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "夜班"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["夜班"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 白班的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "白班"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										// color : '#2ecc71', // an option!
										backgroundColor : Tiger.pbglBcColorObj["白班"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 常白的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "常白"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["常白"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 白连夜的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "白连夜"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["白连夜"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 白班换休的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "白班换休"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["白班换休"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 夜班换休的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "夜班换休"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["夜班换休"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									// 周末休息的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "周末休息"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["周末休息"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									} ,
									// 出差的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "出差"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["出差"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									} ,
									//出差换休的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "出差换休"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["出差换休"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									} ,
									//节日休息的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "节日休息"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["节日休息"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									},
									//休假的单元格渲染
									{
										events : function(start, end, timezone,
												callback) {
											var dayS = start.format();
											var dayE = end.format();
											var queryFilter = {
												"startDate" : dayS,
												"endDate" : dayE,
												"pid" : pid,
												"events" : "休假"
											}
											$
													.ajax({
														url : hostUrl
																+ 'schedule/getSchedulerList',
														type : 'POST',
														async : false, // 同步
														data : queryFilter,
														dataType : 'json',
														success : function(
																result) {
															var events = [];
															for (var i = 0; i < result.length; i++) {
																var day = result[i].day;
																var event = result[i].events;
																events
																		.push({
																			title : event,
																			start : day
																		});
															}
															callback(events);
														}
													});
										},
										backgroundColor : Tiger.pbglBcColorObj["休假"],
										textColor : 'black', // an option!
										className : 'schedualitem-month-single'
									} ],
							displayEventTime : true,
							displayEventEnd : true,
							weekMode : "variable",
							weekNumbers : true,
							aspectRatio : 2,
							allDaySlot : false,
							timeFormat : 'HH:mm',
							showNonCurrentDates : false,
							firstDay : 6,
							buttonText : {
								today : '本月',
								month : '月',
								agendaWeek : '周',
								agendaDay : '日'
							},
							locale : 'zh-cn',
						});
	}

	/**
	 * 初始化日历标题
	 */
	exports.initCalendarToobar = function(options) {
		var view=options.view;
		if(null!=view&&view.indexOf("week")!=0){
			console.log("日历toolbar初始化失败，不支持的视图！"+view);
			return;
		}
		// 设定标题左侧
		var weekEndTime = new Date(Tiger.calenderVisEnd)
				- (24 * 60 * 60 * 1000);
		var monday = $.fullCalendar.formatDate(Tiger.calenderVisStart,
				"yyyy年MM月dd日");
		var sunday = $.fullCalendar.formatDate(new Date(weekEndTime),
				"yyyy年MM月dd日");
		var leftTitleHtml="";
		var centerHtml="";
		var groupName = $(".workingModel").attr("data-name");
		if("week"==view){
			leftTitleHtml= "<span style='font-weight:bold;'>" + monday + " - "
			+ sunday + " 排班表</span>";
			centerHtml="<div class='fc-button-group'><button type='button' class='fc-monthBtn-button fc-button fc-state-default fc-corner-left'>月视图</button><button type='button' class='fc-weekBtn-button fc-button fc-state-default fc-corner-right fc-state-disabled' disabled='disabled'>周视图</button></div>";
		}
		if("weekGroup"==view){
			leftTitleHtml= "<span style='font-weight:bold;'>" + monday + " - "
			+ sunday + " "+groupName+"排班表</span>";
			centerHtml="<div class='fc-button-group'><button type='button' class='fc-monthBtn-button fc-button fc-state-default fc-corner-left'>月视图</button><button type='button' class='fc-weekBtn-button fc-button fc-state-default fc-corner-right'>周视图</button></div>";
		}
		$('#pbgl_calendar .fc-header-left').html(leftTitleHtml);
		$('#pbgl_calendar .fc-header-center').html(centerHtml);
		$('#pbgl_calendar .fc-header-left').css("padding-top", "20px");
		$('#pbgl_calendar .fc-header-center').css("padding-top", "20px");
		$('#pbgl_calendar .fc-header-right').css("padding-top", "20px");
		$("#pbgl_calendar .fc-header-center").find(".fc-monthBtn-button").click(function(){
			loadUI('WEB-INF/views/schedule/schedulerMonth.html');
		});
		$("#pbgl_calendar .fc-header-center").find(".fc-weekBtn-button").click(function(){
			loadUI('WEB-INF/views/schedule/schedulerWeek.html');
		});
	}
	
	/**
	 * 查询排班记录
	 * 
	 * @param {Date}
	 *            visStart 周一
	 * @param {Date}
	 *            visEnd 次周周一
	 */
	var getScheduleEvents = function(visStart, visEnd) {
		var dayS = $.fullCalendar.formatDate(visStart, "yyyy-MM-dd");
		var dayE = $.fullCalendar.formatDate(visEnd, "yyyy-MM-dd");
		var queryFilter = {
			"startDate" : dayS,
			"endDate" : dayE
		}
		$.ajax({
			url : hostUrl + 'schedule/getSchedulerList',
			type : 'POST',
			async : false, // 同步
			data : queryFilter,
			dataType : 'json'
		}).done(function(result) {
			dealScheduleData(result);
		}).fail(function() {
			console.log("error");
		});
		function dealScheduleData(result) {
			// 清空
			Tiger.pbglEventsCache = {};
			Tiger.pbglEventsDateCache = {};
			if (result.length > 0) {
				// 组织结果，将结果按人员组织
				var comparepid = result[0].pid;
				var day = result[0].day;
				var pid, dayEvents = {}, eventsPerDay = [];
				for (var i = 0; i < result.length; i++) {
					pid = result[i].pid;
					if (comparepid == pid) {
						day = result[i].day;
						// 日期：班次
						dayEvents[day] = result[i].events;
					} else {
						Tiger.pbglEventsCache[comparepid] = dayEvents;
						comparepid = pid;
						dayEvents = {};
						day = result[i].day;
						dayEvents[day] = result[i].events;
					}
					eventsPerDay = Tiger.pbglEventsDateCache[day] || [];
					eventsPerDay.push(result[i].events);
					Tiger.pbglEventsDateCache[day] = eventsPerDay;
				}
				;
				Tiger.pbglEventsCache[comparepid] = dayEvents;
			}
		}
	}
	// 提供给外部js调用
	Tiger.getScheduleEvents = getScheduleEvents;
	
	function getSecondline(visStart, visEnd){
		var dayS = $.fullCalendar.formatDate(visStart, "yyyy-MM-dd");
		var queryFilter = {
			"startDate" : dayS,
		}
		$.ajax({
			url : hostUrl + 'secondline/getSecondLine',
			type : 'GET',
			async : false, // 同步
			data : queryFilter,
			dataType : 'json'
		}).done(function(result) {
			if(result.length==0){
				Tiger.tableHeader=null;
				return;
			}
			Tiger.tableHeader={};
			Tiger.tableHeader["groupLeader"]=result[0].groupLeader;
			Tiger.tableHeader["network"]=result[0].network;
			Tiger.tableHeader["dba"]=result[0].dba;
			Tiger.tableHeader["cn"]=result[0].cn;
			Tiger.tableHeader["sdns"]=result[0].sdns;
			Tiger.tableHeader["sos2"]=result[0].sos2;
			Tiger.tableHeader["ebero"]=result[0].ebero;
		}).fail(function() {
			console.log("error");
		});
	}
	function getTxl4Seconline(){
		$.ajax({
			url : hostUrl + 'person/queryPerson',
			type : 'GET',
			async : false, // 同步
		}).done(function(result) {
			Tiger.txl={};
			for(var i=0;i<result.length;i++){
				var name=result[i].name;
				var cellphone=result[i].cellphone;
				Tiger.txl[name]=cellphone;
			}
		}).fail(function() {
			console.log("error");
		});
	}
	Tiger.getSecondline=getSecondline;
	Tiger.getTxl4Seconline=getTxl4Seconline;
});
