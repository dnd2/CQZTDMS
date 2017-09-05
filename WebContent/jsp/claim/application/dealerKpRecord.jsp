<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后开票</title>
<script type="text/javascript">
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;服务站开票单维护查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="20%"></td>
	    <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结算编号</td>
		<td width="20%">
			<input type="text" class="middle_txt" name="balance_no" maxlength="25" id="balance_no"/>
		</td> 
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="20%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="20%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
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
	var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpRecordData.json";
	var title = null;
	var columns = [  
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "操作人", dataIndex: 'USER_NAME', align:'center'},
		{header: "操作时间", dataIndex: 'CREATE_TIME', align:'center'},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "结算编号", dataIndex: 'BALANCE_NO', align:'center'}
	];
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>