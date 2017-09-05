<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商价格查询</title>
<script type="text/javascript">
<!--
function doInit(){
}   

function download() {
	$("fm").action = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DealerPriceQuery/priceDownload.json" ;
	$("fm").submit() ;
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 经销商价格查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	<tr>
	  <td align="right" nowrap>业务范围：</td>
		<td class="table_query_2Col_input" nowrap>
			<select name="areaId">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </td>
		<td align="right" nowrap>车型代码:</td>
		<td align="left" nowrap><input type="text" class="middle_txt" id="modelCode" name="modelCode" /></td>
	</tr> 
	<tr>
	   <td align="center" nowrap colspan="4">
	   	<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询" />
	   	&nbsp;&nbsp;&nbsp;
	   	<input type="button" class="cssbutton" id="btn_download" name="btn_download" onclick="download() ;" value="下载" />
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DealerPriceQuery/dealerPriceQuery.json";
				
	var title = null;

	var columns = [
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "价格", dataIndex: 'SALES_PRICE', align:'center' ,renderer:amountFormat}
				//{header: "价格生成日期", dataIndex: 'CREATE_DATE', align:'center'}
		      ];		         
	
</script>
</body>
</html>
