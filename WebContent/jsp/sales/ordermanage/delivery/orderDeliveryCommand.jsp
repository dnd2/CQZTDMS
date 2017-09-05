<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令下达</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		if('${startYear}'){
			document.getElementById("startYear").value='${startYear}';
		}
		if('${endYear}'){
			document.getElementById("endYear").value='${endYear}';
		}
		if('${startWeek}'){
			document.getElementById("startWeek").value='${startWeek}';
		}	
		if('${endWeek}'){
			document.getElementById("endWeek").value='${endWeek}';
		}
		if('${orderType}'){
			document.getElementById("orderType").value='${orderType}';
		}
		if('${areaId}'){
			document.getElementById("areaId").value='${areaId}';
		}
		if('${transportType}'){
			document.getElementById("transportType").value='${transportType}';
		}
		if('${groupCode}'){
			document.getElementById("groupCode").value='${groupCode}';
			document.getElementById("flag1").checked=true;
			toChangeDis1();
		}
		if('${materialCode}'){
			document.getElementById("materialCode").value='${materialCode}';
			document.getElementById("flag2").checked=true;
			toChangeDis2();
		}
		if('${dealerCode}'){
			document.getElementById("dealerCode").value='${dealerCode}';
		}
		if('${orderNo}'){
			document.getElementById("orderNo").value='${orderNo}';
		}
		__extQuery__(1);
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;发运指令下达</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right" width="20%">大区选择：</td>
			<td align="left">
				<select name="orgId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${orgIdList}" var="org">
						<option value="${org.ORG_ID}"><c:out value="${org.ORG_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">付款方：</td>
			<td>
				<input type="text"  name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true')" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td align="right">订单类型：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel",'-1',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">发运申请单号：</td>
			<td>
				<input type="text" name="orderNo" id="orderNo" class="middle_txt" value="" size="22"/>
			</td>
			<td align="right">运送方式：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right"></td>
			<td></td>
			<td><input type="hidden" name="materialId" id="materialId"/><input type="hidden" name="patchNo" id="patchNo"/></TD>
			<td width="30%" align="left"><input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
			<td></td>
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderDeliveryCommand/deliveryQuery.json";
	var title = null;
	var columns = [
				{header: "订货方", dataIndex: 'DEALER_NAME',align:'center'},
				{header: "付款方代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "付款方", dataIndex: 'DEALER_NAME1',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				//{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center'},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "收货方", dataIndex: 'DEALER_NAME2',align:'center'},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "资源审核人", dataIndex: 'UPDATE_BY', align:'center'},
				{id:'action',header:"操作",sortable: false,dataIndex:'REQ_ID',align:'center',renderer:myLink}
		      ];
	//设置超链接  begin    
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>[发运]</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= '<%=contextPath%>/sales/ordermanage/delivery/OrderDeliveryCommand/deliveryToQueryInit.do?reqId='+value+'&orderType='+value2;
		$('fm').submit();
	}
	
</script>
<!--页面列表 end -->

</body>
</html>