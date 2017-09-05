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
<title>人员注册</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/companygroup/companygroup.js"></script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 经销商集团 &gt; 新集团添加</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="left">集团名称：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="groupName"  datatype="0,is_textarea,30"  />	
			</td>
			<td align="left">集团经销商：</td>
			<td align="left">
				<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="${dealerCode}" size="20"  datatype="0,is_textarea,30" />
      			<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="button" id="button1" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCode','dealerId','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')"/>
			</td>
			<td align="left">包含的经销商：</td>
			<td align="left">
			<input type="hidden" name="dealerIds" size="15" id="dealerIds" value="" />
		  	<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="" id="dealerCodes" datatype="0,is_textarea,30"/>
	      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','dealerIds','true', '${orgId}')" value="..." />
				
			</td>
		</tr>
		<tr>
			<td  align="center"  colspan="6">
				<input type="button" class="normal_btn" onclick="addSubmit();" value="保  存" id="addSub" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>
