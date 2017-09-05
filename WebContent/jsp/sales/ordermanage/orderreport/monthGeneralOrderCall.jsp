<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度常规订单启票</title>
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
	var dealerId = document.getElementById("dealerId").value ;
	var fundTypeId = document.getElementById("typeId").value ;
		
	var url = "<%= contextPath%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json" ;
	
	makeCall(url, showAvailableAmount, {dealerId:dealerId, fundTypeId:fundTypeId}) ;
}

function showAvailableAmount(json) {
	document.getElementById("accountId").value = json.accountId ;
	document.getElementById("accountAmount").innerText= amountFormat(json.returnValue);
	document.getElementById("availableAmount").value = json.returnValue ;
}
//-->
</script>
</head>
<body>
<form method="post" name="fm" id="fm">
	<%	
		List list = (List)request.getAttribute("list");
		if(list.size()==0||list==null){
	%>
	<div class='pageTips'>没有满足条件的数据</div>
	<%}else{ %>
	<div id="detailDiv" style="overflow:scroll">
	<table class="table_list" width="100%">
		<tr class="cssTable">
			<th align="center" nowrap="nowrap">车系</th>
			<th align="center" nowrap="nowrap">物料编号</th>
			<th align="center" nowrap="nowrap">物料名称</th>
			<th align="center" nowrap="nowrap">资源情况</th>
			<th align="center" nowrap="nowrap">提报数量</th>
			<th align="center" nowrap="nowrap">已启票量</th>
			<th align="center" nowrap="nowrap">本次提交数量</th>
			<th align="center" nowrap="nowrap">单价</th>
			<th align="center" nowrap="nowrap">金额</th>
			<th align="center" nowrap="nowrap">折扣率%</th>
      		<th align="center" nowrap="nowrap">折扣后单价</th>
      		<th align="center" nowrap="nowrap">折扣额</th>
		</tr>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
			<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td>
					${list.SERIES_NAME}
					<input type="hidden" name="detailId" value="${list.DETAIL_ID}"/>
					<input type="hidden" name="orderId" value="${list.ORDER_ID}"/>
					<input type="hidden" name="areaId" value="${list.AREA_ID}"/>
					<input type="hidden" name="materialId" value="${list.MATERIAL_ID}"/>
					<input type="hidden" name="orderAmount" value="${list.ORDER_AMOUNT}"/>
					<input type="hidden" name="callAmount" value="${list.CALL_AMOUNT}"/>
					<input type="hidden" name="singlePrice" id="singlePrice${list.DETAIL_ID}"/>
					<input type="hidden" name="totalPrice"/>
					<input type="hidden" name="discountSPrice"/>
					<input type="hidden" name="discountPrice"/>
				</td>
				<td>${list.MATERIAL_CODE}</td>
				<td>${list.MATERIAL_NAME}</td>
				<td>${list.RAMOUNT}</td>
				<td>${list.ORDER_AMOUNT}</td>
				<td>${list.CALL_AMOUNT}</td>
				<td>
					<input type="text" name="applyAmount" id="applyAmount${list.DETAIL_ID}" datatype="0,is_digit,6"  size="3" value="0" onchange="priceTotal();"></input>
				</td>
				<td><span id="single_price_${list.DETAIL_ID}"></span></td><!-- 单价 -->
				<td><span id="total_price_${list.DETAIL_ID}"></span></td><!--金额 -->
				<td>
					<input type='text' name='discountRate' id="discountRate${list.DETAIL_ID}" class='SearchInput' datatype="0,is_double,6" decimal="2" size='2' maxlength='2' value='0' onchange="priceOnBlue(this);priceTotal();" />
				</td>
				<td><span id="discount_s_price_${list.DETAIL_ID}"></span></td><!-- 折扣后单价 -->
				<td><span id="discount_price_${list.DETAIL_ID}"></span></td><!-- 折扣额 -->
			</tr>
		</c:forEach>
		<tr class="table_list_row1">
			<td></td>
			<td></td>
			<td><strong>合计：</strong></td>
			<td></td>
			<td id="heji1"></td><!-- 提报数量合计 -->
			<td id="heji2"></td><!-- 已启票数量合计 -->
			<td id="heji3"></td><!-- 本次提交数量合计 -->
			<td></td>
			<td id="heji4"></td><!-- 金额合计 -->
			<td></td>
			<td></td>
			<td id="heji5"></td><!-- 折扣额 -->
		</tr>
	</table>
	</div>
	<%} %>
	<br>
	<table class="table_query" id="table1">
		<tr class= "tabletitle">
			<td align="right" id="a1">资金类型：</td>
			<td align="left" id="a2">
				<select name="typeId" id="typeId" class="short_sel" onchange="getAvailableAmount();">
				</select>
			</td>
			<td align="right" nowrap="nowrap">可用余额：</td>
			<td align="left" id="accountAmount"></td>
			<td align="right" nowrap="nowrap">订单总价：</td>
			<td align="left"><span id="req_total_price">0.00</span></td>
			<td align="left">
				<input type="hidden" name="accountId" id="accountId" value=""/>
      	  		<input type="hidden" name="discountAccountId" value="">
				<input type="hidden" name="availableAmount" id="availableAmount" value=""/>
				<input type="hidden" name="freezeAmount" id="freezeAmount" value=""/>
			</td>
		</tr>
		<tr class= "tabletitle">
			<td align ="right" >可用折让：</td>
			<td align ="left" colspan="6">
				<input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" value="0" datatype="1,is_double,20" decimal="2" readonly="readonly"/>
			</td>
		</tr>
		<tr class= "tabletitle">
			<td align ="right" >价格类型：</td>
			<td align ="left" colspan="1">
				<select name="priceId" onchange="priceTypeChange();">
        		</select>
			</td>
			<td align="right" class="cssTable" id="reason1">使用其他价格原因：</td>
			<td align="left" class="cssTable" colspan="4" id="reason2">
				<input name="otherPriceReason" type="text" class="long_txt"/>
			</td>
		</tr>
		<tr class="tabletitle">
      		<td align="right"  nowrap="nowrap">是否代交车：</td>
      		<td colspan="3" align="left">
      		<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      		</td>
    	</tr>
    	<tr id="cusTr" style="display:none">
			<td align="right" class="cssTable" id="ssss1">选择大客户：</td>
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
		<tr  class= "tabletitle">
			<td align="right">备注说明：</td>
			<td align="left" colspan="3"><textarea name="orderRemark" id="orderRemark" cols="60" rows="3" onkeyup="value = value.replace(/#/g, '') ;" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onchange="limitChars('orderRemark', 50)"><c:out value="${map.ORDER_REMARK}"/></textarea><font color="red">建议录入不超过50个汉字!</font></td>
		</tr>
		<tr>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
			<td align ="left">
				<input class="normal_btn" name="queryBtn" type="button" value="启票" onclick="applySubmit();">
				<input type="hidden" name="isCheck" value="${isCheck}"/>
				<input type="hidden" name="dealerId" value="${dealerId}"/>
				<input type="hidden" name="reqTotalPrice" value="0"/>
				<input type="hidden" name="discountTotalPrice" value="0"/>
				<input type="hidden" name="reqTotalAmount" value="0"/>
				<input type="hidden" name="ratePara" value="${ratePara}" />
			</td>
			<td align = "left" ><label></label></td>
		</tr>
	</table>
