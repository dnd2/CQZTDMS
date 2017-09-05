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

		<div style="font-size: 20px;" align="center">
			重庆北汽幻速汽车销售公司    入库单
		</div>
		<table width="900px" align="center" >
		<tr height="40px;">
				<td colspan="9">
				<div style="font-size: 15px;" align="left">
					购货方名称: 重庆北汽幻速汽车销售有限公司
					
				</td>
					
				</tr>
				<tr height="40px;">
					
					<td colspan="2">
					<div style="font-size: 15px;" align="left">
						服务站代码: ${dealerPO.dealerCode }
						</div>
					</td>
					<td colspan="2">
					<div style="font-size: 15px;" align="center">
						签收号: ${map.RETURN_NO }
						</div>
					</td>
					<td colspan="3" align="right">
					<div style="font-size: 15px;" align="right">
						入库单号: ${BALANCE_ODER }
						</div>
					</td>
				</tr>
				<tr height="40px;">
					
					<td colspan="2">
					<div style="font-size: 15px;" align="left">
						服务站名称: ${dealerPO.dealerShortname }
						</div>
					</td>
					<td colspan="2">
					<div style="font-size: 15px;" align="center">
						签收日期:  ${map.SIGN_DATE }
						</div>
					</td>
					<td colspan="3" align="right">
					<div style="font-size: 15px;" align="right">
						需求源: 索赔件
						</div>
					</td>
					
				<tr height="40px;">
					
					<td colspan="2">
					<div style="font-size: 15px;" align="left">
						入库时间:  ${map.IN_WARHOUSE_DATE }
						</div>
					</td>
					<td colspan="2">
					<div style="font-size: 15px;" align="center">
						收货人:  ${map.NAME }
						</div>
					</td>
					<td colspan="3" align="right">
					<div style="font-size: 15px;" align="right">
						收货单位: 重庆北汽幻速汽车销售有限公司
						</div>
					</td>
					
				</tr>
				</tr>
		</table>		
		<div align="center">
			<table width="900px" class="tab_edit" id="tab1">
				
				

				<tr>
					<td align="center">
						<font size="2">序号</font>
					</td>
					<td align="center">
						<font size="2">物品名称</font>
					</td>
					<td  align="center" >
						<font size="2">规格型号</font>
					</td>
					<td align="center">
						<font size="2">单位</font>
					</td>
					<td align="center">
						<font size="2">库数</font>
					</td>
					<td align="center">
						<font size="2">仓库</font>
					</td>
					<td align="center">
						<font size="2">索赔单</font>
					</td>
				</tr>
				<% int i = 1; %>
				<c:forEach items="${list}" var="list" varStatus="status">
				<tr>
					<td align="center">
						<font size="2"><%= i %></font>
					</td>
					<td>
						<font size="2">${list.PART_NAME}</font>
					</td>
					<td >
						<font size="2">${list.PART_CODE}</font>
					</td>
					<td align="center">
						<font size="2">件</font>
					</td>
					<td align="center">
						<font size="2">${list.BALANCE_QUANTITY}</font>
					</td>
					<td align="center">
						<font size="2">索赔件库</font>
					</td>
					<td align="center">
						<font size="2">${list.CLAIM_NO}</font>
					</td>
					
				</tr>
				<% i = i+1; %>
				</c:forEach>
				
	   </table>			
		</div>
		<br/>
		<table width="900px" align="center" >
		<tr height="40px;">
				<td colspan="9">
		<div  style="font-size: 15px;" align="right">
			 打印日期：${date}
		</div>
		</td>
		</tr>
		</table>
		
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