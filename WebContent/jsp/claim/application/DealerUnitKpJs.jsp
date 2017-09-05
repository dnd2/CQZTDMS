<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tld/change.tld" prefix="change"%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="<%=request.getContextPath()%>/style/content.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/dict.js"></script>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<style>
@media print {
	INPUT {
		display: none
	}
}
</style>
		<style type="text/css">
#myTable {
	border-collapse: collapse;
}

#myTable td {
	border: 1px #000 solid;
}
</style>
		<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
//设置网页打印的页眉页脚为空    
function PageSetup_Null()   
{   
   try{    
       var Wsh=new ActiveXObject("WScript.Shell");    
       HKEY_Key="header";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
       HKEY_Key="footer";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
   }catch(e){}    
}
</script>
		<title></title>
	</head>
	<div id="kpr" align="center" class="Noprint">
		<input class="ipt" type=button name=button _print value="打印"
			onclick="javascript :printit();">
		<input class="ipt" type=button name=button _setup value="打印页面设置"
			onclick=" javascript : printsetup();">
		<input class="ipt" type=button name=button_show value="打印预览"
			onclick="javascript:printpreview();">
		<input class="ipt" type=button name=button _fh value="关闭"
			onclick=" javascript:window.close();">
	</div>
	<body>
		<div align="center" >
			<table width="900px"  class="tab_edit"  id="tab1"  >
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="center">
						<font size="4">结算费用汇总单</font>
					</td>
				</tr>
				
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="right">
						<font size="2">结算单号：${BRO_NO}</font>
					</td>
				</tr>

				<tr>
					<td align="left" colspan="2"  >
						<font size="2">北汽银翔汽车有限公司：</font>
					</td>
					<td align="right" colspan="2" >
						<font size="2"> 一式三联，两联随发票寄出&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
					</td>
				</tr>
				
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="left">
						<font size="2">我司的维修结算费用明细如下：</font>
					</td>
				</tr>
				
				<tr>
				   <td align="center" >
					<font size="2">	材料</font>
					</td>
					<td>
						<font size="2">${BALANCE_PART_AMOUNT }</font>
					</td>
					
					<td  align="center">
					<font size="2">	维修工时费</font>
					</td>
					<td>
						<font size="2">${BALANCE_L_AMOUNT }</font>
					</td>
					<td >
				</tr>
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="center">
						<font size="2">费用合计：${AMOUNT_SUM }</font>
					</td>
				</tr>
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="right">
						<font size="2"> 单位全称：重庆北汽幻速汽车销售有限公司</font>
					</td>
				</tr>
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="right">
						<font size="2"> 年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="left">
						备注：
					</td>
				</tr>
				
				   <tr>
				   <td >
					<font size="2">	北汽银翔签字确认： </font>
					</td>
					<td>
						<font size="2">北汽幻速签字确认： </font>
					</td>
					<td  colspan="2" style="border-right: 0px;" align="right">
						<font size="2"> 年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;</font>
					</td>
				</tr>
	   </table>		
</div>
		
<script type="text/javascript">

var date =document.getElementById('createD').value;
var d = date.substr(0,16);
document.getElementById('createDate').innerHTML=d;
</script>
	</body>
	<script language="javascript">    
  
function printsetup(){    
// 打印页面设置    
wb.execwb(8,1);    
}    
function printpreview(){    
// 打印页面预览      
wb.execwb(7,1);    
}      
function printit()    
{    
if (confirm('确定打印吗？')){    
  
wb.execwb(6,6)    
}    
}    
</script>
	<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0
		id=wb name=wb width=3></OBJECT>
</html>