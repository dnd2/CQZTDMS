<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->
<script type="text/javascript" >
var claimNo = "${claimNo}";
var myPage;
//查询路径
var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/findLogInfoByclaimNo.json?claimNo="+claimNo;
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作人", dataIndex: 'USER_NAME', align:'center'},
  				{header: "旧供应商代码", dataIndex: 'OLD_PRODUCER_CODE', align:'center'},
  				{header: "新供应商代码", dataIndex: 'NEW_PRODUCER_CODE', align:'center'},
  				{header: "旧供应商名", dataIndex: 'OLD_PRODUCER_NAME', align:'center'},
  				{header: "新供应商名", dataIndex: 'NEW_PRODUCER_NAME', align:'center'},
  				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
	      ];
	__extQuery__(1);
		
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;修改日志
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
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