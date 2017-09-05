<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>真实发运申请修改</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核 &gt;真实发运申请修改</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">发运申请单号：</td>
			<td>
				<input type="text" class="middle_txt" style="width:140px" name="delivryReqNo"  value=""/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td></td>
            <td></td>
            <td align="center">
                <input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
                <input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
<%--                <input name="button2" type=button class="cssbutton" onClick="getDetail();" value="下载">--%>
            </td>
            <td></td>
        <td align="right">
             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/DsOrderDelvyModify/getOrderDeliveryModifyQuery.json";
	var title = null;
	var columns = [
				{header: "开票单位代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "开票单位名称",dataIndex: 'DEALER_NAME',align:'center'},
<!--				{header: "采购单位名称",dataIndex: 'ORDER_DEALER_NAME',align:'center'},-->
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "申请数量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
<!--				{header: "已保留数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},-->
<%--				{header: "已发运数量", dataIndex: 'SHIP_NUMBER', align:'center'},--%>
<%--				{header: "资金类型", dataIndex: 'FUND_TYPE', align:'center'}, --%>
<!--				{header: "订单总价", dataIndex: 'REQ_TOTAL_PRICE', align:'center',renderer:myformat},-->
				{header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
	//设置金钱格式
    function myformat(value,metaDate,record){
        return String.format(amountFormat(value));
    }
	function myLink(value,meta,record){
		var orderType = record.data.ORDER_TYPE;
		var reqId = record.data.REQ_ID;
		var todId = record.data.TOD_ID;//接收表ID
		var historyCount = history.length ;
  		return String.format("<a href='#' onclick='toDetailCheck(\""+ value +"\",\""+ orderType +"\",\""+ reqId +"\",\""+ historyCount +"\",\"" + todId + "\")'>[修改]</a><input type='hidden' name='ver' value='"+record.data.VER+"'/>");
	}

	function getDetail(){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/delivery/DsOrderDelvyModify/orderResourceModifyQueryLoad.json';
	 	$('fm').submit();
		}
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//调整链接
	function toDetailCheck(value1, value2, value3, value4,value5){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/delivery/DsOrderDelvyModify/orderDeliveryModifyDetailQuery.do?reqId='+value3;
	 	$('fm').submit();
	}
	//设置年周
	function myText(value,meta,record){
		var data = record.data;
  		return String.format(data.ORDER_YEAR+"."+value);
	}
	//初始化    
	function doInit(){
		loadcalendar();  //初始化时间控件
		if('${dealerCode}')
			document.getElementById("dealerCode").value='${dealerCode}';
		if('${areaId}')
			document.getElementById("areaId").value='${areaId}';
		if('${groupCode}')
			document.getElementById("groupCode").value='${groupCode}';
		//if('${orderTypeSel}')
		//	document.getElementById("orderTypeSel").value='${orderTypeSel}';
		if('${orderNo}')
			document.getElementById("orderNo").value='${orderNo}';
		if('${reqStatus}')
			document.getElementById("reqStatus").value='${reqStatus}';
		if('${orgCode}')
			document.getElementById("orgCode").value='${orgCode}';
		if('${startDate}')
			document.getElementById("startDate").value = '${startDate}' ;
		if('${endDate}')
			document.getElementById("endDate").value = '${endDate}' ;
		var area = "";
		<c:forEach items="${areaBusList}" var="list">
			var areaId = <c:out value="${list.AREA_ID}"/>
			if(area==""){
				area = areaId;
			}else{
				area = areaId+','+area;
			}
		</c:forEach>
		document.getElementById("area").value=area;
		__extQuery__(1);
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
<!--页面列表 end -->
</body>
</html>