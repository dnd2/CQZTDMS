﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!--link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_tree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/framecommon/default.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/DialogManager.js"></script-->
<title>经销商用户维护</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";
</script>
</head>


<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 系统管理 &gt; 经销商用户维护</div>
	<form id="fm" name="fm">
		<input id="POSE_IDS" name="POSE_IDS" type="hidden"/>
		<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value=""/>
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<div class="form-panel">
			<h2>基本信息</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
					<td class="tblopt"><div align="right">经销商：</div></td>
									<td>
					      				<input name="outDealerCode" type="hidden" id="outDealerCode" class="middle_txt" value=""  />
					      				<input name="outDealerName" type="text" id="outDealerName" class="middle_txt" value="" onclick="chkDlr() ;"/>
					      				<input name="dealerId" type="hidden" id="dealerId" class="middle_txt" value="" />
					                    <!-- <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('outDealerCode','dealerId','false', '', 'true','','','outDealerName');" value="..." /> -->
					                    <input type="button" class="normal_btn" onclick="txtClr('dealerId');txtClr('outDealerCode');txtClr('outDealerName');" value="清 空" id="clrBtn" /> 
					    			</td>
						  <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">用户帐号：</td>
						  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="0,is_digit_letter,20" maxlength="20" onkeydown="clearDiv()" type="text" id="ACNT" name="ACNT" /></td>
						  <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">密码：</td>
						  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" type="password" id="PASSWORD" name="PASSWORD" value="1a2b3c"/><font color="red">*</font></td>
						   <!-- <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">EPC权限:</td>
						  <td class="table_query_3Col_input" nowrap="nowrap">
						  <select class="u-select" name='epc_power' id='epc_power'>
							  <option value='10041001' selected>查看订单</option>
							  <option value='10041002'>操作订单</option>
						  </select></td>  -->
						</tr>
						<%-- <tr>
						 <!--  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">所属公司：</td>
						  <td class="table_query_3Col_input" nowrap="nowrap"><div nowrap><input class="middle_txt" id="COMPANY_NAME" datatype="0,is_null" readonly="readonly" style="cursor: pointer;" name="COMPANY_NAME" type="text"/>
						  <input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
						  </div></td>
						  -->
						  <td class="tblopt"><div align="right">经销商：</div></td>
									<td colspan="5">
					      				<input name="outDealerCode" type="hidden" id="outDealerCode" class="middle_txt" value=""  />
					      				<input name="outDealerName" type="text" id="outDealerName" class="middle_txt" value="" onclick="showOrgDealer('outDealerCode','dealerId','false', '', 'true','','','outDealerName');"/>
					      				<input name="dealerId" type="hidden" id="dealerId" class="middle_txt" value="" />
					                    <!-- <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('outDealerCode','dealerId','false', '', 'true','','','outDealerName');" value="..." /> -->
					                    <input type="button" class="normal_btn" onclick="txtClr('dealerId');txtClr('outDealerCode');txtClr('outDealerName');" value="清 空" id="clrBtn" /> 
					    			</td>
						</tr> --%>
						<tr>
						  <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">姓名：</td>
						  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt"
					datatype="0,is_null,60" maxlength="60" type="text" id="NAME" name="NAME" /></td>
						  <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">E-mail：</td>
							<td class="table_query_3Col_input" nowrap="nowrap"><input name="EMAIL" datatype="1,is_email,49" maxlength="49" id="EMAIL" type="text" class="middle_txt" /></td>
						  <td class="table_query_3Col_label_4Letter right" nowrap="nowrap">性别：</td>
							<td class="table_query_3Col_input" nowrap="nowrap"><span id="xxbb">
					<script type="text/javascript"> genSelBox("GENDER",<%=Constant.GENDER_TYPE%>,"",false,"","");</script></span></td>
						</tr>
						<tr>
							<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">手机：</td>
							<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="1,is_digit,11" maxlength="11" type="text" id="HAND_PHONE" name="HAND_PHONE"/></td>
							<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">电话：</td>
							<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="1,is_phone,19" maxlength="19" type="text" id="PHONE" name="PHONE" /></td>
							<td class="table_query_3Col_label_4Letter right" nowrap="nowrap" style="display:none">店面系统用户：</td>
							<td style="display:none">
							<select name="IS_DCS">
								<option value="0">否</option>
								<option value="1">是</option>
							</select>
							</td>
							<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">状态：</td>
							<td class="table_query_3Col_input" nowrap="nowrap" colspan="5"><span id="zztt">
					<script type="text/javascript">genSelBox("USER_STATUS",<%=Constant.STATUS%>,"",false,"","");</script></span></td>
						</tr>
				</table>
			</div>
		</div>
	</form>
	
	<div class="form-panel">
			<h2>职位信息</h2>
			<div class="form-body">
	<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="roll">
		<th>序号</th>
		<th>职位代码</th>
		<th>职位名称</th>
		<th>操作</th>
	</table>
	</div>
	</div>
	<br>
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr >
			<td align="center"><input class="normal_btn" onmouseover="clearDiv()" id="addPosebtn" type="button" value="分配职位" onclick="addPose('<%=contextPath%>')"/>&nbsp;<input name="button2" type="button" 
	class="normal_btn" onclick="sub('<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/addSgmDealerSysUser.json')" value="保 存"/>&nbsp;<input name="button" type="button" style="width:60px;height:16px;line-height:10px;background-color:#EEF0FC;border:1px solid #5E7692;color:#1E3988;" 
	class="normal_btn" onclick="toGoSysUserSearch()" value="取 消"/></td>
		</tr>
	</table>
	<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
	<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
	<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
