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
	<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>角色维护</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="loadpage()">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 经销商角色选择</div>
<form id="fm" name="fm"><input type="hidden" name="curPage" id="curPage"
	value="1" />
	<input type="hidden" name="rolep" id="rolep" value="<%=Constant.SYS_USER_DEALER %>" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">角色名称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="ROLE_DESC" maxlength="30" datatype="1,is_noquotation,30" id="ROLE_DESC" type="text" class="middle_txt" /><span id="rolenameerr"></span></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">角色类别：</td>
		<td class="table_query_4Col_input" nowrap="nowrap" id="roleType">
		</td>
	</tr>
	<tr>
		<td class="table_query_4Col_input" colspan="4"
			style="text-align: center"><input name="queryBtn" type="button"
			class="normal_btn" onclick="queryRole()" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="button"
			class="normal_btn" onclick="requery()" value="重 置"/> &nbsp; 
		</td>
	</tr>
</table>
</form>
<br />

<table class="table_list" style="border-bottom:1px solid #DAE0EE"  border="0" id="roll">
	<tr class="table_list_th">
		<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;角色列表</td>
	</tr>
  	<th><input type="checkbox" id="selectAll" name="selectAll" onclick="checkselectAllBox(this)"/> </th>		
	<th>角色代码</th>
	<th>角色名称</th>
	<th>角色类别</th>
</table>
<br />
<table width="100%">
	<tr>
		<td class="table_query_4Col_input"
			style="text-align: center">
			<input
			name="button2" type="button" class="normal_btn"
			onclick="add()" value="添 加" />
		</td>
	</tr>
</table>
</body>
</html>
<script>
	var url = "<%=contextPath%>/sysmng/dealerposition/DealerPosition/addSysPositionRoleSerch.json";
	var dels = 2;
	function loadpage() {
		$('roleType').setHTML(getItemValue(<%=Constant.SYS_USER_DEALER %>));
		sendAjax(url,callBack,'fm');
	}

	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				$(objarr[i]).checked = true;
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				$(objarr[i]).checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
				var st = true;
				for(var i=0; i<objarr.length; i++) {
					if($(objarr[i]).checked == false) {
						st = false;
						break;
					}
				}
				st ? $('selectAll').checked = true : "";
			}
		}
	}

	function queryRole() {
		submitForm('fm') ? (delPalRow(),sendAjax(url,callBack,'fm')) : "";
	}

	function callBack(obj) {
		$('selectAll').checked = false;
		var list = obj.ps;
		objarr = null;
		objarr = new Array();
		if(list!=null) {
			for(var i=0; i<list.length; i++) {
				objarr.push(list[i].roleId);
				addPlanRow(list[i].roleName,list[i].roleId,list[i].roleDesc,list[i].roleType);
			}
		}
	}

	function add() {
		if($('selectAll').checked == true) {
			parentContainer.showFun(objarr.toString());
		} else {
			var temp = new Array();
			for(var i=0; i<objarr.length; i++) {
				if($(objarr[i]).checked == true) {
					temp.push(objarr[i]);
				}
			}
			parentContainer.showFun(temp.toString());
		}
		_hide();
	}

	var objarr;
	
	function delPalRow() {
		var addTable = $('roll');
		var rowss = addTable.rows.length;
		for(dels; rowss > dels; rowss--) {
			addTable.deleteRow(rowss-1);
		}
	}

	function addPlanRow(rolename,roleid,roledesc,roletype)
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
		insertRow.insertCell(3);
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'><input type='checkbox' id="+roleid+" name='selectAll' onclick='checkselectAllBox(this)'/></td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+rolename+"</td>";
		addTable.rows[length].cells[2].innerHTML =  "<td>"+roledesc+"</td>";
		addTable.rows[length].cells[3].innerHTML =  "<td nowrap='nowrap'>"+getItemValue(roletype)+"</td>";
	}

	function requery() {
		$('ROLE_DESC').value="";
	}
</script>