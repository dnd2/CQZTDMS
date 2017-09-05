<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史购车信息</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;服务车购车历史</div>
<form method="post" name = "fm" id="fm" >
	<table class="table_list">
		<tr > 
			<th height="12" align=center>
				<input type="button" onClick="closeWindow()" class="normal_btn" style="width=8%" value="关闭"/>
				&nbsp;&nbsp;
			</th>
		</tr>
	</table>    
</form>
<script type="text/javascript">
	function closeWindow(){
		window.close();
	}
</script>
</body>
</html>