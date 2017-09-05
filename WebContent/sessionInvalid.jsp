<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style>
	.sttt {
	color: #FD6536;
	font-size: 14px;
	text-align: center;
}
</style>
<!-- added by andy.ten@tom.com -->
<script language="JavaScript">
function logon()
{
	top.window.location.href = "<%=request.getContextPath()%>/index.jsp";
}
</script>
</head>

<body>
   <div align="center" valign="center">
	<span class="sttt"><font size="5pt">当前用户登录已超时，请重新<a href='javascript:logon();' style='cursor: hand;'>登录</a>系统！</font></span>
   </div>	
</body>
</html>