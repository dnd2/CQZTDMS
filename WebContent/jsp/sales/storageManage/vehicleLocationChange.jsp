<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>车辆位置变更</title>
<script type="text/javascript">
<!-- 
  function chkAllArea() {
	var aChkBox = document.getElementsByName("vehicleIds") ;
	var wareType = document.getElementById('warehouseType').value ;
	var oChkAll = document.getElementById('checkAll') ;
	var iLen = aChkBox.length ;
	var bFlag = true ;

	if (oChkAll.checked) {
		for (var i=0; i<iLen; i++) {
			var iCerrentArea = aChkBox[i].id ;
	
			for (var j=i+1; j<iLen; j++) {
				if (iCerrentArea != aChkBox[j].id) {
					bFlag = false ;
					
					for (var n=0; n<iLen; n++) {
						aChkBox[n].checked = false ;
					}
	
					oChkAll.checked = false ;
					
					break ;
				}
			}
	
			if (!bFlag) {
				break ;
			}
		}
	
		if (!bFlag) {
			MyAlert("请选择同一业务范围的车辆！") ;
			changeFleet(wareType) ;
		} else {
			for (var m=0; m<iLen; m++) {
				if (aChkBox[m].checked) {
					linkWare(aChkBox[m].value, wareType) ; 
					break ;
				}
			}
		}
	} else {
		changeFleet(wareType) ;
	}
  }

  function chkVhclArea(objChk, value__) {
	var aChkBox = document.getElementsByName("vehicleIds") ;
	var iLen = aChkBox.length ;
	var bFlag = false ;

	//1、
	for (var i=0; i<iLen; i++) {
		if (aChkBox[i].checked) {
			if (aChkBox[i].id != value__) {
				if (objChk.checked) {
					objChk.checked = false ;
					MyAlert("请选择同一业务范围的车辆！") ;

					return ;
				}
			}
		}
	}

	//2、
	var iFlag__S = 0 ; 
	for (var n=0; n<iLen; n++) {
		if (aChkBox[n].checked) {
			++iFlag__S ;
		}
	}
	if (iFlag__S > 1) {
		return ;
	}
	
	//3、
	var wareType = document.getElementById('warehouseType').value ;
	if (objChk.checked) {
		linkWare(objChk.value, wareType) ; 
	} else {
		for (var i=0; i<iLen; i++) {
			if (aChkBox[i].checked) {
				bFlag = true ;
				break ;
			}
		}

		if (!bFlag) {
			changeFleet(wareType) ;
		}
	}
  }

  function chgType(objSel) {
	var wareType = objSel.value ;
	var aChkBox = document.getElementsByName("vehicleIds") ;
	var iLen = aChkBox.length ;
	var bFlag = false ;

	for (var i=0; i<iLen; i++) {
		if (aChkBox[i].checked) {
			linkWare(aChkBox[i].value, wareType) ;
			bFlag = true ;
			break ;
		}
	}

	if (!bFlag) {
		changeFleet(wareType) ;
	}
  }

  function linkWare(vhclId, warehouseType) {
	  var url = "<%=contextPath%>/sales/storageManage/VehicleLocationChange/getWareNew.json?COMMAND=1";

	  makeCall(url, getWare, {vhclId: vhclId, warehouseType: warehouseType}) ;
  }

  function getWare(json) {
	  var sWarehouseName = "" ;
		var sWarehouseId = "" ;
		var aWareList = json.wareList__A ;
		var iLen = aWareList.length ;
		
		for (var i=0; i<iLen; i++) {
			sWarehouseName = sWarehouseName + aWareList[i].WAREHOUSE_NAME + "," ;
			sWarehouseId = sWarehouseId + aWareList[i].WAREHOUSE_ID + "," ;
		}
		
		document.form1.warehouse__.options.length = iLen ;
		
		for(var j=0; j<iLen; j++) {
			document.form1.warehouse__.options[j].value=sWarehouseId.split(',')[j];
			document.form1.warehouse__.options[j].text=sWarehouseName.split(',')[j];
		}
  }

  function changeFleet(value) {
		var sWarehouseName = "" ;
		var sWarehouseId = "" ;
		var i = 0;
		<c:forEach var="W_warehouse" items="${list }">
			if(${W_warehouse.WAREHOUSE_TYPE} == value) {
				sWarehouseName =sWarehouseName+'${W_warehouse.WAREHOUSE_NAME}' + "," ;
				sWarehouseId = sWarehouseId + '${W_warehouse.WAREHOUSE_ID}' + "," ;
				i++ ;
			}
		</c:forEach>
		document.form1.warehouse__.options.length=i;
		for(var j=0; j<i; j++) {
			document.form1.warehouse__.options[j].value=sWarehouseId.split(',')[j];
			document.form1.warehouse__.options[j].text=sWarehouseName.split(',')[j];
		}
	}
