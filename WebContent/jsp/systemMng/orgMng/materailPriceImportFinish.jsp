<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料价格管理</title>

<script language="JavaScript">
	function finish(){
		var value = document.getElementById('PRICE_ID').value;
		location = '<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceQueryShow.do?PRICE_ID='+value;
	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>系统管理>组织管理>物料价格管理
	</div>
<table class="table_query">
  <input type="hidden" name="PRICE_ID" id="PRICE_ID" value="${PRICE_ID}"/>
  <tr>
    <td align = left class="zi01"> 返利任务导入已完成 </td>
  </tr>
</table>
<table class=table_query>
  <tr>
    <td align="center"><input class="u-button u-cancel" type="button" value="返回" name="button1" onclick="finish();" />
    </td>
  </tr>
</table>
</div>
</body>
</html>