/*
 * CreateDate : 2009-08-27
 * CreateBy   : ChenLiang
 */
var myEffects;

function isCloseTreeDiv(event,obj,treeDivId) {
	if($(treeDivId).getStyle("opacity") == 0) {
		return false;
	}
	var tempa = $(treeDivId).getStyle("width").toInt();
    var tempb = $(treeDivId).getStyle("height").toInt();
    var ex = event.offsetX;
    var ey = event.offsetY;
    var st = false;
    if(ex > 0 && ex < tempa && ey > 0 && ey < tempb) {
		st = true;
		obj.focus();
	}
    if(!st) {
   		closeTreeDiv(treeDivId,obj);
  	}
}

function closeTreeDiv(treeDivId,obj) {
	if($(treeDivId).getStyle("opacity") < 1) {
		return ;
	}
	
	myEffects.start({
	    'opacity': [1,0]
	});
	
	if(getIEV() <= 6) {
		ShowSels(treeDivId);
	}
}

function addOption(objSelectNow,txt,val){
	var objOption = document.createElement("OPTION");
	objOption.text = txt;
	objOption.value = val;
	objSelectNow.options.add(objOption);
	return objOption;
}

function showError(ermsg,erdiv,errid,msg,msgw) {
	$(ermsg).setText("");
	$(ermsg).appendText(msg);
	$(erdiv).setStyles({
	   display: '',
	   width: msgw,
	   top: $(errid).getTop() - 1,
	   left: $(errid).getLeft() + $(errid).getStyle("width").toInt() + 8
	});
}

Array.prototype.del = function(b) { 
	var a = this.indexOf(b); 
	if (a >= 0) { 
		this.splice(a, 1); 
		return true; 
	} 
	return false; 
}

function callBack(json) {
	var ps = json.ps;
	if(ps != null && ps.records != null){
		// ==================各个开关start by chenyu===================
		var customPageSizeFlag = json.customPageSizeFlag;
		// 每页条数
		if(customPageSizeFlag){
			ps.customPageSizeFlag = customPageSizeFlag;
		}
		// ==================各个开关end by chenyu=====================
		$("_page").setStyle("display","none");
		$('myGrid').setStyle("display","");
		new createGrid(title,columns, $("myGrid"),ps).load();
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		// 动态设置tablediv的高度
		setGridHeight();
	}else{
		$("_page").setStyle("display","");
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').setStyle("display","none");
	}
	$('loader').setStyle("display","none");
	$("queryBtn").disabled = "";
}

function showMask(){
	var screenW = document.body.clientWidth/2;
	$('loader').style.left = (screenW-20) + 'px';
	$('loader').innerHTML = ' 正在载入中... ';
	$('loader').setStyle("display","");
}

function removeMask() {
	$('loader').setStyle("display","none");
}

function HideSels(objID)
{
    var sels = document.getElementsByTagName('select');
    for (var i = 0; i < sels.length; i++)
      if (OO(document.all[objID], sels[i])) {
      	sels[i].style.visibility = 'hidden';
      }
}

function ShowSels(objID)
{
    var sels = document.getElementsByTagName('select');
    for (var i = 0; i < sels.length; i++)
      if (OO(document.all[objID], sels[i]))
        sels[i].style.visibility = 'visible';
}

function OO(obj1, obj2)
{
  var pos1 = getPosin(obj1)
  var pos2 = getPosin(obj2)
  var result = true;
  var obj1Left = pos1.left - window.document.body.scrollLeft;
  var obj1Top = pos1.top - window.document.body.scrollTop;
  var obj1Right = obj1Left + obj1.offsetWidth;
  var obj1Bottom = obj1Top + obj1.offsetHeight;
  var obj2Left = pos2.left - window.document.body.scrollLeft;
  var obj2Top = pos2.top - window.document.body.scrollTop;
  var obj2Right = obj2Left + obj2.offsetWidth;
  var obj2Bottom = obj2Top + obj2.offsetHeight;
  
  if (obj1Right <= obj2Left || obj1Bottom <= obj2Top ||
      obj1Left >= obj2Right || obj1Top >= obj2Bottom)
    result = false;
  return result;
}

function getPosin(Obj)
{
    var sumTop=0;
    var sumLeft=0;    
    while(Obj!=null && Obj!=window.document.body) {
      sumTop+=Obj.offsetTop;
      if(Obj.tagName.toLowerCase()=='div') {
        sumTop-=Obj.scrollTop;
      }
      sumLeft+=Obj.offsetLeft;
      Obj=Obj.offsetParent;
    }
 return {left:sumLeft,top:sumTop}
}

function getIEV() {
	return parseFloat(navigator.appVersion.split("MSIE")[1]);
}

function doError(json){
	var exception = null;
	var errorCode = json.Exception.errCode;
	if(errorCode!=undefined){
		exception=errorCode;
	}
	var message = json.Exception.message;
	if(message!=undefined){
		if(exception==null){
			exception=message;
		}else{
			exception+="："+message;
		}
	}
	MyAlert(exception);
}

function sendJsonAjax(url,valobj,callback) {
	new Json.Remote(url,{onComplete: function(reobj){
	    callback(reobj);
	}}).send(valobj);
}

function sendAjax(url,callBack,formId) {
	new Ajax(url, {method: 'post',onComplete: function() {
		var json = Json.evaluate(this.response.text);
		callBack(json);
		if(json.Exception != null && json.Exception != undefined) {
			doError(json);
		}
	}
	}).request($(formId).toQueryString());
}
function makeNomalFormCall(url,callBack,formId) {
	new Ajax(url, {method: 'post',onComplete: function() {
		var json = Json.evaluate(this.response.text);
		if(json.Exception != null && json.Exception != undefined) {
			doError(json);
		} else {
			callBack(json);
		}
	}
	}).request($(formId).toQueryString());
}

var entThisPage = 1;
window.document.addEvent('keydown', function(){
	if(event.keyCode==13){
		if($defined($('_page'))) {
			__extQuery__(entThisPage);
		}
	};
});