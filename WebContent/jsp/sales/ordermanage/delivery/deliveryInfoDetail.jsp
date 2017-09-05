<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单发运申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function testLen(value) {
	var reg = /^.{0,50}$/ ;
	var flag = reg.exec(value) ;
	return flag ;
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

function getAvailableAmount() {
	
	var dealerId = ${dealerId } ;
	var fundTypeId = document.getElementById("typeId").value ;
	if(fundTypeId==null||"null"==fundTypeId||""==fundTypeId){
		MyAlert("ERP和DMS账户类型不匹配!!!");
		document.getElementById("id1save").disabled=true;
	}else{
		document.getElementById("id1save").disabled=false;
	}
	var url = "<%= contextPath%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json" ;
	
	makeCall(url, showAvailableAmount, {dealerId:dealerId, fundTypeId:fundTypeId}) ;
}

function showAvailableAmount(json) {
	document.getElementById("accountId").value = json.accountId ;
	document.getElementById("accountAmount").innerText= amountFormat(json.returnValue);
	document.getElementById("accountCold").innerText= amountFormat(json.returnValue2);
	document.getElementById("accountTotal").innerText= amountFormat(json.returnValue1);
	document.getElementById("availableAmount").value = json.returnValue ;
	var obj3 = document.getElementById("credit_amount");//信用额度
	obj3.innerHTML = amountFormat(json.returnValue3);	
	var obj4 = document.getElementById("fine_amount");//账户余额-信用额度
	obj4.innerHTML = amountFormat(json.returnValue4);
}
//-->
</script>
</head>
<body>

<form name="fm" id="fm">
	<input type="hidden" name="areaId" id="areaId" value="${areaList[0].AREA_ID}"/>
	<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
	<table class="table_query" id="table1">
		<tr class= "tabletitle">
			<td align="right" id="a1" class="table_query_2Col_label_6Letter" nowrap="nowrap">资金类型：</td>
			<td align="left" id="a2">
				<select name="typeId" id="typeId" class="short_sel" onchange="getAvailableAmount();">
				</select>
			</td>
			<td align="right" id="a3" nowrap="nowrap" class="table_query_2Col_label_6Letter">可用余额：</td>
			<td align="left" id="accountAmount"></td>
			<td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter" style="display:none;"> 订单总价：</td>
			<td align="left" ><span id="allAccount"></span></td>
			<td align="left">
				<input type="hidden" name="accountId" id="accountId" value=""/>
				<input type="hidden" name="availableAmount" id="availableAmount" value=""/>
				<!-- 订单总价 -->
				<input type="hidden" name="totailAccount" id="totailAccount" value=""/>
				<input type="hidden" name="freezeAmount" id="freezeAmount" value=""/>
			</td>
		</tr>
		<tr class="cssTable">
			<td align="right" id="a1">冻结资金：</td>
			<td align="left" id="accountCold">
			</td>
			<td align="right" id="a3" nowrap="nowrap">账户余额：</td>
			<td align="left" id="accountTotal"></td>
			<td align="right" nowrap><span>信用额度：</span></td>
	   	 	<td align="left" nowrap > <span id="credit_amount"></span></td>
	   	 	<td align="left" >&nbsp;</td>
		</tr>
		<tr class="cssTable" id="tmp_license_amountTr">
			<td align="right"  width="12%">账户余额-信用额度：</td>
	    	<td  align="left"><span id="fine_amount"></span></td>
	      <td  align="right"  nowrap="nowrap">临牌数量：</td>
	      <td  align="left"  nowrap="nowrap">
	      	<input id="tmp_license_amount" name="tmp_license_amount" type="text" class="middle_txt" size="10" maxlength="30" value="0" datatype="1,is_double,20" decimal="2"/>
	      </td>
	      <td  colspan="2"></td>
	    </tr>
		<tr class= "tabletitle" style="display:none;">
			<td align ="right" >可用折让：</td>
			<td align ="left" colspan="6">
				<input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" value="0" datatype="1,is_double,20" decimal="2" readonly="readonly"/>
			</td>
		</tr>
		<tr class= "tabletitle">
			<td align ="right" >价格类型：</td>
			<td align ="left" colspan="1">
			<label>${priceId}</label>
			<input type="hidden" name="priceId" value="${priceId}"/>
			<!--价格类型-->
			</td>
			<td align="right" class="cssTable" id="reason1" style="display:none;">使用其他价格原因：</td>
			<td align="left" class="cssTable" colspan="4" id="reason2" style="display:none;">
				<input name="otherPriceReason" type="text" class="long_txt"/>
			</td>
		</tr>
		<tr class="tabletitle" id="discountTr" style="display:none;">
      		<td align="right"  nowrap="nowrap">是否代交车：</td>
      		<td colspan="6" align="left">
      		<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      		</td>
    	</tr>
    	<tr  class="cssTable" id="cusTr" style="display:none">
			<td align="right" class="cssTable" id="ssss1">选择集团客户：</td>
			<td align="left" class="cssTable" id="ssss2">
				<input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30"/>
				<input id="fleetId" name="fleetId" type="hidden"/>				
				<input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
				<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td align="right" class="cssTable" id="address03">运送地址：</td>
			<td align="left" class="cssTable" id="address04">
				<input id="address" name="address" size="50" type="text" datatype="0,is_noquotation,50"/>
			</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class= "tabletitle" id="tran_id">
			<td align ="right">运输方式：</td>
			<td align ="left" id="trantype" colspan="6">
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'');
					</script>
				</label>
			</td>
		</tr>
		<tbody id="addTr">
			<tr class= "tabletitle">
				<td align ="right" >收货方：</td>
				<td align ="left" colspan="6">
					<select name="receiver" onchange="getAddressList();">
			      	</select>
				</td>
			</tr>
			<tr class= "tabletitle">
				<td align ="right" >运送地址：</td>
				<td align ="left">
					<select name="addressId" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
			      	</select>
				</td>
				<td align="right" class="cssTable">收车单位：</td>
				<td align="left" class="cssTable" id="receiveOrg" colspan="4">
				</td>
			</tr>
			<tr class= "tabletitle">
				<td align ="right" >联系人：</td>
				<td align ="left">
					<input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
				<td align="right" class="cssTable">联系电话：</td>
				<td align="left" class="cssTable" colspan="4">
					<input name="tel" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
			</tr>
		</tbody>
		<tr class="cssTable">
				<td align="right">备注说明：</td>
				<td align="left" colspan="6"><textarea name="orderRemark" id="orderRemark" cols="60" rows="3" onkeyup="value = value.replace(/#/g, '') ;" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onchange="limitChars('orderRemark', 50)"></textarea><font color="red">建议录入不超过50个汉字!</font></td>
			</tr>
		<tr class="cssTable">
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td colspan="3"></td>
			<td align = "left" >
				<input class="normal_btn" name="appl" type="button" id="id1saves"  value="关闭" onclick="closeWindow();">
				<input class="normal_btn" name="appl" type="button" id="id1save"  value="确认" onclick="applySubmit();">
				<input class="normal_btn" name="appl" type="button" id="back"  value="返回" onclick="history.back();">
			</td>
			<td align = "left" ><label></label></td>
		</tr>
	</table>
