<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商订单查询</title>
<script type="text/javascript">
	function doInit(){
		showDate();
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 经销商订单管理 > 经销商订单查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	  <tr>  
	  <td width="19%" align="right" nowrap>&nbsp;</td>
        <td align="right" nowrap="nowrap">订单周度：</td>
        <td align="left" nowrap="nowrap">
        	<select id="startYear" name="orderYear1" onchange="showDate();">
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
		  	<select id="startWeek" name="orderWeek1" onchange="showDate();">
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
		   	<select id="endYear" name="orderYear2" onchange="showDate();">
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
  			<select id="endWeek" name="orderWeek2" onchange="showDate();">
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
			<span id="dateInfo" class="innerHTMLStrong"></span>
		</td>
        <td width="19%" align=left nowrap>&nbsp;</td>
    </tr>
	  <tr>
        <td align="right" nowrap="nowrap">&nbsp;</td>
	    <td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select name="areaId" class="short_sel">
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
      	<input type="text" class="middle_txt" name="groupCode" size="15" id="groupCode" value="" />
		<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4')" value="..." />
      </td>
      <td>&nbsp;	   </td> 
    </tr> 
	<tr>
	  <TD align=right nowrap>&nbsp;</TD>
        <TD align="right" nowrap>订单类型：</TD>
		<TD align="left" nowrap>
			<script type="text/javascript">
	      		genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
	      	</script>
        </TD>
		<td align=left nowrap>&nbsp;</td>
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
	  <td align="right" nowrap="nowrap">销售订单号：</td>
	  <td align="left" nowrap="nowrap"><label>
        <input type="text" class="middle_txt" id="orderNo" name="orderNo"/>
      </label></td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
    <tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">上级经销商销售订单号：</td>
	  <td align="left" nowrap="nowrap"><label>
        <input type="text" class="middle_txt" id="orderNo" name="newOrderNo"/>
      </label></td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
	<tr>
	    <td align="center" nowrap>&nbsp;</td>
        <td>&nbsp;</td>
	    <td><input name="button2" type=button class="cssbutton" onClick="totalQuery();" value="汇总查询">
          <input name="button2" type=button class="cssbutton" onClick="detailQuery();" value="明细查询">
          <input name="button2" type=button class="cssbutton" onClick="totalExport();"value="汇总下载">
          <input name="button2" type=button class="cssbutton" onClick="detailExport();"value="明细下载"></td>
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
		url = "<%=request.getContextPath()%>/sales/balancecentermanage/dealerordermanage/DealerOrderQuery/dealerOrderTotalQuery.json";
		columns = [
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "已发车量", dataIndex: 'DELIVERY_AMOUNT', align:'center'}
		      ];
		__extQuery__(1);
	}
	
	function detailQuery(){
		calculateConfig = {};
		url = "<%=request.getContextPath()%>/sales/balancecentermanage/dealerordermanage/DealerOrderQuery/dealerOrderDetailQuery.json";
		columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "提报状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "上级经销商销售订单号", dataIndex: 'OLD_ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "上级订单状态", dataIndex: 'ORDER_STATUSS', align:'center' ,renderer:getItemValue},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "提报类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "已发车量", dataIndex: 'DELIVERY_AMOUNT', align:'center'}
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
		$('fm').action= "<%=request.getContextPath()%>/sales/balancecentermanage/dealerordermanage/DealerOrderQuery/dealerSalesOrderTotalExport.json";
		$('fm').submit();
	}
	
	function detailExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/balancecentermanage/dealerordermanage/DealerOrderQuery/dealerSalesOrderDetailExport.json";
		$('fm').submit();
	}
	function showDate(){
		var startYear = document.getElementById("startYear").value;
		var startWeek = document.getElementById("startWeek").value;
		var endYear = document.getElementById("endYear").value;
		var endWeek = document.getElementById("endWeek").value;

		makeNomalFormCall('<%=request.getContextPath()%>/sales/showDate/ShowDateInfo/showDate.json?startYear='+startYear+'&startWeek='+startWeek+'&endYear='+endYear+'&endWeek='+endWeek,showDateInfo,'fm');
	}
	function showDateInfo(json){
		if(json.returnValue == '1'){
			document.getElementById("dateInfo").innerHTML = json.showDate;
		}
	}
</script>
</body>
</html>
