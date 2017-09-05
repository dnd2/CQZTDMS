<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>动态数据管理</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：数据管理 > 售后数据管理 > 数据管理</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" name="reportId" value="${report.reportId}" />
<input type="hidden" name="userId" value="${userId}" />
<input type="hidden" name="dealerId" value="${dealerId}" />
<table class="table_edit">
	<tr>
		<td width="10%" align="right">数据名称：</td>
		<td width="30%">${report.reportName}</td>
		<td width="10%" align="right">使用权限：</td>
		<td>
			<c:if test="${10011001==report.oemOnly}">厂端专用</c:if>
			<c:if test="${10011002==report.oemOnly}">共享数据</c:if>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">提出人：</td>
		<td width="30%">${report.mentionPerson }</td>
		<td width="10%" align="right">提及时间：</td>
		<td width="30%"><fmt:formatDate pattern="yyyy-MM-dd" value="${report.mentionTime }"/></td>
	</tr>
	<tr>
		<td width="10%" align="right">数据说明：</td>
		<td width="90%" colspan="3">${report.remark}</td>
	</tr>
	<tr>
		<td width="10%" align="right">使用说明：</td>
		<td width="90%" colspan="3">${report.remark2}</td>
	</tr>
	<tr>
		<td width="10%" align="right">数据SQL：</td>
		<td width="60%" rowspan="2">
			<textarea rows="8" cols="50" name="sql">${sql }</textarea><label style="color:red">*</label>
		</td>
	</tr>
</table>
<br />
<a href="#" onclick="showM('show')">显示参数信息</a>
<table class="table_list" id="show" style="display:block">
	<tr class="table_list_row2">
		<td>字段别名</td>
		<td>显示名称</td>
	</tr>
	<c:forEach var="show" items="${list2}" varStatus="st">
		<tr class="table_list_row${st.index%2+1}">
			<td>${show.otherName}</td>
			<td>${show.showName}</td>
		</tr>
	</c:forEach>
</table>
<br />
<a href="#" onclick="showM('input')">输入参数信息</a>
<table class="table_list" id="input" style="display:block">
	<c:forEach var="input" items="${list3}" varStatus="st">
		<tr class="table_list_row${st.index%2+1}">
			<td>${input.paramName}</td>
			<td>
				<c:if test="${input.paramType==13881001}">
					<input type="text" name="${input.paramCode}" class="middle_txt" />
				</c:if>
				<c:if test="${input.paramType==13881002}">
					<input type="text" name="${input.paramCode}" id="${input.paramCode}" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, '${input.paramCode}', false);"/>
				</c:if>
				<c:if test="${input.paramType==13881003}">
					<script>
						genSelBoxExp("${input.paramCode}",'${input.defaultValue}',"",true,"short_sel","","false",'');
					</script>
				</c:if>
				<c:if test="${input.paramType==13881004}">
					<textarea name="${input.paramCode}" rows="2" cols="40"></textarea>
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>
<br />
<a href="#" onclick="showM('sqlUpdate')">转换后SQL</a><label style="color:red">(为了验证此数据SQL与输入参数的一一对应性，可以SQL转换一下)</label>
<table class="table_edit" id="sqlUpdate" style="display:block">
	<tr>
		<td width="90%">
			<label id="sql2"></label>
		</td>
	</tr>
</table>
<table class="table_edit">
	<tr>
		<td align="center" width="90%">
			<input type="button" value="SQL转换" class="normal_btn" onclick="sqlUpdate()" />&nbsp;
			<input type="button" value="删除" class="normal_btn" onclick="delReport()" />&nbsp;
			<input type="button" value="返回" class="normal_btn" onclick="goBack()" />
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
	function sqlUpdate(){
		var url = '<%=contextPath%>/report/reportmng/DynamicReportMng/sqlUpdate.json' ;
		makeNomalFormCall(url,sqlBack,'fm') ;
	}
	function sqlBack(json){
		$('sql2').innerHTML = json.flag;
	}
	function showM(id){
		if($(id).style.display=='block')
			$(id).style.display = 'none' ;
		else
			$(id).style.display = 'block' ;
	}
	function delReport(){
		if(confirm('确认删除此数据？')==true){
			location = '<%=contextPath%>/report/reportmng/DynamicReportMng/deleteReport.do?id=${report.reportId}' ;
		}
	}
	function goBack(){
		location = '<%=contextPath%>/report/reportmng/DynamicReportMng/reportMngInit.do' ;
	}
</script>
</body>
</html>