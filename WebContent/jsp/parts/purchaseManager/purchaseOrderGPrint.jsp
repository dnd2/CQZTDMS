<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title></title>
<style type="text/css">
html,body,td{font-size:14px;margin:0px;height:100%;}
.mesWindow{border:#666 1px solid;background:#fff;}
.mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
.mesWindowContent{margin:4px;font-size:12px;}
.mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
</style>
<style media=print>   
.Noprint{display:none;}  .PageNext{page-break-after: always;}   
</style>
</head>
<script language="javascript">
//获取选择框的值
function getCode(value){
	var str = getItemValue(value);

	document.write(str);
}

var idx = 0;
function getIndex(){
	idx+=1
	document.write(idx);
}

function printOrder(){
	$("printBtn").style.display="none";
	window.print();
	$("printBtn").style.display="";
}
function printWithAlert() {         
	 	document.all.WebBrowser.ExecWB(6,1);      
 }       
 function printWithoutAlert() {        
   document.all.WebBrowser.ExecWB(6,6);       
 }     
 function printSetup() {         
 	document.all.WebBrowser.ExecWB(8,1);       
 }      
 function printPrieview() {         
 	document.all.WebBrowser.ExecWB(7,1);       
 }       
function printImmediately() {         
	document.all.WebBrowser.ExecWB(6,6);        
	 window.close();       
}
var HKEY_Root,HKEY_Path,HKEY_Key; 
HKEY_Root="HKEY_CURRENT_USER"; 
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; 
//设置网页打印的页眉页脚为空 
function PageSetup_Null() { 
	try 
	{ 
	var Wsh=new ActiveXObject("WScript.Shell"); 
	HKEY_Key="header"; 
	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
	HKEY_Key="footer"; 
	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
	} 
	catch(e) 
	{} 
} 
//设置网页打印的页眉页脚为默认值 
function PageSetup_Default() { 
	try 
	{ 
	var Wsh=new ActiveXObject("WScript.Shell"); 
	HKEY_Key="footer"; 
	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&b&p/&P"); 
	//HKEY_Key="footer"; 
	//Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&u&b&d"); 
	} 
	catch(e) 
	{} 
} 
PageSetup_Default();
</script>
<OBJECT  id=WebBrowser  classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display:none">  
</OBJECT>
<body>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type=textarea name=datapackager style="display:none" value=''/>
    <c:forEach items="${allList}" var="subList" varStatus="curSta">
    <c:if test="${curSta.last}">
    <div name=thisblock id=thisblock>
    </c:if>
    <c:if test="${!curSta.last}">
    <div name=thisblock id=thisblock class="PageNext">
    </c:if>
	<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="91%" >	
	<br>
	<center>
	<font size="+2"><b>重庆君马新能源汽车有限公司配件采购订单通知书</b></font>
	</center>
	</TABLE><br/>
	<table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
		<tr>
			<td>通知单号码：&nbsp;${mainMap.ORDER_CODE}</td>
			<td>&nbsp;</td>
			<td>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期：&nbsp;${mainMap.CREATE_DATE}</td>
		</tr>
		<tr>
			<td>供&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：&nbsp;${venderMap.VENDER_NAME}</td>
			<td>&nbsp;</td>
			<td>需&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：&nbsp;${billMap.DEALER_NAME}</td>
		</tr>
		<tr>
			<td>联&nbsp;&nbsp;&nbsp;系&nbsp;&nbsp;&nbsp;人：&nbsp;${venderMap.LINKMAN}</td>
			<td>&nbsp;</td>
			<td>联系地址：&nbsp;${billMap.ADDR}</td>
		</tr>
		<tr>
			<td>联&nbsp;系&nbsp;电&nbsp;话：&nbsp;${venderMap.TEL}</td>
			<td>&nbsp;</td>
			<td>联系电话：&nbsp;${billMap.TEL} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系人：&nbsp;</td>
		</tr>
		<tr>
			<td>传&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;真：&nbsp;${venderMap.FAX}</td>
			<td>&nbsp;</td>
			<td>开&nbsp;&nbsp;户&nbsp;行：&nbsp;${billMap.BANK}</td>
		</tr>
		<tr>
			<td></td>
			<td>&nbsp;</td>
			<td>税&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：&nbsp;${billMap.TAX_NO}</td>
		</tr>
		<tr>
			<td></td>
			<td>&nbsp;</td>
			<td>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：&nbsp;${billMap.ACCOUNT}</td>
		</tr>
		
	</table>
	
	<br>
	<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
	    <tr align="center"> 
	        <td nowrap="nowrap" colspan=1 width="1%">编号</td>
	        <td nowrap="nowrap" colspan=1 width="7%">代码</td>
	        <td nowrap="nowrap" colspan=1 width="10%">名称</td>
	        <td nowrap="nowrap" colspan=1 width="5%">件号</td>
	        <td nowrap="nowrap" colspan=1 width="1%">单位</td>
	        <td nowrap="nowrap" colspan=1 width="3%">数量 </td>
	        <td nowrap="nowrap" colspan=1 width="5%">要求到货期 </td>
	        <td nowrap="nowrap" colspan=1 width="5%">备注</td>
	    </tr>
	    <c:forEach items="${subList}" var="data" varStatus="curSta">
	    <tr>
		    	<td nowrap="nowrap">
		    		<script language="javascript">getIndex()</script>
		    	</td> 
		        <td nowrap="nowrap">&nbsp;${data.PART_OLDCODE}</td> 
		        <td >
		         	<div style="width:190px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                            &nbsp;${data.PART_CNAME}
                    </div>
		        </td> 
		         <td>
		         	<div style="width:120px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                            &nbsp;${data.PART_CODE}
                    </div>
		        </td> 
		        <td nowrap="nowrap">&nbsp;${data.UNIT}</td> 
		        <td >&nbsp;${data.BUY_QTY}</td> 
		        <td nowrap="nowrap">&nbsp;${data.FORECAST_DATE}</td> 
		        <td >&nbsp;${data.REMARK}</td> 
	        </tr>
	    </c:forEach>	     
	</TABLE>
	</div>
    </c:forEach>
	<table border=0px align="center" width="91%">
	 <tr >
	    <td align="left" colspan="4">要求：</td>
	    </tr>
	    <tr>
	    	<td align="left" colspan="4">一、送货时必须有携带本传真件或送货单（送货单上需注明我公司配件编号），铁路发运需注明发货箱数、每箱品数及数量；</td>
	    </tr>
	    <tr>
	   	 	<td align="left" colspan="4">二、产品必须有固定的包装，必须标明产品生产厂家及其地址；必须开具合格证；</td>
	    </tr>
	    <tr>
	    	<td align="left" colspan="4">三、收货地址：>重庆君马新能源汽车有限公司零配件总库；</td>
	    </tr>
	    <tr>
	    	<td align="left" colspan="4">四、保管员收到货后与供方办理签收手续（铁路运输请与运输处办理签收手续），并通知计划员办理进货单。</td>
	    </tr>
	</table><br>
	<TABLE border=0px align="center" width="91%">
		<tr >
			 <td align="left">编制：&nbsp;</td>
			 <td align="left">审核：&nbsp;</td>
			 <td align="left">批准：&nbsp;</td>
			 <td align="left">库房签收：&nbsp;</td>
		</tr>
	</TABLE>
	<table align="center" class="Noprint">  
  <tr>  
    <td align="center">
      <input type=button value="打印" onClick="printWithAlert()" >
      <input type=button value="打印设置" onClick="printSetup()">
      <input type=button value="打印预览" onClick="printPrieview()">
    </td>  
  </tr>
  </table>
</form>
</body>
<script type="text/javascript">
	var divArr = document.getElementsByName("thisblock");
	divArr[(divArr.length-1)].className="";
	
</script>
</html>