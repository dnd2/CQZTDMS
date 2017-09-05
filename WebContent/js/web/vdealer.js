/*
 * CreateDate : 2009-08-26
 * CreateBy   : ChenLiang
 * Comment    : 查看经销商职位信息
 */

window.addEvent('domready', function(){
	myEffects = new Fx.Styles('dtree', {duration: 277,transition: Fx.Transitions.linear});
	poseValInit();
	myresize();
});

function myresize() {
	var hh = document.body.clientHeight;
	
	$('funTree').setStyle("height",(hh-$('funTree').getCoordinates().top-15) < 0 ? "350" : (hh-$('funTree').getCoordinates().top-15));
	$('treetd').setStyle("height",$('funTree').getStyle("height").toInt()+8);
}

function poseValInit() {
	if(tgjzw != null && tgjzw != "") {
		var arrs = tgjzw.split(",");
		for(var i=0; i < arrs.length; i++) {
			$(arrs[i]).checked = true;
		}
	}
	sendAjax(getDealerPoseValUrl+"?poseId="+$('POSE_ID').value,backPoseValInit,'fm');
}

function backPoseValInit(obj) {
	vorgName = obj.vorgName;
	poseFuncList = obj.poseFuncList;
	poseFuncDataList = obj.poseFuncDataList;
	showDataAuth();
	setPageVal();
}

function setPageVal() {
	$('DEPT_NAME').value = vorgName.deptName;
	myfuns = new Array();
	for(var n=0; n<poseFuncList.length; n++) {
		myfuns.push(poseFuncList[n].funcId);
	}
	showFunsTree();
	
	funsh.empty();
	for(var i=0; i<poseFuncDataList.length; i++) {
		if(funsh.get(poseFuncDataList[i].funcId) == null) {
			var narr = new Array();
			narr.push(poseFuncDataList[i].dataAuthId);
			funsh.set(poseFuncDataList[i].funcId,narr);
		} else {
			var arr = funsh.get(poseFuncDataList[i].funcId);
			arr.push(poseFuncDataList[i].dataAuthId);
			funsh.set(poseFuncDataList[i].funcId,arr);
		}
	}
}

function modfiPose() {
	var gg = document.getElementsByName("gjyw");
	var temp = "";
	for(var i=0; i<gg.length; i++) {
		if(gg[i].checked == true) {
			temp += gg[i].value+",";
		}
	}
	if(temp != "") {
		temp = temp.substr(0,temp.length-1);
	}
	$('GG').value = temp;
	
	$('FUNSH').value = getFunsh();
	$('MYFUNS').value = myfuns;
	$('POSE_CODE').value = $('POSE_CODE').value.clean();
	$('POSE_NAME').value = $('POSE_NAME').value.clean();
	submitForm('fm') ? sendAjax(modfiPoseUrl,modfiPoseBack,'fm') : "";
}

function modfiPoseBack(jsons) {
	if(jsons.st == "poseName_error") {
		showError('ermsg','erdiv','POSE_NAME','职位名称重复，请重新输入!',173);
		$('POSE_NAME').select();
	} else if(jsons.st == "succeed") {
		toGoPositionSearch();
	}
}

function getFunsh() {
	var keys = funsh.keys();
	var restr = "";
	for(var i=0; i<keys.length; i++) {
		restr += keys[i]+":"+funsh.get(keys[i])+"#";
	}
	return restr;
}

function toGoPositionSearch() {
	window.location = poseSearch;
}

function selType(obj,sgmCode,dealerCode) {
	if(obj.value == sgmCode) {
		$('corg').setHTML("所属部门：");
		showDataAuth();
	}else if(obj.value == dealerCode) {
		$('corg').setHTML("所属部门：");
		showDataAuth();
	}
}

function showFun(roles) {
	if(roles != null && roles != "" && roles.length >0) {
		sendAjax(getFunsByRoleIdsUrl+"?roleIds="+roles,showFunBack,'fm');
	}
}

function showFunBack(funss) {
	myfuns = null;
	myfuns = new Array();
	
	myfun = funss["funs"];
	for(var n=0; n<myfun.length; n++) {
		myfuns.push(myfun[n].funcId);
	}
	showFunsTree();
}

