<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/vinByDetail.json?query=true";
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "活动代码", dataIndex: 'ACTIVITY_CODE', align:'center'},
		{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
		{header: "VIN数", dataIndex: 'COUNT_VIN', align:'center',renderer:myLink}
	];
	function myLink(value,meta,record){
		var width=800;
		var height=400;
		var activity_code = record.data.ACTIVITY_CODE;
		var count_vin = record.data.COUNT_VIN;
		var ID="1";
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/ActivityAction/vinDetailByCount.do?activity_code="+activity_code+"&ID="
				+ ID + "\","+width+","+height+")' >"+count_vin+"</a>");
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN明细查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%"  nowrap="true">活动代码：</td>
      	<td width="15%" nowrap="true">
      		<input type="text" class="middle_txt" name="activity_code" id="activity_code" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">活动名称：</td>
      	<td width="15%" nowrap="true">
      		<input type="text" class="middle_txt" name="activity_name" id="activity_name" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
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
</html>