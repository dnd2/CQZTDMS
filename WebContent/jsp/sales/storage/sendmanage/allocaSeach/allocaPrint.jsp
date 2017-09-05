<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
List<Map<String, Object>>  list =(List<Map<String, Object>>)request.getAttribute("valueList");
List<Map<String, Object>>  listRow =(List<Map<String, Object>>)request.getAttribute("listRow");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style media=print>
	.Noprint {
		display: none;
	}
}
</style>
<style type="text/css">
<!--
   table.tabsep2 {
		width: 820px;
		border-collapse:collapse;
	}
	
	table.tabsep2 td, table.tabsep2 th {
		border: 1px solid black;
		font-size: 14px;
	}
	
	table.tabsep {
		width: 820px;
	}
	
  td.tdsep {
		height: 26px;
		font-size: 14px;		
	}
-->
</style>
<title>运单打印</title>
</head>
<body>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" class="Noprint">   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
 <%
 if(list!=null){
	 int zzCount=0;
	 for(int i=0;i<list.size();i++){
		Map<String,Object> mp=(Map<String, Object>)list.get(i); 
		 %>
		 <%
		 if(i%15 ==0) {
			 if(i!=0) {
		 %>
		 </table>
		 <br/>
		 </div>
		 <%
			 }
		 if(i <= list.size() - 15){
		 %>
		 <div style="page-break-after: always;">
		 <%} %>
		 <center>
		  <strong><font size="4">潍柴（重庆）汽车有限公司-商品车发运及出库指示单</font></strong>
		</center>
		<br/>
		<table class="tabsep" align="center" border="0">
		  <tr>
			  <td align="left" class="tdsep" width="40%">打印日期：<script type="text/javascript">document.write((new Date()).Format("yyyy-MM-dd hh:mm:ss"))</script></td>
			  <td align="left" class="tdsep" width="60%">打印人：${userMap.name}</td>
		  </tr>
		</table>
		<table class="tabsep2" align="center">
		  <tr>
			<td class="tdsep" align="left" colspan="5" style="border-right: none; border-right-width: 0;"><strong>组板号：${valueMap.BO_NO}</strong></td>
			<td class="tdsep" class="right" colspan="5" style="border-left: none; border-left-width: 0;"><strong>配车总数：<b><%=list.size()%></b>&nbsp;&nbsp;&nbsp;&nbsp;中转车数：<b><%=zzCount %></strong></td>
		  </tr>
		<tr>
		<td class="tdp">序号</td>
		<!--<td class="tdp">流水号</td>-->
		<td class="tdp">发运申请号</td>
		<td class="tdp">是否中转</td>
		<!--<td class="tdp">发票号</td>-->
		<td class="tdp">VIN</td>
		<td class="tdp">车型</td>
		<td class="tdp">配置</td>
		<!--<td class="tdp">发动机号</td>-->
		<td class="tdp">颜色</td>
		<td class="tdp">库位码</td>
		<td class="tdp">台位</td>
		<td class="tdp">经销商</td>
		</tr>
		<%
		 }
		%>
		 <tr><td align="center"  class="tdp"><%=i+1 %></td>
<!--		    <td class="tdp"><%=mp.get("SD_NUMBER") %></td>-->
		    <td class="tdp"><%=mp.get("ORDER_NO")%></td>
			 <%
			 if(mp.get("ORDER_TYPE")!=null && mp.get("ORDER_TYPE").toString().equals("10201004")){
				%>
					<td class="tdp">是</td>
				<% 
			 }else{
				 %>
					<td class="tdp">否</td>
				<%  
			 }
			 %>
<!--		   	<td class="tdp"><%=CommonUtils.checkNull(mp.get("INVOICE_NO"))%></td>-->
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("VIN"))%></td>
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("MODEL_NAME"))%></td>
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("PACKAGE_NAME"))%></td>
		    <!-- <td class="tdp">${vlist.ENGINE_NO }</td>-->
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("COLOR_NAME"))%></td>
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("SIT_CODE"))%></td>
		    <td class="tdp"><%=CommonUtils.checkNull(mp.get("LOADS"))%></td>		    
			<td class="tdp" >
		    	<%=CommonUtils.checkNull(mp.get("DEALER_SHORTNAME"))%>
		    </td>   	   
		    </tr>
	 <%
		 }
 }
	
%>
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
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	}
</script> 
</body>
</html>