<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();    
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时调整设定</title>
<script type="text/javascript">
function goBack()
{
	location="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/adjustLaborHoursetpre.do";
}

function mysubmit()
{
	if(my_is_money()==true)
	{
		fm.action="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/adjustUpdate.do";
		fm.submit();	
	}
}


function my_is_money()
{
	var obj=document.getElementById("adjustPrice");
	var adjustPrice=document.getElementById("adjustPrice").value;	
	if (isNaN(adjustPrice))
	{
		 var tipid = getTip();
		 showTip(obj,"输入错误",tipid);
		 return false 
	}	
	if(adjustPrice.length==0||adjustPrice.length>10)
	{
		var tipid = getTip();
		showTip(obj,"必填，但不能大于10位",tipid);
		return false;
	}
	else
	{
		return true;
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;
    &nbsp;当前位置：  索赔基础数据  &gt; 索赔工时调整设定</div>

<form name="fm" id="fm"  method="post">
<table class="table_edit">  
   <tr>      
       <c:if test="${adjust.adjustMode==lv}">
          <th style="text-align:center;">经销商级别代码</th>
          <th style="text-align:center;">经销商级别名称</th>
       </c:if>
       <c:if test="${adjust.adjustMode==cls}">
          <th style="text-align:center;">经销商类型代码</th>
          <th style="text-align:center;">经销商类型名称</th>
       </c:if>
       <th style="text-align:center;">调整价格</th>             
   </tr>  
  <tr class="table_list_row1">	  
	   <td align="center">${adjust.typeAdjust}</td>
	   <td align="center">${adjust.codeDesc}</td>
	   <td align="center"><input id="adjustPrice" name="adjustPrice" value="${adjust.adjustPrice}"/></td>	   	
  </tr>
  <tr class="table_list_row2">
       <td colspan="3" align="center">
          <input type="hidden" name="id" value="${adjust.id}"/>
          <input type="button" class="normal_btn" onclick="mysubmit();" value="确定"/>&nbsp;
          <input type="button" class="normal_btn" onclick="goBack();" value="取消"/>
       </td>
  </tr>
</table>
</form>
</body>
</html>