<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>入库差异查询</title>
<%
	String contextPath = request.getContextPath();
%>

</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>入库差异查询</div>
<form method="POST" name="fm" id="fm">
<div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>入库差异查询</h2>
		<div class="form-body">
	<table class="table_query" width="85%" align="center" border="0"  id="roll">
		<tr>
				<td class="right" width="15%">导入日期：</td>
				<td>
					<input class="short_txt" readonly="readonly"  type="text" id="startdate" name="startdate" onFocus="WdatePicker({el:$dp.$('startdate'), maxDate:'#F{$dp.$D(\'endDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
					<input class="short_txt" readonly="readonly"  type="text" id="endDate" name="endDate" onFocus="WdatePicker({el:$dp.$('endDate'), minDate:'#F{$dp.$D(\'startdate\')}'})"  style="cursor: pointer;width: 80px;"/>
					<font color="red">默认查询当前日期</font>
				</td>
				<td class="right" width="15%"></td>
				<td></td>
		</tr>
		<tr align="center" >
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
		    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />
		    	  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/> 
		    	  <input type="button" id="queryBtn1" class="normal_btn"  value="导出"  onclick="exportResult();" /> 
		    </td>
	  	</tr>
	</table>
	</div>
</div>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importQuery.json";
	var title = null;

	var columns;

	columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "导入类型", dataIndex: 'IMP_TYPE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "导入用户", dataIndex: 'NAME', align:'center'},
				{header: "导入时间", dataIndex: 'CREATE_DATE', align:'center'},
				//{header: "入库类型", dataIndex: 'IN_TYPE', align:'center'},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色代码", dataIndex: 'COLOR_CODE', align:'center'},
				{header: "颜色名称", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "合格证号", dataIndex: 'HEGEZHENG_CODE', align:'center'},
				{header: "变速箱号", dataIndex: 'GEARBOX_NO', align:'center'},
				{header: "下线日期", dataIndex: 'OFFLINE_DATE', align:'center'}
		      ];

	function doInit(){
		//setDateNow();
		__extQuery__(1);
	}
	function setDateNow(){//设置当前日期
		var date = new Date();
	    var seperator1 = "-";
	    var year = date.getFullYear();
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
		document.getElementById("startdate").value=year + seperator1 + month + seperator1 + strDate;
		document.getElementById("endDate").value=year + seperator1 + month + seperator1 + strDate;
	}
	//导出
	function exportResult(){
		fm.action = '<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importQuery.json?_type=2' ;
		fm.submit();
	}
</script>
</body>
</html>
