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
<title>模板文件下载</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>模板文件下载</div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr >
		    <th><div align="center">模板名称</div></th>
            <th><div align="center">操作</div></th>
  	   </tr>
  	<c:forEach items="${list}" var="paraMap" varStatus="status">
		<tr class="table_list_row${status.index%2+1 }">
			<td><div align="center">${paraMap.PARA_NAME }</div></td>
			<td><div align="center">
				<input type="button" name="down_btn" value="下载" class="cssbutton" onclick="downLoadTemp(${paraMap.PARA_ID });" />
			</div></td>
		</tr>
	</c:forEach>
  	
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="left">
			    <input type='hidden' name='fielId' id="fielId" value='' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
	function downLoadTemp(fileId) {
	    document.getElementById("fielId").value=fileId;
		var url='<%=contextPath %>/sysmng/paraConfig/TemplateDownLoad/templateDownLoad.do';
		frm.action = url;
		frm.submit();
   }
</script>
</body>
</html>
