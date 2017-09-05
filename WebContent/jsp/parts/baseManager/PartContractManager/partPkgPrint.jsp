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
		html,body{font-size:12px;margin:0px;height:100%;}
		.mesWindow{border:#666 1px solid;background:#fff;}
		.mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
		.mesWindowContent{margin:4px;font-size:18px;}
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
		idx += 1;
		document.write(idx);
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
	function PageSetup_Null()
	{
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
	function PageSetup(name,value)
	{
		try
		{
			var Wsh=new ActiveXObject("WScript.Shell");
			HKEY_Key=name;
			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,value);
//			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&p/&P");
//			HKEY_Key="margi_bottom";
//			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"36mm");
//			HKEY_Key="margi_top";
//			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"24mm");
//			HKEY_Key="margi_left";
//			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"10mm");
//			HKEY_Key="margi_right";
//			Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"10mm");
			//HKEY_Key="footer";
			//Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&u&b&d");
		}
		catch(e)
		{}
	}
	PageSetup("margi_right","10mm");
	PageSetup("margi_left","10mm");
</script>
<OBJECT  id=WebBrowser  classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display:none">
</OBJECT>
<body>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
	<c:forEach items="${allList}" var="subList" varStatus="curSta">
		<c:if test="${curSta.last}">
			<div name=thisblock id=thisblock ></div>
		</c:if>
		<c:if test="${!curSta.last}">
			<div name=thisblock id=thisblock class="PageNext" ></div>
		</c:if>
		<TABLE border=0 bordercolor=black cellpadding=3  cellspacing=0 width="91%" >
			<br>
			<br>
			<br>
			<center>
				<font size="+1"><b>在库成本调整单</b></font>
			</center>
		</TABLE>
		<br/>
		<div>
			<TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%" >
				<tr align="center">
					<td></td>
					<td style="border: 0" width="6%"  align="left">单据编码：${orderCode}</td>
					<td style="border: 0" width="5%">供货商编码：${VENDER_CODE}</td>
					<td style="border: 0" colspan="3" width="10%"  align="center">供货商名称：${RulstCom}</td>
				</tr>
				<tr align="center">
					<td  colspan=1 width="2%"  height="20mm" >序号</td>
					<td  colspan=1 width="6%"  height="20mm">备件编码</td>
					<td  colspan=1 width="6%"  height="20mm">备件名称</td>
					<td  colspan=1 width="5%"  height="20mm">备件件号</td>
					<td  colspan=1 width="5%"  height="20mm">调整金额(不含税)</td>
					<td  colspan=1 width="3%"  height="20mm">调整原因</td>
				</tr>
				<c:forEach items="${subList}" var="data" varStatus="count">
					<tr>
						<td align="center"  height="20mm">
							<script language="javascript">
								getIndex()
							</script>
						</td>
						<td  height="20mm">
							&nbsp;${data.PART_OLDCODE}
						</td>
						<td   height="20mm">
							&nbsp;${data.PART_CNAME}
						</td>
						<td  height="20mm">
							&nbsp;${data.PART_CODE}
						</td>
						<td align="right"  height="20mm">
							&nbsp;${data.ADJUST_AMOUNT}
						</td>
						<td height="20mm">
							&nbsp;${data.REMARK}
						</td>
					</tr>
					<c:if test="${curSta.last==true&&count.last==true}">
						<tr align="center" >
							<td  colspan=1 width="2%" align="center"  height="20mm">合计:</td>
							<td  colspan=1 width="5%" align="left"  height="20mm">--</td>
							<td  colspan=1 width="5%" align="left"  height="20mm">--</td>
							<td  colspan=1 width="5%" align="left"  height="20mm">--</td>
							<td  colspan=1 width="3%" align="right"  height="20mm">${Amount}</td>
							<td  colspan=1 width="3%" align="left"  height="20mm">--</td>
						</tr>
						<tr align="center">
							<td  colspan=1 width="2%" align="center"  height="20mm">备注</td>
							<td  colspan=5 width="100%" align="left"  height="20mm">${remark}</td>
						</tr>
					</c:if>
				</c:forEach>
			</TABLE>
			<c:if test="${curSta.last==true}">
				<table align="center" width="91%">
					<tr>
						<td align="left">
							财务人员：
						</td>
						<td align="right">
							制单日期：&nbsp;${MarkDate}
						</td>

					</tr>
				</table>
			</c:if>
		</div>
	</c:forEach>
	<br>
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
</html>