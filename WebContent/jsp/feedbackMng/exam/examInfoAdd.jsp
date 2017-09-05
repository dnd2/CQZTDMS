<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;考试信息管理&gt;考试信息维护</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_edit">
		<tr>
			<td width="10%" height="25" align="right">考试主题：</td>
			<td>
				<textarea rows="1" datatype="0,is_null,200" cols="50" name="examName"></textarea>
			</td>
		</tr>
		<tr>
			<td width="10%" height="25" align="right">开始时间：</td>
			<td>
				<input type="text" class="middle_txt" name="beginDate" id="beginDate" datatype="0,is_datatime,16" 
					group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', true);"/>
			</td>
		</tr>
		<tr>
            <td width="10%" height="25" align="right">结束时间：</td>
            <td>
				<input type="text" class="middle_txt" name="endDate" id="endDate" datatype="0,is_datatime,16" 
					group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', true);"/>
			</td>
		</tr>
		<tr>	
			<td width="10%" align="right">考试内容：</td>
			<td>
				<textarea rows="1" cols="50" name="examRemark"></textarea>
			</td>
		</tr>
	</table>
	<br />
	
	<table class="table_info" border="0" id="file">
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					&nbsp;&nbsp;&nbsp;&nbsp;
				<span align="left"><input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
	</table>
	<br />
	
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" class="normal_btn" value="保存" onclick="save();"/>
				&nbsp;&nbsp;
				<input type="button" class="normal_btn" value="返回" onclick="goBack();"/>
			</td>
		</tr>
	</table>
	
</form>
<script type="text/javascript">
loadcalendar();
function save(){
	if(!$('examName').value) MyAlert('考试主题为必填项');
	else if(!$('beginDate').value) MyAlert('考试开始时间为必填项');
	else if(!$('endDate').value) MyAlert('考试开始时间为必填项');
	else{
		$('fm').action = '<%=contextPath%>/feedbackmng/exam/ExamOemAction/addExamInfo.do' ;
		$('fm').submit();
	}
}
function goBack(){
	location = '<%=contextPath%>/feedbackmng/exam/ExamOemAction/firstUrlInit.do' ;
}
</script>
</BODY>
</html>