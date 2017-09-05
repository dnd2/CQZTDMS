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
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportReportClaimCar.do?date=${date}&N=${N}&model=${model}&math=${math}&A=${A}";
		fm.submit();
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;索赔车辆信息
</div>
<form name="fm" id="fm">
<input type="hidden" name="date" value="${date}">
<input type="hidden" name="N" value="${N}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	
	
	
	<tr>
		
				<td width="15%"  class="table_query_2Col_label_8Letter" nowrap="true" >VIN：</td>
				<td width="15%"><input name="VIN" class="middle_txt" id="VIN"> </td>
				
		<td width="70%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum(1)" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="返回"  class="normal_btn" onclick="history.back();" >
    	</td>
    </tr>
  
    
    
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	function checkSum(){
			__extQuery__(1);
		
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryReportClaimCar.json?date=${date}&N=${N}&model=${model}&math=${math}&A=${A}";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "车架号", dataIndex: 'VIN', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "工单号", dataIndex: 'RO_NO', align:'center'},
		{header: "开单日期", dataIndex: 'RO_STARTDATE', align:'center',renderer:getDate},
		{header: "索赔类型", dataIndex: 'CODE_DESC', align:'center'}
	];
	function getDate(value,meta,record){
		return value.substring(0,10);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
	}

	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
</script>
<!--页面列表 end -->
</html>