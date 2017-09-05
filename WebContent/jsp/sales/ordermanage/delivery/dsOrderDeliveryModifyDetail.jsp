<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Expires" content="0"> 
<meta http-equiv="kiben" content="no-cache"> 

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运申请修改</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 发运申请修改</div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="area" name="area" value="${map.AREA_ID}">
	<table class="table_query">
	    <tr class="cssTable" >
	      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运申请单号：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	${map.DLVRY_REQ_NO}
	      </td>
		  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	 ${map.RAISE_DATE}
	      </td>
	        <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">采购单位：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">${map.DEALER_NAME1}</td>
	    </tr>
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">启票单位：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">${map.DEALER_NAME1}</td>
	  
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">原申请总价：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
		    <script>document.write(amountFormat(${map.REQ_TOTAL_PRICE}));</script>
		    <input type="hidden" name="dealerType" value="${map.DEALER_TYPE}">
	    </td>
	     <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">账户类型：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<select name="fundType" disabled id="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      	</select>
	    </td>
	    </tr>
	  <tr class="cssTable" >
	   
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">账户余额：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show1"></span><input type="hidden" name="availableAmount" value="">
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">冻结资金：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show2"></span><input type="hidden" name="availableAmount_1" value="">
	    </td>
	     <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运方式：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    		<script type="text/javascript">
	    		genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,'${map.DELIVERY_TYPE}',false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
			    </script>
	    </td>
	  </tr>
	  
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">可用余额：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show3"></span><input type="hidden" name="availableAmount_2" value="">
	    </td>	    
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter" style="display:none;">可用折让：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" style="display:none;" >
	    	<input type="text" name="discount" readonly="readonly">
	    </td>
	    </tr>
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>物料编号</th>
			<th>物料名称</th>
			<th>单价</th>
			<th>可申请数量</th>
			<th>已发运数量</th>
			<th>数量</th>
			<th>操作</th>
		</tr>
		<tbody id="tbody1">
			<c:forEach items="${list1}" var="list1" varStatus="vstatus">
				<tr align="center" class="table_list_row1">
					<td>
						${list1.MATERIAL_CODE}
					</td>
					<td>${list1.MATERIAL_NAME}</td>
					<td>${list1.SINGLE_PRICE}
					<input type="hidden" name="detailId" value="<c:out value="${list1.DETAIL_ID}" />" id="detailId${list1.MATERIAL_ID}"/>
					<input type="hidden" name="singlePrice" value="<c:out value="${list1.SINGLE_PRICE}" />" id="singlePrice${list1.MATERIAL_ID}"/>
					<input type="hidden" style="width:50px;" id="materialId${list1.MATERIAL_ID}" name="materialId" value="${list1.MATERIAL_ID}" datatype='0,is_digit,6'>
					</td>
					<td><span id="stock${list1.MATERIAL_ID}">${list1.RESOUCE_COUNT}</span>
						<input type="hidden" name="stockAmout" id="stockAmout${list1.MATERIAL_ID}" value="${list1.RESOUCE_COUNT}" />
					</td>
					<td>${list1.DELIVERY_AMOUNT}
						<input type="hidden" name="deliveryAmout" id="deliveryAmout${list1.MATERIAL_ID}" value="${list1.DELIVERY_AMOUNT}" />
					</td>
					<td>
					<input type="hidden" style="width:50px;" id="oldApplyAmount${list1.MATERIAL_ID}" name="oldApplyAmount" value="${list1.AMOUNT}"/>
					<input type="text" style="width:50px;" id="applyAmount${list1.MATERIAL_ID}" name="applyAmount" value="${list1.AMOUNT}" datatype='0,is_digit,6'
					onchange="toChangeNum(document.getElementById('singlePrice${list1.MATERIAL_ID}').value,<c:out value="${list1.MATERIAL_ID}"/>,<c:out value="${list1.RESOUCE_COUNT}"/>,<c:out value="${list1.DELIVERY_AMOUNT}"/>,this.value);" 
					/>
					</td> <%--已发运数量--%>
					<td><input type="hidden" id="orderPriceSum${list1.MATERIAL_ID}" name="orderPriceSum" value="" />	
					<input type="hidden" name="priceListId" value="${list1.PRICTLIST_ID}" value=""/>&nbsp;</td>
				</tr>
			</c:forEach> 
		</tbody>
		<tr align="center" class="table_list_row2">
			<td></td>
			<td align="right"><strong>合计：</strong></td>
			<td id="reqTotal"></td>
			<td id="reserveTotal"></td>
			<td><input type="hidden" id="priceTotalHidden" name="priceTotalHidden" value=""/></td>
			<td id="priceTotal">
			</td>
			<td></td>
		</tr>
	</table>
		<!--
		<table class="table_query" style="display:none;">
		    <tr class="cssTable" >
		      <td width="100%" align="left">
			      <input type="text" name="materialCode" size="15" id="materialCode" style="display:none;"/>
			      <input type="text" name="checkAmount" size="15" id="checkAmount" style="display:none;"/>
			      <input type="text" name="callAmount" size="15" id="callAmount" style="display:none;"/>
			      <input class='cssbutton' name="add22" id="add22" type="button" onclick="materialShow();" value ='新增产品' />
		          &nbsp;
		      </td>
		    </tr>
		</table>
	<br>
	-->
	<table class="table_query">
		<tr>
			<td align="right" width="10%">价格类型：</td>
			<td align="left" width="90%" colspan="3">
				<input type="text" id="price" name="priceId" value="${priceId}" class="longTxt" readonly="readonly"/>
			</td>
		</tr>
		<tr class="cssTable" id="tmp_license_amountTr">
	      <td width="12%" align="right" valign="top" nowrap="nowrap">临牌数量：</td>
	      <td colspan="3" align="left" valign="top" nowrap="nowrap">
	      	<input id="tmp_license_amount" name="tmp_license_amount" type="text" class="middle_txt" size="10" maxlength="30" value="${map.TMP_LICENSE_AMOUNT}" datatype="1,is_double,20" decimal="2"/>
	      </td>
	    </tr>
		<tr id="otherTr" style="display:none;">
			<td align="right" width="15%">使用其他价格原因：</td>
			<td align="left" width="85%" colspan="3">
				<input name="otherPriceReason" type="text" class="long_txt" value="${map.OTHER_PRICE_REASON}"/>
			</td>
		</tr>
		<tbody id="addTr" style="display:none;">
		
			<tr>
				<td align="right">收货方：</td>
				<td align="left" colspan="3">
				<select name="receiver" onchange="getAddressList();">
			      	</select>
			  </td>
			</tr>
			<tr>
				<td align="right">运送地点：</td>
				<td align="left"><select name="addressId" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
			      	</select></td>
				<td align="right">收车单位：</td>
				<td align="left" class="cssTable" id="receiveOrg" colspan="4">
				</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left"><input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30"  /></td>
				<td align="right">联系电话：</td>
				<td align="left"><input name="tel" type="text" class="middle_txt" size="30" maxlength="30" /></td>
			</tr>
		</tbody>
		<c:if test="${map.IS_FLEET==1}">
			<tr>
				<td align="right">代交车：</td>
				<td align="left" colspan="3">是</td>
			</tr>
			<tr>
				<td align="right">集团客户名称：</td>
				<td align="left"><c:out value="${map.FLEET_NAME}"/></td>
				<td align="right">运送地点：</td>
				<td align="left"><c:out value="${map.FLEET_ADDRESS}"/></td>
			</tr>
		</c:if>
		<c:if test="${map.ORDER_TYPE==10201003}">
			<tr>
				<td align="right">改装说明：</td>
				<td align="left" colspan="3"><c:out value="${map.REFIT_REMARK}"/></td>
			</tr>
		</c:if>
		<tr>
			<td align="right">付款信息备注：</td>
			<td align="left" colspan="3"><c:out value="${map.PAY_REMARK}"/></td>
		</tr>
		<tr>
			<td align="right">备注说明：</td>
			<td align="left" colspan="3"><textarea name="orderRemark" id="orderRemark" cols="30" rows="3" onkeyup="value = value.replace(/#/g, '') ;" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 70);}" onchange="limitChars('orderRemark', 70)"><c:out value="${map.ORDER_REMARK}"/></textarea><font color="red">建议录入不超过50个汉字!</font></td>
		</tr>
		<tr>
			<td align="right">修改描述：</td>
			<td align="left" colspan="3"><textarea name="checkRemark" id="checkRemark" cols="30" rows="3"></textarea></td>
		</tr>
	</table>
	<br>
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="hidden" name="checkGeneral" value="${checkGeneral}">
				<input type="hidden" name="flag" value="1">
				<input type="hidden" name="orderId" value="${orderId}"/>
				<input type="hidden" name="reqVer" value="${map.VER}"/>
				<input type="hidden" name="reqId" value="${reqId}"/>
				<input type="hidden" name="todId" value="${todId}"/>
				<input type="hidden" name="dealerId" value="${map.DEALER_ID}"/>
				<input type="hidden" name="orderDealerId" value="${map.DEALER_ID}"/>
				<input type="hidden" name="reqTotalPrice" value=""/>
				<input type="hidden" name="discountTotalPrice" value=""/>
				<input type="hidden" name="modifyFlag" value="0">
				<input type="hidden" name="orderType" id="orderType" value="10201003"/>
				<input type="hidden" name="reserveTotalAmount" value="0"> 
	      	    <input type="hidden" name="accountId" value="">
	      	    <input type="hidden" name="discountAccountId" value="">
	      	    <input type="hidden" name="dealerCode" value="${dealerCode}">
	      	    <input type="hidden" name="areaId" value="${areaId}">
	      	    <input type="hidden" name="groupCode" value="${groupCode}">
	      	    <input type="hidden" name="reqStatus" value="${reqStatus}">
	      	    <input type="hidden" name="orgCode" value="${orgCode}">
	      	    <input type="hidden" name="startDate" value="${startDate}">
	      	    <input type="hidden" name="endDate" value="${endDate}">
	      	    <input type="hidden" name="oldFundTypeName" value="${map.TYPE_NAME}">
	      	    <input type="hidden" name="oldDeliveryTypeName" value="${map.DELIVERY_TYPE_NAME}">
	      	    <input type="hidden" name="oldPriceDesc" value="${map.PRICE_DESC}">
	      	    <input type="hidden" id="historyCount" value="${historyCount }" />
	      	    <input type="hidden" name="orderTypeName" value="${map.ORDER_TYPE_NAME}"/>
	      	    <input type="hidden" name="receiver" value="${map.RECEIVER}"/>
				<input type="button" id="queryBtn1" name="button1" class="cssbutton" onclick="priceChk();" value="修改完成"/>
				<input type="button" name="button3" class="cssbutton" onclick="history.back();" value="返回"/>
				<input type="hidden" name="ratePara" value="${ratePara}" />
				<input type="hidden" name="erpCode" value="${erpCode}" />
				<!-- 资金类型是否是“兵财” -->
				<input type="hidden" id="isBingcai" name="isBingcai" value="" />
				<!-- 原资金类型 -->		
				<input type="hidden" id="old_fund_type_id" name="old_fund_type_id" value="" />	
					<input type="hidden" id="typeId" name="typeId" value="" />	
				
				<input type="hidden" name="applysAmounts" id="applysAmounts" value=""/>
				<input type="hidden" name="oldApplyAmounts" id="oldApplyAmounts" value=""/>
				<input type="hidden" name="detailIds" id="detailIds" value=""/>
				<input type="hidden" name="orderIds" id="orderIds" value=""/>
				<input type="hidden" name="areaIds" id="areaIds" value=""/>
				<input type="hidden" name="cAmounts" id="cAmounts" value=""/>
				<input type="hidden" name="orderPriceSums" id="orderPriceSums" value=""/>
				<input type="hidden" name="materialIds" id="materialIds" value=""/>
				<input type="hidden" name="singlePrices" id="singlePrices" value=""/>	
				<input type="hidden" name="priceListIds" id="priceListIds" value=""/>	
				
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
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
	//初始化
	function doInit(){
		getFund();
		//priceTotal();
		//getPriceList();
		//addressHide(document.getElementById("deliveryType").value);
		getReceiverList();
		//getStock(1);setRead();
		//getStockSuzuki();
	}
	
	function setRead() {
		var aPrice = document.getElementsByName("singlePrice") ;
		var par = ${par} ;
		
		if(par <= 0) {
			var iLen = aPrice.length ;
			
			for(var i=0; i<iLen; i++) {
				aPrice[i].readOnly = true ;
			}
		}
	}
	
	//取消校验
	function reserveAmountConfirm(){
		if(!chkPriceList()) {
			MyAlert("请选择价格列表!") ;
			
			return false ;
		}
		MyConfirm("确认保存？",reserveAmountSave);
	}
	function reserveAmountSave(){
		disableBtn($("queryBtn1"));
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/reserveAmountConfirm.json",showreserveAmountConfirm,'fm');
	}
	function showreserveAmountConfirm(json){
		if(json.returnValue == '1'){
			//MyAlert("保留数量确认成功!");
			$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyQueryPre.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改,保存失败！");
			useableBtn($("queryBtn1"));
		}else if(json.returnValue == '3'){
			MyAlert("保存失败！可用余额不足！");
			useableBtn($("queryBtn1"));
			
			
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			useableBtn($("queryBtn1"));
			
			
		}
	}
	function getFundTypeList(){
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFundTypeList.json";
		makeCall(url,showFundTypeList,{dealerId:dealerId});
	}
	
	//资金类型列表显示
	function showFundTypeList(json){
		var obj = document.getElementById("fundType");
		document.getElementById("old_fund_type_id").value = '${map.FUND_TYPE_ID}';
		$("typeId").value='${map.FUND_TYPE_ID}';
		obj.options.length = 0;
		for(var i=0;i<json.fundTypeList.length;i++){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
			if(json.fundTypeList[i].TYPE_ID + "" == '${map.FUND_TYPE_ID}'){
				obj.options[i].selected = true;
			}
		}
		var fundType = document.getElementById("fundType").value;
		var fundTypes = fundType.split("|");
		getAvailableAmount(fundTypes[0]);
	}
	
	function getDiscountInfo(){
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showDiscountInfo.json";
		makeCall(url,showDiscountInfo,{dealerId:dealerId});
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
		getDiscountAmount();
	}
	
	function getDiscountAmount(){
		var accountId = document.getElementById("discountAccountId").value;
		var reqId = '${reqId}';
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getDiscountAmount.json";
		makeCall(url,showDiscountAmount,{accountId:accountId,reqId:reqId});
	}
	
	function showDiscountAmount(json){
		document.getElementById("discount").value = json.returnValue;
	}
	
	function getFund(){
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:dealerId});
	}
	
	function showFund(json){
		showFundTypeList(json);
		//showDiscountInfo(json);
	}
	
	
	// 合计价格
	function priceTotal(){
		var reqTotal = 0;
		var reserveTotal = 0;
		var priceTotal = 0;
		var materialId = document.getElementsByName("materialId");
		
		for(var i=0; i<materialId.length; i++){
			var subStr = materialId[i].value;
			var req = parseInt(document.getElementById("reqAmount"+subStr).value, 10);
			var reserve = parseInt(document.getElementById("reserveAmount"+subStr).value, 10);//保留数量
			var singlePrice = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);//单价
			
			//var totalPrice = req*singlePrice;
			var totalPrice = reserve * singlePrice;
			
			document.getElementById("totalPrice"+subStr).value = totalPrice;//金额
			document.getElementById("price2"+subStr).innerHTML = totalPrice == 0 ? "0" : amountFormat(totalPrice);//金额显示
			
			reqTotal += req;
			reserveTotal += reserve;
			priceTotal += totalPrice;
		}
