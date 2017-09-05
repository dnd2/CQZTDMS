<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>退车车辆选择</title>
</head>
<body> 
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 批发申请</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">			
		    <tr>
				<td width="20%" class="tblopt"><div align="right">VIN：</div></td>
				<td width="39%">
      				<textarea id="vin" name="vin"  rows="3"  class="form-control" style="width:150px;"></textarea>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="hidden"  name="exVeh" id="exVeh" value="" />
					<input type="button"  class="u-button u-query" onclick="__extQuery__(1);" value="  查  询  " id="queryBtn" /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<form id="frm">
		<table class="table_query">
			<tr>
				<td align="center">
					<input type="button" class="u-button u-submit"  id="chooseBtn" name="chooseBtn" value="选 择" onclick="chooseVehicle() ;" />
				</td>
			</tr>
		</table>
	</form>   
</div>
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
			chooseFlag = true ;
			infoInsert(aReturnInfo[i]) ;
		}
	}
	if(!chooseFlag) {
		MyAlert("请选择退车车辆!") ;
		return false ;
	} else {
		parentContainer._hide() ;
	}
}

function infoInsert(node) {
	var myTable = parentContainer.document.getElementById("myTable") ;
	var iRowLen = myTable.rows.length ;
	myTable.insertRow(iRowLen) ;
    var tr = myTable.rows[iRowLen];
	if(iRowLen%2==0){
		tr.className = 'table_list_row2';
	}else{
		tr.className = 'table_list_row1';
	}
	tr.setAttribute("style", "background-color: rgb(253, 253, 253);");
	var id;
	for (i = 0; i < columns.length; i++) {
		var oCell = myTable.rows[iRowLen].insertCell(i);
		var dataIndex = node.getAttribute(columns[i].dataIndex);
		if (dataIndex != null) {	
			if (i == 0) {
				oCell.innerHTML = "<input type='hidden' value='" + dataIndex + "' name='vehicleIds'>";
				id = dataIndex;
			}
			else {
				oCell.innerHTML = dataIndex;
			}
		}
	}
	var oCell = myTable.rows[iRowLen].insertCell(columns.length);
	tr.setAttribute("id", "tr" + id);
	oCell.innerHTML = "<a href='#' onclick='rowDelete(" + id + ")'>[删除]</a>" ;
}

function myQuery() {
	var sVin = document.getElementById("vin").value ;
	var sDlyNo = document.getElementById("deliveryNo").value ;
	__extQuery__(1) ;
}
var HIDDEN_ARRAY_IDS=['frm'];
var myPage;
var url = "<%=contextPath%>/sales/storageManage/VehicleDispatch/VehicleDispatchList.json?COMMAND=1";
var title = null;
var columns = [
			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\")' />", width:'6%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
			{header: "VIN", dataIndex: 'VIN', align:'center'},
			//{header: "车辆业务范围", dataIndex: 'AREA_NAME', align:'center'},
			{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
			{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
			{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
			{header: "验收日期", dataIndex: 'STORAGE_DATE', align:'center'},
			{header: "库存天数", dataIndex: 'STORAGE_DAY', align:'center'}
	      ];
//全选checkbox
function myCheckBox(value,metaDate,record)
{
	var input = "<input type='checkbox' name='vehicleIds' ";
	input += " VEHICLE_ID='" + record.data.VEHICLE_ID + "' ";
	input += " VIN='" + record.data.VIN + "' ";
	//input += " AREA_NAME='" + record.data.AREA_NAME + "' ";
	input += " ENGINE_NO='" + record.data.ENGINE_NO + "' ";
	input += " MATERIAL_CODE='" + record.data.MATERIAL_CODE + "' ";
	input += " MATERIAL_NAME='" + record.data.MATERIAL_NAME + "' ";
	input += " STORAGE_DATE='" + record.data.STORAGE_DATE + "' ";
	input += " STORAGE_DAY='" + record.data.STORAGE_DAY + "' ";
	input += " />";
	return String.format(input);
}
</script>
</body>
</html>