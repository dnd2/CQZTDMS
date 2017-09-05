<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	List detailList = (List)request.getAttribute("detailList");
	int dSize = detailList.size();
	int transport_type_01 = Constant.TRANSPORT_TYPE_01;
%>
<title>补充订单预审</title>
<script type="text/javascript">
function doInit(){
	getDealerLevel(document.getElementById("areaId").value);
	getAddressInfo(${order.deliveryAddress});
	//getAvailableAmount(document.getElementById('fundType').value);
	disableArea();
	var isFleet = '${is_fleet}';
	addressHide(isFleet);
	var deliverytype = '${deliverytype}';
	var transport_type_01 = <%=transport_type_01%>;
	if(deliverytype-transport_type_01==0){
		document.getElementById("trId_1").style.display = "none";
		document.getElementById("trId_2").style.display = "none";
	}
}
function round(number,fractionDigits){   
	   with(Math){   
	       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
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
	var orderId = ${orderId } ;
	document.getElementById("isBingcai").value = fundTypes[1];
	
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
	makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId,orderId:orderId});
}	

function showAvailableAmount(json){
	var obj = document.getElementById("span4");
	obj.innerHTML = amountFormat(json.returnValue);
	document.getElementById("accountId").value = json.accountId;
}

function getAddressInfo(arg){
	var addressId = arg;
	var orderId = '${orderId}'
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderPrepCheck/getAddressInfo.json";
	makeCall(url,showAddressInfo,{addressId:addressId,orderId:orderId});
}	
function objToStr(itemValue){
	itemValue = itemValue==null?"":itemValue;
	itemValue = itemValue=='null'?"":itemValue;
	return itemValue;
}
function showAddressInfo(json){
	var noAddressId = json.noAddressId;
	if("1"==noAddressId+""){
		document.getElementById("trId_1").style.display = "none";
		document.getElementById("trId_2").style.display = "none";
	}else{
		var obj1 = document.getElementById("addTd1");
		var obj2 = document.getElementById("addTd2");
		var obj3 = document.getElementById("addTd3");
		var obj4 = document.getElementById("addTd4");
		obj1.innerHTML = objToStr(json.ADDRESS);
		obj2.innerHTML = objToStr(json.RECEIVE_ORG);
		obj3.innerHTML = objToStr(json.LINK_MAN);
		obj4.innerHTML = objToStr(json.TEL);
	}
}

function addressHide(isFleet){
	if("1"==isFleet+"" ){
		document.getElementById("cusTr").style.display = "inline";
		document.getElementById("tranId").style.display = "none";
	}else{
		document.getElementById("cusTr").style.display = "none";
		document.getElementById("tranId").style.display = "inline";
	}
}

function getDealerLevel(arg){
	document.getElementById("area").value = arg;
	var dealerId = arg.split("|")[1];
	
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:dealerId});
}

function showFund(json){
	var dSize = '<%=dSize%>';
	document.getElementById("level").value = json.returnValue;
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
		document.getElementById("span10").style.display = "none";
		document.getElementById("span11").style.display = "none";
		document.getElementById("singleTh").style.display = "none";
		document.getElementById("hejiTh").style.display = "none";
		for(var i=0;i<dSize;i++){
			document.getElementById("span12"+i).style.display = "none";
			document.getElementById("span13"+i).style.display = "none";
			document.getElementById("span14"+i).style.display = "none";
			document.getElementById("singleTd"+i).style.display = "none";
			document.getElementById("hejiTd"+i).style.display = "none";
		}
		document.getElementById("span15").style.display = "none";
		document.getElementById("span16").style.display = "none";
		document.getElementById("span17").style.display = "none";
		document.getElementById("span18").style.display = "none";
		document.getElementById("discountTr").style.display = "none";
		document.getElementById("singleTotal").style.display = "none";
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
		document.getElementById("span10").style.display = "inline";
		document.getElementById("span11").style.display = "inline";
		document.getElementById("singleTh").style.display = "inline";
		document.getElementById("hejiTh").style.display = "inline";
		for(var i=0;i<dSize;i++){
			document.getElementById("span12"+i).style.display = "inline";
			document.getElementById("span13"+i).style.display = "inline";
			document.getElementById("span14"+i).style.display = "inline";
			document.getElementById("singleTd"+i).style.display = "inline";
			document.getElementById("hejiTd"+i).style.display = "inline";
		}
		document.getElementById("span15").style.display = "inline";
		document.getElementById("span16").style.display = "inline";
		document.getElementById("span17").style.display = "inline";
		document.getElementById("span18").style.display = "inline";
		document.getElementById("discountTr").style.display = "inline";
		document.getElementById("singleTotal").style.display = "inline";
		document.getElementById("totalPrice").style.display = "inline";
		
		showFundTypeList(json);//资金类型列表显示
		showDiscountInfo(json);//可用折让显示
		getPriceList();//获得价格类型列表
	}
}

