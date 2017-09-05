<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购订单提报</title>
<script type="text/javascript">
function doInit(){
	getDealerLevel(document.getElementById("areaId").value);
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
	var fundTypeId = arg;
	
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
	makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
}	

function showAvailableAmount(json){
	var obj = document.getElementById("span4");
	obj.innerHTML = amountFormat(json.returnValue);
}

//获得地址列表
function getAddressList(){	
	var dealerId = document.getElementById("receiver").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
	makeCall(url,showAddressList,{dealerId:dealerId});
}	

function showAddressList(json){
	var obj = document.getElementById("deliveryAddress");
	obj.options.length = 0;
	for(var i=0;i<json.addressList.length;i++){
		obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
	}
	
	getAddressInfo(document.getElementById("deliveryAddress").value);
}

//获得联系人信息
function getAddressInfo(arg){
	var addressId = arg;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressInfo.json";
	makeCall(url,showAddressInfo,{addressId:addressId});
}	

function showAddressInfo(json){
	var obj1 = document.getElementById("linkMan");
	var obj2 = document.getElementById("tel");
	var obj3 = document.getElementById("receiveOrg");
	if(!json.info.LINK_MAN) {
		obj1.value = '' ;
	}
	if(!json.info.TEL) {
		obj2.value = '' ;
	}
	if(!json.info.RECEIVE_ORG) {
		obj3.innerHTML = '' ;
	}
	if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
		obj1.value = json.info.LINK_MAN;
	}
	if(json.info.TEL != null && json.info.TEL != "null"){
		obj2.value = json.info.TEL;
	}
	if(json.info.RECEIVE_ORG != null && json.info.RECEIVE_ORG != "null"){
		obj3.innerHTML = json.info.RECEIVE_ORG;
	}
}

//根据运输方式隐藏地址列表
function addressHide(arg){
	var obj1 = document.getElementById("addTr");
	var obj2 = document.getElementById("cusTr");
	if(arg == '<%=Constant.TRANSPORT_TYPE_02%>') {
		obj1.style.display = "inline";
		obj2.style.display = "none";
	}
	else if(arg == '<%=Constant.TRANSPORT_TYPE_03%>'){
		obj1.style.display = "none";
		obj2.style.display = "inline";
	}
	else{
		obj1.style.display = "none";
		obj2.style.display = "none";
	}
}

//根据经销商id，获得经销商级别，过滤地址列表、资金信息
function getDealerLevel(arg){
	document.getElementById("area").value = arg;
	var dealerId = arg.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:dealerId});
}

function showFund(json){
	document.getElementById("dealerLevel").value = json.returnValue;
	var myForm = document.getElementById("fm");
	if(json.returnValue != "1") {
		document.getElementById("span1").style.display = "none";
		document.getElementById("span2").style.display = "none";
		document.getElementById("span3").style.display = "none";
		document.getElementById("span4").style.display = "none";
		document.getElementById("span5").style.display = "none";
		document.getElementById("span6").style.display = "none";
		document.getElementById("span7").style.display = "none";
		document.getElementById("span8").style.display = "none";
		document.getElementById("span9").style.display = "none";
		document.getElementById("totalDiscountPrice").style.display = "none";
		document.getElementById("priceTr").style.display = "none";
		document.getElementById("discountTr").style.display = "none";
		document.getElementById("singleTdTitle").style.display = "none";
		document.getElementById("totalTdTitle").style.display = "none";
		document.getElementById("singleFoot").style.display = "none";
		document.getElementById("totalPrice").style.display = "none";
	}
	else{
		document.getElementById("span1").style.display = "inline";
		document.getElementById("span2").style.display = "inline";
		document.getElementById("span3").style.display = "inline";
		document.getElementById("span4").style.display = "inline";
		document.getElementById("span5").style.display = "inline";
		document.getElementById("span6").style.display = "inline";
		document.getElementById("span7").style.display = "inline";
		document.getElementById("span8").style.display = "inline";
		document.getElementById("span9").style.display = "inline";
		document.getElementById("totalDiscountPrice").style.display = "inline";
		document.getElementById("priceTr").style.display = "inline";
		document.getElementById("discountTr").style.display = "inline";
		document.getElementById("singleTdTitle").style.display = "inline";
		document.getElementById("totalTdTitle").style.display = "inline";
		document.getElementById("singleFoot").style.display = "inline";
		document.getElementById("totalPrice").style.display = "inline";
		
		showFundTypeList(json);//资金类型列表显示
		showDiscountInfo(json);//可用折让显示
		getPriceList();//获得价格类型列表
	}
	
	getReceiverList();//获得收货方列表
}

