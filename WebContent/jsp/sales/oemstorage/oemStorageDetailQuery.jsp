<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车厂库存查询明细</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div id="divHeight">
<form method="post" name="fm" id="fm">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input type="hidden" name="areaId" id="areaId"/>
	<input type="hidden" name="area" id="area"/>
	<input type="hidden" name="warehouseId" id="warehouseId"/>
	<input type="hidden" name="groupCode" id="groupCode"/>
	<input type="hidden" name="materialCode" id="materialCode"/>
	<input type="hidden" name="flag" id="flag"/>
	<input type="hidden" name="custBatch" id="custBatch"/>
	<input type="hidden" name="vin" id="vin"/>
	<input type="hidden" name="days" id="days"/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	//初始化
    function doInit(){
    	var areaId = parent.document.getElementById("areaId").value;
    	var area = parent.document.getElementById("area").value;
    	var warehouseId = parent.document.getElementById("warehouseId").value;
    	var groupCode = parent.document.getElementById("groupCode").value;
    	var materialCode = parent.document.getElementById("materialCode").value;
    	var custBatch = parent.document.getElementById("custBatch").value;
    	var vin = parent.document.getElementById("vin").value;
    	var days = parent.document.getElementById("days").value;
    	document.getElementById("areaId").value = areaId;
    	document.getElementById("area").value = area;
    	document.getElementById("warehouseId").value = warehouseId;
    	document.getElementById("groupCode").value = groupCode;
    	document.getElementById("materialCode").value = materialCode;
    	document.getElementById("custBatch").value = custBatch;
    	document.getElementById("vin").value = vin;
    	document.getElementById("days").value = days;
    	__extQuery__(1);
	}
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageDetailQuery.json";
	var title = null;
	var columns = [
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "订做车批次", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				{id:'action',header: "车辆VIN号", dataIndex: 'VIN', align:'center',renderer:mySelect},
				{header: "所在仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "入库日期", dataIndex: 'STORAGE_DATE', align:'center'},
				{header: "库龄(天)", dataIndex: 'DAYS', align:'center'}
		      ];
	function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vehicleInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,800,600);
	}
</script>
<!--页面列表 end -->
</body>
</html>