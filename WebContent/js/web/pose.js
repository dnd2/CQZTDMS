/*
 * CreateDate : 2009-08-23
 * CreateBy   : ChenLiang
 * Comment    : 职位维护模块
 */
 
 var myfuns; // 用户选择角色所对应的功能
 window.addEvent('domready', function(){
 	if($('POSE_TYPE').value == "10021001") {
		$('trgjtw').setStyle("display","none");
	}
 	showDataAuth();
 	myresize();
 });
 
 function myresize() {
	var hh = document.body.clientHeight;
	
	$('funTree').setStyle("height",(hh-$('funTree').getCoordinates().top-15) < 0 ? "350" : (hh-$('funTree').getCoordinates().top-15));
	$('treetd').setStyle("height",$('funTree').getStyle("height").toInt()+8);
 }
 
 function addPose() {
	
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
 
 function showbm() {
 	if($('POSE_TYPE').value == '10021002') {
 		if($('DEALER_NAME').value == "") {
 			showErrMsg('DEALER_NAME','请先选择经销商','17');
 			return false;
 		}
 	}
	showDEPT();
 }
 
 function toGoPositionSearch() {
	window.location = poseSearch;
 }
 
 var tree_script;
 
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
		
		$('20601001').checked = false;
		$('20601003').checked = false;
		$('20601002').checked = false;
		$('20601004').checked = false;
	
		showDataAuth();
		$('jxs').setStyle("display","none");
		$('trgjtw').setStyle("display","none");
	}else if(obj.value == dealerCode) {
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
		
		$('20601001').checked = false;
		$('20601003').checked = false;
		$('20601002').checked = false;
		$('20601004').checked = false;
		
		showDataAuth();
		$('trgjtw').setStyle("display","");
		$('jxs').setStyle("display","");
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
			arr.del(obj.value)
			funsh.set(thisFunId,arr);
		}
	}
 }
 
 function addRole(path) {
	OpenHtmlWindow(path+"/sysmng/sysposition/SysPosition/addSysPositionRoleInit.do?poseType="+$('POSE_TYPE').value,800,600);
 }
 
 function showFun(roles) {
	if(roles != null && roles != "" && roles.length >0) {
		sendAjax(getFunsByRoleIdsUrl+"?roleIds="+roles,showFunBack,'fm');
	}
	$('20601001').checked = false;
	$('20601003').checked = false;
	$('20601002').checked = false;
	$('20601004').checked = false;
	showGjPose(roles);
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

 function requery2() {
	$('DRLCODE').value="";
	$('DELSNAME').value="";
	$('DEPT_ID').value="";
 }