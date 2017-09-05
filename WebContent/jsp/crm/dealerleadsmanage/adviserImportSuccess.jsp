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
<title>月度任务导入 </title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>顾问导入 </div>
	<c:if test="${not empty errorList}">  
		<table class="table_list" width="85%" align="center" border="0">		
		   <tr>
			    <th><div align="center">行号</div></th>
	            <th><div align="center">错误信息</div></th>
	  	   </tr>
			  	<c:forEach items="${errorList}" var="errorList" >
					<tr class="table_list_row${status.index%2+1 }">
						<td><div align="center">${errorList.rowNum }</div></td>
						<td><div align="center">${errorList.errorMsg }</div></td>
					</tr>
				</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty errorList}">  
	<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="left">返回信息</div></th>
  	   </tr>
		<tr>
			<td colspan="2">导入成功</td>
		</tr>
	</table>
	</c:if>
<form id="fm" name="fm"></form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th>
			<div align="center">
			    <input type='hidden' name='year' value='<%=year %>' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='importSave();' value="返 回" />
			</div>
		</th>	
  	</tr>
</table>
<script type="text/javascript">
function importSave() {
	    var url='<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/adviserEnterImportPage.do';
		fm.action = url;
		fm.submit();
}
</script>
</body>
</html>
