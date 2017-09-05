<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%
	Map<String, Object> tempMap = (Map<String, Object>) request.getAttribute("complaintAcceptMap");
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="../../../js/jslib/prototype.js"></script>
<script type="text/javascript" src="../../../js/jslib/mootools.js"></script>
<script type="text/javascript" src="../../../js/jslib/calendar.js"></script>
<HEAD>
<TITLE>投诉单维护</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
	<script type="text/javascript">
		function exportWord() {
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
</HEAD>
<BODY onload="loadcalendar();">
	<center>
		<div class="wbox">
			<div class="navigation" style="font-size: 13px; text-align: left;">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 投诉咨询管理 &gt;咨询查看
			</div>
		</div>
		<table id="complaintTable" width="100%">
			<tbody>
				<tr>
					<td><table id="Table2" width="100%" class="tab_edit">
							<tbody>
								<tr>
									<th align="center" colspan="7" style="font-size: 14px;">咨询详细信息</th>
								</tr>
								<tr>
									<td rowspan="13" width="2%" align="center" valign="middle">1</td>
									<td rowspan="13" width="2%" align="center" valign="middle">受理抱怨咨询</td>
									<td width="16%" align="right">咨询内容：</td>
									<td width="80%" colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CPCONT")%>
									</td>
								</tr>
								<tr>
									<td align="right">客户姓名：</td>
									<td colspan="3" align="left"><%=CommonUtils.getDataFromMap(tempMap, "CTMNAME")%>
									</td>
								</tr>
								<tr>
									<td align="right">联系电话：</td>
									<td width="32%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "PHONE")%></td>
									<td width="16%" align="right">VIN号码：</td>
									<td width="32%" align="left"><%=CommonUtils.getDataFromMap(tempMap, "VIN")%></td>
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
									<td align="right">状态：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "DEALMODE")%></td>
								</tr>
								<tr>
									<td align="right">车型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "MGROUP")%></td>
									<td align="right">购车日期：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BUYDATE")%></td>
								</tr>
								<tr>
									<td align="right">生产日期：</td>
									<td align="left" colspan="3"><%=CommonUtils.getDataFromMap(tempMap, "PRDATE")%></td>
								</tr>
								<tr>
									<td align="right">业务类型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZTYPE")%></td>
									<td align="right">内容类型：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "BIZCONT")%></td>
								</tr>
								<!-- 
								<tr>
									<td align="right">抱怨级别：</td>
									<td align="left" colspan="3"><%=CommonUtils.getDataFromMap(tempMap, "LEVEL")%></td>
								</tr>
								<tr>
									<td align="right">抱怨对象：</td>
									<td align="left" colspan="3"><%=CommonUtils.getDataFromMap(tempMap, "COBJ")%></td>
								</tr>
								 -->
								<tr>
									<td align="right">回复内容：</td>
									<td align="left" colspan="3"><%=CommonUtils.getDataFromMap(tempMap, "RCONT")%>
									</td>
								</tr>
								<tr>
									<td align="right">受理人：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCUSER")%></td>
									<td align="right">受理日期：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "ACCDATE")%></td>
								</tr>
								<tr>
									<td align="right">处理人：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "RUSER")%></td>
									<td align="right">处理日期：</td>
									<td align="left"><%=CommonUtils.getDataFromMap(tempMap, "RDATE")%></td>
								</tr>
							</tbody>
						</table></td>
				</tr>
				<tr>
					<td align="center">
						<c:choose>
							<c:when test="${openPage==1}">
								<input name="button2" type="button" class="long_btn" onclick="window.close();" value="关闭" /> 
								<input name="button" type="button" class="long_btn" id="three_package_set_btn" onclick="javascript:exportWord();" value="导出WORD" />
							</c:when>
							<c:otherwise>
								<input name="button2" type="button" class="long_btn" onclick="history.back()" value="返回" />
								<input name="button" type="button" class="long_btn" id="three_package_set_btn" onclick="javascript:exportWord();" value="导出WORD" />
							</c:otherwise>
						</c:choose>
					</td> 					
				</tr>
			</tbody>
		</table>
	</center>
</body>
</html>