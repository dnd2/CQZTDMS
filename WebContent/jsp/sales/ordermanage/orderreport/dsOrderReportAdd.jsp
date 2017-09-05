<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>电商补充订单提报</title>
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

function setDivNone() {
	document.getElementById("addressDiv").style.display = "none" ;
}

function setDivInline() {
	document.getElementById("addressDiv").style.display = "inline" ;
}

function changeTableColor_Over(value) {
	document.getElementById(value).style.backgroundColor = "#B0C4DE" ;
}

function changeTableColor_Out(value) {
	document.getElementById(value).style.backgroundColor = "#FFFAF0" ;
}


// 将数据插入层
function insertDiv() {
	$("addressId").value = $('addressName').options[$('addressName').selectedIndex].value
	//调用层的点击事件yinsh
	
	getAddressInfo($("addressId").value);
	
}

// 判断一个字符串中是否包括另一个字符串
function compareValue(value1, value2) {
	if(value1.search(value2) == -1) {
		return false ;
	} else {
		return true ;
	}
}

// 删除字符串左空格
String.prototype.lTrim = function() {
	return this.replace(/(^\s*)/g,"") ;
}
// --------------------------------------------------------------------------------


function doInit(){
	getDealerLevel(document.getElementById("areaId").value);
	var choice_code = document.getElementById("orderYearWeek").value;
	showData(choice_code);
}

//控制不能跨业务范围提报
function disableArea(){
	var rowsnum = document.getElementById("tbody1").rows.length;
	if(rowsnum != 0){
		document.getElementById('areaId').disabled = "disabled";
	}
	else{
		document.getElementById('areaId').disabled = "";
	}
}


//获得可用资金
function getAvailableAmount(arg){
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	var fundTypeId = fundTypes[0];
	if(countType==0){
		document.getElementById("id1save").disabled=true;
		document.getElementById("id1comit").disabled=true;
	}else if(countType>0&&(fundTypes[0]==null||"null"==fundTypes[0]||""==fundTypes[0])){
		MyAlert("ERP和DMS账户类型不匹配!!!");
		document.getElementById("id1save").disabled=true;
		document.getElementById("id1comit").disabled=true;
	}else{
		document.getElementById("id1save").disabled=false;
		document.getElementById("id1comit").disabled=false;
	}
	document.getElementById("isBingcai").value = fundTypes[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAvailableAmount.json";
	makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
}	

function showAvailableAmount(json){
	var obj = document.getElementById("span4");
	obj.innerHTML = amountFormat(json.returnValue);//可用余额
	document.getElementById("accountId").value = json.accountId;
	
	var obj1 = document.getElementById("span12");   //账户余额
	obj1.innerHTML = amountFormat(json.returnValue1);
	
	var obj2 = document.getElementById("span14");//冻结资金
	obj2.innerHTML = amountFormat(json.returnValue2);	
	var obj3 = document.getElementById("credit_amount");//信用额度
	obj3.innerHTML = amountFormat(json.returnValue3);	
	var obj4 = document.getElementById("fine_amount");//账户余额-信用额度
	obj4.innerHTML = amountFormat(json.returnValue4);	
}

//获得地址列表
function getAddressList(){	
	var dealerId = document.getElementById("receiver").value;
	var areaId = document.getElementById("area").value.split("|")[0] ;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAddressList.json";
	makeCall(url,showAddressList,{dealerId:dealerId,areaId:areaId});
}	

function showAddressList(json){
	aAddressName = new Array() ;
	aAddressId = new Array() ;
	for(;$('addressName').options.length>0;){
		$('addressName').remove(0);
	}
	for(var i=0;i<json.addressList.length;i++){
		aAddressName[i] = json.addressList[i].ADDRESS ;
		aAddressId[i] = json.addressList[i].ID ;
		$('addressName').add(new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + ""));
	}
	//调用收货地址旁边的文本框的点击事件yinsh
	$("addressName").selectedIndex = 0;
	$("addressName").onchange();
}

//获得联系人信息
function getAddressInfo(arg){
	var addressId = arg;
	var oldAddress = document.getElementById("oldAddress") ;
	
	if(addressId != "" && addressId != oldAddress.value) {
		oldAddress.value = addressId ;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAddressInfo.json";
		makeCall(url,showAddressInfo,{addressId:addressId});
	}
	
	if(!addressId) {
		oldAddress.value = "" ;
		document.getElementById("linkMan").value = "" ;
		document.getElementById("tel").value = "" ;
		document.getElementById("receiveOrg").innerHTML = "" ;
	}
}	

function showAddressInfo(json){
	var obj1 = document.getElementById("linkMan");
	var obj2 = document.getElementById("tel");
	var obj3 = document.getElementById("receiveOrg");

	if(!json.info.LINK_MAN) {
		obj1.value = '' ;
	}
	if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
		obj1.value = json.info.LINK_MAN;
	}
	if(!json.info.TEL) {
		obj2.value = '' ;
	}
	if(json.info.TEL != null && json.info.TEL != "null"){
		obj2.value = json.info.TEL;
	}
	if(!json.info.RECEIVE_ORG) {
		obj3.innerHTML = '' ;
	}
	if(json.info.RECEIVE_ORG != null && json.info.RECEIVE_ORG != "null"){
		obj3.innerHTML = json.info.RECEIVE_ORG;
	}
}

