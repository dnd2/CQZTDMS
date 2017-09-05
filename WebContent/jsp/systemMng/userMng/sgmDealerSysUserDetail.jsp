<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%
	String contextPath = request.getContextPath();
	TcUserPO userPO = (TcUserPO)request.getAttribute("user");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.po.TcUserPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script> --%>
<title>经销商用户维护</title>
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

<script type="text/javascript">
validateConfig.isOnBlur = false;
var poseIds = "<%=request.getAttribute("poseIds")%>";
var companyName = "<%=request.getAttribute("companyName")%>";
</script>

</head>

<body onload="pageload();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 系统管理 &gt; 经销商用户维护</div>
<form id="myfm" name="myfm">
<input id="POSE_IDS" name="POSE_IDS" type="hidden"/>
<input id="USER_ID" name="USER_ID" type="hidden" value="<%=userPO.getUserId() %>"/>
<!-- <input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="<%=userPO.getCompanyId() %>"/>  -->
<input id="dealerId" name="dealerId" type="hidden" value="<%=userPO.getDealerId() %>"/>
<div class="form-panel">
			<h2>基本信息</h2>
			<div class="form-body">
<table class="table_query" border="0">
<tr>
		<td class="right" >所属经销商：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
		${dealerPo.dealerName }
	  </td>
	  <td class="right">用户帐号：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" id="YHZH"><%=userPO.getAcnt() %></td>
	  <td class="right">密码：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" type="password" id="PASSWORD" name="PASSWORD" /><font color="red">*</font></td>
	</tr>
	<tr>
	<!--   <td class="table_query_3Col_label_4Letter" nowrap="nowrap">所属公司：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
		<input readonly="readonly" class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" value="${companyName }"/>
		<input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
	  </td>
	-->
	<td class="right">帐号是否锁定：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" >
	  	<select id="isLock" name="isLock" class="u-select" style="width:16px">
	  		<option value="1" <c:if test='${user.isLock == 1}'>selected="selected"</c:if>>是</option>
	  		<option value="0" <c:if test='${user.isLock == 0}'>selected="selected"</c:if>>否</option>
	  	</select>
	  </td>
	  <%-- <td class="right">EPC权限:</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><select name='epc_power' id='epc_power' class="u-select">
	  <c:if test="<%=userPO.getQueryOnly() == 10041002%>">
				<option value='10041001' >查看订单</option>
	  			<option value='10041002' selected>操作订单</option>
			</c:if>
			<c:if test="<%=userPO.getQueryOnly() != 10041002%>">
				<option value='10041001' selected>查看订单</option>
	  			<option value='10041002'>操作订单</option>
			</c:if>
	  </select></td>  --%>
	  <td class="right" nowrap="nowrap">状态：</td>
		<td >
		<script type="text/javascript">genSelBox("USER_STATUS",<%=Constant.STATUS%>,<%=userPO.getUserStatus() %>,false,"u-select","");</script></td>
		<td class="right">姓名：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="0,is_null,60" maxlength="60" type="text" id="NAME" name="NAME" value="<%=userPO.getName() %>" /></td>
	</tr>
	<tr>
	  <td class="right">E-mail：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input name="EMAIL"  datatype="1,is_email,49" maxlength="49" id="EMAIL" type="text" class="middle_txt" value="<%=userPO.getEmail() == null ? "" : userPO.getEmail() %>" /></td>
	  <td class="right">性别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
<script type="text/javascript"> genSelBox("GENDER",<%=Constant.GENDER_TYPE%>,<%=userPO.getGender() %>,false,"u-select","");</script></td>
<td class="right">手机：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt"  datatype="1,is_digit,11" maxlength="11" type="text" id="HAND_PHONE" name="HAND_PHONE" value="<%=userPO.getHandPhone() == null ? "" : userPO.getHandPhone() %>"/></td>
	</tr>
	<tr>
		
		<td class="right">电话：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" 
datatype="1,is_phone,19" maxlength="19" type="text" id="PHONE" name="PHONE" value="<%=userPO.getPhone() == null ? "" : userPO.getPhone() %>"/></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap" style="display: none">店面系统用户：</td>
		<td style="display: none">
		<select name="IS_DCS">
			<c:if test="<%=userPO.getIsDcs() == 0%>">
				<option value="0" selected>否</option>
				<option value="1">是</option>
			</c:if>
			<c:if test="<%=userPO.getIsDcs() == 1%>">
				<option value="0">否</option>
				<option value="1" selected>是</option>
			</c:if>
		</select>
		</td>
		<td class="right"></td>
		<td></td>
	</tr>
</table>
</div>
</div>
<div class="form-panel">
			<h2>用户职位</h2>
			<div class="form-body">
<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="roll">
	<th>序号</th>
	<th>职位代码</th>
	<th>职位名称</th>
	<th>操作</th>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td align="center"><input class="normal_btn" onmouseover="clearDiv()" id="addPosebtn" type="button" value="分配职位" onclick="addPose('<%=contextPath%>')"/>&nbsp;
		<input name="button2" type="button" class="normal_btn" onclick="sub('<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/modfiDealerSysUser.json')" value="保 存"/>&nbsp;
		<input name="button" type="button"  class="normal_btn" onclick="toGoSysUserSearch()" value="取 消"/></td>
	</tr>
</table>
</div>
</div>
</form>

<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
</div>
</body>
</html>
<script>

function sub(surl) {
	var password = document.getElementById("PASSWORD");
	if(password.value == undefined || password.value == null || password.value.trim() == "") {
		MyConfirm("密码为空将不会修改当前用户密码，是否继续？", doIt, [surl]) ;
	} else {
		if(!isNumAndStr(password)){
			MyAlert("密码至少有六个字符，必须同时包含字母和数字");
			return false;
		} else {
			MyConfirm("确认修改？", doIt, [surl]) ;
		}
	}
}

function doIt(surl) {
	setPoseIds();
	if(submitForm('myfm')) {
		if($('#NAME')[0].value == "") {
			MyAlert("请填写姓名");
			return false;
		}
		makeNomalFormCall(surl,subBack,'myfm');
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
		$('#ACNT')[0].select();
	} else {
		MyAlert("未知错误");
	}
}

var getPoseByIdsUrl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/addPoseQueryByIPoseIds.JSON";
var dels = 2;
function showPose(poseIds) {
	delPalRow();
	$('#POSE_IDS')[0].value = poseIds;
	if($('#POSE_IDS')[0].value != null && $('#POSE_IDS')[0].value != "") {
		makeNomalFormCall(getPoseByIdsUrl,backshowpose,'myfm');
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
	setPoseIds();
	OpenHtmlWindow(path+"/sysmng/usemng/SgmDealerSysUser/addPoseQueryInit.do?companyId="+<%=userPO.getDealerId() %>+"&POSE_IDS="+$('#POSE_IDS')[0].value,800,500);
}

function toGoSysUserSearch() {
	window.location = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/querySgmDealerSysUserInit.do";
}

function pageload() {
	$('#POSE_IDS')[0].value = poseIds;
	//$('JXS').setHTML(companyName);

	showPose($('#POSE_IDS')[0].value);
}

</script>