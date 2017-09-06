//added by andy.ten@tom.com
var divDialogFlag = false;

// ====================拖动开始===================================
var labelDivHeight = 30; // 设置拖动条的高度
var _IsMousedown = 0;
var _ClickLeft = 0;
var _ClickTop = 0;
var backBoardDivId = "dialog_backBoard_div";
var headDivId = "dialog_head_div";
var _backBoardDiv ;
var _resizeEvtGl = {};

function moveInit(evt) {
	_IsMousedown = 1;
	var browserType = getBrowserType();
	if (browserType == "NSupport") {
		return;
	}
	owner = getTopWinRef();
	owner.getElementById(headDivId).setCapture();
	var obj = evt.srcElement.parentNode;
	obj = owner.getElementById(backBoardDivId);
	_backBoardDiv = obj;
	if (browserType == "fox") {
		_ClickLeft = evt.pageX - parseInt(obj.style.left);
		_ClickTop = evt.pageY - parseInt(obj.style.top);
	} else {
		_ClickLeft = evt.x - parseInt(obj.style.left);
		_ClickTop = evt.y - parseInt(obj.style.top);
	}
}
function Move(evt) {
	var browserType = getBrowserType();
	if (_IsMousedown == 0) {
		return;
	}
	owner = getTopWinRef();
	var objDiv = evt.srcElement.parentNode;
	objDiv = owner.getElementById(backBoardDivId);
	objDiv = _backBoardDiv;
	if (browserType == "fox") {
		objDiv.style.left = evt.pageX - _ClickLeft;
		objDiv.style.top = evt.pageY - _ClickTop;
	} else {
		objDiv.style.left = evt.x - _ClickLeft;
		objDiv.style.top = evt.y - _ClickTop;
	}
}
function stopMove() {
	_IsMousedown = 0;
	owner.getElementById(headDivId).releaseCapture();
	_backBoardDiv = null;
}
function getObjById(id) {
	return document.getElementById(id);
}
function getBrowserType() {
	var browser = navigator.appName;
	var b_version = navigator.appVersion;
	var version = parseFloat(b_version);
	if ((browser == "Netscape")) {
		return "fox";
	} else if (browser == "Microsoft Internet Explorer") {
		if (version >= 4) {
			return "ie4+";
		} else {
			return "ie4-";
		}
	} else {
		return "NSupport";
	}
}

//====================拖动结束===================================

function getTopWinRef(){
	 if(divDialogFlag == false)
	 {  
		 if(parent && parent.parent){
		  	return parent.parent.document;
		 }else if( parent ){
		  	return parent.document;
		 }else{
		  	return document;
		 }
	 }else
	 {
	     return document;
	 }    
}

//modified by andy.ten@tom.com
function _dialogInit(sWidth ,sHeight ,sTop ,sLeft, className){//初始化
    var owner = null;
	owner = getTopWinRef();
	if( owner.getElementById('dialog_div')==null ){
		var div = owner.createElement('div');
		div.setAttribute('id','dialog_div');

		if ( className ) {
			div.classList.add(className);
		}
		
		owner.getElementsByTagName('body').item(0).appendChild(div);
		
		// 创建背景板,装填BackGroupDiv,HeadDiv,ContentDiv,ContentFrame
		// 用户整体拖动
		 var backBoardDiv = owner.createElement('div');
		backBoardDiv.setAttribute('id',backBoardDivId);
		div.appendChild(backBoardDiv); 
		//owner.getElementsByTagName('body').item(0).appendChild(backBoardDiv);

		_crtBackGlassDiv(sWidth ,sHeight ,sTop ,sLeft);
		_crtBackIFrame(sWidth ,sHeight ,sTop ,sLeft); //add by zhaoli
		_crtModalDiv(sWidth ,sHeight ,sTop ,sLeft);
		_crtBackGroudDiv();
		_crtHeadDiv();
		_crtContentDiv();
		_crtContentFrame();

		 var objDiv = div;//owner.getElementById(backBoardDivId);
		if (document.all){//IE
			objDiv.attachEvent("onmousedown",moveInit);
			objDiv.attachEvent("onmouseup",stopMove);
			objDiv.attachEvent("onmousemove",Move);
//			objDiv.attachEvent("onmouseout",stopMove);
		}
		else{//OTHER
			objDiv.addEventListener("onmousedown",moveInit,false); 
			objDiv.addEventListener("onmouseup",stopMove,false); 
			objDiv.addEventListener("onmousemove",Move,false); 
//			objDiv.addEventListener("onmouseout",stopMove,false); 
		} 
		
	} 
//	if(owner.getElementById('dialog_modal_div')!=null){
//		owner.getElementById('dialog_modal_div').onclick = function(){_hide();}
//	}
	owner =null;
}

function _dialogDestroy(){//清扫遗留对象
	var owner = getTopWinRef();
	
	//移除事件，并设置成空。
	if( owner.getElementById('dialog_alert_button') ) 
		owner.getElementById('dialog_alert_button').onclick=null; 
	if( owner.getElementById('dialog_yes_button') ) 
		owner.getElementById('dialog_yes_button').onclick=null;
	if( owner.getElementById('dialog_no_button') ) {
		owner.getElementById('dialog_no_button').onclick=null;
		owner.getElementById('dialog_content_div').innerHTML=''; 
		//owner.getElementById('dialog_content_frame').document.clear(); // This method has been deprecated.
	}
	owner=null;
}
//modified by andy.ten@tom.com
function _crtBackGlassDiv(sWidth ,sHeight ,sTop ,sLeft) //创建全屏半透明 div
{	
	var owner = getTopWinRef();
	var div = owner.createElement('div');
	div.setAttribute('id','dialog_back_glass_div');
	div.style.display='none';
	div.style.background='#000';
	div.style.position='absolute';
	div.style.zIndex=100;
	div.style.top='0%';
	div.style.left='0%';
	/* if(sWidth && isNaN(sWidth) == false)
	    div.style.width= (sWidth) + 'px';
	else
		div.style.width=(screen.width) + 'px';
	div.style.height=(owner.body.clientHeight || owner.documentElement.clientHeight) + 'px'; */
	div.style.width  = "100%";
	div.style.height = "100%";
	
	//背景
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";

	//owner.getElementById('dialog_div').appendChild(div);
	//owner.getElementsByTagName('body').item(0).appendChild(div);
	owner=null;

	return div;
}

//add by zhaoli, 用来解决ie6下,select控件显示的bug
function _crtBackIFrame(sWidth ,sHeight ,sTop ,sLeft){
	var owner = getTopWinRef();
	var div = owner.createElement('iframe');
	div.setAttribute('id','dialog_back_iframe');
	div.style.display='none';
	div.style.background='#000';
	div.style.position='absolute';
	div.style.zIndex=101;
	div.style.top='0%';
	div.style.left='0%';
	//modified by andy.ten@tom.com
	if(sWidth && isNaN(sWidth) == false)
	    div.style.width=(sWidth) + 'px';
	else
		div.style.width=(screen.width) + 'px';
	//div.style.height='100%';
	//modify by yupeng ie下兼容模型 窗口高度大于屏幕高度
	div.style.height=(owner.body.clientHeight || owner.documentElement.clientHeight) + 'px';
	div.style.width  = "100%";
	div.style.height = "100%";
	//背景
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";
	
	owner.getElementById('dialog_div').appendChild(div);
	owner=null;

	return div;
}

function _crtModalDiv(sWidth ,sHeight ,sTop ,sLeft){
	var owner = getTopWinRef();
	var div = owner.createElement('div');
	div.setAttribute('id','dialog_modal_div');
	div.style.display='none';
	div.style.background='#000';
	div.style.position='absolute';
	div.style.zIndex=102;
	div.style.top='0%';
	div.style.left='0%';
	//modified by andy.ten@tom.com
	if(sWidth && isNaN(sWidth) == false)
	    div.style.width=(sWidth) + 'px';
	else
		div.style.width=(screen.width) + 'px';

	//modified by yupeng 
	div.style.height= (owner.body.clientHeight || owner.documentElement.clientHeight) + 'px';
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";
	div.style.width  = "100%";
	div.style.height = "100%";
	
	//背景
	owner.getElementById('dialog_div').appendChild(div);
	owner=null;

	return div;
}

