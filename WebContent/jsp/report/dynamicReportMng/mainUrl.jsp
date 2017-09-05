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
<title>动态数据管理</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：数据管理 > 售后数据管理 > 数据查询</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" name="file" value="commonXml/BillTicketAll.xml"></input>
<table class="table_edit">
	<tr>
		<td width="10%" align="right">数据名称：</td>
		<td width="30%">
			<input type="text" class="middle_txt" id="reportName" name="reportName" />
		</td>
		<td width="20%" align="center">
			<input type="button" id="queryBtn" value="查询" class="normal_btn" onclick="__extQuery__(1)"/>&nbsp;
			<input type="button" value="新增" class="normal_btn" onclick="addReport()" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
	var url = '<%=contextPath%>/report/reportmng/DynamicReportMng/reportQuery.json' ;

	var title = null ;

	var columns = [
	               {header:'序号',renderer:getIndex},
	               {header:'数据名称',dataIndex:'REPORT_NAME'},
	               {header:'数据说明',dataIndex:'REMARK',renderer:fmtLength},
	               {header:'操作',renderer:myHandler}
		           	] ;

	__extQuery__(1) ;
	
	function fmtLength(value){
		if(value.length<50)return value;
		else return value.substring(0,50)+'...' ;
	}
	
	function myHandler(value,meta,rec){
		var id = rec.data.REPORT_ID ;
		var str = '' ;
		str += '<a href="<%=contextPath%>/report/reportmng/DynamicReportMng/updateReportUrl.do?id='+id+'">[修改]</a>&nbsp;' ;
		str += '<a href="<%=contextPath%>/report/reportmng/DynamicReportMng/showReport.do?id='+id+'">[查看]</a>&nbsp;' ;
		return str ;
	}
	function addReport(){
		location = '<%=contextPath%>/report/reportmng/DynamicReportMng/addReport.do' ;
	}
</script>
</body>
</html>