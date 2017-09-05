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
		<div style="font-size: 20px; " align="center"  >
			销货清单
		</div>
		<table width="900px" align="center" >
		<tr>
		<td>
		<div style="font-size: 15px; "  >
			购货方名称: 北汽银翔汽车有限公司
		</div>
		</td>
		</tr>
		<tr>
		<td>
		<div style="font-size: 15px;" align="left">
			销售方名称:  重庆北汽幻速汽车销售有限公司
		</div>
		</td>
		</tr>
		</table>
		<div align="center" >
			<table width="900px"  class="tab_edit"  id="tab1"  >
				<tr>
					<td  colspan="3" style="border-right: 0px;" align="left">
						<font size="2">所属增值税专用发票代码 ：${INVOICE_NO }</font>
					</td>
					<td colspan="6" style="border-left: 0px;" align="right">
						<font size="2">号码 ：${INVOICE_BATCH_NO }</font>
					    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<font size="2">共 1 页  第 1页</font>
					</td>
				</tr>

				<tr>
					<td align="center" width="5%">
						<font size="2">序号</font>
					</td>
					<td align="center" >
						<font size="2">货物（劳务）名称</font>
					</td>
					<td  align="center"  >
						<font size="2">规格型号</font>
					</td>
					<td align="center" >
						<font size="2">单位</font>
					</td>
					<td align="center" >
						<font size="2">数量</font>
					</td>
					<td align="center" >
						<font size="2">单价</font>
					</td>
					<td align="center" >
						<font size="2">金额</font>
					</td>
					<td align="center" >
						<font size="2">税率</font>
					</td>
					<td align="center"  >
						<font size="2">税额</font>
					</td>
				</tr>
				<% int i = 1; %>
				<c:forEach items="${list}" var="list" varStatus="status">
				<tr>
					<td align="center">
					<font size="2">	<%= i %></font>
					</td>
					<td>
						<font size="2">${list.PART_NAME}</font>
					</td>
					<td >
					<font size="2">	${list.PART_CODE}</font>
					</td>
					<td align="center">
					<font size="2">	件</font>
					</td>
					<td align="center">
						<font size="2">${list.BALANCE_QUANTITY}</font>
					</td>
					<td align="center">
						<font size="2">${list.PRICE}</font>
					</td>
					<td align="center">
						<font size="2">${list.BALANCE_AMOUNT}</font>
					</td>
					<td align="center">
						<font size="2">${bf }</font>
					</td>
					<td align="center">
					<font size="2">${list.S_BALANCE_AMOUNT}</font>
					</td>
				</tr>
				<% i = i+1; %>
				</c:forEach>
				
				
				
				<tr>
					<td align="center">
						小计
					</td>
					<td>
						
					</td>
					<td >
						
					</td>
					<td align="center">
						
					</td>
					<td align="center">
						
					</td>
					<td align="center">
					
					</td>
					<td align="center">
						${map.BALANCE_AMOUNT}
					</td>
					<td align="center">
						
					</td>
					<td align="center">
						${map.S_BALANCE_AMOUNT}
					</td>
				</tr>
				
				<tr>
					<td align="center">
					<font size="2">	<%= i %></font>
					</td>
					<td>
						<font size="2">配件价差</font>
					</td>
					<td >
					<font size="2"></font>
					</td>
					<td align="center">
					<font size="2">	</font>
					</td>
					<td align="center">
						<font size="2"></font>
					</td>
					<td align="center">
						<font size="2"></font>
					</td>
					<td align="center">
						<font size="2">${jcfy}</font>
					</td>
					<td align="center">
						<font size="2"></font>
					</td>
					<td align="center">
						<font size="2">${jcsl}</font>
					</td>
				</tr>
				
				<tr>
					<td align="center">
						总计
					</td>
					<td>
						
					</td>
					<td >
						
					</td>
					<td align="center">
						
					</td>
					<td align="center">
						
					</td>
					<td align="center">
					
					</td>
					<td align="center">
						<fmt:formatNumber value="${map.BALANCE_AMOUNT+jcfy}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>   
					</td>
					<td align="center">
						
					</td>
					<td align="center">
						<fmt:formatNumber value="${map.S_BALANCE_AMOUNT+jcsl}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>   
					</td>
				</tr>
				
				<tr>
					<td colspan="1" style="border-right: 0px;" align="center">
						备注
					</td>
					<td colspan="8" style="border-left: 0px;" align="left">
						&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
	   </table>	
	   
	   
	   
				
	   <br/>
	   <table width="900px" align="center" >
		<tr>
			<td colspan="2" style="border-right: 0px;" align="left">
				<font size="2">销售方（发票专用章）：</font>
			</td>
			<td colspan="7" style="border-left: 0px;" align="right">
			<font size="2">	打印日期：${date}</font>
			</td>
		</tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr>
		<td colspan="9" style="border-right: 0px;" align="left">
			<font size="2">注：本清单一式三联：第一联，销货单位留存； 第二联，三联购货单位留存</font>
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