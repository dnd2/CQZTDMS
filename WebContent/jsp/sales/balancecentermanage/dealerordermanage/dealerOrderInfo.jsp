<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商订单审核</title>
<script type="text/javascript">
<!--
	//初始化
	function doInit(){
		var k_dealerId = document.getElementById("k_dealerId").value;
		getDiscountInfo(); //获得使用折让、折让账户
		sumAll();
		getDealerLevel(k_dealerId);
		var isFleet = document.getElementById("isFleet").value;
		addressHide(isFleet);
		var checkAmount = document.getElementsByName("checkAmount");//已满足数量
		var aa=document.getElementsByName("buyNO");
		var type_flag = false;
		for(var i=0;i<checkAmount.length;i++){
			if(checkAmount[i].value >0){
				type_flag = true;
			}
		}
		for(var i=0;i<checkAmount.length;i++){
			if(type_flag){
				aa[i].readOnly = true;
			}else{
				aa[i].readOnly = false;
			}
		}
		
		setDisabled() ;
	}
	
	function setDisabled() {
		var sWareFlag = "${wareFlag}" ;
		var oBuyNo = document.getElementsByName("buyNO");
		
		if(sWareFlag == 1) {
			for(var i=0; i<oBuyNo.length; i++){
				oBuyNo[i].readOnly = true;
			}
		} else if(sWareFlag == 0) {
			for(var i=0; i<oBuyNo.length; i++){
				document.getElementById(i).disabled = true;
			}
		}
	}

	function toClear(obj) {
		document.getElementById(obj).value = "" ;
		}

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
//-->
</script>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;结算中心管理 &gt; 经销商订单管理&gt; 经销商订单审核</div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="wareFlag" name="wareFlag" value="${wareFlag }" />
<table class="table_list">
	<tr align="center" class="tabletitle">
		<th>订货方</th>
		<th>开票方</th>
		<th>订单号</th>
		<th>订单周度</th>
		<th>订单类型</th>
		<th>提报数量</th>
		<th>已审核数量</th>
		<th>待审核数量</th>
	</tr>
	<tr align="center" class="table_list_row2">
		<td>${orderInfo.D_NAME }<input type="hidden" name="receiver" value="${orderInfo.D_DEALER_ID }" /></td>
		<td>${orderInfo.K_NAME }</td>
		<td>${orderInfo.ORDER_NO }</td>
		<td>${orderInfo.ORDER_WEEK }</td>
		<td><script>document.write(getItemValue(${orderInfo.ORDER_TYPE }));</script></td>
		<td>${orderInfo.ORDER_AMOUNT }<input type="hidden" id="reportNO" name="reportNO" value="${orderInfo.ORDER_AMOUNT }" /></td>
		<td>${orderInfo.CHECK_AMOUNT }</td>
		<td>${orderInfo.NO_CHECK_AMOUNT }
			<input type="hidden" id="order_id" name="order_id" value="${orderInfo.ORDER_ID }">
			<input type="hidden" id="isFleet" name="isFleet" value="${orderInfo.IS_FLEET }" />	
			<input type="hidden" id="orderNO" name="orderNO" value="${orderInfo.ORDER_NO }" />	
		</td>
	</tr>
