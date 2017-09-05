<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包凭证补办生产者信息填写
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr >
		<td class="table_query_2Col_label_5Letter">名称：</td>
      	<td>
      	<textarea rows="3" cols="40" name="name"   id="name"></textarea><span style="color: red">*</span>
    </tr>
  <tr >
		<td class="table_query_2Col_label_5Letter">邮编：</td>
      	<td><input name="fax" type="text" id="fax" maxlength="6"  class="middle_txt"/><span style="color: red">*</span></td>
    </tr>
    <tr >
		<td class="table_query_2Col_label_5Letter">地址：</td>
      	<td>
      	<textarea rows="3" cols="40" name="address"   id="address"></textarea><span style="color: red">*</span>
      	</td>
    </tr>
    <tr >
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="确定"  class="normal_btn" onClick="sureReturn();" >
    			</td>
    </tr>
</table>

<script type="text/javascript">
	function sureReturn(){
		var reg = /^\d+$/;
		var name = $('name').value;
		var fax = $('fax').value;
		var address = $('address').value;	
		var str = "";
		if(name=="" || fax=="" || address==""  ){
		MyAlert("带*号必填!");
		return;
		}else if(!reg.test(fax)){
			MyAlert("邮编请输入数字!");
			return false;
		}
		else {
		str = name+"$"+fax+"$"+address+"$"+window.dialogArguments;
		}
		window.returnValue=str;
		window.close();
	}
</script>
</form>
</body>
</html>