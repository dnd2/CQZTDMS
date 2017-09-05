<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购订单发运查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 整车销售 > 结算中心管理 > 采购订单管理 >采购订单发运查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td width="13%"></td>
			<td align="right" nowrap="nowrap">订单周度：</td>
			<td align="left" nowrap="nowrap">
				<select name="startYear">
			       <c:forEach items="${yearList}" var="list1">
				       	<option value="${list1.orderYear}" <c:if test="${list1.orderYear==year}">selected</c:if>>
				       		<c:out value="${list1.orderYear}"/>
				       	</option>
			       </c:forEach>
				</select>年
				<select name="startWeek">
					<c:forEach items="${weekList}" var="list2">
			      	 	<option value="${list2.orderWeek}" <c:if test="${list2.orderWeek==week}">selected</c:if>>
			      	 		<c:out value="${list2.orderWeek}"/>
			      	 	</option>
			       </c:forEach>
				</select>周&nbsp;至
				<select name="endYear">
				   <c:forEach items="${yearList}" var="list3">
			      	 	<option value="${list3.orderYear}" <c:if test="${list3.orderYear==year}">selected</c:if>>
			      	 		<c:out value="${list3.orderYear}"/>
			      	 	</option>
			       </c:forEach>
				</select>年
				<select name="endWeek">
					<c:forEach items="${weekList}" var="list4">
			      	 	<option value="${list4.orderWeek}" <c:if test="${list4.orderWeek==week}">selected</c:if>>
			      	 		<c:out value="${list4.orderWeek}"/>
			      	 	</option>
			       </c:forEach>  
				</select>周
			</td>
			<td width="13%" align="right">选择物料组：</td>
			<td width="35%">
				<input type="text"  name="groupCode" size="15" readonly="readonly" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','3');" value="..." />
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">发运日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">运送方式：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">订单号码：</TD>
			<td><input type="text" id="orderNo" name="orderNo" datatype="1,is_textarea,18" value=""/></TD>
			<td align="right">发运状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("deliveryStatus",<%=Constant.DELIVERY_STATUS%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="5">
				<input type="hidden" id="orderType" name="orderType" value="<%=Constant.ORDER_TYPE_02 %>">
				<input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
		</tr>
	</table>
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryQuery/orderDeliveryQuery.json";
	var title = null;
	var columns = [
				{header: "组织代码",dataIndex: 'CODE',align:'center'},
				{header: "组织名称",dataIndex: 'TTNAME',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center'},
				{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center'},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "订做车批次号", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				{header: "计划数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'MATCH_AMOUNT', align:'center'},
				{header: "总价", dataIndex: 'TOTAL_PRICE', align:'center'},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "状态", dataIndex: 'DELIVERY_STATUS', align:'center',renderer:getItemValue},
				{header: "发运时间", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'DETAIL_ID', align:'center',renderer:myLink}
		      ];
	//设置超链接  begin 
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo(\""+ value +"\")'>[车辆明细]</a>");
	}     
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>