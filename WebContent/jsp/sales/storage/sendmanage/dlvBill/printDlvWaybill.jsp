<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<script src="<%=request.getContextPath()%>/jsp/sales/storage/sendmanage/dlvBill/myQrCode.js"></script>
<head>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> map = (Map<String, Object>) request.getAttribute("map");
%>
<link rel="Stylesheet" href="stylesheet.css" />
<script type="text/javascript">
	var d = new Date();
	var str = d.getFullYear() + "&nbsp;年&nbsp;" + (d.getMonth()+1) + "&nbsp;月&nbsp;" + d.getDate() + "&nbsp;日";
</script>
<title></title>
<style>
/* =================================运单打印网格==================================== */	
table.tab_printBillTitle {
	frame: void;
}

table.tab_printBillTitle td, table.tab_printBillTitle th {
	font-family: '宋体';
	font-size: 15px;
}

table.tab_BillContent {
	border-collapse: collapse;
	border: 1px solid black;
	frame: void;
}

table.tab_BillContent td, table.tab_BillContent th {
	border: 1px solid black;
	font-family: '宋体';
	font-size: 15px;
	text-align: left;
}

table.tab_printBill {
	border-collapse: collapse;
	width: 800px;
	border: 1px solid black;
	frame: void;
}

table.tab_printBill th {
	text-align: center;
	font-family: '宋体';
	font-weight: normal;
	font-size: 14px;
	height: 18px;
	border: solid black;
}

table.tab_printBill td {
	border: 1px solid black;
	text-align: center;
	vertical-align: middle;
	font-family: '宋体';
	font-size: 14px;
	height: 18px;
}
</style>

<style media=print>
   .Noprint {
       display: none;
   }

   .p_next {
       page-break-after: always;
   }