function _crtBackGroudDiv(){//创建边框背景div
	var owner = getTopWinRef();
	var div = owner.createElement('div');
	div.setAttribute('id','dialog_back_groud_div');
	div.style.display='none';
	div.style.background='#666';
	div.style.position='absolute';
	div.style.zIndex=104;

//	owner.getElementById('dialog_div').appendChild(div);
	owner.getElementById(backBoardDivId).appendChild(div);
	owner =null;
}

function _crtHeadDiv(){//创建头div用于拖动
	var owner = getTopWinRef();
	var div = owner.createElement('div'); 
	div.setAttribute('id',headDivId);
	div.style.display='none';
	div.style.background='#dae0ee';
	div.style.position='absolute';
	div.style.zIndex=105;

//	owner.getElementById('dialog_div').appendChild(div);
	owner.getElementById(backBoardDivId).appendChild(div);
	owner=null;
}

function _crtContentDiv(){//创建内容div
	var owner = getTopWinRef();
	var div = owner.createElement('div'); 
	div.setAttribute('id','dialog_content_div');
	div.style.display='none';
	div.style.background='#fff';
	div.style.position='absolute';
	div.style.zIndex=104;
	
//	owner.getElementById('dialog_div').appendChild(div);
	owner.getElementById(backBoardDivId).appendChild(div);
	owner=null;
}

function _crtContentFrame(){//创建iframe
	var owner = getTopWinRef();
	var div = owner.createElement('iframe');
	div.setAttribute('id','dialog_content_frame');
	div.style.display='none';
	div.style.position='absolute';
	div.style.zIndex=105;
	div.style.background='#fff';
	
//	owner.getElementById('dialog_div').appendChild(div);
	owner.getElementById(backBoardDivId).appendChild(div);
	owner=null;
}
function _setSize(width,height, isFrm ,sDivWidth ,sDivHeight){
	var owner = getTopWinRef();
	var bwidth = 8;
	//modified by andy.ten@tom.com
	var ctop = 0;
	var cleft = 0;
	if(sDivWidth)
	{
		ctop = (sDivHeight-height)/3;
		cleft= (sDivWidth-width)/2;	
	}else
	{
		// ctop = (screen.height-height)/3;
		// cleft= (screen.width-width)/2;	
		ctop = ( parent.document.documentElement.clientHeight - height ) / 2;
		cleft = ( parent.document.documentElement.clientWidth - width ) / 2;
	}
	

	var div = owner.getElementById(backBoardDivId);
	if(div){
		div.style.width=width+bwidth*4+'px';
		div.style.height=height+bwidth*4+'px';
		div.style.left=(cleft-bwidth*2)+'px';

		if ( ctop < 0 ) {
			div.style.top=( bwidth * 2 ) + 'px';	
		} else {
			div.style.top=( ctop - bwidth * 2 )+'px';
		}
		
		div.style.position='fixed';
		div.style.zIndex=103;
		div.className = "popup-anim";
	}

	div = owner.getElementById('dialog_back_groud_div');
	if(div){
		div.style.width=width+bwidth*2+'px';
		div.style.height=height+bwidth*2+'px';
		div.style.left=bwidth+'px';
		div.style.top=bwidth+'px';
	}

	div = owner.getElementById(headDivId);
	if(div){
		div.style.width=width+'px';
		div.style.height=labelDivHeight+'px';
		div.style.left=bwidth*2+'px';
		div.style.top=bwidth*2+2+'px';
	}
	
	if( isFrm ){
		div = owner.getElementById('dialog_content_frame');
		if(div){
			div.style.width=width+'px';
			div.style.height=(height-labelDivHeight)+'px';
			div.style.left=bwidth*2+'px';
			div.style.top=(bwidth*2+labelDivHeight-0)+'px';
		}
	}else{
		div = owner.getElementById('dialog_content_div');
		if(div){
			div.style.width=width+'px';
			div.style.height=(height-labelDivHeight)+'px';
			div.style.left=bwidth*2+'px';
			div.style.top=(bwidth*2+labelDivHeight-0)+'px';
		}
	}
	owner=null;
}

function _hide(){
	var owner = getTopWinRef();
	var dialogDiv = owner.getElementById('dialog_div');
	if(dialogDiv){
//		dialogDiv.parentNode.removeChild(dialogDiv);
	
		/* var div = owner.getElementById('dialog_back_glass_div');
		div.style.display='none'; */
		
		div = owner.getElementById('dialog_back_iframe');
		div.style.display='none';
		
		div = owner.getElementById('dialog_modal_div');
		div.style.display='none';
	
		var dialog = owner.getElementById(backBoardDivId);
		dialog.classList.add( 'popup-anim-close' );
		
		setTimeout( function() { 
			dialog.classList.remove( 'popup-anim-close' );
			dialog.style.display='none';
		}, 500);

		div = owner.getElementById('dialog_back_groud_div');
		div.style.display='none';
	
		div = owner.getElementById(headDivId);
		div.style.display='none';
		
		div = owner.getElementById('dialog_content_div');
		div.style.display='none';
		
		div = owner.getElementById('dialog_content_frame');
		div.style.display='none';
		$(dialogDiv).remove();
	}
//	if(typeof(st) == "undefined") {
//		div.src = ""
//	}
	
	
	try{
		bodyCloseFunc();
		_dialogDestroy();
	}catch(e){
		
	}
	owner =null;
	
}

function closePopUpWin( className ) {
	var owner  = getTopWinRef(),
		dialog = owner.getElementById( 'dialog_div' );

	if ( dialog ) {
		div = owner.getElementById('dialog_back_iframe');
		div.style.display='none';
		
		div = owner.getElementById('dialog_modal_div');
		div.style.display='none';
	
		div = owner.getElementById(backBoardDivId);
		div.style.display='none';
		
		div = owner.getElementById('dialog_back_groud_div');
		div.style.display='none';
	
		div = owner.getElementById(headDivId);
		div.style.display='none';
		
		div = owner.getElementById('dialog_content_div');
		div.style.display='none';
		
		div = owner.getElementById('dialog_content_frame');
		div.style.display='none';

		$(dialog).remove();
	}	

	owner =null;
	try {
		_dialogDestroy();
	} catch ( e ) {}

}

/**
 * 弹出框body的关闭事件
 * 兼容以前body上的onbeforeunload事件,关闭兼容模式后,该事件无法生效
 */
function bodyCloseFunc(){
	var dbody = document.body;
	if(dbody){
		var bodyBeforeunload = dbody.getAttribute('onbeforeunload');
		if('string'==typeof bodyBeforeunload){
			if(bodyBeforeunload.indexOf(";")>-1&&bodyBeforeunload.indexOf(";")<bodyBeforeunload.length-1){
				var funs = bodyBeforeunload.split(';');
				for(var idx=0;idx<funs.length;idx++){
					var fun = funs[idx];
						exeFunction(fun);
				}
			} else {
				exeFunction(bodyBeforeunload);
			}
		}
	}
}

/**
 * 执行function
 * @param str 方法名(参数)格式
 */
function exeFunction(str){
	var fname = getFunctionName(str);
	var pname = getFunctionParam(str);
	var _function = null;
	if(fname && eval(fname) instanceof Function){
		if(pname){
			_function = fname+"("+pname+")";
		} else {
			_function = fname+"()";
		}
		eval(_function);
	}
}

/**
 * 获取方法名
 * @param str 方法名(参数)格式
 * @returns 方法名
 */
function getFunctionName(str){
	var fname = null;
	var idx = str.indexOf('(');
	if(idx){
		fname = str.substring(0,idx);
	}
	return fname;
}

/**
 * 获取参数
 * @param str 方法名(参数)格式
 * @returns 参数
 */
function getFunctionParam(str){
	var param = null;
	var pre = str.indexOf('(');
	var suf = str.indexOf(')');
	if(pre&&suf&&suf-pre>1){
		param = str.substring(pre+1,suf);
	}
	return param;
	
}

