<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动总结查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估明细
	</div>
  <form method="post" name="fm" id="fm">
        <table width="100%" class="tab_list">
          <c:forEach var="Evaluate" items="${Evaluate}">
          <tr>
            <td>${Evaluate.DEALER_NAME}</td>   
            <td>${Evaluate.BALANCE_AMOUNT}</td> 
           </tr>
          
          </c:forEach>
          <tr>
           <td colspan="2">
           <input type="button" class="normal_btn" value="关闭" onclick="closeWindow();" >
           </td>
          </tr>
        </table>
</form>
 <br/>
  <script type="text/javascript" >
 	  function closeWindow(){
		if(parent.inIframe){
			parent.window._hide();
		}else{
			window.close();
		}
	  }
 </script>
</body>
</html>