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
<title>发运指令资源批次调整</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 发运指令资源批次调整</div>
<form method="post" name="fm" id="fm">
	<table class="table_query">
		<tr class="cssTable" >
	    <td width="15%" align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">业务范围：</td>
	    <td width="35%" align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	${map.AREA_NAME}
	    </td>
	    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">大区：</td>
	    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
			${map.ORG_NAME}
		</td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    </td>
	    </tr>
	    <tr class="cssTable" >
	      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">销售订单号：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	${map.ORDER_NO}
	      </td>
	      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单类型：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	${map.ORDER_TYPE_NAME} 
	      </td>
	      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	 ${map.RAISE_DATE}
	      </td>
	    </tr>
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">启票单位：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">${map.DEALER_NAME2}</td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">采购单位：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">${map.DEALER_NAME1}</td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">原申请总价：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
		    <script>document.write(amountFormat(${map.REQ_TOTAL_PRICE}));</script>
		    <input type="hidden" name="dealerType" value="${map.DEALER_TYPE}">
		    <input type="hidden" name="oldPrice" value="${map.REQ_TOTAL_PRICE}">
	    </td>
	    </tr>
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">资金类型：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	<input type="hidden" name="fundType" value="${map.FUND_TYPE_ID}">
	      	${map.TYPE_NAME}
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">余额：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show1"></span><input type="hidden" name="availableAmount" value="">
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">可用折让：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" id="discount">
	    </td>
	  </tr>
	  
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">库存组织：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	${map.WAREHOUSE_NAME}
	      	<input type="hidden" name="warehouseId" value="${map.WAREHOUSE_ID}">
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运方式：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<input type="hidden" name="deliveryType" value="${map.DELIVERY_TYPE}">
	    	${map.DELIVERY_TYPE_NAME}
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input"></td>
	    </tr>
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>物料编号</th>
			<th>物料名称</th>
			<c:if test="${map.ORDER_TYPE==10201003}">
				<th>订做批次号</th>
			</c:if>
			<th>申请数量</th>
			<th>保留数量</th>
			<th>单价</th>
			<th>金额</th>
			<th>折扣率 %</th>
			<th>折扣后单价</th>
			<th>折扣额</th>
			<th>可用库存</th>
			<th>可用库存总量</th>
			<th>未满足常规订单</th>
			<th>对应批次</th>
		</tr>
		<tbody id="tbody1">
			<c:forEach items="${list1}" var="list1" varStatus="vstatus">
				<tr align="center" class="table_list_row1">
					<td>
						${list1.MATERIAL_CODE}
						<input type="hidden" id="detailId${list1.MATERIAL_ID}" name="detailId" value="${list1.DETAIL_ID}"/>
						<input type="hidden" name="ver" value="${list1.VER}"/>
						<input type="hidden" id="orderDetailId${list1.MATERIAL_ID}" name="orderDetailId" value="${list1.ORDER_DETAIL_ID}"/>
						<input type="hidden" id="materialId${list1.MATERIAL_ID}" name="materialId" value="${list1.MATERIAL_ID}"/>
						<input type="hidden" id="reqAmount${list1.MATERIAL_ID}" name="reqAmount" value="${list1.REQ_AMOUNT}"/>
						<input type="hidden" id="totalPrice${list1.MATERIAL_ID}" name="totalPrice" value="${list1.TOTAL_PRICE}"/>
						<input type="hidden" id="discountSPrice${list1.MATERIAL_ID}" name="discountSPrice" value="${list1.DISCOUNT_S_PRICE}"/>
						<input type="hidden" id="discountPrice${list1.MATERIAL_ID}" name="discountPrice" value="${list1.DISCOUNT_PRICE}"/>
						<input type="hidden" id="batchNo${list1.MATERIAL_ID}" name="batchNo" value="${list1.BATCH_NO}"/>
						<input type="hidden" id="specialBatchNo${list1.MATERIAL_ID}" name="specialBatchNo" value="${list1.SPECIAL_BATCH_NO}"/>
						<input type="hidden" id="initNo${list1.MATERIAL_ID}" name="initNo" value="${list1.BATCH_NO}"/>
					</td>
					<td>${list1.MATERIAL_NAME}</td>
					<c:if test="${map.ORDER_TYPE==10201003}">
						<td>${list1.SPECIAL_BATCH_NO}</td>
					</c:if>
					<td>${list1.REQ_AMOUNT}</td>
					<td>
						<input type="text" class="mini_txt" id="reserveAmount${list1.MATERIAL_ID}" name="reserveAmount" value="${list1.RESERVE_AMOUNT}" readonly="readonly" datatype='0,is_digit,6'>
						<a href="#" onclick="patchNoSelect('${list1.MATERIAL_ID}');">[审核]</a>
					</td>
					<td><input type="text" id="singlePrice${list1.MATERIAL_ID}" name="singlePrice" size="8" style="text-align:right;display:none" value="${list1.SINGLE_PRICE}" onchange="priceTotal();" />${list1.SINGLE_PRICE}</td>
					<td><span id='price2${list1.MATERIAL_ID}'><script type="text/javascript">document.write(amountFormat(${list1.TOTAL_PRICE}));</script></span></td>
					<td><input type="text" style="display:none" class="mini_txt" id="discountRate${list1.MATERIAL_ID}" name="discountRate" value="${list1.DISCOUNT_RATE}" onchange="priceTotal();">${list1.DISCOUNT_RATE}</td>
					<td><span id="price3${list1.MATERIAL_ID}"><script type="text/javascript">document.write(amountFormat(${list1.DISCOUNT_S_PRICE}));</script></span></td>
					<td><span id="price4${list1.MATERIAL_ID}"><script type="text/javascript">document.write(amountFormat(${list1.DISCOUNT_PRICE}));</script></span></td>
					<td><span id="stock${list1.MATERIAL_ID}">0</span></td>
					<td><span id="avaPrice${list1.MATERIAL_ID}">${list1.AVA_STOCK}</span></td>
					<td>${list1.GENERAL_AMOUNT}</td>
					<td><span id="batch${list1.MATERIAL_ID}">${list1.BATCH_NO}</span> </td>
				</tr>
			</c:forEach> 
		</tbody>
		<tr align="center" class="table_list_row2">
			<td></td>
			<td align="right"><strong>合计：</strong></td>
			<c:if test="${map.ORDER_TYPE==10201003}">
				<td></td>
			</c:if>
			<td id="reqTotal"></td>
			<td id="reserveTotal"></td>
			<td></td>
			<td id="priceTotal"></td>
			<td></td>
			<td></td>
			<td id="discountTotal"></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<table class="table_query">
		<tr>
			<td align="right" width="10%">价格类型：</td>
			<td align="left" width="90%" colspan="3">${map.PRICE_DESC}</td>
		</tr>
		<tr>
			<td align="right" width="15%">使用其他价格原因：</td>
			<td align="left" width="85%" colspan="3">
				${map.OTHER_PRICE_REASON}
			</td>
		</tr>
		<tbody id="addTr">		
			<tr>
				<td align="right">收货方：</td>
				<td align="left" colspan="3">${map.DEALER_NAME3}</td>
			</tr>
			<tr>
				<td align="right">运送地点：</td>
				<td align="left">${map.ADDRESS}</td>
				<td align="right">收车单位：</td>
				<td align="left">${map.RECEIVE_ORG}</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left">${map.LINK_MAN}</td>
				<td align="right">联系电话：</td>
				<td align="left">${map.TEL}</td>
			</tr>
		</tbody>
		<c:if test="${map.IS_FLEET==1}">
			<tr>
				<td align="right">代交车：</td>
				<td align="left" colspan="3">是</td>
			</tr>
			<tr>
				<td align="right">大客户名称：</td>
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
			<td align="left" colspan="3">${map.PAY_REMARK}</td>
		</tr>
		<tr>
			<td align="right">备注说明：</td>
			<td align="left" colspan="3">${map.ORDER_REMARK}</td>
		</tr>
	</table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="hidden" name="reqId" value="${reqId}"/>
				<input type="hidden" name="dealerId" value="${map.BILLING_ORG_ID}"/>
				<input type="hidden" name="orderDealerId" value="${map.ORDER_ORG_ID}"/>
				<input type="hidden" name="reqTotalPrice" value=""/>
				<input type="hidden" name="discountTotalPrice" value=""/>
				<input type="hidden" name="modifyFlag" value="0">
				<input type="hidden" name="orderType" id="orderType" value="<c:out value="${map.ORDER_TYPE}"/>"/>
				<input type="hidden" name="areaGet" value="<c:out value="${map.AREA_ID}"/>"/>
				<input type="hidden" name="orderYear" value="<c:out value="${map.ORDER_YEAR}"/>"/>
				<input type="hidden" name="orderWeek" value="<c:out value="${map.ORDER_WEEK}"/>"/>
				<input type="hidden" name="reserveTotalAmount" value="0"> 
	      	    <input type="hidden" name="accountId" value="">
	      	    <input type="hidden" name="discountAccountId" value="">
	      	    <input type="hidden" name="dealerCode" value="${dealerCode}">
	      	    <input type="hidden" name="areaId" value="${areaId}">
	      	    <input type="hidden" name="groupCode" value="${groupCode}">
	      	    <input type="hidden" name="orderType" value="${orderType}">
	      	    <input type="hidden" name="orderNo" value="${orderNo}">
	      	    <input type="hidden" name="reqStatus" value="${reqStatus}">
	      	    <input type="hidden" name="orgCode" value="${orgCode}">
	      	    <input type="hidden" name="oldFundTypeName" value="${map.TYPE_NAME}">
	      	    <input type="hidden" name="oldDeliveryTypeName" value="${map.DELIVERY_TYPE_NAME}">
	      	    <input type="hidden" name="oldPriceDesc" value="${map.PRICE_DESC}">
				<input type="button" id="queryBtn1" name="button1" class="cssbutton" onclick="toSaveCheck();" value="确认"/>
				<input type="button" name="button3" class="cssbutton" onclick="history.back();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//初始化
	function doInit(){
		getFund();
		priceTotal();
		addressHide(document.getElementById("deliveryType").value);
		getStock();
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
		
		document.getElementById("discount").innerHTML = discount;
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
		getAvailableAmount(document.getElementById("fundType").value);
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:dealerId});
	}
	
	function showFund(json){
		//showFundTypeList(json);
		showDiscountInfo(json);
	}
	
	
	// 合计价格
	function priceTotal(){
		var reqTotal = 0;
		var reserveTotal = 0;
		var priceTotal = 0;
		var discountTotal = 0;
		
		var materialId = document.getElementsByName("materialId");
		for(var i=0; i<materialId.length; i++){
			var subStr = materialId[i].value;
			var req = parseInt(document.getElementById("reqAmount"+subStr).value, 10);
			var reserve = parseInt(document.getElementById("reserveAmount"+subStr).value, 10);//保留数量
			var singlePrice = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);//单价
			var discountRate = parseFloat(document.getElementById("discountRate"+subStr).value, 10);//折扣率
			
			var totalPrice = reserve*singlePrice;
			var discountSPrice = singlePrice*(1-discountRate/100);
			var discountPrice = totalPrice*discountRate/100;
			
			document.getElementById("totalPrice"+subStr).value = totalPrice;//金额
			document.getElementById("price2"+subStr).innerHTML = totalPrice == 0 ? "0" : amountFormat(totalPrice);//金额显示
			document.getElementById("discountSPrice"+subStr).value = discountSPrice;//折扣后单价
			document.getElementById("price3"+subStr).innerHTML = discountSPrice == 0 ? "0" : amountFormat(discountSPrice);//折扣后单价显示
			document.getElementById("discountPrice"+subStr).value = discountPrice;//折扣额
			document.getElementById("price4"+subStr).innerHTML = discountPrice == 0 ? "0" : amountFormat(discountPrice);//折扣额显示
			
			reqTotal += req;
			reserveTotal += reserve;
			priceTotal += totalPrice;
			discountTotal += discountPrice;
		}
		document.getElementById("reqTotal").innerHTML = reqTotal;
		document.getElementById("reserveTotal").innerHTML = reserveTotal;
		document.getElementById("reserveTotalAmount").value = reserveTotal;
		document.getElementById("priceTotal").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		document.getElementById("discountTotal").innerHTML = discountTotal == 0 ? "0" : amountFormat(discountTotal);
		document.getElementById("reqTotalPrice").value = parseFloat(priceTotal, 10) - parseFloat(discountTotal, 10);
		document.getElementById("discountTotalPrice").value = discountTotal;
	}
	
	function getAvailableAmount(arg){
		var dealerId = document.getElementById("dealerId").value;
		var fundTypeId = arg;
		var reqId = '${reqId}';
		var orderType = '${map.ORDER_TYPE}';
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId,reqId:reqId,orderType:orderType});
	}
	
	function showAvailableAmount(json){
		var obj1 = document.getElementById("show1");
		var obj2 = document.getElementById("availableAmount");
		obj1.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
		obj2.value = parseFloat(json.returnValue, 10);
		document.getElementById("accountId").value = json.accountId;
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
		var reqId = document.getElementById("reqId").value;
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/DlvryResourceAdjust/patchNoSelect.do?wareHouseId='+wareHouseId+'&materalId='+materalId+'&batchNo='+batchNo+'&amount='+reserveAmount+'&orderType='+orderType+'&specialBatchNo='+specialBatchNo+'&initNo='+initNo+'&reqAmount='+reqAmount+'&reqId='+reqId,700,500);
	}
	
	//保存校验
	function toSaveCheck(){
		if(submitForm('fm')){
			MyConfirm("是否确认保存？",putForword);
		}
	}
	
	//审核保存提交
	function putForword(){
		disableBtn($("queryBtn1"));
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/DlvryResourceAdjust/dlvryResourceAdjustSave.json",showForwordValue,'fm');
	}
	
	function showForwordValue(json){
		if(json.returnValue == '1'){
			$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/DlvryResourceAdjust/dlvryResourceAdjustQueryPre.do';
	 		$('fm').submit();
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
	
	//获得可用库存
	function getStock(arg){
		var ids = "";//已选中的物料id
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
	
</script>
</body>
</html>