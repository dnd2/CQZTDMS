<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String level = (String)request.getAttribute("level"); 
	List detailList = (List)request.getAttribute("detailList");
	int dSize = detailList.size();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>补充订单提报</title>
<script type="text/javascript">

function doInit(){

	if('${order.deliveryType}' == '<%=Constant.TRANSPORT_TYPE_02%>'){
		document.getElementById("flag").value = "0";
	}
	getDealerLevel(document.getElementById("areaId").value);
	totalPrice();
	addressHide(document.getElementById("deliveryType").value);
	disableArea();
	if('${isFleet}'+"" == "1" ){
		document.getElementById("isCover").checked = true;
		document.getElementById("cusTr").style.display = "inline";
		document.getElementById("addTr").style.display = "none";
		document.getElementById("isCover").value = 1;
		document.getElementById("tran_id").style.display = "none";
	}
}

function disableArea(){
	var rowsnum = document.getElementById("tbody1").rows.length;
	if(rowsnum != 0){
		document.getElementById('areaId').disabled = "disabled";
	}
	else{
		document.getElementById('areaId').disabled = "";
	}
}

function getAvailableAmount(arg){
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	var fundTypeId = fundTypes[0];
	if(fundTypes[0]==null||"null"==fundTypes[0]||""==fundTypes[0]){
		MyAlert("erp和dms账户类型不匹配，请反映情况！！");
		document.getElementById("id1save").disabled="disabled";
		document.getElementById("id1comit").disabled="disabled";
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
	obj.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
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

function getAddressList(){	
	var dealerId = document.getElementById("receiver").value;
	var areaId = document.getElementById("area").value.split("|")[0] ;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAddressList.json";
	makeCall(url,showAddressList,{dealerId:dealerId,areaId:areaId});
}	

function showAddressList(json){
	var obj = document.getElementById("deliveryAddress");
	obj.options.length = 0;
	for(var i=0;i<json.addressList.length;i++){
		obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
		if(json.addressList[i].ID + "" == '${order.deliveryAddress}'){
			obj.options[i].selected = true;
		}
	}
	
	getAddressInfo(document.getElementById("deliveryAddress").value);
}

function getAddressInfo(arg){
	var addressId = arg;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAddressInfo.json";
	makeCall(url,showAddressInfo,{addressId:addressId});
}	

function showAddressInfo(json){
	var obj1 = document.getElementById("linkMan");
	var obj2 = document.getElementById("tel");
	var obj3 = document.getElementById("receiveOrg");
	
	if(document.getElementById("flag").value == "0"){
		document.getElementById("flag").value = "1";
	}
	else{
		if(!json.info.LINK_MAN) {
			obj1.value = '' ;
		}
		if(!json.info.TEL) {
			obj2.value = '' ;
		}
		if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
			obj1.value = json.info.LINK_MAN;
		}
		if(json.info.TEL != null && json.info.TEL != "null"){
			obj2.value = json.info.TEL;
		}
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

function priceOnBlue(thisRate,timeValue){
	var discountRate = thisRate.value;
	if(${returnValue==1}){
		if(discountRate!=0 && discountRate!=15){
			thisRate.value=0;
			document.getElementById("discount_price"+timeValue).value = 0;
			document.getElementById("discount_price_"+timeValue).innerHTML = 0;
			document.getElementById("discount_totalPrice").innerHTML=0;
			document.getElementById("discount_totalPrice_").value=0;
			MyAlert("微车的折扣率只能是0和15");
		}
	}
	if(${returnValue==2}){
	if(discountRate!=0 && discountRate!=10){
		thisRate.value=0;
		document.getElementById("discount_price"+timeValue).value = 0;
		document.getElementById("discount_price_"+timeValue).innerHTML = 0;
		document.getElementById("discount_totalPrice").innerHTML=0;
		document.getElementById("discount_totalPrice_").value=0;
		MyAlert("轿车的折扣率只能是0和10");
	    }
    }
}

function getDealerLevel(arg){
	document.getElementById("area").value = arg;
	getReceiverList();//获得收货方列表
	var dealerId = arg.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:dealerId});
}

function showFund(json){
	dSize = document.getElementById("tbody1").rows.length ;
	document.getElementById("dealerLevel").value = json.returnValue;
	var myForm = document.getElementById("fm");
	if(json.returnValue != "1") {
		document.getElementById("span1").style.display = "inline";
		document.getElementById("span2").style.display = "inline";
		document.getElementById("span3").style.display = "none";
		document.getElementById("span4").style.display = "none";
		document.getElementById("span5").style.display = "none";
		document.getElementById("span6").style.display = "none";
//		document.getElementById("span7").style.display = "none";
//		document.getElementById("span8").style.display = "none";
//		document.getElementById("span9").style.display = "none";
		document.getElementById("priceTr").style.display = "none";
		document.getElementById("discountTr").style.display = "none";
		document.getElementById("singleTdTitle").style.display = "none";
		document.getElementById("totalTdTitle").style.display = "none";
		document.getElementById("singleFoot").style.display = "none";
		document.getElementById("totalPrice").style.display = "none";
		for(var i=0;i<dSize;i++){
			document.getElementById("span12"+i).style.display = "none";
			document.getElementById("span13"+i).style.display = "none";
			document.getElementById("span14"+i).style.display = "none";
		}
		document.getElementById("td1").style.display = "none";
		document.getElementById("td2").style.display = "none";
		document.getElementById("discount_totalPrice").style.display = "none";
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
//		document.getElementById("span7").style.display = "inline";
//		document.getElementById("span8").style.display = "inline";
		//document.getElementById("span9").style.display = "inline";
		document.getElementById("priceTr").style.display = "inline";
//	   document.getElementById("discountTr").style.display = "inline";
		document.getElementById("singleTdTitle").style.display = "inline";
		document.getElementById("totalTdTitle").style.display = "inline";
		document.getElementById("singleFoot").style.display = "inline";
		document.getElementById("totalPrice").style.display = "inline";
			
//		for(var i=0;i<dSize;i++){
//			document.getElementById("span12"+i).style.display = "inline";
//			document.getElementById("span13"+i).style.display = "inline";
//			document.getElementById("span14"+i).style.display = "inline";
//		}
//		document.getElementById("td1").style.display = "inline";
//		document.getElementById("td2").style.display = "inline";
	//	document.getElementById("discount_totalPrice").style.display = "inline";
		showFundTypeList(json);//资金类型列表显示
	//	showDiscountInfo(json);//可用折让显示
	//	getPriceList();//获得价格类型列表
		document.getElementById("payRemarkTR").style.display = "none";
	}
}

//资金类型列表显示
function showFundTypeList(json){
	
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		if(json.fundTypeList[i].TYPE_ID==null){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, "null"+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}else{
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}
		//obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		if(json.fundTypeList[i].TYPE_ID + "" == '${order.fundTypeId}'){
			obj.options[i].selected = true;
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
	var areaId = area.split("|")[0];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/addMaterial.json";
	makeCall(url,addRow,{materialCode:materialCode,priceId:priceId,areaId:areaId}); 
}

function delMaterial(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	totalPrice();
  	disableArea();
  	_setSelDisabled_("tbody1", 0) ;
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

function addRow(json){	
	var level = document.getElementById("dealerLevel").value;
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
			newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice();'><font color='red'>*</font>";
			newCell = newRow.insertCell(5);
			newCell.align = "center";
			newCell.innerHTML = "<span id='price"+timeValue+"'>"+amountFormat(json.infos.OPERAND)+"</span><input id='singlePrice"+timeValue+"' type='hidden' name='singlePrice"+timeValue+"' value='"+json.infos.OPERAND+"'>";

		//	newCell = newRow.insertCell(6);
		//	newCell.align = "center";
		//	newCell.innerHTML = "<span style='display:none;'><input type='text' id='discount_rate"+timeValue+"' name='discount_rate"+timeValue+"' class='SearchInput' datatype='0,is_digit,6' size='2' maxlength='2' value='0' onchange='totalPrice();' />%</span>";

		//	newCell = newRow.insertCell(7);
		//	newCell.align = "center";
		//	newCell.innerHTML = "<span  style='display:none;' id='discount_s_price_"+timeValue+"'></span><input id='discount_s_price"+timeValue+"' type='hidden' name='discount_s_price"+timeValue+"' value=''>";

		//	newCell = newRow.insertCell(8);
		//	newCell.align = "center";
		//	newCell.innerHTML = "<span style='display:none;' id='discount_price_"+timeValue+"'></span><input id='discount_price"+timeValue+"' type='hidden' name='discount_price"+timeValue+"' value=''>";
			

			newCell = newRow.insertCell(6);
			newCell.align = "center";
			newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(7);
			newCell.align = "center";
			newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>  <input type='hidden' id='priceListId"+timeValue+"' name='priceListId"+timeValue+"' value='"+json.infos.LIST_HEADER_ID+"'/>";
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
		newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice();'><font color='red'>*</font>";
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
	_setSelDisabled_("tbody1", 0) ;
}

function materialShow(){
	if(!_getTip_()) {
		return false ;
	} 
	var ids = "";
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
function round(number,fractionDigits){   
	   with(Math){   
	       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
	    }   
} 
function totalPrice(){

	var level = document.getElementById("dealerLevel").value;
	var ratePara = parseInt(document.getElementById("ratePara").value, 10);
	var myForm = document.getElementById("fm");
	var totalAmount = 0;
	var totalPrice = 0;
//	var discount_totalPrice = 0;
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=6 && obj.id.substring(0,6)=="amount"){
			var subStr = obj.id.substring(6,obj.id.length);
			var value1 = parseInt(obj.value, 10);												//数量
			var value2 = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);	//单价
			var value3 = 0;
			var value4 = 0;
//			if(level-1==0){
//				value3 = parseFloat(document.getElementById("discount_rate"+subStr).value);		//折扣率
//				value4 = parseFloat(value2*(100-value3)/100);									//折扣后单价
//				if(value3 - ratePara >0){
//					MyAlert("折扣率不能大于"+ratePara+"%");
//					value3 =0;
//					document.getElementById("discount_rate"+subStr).value = 0;
					value4 = value2;
//				}
//			}else{
//				value4 = value2;
//			}
			var heji = value1*value4;
			document.getElementById("hejiPrice"+subStr).innerHTML = amountFormat(heji);
//			if(level-1==0){
//				document.getElementById("discount_s_price_"+subStr).innerHTML = amountFormat(value4);
//				document.getElementById("discount_s_price"+subStr).value = round(value4,2);
//				var value5 = (value1*value2*value3)/100;											//折扣额
//				document.getElementById("discount_price_"+subStr).innerHTML = amountFormat(value5);
//				document.getElementById("discount_price"+subStr).value = round(value5,2);
//				discount_totalPrice += value5;
//			}
			totalAmount += value1;
			totalPrice += heji;
			
		}   
	}  
	
	document.getElementById("totalAmount").innerHTML = totalAmount;
	document.getElementById("totalPrice").innerHTML = amountFormat(totalPrice);
	document.getElementById("totalAmount_").value = totalAmount;
	document.getElementById("span6").innerHTML = amountFormat(totalPrice);
	document.getElementById("total").value = round(totalPrice,2);
	//document.getElementById("discount_totalPrice").innerHTML = amountFormat(discount_totalPrice);
	//document.getElementById("discount_totalPrice_").value = round(discount_totalPrice,2);
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
	if(submitForm('fm')){
		if(document.getElementById('tmp_license_amount').value.length==0) {
				MyAlert('临牌数量不能为空。') ; 
				return false ;
		}
		if(!_getTip_()) {
			return false ;
		}
		
		var totalDiscountPrice_ = document.getElementById("discount_totalPrice_").value;		//折扣总额
		var discount = document.getElementById("discount").value;								//可用折让
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
		var isCover = document.getElementById("isCover").value;
		if("0"==isCover+"" || !isCover){
			if(document.getElementById("deliveryType").value == '<%=Constant.TRANSPORT_TYPE_02%>' && document.getElementById("deliveryAddress").value == ''){
				//MyAlert("运送地址不能为空！");
				//return false;
			}
		}
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
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/urgentOrderReoprtMod.json',showResult,'fm');
}

function userValidate() {
	var userId = this.parent.document.getElementById('userId').value;
	var sessionId = this.parent.document.getElementById('sessionId').value;
	makeCall('<%=request.getContextPath()%>/util/UserValidator/validate.json', orderAdd , {userId : userId, sessionId : sessionId});
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/doInit.do';
	}else  if(json.returnValue == '2'){
		MyAlert("操作失败！单据状态不对！");
	}else{
		MyAlert("操作失败！可用余额不足！");
	}
}

