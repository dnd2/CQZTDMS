<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>下级经销商周度常规订单启票</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	//初始化
	function doInit(){
		getDealerAreaId(document.getElementById("area").value);
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
		var orderNo = document.getElementById("orderNo").value;
		var ss = document.getElementById("iframe");
		ss.src = "<%=contextPath%>/sales/ordermanage/orderreport/LowWeekGeneralOrderCall/applyQuery.do?&orderYearWeek="+orderYearWeek+"&areaId="+areaId+"&dealerId="+dealerId+"&orderNo="+orderNo;
		document.getElementById('tablelist').style.display="inline";
	}
</script>
</head>
<body onload="getOrderNO() ;">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单提报 &gt;下级经销商周度常规订单启票</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
	      <td width="37%" align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">订单周度： </td>
	      <td width="15%" align="left" nowrap="nowrap" class="table_query_2Col_input">
	      	<select name="orderYearWeek" id="orderYearWeek" onchange="getOrderNO();">
				<c:forEach items="${dateList}" var="list">
					<option value="${list.code}"><c:out value="${list.name}"/></option>
				</c:forEach>
			</select>
	      </td>
	      <td width="48%" align="left" nowrap="nowrap" class="table_query_2Col_label_6Letter">业务范围：</td>
	      <td width="48%" align="left" nowrap="nowrap" class="table_query_2Col_input">
	      	<select name="area" class="short_sel" onchange="getDealerAreaId(this.options[this.options.selectedIndex].value); getOrderNO();">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
				</c:forEach>
			</select>
			<input type="hidden" name="areaId" id="areaId"/>
			<input type="hidden" name="dealerId" id="dealerId"/>
	      </td>
	    </tr>
	    <tr>
	      <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">订单号码：</td>
	      <td align="left" nowrap="nowrap" class="table_query_2Col_input">
	      	<select name="orderNO" id="orderNO">
			</select>
	      </td>
	      <td class="table_query_2Col_label_6Letter">&nbsp;</td>
	      <td class="table_query_2Col_input"><input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="toQuery();" value="查询" /></td>
	    </tr> 
	</table>
</form>
<!-- 查询条件 end -->
<br>
<div id="tablelist" style="display:none">
	<iframe src="" name="iframe" id="iframe" width="100%" height="600" scrolling="yes" frameborder="0"></iframe>
</div>
</body>
</html>