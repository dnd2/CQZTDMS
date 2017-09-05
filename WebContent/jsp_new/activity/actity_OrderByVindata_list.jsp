<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/RepairOrderAction/OrderByVindata.json?query=true";//url查询
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
	     {header: "VIN",dataIndex: 'VIN' ,align:'center'},
	     {header: "工单号",dataIndex: 'RO_NO' ,align:'center'},
		 {header: "服务活动代码", dataIndex: 'CAM_CODE', align:'center'},
	     {header: "服务活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'}
	];
	function doInit(){
	   __extQuery__(1);
	}

</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN管理
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
			<input type="hidden" id="vin" name="vin" value="${vin }"/>
		</td>
      	<td width="15%" nowrap="true">
	   	</td>
		<td width="15%"></td>
		<td width="15%"></td>
		<td width="15%"></td>
		<td width="15%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" onclick="_hide();"  name="bntReset" id="bntReset" value="关闭" class="normal_btn" />
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