//根据运输方式隐藏地址列表
function addressHide(arg){
	var obj1 = document.getElementById("addTr");
	//自提 addTr
	if(arg == '<%=Constant.TRANSPORT_TYPE_01%>') {
		obj1.style.display = "none";
	}
	else{
		obj1.style.display = "inline";
	}
	
}

//根据经销商id，获得经销商级别，过滤地址列表、资金信息
function getDealerLevel(arg){
	document.getElementById("area").value = arg;
	
	getReceiverList();//获得收货方列表
	var dealerId = arg.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:dealerId});
}

function showFund(json){
	
	document.getElementById("dealerLevel").value = json.returnValue;
	$('dealerId').value = json.dealerId;
	if(json.returnValue != "1") {
		document.getElementById("span1").style.display = "inline";
		document.getElementById("span2").style.display = "inline";
		document.getElementById("span3").style.display = "none";
		document.getElementById("span4").style.display = "none";
		document.getElementById("span5").style.display = "none";
		document.getElementById("span6").style.display = "none";
		//document.getElementById("span7").style.display = "none";
		//document.getElementById("span8").style.display = "none";
		//document.getElementById("span9").style.display = "none";
		//document.getElementById("totalDiscountPrice").style.display = "none";
		document.getElementById("priceTr").style.display = "none";
		document.getElementById("discountTr").style.display = "none";
		document.getElementById("singleTdTitle").style.display = "inline";
		document.getElementById("totalTdTitle").style.display = "none";
		document.getElementById("singleFoot").style.display = "none";
		document.getElementById("totalPrice").style.display = "none";
		document.getElementById("payRemarkTR").style.display = "inline";
		showFundTypeList(json);//资金类型列表显示
	}
	else{
		document.getElementById("span1").style.display = "inline";
		document.getElementById("span2").style.display = "inline";
		document.getElementById("span3").style.display = "inline";
		document.getElementById("span4").style.display = "inline";
		document.getElementById("span5").style.display = "inline";
		document.getElementById("span6").style.display = "inline";
		//document.getElementById("span7").style.display = "inline";
		//document.getElementById("span8").style.display = "inline";
		//document.getElementById("span9").style.display = "inline";
		//document.getElementById("totalDiscountPrice").style.display = "inline";
		document.getElementById("priceTr").style.display = "inline";
		document.getElementById("discountTr").style.display = "inline";
		document.getElementById("singleTdTitle").style.display = "inline";
		document.getElementById("totalTdTitle").style.display = "inline";
		document.getElementById("singleFoot").style.display = "inline";
		document.getElementById("totalPrice").style.display = "inline";
		document.getElementById("payRemarkTR").style.display = "none";
		
		showFundTypeList(json);//资金类型列表显示
		//showDiscountInfo(json);//可用折让显示
		getPriceList();//获得价格类型列表
	}
}

//资金类型列表显示
function showFundTypeList(json){
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	countType=json.fundTypeList.length;
	for(var i=0;i<json.fundTypeList.length;i++){
		if(json.fundTypeList[i].TYPE_ID==null){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, "null"+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}else{
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}
		
	}
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	
	getAvailableAmount(fundTypes[0]);//获得账户余额
}

//可用折让显示
function showDiscountInfo(json){
	var discount = 0;
	var discountAccountId = "";
	for(var i=0;i<json.discountList.length;i++){
		discount = json.discountList[i].AVAILABLE_AMOUNT;
		discountAccountId = json.discountList[i].ACCOUNT_ID;
	}
	
	document.getElementById("discount").value = discount;
	document.getElementById("discountAccountId").value = discountAccountId;
}

// 新增产品
function addMaterial(){	
	var priceId = document.getElementById("priceId").value;
	var materialCode = document.getElementById("materialCode").value;
	var area = document.getElementById("area").value;
	var dealerId = document.getElementById("dealerId").value;
	var areaId = area.split("|")[0];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/addMaterial.json";
	makeCall(url,addRow,{materialCode:materialCode,priceId:priceId,areaId:areaId ,dealerId:dealerId}); 
}

// 删除产品
function delMaterial(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	totalPrice();
  	disableArea();
  	_setSelDisabled_("tbody1", 0) ;
}

//价格验证
//function priceOnBlue(thisRate,timeValue){
//	var discountRate = thisRate.value;
//	if(${returnValue==1}){
//		if(discountRate!=0 && discountRate!=15){
//			thisRate.value=0;
//			document.getElementById("discount_price"+timeValue).innerHTML = 0;
//			document.getElementById("totalDiscountPrice").innerHTML=0;
//			document.getElementById("totalDiscountPrice_").value=0;
//			MyAlert("微车的折扣率只能是0和15");
//		}
//	}
//	if(${returnValue==2}){
//	if(discountRate!=0 && discountRate!=10){
//		thisRate.value=0;
//		document.getElementById("discount_price"+timeValue).innerHTML = 0;
//		document.getElementById("totalDiscountPrice").innerHTML=0;
//		document.getElementById("totalDiscountPrice_").value=0;
//		MyAlert("轿车的折扣率只能是0和10");
//	    }
//    }
//}

