<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>预售车辆新增</title>
<script type="text/javascript"><!--
//--------------------------------------------------------------------------------
var countType=0;
function chckSpecialTest(obj) {
	obj.value = obj.value.replace(/#/g, "") ;
}

//属性申明
var aAddressName = new Array() ;
var aAddressId = new Array() ;

// 获取控件左绝对位置
function getAbsoluteLeft(value) {
	var oObj = document.getElementById(value)
	var iLeft = oObj.offsetLeft            
	while(oObj.offsetParent != null) { 
		var oParent = oObj.offsetParent ;
		iLeft += oParent.offsetLeft ;
		oObj = oParent ;
	}
	return iLeft ;
}

// 获取控件上绝对位置
function getAbsoluteTop(value) {
	var oObj = document.getElementById(value) ;
	var iTop = oObj.offsetTop ;
	while(oObj.offsetParent != null)
	{  
		var oParent = oObj.offsetParent ;
		iTop += oParent.offsetTop ;  // Add parent top position
		oObj = oParent ;
	}
	return iTop ;
}

// 获取控件宽度
function getElementWidth(value) {
	var oObj = document.getElementById(value) ;
	return oObj.offsetHeight ;
}

// 设置div控件绝对位置
function setDivLocation(setValue, getValue) {
	var oSet = document.getElementById(setValue) ;
	
	oSet.style.left = getAbsoluteLeft(getValue) ;
	oSet.style.top = parseFloat(getAbsoluteTop(getValue)) + parseFloat(getElementWidth(getValue)) ;
}




// 删除字符串左空格
String.prototype.lTrim = function() {
	return this.replace(/(^\s*)/g,"") ;
}

function doInit(){

	getDealerLevel(document.getElementById("areaId").value);
	var choice_code = document.getElementById("orderYearWeek").value;
}



// 新增产品
function addMaterial(){
	var materialCode = document.getElementById("materialCode").value;
	var url = "<%=request.getContextPath()%>/materialGroup/MaterialGroupTree/getMaterialPre.json";
	makeCall(url,addRow,{materialCode:materialCode}); 
	addListener();
}

// 删除产品
function delMaterial(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	totalPrice();
  	disableArea();
  	_setSelDisabled_("tbody1", 0) ;
}

function addRow(json){	
			var timeValue = json.map.MATERIAL_ID;
			var newRow = document.getElementById("tbody1").insertRow();
			newRow.className  = "table_list_row2";
			var newCell = null;
			newCell = newRow.insertCell(0);
			newCell.align = "center";
			newCell.innerHTML = json.map.MATERIAL_CODE;
			newCell = newRow.insertCell(1);
			newCell.align = "center";
			newCell.innerHTML = json.map.MATERIAL_NAME;
			newCell = newRow.insertCell(2);
			newCell.align = "center";
			newCell.innerHTML = "<input id='ORDER_AMOUNT"+timeValue+"' name='ORDER_AMOUNT"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10' onchange='totalCou("+json.map.MATERIAL_ID+");' />";
			newCell = newRow.insertCell(3);
			newCell.align = "center";
			newCell.innerHTML = "<input id='RET_AMOUNT"+  timeValue+"' name='RET_AMOUNT"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10' onchange='totalCou("+json.map.MATERIAL_ID+");' />";
			newCell = newRow.insertCell(4);
			newCell.align = "center";
			newCell.innerHTML = "<span id='POOR_AMOUNT"+ timeValue+"' name='POOR_AMOUNT"+ timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10'  ></span><input id='POOR_AMOUNTS"+ timeValue+"' name='POOR_AMOUNT"+ timeValue+"' type='hidden' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10'/>";
			newCell = newRow.insertCell(5);
			newCell.align = "center";
			newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId' name='materialId' value='"+json.map.MATERIAL_ID+"'>";
								
	
}

// 物料组树
function materialShow(){
	var ids = "";//已选中的物料id
	var materialIds = document.getElementsByName("materialId");
	
	for (var i=0; i<materialIds.length; i++){  
		var obj = materialIds[i].value;
		if(i<materialIds.length-1){
			ids += obj + ",";
		}else{
			ids += obj;
		}
	} 
	
	showMaterialBase('materialCode','','true','2012112619161228',ids);
}
function totalCou(MATERIAL_ID){
	var ORDER_AMOUNT = document.getElementById('ORDER_AMOUNT' + MATERIAL_ID);
	var RET_AMOUNT = document.getElementById('RET_AMOUNT' + MATERIAL_ID);
	var POOR_AMOUNT = document.getElementById('POOR_AMOUNT' + MATERIAL_ID);
	var POOR_AMOUNTS = document.getElementById('POOR_AMOUNTS' + MATERIAL_ID);
	POOR_AMOUNTS.value=ORDER_AMOUNT.value - RET_AMOUNT.value;
	POOR_AMOUNT.innerText = ORDER_AMOUNT.value - RET_AMOUNT.value;
}

function confirmAdd(arg){
	if(submitForm('fm')){
		if(confirm("是否确认保存?")){
			orderAdd();
		}
	}
}

function orderAdd(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationAdd.json', showResult ,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationInit.do';
	}else{
		MyAlert("提交失败！");
	}
}

//清除按钮
function toClear(){
	document.getElementById("fleetName").value = "";
	document.getElementById("fleetId").value = "";
}

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 预售车辆新增</div>
<form method="POST" name="fm" id="fm">
  
  <TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable >
      <th nowrap="nowrap">物料代码</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">订单数量</th>
      <th nowrap="nowrap">退车数量</th>
      <th nowrap="nowrap">相差数量</th>
      <th nowrap="nowrap">操作</th>
    </TR>
    <tbody id="tbody1">
   
    </tbody>
  </table>	
  <table class="table_query">
    <tr class="cssTable" >
      <td width="100%" align="left">
	      <input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
	      <input class='cssbutton' name="add22" type="button" onclick="materialShow();" value ='新增产品' />
          &nbsp;
      </td>
    </tr>
  </table>
  <p>&nbsp;</p>
  <TABLE class=table_query >
    <tr>
      <th colspan="2" align="left" nowrap="nowrap" style="display:none;"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />订单说明</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
	      <input class="cssbutton" id="id1save" name="baocun" type="button" value="保存" onClick="confirmAdd('1');">
	      <input class="cssbutton" id="shangbao2" name="shangbao2" type="button" value="返回" onclick="history.back();" />
      </td>
    </tr>
 </table>
  <BR>
  <div id="addressDiv" style="display: none; border: solid #808080 1px; position: absolute"></div>
</form>
</body>
</html>
