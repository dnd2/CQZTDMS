<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车厂配额导入</title>

<script language="JavaScript">
<!--
	function finish(){
	    var url='<%=contextPath %>/sales/planmanage/QuotaAssign/StorageImport/storageImportPre.do';
		location=url;
	}
//-->
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 车厂配额导入
	</div>
<table class="table_query">
  <tr>
    <td align = left class="zi01"> ${year }年${month }月 车厂配额导入成功。</td>
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