function addRow(json){	
	
	var level = document.getElementById("dealerLevel").value;
	var flagIs = json.isFlag;
	var isReturn = json.isReturn;
	if(flagIs==0){
	   if(isReturn=="false"){
			MyAlert("该物料存在本周常规订单中存在未启票数量，请通过常规订单采购该物料。");
			return false;
		}
	}
	else{
		// return true;
	}

	if(level == "1"){
		if(parseFloat(json.infos.OPERAND, 10) < <%=Constant.MATERIAL_PRICE_MAX%>){	
			var timeValue = json.info.MATERIAL_ID;
			var newRow = document.getElementById("tbody1").insertRow();
			newRow.className  = "table_list_row2";
			var newCell = newRow.insertCell(0);
			newCell.align = "center";
			newCell.innerHTML = json.info.SERIES_NAME;
			newCell = newRow.insertCell(1);
			newCell.align = "center";
			newCell.innerHTML = json.info.MATERIAL_CODE;
			newCell = newRow.insertCell(2);
			newCell.align = "center";
			newCell.innerHTML = json.info.MATERIAL_NAME;
			newCell = newRow.insertCell(3);
			newCell.align = "center";
			newCell.innerHTML = json.info.RESOURCE_AMOUNT;
			newCell = newRow.insertCell(4);
			newCell.align = "center";
			newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice("+json.infos.OPERAND+","+timeValue+");' ><font color='red'>*</font>";
			newCell = newRow.insertCell(5);
			newCell.align = "center";
			newCell.innerHTML = "<span id='price"+timeValue+"'>"+amountFormat(json.infos.OPERAND)+"</span><input id='singlePrice"+timeValue+"' type='hidden' name='singlePrice"+timeValue+"' value='"+json.infos.OPERAND+"'>";
			//newCell = newRow.insertCell(6);
			//newCell.align = "center";
			//newCell.innerHTML = "<span><input type='text' id='discount_rate"+timeValue+"' name='discount_rate"+timeValue+"' class='SearchInput' datatype='1,is_digit,6' size='2' maxlength='2' value='0' onchange='priceOnBlue(this,"+timeValue+");calculate(document.getElementById(\"singlePrice"+timeValue+"\").value,"+timeValue+");' />%</span>";
			//newCell = newRow.insertCell(7);
			//newCell.align = "center";
			//newCell.innerHTML = "<span id='discount_s_price"+timeValue+"'>0</span>";
			//newCell = newRow.insertCell(8);
			//newCell.align = "center";
			//newCell.innerHTML = "<span id='discount_price"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(6);
			newCell.align = "center";
			newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(7);
			newCell.align = "center";
			newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>"+
						  //    "<input type='hidden' id='discount_s_price_"+timeValue+"' name='discount_s_price_"+timeValue+"' readOnly='readOnly' size='10'  value='' />"+
						  //    "<input type='hidden' id='discount_price_"+timeValue+"' name='discount_price_"+timeValue+"' readOnly='readOnly' size='10'  value='' />"+
						 //	  "<input type='hidden' id='discount_price_calcu"+timeValue+"' name='discount_price_' readOnly='readOnly' size='10'  value='' />"+
								"<input type='hidden' id='hejiPrice_"+timeValue+"' name='hejiPrice_' readOnly='readOnly' size='10'  value='' />"+
								"<input type='hidden' id='priceList"+timeValue+"' name='priceList"+timeValue+"'   value='"+json.infos.LIST_HEADER_ID+"' />";
		}
		else{
			
			MyAlert("该款物料尚未维护价格，不能添加！");
			return false;
		}
	}else{
		var timeValue = json.info.MATERIAL_ID;
		var newRow = document.getElementById("tbody1").insertRow();
		newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = json.info.SERIES_NAME;
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = json.info.MATERIAL_CODE;
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = json.info.MATERIAL_NAME;
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = json.info.RESOURCE_AMOUNT;
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice1("+timeValue+",this.value);'><font color='red'>*</font>"+
							"<input type='hidden' id='amount_"+timeValue+"' name='amount' value='' />";
		newCell = newRow.insertCell(5);
		newCell.style.display = "none";
		newCell.align = "center";
		newCell.innerHTML = "<span id='price"+timeValue+"'>"+amountFormat(0)+"</span><input id='singlePrice"+timeValue+"' type='hidden' name='singlePrice"+timeValue+"' value='0'>";
		newCell = newRow.insertCell(6);
		newCell.style.display = "inline";
		newCell.align = "center";
		newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>";
		
	}
	disableArea();
	_setSelDisabled_("tbody1", 0) ;
}
function totalPrice1(timeValue,value){
	document.getElementById("amount_"+timeValue).value = value;
	var amount = document.getElementsByName("amount");
	var amounts = 0;
	for(var i=0;i<amount.length;i++){
		amounts = parseFloat(amounts)+parseFloat(amount[i].value);
	}
    document.getElementById("totalAmount").innerHTML = amounts;
}
function round(number,fractionDigits){   
   with(Math){   
       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
    }   
}   
//根据折扣率，计算折扣后单价和折扣额
//price:单价
//timeValue:id
function calculate(price,timeValue){
	var amount = document.getElementById("amount"+timeValue).value;							//数量
	
	var discount_rate = document.getElementById("discount_rate"+timeValue).value;			//折扣率
	var ratePara = parseInt(document.getElementById("ratePara").value, 10);
	if(discount_rate - ratePara >0){
		MyAlert("折扣率不能大于"+ratePara+"%,请重新输入!");
		document.getElementById("discount_rate"+timeValue).value = 0;
		document.getElementById("discount_s_price_"+timeValue).value = price; 				//折扣后单价
		document.getElementById("discount_s_price"+timeValue).innerHTML = amountFormat(price);
		document.getElementById("discount_price_"+timeValue).value = 0;					    //折扣额
		document.getElementById("discount_price_calcu"+timeValue).value = 0;					    
		document.getElementById("discount_price"+timeValue).innerHTML = 0;
		document.getElementById("hejiPrice_"+timeValue).value = amount*price;	   			//合计
		document.getElementById("hejiPrice"+timeValue).innerHTML = amountFormat(amount*price);	

		var t_total = document.getElementsByName("hejiPrice_");
		var t_totals = 0;
		for(var i=0;i<t_total.length;i++){
			t_totals = parseFloat(t_totals) + parseFloat(t_total[i].value);
		}
		document.getElementById("total").value = round(t_totals,2);							//总计
		document.getElementById("totalPrice").innerHTML = amountFormat(round(t_totals,2));
		document.getElementById("span6").innerHTML = amountFormat(round(t_totals,2));

		var discount_price = document.getElementsByName("discount_price");
		var discount_prices = 0;
		for(var j=0;j<discount_price.length;j++){
			discount_prices = parseFloat(discount_prices) + parseFloat(discount_price[j].value);
		}
		document.getElementById("totalDiscountPrice_").value = round(discount_prices,2);		//折扣额总计
		document.getElementById("totalDiscountPrice").innerHTML = amountFormat(round(discount_prices,2));
		return;
	}
	
	var dis_rate = price*(discount_rate/100)*amount;							//折扣额
	var dis_value = price - price*(discount_rate/100);							//折扣后单价
	document.getElementById("discount_s_price_"+timeValue).value = round(dis_value,2);
	document.getElementById("discount_price_"+timeValue).value = round(dis_rate,2);
	document.getElementById("discount_price_calcu"+timeValue).value = round(dis_rate,2);
	document.getElementById("discount_s_price"+timeValue).innerHTML = amountFormat(round(dis_value,2));
	document.getElementById("discount_price"+timeValue).innerHTML = amountFormat(round(dis_rate,2));
	
	var discount_prices = document.getElementsByName("discount_price_");
	var discount_prices__ = 0;
	for(var i=0;i<discount_prices.length;i++){
		discount_prices__ = parseFloat(discount_prices__) + parseFloat(discount_prices[i].value);
	}
	document.getElementById("totalDiscountPrice").innerHTML = amountFormat(round(discount_prices__,2));
	document.getElementById("totalDiscountPrice_").value = round(discount_prices__,2);
	
	document.getElementById("hejiPrice"+timeValue).innerHTML = amountFormat(round(amount*dis_value,2));
	document.getElementById("hejiPrice_"+timeValue).value = round(amount*dis_value,2);
	var hejiPrice_ = document.getElementsByName("hejiPrice_");
	var hejiPrice_s = 0;
	for(var j=0;j<hejiPrice_.length;j++){
		hejiPrice_s = parseFloat(hejiPrice_s) + parseFloat(hejiPrice_[j].value);
	}
	document.getElementById("totalPrice").innerHTML = amountFormat(round(hejiPrice_s,2));
	document.getElementById("span6").innerHTML = amountFormat(round(hejiPrice_s,2));
	document.getElementById("total").value = round(hejiPrice_s,2);
}
// 物料组树
function materialShow(){
	if(!_getTip_()) {
		return false ;
	}
	
	var ids = "";//已选中的物料id
	var myForm = document.getElementById("fm");
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=10 && obj.id.substring(0,10)=="materialId"){
			ids += obj.value + ",";
		}   
	} 	
	ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
	var areaObj = document.getElementById("area");
	var areaId = areaObj.value.split("|")[0];	
	
	var productId = "" ;
	
	if(document.getElementById("_productId_")) {
		productId = document.getElementById("_productId_").value ;
	}
	var orderType = document.getElementById("orderType").value;
	showMaterialByAreaId_Suzuki('materialCode','','true',areaId,ids, productId,orderType);
}

