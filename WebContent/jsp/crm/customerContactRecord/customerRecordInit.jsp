<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customerRecord/customerRecord.js"></script>
<title>驾驶线路维护</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"> 
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt;  日程管理  &gt;任务管理 &gt; 跟进任务 &gt;客户接触履历
	</div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input  type="hidden" id="customerId" value="${customerId}"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		        <td align="right" >
		                                        页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
		        </td>
			</tr>
		</table>
	<!--分页部分  -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>   
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar");</script>
</body>
</html>