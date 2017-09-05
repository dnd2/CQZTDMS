<%-- 
创建时间 : 2010.09.30
             创建人:lishuai
             功能描述：车厂查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>合同号维护</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	    function clearInput(inputId){
			var inputVar = document.getElementById(inputId);
			inputVar.value = '';
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;合同号修改</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td  align="center">合同号：</td>
		<td  align="center">
		<input type="hidden" name="id"  value="${id}" />
	
			${ContractNo}
		</td>
	</tr>
	<tr>
		<td  align="center">录入新合同号：</td>
		<td  align="center">
			<input class="" type="text" name="CONSTRACT" id="CONSTRACT" value="" />
		</td>
	</tr>
	<tr align="center">
		<td  align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="保存" onclick="saveConstract();"/>
			
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
	function saveConstract(){
		 fm.action="<%=contextPath%>/claim/authorization/BalanceMain/oemContractNumberModify.do?";
		 fm.submit();
		 parentContainer.__extQuery__(1);
		 _hide();
		 
	}

	
</script>
</body>
</html>