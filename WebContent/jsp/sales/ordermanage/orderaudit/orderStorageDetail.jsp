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
<script type="text/javascript" src="<%=request.getContextPath() %>/js/orderNumberFormat.js"></script>

<title>订单审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function lookBalance(){
	var dealerId = document.getElementById("dealerId").value;
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserve/balanceSelect.do?dealerId='+dealerId,700,500);
	}
<!--
function testLen(value) {
	var reg = /^.{0,70}$/ ;
	var flag = reg.exec(value) ;
	return flag ;
 }
 function   destory(){ 
 		var owner = getTopWinRef();
		owner.getElementById("dialog_div").removeNode(true); 
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

function reserveAmount1(){
	var materialId = document.getElementsByName("materialId");
	for(var i=0; i<materialId.length; i++){
		var subStr = materialId[i].value;
		var reqAmount = parseInt(document.getElementById("reqAmount"+subStr).value, 10);  //申请数量
		var reserveAmount = parseInt(document.getElementById("reserveAmount"+subStr).value, 10);//保留数量
		var stockAmount = parseInt(document.getElementById("stockAmount"+subStr).value, 10);//可用库存数量
		//MyAlert(reserveAmount+"111"+stockAmount);
		//var lockAmount = parseInt(document.getElementById("unCheckGeneralAmount"+subStr).value, 10);//锁定数量
		var reserveActualAmount = parseFloat(document.getElementById("reserveActualAmount"+subStr).value, 10);//审核点进来保留数量默认值
		var singlePrice = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);//单价
		//MyAlert(lockAmount+"222"+reserveActualAmount+"333"+singlePrice);
		//可用库存>申请数量stock
		if((stockAmount+reserveActualAmount)>reqAmount){
			if(reserveAmount>reqAmount){
				MyAlert('保留数量不能大于申请数量！') ; 
				document.getElementById("reserveAmount"+subStr).value=reqAmount;
			}else if(reqAmount<0){
				MyAlert('保留数量不能小于0！') ; 
				document.getElementById("reserveAmount"+subStr).value=0;
			}
		}else{
			if(reserveAmount>(stockAmount+reserveActualAmount)){
				MyAlert('保留数量不能大于当前可用库存！') ; 
				document.getElementById("reserveAmount"+subStr).value=(stockAmount+reserveActualAmount);
				
			}else if(reqAmount<0){
				MyAlert('保留数量不能小于0！') ; 
				document.getElementById("reserveAmount"+subStr).value=0;
			}
		}
		var reserveAmount1 = parseInt(document.getElementById("reserveAmount"+subStr).value, 10);//保留数量
		document.getElementById("stock"+subStr).innerHTML=(stockAmount+reserveActualAmount)-reserveAmount1;
		priceTotal()
	}	
}

function toBackCheck() {
	MyConfirm("确认驳回？",orderBack);
}

function orderBack() {
	disableBtn($("queryBtn1"));
	disableBtn($("queryBtn2"));
	disableBtn($("queryBtn3"));
	
	if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
		disableBtn($("orderBack"));
	}
	
	makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveBack.json",showBackResult,'fm');
}

function showBackResult(json){
	if(json.returnValue == '1'){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveQueryPre.do';
 		$('fm').submit();
 		destoryLock();
 		MyAlert("驳回成功！！！");
 		history.back();
	}else{
		MyAlert("数据已被修改,驳回失败！");
		useableBtn($("queryBtn1"));
		useableBtn($("queryBtn2"));
		useableBtn($("queryBtn3"));
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			useableBtn($("orderBack"));
		}
	}
}
//-->
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lock.js"></script>
</head>
<body onunload="destoryLock();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 订单储运审核</div>
<form method="post" name="fm" id="fm">
	<input type="hidden" id="billId" name="billId" value="${reqId}"/>
	<input type="hidden" id="sessionId" name="sessionId" value="${sessionId}"/>
	<input type="hidden" name="order_type" id="order_type" value="${map.ORDER_TYPE}" />
	<input type="hidden" value="<%=request.getContextPath()%>" id="contextPahts" name="contextPahts"/>
	<table class="table_query">
	 <tbody id="dealerTr">
		<tr class="cssTable" >
	   	<td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter" width="15%"><div  align="right">经销商代码：</div></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" width="15%">${map.DEALER_NAME1}</td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter" width="15%"><div  align="right">经销商名称：</div></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" width="15%">${map.DEALER_CODE}</td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"><div  align="right">省份：</div></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
			${map.ORG_NAME}
		</td>
	    </tr>
	    </tbody>
	     <tbody id="orderTr">
	    <tr class="cssTable" >
	     <%--  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">销售订单号：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	${map.ORDER_NO}
	      </td> --%>
	      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"><div  align="right">订单类型：</div></td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	${map.ORDER_TYPE_NAME} 
	      </td>
	      <td align = "right" style="color:#252525;width:70px;text-align:right;"><div  align="right">价格类型：</div></td>
	      <td align = "left" colspan="1">
	      	<!-- <label id="priceIds" class="long_txt"></label>
	      	<input type="hidden" id="priceId" name="priceId"/> -->
	      	<select id="priceId" name="priceId" disabled onchange="priceTypeChange();">
	        </select>
	      </td>
	       <td align = "right" style="color:#252525;width:70px;text-align:right;"><div  align="right"><span id="span5" style="display:none">订单原总价：</span></div><div  align="right">发运仓库：</div></td>
	  	<td align = "left" style="width:90px;"><input type="hidden" id="reserveTotalPrice" name="reserveTotalPrice" /><span id="spanReserve" style="display:none"></span>	${map.WAREHOUSE_NAME}</td>     
	  	<%-- <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter" width="15%"><div  align="right">发运仓库：</div></td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" width="15%">
	      	${map.WAREHOUSE_NAME}
	      </td> --%>
	  	
	    <%--  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
	      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	      	 ${map.RAISE_DATE}
	      </td> --%>
	    </tr>
	    </tbody>
	     <tbody id="fundTr">
	  <tr class="cssTable" >
	   <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"><div  align="right">资金类型：</div></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<select name="fundType" disabled id="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      	</select>
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"><div  align="right">可用余额：</div></td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
	    	<span id="show1"></span><input type="hidden" name="availableAmount" value="">
	    </td>
	      <td align = "right" style="color:#252525;width:70px;text-align:right;"><div  align="right"><span id="span7">订单金额：</span></div></td>
	  	<td align = "left" style="width:90px;"><input type="hidden" id="payAmount" name="payAmount" /><span id="span8" ></span></td>    
	   <%--  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">申请总价：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
		  <script>document.write(amountFormat(${map.REQ_TOTAL_PRICE}));</script>
		    <input type="hidden" name="dealerType" id="dealerType" value="${map.DEALER_TYPE}">
		    <input type="hidden" name="oldPrice" id="oldPrice" value="${map.REQ_TOTAL_PRICE}">
	    </td> --%>
	    <!--  <td align = "right" style="color:#252525;width:70px;text-align:right;"><span id="span7">保留总价：</span></td>
	  	<td align = "left" style="width:90px;"><input type="hidden" id="reserveTotalPrice" name="reserveTotalPrice" /><span id="spanReserve" ></span></td>      -->
	    </tr>
	    </tbody>
	     <tbody id="priceTr">
	  <tr class="cssTable" >
	 
	   
	     
	      
	    </tr>
	    </tbody>
	     <tbody id="rebateTr">
	     <tr class="cssTable" >
	     <td align = "right" style="color:#252525;width:90px;text-align:right;"><div  align="right">是否使用返利：</div></td>
	      
	     <c:choose>
              <c:when test="${map.IS_REBATE == 10041001}">
                <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">是</td>
              </c:when>
              <c:otherwise>
                 <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">否</td>
              </c:otherwise>
          </c:choose>
	     
	     <td  align="right" nowrap><div  align="right"><span id="rebate_account">返利可用余额：</span></div></td>
	    <td cosplan="2"> <span id="rebate_account_amount"></span></td>
	    <td  align="right" nowrap><div  align="right"><span id="rebate_discount">本次返利金额：</span></div></td>
	    <td> <input type="hidden" id="rebateAmount" name="rebateAmount"/><span id="rebate_discount_amount" name="rebate_discount_amount"></span></td>
	    <!--   <td align = "right" style="color:#252525;width:70px;text-align:right;"><span id="span7">应付金额：</span></td>
	  	<td align = "left" style="width:90px;"><input type="hidden" id="payAmount" name="payAmount" /><span id="span8" ></span></td>     -->
	     </tr>
	     </tbody>
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
			<th>合计</th>
			<th nowrap="nowrap">返利金额</th>
      		<th nowrap="nowrap">返利后总价</th>
			<th>当前库可用库存</th>