function delTboby(){
	var obj = document.getElementById("tbody1");
	var le = obj.rows.length;
	for(var i=le;i>0;i--){
		obj.deleteRow(i-1);
	}
	totalPrice();
  	disableArea();
  	_setSelDisabled_("tbody1", 0) ;
}

var lastIndex,lastValue;

function saveLast(){
	var select = document.getElementById("orderType");
	lastIndex = select.selectedIndex;
	var midValue = select.options[lastIndex].value; 
	if(midValue!="0"){
		lastIndex = select.selectedIndex;
		lastValue = select.options[lastIndex].value;
	} 
}



function changeOrderType(arg){
	if(confirm("更改订单类型会清空‘产品列表’，是否确认更改？")){
		delTboby();
	}else{
		arg.options[lastIndex].selected=true; 
	}	
}

// 合计价格
function totalPrice(price,timeValue){
	/*if(price){
		calculate(price,timeValue);
	}*/
	var myForm = document.getElementById("fm");
	var totalAmount = 0;
	var totalPrice = 0;
	
	for (var i=0; i<myForm.length; i++){  
		
		var obj = myForm.elements[i];
		if(obj.id.length>=6 && obj.id.substring(0,6)=="amount"){
		
			var subStr = obj.id.substring(6,obj.id.length);
			var value1 = window.parseInt(obj.value,10);
			var value2 = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);
			var heji = value1*value2;
			document.getElementById("hejiPrice"+subStr).innerHTML = amountFormat(heji);
			totalAmount += value1;
			totalPrice += heji;
		}   
	}  
	document.getElementById("totalAmount").innerHTML = totalAmount;
	document.getElementById("totalAmount_").value = totalAmount;
	
	//document.getElementById("hejiPrice"+timeValue).innerHTML = amountFormat(totalPrice);

	document.getElementById("span6").innerHTML = amountFormat(totalPrice);
	document.getElementById("totalPrice").innerHTML = round(totalPrice,2);
	document.getElementById("total").value = round(totalPrice,2);

}
function contentTrim(str){
	if(str){
		str = str.replace(/(^\s*)|(\s*$)/g, "");
		return str;
	}else{
		return "";
	} 
}
function confirmAdd(arg){
	if(document.getElementById('tmp_license_amount').value.length==0) {
				MyAlert('临牌数量不能为空。') ; 
				return false ;
	}
	if(!_getTip_()) {
		return false ;
	} ;
	
	var a=document.getElementById("deliveryType").value;
	if(document.getElementById("isCover").checked){
	}else if(a=='<%=Constant.TRANSPORT_TYPE_01%>'){
	}else{
	if(document.getElementById('addressId').value == "") {
		MyAlert('运送地址填写有误！') ; 
		return false ;
	}
	}
	if(!testLen(document.getElementById('orderRemark').value)) {
		MyAlert('备注录入已超过50个汉字的最大限！') ; 
		return false ;
	}
	if(submitForm('fm')){
		var totalDiscountPrice_ = document.getElementById("totalDiscountPrice_").value;		//折扣总额
		var discount = document.getElementById("discount").value;							//可用折让
		if(totalDiscountPrice_ - discount >0){
			MyAlert("折扣总额不能大于可用折让!");
			return;
		}
		
		document.getElementById("submitType").value = arg;
		var level = document.getElementById("dealerLevel").value;
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=6 && obj.id.substring(0,6) == "amount"){
				var amount = parseInt(obj.value, 10);
				if(amount < 1){
					MyAlert("物料数量不能小于1！");
					return false;
				}
				
				if(level == '1'){
					var subStr = obj.id.substring(6, obj.id.length);
					var singlePrice = document.getElementById("singlePrice"+subStr).value;
					if(parseInt(singlePrice, 10)==0){
						MyAlert("未维护价格物料不能提报！");
						return false;
					}
				}
			}   
		} 
		
		if(level == '1'){
			var fundType = document.getElementById("fundType").value;
			if(fundType == ""){
				MyAlert("资金类型不能为空！");
				return false;
			}
		}
		var totalAmount = parseInt(document.getElementById("totalAmount").innerHTML, 10);
		if(totalAmount < 1){
			MyAlert("所选物料总数量不能小于1！");
			return false;
		}
		
		//var isCover = document.getElementById("isCover").value;
		//if("0"==isCover+"" || !isCover){
		//	if(document.getElementById("deliveryType").value == '<%=Constant.TRANSPORT_TYPE_02%>' && document.getElementById("deliveryAddress").value == ''){
		//		MyAlert("运送地址不能为空！");
		//		return false;
		//	}
		//}
		
		var orderRemark = document.getElementById("orderRemark").value;
		if(contentTrim(orderRemark).length>350){
			MyAlert("备注说明内容过多，请重新填写!");
			return;
		}
		var payRemark = document.getElementById("payRemark").value;
		if(contentTrim(payRemark).length>150){
			MyAlert("付款信息备注内容过多，请重新填写!");
			return;
		}
		var b = new Object();
		b = document.getElementById("baocun");
		var a = new Object();
		a = document.getElementById("tibao");
		a.disabled=true;
		b.disabled=true;
		if(confirm("是否确认保存?")){
			userValidate();
			/* orderAdd(); */
		}
		else{
			a.disabled=false;
			b.disabled=false;
		}
	}
}

