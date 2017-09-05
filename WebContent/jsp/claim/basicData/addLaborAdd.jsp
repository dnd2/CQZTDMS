<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="W_ID" id="W_ID" value="<%=request.getAttribute("W_ID")%>"/>
<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=request.getAttribute("WRGROUP_ID")%>"/>
<table class="table_add">
 <tr>
 	<td class="table_add_2Col_label_6Letter">附加工时代码：</td>
 	<td><input type="text" name="ADD_LABOUR_CODE" id="ADD_LABOUR_CODE" class="middle_txt" datatype="0,is_null,9"/></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_6Letter">附加工时名称：</td>
 	<td><input type="text" name="ADD_CN_DES" id="ADD_CN_DES" class="middle_txt" datatype="0,is_null,100"/></td>
 </tr> 
 <tr>
 	<td class="table_add_2Col_label_6Letter">工时系数：</td>
 	<td><input type="text" name="ADD_LABOUR_QUOTIETY" id="ADD_LABOUR_QUOTIETY" class="middle_txt" datatype=0,is_double,7/></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_6Letter">索赔工时：</td>
 	<td><input type="text" name="ADD_LABOUR_HOUR" id="ADD_LABOUR_HOUR" class="middle_txt" datatype=0,is_double,7/></td>
 </tr>
</table>

<table class="table_edit" >
 <tr>
 	<td colspan="2" align="center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="checkFormUpdate();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="JavaScript:history.back();" value="返回"/>
 	</td>
 </tr>
</table>

</form>
<script>
function checkFormUpdate(){
	if(!submitForm('fm')) {
		return false;
	}			
	MyConfirm("是否确认新增?",checkForm);
}

//表单提交方法：
function checkForm(){
		makeFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/additionalLaborAdd.json',showResult,'fm');			
}
//回调方法：
function showResult(json) {
	if(json.error != null && json.error.length > 0){
		MyAlert("索赔工时代码:["+json.error+"]系统已存在，请重新输入");
	}else if(json.success != null && json.success=='true'){
		disableBtn($("commitBtn"));
		var val = document.getElementById("W_ID").value;
		window.location.href = "<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do?ID="+val;
	}else{
		MyAlert("新增失败！请联系管理员");
	}
}
</script>
</body>
</html>
