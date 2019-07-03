/**
 * 系统界面切换加载
 * @param  {String} ui    页面路径
 * @param  {JSON}		options.ID,options.NAME
 */
function loadUI(ui,options) {
    var title = "ID" + ui.length;
    $("#scheduler_content").empty();
    if (ui && ui != ""){
    	ui = window.hostUrl + 'home?template=' + ui;
    	
    }
    
    var i = document.getElementById("title");
    if (i == null) {
        $.post(ui, {
            "cache": "11"
        }, function(data) {
            if (data != null && data.indexOf("redirect->login") == 0) {
                window.location = window.hostUrl;
            }
            $(".workingModel").each(function() {
                $(this).hide();
            });
            if (data != "") {
                var dataStr = "<div id=" + title + " class='workingModel' data-name='" +((typeof options ==='undefined')?'':options.NAME)+"' data-id='"+((typeof options ==='undefined')?'':options.ID)+"'>" + data + "</div>";
                $("#scheduler_content").append(dataStr);
            }
        }, "html");
    } else {
        $(".workingModel").each(function() {
            if ($(this).attr("id") == title) {
                $(this).show();
            } else {
                $(this).hide();
            }
        })
    }
}
/**
 * 加载页面并渲染（freemarker）
 * @author giscafer
 * @version 1.0
 * @date    2015-11-05T00:24:33+0800
 * @param   {String}                 route 请求路由
 * @param   {String}                 ui    页面路径
 */
function loadUIAndRender(route, url, callback) {
    var title = "ID" + url.length;
    $("#scheduler_content").empty();
    if (!route) route = "home";
    if (url && url != "")
        url = window.hostUrl + route + '?template=' + url;
    var i = document.getElementById("title");
    if (i == null) {
        $.post(url, {
            "cache": "11"
        }, function(data) {
            if (data != null && data.indexOf("redirect->login") == 0) {
                window.location = window.hostUrl;
            }
            $(".workingModel").each(function() {
                $(this).hide();
            });
            if (data != "") {
                var dataStr = "<div id=" + title + " class='workingModel'>" + data + "</div>";
                $("#scheduler_content").append(dataStr);
            }
            if (typeof callback === "function") {
                callback();
            }
        }, "html");
    } else {
        $(".workingModel").each(function() {
            if ($(this).attr("id") == title) {
                $(this).show();
            } else {
                $(this).hide();
            }
        })
    }
}
