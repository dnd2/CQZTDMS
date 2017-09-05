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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/userMng/blogOem.js"></script>
<title>微博积分申请</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理&gt;微博积分管理&gt;微博积分查看</div>
 <form method="post" name = "fm" id="fm">
  <input type="hidden" name="contextPaths" id="curPaths" value="<%=request.getContextPath()%>"/>
  <input type="hidden" name="blogId" id="blogId" value="${blog.blogId}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 经销商信息<label></th>
    <tr>
	<td class="table_query_2Col_label_7Letter">经销商代码：</td>
	<td><label>${dealer.dealerCode}</label>
	</td>
	<td class="table_query_2Col_label_7Letter">经销商名称：</td>
	<td><label>${dealer.dealerName}</label></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_7Letter">提报月份：</td>
		<td><select name="integ_month" disabled="disabled"><c:forEach items="${list}" var="str"><<c:if test="${str==blog.integMonth}"><option value="${str}" selected>${str}</option></c:if><option value="${str}">${str}</option></c:forEach></select></td>
		<td></td>
		<td></td>
	</tr>
    <tr>
    <td colspan="4">
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
				
					<th nowrap="nowrap" width="20%" ><center>销售顾问</center></th>
					<th nowrap="nowrap" width="20%" ><center>微博号1</center></th>
					<th nowrap="nowrap" width="10%" ><center>微博号2</center></th>
					<th nowrap="nowrap" width="10%" ><center>微博号3</center></th>
					<th nowrap="nowrap" width="20%" ><center>微博积分</center></th>
				</tr>
				<c:forEach var="po" items="${detailList}">
					<tr id="tr${po.DETAIL_ID }">
					<td><input id="salesIds${po.DETAIL_ID}" type="hidden" name="salesIds" value="${po.SALES_ID}" ><label>${po.NAME}</label></td>
					<td><label>${po.BLOG_ONE}</label></td>
					<td><label>${po.BLOG_TWO}</label></td>
					<td><label>${po.BLOG_THREE}</label></td>
					<td><label>${po.BLOG_INTEG}</label></td>
					</tr>
				</c:forEach>
				
			</table>
		</div>
	</td>
	</tr>
    <tr>
    	<td colspan="4">
	    <table class=table_query>
		 <tr>
		 <td>
		<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.back();" />
		</td>
		</tr>
   	</table>
   </td>
   </tr>
   </table>
</form>

</body>
</html>
