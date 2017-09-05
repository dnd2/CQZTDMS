<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotReportOutStore.do";
       fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;出库明细报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">供应商代码：</td>
      	<td width="15%" nowrap="true">
      		<input name="supply_code" type="text" id="supply_code"  class="middle_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">供应商名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="supply_name" type="text" id="supply_name"  class="middle_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">零件号:</td>
		<td width="15%" nowrap="true">
			<input name="out_part_code" type="text" id="out_part_code"  class="middle_txt" maxlength="30"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">零件名称：</td>
      	<td width="15%" nowrap="true">
      		<input name="out_part_name" type="text" id="out_part_name"  class="middle_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">索赔单号：</td>
      	<td width="15%" nowrap="true">
      		<input name="claim_no" type="text" id="claim_no"  class="middle_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">退赔单号</td>
		<td width="15%" nowrap="true">
			<input name="range_no" type="text" id="range_no"  class="middle_txt" maxlength="30"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%"  nowrap="true">出库开始时间：</td>
      	<td width="15%" nowrap="true">
      		<input name="out_date_start" type="text" id="out_date_start"  readonly="readonly" onfocus="calendar();" class="middle_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">出库结束时间：</td>
      	<td width="15%" nowrap="true">
      		<input name="out_date_end" type="text" id="out_date_end"  readonly="readonly" onfocus="calendar();" class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true"></td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;
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
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/report/dmsReport/Application/reportOutStore.json?query=true";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
		{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
		{header: "退赔单号", dataIndex: 'RANGE_NO', align:'center'},
		{header: "零件号", dataIndex: 'OUT_PART_CODE', align:'center'},
		{header: "零件名称", dataIndex: 'OUT_PART_NAME', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "车架号", dataIndex: 'VIN', align:'center'},
		{header: "数量", dataIndex: 'OUT_AMOUT', align:'center'},
		{header: "故障现象", dataIndex: 'TROUBLE_REASON', align:'center'},
		{header: "原因分析", dataIndex: 'REMARK', align:'center'},
		{header: "出库时间", dataIndex: 'OUT_DATE', align:'center'}
	];
</script>
<!--页面列表 end -->
</html>