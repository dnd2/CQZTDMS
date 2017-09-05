<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 常规订单审核</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>周度<input type="hidden" name="areaId" id="areaId" value="<c:out value="${areaId}"/>"/>
					<input type="hidden" name="area" id="area" value="<c:out value="${area}"/>"/>
					<input type="hidden" name="dealerId" id="dealerId"/>
					</th>
			<th>配置代码</th>
			<th>配置名称</th>
			<th>生产计划数量</th>
			<th>可用资源数量</th>
			<th>提报数量</th>
			<th>已审核数量</th>
			<th>当前库存</th>
		</tr>
		<c:forEach items="${list}" var="list">
			<tr align="center" class="table_list_row2">
				<td><c:out value="${list.ORDER_YEAR}"/>.<c:out value="${list.ORDER_WEEK}"/>
					<input type="hidden" name="orderYear" id="orderYear" value="<c:out value="${list.ORDER_YEAR}"/>"/>
					<input type="hidden" name="orderWeek" id="orderWeek" value="<c:out value="${list.ORDER_WEEK}"/>"/>
				</td>
				<td><c:out value="${list.GROUP_CODE}"/><input type="hidden" name="groupId" id="groupId" value="<c:out value="${list.GROUP_ID}"/>"/></td>
				<td><c:out value="${list.GROUP_NAME}"/></td>
				<td><c:out value="${list.PLAN_AMOUNT}"/></td>
				<td><c:out value="${list.AMOUNT}"/></td>
				<td><c:out value="${list.ORDER_AMOUNT}"/></td>
				<td><span id="checkAmount"><c:out value="${list.CHECK_AMOUNT}"/></span></td>
				<td><c:out value="${list.STOCK_AMOUNT}"/></td>
			</tr>
		</c:forEach> 
	</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="button" name="button1" class="cssbutton" onclick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	//var calculateConfig = {subTotalColumns:"QUOTA_AMT,MIN_AMOUNT,ORDER_AMOUNT,CHECK_AMOUNT|GROUP_NAME"};
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderTotalDlrQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "配置代码",dataIndex: 'GROUP_CODE',align:'center'},
				{header: "配置名称",dataIndex: 'GROUP_NAME',align:'center'},
				{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'},
				{header: "最小提报量", dataIndex: 'MIN_AMOUNT', align:'center'},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center',renderer:myText1},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center',renderer:myText2},
				{header: "操作",sortable: false, dataIndex: 'DEALER_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo(\""+ value +"\")'>[调整]</a>");
	}
	function myText1(value,meta,record){
		return String.format("<span>"+value+"</span>");
	}
	function myText2(value,meta,record){
		return String.format("<span>"+value+"</span>");
	}
	var rowObjNum = null;
	var rowObj = null;
	function searchServiceInfo(value){
		var orderYear = document.getElementById("orderYear").value;
		var orderWeek = document.getElementById("orderWeek").value;
		var areaId = document.getElementById("areaId").value;
		var area = document.getElementById("area").value;
		var groupId = document.getElementById("groupId").value;
		rowObjNum = window.event.srcElement.parentElement.parentElement.rowIndex;
		rowObj = window.event.srcElement.parentElement.parentElement;
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailDlrQuery.do?&dealerId='+value+'&orderYear='+orderYear+'&orderWeek='+orderWeek+'&areaId='+areaId+'&area='+area+'&groupId='+groupId,700,500);
	}
	function doInit(){
		__extQuery__(1);
	}
	function toBack(){
		$('fm').action= '<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderCheckInit.do?&flag=1';
	 	$('fm').submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>