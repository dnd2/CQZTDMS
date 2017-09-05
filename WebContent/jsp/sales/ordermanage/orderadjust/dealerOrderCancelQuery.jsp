<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单取消查询</title>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单调整 > 常规订单调整查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	  <tr>
	      <td width="30%" height="22" align="right">订单取消日期：</td>
	      <td colspan="2">
		      <input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
			  <input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
			  <input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
			  <input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
      	  </td>
      </tr>      
      <tr>
      	<td width="30%" align="right">选择业务范围：</td>
      	<td colspan="2">
			<select class="short_sel" name="areaId">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>    
		</td>
      </tr>      
      <tr>
      	<td width="30%" align="right">订单类型：</td>
      	<td colspan="2">
			<script type="text/javascript">
	      		genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
	      	</script>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
			<input type="button" class="normal_btn" name="bt_search2" value="查询" onClick="__extQuery__(1);">      
		</td>
      </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderadjust/DealerOrderCancelQuery/orderCancelQuery.json";
				
	var title = null;

	var columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "提报时间", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "取消数量", dataIndex: 'CHNG_AMT', align:'center'},
				{header: "取消日期", dataIndex: 'CHNG_DATE', align:'center'},
				{header: "取消人", dataIndex: 'USER_NAME', align:'center'},
				{header: "取消原因", dataIndex: 'CHNG_REASON', align:'center'}
		      ];	
	
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
</script>
</body>
</html>
