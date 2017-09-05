<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购订单订单预审</title>
<script type="text/javascript">
function doInit(){
	getDealerLevel(document.getElementById("areaId").value);
	getAddressInfo(${order.deliveryAddress});
	disableArea();
	var isFleet = '${is_fleet}';
	addressHide(isFleet);
	changeCount_Price();
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
	var deliveryType = document.getElementById("deliveryType").value;
	var zt = <%=Constant.TRANSPORT_TYPE_01%>;
	if(deliveryType==zt){
		document.getElementById("trId_1").style.display = "none";
		document.getElementById("trId_2").style.display = "none";
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
	if(json.returnValue != "1") {
		document.getElementById("span7").style.display = "none";
		document.getElementById("span8").style.display = "none";
		document.getElementById("span18").style.display = "none";
		
	}
	else{
		document.getElementById("span7").style.display = "inline";
		document.getElementById("span8").style.display = "inline";
		document.getElementById("span18").style.display = "inline";
		
		getPriceList();//获得价格类型列表
	}
}

//获得价格类型列表
function getPriceList(){	
	var dealerId = ${map.ORDER_ORG_ID } ;
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
	var singlePrices = document.getElementsByName("singlePrice");//单价
	var totalAmount = 0;
	var caigouMoneyAll = 0;
	for(var i =0;i<buyNos.length;i++){
		var singlePrice = parseFloat(singlePrices[i].value) ;
		var acountPrices = buyNos[i].value*parseFloat(singlePrice);				//合计
		totalAmount += Number(buyNos[i].value);   //数量合计
		caigouMoneyAll += parseFloat(acountPrices);
	}
	document.getElementById("totalAmount_").innerHTML = totalAmount;
	document.getElementById("totalAmount").value = totalAmount;

	document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(caigouMoneyAll);
	document.getElementById("caigouMoneyAll").value = round(caigouMoneyAll,2);
}

//价格类型改变处理
function priceChange(json){
	var MATERIAL_PRICE_MAX = '<%=Constant.MATERIAL_PRICE_MAX%>';
	for(var i=0;i<json.priceList.length;i++){
		var id = json.priceList[i].MATERIAL_ID;
		var price = json.priceList[i].PRICE;
		
		var buyNO = document.getElementById("buyNO"+id).value;  								//采购数量
		var obj1 = document.getElementById("price" + id);										//单价
		if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
			obj1.innerHTML = "价格未维护";
			price = 0;
		}
		else{
			obj1.innerHTML = amountFormat(price);
		}
		
		var singlePrice = document.getElementById("singlePrice"+ id).value = price;

		//采购金额
		document.getElementById("acountPrices"+id).innerHTML = amountFormat(buyNO*price);
		document.getElementById("acountPrices_"+id).value = round(buyNO*price,2);
	}
	var caigouMoneyAlls = document.getElementsByName("acountPrices_");
	var buyMoney = 0;
	for(var j=0;j<caigouMoneyAlls.length;j++){
		buyMoney = Number(buyMoney) + Number(caigouMoneyAlls[j].value);
	}
	document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(buyMoney);		
	document.getElementById("caigouMoneyAll").value = round(buyMoney,2);	
	isShowOtherPriceReason();
}

// 判断是否显示使用其他价格原因
function isShowOtherPriceReason(){	
	var dealerId = ${map.ORDER_ORG_ID};
	var priceId = document.getElementById("priceId").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/isShowOtherPriceReason.json";
	makeCall(url,showOtherPriceReason,{dealerId:dealerId,priceId:priceId});
}	

//使用其他价格原因
function showOtherPriceReason(json){
	if(json.returnValue == "1"){
		document.getElementById("span7").style.display = "none";
		document.getElementById("span8").style.display = "none";
	}
	else{
		document.getElementById("span7").style.display = "none";
		document.getElementById("span8").style.display = "none";
	}
}
function confirmAdd(arg){
	if(submitForm('fm')){
		/*var buyNo = document.getElementsByName("buyNo");//审核数量
		var originalBuyNo = document.getElementsByName("originalBuyNo");//原始数量
		var price = document.getElementsByName("singlePrice");//单价
		for(var i=0;i<buyNo.length;i++){
			if(buyNo[i].value -originalBuyNo[i].value>0){
				MyAlert("审核数量不能大于原始提报数量!");
				return;
			}
			if((price[i].value -0) == 0){
				MyAlert("价格未维护,请重新选择价格类型!");
				return;
			}
		}*/
		MyConfirm("是否确认提交?",orderAdd,[arg]);
	}
}

