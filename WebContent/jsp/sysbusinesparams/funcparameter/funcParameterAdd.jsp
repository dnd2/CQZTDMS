<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();   
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>模块参数新增</title>
</head>
<body  onload="__extQuery__(1)">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>系统管理>系统业务参数维护>模块参数维护</div>
<form method="post" name="fm" id="fm">

<table class="table_query">
    <tr>
	    <td align="right">参数名：</td>
        <td><input type="text" class="middle_txt" id="paraName" name="paraName"  value="" jset="para"/></td>
	    <td align="right">控件ID：</td>
        <td><input type="text" class="middle_txt" id="elementId" name="elementId"  value="" jset="para"/></td>       
     </tr>
</table>

<table class="table_edit">
	<tr>
 		<td colspan="2" align="center">
 			<input class="normal_btn" type="button" value="保存" onclick="save();"/> 
 			&nbsp;&nbsp; 			
 			<input class="normal_btn" type="button" value="返回" onclick="back();" />
 		</td>
	</tr>
</table>   
</form>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	
	function save(){
		var url = "<%=contextPath%>/sysbusinesparams/funcparameter/FuncParameterManager/saveParameter.json";
		makeNomalFormCall(url,function(json){
				MyAlertForFun('新增成功!',back);									
		},'fm');
	}

	function back(){
		fm.action="<%=contextPath%>/sysbusinesparams/funcparameter/FuncParameterManager/funcParameterManageInit.do";
		fm.submit();
	}
	
</script>
<!--页面列表 end -->
</body>
</html>