</table>
<br>
<table class="table_list" id="activeTable">
	<tr align="center" class="tabletitle">
		<th>车系</th>
		<th>物料编号</th>
		<th>物料名称</th>
		<th>提报数量</th>
		<th>已满足数量</th>
		<th>待审核数量</th>
		<th>库存数量</th>  
		<!--  <th>已采购数量</th>-->
		<th>采购数量</th>  
		<th>单价</th>
		<th>折扣率%</th>
      	<th>折扣后单价</th>
      	<th>折扣额</th>  
		<th>合计</th>  
		<th>操作</th>
	</tr>
	<c:forEach items="${orderDetailList}" var="orderDetailList" varStatus="vstatus">
		<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
			${orderDetailList.SERIES_NAME}
			<input type='hidden' id='materialId${orderDetailList.MATERIAL_ID}' name='materialId${orderDetailList.MATERIAL_ID}' value='${orderDetailList.MATERIAL_ID}'>
			<input id='singlePrice${orderDetailList.MATERIAL_ID}' type='hidden' name='singlePrice' value=''>
			<input type="hidden" id="caigouMoneyAll${orderDetailList.MATERIAL_ID}" name="caigouMoneyAll" value="" />
			<input type="hidden" id="availableAmount_" name="availableAmount_" value="" />
			<input type="hidden"  name="detail_id" value="${orderDetailList.DETAIL_ID }" />
			<input type="hidden"  name=materialId value="${orderDetailList.MATERIAL_ID }" />
			</td>
			<td>${orderDetailList.MATERIAL_CODE}<input type="hidden" name="material_id" value="${orderDetailList.MATERIAL_ID}" /></td>
			<td>${orderDetailList.MATERIAL_NAME}</td>
			<td>${orderDetailList.ORDER_AMOUNT}<input type="hidden" name="orderAmount" value="${orderDetailList.ORDER_AMOUNT}" /></td>
			<td>${orderDetailList.CHECK_AMOUNT}<input type="hidden" name="checkAmount" value="${orderDetailList.CHECK_AMOUNT}" /></td>
			<td>${orderDetailList.NO_CHECK_AMOUNT}<input type="hidden" name="nocheckAmount" value="${orderDetailList.NO_CHECK_AMOUNT}" /></td>
			<td>${orderDetailList.RESOURCE_AMOUNT}<input type="hidden" name="resourceAmount" value="${orderDetailList.RESOURCE_AMOUNT}" /></td>
			<!--  
			<td>${orderDetailList.APPLYED_AMOUNT}<input type="hidden" name="applyedAmount" value="${orderDetailList.APPLYED_AMOUNT}" /></td>
			-->
			<td><input type="text" name="buyNO" id="buyNO${orderDetailList.MATERIAL_ID}" value="" class="mini_txt" datatype="1,is_digit,6" onchange="changeCount_Price(this.value,singlePrice${orderDetailList.MATERIAL_ID},${orderDetailList.MATERIAL_ID});" /></td>
			<td>
				<span id="price${orderDetailList.MATERIAL_ID}"></span>
				<input type="hidden" name="applyedAmount" value="0" />	
			</td>
			<!-- 折扣率 -->
		    <td align="center">
		      <input type="text" name="discount_rate" id="discount_rate${orderDetailList.MATERIAL_ID }" value="" datatype='1,is_digit,6' size='2' maxlength='2' onchange="priceOnBlue(this);changeRate('${orderDetailList.MATERIAL_ID}');" />%
		    </td>
			<!-- 折扣后单价 -->
		    <td align="center">
		    	<span id="discount_s_price_${orderDetailList.MATERIAL_ID}"></span>
		    	<input type="hidden" id="discount_s_price${orderDetailList.MATERIAL_ID}" name="discount_s_price${orderDetailList.MATERIAL_ID}" value="" />
		    </td>
		    <!-- 折扣额 -->
		      <td align="center">
		      	<span id="discount_price_${orderDetailList.MATERIAL_ID}"></span>
		      	<input type="hidden" id="discount_price${orderDetailList.MATERIAL_ID}" name="discount_price" value="0" />
		    </td>
			<td>
				<span id="acountPrices${orderDetailList.MATERIAL_ID}"></span>
				<input type="hidden" id="acountPrices_${orderDetailList.MATERIAL_ID}" name="acountPrices_" value="" />
			</td>
			<td id="${vstatus.index}">[<a href="#" onclick="toCheckDetail('${orderDetailList.DETAIL_ID}','${orderDetailList.ORDER_AMOUNT}','${orderDetailList.MATERIAL_ID}');">库存审核</a>]</td>
		</tr>
	</c:forEach>
	<tr align="center" class="table_list_row2">
		<td></td>
		<td></td>
		<td align="right"><strong>合计：</strong></td>
		<td id="orderAmounts"></td>
		<td id="checkAmounts"></td>
		<td id="nocheckAmounts"></td>
		<td id="resourceAmounts"></td>
		<!--<td id="applyedAmounts"></td> 已采购合计 -->
		<td id="caigouNOAll"></td>
		<td></td>
		<td></td>
		<td></td>
		<td><span id="totalDiscountPrice_"></span><input type="hidden" id="totalDiscountPrice" name="totalDiscountPrice" value="" /></td>
		<td><span id="caigouMoneyAll__"></span></td>
		<td>
			<input type="hidden" id="caigouAllNumber" name="caigouAllNumber" value="" />
			<!--  
			<input type="hidden" id="applyedAmounts_" name="applyedAmounts_" value="" />
			-->
		</td>
	</tr>
