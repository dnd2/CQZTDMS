/*
 * CreateDate : 2009-08-25
 * CreateBy   : ChenLiang
 * Comment    : 查看职位信息
 */
var thisFunId = null; // 用户当前选择的功能
var funsh = new Hash();

$(function($){
	$('#zwlb').html(getItemValue($('#POSE_TYPE').val()));
	if($('#POSE_TYPE').val() == "10021001") {
		$('#trgjtw').show();
	}else{
		$('#trgjtw').show();
	}
});

//function poseValInit() {
//	if(tgjzw != null && tgjzw != "") {
//		var arrs = tgjzw.split(",");
//		for(var i=0; i < arrs.length; i++) {
//			$(arrs[i]).checked = true;
//		}
//	}
//	sendAjax(getsysPoseValUrl+"?poseId="+$('POSE_ID').value,backPoseValInit,'fm');
//}

//function backPoseValInit(obj) {
//	roleFuncList = obj.roleFuncList;
//	//poseFuncDataList = obj.poseFuncDataList;
//	roleList = obj.roleList;
//	//showDataAuth();
//	setPageVal();
//}

//function setPageVal() {
//	myfuns = new Array();
//	for(var n=0; n<roleFuncList.length; n++) {
//		myfuns.push(roleFuncList[n].funcId);
//	}
//	showFunsTree();
//	funsh.empty();
//	//modified by andy.ten@tom.com 
//	/**
//	for(var i=0; i<poseFuncDataList.length; i++) {
//		if(funsh.get(poseFuncDataList[i].funcId) == null) {
//			var narr = new Array();
//			narr.push(poseFuncDataList[i].dataAuthId);
//			funsh.set(poseFuncDataList[i].funcId,narr);
//		} else {
//			var arr = funsh.get(poseFuncDataList[i].funcId);
//			arr.push(poseFuncDataList[i].dataAuthId);
//			funsh.set(poseFuncDataList[i].funcId,arr);
//		}
//	}
//	*/
//	for(var n=0; n<roleList.length; n++) {
//		addPlanRow(roleList[n].roleId,roleList[n].roleName,roleList[n].roleDesc,n);
//	}
//}

//function modfiPose() {
//	
//	$('FUNSH').value = getFunsh();
//	$('MYFUNS').value = myfuns;
//	$('POSE_CODE').value = $('POSE_CODE').value.clean();
//	$('POSE_NAME').value = $('POSE_NAME').value.clean();
//	if(document.getElementsByName('ROLE_ID').length < 1)
//	{
//		MyAlert('请选择对应的角色！');
//		return false;
//	}
//	submitForm('fm') ? sendAjax(modfiPoseUrl,modfiPoseBack,'fm') : "";
//}

//function ffhh(jsons) {
//	window.history.go(backcount);
//}

function getFunsh() {
	var keys = funsh.keys();
	var restr = "";
	for(var i=0; i<keys.length; i++) {
		restr += keys[i]+":"+funsh.get(keys[i])+"#";
	}
	return restr;
}

function addPose1(obj) {
	obj.disabled="disabled";
	$('#FUNSH').val(getFunsh());
	$('#MYFUNS').val(myfuns);
	$('#POSE_CODE').val($('POSE_CODE').value);
	$('#POSE_NAME').val($('POSE_NAME').value);
	if(submitForm('fm')) {
		if(myfuns == '' || myfuns == 'undefined') {
			$('#msg').show();
		} else {
			sendAjax(addPoseUrl+"?DEPT_ID="+$('DEPT_ID').value,addPoseBack1,'fm');
		}
	}
 }
function toGoPositionSearch() {
	window.location = poseSearch;
}


 function addPose() {
	
	$('msg').setStyle("visibility","hidden");
	$('FUNSH').value = getFunsh();
	$('MYFUNS').value = myfuns;
	$('POSE_CODE').value = $('POSE_CODE').value.clean();
	$('POSE_NAME').value = $('POSE_NAME').value.clean();
	if(submitForm('fm')) {
		if(!$defined(myfuns)) {
			$('msg').setStyle("visibility","visible");
		} else {
			sendAjax(addPoseUrl+"?DEPT_ID="+$('DEPT_ID').value,addPoseBack,'fm');
		}
	}
 }
 
 function addPoseBack(json) {
	 if(json.st == "poseCode_error") {
			showError('ermsg','erdiv','POSE_CODE','职位代码重复，请重新输入!',173);
			$('POSE_CODE').select();
		} else if(json.st == "poseName_error") {
			showError('ermsg','erdiv','POSE_NAME','职位名称重复，请重新输入!',173);
			$('POSE_NAME').select();
		} else {
			toGoPositionSearch();
		}
 }
 
 function addPoseBack1(json) {
	 if(json.st == "poseCode_error") {
			MyAlert("职位代码重复，请重新输入!") ;
			
			var aButton = document.getElementsByTagName("input") ;
			
			if(aButton) {
				var iLen = aButton.length ;
				
				for(var i=0; i<iLen; i++) {
					aButton[i].disabled = false ;
				}
			}
			
			return ;
		} else if(json.st == "poseName_error") {
			MyAlert("职位名称重复，请重新输入!") ;

			var aButton = document.getElementsByTagName("input") ;
			
			if(aButton) {
				var iLen = aButton.length ;
				
				for(var i=0; i<iLen; i++) {
					aButton[i].disabled = false ;
				}
			}
			
			return ;
		} else {
			toGoPositionSearch();
		}
}

