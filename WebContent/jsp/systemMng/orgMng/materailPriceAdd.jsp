<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){

	}

	function doSave(){
		var priceCode = document.getElementById("priceCode").value;
		if(priceCode==null||priceCode==''){
			alert("价格代码未填");
			return;
		}
		
		var pirceName = document.getElementById("pirceName").value;
		if(pirceName==null||pirceName==''){
			alert("价格描述");
			return;
		}
		
		var startDate = document.getElementById("startDate").value;
		if(startDate==null||startDate==''){
			alert("生效开始时间未填");
			return;
		}
		
		var endDate = document.getElementById("endDate").value;
		if(endDate==null||endDate==''){
			alert("生效结束时间未填");
			return;
		}
				
		MyConfirm("是否保存?",doSavetAction);
	}
	
	function doSavetAction(){				
		var url = "<%=contextPath%>/sysmng/orgmng/MaterailPriceMng/materailPriceSave.json";
		sendAjax(url,doSaveBack,'fm');
	}
	
	function doSaveBack(json){
		history.back();
	}
	
</script>

<title>物料价格管理</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doInit();">
<div class="wbox"  id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 物料价格管理</div>
<form id="fm" name="fm" method="post">

<div id="customerInfoId">

<table class="table_query table_list" class="center" id="ctm_table_id_2">
	<tr>
		<td class="right">价格代码：</td>
		<td class="left"><input id="priceCode" name="priceCode" value="" type="text" class="middle_txt" size="20" 
			 /><font color="red">*</font></td>			
		<td class="right">价格描述：</td>
		<td class="left"><input id="pirceName" value="" name="pirceName" type="text" class="middle_txt" size="20" 
			 /><font color="red">*</font></td>			
	</tr>
	
	<tr>
		<td class="right">生效时间：</td>
		<td class="left">
			 &nbsp;从&nbsp;
			 <input name="startDate"  id="startDate" value="${date }" type="text" class="middle_txt"  style="width:100px" onFocus="WdatePicker({el:$dp.$('startDate'), maxDate:'#F{$dp.$D(\'endDate\')}'})" />
		     &nbsp;至&nbsp; <input name="endDate"  id="endDate" value="${date }" type="text" class="middle_txt"  style="width:100px" onFocus="WdatePicker({el:$dp.$('endDate'), minDate:'#F{$dp.$D(\'startDate\')}'})" />		      			
			<font color="red">*</font> </td>			 
	
	</tr>
		
</table>
</div>


<table class="table_query table_list" id="submitTable">
	<tr >
		<td class="center">
			<input type="button" id="re_Id" value="保存" class="u-button u-submit" onclick="doSave();" /> 
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="history.back();" /> 
		</td>
	</tr>
</table>


</form>
</div>
</body>
</html>