function writeDesc(value){
	var desc = "无";
	if(value > 0){
		desc = "有";
	}
 	document.write(desc);
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
	var obj = document.getElementById("priceId");
	obj.options.length = 0;
	for(var i=0;i<json.priceList.length;i++){
		if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
			obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
			//if('${order.priceId}' == ''){
				obj.options[i].selected = "selected";
			//}
		}
		else{
			obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
		}
		//if(json.priceList[i].PRICE_ID + "" == '${order.priceId}'){
			//obj.options[i].selected = "selected";
		//}
	}
	priceTypeChange();
}

//获得收货方列表
function getReceiverList(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
	var areaId = areaObj.value.split("|")[0];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getReceiverListInNull.json";
	makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
}	

//显示收货方列表
function showReceiverList(json){
	var obj = document.getElementById("receiver");
	obj.options.length = 0;
	for(var i=0;i<json.receiverList.length;i++){
		obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
		if(json.receiverList[i].DEALER_ID + "" == '${order.receiver}'){
			obj.options[i].selected = "selected";
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
		var obj3 = document.getElementById("amount" + id) ;
		
		if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
			price = 0;
			obj1.innerHTML = "价格未维护";
			obj3.value = 0 ;
			obj3.readOnly = true ;
		}
		else{
			obj1.innerHTML = amountFormat(price);
		}
		obj2.value = price;
	}
	isShowOtherPriceReason();//是否显示使用其他价格原因
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
		//delMaterial();
		delTboby();
	}else{
		arg.options[lastIndex].selected=true; 
	}		
}
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lock.js"></script>
</head>
<body onunload="destoryLock();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 电商订单修改</div>
<form method="POST" name="fm" id="fm">
	<input type="hidden" id="billId" name="reqId" value="${billId}"/>
	<input type="hidden" id="sessionId" name="sessionId" value="${sessionId}"/>
	<input type="hidden" value="<%=request.getContextPath()%>" id="contextPahts" name="contextPahts"/>
  <table class="table_query" align="center">
    <tr class= "tabletitle">
      <td align = "right" style="color:#252525;width:70px;text-align:right;">业务范围：</td>
      <td align = "left" >
      	<input type="hidden" name="flag" value="1">
      	<select name="areaId" onchange="getDealerLevel(this.options[this.options.selectedIndex].value);">
			<c:forEach items="${areaList}" var="po">
				<c:choose>
					<c:when test="${po.AREA_ID == order.areaId}">
						<option value="${po.AREA_ID}|${po.DEALER_ID}" selected="selected">${po.AREA_NAME}</option>
					</c:when>
					<c:otherwise>
						<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:otherwise>
				</c:choose> 
			</c:forEach>
        </select>
        <input type="hidden" name="area" value="${order.areaId }">
      </td>
      <td id="_productControl_">
      	<script type="text/javascript">
      		productStart("<%=request.getContextPath()%>", '${order.productComboId}', true, true) ;
      	</script>
      </td>
      <td align = "right" style="color:#252525;width:90px;text-align:right;"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
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
	      <label>${priceId}</label>
	      <input type="hidden" name="priceId" id="priceId" class="long_txt" value="${priceId}" readOnly="readOnly"/>
