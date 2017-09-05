<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/web/role.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
	<title>角色维护</title>
<script>
	var filecontextPath="<%=contextPath%>";
	var tree_url = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/initFunTree.json";
	var roleSearch = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/querySysRoleInit.do";
	var saveUrl = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/sysRoleAdd.json";
	var path = "<%=contextPath%>";
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 角色维护</div>
		<form id="myfm" name="myfm">
			<input type="hidden" value="" id="FUNS" name="FUNS" />
			<input type="hidden" value="" id="GG" name="GG" />
			<table class="table_query">
				<tr>
					<td class="right">角色类别：</td>
				  	<td>
						<script type="text/javascript"> 
							genSelBox("ROLE_TYPE",<%=Constant.SYS_USER%>,"",false,"","onchange='selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)'");
						</script>
				  	</td>
				  	<td rowspan="4" valign="top">
				  		<div id='dtree' class="dtree" style="border:1px solid #5E7692;width: 300px; overflow:auto"></div>
				  	</td>
				</tr>
				<tr>
				  	<td class="right">角色代码：</td>
				  	<td>
			        	<input class="middle_txt" maxlength="30" onkeydown="clearDiv()" datatype="0,is_digit_letter,30" type="text" id="ROLE_NAME" name="ROLE_NAME"/>
			      	</td>
				</tr>
				<tr>
				  	<td class="right">角色名称：</td>
				  	<td>
			        	<input class="middle_txt" maxlength="30" datatype="0,is_null,30" type="text" id="ROLE_DESC" name="ROLE_DESC"/>
			      	</td>
			    </tr>
				<tr>
					<td class="right">状态：</td>
					<td>
			        	<script type="text/javascript"> 
			        		genSelBox("ROLE_STATUS",<%=Constant.STATUS%>,"",false,"","");
			        	</script>
			        </td>
				</tr>
			</table>
			</form>
			<br />
			<table width="100%">
				<tr>
					<td align="center">
						<input class="u-button" type="button" value="保 存" name="queryBtn" onclick="saveRole()"/>
						<input class="u-button" type="button" value="关 闭" onclick="toGoRoleSearch()"/>
					</td>
				</tr>
			</table>
			<br />
			<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
			<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
			<span id="ermsg" style="color: red; position: absolute; margin-top: 1px;"></span>
		</div>
	</div>
</body>
</html>
