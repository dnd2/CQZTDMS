<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库位码打印</title>
</head>
<body onload="printit();" >
<center>
<div style="border: 1px #4b4b4b solid; width:350px;" >
	<table border="0" width="100%">
	 <tr>
	<td align="left"><strong><font size="2">${valueMap.AREA_NAME }-${valueMap.ROAD_NAME }-${valueMap.SIT_NAME } * ${valueMap.PER_CODE }</font></strong></td>
	</tr>
	<tr>
	<td align="left"><strong><font size="2">-${valueMap.VIN }</font></strong></td>
	</tr>
	</table>
</div>
</center>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
			<!-- 
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
				<input class="ipt" type="button" value="关闭" onclick="closeWin()"/>
			-->   
			</div>
		</td>
	</tr>     
</table> 
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}  
	    
	function printit()    
	{    
		try
		{
			wb.execwb(6,6);
		}catch(e){}

		window.opener = null;
		window.close();
	}

	function closeWin() 
	{
		try{_hide();}catch(e){}
		try{window.close();}catch(e){}
	}
</script> 
</body>
</html>