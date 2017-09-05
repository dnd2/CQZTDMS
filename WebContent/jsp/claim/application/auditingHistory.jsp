<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> auditingHisList = (List<Map<String,Object>>)request.getAttribute("auditingHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>授权历史[VIN:<%=vin%>]</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;授权历史</div>
		</td></tr>
		<tr><td>
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>类型</th>
					<th>单号</th>
					<th>经销商名称</th>
					<th>授权时间</th>
					<th>授权人</th>
					<th>授权结果</th>
					<th>授权备注</th>
					<th>工时名称</th>
					<th>配件名称</th>
					<th>状态</th>
				</tr>
				<% if(auditingHisList!=null && auditingHisList.size()>0) { 
					for(int i=0;i<auditingHisList.size();i++){ 
					Map<String,Object> tempMap = auditingHisList.get(i);
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"ORDER_TYPE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CLAIM_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"AUTH_DATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"AUTH_NAME")%>&nbsp;</td>
						<td>
							<script type="text/javascript">
								document.write(getItemValue('<%=CommonUtils.getDataFromMap(tempMap,"IS_AGREE")%>'));
							</script>&nbsp;
						</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REMARK")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"WR_LABOURNAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"PART_NAME")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>);
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