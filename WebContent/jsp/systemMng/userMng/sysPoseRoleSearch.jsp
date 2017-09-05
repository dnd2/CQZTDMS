<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>角色维护</title>
	
	<script>
		var parentContainer = parent || top;
		if( parentContainer.frames ['inIframe'] ){
			parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
		}
		var parentDocument =parentContainer.document;
		
		var url = "<%=contextPath%>/sysmng/sysposition/SysPosition/addSysPositionRoleSerch.json";
		var poseType = '<%=request.getParameter("poseType") %>';
		var dels = 2;
		function loadpage() {
			$('#roleType').html(getItemValue(poseType));
			sendAjax(url,callBack,'fm');
		}

		function checkselectAllBox(obj) {
			if(obj.id == "selectAll" && obj.checked == true) {
				for(var i=0; i<objarr.length; i++) {
					$("#"+objarr[i])[0].checked = true;
				}
			} else if(obj.id == "selectAll" && obj.checked == false) {
				for(var i=0; i<objarr.length; i++) {
					$("#"+objarr[i])[0].checked = false;
				}
			} else {
				if(obj.checked == false) {
					$('#selectAll')[0].checked = false;
				} else {
					var st = true;
					for(var i=0; i<objarr.length; i++) {
						if($(objarr[i]).checked == false) {
							st = false;
							break;
						}
					}
					st ? $('#selectAll')[0].checked = true : "";
				}
			}
		}

		function queryRole() {
			submitForm('fm') ? (delPalRow(),sendAjax(url,callBack,'fm')) : "";
		}

		function callBack(obj) {
			$('#selectAll').attr("checked",false);
			var list = obj.ps;
			objarr = null;
			objarr = new Array();
			if(list!=null) {
				for(var i=0; i<list.length; i++) {
					objarr.push(list[i].roleId+'');
					addPlanRow(list[i].roleName,list[i].roleId+'',list[i].roleDesc,list[i].roleType+'');
				}
			}
		}

		function add() {
			var myRole = null;
			if($('#ROLE_IDS').val() != "") {
				myRole = $('#ROLE_IDS').val().split(",");
			}
			var temp = new Array();
			for(var i=0; i<objarr.length; i++) {
			if($("#"+objarr[i])[0].checked == true) {
					temp.push(objarr[i]);
				}
			}
			if(myRole != null) {
				for(var i=0; i<myRole.length; i++)
				{
					temp.push(myRole[i]);
				}
			}
			var old_roleIds = __parent().document.getElementById("old_roleIds").value;
			if(old_roleIds){
				__parent().showFun(temp.toString()+","+old_roleIds);
			}else{
				__parent().showFun(temp.toString());
			}
			_hide();
		}

		var objarr;
		
		function delPalRow() {
			var addTable = $('#roll')[0];
			var rowss = addTable.rows.length;
			for(dels; rowss > dels; rowss--) {
				addTable.deleteRow(rowss-1);
			}
		}

		function addPlanRow(rolename,roleid,roledesc,roletype)
		{	
			var addTable = $('#roll')[0];
			var rows = addTable.rows;
			var length = rows.length;
			var insertRow = addTable.insertRow(length);
			insertRow.className = "table_list_row1";
			var planrow = length+1;
			insertRow.id = "planrow"+ planrow;
			insertRow.insertCell(0);
			insertRow.insertCell(1);
			insertRow.insertCell(2);
			insertRow.insertCell(3);
			addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'><label class='u-checkbox'><input type='checkbox' id="+roleid+" name='selectAll' onclick='checkselectAllBox(this)'/><span></span></label></td>";
			addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+rolename+"</td>";
			addTable.rows[length].cells[2].innerHTML =  "<td>"+roledesc+"</td>";
			addTable.rows[length].cells[3].innerHTML =  "<td nowrap='nowrap'>"+getItemValue(roletype)+"</td>";
		}

		function requery() {
			$('ROLE_DESC').value="";
		}
	</script>
</head>

<body onload="loadpage()">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 角色选择</div>
	<form id="fm" name="fm">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" name="rolep" id="rolep" value="<%=request.getParameter("poseType") %>" />
		<input type="hidden" id="ROLE_IDS" name="ROLE_IDS" value="${ROLE_IDS}" />
		<table class="table_query">
			<tr>
				<td>角色名称：</td>
				<td>
					<input name="ROLE_DESC" maxlength="30" datatype="1,is_noquotation,30" id="ROLE_DESC" type="text" class="middle_txt" /><span id="rolenameerr"></span>
				</td>
				<td>角色类别：</td>
				<td id="roleType"></td>
			</tr>
			<tr align="center">
				<td colspan="4">
					<input name="queryBtn" type="button" class="normal_btn" onclick="queryRole()" value="查 询" id="queryBtn" /> &nbsp; 
					<input type="button" class="normal_btn" onclick="requery()" value="重 置"/> &nbsp;
					<input name="button2" type="button" class="normal_btn" onclick="add()" value="添 加" /> &nbsp;
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
				</td>
			</tr>
		</table>
	</form>
	<div style="height: 200px; overflow-y: scroll;">
		<table class="table_list" style="border-bottom:1px solid #DAE0EE; "  id="roll">
			<tr class="table_list_th">
				<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;角色列表</td>
			</tr>
		  	<th><label class='u-checkbox'><input type="checkbox" id="selectAll" name="selectAll" onclick="checkselectAllBox(this)"/><span></span></label> </th>		
			<th>角色代码</th>
			<th>角色名称</th>
			<th>角色类别</th>
		</table>
	</div>
	<table width="100%">
		<tr align="center">
			<td>
				<input name="button2" type="button" class="normal_btn" onclick="add()" value="添 加" />
			</td>
		</tr>
	</table>
</body>
</html>