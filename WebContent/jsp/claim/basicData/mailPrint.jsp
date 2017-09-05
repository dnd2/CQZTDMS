<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style media=print>
/* 应用这个样式的在打印时隐藏 */
.Noprint {
	display: none;
}

/* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
hr {
	page-break-after: always;
}
</style>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请单打印</title>
<% 
    String contextPath = request.getContextPath();
    String dept=request.getParameter("dept");
    String remark=request.getParameter("remark");    
    if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(dept)){
    	dept = new String(dept.getBytes("ISO-8859-1"),"UTF-8");
    	remark = new String(remark.getBytes("ISO-8859-1"),"UTF-8");
    }
    
%>
 
</head>
<body  >
	<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
	<form name="fm" id="fm">
<center>
		<table  style="height: 400px;border-collapse: collapse;"  width="750px">
			<tr style="height: 1%;">
			<td width="6%">&nbsp;</td>
				<td align="center" width="4%" style="font-size: 15px">${a}</td>
				<td align="center" width="4%" style="font-size: 15px">${b }</td>
				<td align="center" width="4%" style="font-size: 15px">${c }</td>
				<td align="center" width="4%" style="font-size: 15px">${g }</td>
				<td align="center" width="4%" style="font-size: 15px">${e }</td>
				<td align="center" width="4%" style="font-size: 15px">${f }</td>
				<td colspan="11" width="70%">&nbsp;</td>
			</tr>
			<tr>
			<td colspan="3">&nbsp;</td>
				<td colspan="15" align="left" style="font-size: 25px;" >${bean.address }
				<br />
				联系电话：${bean.phone }
				</td>
			</tr>
		
			<tr >
				<td colspan="18" align="center" style="font-size: 25px;">${bean.webmasterName }(收)</td>
			</tr>	
			<tr>
			<td colspan="3">&nbsp;</td>
				<td colspan="15" align="left" style="font-size: 20px;">回函地址：江西省景德镇市106信箱[<%=dept%>]<br />
				备注：<%=remark%>
				</td>
			</tr>
		</table>
	<table width="100%" cellpadding="1"  class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><div id="kpr"
					align="center">
					<input class="ipt" type="button" value="打印"
						onclick="javascript:printit();" /> <input class="ipt"
						type="button" value="打印页面设置" onclick="javascript:printsetup();" />
					<input class="ipt" type="button" value="打印预览"
						onclick="javascript:printpreview();" />
			</td>
		</tr>
	</table>
	</center>
	</form>
	<script language="javascript">
		function printsetup() {
			wb.execwb(8, 1); // 打印页面设置 
		}
		function printpreview() {
			wb.execwb(7, 1); // 打印页面预览       
		}
		function printit() {
			if (confirm('确定打印吗？')) {
				wb.execwb(6, 6)
			}
		}
	</script>
</body>
</html>