function orderAdd(json){
	if(json.result == true) {
		MyAlert("提交失败！用户操作超时，请重新登陆！");
		return;
	}
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/dsOrderReoprtAdd.json', showResult ,'fm');
	//document.getElementById("id1").disabled = "disabled";
	//document.getElementById("id2").disabled = "disabled";
}

function userValidate() {
	var userId = this.parent.document.getElementById('userId').value;
	var sessionId = this.parent.document.getElementById('sessionId').value;
	makeCall('<%=request.getContextPath()%>/util/UserValidator/validate.json', orderAdd , {userId : userId, sessionId : sessionId});
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/doInit.do';
	}else{
		MyAlert("提交失败！余额不足！");
	}
}

//清除按钮
function toClear(){
	document.getElementById("fleetName").value = "";
	document.getElementById("fleetId").value = "";
}

//大用户弹出
function showFleet(){
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
}

//获得价格类型列表
function getPriceList(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getPriceList.json";
	makeCall(url,showPriceList,{dealerId:dealerId});
}	

//显示价格类型列表
function showPriceList(json){
	var objs = document.getElementById("priceIds");
	objs.innerText=json.priceType;
	var obj = document.getElementById("priceId");
	obj.value=json.priceType;
	
	//obj.options.length = 0;
	//for(var i=0;i<json.priceList.length;i++){
		//if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
			//obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
			//obj.options[i].selected = "selected";
		//}
		//else{
		//	obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
		//}
	//}
	//priceTypeChange();
}

