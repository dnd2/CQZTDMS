<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>地址打印</title>
</head>
<style type="text/css">
	*{font-size: 13px;}
	body{padding: 0; margin: 0px;}
</style>
<body onload="window.print();">
	<table border="0" cellpadding="0" cellspacing="0" style="margin-left: 48px; float:left; table-layout: fixed;">
		<tr height="73">
			<td width="44">&nbsp;</td>
			<td width="60">&nbsp;</td>
			<td width="15">&nbsp;</td>
			<td width="80">&nbsp;</td>
		</tr>
		<tr height="30">
			<td colspan="2">重庆幻速物流有限公司</td>
			<td width="15">&nbsp;</td>
			<td>财务部</td>
		</tr>
		<tr height="60">
			<td colspan="4" valign="top" style="padding: 10px 0;">重庆市合川区土场镇三口村</td>
		</tr>
		<tr height="20">
			<td>&nbsp;</td>
			<td colspan="2"><font style="">02342660259</font></td>
			<td>&nbsp;</td>
		</tr>
		<tr height="35">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr height="40">
			<td colspan="2" width="104" style="table-layout: fixed; word-break: break-all; word-wrap:break-word;">${map['COMPANY_NAME'] == null ? '&nbsp;' : map['COMPANY_NAME'] }</td>
			<td width="15"></td>
			<td>${map['LINK_MAN'] == null ? '&nbsp;' : map['LINK_MAN']}</td>
		<tr/>
		<tr height="50">
			<td colspan="4" style="width: 199px; table-layout: fixed; word-break: break-all; word-wrap:break-word;" valign="middle">${map['ADDRESS'] == null ? '&nbsp;' : map['ADDRESS']}</td>
		<tr/>
		<tr height="30">
			<td></td>
			<td colspan="3"><font style="letter-spacing: 5px;">${map['TEL'] == null ? '&nbsp;' : map['TEL'] }</font></td>
		</tr>
	</table>
</body>
</html>