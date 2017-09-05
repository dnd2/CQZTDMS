<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
  		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">ERP名称：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<!--<label>${po.erpName}</label>
		    	--><input name="erpName" datatype="1,is_null,300" id="erpName_" type="text" class="middle_txt" disabled="disabled" value="${po.erpName}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap"></td>
		    <td class="table_query_2Col_input" nowrap="nowrap"></td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">物料代码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="materialCode" datatype="0,is_null,30" id="materialCode" type="text" class="middle_txt" value="${po.materialCode}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">物料名称：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="materialName" datatype="0,is_null,300" id="materialName" type="text" class="middle_txt" value="${po.materialName}"/>
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">车型6位码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="modelCode" datatype="1,is_null,30" id="modelCode" type="text" class="middle_txt" value="${po.modelCode}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">内饰代码：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="trimCode" datatype="0,is_null,10" id="trimCode" type="text" class="middle_txt" value="${po.trimCode}"/>
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">颜色代码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="colorCode" datatype="0,is_null,10" id="colorCode" type="text" class="middle_txt" value="${po.colorCode}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">颜色名称：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="colorName" datatype="0,is_null,300" id="colorName" type="text" class="middle_txt" value="${po.colorName}"/>
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">年型：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="modelYear" datatype="1,is_null,4" id="modelYear" type="text" class="middle_txt" value="${po.modelYear}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">上市日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="issueDate" name="issueDate" datatype="0,is_date,10" value="${issueDate}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'issueDate', false);" value="&nbsp;" />
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">生效日期：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="enableDate" name="enableDate" datatype="0,is_date,10" value="${enableDate}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'enableDate', false);" value="&nbsp;" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">失效日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="disableDate" name="disableDate" datatype="0,is_date,10" value="${disableDate}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'disableDate', false);" value="&nbsp;" />
		    </td>
	  	</tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">物料状态：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"${po.status}",false,"short_sel","","false",'');
			    </script>
		    </td>
		    <td class="table_query_2Col_label_8Letter" nowrap="nowrap">可提报补充订单：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("rushOrderFlag",<%=Constant.NASTY_ORDER_REPORT_TYPE%>,"${po.rushOrderFlag}",false,"short_sel","","false",'');
			    </script>
		    </td>
	  	</tr>
	  	<tr>    
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">可提报订单：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("orderFlag",<%=Constant.NASTY_ORDER_REPORT_TYPE%>,"${po.orderFlag}",false,"short_sel","","false",'');
			    </script>
		    </td>
		    <td class="table_query_2Col_label_8Letter" nowrap="nowrap">所属物料组：</td>
		    <td class="table_query_2Col_input" colspan="3" nowrap="nowrap">
		    	<input type="text" class="middle_txt" datatype="0,is_null,30" name="groupCode" id="groupCode" readonly="readonly" value="${gpo.groupCode}"/>
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','4','true')" value="..." />
				<input class="mark_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
		    </td>
	  	</tr>
	  	<tr>
		    <td colspan="4" align="center">
		    	<input name="materialId" type="hidden" value="${po.materialId}"">
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
			MyConfirm("是否确认保存?",addSave);
		}
	}
	
	function addSave(){
		makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageMod.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageQueryPre.do';
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
</script>
</body>
</html>
