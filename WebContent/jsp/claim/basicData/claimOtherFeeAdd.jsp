<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔其它费用维护</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据管理&gt;索赔外出维修费用新增</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
		  <table class="table_query">
			    <tr >
			     <td class="right">项目代码：</td>
			     <td><input id="FEE_CODE" name="FEE_CODE" type="text" class="middle_txt" datatype="0,is_null,30"/></td> 
			    </tr> 
			    <tr>
			      <td  class="right">项目名称：</td>
			      <td><input id="FEE_NAME" name="FEE_NAME" type="text" class="middle_txt" datatype="0,is_null,30"/></td> 
			    </tr> 		
			 <tr> 
		     	 <td colspan="4" class="center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="取消" onclick="JavaScript:history.back()"/>
		        </td>
		    </tr>
		  </table>
	</form>
<script>
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? otherfeeAdd() : "";
}
//表单提交方法：
function otherfeeAdd(){
	MyConfirm("是否确认新增？",addotherfee);
}
function addotherfee(){
	var url = '<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeAdd.json' ;
	makeNomalFormCall(url,addBack,'fm');
}

//删除回调方法：
function addBack(json) {
	if(json.feeCode != null) {
		MyAlert("项目代码：【"+json.feeCode+"】系统已存在，请重新输入！");
	}else if(json.success != null && json.success=='true'){
		/* disableBtn($("commitBtn")); */
		document.getElementById("commitBtn").disabled = true ;
		MyAlert("新增成功");
		sendPage() ;
	}else{
		MyAlert("新增失败！请联系管理员");
	}
}
//页面跳转
function sendPage(){
	__parent().__extQuery__(1) ;
	_hide() ;
	<%-- window.location = '<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeInit.do'; --%>
} 
</script>
</div>
</body>
</html>