//-->
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="changeFleet(document.getElementById('warehouseType').value)"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 车辆位置变更</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio" checked="checked"  name="flag"  onclick="toChangeMaterial(1);" />
						物料组选择：
					</div>
				</td>
				<td>
					<input type="text" id="materialCode" name="materialCode"  value=""  />
					<input type="hidden" name="materialName" size="20" id="materialName" value="" />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','materialName','true','');" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%" class="tblopt">
					<div class="right">
					<input type="radio"  name="flag"  onclick="toChangeMaterial(2);"/>
						&nbsp;&nbsp;&nbsp;物料选择：
					</div>
				</td>
				<td>
					<input type="text" id="materialCode__" name="materialCode__"  value=""  readonly="readonly"/>
       				<input type="button" id="bt2" value="..." class="mini_btn" disabled="disabled" onclick="showMaterial('materialCode__','','true','');" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div class="right">库存超过：</div></td>
				<td><input type="text" id="days" name="days" size="10" value="" datatype="1,is_digit,10" />天</td>
				<td></td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div class="right">VIN：</div></td>
				<td width="39%" >
      				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="query();" value=" 查  询  " id="queryBtn" /> 
					<input type="hidden" id="newvehicleArea" name="newvehicleArea" />
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<form  name="form1" id="form1">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
  	<tr>
  		<td align="left" width="45%">
  			<strong><font color="red">“新位置”</font></strong>:
  			<script>
						genSelBoxExp("warehouseType",<%=Constant.DEALER_WAREHOUSE_TYPE%>,"",false,"mini_sel","onchange='chgType(this)'","false",'') ;
			</script> 
  			<select id="warehouse__" name="warehouse__" >
  						
  			</select>
  			<!--<input type="text" id="vehicleAreas" name="vehicleAreas" size="25"  maxlength="100"   />-->
  		</td>
  		<td align="left"><input class=normal_btn type='button' name='saveResButton' onclick='saveRes();' value="保  存" /></td>
  	</tr>
</table>
</form>
<script type="text/javascript"><!--
	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];

	function query(){
		//var days = document.getElementById("days").value;
		//if(days && (days.search("^-?\\d+$")!=0)){
		//	MyAlert("请正确输入库存天数");
		//	return false;
		//}
		if(submitForm('fm')){
			__extQuery__(1); 
		}
	}

	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/VehicleLocationChange/VehicleLocationChangeQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\");chkAllArea();' />", width:'6%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车辆业务范围", dataIndex: 'AREA_NAME', align:'center'},
				{header: "当前位置", align:'center',dataIndex: 'WAREHOUSE_NAME'},
				{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "入库日期", dataIndex: 'STORAGE_DATE', align:'center'},
				{header: "库存天数", dataIndex: 'STORAGE_DAY', align:'center'}
		      ];
		      
	function myCheckBox(value,metaDate,record){
		var iAreaId = record.data.AREA_ID ;
		return String.format("<input type='checkbox' id=\"" + iAreaId + "\" name='vehicleIds' value='" + value + "' onpropertychange=\"chkVhclArea(this," + iAreaId + ");\"/>");
	}
	
	function saveRes(){
		var vehicleIds = document.getElementsByName("vehicleIds");
		// var vehicleAreas = document.getElementById("vehicleAreas").value.replace(/(^\s*)|(\s*$)/g, "");
		var addFlag = false;
		for(var i=0; i<vehicleIds.length; i++){
			if(vehicleIds[i].checked){
				addFlag = true;
				break;
			}
		}   
		if(addFlag){
			// if(vehicleAreas.length>100){
			// 	MyAlert("位置说明字数不能超过100,请重新输入!");
			//	return;
			// }
			// if(vehicleAreas.length<=0){
			//	MyAlert("请输入车辆位置!");
			//	return;
			// }
			// document.getElementById("newvehicleArea").value = vehicleAreas;
			MyConfirm("是否提交?",saveResAction);
		}else{
			MyAlert("请选择车辆信息!");
		}
	}

	function saveResAction(){
		var warehouseId = document.getElementById('warehouse__').value ;
		var ids = document.getElementsByName("vehicleIds");
		var ids__=new Array();
		for(var i=0; i< ids.length; i++){
			if(ids[i].checked){
				ids__.push(ids[i].value);
			}
		}
		makeNomalFormCall('<%=contextPath%>/sales/storageManage/VehicleLocationChange/VehicleLocationChangeSubmit.json?ids__='+ids__.toString()+'&warehouse__='+warehouseId,showResult ,'fm');
	}

	function showResult(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}

	function toChangeMaterial(type){
		var materialCode = document.getElementById("materialCode");//物料组
		var materialCode__ = document.getElementById("materialCode__");//物料
		var bt1 = document.getElementById("bt1");
		var bt2 = document.getElementById("bt2");

		//选择物料组
		if(type==1){
			materialCode__.readOnly = true;
			materialCode__.value="";
			materialCode.readOnly = false;
			bt1.disabled  = false;
			bt2.disabled  = true;
		}else{
			materialCode.readOnly = true;
			materialCode.value="";
			materialCode__.readOnly=false;
			bt1.disabled  = true;
			bt2.disabled  = false;
		}
	}
</script>    
</body>
</html>