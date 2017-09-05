<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/companygroup/companygroup.js"></script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 经销商集团 &gt; 虚拟集团维护</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="companyGroupId" id="companyGroupId" value="${po.companyGroupId}" />
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
				<select style="width:134px;" name="status">
					<option value="10011001" <c:if test="${po.status==10011001}">selected</c:if>>有效</option>
					<option value="10011002" <c:if test="${po.status==10011002}">selected</c:if>>无效</option>
				</select>				
			</td>
		</tr>
		<tr>
			<td align="right">集团经销商：</td>
			<td align="left">
				<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="${dealerCode}" size="20"  datatype="0,is_textarea,30" readonly/>
      			<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="button" id="button1" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCode','dealerId','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')"/>
			</td>
			<td align="right">包含的经销商：</td>
			<td align="left">
<!--			<input type="hidden" name="dealerIds" size="15" id="dealerIds" value="" />-->
		  	<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="${dealerCodes}" id="dealerCodes" datatype="0,is_textarea,30"/>
	      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','dealerIds','true', '${orgId}')" value="..." />
			
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
