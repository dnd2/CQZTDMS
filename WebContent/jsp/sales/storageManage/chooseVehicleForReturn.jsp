<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>退车车辆选择</title>
<script type="text/javascript">
function doInit() {
	var aVehicle = parentDocument.getElementsByName("vehicleIds") ;
	var aTypeId = parentDocument.getElementsByName("accountTypeIds") ;
	var aDlrId = parentDocument.getElementsByName("dealerIds") ;
	var oExVeh = document.getElementById("exVeh") ;
	var oTypeName = document.getElementById("typeName") ;
	var oDlrId = document.getElementById("dealerId") ;
	if(aVehicle) {
		var iLen = aVehicle.length ;
		for(var i=0; i<iLen; i++) {
			if(oExVeh.value.length == 0) {
				oExVeh.value = aVehicle[i].value ;
			} else {
				oExVeh.value += "," + aVehicle[i].value ;
			}
		}
		oTypeName.value = aTypeId[0].id ;
		oDlrId.value = aDlrId[0].value ;
	}
}
function chooseVehicle() {
	var typeFlag = "" ;
	var dealerFlag = "" ;
	var chooseFlag = false ;
	var aReturnInfo = document.getElementsByName("vehicleIds") ;
	var iLen = aReturnInfo.length ;
	for(var i=0; i<iLen; i++) {
		if(aReturnInfo[i].checked) {
			if(!dealerFlag) {
				dealerFlag = aReturnInfo[i].value.split("\||")[10] ;
			} else {
				if(dealerFlag != aReturnInfo[i].value.split("\||")[10]) {
					MyAlert("所选择经销商必须一致!") ;
					return false ;
				}
			}
		}
	}
	for(var i=0; i<iLen; i++) {
		if(aReturnInfo[i].checked) {
			chooseFlag = true;
			infoInsert(aReturnInfo[i].value);
		}
	}
	if(!chooseFlag) {
		MyAlert("请选择退车车辆!") ;
		return false ;
	} else {
		// parent.frames["inIframe"].document.getElementById("applyRetrun").style.display = "inline" ;
		_hide() ;
	}
}


function infoInsert(value) {
	var aReturnInfo = value.split("\||") ;
	var modelName = aReturnInfo[0] ;
	var packageName = aReturnInfo[1] ;
    var materialCode = aReturnInfo[2] ;
	var materialName = aReturnInfo[3] ;
	var vin = aReturnInfo[4] ;
	var singlePrice = aReturnInfo[5] == "null"? "": aReturnInfo[5] ;
	var warehouseName = aReturnInfo[6] ;
	var dealerShortName = aReturnInfo[7] ;
	var lifeCycle = aReturnInfo[8] ;
	var storageTime = aReturnInfo[9] ;
	var dealerId = aReturnInfo[10] ;
	var vehicleId = aReturnInfo[11] ;
	var areaId = aReturnInfo[12] ;
	var accountTypeId = aReturnInfo[13] == "null"? "": aReturnInfo[13] ;
	var priceListId=aReturnInfo[13];
	// MyAlert(parentContainer.document.getElementById("showTab"));
	// var oShowTab = parent.frames["inIframe"].document.getElementById("showTab");
	var oShowTab = parentContainer.document.getElementById("showTab"); 
	var iRowLen = oShowTab.rows.length ;
	oShowTab.insertRow(iRowLen);
	oShowTab.rows[iRowLen].setAttribute("id", "tr" + vehicleId) ;
	var oCell2 = oShowTab.rows[iRowLen].insertCell(0) ;
	oCell2.nowrap = "nowrap" ;
	oCell2.innerHTML = materialCode + "<input type=\"hidden\" name=\"dealerIds\" value=\"" + dealerId + "\" >" + "<input type=\"hidden\" name=\"areaIds\" value=\"" + areaId + "\" >";
	var oCell3 = oShowTab.rows[iRowLen].insertCell(1) ;
	oCell3.nowrap = "nowrap" ;
	oCell3.innerHTML = materialName ;
	var oCell4 = oShowTab.rows[iRowLen].insertCell(2) ;
	oCell4.nowrap = "nowrap" ;
	oCell4.innerHTML = vin + "<input type=\"hidden\" name=\"vehicleIds\" value=\"" + vehicleId + "\" >";
	var oCell6 = oShowTab.rows[iRowLen].insertCell(3) ;
	oCell6.nowrap = "nowrap" ;
	oCell6.innerHTML = singlePrice + "<input type=\"hidden\" name=\"singlePrices\" value=\"" + singlePrice + "\" ><input type=\"hidden\" name=\"priceListIds\" value=\"" + priceListId + "\" >" ;
	var oCell7 = oShowTab.rows[iRowLen].insertCell(4) ;
	oCell7.nowrap = "nowrap" ;
	oCell7.innerHTML = warehouseName ;
	var oCell8 = oShowTab.rows[iRowLen].insertCell(5) ;
	oCell8.nowrap = "nowrap" ;
	oCell8.innerHTML = dealerShortName ;
	var oCell9 = oShowTab.rows[iRowLen].insertCell(6) ;
	oCell9.nowrap = "nowrap" ;
	oCell9.innerHTML = getItemValue(lifeCycle) ;
	var oCell10 = oShowTab.rows[iRowLen].insertCell(7) ;
	oCell10.nowrap = "nowrap" ;
	oCell10.innerHTML = storageTime ;
	var oCell11 = oShowTab.rows[iRowLen].insertCell(8) ;
	oCell11.nowrap = "nowrap" ;
	oCell11.innerHTML = "<a href=\"#\" onclick=\"rowDelete(" + vehicleId + ")\">[删除]</a>" ;
}