</div>
</body>
</html>
<script>
validateConfig.isOnBlur = false;
var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/drlQuery.json?COMMAND=1";
var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";

function chkDlr() {
	var addTable = $('#roll')[0];
	var rows = addTable.rows;
	var length = rows.length;
	
	if(length > 1) {
		MyAlert("请删除已选择职位后，再选择经销商！") ;
		
		return false ;
	} else {
		showOrgDealer('outDealerCode','dealerId','false', '', 'true','','','outDealerName');
	}
}

function sub(surl) {
	var password = document.getElementById("PASSWORD");
	if(!isNumAndStr(password)){
		MyAlert("密码至少有六个字符，必须同时包含字母和数字");
	}else{
		setPoseIds();
		/* $('#ACNT')[0].value = $('#ACNT')[0].value.clean();
		$('#NAME')[0].value = $('#NAME')[0].value.clean();
		$('#HAND_PHONE')[0].value = $('#HAND_PHONE')[0].value.clean();
		$('#PHONE')[0].value = $('#PHONE')[0].value.clean();
		$('#EMAIL')[0].value = $('#EMAIL')[0].value.clean(); */
		if(submitForm('fm')) {
			/*
			if($('HAND_PHONE').value == "" && $('PHONE').value == "") {
				showErrMsg('HAND_PHONE','请填写手机号码','20');
				$('HAND_PHONE').select();
				return false;
			}
			*/
			//$('POSE_IDS').value == "" ? showError('ermsg','erdiv','addPosebtn','请为用户选择职位!',130) : sendAjax(surl,subBack,'fm');
			
			var dealerId = document.getElementById("dealerId").value;
			if(!dealerId) {
				MyAlert("请选择经销商！") ;
				return false ;
			}
			sendAjax(surl,subBack,'fm');
		}
	}
}

function isNumAndStr(elem){
    var str = elem.value; 
    var regexpUperStr=/[A-Z]+/;
    var reexpLowerStr=/[a-z]+/;
    var regexpNum=/\d+/;
    var uperStrFlag = regexpUperStr.test(str);
    var lowerStrFlag = reexpLowerStr.test(str);
    var numFlag = regexpNum.test(str);
    if((((uperStrFlag&&lowerStrFlag)||(lowerStrFlag&&numFlag)||(uperStrFlag&&numFlag)))&&str.length>=6){
		return true;
	 }
    return false;
  }

