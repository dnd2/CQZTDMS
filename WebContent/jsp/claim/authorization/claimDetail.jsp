<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>索赔单附件上传</title>
	</head>
<body >
<form method="post" name="fm" id="fm">
	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;三包档案抽查管理&gt;索赔单附件上传
			    </div>
		    </td>
	    </tr>
	</table>
	
	<table class="table_list">
	<tr class="table_list_th">
		<th>行号</th>
		<th>索赔单号</th>
		<th>是否上传附件</th>
		<th>操作</th>
	</tr>
	<c:forEach var="dtl" items="${detail}" varStatus="vs">
		<tr class="table_list_row${vs.index%2+1}">
			<td >${vs.index+1}</td>
			<td>${dtl.CLAIM_NO}</td>
			<c:if test="${dtl.UPFILE_STATUS==91051002}">
			<td>已上传</td>
			</c:if>
			<c:if test="${dtl.UPFILE_STATUS==91051001}">
			<td>未上传</td>
			</c:if>
			<td><a href="#" onclick="upfile(${dtl.ID},${dtl.CHECK_ID});">添加附件</a></td>
		</tr>
	</c:forEach>
	</table>
	
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" onClick="goBack();" class="normal_btn" value="返回" />
			</td>
		</tr>
	</table>
</form>
</body>
<script type="text/javascript">
	function upfile(id,checkId){
		fm.action="<%=contextPath%>/claim/application/DealerNewKp/claimUpFile.do?ID="+id+"&&checkId="+checkId;
		fm.submit();
	}
	function goBack(){
		fm.action="<%=contextPath%>/claim/application/DealerNewKp/checkApplicationInit.do";
		fm.submit();
	}
</script>
</html>