function myQuery() {
	var sVin = document.getElementById("vin").value ;
	var sDlyNo = document.getElementById("deliveryNo").value ;
	__extQuery__(1) ;
}
</script>
</head>
<body onload="doInit();"> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 退车车辆选择</div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="reason" id="reason"/>
		<table class="table_query">
			<tr>
				<td width="20%" class="tblopt right">VIN：</td>
				<td width="39%">
      				<textarea id="vin" name="vin" cols="20" rows="2" ></textarea>
    			</td>
    			<td  width="20%" class="tblopt right">发运单号：</td>
				<td  width="39%">
					<input type="text" class="middle_txt" id="deliveryNo" name="deliveryNo" value="" />
				</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt right"></td>
				<td width="39%"></td>
				<td width="20%" class="tblopt right"></td>
				<td class="table_query_3Col_input">
					<input type="hidden" name="exVeh" id="exVeh" value="" />
					<input type="hidden" name="typeName" id="typeName" value=""  />
					<input type="hidden" name="dealerId" id="dealerId" value="" />
					<input type="button" class="u-button u-query" onclick="myQuery() ;" value="查 询"  id="queryBtn" />&nbsp;
					<input type="button" class="u-button u-reset"  id="closeBtn" name="closeBtn" value="关 闭" onclick="_hide() ;" />
				</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
	<form id="frm">
		<table class="table_query" style="vertical-align: top;">
			<tr>
				<td class="left">
					<input type="button" class="u-button u-submit"  id="chooseBtn" name="chooseBtn" value="选 择" onclick="chooseVehicle()" />
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript">
	// document.getElementById("frm").style.display = "none";
	var HIDDEN_ARRAY_IDS=['frm'];
	var myPage;
	var title = null;
	var	url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnVehicleReqQuery.json";
	var	columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\")' />", width:'8%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "单价", dataIndex: 'SALES_PRICE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "位置说明", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "入库日期", dataIndex: 'STORAGE_TIME', align:'center'}
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		var dealerId = record.data.DEALER_ID ;
		var vin = record.data.VIN ;
		var vehicleId = record.data.VEHICLE_ID ;
		var areaId = record.data.AREA_ID ;
		var modelName = record.data.MODEL_NAME ;
		var packageName = record.data.PACKAGE_NAME ;
		var materialCode = record.data.MATERIAL_CODE ;
		var materialName = record.data.MATERIAL_NAME ;
		var singlePrice = record.data.SALES_PRICE ;
		var warehouseName = record.data.WAREHOUSE_NAME ;
		var dealerShortName = record.data.DEALER_SHORTNAME ;
		var lifeCycle = record.data.LIFE_CYCLE ;
		var storageTime = record.data.STORAGE_TIME ;
		var priceListId=record.data.LIST_ID;
		var sValue = modelName + "||" + packageName + "||" + materialCode + "||" + materialName + "||" + vin + "||" + singlePrice + "||" + warehouseName + "||" + dealerShortName + "||" + lifeCycle + "||" + storageTime + "||" + dealerId + "||" + vehicleId + "||" + areaId +"||"+priceListId;
		return String.format("<input type='checkbox' name='vehicleIds' value='" + sValue + "' />");
	}
</script>    
</body>
</html>