/**
 * 后台异步请求方法
 * 
 * @param url
 * 		请求URL
 * @param showFunc
 * 		回调函数
 * @param params
 * 		请求参数{[xx:123]}
 */
function makeCall(url, showFunc, params) {
	var paramsObj; 
     if(params!=null) {paramsObj = jQuery.param(params,true);} 
     
	 $.ajax({
		url : url,
		type : "post",
		dataType : 'text',
		cache : false,
		data : paramsObj,
		success : function(msg) {
			var json = eval("(" + msg + ")");
			if (json.Exception) {
				layer.msg(json.Exception.message, {icon: 2});
			} 
			showFunc(json);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			// 通常 textStatus 和 errorThrown 之中
			// 只有一个会包含信息
			layer.msg('error', {icon: 2});
			this; // 调用本次AJAX请求时传递的options参数
		}
	}); 
}
/**
 * 表单异步提交方法, 不验证表单
 * @param url
 * @param showFunc
 * @param formName
 */
function sendAjax(url,showFunc,formName) {
	var ld = Loading.alert();
	__btnDisabled();
	// 序列表化form表单
	var params = $('#'+formName).serialize();
	 $.ajax({
		url : url,
		type : "post",
		dataType : 'text',
		cache : false,
		data : params,
		success : function(msg) {
			Loading.close(ld);
			var json = eval("(" + msg + ")");
			if (json.Exception) {
				// 消息弹出层
				layer.msg(json.Exception.message, {icon: 2});
			} 
			showFunc(json);
			__btnEnabled();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			// 通常 textStatus 和 errorThrown 之中
			// 只有一个会包含信息
			layer.msg('error', {icon: 2});
			__btnEnabled();
			Loading.close(ld); 
			this; // 调用本次AJAX请求时传递的options参数
		}
	}); 
}

/**
 * 表单异步提交方法
 * 
 * @param url
 * @param showFunc
 * @param formName
 */
function makeNomalFormCall(url,showFunc,formName){
	var ld = Loading.alert();
	if(!submitForm(formName)) {
		Loading.close(ld);
		return;
	}
	__btnDisabled();
	// 序列表化form表单
	var params = $('#'+formName).serialize();
	 $.ajax({
		url : url,
		type : "post",
		dataType : 'text',
		cache : false,
		data : params,
		success : function(msg) {
			Loading.close(ld);
			var json = eval("(" + msg + ")");
			if (json.Exception) {
				// 消息弹出层
				layer.msg(json.Exception.message, {icon: 2});
			} 
			showFunc(json);
			__btnEnabled();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			// 通常 textStatus 和 errorThrown 之中
			// 只有一个会包含信息
			layer.msg('error', {icon: 2});
			__btnEnabled();
			Loading.close(ld); 
			this; // 调用本次AJAX请求时传递的options参数
		}
	}); 
}

function __btnDisabled() {
	var el = $("input[name='queryBtn']");

	if ( el != null ) {
		el.attr("disabled",true).addClass('btn-disabled');
	} else {
		var el = parent.document.getElementById('inIframe').contentWindow.document.querySelector("input[name='queryBtn']");
		el.disabled = true;	
		el.classList.add('btn-disabled');
	}	
}

function __btnEnabled() {
	var el = $("input[name='queryBtn']");

	if ( el != null ) {
		el.attr("disabled",false).removeClass('btn-disabled');
	} else {
		var el = parent.document.getElementById('inIframe').contentWindow.document.querySelector("input[name='queryBtn']");
		el.disabled = false;	
		el.classList.remove('btn-disabled');
	}
}