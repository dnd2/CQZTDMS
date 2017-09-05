 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<%  String contextPath = request.getContextPath(); 
%>
<title>预授权管理</title>	
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

<script type="text/javascript">
	
</script>
</head>

<form method="post" name="fm" id="fm">
<input name="type" id="res" value="${res}" type="hidden" />
<body>
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权审核记录展示</div>
<br>
 <table  border="0" align="center" cellpadding="0"cellspacing="1" class="table_list"style="border-bottom: 1px solid #DAE0EE">
				<th colspan="5" align="center">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					审核记录
				</th>
				<tr align="center" class="table_list_row1">
					<td nowrap="true">
						操作人
					</td>
					<td nowrap="true">
						角色
					</td>
					<td nowrap="true">
						操作
					</td>
					<td nowrap="true">
						审核内容
					</td>
					<td nowrap="true">
						操作时间
					</td>
				</tr>
				<c:forEach var="r" items="${list }">
					<tr align="center" class="table_list_row1">
						<td>${r.NAME }</td>
						<td>${r.ROLE_NAME }</td>
						<td><change:tcCode value="${r.STATUS }" showType="0"></change:tcCode> </td>
						<td nowrap="true"><textarea rows="3" cols="25" readonly="readonly">${r.REMARK }</textarea> </td>
						<td><fmt:formatDate value="${r.CREATE_DATE }" pattern='yyyy-MM-dd hh:mm:ss'/></td>
					</tr>
				</c:forEach>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%" nowrap="true">
    				<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="window.close();" class="normal_btn" />
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
</form>
</body>
</html>