</table>
<br>
<table class="table_query" align="center" border="0">
	<tr class= "tabletitle">
      <td align = "right" width="15%">是否结算中心库存车启票：</td>
      <td align = "left" width="20%" colspan="7">
        <c:if test="${wareFlag == 0}">否</c:if>
		<c:if test="${wareFlag == 1}">是</c:if>
      </td>
    </tr>
    <tr class= "tabletitle">
      <td align = "right" width="15%">业务范围：</td>
      <td align = "left" width="20%" colspan="7">
        ${orderInfo.AREA_NAME }
        <input type="hidden" name="area_id" value="${orderInfo.AREA_ID }" />
        <input type="hidden" name="area" value="">
      </td>
    </tr>
    <tr class= "tabletitle">
      <td align = "right" width="15%">使用折让：</td>
      <td align = "left" width="20%" colspan="7">
        <input id="discount" name="discount" type="text" class="middle_txt" size="30" maxlength="30" readonly="readonly"/>
        <input type="hidden" name="discountAccountId" value="">
      </td>
    </tr>
    <tr>  
      <td align = "right"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	        	<c:forEach items="${fundTypeList}" var="po">
	        	<c:if test="${order.fundTypeId==po.TYPE_ID}">
					<option selected="selected" value="${po.TYPE_ID}|${po.IS_USE_ORDER_ACCOUNT}">${po.TYPE_NAME}</option>
				</c:if>
				<c:if test="${order.fundTypeId!=po.TYPE_ID}">
					<option value="${po.TYPE_ID}|${po.IS_USE_ORDER_ACCOUNT}">${po.TYPE_NAME}</option>
				</c:if>
				</c:forEach>
				
	      	</select>
      	</span>
      </td>
    
      <td align = "right" width="20%"><span id="span3">可用余额：</span></td>
      <td align = "left" width="50%"><span id="span4" class="STYLE2"></span></td>
    </tr>
    <tr id="priceTr" class = "tabletitle">
      <td align = "right" >价格类型：</td>
      <td align = "left"  colspan="7">
      	<select name="priceId" onchange="priceTypeChange();">
        </select>
      </td>
    </tr>
    <tr>  
      <td align = "right"><span id="span7">使用其他价格原因：</span></td>
      <td align = "left" colspan="5">
      	<span id="span8">
      		<textarea name="otherPriceReason" id="otherPriceReason" cols="45" rows="3"></textarea>
      	</span>
      </td>
    </tr>
  </table>
