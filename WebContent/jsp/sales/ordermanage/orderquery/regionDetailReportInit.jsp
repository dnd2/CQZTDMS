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
	List seriesList = (List)request.getAttribute("seriesList");	
	List detailList = (List)request.getAttribute("detailList");	
	int seriesLength = seriesList.size();
	int detailLength = detailList.size();
	String flag = String.valueOf(request.getAttribute("flag"));
	String year = String.valueOf(request.getAttribute("year"));
	String month = String.valueOf(request.getAttribute("month"));
	String day = String.valueOf(request.getAttribute("day"));
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
<div  class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;
		<strong>当日销售看板:
		<%
			if(null != flag && "3".equals(flag)){
		%>
			<font color="red"><%=year %>年<%=month %>月<%=day %>日</font>
		<%
			}
			if(null != flag && "2".equals(flag)){
		%>
			<font color="red"><%=year %>年<%=month %>月</font>
		<%
			}
			if(null != flag && "1".equals(flag)){
		%>
			<font color="red"><%=year %>年</font>
		<%
			}
		%>
		</strong>
</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_list" id="activeTable">
	<tr class="table_list_th_report">
		<th rowspan="2">经销商</th>
		<c:if test="${seriesList!=null}">
			<c:forEach items="${seriesList}" var="list">
				<th colspan="2">${list.GROUP_NAME}</th>
			</c:forEach>
		</c:if>
		<th colspan="2">合计</th>
	</tr>
	<tr class="table_list_th">
		<c:if test="${seriesList!=null}">
			<c:forEach items="${seriesList}" var="list">
				<td>启票数量</td>
				<td>实销数量</td>
			</c:forEach>
		</c:if>
		<td>启票数量</td>
		<td>实销数量</td>
	</tr>
	
	<%
		if(null != detailList && detailList.size()>0){
			for(int i=0;i<detailList.size();i++){
				Map map = (Map)detailList.get(i);
	%>			
		<tr align="center" class="<c:if test='${i%2==0}'>table_list_row1</c:if><c:if test='${i%2!=0}'>table_list_row2</c:if>">
			<td><%=map.get("DEALER_NAME") %></td>
			<%
				for(int j=0;j<seriesList.size();j++){
			%>
				<td><%=map.get("BILL_AMOUNT"+j) %><input type="hidden" name="billAmount<%=j %>" value="<%=map.get("BILL_AMOUNT"+j) %>" /></td>
				<td><%=map.get("SALES_AMOUNT"+j) %><input type="hidden" name="salesAmount<%=j %>" value="<%=map.get("SALES_AMOUNT"+j) %>" /></td>
			<%
				}
			%>	
			<td><span id="billSum<%=i %>"></span></td>
			<td><span id="salesSum<%=i %>"></span></td>
		</tr>
	<%
			}
		}
	%>
	<tr class="table_list_row2">
		<td>合计</td>
			<%
				for(int j=0;j<seriesList.size();j++){
			%>
				<td><span id="billAmount_<%=j %>"></span></td>
				<td><span id="salesAmount_<%=j %>"></span></td>
			<%
				}
			%>
		<td><span id="billAll"></span></td>
		<td><span id="salesAll"></span></td>
	</tr>
</table>
<br>
<table border="0" align="center" class="table_list">
	<tr>
		<td>
			<input name="button2" type=button class="cssbutton" onClick="refresh();" value="刷新">
			<input name="button2" type=button class="cssbutton" onClick="goBack();" value="返回">
			<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
		</td>
	</tr>
</table>
</form> 
<script type="text/javascript" >

	function refresh(){
		var flag = ${flag};
		var orgId = ${orgId};
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/regionDetailReport.do?flag="+flag+"&orgId="+orgId;
		$('fm').submit();
	}

	function goBack(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/dayReportInit.do";
		$('fm').submit();
	}

	function round(number,fractionDigits){   
		with(Math){   
	     return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
		}   
	}

	function sum_no(){
		var seriesLength = <%=seriesLength%>;//车系数组长度
		var detailLength = <%=detailLength%>;//区域数组长度
		var billAll = 0;
		var salesAll = 0;
		for(var i=0;i<seriesLength;i++){
			 var billAmount = 0;
			 var salesAmount = 0;
			 
			 var billAmounts = document.getElementsByName("billAmount"+i);
			 var salesAmounts = document.getElementsByName("salesAmount"+i);
			 
			 for(var j=0 ;j<billAmounts.length; j++){
				 billAmount += Number(billAmounts[j].value);
				 salesAmount += Number(salesAmounts[j].value);
				 
			 }
			 document.getElementById("billAmount_"+i).innerHTML = billAmount;
			 document.getElementById("salesAmount_"+i).innerHTML = salesAmount;
			 billAmount = 0;
			 salesAmount = 0;
		}

		for(var k=0;k<detailLength;k++){
			var sum_bill = 0;
			var sum_sales = 0;
			for(var n=0;n<seriesLength;n++){
				var billAmounts = document.getElementsByName("billAmount"+n);
				var salesAmounts = document.getElementsByName("salesAmount"+n);
				var billAmountValue = billAmounts[k].value;
				var salesAmountValue = salesAmounts[k].value;
				sum_bill += Number(billAmountValue);
				sum_sales += Number(salesAmountValue);
			}
			document.getElementById("billSum"+k).innerHTML = sum_bill;
			document.getElementById("salesSum"+k).innerHTML = sum_sales;
			billAll += Number(sum_bill);
			salesAll += Number(sum_sales);
			sum_bill = 0;
			sum_sales = 0;
		}
		document.getElementById("billAll").innerHTML = billAll;
		document.getElementById("salesAll").innerHTML = salesAll;
	}	
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
