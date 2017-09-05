<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/crm/sysUser/user.js"></script>
<!-- 下拉树组件必须的js和css[结束] -->
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
</head>

<body onunload='javascript:destoryPrototype();' onload="loadcalendar();" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt; 岗位人员新增</div>
<form id="fm" name="fm">
<input id="POSE_IDS" name="POSE_IDS" type="hidden"/>
<input type="hidden" name="curPaths" id="curPaths" value="<%=contextPath%>"/>
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${companyPO.companyId}"/>
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">用户帐号：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="0,is_digit_letter,20" maxlength="20" onkeydown="clearDiv()" type="text" id="ACNT" name="ACNT" /></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">密码：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" type="password" id="PASSWORD" name="PASSWORD"/></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">长安铃木入职时间：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input name="entryDate" type="text" id="entryDate"  class="small_txt" readonly   />
		          	<input class="time_ico" type="button" onClick="showcalendar(event, 'entryDate', false);" /><font color="red">*</font>
		          	</td>
	</tr>
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">所属公司：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><div nowrap>
	  <input class="middle_txt"  id="COMPANY_NAME" datatype="0,is_null" disabled  value="${companyPO.companyShortname }" style="cursor: pointer;" name="COMPANY_NAME" type="text"/>
	  <input class="mark_btn"  style="display:none;" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
	  </div></td>
	 <td class="table_query_3Col_label_4Letter" nowrap="nowrap">职位级别：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap">
	          <input type="hidden" id="poseRank" name="poseRank" value="${user.poseRank}" />
	      		<div id="ddtopmenubar2" class="mattblackmenu" style="width:135px; height:auto; float:left; display:inline">
					<ul> 
						<li>
							<a style="width:148px;" rel="ddsubmenu2" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6028', loadPoseRank,'fm');"  notselected="60281001,60281002" deftitle="--请选择--">
							<c:if test="${poseRank==null}">--请选择--</c:if><c:if test="${poseRank!=null}">${poseRank}</c:if>
							</a>
							<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div><div style="width:10px; height:auto; float:left; display:inline" ><font color="red" >*</font></div>
	  </td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap" id="groupTd1">顾问小组：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" id="groupTd2">
	  		<select name="groupId" id="groupId" style="width:155px;" onchange="getParId()">
		  		<option value="">--请选择--</option>
		  		<c:forEach items="${tgpList}" var="po" >
		  			<option value="${po.groupId}">${po.groupName}</option>
		  		</c:forEach>
		  	</select>
	  </td>
	  
	</tr>
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">姓名：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt"
datatype="0,is_letter_cn,10" maxlength="10" type="text" id="NAME" name="NAME" /></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">E-mail：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input name="EMAIL" datatype="1,is_email,49" maxlength="49" id="EMAIL" type="text" class="middle_txt" /><font color="red">*</font></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">性别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><span id="xxbb">
				<input type="hidden" id="GENDER" name="GENDER" value="${user.gender}" />
	      		<div id="ddtopmenubar3" class="mattblackmenu" style="width:150px; height:auto; float:left; display:inline" >
					<ul> 
						<li>
							<a style="width:148px;" rel="ddsubmenu3" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1003', loadGender,'fm');" deftitle="--请选择--">
							<c:if test="${gender==null}">--请选择--</c:if><c:if test="${gender!=null}">${gender}</c:if>
							</a>
							<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div> <div style="width:10px; height:auto; float:left; display:inline" ><font color="red" >*</font></div>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">手机：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="1,is_digit,11" maxlength="11" type="text" id="HAND_PHONE" name="HAND_PHONE"/><font color="red">*</font></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">电话：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" datatype="1,is_phone,19" maxlength="19" type="text" id="PHONE" name="PHONE" /></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">&nbsp;</td>
		<td class="table_query_3Col_input" nowrap="nowrap" colspan="5">
		<span id="zztt">
		<input type="hidden" id="USER_STATUS" name="USER_STATUS" value="10011001" />
