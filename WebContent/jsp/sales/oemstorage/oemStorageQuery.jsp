<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车厂库存查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}

function getBatchNo() {
	var sBatchNos = "" ;
	var sBatchNo = "${batchNOList}" ;
	var aBatchNo = sBatchNo.split(/\D/) ;
	
	var iLen = aBatchNo.length ;

	for(var i=0; i<iLen; i++) {
		if(parseInt(aBatchNo[i])) {
			if(sBatchNos.length == 0) {
				sBatchNos = aBatchNo[i] ;
			} else {
				sBatchNos += "," + aBatchNo[i] ;
			}
		}
	}
	
	document.getElementById("baSpan").innerHTML = genTextBoxStr('batchNoT', 'batchNoT', 'batchNo', 'batchNo', 'baDiv', 'short_txt', '', '', '', sBatchNos, sBatchNos) ;
	document.getElementById("batSpan").innerHTML = genTextBoxStr('batchNoAT', 'batchNoAT', 'batchNoA', 'batchNoA', 'batDiv', 'short_txt', '', '', '', sBatchNos, sBatchNos) ;
}

function setDivDispaly(divId) {
	document.getElementById(divId).style.display = "none" ;
}

function getWare(obj) {
	var oSpan = document.getElementById("wareSpan") ;
	var aWareId = "${wareId}".split(",") ;
	var aWareName = "${wareName}".split(",") ;
	var aWareCode = "${wareCode}".split(",") ;
	var sVal = "" ;
	
	var len = aWareId.length ;
	
	sVal = "<select name='warehouseId' id='warehouseId' class='long_sel'>" ;
	sVal += "<option value=''>-请选择-</option>" ;
	
	if(obj.id == "wareYes") {
		for(var i=0; i<len; i++) {
			if(aWareCode[i] == obj.value) {
				sVal += "<option value='" + aWareId[i] + "'>" + aWareName[i] + "</option>" ;
			}
		}
	} else if(obj.id == "wareNo") {
		for(var i=0; i<len; i++) {
			if(aWareCode[i] == obj.value) {
				sVal += "<option value='" + aWareId[i] + "'>" + aWareName[i] + "</option>" ;
			}
		}
	}
	
	sVal += "</select>" ;
	
	oSpan.innerHTML = sVal ;
}

function chkSysTotalQuery() {
	var sSysFlag = "${sysFlag}" ;
	
	if(sSysFlag == "jc") {
		totalQuery() ;
	} else if(sSysFlag == "cvs") {
		totalQuery_CVS() ;
	} else {
		MyAlert("查询失败:参数错误!") ;
	}
}
</script>

