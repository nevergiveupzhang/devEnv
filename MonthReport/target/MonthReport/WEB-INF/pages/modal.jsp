<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div class="modal fade" id="operationPanel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
   <div class="modal-dialog" role="document">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times; </button>
            <h4 class="modal-title">
            	标题
            </h4>
         </div>
         <div class="modal-body" style="max-height:500px; overflow:auto;">
         
         </div>
         <div class="modal-footer">
            <button id="cancelBtn" type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button id="submitBtn" type="button" class="btn btn-primary">确定</button>
         </div>
      </div>
   </div>
</div>


<div class="modal fade" id="messagePanel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
   <div class="modal-dialog" role="document">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times; </button>
            <h4 class="modal-title">
            	提示
            </h4>
         </div>
         <div class="modal-body" style="max-height:500px; overflow:auto;">
         
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
         </div>
      </div>
   </div>
</div>

<script>
/* options:title,actionName,submitAction */

var submitAction = null;
var cancelAction=null;
function confirm(options){
	if(!options)return;
	options.title = "提示";
	showDialog(options);
}
/* options:title,htmlContent,url,quitName,hideContent,actionName,submitAction */
function initDialog(options){
	if(!options)return;
	if(options.title) {$("#operationPanel .modal-title").html(options.title);}
	if(options.actionName){
		$('#submitBtn').html(options.actionName);
	}
	if(options.quitName){
		$('#cancelBtn').html(options.quitName);
	}
	if(options.htmlContent){
		$("#operationPanel .modal-body").html(options.htmlContent);
	}else if(options.url){
		$("#operationPanel .modal-body").load(options.url,function(r){
		});
	}
	if(options.width){
		$("#operationPanel .modal-content").width(options.width);
	}
	submitAction = options.submitAction;
	cancelAction = options.cancelAction;
}

function showDialog(options){
	if(options){
		initDialog(options);
	}
	$("#operationPanel").modal('show');
}
function closeDialog(){
	$("#operationPanel").modal('hide');
}

$("#submitBtn").click(function(e){
		if(submitAction){
			submitAction();
		}	
	});

$('#operationPanel').on('hidden.bs.modal', function () {
    if(cancelAction){
	    cancelAction();
	}
});


/* options:title,htmlContent,url */
function preview(options){
	if(!options)return;
	createMessageDialog(options);
}
/* options:title,htmlContent, */
function showMessage(options){
	if(!options)return;
	var newOptions={};
	if(typeof options=="string"){
		newOptions.htmlContent = options;	
	}else if(typeof options=="object"){
		newOptions=options;
	}
	createMessageDialog(newOptions);
}

function createMessageDialog(options){
	if(!options)return;
	if(options.title) {$("#messagePanel .modal-title").html(options.title);}
	if(options.htmlContent){
		$("#messagePanel .modal-body").html(options.htmlContent);
	}else if(options.url){
		$("#messagePanel .modal-body").load(options.url);
	}
	if(options.width){
		$("#messagePanel .modal-content").width(options.width);
	}
	if(options.height){
		$("#messagePanel .modal-content").height(options.height);
	}
	$("#messagePanel").modal('show');
}

</script>