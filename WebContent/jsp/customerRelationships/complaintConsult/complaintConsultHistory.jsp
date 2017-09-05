<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> complaintInfoList = (List<Map<String,Object>>)request.getAttribute("complaintInfoList"); 
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>投诉咨询记录 </title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：投诉咨询管理&gt;投诉咨询记录 </div>
		</td></tr>
		<tr><td>
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>客户姓名</th>
					<th>受理日期</th>
					<th>受理人</th>
					<th>状态</th>
					<th>业务类型</th>
					<th>投诉咨询内容</th>					
					<th>查看</th>
				</tr>
				<% if(complaintInfoList!=null && complaintInfoList.size()>0) { 
					for(int i=0;i<complaintInfoList.size();i++){ 
					Map<String,Object> tempMap = complaintInfoList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CTMNAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"ACCDATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"ACCUSER")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"BIZTYPE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CPCONTENT")%>&nbsp;</td>
						<td><a href="<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?cpid=<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>&ctmid=<%=CommonUtils.getDataFromMap(tempMap,"CTMID")%>">查看</a></td>
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