//资金类型列表显示
function showFundTypeList(json){
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
	}
	getAvailableAmount(document.getElementById('fundType').value);//获得账户余额
}

//可用折让显示
function showDiscountInfo(json){
	var discount = 0;
	for(var i=0;i<json.discountList.length;i++){
		discount = json.discountList[i].AVAILABLE_AMOUNT;
	}
	document.getElementById("discount").value = discount;
}

// 新增产品
function addMaterial(){	
	var priceId = document.getElementById("priceId").value;
	var materialCode = document.getElementById("materialCode").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/addMaterial.json";
	makeCall(url,addRow,{materialCode:materialCode,priceId:priceId}); 
}

// 删除产品
function delMaterial(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	totalPrice();
  	disableArea();
}

function addRow(json){	
	var level = document.getElementById("dealerLevel").value;
	if(level == "1"){
		if(parseFloat(json.info.PRICE, 10) < <%=Constant.MATERIAL_PRICE_MAX%>){	
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
			newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice("+json.info.PRICE+","+timeValue+");'><font color='red'>*</font>";
			newCell = newRow.insertCell(5);
			newCell.align = "center";
			newCell.innerHTML = "<span id='price"+timeValue+"'>"+amountFormat(json.info.PRICE)+"</span><input id='singlePrice"+timeValue+"' type='hidden' name='singlePrice"+timeValue+"' value='"+json.info.PRICE+"'>";
			newCell = newRow.insertCell(6);
			newCell.align = "center";
			newCell.innerHTML = "<span><input type='text' id='discount_rate"+timeValue+"' name='discount_rate"+timeValue+"' class='SearchInput' datatype='1,is_digit,6' size='2' maxlength='2' value='0' onchange='calculate(document.getElementById(\"singlePrice"+timeValue+"\").value,"+timeValue+");' />%</span>";
			newCell = newRow.insertCell(7);
			newCell.align = "center";
			newCell.innerHTML = "<span id='discount_s_price"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(8);
			newCell.align = "center";
			newCell.innerHTML = "<span id='discount_price"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(9);
			newCell.align = "center";
			newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(10);
			newCell.align = "center";
			newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>"+
								"<input type='hidden' id='discount_s_price_"+timeValue+"' name='discount_s_price_"+timeValue+"' readOnly='readOnly' size='10'  value='' />"+
								"<input type='hidden' id='discount_price_"+timeValue+"' name='discount_price_"+timeValue+"' readOnly='readOnly' size='10'  value='' />"+
								"<input type='hidden' id='discount_price_calcu"+timeValue+"' name='discount_price_' readOnly='readOnly' size='10'  value='' />"+
								"<input type='hidden' id='hejiPrice_"+timeValue+"' name='hejiPrice_' readOnly='readOnly' size='10'  value='' />";
								
		}
		else{
			MyAlert("该款物料尚未维护价格，不能添加！");
			return false;
		}
	}
	else{
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
		newCell.style.display = "none";
		newCell.align = "center";
		newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>";
		
	}
	disableArea();
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
	if(discount_rate - 10 >0){
		MyAlert("折扣率不能大于10%,请重新输入!");
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
	showMaterialByAreaId('materialCode','','false',areaId,ids);
}

//合计价格
function totalPrice(price,timeValue){
	if(price){
		calculate(price,timeValue);
	}
	var myForm = document.getElementById("fm");
	var totalAmount = 0;
	var totalPrice = 0;
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=6 && obj.id.substring(0,6)=="amount"){
			var subStr = obj.id.substring(6,obj.id.length);
			var value1 = parseInt(obj.value, 10);
			var value2 = parseFloat(document.getElementById("discount_s_price_"+subStr).value, 10);
			var heji = value1*value2;
			document.getElementById("hejiPrice"+subStr).innerHTML = amountFormat(heji);
			totalAmount += value1;
			totalPrice += heji;
		}   
	}  
	document.getElementById("totalAmount").innerHTML = totalAmount;
	document.getElementById("totalAmount_").value = totalAmount;
	
	document.getElementById("totalPrice").innerHTML = amountFormat(totalPrice);
	document.getElementById("span6").innerHTML = amountFormat(totalPrice);
	document.getElementById("total").value = round(totalPrice,2);
	
}
function confirmAdd(arg){
	if(submitForm('fm')){
		var totalDiscountPrice_ = document.getElementById("totalDiscountPrice_").value;		//折扣总额
		var discount = document.getElementById("discount").value;							//可用折让
		if(totalDiscountPrice_ - discount >0){
			MyAlert("折扣总额不能大于可用折让!");
			return;
		}
		
		document.getElementById("submitType").value = arg;
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=6 && obj.id.substring(0,6) == "amount"){
				var amount = parseInt(obj.value, 10);
				if(amount < 1){
					MyAlert("物料数量不能小于1！");
					return false;
				}
			}   
		} 
		var totalAmount = parseInt(document.getElementById("totalAmount").innerHTML, 10);
		if(totalAmount < 1){
			MyAlert("所选物料总数量不能小于1！");
			return false;
		}
		var isCover = document.getElementById("isCover").value;
		if("0"==isCover+"" || !isCover){
			if(document.getElementById("deliveryType").value == '<%=Constant.TRANSPORT_TYPE_02%>' && document.getElementById("deliveryAddress").value == ''){
				MyAlert("运送地址不能为空！");
				return false;
			}
		}
		
		MyConfirm("是否确认保存?",orderAdd);
	}
}

