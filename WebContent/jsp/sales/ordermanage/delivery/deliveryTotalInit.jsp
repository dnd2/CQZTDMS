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

	var myPage;
	//查询路径           
	var url = null;
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "订单号",dataIndex: 'ORDER_NO',align:'center'},
				{header: "操作", dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='lookInit("+record.data.ORDER_ID+")'>[查看]</a>");
	}
	function lookInit(order_id){
		location.href="<%=contextPath%>/sales/ordermanage/delivery/DeliveryApply/applyTotal.do?orderId="+order_id;
	}
	function executeQuery(){
		var orderYearWeek = document.getElementById("orderYearWeek").value;
		var areaId = document.getElementById("areaId").value;
		var dealerId = document.getElementById("dealerId").value;
		var orderNO = document.getElementById("orderNO").value;
		url = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryApply/applyTotalQuery.json?&orderYearWeek="+orderYearWeek+"&areaId="+areaId+"&dealerId="+dealerId+"&orderNO="+orderNO;
		__extQuery__(1);
	}
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
	
	
	
	function chkOrderNO(json) {
		var flag = json.flag ;
		var aOrderNO = json.orderList ;
		
		//if(flag == "0") {
		//	document.getElementById("queryBtn").disabled = true ;
		//	MyAlert("当前周度无可启票常规订单！") ;
		//} else {
		//	document.getElementById("queryBtn").disabled = false ;
		//}
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
	//初始化
	function initOrderWeek(){
			var choice_code = document.getElementById("orderYearWeek").value;
			showData(choice_code);
	}
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
</head>
<body onload="loadcalendar();initOrderWeek();;executeQuery();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;需求订单发运申请（单家）</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>  
			<td align="right" >订单周度：</td>
			<td align="left" >
				<select name="orderYearWeek" id="orderYearWeek" onchange="showData(this.value); ">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
				<span id="data_start" class="innerHTMLStrong"></span> 
				<span id="data_end" class="innerHTMLStrong"></span>
			</td>
			<td align="right" nowrap="nowrap">提报起止时间：</td>
			<td align="left" nowrap="nowrap">
				<div align="left">
       		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
				<input type="hidden" name="areaId" id="areaId"/>
				<input type="hidden" name="dealerId" id="dealerId"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap>&nbsp;</td>
			
			<td>&nbsp;</td> 
		</tr>
		<tr>
	      <td align="right" nowrap="nowrap">销售订单号：</td>
	      <td align="left" nowrap="nowrap">
			<input name="orderNo" id="orderNo"/>
	      </td>
	     	<td align="right" >选择经销商：</td>
			<td colspan="1" >
				<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="" id="dealerCodes"/>
			    <input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
			</td>
	    </tr> 
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><input name="queryBtn" type=button class="cssbutton" onClick="executeQuery();" value="查询"></td>
			<td align="center" nowrap>&nbsp;</td>
		</tr>   
	</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end --> 
</form>
<!-- 查询条件 end -->

<br>
<div id="tablelist" style="display:none">
	<iframe src="" name="iframe" id="iframe" width="100%" height="600" frameborder="no" scrolling="yes"></iframe>
</div>
<script type="text/javascript">
function clrTxt(dealerCodes){
	document.getElementById(dealerCodes).value="";
}
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