<!--	      		<div id="ddtopmenubar4" class="mattblackmenu">-->
<!--					<ul> -->
<!--						<li>-->
<!--							<a style="width:148px;" rel="ddsubmenu4" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1001', loadStatus,'fm');" deftitle="--请选择--">-->
<!--							<c:if test="${status==null}">--请选择--</c:if><c:if test="${status!=null}">${status}</c:if>-->
<!--							</a>-->
<!--							<ul id="ddsubmenu4" class="ddsubmenustyle"></ul>-->
<!--						</li>-->
<!--					</ul>-->
<!--				</div> -->
		</td>
	</tr>
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">上级人员：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" >
	  		<input id="par_user_name" name="par_user_name" type="text" value="" class="middle_txt"  size="30"  readonly="readonly"/> 
			<input id="par_user_id" name="par_user_id" value="0" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toPoseAddList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('par_pose_name');" />
		 <font color="red" >*</font>
	  	
	  </td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap"></td>
	  <td class="table_query_3Col_input" nowrap="nowrap"></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap"></td>
	  <td class="table_query_3Col_input" nowrap="nowrap">
	  </td>
	</tr>
</table>
</form>

<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="roll">
	<tr class="table_list_th">
		<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;用户职位</td>
	</tr>
	<tr>
		<th>序号</th>
		<th>职位代码</th>
		<th>职位名称</th>
		<th>操作</th>
	</tr>	
	
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td align="center"><input class="normal_btn" onmouseover="clearDiv()" id="addPosebtn" type="button" value="分配权限" onclick="addPose('<%=contextPath%>')"/>&nbsp;<input name="button2" type="button" 
class="normal_btn" onclick="sub('<%=contextPath%>/crm/sysUser/DealerSysUser/addSgmDealerSysUser.json')" value="保 存"/>&nbsp;<input name="button" type="button" style="width:60px;height:16px;line-height:10px;background-color:#EEF0FC;border:1px solid #5E7692;color:#1E3988;" 
class="normal_btn" onclick="toGoSysUserSearch()" value="取 消"/></td>
	</tr>
</table>
<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar3", "topbar")</script> 
<!--<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar4", "topbar")</script> -->
</body>
</html>
<script>
validateConfig.isOnBlur = false;
var drlurl = "<%=contextPath%>/crm/sysUser/DealerSysUser/drlQuery.json?COMMAND=1";
var tree_url = "<%=contextPath%>/crm/sysUser/DealerSysUser/initOrgTree.json";

