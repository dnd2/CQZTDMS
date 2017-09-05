<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
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
	<title>北汽幻速开票</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;补录发票信息
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
     <td>      
           发票号：<input type="text" name="INVOICE_NO"  id="INVOICE_NO"  datatype="0,is_null,20" />
    </td>
    <td>
     发票批号：<input type="text" name="INVOICE_BATCH_NO"  id="INVOICE_BATCH_NO"  datatype="0,is_null,20" />
    </td>
	</tr>
	<tr>
   
    
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="确定" onclick="addDealerUnitKp()"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
	<script type="text/javascript">
	function  addDealerUnitKp()
	{
		  if(!submitForm('fm'))
		 {
			return false;
		 }
	 	fm.action = '<%=contextPath%>/claim/application/DealerNewKp/DealerUnitKpFpU.do?id=${id}';
		fm.submit();
	}
	
	</script>
</body>
</html>