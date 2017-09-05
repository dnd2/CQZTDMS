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
<script type="text/javascript">
<!--
function testLen(value) {
	var reg = /^.{0,70}$/ ;
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

function toBackCheck() {
	MyConfirm("确认驳回？",orderBack);
}

function orderBack() {
	disableBtn($("queryBtn1"));
	
	if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
		disableBtn($("orderBack"));
	}
	
	makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyBack.json",showCancelValue,'fm');
}

function showBackResult(json){
	if(json.returnValue == '1'){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyQueryPre.do';
 		$('fm').submit();
	}else{
		MyAlert("数据已被修改,驳回失败！");
		useableBtn($("queryBtn1"));
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			useableBtn($("orderBack"));
		}
	}
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 发运申请修改</div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="area" name="area" value="${map.AREA_ID}">
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
	      <!--<td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单周度：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	 ${map.ORDER_YEAR}年${map.ORDER_WEEK}周
	      </td>
	      --><td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
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
	    	<select name="fundType" disabled id="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      	</select>
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">账户余额：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show1"></span><input type="hidden" name="availableAmount" value="">
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">冻结资金：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show2"></span><input type="hidden" name="availableAmount_1" value="">
	    </td>
	  </tr>
	  
	  <tr class="cssTable" >
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">库存组织：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<select name="warehouseId" onchange="houseChange();">
				<c:forEach items="${wareHouseList}" var="po">
					<c:choose>
						<c:when test="${po.WAREHOUSE_ID == map.WAREHOUSE_ID}">
							<option value="${po.WAREHOUSE_ID}" selected="selected">${po.WAREHOUSE_NAME}</option>
						</c:when>
						<c:otherwise>
							<option value="${po.WAREHOUSE_ID}">${po.WAREHOUSE_NAME}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
	      	</select>
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运方式：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    		<script type="text/javascript">
	    		genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,${map.DELIVERY_TYPE},false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
			    </script>
	    </td>
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
			<th>原保留数量</th>
			<th>本次保留数量</th>
			<th>单价</th>
			<th>金额</th>
			<th>当前库可用库存</th>
			<th>可用库存总量</th>
			<th>未满足常规订单</th>
			<th>已发运数量</th>
			<th>操作</th>
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
						<input type="hidden" id="shipNumber${list1.MATERIAL_ID}" name="shipNumber" value="${list1.SHIP_NUMBER}"/>
						<input type="hidden" id="checkAmount${list1.MATERIAL_ID}" name="checkAmount" value="${list1.CHECK_AMOUNT}"/>
						<input type="hidden" id="callAmount${list1.MATERIAL_ID}" name="callAmount" value="${list1.CALL_AMOUNT}"/>
						<input type="hidden" id="totalPrice${list1.MATERIAL_ID}" name="totalPrice" value="${list1.TOTAL_PRICE}"/>
<!--						<input type="hidden" id="discountSPrice${list1.MATERIAL_ID}" name="discountSPrice" value="${list1.DISCOUNT_S_PRICE}"/>-->
<!--						<input type="hidden" id="discountPrice${list1.MATERIAL_ID}" name="discountPrice" value="${list1.DISCOUNT_PRICE}"/>-->
						<input type="hidden" id="batchNo${list1.MATERIAL_ID}" name="batchNo" value="${list1.BATCH_NO}"/>
						<input type="hidden" id="specialBatchNo${list1.MATERIAL_ID}" name="specialBatchNo" value="${list1.SPECIAL_BATCH_NO}"/>
						<input type="hidden" id="initNo${list1.MATERIAL_ID}" name="initNo" value="${list1.BATCH_NO}"/>
						<input type="hidden" id="unCheckGeneralAmount${list1.MATERIAL_ID}" name="unCheckGeneralAmount" value="${list1.GENERAL_AMOUNT}"/>
						<input type="hidden" id="priceList${list1.MATERIAL_ID}" name="priceList" value="0"/>
					</td>
					<td>${list1.MATERIAL_NAME}</td>
