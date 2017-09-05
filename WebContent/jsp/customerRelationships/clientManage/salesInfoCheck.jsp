<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	
	function checkSubmit(status){
		makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/save.json?status='+status,saveBack,'fm','');
	}
	
	function saveBack(){
		parent.MyAlert("操作成功!");
		if (parent.$('inIframe')) {
			 parentContainer.queryInfo();
		} else {
			parent.queryInfo();
		}
		parent._hide();
	}
</script>

<title>实销信息管理</title>
</head>
<body onunload='doInit();'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户管理 &gt; 
<c:if test="${comm==1 }">呼叫中心</c:if>
<c:if test="${comm==2 }">销售部</c:if>
实销客户信息审核</div>
<form id="fm" name="fm" method="post">
<table class="table_edit" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审批结果</th>
	</tr>
	<tr>
		<td width="15%" align=right>审批意见：</td>
		<td width="85%" align="left"><textarea id="checkRamark" name="checkRamark" cols="80" rows="4"></textarea></td>
	</tr>
	<tr>
		<td colspan="4"  align="center">
			<input type="hidden" name="comm" id="comm" value="${comm }" />
			<input type="hidden" name="ids" id="ids" value="${ids }" />
			<c:if test="${comm==1 }">
				<input type="button" value="通过" class="normal_btn" onclick="checkSubmit(<%=Constant.CALLCENTER_CHECK_STATUS_02 %>);" />
       			<input type="button" value="驳回" class="normal_btn"  onclick="checkSubmit(<%=Constant.CALLCENTER_CHECK_STATUS_03 %>);" /> 
			</c:if>
			<c:if test="${comm==2 }">
				<input type="button" value="通过" class="normal_btn" onclick="checkSubmit(<%=Constant.SALESX_CHECK_STATUS_02 %>);" />
	       		<input type="button" value="驳回" class="normal_btn"  onclick="checkSubmit(<%=Constant.SALESX_CHECK_STATUS_03 %>);" /> 
			</c:if>
       		<input type="button" onclick="parent._hide();"class="normal_btn" value="关闭" />
		</td>
	</tr>
</table>
</form>
</body>
</html>