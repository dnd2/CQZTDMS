<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>服务活动选择</title>
	<script type="text/javascript">
function technology(){
	var flag;
	var obj = document.getElementsByName("hasCarCustomer");
	    for(var i=0; i<obj.length; i ++){
	        if(obj[i].checked){
	            flag=obj[i].value;
	        }
	    }
	if(parent.$('inIframe'))
	{
		__parent().getActivityInfo(flag);
		_hide();	
	}else{
		__parent().getActivityInfo(flag);
		parent._hide();	
	}
	
}
</script>
</head>
<body >
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp; 当前位置： 服务活动类型选择</div>
	<form method="post" name = "fm" id="fm">
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>服务活动类型选择</h2>
			<div class="form-body">
		<table  class="table_query">
		<input type="hidden" id="cpId" name="cpId" value="">
		<tr id="hasCarCustomer">
				<td colspan="8" style="text-align: center">
					<label class="u-label"><input type="radio" id="hasCarCustomer" name="hasCarCustomer"  value="0" onclick="technology();"/>技术升级</label>
					&nbsp;&nbsp;
					<label class="u-label"><input type="radio" id="hasCarCustomer" name="hasCarCustomer"  value="1" onclick="technology();"/>送保养</label>
					&nbsp;&nbsp;
					<label class="u-label"><input type="radio" id="hasCarCustomer" name="hasCarCustomer"  value="2" onclick="technology();"/>送检测</label>
				</td>
	    </tr>
			<tr>
				<td colspan="8" style="text-align: center">
					<input class="u-button u-submit"  type="button" onclick="_hide();" value="关 闭" /></td>
        		</td>
			</tr>
		</table>
		</div>
		</div>
	</form>
	</div>
</body>
</html>