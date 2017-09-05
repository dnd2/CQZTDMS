<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>常规订单资源</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	//点击查询执行的方法
	function executeQuery(){
			var url = "<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/orderResourceQuery.do";
			document.fm.action=url;
			document.fm.target="_blank";
			document.fm.submit();
	}
	//清除数据
	function clrTxt(id){
		$(id).value='';
	}
	//点击下载执行的方法
	function downLoadData(){
			document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderOperation/downLoadSourceData.do';
			document.fm.target="_self";
			document.fm.submit();
	}
</script>
</head>
<body  >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;常规订单资源查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
		  <td align="right" nowrap="nowrap">物料代码：</td>
	      <td align="left" nowrap="nowrap">
			<input name="materialCode" id="materialCode" style="width:180px;"/>
	      </td>
	      <td ><input name="queryBtn" type=button class="cssbutton" onClick="executeQuery();" value="查询"></td>
		<td ><input name="downLoad" type=button class="cssbutton" onClick="downLoadData();" value="下载"></td>
	</tr>
	</table>
</form>
</body>
</html>