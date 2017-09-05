 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<%  String contextPath = request.getContextPath(); 
%>
<title>服务活动管理</title>	
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

<script type="text/javascript">
	$(function(){
		var res=$("#res").val();
		var wf=$("#wf");
		var str="";
		if(res==9){//有维护件 无800
			str+='<td nowrap="true" width="20%;">开始>></td>';
			str+='<td nowrap="true" width="20%;">服务站</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;">发动机部</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;"><<结束</td>';
		}
		if(res==5){//有维护件 有800
			str+='<td nowrap="true" width="20%;">开始>></td>';
			str+='<td nowrap="true" width="20%;">服务站</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;">发动机部</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;">技术部主管</td>';
			str+='<td nowrap="true" width="20%;"><<结束</td>';
		}
		if(res==10){//无维护件 无800
			str+='<td nowrap="true" width="20%;">开始>></td>';
			str+='<td nowrap="true" width="20%;">服务站</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;"><<结束</td>';
		}
		if(res==6){//无维护件 有800 
			str+='<td nowrap="true" width="20%;">开始>></td>';
			str+='<td nowrap="true" width="20%;">服务站</td>';
			str+='<td nowrap="true" width="20%;">技术部</td>';
			str+='<td nowrap="true" width="20%;">技术部主管</td>';
			str+='<td nowrap="true" width="20%;"><<结束</td>';
		}
		wf.append(str);
	});
</script>
</head>

<form method="post" name="fm" id="fm">
<input name="type" id="res" value="${res}" type="hidden" />
<body>
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权流程展示</div>
<table border="1" cellpadding="1" cellspacing="1" class="table_query" width="140%" style="text-align: center;">
	<tr>
		<th colspan="12">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />预授权流程角色
		</th>
	</tr>
	<tr id="wf">
	
	</tr>
</table>
<br>
 <table  border="0" align="center" cellpadding="0"cellspacing="1" class="table_list"style="border-bottom: 1px solid #DAE0EE">
				<th colspan="5" align="center">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					流程日志
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
						<td><fmt:formatDate value="${r.CREATE_DATE }" pattern='yyyy-MM-dd HH:mm:ss'/></td>
					</tr>
				</c:forEach>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%" nowrap="true">
    				<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="_hide();" class="normal_btn" />
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
</form>
</body>
</html>