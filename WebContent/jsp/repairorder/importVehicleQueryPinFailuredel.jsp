<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆PIN码导入  </title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置&gt;售后服务管理&gt;车辆信息管理&gt;车辆PIN码导入 </div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
	
            <th><div align="center">行号</div></th>
            <th><div align="center">错误信息</div></th>
  	   </tr>
  	   <tr>
	  <td><div align="center">${errorInfo.num}</div></td>
	  <td><div align="center">${errorInfo.err}</div></td>

		</tr>

  	
</table>

<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="left">
				<input class="cssbutton" type='button' name='saveResButton' onclick='importSave();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
	
			frm.action ='<%=contextPath%>/repairOrder/VehicleQueryPin/VehicleQueryPinimport.do';
			frm.submit();
		}
}
</script>
</body>
</html>