function sub(surl) {

	var par_user_name=$('par_user_name').value;
	var email=$('EMAIL').value;
	var entryDate=$("entryDate").value;
	
	var poseRank = $("poseRank").value;
	var groupId = $("groupId").value;
	
	if(poseRank == 60281003 || poseRank == 60281004 ){
		if(groupId == null || '' == groupId ){
			MyAlert("请选择组!!!");
			return false;
		}
	}
	
	if(email==null||''==email){
		MyAlert("请填写邮箱!!!");
		return ;
	}
	var gender=$('GENDER').value;
	if(gender==null||''==gender){
		MyAlert("请选择性别!!!")
		return ;
	}
	
	var poseRank=$('poseRank').value;
	if(poseRank==null||''==poseRank){
		MyAlert("职位级别不能为空!!!")
		return ;
	}
	var HAND_PHONE=$('HAND_PHONE').value;
	if(HAND_PHONE==null||''==HAND_PHONE){
		MyAlert("手机不能为空!!!")
		return ;
	}
	if(poseRank!='60281001'&&par_user_name==''){
		MyAlert("请选择该人员的上级！！！");
		return;
	}
	if(entryDate==null||''==entryDate){
		MyAlert("请选择长安铃木入职日期！！！");
		return;
	
	}
	
	
	var password = document.getElementById("PASSWORD");
	if(!isNumAndStr(password)){
		MyAlert("密码至少有六个字符，必须同时包含字母和数字");
	}else{
		setPoseIds();
		var rows=document.getElementById("roll").rows.length;
		if(rows<=2){
			MyAlert("请选择一个权限！！");
			return;
		}
		//$('ACNT').value = $('ACNT').value.clean();
		//$('NAME').value = $('NAME').value.clean();
		//$('HAND_PHONE').value = $('HAND_PHONE').value
		//$('PHONE').value = $('PHONE').value.clean();
		//$('EMAIL').value = $('EMAIL').value.clean();
		if(submitForm('fm')) {
			/*
			if($('HAND_PHONE').value == "" && $('PHONE').value == "") {
				showErrMsg('HAND_PHONE','请填写手机号码','20');
				$('HAND_PHONE').select();
				return false;
			}
			*/
			//$('POSE_IDS').value == "" ? showError('ermsg','erdiv','addPosebtn','请为用户选择职位!',130) : sendAjax(surl,subBack,'fm');
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
		location.href="<%=contextPath%>/crm/sysUser/DealerSysUser/doInit.do";
	}else if(redata.st != null && redata.st == "acnt_error"){
		//showError('ermsg','erdiv','ACNT','用户帐号重复,请重新输入!',170);
		//$('ACNT').select();
		MyAlert("用户账户重复，请重新输入！！！");
	}else {
		MyAlert("未知错误");
	}
}


var dels = 2;
function showPose(poseIds) {
	MyAlert("poseIds==="+poseIds);
	delPalRow();
	var getPoseByIdsUrl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/addPoseQueryByIPoseIds.json";
	$('POSE_IDS').value = poseIds;
	if($('POSE_IDS').value != null && $('POSE_IDS').value != "") {
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
	var addTable = $('roll');
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
	var addTable = $('roll');
	var rowss = addTable.rows.length;
	for(dels; rowss > dels; rowss--) {
		addTable.deleteRow(rowss-1);
	}
}

function setPoseIds() {
	var poses = new Array();
	var addTable = $('roll');
	var rowss = addTable.rows.length;
	for(dels; rowss > dels; rowss--) {
		poses.push(addTable.rows[rowss-1].id);
	}
	if(poses.length > 0) {
		$('POSE_IDS').value = poses.toString();
	} else {
		$('POSE_IDS').value = "";
	}
}

function deleteRow(row){
	var addTable = $('roll');
	var rows = addTable.rows;
	addTable.deleteRow(row);
	for(var i = row;i<addTable.rows.length;i++){
		addTable.rows[i].cells[0].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'>"+(i-1) +"</td>";
		addTable.rows[i].cells[3].innerHTML = "<td class=table_info_input nowrap=nowrap align='center'><a href='javascript:void(0)' target='_self' onclick='deleteRow("+i+");'>[删除]</a></td>";
	}
}

function clearDiv() {
	$('erdiv').setStyle("display","none");
}

function addPose(path) {

	var rows=document.getElementById("roll").rows.length;
	if(rows>=3){
		MyAlert("只能选择一个权限！！");
		return;
	}
	var did = $("COMPANY_ID").value;
	//if(!$defined("COMPANY_ID") || did == "") {
	//	showError('ermsg','erdiv','COMPANY_NAME','请先为用户选择所属公司!',150);
	//	return false;
	//}
	setPoseIds();
	OpenHtmlWindow(path+"/crm/sysUser/DealerSysUser/addPoseQueryInit.do?companyId="+did+"&POSE_IDS="+$('POSE_IDS').value,800,500);
}

function toGoSysUserSearch() {
	window.location.href = "<%=contextPath%>/crm/sysUser/DealerSysUser/doInit.do";
}

function requery2() {
	$('DRLCODE').value="";
	$('DELSNAME').value="";
	$('DEPT_ID').value="";
}
function doCusChange(value){
		if(value==60281004){
			document.getElementById("groupTd1").style.display="inline";
			document.getElementById("groupTd2").style.display="inline";
		}else{
			document.getElementById("groupTd1").style.display="none";
			document.getElementById("groupTd2").style.display="none";
		}
}


/*$(function(){
	MyAlert("sss");
	$("#groupId").change(function(){
		getParId();
	});

	
})*/




</script>