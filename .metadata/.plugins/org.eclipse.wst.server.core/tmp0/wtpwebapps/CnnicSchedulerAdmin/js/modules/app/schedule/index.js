/**
 * 排班管理业务逻辑module
 * @author zhangtao
 * @version 1.0
 * @date    2018-12-31
 */
define(function(require, exports, module) {
	var Messager = require('support/messager');
	//外部全局变量
	Tiger.personGroupArrCache = [];
	Tiger.planNameAndColorMappingObjCache = {};
	Tiger.pidAndEventsMappingObjCache = {};
	exports.buildCalendar = function() {
		cachingPersonGroupArrToTiger();
		cachingplanNameAndColorMappingObjCacheToTiger();
		initCalender();//initCalender必须在cachingPersonGroupArrToTiger和cachingplanNameAndColorMappingObjCacheToTiger之后，在bindPlanToolbarBtnEvent之前执行
		createPlanToolbar();//createPlanToolbar必须在cachingplanNameAndColorMappingObjCacheToTiger之后，在bindPlanToolbarBtnEvent之前执行
		bindPlanToolbarBtnEvent();
	}
	/**
	 * 获取人员的分组信息
	 * @author zhangtao
	 * @version 1.0
	 * @date    2018-12-30
	 */
	function cachingPersonGroupArrToTiger() {
		$.ajax({
			url : window.hostUrl + 'schedule/getGroupPersonList/',
			type : 'GET',
			async:false, //必须同步
			dataType : 'json'
		}).done(function(data) {
			Tiger.personGroupArrCache = [];
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
						Tiger.personGroupArrCache.push(obj);
						compareGid = Gid;
						compareGName = data[i].groupName || '未分组人员';
						personArr = [];
						var name = data[i].pid + "|" + data[i].name;
						personArr.push(name);
					}
				}
				var obj = {
					'groupName' : compareGName,
					'personArr' : personArr
				}
				Tiger.personGroupArrCache.push(obj);
			}
		}).fail(function() {
			console.log("error");
		});
	}
	
	/**
	 * 初始化Calender
	 */
	function initCalender() {
		var calendar = $('#pbgl_calendar').fullCalendar({
			header : {
				left : '',
				center : '',
				right : 'prev,next,today'
			},
			buttonText : {
				today : '本周',
			},
			defaultView : "basicWeek",
			firstday : 1
		});
	}

	function cachingplanNameAndColorMappingObjCacheToTiger() {
		$.ajax({
			url : window.hostUrl + 'plan/getList/',
			type : 'GET',
			async:false, //必须同步
			dataType : 'json'
		}).done(function(result) {
			Tiger.planNameAndColorMappingObjCache = {};
			if (!result.length)
				return;
			for (var i = 0; i < result.length; i++) {
				var planName = result[i].planName;
				var color = result[i].color;
				Tiger.planNameAndColorMappingObjCache[planName] = color;
			}
		}).fail(function() {
			console.log("error");
		});
	}
	/**
	 * 查询班次顺序，并创建班次弹出选择层
	 */
	function createPlanToolbar() {
		$.ajax({
			url : window.hostUrl + 'plan/getPlanOrderList/',
			type : 'GET',
			async:false, //必须同步
			dataType : 'json'
		}).done(function(result) {
			dealPlanOrderData(result);
		}).fail(function() {
			console.log("error");
		});

		function dealPlanOrderData(result) {
			var className = "btn-toolbar-button paiban_btn float_button";
			if (!result.length)
				return;
			var planData = result[0].planItem || "";
			if (planData.length > 0) {
				var planNameArr = planData.split("|");
				var html = '';
				for (var k = 0; k < planNameArr.length; k++) {
					var color = Tiger.planNameAndColorMappingObjCache[planNameArr[k]] || "#fff";
					html += '<button class="' + className + '" name="' + color
							+ '">' + planNameArr[k] + '</button>';
				}
				html += '<span class="paiban_seperator"></span>';
				$("#plan_toolbar_container").html('');
				$("#plan_toolbar_container").append(html);
			}
		}
	}
	/**
	 * 弹出层的班次按钮绑定事件
	 */
	function bindPlanToolbarBtnEvent() {
		Tiger.scheduleItemCloseBind();
		//弹窗选择层
		$("#plan_toolbar_container")
				.find('button')
				.click(
						function(e) {
							var planName = $(this).html();
							var planColor = $(this).attr('name');
							var itemContainer = currentPopupTargetObj
									.find('.fc-day-content');
							var itemLen = itemContainer.children().length;
							//获取已有班次名称
							var itemInfo = itemContainer.children().find(
									'.pbschedualitem').map(function() {
								return $(this).html();
							}).get().join('|');
							if (itemLen < 1) {
								var html = '<div class="schedualitem" style="background-color:'
										+ planColor + '">';
								html += '<div class="pbschedualitem">'
										+ planName
										+ '</div>'
										+ '<button type="button" class="close pbschedualitemclose">&times;</button></div>';
								itemContainer.append(html);
								Tiger.scheduleItemCloseBind();
							} else {
								Messager.alert('每天排班数量不许超过1个！', 1000);
							}
							//标记为修改
							currentPopupTargetObj.find('.fc-day-content').attr(
									'data-edit', 'edit');
						});
	}
	/**
	 * 初始化日历标题
	 */
	exports.initCalendarTitle = function() {
		//设定标题
		var weekEndTime = new Date(Tiger.calenderVisEnd)
				- (24 * 60 * 60 * 1000);
		var weekStartDay = $.fullCalendar.formatDate(Tiger.calenderVisStart,
				"yyyy年MM月dd日");
		var weekEndDay = $.fullCalendar.formatDate(new Date(weekEndTime),
				"yyyy年MM月dd日");
		var titleHtml = "<span style='font-weight:bold;'>本周 (" + weekStartDay
				+ " - " + weekEndDay + ") 排班表</span>"
		$('#pbgl_calendar .fc-header-left').html('');
		$('#pbgl_calendar .fc-header-left').append(titleHtml);
	}
	/**
	 * 查询排班记录
	 * @param  {Date} visStart 周一
	 * @param  {Date} visEnd   次周周一
	 */
	Tiger.cachingScheduleEvents = function(visStart, visEnd) {
		var dayS = $.fullCalendar.formatDate(visStart, "yyyy-MM-dd");
		var dayE = $.fullCalendar.formatDate(visEnd, "yyyy-MM-dd");
		var whereString = "day>='" + dayS + "' and day<'" + dayE + "'";
		var queryFilter = {
			"whereString" : whereString
		}
		$.ajax({
			url : window.hostUrl + 'schedule/getSchedulerList',
			type : 'GET',
			async : false, //同步
			data : queryFilter,
			dataType : 'json'
		}).done(function(result) {
			dealScheduleData(result);
		}).fail(function() {
			console.log("error");
		});
		function dealScheduleData(result) {
			//清空
			Tiger.pidAndEventsMappingObjCache = {};
			if (result.length > 0) {
				//组织结果，将结果按人员组织
				var comparepid = result[0].pid;
				var day = result[0].day;
				var pid, dayEventMapping = {};
				for (var i = 0; i < result.length; i++) {
					pid = result[i].pid;
					if (comparepid == pid) {
						day = result[i].day;
						//日期：班次
						dayEventMapping[day] = result[i].events;
					} else {
						Tiger.pidAndEventsMappingObjCache[comparepid] = dayEventMapping;
						comparepid = pid;
						dayEventMapping = {};
						day = result[i].day;
						dayEventMapping[day] = result[i].events;
					}
				}
				;
				Tiger.pidAndEventsMappingObjCache[comparepid] = dayEventMapping;
			}
		}
	}

	Tiger.scheduleItemCloseBind=function(){
		$("button.pbschedualitemclose").click(
				function(e) {
					$(this).parent().parent().attr(
							'data-edit', 'edit');
					;
					$(this).parent().remove();
				});
	}
	/**
	 * 日历编辑
	 */
	exports.calendarEditFuc = function() {
		//可编辑
		Tiger.popUpLayer = new PopupLayer({
			trigger : ".fc-day-content",
			popupBlk : "#plan_toolbar_container",
			//closeBtn : "#close7",
			eventType : "mouseover",
			disEventType : "mouseout"
		});
		$("#piaban_startEdit").hide();
		$("#piaban_clean").hide();
		$("#piaban_add").hide();
		$("#piaban_stopEdit").show();
		//控制编辑的时候不能夸周编辑
		$(".fc-button").addClass('fc-state-disabled');
	}

	/**
	 * 结束编辑
	 * @return {[type]} [description]
	 */
	exports.calendarStopEditFuc = function() {
		try {
			$("#piaban_stopEdit").hide();
			$("#piaban_add").show();
			$("#piaban_clean").show();
			$("#piaban_startEdit").show();
			$("#pbgl_calendar .fc-day-content").unbind('mouseover');
			$("#pbgl_calendar .fc-day-content").unbind('mouseout');
			$("#pbgl_calendar .fc-border-separate").find('td').removeClass(
					'tdselected');
			Tiger.popUpLayer.close();
			$("#plan_toolbar_container").hide();
			//保存后释放按钮
			$(".fc-button").removeClass('fc-state-disabled');
		} catch (e) {
			console.log(e);
		}

		//保存数据库……
		updateSchedual();
		$("#pbgl_calendar .pbschedualitemclose").hide();
	}
	/**
	 * 保存排班（更新）
	 */
	function updateSchedual() {
		var shedualtd = $(".fc-border-separate").find('td.fc-day');
		var divContents = shedualtd.find('.fc-day-content[data-edit=edit]');
		Tiger.notInPeriod=[];
		Tiger.notChanged=[];
		divContents.each(function(index, el) {
			var divContent = $(el) //获取编辑过的td.div
			var pid = divContent.attr('id'); //获取巡护员ID
			var pname=divContent.attr('name');
			var eventsDate = divContent.parent().attr('data-date'); //获取时间
			var events = divContent.children().find('.pbschedualitem').map(
					function() {
						return $(this).html();
					}).get().join('|');
			updateSchedualByPerson(pid, pname,eventsDate, events);
		});
		if(Tiger.notInPeriod.length>0){
			var alertStr="";
			for(var i=0;i<Tiger.notInPeriod.length;i++){
				alertStr+=Tiger.notInPeriod[i];
				if(i!=(Tiger.notInPeriod.length-1)){
					alertStr+=",";
				}
			}
			Messager.alert(alertStr+" 不在排班周期内！", 2000);
			Tiger.notInPeriod=[];
		}
		
		if(Tiger.notChanged.length>0){
			var alertStr="";
			for(var i=0;i<Tiger.notChanged.length;i++){
				alertStr+=Tiger.notChanged[i];
				if(i!=(Tiger.notChanged.length-1)){
					alertStr+=",";
				}
			}
			Messager.alert(alertStr+"修改前后的班次未发生变化！", 1000);
			Tiger.notChanged=[];
		}
		
		//保存数据库
		function updateSchedualByPerson(pid, pname,eventsDate, events) {
			var updateJson = {
				"pid" : pid,
				"pname" : pname,
				"eventsDate": eventsDate,
				"events": events
			}
			$.ajax({
				url : window.hostUrl + 'schedule/updateSchedule',
				type : 'POST',
				data : updateJson,
				async : false, 
				dataType : 'json'
			}).done(function(resultCode) {
				if(-1==resultCode){
					Tiger.notInPeriod.push(eventsDate);
				}else if(-2==resultCode){
					Tiger.notChanged.push(pname+":"+eventsDate);
				}
			}).fail(function() {
				console.log("error");
			});
		}
	}

	exports.cleanCurrentWeekSchedule = function() {
		var weekStartDay = $.fullCalendar.formatDate(Tiger.calenderVisStart,
				"yyyy-MM-dd");
		var weekEndDay = $.fullCalendar.formatDate(Tiger.calenderVisEnd,
				"yyyy-MM-dd");
		var whereStr = "day>='" + weekStartDay + "' and day<'" + weekEndDay
				+ "'";
		var updateFilter = {
			"whereString" : whereStr
		}
		$.ajax({
			url : window.hostUrl + 'schedule/deleteSchedule',
			type : 'POST',
			data : updateFilter,
			dataType : 'json'
		}).done(function(result) {
			console.log(result);
			var shedualtd = $(".fc-border-separate").find('td.fc-day');
			var divContents = shedualtd.find('.fc-day-content');
			divContents.html("");
		}).fail(function() {
			console.log("error");
		});
	}

	exports.addCurrentWeekSchedule = function() {
		var weekStartDay = $.fullCalendar.formatDate(Tiger.calenderVisStart,
				"yyyy-MM-dd");
		var weekEndDay = $.fullCalendar.formatDate(Tiger.calenderVisEnd,
				"yyyy-MM-dd");
		var updateFilter = {
				"startDate" : weekStartDay,
				"endDate" : weekEndDay
			}
		$.ajax({
			url : window.hostUrl + 'schedule/addCurrentWeekSchedule',
			type : 'POST',
			data : updateFilter,
			dataType : 'json'
		}).done(function(result) {
			if(0==result){
				Messager.alert('排班成功！', 2000);
			}else if(-1==result){
				Messager.alert('已排过本周班次，请勿重复安排！', 2000);
			}
			console.log(result);
		}).fail(function(result) {
			console.log(result);
		});
	}
	
	/**
	 * 新增空的排班记录到数据库（新增）
	 * @param  {String} pid  分组人员Id
	 * @param  {String} name 
	 * @param  {String} day  排班日期如 2015-10-11
	 */
	Tiger.saveNullScheduleByPerson = function(pid, name, day) {
		var currentDate = new Date();
		var createDate = currentDate.getFullYear() + "-"
				+ (currentDate.getMonth() + 1) + "-" + currentDate.getDate()
				+ " " + currentDate.getHours() + ":" + currentDate.getMinutes()
				+ ":" + currentDate.getSeconds();
		var objStr = {
			'pid' : pid,
			'name' : name,
			'day' : day,
			'events' : "",
			'createDate' : createDate
		}
		//对象数据字符串
		var objJson = JSON.stringify(new Array(objStr));
		$.ajax({
			url : window.hostUrl + 'schedule/saveSchedule',
			type : 'POST',
			async : false, //同步（避免主键重复问题）
			data : {
				"inserted" : objJson
			},
			dataType : 'json'
		}).done(function(result) {
		}).fail(function() {
			console.log("error");
		});
	}
});
