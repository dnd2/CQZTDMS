/*
 * CreateDate : 2009-08-04
 * CreateBy   : ChenLiang
 * Comment    : 角色维护模块
 */

//window.addEvent('domready', function(){
//	pageload();
//});

$(document).ready(function(){
	pageload();
});

function myresize() {
	var ww = document.body.clientWidth;
	var hh = document.body.clientHeight;
	
	$('#dtree').css({ "margin-left": (ww-$('#dtree').width)/2});
	$('#dtree').css({"height":(hh-$('#dtree').position().top-25) < 0 ? "430" : (hh-$('#dtree').position().top-25)});
	$('#treetd').css({"height" : $('#dtree').height()+25});
}

var tree_root_id = {"tree_root_id" : ""};
var subStr = "funlist";
var addNodeId;

var tree_script;

function pageload() {
	myresize();
	$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
	
	sendAjax(tree_url,createTree,'myfm');
}

function selType(obj,sgmCode,dealerCode) {
	if(obj.value == sgmCode) {
		$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
		sendAjax(tree_url,createTree,'myfm');
	}else if(obj.value == dealerCode) {
		$('#dtree').html("<img src='"+path+"/img/tree/loading.gif' />载入中...");
		sendAjax(tree_url,createTree,'myfm');
	}
}

PPPID = "10090102,10090101";
function setsele() {
	a.chkclickByFunId(PPPID);
}

function createTree(reobj) {
	$('#dtree').empty();

 	a = new dTree('a','dtree','true','true');
	a.config.folderLinks=true;
	a.config.closeSameLevel=false;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;
	    var nodeID = a.aNodes[id].id;
	    
	    for(var n=0; n<a.aNodes.length; n++) {
	    	if(iscontains(a.aNodes[n].id, addNodeId+"01")) {
	    		return;
	    	}
	    }
	}
	
	var funlistobj = reobj[subStr];
	var funcCode,parFuncId,funcId,funcName;
	for(var i=0; i<funlistobj.length; i++) {
		parFuncId = funlistobj[i].parFuncId;
		funcId = funlistobj[i].funcId;
		funcName = funlistobj[i].funcName;
		if("10" == funcId) { //系统根节点
			a.add(funcId,"-1",funcName);
		} else if(funcId.length<=10){
			a.add(funcId,parFuncId,funcName);
		}
	}
	a.draw();
	setsele();
	a.closeAll();
}

function getFunList() {
	var flist = new Array();
	for(var n=0; n<a.aNodes.length; n++) {
		var tid = a.aNodes[n].id;
		var tsrc = $('#ckk' + a.obj + a.aNodes[n]._ai)[0].src;
		if(tid.indexOf('1012')!=-1 && tid.indexOf('101204')==-1&& 
		tid.indexOf('101205')==-1 && tid.indexOf('101206')==-1 && tid.indexOf('101208')==-1
		&& tid.indexOf('101210')==-1 && tid.indexOf('101211')==-1){
				if(tid.length == 10  && iscontains(tsrc, "/checked.gif")) {
			flist.push(tid);
		}
		}
		else{
		if(tid.length == 8 && iscontains(tsrc, "/checked.gif")) {
			flist.push(tid);
		}
		}
	}
	return flist;
}

function saveRole(url) {
	$('#FUNS').val(getFunList().toString());
	makeNomalFormCall(saveUrl,addRoleCallBack,'myfm');
	// submitForm('myfm') ? sendAjax(saveUrl,addRoleCallBack,'myfm') : $('myfh').disabled = false,$('mysub').disabled = false;
}

function addRoleCallBack(json) {
	if(json.st != null && json.st == "succeed") {
		//TODO UI 消息提示
		__parent.__extQuery__(1);
		_hide();
	}else {
		if(json.st == "roleName_error") {
			alert('角色代码重复,请重新输入!');
		} else if(json.st == "roleDesc_error") {
			alert('角色名称重复,请重新输入!');
		}
	}
}

function clearDiv() {
	$('erdiv').setStyle("display","none");
}

function toGoRoleSearch() {
	// parent.document.getElementById ('inIframe').contentWindow.__extQuery__(1);
	_hide();
}