function _hide2(){
	var owner = getTopWinRef();
	var dialogDiv = owner.getElementById('dialog_div');
	if(dialogDiv){
		dialogDiv.removeNode(true);
	}
	/*var div = owner.getElementById('dialog_back_glass_div');
	div.style.display='none';
	
	div = owner.getElementById('dialog_back_iframe');
	div.style.display='none';
	
	div = owner.getElementById('dialog_modal_div');
	div.style.display='none';

	div = owner.getElementById(backBoardDivId);
	div.style.display='none';
	
	div = owner.getElementById('dialog_back_groud_div');
	div.style.display='none';

	div = owner.getElementById(headDivId);
	div.style.display='none';
	
	div = owner.getElementById('dialog_content_div');
	div.style.display='none'; 
	
	div = owner.getElementById('dialog_content_frame');
	div.style.display='none'; 
	*/
	owner =null;
	_dialogDestroy();
}

function _show(isFrm, title){
	var owner = getTopWinRef();
	var div = owner.getElementById('dialog_back_glass_div');
	setNodeDisplay(div);
	
	div = owner.getElementById('dialog_back_iframe');
	setNodeDisplay(div);
	
	div = owner.getElementById('dialog_modal_div');
	setNodeDisplay(div);

	div = owner.getElementById(backBoardDivId);
	setNodeDisplay(div);
	
	div = owner.getElementById('dialog_back_groud_div');
	setNodeDisplay(div);

	div = owner.getElementById(headDivId);
	setNodeDisplay(div);
	var title = title || '信息提示';
	div.innerHTML = '<span class="dialog-title">' + title + '</span><i class="close-win"></i>';
	
	if( isFrm ){
		div = owner.getElementById('dialog_content_frame');
		setNodeDisplay(div);
	}else{
		div = owner.getElementById('dialog_content_div');
		setNodeDisplay(div);
		div = owner.getElementById('dialog_content_frame');
		div.style.width='0';
		div.style.height='0';
	} 
	owner=null; 
}
/**
 * 设置node显示状态,只有_node参数,display=''
 * 有_none参数,则指定display=_none
 * @param _node
 * @param _none
 */
function setNodeDisplay(_node,_none){
	if(_node){
		if(_none){
			_node.style.display=_node;
		} else {
			_node.style.display='';
		}
	}
}

/**
 * 通用提示框 - 只有一个确定按钮
 * 
 * @param {String} info 提示信息内容
 * @param {Function} callback 点击确定后执行的回调函数
 */
function MyAlert( info, callback, data){
	layer.alert(info,{
		  title: '消息提示',
		  icon: 7,
		  skin: 'layer-ext-moon',
		  yes: function(index){
			  if(typeof callback == 'function') {
				  callback(data);
			  }
			  layer.close(index);
		  },
		  cancel: function(index, layero){ 
			  if(typeof callback == 'function') {
				  callback(data);
			  }
			  layer.close(index);
			  return false; 
			}    
		});
}

/**
 * 确认对话框
 * 
 * @param info
 * 		确认提示信息
 * @param funYes
 * 		确认回调函数
 * @param arrYes
 * 		确认回调参数
 * @param funNo
 * 		取消回调函数
 * @param arrNo
 * 		取消回调参数
 */
function MyConfirm(info,funYes,arrYes,funNo,arrNo){
	layer.confirm(info, {
		icon: 3, title:'确认对话',btn: ['确定','取消'] 
		}, function(index){
			if(typeof funYes == 'function'){
				if(arrYes){funYes.apply(this,arrYes);}
				else funYes();
			}
			layer.close(index);
		}, function(index){
			if(typeof funNo == 'function'){
				if(arrNo){funNo.apply(this,arrNo);}
				else funNo();
			}
			layer.close(index);
		});
}

/**
 * 废弃不用
 * @deprecated
 * @param info
 * @param fun
 */
function MyAlertForFun(info, fun) {
	MyAlert( info, fun );
}

function _dialogInitNotClose(sWidth ,sHeight ,sTop ,sLeft){//初始化
    var owner = null;
	owner = getTopWinRef();
	if( owner.getElementById('dialog_div')==null ){
		var div = owner.createElement('div');
		div.setAttribute('id','dialog_div');
		//owner.getElementsByTagName('body').item(0).appendChild(div);

		// 创建背景板,装填BackGroupDiv,HeadDiv,ContentDiv,ContentFrame
		// 用户整体拖动
		/* var backBoardDiv = owner.createElement('div');
		backBoardDiv.setAttribute('id',backBoardDivId);
		div.appendChild(backBoardDiv); */
		
		_crtBackGlassDiv(sWidth ,sHeight ,sTop ,sLeft);
		_crtBackIFrame(sWidth ,sHeight ,sTop ,sLeft); 
		_crtModalDiv(sWidth ,sHeight ,sTop ,sLeft);
		_crtBackGroudDiv();
		_crtContentDiv();
		_crtContentFrame();
	} 
	if(owner.getElementById('dialog_modal_div')!=null){
		owner.getElementById('dialog_modal_div').onclick = function(){}
	}
	owner =null;
}


function MyUnCloseConfirm(info,funYes,arrYes,funNo,arrNo){
	var owner = getTopWinRef();
	try{
		_dialogInitNotClose();
	owner.getElementById('dialog_content_div').innerHTML='\
	<div class="dialog_content_box">\
	 <!--div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div-->\
	 <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	 <div style="padding:2px;text-align:center;background:#D0BFA1;">\
	  <input id="dialog_yes_button" type="button" value=" 确定 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	  <input id="dialog_no_button" type="button" value=" 取消 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	 </div>\
	</div>';
	owner.getElementById('dialog_alert_info').innerHTML=info;
	owner.getElementById('dialog_yes_button').onclick=function(){
	try{
	 if(funYes&&arrYes){
	  funYes.apply(this,arrYes);
	 }else if(funYes){
	  funYes();
	 }
	}finally{
	 _hide();
	}
	};
	owner.getElementById('dialog_no_button').onclick=function(){
	try{
	 if(funNo&&arrNo){
	  funNo.apply(this,arrNo);
	 }else if(funNo){
	  funNo();
	 }
	}finally{
	 _hide();
	}
	};
	
	_setSize(300, 200); 
	_show();
	}catch(e){
	alert('MyAlert : '+ e.name+'='+e.message);
	 _hide();
	}finally{
	owner=null;
	}
}
/***********add by wangsw 20130524 增加新的确认对话框弹出层提示方法(点击层外侧不关闭层)end************/

/***********add by wangsw 20130524 增加新的确认对话框弹出层提示方法(点击层外侧不关闭层)begin************/
function MyUnCloseConfirm1(info,funYes,arrYes,funNo,arrNo,cancel,arrCancel){
	var owner = getTopWinRef();
	try{
		_dialogInitNotClose();
	owner.getElementById('dialog_content_div').innerHTML='\
	<div class="dialog_content_box">\
	 <!--div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div-->\
	 <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	 <div style="padding:2px;text-align:center;background:#D0BFA1;">\
	  <input id="dialog_yes_button" type="button" value=" 是 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	  <input id="dialog_no_button" type="button" value=" 否 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		<input id="dialog_cancel_button" type="button" value=" 取消 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	 </div>\
	</div>';
	owner.getElementById('dialog_alert_info').innerHTML=info;
	owner.getElementById('dialog_yes_button').onclick=function(){
	try{
	 if(funYes&&arrYes){
	  funYes.apply(this,arrYes);
	 }else if(funYes){
	  funYes();
	 }
	}finally{
	 _hide();
	}
	};
	
	owner.getElementById('dialog_no_button').onclick=function(){
	try{
	 if(funNo&&arrNo){
	  funNo.apply(this,arrNo);
	 }else if(funNo){
	  funNo();
	 }
	}finally{
	 _hide();
	}
	};
	
	owner.getElementById('dialog_cancel_button').onclick=function(){
		try{
		 if(cancel&&arrCancel){
			 cancel.apply(this,arrCancel);
		 }else if(cancel){
			 cancel();
		 }
		}finally{
		 _hide();
		}
		};
	
	_setSize(300, 200); 
	_show();
	}catch(e){
	alert('MyAlert : '+ e.name+'='+e.message);
	}finally{
	owner=null;
	}
}
/***********add by wangsw 20130524 增加新的确认对话框弹出层提示方法(点击层外侧不关闭层)end************/

/**
 * added by andy.ten@tom.com
 */