//获得收货方列表
function getReceiverList(){	
	var areaObj = document.getElementById("area");
	var areaId = areaObj.value.split("|")[0];
	var dealerId = areaObj.value.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getReceiverListInNull.json";
	makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
}	

//显示收货方列表
function showReceiverList(json){
	var obj = document.getElementById("receiver");
	obj.options.length = 0;
	for(var i=0;i<json.receiverList.length;i++){
		//获取当前经销商id
		var curDealerId=document.getElementById("dealerIds").value;
		if(json.receiverList[i].DEALER_ID==curDealerId){
			obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
			obj.options[i].selected=true;
		}else{
			obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
		}
	}
	getAddressList();//获得发运地址列表
}

// 判断是否显示使用其他价格原因
function isShowOtherPriceReason(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var priceId = document.getElementById("priceId").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/isShowOtherPriceReason.json";
	makeCall(url,showOtherPriceReason,{dealerId:dealerId,priceId:priceId});
}	

//使用其他价格原因
function showOtherPriceReason(json){
	if(json.returnValue == "1"){
		document.getElementById("otherTr").style.display = "inline";
	}
	else{
		document.getElementById("otherTr").style.display = "none";
	}
}

//价格类型改变
function priceTypeChange(){	
	var priceId = document.getElementById("priceId").value;
	var ids = "";//已选中的物料id
	var myForm = document.getElementById("fm");
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=10 && obj.id.substring(0,10)=="materialId"){
			ids += obj.value + ",";
		}   
	} 	
	ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getSinglePriceList.json";
	makeCall(url,priceChange,{priceId:priceId, ids:ids});
}	

//价格类型改变处理
function priceChange(json){
	for(var i=0;i<json.priceList.length;i++){
		var id = json.priceList[i].MATERIAL_ID;
		var price = json.priceList[i].PRICE;
		
		var obj1 = document.getElementById("price" + id);
		var obj2 = document.getElementById("singlePrice" + id);
		
		if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
			price = 0;
			obj1.innerHTML = "价格未维护";
		}
		else{
			obj1.innerHTML = amountFormat(price);
		}
		obj2.value = price;
		calculate(price,id);
	}
	//isShowOtherPriceReason();
	totalPrice();
}
//是否代交车
function showFleetInfo(){
	if(document.getElementById("isCover").checked){
		document.getElementById("cusTr").style.display = "inline";
		document.getElementById("addTr").style.display = "none";
		document.getElementById("tran_id").style.display = "none";
		document.getElementById("isCover").value = 1;
	}else{
		document.getElementById("cusTr").style.display = "none";
		document.getElementById("addTr").style.display = "inline";
		document.getElementById("tran_id").style.display = "inline";
		addressHide(document.getElementById("deliveryType").value);
		document.getElementById("isCover").value = 0;
	}
}
function showData(choice_code){
	var data_start = "";
	var data_end = "";
	<c:forEach items="${dateList}" var="list">
		var code = "${list.code}";
		if(choice_code+"" == code+""){
			data_start = "${list.date_start}";
			data_end = "${list.date_end}";
		}
	</c:forEach>
	if(data_start){
		document.getElementById("data_start").innerHTML = data_start+"  至  ";
		document.getElementById("data_end").innerHTML = data_end;
	}
}
/** 
* 限制textarea文本域输入的字符个数 
* @id        textarea表单ID 
* @count 要限制的最大字符数 
*/  
function limitChars(id, count){  
    var obj = document.getElementById(id);  
    if (obj.value.length > count){  
        obj.value = obj.value.substr(0, count); 
        MyAlert('录入已超过' + count + '个汉字的最大限！') ; 
    }  
}  

function testLen(value) {
	var reg = /^.{0,50}$/ ;
	var flag = reg.exec(value) ;
	return flag ;
  }
  
