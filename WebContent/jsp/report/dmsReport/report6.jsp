<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData6.do";
       fm.submit();
	}
	function expotDataOut(){
		OpenHtmlWindow('<%=contextPath%>/jsp/report/dmsReport/report6_1.jsp?',800,500);
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;通用导出入
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<!-- 查询条件 -->
	<tr>
		<td width="40%"></td>
	<!-- 	<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td> -->
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">旧件回运单号：</td>
		<td width="15%">
			<input name="return_no" type="text" id="return_no"  maxlength="30"  class="middle_txt"/>
		</td>
		<td width="45%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导入"  class="normal_btn" onClick="expotDataOut();" >
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
	var url = "<%=contextPath%>/report/dmsReport/Application/report6.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "主键列", dataIndex: 'ID', align:'center'},
				{header: "索赔申请单", dataIndex: 'CLAIM_NO', align:'center'},
  				{header: "回运数",dataIndex: 'RETURN_AMOUNT',align:'center'},
  				{header: "签收数", dataIndex: 'SIGN_AMOUNT', align:'center'},
  				{header: "装箱单号",dataIndex: 'BOX_NO',align:'center'},
  				{header: "扣除原因",dataIndex: 'DEDUCT_REMARK',align:'center'},
  				{header: "责任性质",dataIndex: 'IS_MAIN_CODE',align:'center',renderer:getItemValue},
  				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "编号",dataIndex: 'BARCODE_NO',align:'center'},
  				{header: "存放库位",dataIndex: 'LOCAL_WAR_HOUSE',align:'center'},
  				{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
  				{header: "维修日期",dataIndex: 'RO_STARTDATE',align:'center'},
  				{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}
	];
	
</script>
<!--页面列表 end -->
</html>