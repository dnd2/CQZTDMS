<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
    String logtype = (String)request.getAttribute("logtype");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>系统管理</title>
<script type="text/javascript"> 
    function subs ()
    { 
        if(submitForm('fm'))
		{  
    		fm.action="<%=request.getContextPath()%>/sysmng/sysLog/SysLogDownLoad/logDownLoad.do";
			fm.submit();
	    } 
    }
    
 </script>

</head>
<body onload="loadcalendar();autoAlertException();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：
系统管理 &gt; 系统日志管理 &gt; 日志查看</div>

<div id="mydivv" style="position: absolute; visibility: hidden;">

</div>
<form id="fm" name="fm" method="post">
<table class="table_query" border="0">
	<tr>
		<td align="right" nowrap="nowrap">查询时间：</td>
		<td nowrap="nowrap">
		<input id="date1" name="date1" class="short_txt" type="text" 
		        readonly datatype="0,is_date,<%=Constant.Length_Check_Char_10 %>"  group="date1,date2" />
		<input class="time_ico" type="button" onclick="showcalendar(event, 'date1', false);" value="&nbsp;" />
		  至
		 <input id="date2" name="date2" class="short_txt" readonly type="text" 
		 		datatype="0,is_date,<%=Constant.Length_Check_Char_10 %>" group="date1,date2"/>
		 <input class="time_ico" type="button" onclick="showcalendar(event, 'date2', false);" value="&nbsp;" /></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">类型：</td>
		<td nowrap="nowrap">
		<select class="short_sel" name="logtype" id="logtype">
			<option value="ACTION">业务日志</option>
			<option value="INTERFACE" <%if(logtype !=""&& logtype=="INTERFACE") { %> selected <%} %> >接口日志</option>
			<option value="ERRORAPP" <%if(logtype !=""&& logtype=="ERRORAPP") { %> selected <%} %>  >错误日志</option>
		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input id="queryBtn"
			name="queryBtn" type="button" class="normal_btn" onclick="subs()"
			value="下 载" /></td>
	</tr>
</table>
</form>
</div>
</body>
</html>