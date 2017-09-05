<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> maintaimHisList = (List<Map<String,Object>>)request.getAttribute("maintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>维修历史[VIN:<%=vin%>]</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;历史信息</div>
		</td></tr>
		<tr><td>
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>经销商代码</th>
					<th>经销商名称</th>
					<th>特殊费用单号</th>
					<th>单据状态</th>
					<th>VIN</th>
					<th>申报时间</th>
					<th>申报金额</th>
					<th>结算金额</th>
					<th>备注</th>
					<th>审核人</th>
				</tr>
				<% if(maintaimHisList!=null && maintaimHisList.size()>0) { 
					for(int i=0;i<maintaimHisList.size();i++){ 
					Map<String,Object> tempMap = maintaimHisList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_CODE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FEE_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"VIN")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"MAKE_DATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DECLARE_SUM1")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DECLARE_SUM")%>&nbsp;</td>
						<td style="word-wrap:break-word"><%=CommonUtils.getDataFromMap(tempMap,"APPLY_CONTENT")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"PERSON")%>&nbsp;</td>
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