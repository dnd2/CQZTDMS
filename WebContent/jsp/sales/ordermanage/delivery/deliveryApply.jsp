<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单发运申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	//初始化
	function doInit(){
			getDealerAreaId(document.getElementById("area").value);
			var choice_code = document.getElementById("orderYearWeek").value;
			showData(choice_code);
	}
	//设置业务范围ID,经销商ID
	function getDealerAreaId(arg){
		var areaObj = document.getElementById("area");
		var areaId = areaObj.value.split("|")[0];
		var dealerId = areaObj.value.split("|")[1];
		document.getElementById("areaId").value = areaId;
		document.getElementById("dealerId").value = dealerId;
	}	
	
	function toQuery(){
		queryDtl() ;
	}
	
	function getOrderNO() {
		var orderYearWeek = document.getElementById("orderYearWeek").value;
		var area = document.getElementById("area").value;
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/LowWeekGeneralOrderCall/getGeneralOrderNO.json";
		makeCall(url,chkOrderNO,{orderYearWeek:orderYearWeek,area:area});
	}
	
	function setOrderNO(aOrderNO) {
		var iLen = aOrderNO.length ;
		
		document.getElementById("orderNo").options.length = iLen ;
		
		for(var i=0; i<iLen; i++) {
			$('fm').orderNO.options[i].value = aOrderNO[i].ORDER_NO ;
			$('fm').orderNO.options[i].text = aOrderNO[i].ORDER_NO ;
		}
	}
	
	function chkOrderNO(json) {
		var flag = json.flag ;
		var aOrderNO = json.orderList ;
		
		if(flag == "0") {
			document.getElementById("queryBtn").disabled = true ;
			
			MyAlert("当前周度无可启票常规订单！") ;
		} else {
			document.getElementById("queryBtn").disabled = false ;
		}
		
		setOrderNO(aOrderNO) ;
	}
	
	function queryDtl() {
		var orderYearWeek = document.getElementById("orderYearWeek").value;
		var areaId = document.getElementById("areaId").value;
		var dealerId = document.getElementById("dealerId").value;
		var orderNO = document.getElementById("orderNO").value;
		var ss = document.getElementById("iframe");
		ss.src = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryApply/applyQuery.do?&orderYearWeek="+orderYearWeek+"&areaId="+areaId+"&dealerId="+dealerId+"&orderNO="+orderNO;
		document.getElementById('tablelist').style.display="inline";
	}
</script>
</head>
<body onload="getOrderNO();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;常规订单发运申请</div>
<c:if test="${IsExpire>=1}">
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>  
			<td width="19%" align="right" nowrap>&nbsp;</td>
			<td align="right" width="23%">订单周度：</td>
			<td align="left" width="50%">
				<select name="orderYearWeek" id="orderYearWeek" onchange="showData(this.value); getOrderNO();">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
				<span id="data_start" class="innerHTMLStrong"></span> 
				<span id="data_end" class="innerHTMLStrong"></span>
			</td>
			<td width="31%" align=left nowrap>&nbsp;</td>
		</tr>
		<tr>
			<td align="right" nowrap>&nbsp;</td>
			<td align="right" nowrap="nowrap">选择业务范围：</td>
			<td align="left" nowrap="nowrap">
				<select name="area" class="short_sel" onchange="getDealerAreaId(this.options[this.options.selectedIndex].value); getOrderNO();">
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="areaId" id="areaId"/>
				<input type="hidden" name="dealerId" id="dealerId"/>
			</td>
			<td>&nbsp;</td> 
		</tr>
		<tr>
		<td align="right" nowrap>&nbsp;</td>
	      <td align="right" nowrap="nowrap">销售订单号：</td>
	      <td align="left" nowrap="nowrap">
	      	<select name="orderNO" id="orderNO">
			</select>
	      </td>
	      <td>&nbsp;</td> 
	    </tr> 
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><input name="queryBtn" type=button class="cssbutton" onClick="toQuery();" value="查询"></td>
			<td align="center" nowrap>&nbsp;</td>
		</tr>   
	</table>
</form>
<!-- 查询条件 end -->
<br>
<div id="tablelist" style="display:none">
	<iframe src="" name="iframe" id="iframe" width="100%" height="600" frameborder="no" scrolling="yes"></iframe>
</div>
</c:if>
<c:if test="${IsExpire==0}"><center style="background-color:yellow;"> <div class='pageTips'>合同到期日为${expireDate}，已经到期，无法操作！！</div></center></c:if>
<script type="text/javascript">
function showData(choice_code){
		var data_start = "";
		var data_end = "";
		<c:forEach items="${dateList}" var="list">
			var code = "${list.code}";
			if(choice_code+"" == code+""){
				data_start = "${list.date_start}";
				data_end = "${list.date_end}";
			}
		</c:forEach>
		if(data_start){
			document.getElementById("data_start").innerHTML = data_start+"  至  ";
			document.getElementById("data_end").innerHTML = data_end;
		}
	}
</script>
</body>
</html>