<%--		document.getElementById("reqTotal").innerHTML = reqTotal;--%>
		document.getElementById("reserveTotal").innerHTML = reserveTotal;
		document.getElementById("reserveTotalAmount").value = reserveTotal;
		document.getElementById("priceTotal").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		document.getElementById("priceTotalHidden").value = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		document.getElementById("reqTotalPrice").value = parseFloat(priceTotal, 10);
	}
	
	function getAvailableAmount(arg){
		var fundType = document.getElementById("fundType").value;
		var fundTypes = fundType.split("|");
		var dealerId = document.getElementById("dealerId").value;
		var fundTypeId = fundTypes[0];
		var reqId = '${reqId}';
		var orderType = '${map.ORDER_TYPE}';
		document.getElementById("isBingcai").value = fundTypes[1];
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId,reqId:reqId,orderType:orderType});
	}
	
	function showAvailableAmount(json){
		var obj1 = document.getElementById("show1");//视图账户余额
		var obj2 = document.getElementById("availableAmount");
		obj1.innerHTML = amountFormat(parseFloat(json.returnValue1, 10));
		obj2.value = parseFloat(json.returnValue1, 10);
		var obj3 = document.getElementById("show2");//冻结资金
		var obj4 = document.getElementById("availableAmount_1");
		obj3.innerHTML = amountFormat(parseFloat(json.returnValue2, 10));
		obj4.value = parseFloat(json.returnValue2, 10);
		var obj5 = document.getElementById("show3");//可用余额
		var obj6 = document.getElementById("availableAmount_2");
		obj5.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
		obj6.value = parseFloat(json.returnValue, 10);		
		document.getElementById("accountId").value = json.accountId;
	}
	
	//获得价格类型列表
	function getPriceList(){	
		var dealerId = document.getElementById("dealerId").value;
		var reqId = document.getElementById("reqId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getPriceListByReqId.json";
		makeCall(url,showPriceList,{dealerId:dealerId,reqId:reqId});
	}	
	
	//显示价格类型列表
	function showPriceList(json){
		var obj = document.getElementById("priceId");
		obj.options.length = 0;
		obj.options[0] = new Option("-请选择-", "");
		for(var i=0;i<json.priceList.length;i++){
			if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
				obj.options[i+1] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
				obj.options[i+1].selected = "selected";
			}
			else{
				obj.options[i+1] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
			}
		}
		isShowOtherPriceReason();
	}
	
	//价格类型改变
	function priceTypeChange(){	
		var priceId = document.getElementById("priceId").value;
		if(priceId) {
			document.getElementById("queryBtn1").disabled = true ;
			
			var ids = "";//已选中的物料id
			var materialId = document.getElementsByName("materialId");
			for(var i=0; i<materialId.length; i++){
				ids += materialId[i].value + ",";
			}	
			ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getSinglePriceList.json";
			makeCall(url,priceChange,{priceId:priceId, ids:ids});
		}
	}	
	
	//价格类型改变处理
	function priceChange(json){
		for(var i=0;i<json.priceList.length;i++){
			var id = json.priceList[i].MATERIAL_ID;
			var price = json.priceList[i].PRICE;
			
			var obj2 = document.getElementById("singlePrice" + id);
			var obj3 = document.getElementById("priceDesc" + id);
			obj3.innerHTML = "";
			if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
				price = 0;
				obj3.innerHTML = "<font color='red'>价格未维护</font>";
			}
			obj2.value = price;
		}
		isShowOtherPriceReason();//是否显示使用其他价格原因
		priceTotal();
		
		document.getElementById("queryBtn1").disabled = false ;
	}
	
	// 判断是否显示使用其他价格原因
	function isShowOtherPriceReason(){	
		var dealerId = document.getElementById("dealerId").value;
		var priceId = document.getElementById("priceId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/isShowOtherPriceReason.json";
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
	
	// 新增产品
	function addMaterial(){	
		getMaterialInfo();
	}
	
	// 判断是否有价格
	function isHasPrice(json){	
		if(json.returnValue == '1'){
			MyAlert("您选中的产品没有在开票方的价格列表中维护价格,不能添加！");
			
			if(document.getElementById("add22")) {
				document.getElementById("add22").disabled = false ;
			}
			
			return false;
		}
		else{
			getMaterialInfo();
		}
	}
	
	// 获得产品信息
	function getMaterialInfo(){
		var priceId = document.getElementById("priceId").value;
		var materialCode = document.getElementById("materialCode").value;
		var orderDealerId = document.getElementById("dealerId").value;
		var orderType = '10201003';
		var url = "<%=request.getContextPath()%>/sales/ordermanage/delivery/DsOrderDelvyModify/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode,priceId:priceId,orderDealerId:orderDealerId,orderType:orderType}); 
	}
	
	function addRow(json){
		if(parseFloat(json.infos.OPERAND, 10) < <%=Constant.MATERIAL_PRICE_MAX%>){
			var timeValue = json.info.MATERIAL_ID;
			var checkAmount = json.info.CHECK_AMOUNT;
			var callAmount = json.info.CALL_AMOUNT;
			var newRow = document.getElementById("tbody1").insertRow();
			newRow.className  = "table_list_row1";
			newCell = newRow.insertCell(0);//物料编号
			newCell.align = "center";
			var tempHtml = json.info.MATERIAL_CODE;
			tempHtml += "<input type='hidden' id='detailId"+timeValue+"' name='detailId' value=''/>";
			tempHtml += "<input type='hidden' id='materialId"+timeValue+"' name='materialId' value='"+timeValue+"'/>";
			tempHtml += "<input type='hidden' id='reqAmount"+timeValue+"' name='reqAmount' value='0'/>";
			tempHtml += "<input type='hidden' id='priceList"+timeValue+"' name='priceList'   value='"+json.infos.LIST_HEADER_ID+"' />";
			tempHtml += "<input type='hidden' name='singlePrice' value='" + json.info.SINGLE_PRICE + "' id='" + json.info.MATERIAL_ID + "'/>";
			newCell.innerHTML = tempHtml;
			newCell = newRow.insertCell(1);//物料名称
			newCell.innerHTML = json.info.MATERIAL_NAME;
			
			newCell = newRow.insertCell(2);//单价
			newCell.innerHTML = json.info.SINGLE_PRICE;
			
			newCell = newRow.insertCell(3);//可申请数量
			newCell.innerHTML = "<span id='stock"+timeValue+"'>" + json.info.ABLE_APPLY  + "</span>";
			
			newCell = newRow.insertCell(4);//已发运数量
			newCell.innerHTML = "<span id='stock"+timeValue+"'>0</span>";
			
			newCell = newRow.insertCell(5);//数量
			newCell.innerHTML = "<input type='hidden' style='width:50px;' id='oldApplyAmount"+timeValue+"' name='oldApplyAmount' value='0' /><input type='text' style='width:50px;' onchange='toChangeNum(" + json.info.SINGLE_PRICE + "," + json.info.DEALER_ID + "," + json.info.ABLE_APPLY + ",0,this.value)' id='applyAmount"+timeValue+"' name='applyAmount' value='0' datatype='0,is_digit,6' /><font color='red'>*</font>";
			
			newCell = newRow.insertCell(6);//操作
			newCell.innerHTML = "<input type='hidden' id='orderPriceSum" + json.info.MATERIAL_ID +"' name='orderPriceSum' value='' /><input type='hidden' name='priceListId' value='" + json.info.PRICTLIST_ID + "'/><a href='#' onclick='delMaterial();'>[删除]</a>";
			priceTotal();
			//getStock(timeValue);
			//	setRead() ;
			
			
		}
		else{
			MyAlert("该款物料尚未维护价格，不能添加！");
			
			if(document.getElementById("add22")) {
				document.getElementById("add22").disabled = false ;
			}
			
			return false;
		}
	}
	
	// 删除产品
	function delMaterial(){
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-1);  
	  	priceTotal();
	}
	
	// 物料组树
	function materialShow(){
		
			var ids = "";//已选中的物料id
			var materialId = document.getElementsByName("materialId");
			for(var i=0; i<materialId.length; i++){
				ids += materialId[i].value + ",";
			}	
			ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
			var areaId = document.getElementById("area").value;	
			var productId = "" ;
			
			if(document.getElementById("_productId_")) {
				productId = document.getElementById("_productId_").value ;
			}
			var orderId = document.getElementById("orderId").value;
			var orderType = '10201003';
			var areaId=$("areaId").value;
			showMaterialByAreaId_Suzuki('materialCode','','true',areaId,ids, '',orderType);
	}
	
	//仓库改变
	function houseChange(){	
		var materialId = document.getElementsByName("materialId");
		for (var i=0; i<materialId.length; i++){  
			var subStr = materialId[i].value;
			var reserveValue = document.getElementById("reserveAmount"+subStr).value;
			document.getElementById("avaPrice"+subStr).innerHTML = parseInt(document.getElementById("avaPrice"+subStr).innerHTML, 10) + parseInt(reserveValue, 10);
			document.getElementById("stock"+subStr).innerHTML = parseInt(document.getElementById("stock"+subStr).innerHTML, 10) + parseInt(reserveValue, 10);
			document.getElementById("reserveAmount"+subStr).value = 0;//保留数量
			document.getElementById("batchNo"+subStr).value = "";//对应批次
			document.getElementById("batch"+subStr).innerHTML = "";//对应批次显示  
		} 
		priceTotal();	
		getStock();
	}
	
	//批次号选择
	function patchNoSelect(materalId){
		var wareHouseId = document.getElementById("wareHouseId").value;
		var batchNo = document.getElementById("batchNo"+materalId).value;
		var reserveAmount = document.getElementById("reserveAmount"+materalId).value;
		var orderType = '${map.ORDER_TYPE}';
		var specialBatchNo = document.getElementById("specialBatchNo"+materalId).value;
		var initHouseId = '${map.WAREHOUSE_ID}'
		var initNo = (initHouseId != "" && initHouseId == wareHouseId) ? document.getElementById("initNo"+materalId).value : "";
	    var reqAmount = document.getElementById("reqAmount"+materalId).value;
	    var shipNumber = document.getElementById("shipNumber"+materalId).value;
	    var checkAmount = document.getElementById("checkAmount"+materalId).value;
	    var callAmount = document.getElementById("callAmount"+materalId).value;
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/patchNoSelect.do?wareHouseId='+wareHouseId+'&materalId='+materalId+'&batchNo='+batchNo+'&amount='+reserveAmount+'&orderType='+orderType+'&specialBatchNo='+specialBatchNo+'&initNo='+initNo+'&reqAmount='+reqAmount+'&shipNumber='+shipNumber+'&callAmount='+callAmount+'&checkAmount='+checkAmount,700,500);
	}
	
	//保存校验
	function toSaveCheck(){
		//if(submitForm('fm')){
			if(confirm("确认修改吗？？？")){
				putForword();
			}
			//MyConfirm(returnStr,putForword);
		//}
	}
	
	//审核保存提交
	function putForword(){
	
			var cnt = 0;
			var applyAmount = '';
			var oldApplyAmount = '';
			var materialId = '';
			var singlePrice = '';
			var orderPriceSum = '';     //订单价格合计
			var priceListIds='';
			var detailId='';
			var Amounts = document.getElementsByName("applyAmount");
			var OldAmounts = document.getElementsByName("oldApplyAmount");
			var singlePrices = document.getElementsByName("singlePrice");
			var materialIds = document.getElementsByName("materialId");
			var detailIds = document.getElementsByName("detailId");
			var orderPriceSums = document.getElementsByName("orderPriceSum");
			var priceListId = document.getElementsByName("priceListId");
			var deliveryAmouts = document.getElementsByName("deliveryAmout");
			var stockAmouts=document.getElementsByName("stockAmout");
			for(var i=0 ;i< Amounts.length; i++){
				if(deliveryAmouts[i].value>Amounts[i].value){
					MyAlert("第"+(i+1)+"行，申请数量必须大于等于已发运数！");
					return;
				}
				if(Amounts[i].value>stockAmouts[i].value){
					MyAlert("第"+(i+1)+"行，申请数量必须小于等于可申请数！");
					return;
				}
				cnt = cnt+Amounts[i].value;
				applyAmount = Amounts[i].value + ',' + applyAmount;
				oldApplyAmount = OldAmounts[i].value + ',' + oldApplyAmount;
				singlePrice = singlePrices[i].value + ',' + singlePrice;
				materialId = materialIds[i].value + ',' + materialId;
				orderPriceSum = orderPriceSums[i].value+','+orderPriceSum;			//订单价格合计
				if (null != priceListId && priceListId.length > 0) {
					priceListIds =priceListId[i].value+','+priceListIds;
				} else {
					priceListIds = ',' + priceListIds;
				}
				detailId=detailIds[i].value+","+detailId;
			}
			
			
			document.getElementById("applysAmounts").value = applyAmount;
			document.getElementById("oldApplyAmounts").value = oldApplyAmount;
			document.getElementById("materialIds").value = materialId;
			document.getElementById("singlePrices").value = singlePrice;
			document.getElementById("priceListIds").value=priceListIds;
			document.getElementById("orderPriceSums").value = orderPriceSum;
			
			document.getElementById("detailIds").value=detailId;
			//if(cnt==0){
			//	MyAlert("申请数量不能为零！");
	          //  return;
			//}
		disableBtn($("queryBtn1"));
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/delivery/DsOrderDelvyModify/orderResourceModifySave.json",showForwordValue,'fm');
	}
	
	function showForwordValue(json){
		if(json.returnValue == '1'){
			$('fm').action = "<%=contextPath%>/sales/ordermanage/delivery/DsOrderDelvyModify/doInit.do" ;
			$('fm').submit() ;
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改,保存失败！");
			useableBtn($("queryBtn1"));
		}else if(json.returnValue == '3'){
			MyAlert("提交失败！可用余额不足！");
			useableBtn($("queryBtn1"));
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			useableBtn($("queryBtn1"));
			
		}
	}
	
	//根据运输方式隐藏地址列表
	function addressHide(arg){
		
		var obj1 = document.getElementById("addTr");
		//自提 addTr
		if(arg == '<%=Constant.TRANSPORT_TYPE_01%>') {
			obj1.style.display = "none";
		}
		else if(arg == '<%=Constant.TRANSPORT_TYPE_02%>'){
			obj1.style.display = "inline";
		}
	}
	
	//获得收货方列表
	function getReceiverList(){	
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getReceiverList.json";
		makeCall(url,showReceiverList,{dealerId:dealerId});
	}	
	
	//显示收货方列表
	function showReceiverList(json){
		var obj = document.getElementById("receiver");
		obj.options.length = 0;
		for(var i=0;i<json.receiverList.length;i++){
			obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
			if(json.receiverList[i].DEALER_ID + "" == '${map.RECEIVER}'){
				obj.options[i].selected = "selected";
			}
		}
		getAddressList();//获得发运地址列表
	}
	
	function getAddressList(){	
		var dealerId = document.getElementById("receiver").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/DsOrderReport/getAddressList.json";
		makeCall(url,showAddressList,{dealerId:dealerId});
	}	
	
	function showAddressList(json){
		var obj = document.getElementById("addressId");
		obj.options.length = 0;
		for(var i=0;i<json.addressList.length;i++){
			obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
			if(json.addressList[i].ID + "" == '${map.ADDRESS_ID}'){
				obj.options[i].selected = true;
			}
		}
		getAddressInfo(document.getElementById("addressId").value);
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
			if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
				obj1.value = json.info.LINK_MAN;
			}
			if(json.info.TEL != null && json.info.TEL != "null"){
				obj2.value = json.info.TEL;
			}
		}
		
		if(json.info.RECEIVE_ORG != null && json.info.RECEIVE_ORG != "null"){
			obj3.innerHTML = json.info.RECEIVE_ORG;
		}
	}
	
	//获得可用库存
	function getStock(arg){
		var ids = "";//已选中的物料id
		if(arg == "1"){
			return ;
		}
		if(arg == null || arg == ""){
			var materialId = document.getElementsByName("materialId");
			for (var i=0; i<materialId.length; i++){  
				ids += materialId[i].value + ","; 
			} 	
			ids = (ids == "" ? ids : ids.substring(0,ids.length-1));	
		}
		else{
			ids += arg;
		}
		var warehouseId = document.getElementById("warehouseId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getStock.json";
		makeCall(url,showStock,{warehouseId:warehouseId, ids:ids});
	}	
	
	//可用库存显示
	function showStock(json){
		for(var i=0;i<json.stockList.length;i++){
			var id = json.stockList[i].MATERIAL_ID;
			var amount = json.stockList[i].AMOUNT;
			
			var obj2 = document.getElementById("stock" + id);
			obj2.innerHTML = amount;
		}
	}
	
	//可用库存默认显示
	function getStockSuzuki(){
		var id = "";//已选中的物料id
		var materialId = document.getElementsByName("materialId");
		for (var i=0; i<materialId.length; i++){  
			id = materialId[i].value;
			var reserveAmount = document.getElementById("reserveAmount" + id);//保留数量
			if(reserveAmount.value == 0){
				var avaPrice = document.getElementById("avaPrice" + id);//库存总量
				var stock = document.getElementById("stock" + id);//可用库存
				var reqAmount = document.getElementById("reqAmount" + id).value;//申请数量
				var subAmount = Number(avaPrice.innerHTML) - Number(reqAmount);
				if(subAmount >= 0){
					reserveAmount.readOnly = false;
					reserveAmount.value = Number(reqAmount);
					reserveAmount.readOnly = true;
					
					stock.innerHTML = subAmount;
					avaPrice.innerHTML = subAmount;
					//批次
					var returnStr1 = "1-"+Number(reqAmount)+"/";
			        document.getElementById("batchNo"+ id).value = returnStr1;
			        document.getElementById("batch" + id).innerHTML = returnStr1;					
				}else{
					if(Number(avaPrice.innerHTML) >= 0){
						reserveAmount.readOnly = false;
						reserveAmount.value = Number(stock.innerHTML);
						reserveAmount.readOnly = true;
						//批次
						var returnStr1 = "1-"+Number(avaPrice.innerHTML)+"/";
				        document.getElementById("batchNo"+ id).value = returnStr1;
				        document.getElementById("batch" + id).innerHTML = returnStr1;
				        stock.innerHTML = 0;
						avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML);   
					}else{
						reserveAmount.readOnly = false;
						reserveAmount.value = '0';
						reserveAmount.readOnly = true;
						// stock.innerHTML = 0;
						//avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML); 
					}		        						
				}
			}else{
				getStock();
			}
		}	
		priceTotal();
	}
	
	function singlePriceChange(arg){
		var singlePrice = document.getElementById("priceDesc"+arg).value;
		document.getElementById("singlePrice"+arg).value = singlePrice;
		priceTotal();
	}
	
	//取消校验
	function toCancelCheck(){
		MyConfirm("确认取消？",cancelForword);
	}
	
	//取消提交
	function cancelForword(){
		disableBtn($("queryBtn1"));
		
		
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyCancel.json",showCancelValue,'fm');
	}
	
	function showCancelValue(json){
		if(json.returnValue == '1'){
			$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyQueryPre.do';
	 		$('fm').submit();
		}else{
			MyAlert("数据已被修改,取消失败！");
			useableBtn($("queryBtn1"));
			
		}
	}
	
	function chkPriceList() {
		var objPriceList = document.getElementById("priceId").value ;
		
		if(!objPriceList) {
			return false ;
		} else {
			return true ;
		}
	}
	
	function chkReserverAmount() {
		var aReqAmount = document.getElementsByName("reqAmount") ;
		var aReserveAmount = document.getElementsByName("reserveAmount") ;
		
		var iLen = aReqAmount.length ;
		
		for(var i=0; i<iLen; i++) {
			if(parseInt(aReqAmount[i].value) != 0 && aReserveAmount[i].value != '') {
				if(parseInt(aReqAmount[i].value) < parseInt(aReserveAmount[i].value)) {
					return false ;
				}
			}
		}
		
		return true ;
	}
	
	function priceChk_() {
			reserveAmountConfirm() ;
	}
	
	function retPriceResult_(json) {
		var flag = json.priComparaFlag ;
		
		if(flag == 1 || flag == 2) {
			reserveAmountConfirm() ;
		} else if(flag == -1) {
			MyAlert("操作失败：结算中心模式不允许保留资源后的订单金额大于之前金额！") ;
			
			return false ;
		}
	}
	
	function priceChk() {
		var typeId=document.getElementById("fundType").value.split('\|')[0];
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/checkExists.json?typeId="+typeId ;
		makeFormCall(url, compartToAction, "fm") ;
		
	}
	function compartToAction(json){
			toSaveCheck() ;
	}
	function retPriceResult(json) {
		var flag = json.priComparaFlag ;
		
		if(flag == 1 || flag == 2) {
			toSaveCheck() ;
		} else if(flag == -1) {
			MyAlert("操作失败：结算中心模式不允许保留资源后的订单金额大于之前金额！") ;
			
			return false ;
		}
	}
	
	//本次申请数量与价格合计联动
	function toChangeNum(value1,value2,value3,value4,value5){
		var price = Number(value1);
		var detailId = Number(value2);
		var checkAmount = Number(value3);
		var callAmount = Number(value4);
		var applyAmount = Number(value5);
		if(price == 0){
			MyAlert("价格未维护，不能申请！");
			document.getElementById("applyAmount"+detailId).value="0";
			document.getElementById("applyAmount"+detailId).focus();
		}

		if(applyAmount>checkAmount){
			MyAlert("本次申请数量过大，请重新输入！");
			//document.getElementById("applyAmount"+detailId).value="0";
			document.getElementById("applyAmount"+detailId).focus();
		}if(applyAmount<callAmount){
			MyAlert("本次申请数量必须大于已发运数，请重新输入！");
			document.getElementById("applyAmount"+detailId).focus();
		}else{
			var trobj = document.getElementById("tbody1").getElementsByTagName("tr");
			var sumAccount = 0;
			for (var i = 0; i < trobj.length; i++) {
				var tdobj = trobj[i].getElementsByTagName("td");
				var td_checkAmount = parseInt(tdobj[2].innerText, 0);
				var td_callAmount = parseInt(tdobj[5].getElementsByTagName("input")[0].value, 0);
				sumAccount += td_checkAmount * td_callAmount;
				document.getElementsByName("orderPriceSum")[i].value = td_checkAmount * td_callAmount;
			}
			document.getElementById("reqTotal").innerText=amountFormat(sumAccount);
			
			/*
			var sumAccount = applyAmount*price;
			<c:forEach items="${list1}" var="list1">
	        var id = <c:out value="${list1.MATERIAL_ID}"/>
	        if(detailId==Number(id)){
	       	 document.getElementById("reqTotal").innerText=amountFormat(sumAccount);
	       	 document.getElementById("orderPriceSum"+detailId).value=sumAccount;
	        }
	        </c:forEach>
	        */
	       // var discount_rate = document.getElementById("discount_rate"+detailId).value;
	       /*
	       var  discount_rate=0;
	        var single_Price = document.getElementById("tempPrice"+detailId).value;
	        var applyAmount = document.getElementById("applyAmount"+detailId).value;
	        discountRateChange(detailId,discount_rate,single_Price,applyAmount)
	        
			changeAllAccount();
			*/
		}
	}
	
</script>
</body>
</html>