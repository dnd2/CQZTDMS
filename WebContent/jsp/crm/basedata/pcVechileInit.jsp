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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/competVechile.js"></script>
<title>经销商用户维护</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;客户管理 &gt;竞品维护</div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_query" border="0">
		<tr>
	      <td align="left" nowrap >竞品状态：</td>
	      <td align="left" nowrap >
	      	<select name="status">
	      		<option value="">--请选择--</option>
	      		<option value="10011001">有效状态</option>
	      		<option value="10011002">失效状态</option>
	      	</select>
	      </td>
	     <td >竞品名称：</td>
	     <td ><input type="text" name="competName" id="competName"/></td>
		</tr>
	    <tr>
	    	<td> &nbsp;</td>
			<td  colspan="2" align="left" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
					<c:if test="${fn:contains(funcStr, 1011090301)}">&nbsp;&nbsp;&nbsp;
        			<input name="addBtn" type="button" class="normal_btn" onclick="addGroup()" value="新增" />
        			</c:if>
			</td>
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
</body>
</html>