<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>关注件预授权维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
售后服务管理&gt;索赔预授权&gt;故障代码维护</div>
<form name='fm' id='fm'>
<table class="table_edit">
	<tr>
		<td style="color: #252525; width: 115px; text-align: right">故障代码：</td>
		<td>
		<c:if test="${flag==1}">
		${code}
		<input type="hidden" name="MAL_CODE" maxlength="25"   class="middle_txt" value="${code }"/>
		<input type="hidden" name="ID" id="ID" value='<%=request.getAttribute("ID")==null?"":request.getAttribute("ID")%>' />
		</c:if>
		<c:if test="${flag==0}">
		<input type="text" name="MAL_CODE" id="MAL_CODE" maxlength="25"  onblur="checkCode();" class="middle_txt" value=""/>
		</c:if>
		</td>
		<td style="color: #252525; width: 115px; text-align: right">故障名称：
		</td>
		<td>
		<input type="text" name="MAL_NAME" id="MAL_NAME"maxlength="25"   class="middle_txt" value="${name }"/>
		<input type="hidden" name="flag" id="flag" value="${flag }"/>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="center"><input name="ok" type="button"
			class="normal_btn" id="commitBtn" value="确定"
			onclick="checkFormUpdate();" /> <input name="back" type="button"
			class="normal_btn" value="返回" onclick="sendPage();" /></td>
	</tr>
</table>
</form>
<script>
   var checkType=true;
	//表单提交方法：
	function checkForm(){
			disableBtn($("commitBtn"));
			makeFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/malFunctionUpdate.json',showResult,'fm');	
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("保存成功！");
			window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/malFunction.do";
		}else{
			MyAlert("保存失败，请联系管理员！");
		}
	}
	
	
	//表单提交前的验证：
	function checkFormUpdate(){
		var code  = document.getElementById("MAL_CODE").value;
		var name  = document.getElementById("MAL_NAME").value;
		if(code==null||code==""){
		MyAlert("请填写故障代码!");
		return false;
		}
		if(name==null||name==""){
		MyAlert("请填写故障名称!");
		return false;
		}
		if(!checkType){
		MyAlert("请重新填写故障代码!");
		return false;
		}
		MyConfirm("是否确认保存？",checkForm,[]);
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/malFunction.do";
	}	
	
	function checkCode(){
	var code = document.getElementById("MAL_CODE").value;
	if(code!=""){
		makeFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/malFunctionCheck.json?code='+code,showResults,'fm');	
		}
	}
	function showResults(json){
		if(json.success != null && json.success == "true"){
			checkType = false;
			MyAlert("该代码已经存在,请重新输入！");
		}else{
		checkType = true;
		}
	}
function changeEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/changequd.json?code='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeBack,'fm','');
			}else{
				resetSelectOption('qudId',null,null,null,null,null);
			}
		}
function changeBack(json) {
		
		//根据ID获得select元素
	var mySelect = document.getElementsByName("qudId");
	for(var j=0; j<mySelect.length;j++){
		mySelect[j].options.length=0;
	}
	for(var i=0;i<json.regionList.size();i++){
		var varItem = new Option(json.regionList[i].QUD_SON_NAME,json.regionList[i].QUD_ID+"");
		mySelect[0].options.add(varItem);
	}
	}
</script>
</body>
</html>