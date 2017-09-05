<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
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
   
<form name="fm" id="fm">
 <table class="table_edit">
	   <tr>
<!--	     <td class="table_edit_2Col_label_5Letter">工时系数：</td>-->
         <td><input type="hidden" name="LABOUR_QUOTIETY" id="LABOUR_QUOTIETY" datatype="0,isMoney,7"  value="" class="short_txt"/></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">索赔工时：</td>
         <td><input type="text" name="LABOUR_HOUR" id="LABOUR_HOUR" datatype="0,isMoney,7"  value="" class="short_txt"/></td>
       </tr> 
       <tr>
       <td colspan="2">&nbsp;</td>
       </tr>
	   <tr>
         <td colspan="2" align="center">
         <input class="normal_btn" type="button" name="ok" value="确定" onclick="mainValue();"/>
         <input class="normal_btn" name="back" type="button" value="返回" onclick="_hide();"/>
         </td>
       </tr>               
</table>
</form> 
<script type="text/javascript" >
	function mainValue(){
		if($('LABOUR_HOUR').value!=""){
			_hide();
			parentContainer.getLabourPrice($('LABOUR_QUOTIETY').value,$('LABOUR_HOUR').value);
			
		}else{
			MyAlert("工时数不能为空");
		}
		
		
	}
</script>
</body>
</html>
