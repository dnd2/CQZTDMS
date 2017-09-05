<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%
String contextPath = request.getContextPath();
HashMap hm = (HashMap)request.getAttribute("SELMAP"); //对应的待修改的索赔工时大类
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
<input type="hidden" name="ID" id="ID" value="<%=hm.get("ID")==null?"":hm.get("ID").toString()%>"/>
<input type="hidden" name="P_ID" id="P_ID" value="<%=hm.get("PATER_ID")==null?"":hm.get("PATER_ID").toString()%>"/>
<table class="table_query">
 <tr>
 	<td style="text-align:right">工时大类代码：</td>
 	<td>
 	<%=hm.get("LABOUR_CODE")==null?"":hm.get("LABOUR_CODE").toString()%>
<%-- 	<input type="text" name="CODE" id="CODE" value="<%=hm.get("LABOUR_CODE")==null?"":hm.get("LABOUR_CODE").toString()%>" class="middle_txt" datatype="0,is_null,20"/>--%>
 	</td>
 </tr>
 <tr>
 	<td style="text-align:right">工时大类名称：</td>
 	<td><input type="text" name="CN_DES" id="CN_DES" value="<%=hm.get("CN_DES")==null?"":hm.get("CN_DES").toString()%>" class="middle_txt" datatype="0,is_null,100"/></td>
 </tr> 
 <tr>
 	<td style="text-align:right">上级大类：</td>
 	<td>
 		<input type="text" name="P_LABOUR_CODE" id="P_LABOUR_CODE" value="<%=hm.get("P_LABOUR_CODE")==null?"":hm.get("P_LABOUR_CODE").toString()%>" readonly="readonly" class="middle_txt" onclick="selPaterClass();" datatype=1,is_null,20/>
 	</td>
 </tr>
 <tr>
 	<td style="text-align:right">备注：</td>
 	<td><input type="text" name="REMARK" id="REMARK" value="<%=hm.get("REMARK")==null?"":hm.get("REMARK").toString()%>" class="middle_txt" datatype=1,is_null,100/></td>
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
	MyConfirm("是否确认修改?",checkForm);
}

//表单提交方法：
function checkForm(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassUpdate.json',showResult,'fm');			
}
//回调方法：
function showResult(json) {
	if(json.success != null && json.success=='true'){
	MyAlert("修改成功！");
		//disableBtn($("commitBtn"));
		window.location.href = "<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassInit.do";
	}else{
		MyAlert("修改失败！请联系管理员");
	}
}
</script>
</body>
</html>
