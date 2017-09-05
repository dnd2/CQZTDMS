<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补充订单提报</title>
<script type="text/javascript">
function doInit(){
	__extQuery__(1);
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 补充订单预审</div>
<form method="POST" name="fm" id="fm">
  <!--<table class="table_query" align=center width="95%">
	 <tr>
	    <td width="37%" align="right" nowrap>订单周度：   </td>
	    <td width="43%" class="table_query_2Col_input" nowrap>
		    <select name="orderWeek">
		      <c:forEach items="${dateList}" var="po">
					<option value="${po.code}">${po.name}</option>
			  </c:forEach>
	        </select>
        </td>
	    <td width="20%" align=left nowrap>&nbsp;</td>
    </tr> 
    <tr>
      <td align="right" nowrap></td>
      <td align="right" nowrap></td>
      <td align="left" nowrap></td>
    </tr>
	<tr>
	  <TD align="right" nowrap>业务范围：</TD>
		<TD class="table_query_2Col_input" nowrap>
			<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </TD>
		<td align=left nowrap>&nbsp;</td>
	</tr> 
	<tr>
      <td align="right" nowrap>&nbsp;</td>
	   <td align="center" nowrap>
	   	<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
	   	<input id="addBtn" name="button22" type=button class="cssbutton" onClick="loginAdd();" value="新增">
	   </td>
	   <td></td>
	</tr>
  </table>
  
	--><!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderPrepCheck/urgentOrderPrepCheckQuery.json?command=1";
				
	var title = null;

	var columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "订单周度", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];		         
	
	//修改的超链接
	
	function myLink(value,meta,record){
		var data = record.data;
  		return String.format("<a href='#' onclick='loginCheck(\""+ value +"\",\""+ data.IS_FLEET +"\",\""+ data.FLEET_ID +"\",\""+ data.ORDER_NO +"\",\""+ data.ORDER_TYPE +"\")'>[审核]</a>");
	}
	
	function loginCheck(arg,isFleet,fleetId,orderNO,orderType){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderPrepCheck/urgentOrderPrepCheckPre.do?orderId='+arg+'&isFleet='+isFleet+'&fleetId='+fleetId+'&orderNO='+orderNO+'&orderType='+orderType;
	 	$('fm').submit();
	}
</script>
</body>
</html>
