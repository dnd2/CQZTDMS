<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<% 
	String contextPath = request.getContextPath(); 
	List boardList = (List)request.getAttribute("boardList");	
	List boardListDay = (List)request.getAttribute("boardListDay");	
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>当日销售看板</title>
<script type="text/javascript">
	function doInit(){
		sum_no();
	}
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>当日销售看板</strong>
</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_list" id="activeTable">
	<tr class="table_list_th_report">
		<th rowspan="2"><font style="font-size: 17px">产品系列</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">启票数量</font></th>
		<th colspan="3"><font style="font-size: 17px">实销数量</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">当日累计</th>
		<th>本月累计</th>
		<th>本年累计</th>
		<th>当日累计</th>
		<th>本月累计</th>
		<th>本年累计</th>
	</tr>
	
		<%
			if(null != boardList && boardList.size()>0){
				for(int i=0;i<boardList.size();i++){
				Map map = (Map)boardList.get(i);
				Map map_day = (Map)boardListDay.get(i);
		%>
		<tr class="table_list_row1" height="25px">
			<td><%=map.get("SERIES_NAME") %></td>
			<td><%=map_day.get("TODAY_BILL_AMOUNT") %><input type="hidden" name="number1" value="<%=map_day.get("TODAY_BILL_AMOUNT") %>" /></td>
			<td><%=map.get("MONTH_BILL_AMOUNT") %><input type="hidden" name="number2" value="<%=map.get("MONTH_BILL_AMOUNT") %>" /></td>
			<td><%=map.get("YEAR_BILL_AMOUNT") %><input type="hidden" name="number3" value="<%=map.get("YEAR_BILL_AMOUNT") %>" /></td>
			<td><%=map_day.get("TODAY_SALES_AMOUNT") %><input type="hidden" name="number4" value="<%=map_day.get("TODAY_SALES_AMOUNT") %>" /></td>
			<td><%=map.get("MONTH_SALES_AMOUNT") %><input type="hidden" name="number5" value="<%=map.get("MONTH_SALES_AMOUNT") %>" /></td>
			<td><%=map.get("YEAR_SALES_AMOUNT") %><input type="hidden" name="number6" value="<%=map.get("YEAR_SALES_AMOUNT") %>" /></td>
		</tr>
			<%
				}
			}
		%>
	
	<tr align="center" class="table_list_row2" height="25px">
		<td>合计</td>
		<td><a href="#" onclick="DayDealerReportAction();"><span id="todayBillSum"></span></a></td>
		<td><a href="#" onclick="monthReportAction();"><span id="monthBillSum"></span></a></td>
		<td><a href="#" onclick="yearReportAction();"><span id="yearBillSum"></span></a></td>
		<td><a href="#" onclick="DayDealerReportAction();"><span id="todaySalesSum"></span></a></td>
		<td><a href="#" onclick="monthReportAction();"><span id="monthSalesSum"></span></a></td>
		<td><a href="#" onclick="yearReportAction();"><span id="yearSalesSum"></span></a></td>
	</tr>
	<tr align="center" class="table_list_row1" height="25px">
		<td>目标</td>
		<td></td>
		<td>${monthBillPlan }<input type="hidden" id="monthBillPlan" name="monthBillPlan" value="${monthBillPlan }" /></td>
		<td>${yearBillPlan }<input type="hidden" id="yearBillPlan" name="yearBillPlan" value="${yearBillPlan }" /></td>
		<td></td>
		<td>${monthSalesPlan }<input type="hidden" id="monthSalesPlan" name="monthSalesPlan" value="${monthSalesPlan }" /></td>
		<td>${yearSalesPlan }<input type="hidden" id="yearSalesPlan" name="yearSalesPlan" value="${yearSalesPlan }" /></td>
	</tr>
	<tr align="center" class="table_list_row2" height="25px">
		<td>目标达成率</td>
		<td></td>
		<td><span id="rate1"></span></td>
		<td><span id="rate2"></span></td>
		<td></td>
		<td><span id="rate3"></span></td>
		<td><span id="rate4"></span></td>
	</tr>
