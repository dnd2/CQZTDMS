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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/crm/sysUser/user.js"></script>
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

<body onload="pageload();loadcalendar();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt; 岗位人员修改</div>
<form id="fm" name="fm">
<input id="POSE_IDS" name="POSE_IDS" type="hidden"/>
<input type="hidden" name="curPaths" id="curPaths" value="<%=contextPath%>"/>
<input id="USER_ID" name="USER_ID" type="hidden" value="<%=userPO.getUserId() %>"/>
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="<%=userPO.getCompanyId() %>"/>
<table class="table_query" border="0">
<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">用户帐号：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" id="YHZH"><%=userPO.getAcnt() %></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">密码：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" type="password" id="PASSWORD" name="PASSWORD" /></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">帐号是否锁定：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap">
<!--	  	<select id="isLock" name="isLock">-->
<!--	  		<option value="1" <c:if test='${user.isLock == 1}'>selected="selected"</c:if>>是</option>-->
<!--	  		<option value="0" <c:if test='${user.isLock == 0}'>selected="selected"</c:if>>否</option>-->
<!--	  	</select>-->
<!--	  	  <input type="hidden" id="isLock" name="isLock" value="${user.isLock}" />-->
<!--	      		<div id="ddtopmenubar1" class="mattblackmenu">-->
<!--					<ul> -->
<!--						<li>-->
<!--							<a style="width:148px;" rel="ddsubmenu1" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIsLock,'fm');" deftitle="--请选择--">-->
<!--							<c:if test="${user.isLock==1}">是</c:if><c:if test="${user.isLock==0}">否</c:if>-->
<!--							</a>-->
<!--							<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>-->
<!--						</li>-->
<!--					</ul>-->
<!--				</div> -->
				
				<select id="isLock" name="isLock" style="width:151px;">
					<c:if test="${user.isLock==1}"><option selected value="1">是</option><option value="0">否</option></c:if>
					<c:if test="${user.isLock==0}"><option selected value="0">否</option><option value="1">是</option></c:if>
				</select>
	  </td>
	</tr>
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">所属公司：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
		<input readonly="readonly" class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" value="${companyName }"/>
		<input class="mark_btn" style="display:none;" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
	  </td>
	
	
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">职位级别:</td>
	  <td class="table_query_3Col_input" nowrap="nowrap">
	  			
	   			<input type="hidden" id="poseRank" name="poseRank" value="${user.poseRank}" />
	      		<div id="ddtopmenubar2" class="mattblackmenu" style="width:132px; height:auto; float:left; display:inline">
					<ul> 
						<li>
							<a style="width:132px;" rel="ddsubmenu2" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6028', loadPoseRank,'fm');"  notselected="60281001,60281002" deftitle="--请选择--">
							<c:if test="${poseRank==null}">--请选择--</c:if><c:if test="${poseRank!=null}">${poseRank}</c:if>
							</a>
							<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div><div style="width:10px; height:auto; float:left; display:inline" ><font color="red" >*</font></div>
	     
	  </td>
	    <td class="table_query_3Col_label_4Letter" nowrap="nowrap" id="groupTd1">顾问小组：</td>
		<td class="table_query_3Col_input" nowrap="nowrap" id="groupTd2"><select name="groupId" id="groupId" style="width:151px;">
		  		<option value="">--请选择--</option>
		  		<c:forEach items="${tgpList}" var="po">
		  			<c:if test="${user.groupId==po.groupId}"><option value="${po.groupId}" selected >${po.groupName}</option> </c:if>
		  			<c:if test="${user.groupId!=po.groupId}"><option value="${po.groupId}">${po.groupName}</option></c:if>
		  		</c:forEach>
		  	</select></td>
	</tr>
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">姓名：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt"
      datatype="0,is_null,60" maxlength="60" type="text" id="NAME" name="NAME" value="<%=userPO.getName() %>" /></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">E-mail：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap"><input name="EMAIL" 
      datatype="1,is_email,49" maxlength="49" id="EMAIL" type="text" class="middle_txt" value="<%=userPO.getEmail() == null ? "" : userPO.getEmail() %>" /><font color="red">*</font></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">性别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
 				<input type="hidden" id="GENDER" name="GENDER" value="${user.gender}" />
	      		<div id="ddtopmenubar3" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:148px;" rel="ddsubmenu3" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1003', loadGender,'fm');" deftitle="--请选择--">
							<c:if test="${gender==null}">--请选择--</c:if><c:if test="${gender!=null}">${gender}</c:if>
							</a>
							<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div> 

</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">手机：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" 
             datatype="1,is_digit,11" maxlength="11" type="text" id="HAND_PHONE" name="HAND_PHONE" value="<%=userPO.getHandPhone() == null ? "" : userPO.getHandPhone() %>"/><font color="red">*</font></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">电话：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><input class="middle_txt" 
              datatype="1,is_phone,19" maxlength="19" type="text" id="PHONE" name="PHONE" value="<%=userPO.getPhone() == null ? "" : userPO.getPhone() %>"/></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">是否在职：</td>
		<td class="table_query_3Col_input" nowrap="nowrap" colspan="5">
