<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>旧件、考核、行政扣款汇总明细</title>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;旧件、考核、行政扣款汇总明细
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_list">
	<tr class="table_list_th">
		<th  >行号</th>
		<th  >经销商代码</th>
		<th  >经销商名称</th>
		<th  >旧件汇总单号</th>
		<th  >旧件抵扣数量</th>
		<th  >抵扣金额</th>
		<th  >操作</th>
	</tr>
	<%
  		List deductInfo=(List)request.getAttribute("deductInfo");
		if(deductInfo!=null){
    	for(int i=0;i<deductInfo.size();i++)
    	{
    		Map map=(Map)deductInfo.get(i);
    		if(i+1>=4) break;
    		int col=i%2;
    		if(col==0) col=1;
    		else col=2;
  	%>
	<tr class="table_list_row<%=col %>">
		<td ><%=i+1 %></td>
		<td  ><%=map.get("DEALER_CODE") %></td>
		<td  ><%=map.get("DEALER_NAME") %></td>
		<td  ><%=map.get("BALANCE_NO") %></td>
		<td  ><%=map.get("DEDUCT_COUNT") %></td>
		<td  ><%=map.get("TOTAL_AMOUNT") %></td>
		<td  ><a href="#" onclick="toDetail(<%=map.get("ID") %>);">明细</a></td>
	</tr>
	<%}}//if(list.size()>=4){%>
	</table>
	<br />
	<table class="table_list">
	<tr class="table_list_th">
		<th  >行号</th>
		<th  >经销商代码</th>
		<th  >经销商名称</th>
		<th  >考核扣款金额</th>
	</tr>
  <%
  		List fineInfo=(List)request.getAttribute("fineInfo");
  		if(fineInfo!=null){
	    for(int i=0;i<fineInfo.size();i++){
	    	Map map=(Map)fineInfo.get(i);
	    	if(i+1>=4) break;
	    	int col=i%2;
	    	if(col==0) col=1;
	    	else col=2;
  %>
	<tr class="table_list_row<%=col %>">
	    <td ><%=i+1 %></td>
	    <td  ><%=map.get("DEALER_CODE") %></td>
		<td  ><%=map.get("DEALER_NAME") %></td>
		<td  ><a href="#" onclick="fineDetail(<%=map.get("FINE_ID") %>);"><%=map.get("FINE_SUM") %></a></td>
	</tr>
	<%}}%>
	</table>
	<br />
	<table class="table_list">
	<tr class="table_list_th">
		<th  >行号</th>
		<th  >经销商代码</th>
		<th  >经销商名称</th>
		<th  >行政扣款金额</th>
	</tr>
  <%
  		List adminDeductInfo=(List)request.getAttribute("adminDeductInfo");
  		if(adminDeductInfo!=null){
	    for(int i=0;i<adminDeductInfo.size();i++){
	    	Map map=(Map)adminDeductInfo.get(i);
	    	if(i+1>=4) break;
	    	int col=i%2;
	    	if(col==0) col=1;
	    	else col=2;
  %>
	<tr class="table_list_row<%=col %>">
	    <td ><%=i+1 %></td>
	    <td  ><%=map.get("DEALER_CODE") %></td>
		<td  ><%=map.get("DEALER_NAME") %></td>
		<td  ><%=map.get("DEDUCT_AMOUNT") %></td>
	</tr>
	<%}}%>
	</table>
	<br/>
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" class="normal_btn" name="backBtn" onclick="closeWindow();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function toDetail(id){
	window.open("<%=contextPath%>/claim/application/DealerBalance/deductInfo.do?id="+id);
}
function fineDetail(id){
	window.open("<%=contextPath%>/claim/other/Bonus/queryDealerByDlrDetail.do?fineId="+id);
}
function closeWindow(){
	try{
		_hide();
	}catch(e){
		window.close();
	}
}
</script>
</body>
</html>