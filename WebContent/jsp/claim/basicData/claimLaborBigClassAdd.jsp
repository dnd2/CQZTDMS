<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时大类维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时大类维护</div>

<form name='fm' id='fm'>
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
<input type="hidden" name="W_ID" id="W_ID" value="<%=request.getAttribute("W_ID")%>"/>
<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=request.getAttribute("WRGROUP_ID")%>"/>
<input type="hidden" name="P_ID" id="P_ID"/>
<table class="table_query">
 <tr>
 	<td style="text-align:right">工时大类代码：</td>
 	<td><input type="text" name="CODE" id="CODE" class="middle_txt" datatype="0,is_null,20"/></td>
 </tr>
 <tr>
 	<td style="text-align:right">工时大类名称：</td>
 	<td><input type="text" name="CN_DES" id="CN_DES" class="middle_txt" datatype="0,is_null,100"/></td>
 </tr> 
 <tr>
 	<td style="text-align:right">上级大类：</td>
 	<td>
 		<input type="text" name="P_LABOUR_CODE" id="P_LABOUR_CODE" readonly="readonly" onclick="selPaterClass();" class="middle_txt" datatype=1,is_null,20/>
 		<!-- <input type="button" onclick="selPaterClass();" class="mini_btn" value="..."/> -->
 	</td>
 </tr>
 <tr>
 	<td style="text-align:right">备注：</td>
 	<td><input type="text" name="REMARK" id="REMARK" class="middle_txt" datatype=1,is_null,100/></td>
 </tr>
</table>

<table class="table_query" >
 <tr>
 	<td colspan="2" style="text-align:center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="checkFormUpdate();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="reBack();" value="返回"/>
 	</td>
 </tr>
</table>
</div>
</div>
</form>
</div>

<script>
//父类选择页面：
function selPaterClass(){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/laborPaterClassQueryInit.do',600,400);
}
//上级大类赋值：
function setPaterClass(paterid,paterCode){
	document.getElementById("P_LABOUR_CODE").value = paterCode;
	document.getElementById("P_ID").value = paterid;
}
//返回方法：
function reBack(){
  location="<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassInit.do";   
}
function checkFormUpdate(){
	if(!submitForm('fm')) {
		return false;
	}			
	MyConfirm("是否确认新增?",checkForm);
}

//表单提交方法：
function checkForm(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassAdd.json',showResult,'fm');			
}
//回调方法：
function showResult(json) {
	if(json.error != null && json.error.length > 0){
		MyAlert("工时大类代码:["+json.error+"]系统已存在，请重新输入！");
	}else if(json.success != null && json.success=='true'){
	MyAlert("新增成功！");
		//disableBtn($("commitBtn"));
		window.location.href = "<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassInit.do";
	}else{
		MyAlert("新增失败！请联系管理员");
	}
}
</script>
</body>
</html>
