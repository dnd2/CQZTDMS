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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<title>订单数量审核</title>
</head>
<body onunload='javascript:destoryPrototype();  ' onload="__extQuery__(1);loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置： 整车销售 &gt; 潜客管理	&gt;订单管理 &gt;订单数量审核
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" id="curPaths" value="<%=contextPath%>" /> 
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" /> 
			<input type="hidden" id="userDealerId" name="userDealerId" value="${userDealerId}" /> 
			<input type="hidden" id="adviser" name="adviser" value="${adviser}" />
			<table class="table_query" border="0">
				<tr>
					<td class="table_query_3Col_label_6Letter">
						客户名称：
					</td>
					<td>
						<input type="text" name="customer_name" id="customer_name" />
					</td>
					<td align="left" class="table_query_3Col_label_6Letter">
						客户手机：
					</td>
					<td align="left" nowrap>
						<input type="text" name="telephone" id="telephone" />
					</td>
					<td class="table_query_3Col_label_6Letter">
						订单日期：
					</td>
					<td>
						<input name="orderDate" type="text" id="orderDate" 	class="short_txt" readonly />
						  <input class="time_ico" type="button" onClick="showcalendar(event, 'orderDate', false);" />
						 到：<input name="orderDateEnd" type="text" id="orderDateEnd" 	class="short_txt" readonly />
						  <input class="time_ico" type="button" onClick="showcalendar(event, 'orderDateEnd', false);" />
					</td>
					<td class="table_query_3Col_label_6Letter"></td>
					<td>
					</td>
				</tr>
				<tr>
					<td align="left" class="table_query_3Col_label_6Letter" nowrap>
						订单状态：
					</td>
					<td align="left" nowrap>
						<input type="hidden" id="order_status" name="order_status" value=""/>
		      			<div id="ddtopmenubar28" class="mattblackmenu">
							<ul> 
								<li>
									<a style="width:130px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6023', loadOrderStatus);" deftitle="--请选择--">
									--请选择--</a>
									<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
								</li>
							</ul>
						</div>
					</td>
					<c:if test="${adviserLogon=='yes' }">
					<td class="table_query_3Col_label_6Letter"></td>
					<td></td>
					</c:if>
					<c:if test="${adviserLogon=='no' }">
					<td class="table_query_3Col_label_6Letter">顾问：</td>
					<td>
						<select id="adviserId" name="adviserId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
					</c:if>
					<c:if test="${managerLogon=='yes' }">
					<td class="table_query_3Col_label_6Letter">分组：</td>
					<td>
						<select id="groupId" name="groupId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
					</c:if>
					<c:if test="${managerLogon=='no' }">
					<td class="table_query_3Col_label_6Letter"></td>
					<td></td>
					</c:if>
				</tr>
				<tr>
					<td colspan="7" align="center">
						<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
					</td>
					<td   align="right">页面大小：
					 <input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3" />
					</td>
				</tr>
			</table>
			<!--分页部分  -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/order/OrderManage/orderAmountFindQuery.json";
	var title = null;
	var columns = [
				{header: "订单编号", dataIndex: 'ORDER_ID', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "订单日期", dataIndex: 'ORDER_DATE', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center'},
				{header: "顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink}
		      ];
	
	function myLink(value,meta,record){
		if(record.data.ORDER_STATUS =="新增待审核") {
			return String.format("<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkDetailUrl(\""+record.data.ORDER_ID+"\")'>[详情]</a><a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkAuditUrl(\""+record.data.ORDER_ID+"\",\""+record.data.ORDER_STATUS2+"\")'>[审核]</a>");
		} else {
			return String.format("<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkDetailUrl(\""+record.data.ORDER_ID+"\")'>[详情]</a>");
		}
	}   
	function checkDetailUrl(orderId){
		window.location.href="<%=contextPath%>/crm/order/OrderManage/orderAmountDetailInit.do?orderId="+orderId;
	}
	function checkAuditUrl(orderId,orderStatus){
		window.location.href="<%=contextPath%>/crm/order/OrderManage/orderAmountAuditInit.do?orderId="+orderId+"&orderStatus="+orderStatus;
	}
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
</body>
</html>