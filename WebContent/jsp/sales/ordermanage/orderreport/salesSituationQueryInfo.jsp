<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>补充订单提报</title>
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
	var url = "<%=request.getContextPath()%>/materialGroup/MaterialGroupTree/getMaterial.json";
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
			newCell.innerHTML = "<span id='ORDER_AMOUNT"+timeValue+"' name='ORDER_AMOUNT"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10' onchange='totalCou("+json.map.MATERIAL_ID+");' ></span>";
			newCell = newRow.insertCell(3);
			newCell.align = "center";
			newCell.innerHTML = "<span id='RET_AMOUNT"+  timeValue+"' name='RET_AMOUNT"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10' onchange='totalCou("+json.map.MATERIAL_ID+");'></span>";
			newCell = newRow.insertCell(4);
			newCell.align = "center";
			newCell.innerHTML = "<span id='POOR_AMOUNT"+ timeValue+"' name='POOR_AMOUNT"+ timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,10' value='0' maxlength='10' readonly='readonly'></span>";
						
	
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
	POOR_AMOUNT.value = ORDER_AMOUNT.value - RET_AMOUNT.value;
}

function confirmAdd(arg){
	if(submitForm('fm')){
		if(confirm("是否确认保存?")){
			orderAdd();
		}
	}
}

function orderAdd(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationMod.json', showResult ,'fm');
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 预售车辆查看</div>
<form method="POST" name="fm" id="fm">
  <input type="hidden" id="situationId" name="situationId" value="${situationId}">
  <c:if test="${dutyType!=10431005}">
	  <table class=table_query >
	   <tr colspan="2" align="left" nowrap="nowrap" style="display:none;"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />经销商信息</tr>
	  
	  	<tr>
	  		<td>经销商代码:</td>
	  		<td align="left">${dealer.dealerCode}&nbsp;</td>
	  		<td >经销商名称:</td>
	  		<td align="left">${dealer.dealerShortname}&nbsp;</td>
	  	</tr>
	  	<tr>
	  		<td>经销商地址:</td>
	  		<td align="left">${address.address}&nbsp;</td>
	  		<td>联系人:</td>
	  		<td align="left">${dealer.linkMan}&nbsp;</td>
	  	</tr>
	  	<tr>
	  		<td>联系电话：</td>
	  		<td align="left">${dealer.phone}&nbsp;</td>
	  		<td>&nbsp;</td>
	  		<td align="left">&nbsp;</td>
	  	</tr>
	  </table>
  </c:if>
  <TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable >
      <th nowrap="nowrap">物料代码</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">订单数量</th>
      <th nowrap="nowrap">退车数量</th>
      <th nowrap="nowrap">相差数量</th>
    </TR>
    <tbody id="tbody1">
    <c:forEach var="i" items="${list}">
    	  <tr class="table_list_row2">
    	  	<td align="center">${i.MATERIAL_CODE}</td>
    	  	<td align="center">${i.MATERIAL_NAME}</td>
    	  	<td align="center"><span id="ORDER_AMOUNT${i.MATERIAL_ID}" name="ORDER_AMOUNT${i.MATERIAL_ID}" class="SearchInput" datatype="0,is_digit,10" value="${i.ORDER_AMOUNT}" maxlength="10" readonly="readonly" type="text">${i.ORDER_AMOUNT}</span></td>
    	  	<td align="center"><span id="RET_AMOUNT${i.MATERIAL_ID}" name="RET_AMOUNT${i.MATERIAL_ID}" class="SearchInput" datatype="0,is_digit,10" value="${i.RET_AMOUNT}" maxlength="10" readonly="readonly" type="text">${i.RET_AMOUNT}</span></td>
    	  	<td align="center"><span id="POOR_AMOUNT${i.MATERIAL_ID}" name="POOR_AMOUNT${i.MATERIAL_ID}" class="SearchInput" datatype="0,is_digit,10" value="${i.POOR_AMOUNT}" maxlength="10" readonly="readonly" type="text">${i.POOR_AMOUNT}</span></td>
    	  </tr>
    </c:forEach>
    </tbody>
  </table>	
  <table class="table_query">
    <tr class="cssTable" >
      <td width="100%" align="left">
	      <input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
          &nbsp;
      </td>
    </tr>
  </table>
  <p>&nbsp;</p>
  <TABLE class=table_query>
    <tr>
      <th colspan="2" align="left" nowrap="nowrap" style="display:none;"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />订单说明</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
	      <input class="cssbutton" id="shangbao2" name="shangbao2" type="button" value="返回" onclick="history.back();" />
      </td>
    </tr>
 </table>
  <BR>
  <div id="addressDiv" style="display: none; border: solid #808080 1px; position: absolute"></div>
</form>
</body>
</html>