<!--	      	<select name="priceId" onchange="priceTypeChange();">-->
<!--	        </select>-->
	      </td>
	      <td align = "right" style="color:#252525;width:90px;text-align:right;">订单类型：</td>
	      <td align = "left" colspan="1">
	      	<select id="orderType" name="orderType" onclick="saveLast()" onchange="changeOrderType(this);">
                        <option value="<%=Constant.ORDER_TYPE_06%>" selected="selected">电商订单</option>
	        </select></td>
	      <td align = "right" style="color:#252525;width:70px;text-align:right;"><span id="span3">可用余额：</span></td>
	      <td align = "left" ><span id="span4" ></span></td>
	      <td align="right" style="color:#252525;width:70px;text-align:right;"><span id="span5">订单总价：</span></td>
	      <td align = "left"><span id="span6">0</span></td>	        
	    </tr>
	    <tr id="otherTr" class = "tabletitle" style="display:none;">
	      <td align = "right">使用其他价格原因：</td>
	      <td align = "left" colspan="7">
	      	<input name="otherPriceReason" type="text" class="long_txt" value="${order.otherPriceReason}"/>
	      </td>
	    </tr>
    </tbody>
    <tr  class= "tabletitle">
	    <td align="right" nowrap style="color:#252525;width:70px;text-align:right;">订单周度：   </td>
	    <td  class="table_query_2Col_input" nowrap>
		    <select name="orderWeek" id="orderYearWeek" onchange="showData(this.value);">
		      <c:forEach items="${dateList}" var="po"> 
		   <c:choose>   
    			<c:when test="${myWeek==po.code}" >   
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
    <tbody id="tbody1">
    	<%
			int i =0;
		%>
    	<c:forEach items="${detailList}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}<input type="hidden" name="mid" value="${po.MATERIAL_ID}" /></td>
		      <td align="center">${po.MATERIAL_CODE}</td>
		      <td align="center">${po.MATERIAL_NAME}</td>
		      <td align="center"><script>writeDesc(${po.RESOURCE_AMOUNT})</script></td>				
		      <td align="center"><input id='amount${po.MATERIAL_ID}' name='amount${po.MATERIAL_ID}' type='text' class='SearchInput' datatype='0,is_digit,6' value='${po.ORDER_AMOUNT}' size='2' maxlength='6' onchange='totalPrice();'></td>
		      <td align="center" style="display:<%=level.equals("1")?"inline":"none" %>">
			      <span id='price${po.MATERIAL_ID}'>
				      <script type="text/javascript">document.write(amountFormat(${po.SINGLE_PRICE}));</script>
			      </span>
				  <input type='hidden' id='singlePrice${po.MATERIAL_ID}' name='singlePrice${po.MATERIAL_ID}' value='${po.SINGLE_PRICE}'>
		      </td>
		      <!-- 折扣率 -->
