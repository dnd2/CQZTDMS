//added by andy.ten@tom.com
var divDialogFlag = false;

// ====================拖动开始===================================
var labelDivHeight = 15; // 设置拖动条的高度
var _IsMousedown = 0;
var _ClickLeft = 0;
var _ClickTop = 0;
var backBoardDivId = "dialog_backBoard_div";
var headDivId = "dialog_head_div";
var _backBoardDiv ;
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
		 if(parent==null||parent==''){
			
		 }
		 else if(parent.parent==null||parent.parent==''){
			 return parent;
		 }
		 else if(parent.parent){
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
function _dialogInit(sWidth ,sHeight ,sTop ,sLeft){//初始化
    var owner = null;
	owner = getTopWinRef();
	if( owner.getElementById('dialog_div')==null ){
		var div = owner.createElement('div');
		div.setAttribute('id','dialog_div');
		owner.getElementsByTagName('body').item(0).appendChild(div);
		
		// 创建背景板,装填BackGroupDiv,HeadDiv,ContentDiv,ContentFrame
		// 用户整体拖动
		var backBoardDiv = owner.createElement('div');
		backBoardDiv.setAttribute('id',backBoardDivId);
		div.appendChild(backBoardDiv);
		
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
	if(owner.getElementById('dialog_modal_div')!=null){
		owner.getElementById('dialog_modal_div').onclick = function(){_hide();}
	}
	owner =null;
}

function _dialogDestroy(){//清扫遗留对象
	var owner = getTopWinRef();
	if(owner!=null){
	//移除事件，并设置成空。
	if( owner.getElementById('dialog_alert_button') ) 
		owner.getElementById('dialog_alert_button').onclick=null; 
	if( owner.getElementById('dialog_yes_button') ) 
		owner.getElementById('dialog_yes_button').onclick=null;
	if( owner.getElementById('dialog_no_button') ) {
		owner.getElementById('dialog_no_button').onclick=null;
		owner.getElementById('dialog_content_div').innerHTML=''; 
		owner.getElementById('dialog_content_frame').document.clear();
	}}
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
	div.style.top='0px';
	div.style.left='0px';
	if(sWidth && isNaN(sWidth) == false)
	    div.style.width= (sWidth) + 'px';
	else
		div.style.width=(screen.width - 23) + 'px';
	div.style.height=(owner.body.clientHeight || owner.documentElement.clientHeight) + 'px';
	
	//背景
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";
	
	owner.getElementById('dialog_div').appendChild(div);
	owner=null;
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
	div.style.top='0px';
	div.style.left='0px';
	//modified by andy.ten@tom.com
	if(sWidth && isNaN(sWidth) == false)
	    div.style.width=(sWidth) + 'px';
	else
		div.style.width=(screen.width - 23) + 'px';
	//div.style.height='100%';
	//modify by yupeng ie下兼容模型 窗口高度大于屏幕高度
	div.style.height=(owner.body.clientHeight || owner.documentElement.clientHeight) + 'px';
	//背景
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";
	
	owner.getElementById('dialog_div').appendChild(div);
	owner=null;
}

function _crtModalDiv(sWidth ,sHeight ,sTop ,sLeft){
	var owner = getTopWinRef();
	var div = owner.createElement('div');
	div.setAttribute('id','dialog_modal_div');
	div.style.display='none';
	div.style.background='#000';
	div.style.position='absolute';
	div.style.zIndex=102;
	div.style.top='0px';
	div.style.left='0px';
	//modified by andy.ten@tom.com
	if(sWidth && isNaN(sWidth) == false)
	    div.style.width=(sWidth) + 'px';
	else
		div.style.width=(screen.width - 23) + 'px';
	
	//modified by yupeng 
	div.style.height= (owner.body.clientHeight || owner.documentElement.clientHeight) + 'px';
	
	div.style.filter = "Alpha(Opacity=40)";
	div.style.opacity = "0.4";
	
	//背景
	owner.getElementById('dialog_div').appendChild(div);
	owner=null;
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
		ctop = (screen.height-height)/3;
		cleft= (screen.width-width)/2;	
	}

	var div = owner.getElementById(backBoardDivId);
	if(div){
		div.style.width=width+bwidth*4+'px';
		div.style.height=height+bwidth*4+'px';
		div.style.left=(cleft-bwidth*2)+'px';
		div.style.top=(ctop-bwidth*2)+'px';
		div.style.position='absolute';
		div.style.zIndex=103;
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
		div.style.top=bwidth*2+'px';
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
		dialogDiv.parentNode.removeChild(dialogDiv);
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
	
	if(typeof(st) == "undefined") {
		div.src = ""
	}
	
	*/
	try{
		bodyCloseFunc();
	}catch(e){
		
	}
	owner =null;
	_dialogDestroy();
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

function _show(isFrm){
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

//=========================================================================

function MyAlert(info){
if(info.indexOf('No Global')!=-1){
	return;
}
 var owner = getTopWinRef();
 try{
  _dialogInit();  
  owner.getElementById('dialog_content_div').innerHTML='\
    <div style="font-size:12px;">\
     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
      <b>信息</b>\
     </div>\
     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
     </div>\
    </div>';
  owner.getElementById('dialog_alert_info').innerHTML=info;
  owner.getElementById('dialog_alert_button').onclick=_hide;
  
  _setSize(300, 200); 
  _show();
 }catch(e){
  alert('MyAlert : '+ e.name+'='+e.message);
 }finally{
  owner=null;
 }
}
//add by wjb 
function MyAlertForFun(info,fun){
 var owner = getTopWinRef();
 try{
  _dialogInit();  
  owner.getElementById('dialog_content_div').innerHTML='\
    <div style="font-size:12px;">\
     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
      <b>信息</b>\
     </div>\
     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
     </div>\
    </div>';
  owner.getElementById('dialog_alert_info').innerHTML=info;
  owner.getElementById('dialog_alert_button').onclick=function(){
	try{
	 if(fun){
	  fun();
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
/*******add by liuxh 20100808 初始化DIV时不添加点击层外侧关闭层事件********/
function _dialogInitNotClose(sWidth ,sHeight ,sTop ,sLeft){//初始化
    var owner = null;
	owner = getTopWinRef();
	if( owner.getElementById('dialog_div')==null ){
		var div = owner.createElement('div');
		div.setAttribute('id','dialog_div');
		owner.getElementsByTagName('body').item(0).appendChild(div);

		// 创建背景板,装填BackGroupDiv,HeadDiv,ContentDiv,ContentFrame
		// 用户整体拖动
		var backBoardDiv = owner.createElement('div');
		backBoardDiv.setAttribute('id',backBoardDivId);
		div.appendChild(backBoardDiv);
		
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
/*******add by liuxh 20100808 初始化DIV时不添加点击层外侧关闭层事件********/
/***********add by liuxh 20130524 增加新的弹出层提示方法(点击层外侧不关闭层)beg************/
function MyUnCloseAlert(info,fun){
	 var owner = getTopWinRef();
	 try{
	  _dialogInitNotClose();
	  owner.getElementById('dialog_content_div').innerHTML='\
	    <div style="font-size:12px;">\
	     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	      <b>信息</b>\
	     </div>\
	     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
	      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	     </div>\
	    </div>';
	  owner.getElementById('dialog_alert_info').innerHTML=info;
	  owner.getElementById('dialog_alert_button').onclick=function(){
		try{
		 if(fun){
		  fun();
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

/***********add by wsw 20130524 增加新的弹出层提示方法(点击层外侧不关闭层) 用作进度条显示beg************/
function MyUnCloseProcess(info){
	 var owner = getTopWinRef();
	 try{
	  _dialogInitNotClose();
	  owner.getElementById('dialog_content_div').innerHTML='\
	    <div style="font-size:12px;">\
	     <div style="background-color:#08327e; padding:15px 5px 5px 10px; color:#ffffff;">\
	      <b>提示信息</b>\
	     </div>\
		  <div style="padding:2px;text-align:center;margin-top:5px;">\
		  <img src="'+g_webAppName+'/images/default/progress/large-loading.gif" />\
	     </div>\
	     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	    </div>';
	  owner.getElementById('dialog_alert_info').innerHTML=info;
	  _setSize(300, 150); 
	  _show();
	 }catch(e){
	  alert('MyAlert : '+ e.name+'='+e.message);
	 }finally{
	  owner=null;
	 }
}

//add by wjb 
function MyAlertForFun(info,fun){
 var owner = getTopWinRef();
 try{
  _dialogInit();  
  owner.getElementById('dialog_content_div').innerHTML='\
    <div style="font-size:12px;">\
     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
      <b>信息</b>\
     </div>\
     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
     </div>\
    </div>';
  owner.getElementById('dialog_alert_info').innerHTML=info;
  owner.getElementById('dialog_alert_button').onclick=function(){
	try{
	 if(fun){
	  fun();
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
/***********add by liuxh 20130524 增加新的弹出层提示方法(点击层外侧不关闭层)end************/
function MyConfirm(info,funYes,arrYes,funNo,arrNo){
	var owner = getTopWinRef();
	try{
	_dialogInit();
	owner.getElementById('dialog_content_div').innerHTML='\
	<div style="font-size:12px;">\
	 <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div>\
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
	}finally{
	owner=null;
	}
}
/***********add by wangsw 20130524 增加新的确认对话框弹出层提示方法(点击层外侧不关闭层)begin************/
function MyUnCloseConfirm(info,funYes,arrYes,funNo,arrNo){
	var owner = getTopWinRef();
	try{
		_dialogInitNotClose();
	owner.getElementById('dialog_content_div').innerHTML='\
	<div style="font-size:12px;">\
	 <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div>\
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
	<div style="font-size:12px;">\
	 <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div>\
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
		<div style="font-size:12px;">\
	 	<div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  	<b>信息</b>\
	 	</div>\
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
    <div style="font-size:12px;">\
     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
      <b>信息</b>\
     </div>\
     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
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

function OpenHtmlWindow(url,width,height){
	 var owner = getTopWinRef();
	 try{
	  _dialogInit();
	  _setSize(width, height, true);
	  _show(true);
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