<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/data/dataDealerManage.js"></script>
<title>经销商用户维护</title>
</head>
<body  onload="setTableBtn();"> 
<div class="wbox" style="height:800px;">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;字典管理 &gt;字典查看 </div>
	
	<form id="fm" name="fm" method="post">
		<input name="dataSource"  type="hidden" value="${tableType}" />
		<input type="hidden" value="<%=contextPath%>" name="curPaths"/>
	<table class="table_query" border="0">
	<tr>
		
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">父级字典代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		${tc.type}
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >父级字典名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
		${tc.typeName}
		</td>
		<td class="table_query_2Col_label_11Letter" nowrap="nowrap">状态：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
	              <script>document.write(getItemValue('${tc.status}'));</script>
        </td>
        
	</tr>
	<tr id="zz" style="display: inline;">
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">本级字典代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		${tc.codeId}
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >本级字典名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
	      ${tc.codeDesc}
		</td>
		<td class="table_query_2Col_label_11Letter" nowrap="nowrap">级数：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
				${tc.codeLevel}
        </td>
		
	</tr>
	<tr>
		<td colspan="6" class="table_query_4Col_input"
			style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn"  value="返回"  onclick="history.back();" id="queryBtn" /> 
			
	</td>
	</tr>
</table>
<div height="200px;" id="curDiv" style="background-color: #F3F4F8;">
<table id="nextTable"  border="1" height="80" width="100%;">
	<tr style="height:20px;">
		<td >机构代码</td>
		<td >机构名称</td>
		<td >子级字典代码</td>
		<td >子级字典名称</td>
	</tr>
	<c:forEach items="${tcList}" var="tc">
		<tr>
			<td >${tc.DEALER_CODE}&nbsp;</td>
			<td >${tc.DEALER_SHORTNAME}&nbsp;</td>
			<td >${tc.CODE_ID}</td>
			<td >${tc.CODE_DESC}</td>
		</tr>
	</c:forEach>
</table>
</div>
</form>
</div>   
</body>
</html>