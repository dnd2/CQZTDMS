<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 24px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 14px;
	color: #000000;
}

hr {
	page-break-after: always;
}

</style>

<style>
<!--
td.boldTd {
	font-size: 14px;
	font-family: '宋体';
	font-weight: bold;
	height: 20px;
	text-align: left;
}

td.normalTd {
	font-size: 14px;
	font-family: '宋体';
	height: 20px;
}

table.sepTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.sepTab td,table.sepTab th {
	border: 1px solid black;
	font-size: 14px;
	font-family: '宋体';
	height: 20px;
}

table.midTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.midTab td {
	border: 1px solid black;
	font-family: '宋体';
	height: 20px;
	font-weight: normal;
}

table.largeTab {
	border-collapse: collapse;
	width: 100%;
	border: 1px solid black;
}

table.largeTab td,table.largeTab th {
	border: 1px solid black;
	font-size: 20px;
	font-family: '宋体';
	height: 30px;
}
-->
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
<body>
<center>
<form id="fm" name="fm">
			<div style="width: 500px; text-align: center;" id="${balancePO1.yieldly}" align="center">
				<table width="100%">
					<tr>
						<td align="center"><span
							style="font-size: 24px; font-weight: bold;">昌河汽车质量保证费用结算通知单</span>
						</td>
					</tr>
					<tr>
						<td class="boldTd">服务站名称：&nbsp; &nbsp; ${map.AREA_NAME}</td>
					</tr>
					<tr>
						<td class="boldTd">结算编号：&nbsp; &nbsp;${map.REMARK}</td>
					</tr>
					<tr>
						<td class="boldTd">上报批号:&nbsp; &nbsp; ${sbNO}</td>
					</tr>

				</table>
				<table width="100%">
					<tr>
						<td class="normalTd" colspan="2" align="left">&nbsp; &nbsp;贵单位  来结算单据  共 ${leng} 张（旧件    验</td>
						
					     
					</tr>
						
					<tr>
						<td class="normalTd" colspan="2" align="left">
								收）,其中正常单 0 张 强保单 0 张 售前单 0 张 外派单 0 张 特殊单 0 张
						</td>
					</tr>
					<tr>
						<td class="normalTd" colspan="2" align="left"> 活动单 0 张 其它 ${leng} 张</td>
					</tr>
					<tr>
						<td class="normalTd" align="left">本次结算情况如下：<span style="color: red;">开票金额为0的产地无需开票</span> </td>
					</tr>
				</table>
				<table class="sepTab">
					<tr>
						<td rowspan="2"><center>产地</center></td>
						<td rowspan="2"><center>劳务费(元)</center></td>
						<td colspan="2"><center>材料费(元)</center></td>
						<td colspan="2"><center>金额总计(元)</center></td>
					</tr>
					<tr>
						<td>一般纳税人</td>
						<td>小规模纳税人</td>
						<td>一般纳税人</td>
						<td>小规模纳税人</td>
					</tr>
					<c:forEach var="yldlist" items="${yldlist}">
						<tr>
							<c:if test="${yldlist.AREA_NAME == '九江'}">
							<td>九江</td>
							</c:if>
							<c:if test="${yldlist.AREA_NAME != '九江'}">
							<td>景德镇</td>
							</c:if>
							<td>${yldlist.LABOUR_AMOUNT}</td>
							<td>${yldlist.PART_AMOUNT}</td>
							<td>${yldlist.PART_AMOUNT01}</td>
							<td>${yldlist.AMOUNT_SUM}</td>
							<td>${yldlist.AMOUNT_SUM01}</td>
						</tr>
					</c:forEach>
					<tr>
						<td>合计</td>
						<td>${finemap.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${finemap.PLUS_MINUS_DATUM_SUM}</td>
						<td>${finemap.PLUS_MINUS_DATUM_SUM1}</td>
						<td><fmt:formatNumber value="${finemap.PLUS_MINUS_LABOUR_SUM + finemap.PLUS_MINUS_DATUM_SUM}" pattern="0.00" ></fmt:formatNumber></td>
						<td>${finemap.AOUNT}</td>
					</tr>
				</table>
				<br />
				<table width="95%">
					<tr>
						<td class="normalTd" align="left">&nbsp;&nbsp;如贵单位无异议，请按九江、景德镇两地分别开具增值税发票，在五张返</td>
					</tr>
					<tr>
						<td class="normalTd" align="left">回联上加盖财务章一并返回，我公司将按贵单位发票上的名称，开户行和帐</td>
					</tr>
					<tr>
						<td class="normalTd" align="left">号办理转帐手续。</td>
					</tr>
				</table>
				<br />
				<table class="midTab" width="100%">
					<c:forEach var="delist" items="${delist}">
						<tr>
							<td>${delist.kp}</td>
							<td>${delist.jj}</td>
							<td>${delist.jdz}</td>
						</tr>
					</c:forEach>
				</table>
				<br />
				<table width="100%" class="tab_printvoid">
					<tr>
						<td width="48%" style="font-size: 20px; text-align: center;">室主任</td>
						<td width="48%" style="font-size: 20px; text-align: center;">主管结算员</td>
					</tr>
					<tr>
						<td height="50px;">&nbsp;</td>
						<td height="50px;">&nbsp;</td>
					</tr>
				</table>
				<br />
				<table width="100%">
					<tr>
						<td class="normalTd" align="left">&nbsp; &nbsp;注：此单一式六联，服务站自存一联，返回技术服务处五联</td>
					</tr>
					<tr>
						<td class="normalTd" align="left">返回地址：江西省景德镇市106信箱技术服务处结算室(邮编333002)</td>
					</tr>
				</table>
			</div>
			<hr color="white" />
			<center>
			<div style="width: 500px;">
				<table width="100%">
					<tr>
						<td align="center"><span
							style="font-size: 24px; font-weight: bold;">昌河汽车质量保证费用结算明细表</span>
						</td>
					</tr>
					<tr>
						<td class="boldTd">结算编号：&nbsp; &nbsp;  ${map.AREA_NAME}</td>
					</tr>
					<tr>
						<td class="boldTd">上报批号:&nbsp; &nbsp; ${sbNO}</td>
					</tr>
				</table>
				<table width="100%" class="sepTab">
					<tr>
						<td  align="center" width="25%">费用类型</td>
						<td  align="center"  width="25%">劳务费(元)</td>
						<td  align="center" width="25%">材料费(元)</td>
						<td  align="center" >备注</td>
					</tr>
					<tr>
						<td>正负激励</td>
						<td>${finemap.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${finemap.PLUS_MINUS_DATUM_SUM}</td>
						<td></td>
					</tr>
					<tr>
						<td>总计</td>
						<td>${finemap.PLUS_MINUS_LABOUR_SUM}</td>
						<td>${finemap.PLUS_MINUS_DATUM_SUM}</td>
						<td></td>
					</tr>
					<tr>
					<td colspan="4" align="center" style="font-size:18">服务商各站点结算费用明细</td>
					</tr>
					<tr>
						<td colspan="2">经销商代码</td>
						<td colspan="1">劳务费</td>
						<td colspan="1">材料费</td>
					</tr>
					<tr>
						<td colspan="2">${finemap.DEALER_CODE}</td>
						<td colspan="1">${finemap.PLUS_MINUS_LABOUR_SUM}</td>
						<td colspan="1">${finemap.PLUS_MINUS_DATUM_SUM}</td>
					</tr>
				</table>
			</div>
			</center>
</form>
</center>
<script type="text/javascript">

var date =document.getElementById('createD').value;
var d = date.substr(0,10);
document.getElementById('createDate').innerHTML=d;
</script>
</body>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
<tr>    
<td width="100%" height="25" colspan="3"><script language="javascript">    
  
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
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> </td>    
</tr>   
</table>  
</html>