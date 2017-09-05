<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>经销商用户组修改</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/pcgroup.js"></script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt; 组织架构修改</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="groupId" id="groupId" value="${po.groupId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">组名称：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${po.groupName}" datatype="0,is_textarea,30" />	
			</td>
			<td align="right">状态：</td>
			<td align="left">
	            <input type="hidden" id="status" name="status" value="${po.status}" />
	      		<div id="ddtopmenubar" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ddsubmenu40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1001', loadStatus);" deftitle="--请选择--">
							<c:if test="${po.status==null||po.status==''}">--请选择--</c:if><c:if test="${po.status!=null&&po.status!=''}">${status}</c:if>
							</a>
							<ul id="ddsubmenu40" class="ddsubmenustyle"></ul>
						</li>
					</ul>
					
				</div>
				
			</td>
			
		</tr>
		<tr>
		<td align="center" colspan="4">
			<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="normal_btn" onclick="updateSubmit();" value="保  存" id="addSub" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
	</table>
</form>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar")</script>
</div>
</body>
</html>
