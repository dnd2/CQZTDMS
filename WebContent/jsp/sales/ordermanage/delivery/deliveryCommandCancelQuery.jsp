<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令取消</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;发运指令取消</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">发运日期：</td>
			<td align="left" colspan="3">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="${dateStr}" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="${dateStr}" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">订单类型：</TD>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
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
			<td width="13%" align="right">发运单号：</TD>
			<td width="35%"><input type="text" name="orderNo" class="middle_txt" value=""/></TD>
			<td align="right">发运状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("deliveryStatus",<%=Constant.DELIVERY_STATUS%>,"-1",true,"short_sel",'',"false",'<%=Constant.DELIVERY_STATUS_01%>,<%=Constant.DELIVERY_STATUS_02%>,<%=Constant.DELIVERY_STATUS_06%>,<%=Constant.DELIVERY_STATUS_09%>,<%=Constant.DELIVERY_STATUS_13%>');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"></td>
			<td>
			</td>
			<TD>&nbsp;</TD>
			<td align="left">
				<input name="button2" id="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryCommandCancel/deliveryCommandCancelQuery.json";
	var title = null;
	var columns = [
				{header: "开票方代码",dataIndex: 'CODE',align:'center'},
				{header: "开票方名称",dataIndex: 'TTNAME',align:'center'},
				{header: "采购单位",dataIndex: 'ORDER_ORG_NAME',align:'center'},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "状态", dataIndex: 'DELIVERY_STATUS', align:'center',renderer:getItemValue},
				{header: "失败原因", dataIndex: 'LOSE_REASON', align:'center'},
				{header: "ERP订单号", dataIndex: 'ERP_ORDER', align:'center'},
				{header: "总价", dataIndex: 'TOTAL_PRICE', align:'center',renderer:amountFormat},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "发运时间", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'REQ_ID', align:'center',renderer:myMatchDetail}
		      ];
	//设置链接
	function myMatchDetail(value,meta,record){
	    return String.format("<a href='#' onclick='infoquery("+ value +","+ record.data.ORDER_TYPE + ")'>[取消]</a>");
	}
	//配车明细链接
	function infoquery(val1,val2){ 
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/DeliveryCommandCancel/deliveryCommandCancelInfo.do?reqId="+val1+"&orderType="+val2;
		$('fm').submit();
	}
	//设置超链接  begin
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>