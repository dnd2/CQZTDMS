<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度常规订单预审核</title>
<script type="text/javascript">
function doInit(){
	
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单审核 > 月度常规订单预审核</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	<tr>
      <td align="right" nowrap >订单月度：</td>
      <td width="43%" class="table_query_2Col_input" nowrap>
		    <select name="orderMonth"  id="orderMonth" >
		      <c:forEach items="${dateList}" var="list">
					<!--<option value="${po.date_key}">${po.date_value}</option>
			  -->
			  	<option value="${list.code}"><c:out value="${list.name}"/></option>
			  </c:forEach>
	        </select>
        </td>
     
      <td align="right" nowrap >业务范围：</td>
      <TD nowrap width="20%">
			<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </TD>
        <td align="right" nowrap ></td>
    </tr>
    <tr>
		<td align="right">订单号码：</TD>
		<td align="left"><input type="text" class="middle_txt" name="orderNo"  value="" size="22"/></td>
		<td align="left" nowrap ></td>
		<td align="right" nowrap ></td>
		<td align="left" width="30%">
      		<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询" />
        </td>
         <td align="right" nowrap ></td>
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheck/monthOrderPreCheckQuery.json";
				
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];		         
	//修改的超链接
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='toCheck(\""+ value +"\")'>[审核]</a>");
	}
	
	function toCheck(orderId){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheck/monthOrderCheck_BUS_Detail.do?orderId='+orderId,700,550);
	}
	function reloadAction(){
		__extQuery__(1);
	}
</script>
</body>
</html>
