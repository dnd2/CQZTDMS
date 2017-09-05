<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>详情</title>
</head>
<body  onload="__extQuery__(1);">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" /> 
				&nbsp;当前位置： 整车销售 &gt; 潜客管理	&gt;日程管理 &gt;任务管理&gt;顾问客户详情
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" id="curPaths" value="<%=contextPath%>" /> 
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
			<!--分页部分  -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
	<script type="text/javascript">
	
	var myPage;
	
	var url = "<%=contextPath%>/crm/taskmanage/TaskManage/showOverviewInfo.json?adviser=${adviser}&flag=${flag}";
	
	var title = null;
	
	var columns = [
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "顾问", dataIndex: 'NAME', align:'center'},
				{header: "任务名称", dataIndex: 'CODE_DESC', align:'center'},
				{header: "日期", dataIndex: 'REMINDDATE', align:'center'},
				{header: "数量", dataIndex: 'REMINDNUM', align:'center'}
				];
	 
	</script>
</body>
</html>