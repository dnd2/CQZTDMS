<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%
	Map<String, Object> tempMap = (Map<String, Object>) request
			.getAttribute("complaintConsult");
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>投诉咨询记录</title>
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function exportWord() {
		/*var oWD = new ActiveXObject("Word.Application"); 
		oWD.WindowState = 2; 
		var oDC = oWD.Documents.Add("",0,1); 
		var oRange =oDC.Range(0,1); 
		var sel = document.body.createTextRange(); 
		sel.moveToElementText(id);  //里面参数为div的id或者form的id或者table的id或者...表示将此范围内输出word
		sel.select(); 
		sel.execCommand("Copy"); 
		oRange.Paste(); 
		oWD.Application.Visible = true; */
		var word = new ActiveXObject("Word.Application");
		var doc = word.Documents.Add("", 0, 1);
		var Range = doc.Range();
		var sel = document.body.createTextRange();
		sel.moveToElementText(Table2);
		sel.select();
		sel.execCommand("Copy");
		Range.Paste();
		word.Application.Visible = true;
	}
</script>
</head>
<body onload="loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置：投诉咨询管理 &gt;投诉咨询记录
		</div>
	</div>
	<FORM name="FRM2" METHOD=POST>
		<div id="res">
			<table id="complaintTable" width="100%">
				<tbody>
					<tr>
						<td><table id="Table2" width="100%" class="tab_edit">
								<tr>
									<th align="center" style="font-size: 14px;" colspan="5">投诉咨询详细信息</th>
								</tr>
								<tr>
									<td rowspan="4" width="10%" align="center">客户信息</td>
									<td width="20%" align="right">投诉咨询单号：</td>
									<td width="70%" colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPNO")%></td>
								</tr>
								<tr>
									<td align="right">客户姓名：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CTMNAME")%></td>
									<td width="15%" align="right">客户电话：</td>
									<td width="25%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPPHONE")%></td>
								</tr>
								<tr>
									<td align="right">联系人：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "PERSON")%></td>
									<td align="right">联系电话：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "CPPHONE")%></td>
								</tr>
								<tr>
									<td align="right">省份：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "PRO")%></td>
									<td align="right">城市：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CITY")%></td>
								</tr>
								<tr>
									<td align="center" rowspan="5">车辆信息</td>
									<td width="15%" align="right">VIN：</td>
									<td width="25%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "VIN")%></td>
									<td align="right">是否有车：</td>
									<td>
										<%
											if(CommonUtils.getDataFromMap(tempMap, "ISHASCAR") != null && CommonUtils.getDataFromMap(tempMap, "ISHASCAR").toString().equals("0")) {
												out.print("有车");
											} else {
												out.print("无车");
											}
										%>
									</td>
								</tr>
								<tr>
									<td align="right">车辆用途：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPUSER")%></td>
									<td align="right">车辆性质：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "NATURE")%></td>
								</tr>
								<tr>
									<td align="right">行驶里程：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "MILEAGE")%></td>
									<td align="right">车系：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "SERIESID")%></td>
								</tr>
								<tr>
									<td align="right">车型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "MODELID")%></td>
									<td align="right">购车日期：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BDATE")%></td>
								</tr>
								<tr>
									<td align="right">记录人：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCUSER")%></td>
									<td align="right">记录时间：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCDATE")%></td>
								</tr>
								<tr>
									<td rowspan="2" align="center">业务类型</td>
									<td align="right">业务类型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZTYPE")%>
									</td>
									<td align="right">业务内容：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZCONT")%>
								</tr> 
								<tr>
									<%-- <td align="right">业务内容：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZCONT")%> --%>
									<td align="right">复杂度状态：</td>
									<td colspan="4" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPLEVEL")%></td>
								</tr>
								<tr>
									<td align="center">投诉/咨询内容</td>
									<td colspan="4" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPCONT")%>
									</td>
								</tr>
								<tr id="fankui">
									<td align="center">处理结果</td>
									<td valign="middle" colspan="4">
										<table width="100%" class="voidTab">
											<tr>
							              		<th align="center" width="10%">处理时间</th>
							              		<th align="center" width="70%">处理内容</th>
							              		<th align="center" width="8%">当前处理人</th>
							              		<th align="center" width="12%">处理状态</th>
						              		</tr>
						              		<c:forEach items="${dealRecordList}" var="dealR">
						              			<tr>
						              				<td align="center">${dealR.CDDATE}</td>
						              				<td align="left">${dealR.CDCONT}</td>
						              				<td align="center">${dealR.USERNAME}</td>
						              				<td align="center">${dealR.STATUS}</td>
						              			</tr>
						              		</c:forEach>
										</table>
									</td>
								</tr>
								
					<tr>
						<td align="center" colspan="6">
								<input name="button" type="button" class="long_btn" onclick="window.close();" ;" value="关闭" /> 
								<!-- <input name="button" type="button"class="long_btn" id="three_package_set_btn"onclick="javascript:exportWord();" ;" value="导出WORD" />  -->
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	</FORM>
</body>
</html>