<!--		      <td align="center" id="span12<%=i %>" style="display:none;">-->
<!--		      	<input type="text" name="discount_rate${po.MATERIAL_ID}" id="discount_rate${po.MATERIAL_ID}" value="${po.DISCOUNT_RATE}" datatype='1,is_digit,6' size='2' maxlength='2' onchange="priceOnBlue(this,${po.MATERIAL_ID});totalPrice();" />%-->
<!--		      </td>-->
		      <!-- 折扣后单价 -->
<!--		      <td align="center" id="span13<%=i %>" style="display:none;">-->
<!--		      	<span id="discount_s_price_${po.MATERIAL_ID}">${po.DISCOUNT_S_PRICE}</span>-->
<!--		      	<input type="hidden" id="discount_s_price${po.MATERIAL_ID}" name="discount_s_price${po.MATERIAL_ID}" value="${po.DISCOUNT_S_PRICE}" />-->
<!--		      </td>-->
		      <!-- 折扣额 -->
<!--		      <td align="center" id="span14<%=i %>" style="display：none;">-->
<!--		      	<span id="discount_price_${po.MATERIAL_ID}">${po.DISCOUNT_PRICE}</span>-->
<!--		      	<input type="hidden" id="discount_price${po.MATERIAL_ID}" name="discount_price${po.MATERIAL_ID}" value="${po.DISCOUNT_PRICE}" />-->
<!--		      </td>-->
		      
		      
		      <td align="center" style="display:<%=level.equals("1")?"inline":"none" %>"><span id='hejiPrice${po.MATERIAL_ID}'>${po.TOTAL_PRICE}</span></td>
		      <td align="center"><a href='#' onclick='delMaterial();'>[删除]</a><input type="hidden" id="priceListId${po.MATERIAL_ID}" name="priceListId${po.MATERIAL_ID}"  value="${po.PRICE_LIST_ID}"/><input type='hidden' id='materialId${po.MATERIAL_ID}' name='materialId${po.MATERIAL_ID}' value='${po.MATERIAL_ID}'></td>
		    </tr>
		     <%
		      	i=i+1;
		    %>
    	</c:forEach>
    </tbody>
     <tr class="table_list_row1" id="totalTr">
      <td nowrap="nowrap"  >&nbsp;</td>
      <td nowrap="nowrap" >&nbsp;</td>
      <td align="right" nowrap="nowrap"  ><strong>总计： </strong></td>
      <td align="center" nowrap="nowrap" >&nbsp;</td>
      <td align="center" nowrap="nowrap" id="totalAmount">${totalCount}</td>
      <td align="center" nowrap="nowrap" id="singleFoot">&nbsp;</td>
