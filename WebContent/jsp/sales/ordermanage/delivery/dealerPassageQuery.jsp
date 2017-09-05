<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途车辆信息查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body onload="loadcalendar();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;在途车辆信息查询</div>
<form method="post" name="fm" id="fm">
<input name="sendCarsNo" type="hidden" id="sendCarsNo"  class="middle_txt" value=""/>
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">ERP订单号：</td>
			<td>
				<input type="text" name="erpNo" class="middle_txt" id="erpNo" value="" />
			</td>
			<td align="right">送车交接单号：</TD>
			<td align="left">
				<input type="text" name="sendcarNo" class="middle_txt" id="sendcarNo" value="" />
			</td>
		</tr>
		<tr>
		  <td class="table_query_2Col_label_6Letter">发运日期： </td>
         <td nowrap="nowrap">
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);">
          </td>
          
          <td class="table_query_2Col_label_6Letter">采购订单号： </td>
          <td align="left">
          <input type="text" name="orderNo" class="middle_txt" id="orderNo" value="" />
          </td>
		</tr>
		<tr>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td class="table_query_2Col_label_6Letter">发运状态：</td>
	          <td align="left" >
	          <script type="text/javascript">
	            genSelBoxExp("delivery_status",<%=Constant.DELIVERY_STATUS%>,"${delivery_status }",true,"short_sel","","true",'<%=Constant.DELIVERY_STATUS_01%>,<%=Constant.DELIVERY_STATUS_02%>,<%=Constant.DELIVERY_STATUS_03%>,<%=Constant.DELIVERY_STATUS_04%>,<%=Constant.DELIVERY_STATUS_06%>,<%=Constant.DELIVERY_STATUS_07%>,<%=Constant.DELIVERY_STATUS_08%>,<%=Constant.DELIVERY_STATUS_09%>,<%=Constant.DELIVERY_STATUS_13%>');
	           </script>
	          </td>
		</tr>
		
		<tr><td align="right">&nbsp;</td>
			<td align="left">
				<input name="button2" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td></tr>
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/dealerPassageQuery.json";
	
	var title = null;
	
	var columns = [
				{header: "序号",dataIndex: 'NUM',align:'center'},
				{header: "送车交接单",dataIndex: 'SENDCAR_ORDER_NUMBER',align:'center',renderer:myHref},
				{header: "销售订单号",dataIndex: 'ORDER_NUMBER',align:'center'},
				{header: "发运类型",dataIndex: 'DELIVERY_TYPE',align:'center',renderer:getItemValue},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "发运时间", dataIndex: 'FLATCAR_ASSIGN_DATE', align:'center'},
				{header: "财务审核时间", dataIndex: 'FINANCE_CHK_DATE', align:'center'},
				{header: "承运单位", dataIndex: 'SHIP_METHOD_CODE', align:'center'},
				{header: "发车数量", dataIndex: 'DE_AMOUNT', align:'center'},
				{header: "实际在途", dataIndex: 'DEING_AMOUNT', align:'center'},
				{header: "平板车号", dataIndex: 'FLATCAR_ID', align:'center'},
				{header: "发车仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "一级采购单位", dataIndex: 'BILLING_DLR_NAME', align:'center'},
				{header: "二级采购单位", dataIndex: 'ORDER_DLR_NAME', align:'center'},
				{header: "收车单位", dataIndex: 'RECEIVE_DLR_NAME', align:'center'},
				{header: "收车地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "司机名称", dataIndex: 'MOTORMAN', align:'center'},
				{header: "司机电话", dataIndex: 'MOTORMAN_PHONE', align:'center'},
				{header: "单据情况", dataIndex: 'STATUS', align:'center'}
		      ];

	function remarkCode()
	{
		document.getElementById("dealerCode").value = "";
	}
	function myHref(value,meta,record){
	      		var link = "<a href='#' onclick='view("+record.data.SENDCAR_ORDER_NUMBER+");'>"+value+"</a>";
	      		return String.format(link);
	}      
	function view(sendCarNo){
		$('sendCarsNo').value=sendCarNo;
      	OpenHtmlWindow("<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/showSendCarsDetail.do?sendCarNo="+sendCarNo,800,500);
      	//OpenHtmlWindow("<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/viewVehiclePinRequest?pinId="+pinId+"&userId="+userId,800,500);
    }
</script>
<!--页面列表 end -->
</body>
</html>