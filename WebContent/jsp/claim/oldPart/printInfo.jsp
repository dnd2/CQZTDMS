<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
<%
	String id = (String)request.getAttribute("id");
%>
<form action="">
	<table>
		<tr>
			<td>
				<select name="boxNo" id="boxNo">
					<option value="">-打印所有-</option>
					<c:forEach var="list" items="${list}">
						<option value="${list.BOX_NO }">${list.BOX_NO }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
		<tr>
			<td>
				<input onclick="returnPrint(<%=id %>)"  type="button" value="打印" />
				<input onclick="history.back();"  type="button" value="返回" />
			</td>
		</tr>
		<!--<tr>
			<td>选择</td>
			<td>装箱单号</td>
		</tr>
		<c:forEach var="list" items="${list}">
		<tr>
			<td><input type="checkbox" name="boxNo" id="boxNo" value="${list.BOX_NO }" /></td>
			<td>${list.BOX_NO }</td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="2"><input onclick="returnPrint(<%=id %>)"  type="button" value="打印" /></td>
		</tr>-->
	</table>
</form>
<script type="text/javascript">
//打印
function returnPrint(id){
	var boxNo=document.getElementById("boxNo").value;
	//if(boxNo==''){
		//MyAlert('请选择装箱单号!');
	//}else{
		window.open('<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/roMainPrint.do?boxNo='+boxNo+'&id='+id,"旧件清单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	//}
}
</script>
</html>