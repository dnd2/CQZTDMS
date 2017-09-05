<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
	String contextPath = request.getContextPath();
	List<Map<String, Object>> valueList = (List<Map<String, Object>>) request
			.getAttribute("mlist");
%>
<!-- <link rel="Stylesheet" href="stylesheet.css" /> -->
<script type="text/javascript">
	var d = new Date();
	var str = d.getFullYear() + "&nbsp;年&nbsp;" + (d.getMonth()+1) + "&nbsp;月&nbsp;" + d.getDate() + "&nbsp;日";
</script>
<title></title>
<style media="print">
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
</style>
</head>
<body>
	<center>	
		<%
		if (valueList != null && valueList.size() > 0) {
			for(int i = 0; i < valueList.size();i++) {
				Map<String, Object> veMap = (Map<String, Object>) valueList.get(i);
		%>	
		<div style="page-break-after: always; text-align: center;">
		<table width="800px" class="tab_printBillTitle" align="center">
			<tr>
				<td align="left" width="300"></td>
				<td align="left" width="50">&nbsp;</td>
				<td align="left" width="450"><span style="font-size: 18px; font-weight: bold; font-family: 宋体">发运单信息</span></td>
			</tr>
			<tr height="26">
				<td align="left">经销商或收货仓库：<font color="red">（<%=CommonUtils.checkNull(veMap.get("DEALER_NAME"))%>）</font></td>
				<td></td>
				<td class="right">日期：&nbsp;&nbsp;<script type="text/javascript">document.write(str);</script></td>
			</tr>
		</table>
		<table width="800px" class="tab_BillContent" align="center">
			<tr>
				<td>发运方式：<%=CommonUtils.checkNull(veMap.get("TRANS_NAME"))%></td>
				<td colspan="7">结算地：<%=CommonUtils.checkNull(veMap.get("BAL_ADDR"))%></td>
			</tr>
		</table>
		<table class="tab_printBill" style="border-top: none;" align="center">
			<tr>
				<td width="4%" style="border-top: none;"><strong>序号</strong></td>
				<td width="17%" style="border-top: none;"><strong>订单号</strong></td>
				<td width="12%" style="border-top: none;"><strong>车系</strong></td>
				<td width="12%" style="border-top: none;"><strong>车型</strong></td>
				<td width="17%" style="border-top: none;"><strong>配置</strong></td>
				<td width="10%" style="border-top: none;"><strong>颜色</strong></td>
				<td width="20%" style="border-top: none;"><strong>物料代码</strong></td>
				<td width="8%" style="border-top: none;"><strong>组板量</strong></td>
			</tr>
			<%
				List<Map<String, Object>> dlist=(List<Map<String, Object>>)veMap.get("dlist");//遍历明细列表
				if (dlist != null && dlist.size() > 0) {
					for(int j = 0; j < dlist.size();j++) {
						Map<String, Object> dMap = (Map<String, Object>) dlist.get(j);
			%>
			<tr>
				<td><%=j + 1%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("ORD_NO"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("SERIES_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("MODEL_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("PACKAGE_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("COLOR_NAME"))%>&nbsp; </td>
				<td><%=CommonUtils.checkNull(dMap.get("MATERIAL_CODE"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(dMap.get("BD_TOTAL"))%>&nbsp;</td>
			</tr>
			
			<%
					}
				}
			%>
				 </table>
			</div>
			<%	 
			  }
			}
			%>
			
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
<script language="javascript">    
function printsetup() {
	//wb.execwb(8, 1); // 打印页面设置
	if ( document.all.wb && document.all.wb.ExecWB ) {
		document.all.wb.ExecWB(8, 1);// 打印页面设置
	} else {
		window.print();
	}
}

function printpreview() {
	//wb.execwb(7, 1); // 打印页面预览
	if ( document.all.wb && document.all.wb.ExecWB ) {
		document.all.wb.ExecWB(7, 1);//打印页面预览
	} else {
		window.print();
	}
}
function printit() {
	// MyConfirm("确定打印吗？",printSubmit);
	printSubmit();
}

function printSubmit(){
	//document.getElementById("savebtn").disabled=true;

	if ( document.all.wb && document.all.wb.ExecWB ) {
		document.all.wb.ExecWB(6, 1);//打印
	} else {
		window.print();
	}
}
</script>
</body>
</html>