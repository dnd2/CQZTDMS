<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>新增代码</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;下发代码维护</div>
<form name='fm' id='fm'>
		<input name="METHOD_TYPE" type="hidden"/>
		  <table class="table_add">
			    <tr>
			     <td class="table_add_3Col_label_4Letter">类别：</td>
			     <td> 
					<script type="text/javascript">
		              genSelBoxExp("TYPE_CODE",<%=Constant.BUSINESS_CHNG_CODE%>,"",false,"min_sel","","false",'');
		            </script>
			      </td>
			      <td class="table_add_3Col_label_4Letter">代码：</td>
			      <td> 
			        <input id="CODE" name="CODE" type="text" class="short_txt" datatype="0,is_null,20"/>
			      </td>
			      <td  class="table_add_3Col_label_4Letter">描述：</td>
			      <td> 
			        <input id="CODE_NAME" name="CODE_NAME" type="text" class="middle_txt" datatype="0,is_null,150"/>
			      </td>
			    </tr>
			<tr> 
		     	 <td colspan="6" align="center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="取消" onclick="JavaScript:history.back()"/>
		        </td>
		    </tr>
		  </table>
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
	makeNomalFormCall('<%=contextPath%>/claim/basicData/DownloadcodeMain/downloadCodeAdd.json',addBack,'fm','');
}
//新增后的回调方法：
function addBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("代码："+json.returnValue+"系统已存在！");
	    }else {
	    	disableBtn($("commitBtn"));
	    	MyAlertForFun("新增成功 !",sendPage);
	    }
	} else {
		MyAlert("新增失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/DownloadcodeMain/downloadcodeInit.do';
} 
</script>
</body>
</html>