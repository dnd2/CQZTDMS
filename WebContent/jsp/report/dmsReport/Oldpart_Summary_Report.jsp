<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
   function doInit(){
	  loadcalendar();  //初始化时间控件
    }
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/OldpartSummaryExport.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;结算汇总报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	
	<tr>
		<td width="10%" align="right">配件代码: </td>
		<td width="15%" > 
		    <input type="text" name="part_code" id="part_code" class="middle_txt"/>
		</td>
		<td width="10%">  </td>
		<td width="10%">  配件名称：</td>
		<td width="15%" align="right">
		    <input type="text" name="part_name" id="part_name" class="middle_txt"/>
		</td>
		<td width="15%">
		</td>
		<td width="5%">  </td>
		<td width="10%" align="right"> </td>
		<td width="15%" > 
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
    
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>


<form action="" id="fm1">
	<input type="hidden" name="year" id="year1"/>
	<input type="hidden" name="month" id="month1"/>

</form>

</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	var year=0;
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/OldpartSummaryReport.json?type=query";
	var title = null;
	var columns = [
        {header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "配件金额", dataIndex: 'PRICE', align:'center'},
		{header: "申报数量", dataIndex: 'PART_CODE_COUNT', align:'center'},
		{header: "申报总金额", dataIndex: 'BALANCE_AMOUNT', align:'center'}
	];
</script>
<!--页面列表 end -->
</html>