<table class="table_query">
	 <tr class="cssTable" id="discountTr">
      <td width="12%" align="right" valign="top" nowrap="nowrap">是否代交车：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input type="checkbox" name="isCover" id="isCover" onclick="addressHide();" value="" />
      </td>
    </tr>
    <tr class="cssTable" id="cusTr" style="display:none">
      <td align="right" nowrap="nowrap">选择集团客户：</td>
      <td align="left" nowrap="nowrap">
	      <input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30" value="${fleet_name }" readonly="readonly" />
		  <input id="fleetId" name="fleetId"  value="${fleet_id }" type="hidden"/>				
		  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
		  <input class="cssbutton" type="button" value="清除" onclick="toClear('fleetName');toClear('fleetId');"/>
	  </td>
      <td align="right" nowrap="nowrap">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap">
	      <input type="text" id="fleetAddress" name="fleetAddress" value="${address }" size="50" datatype="0,is_noquotation,50"/>
	  </td>
    </tr>
	<tbody id="tranId">
	<tr>
		<td align="right" width="15%">运送方式：</td>
		<td align="left" >
		<script>document.write(getItemValue(${orderInfo.DELIVERY_TYPE }));</script>
		<input type="hidden" id="deliveryType" name="deliveryType" value="${orderInfo.DELIVERY_TYPE }" />
		</td>
	</tr>
	<tr>
		<td align="right">运送地点：</td>
		<td align="left">${orderInfo.DELIVERY_ADDRESS }
		<input type="hidden" id="deliveryAddress" name="deliveryAddress" value="${orderInfo.ID }" />
		</td>
	</tr>
	<tr>
		<td align="right">联系人：</td>
		<td align="left">
		<input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" value="${orderInfo.LINK_MAN }" />
		</td>
	</tr>
	<tr>
		<td align="right">联系电话：</td>
		<td align="left">
		<input id="tel" name="tel" type="text" class="middle_txt" size="30" maxlength="11" value="${orderInfo.TEL }" />
		</td>
	</tr>
	<tr>
		<td align="right">改装说明：</td>
		<td align="left"><font style="color: red">${orderInfo.REFIT_REMARK }</font></td>
	</tr>
	</tbody>
	<tr>
		<td align="right">付款信息备注：</td>
		<td align="left">${orderInfo.PAY_REMARK }</td>
	</tr>
	<tr>
		<td align="right">备注说明：</td>
		<td align="left">
			<textarea id="orderRemark" name="orderRemark" rows="3" cols="80" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeyup="value = value.replace(/#/g, '') ;" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onchange="limitChars('orderRemark', 50)">${orderInfo.ORDER_REMARK }</textarea>
		</td>
	</tr>
</table>
<br>
<table class="table_list">
	<tr align="center" class="tabletitle">
		<th>审核日期</th>
		<th>审核单位</th>
		<th>审核人</th>
		<th>审核结果</th>
		<th>审核描述</th>
	</tr>
	<c:forEach items="${checkHisList}" var="checkHisList" varStatus="vstatus1">
		<tr align="center" class="<c:if test='${vstatus1.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus1.index%2!=0}'>table_list_row2</c:if>">
			<td><c:out value="${checkHisList.CHECK_DATE}" /></td>
			<td><c:out value="${checkHisList.ORG_NAME}" /></td>
			<td><c:out value="${checkHisList.USER_NAME}" /></td>
			<td><c:out value="${checkHisList.CHECK_STATUS}" /></td>
			<td><c:out value="${checkHisList.CHECK_DESC}" /></td>
		</tr>
	</c:forEach>
</table>

<table class="table_list">
    <tr class="table_list_row2" >
      <th colspan="2" align="left">审核信息</th>
    </tr>
    <tr class="table_list_row2" >
      <td width="9%" align="right">审核描述：</td>
      <td width="91%" align="left"><label>
        <textarea id="check_desc" name="check_desc" cols="50" rows="3"></textarea>
      </label></td>
    </tr>
    <tr class="table_list_row2" >
      	<td align="left">&nbsp;</td>
      	<td align="left">
		<input class='cssbutton' id="add22" name="add22" type="button" onclick="checkSubmit();" value ='审核' />
		<input class='cssbutton' id="ret" name="ret" type="button" onclick="retBack() ;" value ='驳回' />
		<!--<input class='cssbutton'  name="add21" type="button" onclick="checkClose();" value ='关闭' />-->
		<!--<input class='long_btn'  name="add221" type="button" onclick="createOrder();" value ='生成采购单' />-->
		<input class='cssbutton'  name="add232" type="button" onclick="history.back();" value ='返回' />
		<input type="hidden" id="k_dealerId" name="k_dealerId" value="${orderInfo.DEALER_ID }" />
		<input type="hidden" id="d_dealerId" name="d_dealerId" value="${orderInfo.D_DEALER_ID }" />
		<input type="hidden" id="totalPrice_" name="totalPrice_" value="" />
		<input type="hidden" id="fundTypeId" name="fundTypeId" value="" />
		<input type="hidden" id="isBingcai" name="isBingcai" value="" />
		<input type="hidden" name="ratePara" value="${ratePara}" />
	 </td>
   </tr>
