<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.math.BigDecimal"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆PIN码导入</title>
</head>
<body >
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置&gt;售后服务管理&gt;车辆信息管理&gt;车辆PIN码导入 </div>
  <form method="post" name="fm" id="fm">
	
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
	      <th style="width:50%" >VIN</th>
		<th style="width:50%" align="center">PIN</th>
       </tr>
<c:forEach items="${list}" var="aa">
<tr>
<TD ><input type="hidden" value="${aa.VIN }" name="VIN">${aa.VIN }</TD>
<TD><input type="hidden" value="${aa.PIN }" name="PIN">${aa.PIN }</TD>
</tr>

</c:forEach>

  	
</table>

<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" class="table_list_row1">
		<th colspan="6">
			<div align="center">
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='确认导入' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
 function isSave(){
          if(submitForm('fm')){
      	    MyConfirm("是否确认保存信息?",importSave);
          }
       }
function importSave() {
		document.getElementById("savebtn").disabled=true;
		fm.action = "<%=contextPath%>/repairOrder/VehicleQueryPin/importExcel.do";
		fm.submit();
}
</script>
</body>
</html>