function orderAdd(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/balancecentermanage/stockordermanage/StockOrderReport/stockOrderReportAdd.json',showResult,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/balancecentermanage/stockordermanage/StockOrderReport/stockOrderReportInit.do';
	}else{
		MyAlert("提交失败！可用余额不足！");
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getPriceList.json";
	makeCall(url,showPriceList,{dealerId:dealerId});
}	

//显示价格类型列表
function showPriceList(json){
	var obj = document.getElementById("priceId");
	obj.options.length = 0;
	for(var i=0;i<json.priceList.length;i++){
		if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
			obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
			obj.options[i].selected = "selected";
		}
		else{
			obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
		}
	}
	priceTypeChange();
}

//获得收货方列表
function getReceiverList(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
	makeCall(url,showReceiverList,{dealerId:dealerId});
}	

//显示收货方列表
function showReceiverList(json){
	var obj = document.getElementById("receiver");
	obj.options.length = 0;
	for(var i=0;i<json.receiverList.length;i++){
		obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
	}
	getAddressList();//获得发运地址列表
}

// 判断是否显示使用其他价格原因
function isShowOtherPriceReason(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var priceId = document.getElementById("priceId").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/isShowOtherPriceReason.json";
	makeCall(url,showOtherPriceReason,{dealerId:dealerId,priceId:priceId});
}	

