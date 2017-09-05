<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户关怀活动小项明细</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
<div id="loader" style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;客户关怀活动小项明细</div>
<form method="post" name="fm" id="fm">
<table class="table_query">
	<tr>
		<td width="15%" align="right">活动主题：</td>
		 <td width="30%" >
			<input type="text" readonly="readonly" name="subjectName" id="subjectName" class="long_txt"/>
			<input type="hidden" name="subjectId" id="subjectId"/>
			<span style="color:red">*</span>
			<input type="button" class="mini_btn" value="..." onclick="showsubjectId();"/>
		    <input type="button" class="normal_btn" value="清除" onclick="wrapOut2();"/>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询" onclick="queryPer();" />&nbsp;
		    <input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载" onclick="exportExcel();" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /></form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/report/service/ActivityReport/activityReportQuery.json?type=0";
				
	var title = null;

	var columns = [
				{header: "服务站简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "主题名称", dataIndex: 'SUBJECT_NAME', align:'center'},
				{header: "主题开始时间", dataIndex: 'SDATE', align:'center'},
				{header: "主题结束时间", dataIndex: 'EDATE', align:'center'},
				{header: "车主", dataIndex: 'OWNER_NAME', align:'center'},
				{header: "车主电话", dataIndex: 'OWNER_PHONE', align:'center'},
				{header: "活动项目", dataIndex: 'PRO_NAME', align:'center'},
				{header: "实际数量", dataIndex: 'TOTAL', align:'center'},
				{header: "实际金额", dataIndex: 'AMOUNT', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		            
	function exportExcel(){
	if($('subjectId').value==""){
  		MyAlert("请选择活动主题!");
  		return false;
  	}else{
		exportExcelDo();
	}
	}
	
function exportExcelDo(){
	fm.action = "<%=contextPath%>/report/service/ActivityReport/activityReportQuery.do?type=1";
		fm.submit();
}
  function queryPer(){
  if($('subjectId').value==""){
  MyAlert("请选择活动主题!");
  return false;
  	}else{
	 	 __extQuery__(1);
	 	 }
	}
	function wrapOut2()
	{
	  document.getElementById('subjectName').value = '';
	  document.getElementById('subjectId').value = '';
	}
	function showsubjectId()
	{
		OpenHtmlWindow('<%=contextPath%>/report/service/ActivityReport/subjectOpen.do',800,460);
	}
</script>
</body>
</html>