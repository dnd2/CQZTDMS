<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrLabourPricePO"%>
<%@page import="com.infodms.dms.util.StringUtil"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="success();">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;未维护基础数据的服务站</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
 		<td width="10%" align="right">经销商代码：</td>
 		<td width="20%"><input type="text" class="middle_txt" name="dealer_code"/></td>
		<td width="10%" align="right">经销商名称：</td>
		<td width="20%"><input type="text" class="middle_txt" name="dealer_name"/></td>
	</tr>
	<tr>
 		<td width="10%" align="right">基础数据类型：</td>
 		<td width="40%" colspan="3">
				<select name="dealerType" value="" id="dealerType" class="short_sel">
				<option value=""> --请选择--</option>
				<option value="1">工时单价</option>
				<option value="2">配件加价率</option>
				</select><span style="color: red">*</span>
		</td>
		
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="查询" onclick="mainQuery();"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/noBaseDataQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',width:'8%',align:'center',renderer:getIndex},
	               {header:'经销商代码',width:'12%',align:'center',dataIndex:'DEALER_CODE'},
	               {header:'经销商名称',width:'14%',align:'center',dataIndex:'DEALER_NAME'}
	               
		           	];
	function mainQuery(){
	var type=$('dealerType').value;
	if(type==""){
	MyAlert("请选择基础数据类型!");
	return;
	}else{
		__extQuery__(1);
		}
	}
</script>
</body>
</html>