//资金类型列表显示
function showFundTypeList(json){
	var obj = document.getElementById("fundType");
	var old_fundTypeId = '${order.fundTypeId}';
	document.getElementById("old_fundTypeId").value = old_fundTypeId;
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
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

//获得价格类型列表
function getPriceList(){	
	var dealerId = document.getElementById("area").value.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getPriceList.json";
	makeCall(url,showPriceList,{dealerId:dealerId});
}	
//显示价格类型列表
function showPriceList(json){
	
	var obj = document.getElementById("priceId");
	obj.options.length = 0;

	if(${orderType} == <%=Constant.ORDER_TYPE_03 %>) {
		for(var i=0;i<json.priceList.length;i++){
			if(json.priceList[i].PRICE_ID == "${headPO.priceId}"){
				if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
					obj.options[0] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
				} else {
					obj.options[0] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
				}
				obj.options[0].selected = "selected";
			} else{
				for(var i=0;i<json.priceList.length;i++){
					if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
						obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
						obj.options[i].selected = "selected";
					}
					else{
						obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
					}
				}
			}
		}
	} else {
		for(var i=0;i<json.priceList.length;i++){
			if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
				obj.options[i].selected = "selected";
			}
			else{
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
			}
		}
	}
	
	if(${orderType} == <%=Constant.ORDER_TYPE_03 %>) {
		document.getElementById("otherTr").style.display = "none" ;
		// priceTypeChange();
		changeCount_Price();
	} else {
		priceTypeChange();
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
function changeCount_Price(){
	var ids = document.getElementsByName("materialId");
	
	var buyNos = document.getElementsByName("buyNo");//采购数量
	var discount_rates = document.getElementsByName("discount_rate");//折扣率
	var discount_s_prices = document.getElementsByName("discount_s_price");//折扣后单价
	var singlePrices = document.getElementsByName("singlePrice");//单价
	var ratePara = parseInt(document.getElementById("ratePara").value, 10);
	var totalAmount = 0;
	var caigouMoneyAll = 0;
	var discountAllMoney = 0;
	for(var i =0;i<buyNos.length;i++){
		var discount_rate = discount_rates[i].value; //折扣率
		if(discount_rate -ratePara>0){
			MyAlert("折扣率不能大于"+ratePara+"%!");
			document.getElementById("discount_rate"+ids[i].value).value = 0;		//折扣率修改为0
			document.getElementById("discount_s_price_"+ids[i].value).innerHTML = amountFormat(singlePrices[i].value);		    //折扣后单价
			document.getElementById("discount_s_price"+ids[i].value).value = round(singlePrices[i].value,2);
			document.getElementById("discount_price"+ids[i].value).value = 0;					    							//折扣额
			document.getElementById("discount_price_"+ids[i].value).innerHTML = 0;
			document.getElementById("acountPrices"+ids[i].value).innerHTML = amountFormat(buyNos[i].value*singlePrices[i].value);//合计
			document.getElementById("acountPrices_"+ids[i].value).value = round(buyNos[i].value*singlePrices[i].value,2);
			var discountAllMoney = 0;
			var caigouMoneyAll = 0;
			var discount_price_c = document.getElementsByName("discount_price");
			var acountPrices_c = document.getElementsByName("acountPrices");
			for(var j=0;j<buyNos.length;j++){
				discountAllMoney += parseFloat(discount_price_c[i].value);
				caigouMoneyAll += parseFloat(acountPrices_c[i].value);
			}
			document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(caigouMoneyAll);
			document.getElementById("span6").innerHTML = amountFormat(caigouMoneyAll);
			document.getElementById("caigouMoneyAll").value = round(caigouMoneyAll,2);

			document.getElementById("discountAllMoney_").innerHTML = amountFormat(discountAllMoney);
			document.getElementById("discountAllMoney").value = round(discountAllMoney,2);
			return;
		}
		var discount_s_price = parseFloat(singlePrices[i].value)-parseFloat(singlePrices[i].value)*discount_rates[i].value/100;//折扣后单价
		document.getElementById("discount_s_price_"+ids[i].value).innerHTML = amountFormat(discount_s_price);
		document.getElementById("discount_s_price"+ids[i].value).value = round(discount_s_price,2);
		var discount_price = buyNos[i].value*discount_rate*parseFloat(singlePrices[i].value)/100;//折扣额
		document.getElementById("discount_price_"+ids[i].value).innerHTML = amountFormat(discount_price);
		document.getElementById("discount_price"+ids[i].value).value = round(discount_price,2);
		var acountPrices = buyNos[i].value*parseFloat(discount_s_price);				//合计
		document.getElementById("acountPrices"+ids[i].value).innerHTML = amountFormat(acountPrices);
		document.getElementById("acountPrices_"+ids[i].value).value = round(acountPrices,2);
		totalAmount += Number(buyNos[i].value);   //数量合计
		caigouMoneyAll += parseFloat(acountPrices);
		discountAllMoney += parseFloat(discount_price);
	}
	document.getElementById("totalAmount_").innerHTML = totalAmount;
	document.getElementById("totalAmount").value = totalAmount;

	document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(caigouMoneyAll);
	document.getElementById("span6").innerHTML = amountFormat(caigouMoneyAll);
	document.getElementById("caigouMoneyAll").value = round(caigouMoneyAll,2);

	document.getElementById("discountAllMoney_").innerHTML = amountFormat(discountAllMoney);
	document.getElementById("discountAllMoney").value = round(discountAllMoney,2);
}

//价格类型改变处理
function priceChange(json){
	var discountAllMoney = 0;
	var MATERIAL_PRICE_MAX = '<%=Constant.MATERIAL_PRICE_MAX%>';
	for(var i=0;i<json.priceList.length;i++){
		var id = json.priceList[i].MATERIAL_ID;
		var price = json.priceList[i].PRICE;
		
		var buyNO = document.getElementById("buyNO"+id).value;  								//采购数量
		var discountRate = document.getElementById("discount_rate"+id).value;					//折扣率
		var obj1 = document.getElementById("price" + id);										//单价
		if(price-MATERIAL_PRICE_MAX>0){
			obj1.innerHTML ="价格未维护";
			price=0;
		}else{
			obj1.innerHTML = amountFormat(price);
		}
		var singlePrice = document.getElementById("singlePrice"+ id).value = price;
		var discount_s_price = parseFloat(singlePrice)-parseFloat(singlePrice)*parseFloat(discountRate)/100;  //折扣后单价

		var discount_price = buyNO*parseFloat(singlePrice)*parseFloat(discountRate)/100;		//折扣额
		
		document.getElementById("discount_s_price"+id).value = discount_s_price;
		document.getElementById("discount_s_price_"+id).innerHTML = amountFormat(discount_s_price);
		document.getElementById("discount_price"+id).value = round(discount_price,2);
		document.getElementById("discount_price_"+id).innerHTML = amountFormat(discount_price);
		
		//采购金额
		
		document.getElementById("acountPrices"+id).innerHTML = amountFormat(buyNO*discount_s_price);
		document.getElementById("acountPrices_"+id).value = round(buyNO*discount_s_price,2);

		discountAllMoney += parseFloat(discount_price);
		//changeRate(id)	
	}
	var caigouMoneyAlls = document.getElementsByName("acountPrices_");
	var buyMoney = 0;
	for(var j=0;j<caigouMoneyAlls.length;j++){
		buyMoney = Number(buyMoney) + Number(caigouMoneyAlls[j].value);
	}
	document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(buyMoney);		
	document.getElementById("span6").innerHTML = amountFormat(buyMoney);		
	document.getElementById("caigouMoneyAll").value = round(buyMoney,2);	
	document.getElementById("discountAllMoney_").innerHTML = amountFormat(discountAllMoney);		
	document.getElementById("discountAllMoney").value = round(discountAllMoney,2);		
	isShowOtherPriceReason();
	//totalPrice();
}

// 判断是否显示使用其他价格原因
function isShowOtherPriceReason(){	
	var areaObj = document.getElementById("area");
	//var dealerId = areaObj.value.split("|")[1];
	var dealerId = document.getElementById("area").value.split("|")[1];;
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
function confirmAdd(arg){
	if(submitForm('fm')){
		var discountAllMoney = document.getElementById("discountAllMoney").value;
		var discount = document.getElementById("discount").value;

		var buyNo = document.getElementsByName("buyNo");//审核数量
		var originalBuyNo = document.getElementsByName("originalBuyNo");//原始数量
		var price = document.getElementsByName("singlePrice");//单价
		var level = document.getElementById("level").value;
		for(var i=0;i<buyNo.length && arg == 1;i++){
			if(buyNo[i].value -originalBuyNo[i].value>0){
				MyAlert("审核数量不能大于原始提报数量!");
				return;
			}
			if(level == '1'){
				if((price[i].value -0) == 0 && buyNo[i].value > 0){
					MyAlert("价格未维护的物料审核数量不能大于0!");
					return;
				}
			}
		}
		if(discountAllMoney-discount>0 && arg == 1){
			MyAlert("折扣总额不能大于可用折让!");
			return;
		}
		MyConfirm("是否确认提交?",orderAdd,[arg]);
	}
}

function orderAdd(arg){
	disableBtn($("add2"));
	disableBtn($("add22"));
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderPrepCheck/urgentOrderReoprtCheck.json?isPass='+arg,showResult,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderPrepCheck/urgentOrderPrepCheckQuery.do';
	}else{
		MyAlert("提交失败！账户余额不足！");
		useableBtn($("add2"));
		useableBtn($("add22"));
	}
}
function priceOnBlue(thisRate){
	var discountRate = thisRate.value;
	if(${returnValue==1}){
		if(discountRate!=0 && discountRate!=15){
			thisRate.value=0;
			MyAlert("微车的折扣率只能是0和15");
		}
	}
	if(${returnValue==2}){
	if(discountRate!=0 && discountRate!=10){
		thisRate.value=0;
		MyAlert("轿车的折扣率只能是0和10");
	    }
    }
}




</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 补充订单预审</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align="center">
    <tr class= "tabletitle">
      <td align = "right" >业务范围：</td>
      <td align = "left" >
      	<select name="areaId" onchange="getDealerLevel(this.options[this.options.selectedIndex].value);" disabled="disabled">
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
        <input type="hidden" name="area" value="">
      </td>
      <td align = "right"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      		<c:forEach items="${fundTypeList}" var="po">
				</c:forEach>
	      	</select>
      	</span>
      </td>
      <td align = "right"><span id="span3">可用余额：</span></td>
      <td align = "left"><span id="span4" class="STYLE2"></span></td>
      <td align="right"><span id="span5">订单总价：</span></td>
      <td align = "left"><span id="span6" class="STYLE2">
      	<!--<script type="text/javascript">document.write(amountFormat(${order.orderPrice}))</script>-->
      	
      	</span>
      </td>
    </tr>
    <tbody id="priceTr">
	    <tr class = "tabletitle" id="span18">
	      <td align = "right" >价格类型：</td>
	      <td align = "left" colspan="7">
	      <c:if test="${orderType != 10201003}"><select name="priceId" onchange="priceTypeChange();"></select></c:if>
	      <c:if test="${orderType == 10201003}"><select name="priceId"></select></c:if>
	      </td>
	    </tr>
	    <tr id="otherTr" class = "tabletitle">
	      <td align = "right"><span id="span7">使用其他价格原因：</span></td>
	      <td align = "left" colspan="7">
	      	<span id="span8">
	      		<input name="otherPriceReason" type="text" class="long_txt" value="${order.otherPriceReason}"/>
	      	</span>
	      </td>
	    </tr>
    </tbody>
  </table>
  <TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable >
      <th nowrap="nowrap">车系</th>
      <th nowrap="nowrap">物料编号</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">原始提报数量</th>
      <th nowrap="nowrap">审核数量</th>
      <th id="singleTh">单价</th>
      <th id="span9">折扣率%</th>
      <th id="span10">折扣后单价</th>
      <th id="span11">折扣额</th> 
      <th id="hejiTh">合计</th>
      <th nowrap="nowrap">库存数量</th>
    </tr>
    <tbody id="tbody1">
     		<%
		       int i =0;
		    %>
    	<c:forEach items="${detailList}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">
		      <input type='hidden' id='materialId${po.MATERIAL_ID}' name='materialId' value='${po.MATERIAL_ID}'>
		      <input type="hidden" name="detail_id" value="${po.DETAIL_ID}" />
		      	${po.SERIES_NAME}
		      </td>
		      <td align="center">${po.MATERIAL_CODE}</td>
		      <td align="center">${po.MATERIAL_NAME}</td>
		       <td align="center">${po.ORDER_AMOUNT}</td>
		      <td align="center">
		      	<input type="text" id="buyNo${po.MATERIAL_ID}" name="buyNo" value="${po.ORDER_AMOUNT}" datatype='1,is_digit,4' size="4" maxlength="4" onchange="changeCount_Price();" >
		      	<input type="hidden" id="originalBuyNo${po.MATERIAL_ID}" name="originalBuyNo" value="${po.ORDER_AMOUNT}" />	
		      </td>
		      <td align="center" id="singleTd<%=i %>">
		      	<span id="price${po.MATERIAL_ID}">${po.SINGLE_PRICE }</span>
		      	<input type="hidden" id="singlePrice${po.MATERIAL_ID}" name="singlePrice" value="${po.SINGLE_PRICE }" />
		      </td>
		     
		     
		      <!-- 折扣率 -->
		      <td align="center" id="span12<%=i %>" >
		      	<input type="text" name="discount_rate" id="discount_rate${po.MATERIAL_ID }" value="0" datatype='1,is_digit,6' size='2' maxlength='2' onchange="priceOnBlue(this);changeCount_Price();" />%
		      </td>
			  <!-- 折扣后单价 -->
		      <td align="center" id="span13<%=i %>" >
		    	<span id="discount_s_price_${po.MATERIAL_ID}"></span>
		    	<input type="hidden" id="discount_s_price${po.MATERIAL_ID}" name="discount_s_price" value="" />
		      </td>
		      <!-- 折扣额 -->
		      <td align="center" id="span14<%=i %>" >
		      	<span id="discount_price_${po.MATERIAL_ID}"></span>
		      	<input type="hidden" id="discount_price${po.MATERIAL_ID}" name="discount_price" value="0" />
		      </td>
		     
			  <td  id="hejiTd<%=i %>">
				<span id="acountPrices${po.MATERIAL_ID}"></span>
				<input type="hidden" id="acountPrices_${po.MATERIAL_ID}" name="acountPrices_" value="" />
			 </td>
		      <td align="center">${po.RESOURCE_AMOUNT}</td>
		    </tr>
		    <%
		      	i=i+1;
		    %>
    	</c:forEach>
    </tbody>
     <tr class="table_list_row1">
      <td nowrap="nowrap"  >&nbsp;</td>
      <td nowrap="nowrap" >&nbsp;</td>
      <td align="right" nowrap="nowrap"  ><strong>总计： </strong></td>
      <td></td>
      <td align="center" nowrap="nowrap">
      	<span  id="totalAmount_"> </span>
      	<input type="hidden" id="totalAmount" name="totalAmount" value="" />
      </td>
      <td align="center" nowrap="nowrap" id="singleTotal">&nbsp;</td>
      <td id="span15"></td>
      <td id="span16"></td>
      <td align="center" nowrap="nowrap" id="span17">
      	<span id="discountAllMoney_"></span>
      	<input type="hidden" id="discountAllMoney" name="discountAllMoney" value="0" />
      </td>
      <td align="center" nowrap="nowrap" id="totalPrice">
      	<span id="caigouMoneyAll__"></span>
      	<input type="hidden" id="caigouMoneyAll" name="caigouMoneyAll" value="" />
      </td>
      <td nowrap="nowrap" >&nbsp;</td>
    </tr>
  </table>	
  <TABLE class=table_query>
   <tr>
      <th colspan="4" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明</th>
    </tr>
    <tr class="cssTable" id="discountTr">
      <td width="10%" align="right" valign="top" nowrap="nowrap">可用折让：</td>
      <td width="90%" colspan="3" align="left" valign="top" nowrap="nowrap">
       <input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" readonly="readonly"/>
      </td>
    </tr>
    <tr class="cssTable">
	      <td align="right" nowrap="nowrap">收货方：</td>
	      <td align="left" nowrap="nowrap" colspan="3" >
		     ${r_dealerName }<input type="hidden" id="receiver" name="receiver" value="${receiver }" />
		  </td>
   </tr>
   <tr class="cssTable" id="cusTr" style="display:none">
      <td align="right" nowrap="nowrap">集团客户名称：</td>
      <td align="left" nowrap="nowrap">
	       ${fleetName }
		  <input id="fleetId" name="fleetId"  value="${fleet_id }" type="hidden"/>
	  </td>
      <td align="right" nowrap="nowrap"  width="10%">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap"  width="50%">
	     ${fleetAddress }
	  </td>
    </tr>
    <tbody id="tranId">
    <tr class="cssTable" id="tran_id">
      <td align="right"  nowrap="nowrap">运输方式： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      <script type="text/javascript">writeItemValue(${order.deliveryType})</script>
      </td>
    </tr>
	<tr class="cssTable" id="trId_1">
      <td align="right" nowrap="nowrap" id="addTd">运送地址：</td>
      <td align="left" nowrap="nowrap" >
	      <span id="addTd1"></span>
      </td>
      <td align="right" nowrap="nowrap" width="10%">收车单位：</td>
	  <td align="left"  nowrap="nowrap" id="receiveOrg" width="50%"><span  id="addTd2"></span></td>
    </tr>
     <tr class="cssTable" id="trId_2">
	      <td align="right" nowrap="nowrap">联系人：</td>
	      <td align="left" nowrap="nowrap" ><span id="addTd3"></span></td>
	      <td align="right" nowrap="nowrap">联系电话：</td>
	      <td align="left" nowrap="nowrap" ><span id="addTd4"></span></td>
	    </tr>
    </tbody>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">备注说明：</td>
      <td align="left" nowrap="nowrap" >${order.orderRemark}</td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">付款信息备注：</td>
      <td align="left" nowrap="nowrap" >${order.payRemark}</td>
    </tr>
  </table>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
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
  <p><BR>
  </p>
  <table class="table_query">
    <tr class="cssTable" >
      <th colspan="2" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审核信息</th>
    </tr>
    <tr class="cssTable" >
      <td width="9%" align="right">审核描述：</td>
      <td width="91%" align="left"><label>
        <textarea name="checkDesc" cols="50" rows="3"></textarea>
      </label></td>
    </tr>
    <tr class="cssTable" >
      <td align="left">&nbsp;</td>
      <td align="left">
      <input type="hidden" name="orderId" value="${order.orderId}" />
      <input type="hidden" name="accountId" value="" />
      <input type="hidden" name="discountAccountId" value="" />
      <input type="hidden" name="orderNO" value="${orderNO }" />
	  <input type="hidden" name="ratePara" value="${ratePara}" />
	  <input type="hidden" name="level" value="" />
	  <input type="hidden" name="orderType" id="orderType" value="${orderType }">
      <input class="cssbutton"  name="add2" type="button" onclick="confirmAdd('1');" value ='通过' />
      <input class="cssbutton"  name="add22" type="button" onclick="confirmAdd('0');" value ='驳回' />
      <input class="cssbutton"  name="add232" type="button" onclick="history.back();" value ='返回' />
      <!-- 资金类型是否是“兵财” -->
	  <input type="hidden" id="isBingcai" name="isBingcai" value="" />	
	  <!-- 原资金类型id -->
	  <input type="hidden" id="old_fundTypeId" name="old_fundTypeId" value="" />
	  </td>
    </tr>
  </table>
</form>
</body>
</html>
