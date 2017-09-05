<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;紧急调件清单
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="20%"></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">借件单号：</td>
		<td width="15%">
			<input name="borrowNo" type="text" id="borrowNo" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">提报时间：</td>
      	<td width="10%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="5%" class="table_query_2Col_label_6Letter" nowrap="true">至</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="25%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
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
	var url = "<%=contextPath%>/claim/oldPart/EmergencyDevice/listShowData.json?action=choose&dealer_id="+${dealer_id};
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
		{header: "申请部门", dataIndex: 'APPLY_DEPT', align:'center'},
		{header: "借件人", dataIndex: 'BORROW_PERSON', align:'center'},
		{header: "借件部门", dataIndex: 'BORROW_DEPT', align:'center'},
		{header: "收件人", dataIndex: 'CONSIGNEE_PERSON', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
		{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
		{header: "下发时间", dataIndex: 'NEXT_TIME', align:'center',renderer:show}
	];
	function show(value,meta,record){
		if(value==""||value==null){
			return String.format('--');
		}else{
			return String.format(value);
		}
	}
	//超链接设置
	   function myLink(value,meta,record){
			return String.format("<input type='radio' name='id' onclick='setDataShow(\""+value+"\")' />");
	}
	function setDataShow(id){
		if (parent.$('inIframe')) {
 			parentContainer.setDataShow(id);
 		 } else {
			parent.setDataShow(id);
		 }
		 parent._hide();
	}
</script>
<!--页面列表 end -->
</html>