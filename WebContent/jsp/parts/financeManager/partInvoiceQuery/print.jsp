<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<title></title>
<style type="text/css">

</style>
</head>
<body onload="window.print();">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
	<table border="0px" width=100% height=100% >
			<%--<div style="position:relative;top:55pt;left:15pt"><font size="3" >售后配件处</font></div>
	        <div style="position:relative;top:55pt;left:240pt"><font size="3" ><%=request.getParameter("year")%>年<%=request.getParameter("month")%>月<%=request.getParameter("day")%>日<%=request.getParameter("hour")%>时</font></div>
	        <div style="position:relative;top:105pt;left:15pt"><font size="3" >财务负责人</font></div>
	        <div style="position:relative;top:105pt;left:260pt"><font size="3" >0798-8462131</font></div>
	        <div style="position:relative;top:125pt;left:15pt"><font size="3" >北汽幻速汽车销售有限公司销售公司售后配件处</font></div>
	        <div style="position:relative;top:140pt;left:15pt"><font size="3" >江西景德镇108信箱</font></div>
	        <div style="position:relative;top:170pt;left:280pt"><font size="3" >3&nbsp;3&nbsp;3&nbsp;0&nbsp;0&nbsp;2&nbsp;</font></div>
	        <div style="position:relative;top:105pt;left:390pt"><font size="3" >财务负责人</font></div>
	        <div style="position:relative;top:105pt;left:540pt"><font size="3" ><%=request.getParameter("TEL")%></font></div>
	        <div style="position:relative;top:125pt;left:390pt"><font size="3" ><%=request.getParameter("DEALER_NAME")%></font></div>
	        <div style="position:relative;top:140pt;left:390pt;white-space:normal; width:280px;"><font size="3" ><%=request.getParameter("ADDR")%></font></div>
	        <div style="position:relative;top:170pt;left:600pt"><font size="3" ><%=request.getParameter("POST_CODE")%></font></div>
	        <div style="position:relative;top:200pt;left:580pt"><font size="3" ><%=request.getParameter("AMOUNT")%></font></div>--%>
            <div style="position:absolute;top:60pt;left:15pt"><font size="3" ><%--${dataMap.userName}--%>售后配件处</font></div>
            <div style="position:absolute;top:85pt;left:15pt"><font size="3" ><%--${dataMap.sellerName}--%>北汽幻速汽车销售有限公司销售公司售后配件处</font></div>
            <div style="position:absolute;top:100pt;left:15pt"><font size="3" ><%--${dataMap.addr}--%>江西景德镇108信箱</font></div>
            <div style="position:absolute;top:135pt;left:195pt"><font size="3" ><%--${dataMap.addr}--%>3&nbsp;&nbsp;3&nbsp;&nbsp;3&nbsp;&nbsp;0&nbsp;&nbsp;0&nbsp;&nbsp;2&nbsp;&nbsp;</font></div>
            <div style="position:absolute;top:60pt;left:140pt"><font size="3" ><%--${dataMap.userName}--%>0798-8462009</font></div>
            <div style="position:absolute;top:165pt;left:15pt"><font size="3" ><%--${dataMap.receiver}--%>财务负责人</font></div>
            <div style="position:absolute;top:165pt;left:140pt"><font size="3" ><%=request.getParameter("TEL")%></font></div>
            <div style="position:absolute;top:190pt;left:15pt"><font size="3" ><%=request.getParameter("DEALER_NAME")%></font></div>
           <%-- <div style="position:absolute;top:150pt;left:540pt"><font size="3" >${dataMap.year}年${dataMap.month}月${dataMap.day}日</font></div>--%>
            <div style="position:absolute;top:210pt;left:15pt;white-space:normal; width:340px;"><font size="3" ><%=request.getParameter("ADDR")%></font></div>
            <div style="position:absolute;top:240pt;left:500pt;white-space:normal; width:100px;"><font size="3" >&nbsp;</font></div>
            <div style="position:absolute;top:320pt;left:15pt"><font size="3" >&nbsp;发票</font></div>
	</table>
</form>
</body>
</html>