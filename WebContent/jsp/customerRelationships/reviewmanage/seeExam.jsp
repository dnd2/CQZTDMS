<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); 
	request.setAttribute("QD_QUE_TYPE_1",Constant.QD_QUE_TYPE_1);
	request.setAttribute("QD_QUE_TYPE_2",Constant.QD_QUE_TYPE_2);
	request.setAttribute("QD_QUE_TYPE_3",Constant.QD_QUE_TYPE_3);
	request.setAttribute("QD_QUE_TYPE_4",Constant.QD_QUE_TYPE_4);
	request.setAttribute("qd_txt_type_1",Constant.qd_txt_type_1);
	request.setAttribute("qd_txt_type_2",Constant.qd_txt_type_2);
%>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt; 回访管理 &gt;题库管理&gt;问题浏览
</div>
 <table width="100%" align="center" cellpadding="2" cellspacing="1" class=table_query>
 <c:if test="${fn:length(questionlist)==0}">
 	<tr ><td align="center" colspan="2" class="table_list_row0" style="background-color:#FFFFCC;height:80px;">该题库中没有问题 ！</td></tr>
 </c:if>
    <c:forEach var="ql" items="${questionlist}">
    	<tr ><td align="right">问题 ${ql.ED_NO}：</td>
        	<td align="left" style="WORD-WRAP: break-word; WORD-BREAK: break-all">${ql.ED_QUESTION}</td>
        </tr>
    	<c:choose>
			 <c:when test="${ql.ED_QUE_TYPE==QD_QUE_TYPE_1}"><!-- 单选 -->
				<tr class="">
		        <td  align="left" style="WIDTH: 17%">&nbsp;</td>
		        <td width="1046">
		        <table id="ctl00" cellspacing="5" border="0">
		          <tbody><tr>
		          <c:forTokens items="${ql.ED_CHOICE}" delims="\\|" var="tech">
					<td><input id="ctl00_0" type="radio" value="0" name="ctl00" />
		                <label for="ctl00_0">${tech}</label></td>
				  </c:forTokens>
		            </tr></tbody></table></td>
		       </tr>
			 </c:when>
			 <c:when test="${ql.ED_QUE_TYPE==QD_QUE_TYPE_2}"><!-- 多选 -->
				<tr class="">
		        <td  align="left" style="WIDTH: 17%">&nbsp;</td>
		        <td width="1046">
		        <table id="ctl00" cellspacing="5" border="0">
		          <tbody><tr>
		          <c:forTokens items="${ql.ED_CHOICE}" delims="\\|" var="tech">
					<td><input id="ctl00_0" type="checkbox" value="0" name="ctl00" />
		                <label for="ctl00_0">${tech}</label></td>
				  </c:forTokens>
		            </tr></tbody></table></td>
		       </tr>
			 </c:when>
			 <c:when test="${ql.ED_QUE_TYPE==QD_QUE_TYPE_3}"><!-- 问答 -->
				<tr class="">
		        <td  align="left" style="WIDTH:17%">&nbsp;</td>
		        <td width="80%">
		        <table id="ctl00" cellspacing="5" border="0">
		          <tbody><tr>
		          	<c:choose>
		          		<c:when test="${ql.ED_TXT_TYPE==qd_txt_type_1}">
		          			<td><input type="text" style="width:${ql.ED_WIDTH}px; " ></td>
		          		</c:when>
		          		<c:when test="${ql.ED_TXT_TYPE==qd_txt_type_2}">
		          			<td><textarea style="width:${ql.ED_WIDTH}px; height:${ql.ED_HIGHT}px;" ></textarea></td>
		          		</c:when>
		          	</c:choose>
		            </tr></tbody></table></td>
		       </tr>
			 </c:when>
			 <c:when test="${ql.ED_QUE_TYPE==QD_QUE_TYPE_4}"><!-- 统计问答 -->
				<tr class="">
		        <td  align="left" style="WIDTH:17%">&nbsp;</td>
		        <td width="80%">
		        <table id="ctl00" cellspacing="5" border="0">
		          <tbody><tr>
		          <c:choose>
		          		<c:when test="${ql.ED_TXT_TYPE==qd_txt_type_1}">
		          			<td><input type="text" style="width:${ql.ED_WIDTH}px; " /></td>
		          		</c:when>
		          		<c:when test="${ql.ED_TXT_TYPE==qd_txt_type_2}">
		          			<td><textarea style="width:${ql.ED_WIDTH}px; height:${ql.ED_HIGHT}px;" ></textarea></td>
		          		</c:when>
		          	</c:choose>
					
		            </tr></tbody></table></td>
		        </tr>
			 </c:when>
    	</c:choose>
   </c:forEach>
     
      <tr>
        <td colspan="2" align="center">
  		<input id="queryBtn2" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="button2" /></td>
      </tr>
  </table>
<br/>