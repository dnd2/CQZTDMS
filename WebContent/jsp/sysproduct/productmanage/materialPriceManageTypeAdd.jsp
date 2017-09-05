<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料价格维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">价格代码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="priceCode" datatype="0,is_null,30" id="priceCode" type="text" class="middle_txt" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">价格描述：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="priceDesc" datatype="0,is_null,400" id="priceDesc" type="text" class="middle_txt" />
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">生效日期：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input class="short_txt"  type="text" id="startDate" name="startDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">失效日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input class="short_txt"  type="text" id="endDate" name="endDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
		    </td>
	  	</tr>
	  	<tr>
		    <td colspan="4" align="center">
		      <input name="button2" type="button" class="normal_btn" onclick="confirmAdd();" value="保存"/>
		      <input name="button" type="button" class="normal_btn" onclick="history.back();" value="返回"/>
		    </td>
	  	</tr>
	</table>
</form>
<script type="text/javascript">
	//初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	
	function confirmAdd(){
		if(submitForm('fm')){
			addSave();
		}
	}
	
	function addSave(){
		makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeAdd.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeQuery.do';
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
