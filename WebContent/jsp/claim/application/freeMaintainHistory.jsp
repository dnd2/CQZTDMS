<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> freeMaintaimHisList = (List<Map<String,Object>>)request.getAttribute("freeMaintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>保养历史<%=vin %></title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;保养历史</div>
		</td></tr>
		<tr><td>
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>经销商代码</th>
					<th>经销商名称</th>
					<th>工单号码</th>
					<th>开工单日期</th>
					<th>单据类型</th>
					<th>工单状态</th>
					<th>行驶里程</th>
					<th>保养次数</th>
					<th>索赔单号</th>
					<th>索赔单上报时间</th>
					<th>索赔单号状态</th>
				</tr>
				<% if(freeMaintaimHisList!=null && freeMaintaimHisList.size()>0) { 
					for(int i=0;i<freeMaintaimHisList.size();i++){ 
					Map<String,Object> tempMap = freeMaintaimHisList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_CODE")%>&nbsp;</td>						
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_CREATE_DATE")%>&nbsp;</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"REPAIR_TYPE_CODE")%>) ;
							</script>
						</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"RO_STATUS")%>) ;
							</script>
						</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"IN_MILEAGE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FREE_TIMES")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CLAIM_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPORT_DATE")%>&nbsp;</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>) ;
							</script>
						</td>
					</tr>
				<% } 
				}%>
			</table>
		</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td align="center">
				<input type="button" name="closeBtn" class="normal_btn" value="关闭" onclick="window.close();"/>
			</td>
		</tr>
	</table>
</body>
</html>