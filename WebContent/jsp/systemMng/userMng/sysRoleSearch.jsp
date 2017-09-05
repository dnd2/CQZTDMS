﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
	<title>角色维护</title>
	<script>
		var myPage;
	
		var url = "<%=contextPath%>/sysmng/sysrole/ActionSysRole/sysRoleQuery.json?COMMAND=1";
		  
		//设置表格标题
		var title = null;
		
		// 自定义方法属性
		var roleObj = {
			add : function(){
				OpenHtmlWindow("<%=contextPath%>/sysmng/sysrole/ActionSysRole/addSysRoleInit.do", 650, 380, '角色维护');
			},
			edit : function edit(editurl) {
				OpenHtmlWindow(editurl, 650, 380,'角色维护');
			},
			myLink : function(value, metadata){
		        return String.format("<a href='javascript:;' onclick=roleObj.edit(\"<%=contextPath%>/sysmng/sysrole/ActionSysRole/viewSysRoleInit.do?roleId=" + value + "\")>[修改]</a>");
		    }
	   	};
		
		//设置列名属性
		var columns = [
			{header: "序号", align:'center', renderer:getIndex, width:'7%'},
			{header: "操作", id:'action', sortable: false, dataIndex: 'ROLE_ID', renderer:roleObj.myLink},
			{header: "角色代码", dataIndex: 'ROLE_NAME', align:'center', orderCol:"ROLE_NAME"},
			{header: "角色名称", dataIndex: 'ROLE_DESC', align:'center'},
			{header: "角色类别", dataIndex: 'ROLE_TYPE', align:'center',renderer:getItemValue},
			{header: "状态", dataIndex: 'ROLE_STATUS', align:'center',renderer:getItemValue, orderCol:"ROLE_STATUS"},
			{header: "最后操作人", dataIndex: 'UPDATE_NAME', align:'center'},
			{header: "最后操作时间", dataIndex: 'UPDATE_DATE', align:'center'}
		];
	
		   $(document).ready(function(){__extQuery__(1);});
		   
	</script>
</head>

<body>
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt; 角色维护</div>
		<form id="fm" name="fm">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query" border="0">
						<tr>
							<td class="right">角色代码：</td>
							<td>
								<input name="ROLE_NAME" maxlength="30" value="" datatype="1,is_noquotation,30" id="ROLE_NAME" type="text" class="middle_txt" />
							</td>
							<td class="right">角色名称：</td>
							<td>
								<input name="ROLE_DESC" maxlength="30" value="" datatype="1,is_noquotation,30" id="ROLE_DESC" type="text" class="middle_txt" />
								<span id="rolenameerr"></span>
							</td>
							<td class="right">角色类别：</td>
							<td>
								<script type="text/javascript"> 
									genSelBox("ROLE_TYPE",<%=Constant.SYS_USER%>,"",false,"","onchange='__extQuery__(1)'");
								</script>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="text-align: center">
								<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
								<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
								<input type="button" class="u-button u-submit" onclick="roleObj.add();" value="新 增" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>