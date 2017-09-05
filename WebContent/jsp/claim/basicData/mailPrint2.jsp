<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
	 String dept=request.getParameter("dept");
	    if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(dept)){
	    	dept = new String(dept.getBytes("ISO-8859-1"),"UTF-8");
	    }
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">



function saveTransIdAndPrint(){
	 window.print();
}
function saveTransId(){

       saveTransIdAndPrint();
} 
document.body.onkeydown=function(event){
	var eve = document.all?window.event:event;
	if(eve.keyCode==13){
		var elm=eve.srcElement || eve.target;
		return;
	}
}
</script>
</head>

<body style="background-repeat: no-repeat;background-attachment: scroll;margin: auto;scrolling:auto" >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">

		<div style="position:absolute;top:455px;left:350px">
			<input style="width:50px" id="printBtn" type="button" value="打 印" onclick="saveTransId();"/>
		</div>
		<div style="position:absolute;top:455px;left:410px">
			<input style="width:50px" id="printBtn2" type="button" value="关 闭" onclick="window.close()"/>
		</div>
	<div id ="printDiv" >
		<div style="position:absolute;top:68pt;left:110pt"><font size="2" >${user.name }</font></div>
		<div style="position:absolute;top:90pt;left:110pt"><font size="2" >景德镇昌河汽车股份有限公司</font></div>
		<div style="position:absolute;top:105pt;left:110pt"><font size="2" >江西省景德镇市106信箱[<%=dept %>]</font></div>
        <div style="position:absolute;top:125pt;left:270pt"><font size="2" >3&nbsp;3&nbsp;3&nbsp;0&nbsp;0&nbsp;2&nbsp;</font></div>
        <div style="position:absolute;top:68pt;left:260pt"><font size="2" > 0798-8462131</font></div>

		<div style="position:absolute;top:155pt;left:110pt"><font size="2" >${bean.webmasterName }</font></div>
        <div style="position:absolute;top:155pt;left:260pt"><font size="2" >${bean.phone }</font></div>
        <div style="position:absolute;top:175pt;left:110pt"><font size="2" >${bean.dealerName }</font></div>
		<div style="position:absolute;top:190pt;left:110pt;white-space:normal; width:270px;"><font size="2" >${bean.address }</font></div>
        <div style="position:absolute;top:215pt;left:270pt;white-space:normal; width:100px;"><font size="2" >${a }&nbsp;${b }&nbsp;${c }&nbsp;${g }&nbsp;${e }&nbsp;${f }&nbsp;</font></div>

	</div>
</form>
</body>
</html>
