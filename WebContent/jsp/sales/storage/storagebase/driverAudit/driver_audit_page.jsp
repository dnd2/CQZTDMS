<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/fmt" prefix="fmt"%>

<head>
<%
	String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>司机审核</title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>司机审核</div>

<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>司机审核</h2>
	<div class="form-body">
	<input type="hidden" value="${map.DRIVER_ID }" id="driver_id" name="driver_id" />
	<table class="table_query">
		<tr>
			<td class="right" width="80px;">司机姓名：</td>
			<td align="left">${map.DRIVER_NAME }</td>
			<td class="right" width="80px;">司机电话：</td>
			<td align="left">${map.DRIVER_PHONE }</td>
			<td class="right" width="80px;">申请日期：</td>
			<td align="left"><fmt:formatDate value="${map.APPLY_DATE }"
				pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<td class="right">审核意见：</td>
			<td colspan="5"><textarea class="form-control" id="audit_remark" name="audit_remark"></textarea></td>
		</tr>
		<tr>
			<td height="8" colspan="8" class="table_query_4Col_input" style="text-align: center">
				<input type="button" class="normal_btn" id="saveButton" onclick="addReservoir(20501003)" value="审核通过" />&nbsp;&nbsp;
				<input type="button" class="normal_btn" id="saveButton1" onclick="addReservoir(20501004)" value="审核拒绝" />&nbsp;&nbsp;
				<input type="button" class="normal_btn" id="goBack" onclick="back();" value="返回" /></td>
		</tr>
	</table>
	</div>
</div>
<!-- 基本信息end --></form>
<!--页面列表 begin -->
<script type="text/javascript">
//添加
function addReservoir(status)
{
	var audit_remark = document.getElementById("audit_remark").value;
	if(status==20501004 && audit_remark==""){
		MyAlert("审核拒绝,审核备注不能为空!");
		return;
	}
	if(!submitForm("fm")){
		return;
	}
	
	MyConfirm("确认审核？", addFareMileage, [status]);
}

function addFareMileage(status)
{
	disabledButton(["saveButton","saveButton1","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAudit.json?status="+status,addFareMileageBack,'fm'); 
}

function addFareMileageBack(json)
{
	if(json.reslut=='SUCCESS'){
		 back();
	}
	else
	{
		disabledButton(["saveButton","goBack"],true);
		//MyAlert("操作失败！请联系系统管理员！");
		MyAlert(json.reslut);
	}
}
function back(){
	window.location.href='<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAuditList.do';
}
</script>
</body>
</html>
