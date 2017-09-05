<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.infodms.dms.po.TcUserPO"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
	TcUserPO tcUserPO = (TcUserPO)request.getAttribute("user");
	SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String lastTime = "";
	if(tcUserPO.getLastsigninTime() != null) {
		lastTime = formate.format(tcUserPO.getLastsigninTime());
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<title>个人资料</title>
<style>
.img {
	border: none
}
</style>
</head>

<script type="text/javascript">
	var psoeSearch = "<%=contextPath%>/individualInfo/individual/IndiviDual/getPoseByUserId.json";
	var thisPose = "<%=request.getAttribute("thisPose")%>";
</script>

<body onload="loadPose();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息 &gt; 个人信息 &gt; 个人资料</div>
	<form id="myfm" name="myfm" action="<%=contextPath%>/common/MenuShow/menuDisplay.do" method="post">
	<input type="hidden" value="<%=tcUserPO.getUserId() %>" id="userId" name="userId" />
	<input value="" type="hidden" name="deptId" />
	<input value="" type="hidden" name="poseId" />
</form>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_3Letter" nowrap="nowrap">用户名：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><%=tcUserPO.getEmpNum() %></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">最后登录时间：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><%=lastTime %></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">部门：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><%=(String)request.getAttribute("deptName") %></td>
	</tr>
	<tr>
	  <td class="table_query_3Col_label_3Letter" nowrap="nowrap">姓名：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap"><%=tcUserPO.getName() %></td>
	  <td class="table_query_3Col_label_6Letter" nowrap="nowrap">性别：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap" id="sb"><script>$('sb').setHTML(getItemValue(<%=tcUserPO.getGender() %>))</script></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">职位：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap"><%=(String)request.getAttribute("poseNmae") %></td>
	  </tr>
	<tr>
	  <td class="table_query_3Col_label_3Letter" nowrap="nowrap">手机：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap"><%=tcUserPO.getHandPhone()==null ? "" : tcUserPO.getHandPhone() %></td>
	  <td class="table_query_3Col_label_6Letter" nowrap="nowrap">电话：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap"><%=tcUserPO.getPhone()==null ? "" : tcUserPO.getPhone() %></td>
	  <td class="table_query_3Col_label_4Letter" nowrap="nowrap">E-mail：</td>
	  <td class="table_query_4Col_input" nowrap="nowrap"><%=tcUserPO.getEmail()==null ? "" : tcUserPO.getEmail() %></td>
	  </tr>
</table>
</div>

<table class="table_list" style="border-bottom:1px solid #DAE0EE"  border="0" id="roll">
	<tr class="table_list_th">
		<td colspan="3"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;职位列表</td>
	</tr>
	<th>序号</th>
	<th>职位名称</th>
	<th>职位选择</th>
</table>
<table class="table_list" style="border-bottom:1px solid #DAE0EE"  border="0" id="roll">
	<tr class="table_list_th">
		<td colspan="3"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;<a onclick="aaa()" href='javascript:void(0)'>[修改密码]</a></td>
	</tr>
</table>
</body>
<script type="text/javascript">
	var dels = 2;
	function loadPose() {
		sendAjax(psoeSearch,callBack,'myfm');
	}

	function callBack(obj) {
		if(obj.ps == null) {
			return false;
		}
		var list = obj.ps
		if(list!=null) {
			for(var i=0; i<list.length; i++) {
				addPlanRow(list[i].poseName,list[i].poseId,list[i].deptId);
			}
		}
	}

	function addPlanRow(poseName,poseId,deptid)
	{	
		var addTable = $('roll');
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		var planrow = length+1;
		insertRow.id = "planrow"+ planrow;
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'>"+(length-1)+"</td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+poseName+"</td>";
		if(thisPose == poseId) {
			addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'><a href='javascript:void(0)' onclick='showFunn("+poseId+","+deptid+")'>[功能列表]</a></td>";
		} else {
			addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'><a href='javascript:void(0)' onclick='showFunn("+poseId+","+deptid+")'>[功能列表]</a>&nbsp;<a href='javascript:void(0)' onclick='swpose("+poseId+","+deptid+")'>[职位切换]</a></td>";
		}
	}

	function showFunn(poseid,deptid) {
		OpenHtmlWindow("<%=contextPath%>/individualInfo/individual/IndiviDual/showFunInit.do?poseid="+poseid,800,600);
	}

	function swpose(poseid,deptid) {
		parent.window.location.href = "<%=contextPath%>/common/MenuShow/menuDisplay.do?deptId="+deptid+"&poseId="+poseid;
	}

	function aaa() {
		parent.window.location.href = "http://universedev.shanghaigm.com/pkmspasswd";
		//OpenHtmlWindow("http://universedev.shanghaigm.com/pkmspasswd");
	}
</script>
</html>