<!--      <td id="td1"></td>-->
<!--	  <td id="td2"></td>-->
<!--	  <td align="center" nowrap="nowrap" id="discount_totalPrice"></td>-->
      <td align="center" nowrap="nowrap" id="totalPrice">${order.orderPrice}</td>
      <td nowrap="nowrap" >
      	<input type="hidden" name="discount_totalPrice_" id="discount_totalPrice_" value="0" />
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
  <table class="table_list" style="border-bottom:1px solid #DAE0EE;display:none;"  >
    <tr class="table_list_row1">
      <th  align="center" nowrap="nowrap" >日期</th>
      <th align="center" nowrap="nowrap"  >单位</th>
      <th align="center" nowrap="nowrap"  >操作人</th>
      <th align="center" nowrap="nowrap"  >审核结果</th>
      <th align="center" nowrap="nowrap"  >审核描述</th>
    </tr>
  	<c:forEach items="${checkList}" var="po">
  		<tr class="table_list_row1">
	      <td align="center" nowrap="nowrap" class="table_list_row1" >${po.CHECK_DATE}</td>
	      <td align="center" nowrap="nowrap" class="table_list_row1"  >${po.ORG_NAME}</td>
	      <td align="center" nowrap="nowrap" class="table_list_row1"  >${po.NAME}</td>
	      <td align="center" nowrap="nowrap" class="table_list_row1"  ><script type="text/javascript">writeItemValue(${po.CHECK_STATUS})</script></td>
	      <td align="center" nowrap="nowrap"  >${po.CHECK_DESC}</td>
	    </tr>
  	</c:forEach>
  </table>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <TABLE class=table_query>
    <tr>
      <th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class="cssTable" id="tmp_license_amountTr">
      <td width="12%" align="right" valign="top" nowrap="nowrap">临牌数量：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input id="tmp_license_amount" name="tmp_license_amount" type="text" class="middle_txt" size="10" maxlength="30" value="${order.tmpLicenseAmount}" datatype="1,is_double,20" decimal="2"/>
      </td>
    </tr>
    <tr class="cssTable" id="discountTr" style="display:none;">
      <td width="12%" align="right" valign="top" nowrap="nowrap" style="display:none;">可用折让：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap" style="display:none;">
      	<input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" value="0" datatype="1,is_double,20" decimal="2" readonly="readonly"/>
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
	      <input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30" value="${fleetName}" />
		  <input id="fleetId" name="fleetId" type="hidden" value="${fleetId }"/>				
		  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
		  <input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
	  </td>
      <td align="right" nowrap="nowrap">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap">
	      <input type="text" id="fleetAddress" name="fleetAddress" size="50" datatype="0,is_noquotation,50" value="${fleetAddress }" />
	  </td>
    </tr>
    
    <tr class="cssTable" id="tran_id">
      <td align="right"   nowrap="nowrap">运输方式： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      <script type="text/javascript">
	      	genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,${order.deliveryType},false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
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
	      <td align="left" valign="top" nowrap="nowrap"><input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" value="${order.linkMan}"/></td>
	      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input name="tel" type="text" class="middle_txt" size="30" maxlength="30" value="${order.tel}"/></td>
	    </tr>
    </tbody>
    <!--<tr class="cssTable" id="addTr" style="display:inline">
      <td align="right" nowrap="nowrap">运送地址：</td>
      <td align="left" nowrap="nowrap">
	      <select name="deliveryAddress" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
	      </select><span id="linkInfo"></span>
	  </td>
      <td colspan="2" align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
    -->
    <TR class=cssTable id="payRemarkTR">
      <TD height="66" align="right"  nowrap>付款信息备注：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea id="payRemark" name="payRemark" rows="3" cols="80">${order.payRemark}</textarea></TD>
    </TR>
    <TR class=cssTable >
      <TD height="66" align="right"  nowrap>备注说明：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea name="orderRemark" rows="3" cols="80">${order.orderRemark}</textarea></TD>
    </TR>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
      	  <input type="hidden" name="total" value="${order.orderPrice}" />
      	  <input type="hidden" name="orderId" value="${order.orderId}" />
      	  <input type="hidden" name="isCheck" value="${isCheck}" />
      	  <input type="hidden" name="dealerLevel" value="" />
      	  <input type="hidden" name="submitType" value="" />
      	  <input type="hidden" name="accountId" value="" />
      	  <input type="hidden" name="discountAccountId" value="" />
      	  <input type="hidden" name="orderNO" value="${orderNO }" />
		  <input type="hidden" name="ratePara" value="${ratePara}" />
	      <input class="cssbutton" name="baocun" id="id1save" type="button"  value="保存" onClick="confirmAdd('1');" />
	      <input class="cssbutton" name="tibao" id="id1comit"type="button"  value="提报" onClick="confirmAdd('2');" />
	      <input class="cssbutton" name="shangbao2" type="button" value="返回" onclick="history.back();" />
	       <!-- 资金类型是否是“兵财” -->
		  <input type="hidden" id="isBingcai" name="isBingcai" value="" />	
      </td>
    </tr>
 </table>
  <BR>
</form>
</body>
</html>