//使用其他价格原因
function showOtherPriceReason(json){
	if(json.returnValue == "1"){
		document.getElementById("span7").style.display = "inline";
		document.getElementById("span8").style.display = "inline";
	}
	else{
		document.getElementById("span7").style.display = "none";
		document.getElementById("span8").style.display = "none";
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getSinglePriceList.json";
	makeCall(url,priceChange,{priceId:priceId, ids:ids});
}	

//价格类型改变处理
function priceChange(json){
	for(var i=0;i<json.priceList.length;i++){
		var id = json.priceList[i].MATERIAL_ID;
		var price = json.priceList[i].PRICE;
		
		var obj1 = document.getElementById("price" + id);
		obj1.innerHTML = amountFormat(price);
		
		var obj2 = document.getElementById("singlePrice" + id);
		obj2.value = price;
		calculate(price,id);
	}
	isShowOtherPriceReason();
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
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 采购订单管理 > 采购订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align="center">
    <tr class= "tabletitle">
      <td align = "right" >业务范围：</td>
      <td align = "left" >
      	<select name="areaId" onchange="getDealerLevel(this.options[this.options.selectedIndex].value);">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
        <input type="hidden" name="area" value="">
      </td>
      <td align = "right"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      	</select>
      	</span>
      </td>
      <td align = "right"><span id="span3">可用余额：</span></td>
      <td align = "left"><span id="span4" class="STYLE2"></span></td>
      <td align="right"><span id="span5">订单总价：</span></td>
      <td align = "left"><span id="span6" class="STYLE2">0</span></td>
    </tr>
    <tr id="priceTr" class = "tabletitle">
      <td align = "right" >价格类型：</td>
      <td align = "left" >
      	<select name="priceId" onchange="priceTypeChange();">
        </select>
      </td>
      <td align = "right"><span id="span7">使用其他价格原因：</span></td>
      <td align = "left" colspan="5">
      	<span id="span8"><input name="otherPriceReason" type="text" class="long_txt"/></span>
      </td>
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
      <th nowrap="nowrap" id="span7">折扣率%</th>
      <th nowrap="nowrap" id="span8">折扣后单价</th>
      <th nowrap="nowrap" id="span9">折扣额</th>
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
      <td align="right" nowrap="nowrap" id="totalDiscountPrice" colspan="3">0</td>
      <td align="center" nowrap="nowrap" id="totalPrice">0</td>
      <td nowrap="nowrap" >
      	<input type='hidden' id='totalDiscountPrice_' name='totalDiscountPrice_' readOnly='readOnly' size='10'  value='' />
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
      <th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class="cssTable" id="discountTr">
      <td width="12%" align="right" valign="top" nowrap="nowrap">可用折让：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" value="0" datatype="1,is_double,20" decimal="2" readonly="readonly"/>
      </td>
    </tr>
    <tr class="cssTable" id="discountTr">
      <td width="12%" align="right" valign="top" nowrap="nowrap">是否代交车：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      </td>
    </tr>
    <tr class="cssTable" id="cusTr" style="display:none">
      <td align="right" nowrap="nowrap">选择集团客户：</td>
      <td align="left" nowrap="nowrap">
	      <input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30"/>
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
	      	genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'');
	      </script>
      </td>
    </tr>
    <tbody id="addTr">
	    <tr class="cssTable">
	      <td align="right" nowrap="nowrap">收货方：</td>
	      <td align="left" nowrap="nowrap">
		      <select name="receiver" onchange="getAddressList();">
		      </select>
		  </td>
	      <td colspan="2" align="left" nowrap="nowrap">&nbsp;</td>
	    </tr>
	    <tr class="cssTable" style="display:inline">
	      <td align="right" nowrap="nowrap">运送地址：</td>
	      <td align="left" nowrap="nowrap">
		      <select name="deliveryAddress" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
		      </select><!--<span id="linkInfo"></span>-->
		  </td>
	      <td align="right" valign="top" nowrap="nowrap">收车单位：</td>
	      <td align="left" valign="top" nowrap="nowrap" id="receiveOrg"></td>
	    </tr>
	    <tr class="cssTable" >
	      <td align="right" valign="top" nowrap="nowrap">联系人：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input name="tel" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	    </tr>
    </tbody>
    
    <TR class=cssTable >
      <TD height="66" align="right"  nowrap>备注说明：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea name="orderRemark" rows="3" cols="80"></textarea></TD>
    </TR>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
      	  <input type="hidden" name="total" value="0">
      	  <input type="hidden" name="year" value="${year}">
      	  <input type="hidden" name="week" value="${week}">
      	  <input type="hidden" name="isCheck" value="${isCheck}">
      	  <input type="hidden" name="dealerLevel" value="">
      	  <input type="hidden" name="submitType" value="">
	      <input class="cssbutton" name="baocun" type="button"  value="保存" onClick="confirmAdd('1');">
	      <input class="cssbutton" name="baocun" type="button"  value="提报" onClick="confirmAdd('2');">
	      <input class="cssbutton" name="shangbao2" type="button" value="返回" onclick="history.back();" />
      </td>
    </tr>
 </table>
  <BR>
</form>
</body>
</html>
