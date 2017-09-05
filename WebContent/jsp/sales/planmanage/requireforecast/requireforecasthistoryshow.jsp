<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>需求预测提报</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>需求预测>需求预测提报</div>
<form method="post" name="fm" id="fm">
   <table class="table_list" >
   <input type="hidden" name="groupId" value="${groupId }" />
    <input type="button" name="btn" value="关闭" onclick="_hide();" />
   </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径

	var url="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/forecastReportThreeMonthHistory.json";		
		
	var title = null;
	
	var columns=[
				{header: "配置代码", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "前三月平均预测数量", dataIndex: 'FORECAST_AMOUNT', align:'center'},
				{header: "前三月平均订单数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "前三月平均实销量", dataIndex: 'MATCH_AMOUNT', align:'center'},
				{header: "当前库存数量	", dataIndex: 'VEHICLE_AMOUNT', align:'center'}
		      ];
    function doInit(){
    	__extQuery__(1);
    }
    	
</script>
<!--页面列表 end -->
</body>
</html>
