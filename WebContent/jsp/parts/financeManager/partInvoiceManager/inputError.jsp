<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>开票信息导入</title>

</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;开票信息导入错误信息</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
<table class="table_list" >  
	<c:if test="${errArr!=null}">
		<c:forEach items="${errArr}" var="list">
		<tr>
	  	  <td  align="left">${list }</td>
	  	</tr>
		</c:forEach>
	</c:if>
</table>
<table width="95%"  align="center" class="table_query">
  <tr class=csstr>
    <td align="center">
        <input class="normal_btn" type="button" value="返回" name="button1"  onClick="goBack()">
    </td>
  </tr>
</table>
</form>
</div>
<script type="text/javascript">
	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/partInvoiceQueryInit.do";
		fm.submit();
	}
	
		//失效按钮
	function btnDisable(){
	
	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });
	
	}
	
	//有效按钮
	function btnEnable(){
	
	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });
	
	}
</script>
</body>
</html>
