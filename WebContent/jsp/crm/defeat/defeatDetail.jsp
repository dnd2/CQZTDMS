<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>战败实效查询</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/defeat/defeat.js"></script>
</head>
<body onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 战败/失效管理 &gt; 战败/失效查询</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="defeatId" id="defeatId" value="${tpd.defeatfailureId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
<div>
				<b style="display: inline-block; float: left">战败/失效信息</b>
				<hr style="display: inline-block;"/>
			</div>
	<table class="table_query" border="0">
		<tr>
			<td align="right">客户姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpc.customerName}"   readonly/>	
			</td>
			<td align="right">手机：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpc.telephone}"   readonly/>	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向车型：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpiv.seriesName}"   readonly/>	
			</td>
		</tr>
		<tr>
			<td align="right">战败车型：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpdv.seriesName}"  readonly/>
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">顾问：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tu.name}"  readonly />	
			</td>
			<td align="right"><!-- 战败/失效时间： --></td>
			<%-- <td align="left">
				<c:if test="${tpd.defeatfailureType==60391001}">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${map.endDate}" readonly/>	
				</c:if>
				<c:if test="${tpd.defeatfailureType==60391002}">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${failureDate}" readonly/>	
				</c:if>
			</td> --%>
			
		</tr>
		<tr>
			<td align="right">战败原因：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${defeatReason}"  readonly/>	
			</td>
			<td align="right">&nbsp;</td>
			<td align="left">
				&nbsp;
<!--				<input type="text" class="middle_txt" name="groupName" id="name" value="${tc.codeDesc}" datatype="0,is_textarea,30" disabled />	-->
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">&nbsp;</td>
			<td align="left">
			</td>
		</tr>
		<tr>
			<td align="right">原因分析：</td>
			<td align="left" colspan="5">
				<textarea rows="5" cols="70" readonly>${tpd.reasonAnalysis}</textarea>
			</td>
		</tr>
		
		<tr>
			<td align="right">审核结果：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${audit_result}"   readonly/>	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">&nbsp;</td>
			<td align="left">
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">&nbsp;</td>
			<td align="left">
			</td>
		</tr>
		
		<tr>
		<td align="center" colspan="6">
			<input type="button" class="normal_btn" onclick="history.back();" value="返  回" id="retBtn" />
		</td>
		</tr>
	</table>
	
	<div>
				<b style="display: inline-block; float: left">审核记录</b>
				<hr style="display: inline-block;"/>
	</div>
	<table class="table_query" border="0">
		<tr>
			<td align="left">审核类型</td>
			<td align="left">审核人：</td>
			<td align="left">审核结果</td>
			<td align="left">审核意见</td>
			<td align="left">
				审核时间	
			</td>
		</tr>
		<c:forEach var="po" items="${auditList}">
			<tr>
				<td align="left">${po.AUDIT_TYPE}</td>
				<td align="left">${po.NAME}</td>
				<td align="left">${po.AUDIT_RESULT}</td>
				<td align="left">${po.AUDIT_REMARK}</td>
				<td align="left">${po.AUDIT_TIME}	</td>
			</tr>
		</c:forEach>
	</table>
</form>
</div>
</body>
</html>