</form>
<script type="text/javascript" ><!--
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
			addressHide(document.getElementById("transportType").value);
			document.getElementById("isCover").value = 0;
		}
	}
	
	function getFund(){
		var dealerId = ${dealerId};
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:dealerId});
	}
	
	function showFund(json){
		showFundTypeList(json);
		showDiscountInfo(json);
	}
	
	//资金类型列表显示
	function showFundTypeList(json){
		var obj = document.getElementById("typeId");
		obj.options.length = 0;
		for(var i=0;i<json.fundTypeList.length;i++){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
<!--			if(json.fundTypeList[i].TYPE_ID==null){-->
<!--				obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, "null"+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);-->
<!--			}else{-->
<!--				obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);-->
<!--			}-->
		}
		
		getAvailableAmount();
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
		var dealerId = ${dealerId};
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
	
	//价格类型改变
	function priceTypeChange(){	
		var btn = document.getElementById("appl") ;
		
		btn.disabled = true ;
		
		var priceId = document.getElementById("priceId").value;
		var ids = "";//已选中的物料id
		var myForm = document.getElementById("form1");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=8 && obj.id.substring(0,8)=="detailId"){
				ids += obj.value + ",";
			}   
		} 	
		ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getDetailSinglePriceList.json";
		makeCall(url,priceChange,{priceId:priceId, ids:ids});
	}	
	
	//价格类型改变处理
	function priceChange(json){
		//var discount_rate = document.getElementsByName("discount_rate");
		var applyAmount = document.getElementsByName("applyAmount");
		for(var i=0;i<json.priceList.length;i++){
			var id = json.priceList[i].DETAIL_ID;
			var price = json.priceList[i].PRICE;
			
			var obj1 = document.getElementById("priceTd" + id);
			var obj2 = document.getElementById("tempPrice" + id);
			var obj3 = document.getElementById("applyAmount" + id);
			var desc = price;
			if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
				desc = "价格未维护";
				price = 0;
			}
			
			obj1.innerHTML = desc + "<input type='hidden' name='singlePrice' value='"+price+"'/>";
			obj2.value = price;
			document.getElementById(id).innerText = amountFormat(obj3.value * price);
			

			discountRateChange(id,discount_rate[i].value,price,applyAmount[i].value);
		}
		isShowOtherPriceReason();
		changeAllAccount();
		
		var btn = document.getElementById("appl") ;
		
		btn.disabled = false ;
	}
	
	// 判断是否显示使用其他价格原因
	function isShowOtherPriceReason(){	
		var dealerId = ${dealerId};
		var priceId = document.getElementById("priceId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/isShowOtherPriceReason.json";
		makeCall(url,showOtherPriceReason,{dealerId:dealerId,priceId:priceId});
	}	
	
	//使用其他价格原因
	function showOtherPriceReason(json){
		if(json.returnValue == "1"){
			document.getElementById("reason1").style.display = "inline";
			document.getElementById("reason2").style.display = "inline";
		}
		else{
			document.getElementById("reason1").style.display = "none";
			document.getElementById("reason2").style.display = "none";
		}
	}
	
	//获得收货方列表
	function getReceiverList(){	
		var dealerId = ${dealerId};
		
		if(document.getElementsByName("areaId").length > 0) {
			var areaId = document.getElementsByName("areaId")[0].value ;
		
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
			makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
		} 
	}	
	
	//显示收货方列表
	function showReceiverList(json){
		var obj = document.getElementById("receiver");
		obj.options.length = 0;
		for(var i=0;i<json.receiverList.length;i++){
			var dealerId=document.getElementById("dealerIds").value;
			if(dealerId==json.receiverList[i].DEALER_ID){
				obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
				obj.options[i].selected=true;
			}else{
				obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
			}
		}
		getAddressList();//获得发运地址列表
	}
	
	//获得地址列表
	function getAddressList(){	
		var dealerId = document.getElementById("receiver").value;
		var areaId = document.getElementsByName("areaId")[0].value ;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
		makeCall(url,showAddressList,{dealerId:dealerId,areaId:areaId});
	}	
	
	function showAddressList(json){
		var obj = document.getElementById("addressId");
		obj.options.length = 0;
		for(var i=0;i<json.addressList.length;i++){
			obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
		}
		
		getAddressInfo(document.getElementById("addressId").value);
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
		//var obj2 = document.getElementById("cusTr");
		if(arg == '<%=Constant.TRANSPORT_TYPE_01%>') {
			obj1.style.display = "none";
			//obj2.style.display = "none";
		}
		
		else{
			obj1.style.display = "inline";
			//obj2.style.display = "none";
		}
	}
	
	//设置订单总价与本次申请数量合计
	function changeAllAccount(){
		
		var allAccount = 0;
		var allAmount = 0;
		var applyAmounts = document.getElementsByName("applyAmount");
		//var singlePrices = document.getElementsByName("dis_s_price");
		var singlePrices = document.getElementsByName("singlePrice");
		
		for(var i=0; i<applyAmounts.length; i++){
			allAccount = Number(allAccount) + (Number(singlePrices[i].value) * Number(applyAmounts[i].value));
			allAmount = allAmount + Number(applyAmounts[i].value);
		}
		if(allAccount==0){
			document.getElementById("allAccount").innerText = "0.00";
			document.getElementById("allAccounts").innerText = "0.00";
		}else{
			document.getElementById("allAccount").innerText=amountFormat(allAccount);
			document.getElementById("allAccounts").innerText=amountFormat(allAccount);
		}
		document.getElementById("totailAccount").value = allAccount;
		document.getElementById("applyAmounts").innerText = allAmount;
	}


	function round(number,fractionDigits){   
		   with(Math){   
		       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
		    }   
	}

	//计算折扣总额
	function disAllPrice(){
		var discount_priceAll = document.getElementsByName("dis_price");
		var discount_priceAlls = 0;
		for(var i=0;i<discount_priceAll.length;i++){
			discount_priceAlls = parseFloat(discount_priceAlls)+ parseFloat(discount_priceAll[i].value);
		}
		document.getElementById("discountAllPrice").value = round(discount_priceAlls,2);
		document.getElementById("discountAllPrice_").innerHTML = amountFormat(round(discount_priceAlls,2));
	}
	
	
	
	//折扣率联动
	//detail_id:id
	//discount_rate:折扣率
	//single_Price：单价
	//applyAmount：申请数量
	function discountRateChange(detail_id,discount_rate,single_Price,applyAmount){
		//var discount_s_price = 0;//折扣后单价
		//var discount_price = 0;  //折扣额
		var price_sum = 0;		 //价格合计

//		var ratePara = parseInt(document.getElementById("ratePara").value, 10);
//		if(discount_rate - ratePara >0){
//			MyAlert("折扣率不能大于"+ratePara+"%!");
//			discount_rate = 0;
//			document.getElementById("discount_rate"+detail_id).value = 0;
//		}
		
//		discount_s_price = parseFloat(single_Price) - parseFloat(single_Price*discount_rate/100);
//		discount_price = parseFloat(single_Price)*parseFloat(discount_rate/100) * parseFloat(applyAmount)
		
		/*document.getElementById("discount_s_price"+detail_id).value = round(discount_s_price,2);
		document.getElementById("dis_s_price"+detail_id).value = round(discount_s_price,2);
		document.getElementById("discount_s_price_"+detail_id).innerHTML = amountFormat(round(discount_s_price,2));

		document.getElementById("discount_price"+detail_id).value = round(discount_price,2);
		document.getElementById("dis_price"+detail_id).value = round(discount_price,2);
		document.getElementById("discount_price_"+detail_id).innerHTML = amountFormat(round(discount_price,2));*/

		price_sum = parseFloat(single_Price)*parseFloat(applyAmount);
		document.getElementById("totailAccount").value = round(price_sum,2);
		document.getElementById(detail_id).innerHTML = amountFormat(round(price_sum,2));
		document.getElementById("orderPriceSum"+detail_id).value=round(price_sum,2);
		//disAllPrice();
		//changeAllAccount();
	}
	//初始化
	function doInit(){
		getFund();
		getPriceList();
		
		getReceiverList();
		var list = "<%=request.getAttribute("list")%>";
		if(list.substring(1,list.length-1)){
			document.getElementById("table1").style.display = "";
		}else{
			document.getElementById("table1").style.display = "none";
		}
		//sumAll();
		//changeAllAccount();
		//selectAction();
	}
	//资金类型与余额联动
	/*function showAccountAmount(){
		var typeId = document.getElementById("typeId").value;
		<c:forEach items="${accountlist}" var="list">
         var id=<c:out value="${list.TYPE_ID}"/>
         var accountId=<c:out value="${list.ACCOUNT_ID}"/>
         if(typeId==id){
        	 var availableAmount=<c:out value="${list.AVAILABLE_AMOUNT}"/>
        	 var freezeAmount=<c:out value="${list.FREEZE_AMOUNT}"/>
        	 document.getElementById("accountAmount").innerText= amountFormat(availableAmount);
        	 document.getElementById("accountId").value = accountId;
        	 document.getElementById("availableAmount").value = availableAmount;
        	 document.getElementById("freezeAmount").value = freezeAmount;
         }
		</c:forEach>
	}*/
	//运送方式与地址隐藏联动
	function selectAction(){
		var transportType = document.getElementById("transportType").value;
		if(transportType==<%=Constant.TRANSPORT_TYPE_01%>){
			document.getElementById('address01').style.display="none";
			document.getElementById('address02').style.display="none";
			document.getElementById('address03').style.display="none";
			document.getElementById('address04').style.display="none";
			document.getElementById('ssss1').style.display="none";
			document.getElementById('ssss2').style.display="none";
		}if(transportType==<%=Constant.TRANSPORT_TYPE_02%>){
			document.getElementById('address01').style.display="inline";
			document.getElementById('address02').style.display="inline";
			document.getElementById('address03').style.display="none";
			document.getElementById('address04').style.display="none";
			document.getElementById('ssss1').style.display="none";
			document.getElementById('ssss2').style.display="none";
		}
		if(transportType==<%=Constant.TRANSPORT_TYPE_03%>){
			document.getElementById('address01').style.display="none";
			document.getElementById('address02').style.display="none";
			document.getElementById('address03').style.display="inline";
			document.getElementById('address04').style.display="inline";
			document.getElementById('ssss1').style.display="inline";
			document.getElementById('ssss2').style.display="inline";
		}
	}
	//合计订单数量，已审核数量，已申请数量
	function sumAll(){
		var orderAmount =0;
		var checkAmount =0;
		var callAmount =0;
		var orderAmounts = document.getElementsByName("orderAmount");
		var checkAmounts = document.getElementsByName("checkAmount");
		var callAmounts = document.getElementsByName("callAmount");
		for(var i=0; i<orderAmounts.length; i++){
			orderAmount = orderAmount + Number(orderAmounts[i].value);
			checkAmount = checkAmount + Number(checkAmounts[i].value);
			callAmount = callAmount + Number(callAmounts[i].value);
		}
		document.getElementById("orderAmounts").innerText = orderAmount;
		document.getElementById("checkAmounts").innerText = checkAmount;
		document.getElementById("callAmounts").innerText = callAmount;
	}
	//申请提醒
	function applySubmit(){
		if(submitForm('fm')){
			if(document.getElementById('tmp_license_amount').value.length==0) {
				MyAlert('临牌数量不能为空。') ; 
				return false ;
			}
			
			if(!testLen(document.getElementById('orderRemark').value)) {
				MyAlert('备注录入已超过50个汉字的最大限！') ; 
				return false ;
			}
			if(confirm("确认申请？")){
				putForword();
			};
		}
	}
	//申请发运
	function putForword(){
	
			var orderId=parent.$('inIframe').contentWindow.document.getElementById("orderId").value;
			var cAmounts= parent.$('inIframe').contentWindow.$("cAmounts").value;
			var Amounts= parent.$('inIframe').contentWindow.$("Amounts").value;
			var orderIds= parent.$('inIframe').contentWindow.$("orderIds").value;
			var detailIds= parent.$('inIframe').contentWindow.$("detailIds").value;
			var materialIds= parent.$('inIframe').contentWindow.$("materialIds").value;
			var singlePrices= parent.$('inIframe').contentWindow.$("singlePrices").value
			var areaIds= parent.$('inIframe').contentWindow.$("areaIds").value;
			var orderPriceSums= parent.$('inIframe').contentWindow.$("orderPriceSums").value;
			var priceListId= parent.$('inIframe').contentWindow.$("priceListIds").value;
			var url='<%=contextPath%>/sales/ordermanage/delivery/DeliveryApply/applyDelvSubmit.json';
			url+='?&Amounts='+Amounts+'&orderIds='+orderIds;
			url+='&detailIds='+detailIds+'&materialIds='+materialIds;
			url+='&singlePrices='+singlePrices+'&cAmounts='+cAmounts;
			url+='&areaIds='+areaIds+'&orderPriceSums='+orderPriceSums;
			url+='&priceListIds='+priceListId;
			makeFormCall(url,showForwordValue,'fm');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			MyAlert("申请成功！");
			parent._hide();
			parent.$('inIframe').contentWindow.executeQuery();
		}else if(json.returnValue == '3') {
			MyAlert("申请失败：物料" + json.metStr + "允许启票数量已超出！");
		} else if(json.returnValue == '4') {
			MyAlert("价格列表未加载成功，请等待价格列表加载完毕再提交！");
		}else if(json.returnValue=='a'){
			MyAlert("申请失败！资源不足！");
		}else if(json.returnValue =='p'){
			MyAlert("申请失败！存在价格没有维护！");
		}else{
			MyAlert("申请失败！请联系系统管理员！");
		}
	}
	//清除按钮
  	function toClear(){
		document.getElementById("fleetName").value="";
		document.getElementById("fleetId").value="";
  	}
  	//关闭当前窗口
  	function closeWindow(){
  		parent._hide();
  	}
  </script>
</body>
</html>