<!--					<c:if test="${map.ORDER_TYPE==10201003}">-->
<!--						<td>${list1.SPECIAL_BATCH_NO}</td>-->
<!--					</c:if>-->
					<td>${list1.REQ_AMOUNT}</td>
					<td>
						<input type="text" class="mini_txt" id="reserveAmount${list1.MATERIAL_ID}" name="reserveAmount" value="${list1.RESERVE_AMOUNT}" readonly="readonly" datatype='0,is_digit,6'>
						<a href="#" onclick="patchNoSelect('${list1.MATERIAL_ID}');">[修改]</a>
					</td>
					<td><input type="text" id="singlePrice${list1.MATERIAL_ID}" name="singlePrice" size="8" style="text-align:right" value="${list1.SINGLE_PRICE}" onchange="priceTotal();" datatype='0,isMoney,10'/><span id="priceDesc${list1.MATERIAL_ID}"></span></td>
					<td><span id='price2${list1.MATERIAL_ID}'><script type="text/javascript">document.write(amountFormat(${list1.TOTAL_PRICE}));</script></span></td>
					<td><span id="stock${list1.MATERIAL_ID}">0</span></td> <%--当前可用库存--%>
					<td><span id="avaPrice${list1.MATERIAL_ID}">${list1.AVA_STOCK}</span></td> <%--可用库存总量--%>
					<td>${list1.GENERAL_AMOUNT}</td><%--未满足常规订单--%>
<%--					<td><span id="batch${list1.MATERIAL_ID}">${list1.BATCH_NO}</span> </td>对应批次--%>
					<td>${list1.SHIP_NUMBER} </td><%--对应批次--%>
					<td>&nbsp;</td> 
				</tr>
			</c:forEach> 
		</tbody>
		<tr align="center" class="table_list_row2">
			<td></td>
			<td align="right"><strong>合计：</strong></td>
