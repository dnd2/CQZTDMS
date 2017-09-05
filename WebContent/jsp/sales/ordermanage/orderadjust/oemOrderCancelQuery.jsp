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
	//showDate();
	loadcalendar();
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单调整 > 常规订单调整查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	  <!--<tr>  
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
    --><tr>  
	  <td width="19%" align="right" nowrap>&nbsp;</td>
        <td align="right" nowrap="nowrap">订单提报时间：</td>
        <td align="left" nowrap="nowrap">
        	<div align="left">
       		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
		</td>
        <td width="19%" align=left nowrap>&nbsp;</td>
    </tr>
    <TR>
	  <TD align="right" nowrap>&nbsp;</td>
		<td width="12%" align="right" nowrap>业务范围：</td>
		<td width="50%" align="left" nowrap>
			<select name="areaId" class="short_sel">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </td>
		<td align=left>&nbsp;			</td>
	</tr>
    <TR>
	  <TD align="right" nowrap>&nbsp;</td>
		<td width="12%" align="right" nowrap>选择经销商：    </td>
		<td width="50%" align="left" nowrap>
			<input type="text" class="middle_txt" name="dealerCode" size="15" value="" readonly="readonly" id="dealerCode"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
			<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
           </td>
		<td align=left>&nbsp;			</td>
	</tr>
	<!--<tr>
	  <TD align=right nowrap>&nbsp;</TD>
        <TD align="right" nowrap>订单类型：</TD>
		<TD align="left" nowrap>
			<script type="text/javascript">
	      		genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
	      	</script>
        </TD>
		<td align=left nowrap>&nbsp;</td>
	</tr>
	--><tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">订单号码：</td>
	  <td align="left" nowrap="nowrap">
	  	<input type="text" class="middle_txt" name="orderNo" />
        &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
        <input type="button" class="cssbutton" name="queryBtn" value="查询" onclick="__extQuery__(1);" />
	  </td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OemOrderCancelQuery/orderCancelQuery.json";
				
	var title = null;

	var columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
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
