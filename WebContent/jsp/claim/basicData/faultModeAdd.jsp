<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>故障新增</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;失效模式新增</div>
<form name='fm' id='fm'>
		<input name="METHOD_TYPE" type="hidden"/>
		<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
		  <table class="table_query">
		  <tr>
            <td style="text-align:right">失效模式代码：</td>
            <td>
                 <input name="modeCode" id="modeCode" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
            </tr>
            <tr>
            <td style="text-align:right">失效模式名称：</td>
            <td>
				<input name="modeName" id="modeName" datatype="0,is_null,300"  type="text" class="middle_txt"/>
            </td>
          </tr>
			<tr> 
		     	 <td colspan="2"  style="text-align:center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide() ;"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
<script>
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? Add() : "";
}
//表单提交方法：
function Add(){
	MyConfirm("是否确认新增?",addDownCode);
}
//新增方法：
function addDownCode(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/FailureMaintainMain/failureModeSave.json',addBack,'fm','');
}
//新增后的回调方法：
function addBack(json) {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("失效模式代码："+json.returnValue+"系统已存在！");
	    }else {
	    	document.getElementById("commitBtn").disabled = true ;
	    	MyAlert("新增成功 !") ;
	    	__parent().__extQuery__(1) ;
	    	_hide() ;
	    	/* MyAlertForFun("新增成功 !",sendPage); */
	    }
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/FailureMaintainMain/FailureModeInit.do';
} 
</script>
</div>
</body>
</html>