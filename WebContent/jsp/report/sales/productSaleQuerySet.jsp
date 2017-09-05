<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.component.dict.CodeDict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>生产销售快报（新）</title>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/report/reportOne/ProductSalesReport/productSaleReportSetQuery.json";
	var title = null;
	
	var columns = [
	  	{header: "序号", align:'center', renderer: getIndex},
		{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
		{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'}
	];

	function doInit() {
		loadcalendar();
		__extQuery__(1);
	}
</script>
</head>
<body onunload="destoryPrototype()">
	<div class="navigation">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />
		&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;生产销售快报(新)
	</div>
	<form method="post" name="fm" id="fm">
		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>
</body>
</html>