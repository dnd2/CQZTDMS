<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>装箱单管理-查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
	
		document.write(str);
	}
	//获取序号
	function getIdx(id){
		document.write(document.getElementById(id).rows.length-1);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=true';
	}
	function detailOrder(id){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/pickOrderDetail.do?soId='+id;
	}
</script>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置:配件管理 > 配件销售管理  &gt;装箱单管理 &gt;查看</div>
	<table id="file" class="table_list" >
    <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
    <tr>
      <th>序号</th>
      <th>销售单号</th>
      <th>订货单位</th>
      <th>金额</th>
      <th>销售日期</th>
      <th>备注</th>
      <th>操作</th>
    </tr>
     <c:forEach items="${soOrderList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">getIdx("file");</script>
   			  </td>
   			  <td align="center"><c:out value="${data.SO_CODE}" /></td>
   			  <td align="center"><c:out value="${data.DEALER_NAME}" /></td>
   			  <td align="center"><c:out value="${data.AMOUNT}" /></td>
   			  <td align="center"><c:out value="${data.CREATE_DATE}" /></td>
   			  <td align="center"><c:out value="${data.REMARK}" /></td>
		      <td align="center"><a href="#" onclick='detailOrder(${data.SO_ID})'>[查看]</a></td>
		 </tr>
	</c:forEach>
  </table>
	<table border="0" class="table_query">
	  <tr>
	  	<td class="center"><input class="u-button" type="button" value="返 回" onclick="goBack()"/></td>
	  </tr>
	 </table>
</div>
</body>
</html>

