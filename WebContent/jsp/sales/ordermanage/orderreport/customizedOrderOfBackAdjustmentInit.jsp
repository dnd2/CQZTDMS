<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
<script type="text/javascript">
function setOrderInfo() {
	var iIsFleet = ${tvo.isFleet} ;
	
	if(iIsFleet == 0) {
		var iDeliveryType = ${tvo.deliveryType} ;
		
		if(iDeliveryType == <%=Constant.TRANSPORT_TYPE_01 %>) {
			
		} else if(iDeliveryType == <%=Constant.TRANSPORT_TYPE_02 %>) {
			getReceiverList() ;
			
			var oLinkMan = document.getElementById("linkMan") ;
			oLinkMan.value = ${tvo.linkMan} ;
			
			var oTel = document.getElementById("tel") ;
			oTel.value = ${tvo.tel} ;
		}
	} else if(iIsFleet == 1) {
		getReceiverList() ;
		
		var oIsCover = document.getElementById("isCover") ;
		oIsCover.checked = true ;
		showFleetInfo() ;
		
		var oFleetName = document.getElementById("fleetName") ;
		oFleetName.value = "${tf.fleetName}" ;
		
		var oFleetId = document.getElementById("fleetId") ;
		oFleetId.value = ${tvo.fleetId} ;
		
		var oFleetAddress = document.getElementById("fleetAddress") ;
		oFleetAddress.value = "${tvo.fleetAddress}" ;
	}
}

function getLineTotalPrice(txtObj) {
	var iFlagId = txtObj.id.replace("amount", "") ;
		
	var iCount = txtObj.value ;
	var dPrice = document.getElementById("salePrice" + iFlagId).value ; 
		
	document.getElementById("customizedPrice" + iFlagId).innerHTML = iCount * dPrice ;
	document.getElementById("cusPrice" + iFlagId).value = iCount * dPrice ;
}

function getTotalPrice() {
		var aLinesPrice = document.getElementsByName("cusPrice") ;
		var iLen = aLinesPrice.length ;
		
		var dTotalPrice = 0 ;
		
		for(var i=0; i<iLen; i++) {
			dTotalPrice += parseFloat(aLinesPrice[i].value) ;
		}
		
		document.getElementById("show2").innerHTML = amountFormat(parseFloat(dTotalPrice, 10)) ;
		document.getElementById("totPrice").innerHTML = amountFormat(parseFloat(dTotalPrice, 10)) ;
		document.getElementById("tPrice").value = dTotalPrice ;
}

function getTotalCount() {
	var aLinesCount = document.getElementsByName("amount") ;
	var iLen = aLinesCount.length ;
	
	var dTotalCount = 0 ;
	
	for(var i=0; i<iLen; i++) {
		dTotalCount += parseInt(aLinesCount[i].value) ;
	}
	
	document.getElementById("totCount").innerHTML = dTotalCount ;
	document.getElementById("tCount").value = dTotalCount ;
}

function setTotal(txtObj) {
	if(${isSecond == 'false'}) {
		getTotalCount() ;
		
		if(txtObj)
			getLineTotalPrice(txtObj) ;
		
		getTotalPrice() ;
	}
}

function chkOrderAmont() {
	var url = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedQuery/chkCustomizedOrderByOrderAmount.json' ;
	
	makeFormCall(url, newResult, 'fm') ;
}