--></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lock.js"></script>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 电商补充订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align="center">
    <tr class= "tabletitle">
      <td align = "right" style="color:#252525;width:70px;text-align:right;">业务范围：</td>
      <td align = "left" >
      	<select id="areaId" name="areaId" onchange="getDealerLevel(this.options[this.options.selectedIndex].value);">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
        <input type="hidden" id="area" name="area" value="">
        <input type="hidden" id="dealerId" value="">
        <input type="hidden" id="dealerIds" name="dealerIds" value="${curDealerId}">
      </td>
      <td id="_productControl_">
      	<script type="text/javascript">
      		productStart("<%=request.getContextPath()%>", "", false, true) ;
      	</script>
      </td>
      <td align = "right" style="color:#252525;width:90px;text-align:right;"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType" id="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      	</select>
      	</span>
      </td>
      <td align = "right" style="color:#252525;width:70px;text-align:right;"><span id="span11">账户余额：</span></td>
      <td align = "left"><span id="span12" class="STYLE2"></span></td>
      <td align="right" style="color:#252525;width:70px;text-align:right;"><span id="span13">冻结资金：</span></td>
      <td align = "left"><span id="span14" class="STYLE2">0</span></td>
    </tr>
    <tbody id="priceTr">
	    <tr class = "tabletitle">
	      <td align = "right" style="color:#252525;width:70px;text-align:right;">价格类型：</td>
	      <td align = "left" colspan="2">
	      	<label id="priceIds" class="long_txt"></label>
	      	<input type="hidden" id="priceId" name="priceId"/>
<!--	      	<select id="priceId" name="priceId" onchange="priceTypeChange();">-->
<!--	        </select>-->
	      </td>
	      <td align = "right" style="color:#252525;width:90px;text-align:right;">订单类型：</td>
	      <td align = "left" colspan="1">
	      	<select id="orderType" name="orderType" onclick="saveLast()" onchange="changeOrderType(this);">
                  <option value="<%=Constant.ORDER_TYPE_06%>">电商订单</option>
	        </select>
	      </td>
	      <td align = "right" style="color:#252525;width:70px;text-align:right;"><span id="span3">可用余额：</span></td>
	      <td align = "left" ><span id="span4" ></span></td>
	      <td align="right" style="color:#252525;width:70px;text-align:right;"><span id="span5">订单总价：</span></td>
	      <td align = "left"><span id="span6">0</span></td>	 	      
	    </tr>
	    <tr id="otherTr" class = "tabletitle" style="display:none;">
	      <td align = "right">使用其他价格原因：</td>
	      <td align = "left" colspan="7">
	      	<input id="otherPriceReason" name="otherPriceReason" type="text" class="long_txt"/>
	      </td>
	    </tr>
    </tbody>
<tr  class= "tabletitle">
	    <td align="right" nowrap style="color:#252525;width:70px;text-align:right;">订单周度：   </td>
	    <td  class="table_query_2Col_input" nowrap>
		    <select name="orderWeek" id="orderYearWeek" onchange="showData(this.value);">
		      <c:forEach items="${dateList}" var="po"> 
		      <c:choose>   
    				<c:when test="${po.code==myWeek}" >   
					<option value="${po.code}" selected="selected">${po.name}</option>
					</c:when>
					<c:otherwise>  
							<option value="${po.code}">${po.name}</option> 
					</c:otherwise>
					</c:choose>
			  </c:forEach>
	        </select>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end" class="innerHTMLStrong"></span>
        </td>
        <td colspan="2"></td>
	    <td  align="right" nowrap><span>信用额度：</span></td>
	    <td> <span id="credit_amount"></span></td>
	    <td align="right">账户余额-信用额度：</td>
	    <td><span id="fine_amount"></span></td>
    </tr> 
  </table>
  <TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable >
      <th nowrap="nowrap">车系</th>
      <th nowrap="nowrap">物料编号</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">资源情况</th>
      <th nowrap="nowrap">数量</th>
      <th nowrap="nowrap" id="singleTdTitle">单价</th>
<!--      <th nowrap="nowrap" id="span7" style="display:none;">折扣率%</th>-->
<!--      <th nowrap="nowrap" id="span8" style="display:none;">折扣后单价</th>-->
<!--      <th nowrap="nowrap" id="span9" style="display:none;">折扣额</th>-->
      <th nowrap="nowrap" id="totalTdTitle">合计</th>
      <th nowrap="nowrap">操作</th>
    </tr>
    <tbody id="tbody1"></tbody>
     <tr class="table_list_row1">
      <td nowrap="nowrap"  >&nbsp;</td>
      <td nowrap="nowrap" >&nbsp;</td>
      <td align="right" nowrap="nowrap"  ><strong>总计： </strong></td>
      <td align="center" nowrap="nowrap" >&nbsp;</td>
      <td align="center" nowrap="nowrap" id="totalAmount">0</td>
      <td align="center" nowrap="nowrap" id="singleFoot">&nbsp;</td>
