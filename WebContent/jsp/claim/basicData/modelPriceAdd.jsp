<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>单据里程修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;二次索赔车型-单价维护</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
<input type="hidden" id="type" class="middle_txt" value="${type }"  name="type"/>
<input type="hidden" id="id" class="middle_txt" value="${id }"  name="id"/>
	<tr>
 		<td width="10%" align="right">车型代码：</td>
 		<td width="20%"  >
 		<c:if test="${type==0}">
 			<input type="text" id="modelCode" class="middle_txt"  name="modelCode"/>
 		</c:if>
 		<c:if test="${type==1}">
 			<input type="text" id="modelCode1" disabled="disabled" class="middle_txt" value="${bean.modelCode }"  name="modelCode1"/>
 			<input type="hidden" id="modelCode"  class="middle_txt" value="${bean.modelCode }"  name="modelCode"/>
 		</c:if>
 		<span style="color:red">*</span></td>
 		<td width="10%" align="right">车型名称：</td>
 		<td width="20%"  >
 		<input type="text" id="modelName" class="middle_txt" value="${bean.modelName }"  name="modelName"/>
 		<span style="color:red">*</span></td>
	</tr>
		<tr>
 		<td width="10%" align="right">工时单价：</td>
 		<td width="20%"  >
 		<input type="text" id="modelPrice" class="middle_txt" onblur="checkData();"  value="${bean.modelPrice }" name="modelPrice"/>
 		<span style="color:red">*</span></td>
 		<td width="10%" align="right">是否有效：</td>
 		<td width="20%"  >
 		<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.IF_TYPE%>,'${bean.status }',false,"short_sel","","false","");
        </script>
 		<span style="color:red">*</span></td>
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="保存" id="saves" onclick="save();"/> 
 			<input class="normal_btn" type="button" value="返回" id="backs" onclick="history.back();"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
function checkData(){
	var price = $('modelPrice').value;
	var reg = /^(\d+\.\d{1,2}|\d+)$/;
	if(price==""){
	MyAlert("请输入工时单价!");
	return false;
	}
	if(!reg.test(price)){
		MyAlert("工时单价为最多2位的小数!");
		$('modelPrice').value="";
		return false;
	}
}
function save(){
	var reg = /^(\d+\.\d{1,2}|\d+)$/;
	var modelCode = $('modelCode').value;
	var modelName = $('modelName').value;
	var modelPrice = $('modelPrice').value;
	if(modelCode==""){
		MyAlert("请输入车型代码!");
		return false;
	}
	if(modelName==""){
		MyAlert("请输入车型名称!");
		return false;
	}
	if(modelPrice==""){
		MyAlert("请输入工时金额!");
		return false;
	}
	if(!reg.test(modelPrice)){
		MyAlert("工时单价为最多2位的小数!");
		$('modelPrice').value="";
		return false;
	}
	MyConfirm("确定保存？",confirmAdd0,[]);
}
function  confirmAdd0(){
	$('saves').disabled=true;
	$('backs').disabled=true;
	url= "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modelPriceSave.json";
		makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
$('saves').disabled=false;
$('backs').disabled=false;
	if(json.ok==""){
		MyAlert("保存成功!");
		fm.action = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modelPricePer.do";
		fm.submit();
	}else{
		MyAlert(json.ok);
		return false;
	}
}

</script>
</body>
</html>
