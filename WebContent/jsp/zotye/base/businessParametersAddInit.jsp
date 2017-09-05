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
<title>参数新增</title>
<script type="text/javascript">
function chkVal(value){
	var str = document.getElementById(value).value ;
	if(!/^[1-9]+[0-9]{0,9}$/.test(str)){
		document.getElementById(value).value = "" ;
		MyAlert("该项只能输入不超过10位的正整数！");
	}
}

function addSubmit() {
	if(!document.getElementById('textName').value) {
		MyAlert("请填写名称！") ;
		return false ;
	}
	
	MyConfirm("确认提交?", submitSure) ;
}

function submitSure() {
	document.getElementById("addSub").disabled = true ;
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/businessParametersInsert.json";
	makeNomalFormCall(url, SubmitTip, "fm") ;
}

function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("操作成功!") ;
		
		fm.action = "<%=contextPath%>/zotye/base/BusinessParametersAction/businessParametersInit.do";
		fm.submit() ;
	} else {
		document.getElementById("addSub").disabled = false ;
		MyAlert("操作失败!") ;
	}
}

</script>
</head>
<body>
<div class="wbox">
<form id="fm" name="fm" method="post">
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
	<table class="table_query" border="0">
		 <tr>
		 		<Td class="right">参数类型:</Td>
		 		<td align="left">
		 			${hiddenParametarType }
		 			<input type="hidden" id="hiddenType" name="hiddenType" value="${selectParametarType }"/>
		 		</td>
		 		<td class="right">名称:</td>
			<td align="left">
				<input type="text" class="middle_txt" id="textName" name="textName" maxlength="20" value=""/>&nbsp;<font color="red">*</font>
			</td>
			<td class="right">值:</td>
			<td align="left">
				<input type="text" class="middle_txt" id="textValue" name="textValue" maxlength="20" value=""/>
			</td>
		 </tr>
		<tr>
			<td class="right">备注:</td>
			<td align="left">
				<input type="text" class="middle_txt" id="textRemark" name="textRemark" maxlength="100" value=""/>
			</td>
			<td class="right">排序:</td>
			<td align="left">
				<input type="text" class="middle_txt" id="textSort" name="textSort" maxlength="10" onblur="chkVal(this.id)" value=""/>
			</td>
			<td class="right">状态:</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("selectStatus",<%=Constant.STATUS%>,"","","",'',"false","");
				</script>
			</td>
		</tr>
		<tr>
			<td class="right">维护界面显示:</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("selectIsShow",<%=Constant.IF_TYPE%>,"","","",'',"false","");
				</script>
			</td>
			<td></td>
			<td colspan="3"></td>
		</tr>
	</table>
	</div>
	</div>
	<table class="table_query" border="0" align="center">
	<tr>
		<td class="center" colspan="6">
			<input type="button" class="normal_btn" onclick="addSubmit();" value="提 交" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
</table>
</form>
</div>

</body>
</html>