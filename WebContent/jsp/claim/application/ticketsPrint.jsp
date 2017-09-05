<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/style/content.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/dict.js"></script>
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

.PageNext {
	page-break-after: always;
}
</style>

<style type="text/css">
table.sepTab {
	border-collapse: collapse;
	border: 1px solid black;
	text-align: center
}

table.sepTab td,table.sepTab th {
	border: 1px solid black;
	font-size: 14px;
	font-family: '宋体';
	height: 20px;
}
</style>
<script type="text/javascript">
	//去除打印时的页眉和页脚
	var HKEY_Root, HKEY_Path, HKEY_Key;
	HKEY_Root = "HKEY_CURRENT_USER";
	HKEY_Path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	//设置网页打印的页眉页脚为空    
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
<title>收票打印</title>
</head>
<body>
	<form id="fm" name="fm">
	    <br/>
	    <br/>
	     <br/>
		<center>
			<table style="font:xx-large;"  class="tab_printsep"  width="100%">
				<tr>
					<td>运费</td>
					
					<c:if test="${ticketsPO.balanceYieldly == 95411001}">
						<td nowrap="nowrap">${ticketsPO.carriage}</td>
					</c:if>
					<c:if test="${ticketsPO.balanceYieldly == 95411002}">
						<td nowrap="nowrap">${ticketsPO.daCarriage}</td>
					</c:if>
					
					<td>服务站简称</td>
					<td colspan="5">${ticketsPO.dealername}</td>
				</tr>
				<tr>
					<td>信函总类</td>
					<td nowrap="nowrap"><script type='text/javascript'>
						var activityType = getItemValue('${ticketsPO.letter}');
						document.write(activityType);
					</script>
					</td>
					<td>发出日期</td>
					<td nowrap="nowrap"><fmt:formatDate
							value="${ticketsPO.startdate}" pattern="yyyy-MM-dd" />
					</td>
					<td>收到日期</td>
					<td><fmt:formatDate value="${ticketsPO.enddate}"
							pattern="yyyy-MM-dd" />
					</td>
					<td>信函情况</td>
					<td nowrap="nowrap"><script type='text/javascript'>
						var activityType = getItemValue('${ticketsPO.lettersf}');
						document.write(activityType);
					</script>
					</td>
				</tr>
				<tr>
					<td colspan="2">单据批号</td>
					<td colspan="2">收单数</td>
					<td colspan="2">索赔类型</td>
					<td colspan="2">编号</td>
				</tr>
				<c:forEach items="${ticketsList}" var="ticketsList" >
					<tr>
						<td colspan="2">${ticketsList.goodsnum}</td>
						<td colspan="2">${ticketsList.aplcount}</td>
						<td colspan="2">${ticketsList.claimType}</td>
						<td colspan="2">${ticketsList.numberAp}</td>
					</tr>
				</c:forEach>
			</table>
		</center>
	</form>
	<script type="text/javascript">
		var date = document.getElementById('createD').value;
		var d = date.substr(0, 10);
		document.getElementById('createDate').innerHTML = d;
	</script>
</body>
<table width="100%" cellpadding="1" onmouseover="kpr.style.display='';">
	<tr>
		<td width="100%" height="25" colspan="3"><script
				language="javascript">
			function printsetup() {
				// 打印页面设置    
				wb.execwb(8, 1);
			}
			function printpreview() {
				// 打印页面预览      
				wb.execwb(7, 1);
			}
			function printit() {
				if (confirm('确定打印吗？')) {

					wb.execwb(6, 6)
				}
			}
		</script> <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0
				id=wb name=wb width=3></OBJECT>
			<div id="kpr" align="center">
				<input class="normal_btn" type=button name=button _print value="打印"
					onclick="kpr.style.display='none';javascript :printit();">
				<input class="normal_btn" style="width: 80px;" type=button name=button _setup value="打印页面设置"
					onclick=" javascript : printsetup();"> <input class="normal_btn"
					type=button name=button_show value="打印预览"
					onclick="kpr.style.display='none';javascript:printpreview();">
				<input class="normal_btn" type=button name=button _fh value="关闭"
					onclick=" javascript:window.close();">
		</td>
	</tr>
</table>
</html>