<!--			<c:if test="${map.ORDER_TYPE==10201003}">-->
<!--				<td></td>-->
<!--			</c:if>-->
			<td id="reqTotal"></td>
			<td id="reserveTotal"></td>
			<td><input type="hidden" id="priceTotalHidden" name="priceTotalHidden" value=""/></td>
			<td id="priceTotal">
			</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
		<table class="table_query">
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
	      	    <input type="hidden" name="orderTypeSel" value="${orderTypeSel}">
	      	    <input type="hidden" name="orderNo" value="${orderNo}">
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
<%--				<input type="button" id="queryBtn2" name="button1" class="cssbutton" onclick="toCancelCheck();" value="订单取消"/>--%>
<%--				<input type="hidden" id="queryBtn3" name="button3" class="long_btn" onclick="priceChk_();" value="保留数量确认"/>--%>
				<c:if test="${map.ORDER_TYPE==10201003}">
				<input type="button" name="orderBack" id="orderBack" class="cssbutton" onclick="toBackCheck();" value="驳回"/>
				</c:if>
				<input type="button" name="button3" class="cssbutton" onclick="history.back();" value="返回"/>
				<input type="hidden" name="ratePara" value="${ratePara}" />
				<input type="hidden" name="erpCode" value="${erpCode}" />
				<!-- 资金类型是否是“兵财” -->
				<input type="hidden" id="isBingcai" name="isBingcai" value="" />
				<!-- 原资金类型 -->		
				<input type="hidden" id="old_fund_type_id" name="old_fund_type_id" value="" />		
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
		if('${map.DELIVERY_TYPE}' == '<%=Constant.TRANSPORT_TYPE_02%>'){
			document.getElementById("flag").value = "0";
		}
		getFund();
		priceTotal();
		//getPriceList();
	
		addressHide(document.getElementById("deliveryType").value);
		//getReceiverList();
		getStock(1);setRead();
		
		getStockSuzuki();
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
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		}
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
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else if(json.returnValue == '3'){
			MyAlert("保存失败！可用余额不足！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
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
		showDiscountInfo(json);
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
		
		if(${map.ORDER_TYPE}!=10201003) {
			priceTypeChange() ;
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
		if(document.getElementById("add22")) {
			document.getElementById("add22").disabled = true ;
		}
		var dealerType = document.getElementById("dealerType").value;
		if(dealerType == '<%=Constant.DEALER_TYPE_JSZX%>'){
			var orderId = document.getElementById("orderId").value;
			var materialCode = document.getElementById("materialCode").value;
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/isHasPriceAtJszx.json";
			makeCall(url,isHasPrice,{materialCode:materialCode,orderId:orderId}); 
		}
		else{
			getMaterialInfo();
		}
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
		var checkAmount = document.getElementById("checkAmount").value;
		var callAmount = document.getElementById("callAmount").value;
		var erpCode = document.getElementById("erpCode").value;
		var orderDealerId = document.getElementById("orderDealerId").value;
		var orderType = document.getElementById("orderType").value;
		var orderId = document.getElementById("orderId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode,priceId:priceId,entityCode:erpCode,orderDealerId:orderDealerId,checkAmount:checkAmount,callAmount:callAmount,orderType:orderType,orderId:orderId}); 
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
			tempHtml += "<input type='hidden' name='ver' value='0'/>";
			tempHtml += "<input type='hidden' id='orderDetailId"+timeValue+"' name='orderDetailId' value='"+json.info.DETAIL_ID+"'/>";
			tempHtml += "<input type='hidden' id='materialId"+timeValue+"' name='materialId' value='"+timeValue+"'/>";
			tempHtml += "<input type='hidden' id='reqAmount"+timeValue+"' name='reqAmount' value='0'/>";
			tempHtml += "<input type='hidden' id='shipNumber"+timeValue+"' name='shipNumber' value='0'/>";
			tempHtml += "<input type='hidden' id='checkAmount"+timeValue+"' name='checkAmount' value='"+checkAmount+"'/>";
			tempHtml += "<input type='hidden' id='callAmount"+timeValue+"' name='callAmount' value='"+callAmount+"'/>";
			tempHtml += "<input type='hidden' id='totalPrice"+timeValue+"' name='totalPrice' value=''/>";
			tempHtml += "<input type='hidden' id='discountSPrice"+timeValue+"' name='discountSPrice' value=''/>";
			tempHtml += "<input type='hidden' id='discountPrice"+timeValue+"' name='discountPrice' value=''/>";
			tempHtml += "<input type='hidden' id='batchNo"+timeValue+"' name='batchNo' value=''/>";
			tempHtml += "<input type='hidden' id='specialBatchNo"+timeValue+"' name='specialBatchNo' value=''/>";
			tempHtml += "<input type='hidden' id='initNo"+timeValue+"' name='initNo' value=''/>";
			tempHtml += "<input type='hidden' id='unCheckGeneralAmount"+timeValue+"' name='unCheckGeneralAmount' value='"+json.info.GENERAL_AMOUNT+"'/>";
			tempHtml += "<input type='hidden' id='priceList"+timeValue+"' name='priceList'   value='"+json.infos.LIST_HEADER_ID+"' />";
			newCell.innerHTML = tempHtml;
			newCell = newRow.insertCell(1);//物料名称
			newCell.innerHTML = json.info.MATERIAL_NAME;
			newCell = newRow.insertCell(2);//提报数量
			newCell.innerHTML = "0";
			newCell = newRow.insertCell(3);//保留数量
			newCell.innerHTML = "  <input type='text' style='width:20px; height:18px;line-height:15px;border:1px solid #a6b2c8;padding-left: 2px;' id='reserveAmount"+timeValue+"' name='reserveAmount' value='0' readonly='readonly' datatype='0,is_digit,6'><font color='red' fontSize='9pt' width='7' paddingLeft='2px' height='18px'> * </font><a href='#' onclick='patchNoSelect(\""+json.info.MATERIAL_ID+"\")'>[修改]</a>";
<%--									<input type="text" class="mini_txt" id="reserveAmount${list1.MATERIAL_ID}" name="reserveAmount" value="${list1.RESERVE_AMOUNT}" readonly="readonly" datatype='0,is_digit,6'><a href="#" onclick="patchNoSelect('${list1.MATERIAL_ID}');">[修改]</a>--%>
			
			
			newCell = newRow.insertCell(4);//单价
			newCell.innerHTML = "<input type='text' id='singlePrice"+timeValue+"' name='singlePrice' value='"+json.info.PRICE+"' size='8' style='text-align:right'  onchange='priceTotal();' datatype='0,isMoney,10'/><font color='red'>*</font><span id='priceDesc"+timeValue+"'></span>";
			newCell = newRow.insertCell(5);//金额
			newCell.innerHTML = "<span id='price2"+timeValue+"'>0</span>";
<%--			newCell = newRow.insertCell(6);//折扣率--%>
<%--			newCell.innerHTML = "<input type='text' class='mini_txt' id='discountRate"+timeValue+"' name='discountRate' value='0' onchange='priceTotal();' datatype='0,is_double,6' decimal='2'><font color='red'>*</font>";--%>
<%--			newCell = newRow.insertCell(7);//折扣后单价--%>
<%--			newCell.innerHTML = "<span id='price3"+timeValue+"'></span>";--%>
<%--			newCell = newRow.insertCell(8);//折扣额--%>
<%--			newCell.innerHTML = "<span id='price4"+timeValue+"'></span>";--%>
			newCell = newRow.insertCell(6);//可用库存
			newCell.innerHTML = "<span id='stock"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(7);//可用库存总量
			newCell.innerHTML = "<span id='avaPrice"+timeValue+"'>"+json.info.AVA_STOCK+"</span>";
			newCell = newRow.insertCell(8);//未满足常规订单
			newCell.innerHTML = json.info.GENERAL_AMOUNT;
<%--			newCell = newRow.insertCell(12);//对应批次--%>
<%--			newCell.innerHTML = "<span id='batch"+timeValue+"'></span>";--%>
			newCell = newRow.insertCell(9);//已发运数量
			newCell.innerHTML = "0";			
			newCell = newRow.insertCell(10);//操作
			newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a>";
			
			priceTotal();
			getStock(timeValue);setRead() ;
			
			if(document.getElementById("add22")) {
				document.getElementById("add22").disabled = false ;
			}
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
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=10 && obj.id.substring(0,10)=="materialId"){
				ids += obj.value + ",";
			}   
		} 
		ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
		var areaId = document.getElementById("area").value;	
		var productId = "" ;
		
		if(document.getElementById("_productId_")) {
			productId = document.getElementById("_productId_").value ;
		}
		var orderId = document.getElementById("orderId").value;
		var orderType = document.getElementById("orderType").value;
		if(orderType == '<%=Constant.ORDER_TYPE_01%>'){
			showMaterialByModify_Suzuki('materialCode','checkAmount','callAmount','true',areaId,ids, productId,orderId);
		}else{
			showMaterialByAreaId_Suzuki('materialCode','','true',areaId,ids, productId,orderType);
		}
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
		if(!chkPriceList()) {
			MyAlert("请选择价格列表!") ;
			
			return false ;
		}
		if(!testLen(document.getElementById('orderRemark').value)) {
			MyAlert('备注录入已超过70个汉字的最大限！') ; 
			return false ;
		}
		if(submitForm('fm')){
			var priceFlag = 1 ;
			document.getElementById("modifyFlag").value = "0";
			var detailId = document.getElementsByName("detailId");
			var materialId = document.getElementsByName("materialId");
			var reqAmount = document.getElementsByName("reqAmount");
			var reserveAmount = document.getElementsByName("reserveAmount");
			//var discountRate = document.getElementsByName("discountRate");
			var unCheckGeneralAmount = document.getElementsByName("unCheckGeneralAmount");
			var checkGeneral = document.getElementById("checkGeneral").value;
			var orderType = document.getElementById("orderType").value;
			//var ratePara = parseInt(document.getElementById("ratePara").value, 10);
			var singlePrice = document.getElementsByName("singlePrice") ;
			for(var i=0 ;i< detailId.length; i++){
			
				var reqValue = reqAmount[i].value;
				var reserveValue = reserveAmount[i].value;
				//var discountRateValue = discountRate[i].value;
				var materialValue = materialId[i].value;
				var avaStockValue = document.getElementById("avaPrice"+materialValue).innerHTML;
				var unCheckGeneralValue = unCheckGeneralAmount[i].value;
				var priceDesc = document.getElementById("priceDesc" + materialId[i].value).innerHTML;
				var singleValue = parseFloat(singlePrice[i].value) ;
				//if(parseFloat(discountRateValue, 10) > ratePara) {
				//	MyAlert("折扣率不能大于"+ratePara+"%！");
				//	return;
				//}
<!--				if(parseInt(reqValue, 10) != parseInt(reserveValue, 10)){-->
<!--					if('${map.ORDER_TYPE}' == '<%=Constant.ORDER_TYPE_01%>'){-->
<!--						if(parseInt(reqValue, 10) < parseInt(reserveValue, 10)){-->
<!--							MyAlert("常规订单保留数量不能大于申请数量！");-->
<!--							return;-->
<!--						}-->
<!--					}-->
<!--					else{-->
<!--						document.getElementById("modifyFlag").value = "1";-->
<!--					}-->
<!--				}-->
				if(priceDesc != "" && reserveValue > 0){
					MyAlert("价格未维护物料保留资源数量不能大于0！");
					return;
				}
				if(orderType != "<%=Constant.ORDER_TYPE_01%>" && checkGeneral == '1'){
					if(parseInt(avaStockValue, 10) - parseInt(unCheckGeneralValue, 10) < 0){
						MyAlert("可用库存总量不能小于未满足常规订单！");
						return;
					}
				}
				
				if(parseInt(reserveValue) > 0) {
					if(singleValue == 0) {
						priceFlag = 0 ;
					} else if(singleValue < 0) {
						MyAlert("资源单价不能为负数！");
						
						return;
					}
				}
			}
			var dealerType = document.getElementById("dealerType").value;
			if(dealerType == "<%=Constant.DEALER_TYPE_JSZX%>"){
				var oldPrice = document.getElementById("oldPrice").value;
				var totalPrice = document.getElementById("reqTotalPrice").value;
				if(parseFloat(totalPrice, 10) > parseFloat(oldPrice, 10)) {
					MyAlert("结算中心订单审核资源总价不能大于原发运申请总价！");
					return;
				}
			}
			var reserveTotalAmount = document.getElementById("reserveTotalAmount").value;
			//if(reserveTotalAmount == 0){
			//	MyAlert("资源保留数量合计不能为0！");
			//	return;
			//}
			
			var returnStr = "";
			var value1 = document.getElementById("fundType").value;
			var value2 = document.getElementById("deliveryType").value;
			var value3 = document.getElementById("priceId").value;
			if(value1.split("|")[0] != '${map.FUND_TYPE_ID}'){
				var oldValue = document.getElementById("oldFundTypeName").value;
				var newValue = document.getElementById('fundType').options[document.getElementById('fundType').selectedIndex].text
				returnStr += "原资金类型：" + oldValue + "<BR>现资金类型：" + newValue + "<BR>";
			}
			if(value2 != '${map.DELIVERY_TYPE}'){
				var oldValue = document.getElementById("oldDeliveryTypeName").value;
				var newValue = document.getElementById('deliveryType').options[document.getElementById('deliveryType').selectedIndex].text
				returnStr += "原发运方式：" + oldValue + "<BR>现发运方式：" + newValue + "<BR>";
			}
			if(value3 != '${map.PRICE_ID}'){
				var oldValue = document.getElementById("oldPriceDesc").value;
				var newValue = document.getElementById('priceId').options[document.getElementById('priceId').selectedIndex].text
				returnStr += "原价格类型：" + oldValue + "<BR>现价格类型：" + newValue + "<BR>";
			}
			
			if(priceFlag == 0) {
				returnStr += "<font color='red'>存在保留数量大于0，且保留资源单价为0的资源</font>，是否确认保存？" ;
			} else {
				returnStr += "是否确认保存？";
			}
			MyConfirm(returnStr,putForword);
		}
	}
	
	//审核保存提交
	function putForword(){
		disableBtn($("queryBtn1"));
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		}
		
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifySave.json",showForwordValue,'fm');
	}
	
	function showForwordValue(json){
		if(json.returnValue == '1'){
			$('fm').action = "<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyQueryPre.do" ;
			$('fm').submit() ;
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改,保存失败！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else if(json.returnValue == '3'){
			MyAlert("提交失败！可用余额不足！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
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
		var dealerId = document.getElementById("orderDealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
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
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
		makeCall(url,showAddressList,{dealerId:dealerId});
	}	
	
	function showAddressList(json){
		var obj = document.getElementById("deliveryAddress");
		obj.options.length = 0;
		for(var i=0;i<json.addressList.length;i++){
			obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
			if(json.addressList[i].ID + "" == '${map.ADDRESS_ID}'){
				obj.options[i].selected = true;
			}
		}
		
		getAddressInfo(document.getElementById("deliveryAddress").value);
	}
	
	function getAddressInfo(arg){
		var addressId = arg;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressInfo.json";
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
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		}
		
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyCancel.json",showCancelValue,'fm');
	}
	
	function showCancelValue(json){
		if(json.returnValue == '1'){
			$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/orderResourceModifyQueryPre.do';
	 		$('fm').submit();
		}else{
			MyAlert("数据已被修改,取消失败！");
			useableBtn($("queryBtn1"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
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
		if(parseInt(${parReserve}) <= 0) {
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/compareToAction.json" ;
			
			makeFormCall(url, retPriceResult_, "fm") ;
		} else {
			reserveAmountConfirm() ;
		}
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
		if(json.Flag.COUNTS<=0){
			MyAlert("ERP和DMS的资金类型不匹配！！！")
			return;
		}
		if(parseInt(${parReserve}) <= 0) {
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceModify/compareToAction.json" ;
			makeFormCall(url, retPriceResult, "fm") ;
		} else {
			toSaveCheck() ;
		}
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
</script>
</body>
</html>