var addFunNodeId;
function showFunsTree() {
	b = null;
	b = new dTree('b','funTree','false','false','true'); 
	b.config.closeSameLevel=false;
	b.config.myfun="setDataAuth";
	b.config.folderLinks=false;
	sendAjax(fun_tree_url+"?tree_root_id=",createFunTree,'fm');
	b.closeAll();
}

function createFunTree(reobj) {
	var funlistobj = reobj["funlist"];
	var funcCode,parFuncId,funcId,funcName;
	for(var i=0; i<funlistobj.length; i++) {
		parFuncId = funlistobj[i].parFuncId;
		funcId = funlistobj[i].funcId;
		funcName = funlistobj[i].funcName;
		funcCode = funlistobj[i].funcCode;
		if(parFuncId == 0) { //系统根节点
			b.add(funcId,"-1",funcName);
		} else if(funcId.length<=8 && isVev(funcId)){
			b.add(funcId,parFuncId,funcName,funcCode);
		}
	}
	b.draw();
	b.openAll();
}

function isVev(fId) {
	for(var n=0; n<myfuns.length; n++) {
		if(myfuns[n].indexOf(fId) == 0) {
			return true;
		}
	}
	return false;	
}

var thisFunId = null; // 用户当前选择的功能
var funsh = new Hash();
function setDataAuth(id) {
	var fid = b.aNodes[id].id;
	thisFunId = fid;
	setSeleClear();
	if(funsh.get(fid) == null) { //第一次
	} else {
		setSele(fid);
	}
}

function setSele(fid) {
	var arrobj = funsh.get(fid);
	var aa = document.getElementsByName("c1");
	for(var n=0; n<arrobj.length; n++) {
		for(var v=0; v<aa.length; v++) {
			if(aa[v].value == arrobj[n]) {
				aa[v].checked = true;
			}
		}
	}
}

function setSeleClear() {
	var aa = document.getElementsByName("c1");
	for(var v=0; v<aa.length; v++) {
		aa[v].checked = false;
	}
}

function showDataAuth() {
	$('dataAuth').setHTML("载入中...");
	sendAjax(getDataAuthUrl,showDataAuthBack,'fm');
}

function showDataAuthBack(dataAuth) {
	var jsonObj = dataAuth["dataAuth"];
	$('dataAuth').innerHTML = "";
	$('dataAuth').innerHTML += "<input type='checkbox' name='c1' onclick='selck(this)' value='"+jsonObj[0].dataAuthId+"'>"+jsonObj[0].dataAuthName+"</br>";
	$('dataAuth').innerHTML += "<input type='checkbox' name='c1' onclick='selck(this)' value='"+jsonObj[1].dataAuthId+"'>"+jsonObj[1].dataAuthName+"</br>";
	$('dataAuth').innerHTML += "<input type='checkbox' name='c1' onclick='selck(this)' value='"+jsonObj[2].dataAuthId+"'>"+jsonObj[2].dataAuthName;
}

function selck(obj) {
	if(obj.checked == true && thisFunId!= null) {
		if(funsh.get(thisFunId)!=null) {
			var arr = funsh.get(thisFunId);
			arr.push(obj.value);
			funsh.set(thisFunId,arr);
		} else {
			var arr = new Array();
			arr.push(obj.value);
			funsh.set(thisFunId,arr);
		}
	} else if(obj.checked == false && thisFunId!= null) {
		if(funsh.get(thisFunId)!=null) {
			var arr = funsh.get(thisFunId);
			arr.del(obj.value);
			funsh.set(thisFunId,arr);
		}
	}
}

var myfuns; // 用户选择角色所对应的功能

function addRole(path) {
	OpenHtmlWindow(path+"/sysmng/dealerposition/DealerPosition/addDealerRoleInit.do",800,600);
}

function getPageValue() {
	var pageVal = {
		ROLE_DESC : $('ROLE_DESC').value,
		ROLE_NAME : $('ROLE_NAME').value,
		ROLE_STATUS: $('ROLE_STATUS').value,
		ROLE_TYPE : $('ROLE_TYPE').value,
		FUNS : getFunList()
	}
	return pageVal;
}