</form>
<script type="text/javascript" ><!--

function priceOnBlue(thisRate,timeValue){
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
	}
	
	// 合计价格
	function priceTotal(){
		var orderTotal = 0;
		var callTotal = 0;
		var applyTotal = 0;
		var priceTotal = 0;
		var discountTotal = 0;
		
		var detailId = document.getElementsByName("detailId");
		var orderAmount = document.getElementsByName("orderAmount");
		var callAmount = document.getElementsByName("callAmount");
		var applyAmount = document.getElementsByName("applyAmount");
		var singlePrice = document.getElementsByName("singlePrice");
		var totalPrice = document.getElementsByName("totalPrice");
		var discountRate = document.getElementsByName("discountRate");
		var discountSPrice = document.getElementsByName("discountSPrice");
		var discountPrice = document.getElementsByName("discountPrice");
		
		for(var i=0; i<detailId.length; i++){
			var subStr = detailId[i].value;
			var order = parseInt(orderAmount[i].value, 10);
			var call = parseInt(callAmount[i].value, 10);
			var apply = parseInt(applyAmount[i].value, 10);
			var singlePriceValue = parseFloat(singlePrice[i].value, 10);//单价
			var totalPriceValue = apply*singlePriceValue;//金额
			var discountRateValue = parseFloat(discountRate[i].value, 10);//折扣率
			var discountSPriceValue = singlePriceValue*(1-discountRateValue/100);//折扣后单价
			var discountPriceValue = totalPriceValue*discountRateValue/100;//折扣额
			
			totalPrice[i].value = totalPriceValue;//金额
			document.getElementById("total_price_"+subStr).innerHTML = totalPriceValue == 0 ? "0" : amountFormat(totalPriceValue);//金额显示
			discountSPrice[i].value = discountSPriceValue;//折扣后单价
			document.getElementById("discount_s_price_"+subStr).innerHTML = discountSPriceValue == 0 ? "0" : amountFormat(discountSPriceValue);//折扣后单价显示
			
			discountPrice[i].value = discountPriceValue;//折扣额
			document.getElementById("discount_price_"+subStr).innerHTML = discountPriceValue == 0 ? "0" : amountFormat(discountPriceValue);//折扣额显示
			
			orderTotal += order;
			callTotal += call;
			applyTotal += apply;
			priceTotal += totalPriceValue;
			discountTotal += discountPriceValue;
		}
		
		
		document.getElementById("heji1").innerHTML = orderTotal;//提报数量合计
		document.getElementById("heji2").innerHTML = callTotal;//已启票数量合计
		document.getElementById("heji3").innerHTML = applyTotal;//本次提交数量合计
		document.getElementById("heji4").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		document.getElementById("heji5").innerHTML = discountTotal == 0 ? "0" : amountFormat(discountTotal);
		document.getElementById("reqTotalPrice").value = parseFloat(priceTotal, 10) - parseFloat(discountTotal, 10);
		document.getElementById("discountTotalPrice").value = discountTotal;
		document.getElementById("reqTotalAmount").value = applyTotal;
		document.getElementById("req_total_price").innerHTML = amountFormat(document.getElementById("reqTotalPrice").value);
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
		var btn = document.getElementById("queryBtn") ;
		
		btn.disabled = true ;
		
		var priceId = document.getElementById("priceId").value;
		var ids = "";//已选中的物料id
		var detailId = document.getElementsByName("detailId");
		for(var i=0; i<detailId.length; i++){
			ids += detailId[i].value + ",";
		}	
		ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getDetailSinglePriceList.json";
		makeCall(url,priceChange,{priceId:priceId, ids:ids});
	}	
	
	//价格类型改变处理
	function priceChange(json){
		for(var i=0;i<json.priceList.length;i++){
			var id = json.priceList[i].DETAIL_ID;
			var price = json.priceList[i].PRICE;
			
			var obj1 = document.getElementById("singlePrice" + id);
			var obj2 = document.getElementById("single_price_" + id);
			var desc = amountFormat(price);
			if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
				desc = "价格未维护";
				price = 0;
			}
			
			obj1.value = price;
			obj2.innerHTML = desc;
		}
		isShowOtherPriceReason();
		priceTotal();
		
		var btn = document.getElementById("queryBtn") ;
		
		btn.disabled = false ;
	}
	
	// 判断是否显示使用其他价格原因
	function isShowOtherPriceReason(){	
		var dealerId = '${dealerId}';
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
			obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
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
		if(arg == '<%=Constant.TRANSPORT_TYPE_02%>') {
			obj1.style.display = "inline";
		}
		
		else{
			obj1.style.display = "none";
		}
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

	//申请提醒
	function applySubmit(){
		if(submitForm('fm')){
			if(!testLen(document.getElementById('orderRemark').value)) {
				MyAlert('备注录入已超过50个汉字的最大限！') ; 
				return false ;
			}
			
			if(!document.getElementById("addressId").value) {
				MyAlert('无可用发运地址！') ; 
				return false ;
			}
			
			var reqTotalAmount = document.getElementById("reqTotalAmount").value;
			if(reqTotalAmount==0){
				MyAlert("申请数量不能为零！");
	            return;
			}
			var isCheck = document.getElementById("isCheck").value;
			if(isCheck==0){
				var accountAmount = document.getElementById("availableAmount").value;
				var allAccount = document.getElementById("reqTotalPrice").value;
				if(Number(allAccount)>Number(accountAmount)){
					MyAlert("订单总价大于可用余额，请重新输入！");
					return;
				}
				
				var discountValue = document.getElementById("discount").value;
				var discountTotalPrice = document.getElementById("discountTotalPrice").value;
				if(Number(discountTotalPrice)>Number(discountValue)){
					MyAlert("折扣额合计大于可用折让，请重新输入！");
					return;
				}
			}
			var detailId = document.getElementsByName("detailId");
			var orderAmount = document.getElementsByName("orderAmount");
			var callAmount = document.getElementsByName("callAmount");
			var applyAmount = document.getElementsByName("applyAmount");
			var singlePrice = document.getElementsByName("singlePrice");
			var discountRate = document.getElementsByName("discountRate");
			var ratePara = parseInt(document.getElementById("ratePara").value, 10);
			for(var i=0; i<detailId.length; i++){
				if(parseFloat(singlePrice[i].value)==0 && parseInt(applyAmount[i].value) > 0){
					MyAlert("价格未维护物料启票数量不能大于0！");
					applyAmount[i].value = "0";
					return;
				}
				if(parseInt(applyAmount[i].value) > parseInt(orderAmount[i].value) - parseInt(callAmount[i].value)){
					MyAlert("本次提报数量不能大于提报数量减去已启票量！");
					return;
				}
				if(parseFloat(discountRate[i].value, 10) > ratePara) {
					MyAlert("折扣率不能大于"+ratePara+"%！");
					return;
				}
			}
			MyConfirm("确认申请？",putForword);
		}
	}
	//申请发运
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderreport/MonthGeneralOrderCall/applySubmit.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("申请成功！");
			window.parent.toQuery();
		} else if(json.returnValue == '2') {
			window.parent.MyAlert("申请失败：物料" + json.metStr + "允许启票数量已超出！");
			window.parent.toQuery();
		} else if(json.returnValue == '3') {
			window.parent.MyAlert("价格列表未加载成功，请等待价格列表加载完毕再提交！");
			window.parent.toQuery();
		} else{
			MyAlert("申请失败！请联系系统管理员！");
		}
	}
	//清除按钮
  	function toClear(){
		document.getElementById("fleetName").value="";
		document.getElementById("fleetId").value="";
  	}
    //大用户弹出
  	function showFleet(){
  		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
  	}
--></script>
</body>
</html>