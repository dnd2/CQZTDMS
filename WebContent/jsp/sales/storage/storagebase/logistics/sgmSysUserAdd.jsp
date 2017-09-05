<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>用户维护</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";
</script>
<style>
.img {
	border: none
}
</style>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 储运管理 &gt; 基础管理 &gt; 物流人员维护</div>
<form id="myfm" name="myfm">
	<input id="POSE_IDS" name="POSE_IDS" type="hidden" value=""/>
<div class="form-panel">
	<h2>物流人员维护</h2>
	<div class="form-body">
	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">用户帐号：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input class="middle_txt" datatype="0,is_digit_letter,20" maxlength="20" onkeydown="clearDiv()" type="text" id="ACNT" name="ACNT" />
			</td>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">密码：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input class="middle_txt" type="password" id="PASSWORD" name="PASSWORD" value="1a2b3c"/>
			</td>
			<td class="table_query_3Col_label_4Letter" nowrap="nowrap"></td>
			<td class="table_query_3Col_input" nowrap="nowrap"></td>
		</tr>
		<tr>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">姓名：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input class="middle_txt" datatype="0,is_letter_cn,10" maxlength="10" type="text"  id="NAME" name="NAME" />
			</td>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">E-mail：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input name="EMAIL" datatype="1,is_email,49" maxlength="49" id="EMAIL" type="text"  class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">性别：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<script type="text/javascript"> 
					genSelBox("GENDER",<%=Constant.GENDER_TYPE%>,"",false,"","");
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">手机：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input class="middle_txt" datatype="1,is_digit,11" maxlength="11" type="text"  id="HAND_PHONE" name="HAND_PHONE"/>
			</td>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">电话：</td>
			<td class="table_query_3Col_input" nowrap="nowrap">
				<input class="middle_txt" datatype="1,is_phone,19" maxlength="19" type="text"  id="PHONE" name="PHONE" />
			</td>
			<td class="table_query_3Col_label_4Letter right" nowrap="nowrap">状态：</td>
			<td class="table_query_3Col_input" nowrap="nowrap" colspan="5">
				<script type="text/javascript">
					genSelBox("USER_STATUS",<%=Constant.STATUS%>,"",false,"","");
				</script>
			</td>
		</tr>
	</table>
	</div>
</div>
</form>

<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="roll">
	<tr class="table_list_th">
		<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;用户职位</td>
	</tr>	
	<th>序号</th>
	<th>职位代码</th>
	<th>职位名称</th>
	<th>操作</th>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td align="center">
			<input class="normal_btn" onmouseover="clearDiv()" id="addPosebtn" type="button" value="分配职位" onclick="addPose('<%=contextPath%>')"/>&nbsp;
			<input name="button2" type="button" class="normal_btn" onclick="sub('<%=contextPath%>/sales/storage/storagebase/LogisticUserManage/addSgmSysUser.json')" value="保 存"/>&nbsp;
			<input name="button" type="button" class="normal_btn" onclick="toGoSysUserSearch()" value="取 消"/></td>
	</tr>
</table>

<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
</div>
</body>
</html>
<script>

function sub(surl) {
	var password = document.getElementById("PASSWORD");
	var POSE_IDS = document.getElementById("POSE_IDS").value;
	if(!isNumAndStr(password)){
		if(POSE_IDS.indexOf('2010082600119486')>-1){
			MyAlert("密码至少有八个字符，必须同时包含字母和数字");
		}else{
			MyAlert("密码至少有六个字符，必须同时包含字母和数字");
		}
	}else{
		setPoseIds();
		//$('#ACNT')[0].value = "";
		//$('#NAME')[0].value = "";
		//$('#HAND_PHONE')[0].value = "";
		//$('#PHONE')[0].value = "";
		//$('#EMAIL')[0].value = "";
		if(submitForm('myfm')) {
			$('#POSE_IDS')[0].value == "" ? showError('ermsg','erdiv','addPosebtn','请为用户选择职位!',130) : sendAjax(surl,subBack,'myfm');
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
    var POSE_IDS = document.getElementById("POSE_IDS").value;
    if(((uperStrFlag&&lowerStrFlag)||(lowerStrFlag&&numFlag)||(uperStrFlag&&numFlag))){
	     if(POSE_IDS.indexOf('4000008240')>-1){
		     if(str.length>=8){
			     return true;
			 }
		 }else{
			 if(str.length>=6){
				 return true;
				 }
	     }
	 }
    return false;
  }

function subBack(redata) {
	if(redata.st != null && redata.st == "succeed") {
		toGoSysUserSearch();
	}else if(redata.st != null && redata.st == "acnt_error"){
		showError('ermsg','erdiv','ACNT','用户帐号重复,请重新输入!',170);
		document.getElementById('ACNT').select();
	} else {
		MyAlert("位置错误");
	}
}

var getPoseByIdsUrl = "<%=contextPath%>/sales/storage/storagebase/LogisticUserManage/addPoseQueryByIPoseIds.JSON";
var dels = 2;
function showPose(poseIds) {
	delPalRow();
	$('#POSE_IDS')[0].value = poseIds;
	if($('#POSE_IDS')[0].value != null && $('POSE_IDS').value != "") {
		sendAjax(getPoseByIdsUrl,backshowpose,'myfm');
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
	for(dels; rowss > dels; rowss--) {
		poses.push(addTable.rows[rowss-1].id);
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
	$('#erdiv')[0].setStyle("display","none");
}

function addPose(path) {
	setPoseIds();
	OpenHtmlWindow(path+"/sales/storage/storagebase/LogisticUserManage/addPoseQueryInit.do?POSE_IDS="+$('#POSE_IDS')[0].value,800,500);
}

function toGoSysUserSearch() {
	window.location = "<%=contextPath%>/sales/storage/storagebase/LogisticUserManage/querySgmSysUserInit.do";
}

</script>