<!--			<input type="hidden" id="USER_STATUS" name="USER_STATUS" value="${user.userStatus}" />-->
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
				<select id="USER_STATUS" name="USER_STATUS" style="width:151px;">
					<c:if test="${user.userStatus==10011001}"><option selected value="10011001">是</option><option value="10011002">否</option></c:if>
					<c:if test="${user.userStatus==10011002}"><option selected value="10011002">否</option><option value="10011001">是</option></c:if>
				</select>
			

</td>
	</tr>
	
	<tr>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">上级人员：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap" colspan="3">
	  		<input id="par_user_name" name="par_user_name" type="text" value="${parUser.name}" class="middle_txt"  size="30"  readonly="readonly"/> 
			<input id="par_user_id" name="par_user_id" value="${user.parUserId}" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toPoseList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('par_pose_name');" /><font color="red">&nbsp;*</font>
	  	
	  </td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">长安铃木入职时间：</td>
	  <td class="table_query_3Col_input" nowrap="nowrap">
		<input name="entryDate" type="text" id="entryDate"  class="small_txt" readonly value="${entryDate}"  />
		<input class="time_ico" type="button" onClick="showcalendar(event, 'entryDate', false);" /><font color="red">*</font>
	 </td>
	</tr>
</table>


<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="roll">
	<tr class="table_list_th">
		<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;用户职位<font color="red">&nbsp;*</font></td>
	</tr>	
	<tr>
		<th>序号</th>
		<th>权限代码</th>
		<th>权限名称</th>
		<th>操作</th>
	</tr>
	
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" id="poseTable">
	<tr >
		<td align="center"><input class="normal_btn" onmouseover="clearDiv()" id="addPosebtn" type="button" value="分配权限" onclick="addPose('<%=contextPath%>')"/>&nbsp;
		<input name="button2" type="button" class="normal_btn" onclick="sub('<%=contextPath%>/crm/sysUser/DealerSysUser/modfiDealerSysUser.json')" value="保 存"/>&nbsp;
		<input name="button" type="button" class="normal_btn" onclick="toGoSysUserSearch()" value="取 消"/></td>
	</tr>
</table>
</form>

<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar3", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar4", "topbar")</script> 
<script>
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
		MyAlert("请选择性别!!!");
		return ;
	}
	var poseRank=$('poseRank').value;
	if(poseRank==null||''==poseRank){
		MyAlert("职位级别不能为空!!!");
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
	var rows=document.getElementById("roll").rows.length;
		if(rows<=2){
			MyAlert("请选择一个权限！！");
			return;
		}
	var password = document.getElementById("PASSWORD");
	if(password.value == undefined || password.value == null || password.value.trim() == "") {
		if (!window.confirm("密码为空将不会修改当前用户密码，是否继续？")) {
			return false;
		} 
	}else {
		if(!isNumAndStr(password)){
			MyAlert("密码至少有六个字符，必须同时包含字母和数字");
			return false;
		}
	}
	setPoseIds();
//	$('NAME').value = $('NAME').value.clean();
//	$('HAND_PHONE').value = $('HAND_PHONE').value.clean();
//	$('PHONE').value = $('PHONE').value.clean();
//	$('EMAIL').value = $('EMAIL').value.clean();
	if(submitForm('fm')) {
		/*
		if($('HAND_PHONE').value == "" && $('PHONE').value == "") {
			showErrMsg('HAND_PHONE','请填写手机号码','20');
			$('HAND_PHONE').select();
			return false;
		}
		*/
		if($('NAME').value == "") {
			MyAlert("请填写姓名！！！");
			return false;
		}
		if($('poseRank').value == "") {
			MyAlert("请填写职位级别！！！");
			return false;
		}
		
		/*
		if($('HAND_PHONE').value == "") {
			MyAlert("请填写手机号码");
			return false;
		}
		*/
		//$('POSE_IDS').value == "" ? showError('ermsg','erdiv','addPosebtn','请为用户选择职位!',130) : sendAjax(surl,subBack,'fm');
		sendAjax(surl,subBack,'fm');
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
	} else {
		MyAlert("未知错误");
	}
}

var getPoseByIdsUrl = "<%=contextPath%>/crm/sysUser/DealerSysUser/addPoseQueryByIPoseIds.JSON";
var dels = 2;
function showPose(poseIds) {
	delPalRow();
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
	setPoseIds();
	OpenHtmlWindow(path+"/crm/sysUser/DealerSysUser/addPoseQueryInit.do?companyId="+<%=userPO.getCompanyId() %>+"&POSE_IDS="+$('POSE_IDS').value,800,500);
}

function toGoSysUserSearch() {
	window.location = "<%=contextPath%>/crm/sysUser/DealerSysUser/doInit.do";
}

function pageload() {
	$('POSE_IDS').value = poseIds;
	//$('JXS').setHTML(companyName);

	showPose($('POSE_IDS').value);
}
</script>
</body>



</html>
