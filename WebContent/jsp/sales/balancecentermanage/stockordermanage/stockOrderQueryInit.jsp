<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采购订单查询</title>
<script type="text/javascript">

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 采购订单管理 > 采购订单查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	  <tr>  
	  <td width="19%" align="right" nowrap>&nbsp;</td>
        <td align="right" nowrap="nowrap">订单周度：</td>
        <td align="left" nowrap="nowrap">
        	<select name="orderYear1">
        		<c:forEach items="${years}" var="po">
        			<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
          	</select>
          	年
		  	<select name="orderWeek1">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>
          	至
		   	<select name="orderYear2">
        		<c:forEach items="${years}" var="po">
					<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>
          	年
  			<select name="orderWeek2">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
			</select>
		</td>
        <td width="19%" align=left nowrap>&nbsp;</td>
    </tr>
	  <tr>
        <td align="right" nowrap="nowrap">&nbsp;</td>
	    <td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>
        </td>
	    <td>&nbsp;</td>
    </tr>
	 <tr>
      <td align="right" nowrap>&nbsp;</td><td align="right" nowrap >选择物料组：</td>
      <td align="left" nowrap >
      	<input type="text"  name="groupCode" size="15" id="groupCode" value="" />
		<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
      </td>
      <td>&nbsp;	   </td> 
    </tr> 
	
	<tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">订单状态：</td>
	  <td align="left" nowrap="nowrap">
			<script type="text/javascript">
	      		genSelBoxExp("orderStatus",<%=Constant.ORDER_STATUS%>,"",true,"short_sel","","false",'');
	      	</script>
	  </td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
	<tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">订单号码：</td>
	  <td align="left" nowrap="nowrap"><label>
        <input type="text" id="orderNo" name="orderNo" datatype="1,is_textarea,18"/>
      </label></td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
	   <tr>
	    <td align="center" nowrap>&nbsp;</td>
        <td>&nbsp;</td>
	    <td>
	    	<input name="button2" type=button class="cssbutton" onClick="totalQuery();" value="汇总查询">
          	<input name="button2" type=button class="cssbutton" onClick="detailQuery();" value="明细查询">
          	<input name="button2" type=button class="cssbutton" onClick="totalExport();"value="汇总下载">
          	<input name="button2" type=button class="cssbutton" onClick="detailExport();"value="明细下载">
          	<input type="hidden" id="orderType" name="orderType" value="<%=Constant.ORDER_TYPE_02 %>">
         </td>
	    <td align="center" nowrap>&nbsp;</td>
    </TR>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url;
				
	var title = null;

	var columns;		
	         
	var calculateConfig;
	
	function totalQuery(){
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"ORDER_TYPE|ORDER_TYPE",totalColumns:"ORDER_TYPE"};
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderTotalQuery.json";
		columns = [
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'MATCH_AMOUNT', align:'center' ,renderer:myLink2}
		      ];
		__extQuery__(1);
	}
	
	function detailQuery(){
		calculateConfig = {};
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderDetailQuery.json";
		columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "提报类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'MATCH_AMOUNT', align:'center' ,renderer:myLink2}
		      ];
		__extQuery__(1);
	}
	
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	} 

	
	function myLink2(value,meta,record){
		return String.format("<a href='#'>"+value+"</a>");
	}
	
	function totalExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderTotalExport.do";
		$('fm').submit();
	}
	
	function detailExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderDetailExport.do";
		$('fm').submit();
	}
</script>
</body>
</html>
