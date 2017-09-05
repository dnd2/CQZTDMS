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
<title>首保信息修改</title>
<script type="text/javascript">
function chkVal(value){
	var str = document.getElementById(value).value ;
	if(!/^[1-9]+[0-9]{0,7}$/.test(str)){
		document.getElementById(value).value = "" ;
		MyAlert("该项只能输入不超过8位的正整数！");
	}
}

function addSubmit() {
	if(!document.getElementById('textMainMile').value) {
		MyAlert("请填写保养里程！") ;
		return false ;
	}
	
	if(!document.getElementById('textMainDate').value) {
		MyAlert("请填写保养时间！") ;
		return false ;
	}
	
	MyConfirm("确认提交?", submitSure) ;
}

function submitSure() {
	document.getElementById("addSub").disabled = true ;
	var url = "<%=contextPath%>/zotye/service/baseInfo/FirstMaintenanceAction/firstMaintenaceUpdate.json";
	makeNomalFormCall(url, SubmitTip, "fm") ;
}

function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("操作成功!") ;
		
		__parent().__extQuery__(1);
		_hide();
		<%-- fm.action = "<%=contextPath%>/zotye/service/baseInfo/FirstMaintenanceAction/firstMaintenaceInit.do";
		fm.submit() ; --%>
	} else {
		document.getElementById("addSub").disabled = false ;
		MyAlert("操作失败!") ;
	}
}

</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 基础数据管理 &gt; 首保信息修改</div>
<form id="fm" name="fm" method="post">
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
	<table class="table_query" border="0">
		 <tr>
		 		<td style="text-align:right">保养里程:</Td>
		 		<td style="text-align:left">
		 			<input type="text" class="middle_txt" id="textMainMile" name="textMainMile" maxlength="8" onblur="chkVal(this.id) ;" value="${mainMile }"/>&nbsp;<font color="red">*</font>
		 		</td>
		 </tr>
		<tr>
			<td style="text-align:right">保养时间/天</td>
			<td style="text_align:left">
				<input type="text" class="middle_txt" id="textMainDate" name="textMainDate" maxlength="8" onblur="chkVal(this.id) ;" value="${mainDate }"/>&nbsp;<font color="red">*</font>
			</td>
		</tr>
	</table>
	<br />
	<table class="table_query" border="0" align="center">
	<tr>
		<td style="text-align:center" colspan="2">
			<input type="hidden" id="id" name="id" value="${id }" />
			<input type="button" class="normal_btn" onclick="addSubmit();" value="提 交" id="addSub" />
			<input type="button" class="normal_btn" onclick="_hide(); ;" value="返 回" id="retBtn" />
		</td>
	</tr>
</table>
</div>
</div>
</form>
</div>

</body>
</html>