function subBack(redata) {
	if(redata.st != null && redata.st == "succeed") {
		toGoSysUserSearch();
	}else if(redata.st != null && redata.st == "acnt_error"){
		showError('ermsg','erdiv','ACNT','用户帐号重复,请重新输入!',170);
		$('ACNT').select();
	}else {
		MyAlert("未知错误");
	}
}


var dels = 2;
function showPose(poseIds) {
	delPalRow();
	var getPoseByIdsUrl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/addPoseQueryByIPoseIds.json";
	$('#POSE_IDS')[0].value = poseIds;
	if($('#POSE_IDS')[0].value != null && $('#POSE_IDS')[0].value != "") {
		sendAjax(getPoseByIdsUrl,backshowpose,'fm');
	}
}

function backshowpose(obj) {
	var list = obj.ps;
	objarr = null;
	objarr = new Array();
	if(list!=null) {
		for(var i=0; i<list.length; i++) {
			objarr.push(list[i].poseId);
			addPlanRow(list[i].poseCode,list[i].poseId,list[i].poseName);
		}
	}
}

function addPlanRow(poseCode,poseId,poseName)
{
	var addTable = $('#roll')[0];
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row1";
	var planrow = length+1;
	insertRow.id = poseId;
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	addTable.rows[length].cells[0].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+(length-1) +"</td>";
	addTable.rows[length].cells[1].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+poseCode+"</td>";
	addTable.rows[length].cells[2].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'>"+poseName+"</td>";
	addTable.rows[length].cells[3].innerHTML =  "<td class=table_info_label nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+(length)+");'>[删除]</a></td>";
}

function delPalRow() {
	var addTable = $('#roll')[0];
	var rowss = addTable.rows.length;
	for(dels; rowss > dels; rowss--) {
		addTable.deleteRow(rowss-1);
	}
}

function setPoseIds() {
	var poses = new Array();
	var addTable = $('#roll')[0];
	var rowss = addTable.rows.length;
	
	for(var i = 1; rowss > i; i++) {
		poses.push(addTable.rows[i].id);
	}
	if(poses.length > 0) {
		$('#POSE_IDS')[0].value = poses.toString();
	} else {
		$('#POSE_IDS')[0].value = "";
	}
}

function deleteRow(row){
	var addTable = $('#roll')[0];
	var rows = addTable.rows;
	addTable.deleteRow(row);
	for(var i = row;i<addTable.rows.length;i++){
		addTable.rows[i].cells[0].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'>"+(i-1) +"</td>";
		addTable.rows[i].cells[3].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+i+");'>[删除]</a></td>";
	}
}

function clearDiv() {
	$('#erdiv')[0].style.display = "none" ;
}

function addPose(path) {
	//var did = $("COMPANY_ID").value;
	/**if(!$defined("COMPANY_ID") || did == "") {
		//showError('ermsg','erdiv','COMPANY_NAME','请先为用户选择所属公司!',150);
		MyAlert("请先为用户选择所属公司!");
		return false;
	}**/
	var dealerId = document.getElementById("dealerId").value;
	if(!dealerId) {
		MyAlert("请选择经销商！") ;
		return false ;
	}
	setPoseIds();
	OpenHtmlWindow(path+"/sysmng/usemng/SgmDealerSysUser/addPoseQueryInit.do?POSE_IDS="+$('#POSE_IDS')[0].value+"&companyId="+dealerId,800,500);
}

function toGoSysUserSearch() {
	window.location = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/querySgmDealerSysUserInit.do";
}

function requery2() {
	$('#DRLCODE')[0].value="";
	$('#DELSNAME')[0].value="";
	$('#DEPT_ID')[0].value="";
}

function txtClr(value){
	document.getElementById(value).value = "";
}
</script>