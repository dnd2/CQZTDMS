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
			北汽幻速索赔结算开票商家信息表
		</div>
		<div align="center" >
			<table width="900px"  class="tab_edit"  id="tab1"  >
				<tr>
					<td align="center" width="5%">
						<font size="2">序号</font>
					</td>
					<td align="center" >
						<font size="2">收票人</font>
					</td>
					<td  align="center"  >
						<font size="2">收票日期</font>
					</td>
					<td align="center" >
						<font size="2">服务站代码</font>
					</td>
					<td align="center" >
						<font size="2">服务站简称</font>
					</td>
					<td align="center" >
						<font size="2">发票号码</font>
					</td>
					<td align="center" >
						<font size="2">税额</font>
					</td>
					<td align="center" >
						<font size="2">开票总金额</font>
					</td>
					<td align="center"  >
						<font size="2">税率</font>
					</td>
					<td align="center"  >
						<font size="2">实际结算金额</font>
					</td>
					
					<td align="center"  >
						<font size="2">索赔月份</font>
					</td>
					<td align="center"  >
						<font size="2">备注</font>
					</td>
				</tr>
				<% int i = 1; %>
				<c:forEach items="${ps}" var="ps" varStatus="status">
				<tr>
					<td align="center">
					<font size="2">	<%= i %></font>
					</td>
					<td>
						<font size="2">${ps.USER_NAME}</font>
					</td>
					<td >
					<font size="2">	${ps.COLLECT_TICKETS_DATE}</font>
					</td>
					<td align="center">
					<font size="2">${ps.DEALER_CODE}件</font>
					</td>
					<td align="center">
						<font size="2">${ps.DEALER_SHORTNAME}</font>
					</td>
					<td align="center">
						<font size="2">${ps.LABOUR_RECEIPT}</font>
					</td>
					<td align="center">
						<font size="2">${ps.TAX_RATE_MONEY}</font>
					</td>
					<td align="center">
						<font size="2">${ps.AMOUNT_OF_MONEY }</font>
					</td>
					<td align="center">
					<font size="2">${ps.TAX_DISRATE}</font>
					</td>
					<td align="center">
					<font size="2">${ps.AMOUNT_SUM}</font>
					</td>
					<td align="center">
					<font size="2">${ps.END_DATE}</font>
					</td>
					<td align="center">
					<font size="2">${ps.REMARK}</font>
					</td>
				</tr>
				<% i = i+1; %>
				</c:forEach>
				
	   </table>	
		
	   <br/>
	   <table width="900px" align="center" >
		<tr>
			<td colspan="3" style="border-right: 0px;" align="left">
				<font size="2">编制：</font>
			</td>
			<td colspan="3" style="border-left: 0px;" align="right">
			<font size="2">	审核：</font>
			</td>
			<td colspan="3" style="border-left: 0px;" align="right">
			<font size="2">	审批：</font>
			</td>
			<td colspan="3" style="border-left: 0px;" align="right">
			<font size="2">	财务：</font>
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