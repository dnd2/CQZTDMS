<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>添加位置</title>
<script type="text/javascript">
	function confirmUpdate() {
		var address = document.getElementById("address").value;
		if (address == "") {
			MyAlert("请填写位置!");
			return;
		}
		MyConfirm("确定添加位置？", editSubmitActions);
	}
	function editSubmitActions() {
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/saveAddAddress.json",showValue,'myfm');
	}
	
	function showValue(json){
		var ok = json.OK;
		if (ok == 1) {
			MyAlert("保存成功！");
			//parent.document.getElementById('inIframe').contentWindow.fromwordInit(billId);//然后跳转父页面的方法
			//MyAlertForFun('保存成功！',_hide);
		} else {
			var errinfo = json.errinfo;
			if ("" != errinfo) {
				MyAlert("勾选的车辆存在未绑定的车辆，请先绑定再上报位置！");
			}
		}
	}

	function goback() {
		parent._hide();
	}

    </script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>在途位置维护 &gt; 添加位置</div>
<form id="myfm" name="myfm" method="post">
<div class="form-panel">
<h2>位置信息</h2>
<div class="form-body">
	<input type="hidden" name="dtl_ids" value="${dtl_ids}" />
<table class="table_query">
	<tr>
		<td class="right">位置：</td>
		<td><input maxlength="1000" name="address" id="address" datatype="0,is_null" class="middle_txt"/></td>
	</tr>
</table>
</div>
</div>
<table width="100%">
	<tr>
		<td class="table_query_4Col_input" style="text-align: center">
			<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="confirmUpdate();" class="normal_btn" />
			<input type="button" name="backBtn" id="backBtn" value="关闭" onclick="goback();" class="normal_btn" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
