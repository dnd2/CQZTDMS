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
									<th align="center" style="font-size: 14px;" colspan="6">抱怨咨询详细信息</th>
								</tr>
								<tr>
									<td rowspan="8" width="2%" align="center">1</td>
									<td rowspan="8" width="2%" align="center">受理抱怨咨询</td>
									<td width="16%" align="right">编号：</td>
									<td width="80%" colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPNO")%></td>
								</tr>
								<tr>
									<td align="right">抱怨/咨询内容：</td>
									<td colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPCONT")%>
									</td>
								</tr>
								<tr>
									<td align="right">客户姓名：</td>
									<td colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CTMNAME")%></td>
								</tr>
								<tr>
									<td width="15%" align="right">联系电话：</td>
									<td width="25%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPPHONE")%></td>
									<td width="15%" align="right">VIN号/车牌号：</td>
									<td width="25%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "VIN")%></td>
								</tr>
								<tr>
									<td align="right">省份：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "PRO")%></td>
									<td align="right">城市：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CITY")%></td>
								</tr>
								<tr>
									<td align="right">行驶里程：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "MILEAGE")%></td>
									<td align="right">车辆用途：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "VINUSE")%></td>
								</tr>
								<tr>
									<td align="right">车型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "MNAME")%></td>
									<td align="right">购车日期：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BUYDATE")%></td>
								</tr>
								<tr>
									<td align="right">记录人：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCISER")%></td>
									<td align="right">记录时间：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCDATE")%></td>
								</tr>
								<tr>
									<td rowspan="4" align="center">2</td>
									<td rowspan="4" align="center">抱怨受理</td>
									<td align="right">抱怨类型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZCONT")%>
									</td>
									<td align="right">故障部件：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "FAULTP")%></td>
								</tr>
								<tr>
									<td align="right">抱怨级别：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPLEV")%></td>
									<td align="right">规定处理期限(天)：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPLIM")%></td>
								</tr>
								<tr>
									<td align="right">处理部门：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPDEALORG")%>
									</td>
									<td align="right">规定关闭时间：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "SETCLOSEDATE")%></td>
								</tr>
								<tr>
									<td align="right">转出人：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "TURNUSER")%></td>
									<td align="right">转出时间：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "TURNDATE")%></td>
								</tr>
								<tr id="fankui">
									<td align="center">3</td>
									<td align="center">处理结果和反馈确认</td>
									<td valign="middle" colspan="4">
										<table width="100%" class="voidTab">
											<tr>
							              		<th align="center" width="20%">处理时间</th>
							              		<th align="center" width="50%">处理内容</th>
							              		<th align="center" width="10%">反馈人</th>
							              		<th align="center" width="20%">当前处理人</th>
						              		</tr>
						              		<c:forEach items="${dealRecordList}" var="dealR">
						              			<tr>
						              				<td align="center">${dealR.CDDATE}</td>
						              				<td align="center">${dealR.CDCONT}</td>
						              				<td align="center">${dealR.USERNAME}</td>
						              				<td align="center">${dealR.NEXTNAME}</td>
						              			</tr>
						              		</c:forEach>
										</table>
									</td>
								</tr>
								<tr id="huifang">
									<td align="center">4</td>
									<td align="center">用户回访</td>
									<td valign="top" colspan="4">
										<table width="100%" width="100%" class="voidTab">
											<tr>
												<th width="20%">回访时间</th>
												<th width="50%">回访内容</th>
												<th width="20%">回访结果</th>
												<th width="10%">回访人</th>
											</tr>
											<c:forEach items="${returnRecordList}" var="returnR">
												<tr>
													<td align="center">${returnR.CRDATE}</td>
													<td align="center">${returnR.CRCONT}</td>
													<td align="center">${returnR.CONFIRMOPTION}</td>
													<td align="center">${returnR.CRUSER}</td>
												</tr>
											</c:forEach>
										</table>
									</td>
								</tr>
								<tr id="huifang">
				              <td align="center">5 </td>
				              <td align="center">申请延期记录 </td>
				              <td valign="top" colspan="4">
				              	<table width="100%" class="tab_edit">
				              		<tr>
					              		<th width="10%">申请延期至时间</th>
					              		<th width="5%">申请天数</th>
					              		<th width="10%">申请类型</th>
					              		<th width="10%">申请内容</th>
					              		<th width="10%">申请人</th>
					              		<th width="10%">申请时间</th>
					              		<th width="10%">审核时间</th>
					              		<th width="10%">审核内容</th>
					              		<th width="10%">审核人</th>
					              		<th width="5%">审核状态</th>
					              		<th width="10%">延期当前处理人</th>
				              		</tr>
				              		<c:forEach items="${verifyRecordList}" var="verify">
				              			<tr>
				              				<td align="center">${verify.CLDATE}</td>
				              				<td align="center">${verify.DAYS}</td>
				              				<td align="center"><script type='text/javascript'>
								              var activityKind=getItemValue('${verify.CPDEFERTYPE}');
								              document.write(activityKind) ;
								             </script></td>
				              				<td align="center">${verify.CLCONT}</td>
				              				<td align="center">${verify.CLUSER}</td>
				              				<td align="center">${verify.CREATEDATE}</td>
				              				<td align="center">${verify.CLVERIFYDATE}</td>
				              				<td align="center">${verify.CLVERIFYCONTENT}</td>
				              				<td align="center">${verify.CLVERIFYUSER}</td>
				              				<td align="center">${verify.CLVERIFYSTATUS}</td>
				              				<td align="center">${verify.NEXTNAME}</td>
				              			</tr>
				              		</c:forEach>
				              	</table>
				              </td>
				            </tr>     
								<tr>
									<td rowspan="3" align="center">6</td>
									<td rowspan="3" align="center">抱怨关闭</td>
									<td align="right">关闭理由：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "CLOSEREASE")%></td>
									<td align="right">是否正常关闭：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "ISNORMALCLOSE")%></td>
								</tr>
								<tr>
									<td align="right">关闭人：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "CLOSEUSER")%></td>
									<td align="right">是否及时关闭：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "ISTIMELYCLOSE")%></td>
								</tr>
								<tr>
									<td align="right">关闭时间：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "CLOSEDATE")%></td>
									<td align="right">实际处理时长(小时)：</td>
									<td><%=CommonUtils.getDataFromMap(tempMap, "DURATIONCLOSE")%>小时
									</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td align="center">
							<c:choose>
								<c:when test="${openPage==1}">
									<input name="button" type="button" class="long_btn" onclick="window.close();" ;" value="关闭" /> 
									<input name="button" type="button"class="long_btn" id="three_package_set_btn"onclick="javascript:exportWord();" ;" value="导出WORD" /> 
								</c:when>
								<c:otherwise>
									<input name="button" type="button" class="long_btn" onclick="history.back();" ;" value="返回" /> 
									<input name="button" type="button"class="long_btn" id="three_package_set_btn"onclick="javascript:exportWord();" ;" value="导出WORD" /> 
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	</FORM>
</body>
</html>