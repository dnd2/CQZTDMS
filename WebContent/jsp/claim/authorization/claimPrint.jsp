<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>收单报表</title>
</head>

<body>
<br/>
<center><strong><font size="6">收单签收表</font></strong></center>
<br><br><br>
<div align="center">
<table width="700px" border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr>
		<td colspan="4" class="tdp" align="left">收单起：${bDate}</td>
		<td colspan="5" class="tdpright" align="left">收单止：${eDate}</td>
	</tr>
	<tr>
		<td height="2px" colspan="9">&nbsp;</td>
	</tr>
	<c:if test="${list!=null}">
		<tr>
			<td class="tdp" nowrap>序号</td>
			<td class="tdp" nowrap>维修站代码</td>
			<td class="tdp" width="200px">维修站名称</td>
			<td class="tdp" nowrap>收单人</td>
			<td class="tdp" nowrap>收单时间</td>
			<td class="tdp" nowrap>维修时间起</td>
			<td class="tdp" nowrap>维修时间止</td>
			<td class="tdp" nowrap>省份</td>
			<td class="tdpright" nowrap>签收确认</td>
		</tr>
	</c:if>
	<c:forEach items="${list}" var="map" varStatus="st">
		<tr>
			<td class="tdp">${st.index+1}</td>
			<td class="tdp">${map.DEALER_CODE}</td>
			<td class="tdp" width="200px">${map.DEALER_NAME}</td>
			<td class="tdp">${map.NAME}</td>
			<td class="tdp">${map.SIGN_DATE}</td>
			<td class="tdp">${map.B_DATE}</td>
			<td class="tdp">${map.E_DATE}</td>
			<td class="tdp">${map.REGION_NAME}</td>
			<td class="tdpright"></td>
		</tr>
	</c:forEach>
</table>


<br><br><br>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr">
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();">    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();">    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">
			</div>
		</td>
	</tr>
</table>
</div>
<script language="javascript">

	function printsetup()
	{
		wb.execwb(8,1);// 打印页面设置
	}
	function printpreview()
	{
		wb.execwb(7,1);// 打印页面预览
	}
	function printit()
	{
		if(confirm('确定打印吗？'))
		{
			wb.execwb(6,6)
		}
	}
	
</script> 
</body>
</html>