</head>
<body onunload='javascript:destoryPrototype();'>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 车厂库存管理 &gt; 车厂库存查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<!--<tr>
			<td align="right" width="13%"><input type="hidden" name="area" id="area"/></td>
			<td align="right" width="23%">业务范围：</td>
			<td align="left" width="50%">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td>&nbsp;</td> 
		</tr>
		-->
		<tr>
			<td align="right" width="13%">&nbsp;</td>
			<td align="right" width="23%">仓库类型：</td>
			<td align="left" width="50%">
				<input type="radio" name="wareType" id="wareYes" onclick="getWare(this) ;" checked="checked" value="可用库" /><label for="wareYes">可用库</label><input type="radio" name="wareType" id="wareNo" onclick="getWare(this) ;" value="不可用库" /><label for="wareNo">不可用库</label>
			</td>
			<td>&nbsp;</td> 
		</tr>
		<tr>
			<td align="right" width="13%">&nbsp;</td>
			<td align="right" width="23%">所在仓库：</td>
			<td align="left" width="50%">
				<span id="wareSpan">
				</span>
				<!--<select name="warehouseId" id="warehouseId" class="long_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${warehouseList}" var="warehouseList">
						<option value="${warehouseList.WAREHOUSE_ID}"><c:out value="${warehouseList.WAREHOUSE_NAME}"/></option>
					</c:forEach>
				</select>
			--></td>
			<td>&nbsp;</td> 
		</tr>
		<tr>
			<td align="right"></td>
			<td align="right"><input type="radio"  name="flag" value="1" checked="checked" onClick="toChangeDis1();"/>&nbsp;物料组：</td>
			<td>
				<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" id="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('groupCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"><input type="radio"  name="flag" value="2" onClick="toChangeDis2();"/>&nbsp;&nbsp;&nbsp;&nbsp;物料：</td>
			<td>
				<input type="text" class="middle_txt" name="materialCode" size="15"  value="" id="materialCode" />
				<input name="button3" id="button" type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">批次号：</td>
			<td>
			<!--<span id="baSpan"></span>至<span id="batSpan"></span>-->
				<select name="batchNo" id="batchNo" class="min_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${batchNOList}" var="batchNOList">
						<option value="${batchNOList.BATCH_NO}"><c:out value="${batchNOList.BATCH_NO}"/></option>
					</c:forEach>
				</select> 至 
				<select name="batchNoA" id="batchNoA" class="min_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${batchNOList}" var="batchNOListA">
						<option value="${batchNOListA.BATCH_NO}"><c:out value="${batchNOListA.BATCH_NO}"/></option>
					</c:forEach>
				</select>
			</td>
	  		<td></td>
		</tr>
		<!--<tr>
			<td></td>
			<td align="right">订做车批次：</td>
			<td>
				<input type="text" id="custBatch" name="custBatch" value=""/>
	  		</td>
	  		<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">底盘号（VIN）：</td>
			<td>
				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
	  		</td>
	  		<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">库龄超过：</td>
			<td><input type="text" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>
			<td></td>
		</tr>-->
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td></td>
			<td align="left">
				<input type="hidden" name="warehouseId_" value=""/>
				<input type="button" name="button1" class="cssbutton" onclick="chkSysTotalQuery();" value="汇总查询" id="queryBtn1" /> 
				<input type="button" name="button2" class="cssbutton" onclick="detailQuery();" value="明细查询" id="queryBtn2" />
				<input type="button" name="button3" class="cssbutton" onclick="totalDownLoad();" value="汇总下载" id="queryBtn1" /> 
				<input type="button" name="button4" class="cssbutton" onclick="detailDownLoad();" value="明细下载" id="queryBtn2" /> 
			</td>
			<td align="center" nowrap>&nbsp;</td>
		</tr>   
	</table>
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<br>
<!-- 查询条件 end -->
<script type="text/javascript">
	var url;
	
	var title = null;
	
	var columns;		
	
	var calculateConfig;
	
	function doInit(){
		//var area = "";
		//<c:forEach items="${areaBusList}" var="list">
		//	var areaId = <c:out value="${list.AREA_ID}"/>
		//	if(area==""){
		//		area = areaId;
		//	}else{
		//		area = areaId+','+area;
		//	}
		//</c:forEach>
		//document.getElementById("area").value=area;
		document.getElementById("materialCode").disabled="true";
		document.getElementById("button").disabled="true";
		
		getWare(document.getElementById("wareYes"))
	}
	function toChangeDis1(){
		document.getElementById("materialCode").disabled="true";
		document.getElementById("button").disabled="true";
		document.getElementById("groupCode").disabled="";
		document.getElementById("button3").disabled="";
		document.getElementById("materialCode").value="";
	}
	function toChangeDis2(){
		document.getElementById("materialCode").disabled="";
		document.getElementById("button").disabled="";
		document.getElementById("groupCode").disabled="true";
		document.getElementById("button3").disabled="true";
		document.getElementById("groupCode").value="";
	}
	function totalQuery(){
		/*var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}*/
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"SERIES_NAME|BATCH_NO",totalColumns:"BATCH_NO"};
		//calculateConfig = {subTotalColumns:"SERIES_NAME|BATCH_NO",totalColumns:"SERIES_NAME"};     
		document.getElementById("warehouseId_").value = document.getElementById("warehouseId").value;   
		url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageTotalQuery.json";
		columns = [
					//{header: "业务范围",dataIndex: 'AREA_NAME', align:'center'},
					{header: "车系名称",dataIndex: 'SERIES_NAME', align:'center'},
					{header: "车型名称",dataIndex: 'MODEL_NAME', align:'center'},
					//{header: "配置代码", dataIndex: 'PACKAGE_CODE', align:'center'},
					{header: "状态名称", dataIndex: 'PACKAGE_NAME', align:'center'},
					{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
					//{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
					{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
					{header: "所在仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
					//{header: "库存量", dataIndex: 'STOCK_AMOUNT', align:'center', renderer:myLink1},
					//{header: "保留量", dataIndex: 'RESERVE_AMOUNT', align:'center', renderer:myLink2},
					//{header: "可用库存", dataIndex: 'RESOURCE_AMOUNT', align:'center', renderer:myLink3}
					{header: "移库在途数量", dataIndex: 'MOVE_E_AMOUNT', align:'center'},
					{header: "库存量", dataIndex: 'STOCK_AMOUNT', align:'center'},
					{header: "保留量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
					{header: "可用库存", dataIndex: 'RESOURCE_AMOUNT', align:'center'}
			      ];
		__extQuery__(1);
		//setDivDispaly("baDiv") ;
		//setDivDispaly("batDiv") ;
	}
	function totalQuery_CVS(){
		/*var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}*/
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"SERIES_NAME|BATCH_NO",totalColumns:"BATCH_NO"};
		//calculateConfig = {subTotalColumns:"SERIES_NAME|BATCH_NO",totalColumns:"SERIES_NAME"};     
		document.getElementById("warehouseId_").value = document.getElementById("warehouseId").value;   
		url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageTotalQuery.json";
		columns = [
					//{header: "业务范围",dataIndex: 'AREA_NAME', align:'center'},
					{header: "车系名称",dataIndex: 'SERIES_NAME', align:'center'},
					{header: "车型名称",dataIndex: 'MODEL_NAME', align:'center'},
					//{header: "配置代码", dataIndex: 'PACKAGE_CODE', align:'center'},
					//{header: "配置名称", dataIndex: 'PACKAGE_NAME', align:'center'},
					{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
					//{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
					{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
					{header: "所在仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
					//{header: "库存量", dataIndex: 'STOCK_AMOUNT', align:'center', renderer:myLink1},
					//{header: "保留量", dataIndex: 'RESERVE_AMOUNT', align:'center', renderer:myLink2},
					//{header: "可用库存", dataIndex: 'RESOURCE_AMOUNT', align:'center', renderer:myLink3}
					{header: "库存量", dataIndex: 'STOCK_AMOUNT', align:'center'},
					{header: "保留量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
					{header: "可用库存", dataIndex: 'RESOURCE_AMOUNT', align:'center'}
			      ];
		__extQuery__(1);
		//setDivDispaly("baDiv") ;
		//setDivDispaly("batDiv") ;
	}
	function detailQuery(){
		calculateConfig = {};
		url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageDetailQuery.json";
		columns = [
					{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
					{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
					{header: "订做车批次", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
					{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
					{id:'action',header: "车辆VIN号", dataIndex: 'VIN', align:'center',renderer:mySelect},
					{header: "所在仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
					{header: "入库日期", dataIndex: 'STORAGE_DATE', align:'center'},
					{header: "库龄(天)", dataIndex: 'DAYS', align:'center'}
			      ];
		__extQuery__(1);
		//setDivDispaly("baDiv") ;
		//setDivDispaly("batDiv") ;
	}
	function totalDownLoad(){
		/*var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}*/
		$('fm').action="<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageTotalDownLoad.json";
     	$('fm').submit();
    }
	function detailDownLoad(){
		/*var days = document.getElementById("days").value;
		if(days && (days.search("^-?\\d+$")!=0)){
			MyAlert("请正确输入库存天数");
			return false;
		}*/
		$('fm').action="<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageDetailDownLoad.json";
     	$('fm').submit();
    }
	function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\");'>"+value+"</a>");
	}
	function vehicleInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
	
	function myLink1(value,meta,record){
		var warehouseId = document.getElementById("warehouseId_").value;
	  	return String.format("<a href=\"#\" onclick='showInfoView1(\""+record.data.MATERIAL_ID+"\",\""+warehouseId+"\")'>"+ value +"</a>");
	}
	
	function showInfoView1(val1,val2){
		OpenHtmlWindow('<%=contextPath%>/sales/oemstorage/OemStorageQuery/viewDetail.do?materalId='+val1+'&type=1&warehouseId='+val2,700,500);
	}
	
	function myLink2(value,meta,record){
		var warehouseId = document.getElementById("warehouseId_").value;
	  	return String.format("<a href=\"#\" onclick='showInfoView2(\""+record.data.MATERIAL_ID+"\",\""+warehouseId+"\")'>"+ value +"</a>");
	}
	
	function showInfoView2(val1,val2){
		OpenHtmlWindow('<%=contextPath%>/sales/oemstorage/OemStorageQuery/viewDetail.do?materalId='+val1+'&type=2&warehouseId='+val2,700,500);
	}
	
	function myLink3(value,meta,record){
		var warehouseId = document.getElementById("warehouseId_").value;
	  	return String.format("<a href=\"#\" onclick='showInfoView3(\""+record.data.MATERIAL_ID+"\",\""+warehouseId+"\")'>"+ value +"</a>");
	}
	
	function showInfoView3(val1,val2){
		OpenHtmlWindow('<%=contextPath%>/sales/oemstorage/OemStorageQuery/viewDetail.do?materalId='+val1+'&type=3&warehouseId='+val2,700,500);
	}
	
</script>
</body>
</html>