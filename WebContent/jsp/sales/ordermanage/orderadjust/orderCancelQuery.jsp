<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单取消</title>
<script type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		//showDate();
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单调整 > 常规订单调整</div>
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
      -->
      <tr>  
	  	<td width="19%" align="right" nowrap>&nbsp;</td>
        <td align="right" nowrap="nowrap">订单提报时间：</td>
        <td align="left" nowrap="nowrap">
        	<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="${dateStr}" />
			<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
			<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="${dateStr}" />
			<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
		</td>
        <td width="19%" align=left nowrap>&nbsp;</td>
      </tr>
	  <tr>
        <td align="right" nowrap="nowrap">&nbsp;</td>
	    <td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select name="areaId" class="short_sel">
      	  	    <option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>
        </td>
	    <td>&nbsp;</td>
    </tr>
    <tr>
    <td align="right" nowrap ></td>
      <td  align="right" nowrap></td>
      <td  align="center" nowrap></td>
      <td align="left" nowrap>      </td>
    <TR>
	  <TD align="right" nowrap>&nbsp;</td>
			<td width="12%" align="right" nowrap>选择经销商：</td>
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
		  <label>
	        <input type="text" class="middle_txt" name="orderNo" />&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
	        <input type="button" class="cssbutton" name="queryBtn" value="查询" onClick="__extQuery__(1);">
	      </label>
      </td>
	  <td align="left" nowrap="nowrap"></td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
    
    <table class="table_query" width="95%" align="center" border="0" id="table1">
	  <tr class="tabletitle">
	    <td width="11%" align="right">输入取消原因：</td>
	    <td width="21%" align="left"><textarea rows="3" name="chngReason"></textarea></td>
	    <td width="68%" align="left">
		    <input type="hidden" name="orderIds" id="orderIds"/>
		    <input type="hidden" name="vers" id="vers"/>
		    <input type="button" class="cssbutton" name="bt_search" value="取消订单" onclick="confirmCancel();" />
	    </td>
	  </tr>
	</table>
</form>

<script type="text/javascript">
	document.getElementById("table1").style.display = "none";
	var HIDDEN_ARRAY_IDS = ['table1'];
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderCancelQuery.json";
				
	var title = null;	
	
	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderId\")' />", dataIndex: 'ORDER_ID', align:'center',renderer:myCheckBox},
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "付款方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "订单周度", dataIndex: 'ORDER_DATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "申请发运数量", dataIndex: 'CALL_AMOUNT', align:'center'},
				//{header: "发运指令数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				//{header: "发运数量", dataIndex: 'MATCH_AMOUNT', align:'center'},
				{header: "操作", dataIndex: 'ORDER_ID', align:'center' ,renderer:myLink2}
		      ];    
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderId' value='" + value + "'/>");
	}
	
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a><input type='hidden' name='ver' value='"+record.data.VER+"'/>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	
	function myLink2(value,metaDate,record){
		return String.format("<a href='#' onclick='loginCancel(\""+ value +"\","+record.data.VER+")'>[部分取消]</a>");
	}
	
	function loginCancel(arg,ver){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderPartCancelQuery.do?orderId='+arg+'&ver='+ver;
	 	$('fm').submit();
	}
	
	function confirmCancel(){
		var cnt = 0;
		var orderIds ='';
		var vers ='';
		var orderId = document.getElementsByName("orderId");
		var ver = document.getElementsByName("ver");
		for(var i=0 ;i< orderId.length; i++){
			if(orderId[i].checked){
				cnt++;
				orderIds = orderId[i].value + ',' + orderIds;
				vers = ver[i].value + ',' + vers;
			}
		}
		if(cnt==0){
			MyAlert("请选择！");
            return false;
		}
		document.getElementById("orderIds").value=orderIds;
		document.getElementById("vers").value=vers;
		MyConfirm("是否确认取消订单?",allCancel);
	}
	
	function allCancel(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderCancel.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderCancelQueryPre.do';
		}else if(json.returnValue == '2'){
			parent.MyAlert("数据已被修改，操作失败！");
			__extQuery__(1);
		}else{
			MyAlert("取消失败！请联系系统管理员！");
		}
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
