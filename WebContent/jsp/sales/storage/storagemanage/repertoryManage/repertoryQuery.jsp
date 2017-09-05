<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.component.dict.CodeDict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>

<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
	String expStatusCode = Constant.ORDER_STATUS_01 + "," + Constant.ORDER_STATUS_03 + "," + Constant.ORDER_STATUS_13;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车厂库存查询</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/sales/ordermanage/extractionofvehicle/js/order_material.js"></script>
<script type="text/javascript">

	var myPage;
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RepertoryManage/queryResourceAmount.json?queryType=1";
	var title = null;
	
	var columns = [
				{header: "仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},  
	       		//{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
	       		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
	       		{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
	       		{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
	       		{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
	       		{header: "可用库存", dataIndex: 'RES_AMOUNT', align:'center'},
	       		//{header: "已分配库存", dataIndex: 'INVO_AMOUNT', align:'center'},
	       		{header: "锁定库存", dataIndex: 'KEEP_AMOUNT', align:'center'},
	       		//{header: "预留库存", dataIndex: 'RESERVE_AMOUNT', align:'center'},
	       		{header: "借出库存", dataIndex: 'BORROW_AMOUNT', align:'center'},
	       		{header: "质损库存", dataIndex: 'MATTER_AMOUNT', align:'center'},
	       		//{header: "总库存", dataIndex: 'TAL_AMOUNT', align:'center'},
	       		//{header: "实际库存", dataIndex: 'SJ_AMOUNT', align:'center'}
	       		{header: "实物库存", dataIndex: 'STOCK_AMOUNT', align:'center'}
	       	];
	
	function doInit() {
		
	}
	
	function clrTxt(arg){
		document.getElementById(arg).value = "";
	}
	function exportExcel(){
		fm.action= "<%=contextPath%>/sales/storage/storagemanage/RepertoryManage/queryResourceAmount.json";
	    fm.submit();
	}
</script>
</head>
<body onunload="destoryPrototype()">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>车厂库存查询</div>
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>车厂库存查询</h2>
<div class="form-body">
	<form method="post" name="fm" id="fm">
		<input type="hidden" name="orderId" id="orderId" />
		<table class="table_query">
			<tr>
				<td>
					选择车系：
				</td>
				<td>
					<input size=15 name="groupCode" id="groupCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
			        <input class="mini_btn" onclick="showMaterialGroup('groupCode' ,'' ,'true' ,'2', 'false')" value="..." type=button id=button1>
			        <input class="normal_btn" onclick="clrTxt('groupCode');" value=清空 type="button">
				</td>
				<td>
					选择车型：
				</td>
				<td>
					<input size=15 name="modelCode" id="modelCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
			        <input class="mini_btn" onclick="showMaterialGroup('modelCode' ,'' ,'true' ,'3', 'false')" value="..." type=button id=button1 >
			        <input class="normal_btn" onclick="clrTxt('modelCode');" value=清空 type="button">
				</td>
				<td>
					选择配置：
				</td>
				<td>
					<input size=15 name="packageCode" id="packageCode" type="text" maxlength="20"  class="middle_txt" style="width:80px;">
			        <input class="mini_btn" onclick="showMaterialGroup('packageCode' ,'' ,'true' ,'4', 'false')" value="..." type=button id=button1 >
			        <input class="normal_btn" onclick="clrTxt('packageCode');" value=清空 type="button">
				</td>
			</tr>	
			<tr>	
				<td>
					选择物料： 
				</td>
				<td>
					<input size=15 type="text" maxlength="20"  id="materialCode" name="materialCode" class="middle_txt" style="width:80px;">
	          		<input id="button2" class="mini_btn" onclick="showMaterial('materialCode' ,'' ,'true' ,null, 'false');" value="…" type=button >
	          		<input class="normal_btn" onclick="clrTxt('materialCode');" value=清空 type=button>
				</td>
				<td>颜色：</td>
				<td>
					<input type="text" maxlength="20"  class="middle_txt" name="colorName" />
				</td>
				<td>仓库：</td>
				<td>
				<select name="YIELDLY" id="YIELDLY" class="u-select" >
			 		<option value="">--请选择--</option>
					<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
	  			</select>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="left"><font color="red">总库存：运单确认前的库存(包含车辆出库至运单确认之间的数量) </font></td>
			</tr>
			<tr>
				<td colspan="6" align="left"><font color="red">实际库存：车辆出库前的库存(不包含车辆出库后的库存) </font></td>
			</tr>
			<tr>
				<td colspan="6" align="left"><font color="red">总库存=可分配库存+已分配库存+已锁定库存+预留库存+待处理库存+借出库存 </font></td>
			</tr>
			<tr>
				<td colspan="6" class="table_query_4Col_input" style="text-align: center">
					<input class="u-button u-query" type="button" value="查询" id="queryBtn" onclick="__extQuery__(1);">
					<input type="button" value="导出"  class="normal_btn" onClick="exportExcel()" />
				</td>
			</tr>
		</table>
		</div>
	</div>
		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>
</body>	
</html>