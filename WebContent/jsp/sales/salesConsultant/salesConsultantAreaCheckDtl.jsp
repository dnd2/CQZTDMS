<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售顾问审核</title>
<script type="text/javascript">
<!--
function doInit()
{  
	loadcalendar();   //初始化时间控件
}

function checkIt(value) {
	if(submitForm("fm")) {
		MyConfirm("确认操作?", checkSure, [value]) ;
	}
}

function checkSure(value) {
	document.getElementById("status").value = value ;
	
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/areaCheck.json";
	makeFormCall(url, checkTip, "fm") ;
}

function checkTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("操作成功!") ;
		
		$('fm').action= "<%=contextPath%>/sales/salesConsultant/SalesConsultant/areaCheckInit.do";
		$('fm').submit();
	} else {
		MyAlert("操作失败!") ;
	}
}
//-->
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 销售顾问管理 &gt; 销售顾问审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<th colspan="2">
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售顾问信息
			</th>
		</tr>
		<tr>
			<td align="right">经销商全称：</td>
			<td align="left">${dtlMap.DEALER_NAME }</td>
		</tr>
		<tr>
			<td align="right">姓名：</td>
			<td align="left">${dtlMap.NAME }</td>
		</tr>
		<tr>
			<td align="right">身份证号：</td>
			<td align="left">${dtlMap.IDENTITY_NUMBER}</td>
		</tr>
		<tr>
			<td align="right">性别：</td>
			<td align="left">
				<script>document.write(getItemValue(${dtlMap.SEX }));</script>
			</td>
		</tr>
		<tr>
			<td align="right">年龄：</td>
			<td align="left">${dtlMap.AGE }</td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td align="left">
				<script>document.write(getItemValue(${dtlMap.ACADEMIC_RECORDS }));</script>
			</td>
		</tr>
		<tr>
			<td align="right">从事汽车行业年份：</td>
			<td align="left">${dtlMap.TRADEYEAR }&nbsp;年</td>
		</tr>
		<tr>
			<td align="right">从事长安汽车行业年份：</td>
			<td align="left">${dtlMap.CHANATRADEYEAR }&nbsp;年</td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td align="left">${dtlMap.TEL }</td>
		</tr>
		<tr>
			<td align="right">申请原因：</td>
			<td align="left">
				<textarea id="reason" name="reason" rows="3" cols="30" disabled="disabled">${dtlMap.TEL }</textarea>
			</td>
		</tr>
	</table>
	<table class="table_query" border="0">
		<tr>
			<th colspan="4">
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核信息
			</th>
		</tr>
		<tr align="center">
			<td>审核人员</td>
			<td>审批状态</td>
			<td>审批描述</td>
			<td>审批时间</td>
		</tr>
		<c:forEach items="${chkList}" var="chkList">
			<tr class="table_list_row2" align="center">
				<td>${chkList.NAME}</td>
				<td>
					<script>document.write(getItemValue(${chkList.STATUS }));</script>
				</td>
				<td>${chkList.DEPICT}</td>
				<td>${chkList.CHKDATE}</td>
			</tr>
		</c:forEach>
		<tr>
			<td align="right">审核意见：</td>
			<td align="left">
				<textarea id="desc" name="desc" rows="3" cols="30"></textarea>
			</td>
		</tr>
	</table>
	<br />
	<table border="0" align="center">
	<tr>
		<td align="center" colspan="2">
			<input type="hidden" name="headId" id="headId" value="${dtlMap.ID }" />
			<input type="hidden" name="status" id="status" value="" />
			<input type="button" class="normal_btn" onclick="checkIt(<%=Constant.SALES_CONSULTANT_STATUS_PASS %>);" value="通过" id="passIt" />
			<input type="button" class="normal_btn" onclick="checkIt(<%=Constant.SALES_CONSULTANT_STATUS_RETURN %>);" value="驳回" id="returnIt" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>