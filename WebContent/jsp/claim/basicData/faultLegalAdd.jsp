<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>故障法定名称维护新增</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;严重安全性能故障法定名称维护&gt;故障法定名称维护新增</div>
<form method="post" name='fm' id='fm'>
		<input name="METHOD_TYPE" type="hidden"/>
		<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
		  <table class="table_query">
		  <tr>
            <td style="text-align:right">故障法定代码：</td>
            <td>
                 <input name="legalCode" id="legalCode" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">故障法定名称：</td>
            <td>
				<input name="legalName" id="legalName" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">故障模式代码：</td>
            <td>
            	<input type="text" class="middle_txt" id="failureCode" name="failureCode"  datatype="0,is_null,30" id="PART_CODE" onClick="javascript:selectMainPartCode(this)" readonly/>
            	<!-- <span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span> -->
            </td>
            <td style="text-align:right">故障模式名称：</td>
            <td>
				<input name="failureName" id="failureName" datatype="0,is_null,30"  type="text" class="middle_txt" readonly/>
            </td>
          </tr>
			<tr> 
		     	 <td colspan="4" style="text-align:center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="onBack();"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
<script>
//选择故障模式
function selectMainPartCode(obj){
	myobj = getRowObj(obj); 
	OpenHtmlWindow('<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/selectFailureForward.do',800,500);

}
//得到行对象
function getRowObj(obj){
	var i = 0;
    while(obj.tagName.toLowerCase() != "tr"){
    obj = obj.parentNode;
    if(obj.tagName.toLowerCase() == "table")
        return null;
    }
    return obj;
}
//得到故障模式然后可以继续选择修改
function setMainPartCode(failureCode,failureName,flagCheck) {
	cloMainPart=1 ;
	myobj.cells.item(1).innerHTML='<input type="text" datatype="0,is_null,30" class="middle_txt" name="failureCode" value="'+failureCode+'" size="10" onClick="javascript:selectMainPartCode(this)" id="failureCode" readonly/>';
	myobj.cells.item(3).innerHTML='<input type="text" datatype="0,is_null,30" class="middle_txt" name="failureName" value="'+failureName+'" id="failureName" readonly/>';
	parent._hide();
}


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
	makeNomalFormCall('<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalAdd.json',addBack,'fm','');
}
//新增后的回调方法：
function addBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("故障法定代码："+json.returnValue+"系统已存在！");
	    }else {
	    	document.getElementById("commitBtn").disabled = true ;
	    	MyAlert("新增成功 !");
	    	sendPage() ;
	    }
	} else {
		MyAlert("新增失败！联系DCS系统运维团队，问题提报热线:023-67543333！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalInit.do';
}
//返回
function onBack(){
    location="<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalInit.do";   
}
</script>
</div>
</body>
</html>