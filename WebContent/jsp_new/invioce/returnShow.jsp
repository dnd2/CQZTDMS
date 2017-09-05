<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>汇总出库根据配件代码批量修改供应商</title>
<% String contextPath = request.getContextPath();%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript">
	$(function(){
		var val=0;
		$("input[name='auth_price']").each(function(){
			val+=parseFloat($(this).val());
		});
		$("#sum_all").text(val);
	});
</script>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;回运运费明细</div>
 <form method="post" name ="fm" id="fm">
 <table id="addOutPart" border="1" cellpadding="0" cellspacing="0" class="table_query" width="100%" style="text-align: center;">
 	<th colspan="13">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>回运运费明细&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp;    运费合计：<span style="color: red;" id="sum_all"></span>
	</th>
 	<tr>
         <td nowrap='true' width="20%">回运单号</td>
         <td nowrap='true' width="20%">审核运费</td>
         <td nowrap='true' width="20%">回运类型</td>
     </tr>
     <c:forEach var="t" items="${list}">
     	<tr>
     		 <td nowrap='true' width="20%">${t.RETURN_NO }</td>
     		 <td nowrap='true' width="20%"><input type="hidden" name="auth_price" value="${t.AUTH_PRICE }"/>${t.AUTH_PRICE }</td>
     		 <td nowrap='true' width="20%">${t.RETURN_TYPE }</td>
     	 </tr>
     </c:forEach>
</table>
 <table id="addOutPart" border="1" cellpadding="1" cellspacing="1" class="table_query" width="100%" style="text-align: center;">
 		<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="_hide();" class="normal_btn" />
    	</td>
    </tr>
</table>
</form>
<br />
</body>
</html>
