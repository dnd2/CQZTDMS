<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	String dlr = (String)request.getAttribute("dlr");
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>考试管理</TITLE>

</HEAD>
<BODY>
<div class="wbox">
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;考试信息管理&gt;考试信息维护</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_edit">
		<tr>
			<td width="10%" height="25" align="right">考试代码：</td>
			<td>${exam.examCode}</td>
			<td width="10%" height="25" align="right">考试主题：</td>
			<td>${exam.examName}</td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">开始时间：</td>
			<td><fmt:formatDate value="${exam.examStartTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td width="10%" height="25" align="right">结束时间：</td>
            <td><fmt:formatDate value="${exam.examEndTime}" pattern="yyyy-MM-dd HH:mm"/></td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">考试描述：</td>
			<td colspan="3">${exam.examRemark}</td>
		</tr>
	</table>
	<br />
	
	<table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
			<script type="text/javascript">
    			addUploadRowByDL('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
			</script>
    	<%} %>
	</table>
	<br />
	
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" class="normal_btn" value="返回" onclick="goBack();"/>
			</td>
		</tr>
	</table>
	
</form>
</div>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/feedbackmng/exam/ExamOemAction/firstUrlInit.do' ;
}
</script>
</BODY>
</html>