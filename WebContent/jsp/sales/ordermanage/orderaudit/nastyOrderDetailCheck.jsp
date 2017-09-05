<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 补充订单订单审核</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>订货方</th>
			<th>开票方</th>
			<th>订单号</th>
			<th>订单周度</th>
			<th>订单类型</th>
			<th>资金类型</th>
			<th>提报数量</th>
			<th>已审核数量</th>
			<th>待审核数量</th>
		</tr>
			<tr align="center" class="table_list_row2">
				<td><c:out value="${map.DEALER_NAME1}"/></td>
				<td><c:out value="${map.DEALER_NAME2}"/></td>
				<td>
					<c:out value="${map.ORDER_NO}"/>
					<input type="hidden" name="orderId" id="orderId" value="<c:out value="${orderId}"/>"/>
					<input type="hidden" name="orderVer" id="orderVer" value="${map.VER}"/>
				</td>
				<td>
					<c:out value="${map.ORDER_YEAR}"/>.<c:out value="${map.ORDER_WEEK}"/>
					<input type="hidden" name="areaId" value="<c:out value="${map.AREA_ID}"/>"/>
					<input type="hidden" name="orderYear" value="<c:out value="${map.ORDER_YEAR}"/>"/>
					<input type="hidden" name="orderWeek" value="<c:out value="${map.ORDER_WEEK}"/>"/>
				</td>
				<td>
					<c:out value="${map.ORDER_TYPE_NAME}"/>
					<input type="hidden" name="orderType" id="orderType" value="<c:out value="${map.ORDER_TYPE}"/>"/>
				</td>
				<td><c:out value="${map.TYPE_NAME}"/></td>
				<td><c:out value="${map.ORDER_AMOUNT}"/></td>
				<td><c:out value="${map.CHECK_AMOUNT}"/></td>
				<td><c:out value="${map.WAIT_CHECK}"/></td>
			</tr>
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>车系</th>
			<th>物料编号</th>
			<th>物料名称</th>
			<c:if test="${map.ORDER_TYPE!=10201002}">
				<th>订做批次号</th>
			</c:if>
			<th>提报数量</th>
			<th>单价</th>
			<th>合计</th>
			<th>上次审核数量</th>
			<!--<th>待审核数量</th>
			--><th>本次审核数量</th>
			<!--<th>审核数量</th>
			--><th>资源数量</th>
			<th>可用库存</th>
		</tr>
		<c:forEach items="${list1}" var="list1" varStatus="vstatus">
			<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td><c:out value="${list1.SERIES_NAME}"/></td>
				<td><c:out value="${list1.MATERIAL_CODE}"/></td>
				<td><c:out value="${list1.MATERIAL_NAME}"/></td>
				<c:if test="${map.ORDER_TYPE!=10201002}">
					<td>
						<c:out value="${list1.SPECIAL_BATCH_NO}"/>
						<input type="hidden" name="batchNo" value="<c:out value="${list1.SPECIAL_BATCH_NO}"/>"/>
					</td>
				</c:if>
				<td><c:out value="${list1.ORDER_AMOUNT}"/><input type="hidden" name="orderAmount" value="<c:out value="${list1.ORDER_AMOUNT}"/>"/><input type="hidden" id="amount${list1.DETAIL_ID}" name="amount${list1.DETAIL_ID}" value="<c:out value="${list1.ORDER_AMOUNT}"/>"/></td>
				<td><span id='price${list1.DETAIL_ID}'><script type="text/javascript">document.write(amountFormat(${list1.SINGLE_PRICE}));</script></span><input type="hidden" id="singlePrice${list1.DETAIL_ID}" name="singlePrice${list1.DETAIL_ID}" value="<c:out value="${list1.SINGLE_PRICE}"/>"/></td>
				<td><span id="hejiPrice${list1.DETAIL_ID}"><script type="text/javascript">document.write(amountFormat(${list1.TOTAL_PRICE}));</script></span></td>
				<td><c:out value="${list1.CHECK_AMOUNT}"/><input type="hidden" name="checkAmount" value="<c:out value="${list1.CHECK_AMOUNT}"/>"/></td>
				<!--<td><c:out value="${list1.WAIT_CHECK}"/><input type="hidden" name="waitCheck" value="<c:out value="${list1.WAIT_CHECK}"/>"/></td>
				-->
				<td>
					<!--<input type="text" name="amount" id="<c:out value="${list1.DETAIL_ID}"/>" size="4" value="0" datatype="1,is_noquotation,4" 
					onchange="toChangeNum(this.value,<c:out value="${list1.WAIT_CHECK}"/>,<c:out value="${list1.RESOURCE_AMOUNT}"/>,<c:out value="${list1.DETAIL_ID}"/>,<c:out value="${list1.CHECK_AMOUNT}"/>)"/>
					-->
					<input type="text" name="amount" id="<c:out value="${list1.DETAIL_ID}"/>" size="4" value="0" datatype="1,is_noquotation,4" onchange="toChangeNum(this.value,<c:out value="${list1.ORDER_AMOUNT}"/>,<c:out value="${list1.RESOURCE_AMOUNT}"/>,<c:out value="${list1.CHECK_AMOUNT}"/>,<c:out value="${list1.DETAIL_ID}"/>)"/>
					<input type="hidden" name="detailId" value="<c:out value="${list1.DETAIL_ID}"/>"/>
					<input type="hidden" name="materialId" value="<c:out value="${list1.MATERIAL_ID}"/>"/>
					<input type="hidden" name="ver" value="<c:out value="${list1.VER}"/>"/>
					<input type="hidden" id="oAmount${list1.DETAIL_ID}" name="oAmount${list1.DETAIL_ID}" value="<c:out value="${list1.ORDER_AMOUNT}"/>"/>
					<input type="hidden" id="did${list1.DETAIL_ID}" name="did${list1.DETAIL_ID}" value="<c:out value="${list1.DETAIL_ID}"/>"/>
				</td>
				<td><c:out value="${list1.RESOURCE_AMOUNT}"/><input type="hidden" name="resourceAmount" value="<c:out value="${list1.RESOURCE_AMOUNT}"/>"/></td>
				<td><c:out value="${list1.AVA_STOCK}"/><input type="hidden" name="stockAmount" value="<c:out value="${list1.AVA_STOCK}"/>"/></td>
			</tr>
		</c:forEach> 
		<tr align="center" class="table_list_row2">
			<td></td>
			<td></td>
			<td align="right"><strong>合计：</strong></td>
			<c:if test="${map.ORDER_TYPE!=10201002}">
				<td></td>
			</c:if>
			<td id="orderAmounts"></td>
			<td></td>
			<td id="totalPrices"></td>
			<td id="checkAmounts"></td>
			<!--<td id="waitChecks"></td>
			--><td id="amounts">0</td>
			<td id="resourceAmounts"></td>
			<td id="stockAmounts"></td>
		</tr>
	</table>
	<br>
	<table class="table_query">
		<tr>
			<td align="right" width="10%">资金类型：</td>
			<td align="left" width="40%">
				<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
					<c:forEach items="${fundTypeList}" var="po">
						<c:choose>
							<c:when test="${po.TYPE_ID == map.FUND_TYPE_ID}">
								<option value="${po.TYPE_ID}" selected="selected">${po.TYPE_NAME}</option>
							</c:when>
							<c:otherwise>
								<option value="${po.TYPE_ID}">${po.TYPE_NAME}</option>
							</c:otherwise>
						</c:choose> 
					</c:forEach>
		      	</select>
			</td>
			<td align="right" width="10%">可用余额：</td>
			<td align="left" width="40%"><span id="show1"></span><input type="hidden" name="availableAmount" value=""> </td>
		</tr>
		<tr>
			<td align="right" width="10%">价格类型：</td>
			<td align="left" width="90%" colspan="3">
				<select name="priceId" onchange="priceTypeChange();">
		      	</select>
			</td>
		</tr>
		<tr id="otherTr">
			<td align="right" width="10%">使用其他价格原因：</td>
			<td align="left" width="90%" colspan="3">
				<input name="otherPriceReason" type="text" class="long_txt" value="${map.OTHER_PRICE_REASON}"/>
			</td>
		</tr>
		<tr>
			<td align="right" width="10%">使用折让：</td>
			<td align="left" width="40%"><c:out value="${map.DISCOUNT}"/></td>
			<td align="right" width="10%">运送方式：</td>
			<td align="left" width="40%"><c:out value="${map.DELIVERY_TYPE_NAME}"/></td>
		</tr>
		<c:if test="${map.DELIVERY_TYPE==10291002}">
			<tr>
				<td align="right">收货方：</td>
				<td align="left"><c:out value="${map.DEALER_NAME3}"/></td>
				<td align="right">运送地点：</td>
				<td align="left"><c:out value="${map.ADDRESS}"/></td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left"><c:out value="${map.LINK_MAN}"/></td>
				<td align="right">联系电话：</td>
				<td align="left"><c:out value="${map.TEL}"/></td>
			</tr>
		</c:if>
		<c:if test="${map.DELIVERY_TYPE==10291003}">
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
			<td align="right">备注说明：</td>
			<td align="left" colspan="3"><c:out value="${map.ORDER_REMARK}"/></td>
		</tr>
		<tr>
			<td align="right">审核描述：</td>
			<td align="left" colspan="3"><textarea name="checkRemark" id="checkRemark" cols="30" rows="3"></textarea></td>
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
		<c:forEach items="${list2}" var="list2" varStatus="vstatus1">
			<tr align="center" class="<c:if test='${vstatus1.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus1.index%2!=0}'>table_list_row2</c:if>">
				<td><c:out value="${list2.CHECK_DATE}"/></td>
				<td><c:out value="${list2.ORG_NAME}"/></td>
				<td><c:out value="${list2.USER_NAME}"/></td>
				<td><c:out value="${list2.CHECK_STATUS}"/></td>
				<td><c:out value="${list2.CHECK_DESC}"/></td>
			</tr>
		</c:forEach> 
	</table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="hidden" name="detailIds" id="detailIds"/>
				<input type="hidden" name="strAmounts" id="strAmounts"/>
				<input type="hidden" name="oldAmounts" id="oldAmounts"/>
				<input type="hidden" name="odAmounts" id="odAmounts"/>
				<input type="hidden" name="materialIds" id="materialIds"/>
				<input type="hidden" name="vers" id="vers"/>
				<c:if test="${map.ORDER_TYPE!=10201002}">
					<input type="hidden" name="batchNos" id="batchNos"/>
				</c:if>
				<input type="hidden" name="aaaaa" id="aaaaa"/>
				<input type="hidden" name="bbbbb" id="bbbbb"/>
				<input type="hidden" name="ccccc" id="ccccc"/>
				<input type="hidden" name="dealerId" value="${map.BILLING_ORG_ID}"/>
				<input type="hidden" name="total" value=""/>
				<input type="hidden" name="checkPrice" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck('1');" value="审核完成"/>
				<input type="button" name="button3" class="cssbutton" onclick="toSaveCheck('0');" value="审核驳回"/>
				<!--<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck();" value="保存"/>-->
				<input type="button" name="button3" class="cssbutton" onclick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//初始化
	function doInit(){
		sumAll();
		getAvailableAmount(document.getElementById("fundType").value);
		getPriceList();
	}
	//合计订单数量，已审核数量，为审核数量，待审核数量
	function sumAll(){
		var orderAmount =0;
		var checkAmount =0;
		//var waitCheck =0;
		var resourceAmount =0;
		var stockAmount =0;
		var orderAmounts = document.getElementsByName("orderAmount");
		var checkAmounts = document.getElementsByName("checkAmount");
		//var waitChecks = document.getElementsByName("waitCheck");
		var resourceAmounts = document.getElementsByName("resourceAmount");
		var stockAmounts = document.getElementsByName("stockAmount");
		for(var i=0; i<orderAmounts.length; i++){
			orderAmount = orderAmount + Number(orderAmounts[i].value);
			checkAmount = checkAmount + Number(checkAmounts[i].value);
			//waitCheck = waitCheck + Number(waitChecks[i].value);
			resourceAmount = resourceAmount + Number(resourceAmounts[i].value);
			stockAmount = stockAmount + Number(stockAmounts[i].value);
		}
		document.getElementById("orderAmounts").innerText = orderAmount;
		document.getElementById("checkAmounts").innerText = checkAmount;
		//document.getElementById("waitChecks").innerText = waitCheck;
		document.getElementById("resourceAmounts").innerText = resourceAmount;
		document.getElementById("stockAmounts").innerText = stockAmount;
		document.getElementById("aaaaa").value = checkAmount;
		//document.getElementById("bbbbb").value = waitCheck;
		
		var myForm = document.getElementById("fm");
		var checkPrice = 0;
		var totalPrice = 0;
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=7 && obj.id.substring(0,7)=="oAmount"){
				var subStr = obj.id.substring(7,obj.id.length);
				var value1 = parseInt(obj.value, 10);
				var value2 = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);
				var value3 = parseFloat(document.getElementById(subStr).value, 10);
				var heji1 = value1 * value2;
				var heji2 = value2 * value3;
				document.getElementById("hejiPrice"+subStr).innerHTML = amountFormat(heji1);
				
				totalPrice += heji1;
				checkPrice += heji2;
			}   
		}  
		document.getElementById("totalPrices").innerHTML = amountFormat(totalPrice);
		document.getElementById("total").value = totalPrice;
		document.getElementById("checkPrice").value = checkPrice;
	}
	//本次申请数量校验
	function toChangeNum(value1,value2,value3,value4,value5){
		var allamounts= 0;
		var amount = value1;
		var orderAmount = value2;
		var resourceAmount = value3;
		var oldAmount = value4;
		var detailId = value5;
		var exp = /^(-?[1-9][0-9]*|0)$/;
		if(Number(amount)>Number(resourceAmount)+Number(oldAmount)){
			//MyDivAlert("本次审核数量不能大于资源数量加上上次审核数量,请重新输入！");
			//document.getElementById(detailId).value="0";
			//document.getElementById(detailId).focus();
		}
		if(Number(amount)>Number(orderAmount)){
			MyDivAlert("本次审核数量不能大于提报数量，请重新输入！");
			document.getElementById(detailId).value="0";
			document.getElementById(detailId).focus();
		}
		if(!exp.test(amount)){
			MyDivAlert("本次审核数量只能为整数，请重新输入！");
			document.getElementById(detailId).value="0";
			document.getElementById(detailId).focus();
		}
		if(Number(amount)<0){
			MyDivAlert("本次审核数量不能小于零，请重新输入！");
		 	document.getElementById(detailId).value="0";
			document.getElementById(detailId).focus();
		}
		var amounts = document.getElementsByName("amount");
		for(var i=0; i<amounts.length; i++){
			allamounts = allamounts + Number(amounts[i].value);
		}
		document.getElementById("amounts").innerText = allamounts;
	}
	//保存校验
	function toSaveCheck(arg){
		document.getElementById("flag").value = arg;
		var cnt = 0;
		var amounts = '';
		var detailIds ='';
		var oldAmounts ='';
		var orderAmounts ='';
		var materialIds ='';
		var batchNos ='';
		var vers = '';
		var orderType = document.getElementById("orderType").value;
		//var waitCheck = document.getElementsByName("waitCheck");
		//var checkAmount = document.getElementsByName("checkAmount");
		var orderAmount = document.getElementsByName("orderAmount");
		var resourceAmount = document.getElementsByName("resourceAmount");
		var amount = document.getElementsByName("amount");
		var detailId = document.getElementsByName("detailId");
		var oldAmount = document.getElementsByName("checkAmount");
		var materialId = document.getElementsByName("materialId");
		var ver = document.getElementsByName("ver");
		var exp = /^(-?[1-9][0-9]*|0)$/;
		if(orderType==<%=Constant.ORDER_TYPE_03%>){
			var batchNo = document.getElementsByName("batchNo");
		}
		
		for(var i=0 ;i< detailId.length; i++){
			cnt = cnt+Number(amount[i].value);
			if(Number(amount[i].value) > Number(orderAmount[i].value)){
				if(arg == '1'){
					MyDivAlert("本次审核数量不能大于提报数量，请重新输入！");
					return false;
				}
			}
			if(Number(amount[i].value) > Number(resourceAmount[i].value)+Number(oldAmount[i].value)){
				if(arg == '1'){
					//MyDivAlert("本次审核数量不能大于资源数量加上上次审核数量,请重新输入！");
					//return false;
				}
			}
			if(!exp.test(amount[i].value)){
				if(arg == '1'){
					MyDivAlert("本次审核数量只能为整数，请重新输入！");
					return false;
				}
			}
			if((Number(amount[i].value))<0){
				if(arg == '1'){
			 		MyDivAlert("本次审核数量不能小于零，请重新输入！");
				 	return false;
			 	}
			}
			amounts = amount[i].value + ',' + amounts;
			detailIds = detailId[i].value + ',' + detailIds;
			oldAmounts = oldAmount[i].value + ',' + oldAmounts;
			orderAmounts = orderAmount[i].value + ',' + orderAmounts;
			materialIds = materialId[i].value + ',' + materialIds;
			vers = ver[i].value + ',' + vers;
			if(orderType==<%=Constant.ORDER_TYPE_03%>){
				batchNos = batchNo[i].value + ',' + batchNos;
			}
		}
		if(cnt==0){
			if(arg == '1'){
				MyDivAlert("本次审核数量不能全部为零！");
	            return;
            }
		}
		
		document.getElementById("ccccc").value = cnt;
		document.getElementById("detailIds").value=detailIds;
		document.getElementById("strAmounts").value=amounts;
		document.getElementById("oldAmounts").value=oldAmounts;
		document.getElementById("odAmounts").value=orderAmounts;
		document.getElementById("materialIds").value=materialIds;
		document.getElementById("vers").value=vers;
		if(orderType==<%=Constant.ORDER_TYPE_03%>){
			document.getElementById("batchNos").value=batchNos;
		}
		MyDivConfirm("确认保存？",putForword);
	}
	//审核保存提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/NastyOrderCheckSave.json",showForwordValue,'fm','queryBtn');
	}
	//返回
	function toBack(){
		parent._hide();
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			//var check = document.getElementById("aaaaa").value;
			//var wait = document.getElementById("bbbbb").value;
			//var amount = document.getElementById("ccccc").value;
			//var parentRowObj = window.parent.$('inIframe').contentWindow.rowObj;
			//parentRowObj.cells[9].innerText=Number(amount);
			//parentRowObj.cells[10].innerText=Number(wait)-Number(amount);
			//parentRowObj.cells[10].childNodes[1].value = Number(parentRowObj.cells[10].childNodes[1].value)+1;
			var parentRowObj = parent.$('inIframe').contentWindow.rowObj;
			var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
			parentRowObj.parentElement.rows(rowIndex).removeNode(true);
			parent._hide();
			parent.MyAlert("保存成功！");
		}else if(json.returnValue == '2'){
			parent._hide();
			parent.MyAlert("数据已被修改,保存失败！");
			parent.toQuery();
		}else if(json.returnValue == '3'){
			parent._hide();
			parent.MyAlert("提交失败！可用余额不足！");
			parent.toQuery();
		}else{
			MyDivAlert("保存失败！请联系系统管理员！");
		}
	}
	
	function getAvailableAmount(arg){
		var dealerId = document.getElementById("dealerId").value;
		var fundTypeId = arg;
		var orderId = '${orderId}';
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId,orderId:orderId});
	}
	
	function showAvailableAmount(json){
		var obj1 = document.getElementById("show1");
		var obj2 = document.getElementById("availableAmount");
		obj1.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
		obj2.value = parseFloat(json.returnValue, 10);
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
				obj.options[i].selected = "selected";
			}
			else{
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
			}
		}
		isShowOtherPriceReason();
	}
	
	//价格类型改变
	function priceTypeChange(){	
		var priceId = document.getElementById("priceId").value;
		var ids = "";//已选中的物料id
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=3 && obj.id.substring(0,3)=="did"){
				ids += obj.value + ",";
			}   
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
			
			var obj1 = document.getElementById("price" + id);
			obj1.innerHTML = amountFormat(price);
			
			var obj2 = document.getElementById("singlePrice" + id);
			obj2.value = price;
		}
		isShowOtherPriceReason();//是否显示使用其他价格原因
		sumAll();
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
</script>
</body>
</html>