function MyDivConfirm(info,funYes,arrYes,funNo,arrNo)
{
    divDialogFlag = true;
	var owner = getTopWinRef();
	try
	{
		_dialogInit(800,500); 
		owner.getElementById('dialog_content_div').innerHTML='\
		<div class="dialog_content_box">\
	 	<!--div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  	<b>信息</b>\
	 	</div-->\
	 	<div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	 	<div style="padding:2px;text-align:center;background:#D0BFA1;">\
	  	<input id="dialog_yes_button" type="button" value=" 确定 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	  	<input id="dialog_no_button" type="button" value=" 取消 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	 	</div>\
		</div>';
		owner.getElementById('dialog_alert_info').innerHTML=info;
		owner.getElementById('dialog_yes_button').onclick=function()
		{
			try
			{
		 		if(funYes&&arrYes)
		 		{
		  			funYes.apply(this,arrYes);
		 		}else if(funYes)
		 		{
		  			funYes();
		 		}
			}finally{
		 		_hide();
			}
		};
	owner.getElementById('dialog_no_button').onclick=function()
	{
		try
		{
		 if(funNo&&arrNo){
		  funNo.apply(this,arrNo);
		 }else if(funNo){
		  funNo();
		 }
		}finally{
		 _hide();
		}
	};
	_setSize(300, 100, '' ,900,500); 
	_show();
	}catch(e){
	alert('MyAlert : '+ e.name+'='+e.message);
	}finally{
	owner=null;
	//divDialogFlag = false;
	}
}

/**
 * added by andy.ten@tom.com
 */
function MyDivAlert(info){
 divDialogFlag = true;
 var owner = getTopWinRef();
 try{
  	_dialogInit(800,500);  
  	owner.getElementById('dialog_content_div').innerHTML='\
    <div class="dialog_content_box">\
     <!--div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
      <b>信息</b>\
     </div-->\
     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
     <div class="button-box">\
      <input class="u-button u-reset" id="dialog_alert_button" type="button" value="确定"/>\
     </div>\
    </div>';
  owner.getElementById('dialog_alert_info').innerHTML=info;
  owner.getElementById('dialog_alert_button').onclick=_hide;
  
  _setSize(300, 200, '' ,800,500); 
  _show();
 }catch(e){
  alert('MyAlert : '+ e.name+'='+e.message);
 }finally{
  owner=null;
  //divDialogFlag = false;
 }
}

/**
 * 系统公用弹窗
 * 
 * @param url
 * 		页面路径
 * @param width
 * 		弹窗宽度
 * @param height
 * 		弹窗高度
 * @param title
 * 		弹窗标题
 */
function OpenHtmlWindow(url,width,height,title) {
	var owner = getTopWinRef();
	title = title || '信息提示';
	try{
		$(owner).keyup(function(e) {
			if ( e.keyCode == 27 ) {
				_hide();
				$(owner).unbind( 'keyup' );
			}
		});

		_dialogInit();
		_setSize(width, height, true);
		_show(true, title);
		owner.getElementById('dialog_content_frame').src = url;
	}catch(e){
		alert('MyAlert : '+ e.name+'='+e.message);
	}finally{
		owner=null;
	}
}

function OpenHtmlWindow2(url,width,height,st){
	 var owner = getTopWinRef();
	 try{
	  _dialogInit();
	  _setSize(width, height, true);
	  _show(true);
	  if(!st) {
	  	owner.getElementById('dialog_content_frame').src = url;
	  }
	 }catch(e){
	  alert('MyAlert : '+ e.name+'='+e.message);
	 }finally{
	  owner=null;
	 }
}

jQuery(function($) {
	$('.u-main').on('click', '.close-win', function() {
		_hide && _hide();
	});

	if ( ! _resizeEvtGl.gl ) {
		/* _resizeEvtGl.gl   = $(window);
		_resizeEvtGl.gl.resize(function() {
			_resizeEvtGl.wraper = $('.u-wrap');
			_resizeEvtGl.bgDiv = $('#dialog_back_glass_div');
			_resizeEvtGl.bgIframe = $('#dialog_back_iframe');
			_resizeEvtGl.modal = $('#dialog_modal_div');

			var width  = _resizeEvtGl.wraper.width(),
				height = _resizeEvtGl.wraper.height();

			_resizeEvtGl.bgDiv.width(width);
			_resizeEvtGl.bgDiv.height(height);
			_resizeEvtGl.bgIframe.width(width);
			_resizeEvtGl.bgIframe.height(height);
			_resizeEvtGl.modal.width(width);
			_resizeEvtGl.modal.height(height);
		}); */
	}
});
/**
 * Showing a tip when preloading
 */
var Loading = {
	parent: window.parent.document.getElementsByTagName('body'),

	alert: function(parentEl) {
		var loading = document.createElement('div'),
			mask    = document.createElement('div'),
			body;
			
		loading.classList.add('page-loading');
		loading.innerHTML = '加载中, 请稍后...';
		mask.classList.add('loading-mask');
		body = this.parent ? this.parent[0] : document.body;

		parentEl = parentEl || body;
		mask.style.width = body.scrollWidth;
		parentEl.appendChild(mask);
		parentEl.appendChild(loading);
		return {
			load: loading,
			mask: mask
		};	
	},

	close: function(loadEl, parentEl) {
		if (! loadEl || typeof loadEl != 'object' || loadEl.constructor != Object) return;
		body = this.parent ? this.parent[0] : document.body;

		parentEl = parentEl || body;
		parentEl.removeChild(loadEl.load);
		parentEl.removeChild(loadEl.mask);
	}
};

/*HanXian Wang
 * alert重写
 */
uAlert = function(txt, funYes,arrYes)  
{  	
	var shield = document.createElement("DIV"),
		doc    = parent.document || top.document;  
	shield.id = "shield";  
	shield.className = "shield"; 
    /* shield.style.position = "absolute";  
    shield.style.left = "0px";  
    shield.style.top = "0px";  
    shield.style.width = "100%";  
    shield.style.height = doc.body.scrollHeight+"px";  
    shield.style.background = "#333";  
    shield.style.textAlign = "center";  
    shield.style.zIndex = "10000";  
    shield.style.filter = "alpha(opacity=0)";   */
    var alertFram = document.createElement("DIV");  
	alertFram.id="alertFram"; 
    alertFram.style.position = "absolute";  
    alertFram.style.left = "50%";  
    alertFram.style.top = "50%";  
    alertFram.style.marginLeft = "-225px";  
    alertFram.style.marginTop = "-75px";  
    alertFram.style.width = "450px";  
    alertFram.style.height = "150px";  
    alertFram.style.background = "#ccc";  
    alertFram.style.textAlign = "center";  
    alertFram.style.lineHeight = "150px";  
    alertFram.style.zIndex = "10001";  
    strHtml = "<ul class=\"alert-box\">\n";  
    strHtml += " <li class=\"alert-header\"><i></i><span>信息</span><i class=\"close-dv\" onclick='doOk()'></i></li>\n";  
    strHtml += " <li>"+txt+"</li>\n";  
    strHtml += " <li><input type=\"button\" class=\"u-button u-query\" value=\"确 定\" onclick=\"doOk()\" /></li>\n";  
    strHtml += "</ul>\n";  
    alertFram.innerHTML = strHtml;  
    doc.body.appendChild(alertFram);  
    doc.body.appendChild(shield);  
    var c = 0;  
    this.doAlpha = function(){
        if (c++ > 20){clearInterval(ad);return 0;}  
        shield.style.filter = "alpha(opacity="+c+");";  
    } 
    var ad = setInterval("doAlpha()",5);  
    
    parent.window.doOk = function() {  
    	if(funYes&&arrYes){
    		funYes.apply(this,arrYes);
    	}else if(funYes){
    		funYes();
    	}
        alertFram.style.display = "none";  
        shield.style.display = "none";  
    }  
    alertFram.focus();  
    doc.body.onselectstart = function(){return false;};  
}  

/*HanXian Wang
 * confirm重写
 */