</table>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="refresh();" value="刷新">
	</td></tr>
</table>
</form> 
<script type="text/javascript" >
	function refresh(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayDealerReport/dayDealerReportInit.do";
		$('fm').submit();
	}
	//setTimeout('myrefresh()',1000);
	function DayDealerReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayDealerReport/regionDealerDetailReport.do?flag="+3;
		$('fm').submit();
	}
	function monthReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayDealerReport/regionDealerDetailReport.do?flag="+2;
		$('fm').submit();
	}
	function yearReportAction(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayDealerReport/regionDealerDetailReport.do?flag="+1;
		$('fm').submit();
	}
	function round(number,fractionDigits){   
		with(Math){   
	     return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
		}   
	}

	function sum_no(){
		var number1 = document.getElementsByName("number1");
		var number2 = document.getElementsByName("number2");
		var number3 = document.getElementsByName("number3");
		var number4 = document.getElementsByName("number4");
		var number5 = document.getElementsByName("number5");
		var number6 = document.getElementsByName("number6");
		var numbers1 = 0;
		var numbers2 = 0;
		var numbers3 = 0;
		var numbers4 = 0;
		var numbers5 = 0;
		var numbers6 = 0;
		
		for(var i=0;i<number1.length;i++){
			numbers1 += Number(number1[i].value);
			numbers2 += Number(number2[i].value);
			numbers3 += Number(number3[i].value);
			numbers4 += Number(number4[i].value);
			numbers5 += Number(number5[i].value);
			numbers6 += Number(number6[i].value);
		}
		
		document.getElementById("todayBillSum").innerHTML = numbers1;
		document.getElementById("monthBillSum").innerHTML = numbers2;
		document.getElementById("yearBillSum").innerHTML = numbers3;
		document.getElementById("todaySalesSum").innerHTML = numbers4;
		document.getElementById("monthSalesSum").innerHTML = numbers5;
		document.getElementById("yearSalesSum").innerHTML = numbers6;
		
		//计算目标达成率=合计/目标
		var monthBillSum = document.getElementById("monthBillSum").innerHTML;		//月度启票合计
		var yearBillSum = document.getElementById("yearBillSum").innerHTML;			//年度启票合计
		var monthSalesSum = document.getElementById("monthSalesSum").innerHTML;		//月度启票合计
		var yearSalesSum = document.getElementById("yearSalesSum").innerHTML;		//年度启票合计
	
		var monthBillPlan = document.getElementById("monthBillPlan").value;			//月度启票目标
		var yearBillPlan = document.getElementById("yearBillPlan").value;			//年度启票目标
		var monthSalesPlan = document.getElementById("monthSalesPlan").value;		//月度销售目标
		var yearSalesPlan = document.getElementById("yearSalesPlan").value;			//月度销售目标
	
		if(monthBillPlan+"" != "0"){
			document.getElementById("rate1").innerHTML = round(100*monthBillSum/monthBillPlan,2)+"%";
		}else{
			document.getElementById("rate1").innerHTML = 0;
		}
		
		if(yearBillPlan+"" != "0"){
			document.getElementById("rate2").innerHTML = round(100*yearBillSum/yearBillPlan,2)+"%";
		}else{
			document.getElementById("rate2").innerHTML = 0;
		}
	
		if(monthSalesPlan+"" != "0"){
			document.getElementById("rate3").innerHTML = round(100*monthSalesSum/monthSalesPlan,2)+"%";
		}else{
			document.getElementById("rate3").innerHTML = 0;
		}
	
		if(yearSalesPlan+"" != "0"){
			document.getElementById("rate4").innerHTML = round(100*yearSalesSum/yearSalesPlan,2)+"%";
		}else{
			document.getElementById("rate4").innerHTML = 0;
		}
	 }	
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+value+"&orderType="+value2;
		$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