<!--      <td align="right" nowrap="nowrap" id="totalDiscountPrice" >0</td>-->
      <td align="center" nowrap="nowrap" id="totalPrice">0</td>
      <td nowrap="nowrap" >
      	<input type='hidden' id='totalDiscountPrice_' name='totalDiscountPrice_' readOnly='readOnly' size='10'  value='0' />
      	<input type="hidden" id="totalAmount_" name="totalAmount_" value="" />
      </td>
    </tr>
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
  <TABLE class=table_query>
    <tr>
      <th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class="cssTable" id="tmp_license_amountTr">
      <td width="12%" align="right" valign="top" nowrap="nowrap">临牌数量：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input id="tmp_license_amount" name="tmp_license_amount" type="text" class="middle_txt" size="10" maxlength="30" value="0" datatype="1,is_double,20" decimal="2"/>
      </td>
    </tr>
    <tr class="cssTable" id="discountTr" style="display:none;">
      <td width="12%" align="right" valign="top" nowrap="nowrap" style="display:none;" >可用折让：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap" style="display:none;">
      	<input id="discount" name="discount" type="text" class="middle_txt" size="10" maxlength="30" value="0" datatype="1,is_double,20" decimal="2" readonly="readonly"/>
      </td>
    </tr>
    <tr class="cssTable" id="discountTr" style="display:none;">
      <td width="12%" align="right" valign="top" nowrap="nowrap">是否代交车：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      </td>
    </tr>
    <tr class="cssTable" id="cusTr" style="display:none">
      <td align="right" nowrap="nowrap">选择集团客户：</td>
      <td align="left" nowrap="nowrap">
	      <input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30" readonly="readonly" />
		  <input id="fleetId" name="fleetId" type="hidden"/>				
		  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
		  <input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
	  </td>
      <td align="right" nowrap="nowrap">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap">
	      <input type="text" id="fleetAddress" name="fleetAddress" size="50" datatype="0,is_noquotation,50"/>
	  </td>
    </tr>
   
    <tr class="cssTable" id="tran_id">
      <td align="right"   nowrap="nowrap">运输方式： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      <script type="text/javascript">
	      	genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
	      </script>
      </td>
    </tr>
     <tbody id="addTr">
	    <tr class="cssTable">
	      <td align="right" nowrap="nowrap">收货方：</td>
	      <td align="left" nowrap="nowrap">
		      <select id="receiver" name="receiver" onchange="getAddressList();">
		      </select>
		  </td>
	      <td colspan="2" align="left" nowrap="nowrap">&nbsp;</td>
	    </tr>
	    <tr class="cssTable" style="display:inline">
	      <td align="right" nowrap="nowrap">运送地址：</td>
	      <td align="left" nowrap="nowrap">
	      	<select type="text" name="addressName" id="addressName" onchange="insertDiv();" class="long_txt" >
	      	
	      	</select><font color="red">*</font>
  			
  			
  			<input type="hidden" name="deliveryAddress" id="addressId" />
		      <!--<select name="deliveryAddress" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
		      </select><span id="linkInfo"></span>
		  --></td>
	      <td align="right" valign="top" nowrap="nowrap">收车单位：</td>
	      <td align="left" valign="top" nowrap="nowrap" id="receiveOrg"></td>
	    </tr>
	    <tr class="cssTable" >
	      <td align="right" valign="top" nowrap="nowrap">联系人：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input id="linkMan" name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input id="tel" name="tel" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	    </tr>
    </tbody>
    <TR class=cssTable id="payRemarkTR">
      <TD height="66" align="right"  nowrap>付款信息备注：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea id="payRemark" name="payRemark" rows="3" cols="80"></textarea></TD>
    </TR>
    <TR class=cssTable >
      <TD height="66" align="right"  nowrap>备注说明：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea id="orderRemark" name="orderRemark" rows="3" cols="80" onkeyup="chckSpecialTest(this);" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onchange="limitChars('orderRemark', 50)" ></textarea></TD>
    </TR>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
      	  <input type="hidden" id="total" name="total" value="0">
      	  <input type="hidden" id="year" name="year" value="${year}">
      	  <input type="hidden" id="week" name="week" value="${week}">
      	  <input type="hidden" id="isCheck" name="isCheck" value="${isCheck}">
      	  <input type="hidden" id="dealerLevel" name="dealerLevel" value="">
      	  <input type="hidden" id="submitType" name="submitType" value="">
      	  <input type="hidden" id="accountId" name="accountId" value="">
      	  <input type="hidden" id="discountAccountId" name="discountAccountId" value="">
		  <input type="hidden" id="ratePara" name="ratePara" value="${ratePara}" />
		  <input type="hidden" id="oldAddress" name="oldAddress" />
	      <input class="cssbutton" id="id1save" name="baocun" type="button" value="保存" onClick="confirmAdd('1');">
	      <input class="cssbutton" id="id1comit" name="tibao" type="button"  value="提报" onClick="confirmAdd('2');">
	      <input class="cssbutton" id="shangbao2" name="shangbao2" type="button" value="返回" onclick="history.back();" />
	      <!-- 资金类型是否是“兵财” -->
		  <input type="hidden" id="isBingcai" name="isBingcai" value="" />	
      </td>
    </tr>
 </table>
  <BR>
  <div id="addressDiv" style="display: none; border: solid #808080 1px; position: absolute"></div>
</form>
</body>
</html>
