<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务站信封打印信息</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;索赔基础数据&gt;服务站信封打印</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">所属处：</td>
      	<td>
      	<input name="dept" type="text" id="dept" maxlength="20"  class="middle_txt"/><span style="color: red">* 如：结算室/技术服务处</span>
    	</td>
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">备注：</td>
      	<td>
      	<textarea rows="3" cols="40" name="remark"   id="remark"></textarea><span style="color: red">*</span>
      	</td>
    </tr>
    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="确定"  class="normal_btn" onClick="sureReturn();" >
    	</td>
    </tr>
</table>

<script type="text/javascript">
	function sureReturn(){
		var dept = $('dept').value;
		var remark = $('remark').value;	
		var str = "";
		if(dept=="" || remark==""  ){
		MyAlert("带*号必填!");
		return;
		}else {
		str = dept+"$"+remark+"$"+window.dialogArguments;
		}
		window.returnValue=str;
		window.close();
	}
</script>
</form>
</body>
</html>