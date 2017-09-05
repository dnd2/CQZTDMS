<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>动态报表管理</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：数据管理 > 售后数据管理 > 数据管理</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" name="file" value="commonXml/BillTicketAll.xml" />
<input type="hidden" name="paramCode" value="userId" />
<input type="hidden" name="paramCode" value="dealerId" />
<table class="table_edit">
	<tr>
		<td width="10%" align="right">数据名称：</td>
		<td width="30%">
			<input type="text" class="middle_txt" id="reportName" name="reportName" datatype="0,is_null"/>
		</td>
		<td width="10%" align="right">使用权限：</td>
		<td>
			<select name="oemOnly" class="short_sel">
				<option value=10011001>厂端专用</option>
				<option value=10011002>共享数据</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">提出人：</td>
		<td width="30%">
			<input type="text" class="middle_txt" id="person" name="person"/>
		</td>
		<td width="10%" align="right">提及时间：</td>
		<td width="30%">
			<input type="text" name="mentionTime" id="CON_APPLY_DATE_START"
             value="" type="text" class="short_txt" 
             datatype="1,is_date,10"
             hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START', false);"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">数据说明：</td>
		<td width="40%">
			<textarea rows="4" cols="50" name="remark"></textarea>
		</td>
		<td width="10%" align="right">数据SQL：</td>
		<td width="60%" rowspan="2">
			<textarea rows="8" cols="50" name="sql"></textarea><label style="color:red">*</label>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">使用说明：</td>
		<td width="40%">
			<textarea rows="4" cols="50" name="remark2"></textarea>
		</td>
	</tr>
</table>
<br />
<a href="#" onclick="show('show1')">显示参数信息</a>
<table class="table_list" id="show1" style="display:block">
	<tr class="table_list_row2">
		<td>字段别名</td>
		<td>显示名称</td>
		<td>
			<input type="button" class="normal_btn" value="新增" onclick="addShow()" />
		</td>
	</tr>
</table>
<br />
<a href="#" onclick="show('input')">输入参数信息</a>
<table class="table_list" id="input" style="display:block">
	<tr class="table_list_row2">
		<td>参数代码</td>
		<td>参数名称</td>
		<td>输入数据类型</td>
		<td>默认值</td>
		<td>
			<input type="button" class="normal_btn" value="新增" onclick="addInput()" />
		</td>
	</tr>
</table>
<br />
<table class="table_edit">
	<tr>
		<td align="center" width="90%">
			<input type="button" value="确认" id="surBtn" class="normal_btn" onclick="doAdd()" />&nbsp;
			<input type="button" value="返回" id="bakBtn" class="normal_btn" onclick="goBack()" />
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
	function doAdd(){
		var sql = $('sql').value.trim();
		if(sql==null || sql=='' || sql=='null'){
			MyAlert('带*的为必填项') ;
			return ;
		}
		var arr1 = document.getElementsByName('otherName') ;
		for(var i=0;i<arr1.length;i++){
			if(arr1[i].value==null || arr1[i].value=='' || arr1[i].value=='null'){
				MyAlert('带*的为必填项') ;
				return ;
			}
		}
		var arr2 = document.getElementsByName('paramCode') ;
		for(var i=2;i<arr2.length;i++){
			if(arr2[i].value==null || arr2[i].value=='' || arr2[i].value=='null'){
				MyAlert('带*的为必填项') ;
				return ;
			}
		}
		var url = '<%=contextPath%>/report/reportmng/DynamicReportMng/reportInsert.json' ;
		makeNomalFormCall(url,addBack,'fm') ;
	}
	function addBack(json){
		if(json.flag){
			goBack() ;
		}else
			MyAlert('添加失败!请联系系统管理员') ;
	}
	function addShow(){
		var tab = $('show1') ;
		var length = tab.rows.length ;
		var insertRow = tab.insertRow(length) ;
		insertRow.insertCell(0) ;
		insertRow.insertCell(1) ;
		insertRow.insertCell(2) ;
		var curRow = tab.rows[length] ;
		if(length%2==1)
			insertRow.className = 'table_list_row1' ;
		else 
			insertRow.className = 'table_list_row2' ;
		curRow.cells[0].innerHTML = '<input type="text" name="otherName" class="middle_txt" /><label style="color:red">*</label>' ;
		curRow.cells[1].innerHTML = '<input type="text" name="showName" class="middle_txt" />' ;
		curRow.cells[2].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this,'+"show1"+')" />' ;
	}
	function addInput(){
		var tab = $('input') ;
		var length = tab.rows.length ;
		var insertRow = tab.insertRow(length) ;
		insertRow.insertCell(0) ;
		insertRow.insertCell(1) ;
		insertRow.insertCell(2) ;
		insertRow.insertCell(3) ;
		insertRow.insertCell(4) ;
		insertRow.insertCell(5) ;
		var curRow = tab.rows[length] ;
		if(length%2==1)
			insertRow.className = 'table_list_row1' ;
		else 
			insertRow.className = 'table_list_row2' ;
		curRow.cells[0].innerHTML = '<input type="text" name="paramCode" class="middle_txt" /><label style="color:red">*</label>' ;
		curRow.cells[1].innerHTML = '<input type="text" name="paramName" class="middle_txt" />' ;
		curRow.cells[2].innerHTML = '<select name="paramType" class="short_sel"><option value=13881001>文本框</option><option value=13881002>日期控件</option><option value=13881003>下拉列表</option><option value=13881004>多值输入框</option></select>' ;
		curRow.cells[3].innerHTML = '<input type="text" class="middle_txt" name="defaultValue" />' ;
		curRow.cells[4].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this,'+"input"+')" />' ;
	}
	function delRow(obj,id){
		$(id).deleteRow(obj.parentElement.parentElement.rowIndex);
	}
	function show(id){
		if($(id).style.display=='block')
			$(id).style.display = 'none' ;
		else
			$(id).style.display = 'block' ;
	}
	function goBack(){
		location = '<%=contextPath%>/report/reportmng/DynamicReportMng/reportMngInit.do' ;
	}
</script>
</body>
</html>