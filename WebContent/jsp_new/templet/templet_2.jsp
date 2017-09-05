<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
	//附件
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>?</title>
<script type="text/javascript">
	
</script>
</head>
<body>
<div class="navigation">
<img src="${contextPath}/img/nav.gif" />&nbsp;当前位置：?&gt;?&gt;?
</div>
<form name="fm" id="fm" method="post">
<table border="1" cellpadding="1" cellspacing="1" class="tab_edit" width="100%" style="text-align: center;">

</table>
<br/>
<table width=100%  border="0" cellspacing="0" cellpadding="0"  style="text-align: center;">
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >?：</td>
    	<td nowrap="true" width="15%" >
    		
    	</td>
		<td nowrap="true" width="10%" >?</td>
    	<td nowrap="true" width="15%" >
    	
    	</td>
		<td nowrap="true" width="10%" >?</td>
    	<td nowrap="true" width="15%" >
    	
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br/>
</form>
</body>
</html>