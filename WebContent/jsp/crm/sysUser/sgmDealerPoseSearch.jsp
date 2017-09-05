<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
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
<title>职位查询</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="loadpage()">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt; 权限查询</div>
<form id="fm" name="fm">
<input type="hidden" value="<%=request.getAttribute("poseIds") %>" id="poseIds" name="poseIds" />
<input type="hidden" id="companyId" name="companyId" value="<%=request.getAttribute("comapnyID")%>" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">权限代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="POSE_CODE" maxlength="30" datatype="1,is_noquotation,30" id="POSE_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">权限名称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap" >
		<input name="POSE_NAME" maxlength="30" datatype="1,is_noquotation,30" id="POSE_NAME" type="text" class="middle_txt" />
		</td>
	</tr>
	<tr>
		<td class="table_query_4Col_input" colspan="4" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="queryPose()" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="button" class="normal_btn" onclick="requery()" value="重 置"/> &nbsp;
			<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
		</td>
	</tr>
</table>
</form>
<br />
<table class="table_list" style="border-bottom:1px solid #DAE0EE"  border="0" id="roll">
	<tr class="table_list_th">
		<td colspan="3"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;权限列表</td>
	</tr>
  	<th>
  	<input type="checkbox" id="selectAll" name="selectAll" onclick="checkselectAllBox(this)" style="display:none;"/>
  	
  	</th>		
	<th>权限代码</th>
	<th>权限名称</th>
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
	var url = "<%=contextPath%>/crm/sysUser/DealerSysUser/addPoseQuery.json";
	var dels = 2;
	var mypose = null;
	
	function loadpage() {
		sendAjax(url,callBack,'fm');
		if($('poseIds').value != "") {
			mypose = $('poseIds').value.split(",");
		}
	}

	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				document.getElementById(objarr[i]).checked = true;
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				document.getElementById(objarr[i]).checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
				var st = true;
				for(var i=0; i<objarr.length; i++) {
					if(document.getElementById(objarr[i]).checked == false) {
						st = false;
						break;
					}
				}
				st ? $('selectAll').checked = true : "";
			}
		}
	}

	function queryPose() {
		delPalRow();
		sendAjax(url,callBack,'fm');
	}

	function callBack(obj) {
		$('selectAll').checked = false;
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

	function add() {
		if($('selectAll').checked == true) {
			if(mypose != null) {
				objarr.extend(mypose);
			}
			parent.$('inIframe').contentWindow.showPose(objarr.toString());
		} else {
			var temp = new Array();
			for(var i=0; i<objarr.length; i++) {
				if(document.getElementById(objarr[i]).checked == true) {
					temp.push(objarr[i]);
				}
			}
			if(mypose != null) {
				temp.extend(mypose);
			}
			parent.$('inIframe').contentWindow.showPose(temp.toString());
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

	function addPlanRow(poseCode,poseId,poseName)
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
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'><input type='radio' id="+poseId+" name='selectAll' onclick='checkselectAllBox(this)'/></td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+poseCode+"</td>";
		addTable.rows[length].cells[2].innerHTML =  "<td>"+poseName+"</td>";
	}

	function requery() {
		$('POSE_NAME').value="";
		$('POSE_CODE').value="";
	}
</script>