</table>

	
</form>

<script type="text/javascript">
	function contentTrim(str){
		if(str){
			str = str.replace(/(^\s*)|(\s*$)/g, "");
			return str;
		}else{
			return "";
		} 
	}

	function addressHide(isFleet){
		if("1"==isFleet+"" || document.getElementById("isCover").checked){
			document.getElementById("cusTr").style.display = "inline";
			document.getElementById("tranId").style.display = "none";
			document.getElementById("isCover").checked = true;
			document.getElementById("isCover").value = 1;
		}else{
			document.getElementById("cusTr").style.display = "none";
			document.getElementById("tranId").style.display = "inline";
			document.getElementById("isCover").checked = false;
			document.getElementById("isCover").value = 0;
		}
	}

	function round(number,fractionDigits){   
	   with(Math){   
	       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
	    }   
	} 
	function changeRate(id){
		var MATERIAL_PRICE_MAX = '<%=Constant.MATERIAL_PRICE_MAX%>';
		var buyNO = document.getElementById("buyNO"+id).value;				  //采购数量
		var singlePrice = document.getElementById("singlePrice"+id).value;    //单价
		var discount_rate = document.getElementById("discount_rate"+id).value;//折扣率
		if(singlePrice-MATERIAL_PRICE_MAX>0){
			document.getElementById("price"+id).innerHTML ="价格未维护";
			singlePrice=0;
		}else{
			document.getElementById("price"+id).innerHTML = amountFormat(singlePrice);
		}
		var ratePara = parseInt(document.getElementById("ratePara").value, 10);
		if(discount_rate - ratePara >0){
			MyAlert("折扣率不能大于"+ratePara+"%,请重新输入!");
			document.getElementById("discount_rate"+id).value = 0;			 						//折扣率修改为0
			document.getElementById("discount_s_price_"+id).innerHTML = amountFormat(singlePrice);	//折扣后单价
			document.getElementById("discount_s_price"+id).value = round(singlePrice,2);
			document.getElementById("discount_price"+id).value = 0;					    			//折扣额
			document.getElementById("discount_price_"+id).innerHTML = 0;
			document.getElementById("caigouMoneyAll"+id).value = buyNO*singlePrice;	   				//合计
			document.getElementById("acountPrices"+id).innerHTML = amountFormat(buyNO*singlePrice);
			document.getElementById("acountPrices_"+id).value = round(buyNO*singlePrice,2);

			return;
		}
		var dis_rate = singlePrice*(discount_rate/100)*buyNO;										//折扣额
		var dis_value = singlePrice - singlePrice*(discount_rate/100);								//折扣后单价

		document.getElementById("discount_s_price_"+id).innerHTML = amountFormat(dis_value);						
		document.getElementById("discount_s_price"+id).value = round(dis_value,2);
		
		document.getElementById("discount_price_"+id).innerHTML = amountFormat(dis_rate);						
		document.getElementById("discount_price"+id).value = round(dis_rate,2);
		document.getElementById("acountPrices"+id).innerHTML = amountFormat(buyNO*dis_value)        //合计
		document.getElementById("acountPrices_"+id).value = round(buyNO*dis_value,2)        //合计
		
		var discount_prices = document.getElementsByName("discount_price");							//折扣额
		var discount_prices_ = 0;
		for(var i=0; i<discount_prices.length; i++){
			discount_prices_ = parseFloat(discount_prices_)+parseFloat(discount_prices[i].value);
		}
		document.getElementById("totalDiscountPrice").value = round(discount_prices_,2);
		document.getElementById("totalDiscountPrice_").innerHTML = amountFormat(discount_prices_);
		
		var acountPrices = document.getElementsByName("acountPrices_");
		var acountPrice = 0;
		for(var i=0;i<acountPrices.length;i++){
			acountPrice = parseFloat(acountPrice) + parseFloat(acountPrices[i].value);
		}
		document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(acountPrice);	
		
	}
	
	function changeCount_Price(acount,price,mid){
		if(acount>=0){
			document.getElementById("acountPrices"+mid).innerHTML = amountFormat(parseFloat(acount)*parseFloat(price.value));
			document.getElementById("acountPrices_"+mid).value = round((parseFloat(acount)*parseFloat(price.value)),2);
			document.getElementById("caigouMoneyAll"+mid).value = parseFloat(acount)*parseFloat(price.value);
		}else{
			MyAlert("请正确填写采购数量");
			return;
		}
		var buyNOs = document.getElementsByName("buyNO");
		var caigouMoneyAlls = document.getElementsByName("caigouMoneyAll");
		var num = 0;
		var buyMoney = 0;
		var flag = false;
		for(var j=0;j<buyNOs.length;j++){
			num = Number(num) + Number(buyNOs[j].value);
			buyMoney = Number(buyMoney) + Number(caigouMoneyAlls[j].value);
			if(contentTrim(buyNOs[j].value)){
				flag = true;
			}
		}
		for(var i=0;i<buyNOs.length;i++){
			if(flag){
				document.getElementById(i).disabled = "disabled";
			}else{
				document.getElementById(i).disabled = "";
			}
		}
		document.getElementById("caigouNOAll").innerHTML = num;		
		document.getElementById("caigouAllNumber").value = num;
		document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(buyMoney);		
		document.getElementById("totalPrice_").value = buyMoney;
		
	}
	//获得价格类型列表
	function getPriceList(){	
		var areaObj = document.getElementById("area");
		var dealerId = document.getElementById("k_dealerId").value;
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
	//合计:提报数量，审核数量，待审核数量
	function sumAll(){
		var orderAmount =0;
		var checkAmount =0;
		var nocheckAmount =0;
		var resourceAmount =0;
		//var applyedAmounts = 0;
		var orderAmounts = document.getElementsByName("orderAmount");
		var checkAmounts = document.getElementsByName("checkAmount");
		var nocheckAmounts = document.getElementsByName("nocheckAmount");
		var resourceAmounts = document.getElementsByName("resourceAmount");
		var applyedAmount = document.getElementsByName("applyedAmount");	//已采购数量
		for(var i=0; i<orderAmounts.length; i++){
			orderAmount = orderAmount + Number(orderAmounts[i].value);
			checkAmount = checkAmount + Number(checkAmounts[i].value);
			nocheckAmount = nocheckAmount + Number(nocheckAmounts[i].value);
			resourceAmount = resourceAmount + Number(resourceAmounts[i].value);
			//applyedAmounts = Number(applyedAmounts) + Number(applyedAmount[i].value);
		}
		document.getElementById("orderAmounts").innerText = orderAmount;
		document.getElementById("checkAmounts").innerText = checkAmount;
		document.getElementById("nocheckAmounts").innerText = nocheckAmount;
		document.getElementById("resourceAmounts").innerText = resourceAmount;
		//document.getElementById("applyedAmounts").innerHTML = applyedAmounts;
		//document.getElementById("applyedAmounts_").value = applyedAmounts;
	}


	//根据经销商id，获得经销商级别，过滤地址列表、资金信息
	function getDealerLevel(arg){
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:arg});
	}
	function showFund(json){
		if(json.returnValue != "1") {
			document.getElementById("span1").style.display = "none";
			document.getElementById("span2").style.display = "none";
			document.getElementById("span3").style.display = "none";
			document.getElementById("span4").style.display = "none";
			//document.getElementById("span5").style.display = "none";
			//document.getElementById("span6").style.display = "none";
			document.getElementById("priceTr").style.display = "none";
		}
		else{
			document.getElementById("span1").style.display = "inline";
			document.getElementById("span2").style.display = "inline";
			document.getElementById("span3").style.display = "inline";
			document.getElementById("span4").style.display = "inline";
			//document.getElementById("span5").style.display = "inline";
			//document.getElementById("span6").style.display = "inline";
			document.getElementById("priceTr").style.display = "inline";
			
			
			getAvailableAmount(document.getElementById('fundType').value);//获得账户余额
			getPriceList();//获得价格类型列表
		}
	}
	//获得可用资金
	function getAvailableAmount(arg){
		var areaObj = document.getElementById("area");
		//var dealerId = areaObj.value.split("|")[1];
		var dealerId = document.getElementById("k_dealerId").value;
		var fundTypeId = arg.split("|")[0];
		var isBingcai = arg.split("|")[1];
		document.getElementById("fundTypeId").value = fundTypeId;
		document.getElementById("isBingcai").value = isBingcai;
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
	}	
	function showAvailableAmount(json){
		var obj = document.getElementById("span4");
		document.getElementById("availableAmount_").value = json.returnValue;
		obj.innerHTML = amountFormat(json.returnValue);
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
		//var buyNO = document.getElementsByName("buyNO");
		for(var i=0;i<json.priceList.length;i++){
			var id = json.priceList[i].MATERIAL_ID;
			var price = json.priceList[i].PRICE;
			var buyNO = document.getElementById("buyNO"+id).value;//采购数量
			var obj1 = document.getElementById("price" + id);
			obj1.innerHTML = amountFormat(price);
			document.getElementById("discount_s_price"+id).value = price;
			document.getElementById("discount_s_price_"+id).innerHTML = amountFormat(price);
			//采购金额
			document.getElementById("acountPrices"+id).innerHTML = amountFormat(buyNO*price);
			document.getElementById("acountPrices_"+id).value = round(buyNO*price,2);
			var obj2 = document.getElementById("singlePrice" + id);
			obj2.value = price;
			document.getElementById("caigouMoneyAll"+id).value = buyNO*price;
			changeRate(id)	
		}
		var caigouMoneyAlls = document.getElementsByName("caigouMoneyAll");
		var buyMoney = 0;
		for(var j=0;j<caigouMoneyAlls.length;j++){
			buyMoney = Number(buyMoney) + Number(caigouMoneyAlls[j].value);
		}
		document.getElementById("caigouMoneyAll__").innerHTML = amountFormat(buyMoney);		
		isShowOtherPriceReason();
		//totalPrice();
	}
	
	// 判断是否显示使用其他价格原因
	function isShowOtherPriceReason(){	
		var areaObj = document.getElementById("area");
		//var dealerId = areaObj.value.split("|")[1];
		var dealerId = document.getElementById("k_dealerId").value;
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
	
	//detail_id: 订单明细id；no_check_amount：待审核数量；material_id：物料id;
	function toCheckDetail(detail_id,order_amount,material_id){
		var order_id = document.getElementById("order_id").value;
		OpenHtmlWindow('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toCheckDetail.do?detail_id='+detail_id+'&order_amount='+order_amount+'&material_id='+material_id+'&order_id='+order_id,700,500);
	}
	function queryOrderInfo(){
		var order_id = document.getElementById("order_id").value;
		var areaId = '<%=request.getAttribute("areaId") %>';
		showDealerInfo(order_id,areaId);
	}
	function showDealerInfo(order_id,areaId){
		$('fm').action= '<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toCheck.do?order_id='+order_id+'&areaId='+areaId;
		$('fm').submit();
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
		
	//保存校验
	function checkSubmit(value){
		if(!testLen(document.getElementById('orderRemark').value)) {
			MyAlert('备注录入已超过50个汉字的最大限！') ; 
			return false ;
		}
		if(submitForm('fm')){
			var totalDiscountPrice = document.getElementById("totalDiscountPrice").value;//折扣总额
			var discount = document.getElementById("discount").value;					 //可用折让
			var discountAccountId = document.getElementById("discountAccountId").value;	
			if(totalDiscountPrice - discount >0){
				MyAlert("折扣总额不能大于可用折让!");
				return;
			}
			var fundType = document.getElementsByName("fundType");				//资金类型
			if(!fundType[0].value){
				MyAlert("资金类型不存在，无法进行操作");
				return;
			}
			
			var nocheckAmounts = document.getElementsByName("nocheckAmount");	//待审核数量
			//var applyedAmounts = document.getElementsByName("applyedAmount");	//已采购数量
			
			var orderAmounts = document.getElementsByName("orderAmount");		//提交数量
			var buyNOs = document.getElementsByName("buyNO");					//采购数量
			for(var i=0;i<buyNOs.length; i++ ){
				if(parseInt(buyNOs[i].value, 10)> parseInt(orderAmounts[i].value, 10))	{
					MyAlert("采购数量不能大于提报数量！");
					return;
				}
			}
			var singlePrice = document.getElementsByName("singlePrice");        //单价
			//已采购数量+采购数量  不能大于 “待审核数量”
			//for(var i=0;i<nocheckAmounts.length;i++){
			//	if(Number(applyedAmounts[i].value) + Number(buyNOs[i].value) - Number(nocheckAmounts[i].value) >0){
			//		MyAlert("已采购数量+采购数量  不能大于 “待审核数量”");
			//		return;
			//	}
			//	if(singlePrice[i].value-0>0){
			//		MyAlert("价格未维护，无法提报!”");
			//		return;
			//	}
			//}
			
			//如果已满足数量总和与采购数量总和都为0，提示无法进行审核
			var checkAmount = document.getElementsByName("checkAmount");		//已满足数量
			var buy_check_sum = 0;
			for(var i=0;i<buyNOs.length;i++){
				buy_check_sum = Number(buy_check_sum)+Number(buyNOs[i].value)+Number(checkAmount[i].value);
			}
			if(buy_check_sum == 0){
				MyAlert("无审核信息，请重新操作");
				return;
			}
			var check_desc = document.getElementById("check_desc").value;
			if(check_desc.length>500){
				MyAlert("审核描述内容过多，请重新填写(最多500字)");
				return;
			}else{
				MyConfirm("确认提交？",checkSubmitAction);
			}
		}
	}
	//订单关闭
	function checkClose(){
		MyConfirm("是否关闭订单?",checkCloseAction);
	}
	
	//订单驳回
	function retBack() {
		MyConfirm("是否驳回订单?",retBackAction);
	}

	function retBackAction() {
		$('fm').action= "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/retBack.do" ;
		$('fm').submit();
	}

	function checkCloseAction(){
		$('fm').action= "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/checkClose.do"
		$('fm').submit();
	}
	
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	} 
	function checkSubmitAction(){
		var areaId = '${areaId}';
	 	makeNomalFormCall('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/checkSubmitAction.json?areaId='+areaId,showResult,'fm');
	 	document.getElementById("add22").disabled = "disabled";
	}
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("提交失败！请确认操作是否正确！");
			useableBtn($("add22"));
		} else if(json.returnValue == '2'){
			MyAlert("提交失败！可用余额不足！");
			useableBtn($("add22"));
		} else {
			window.parent.MyAlert("操作成功！");
			$('fm').action= "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/dealerOrderCheckInit.do";
			$('fm').submit();
		}
		
	}
	function createOrder(){
		var caigouNOAll = document.getElementById("caigouNOAll").innerHTML;//用户填写的采购数量总和
		var reportNO = document.getElementById("reportNO").value;//订单提报数量
		if(caigouNOAll>reportNO){
			MyAlert("采购数量不能大于提报数量!");
			return;
		}
		if(!caigouNOAll){
			MyAlert("请填写采购数量");
			return;
		}
		MyConfirm("是否生成采购订单?",createOrderAction);
		
	}

	function createOrderAction(){
		$('fm').action= "<%=request.getContextPath()%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/createOrder.do";
		$('fm').submit();
	}
	
	function getDiscountInfo(){
		var dealerId = document.getElementById("k_dealerId").value;
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
	}
	
	//大用户弹出
	function showFleet(){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
	}
</script>
</body>
</html>