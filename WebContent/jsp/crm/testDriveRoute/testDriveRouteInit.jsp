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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/testDriveRoute/testDriveRoute.js"></script>
<title>驾驶线路维护</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;字典管理 &gt; 试驾路线维护
	</div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
		      	<td align="right" width="25%">
		      		线路名称：
		      	</td>
		      	<td>
		      		<input type="text" id="routeName" name="routeName"   />
		      	</td>
			    <td align="right">
			                        里程数：
			    </td>
			    <td >
			    	<input type="text" name="mileage" id="mileage"/>
			    </td>
			</tr>
		    <tr>
		    	<td> &nbsp;</td>
				<td colspan="2" align="center" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
					 <c:if test="${fn:contains(funcStr, 1011090201)}">&nbsp;&nbsp;&nbsp;
	        		    <input name="addBtn" type="button" class="normal_btn" onclick="addRoute()" value="新增" />
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
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar")</script>
</body>
</html>