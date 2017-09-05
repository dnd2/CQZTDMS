<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%
String contextPath = request.getContextPath();
HashMap hm = (HashMap)request.getAttribute("SELMAP"); //参数对应的值
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="ID" id="ID" value="<%=hm.get("ID")==null?"":hm.get("ID")%>"/>
<input type="hidden" name="W_ID" id="W_ID" value="<%=(String)request.getAttribute("WID")%>"/>
<table class="table_add">
 <tr>
 	<td class="table_add_2Col_label_6Letter">附加工时代码：</td>
 	<td><%=hm.get("LABOUR_CODE")==null?"":hm.get("LABOUR_CODE")%></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_6Letter">附加工时名称：</td>
 	<td><%=hm.get("CN_DES")==null?"":hm.get("CN_DES")%></td>
<%-- 	<td><input type="text" name="ADD_CN_DES" id="ADD_CN_DES" value="" class="middle_txt" datatype="0,is_null,100"/></td>--%>
 </tr> 
 <tr>
 	<td class="table_add_2Col_label_6Letter">工时系数：</td>
 	<td><input type="text" name="ADD_LABOUR_QUOTIETY" id="ADD_LABOUR_QUOTIETY" value="<%=hm.get("LABOUR_QUOTIETY")==null?"":hm.get("LABOUR_QUOTIETY")%>" class="middle_txt" datatype=0,is_double,7/></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_6Letter">索赔工时：</td>
 	<td><input type="text" name="ADD_LABOUR_HOUR" id="ADD_LABOUR_HOUR" value="<%=hm.get("LABOUR_HOUR")==null?"":hm.get("LABOUR_HOUR")%>" class="middle_txt" datatype=0,is_double,7/></td>
 </tr>
</table>

<table class="table_edit" >
 <tr>
 	<td colspan="2" align="center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="checkForm();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="JavaScript:history.back();" value="返回"/>
 	</td>
 </tr>
</table>

</form>
<script>
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? Add() : "";
}
//表单提交方法：
function Add(){
	MyConfirm("是否确认修改?",updateLabour);
}
function updateLabour(){
	disableBtn($("commitBtn"));
	fm.action = '<%=contextPath%>/claim/basicData/ClaimLaborMain/additionalLaborUpdate.do';
	fm.submit();
}
</script>
</body>
</html>
