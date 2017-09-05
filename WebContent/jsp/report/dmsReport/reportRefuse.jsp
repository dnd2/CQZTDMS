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
		fm.action="<%=contextPath%>/report/dmsReport/Application/expotReportRefuse.do";
		fm.submit();
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;查询拒赔明细
</div>
<form name="fm" id="fm">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	
	<!-- 查询条件 -->
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">供应商编号：</td>
      	<td width="15%" nowrap="true">
      		<input name="supply_code" type="text" id="supply_code"  class="middle_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">供应商名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="supply_name" type="text" id="supply_name"  class="middle_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
<!-- 			<input name="out_part_code" type="text" id="out_part_code"  class="middle_txt" maxlength="30"/> -->
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
      	<td width="15%" nowrap="true">
      		<input name="claim_no" type="text" id="claim_no"  class="middle_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">车架号：</td>
      	<td width="15%" nowrap="true">
      		<input name="vin" type="text" id="vin"  class="middle_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
<!-- 			<input name="out_part_code" type="text" id="out_part_code"  class="middle_txt" maxlength="30"/> -->
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配件名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="part_name" type="text" id="part_name"  class="middle_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">配件编号：</td>
      	<td width="15%" nowrap="true">
      		<input name="part_code" type="text" id="part_code"  class="middle_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
<!-- 			<input name="out_part_code" type="text" id="out_part_code"  class="middle_txt" maxlength="30"/> -->
		</td>
		<td width="12.5%"></td>
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
	
	function doInit()
	{
		__extQuery__(1);
	}   
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/reportRefuse.json";
	var title = null;
	var columns = [
		       		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		       		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		       		{header: "拒赔数", dataIndex: 'RETURN_AMOUNT', align:'center'},
		       		{header: "车架号", dataIndex: 'VIN', align:'center'},
		       		{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
		       		{header: "供应商编号", dataIndex: 'PRODUCER_CODE', align:'center'},
		       		{header: "配件编号", dataIndex: 'PART_CODE', align:'center'},
		       		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'}
		       	];
	function getYear(value,meta,record){
		return value.substring(0,4);
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