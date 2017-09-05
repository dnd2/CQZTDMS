<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String fileId = (String)request.getAttribute("fileId");
	String fileUrl = (String)request.getAttribute("fileUrl");
%>
</head>
<body onunload='javascript:destoryPrototype()' onload="setValue()">
	上传已成功
</body>
<script type="text/javascript">
	function setValue()
	{
		var bid6 = parent.document.getElementById('inIframe').contentWindow.document.getElementById('bid6');
		var file6 = parent.document.getElementById('inIframe').contentWindow.document.getElementById('file6');
		bid6.value = '<%=fileId%>';
		var suffix = '<%=fileId.toLowerCase()%>'.split('.')[1];
		var fileName = suffix + '.png';
		file6.src='../../../img/' + fileName;
	}
</script>
</html>