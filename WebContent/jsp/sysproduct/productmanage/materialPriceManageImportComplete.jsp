<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料价格维护</title>

<script language="JavaScript">

	function finish(){
	    var url='<%=contextPath %>/sysproduct/productmanage/MaterialPriceMaintenance/MaterialPriceInit.do';
		location=url;
	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护
	</div>
<table class="table_query">
  <tr>
    <td align = left class="zi01"> 物料价格导入成功。</td>
  </tr>
</table>
<table class=table_query>
  <tr>
    <td align="center"><input class="cssbutton" type="button" value="返回" name="button1" onclick="finish();" />
    </td>
  </tr>
</table>
</div>
</body>
</html>