<!-- 			<th>未满足常规订单</th> -->
			<th>当月任务数</th>
			<th>当月已审核数</th>
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
						<input type="hidden" id="batchNo${list1.MATERIAL_ID}" name="batchNo" value="${list1.BATCH_NO}"/>
						<input type="hidden" id="specialBatchNo${list1.MATERIAL_ID}" name="specialBatchNo" value="${list1.SPECIAL_BATCH_NO}"/>
						<input type="hidden" id="initNo${list1.MATERIAL_ID}" name="initNo" value="${list1.BATCH_NO}"/>
						<input type="hidden" id="stockAmount${list1.MATERIAL_ID}" name="stockAmount" value="${list1.WARHOUSE_STOCK}"/>
						<input type="hidden" id="planAmount${list1.MATERIAL_ID}" name="planAmount" value="${list1.PLAN_AMOUNT}"/>
						<input type="hidden" id="auditAmount${list1.MATERIAL_ID}" name="auditAmount" value="${list1.AUDIT_AMOUNT}"/>
						<%-- <input type="hidden" id="unCheckGeneralAmount${list1.MATERIAL_ID}" name="unCheckGeneralAmount" value="${list1.GENERAL_AMOUNT}"/> --%>
					</td>
					<td>${list1.MATERIAL_NAME}</td>
					<c:if test="${map.ORDER_TYPE==10201003}">
						<td>${list1.SPECIAL_BATCH_NO}</td>
					</c:if>
					<td>${list1.REQ_AMOUNT}</td>
					<td>
					    <input type="hidden" class="mini_txt" id="reserveActualAmount${list1.MATERIAL_ID}" name="reserveActualAmount" value="${list1.RESERVE_AMOUNT}" datatype='0,is_digit,6'>
						<input type="text" class="mini_txt" id="reserveAmount${list1.MATERIAL_ID}" name="reserveAmount" value="${list1.RESERVE_AMOUNT}" onchange="reserveAmount1();" readonly="readonly" datatype='0,is_digit,6'>
						<%-- <a href="#" onclick="patchNoSelect('${list1.MATERIAL_ID}','${list1.RESERVE_AMOUNT}');">[审核]</a> --%>
					</td>
					<td><input type="text" id="singlePrice${list1.MATERIAL_ID}" name="singlePrice" size="8" style="text-align:right" value="${list1.SINGLE_PRICE}" onchange="priceTotal();" readonly="readonly" datatype='0,isMoney,10'/><span id="priceDesc${list1.MATERIAL_ID}"></span></td>
					<td><span id='price2${list1.MATERIAL_ID}'><script type="text/javascript">document.write(amountFormat(${list1.TOTAL_PRICE}));</script></span></td>
					 <td align="center" style=""><span id='lineRebateAmount${list1.MATERIAL_ID}'><script type="text/javascript">document.write(amountFormat(${list1.REBATE_AMOUNT }));</script></span><input type="hidden" id="lineRebateAmountHid${list1.MATERIAL_ID}" name="lineRebateAmountHid" value="${list1.REBATE_AMOUNT }"></td>
		      	  <td align="center" style=""><span id='lineRebateTotalPrice${list1.MATERIAL_ID}'> <script type="text/javascript">document.write(amountFormat( ${list1.TOTAL_PRICE}-${list1.REBATE_AMOUNT }));</script> </span></td>
					<td><span id="stock${list1.MATERIAL_ID}" name="stock">${list1.WARHOUSE_STOCK}</span></td> <%--当前可用库存--%>
					<%-- <td><a href="#" onclick="look_un_amount('${list1.MATERIAL_ID}');"><span id="unentity${list1.MATERIAL_ID}" name="unentity">${list1.GENERAL_AMOUNT}</span></a></td>未满足常规订单 --%>
					<td><span id="planAmount${list1.MATERIAL_ID}" name="planAmount">${list1.PLAN_AMOUNT}</span></td> <%--当月任务数--%>
					<td><span id="auditAmount${list1.MATERIAL_ID}" name="auditAmount">${list1.AUDIT_AMOUNT}</span></td> <%--当月已审核数--%>
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
			<td><input type="hidden" id="priceTotalHidden" name="priceTotalHidden" value=""/></td>
			<td id="priceTotal">
			
			</td>
			
			<td id="lineRebate"></td>
<!-- 			<td></td> -->
<!--			<td id="discountTotal"></td>-->
			<td id="lineRebateTotal"></td>
			<td></td>
			<td></td>
			<td></td>
