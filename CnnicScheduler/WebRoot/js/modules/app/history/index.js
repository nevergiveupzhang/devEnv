define(function(require, exports, module) {
    var Messager = require('support/messager');
    
    exports.initHistory = function() {
        var url = window.hostUrl + 'history/getRecent30Days';
        $.post(url, function(res) {
        		var tbody=$("#historyTable tbody");
        		for(var i=0;i<res.length;i++){
        			var historyItem=res[i];
        			var tr="<tr>";
        			tr+="<td>"+historyItem["person_name"]+"</td>";
        			tr+="<td>"+historyItem["events_date"]+"</td>";
        			tr+="<td>"+historyItem["log_date"]+"</td>";
        			tr+="<td>"+historyItem["from_events"]+"</td>";
        			tr+="<td>"+historyItem["to_events"]+"</td>";
        			tr+="</tr>";
        			tbody.append(tr);
        		}
        }, "JSON");
    };
});
