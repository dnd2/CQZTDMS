<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="co" %>   
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onload="setFalse();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;问卷管理&gt;问卷浏览 
</div>

<table width="89%" align="center" cellpadding="2" cellspacing="1" id="questionair" class="table_query"  border="1px">
  <tbody>
       <tr  align="center">
      <td height="26" colspan="2" align="left" valign="middle" bgcolor="#ffffff"> 
		<strong><span id="ReturnVisitHandling1_lblHead">${result.questionair[0].QR_NAME} </span></strong></td>
    </tr>
    <tr  align="center">
      <td height="26" colspan="2" align="center" valign="middle" bgcolor="#ffffff">${result.questionair[0].QR_GUIDE}</td>
    </tr>
    <tr class="">
      <td width="94" align="center">题号</td>
      <td width="1057" align="center">问题</td>
    </tr>
    <c:if test="${fn:length(questionhtml)>0}">
    ${questionhtml}
	</c:if>
	<c:if test="${fn:length(questionhtml)==0}">
     <tr class="">
      <td colspan="2" align="center"><font color="red">该问卷还未增加问题。</font></td>
    </tr>
	</c:if>
	
    <tr>
    	<td colspan="2" align="left">${result.questionair[0].QR_THANKS}</td>
    </tr>
  </tbody>
</table>
<table align="center">
	<tbody>
    <tr class="">
      <td colspan="4" align="center" bgcolor="#ffffff">
      <input id="queryBtn4" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="queryBtn3" /></td>
    </tr>
  </tbody>
</table>  
<script type="text/javascript" >
function setFalse()
{
	var clist=document.getElementById("questionair").getElementsByTagName("input");
	var ctextarealist=document.getElementById("questionair").getElementsByTagName("textarea");
	 for(var i=0;i<clist.length && clist[i];i++)
	 {
	 	clist[i].disabled='disabled';
	 }
	 for( var j =0;j<ctextarealist.length;j++)
	 {
	 	ctextarealist[j].disabled='disabled';
	 }
}
</script>
</body>
</html>