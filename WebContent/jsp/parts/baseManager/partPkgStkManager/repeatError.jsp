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
<title>配件包装储运维护</title>
<style type="text/css">
.Pop_TR{
 background-color:expression(this.rowIndex%2==0 ? "#F7F7F7":"#DAE0EE");
 cursor:hand;
}

</style>
</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;配件包装储运维护导入重复信息</div>
 <form name="fm" method="post" id="fm">
<table class="table_list" >  

		
		<tr style="background: #FFFFCC">
	  	<td align="center">经销商代码</td>
	  	<td align="center">活动编码</td>
	  	<td align="center">配件编码</td>
	  	<td align="center">切换件编码</td>
		</tr>
		<c:forEach items="${dataError}" var="datamap">
			<tr class="Pop_TR">
		  	<td align="center">${datamap.DEALER_CODE }</td>
		  	<td align="center">${datamap.ACTIVITY_CODE }</td>
		  	<td align="center">${datamap.PART_OLDCODE }</td>
		  	<td align="center">${datamap.REPART_OLDCODE }</td>
		  	</tr>
		</c:forEach>
	
</table>
<input type="hidden" name="count" value="${count }"/>

<table width="95%"  align="center" class="table_query">
  <tr class=csstr>
    <td align="center">
        <input class="normal_btn" type="button" value="返回" name="button1"  onClick="goBack()">
    </td>
  </tr>
</table>
</form>
<script type="text/javascript">
	//返回
	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderCheckInit.do";
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
