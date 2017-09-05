<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String year = (String)request.getAttribute("year");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
function importSave() {
	history.back();
}
</script>


<title>物料价格管理 </title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>系统管理>组织管理>物料价格管理 </div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">行号</div></th>
            <th><div align="center">错误信息</div></th>
  	   </tr>
  	<c:forEach items="${errorList}" var="errorList" >
		<tr class="table_list_row${status.index%2+1 }">
			<td><div align="center">${errorList.rowNum }</div></td>
			<td><div align="center">${errorList.errorDesc }</div></td>
		</tr>
	</c:forEach>
  	
</table>

<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="center">
			    <input type='hidden' name='year' value='<%=year %>' />
				<input class="u-button u-query" type='button' name='saveResButton' onclick='importSave();' value='确定' />
			</div>
		</th>	
  	</tr>
</table>
</form>

</body>
</html>