</style>
</head>
<body>
	<center>
	<form name="fm" method="post" id="fm">
	<input type="hidden" name="billId" id="billId" value="<%=CommonUtils.checkNull(map.get("BILL_ID"))%>"/>
		<div style="page-break-after: always; text-align: center;">
		<table width="800px" class="tab_printBillTitle" align="center">
			<tr>
				<td align="left" width="200">&nbsp;</td>
				<td align="left" width="150">&nbsp;</td>
				<td align="left" width="450"><span style="font-size: 18px; font-weight: bold; font-family: 宋体">交接单信息</span></td>
			</tr>
			<tr height="26">
				<td align="left">交接单号：<%=CommonUtils.checkNull(map.get("BILL_NO"))%></td>
				<td align="left"></td>
				<td class="right">日期：&nbsp;&nbsp;&nbsp;&nbsp;<script type="text/javascript">document.write(str);</script></td>
			</tr>
		</table>
		<table width="800px" class="tab_BillContent" align="center">
			<tr>
				<td colspan="3">承运商：<%=CommonUtils.checkNull(map.get("LOGI_NAME"))%></td>
				<td colspan="3">发运仓库：<%=CommonUtils.checkNull(map.get("WAREHOUSE_NAME"))%></td>
				<td colspan="3">经销商或收货仓库：<%=CommonUtils.checkNull(map.get("DEALER_NAME"))%></td>
			</tr>
			<tr>
				<td colspan="3">组板号：<%=CommonUtils.checkNull(map.get("BO_NO"))%></td>
				<td colspan="3">组板日期：<%=CommonUtils.checkNull(map.get("BO_DATE"))%></td>
				<td colspan="3">交接量：<%=CommonUtils.checkNull(map.get("VEH_NUM"))%>&nbsp;&nbsp;打印次数：<%=CommonUtils.checkNull(map.get("PRINT_COUNT"))%></td>
			</tr>
			<tr>
				<td colspan="3">最晚发运日期：<%=CommonUtils.checkNull(map.get("DLV_FY_DATE"))%></td>
				<td colspan="3">最晚交接日期：<%=CommonUtils.checkNull(map.get("DLV_JJ_DATE"))%></td>
				<td colspan="3">发运方式：<%=CommonUtils.checkNull(map.get("SHIP_NAME"))%></td>
			</tr>
			<tr>
				<td colspan="3">发运结算地：<%=CommonUtils.checkNull(map.get("BAL_ADDR"))%></td>
				<td colspan="6">详细地址：<%=CommonUtils.checkNull(map.get("REQ_ADDR"))%></td>
			</tr>
		</table>
		<table class="tab_printBill" style="border-top: none;" align="center">
			<tr>
				<td width="5%" style="border-top: none;"><strong>序号</strong></td>
				<td width="20%" style="border-top: none;"><strong>订单号</strong></td>
				<td width="15%" style="border-top: none;"><strong>车系/颜色</strong></td>
				<td width="25%" style="border-top: none;"><strong>车型/物料代码</strong></td>
				<td width="27%" style="border-top: none;"><strong>配置/VIN</strong></td>
				<td style="border-top: none;"><strong>VIN二维码</strong></td>
			</tr>
			<%
				List<Map<String, Object>> dlist=(List<Map<String, Object>>)request.getAttribute("dlist");//遍历明细列表
				if (dlist != null && dlist.size() > 0) {
					for(int j = 0; j < dlist.size();j++) {
						Map<String, Object> dMap = (Map<String, Object>) dlist.get(j);
			%>
			<tr height="37px">
				<td rowspan="2">&nbsp;<%=j + 1%>&nbsp;</td>
				<td rowspan="2"><%=CommonUtils.checkNull(dMap.get("ORDER_NO"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("SERIES_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("MODEL_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("PACKAGE_NAME"))%>&nbsp;</td>
				<td rowspan="2" align="center">
					<div id="qrcode<%=j + 1%>"></div>
					<input type="hidden" id="getval<%=j + 1%>" value="<%=CommonUtils.checkNull(dMap.get("VIN"))%>"/>
					<input type="hidden" name="dsize" value="<%=j + 1%>"/>
					
				</td>
			</tr>
			<tr height="37px">			
				<td><%=CommonUtils.checkNull(dMap.get("COLOR_NAME"))%>&nbsp; </td>
				<td><%=CommonUtils.checkNull(dMap.get("MATERIAL_CODE"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("VIN"))%>&nbsp;</td>
			</tr>			
			<%
					}
				}
			%>
				 </table>
			</div>
		</form>
	</center>
	<center>
		<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" class="Noprint">
			<tr>
				<td width="100%" height="25" colspan="3">
					 <OBJECT id="wb" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" style="display:none"></OBJECT>
					<div id="kpr" align="center">
						<input class="ipt" type="button" id="savebtn" value="打印" onclick="printit();" />
						<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />
						<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();" />
					</div>
				</td>
			</tr>
		</table>
	</center>
<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root, HKEY_Path, HKEY_Key;
HKEY_Root = "HKEY_CURRENT_USER";
HKEY_Path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
// 设置网页打印的页眉页脚为空
function PageSetup_Null() {
	try {
		var Wsh = new ActiveXObject("WScript.Shell");
		HKEY_Key = "header";
		Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
		HKEY_Key = "footer";
		Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
	} catch (e) {
	}
}
</script>
<script language="javascript">  

function printsetup() {
	//wb.execwb(8, 1); // 打印页面设置
	 document.all.wb.ExecWB(8, 1);// 打印页面设置
}

function printpreview() {
	//wb.execwb(7, 1); // 打印页面预览
	document.all.wb.ExecWB(7, 1);//打印页面预览
}
function printit() {
	MyConfirm("确定打印吗？",printSubmit);
}


function printSubmit(){
	document.getElementById("savebtn").disabled=true;
	makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/sendmanage/DlvWayBillManage/printCountAdd.json',saveResult,'fm');
}

function saveResult(json){
	if(json.returnValue==1){
		  document.all.wb.ExecWB(6, 1);//打印
	}else{
		MyAlert("打印失败!");
	}
	
}
</script>
<script>  
var dsize = document.getElementsByName("dsize");
for (var i = 0; i < dsize.length; i++) {
	var qrcode = new QRCode(document.getElementById("qrcode" + (i + 1)), {
				width : 65,// 设置宽高
				height : 65
			});
	document.getElementById("getval" + (i + 1)).onclick = function() {
		qrcode.makeCode(document.getElementById("getval" + (i + 1)).value);
	};
	qrcode.makeCode(document.getElementById("getval" + (i + 1)).value);
}    
</script>
</body>
</html>