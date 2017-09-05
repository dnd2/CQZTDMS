<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/userMng/blog.js"></script>
<title>微博积分申请</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理&gt;微博积分管理&gt;微博积分新增</div>
 <form method="post" name = "fm" id="fm">
  <input type="hidden" name="contextPaths" id="curPaths" value="<%=request.getContextPath()%>"/>
 	<input type="hidden" id="selectVehiles" name="selectVehiles"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 经销商信息</th>
    <tr>
	<td class="table_query_2Col_label_7Letter">经销商代码：</td>
	<td><label>${dealer.dealerCode}</label>
	</td>
	<td class="table_query_2Col_label_7Letter">经销商名称：</td>
	<td><label>${dealer.dealerName}</label></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_7Letter">提报年份：</td>
		<td><select name="year"><c:forEach items="${yearList}" var="str"><option value="${str}">${str}</option></c:forEach></select></td>
		<td class="table_query_2Col_label_7Letter">提报月份：</td>
		<td><select name="integ_month"><c:forEach items="${list}" var="str"><c:if test="${str==month}"><option value="${str}" selected>${str}</option></c:if><<c:if test="${str!=month}"><option value="${str}">${str}</option></c:if></c:forEach></select></td>
	</tr>
    <tr>
    <td colspan="4">
    	<input type="button" class="normal_btn" name="addVehicle" id="addVehicle" value="添加行" onclick="choosePersons() ;" />&nbsp;
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
				
					<th nowrap="nowrap" width="20%" ><center>销售顾问</center></th>
					<th nowrap="nowrap" width="20%" ><center>微博号1</center></th>
					<th nowrap="nowrap" width="10%" ><center>微博号2</center></th>
					<th nowrap="nowrap" width="10%" ><center>微博号3</center></th>
					<th nowrap="nowrap" width="20%" ><center>微博积分</center></th>
					<th nowrap="nowrap" width="10%" ><center>操作</center></th>
				</tr>
			</table>
		</div>
	</td>
	</tr>
    <tr>
    	<td colspan="4">
	    <table class=table_query>
		 <tr>
		 <td>
		<input type="button" value="保存" name="saveBtn" class="normal_btn" onclick="saveBlogInfo()"/>
		<input type="button" value="提交" name="submitBtn" class="normal_btn" onclick="submission()">	
		<input type="button" value="取消" name="cancelBtn"  class="normal_btn" onclick="history.back();" />
		</td>
		</tr>
   	</table>
   </td>
   </tr>
   </table>
</form>

</body>
</html>
