<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
<title>库存查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
	<div class="wbox">
		<form id="fm" name="fm" method="post">
		
		<input type="hidden" name="curPage" id="curPage" value="1" /> 
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table style="display: none">
			<tr>
				<td>
				<!-- 父页面：物料组选择 -->	
				<input type="text" id="materialCode" name="materialCode" />
				</td>
			</tr>
			<tr>
				<td>
				<!-- 父页面：库存超过天数 -->
				<input type="text" id="days" name="days" />
				</td>
			</tr>
			<tr>
				<td>
				<!-- 父页面：VIN -->
				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>	
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
<script type="text/javascript">
	<!--得到父页面查询条件-->
	var materialCode = parent.document.getElementById("materialCode").value;
	var days = parent.document.getElementById("days").value;
	var vin = parent.document.getElementById("vin").value;
	document.getElementById("materialCode").value = materialCode;
	document.getElementById("days").value = days;
	document.getElementById("vin").value = vin;

	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/StorageQuery/StorageQueryDLR_Detail.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "位置说明", dataIndex: 'VEHICLE_AREA', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "入库日期", dataIndex: 'STORAGE_TIME', align:'center'},
				{header: "库存天数", dataIndex: 'DAY_COUNT', align:'center'}
		      ];

</script>    
</body>
</html>