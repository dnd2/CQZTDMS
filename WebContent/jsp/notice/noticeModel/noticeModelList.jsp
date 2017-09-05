<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script src="<%=request.getContextPath()%>/js/notice/noticeModel/noticeModel.js" ></script>
<title>消息提醒模版列表</title>
</head>
<body onload="initQuery();">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:系统管理&gt;经销商管理&gt;消息提醒模版管理

<form method="post" name = "fm" id="fm">
<table class="table_query" border="0">
	<tr>
		<td align="center" colspan="4">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);" />			
<!--             <input class="normal_btn" type="button" value="重 置" onclick="requery()"/> -->
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/notice/noticeModel/NoticeModelAction/toAddNotice.do'"/>
		</td>
	</tr>
</table>


	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
</script>
</body>
</html>