uConfirm = function(txt, funYes,arrYes,funNo,arrNo)  
{
	if(funYes != undefined) {
	    var shield = document.createElement("DIV");  
	    shield.id = "shield";  
	    shield.style.position = "absolute";  
	    shield.style.left = "0px";  
	    shield.style.top = "0px";  
	    shield.style.width = "100%";  
	    shield.style.height = document.body.scrollHeight+"px";  
	    shield.style.background = "#333";  
	    shield.style.textAlign = "center";  
	    shield.style.zIndex = "10000";  
	    shield.style.filter = "alpha(opacity=0)";  
	    var alertFram = document.createElement("DIV");  
	    alertFram.id="alertFram";  
	    alertFram.style.position = "absolute";  
	    alertFram.style.left = "50%";  
	    alertFram.style.top = "50%";  
	    alertFram.style.marginLeft = "-225px";  
	    alertFram.style.marginTop = "-75px";  
	    alertFram.style.width = "450px";  
	    alertFram.style.height = "150px";  
	    alertFram.style.background = "#ccc";  
	    alertFram.style.textAlign = "center";  
	    alertFram.style.lineHeight = "150px";  
	    alertFram.style.zIndex = "10001";  
	    strHtml = "<ul style=\"list-style:none;margin:0px;padding:0px;width:100%\">\n";  
	    strHtml += " <li style=\"background:#f8f8ff;text-align:left;padding-left:20px;font-size:14px;font-weight:bold;height:25px;line-height:25px;border:1px solid #c0c0c0;\">确认</li>\n";  
	    strHtml += " <li style=\"background:#fff;text-align:center;font-size:12px;height:120px;line-height:120px;border-left:1px solid #c0c0c0;border-right:1px solid #c0c0c0;\">"+txt+"</li>\n";  
	    strHtml += " <li style=\"background:#F8F8FF;text-align:center;font-weight:bold;height:30px;line-height:30px; border:1px solid #c0c0c0;\"><input type=\"button\" class=\"normal_btn\" value=\"确 定\" onclick=\"doOk()\" /><input type=\"button\" class=\"normal_btn\" value=\"取 消\" onclick=\"doCal()\" /></li>\n";  
	    strHtml += "</ul>\n";  
	    alertFram.innerHTML = strHtml;  
	    document.body.appendChild(alertFram);  
	    document.body.appendChild(shield);  
	    var c = 0;  
	    this.doAlpha = function(){
	        if (c++ > 20){clearInterval(ad);return 0;}  
	        shield.style.filter = "alpha(opacity="+c+");";  
	    } 
	    var ad = setInterval("doAlpha()",5);  
	    
	    this.doOk = function(){  
	    	if(funYes&&arrYes){
	    		funYes.apply(this,arrYes);
	    	}else if(funYes){
	    		funYes();
	    	}
	        alertFram.style.display = "none";  
	        shield.style.display = "none";  
	    }  
	    this.doCal = function(){
	    	if(funNo&&arrNo){
	    		funNo.apply(this,arrNo);
	    	}else if(funNo){
	    		funNo();
	    	}
	        alertFram.style.display = "none";  
	        shield.style.display = "none";  
	    }  
	    alertFram.focus();  
	    document.body.onselectstart = function(){return false;};  
	} else {
		return window.confirm(txt) ;
	}
}  

