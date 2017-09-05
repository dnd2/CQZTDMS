<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<head>
<%
String contextPath=request.getContextPath();
List<Map<String, Object>>  errorJsp =(List<Map<String, Object>> )request.getAttribute("errorJsp");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售经销商信息导入</title>

<script language="JavaScript">
	function finish(){
	   var url="<%=contextPath %>/sysmng/dealer/DealerInfo/queryDealerInfoInit.do";
        location=url;
	}
	
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置 >系统管理>经销商管理>销售经销商维护
	</div>
<table class=table_query>
	
	
	 <%
	if(errorJsp!=null && errorJsp.size()>0)	{
		%>
		<tr>
	  <td align="left" colspan="2"><font color="red">以下数据未导入数据库，具体错误信息请看下面</font></td>
	  </tr>
		<tr>
	  <td align="center">行号</td>
	  <td align="center">错误信息</td>
	  </tr>
		<%
		for(int bo=0;bo<errorJsp.size();bo++){
			Map<String, Object> errMap=(Map<String, Object>)errorJsp.get(bo);
	%>
	<tr>
	 <td align="center">
	 	<%=errMap.get("rowNum") %>
	 </td>
	  <td align="center">
	    <%=errMap.get("errorInfo") %>
	 </td>
	  </tr>
	  <%
		}
	}
	%>
 
  <tr>
    <td align="center" colspan="2">
    <input class="cssbutton" type="button" value="确认" name="button1" onclick="finish();" />
    </td>
  </tr>
</table>
</div>
</body>
</html>