function newResult(json) {
	var amountFlag = json.amountFlag ;
	
	if(amountFlag == -1) {
		MyAlert("订做车订单提报数量不能超过需求提报数量！") ;
	} else if(amountFlag == 1) {
		toConfirm() ;
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车订单管理 > 订做车订单调整</div>
<form method="POST" name="fm" id="fm">
	<table class=table_query>
	<tr>
	      <th colspan="6" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 销售订单号：${tvo.orderNo }
	      <input type="hidden" name="orderId" id="orderId" value="${tvo.orderId }" />
	      </th>
	      <th align="right" nowrap="nowrap">&nbsp;</th>
	      <th align="right" nowrap="nowrap">&nbsp;</th>
	    </tr>
		<tr class=cssTable>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel" disabled="disabled">
					<c:forEach items="${areaList}" var="po">
						<option <c:if test="${po.AREA_ID==reqPO.areaId}">selected</c:if> value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">选择资金类型：</td>
			<td align="left">
				<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      		</select>
			</td>
			<c:if test="${isSecond == 'false'}">
			<td align="right">可用余额：
				<span id="show1"></span><input type="hidden" name="availableAmount" value="0">
			</td>
			<td align="left" id="accountAmount"></td>
			<td align="right">订单总价：</td>
			<td align="left"><span id="show2"></span></td>
			</c:if>
		</tr>
		<c:if test="${isSecond == 'false'}">
		<tr id="priceTr" class = "tabletitle">
	      <td align = "right" >价格类型：</td>
	      <td align = "left" colspan="3">
	      	<select name="priceId" onchange="priceTypeChange();" disabled="disabled">
	      		<%-- <option >${priceType.PRICE_DESC}</option> --%>
	        </select>
	      </td>
	      <td align = "right"><span id="span7">使用其他价格原因：</span></td>
	      <td align = "left" colspan="3">
	      	<span id="span8"><input name="otherPriceReason" type="text" class="long_txt"/></span>
	      </td>
	    </tr>
	    </c:if>
	</table>
	<br>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">订做批次号</th>
			<!-- <th nowrap="nowrap">资源情况</th> -->
			<!-- <th nowrap="nowrap">已提报数量</th> -->
			<th nowrap="nowrap">可提报数量</th>
			<c:if test="${isSecond == 'false'}">
			<!-- <th nowrap="nowrap">单价</th>
			<th nowrap="nowrap">金额</th> -->
			<!-- <th nowrap="nowrap">价格浮动</th> -->
			<!-- <th nowrap="nowrap">折扣率 %</th> -->
			<!-- <th nowrap="nowrap">折扣后单价</th> -->
			<!-- <th nowrap="nowrap">折扣额</th> -->
			</c:if>
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">
		      	${po.SERIES_NAME}
		      	<input type="hidden" name="groupId" value="${po.GROUP_ID}"/>
		      	<input type="hidden" name="specialBatchNo2" value="${po.SPECIAL_BATCH_NO}"/>
		      	<input type="hidden" id="singlePrice${po.MATERIAL_ID}" name="singlePrice" value="${po.SALES_PRICE}"/>
				<input type="hidden" id="totalPrice${po.MATERIAL_ID}" name="totalPrice" value="0"/>
				<input type="hidden" id="discountSPrice${po.MATERIAL_ID}" name="discountSPrice2" value="0"/>
				<input type="hidden" id="discountPrice${po.MATERIAL_ID}" name="discountPrice2" value="0"/>
				<input type="hidden" name="dtlId" value="${po.DTL_ID}"/>
				<input type="hidden" name="applyAmount2" value="${po.APPLY_AMOUNT}"/>
				<input type="hidden" id="orderAmount${po.MATERIAL_ID}" name="orderAmount2" value="${po.ORDER_AMOUNT}"/>
				<input type="hidden" name='materialPrice' value="${po.SALES_PRICE}" />
		      </td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.SPECIAL_BATCH_NO}</td>
		      <%-- <td align="center">${po.RESOURCE_AMOUNT}</td> --%>
		      <%-- <td align="center">${po.ORDER_AMOUNT}</td> --%>
		      <td align="center">
		      	${po.AMOUNT}
		      	<%-- <input type="text" class="mini_txt" id="amount${po.MATERIAL_ID}" name="amount" value="${po.AMOUNT}" datatype='0,is_digit,6' onchange="priceTotal();"/> --%>
		      </td>
		      <c:if test="${isSecond == 'false'}">
		      <%-- <td align="center">
			      <span id='price1${po.MATERIAL_ID}'>
			      	<script type="text/javascript">
			      		document.write(amountFormat(${po.SALES_PRICE}));
			      	</script>
			      </span>
		      </td> --%>
		     <%--  <td align="center"><span id='price2${po.MATERIAL_ID}'></span></td> --%>
		     <%--  <td align="center">${po.CHANGE_PRICE}</td> --%>
		      <%-- <td align="center"><input type="text" class="mini_txt" id="discountRate${po.MATERIAL_ID}" name="discountRate" value="0" onchange="priceOnBlue(this);priceTotal();" datatype="0,is_double,6" decimal="2"></td> --%>
		      <%-- <td align="center"><span id='price3${po.MATERIAL_ID}'></span></td> --%>
		      <%-- <td align="center"><span id='price4${po.MATERIAL_ID}'></span></td> --%>
		      </c:if>
		    </tr>
    	</c:forEach>
    	<%-- <tr class="table_list_row1" >
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap" align="right"><strong>总计：</strong></td>
			<td nowrap="nowrap" align="center" id="orderTotal"></td>
			<!-- <td nowrap="nowrap" align="center" id="amountTotal"></td> -->
			<c:if test="${isSecond == 'false'}">
			<!-- <td nowrap="nowrap"></td>
			<td nowrap="nowrap" align="center" id="priceTotal"></td> -->
			<td nowrap="nowrap"></td>
			<td nowrap="nowrap"></td>
			<!-- <td nowrap="nowrap"></td>
			<td nowrap="nowrap" align="center" id="discountTotal"></td> -->
			</c:if>
		</tr> --%>
	</table>	
	<br>
	<table class="table_query">
		<tr class="cssTable" >
			<td width="100%" align="left">
				<input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
				<input class="cssbutton" name="addMaterial" type="button" onclick="materialShow();" value ='选择物料' />
				&nbsp;				
			</td>
		</tr>
	</table>
	<br/>
	<table class=table_list style="border-bottom:1px solid #DAE0EE">
		<tr class="cssTable" >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">物料编号</th>
			<th nowrap="nowrap">物料名称</th>
			<!-- <th nowrap="nowrap">价格</th> -->
			<th nowrap="nowrap">价格浮动</th>
			<c:if test="${isSecond == 'false'}">
				<th nowrap="nowrap">计算后单价</th>
				<th nowrap="nowrap">计算后总价</th>
			</c:if>
			<!-- <th nowrap="nowrap">资源情况</th> -->
			<!-- <th nowrap="nowrap">折扣率 %</th> -->
			<th nowrap="nowrap">数量</th>
			<th nowrap="nowrap">操作</th>
		</tr>
		<tbody id="materialBody">
		
		<c:forEach var="list" items="${matList }">
		<tr class="table_list_row2">
			<td>${list.SERIES_CODE}</td>
			<td>${list.GROUP_CODE}</td>
			<td>${list.MATERIAL_CODE}</td>
			<td>${list.MATERIAL_NAME}</td>
			<td>${list.CHANGE_PRICE}</td>
			<c:if test="${isSecond == 'false'}">
				<td>${list.SALES_PRICE }</td>
				<td>
					<span id="customizedPrice${list.MATERIAL_ID }">${list.SALES_PRICE * list.ORDER_AMOUNT}</span>
					<input type="hidden" name="cusPrice" id="cusPrice${list.MATERIAL_ID }" value="${list.SALES_PRICE * list.ORDER_AMOUNT}" />
				</td>
			</c:if>
			<td><input name='amount' id="amount${list.MATERIAL_ID }" type='text' class="mini_txt" datatype='1,is_digit,6' class='SearchInput' value='${list.ORDER_AMOUNT }' onChange="setTotal(this) ;" size='2' maxlength='6'/></td>
			<td><a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' name='materialId' value='${list.MATERIAL_ID }'><input type='hidden' id='salePrice${list.MATERIAL_ID }' name='salePrice' value='${list.SALES_PRICE }'><input type='hidden' name='specialBatchNo' value='${list.SPECIAL_BATCH_NO }'/><input type='hidden'  name='orderAmount' value='${list.ORDER_AMOUNT }'/><input type='hidden' name='applyAmount' value='${list.APPLY_AMOUNT }'/></td>
		</tr>
		</c:forEach>
		<c:if test="${isSecond == 'false'}">
			<tr class="table_list_row2">
				<td colspan="6" align="right"><strong>总计：</strong></td>
				<td>
					<strong>
						<span id="totPrice"></span>
						<input type="hidden" name="tPrice" id="tPrice" value="" />
					</strong>
				</td>
				<td>
					<strong>
						<span id="totCount"></span>
						<input type="hidden" name="tCount" id="tCount" value="" />
					</strong>
				</td>
				<td></td>
			</tr>
		</c:if>
		</tbody>
	</table>
	<br/>
	<table class=table_query>
		<tr>
	      <th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
	      <th align="right" nowrap="nowrap">&nbsp;</th>
	      <th align="right" nowrap="nowrap">&nbsp;</th>
	    </tr>
	    <c:if test="${isSecond == 'false'}">
		    <tr class="cssTable" id="discountTr">
		      <td width="12%" align="right" valign="top" nowrap="nowrap">可用折让：</td>
		      <td colspan="3" align="left" valign="top" nowrap="nowrap">
		      	<input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" value="0.00" datatype="1,is_double,20" decimal="2"/>
		      </td>
		    </tr>
		</c:if>
	    <tr class="cssTable">
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
		<tr class="cssTable" id="tranTr">
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
			      </select>
			  </td>
		      <td align="right" valign="top" nowrap="nowrap">收车单位：</td>
		      <td align="left" valign="top" nowrap="nowrap" id="receiveOrg"></td>
		    </tr>
		    <tr class="cssTable" >
		      <td align="right" valign="top" nowrap="nowrap">联系人：</td>
		      <td align="left" valign="top" nowrap="nowrap"><input name="linkMan" id="linkMan" type="text" class="middle_txt" size="30" maxlength="30" /></td>
		      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
		      <td align="left" valign="top" nowrap="nowrap"><input name="tel" id="tel" type="text" class="middle_txt" size="30" maxlength="30" /></td>
		    </tr>
	    </tbody>
	    <tr class=cssTable >
	      <TD height="66" align="right" nowrap>备注说明：</TD>
		  <TD colspan="3" align="left" nowrap><textarea name="orderRemark" rows="3" cols="80">${tvo.orderRemark }</textarea></TD>
   		</tr>
		<tr class=cssTable>
			<td align="right">改装说明：</td>
			<td colspan="3" align="left"  nowrap><textarea name="refitRemark" id="refitRemark" rows="3" cols="80" readonly="readonly"><c:out value="${reqPO.refitDesc}"/></textarea></td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="reqId" value="${reqPO.reqId}"/>
				<input type="hidden" name="area" value="${reqPO.areaId}"/>
				<input type="hidden" name="dealerId" value="${reqPO.dealerId}"/>
				<input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
				<input type="hidden" name="orderTotalPrice" value="0">
				<input type="hidden" name="discountTotalPrice" value="0">
				<input type="hidden" name="reqTotalAmount" value="0">
				<input type="hidden" name="ratePara" value="${ratePara}" />
				<input type="hidden" name="priceTypeId" value="${reqPO.priceId }" />
				<input type="hidden" name="accountId" id="accountId" value="" />
				<input type="button" name="button1" class="cssbutton" onclick="chkOrderAmont();" value="提报" id="queryBtn1" /> 
				<input type="button" name="button2" class="cssbutton" onclick="history.back();" value="返回" id="queryBtn2" /> 
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
		/* priceTotal(); */
		if(${isSecond == 'false'}) {
			getPriceList();
		}
		setTotal() ;
		setOrderInfo() ;
	}
	
	function getFund(){
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:dealerId});
	}
	
	function showFund(json){
		showFundTypeList(json);
		showDiscountInfo(json);
	}
	
	//资金类型列表显示
	function showFundTypeList(json){
		var obj = document.getElementById("fundType");
		obj.options.length = 0;
		for(var i=0;i<json.fundTypeList.length;i++){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
			if(json.fundTypeList[i].TYPE_ID + "" == '${tvo.fundTypeId}'){
				obj.options[i].selected = true;
			}
		}
		
		getAvailableAmount(document.getElementById("fundType").value);
	}
	
	function getAvailableAmount(arg){
		var dealerId = document.getElementById("dealerId").value;
		var fundTypeId = arg;
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
	}
	
	function showAvailableAmount(json){
		var obj1 = document.getElementById("show1");
		var obj2 = document.getElementById("availableAmount");
		obj1.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
		obj2.value = parseFloat(json.returnValue, 10);
		document.getElementById("accountId").value = json.accountId;
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
	
	//提交校验
	function toConfirm(){
		if(submitForm('fm')){
			var flag = document.getElementById("isCheck").value;
			var materialId = document.getElementsByName("materialId");
			var amount = document.getElementsByName("amount");
			if(${isSecond == 'false'}) {
				var total = document.getElementById("orderTotalPrice").value;
				var ava = document.getElementById("availableAmount").value;
				var discountValue = document.getElementById("discount").value;
				var discountTotal = document.getElementById("discountTotalPrice").value;
				var discountRate = document.getElementsByName("discountRate");
			}
			var reqTotalAmount = document.getElementById("reqTotalAmount").value;
			
			if(${isSecond == 'false'}) {
				var ratePara = parseInt(document.getElementById("ratePara").value, 10);
				
				var priceId = document.getElementById("priceId").value;
				document.getElementById("priceTypeId").value = priceId;
				
				for(var i=0 ;i< materialId.length; i++){
					var materialValue = materialId[i].value;
					/* var discountRateValue = discountRate[i].value */;
					var amountValue = amount[i].value;
					var materialPrice = document.getElementsByName('materialPrice');
					
					/* if(parseFloat(discountRateValue, 10) > ratePara) {
						MyAlert("折扣率不能大于"+ratePara+"%！");
						return;
					} */
					for(var j=0; j<materialPrice.length; j++) {
						if(materialPrice[j].value == "价格未维护" && parseInt(amountValue, 10) > 0){
							MyAlert("价格未维护物料提报数量不能大于0！");
							return;
						}
					}
				}
				/* if(parseFloat(discountValue, 10) < parseFloat(discountTotal, 10)) {
					MyAlert("折扣额合计不能大于可用折让！");
					return;
				} */
				if(flag==0 && parseFloat(total, 10)>parseFloat(ava, 10)){
					MyAlert("订单总价大于可用余额，不能提报！");
					return false;
				}
				
			}
			
			var deliveryAddress = document.getElementsByName('deliveryAddress')[0].value;
			if(deliveryAddress == "" || deliveryAddress == undefined) {
				MyAlert("运送地址不能为空！");
				return;
			}
			var amountTotal = 0;
			for(var i=0; i<amount.length; i++) {
				if(parseInt(amount[i].value, 10) < 1) {
					MyAlert("提报数量不能小于1！");
					return;
				}
				amountTotal += parseInt(amount[i].value);
			}
			
			 
			if(materialId.length == 0) {
				MyAlert("物料数量不能小于1！");
				return;
			}
			
			//计算订单总价
			var orderTotalPrice = 0;
			var salePrice = document.getElementsByName("salePrice");
			var amount = document.getElementsByName('amount');
			for(var i=0; i<salePrice.length; i++) {
				orderTotalPrice += salePrice[i].value * amount[i].value;
			}
			document.getElementById('orderTotalPrice').value = orderTotalPrice;
			//发运总数量
			
			document.getElementById("reqTotalAmount").value = amountTotal;
			MyConfirm("确认提报？",toSubmit);
		}
	}
	//提交
	function toSubmit(){
		//document.getElementById("remark").disabled=false;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedQuery/specialNeedToAdjustment.json',showResult,'fm');
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("调整成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedQuery/customizedOrderOfBackInit.do';
			$('fm').submit();
		}else{
			MyAlert("调整失败！请联系系统管理员！");
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
  	//获得价格类型列表
	function getPriceList(){	
		var dealerId = document.getElementById("dealerId").value;
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
				if('${reqPO.priceId}' == '' || '${reqPO.priceId}' == '0'){
					obj.options[i].selected = "selected";
				}
			}
			else{
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
			}
			if(json.priceList[i].PRICE_ID + "" == '${reqPO.priceId}'){
				obj.options[i].selected = "selected";
			}
		}
		isShowOtherPriceReason();
	}
	//获得收货方列表
	function getReceiverList(){	
		var dealerId = document.getElementById("dealerId").value;
		var areaId = document.getElementById("area").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
		makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
	}	
	
	//显示收货方列表
	function showReceiverList(json){
		var obj = document.getElementById("receiver");
		obj.options.length = 0;
		
		var lReceiver = ${tvo.receiver} ;
		
		for(var i=0;i<json.receiverList.length;i++){
			obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
			
			if(json.receiverList[i].DEALER_ID == lReceiver) {
				obj.options[i].selected = "selected";
			}
		}
		
		getAddressList();//获得发运地址列表
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
		var materialId = document.getElementsByName("materialId");
		for(var i=0; i<materialId.length; i++){
			ids += materialId[i].value + ",";
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
			
			var obj1 = document.getElementById("price1" + id);
			var obj2 = document.getElementById("singlePrice" + id);
			if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
				obj1.innerHTML = "价格未维护";
				price = 0;
			}
			else{
				obj1.innerHTML = amountFormat(price);
			}
			obj2.value = price;
		}
		isShowOtherPriceReason();
		priceTotal();
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
	
	//获得地址列表
	function getAddressList(){	
		var dealerId = document.getElementById("receiver").value;
		var areaId = document.getElementById("area").value.split("|")[0] ;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
		makeCall(url,showAddressList,{dealerId:dealerId, areaId:areaId});
	}	
	
	function showAddressList(json){
		var obj = document.getElementById("deliveryAddress");
		obj.options.length = 0;
		
		var lAddress = ${tvo.deliveryAddress} ;
		
		for(var i=0;i<json.addressList.length;i++){
			obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
			
			if(json.addressList[i].ID == lAddress) {
				obj.options[i].selected = "selected";
			}
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
		if (!json.info.LINK_MAN) {
			obj1.value = '' ;
		}
		if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
			obj1.value = json.info.LINK_MAN;
		}
		if (!json.info.TEL) {
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
	
	//是否代交车
	function showFleetInfo(){
		if(document.getElementById("isCover").checked){
			document.getElementById("cusTr").style.display = "inline";
			document.getElementById("addTr").style.display = "none";
			document.getElementById("tranTr").style.display = "none";
			document.getElementById("isCover").value = 1;
		}else{
			document.getElementById("cusTr").style.display = "none";
			document.getElementById("addTr").style.display = "inline";
			document.getElementById("tranTr").style.display = "inline";
			addressHide(document.getElementById("deliveryType").value);
			document.getElementById("isCover").value = 0;
		}
	}
	
	// 合计价格
	function priceTotal(){
		var orderTotal = 0;
		var amountTotal = 0;
		
		if(${isSecond == 'false'}) {
			var priceTotal = 0;
			var discountTotal = 0;
		}
		
		var materialId = document.getElementsByName("materialId");
		for(var i=0; i<materialId.length; i++){
			var subStr = materialId[i].value;
			var orderAmount = parseInt(document.getElementById("orderAmount"+subStr).value, 10);//已提报数量
			var amount = parseInt(document.getElementById("amount"+subStr).value, 10);//数量
			
			if(${isSecond == 'false'}) {
				var singlePrice = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);//单价
				var discountRate = parseFloat(document.getElementById("discountRate"+subStr).value, 10);//折扣率
			
			
				var totalPrice = amount*singlePrice;
				var discountSPrice = singlePrice*(1-discountRate/100);
				var discountPrice = totalPrice*discountRate/100;
				
				document.getElementById("totalPrice"+subStr).value = totalPrice;//金额
				document.getElementById("price2"+subStr).innerHTML = totalPrice == 0 ? "0" : amountFormat(totalPrice);//金额显示
				document.getElementById("discountSPrice"+subStr).value = discountSPrice;//折扣后单价
				document.getElementById("price3"+subStr).innerHTML = discountSPrice == 0 ? "0" : amountFormat(discountSPrice);//折扣后单价显示
				document.getElementById("discountPrice"+subStr).value = discountPrice;//折扣额
				document.getElementById("price4"+subStr).innerHTML = discountPrice == 0 ? "0" : amountFormat(discountPrice);//折扣额显示
			}
			orderTotal += orderAmount;
			amountTotal += amount;
			if(${isSecond == 'false'}) {
				priceTotal += totalPrice;
				discountTotal += discountPrice;
			}
		}
		
		document.getElementById("orderTotal").innerHTML = orderTotal;
		document.getElementById("amountTotal").innerHTML = amountTotal;
		document.getElementById("reqTotalAmount").value = amountTotal;
		if(${isSecond == 'false'}) {
			document.getElementById("priceTotal").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
			document.getElementById("discountTotal").innerHTML = discountTotal == 0 ? "0" : amountFormat(discountTotal);
			document.getElementById("orderTotalPrice").value = parseFloat(priceTotal, 10) - parseFloat(discountTotal, 10);
			document.getElementById("show2").innerHTML = amountFormat(parseFloat(priceTotal, 10) - parseFloat(discountTotal, 10));
			document.getElementById("discountTotalPrice").value = discountTotal;
		}
	}
	
	//TODO 物料选择弹出窗口 2012-05-15
	//物料弹出选择
	function materialShow(){
		var ids= "";
		gids = document.getElementsByName("groupId");
		for (var i=0; i<gids.length; i++){  
			var value = gids[i].value;
			if(i != gids.length-1){
					ids += value + ",";
			}else {
					ids += value;
			}   
		} 	
		var areaObj = document.getElementById("area");
		var areaId = areaObj.value;	
		
		var productId = "" ;
		
		if(document.getElementById("_productId_")) {
			productId = document.getElementById("_productId_").value ;
		}  
		var areaObj = document.getElementById("area");
		var areaId = areaObj.value;	
		/* showMaterialGroupByConf('materialCode','','true', '4' , areaId); */
		showMaterialByAreaIdAndMaterialGroupId('materialCode', '', 'true', areaId, ids);
	}
	
	//新增物料链接
	function addMaterial(){		
		var materialCode = document.getElementById("materialCode").value;
		/* var materialIds = document.getElementsByName('materialId');
		var addedMaterialId = "";
		for(var i=0; i<materialIds.length; i++) {
			if(i != materialIds.length-1) {
				addedMaterialId += materialIds[i].value + ",";
			} else {
				addedMaterialId += materialIds[i].value;
			}
		} */
		var reqId = document.getElementById("reqId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedOrderReoprt/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode, reqId:reqId/* , addedMaterialId:addedMaterialId */}); 
	}
	//删除物料链接
	function delMaterial(){	
	  	document.getElementById("materialBody").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
	  	setTotal() ;
	  	disableArea();
	  	_setSelDisabled_("materialBody", 0) ;
	}
	//新增物料列表
	function addRow(json){
		for(var i=0; i<json.info.length; i++) {
			var timeValue = new Date().getTime();
			//判断配置是否重复
			var isDuplicate = false;
			var materialIds = document.getElementsByName('materialId');
			for(var k=0; k<materialIds.length; k++) {
				if(materialIds[k].value == json.info[i].MATERIAL_ID) {
					isDuplicate = true;
				}
			}
			if(isDuplicate) {
				MyAlert(json.info[i].MATERIAL_NAME+"物料已存在!");
			} else {
				var newRow = document.getElementById("materialBody").insertRow(i);
				newRow.className  = "table_list_row2";
				var newCell = newRow.insertCell(0);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].SERIES_CODE;
				newCell = newRow.insertCell(1);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].GROUP_CODE;
				newCell = newRow.insertCell(2);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].MATERIAL_CODE;
				newCell = newRow.insertCell(3);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].MATERIAL_NAME;
				newCell = newRow.insertCell(4);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].CHANGE_PRICE == null ? "" : json.info[i].CHANGE_PRICE;
				
				var iNext = 5 ;
				
				if(${isSecond == 'false'}) {
					newCell = newRow.insertCell(iNext++);
					newCell.align = "center";
					newCell.innerHTML = json.info[i].SALES_PRICE ;
					
					newCell = newRow.insertCell(iNext++);
					newCell.align = "center";
					newCell.innerHTML = "<span id=\"customizedPrice" + json.info[i].MATERIAL_ID + "\">" + 0 + "</span><input type=\"hidden\" name=\"cusPrice\" id=\"cusPrice" + json.info[i].MATERIAL_ID + "\" value=\"0\" />";
				}
				
				newCell = newRow.insertCell(iNext++);
				newCell.align = "center";
				newCell.innerHTML = "<input id='amount" + json.info[i].MATERIAL_ID + "' name='amount' type='text' class=\"mini_txt\" datatype='1,is_digit,6' class='SearchInput' onChange=\"setTotal(this) ;\" value='0' size='2' maxlength='6'/>";
				newCell = newRow.insertCell(iNext);
				newCell.align = "center";
				newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId' value='"+json.info[i].MATERIAL_ID+"'><input type='hidden' id='salePrice" + json.info[i].MATERIAL_ID + "' name='salePrice' value='"+json.info[i].SALES_PRICE+"'><input type='hidden' name='specialBatchNo' value='"+json.info[i].SPECIAL_BATCH_NO+"'/><input type='hidden'  name='discountPrice' value='0'/><input type='hidden'  name='discountSPrice' value='0'/><input type='hidden'  name='orderAmount' value='"+json.info[i].ORDER_AMOUNT+"'/><input type='hidden' name='applyAmount' value='"+json.info[i].APPLY_AMOUNT+"'/>";
			}
		}
		disableArea();
		_setSelDisabled_("materialBody", 0) ;
	}
	
</script>
</body>
</html>
