<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质损区域维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
售后服务管理&gt;索赔预授权&gt;质损区域维护</div>
<form name='fm' id='fm'>
<table class="table_edit">
	<tr>
		<td style="color: #252525; width: 115px; text-align: right">质损大类代码：
		</td>
		<td>
		<c:if test="${flag==1}">
		${code}
		<input type="hidden" name="QUD_CODE"  class="middle_txt" value="${code }"/>
		<input type="hidden" name="ID" id="ID" value='<%=request.getAttribute("ID")==null?"":request.getAttribute("ID")%>' />
		</c:if>
		<c:if test="${flag==0}">
		<input type="text" name="QUD_CODE" id="QUD_CODE" maxlength="25"  class="middle_txt" value=""/>
		</c:if>
		</td>
		
		<td style="color: #252525; width: 115px; text-align: right">质损大类名称：
		</td>
		<td>
		<input type="text" name="QUD_NAME" id="QUD_NAME" maxlength="25"  class="middle_txt" value="${name }"/>
		<input type="hidden" name="flag" id="flag" value="${flag }"/>
		</td>
	</tr>
	<tr>
		<td style="color: #252525; width: 115px; text-align: right">质损小类代码：
		</td>
		<td>
		<c:if test="${flag==1}">
		${sonCode}
		<input type="hidden" name="QUD_SON_CODE"  class="middle_txt" value="${sonCode }"/>
		</c:if>
		<c:if test="${flag==0}">
		<input type="text" name="QUD_SON_CODE" maxlength="25"  id="QUD_SON_CODE" onblur="checkCode();" class="middle_txt" value=""/>
		</c:if>
		</td>
		
		<td style="color: #252525; width: 115px; text-align: right">质损小类名称：
		</td>
		<td>
		<input type="text" name="QUD_SON_NAME" maxlength="25"  id="QUD_SON_NAME"  class="middle_txt" value="${sonName }"/>
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
			makeFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/qudUpdate.json',showResult,'fm');	
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("保存成功！");
			window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/qualityDamage.do";
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
	
	
	//表单提交前的验证：
	function checkFormUpdate(){
		var code  = document.getElementById("QUD_CODE").value;
		var name  = document.getElementById("QUD_NAME").value;
		if(code==null||code==""){
		MyAlert("请填写质损区域代码!");
		return false;
		}
		if(name==null||name==""){
		MyAlert("请填写质损区域名称!");
		return false;
		}
		if(!checkType){
		MyAlert("请重新填写质损代码!");
		return false;
		}
		MyConfirm("是否确认保存？",checkForm,[]);
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/qualityDamage.do";
	}	
	
	function checkCode(){
	var code = document.getElementById("QUD_SON_CODE").value;
	if(code!=""){
		makeFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/qudCheck.json?code='+code,showResults,'fm');	
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
</script>
</body>
</html>