<!--			<td></td>-->
<!--			<td></td>-->
<!--			<td></td>-->
		</tr>
	</table>
	<c:if test="${map.ORDER_TYPE!=10201001}">
		<table class="table_query">
		    <tr class="cssTable" >
		      <td width="100%" align="left">
			      <input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
<!--			      <input class='cssbutton' name="add22" id="add22" type="button" onclick="materialShow();" value ='新增产品' />-->
		          &nbsp;
		      </td>
		    </tr>
		</table>
	</c:if>
	<br>
	<table class="table_query">
		<tr>
			<td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter" width="15%"><div  align="right">发运方式：</div></td>
		    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input" width="20%">
		   		<%--  <script type="text/javascript">
				      	genSelBoxExp("deliveryType",<%=Constant.TT_TRANS_WAY%>,${map.DELIVERY_TYPE},false,"short_sel","","false",'');
				    </script> --%>
				    ${map.DELIVERY_TYPE_NAME }
				     <input type="hidden" id="deliveryType" name="deliveryType" value="${map.DELIVERY_TYPE}">
	    </td>
	    <td width="10%"></td><td></td>
		</tr>
		<%-- <tr class="cssTable" id="tmp_license_amountTr">
	      <td width="12%" align="right" valign="top" nowrap="nowrap"><div  align="right" style="display:none">临牌数量：</div></td>
	      <td colspan="3" align="left" valign="top" nowrap="nowrap">
	      	<input id="tmp_license_amount" name="tmp_license_amount" type="text" class="middle_txt" size="10" maxlength="30" readonly="readonly" value="${map.TMP_LICENSE_AMOUNT}" datatype="1,is_double,20" decimal="2"/>
	      </td>
	    </tr> --%>
		<tr id="otherTr" style="display:none;">
			<td align="right" ><div  align="right">使用其他价格原因：</div></td>
			<td align="left"  colspan="3">
				<input name="otherPriceReason" type="text" class="long_txt" value="${map.OTHER_PRICE_REASON}"/>
			</td>
		</tr>
		<tbody id="addTr">
			<!--<tr>
				<td align="right">收货方：</td>
				<td align="left" colspan="3">
					<select name="receiver" onchange="getAddressList();">
		      		</select>
				</td>
			</tr>
			<tr>
				<td align="right">运送地点：</td>
				<td align="left">
					<select name="deliveryAddress" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
		      		</select>
				</td>
				<td align="right">收车单位：</td>
				<td align="left" id="receiveOrg"></td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left">
					<input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" value="${map.LINK_MAN}"/>
				</td>
				<td align="right">联系电话：</td>
				<td align="left">
					<input name="tel" type="text" class="middle_txt" size="30" maxlength="30" value="${map.TEL}"/>
				</td>
			</tr>-->
		
			<tr>
				<td align="right"><div  align="right">收货方：</div></td>
				<td align="left" colspan="3">${map.DEALER_NAME3}</td>
			</tr>
			<c:choose>  
  				<c:when test="${map.IS_CUSTOM_ADDR==10041001}"> 
   
			<tr>
				<td align="right"><div  align="right">运送地点：</div></td>
				<td align="left" colspan="3">${map.PROVICE}&nbsp;${map.CITY}&nbsp;${map.TOWN}&nbsp;${map.CUSTOM_ADDR}</td>
			</tr>
			<tr>
				<td align="right"><div  align="right">联系人：</div></td>
				<td align="left">${map.CUSTOM_LINK_MAN}</td>
				<td align="right"><div  align="right">联系电话：</div></td>
				<td align="left">${map.CUSTOM_TEL}</td>
			</tr>
			</c:when>  
			<c:otherwise>
			<tr>
				<td align="right"><div  align="right">运送地点：</div></td>
				<td align="left" colspan="3" >${mapLinkInfo.ADDRESS}</td>
			</tr>
			<tr>
				<td align="right"><div  align="right">联系人：</div></td>
				<td align="left">${mapLinkInfo.LINK_MAN}</td>
				<td align="right"><div  align="right">联系电话：</div></td>
				<td align="left">${mapLinkInfo.TEL}</td>
			</tr>
			</c:otherwise>
			</c:choose>
		</tbody>
		<c:if test="${map.IS_FLEET==1}">
			<tr>
				<td align="right"><div  align="right">代交车：</div></td>
				<td align="left" colspan="3">是</td>
			</tr>
			<tr>
				<td align="right"><div  align="right">集团客户名称：</div></td>
				<td align="left"><c:out value="${map.FLEET_NAME}"/></td>
				<td align="right"><div  align="right">运送地点：</div></td>
				<td align="left"><c:out value="${map.FLEET_ADDRESS}"/></td>
			</tr>
		</c:if>
		<c:if test="${map.ORDER_TYPE==10201003}">
			<tr>
				<td align="right"><div  align="right">改装说明：</div></td>
				<td align="left" colspan="3"><c:out value="${map.REFIT_REMARK}"/></td>
			</tr>
		</c:if>
		<tr style="display:none">
			<td align="right"><div  align="right">付款信息备注：</div></td>
			<td align="left" colspan="3"><c:out value="${map.PAY_REMARK}"/></td>
		</tr>
		<tr>
			<td align="right"><div  align="right">备注说明：</div></td>
			<td align="left" colspan="3"><c:out value="${map.ORDER_REMARK}"/></td>
		</tr>
		<tr>
			<td align="right"><div  align="right">审核描述：</div></td>
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
			<tr align="center" class="table_list_row1">
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
				<input type="hidden" name="checkGeneral" id="checkGeneral"  value="${checkGeneral}">
				<input type="hidden" name="flag" id="flag" value="1">
				<input type="hidden" name="orderId" value="${orderId}"/>
				<input type="hidden" name="isRebate" id="isRebate" value="${map.IS_REBATE}"/>
				<input type="hidden" name="reqVer" value="${map.VER}"/>
				<input type="hidden" name="reqId" id="reqId" value="${reqId}"/>
				<input type="hidden" name="dealerId" id="dealerId" value="${map.BILLING_ORG_ID}"/>
				<input type="hidden" name="orderDealerId" value="${map.ORDER_ORG_ID}"/>
				<input type="hidden" name="reqTotalPrice" id="reqTotalPrice" value=""/>
				<input type="hidden" name="discountTotalPrice" value=""/>
				<input type="hidden" name="modifyFlag" id="modifyFlag" value="0">
				<input type="hidden" name="orderType" id="orderType" value="<c:out value="${map.ORDER_TYPE}"/>"/>
				<input type="hidden" name="areaGet" value="<c:out value="${map.AREA_ID}"/>"/>
				<input type="hidden" name="orderYear" value="<c:out value="${map.ORDER_YEAR}"/>"/>
				<input type="hidden" name="orderWeek" value="<c:out value="${map.ORDER_WEEK}"/>"/>
				<input type="hidden" name="reserveTotalAmount" id="reserveTotalAmount" value="0"> 
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
	      	    <input type="hidden" name="warehouseId" id="warehouseId" value="${map.WAREHOUSE_ID}"/>
	      	     <input type="hidden" id="total" name="total" value="0">
				<input type="button" id="queryBtn1" name="button1" class="cssbutton" onclick="storageAudit(1);" value="审核完成"/>
				<input type="button" id="queryBtn2" name="button2" class="cssbutton" onclick="storageAudit(2);" value="驳回"/>
				<input type="button" name="lookBal" id="lookBal" class="cssbutton" onclick="lookBalance();" value="查看余额"/>
				<input type="button" name="button3" class="cssbutton" onclick="history.back();" value="返回"/>
				<input type="hidden" name="ratePara" value="${ratePara}" />
				<input type="hidden" name="erpCode" value="${erpCode}" />
				<input type="hidden" id="storageAuditFlag" name="storageAuditFlag" value="" />
				<!-- 资金类型是否是“兵财” -->
				<input type="hidden" id="isBingcai" name="isBingcai" value="" />
				<!-- 原资金类型 -->		
				<input type="hidden" id="old_fund_type_id" name="old_fund_type_id" value="" />		
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function returnBack(){
		window.location.href="<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveQueryPre.do";
	}
	
	function storageAudit(value){
		document.getElementById("storageAuditFlag").value=value;
		var message="";
		if(value==1){
			message="是否确认审核通过！"
		}
		if(value==2){
			message="是否确认驳回！"
		}
		MyConfirm(message,putForword);
	}
	
	//审核保存提交
	function putForword(){
		document.getElementById("queryBtn1").disabled = true ;
		document.getElementById("queryBtn2").disabled = true ;
		document.getElementById("lookBal").disabled = true ;
		//disableBtn($("queryBtn3"));
		<%-- if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		} --%>
		
		var fundType=document.getElementById("fundType").value;
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderStorageAudit/orderStorageSave.json?fundType="+fundType,showForwordValue,'fm');
	}
	
	function showForwordValue(json){
		if(json.returnValue == '1'){
			MyAlert("审核完成！");
			//history.back();
			//MyAlert(123);
			//destoryLock();
			
			document.fm.action = "<%=contextPath%>/sales/ordermanage/orderaudit/OrderStorageAudit/orderStorageQueryPre.do" ;
			document.fm.submit() ;
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改,保存失败！");
			document.getElementById("queryBtn1").disabled = false ;
			document.getElementById("queryBtn2").disabled = false ;
			document.getElementById("lookBal").disabled = false ;
			
		}else if(json.returnValue == '3'){
			MyAlert("提交失败！可用余额不足！");
			document.getElementById("queryBtn1").disabled = false ;
			document.getElementById("queryBtn2").disabled = false ;
			document.getElementById("lookBal").disabled = false ;
			
			
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			document.getElementById("queryBtn1").disabled = false ;
			document.getElementById("queryBtn2").disabled = false ;
			document.getElementById("lookBal").disabled = false ;
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
	//初始化
	function doInit(){
		if(${map.DELIVERY_TYPE}== <%=Constant.TRANSPORT_TYPE_02%>){
			document.getElementById("flag").value = "0";
		}
		getFund();
		
		getPriceList();
		getStockSuzuki();
		priceTotal();
		//addressHide(document.getElementById("deliveryType").value);
		//getReceiverList();
		getStock(1);
		setRead();
		
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
		disableBtn($("queryBtn2"));
		disableBtn($("queryBtn3"));
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		}
		
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/reserveAmountConfirm.json",showreserveAmountConfirm,'fm');
	}
	function showreserveAmountConfirm(json){
		if(json.returnValue == '1'){
			//MyAlert("保留数量确认成功!");
			$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveQueryPre.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改,保存失败！");
			useableBtn($("queryBtn1"));
			useableBtn($("queryBtn2"));
			useableBtn($("queryBtn3"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else if(json.returnValue == '3'){
			MyAlert("保存失败！可用余额不足！");
			useableBtn($("queryBtn1"));
			useableBtn($("queryBtn2"));
			useableBtn($("queryBtn3"));
			
			if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
				useableBtn($("orderBack"));
			}
			
		}else{
			MyAlert("保存失败！请联系系统管理员！");
			useableBtn($("queryBtn1"));
			useableBtn($("queryBtn2"));
			useableBtn($("queryBtn3"));
			
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
			//var discountRate = parseFloat(document.getElementById("discountRate"+subStr).value, 10);//折扣率
			var totalPrice = reserve*singlePrice;
			//var discountSPrice = singlePrice*(1-discountRate/100);
			//var discountPrice = totalPrice*discountRate/100;
			document.getElementById("totalPrice"+subStr).value = totalPrice;//金额
			document.getElementById("price2"+subStr).innerHTML = totalPrice == 0 ? "0" : amountFormat(totalPrice);//金额显示
			//document.getElementById("discountSPrice"+subStr).value = discountSPrice;//折扣后单价
			//document.getElementById("price3"+subStr).innerHTML = discountSPrice == 0 ? "0" : amountFormat(discountSPrice);//折扣后单价显示
			//document.getElementById("discountPrice"+subStr).value = discountPrice;//折扣额
			//document.getElementById("price4"+subStr).innerHTML = discountPrice == 0 ? "0" : amountFormat(discountPrice);//折扣额显示
			reqTotal += req;
			reserveTotal += reserve;
			priceTotal += totalPrice;
			//discountTotal += discountPrice;
		}
		document.getElementById("reqTotal").innerHTML = reqTotal;
		document.getElementById("reserveTotal").innerHTML = reserveTotal;
		document.getElementById("reserveTotalAmount").value = reserveTotal;
		//屏蔽合计金额
		//document.getElementById("priceTotal").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		document.getElementById("priceTotalHidden").value=priceTotal == 0 ? "0" : amountFormat(priceTotal);
		//document.getElementById("discountTotal").innerHTML = discountTotal == 0 ? "0" : amountFormat(discountTotal);
		document.getElementById("reqTotalPrice").value = parseFloat(priceTotal, 10);
		//document.getElementById("discountTotalPrice").value = discountTotal;
		document.getElementById("reserveTotalPrice").value = priceTotal; //现保留总价
		document.getElementById("spanReserve").innerHTML = priceTotal == 0 ? "0" : amountFormat(priceTotal);
		var isRebate=document.getElementById("isRebate").value;
		changeRebate(isRebate);
	}
	
	//计算应付金额
	 function payAmount(){
		  var totalPrice=document.getElementById("reserveTotalPrice").value;
		  var rebateAmount=document.getElementById("rebateAmount").value;
		 // MyAlert("totalPrice=="+totalPrice);
		  //MyAlert("rebateAmount=="+rebateAmount);
		  var payAmount=totalPrice-rebateAmount;
		  document.getElementById("span8").innerHTML=amountFormat(payAmount);
		  //document.getElementById("span8").innerHTML=payAmount;
		 /*  if(payAmount<0){
			  var payAmount1=0-payAmount*1;
			  document.getElementById("span8").innerHTML="-"+amountFormat(payAmount1);
			  
		  }else{
		  	  document.getElementById("span8").innerHTML=amountFormat(payAmount);
		  }  */
		  document.getElementById("payAmount").value=payAmount;
		  var myForm = document.getElementById("fm");
		  var rebateTotalAmount=document.getElementById("rebateAmount").value;
			var lineTotalAmount=0;
			var lineTotalPrice=0;
			var materialId = document.getElementsByName("materialId");
			for(var i=0; i<materialId.length; i++){
				var subStr = materialId[i].value;
				//if(obj1.id.length>=6&&obj1.id.substring(0,6)=="amount"){
					//var subStr = obj1.id.substring(6,obj1.id.length);
					var value1 = parseInt(document.getElementById("reserveAmount"+subStr).value, 10);
					var value2 = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);
					var heji = value1*value2;
					var lineAmount=0;
					var linePrice=0;
					//var strLineAmount="0";
					//var strLinePrice="0";
					if(value1>0){
						 lineAmount=(heji*rebateTotalAmount)/totalPrice;
						 linePrice=(heji-(heji*rebateTotalAmount)/totalPrice);
						 //strLineAmount=amountFormat(lineAmount)+"";
						 //strLinePrice=amountFormat(linePrice)+"";
					}
					document.getElementById("lineRebateAmount"+subStr).innerHTML=lineAmount==0?0:amountFormat(lineAmount);
					document.getElementById("lineRebateTotalPrice"+subStr).innerHTML=linePrice==0?0:amountFormat(linePrice);
					document.getElementById("lineRebateAmountHid"+subStr).value=lineAmount;
					//alert("lineAmount="+lineAmount);
					//alert("linePrice="+linePrice);
					lineTotalAmount+=lineAmount;
					lineTotalPrice+=linePrice;
				//}
			}
			//屏蔽返利合计金额
			//document.getElementById("lineRebate").innerHTML=amountFormat(lineTotalAmount);
			document.getElementById("lineRebateTotal").innerHTML=amountFormat(lineTotalPrice);
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
		obj1.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
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
		
		var obj7 = document.getElementById("credit_amount");//信用额度
	obj7.innerHTML = amountFormat(json.returnValue3);	
	var obj8 = document.getElementById("fine_amount");//账户余额-信用额度
	obj8.innerHTML = amountFormat(json.returnValue4);
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
		//obj.options[0] = new Option("-请选择-", "");
		for(var i=0;i<json.priceList.length;i++){
			if(json.priceList[i].IS_DEFAULT == '<%=Constant.IF_TYPE_YES%>'){
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC + "*", json.priceList[i].PRICE_ID + "");
			}
			else{
				obj.options[i] = new Option(json.priceList[i].PRICE_DESC, json.priceList[i].PRICE_ID + "");
			}
			if(json.priceList[i].PRICE_ID == '${map.PRICE_ID}'){
				obj.options[i].selected = true;
			}
		}
		
		/* if(${map.ORDER_TYPE}!=10201003) {
			priceTypeChange() ;
		}  */
		
		//isShowOtherPriceReason();
	}
	
	//价格类型改变
	function priceTypeChange(){	
		var priceId = document.getElementById("priceId").value;
		if(priceId) {
			document.getElementById("queryBtn1").disabled = true ;
			document.getElementById("queryBtn2").disabled = true ;
			document.getElementById("queryBtn3").disabled = true ;
			
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
		//isShowOtherPriceReason();//是否显示使用其他价格原因
		priceTotal();
		
		document.getElementById("queryBtn1").disabled = false ;
		document.getElementById("queryBtn2").disabled = false ;
		document.getElementById("queryBtn3").disabled = false ;
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
		var erpCode = document.getElementById("erpCode").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode,priceId:priceId,entityCode:erpCode}); 
	}
	
	function addRow(json){	
		if(parseFloat(json.info.PRICE, 10) < <%=Constant.MATERIAL_PRICE_MAX%>){	
			var timeValue = json.info.MATERIAL_ID;
			var newRow = document.getElementById("tbody1").insertRow();
			newRow.className  = "table_list_row1";
			newCell = newRow.insertCell(0);//物料编号
			newCell.align = "center";
			var tempHtml = json.info.MATERIAL_CODE;
			tempHtml += "<input type='hidden' id='detailId"+timeValue+"' name='detailId' value=''/>";
			tempHtml += "<input type='hidden' name='ver' value='0'/>";
			tempHtml += "<input type='hidden' id='orderDetailId"+timeValue+"' name='orderDetailId' value=''/>";
			tempHtml += "<input type='hidden' id='materialId"+timeValue+"' name='materialId' value='"+timeValue+"'/>";
			tempHtml += "<input type='hidden' id='reqAmount"+timeValue+"' name='reqAmount' value='0'/>";
			tempHtml += "<input type='hidden' id='totalPrice"+timeValue+"' name='totalPrice' value=''/>";
			tempHtml += "<input type='hidden' id='discountSPrice"+timeValue+"' name='discountSPrice' value=''/>";
			tempHtml += "<input type='hidden' id='discountPrice"+timeValue+"' name='discountPrice' value=''/>";
			tempHtml += "<input type='hidden' id='batchNo"+timeValue+"' name='batchNo' value=''/>";
			tempHtml += "<input type='hidden' id='specialBatchNo"+timeValue+"' name='specialBatchNo' value=''/>";
			tempHtml += "<input type='hidden' id='initNo"+timeValue+"' name='initNo' value=''/>";
			tempHtml += "<input type='hidden' id='unCheckGeneralAmount"+timeValue+"' name='unCheckGeneralAmount' value='"+json.info.GENERAL_AMOUNT+"'/>";
			newCell.innerHTML = tempHtml;
			newCell = newRow.insertCell(1);//物料名称
			newCell.innerHTML = json.info.MATERIAL_NAME;
			newCell = newRow.insertCell(2);//提报数量
			newCell.innerHTML = "0";
			newCell = newRow.insertCell(3);//保留数量
			newCell.innerHTML = "<input type='text' class='mini_txt' id='reserveAmount"+timeValue+"' name='reserveAmount' value='0' ><a href='#' onclick='patchNoSelect(\""+json.info.MATERIAL_ID+"\")' readonly='readonly' datatype='0,is_digit,6'><font color='red'>*</font>[审核]</a>";
			newCell = newRow.insertCell(4);//单价
			newCell.innerHTML = "<input type='text' id='singlePrice"+timeValue+"' name='singlePrice' value='"+json.info.PRICE+"' size='8' style='text-align:right'  onchange='priceTotal();' datatype='0,isMoney,10'/><font color='red'>*</font><span id='priceDesc"+timeValue+"'></span>";
			newCell = newRow.insertCell(5);//金额
			newCell.innerHTML = "<span id='price2"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(6);//折扣率
			newCell.innerHTML = "<input type='text' class='mini_txt' id='discountRate"+timeValue+"' name='discountRate' value='0' onchange='priceTotal();' datatype='0,is_double,6' decimal='2'><font color='red'>*</font>";
			newCell = newRow.insertCell(7);//折扣后单价
			newCell.innerHTML = "<span id='price3"+timeValue+"'></span>";
			newCell = newRow.insertCell(8);//折扣额
			newCell.innerHTML = "<span id='price4"+timeValue+"'></span>";
			newCell = newRow.insertCell(9);//可用库存
			newCell.innerHTML = "<span id='stock"+timeValue+"'>0</span>";
			newCell = newRow.insertCell(10);//可用库存总量
			newCell.innerHTML = "<span id='avaPrice"+timeValue+"'>"+json.info.AVA_STOCK+"</span>";
			newCell = newRow.insertCell(11);//未满足常规订单
			newCell.innerHTML = json.info.GENERAL_AMOUNT;
			newCell = newRow.insertCell(12);//对应批次
			newCell.innerHTML = "<span id='batch"+timeValue+"'></span>";
			newCell = newRow.insertCell(13);//操作
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
		var materialId = document.getElementsByName("materialId");
		for (var i=0; i<materialId.length; i++){  
			ids += materialId[i].value + ","; 
		} 	
		ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
		var areaId = document.getElementById("areaGet").value;	
		
		var productId = "${map.PRODUCT_COMBO_ID}" ;
		showMaterialByAreaId('materialCode','','false',areaId,ids,productId);
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
			//document.getElementById("batch"+subStr).innerHTML = "";//对应批次显示  
		} 
		priceTotal();	
		getStock();
	}
	
	//批次号选择
	function patchNoSelect(materalId,reserveActualAmount){
		var wareHouseId = document.getElementById("wareHouseId").value;
		var batchNo = document.getElementById("batchNo"+materalId).value;
		var reserveAmount = document.getElementById("reserveAmount"+materalId).value;
		var orderType = '${map.ORDER_TYPE}';
		var specialBatchNo = document.getElementById("specialBatchNo"+materalId).value;
		var initHouseId = '${map.WAREHOUSE_ID}'
		var initNo = (initHouseId != "" && initHouseId == wareHouseId) ? document.getElementById("initNo"+materalId).value : "";
		var reqAmount = document.getElementById("reqAmount"+materalId).value;
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/patchNoSelect.do?wareHouseId='+wareHouseId+'&materalId='+materalId+'&batchNo='+batchNo+'&amount='+reserveAmount+'&orderType='+orderType+'&specialBatchNo='+specialBatchNo+'&initNo='+initNo+'&reqAmount='+reqAmount+'&reserveActualAmount='+reserveActualAmount,700,500);
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
		MyAlert(4);
		if(submitForm('fm')){
			var priceFlag = 1 ;
			document.getElementById("modifyFlag").value = "0";
			var detailId = document.getElementsByName("detailId");
			var materialId = document.getElementsByName("materialId");
			var reqAmount = document.getElementsByName("reqAmount");
			var reserveAmount = document.getElementsByName("reserveAmount");
			//var discountRate = document.getElementsByName("discountRate");
			//var unCheckGeneralAmount = document.getElementsByName("unCheckGeneralAmount");
			var checkGeneral = document.getElementById("checkGeneral").value;
			var orderType = document.getElementById("orderType").value;
			//var ratePara = parseInt(document.getElementById("ratePara").value, 10);
			var singlePrice = document.getElementsByName("singlePrice") ;
			MyAlert(5);
			for(var i=0 ;i< detailId.length; i++){
				var reqValue = reqAmount[i].value;
				var reserveValue = reserveAmount[i].value;
				//var discountRateValue = discountRate[i].value;
				var materialValue = materialId[i].value;
				//var avaStockValue = document.getElementById("avaPrice"+materialValue).innerHTML;
				//var unCheckGeneralValue = unCheckGeneralAmount[i].value;
				var priceDesc = document.getElementById("priceDesc" + materialId[i].value).innerHTML;
				var singleValue = parseFloat(singlePrice[i].value) ;
				if(priceDesc != "" && reserveValue > 0){
					MyAlert("价格未维护物料保留资源数量不能大于0！");
					return;
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
			//var discountValue = document.getElementById("discount").value;
			//var discountTotal = document.getElementById("discountTotalPrice").value;
			//if(parseFloat(discountValue, 10) < parseFloat(discountTotal, 10)) {
			//	MyAlert("折扣额合计不能大于可用折让！");
			//	return;
			//}
			MyAlert(6);
			<%-- var dealerType = document.getElementById("dealerType").value;
			if(dealerType == "<%=Constant.DEALER_TYPE_JSZX%>"){
				var oldPrice = document.getElementById("oldPrice").value;
				var totalPrice = document.getElementById("reqTotalPrice").value;
				if(parseFloat(totalPrice, 10) > parseFloat(oldPrice, 10)) {
					MyAlert("结算中心订单审核资源总价不能大于原发运申请总价！");
					return;
				}
			} --%>
			var reserveTotalAmount = document.getElementById("reserveTotalAmount").value;
			if(reserveTotalAmount == 0){
				MyAlert("资源保留数量合计不能为0！");
				return;
			}
			var returnStr = "";
			//var value1 = document.getElementById("fundType").value;
			//var value2 = document.getElementById("deliveryType").value;
			//var value3 = document.getElementById("priceId").value;
			if(priceFlag == 0) {
				returnStr += "<font color='red'>存在保留数量大于0，且保留资源单价为0的资源</font>，是否确认保存？" ;
			} else {
				returnStr += "是否确认保存？";
			}
			MyConfirm(returnStr,putForword);
		}
	}
	
	function checkCurStock(){
		MyAlert(3);
		if(${map.ORDER_TYPE }==<%=Constant.ORDER_TYPE_01%>){
			toSaveCheck();
		}else{
			var orderType='${map.ORDER_TYPE }';
			var warehouseId=document.getElementById("warehouseId").value;
			//MyAlert("orderType=="+orderType);
			makeSameFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderStockCheck.json?orderType="+orderType+"&warehouseId="+warehouseId,showStockValue,'fm');
	}
		
	}
	
	function showStockValue(json){
		if(json.Flag == '1'){
			MyAlert("可用库存总量不能满足订单数量要求！");
			return;
		}else{
			toSaveCheck();
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
		var orderType=document.getElementById("order_type").value;
		for (var i=0; i<materialId.length; i++){  
			id = materialId[i].value;
			var reserveAmount = document.getElementById("reserveAmount" + id);//保留数量
			var unentity=document.getElementById("unentity" + id);//未满足常规订单数量
			if(reserveAmount.value == 0){
				//var avaPrice = document.getElementById("avaPrice" + id);//可用库存
				var stock = document.getElementById("stock" + id);//可用库存
				//MyAlert(stock.value);
				var reqAmount = document.getElementById("reqAmount" + id).value;//申请数量
				//var subAmount = Number(avaPrice.innerHTML) - Number(reqAmount);
				var stockAmount=Number(stock.innerHTML) - Number(reqAmount);
				//MyAlert(Number(stock.innerHTML));
				//MyAlert(Number(unentity.innerHTML));
				//MyAlert(Number(stock.innerHTML)>=Number(unentity.innerHTML));
				if(orderType!=<%=Constant.ORDER_TYPE_01%>){ //非 常规订单
				if(Number(stock.innerHTML)>(Number(unentity.innerHTML)+Number(reqAmount))){	 //库存>未满足常规订单+申请数量；
					//MyAlert(10);	
					if(stockAmount >= 0){
						reserveAmount.readOnly = false;
						reserveAmount.value = Number(reqAmount);
						//reserveAmount.readOnly = true;
						
						stock.innerHTML = stockAmount;				
						//avaPrice.innerHTML = subAmount;				
						//批次
						var returnStr1 = "1-"+Number(reqAmount)+"/";
				        document.getElementById("batchNo"+ id).value = returnStr1;
				        //document.getElementById("batch" + id).innerHTML = returnStr1;					
					}else{
						if(Number(stock.innerHTML)>0){
							reserveAmount.readOnly = false;
							reserveAmount.value = Number(stock.innerHTML);
							//reserveAmount.readOnly = true;
							//批次
							//var returnStr1 = "1-"+Number(avaPrice.innerHTML)+"/";
					        document.getElementById("batchNo"+ id).value = returnStr1;
					        //document.getElementById("batch" + id).innerHTML = returnStr1;
					        stock.innerHTML = 0;
							//avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML);   
						}else{
							reserveAmount.readOnly = false;
							reserveAmount.value = '0';
							//reserveAmount.readOnly = true;
							// stock.innerHTML = 0;
							//avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML); 
						}			        						
					}
				}
				//申请数量>库存-未满足常规订单，且库存>未满足常规订单 
				else if(Number(reqAmount)>(Number(stock.innerHTML)-Number(unentity.innerHTML)) && Number(stock.innerHTML)>Number(unentity.innerHTML)){
					//MyAlert(20);
						reserveAmount.readOnly = false;
						reserveAmount.value = Number(stock.innerHTML)-Number(unentity.innerHTML);
						//reserveAmount.readOnly = true;
						
						stock.innerHTML = Number(unentity.innerHTML);				
						//avaPrice.innerHTML = subAmount;				
						//批次
						var returnStr1 = "1-"+Number(reqAmount)+"/";
				        document.getElementById("batchNo"+ id).value = returnStr1;
				 }
				}else{
				////常规订单
					if(stockAmount >= 0){
						reserveAmount.readOnly = false;
						reserveAmount.value = Number(reqAmount);
						//reserveAmount.readOnly = true;
						
						stock.innerHTML = stockAmount;				
						//avaPrice.innerHTML = subAmount;				
						//批次
						var returnStr1 = "1-"+Number(reqAmount)+"/";
				        document.getElementById("batchNo"+ id).value = returnStr1;
				        //document.getElementById("batch" + id).innerHTML = returnStr1;					
					}else{
						if(Number(stock.innerHTML)>0){
							reserveAmount.readOnly = false;
							reserveAmount.value = Number(stock.innerHTML);
							//reserveAmount.readOnly = true;
							//批次
							//var returnStr1 = "1-"+Number(avaPrice.innerHTML)+"/";
					        document.getElementById("batchNo"+ id).value = returnStr1;
					        //document.getElementById("batch" + id).innerHTML = returnStr1;
					        stock.innerHTML = 0;
							//avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML);   
						}else{
							reserveAmount.readOnly = false;
							reserveAmount.value = '0';
							//reserveAmount.readOnly = true;
							// stock.innerHTML = 0;
							//avaPrice.innerHTML = Number(avaPrice.innerHTML)- Number(stock.innerHTML); 
						}			        						
					}
				}
				///常规订单end
			}else{
				//getStock();
				//yinshunhui start
				//var avaPrice = document.getElementById("avaPrice" + id);//库存总量
				//var stock = document.getElementById("stock" + id);//可用库存
				//var curReserveAmount = document.getElementById("reserveAmount" + id);//保留数量
				//MyAlert(stock.innerHTML);
				//if(Number(stock.innerHTML)<0){
					//MyAlert(123);
					//if(Number(stock.innerHTML)+Number(curReserveAmount.value)>0){
						//curReserveAmount.value=Number(stock.innerHTML)+Number(curReserveAmount.value);
						//avaPrice.innerHTML=0;
						//stock.innerHTML=0
					//}else{
						//curReserveAmount.value=0;
						//avaPrice.innerHTML=Number(stock.innerHTML)+Number(curReserveAmount.value);
						//stock.innerHTML=Number(stock.innerHTML)+Number(curReserveAmount.value);
					//}
				//}
				
				//yinshunhui end
				
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
		disableBtn($("queryBtn2"));
		disableBtn($("queryBtn3"));
		
		if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_03%>) {
			disableBtn($("orderBack"));
		}
		
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveCancel.json",showCancelValue,'fm');
	}
	
	function showCancelValue(json){
		if(json.returnValue == '1'){
			MyAlert("订单取消成功！");
			destoryLock();
			history.back();
			//$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveQueryPre.do';
	 		//$('fm').submit();
		}else{
			MyAlert("数据已被修改,取消失败！");
			useableBtn($("queryBtn1"));
			useableBtn($("queryBtn2"));
			useableBtn($("queryBtn3"));
			
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
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserve/compareToAction.json" ;
			
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
		//验证常规订单资源不足
		var id = "";//已选中的物料id
		var materialId = document.getElementsByName("materialId");
		MyAlert(1);
		for (var i=0; i<materialId.length; i++){  
				id = materialId[i].value;
				var reserveAmount=document.getElementById("reserveAmount" + id).value; //保留数量
				var stock = document.getElementById("stockAmount" + id).value;//数据库取到可用库存
				var amount=parseInt(stock-reserveAmount); 
				//var unCheckGeneralAmount=document.getElementById("unCheckGeneralAmount" + id).value;
				//MyAlert(stock);
				if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_01%>&&reserveAmount!=0) {
					if(amount<0){
						MyAlert("第"+(i+1)+"行可用库存不足！！！")
						return;
					}
				
				}
				if(${map.ORDER_TYPE } != <%=Constant.ORDER_TYPE_01%>&&reserveAmount!=0){
					if(amount<0){
						MyAlert("第"+(i+1)+"行可用库存不足！！！")
						return;
					}
				}
		}
		MyAlert(2);
		checkCurStock();
	}
	function compartToAction(json){
		if(json.Flag.COUNTS<=0){
			MyAlert("ERP和DMS的资金类型不匹配！！！")
			return;
		}
		//验证常规订单资源不足
		var id = "";//已选中的物料id
		var materialId = document.getElementsByName("materialId");
		for (var i=0; i<materialId.length; i++){  
				id = materialId[i].value;
				var reserveAmount=document.getElementById("reserveAmount" + id).value; //保留数量
				var stock = document.getElementById("stockAmount" + id).value;//数据库取到可用库存
				var amount=parseInt(stock-reserveAmount); 
				//MyAlert(amount);
				//var unCheckGeneralAmount=document.getElementById("unCheckGeneralAmount" + id).value;
				//MyAlert(stock);
				if(${map.ORDER_TYPE } == <%=Constant.ORDER_TYPE_01%>&&reserveAmount!=0) {
					if(amount<0){
						MyAlert("第"+(i+1)+"行可用库存不足！！！")
						return;
					}
				
				}
				if(${map.ORDER_TYPE } != <%=Constant.ORDER_TYPE_01%>&&reserveAmount!=0){
					if(amount<0){
						MyAlert("第"+(i+1)+"行可用库存不足！！！")
						return;
					}
					if(amount<unCheckGeneralAmount){
						MyAlert("第"+(i+1)+"行可用库存小于未满足常规订单！！！")
						return;
					}
				}
		}		
		
		if(parseInt(${parReserve}) <= 0) {
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserve/compareToAction.json" ;
			makeFormCall(url, retPriceResult, "fm") ;
		} else {
		
				checkCurStock();
				//toSaveCheck() ;
		}
	}
	function retPriceResult(json) {
		var flag = json.priComparaFlag ;
		
		if(flag == 1 || flag == 2) {
			checkCurStock();
			//toSaveCheck() ;
		} else if(flag == -1) {
			MyAlert("操作失败：结算中心模式不允许保留资源后的订单金额大于之前金额！") ;
		
			return false ;
		}
	}
	function look_un_amount(material_id){
		var url="<%=contextPath%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/resourceQueryDetail.do?material_id="+material_id;
		OpenHtmlWindow(url,700,500);
	}
	 //是否使用返利
	  function changeRebate(value){
		  if(value==<%=Constant.IF_TYPE_YES%>){
			  	document.getElementById("rebate_discount").style.display = "inline";
				document.getElementById("rebate_account").style.display = "inline";
				document.getElementById("rebate_discount_amount").style.display = "inline";
				document.getElementById("rebate_account_amount").style.display = "inline";
				var dealerId=document.getElementById("dealerId").value;
			  	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getRebateAmount.json";
				makeCall(url,getRebateAmount,{dealerId:dealerId});
		  }else{
			  	document.getElementById("rebateAmount").value="0";
			  	document.getElementById("rebate_discount").style.display = "none";
				document.getElementById("rebate_account").style.display = "none";
				document.getElementById("rebate_discount_amount").style.display = "none";
				document.getElementById("rebate_account_amount").style.display = "none";
				//document.getElementById("rebate_amount").style.display = "none";
				 payAmount(); //切换是否返利 计算应付金额
		  }
		  //parAmount(); //切换是否返利 冲计算应付金额
		  
	  }
	  
	  function getRebateAmount(json){
		  //MyAlert(json.rebateDiscount);
		  //if(json.rebateList.length==1){
			  var totalAmount=document.getElementById("reserveTotalPrice").value;
			 //MyAlert("===totalAmount=="+totalAmount)
			  if(totalAmount==null){
				  totalAmount=0;
			  }
			  var rebateDiscountAmount=totalAmount*json.rebateDiscount;//返利金额=订单总价*返利折扣
			  //MyAlert("---rebateAmount:"+json.rebateAmount);
			  if(json.rebateAmount>0){
				  //MyAlert("rebateDiscountAmount:"+rebateDiscountAmount);
				  if(rebateDiscountAmount<json.rebateAmount){
					 //MyAlert(rebateDiscountAmount);
					  document.getElementById("rebate_discount_amount").innerHTML=amountFormat(rebateDiscountAmount+0);
					  //MyAlert("rebateDiscountAmount11="+rebateDiscountAmount);
					  document.getElementById("rebateAmount").value=rebateDiscountAmount;
					  //MyAlert("rebateDiscountAmount33="+rebateDiscountAmount);
				  }else{
					 // MyAlert("rebateDiscountAmount22="+rebateDiscountAmount);
					  document.getElementById("rebate_discount_amount").innerHTML=amountFormat(json.rebateAmount+0);
					  document.getElementById("rebateAmount").value=json.rebateAmount;
				  }
			  }else{
				  document.getElementById("rebate_discount_amount").innerHTML=0;
				  document.getElementById("rebateAmount").value=0;
			  }
			  //MyAlert(999);
			  document.getElementById("rebate_account_amount").innerHTML=amountFormat(json.rebateAmount+0);
			 /*  var totalPrice=document.getElementById("total").value;
			  var rebateAmount=document.getElementById("rebateAmount").value;
			  MyAlert(totalPrice+"--"+rebateAmount);
			  var parAmount=totalPrice-rebateAmount;
			  document.getElementById("span8").innerHTML=amountFormat(round(parAmount,2)); */
			  payAmount(); //切换是否返利 冲计算应付金额
		  /* }else if(json.list.length<1){
			  MyAlert('返利账户未维护！') ; 
		  }else{
			  MyAlert('返利账户维护异常！') ; 
		  } */
	  }
	  
</script>
</body>
</html>