function orderAdd(arg){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/balancecentermanage/stockordermanage/StockOrderConfirm/stockOrderConfirm.json?checkType='+arg,showResult,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/balancecentermanage/stockordermanage/StockOrderConfirm/stockOrderConfirmInit.do';
	}else{
		MyAlert("提交失败！账户余额不足！");
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 下级经销商订单管理  > 采购订单确认</div>
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
    </tr>
    <tbody id="priceTr">
	    <tr class = "tabletitle" id="span18">
	      <td align = "right" >价格类型：</td>
	      <td align = "left" colspan="7">
	      	<select name="priceId" onchange="priceTypeChange();">
	        </select>
	      </td>
	    </tr>
	    <tr id="otherTr" class = "tabletitle">
	      <td align = "right"><span id="span7">使用其他价格原因：</span></td>
	      <td align = "left" colspan="7">
	      	<span id="span8">
	      		<input name="otherPriceReason" type="text" class="long_txt" value="${order.otherPriceReason}" disabled="disabled"/>
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
      <th nowrap="nowrap">单价</th>
      <th nowrap="nowrap">合计</th>
      <th nowrap="nowrap">库存数量</th>
    </tr>
    <tbody id="tbody1">
    	<c:forEach items="${detailList}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">
		      <input type='hidden' id='materialId${po.MATERIAL_ID}' name='materialId' value='${po.MATERIAL_ID}'>
		      	${po.SERIES_NAME}
		      </td>
		      <td align="center">${po.MATERIAL_CODE}</td>
		      <td align="center">${po.MATERIAL_NAME}</td>
		       <td align="center">${po.ORDER_AMOUNT}</td>
		      <td align="center">
		      	<input type="text" id="buyNo${po.MATERIAL_ID}" name="buyNo" value="${po.ORDER_AMOUNT}" datatype='1,is_digit,4' size="4" maxlength="4" onchange="changeCount_Price();" disabled="disabled">
		      	<input type="hidden" id="originalBuyNo${po.MATERIAL_ID}" name="originalBuyNo" value="${po.ORDER_AMOUNT}" />	
		      </td>
		      <td align="center">
		      	<span id="price${po.MATERIAL_ID}"></span>
		      	<input type="hidden" id="singlePrice${po.MATERIAL_ID}" name="singlePrice" value="" />
		      </td>
			  <td>
				<span id="acountPrices${po.MATERIAL_ID}"></span>
				<input type="hidden" id="acountPrices_${po.MATERIAL_ID}" name="acountPrices_" value="" />
			 </td>
		      <td align="center">${po.RESOURCE_AMOUNT}</td>
		    </tr>
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
      <td align="center" nowrap="nowrap" >&nbsp;</td>
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
      <td width="10%" align="right" valign="top" nowrap="nowrap"></td>
      <td width="90%" colspan="3" align="left" valign="top" nowrap="nowrap">
       <input id="discount" name="discount" type="hidden" class="middle_txt" size="30" maxlength="30" value="50000.00" datatype="1,is_double,20" decimal="2" disabled="disabled"/>
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
	       ${fleet_name }
		  <input id="fleetId" name="fleetId"  value="${fleet_id }" type="hidden"/>
	  </td>
      <td align="right" nowrap="nowrap"  width="10%">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap"  width="50%">
	     ${address }
	  </td>
    </tr>
    <tbody id="tranId">
    <tr class="cssTable" id="tran_id">
      <td align="right"  nowrap="nowrap">运输方式： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      <script type="text/javascript">writeItemValue(${order.deliveryType})</script>
	      <input type="hidden" id="deliveryType" value="${order.deliveryType}" />
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
    <tr class="cssTable">
      <td align="right"  nowrap="nowrap">备注说明： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      ${order.orderRemark}
      </td>
    </tr>
    <tr class="cssTable">
      <td align="right"  nowrap="nowrap">付款信息备注： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      ${order.payRemark}
      </td>
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
      <input type="hidden" name="orderNO" value="${map.ORDER_NO }" />
      <input class="cssbutton"  name="add2" type="button" onclick="confirmAdd('1');" value ='通过' />
      <input class="cssbutton"  name="add22" type="button" onclick="confirmAdd('0');" value ='驳回' />
      <input class="cssbutton"  name="add232" type="button" onclick="history.back();" value ='返回' /></td>
    </tr>
  </table>
</form>
</body>
</html>
