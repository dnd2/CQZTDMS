<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车型组维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;车型组维护</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<table class="table_query">
 <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">车型组类型：</td>
 	<td>
 	  <script type="text/javascript">
	     genSelBoxExp("WRGROUP_TYPE",<%=Constant.WR_MODEL_GROUP_TYPE%>,"<%=Constant.WR_MODEL_GROUP_TYPE_01%>",false,"","","false",'<%=Constant.WR_MODEL_GROUP_TYPE_02%>');
	   </script>
 	</td>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">车型组代码：</td>
 	<td><input type="text" name="WRGROUP_CODE" id="WRGROUP_CODE" class="middle_txt" maxlength="15" />&nbsp;<font color="red">*</font></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">车型组名称：</td>
 	<td>
 	    <input type="text" name="WRGROUP_NAME" id="WRGROUP_NAME" class="middle_txt" maxlength="30" />&nbsp;<font color="red">*</font>
 	</td>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">保养费用：</td>
 	<td>
 		 <input type="text" name="FREE"  id="FREE" class="middle_txt" maxlength="8" value="" />&nbsp;<font color="red">*</font>
 		 <input type="hidden" id="LABOUR_PRICE" name="LABOUR_PRICE" value="0.00" />
 		 <input type="hidden" id="PART_PRICE" name="PART_PRICE" value="" />
 	</td>
 </tr> 
 <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">新车整备费：</td>
 	<td>
 		 <input type="text" id="textNewCarFee" name="textNewCarFee" class="middle_txt" maxlength="8" blurback="true" value=""/>&nbsp;<font color="red">*</font>
 	</td>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">首保条件：</td>
 	<td>
 		 <select name="selFirstMainCondition" id="selFirstMainCondition" class="u-select">
			<option value="">-请选择-</option>
			<c:forEach items="${list }" var="firstMainConditionMap">
				<option value="${firstMainConditionMap.ID }">首保里程：${firstMainConditionMap.END_MILEAGE }&nbsp;-&nbsp;首保时间：${firstMainConditionMap.MAX_DAYS }</option>
			</c:forEach>
		</select>&nbsp;<font color="red">*</font>
 	</td>
 </tr>
</table>

<table class="table_query" >
 <tr>
 	<td colspan="4" style="text-align:center">
 		<input class="normal_btn" type="button" name="ok" id="btnAdd" value="新增" onclick="checkForm('<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelAdd.json');"/> 
 		<input class="normal_btn" name="back" type="button" onclick="_hide();" value="返回"/>
 	</td>
 </tr>
</table>
</div>
</div>
</form>
</div>
<script>
function chkCon() {
	if(chkNull("WRGROUP_CODE", "请填写车型组代码！")) return false ;
	if(chkNull("WRGROUP_NAME", "请填写车型组名称！")) return false ;
	if(chkNull("FREE", "请填写保养费用名称！")) return false ;
	if(chkNull("textNewCarFee", "请填写新车整备费！")) return false ;
	if(chkNull("selFirstMainCondition", "请选择首保条件！")) return false ;
	if(!chkPositiveDouble("FREE", "保养费用必须填写数字！")) return false ;
	if(!chkPositiveDouble("textNewCarFee", "新车整备费必须填写数字！")) return false ;
	
	return true ;
}
function chkNull(value, str) {
	if(!document.getElementById(value).value) {
		MyAlert(str) ;
		return true ;
	} 
	return false ;
}
function chkPositiveDouble(value, str) {
	var strChk = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/ ;
	
	if(!strChk.test(document.getElementById(value).value)) {
		document.getElementById(value).value = "" ;
		MyAlert(str) ;
		return false ;
	}
	
	return true ;
}
//表单提交前的验证：
function checkForm(url){
	if(!chkCon()) return false ;
		
	document.getElementById("PART_PRICE").value = document.getElementById("FREE").value ;

	submitForm('fm') == true ? MyConfirm("确认操作？", Add, [url]) : "";
}
//表单提交方法：
function Add(url){
	document.getElementById("btnAdd").disabled = true ;
	makeNomalFormCall(url,addBack,'fm','');
}
//回调方法：
function addBack(json) {
	if(json.error != null && json.error.length > 0){
		document.getElementById("btnAdd").disabled = false ;
		MyAlert("车型组代码:["+json.error+"]系统已存在，请重新输入");
	}else if(json.success != null && json.success=='true'){
		//document.getElementById("btnAdd").disabled = false ;
		MyAlert("新增成功!");
		
		__parent().__extQuery__(1);
		_hide();
	}else{
		document.getElementById("btnAdd").disabled = false ;
		MyAlert("新增失败！请联系管理员");
	}
}
//页面跳转
<%-- function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelInit.do';
}  --%>
</script>
</body>
</html>