function showbm() {
 	if($('POSE_TYPE').value == '10021002') {
 		if($('DEALER_NAME').value == "") {
 			showErrMsg('DEALER_NAME','请先选择经销商','17');
 			return false;
 		}
 	}
	showDEPT();
 }
 function selType(obj,sgmCode,dealerCode) {
	if(obj.value == sgmCode) {
		$('funTree').empty();
		tree_script = new Element('script',{
	 		'type' : 'text/javascript'
		});
		tree_script.setText("b = new dTree('b','funTree','true','true');");
	 	tree_script.injectInside($('funTree'));
		$('DEPT_ID').value = "";
		$('FUNSH').value = "";
		$('MYFUNS').value = "";
		$('DEALER_ID').value = "";
		$('DEALER_NAME').value = "";
		$('DEPT_NAME').value = "";
		$('tree_root_id').value = "";
		
	
		// showDataAuth();
		$('jxs').setStyle("display","none");
		$('trgjtw').setStyle("display","");
		$('bm').setStyle("display","");
	}else if(obj.value == dealerCode) {
		$('funTree').empty();
		tree_script = new Element('script',{
	 		'type' : 'text/javascript'
		});
		tree_script.setText("b = new dTree('b','funTree','true','true');");
	 	tree_script.injectInside($('funTree'));
		$('DEPT_ID').value = "";
		$('bm').setStyle("display","none");
		$('FUNSH').value = "";
		$('MYFUNS').value = "";
		$('DEALER_ID').value = "";
		$('DEALER_NAME').value = "";
		$('DEPT_NAME').value = "";
		$('tree_root_id').value = "";
		
		
	//	showDataAuth();
	//modify by begin xiayapeng 维护经销商职位加入业务范围选择
		//$('trgjtw').setStyle("display","none");
		$('trgjtw').setStyle("display","");
	//modify by end
		$('jxs').setStyle("display","");
	}
 }

function showFun(roles) {
	if(roles != null && roles != "" && roles.length >0) {
		sendAjax(getFunsByRoleIdsUrl+"?roleIds="+roles,showFunBack,'fm');
	}
	
	//showGjPose(roles);
}

function showGjPose(gids) {
	if(gids != null && gids != "" && gids.length >0) {
		sendAjax(getGjzwIdsUrl+"?roleIds="+gids,showGjPoseBack,'fm');
	}
 }
 
 function showGjPoseBack(gjids) {
	if(gjids != null) {
		var tmp = gjids.gjzwlist;
		for(var i=0; i<tmp.length; i++) {
			$(tmp[i].binsCodeId).checked = true;
		}
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
	delPalRow();
	var myRole = new Array();
	myRole = funss["roelList"];
	for(var n=0; n<myRole.length; n++) {
		addPlanRow(myRole[n].roleId,myRole[n].roleName,myRole[n].roleDesc,n);
	}
}

function addPlanRow(roleId,roleCode,roleName,n)
{	
	var addTable = $('#role_list')[0];
	var rows = addTable.rows;
	var length = rows.length;
	
	var insertRow = addTable.insertRow(length);
	//modified by andy.ten@tom.com
	if(n == 0)
		insertRow.className = "table_list_row2";
	else
	    insertRow.className = "table_list_row1";
	insertRow.id = roleId;
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	addTable.rows[length].cells[0].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+(length-1)+"</td>";
	addTable.rows[length].cells[1].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><input type='hidden' value='"+roleId+"' name='ROLE_ID'/>"+roleCode+"</td>";
	addTable.rows[length].cells[2].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+roleName+"</td>";
	addTable.rows[length].cells[3].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+(length)+");'>[删除]</a></td>";
}
function deleteRow(row){
	var addTable = $('#role_list')[0];
	var rows = addTable.rows;
	addTable.deleteRow(row);
	for(var i = row;i<addTable.rows.length;i++){
		addTable.rows[i].cells[3].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+i+");'>[删除]</a></td>";
	}
	var roleIds = document.getElementsByName('ROLE_ID');
	var temp = new Array();
	for(var i=0; i<roleIds.length; i++) {
		temp.push(roleIds[i].value);
	}
	showFun(temp.toString());
}

function delPalRow() {
	var addTable = $('#role_list')[0];
	var rowss = addTable.rows.length;
	for(var dels = 2; rowss > dels; rowss--) { 
		addTable.deleteRow(rowss-1);
	}
}
function setRoleIds() {
	var roles = new Array();
	var addTable = $('#role_list')[0];
	var rowss = addTable.rows.length;
	for(var dels = 2; rowss > dels; rowss--) {
		roles.push(addTable.rows[rowss-1].id);
	}
	if(roles.length > 0) {
		$('#ROLE_IDS').val(roles.toString());
	} else {
		$('#ROLE_IDS').val("");
	}
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
		} else if(funcId.length<=10 && isVev(funcId)){
			b.add(funcId,parFuncId,funcName,funcCode);
		}
	}
	b.draw();
	b.openAll();
}

function isVev(fId) {
	for(var n=0; n<myfuns.length; n++) {
		if((myfuns[n]+'').indexOf(fId+'') == 0) {
			return true;
		}
	}
	return false;	
}

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
	sendAjax(getDataAuthUrl+"?type="+$('POSE_TYPE').value,showDataAuthBack,'fm');
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
var backcount = -1;
function addRole(path) {
	backcount --;
	var role_ids = document.getElementsByName("ROLE_ID");
	var old_roleIds="";
	if(role_ids){
		for(var i=0; i<role_ids.length;i++){
			old_roleIds = old_roleIds + role_ids[i].value;
			if(i != role_ids.length-1){
				old_roleIds = old_roleIds+",";
			}
		}
		document.getElementById("old_roleIds").value=old_roleIds;
	}
	OpenHtmlWindow(path+"/sysmng/sysposition/SysPosition/addSysPositionRoleInit.do?poseType="+$('#POSE_TYPE').val(),800,600);
}
