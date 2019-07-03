define(function(require, exports, module) {
    var Messager = require('support/messager');
    
    exports.addSecondline = function() {
        var isvalid = $("#updateSecondlineForm").data('bootstrapValidator').isValid();
        if (!isvalid) {
            $("#updateSecondlineForm").data('bootstrapValidator').validate()
            return;
        }
        var groupLeader = $("#inputGroupLeader option:selected").text();
        var network = $("#inputNetwork option:selected").text();
        var dba = $("#inputDba option:selected").text();
        var cn = $("#inputCn option:selected").text();
        var sdns = $("#inputSdns option:selected").text();
        var ebero = $("#inputEbero option:selected").text();
        var sos2 = $("#inputSos2 option:selected").text();
        var startDate = $("#inputStartDate").val();
        var obj = {
            "groupLeader": groupLeader,
            "network": network,
            "dba": dba,
            "cn": cn,
            "sdns": sdns,
            "ebero": ebero,
            "sos2":sos2,
            "startDate":startDate
        }
        var objJson = JSON.stringify(new Array(obj));
        var effectPlan = {
                "inserted": objJson,
                "startDate":startDate
            };
        var url = window.hostUrl + 'secondline/save';
        $.post(url, effectPlan, function(res) {
            if (res) {
            	Messager.alert("保存成功！",1000);
            	loadUI('WEB-INF/views/secondline/index.html');
            	}
        }, "JSON");
    };
});
