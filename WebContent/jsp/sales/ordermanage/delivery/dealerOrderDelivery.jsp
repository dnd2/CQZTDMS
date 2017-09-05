<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单同步确认</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单调整 &gt;订单同步确认</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
<!-- 查询条件 end -->
<!--分页 begin -->

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderDeliveryCommand/dealerDeliveryQuery.json";
	var title = null;
	var columns = [
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{id:'action',header:"操作",sortable: false,dataIndex:'REQ_ID',align:'center',renderer:myLink}
		      ];
	//设置超链接  begin    
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>[查看]</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= '<%=contextPath%>/sales/ordermanage/delivery/OrderDeliveryCommand/dealerDeliveryToCheck.do?reqId='+value+'&orderType='+value2;
		$('fm').submit();
	}
	
</script>
<!--页面列表 end -->

</body>
</html>