/**
 * 'Layer' This plugin from the 3 third party provides a variety of popup windows.
 */
 ;!function(e,t){"use strict";var i,n,a=e.layui&&layui.define,o={getPath:function(){var e=document.scripts,t=e[e.length-1],i=t.src;if(!t.getAttribute("merge"))return i.substring(0,i.lastIndexOf("/")+1)}(),config:{},end:{},minIndex:0,minLeft:[],btn:["&#x786E;&#x5B9A;","&#x53D6;&#x6D88;"],type:["dialog","page","iframe","loading","tips"]},r={v:"3.0.3",ie:function(){var t=navigator.userAgent.toLowerCase();return!!(e.ActiveXObject||"ActiveXObject"in e)&&((t.match(/msie\s(\d+)/)||[])[1]||"11")}(),index:e.layer&&e.layer.v?1e5:0,path:o.getPath,config:function(e,t){return e=e||{},r.cache=o.config=i.extend({},o.config,e),r.path=o.config.path||r.path,"string"==typeof e.extend&&(e.extend=[e.extend]),o.config.path&&r.ready(),e.extend?(a?layui.addcss("modules/layer/"+e.extend):r.link("../img/skin/"+e.extend),this):this},link:function(t,n,a){if(r.path){var o=i("head")[0],s=document.createElement("link");"string"==typeof n&&(a=n);var l=(a||t).replace(/\.|\//g,""),f="layuicss-"+l,c=0;s.rel="stylesheet",s.href=r.path+t,s.id=f,i("#"+f)[0]||o.appendChild(s),"function"==typeof n&&!function u(){return++c>80?e.console&&console.error("layer.css: Invalid"):void(1989===parseInt(i("#"+f).css("width"))?n():setTimeout(u,100))}()}},ready:function(e){var t="skinlayercss",i="303";return a?layui.addcss("modules/layer/default/layer.css?v="+r.v+i,e,t):r.link("../img/skin/default/layer.css?v="+r.v+i,e,t),this},alert:function(e,t,n){var a="function"==typeof t;return a&&(n=t),r.open(i.extend({content:e,yes:n},a?{}:t))},confirm:function(e,t,n,a){var s="function"==typeof t;return s&&(a=n,n=t),r.open(i.extend({content:e,btn:o.btn,yes:n,btn2:a},s?{}:t))},msg:function(e,n,a){var s="function"==typeof n,f=o.config.skin,c=(f?f+" "+f+"-msg":"")||"layui-layer-msg",u=l.anim.length-1;return s&&(a=n),r.open(i.extend({content:e,time:3e3,shade:!1,skin:c,title:!1,closeBtn:!1,btn:!1,resize:!1,end:a},s&&!o.config.skin?{skin:c+" layui-layer-hui",anim:u}:function(){return n=n||{},(n.icon===-1||n.icon===t&&!o.config.skin)&&(n.skin=c+" "+(n.skin||"layui-layer-hui")),n}()))},load:function(e,t){return r.open(i.extend({type:3,icon:e||0,resize:!1,shade:.01},t))},tips:function(e,t,n){return r.open(i.extend({type:4,content:[e,t],closeBtn:!1,time:3e3,shade:!1,resize:!1,fixed:!1,maxWidth:210},n))}},s=function(e){var t=this;t.index=++r.index,t.config=i.extend({},t.config,o.config,e),document.body?t.creat():setTimeout(function(){t.creat()},30)};s.pt=s.prototype;var l=["layui-layer",".layui-layer-title",".layui-layer-main",".layui-layer-dialog","layui-layer-iframe","layui-layer-content","layui-layer-btn","layui-layer-close"];l.anim=["layer-anim","layer-anim-01","layer-anim-02","layer-anim-03","layer-anim-04","layer-anim-05","layer-anim-06"],s.pt.config={type:0,shade:.3,fixed:!0,move:l[1],title:"&#x4FE1;&#x606F;",offset:"auto",area:"auto",closeBtn:1,time:0,zIndex:19891014,maxWidth:360,anim:0,isOutAnim:!0,icon:-1,moveType:1,resize:!0,scrollbar:!0,tips:2},s.pt.vessel=function(e,t){var n=this,a=n.index,r=n.config,s=r.zIndex+a,f="object"==typeof r.title,c=r.maxmin&&(1===r.type||2===r.type),u=r.title?'<div class="layui-layer-title" style="'+(f?r.title[1]:"")+'">'+(f?r.title[0]:r.title)+"</div>":"";return r.zIndex=s,t([r.shade?'<div class="layui-layer-shade" id="layui-layer-shade'+a+'" times="'+a+'" style="'+("z-index:"+(s-1)+"; background-color:"+(r.shade[1]||"#000")+"; opacity:"+(r.shade[0]||r.shade)+"; filter:alpha(opacity="+(100*r.shade[0]||100*r.shade)+");")+'"></div>':"",'<div class="'+l[0]+(" layui-layer-"+o.type[r.type])+(0!=r.type&&2!=r.type||r.shade?"":" layui-layer-border")+" "+(r.skin||"")+'" id="'+l[0]+a+'" type="'+o.type[r.type]+'" times="'+a+'" showtime="'+r.time+'" conType="'+(e?"object":"string")+'" style="z-index: '+s+"; width:"+r.area[0]+";height:"+r.area[1]+(r.fixed?"":";position:absolute;")+'">'+(e&&2!=r.type?"":u)+'<div id="'+(r.id||"")+'" class="layui-layer-content'+(0==r.type&&r.icon!==-1?" layui-layer-padding":"")+(3==r.type?" layui-layer-loading"+r.icon:"")+'">'+(0==r.type&&r.icon!==-1?'<i class="layui-layer-ico layui-layer-ico'+r.icon+'"></i>':"")+(1==r.type&&e?"":r.content||"")+'</div><span class="layui-layer-setwin">'+function(){var e=c?'<a class="layui-layer-min" href="javascript:;"><cite></cite></a><a class="layui-layer-ico layui-layer-max" href="javascript:;"></a>':"";return r.closeBtn&&(e+='<a class="layui-layer-ico '+l[7]+" "+l[7]+(r.title?r.closeBtn:4==r.type?"1":"2")+'" href="javascript:;"></a>'),e}()+"</span>"+(r.btn?function(){var e="";"string"==typeof r.btn&&(r.btn=[r.btn]);for(var t=0,i=r.btn.length;t<i;t++)e+='<a class="'+l[6]+t+'">'+r.btn[t]+"</a>";return'<div class="'+l[6]+" layui-layer-btn-"+(r.btnAlign||"")+'">'+e+"</div>"}():"")+(r.resize?'<span class="layui-layer-resize"></span>':"")+"</div>"],u,i('<div class="layui-layer-move"></div>')),n},s.pt.creat=function(){var e=this,t=e.config,a=e.index,s=t.content,f="object"==typeof s,c=i("body");if(!t.id||!i("#"+t.id)[0]){switch("string"==typeof t.area&&(t.area="auto"===t.area?["",""]:[t.area,""]),t.shift&&(t.anim=t.shift),6==r.ie&&(t.fixed=!1),t.type){case 0:t.btn="btn"in t?t.btn:o.btn[0],r.closeAll("dialog");break;case 2:var s=t.content=f?t.content:[t.content,"auto"];t.content='<iframe scrolling="'+(t.content[1]||"auto")+'" allowtransparency="true" id="'+l[4]+a+'" name="'+l[4]+a+'" onload="this.className=\'\';" class="layui-layer-load" frameborder="0" src="'+t.content[0]+'"></iframe>';break;case 3:delete t.title,delete t.closeBtn,t.icon===-1&&0===t.icon,r.closeAll("loading");break;case 4:f||(t.content=[t.content,"body"]),t.follow=t.content[1],t.content=t.content[0]+'<i class="layui-layer-TipsG"></i>',delete t.title,t.tips="object"==typeof t.tips?t.tips:[t.tips,!0],t.tipsMore||r.closeAll("tips")}e.vessel(f,function(n,r,u){c.append(n[0]),f?function(){2==t.type||4==t.type?function(){i("body").append(n[1])}():function(){s.parents("."+l[0])[0]||(s.data("display",s.css("display")).show().addClass("layui-layer-wrap").wrap(n[1]),i("#"+l[0]+a).find("."+l[5]).before(r))}()}():c.append(n[1]),i(".layui-layer-move")[0]||c.append(o.moveElem=u),e.layero=i("#"+l[0]+a),t.scrollbar||l.html.css("overflow","hidden").attr("layer-full",a)}).auto(a),2==t.type&&6==r.ie&&e.layero.find("iframe").attr("src",s[0]),4==t.type?e.tips():e.offset(),t.fixed&&n.on("resize",function(){e.offset(),(/^\d+%$/.test(t.area[0])||/^\d+%$/.test(t.area[1]))&&e.auto(a),4==t.type&&e.tips()}),t.time<=0||setTimeout(function(){r.close(e.index)},t.time),e.move().callback(),l.anim[t.anim]&&e.layero.addClass(l.anim[t.anim]),t.isOutAnim&&e.layero.data("isOutAnim",!0)}},s.pt.auto=function(e){function t(e){e=s.find(e),e.height(f[1]-c-u-2*(0|parseFloat(e.css("padding-top"))))}var a=this,o=a.config,s=i("#"+l[0]+e);""===o.area[0]&&o.maxWidth>0&&(r.ie&&r.ie<8&&o.btn&&s.width(s.innerWidth()),s.outerWidth()>o.maxWidth&&s.width(o.maxWidth));var f=[s.innerWidth(),s.innerHeight()],c=s.find(l[1]).outerHeight()||0,u=s.find("."+l[6]).outerHeight()||0;switch(o.type){case 2:t("iframe");break;default:""===o.area[1]?o.fixed&&f[1]>=n.height()&&(f[1]=n.height(),t("."+l[5])):t("."+l[5])}return a},s.pt.offset=function(){var e=this,t=e.config,i=e.layero,a=[i.outerWidth(),i.outerHeight()],o="object"==typeof t.offset;e.offsetTop=(n.height()-a[1])/2,e.offsetLeft=(n.width()-a[0])/2,o?(e.offsetTop=t.offset[0],e.offsetLeft=t.offset[1]||e.offsetLeft):"auto"!==t.offset&&("t"===t.offset?e.offsetTop=0:"r"===t.offset?e.offsetLeft=n.width()-a[0]:"b"===t.offset?e.offsetTop=n.height()-a[1]:"l"===t.offset?e.offsetLeft=0:"lt"===t.offset?(e.offsetTop=0,e.offsetLeft=0):"lb"===t.offset?(e.offsetTop=n.height()-a[1],e.offsetLeft=0):"rt"===t.offset?(e.offsetTop=0,e.offsetLeft=n.width()-a[0]):"rb"===t.offset?(e.offsetTop=n.height()-a[1],e.offsetLeft=n.width()-a[0]):e.offsetTop=t.offset),t.fixed||(e.offsetTop=/%$/.test(e.offsetTop)?n.height()*parseFloat(e.offsetTop)/100:parseFloat(e.offsetTop),e.offsetLeft=/%$/.test(e.offsetLeft)?n.width()*parseFloat(e.offsetLeft)/100:parseFloat(e.offsetLeft),e.offsetTop+=n.scrollTop(),e.offsetLeft+=n.scrollLeft()),i.attr("minLeft")&&(e.offsetTop=n.height()-(i.find(l[1]).outerHeight()||0),e.offsetLeft=i.css("left")),i.css({top:e.offsetTop,left:e.offsetLeft})},s.pt.tips=function(){var e=this,t=e.config,a=e.layero,o=[a.outerWidth(),a.outerHeight()],r=i(t.follow);r[0]||(r=i("body"));var s={width:r.outerWidth(),height:r.outerHeight(),top:r.offset().top,left:r.offset().left},f=a.find(".layui-layer-TipsG"),c=t.tips[0];t.tips[1]||f.remove(),s.autoLeft=function(){s.left+o[0]-n.width()>0?(s.tipLeft=s.left+s.width-o[0],f.css({right:12,left:"auto"})):s.tipLeft=s.left},s.where=[function(){s.autoLeft(),s.tipTop=s.top-o[1]-10,f.removeClass("layui-layer-TipsB").addClass("layui-layer-TipsT").css("border-right-color",t.tips[1])},function(){s.tipLeft=s.left+s.width+10,s.tipTop=s.top,f.removeClass("layui-layer-TipsL").addClass("layui-layer-TipsR").css("border-bottom-color",t.tips[1])},function(){s.autoLeft(),s.tipTop=s.top+s.height+10,f.removeClass("layui-layer-TipsT").addClass("layui-layer-TipsB").css("border-right-color",t.tips[1])},function(){s.tipLeft=s.left-o[0]-10,s.tipTop=s.top,f.removeClass("layui-layer-TipsR").addClass("layui-layer-TipsL").css("border-bottom-color",t.tips[1])}],s.where[c-1](),1===c?s.top-(n.scrollTop()+o[1]+16)<0&&s.where[2]():2===c?n.width()-(s.left+s.width+o[0]+16)>0||s.where[3]():3===c?s.top-n.scrollTop()+s.height+o[1]+16-n.height()>0&&s.where[0]():4===c&&o[0]+16-s.left>0&&s.where[1](),a.find("."+l[5]).css({"background-color":t.tips[1],"padding-right":t.closeBtn?"30px":""}),a.css({left:s.tipLeft-(t.fixed?n.scrollLeft():0),top:s.tipTop-(t.fixed?n.scrollTop():0)})},s.pt.move=function(){var e=this,t=e.config,a=i(document),s=e.layero,l=s.find(t.move),f=s.find(".layui-layer-resize"),c={};return t.move&&l.css("cursor","move"),l.on("mousedown",function(e){e.preventDefault(),t.move&&(c.moveStart=!0,c.offset=[e.clientX-parseFloat(s.css("left")),e.clientY-parseFloat(s.css("top"))],o.moveElem.css("cursor","move").show())}),f.on("mousedown",function(e){e.preventDefault(),c.resizeStart=!0,c.offset=[e.clientX,e.clientY],c.area=[s.outerWidth(),s.outerHeight()],o.moveElem.css("cursor","se-resize").show()}),a.on("mousemove",function(i){if(c.moveStart){var a=i.clientX-c.offset[0],o=i.clientY-c.offset[1],l="fixed"===s.css("position");if(i.preventDefault(),c.stX=l?0:n.scrollLeft(),c.stY=l?0:n.scrollTop(),!t.moveOut){var f=n.width()-s.outerWidth()+c.stX,u=n.height()-s.outerHeight()+c.stY;a<c.stX&&(a=c.stX),a>f&&(a=f),o<c.stY&&(o=c.stY),o>u&&(o=u)}s.css({left:a,top:o})}if(t.resize&&c.resizeStart){var a=i.clientX-c.offset[0],o=i.clientY-c.offset[1];i.preventDefault(),r.style(e.index,{width:c.area[0]+a,height:c.area[1]+o}),c.isResize=!0,t.resizing&&t.resizing(s)}}).on("mouseup",function(e){c.moveStart&&(delete c.moveStart,o.moveElem.hide(),t.moveEnd&&t.moveEnd(s)),c.resizeStart&&(delete c.resizeStart,o.moveElem.hide())}),e},s.pt.callback=function(){function e(){var e=a.cancel&&a.cancel(t.index,n);e===!1||r.close(t.index)}var t=this,n=t.layero,a=t.config;t.openLayer(),a.success&&(2==a.type?n.find("iframe").on("load",function(){a.success(n,t.index)}):a.success(n,t.index)),6==r.ie&&t.IE6(n),n.find("."+l[6]).children("a").on("click",function(){var e=i(this).index();if(0===e)a.yes?a.yes(t.index,n):a.btn1?a.btn1(t.index,n):r.close(t.index);else{var o=a["btn"+(e+1)]&&a["btn"+(e+1)](t.index,n);o===!1||r.close(t.index)}}),n.find("."+l[7]).on("click",e),a.shadeClose&&i("#layui-layer-shade"+t.index).on("click",function(){r.close(t.index)}),n.find(".layui-layer-min").on("click",function(){var e=a.min&&a.min(n);e===!1||r.min(t.index,a)}),n.find(".layui-layer-max").on("click",function(){i(this).hasClass("layui-layer-maxmin")?(r.restore(t.index),a.restore&&a.restore(n)):(r.full(t.index,a),setTimeout(function(){a.full&&a.full(n)},100))}),a.end&&(o.end[t.index]=a.end)},o.reselect=function(){i.each(i("select"),function(e,t){var n=i(this);n.parents("."+l[0])[0]||1==n.attr("layer")&&i("."+l[0]).length<1&&n.removeAttr("layer").show(),n=null})},s.pt.IE6=function(e){i("select").each(function(e,t){var n=i(this);n.parents("."+l[0])[0]||"none"===n.css("display")||n.attr({layer:"1"}).hide(),n=null})},s.pt.openLayer=function(){var e=this;r.zIndex=e.config.zIndex,r.setTop=function(e){var t=function(){r.zIndex++,e.css("z-index",r.zIndex+1)};return r.zIndex=parseInt(e[0].style.zIndex),e.on("mousedown",t),r.zIndex}},o.record=function(e){var t=[e.width(),e.height(),e.position().top,e.position().left+parseFloat(e.css("margin-left"))];e.find(".layui-layer-max").addClass("layui-layer-maxmin"),e.attr({area:t})},o.rescollbar=function(e){l.html.attr("layer-full")==e&&(l.html[0].style.removeProperty?l.html[0].style.removeProperty("overflow"):l.html[0].style.removeAttribute("overflow"),l.html.removeAttr("layer-full"))},e.layer=r,r.getChildFrame=function(e,t){return t=t||i("."+l[4]).attr("times"),i("#"+l[0]+t).find("iframe").contents().find(e)},r.getFrameIndex=function(e){return i("#"+e).parents("."+l[4]).attr("times")},r.iframeAuto=function(e){if(e){var t=r.getChildFrame("html",e).outerHeight(),n=i("#"+l[0]+e),a=n.find(l[1]).outerHeight()||0,o=n.find("."+l[6]).outerHeight()||0;n.css({height:t+a+o}),n.find("iframe").css({height:t})}},r.iframeSrc=function(e,t){i("#"+l[0]+e).find("iframe").attr("src",t)},r.style=function(e,t,n){var a=i("#"+l[0]+e),r=a.find(".layui-layer-content"),s=a.attr("type"),f=a.find(l[1]).outerHeight()||0,c=a.find("."+l[6]).outerHeight()||0;a.attr("minLeft");s!==o.type[3]&&s!==o.type[4]&&(n||(parseFloat(t.width)<=260&&(t.width=260),parseFloat(t.height)-f-c<=64&&(t.height=64+f+c)),a.css(t),c=a.find("."+l[6]).outerHeight(),s===o.type[2]?a.find("iframe").css({height:parseFloat(t.height)-f-c}):r.css({height:parseFloat(t.height)-f-c-parseFloat(r.css("padding-top"))-parseFloat(r.css("padding-bottom"))}))},r.min=function(e,t){var a=i("#"+l[0]+e),s=a.find(l[1]).outerHeight()||0,f=a.attr("minLeft")||181*o.minIndex+"px",c=a.css("position");o.record(a),o.minLeft[0]&&(f=o.minLeft[0],o.minLeft.shift()),a.attr("position",c),r.style(e,{width:180,height:s,left:f,top:n.height()-s,position:"fixed",overflow:"hidden"},!0),a.find(".layui-layer-min").hide(),"page"===a.attr("type")&&a.find(l[4]).hide(),o.rescollbar(e),a.attr("minLeft")||o.minIndex++,a.attr("minLeft",f)},r.restore=function(e){var t=i("#"+l[0]+e),n=t.attr("area").split(",");t.attr("type");r.style(e,{width:parseFloat(n[0]),height:parseFloat(n[1]),top:parseFloat(n[2]),left:parseFloat(n[3]),position:t.attr("position"),overflow:"visible"},!0),t.find(".layui-layer-max").removeClass("layui-layer-maxmin"),t.find(".layui-layer-min").show(),"page"===t.attr("type")&&t.find(l[4]).show(),o.rescollbar(e)},r.full=function(e){var t,a=i("#"+l[0]+e);o.record(a),l.html.attr("layer-full")||l.html.css("overflow","hidden").attr("layer-full",e),clearTimeout(t),t=setTimeout(function(){var t="fixed"===a.css("position");r.style(e,{top:t?0:n.scrollTop(),left:t?0:n.scrollLeft(),width:n.width(),height:n.height()},!0),a.find(".layui-layer-min").hide()},100)},r.title=function(e,t){var n=i("#"+l[0]+(t||r.index)).find(l[1]);n.html(e)},r.close=function(e){var t=i("#"+l[0]+e),n=t.attr("type"),a="layer-anim-close";if(t[0]){var s="layui-layer-wrap",f=function(){if(n===o.type[1]&&"object"===t.attr("conType")){t.children(":not(."+l[5]+")").remove();for(var a=t.find("."+s),r=0;r<2;r++)a.unwrap();a.css("display",a.data("display")).removeClass(s)}else{if(n===o.type[2])try{var f=i("#"+l[4]+e)[0];f.contentWindow.document.write(""),f.contentWindow.close(),t.find("."+l[5])[0].removeChild(f)}catch(c){}t[0].innerHTML="",t.remove()}"function"==typeof o.end[e]&&o.end[e](),delete o.end[e]};t.data("isOutAnim")&&t.addClass(a),i("#layui-layer-moves, #layui-layer-shade"+e).remove(),6==r.ie&&o.reselect(),o.rescollbar(e),t.attr("minLeft")&&(o.minIndex--,o.minLeft.push(t.attr("minLeft"))),r.ie&&r.ie<10||!t.data("isOutAnim")?f():setTimeout(function(){f()},200)}},r.closeAll=function(e){i.each(i("."+l[0]),function(){var t=i(this),n=e?t.attr("type")===e:1;n&&r.close(t.attr("times")),n=null})};var f=r.cache||{},c=function(e){return f.skin?" "+f.skin+" "+f.skin+"-"+e:""};r.prompt=function(e,t){var a="";if(e=e||{},"function"==typeof e&&(t=e),e.area){var o=e.area;a='style="width: '+o[0]+"; height: "+o[1]+';"',delete e.area}var s,l=2==e.formType?'<textarea class="layui-layer-input"'+a+">"+(e.value||"")+"</textarea>":function(){return'<input type="'+(1==e.formType?"password":"text")+'" class="layui-layer-input" value="'+(e.value||"")+'">'}(),f=e.success;return delete e.success,r.open(i.extend({type:1,btn:["&#x786E;&#x5B9A;","&#x53D6;&#x6D88;"],content:l,skin:"layui-layer-prompt"+c("prompt"),maxWidth:n.width(),success:function(e){s=e.find(".layui-layer-input"),s.focus(),"function"==typeof f&&f(e)},resize:!1,yes:function(i){var n=s.val();""===n?s.focus():n.length>(e.maxlength||500)?r.tips("&#x6700;&#x591A;&#x8F93;&#x5165;"+(e.maxlength||500)+"&#x4E2A;&#x5B57;&#x6570;",s,{tips:1}):t&&t(n,i,s)}},e))},r.tab=function(e){e=e||{};var t=e.tab||{},n=e.success;return delete e.success,r.open(i.extend({type:1,skin:"layui-layer-tab"+c("tab"),resize:!1,title:function(){var e=t.length,i=1,n="";if(e>0)for(n='<span class="layui-layer-tabnow">'+t[0].title+"</span>";i<e;i++)n+="<span>"+t[i].title+"</span>";return n}(),content:'<ul class="layui-layer-tabmain">'+function(){var e=t.length,i=1,n="";if(e>0)for(n='<li class="layui-layer-tabli xubox_tab_layer">'+(t[0].content||"no content")+"</li>";i<e;i++)n+='<li class="layui-layer-tabli">'+(t[i].content||"no  content")+"</li>";return n}()+"</ul>",success:function(t){var a=t.find(".layui-layer-title").children(),o=t.find(".layui-layer-tabmain").children();a.on("mousedown",function(t){t.stopPropagation?t.stopPropagation():t.cancelBubble=!0;var n=i(this),a=n.index();n.addClass("layui-layer-tabnow").siblings().removeClass("layui-layer-tabnow"),o.eq(a).show().siblings().hide(),"function"==typeof e.change&&e.change(a)}),"function"==typeof n&&n(t)}},e))},r.photos=function(t,n,a){function o(e,t,i){var n=new Image;return n.src=e,n.complete?t(n):(n.onload=function(){n.onload=null,t(n)},void(n.onerror=function(e){n.onerror=null,i(e)}))}var s={};if(t=t||{},t.photos){var l=t.photos.constructor===Object,f=l?t.photos:{},u=f.data||[],d=f.start||0;s.imgIndex=(0|d)+1,t.img=t.img||"img";var y=t.success;if(delete t.success,l){if(0===u.length)return r.msg("&#x6CA1;&#x6709;&#x56FE;&#x7247;")}else{var p=i(t.photos),h=function(){u=[],p.find(t.img).each(function(e){var t=i(this);t.attr("layer-index",e),u.push({alt:t.attr("alt"),pid:t.attr("layer-pid"),src:t.attr("layer-src")||t.attr("src"),thumb:t.attr("src")})})};if(h(),0===u.length)return;if(n||p.on("click",t.img,function(){var e=i(this),n=e.attr("layer-index");r.photos(i.extend(t,{photos:{start:n,data:u,tab:t.tab},full:t.full}),!0),h()}),!n)return}s.imgprev=function(e){s.imgIndex--,s.imgIndex<1&&(s.imgIndex=u.length),s.tabimg(e)},s.imgnext=function(e,t){s.imgIndex++,s.imgIndex>u.length&&(s.imgIndex=1,t)||s.tabimg(e)},s.keyup=function(e){if(!s.end){var t=e.keyCode;e.preventDefault(),37===t?s.imgprev(!0):39===t?s.imgnext(!0):27===t&&r.close(s.index)}},s.tabimg=function(e){if(!(u.length<=1))return f.start=s.imgIndex-1,r.close(s.index),r.photos(t,!0,e)},s.event=function(){s.bigimg.hover(function(){s.imgsee.show()},function(){s.imgsee.hide()}),s.bigimg.find(".layui-layer-imgprev").on("click",function(e){e.preventDefault(),s.imgprev()}),s.bigimg.find(".layui-layer-imgnext").on("click",function(e){e.preventDefault(),s.imgnext()}),i(document).on("keyup",s.keyup)},s.loadi=r.load(1,{shade:!("shade"in t)&&.9,scrollbar:!1}),o(u[d].src,function(n){r.close(s.loadi),s.index=r.open(i.extend({type:1,id:"layui-layer-photos",area:function(){var a=[n.width,n.height],o=[i(e).width()-100,i(e).height()-100];if(!t.full&&(a[0]>o[0]||a[1]>o[1])){var r=[a[0]/o[0],a[1]/o[1]];r[0]>r[1]?(a[0]=a[0]/r[0],a[1]=a[1]/r[0]):r[0]<r[1]&&(a[0]=a[0]/r[1],a[1]=a[1]/r[1])}return[a[0]+"px",a[1]+"px"]}(),title:!1,shade:.9,shadeClose:!0,closeBtn:!1,move:".layui-layer-phimg img",moveType:1,scrollbar:!1,moveOut:!0,isOutAnim:!1,skin:"layui-layer-photos"+c("photos"),content:'<div class="layui-layer-phimg"><img src="'+u[d].src+'" alt="'+(u[d].alt||"")+'" layer-pid="'+u[d].pid+'"><div class="layui-layer-imgsee">'+(u.length>1?'<span class="layui-layer-imguide"><a href="javascript:;" class="layui-layer-iconext layui-layer-imgprev"></a><a href="javascript:;" class="layui-layer-iconext layui-layer-imgnext"></a></span>':"")+'<div class="layui-layer-imgbar" style="display:'+(a?"block":"")+'"><span class="layui-layer-imgtit"><a href="javascript:;">'+(u[d].alt||"")+"</a><em>"+s.imgIndex+"/"+u.length+"</em></span></div></div></div>",success:function(e,i){s.bigimg=e.find(".layui-layer-phimg"),s.imgsee=e.find(".layui-layer-imguide,.layui-layer-imgbar"),s.event(e),t.tab&&t.tab(u[d],e),"function"==typeof y&&y(e)},end:function(){s.end=!0,i(document).off("keyup",s.keyup)}},t))},function(){r.close(s.loadi),r.msg("&#x5F53;&#x524D;&#x56FE;&#x7247;&#x5730;&#x5740;&#x5F02;&#x5E38;<br>&#x662F;&#x5426;&#x7EE7;&#x7EED;&#x67E5;&#x770B;&#x4E0B;&#x4E00;&#x5F20;&#xFF1F;",{time:3e4,btn:["&#x4E0B;&#x4E00;&#x5F20;","&#x4E0D;&#x770B;&#x4E86;"],yes:function(){u.length>1&&s.imgnext(!0,!0)}})})}},o.run=function(t){i=t,n=i(e),l.html=i("html"),r.open=function(e){var t=new s(e);return t.index}},e.layui&&layui.define?(r.ready(),layui.define("jquery",function(t){r.path=layui.cache.dir,o.run(layui.jquery),e.layer=r,t("layer",r)})):"function"==typeof define&&define.amd?define(["jquery"],function(){return o.run(e.jQuery),r}):function(){o.run(e.jQuery),r.ready()}()}(window);