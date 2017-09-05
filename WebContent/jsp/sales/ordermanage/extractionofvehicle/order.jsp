<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
/**
 * 获取账户余额和可用用余额
 */
function loadAccountInfo() {
	
	var dealerId = document.getElementById("dealerId").value;
	var yieldlyId = document.getElementById("yieldId").value;
	var finType = document.getElementById("finType").value;
	
	var url = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/loadDealerAreaAccountInfo.json";

	makeCall(url, showAccount, {dealerId: dealerId, yieldlyId: yieldlyId, finType: finType});
	
}
/**
 * 显示账户数据
 */
function showAccount(json) {
	
	if(json.info) 
	{
		document.getElementById("yfin_total_span").innerHTML = amountFormat(json.info.AMOUNT);
		document.getElementById("yfin_total").value = json.info.AMOUNT;
		
		document.getElementById("fin_useful_span").innerHTML = json.info.USEFUL_AMOUNT;
		document.getElementById("fin_useful").value = json.info.USEFUL_AMOUNT;
	}
	
}
	function checkZY(zy_count,sub_count,check_count,x_count){
		if(zy_count<x_count.value){
			x_count.value="";
			MyAlert("审核数不能大于资源预留数！");
			return ;
		}
		if(sub_count<x_count.value){
			x_count.value="";
			MyAlert("审核数不能大于申请数量！");
			return ;
		}
	}
	//设定资金
	function checkAccCount(id) 
	{
		var cpObj = document.getElementById('check_price'+id);
		var price = cpObj.value; 
		var returnValue = isMoney(cpObj);
		if(returnValue != true) {
			MyAlert("审核价格" + returnValue); cpObj.select(); return false;
		}
		
		var priceObj = document.getElementsByName("check_price");
		for(var i=0;i<priceObj.length;i++) {
			priceObj[i].value = price;
		}
		
		var amobj = document.getElementsByName('materialId');
		
		for(var i=0;i<amobj.length;i++) {
			if(!countMaterial(amobj[i].value)) return;
		}
		
		if(!countOrderAccountInit()) return;
	}
	function countMaterial(id) {
		try
		{
			// 分配数量
			var sel = document.getElementById('invoNum' + id).value; 
			// 审核价
			var checkPrice = document.getElementById("check_price"+id).value; 
			// 折扣率
			var disValue = document.getElementById("dis_value"+id).value;
			// 单个折扣额
			var singleDisPrice = checkPrice * (disValue/100);

			var returnValue = isMoney(document.getElementById("check_price"+id));
			if(returnValue != true) {
				fobj = document.getElementById("check_price"+id); MyUnCloseAlert("审核价格" + returnValue, txtfocus);
				return false;
			}

			// 统计单个合计
			var singleAllDisPrice = singleDisPrice * sel;	// 折扣总额
			var singlePrice = checkPrice * ( 1 - (disValue/100) );	// 折后单价
			var total = singlePrice * sel;

			document.getElementById("discount_value"+id).value = singlePrice;
			document.getElementById("discount_value_span"+id).innerHTML = amountFormat(singlePrice);
			
			document.getElementById("discount_price"+id).value = singleAllDisPrice;
			document.getElementById("discount_price_span"+id).innerHTML = amountFormat(singleAllDisPrice);
			
			document.getElementById("distotal" + id).value = total;
			document.getElementById("distotal_span" + id).innerHTML = amountFormat(total);

			return true;
		}
		catch(e)
		{
			return false;
		}
	}
	function countOrderAccountInit() 
	{
		try
		{	
			// 合计分配的总数量
			var checkAmountObjs = document.getElementsByName("invoNum");
			var sPrice = document.getElementsByName('standard_price'); 
			var checkAmountTotal = 0, priceTotal = 0; 
			for(var i=0;i<checkAmountObjs.length;i++) {
				checkAmountTotal += parseInt(checkAmountObjs[i].value); 
				priceTotal += parseFloat(sPrice[i].value) * parseInt(checkAmountObjs[i].value);
			}
			document.getElementById("orderTotalPrice").innerHTML = amountFormat(priceTotal);
			document.getElementById("orderTotal").value = priceTotal;
			// 合计折扣额
			var discountPriceObjs = document.getElementsByName("discount_price");
			var discountPriceTotal = 0.00; 
			for(i=0;i<discountPriceObjs.length;i++) {
				discountPriceTotal += parseFloat(discountPriceObjs[i].value);
			}
			document.getElementById("totalDiscountPrice_span").innerHTML = amountFormat(discountPriceTotal);
			// 合计
			var distotalObjs = document.getElementsByName("distotal");
			var totalPriceTotal = 0.00; 
			for(i=0;i<distotalObjs.length;i++) {
				totalPriceTotal += parseFloat(distotalObjs[i].value);
			}
			document.getElementById("totalPrice_span").innerHTML = amountFormat(totalPriceTotal);
			document.getElementById("order_total_price_span").innerHTML = amountFormat(totalPriceTotal);
			hjCount();
			
			return true;
		}
		catch(e)
		{
			return false;
		}
	}
	//审核通过
	function save(_type){
		MyConfirm("确认修改发运申请审核数量！",saveSales);	
	}
	function saveSales()
	{ 
		disabledButton(["sendbtn","backbtn"],true);
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/saveCheckOtherMain.json",saveSalesBack,'fm','queryBtn'); 
	}

	function saveSalesBack(json)
	{
		if(json.returnValue == 1)
		{
			makeNomalFormCall("<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/saveCheckMainOtherPro.json",saveSalesProBack,'fm','queryBtn'); 
		}else
		{
			disabledButton(["sendbtn","backbtn"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function saveSalesProBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/orderSalesSpecialInit.do";
			fm.submit();
		}else
		{
			disabledButton(["sendbtn","backbtn"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function checkNum(handle){
		var pattern = /^(\d){0,100}$/; 
	    if (!pattern.exec(handle.value)) 
	    {
	    	handle.value='';
	    	MyAlert( "请输入正整数或0.");
	    }
	}
</script>
<title>提车单资源审核</title>
</head>
<body onload="countOrderAccountInit();">
	<div class=navigation>
		<img src="<%=request.getContextPath()%>/img/nav.gif">
		&nbsp;当前位置： 订单管理 &gt; 发运申请管理 &gt; 发运申请资源查看
	</div>
	<form method="POST" name="fm" id="fm">
     	<input type="hidden" name="yieldId" id="yieldId" value="${orderMap.AREA_ID }" /> 
     	<input type="hidden" name="orderId" id="orderId" value="${orderMap.ORDER_ID }" />
     	<input type="hidden" name="dealerId" id="dealerId" value="${orderMap.DEALER_ID }"/>
     	<input type="hidden" name="finType" id="finType" value="${orderMap.FUND_TYPE_ID }"/>
     	<input type="hidden" name="process_type" id="process_type"  value="${process_tpye }"/>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		     <!--  <th nowrap="nowrap">车系</th>
		      <th nowrap="nowrap">车型</th> -->
		      <th nowrap="nowrap">车系</th>
		      <th nowrap="nowrap">车型</th>
		      <th nowrap="nowrap">配置</th>
		      <th nowrap="nowrap">颜色</th>
		      <th nowrap="nowrap">申请数量</th>
			  <th nowrap="nowrap">审核数量</th>
		      <th nowrap="nowrap" id="singleTdTitle">市场零售价</th>
		      <th style="display: none"> nowrap="nowrap" id="singleTdTitle">审核价</th>
		      <th nowrap="nowrap" id="span7">基本折扣率</th>
		      <th nowrap="nowrap" id="span8">启票价</th>
		      <th nowrap="nowrap" id="span9">基本折扣额</th>
		      <th nowrap="nowrap" id="totalTdTitle">启票金额</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${materialList }" var="list" varStatus="status">
    				<tr class="table_list_row2">
    					<%-- <td>
    						${list.SERIES_NAME }
    						<input type='hidden' id='orderDetailId${list.MATERIAL_ID }' name='orderDetailId' value='${list.DETAIL_ID }' />
    						<input type='hidden' id='materialId${list.MATERIAL_ID }' name='materialId' value='${list.MATERIAL_ID }' />
    					</td>
    					<td>${list.MODEL_NAME }</td> --%>
    					<td>
    						<input type='hidden' id='orderDetailId${list.MATERIAL_ID }' name='orderDetailId' value='${list.DETAIL_ID }' />
    						<input type='hidden' id='materialId${list.MATERIAL_ID }' name='materialId' value='${list.MATERIAL_ID }' />
    						${list.SERIES_NAME }
    					</td>
    					<td>${list.MODEL_NAME }</td>
    					<td>${list.PACKAGE_NAME }</td>
    					<td>${list.COLOR_NAME }</td>
    					<td>${list.ORDER_AMOUNT }<input type="hidden" name="subNum" id="subNum${list.MATERIAL_ID }" value="${list.ORDER_AMOUNT }" /></td>
    					<td>${list.CHECK_AMOUNT }<input type="hidden" name="invoNum" id="invoNum${list.MATERIAL_ID }" value="${list.CHECK_AMOUNT }" /></td>
    					<td>
    						<span id='standard_price_span'>
    							<script>document.write(amountFormat(${list.C_PRICE }));</script>
    						</span>
    						<input id='standard_price${list.MATERIAL_ID}' type='hidden' name='standard_price' value='${list.C_PRICE}'>
    					</td>
    					<td style="display: none">
    						<c:choose>
    							<c:when test="${list.CHK_PRICE == 0}">
    								<input id='check_price${list.MATERIAL_ID}' type='text' size='5' name='check_price'  class='middle_txt' style='width:50px;' onfocus='this.select()' value='${list.C_PRICE}' onchange="checkAccCount('${list.MATERIAL_ID}')">
    							</c:when>
    							<c:otherwise>
    								<input id='check_price${list.MATERIAL_ID}' type='text' size='5' name='check_price'  class='middle_txt' style='width:50px;' onfocus='this.select()' value='${list.CHK_PRICE}' onchange="checkAccCount('${list.MATERIAL_ID}')">
    							</c:otherwise>
    						</c:choose>
    					</td>
    					<td>
    						<span name='dis_value_span' id='dis_value_span${status.index }'>${list.DISCOUNT_RATE}%</span>
    						<input id='dis_value${list.MATERIAL_ID}' type='hidden' name='dis_value' value='${list.DISCOUNT_RATE}'/>
    						<input type="hidden" name="disRate" value="${list.DISCOUNT_RATE }"/>
    						<input type="hidden" name="baseDisRate" value="${list.DISCOUNT_RATE }"/>
    					</td>
    					<td>
    						<span id='discount_value_span${list.MATERIAL_ID}'>
    							<script>document.write(amountFormat(${list.DISCOUNT_S_PRICE }));</script>
    						</span>
    						<input type='hidden' id='discount_value${list.MATERIAL_ID}' name='discount_value' value='${list.DISCOUNT_S_PRICE }' />
    					</td>
    					<td>
    						<span id='discount_price_span${list.MATERIAL_ID}' >
    							<script>document.write(amountFormat(${list.DISCOUNT_PRICE }));</script>
    						</span>
							<input type='hidden' id='discount_price${list.MATERIAL_ID}' name='discount_price' value='${list.DISCOUNT_PRICE }' />
    					</td>
    					<td>
    						<span id='distotal_span${list.MATERIAL_ID}'>
    						<script>document.write(amountFormat(${list.TOTAL_PRICE }));</script>
    						</span>
							<input type='hidden' id='distotal${list.MATERIAL_ID}' name='distotal' value='${list.TOTAL_PRICE }' />
    					</td>
    				</tr>
    			</c:forEach>
    		</tbody>
		     <tr class="table_list_row1">
		      <td align="right" nowrap="nowrap"  >
		      		<strong>合计： </strong>
		      </td>
			  <td align="center" nowrap="nowrap" >&nbsp;</td>
			  <td align="center" nowrap="nowrap" >&nbsp;</td>
		      <td align="center" nowrap="nowrap" >&nbsp;</td>
		      <td>&nbsp;</td>
		      <td align="center" nowrap="nowrap">
		      		<span id='totalSubAmount_span' title="提报总数">
		      			
		      		</span> 
		      		<span style="display: none" id='totalCheckedAmount_span_have' title="已审核总数">
			  			
			  		</span>
		      </td>
			  <td align="center" nowrap="nowrap" >
			  		<span id='totalCheckedAmount_span' title="审核总数">
			  			
			  		</span>
			  </td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td style="display: none">&nbsp;</td>
		      <td align="center" nowrap="nowrap">
		      		<span id='totalDiscountPrice_span' title="折扣额">0</span> 
		      </td>
		      <td align="center" nowrap="nowrap">
		      		<span id='totalPrice_span' title="合计">0</span> 
		      </td>
		    </tr>
  		</table>	
	  <TABLE class=table_query align=center style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif"> <a href="javascript:tabDisplayControl('moneyTable')">资金信息</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
	  <TABLE class=table_query align=center id="moneyTable">
	  	<colgroup>
	  		<col align="right" width="100"/>
	  		<col align="left"/>
	  		<col align="right" width="100"/>
	  		<col align="left"/>
	  		<col align="right" width="100"/>
	  		<col align="left"/>
	  	</colgroup>
	  	<tr>
	  		<td align="right">批售单号：</td>
			<td align="left"><span id="orderNo_span"><c:out value="${orderMap.S_ORDER_NO }" default="0"></c:out></span></td>
			<td align="right">发运申请号：</td>
			<td align="left"><span id="orderNo_span"><c:out value="${orderMap.ORDER_NO }" default="0"></c:out></span></td>
			<td align="right">订单类型：</td>
			<td align="left"><span id="orderType_span">${orderMap.ORDER_TYPE_NAME }</span></td>	
		</tr>
		<tr>
			<td align="right">提报时间：</td>
			<td align="left"><span id="orderDate">${orderMap.RAISE_DATE }</span></td>
			<td align="right">期望交货日期：</td>
			<td align="left"><span id="expectDate_span"><c:out value="${orderMap.EXPECT_DATE }"></c:out></span></td>
			<td align="right">期望下线日期：</td>
			<td align="left"><span id="expectLastDate_span">${orderMap.EXPECT_LAST_DATE }</span></td>
<!-- 			<td align="right">预计下线日期：</td> -->
<%-- 			<td align="left"><span id="planLastDate_span">${orderMap.PLAN_LAST_DATE }</span></td> --%>
		</tr>
<!-- 		<tr> -->
<!-- 			<td align="right">预计发运日期：</td> -->
<%-- 			<td align="left"><span id="planSendDate_span"><c:out value="${orderMap.PLAN_SEND_DATE }" ></c:out></span></td> --%>
<!-- 			<td align="right">预计交货日期：</td> -->
<%-- 			<td align="left"><span id="planDeliverDate_span">${orderMap.PLAN_DELIVER_DATE }</span></td> --%>
<!-- 		</tr> -->
		<tr>
			<td align="right">经销商代码：</td>
			<td align="left"><span id="dealer_code_span">${orderMap.DEALER_CODE }</span></td>
			<td align="right">经销商名称：</td>
			<td align="left"><span id="dealer_name_span">${orderMap.DEALER_NAME }</span></td>
			<td align="right">发运方式：</td>
			<td align="left"><span id="delivery_span">${orderMap.DELIVERY_TYPE_NAME }</span></td>
		</tr>
	    <tr>
	      <td align="right">账户剩余金额：</td>
	      <td align="left">
				<span id='yfin_total_span'>0</span> 
				<input type="hidden" name="yfun_total" id="yfin_total" value="0"/>
		  </td>
	      <td align="right"> 付款资金类型：</td>
      		<td align="left">
      			<span id="finType_span">${orderMap.FUND_TYPE_NAME }</span>
       		</td>
	      <td colspan="2">&nbsp;</td>
	    </tr>
	    <%---     
	    <tr>
	    	<td>订单折前总价：</td>
	    	<td>
	    		<span id='orderTotalPrice'>0.00</span>
	    		<input id="orderTotal" type="hidden" value="0">
	    	</td>
	    </tr>
	    --%>
	    <tr>
	      <td align="right">订单折后总价：</td>
	      <td align="left">
	      		<span id='order_total_price_span'>
	      			<fmt:formatNumber value="${orderMap.ORDER_YF_PRICE }" pattern="###,###.##"  minFractionDigits="2"/>
	      		</span> 
	      		<span id="disValueSpan"></span>
	    		<script>document.getElementById('disValueSpan').innerHTML = '(' + document.getElementsByName('disRate')[0].value + '%)';</script>
	      		<input type="hidden" name="order_total_price" id="order_total_price" value="${orderMap.ORDER_PRICE }"/>
	      </td>
	      	<td align="right">零售客户代码：</td>
	      	<td align="left">
	      		<span id='sporadic_customer_code_span'>${orderMap.SPORADIC_CUSTOMER_CODE }</span> 
	      	</td>
			<td align="right">零售客户名称：</td>
			<td align="left">
				<span id='sporadic_customer_name_span'>${orderMap.SPORADIC_CUSTOMER_NAME }</span> 
			</td>
	    </tr>
	  </TABLE>
	    <TABLE class=table_query align=center style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif"> <a href="javascript:tabDisplayControl('moneyTable')">其他信息</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
	  	<table class="table_query" align=center>
    		<colgroup>
		  		<col align="right" width="100"/>
		  		<col align="left"/>
		  		<col align="right" width="100"/>
		  		<col align="left"/>
		  	</colgroup>
			<tr>
				<td align="right">收货方：</td>
				<td align="left">
					<span id="rec_dealer_name_span">
						${orderMap.REC_DEALER_NAME }
					</span>
					<c:if test="${orderMap.IS_HANDOVER == '10041001' }">
						<span id="hander_span" style="color:#3809F7;">(代交车)</span>
					</c:if>
				</td>
				<td align="right"><span id="fleetName_span" style="display:none;">大客户：</span></td>
				<td align="left">
					<span id="fleetName" style="display:none;">
						${orderMap.FLEET_NAME }
					</span>
					<script type="text/javascript">
						if('${orderMap.FLEET_ID }' != 0 || '${orderMap.FLEET_ID }' != null) {
							document.getElementById("fleetName_span").style.display = '';
							document.getElementById("fleetName").style.display = '';
						}
					</script>
				</td>
			</tr>
			<tr>
				<td align="right">运送地点：</td>
				<td align="left">
					<span id="address_span">${orderMap.ADDRESS }</span>
				</td>
				<td align="right">收车经销商：</td>
				<td align="left">
					<span id="rec_unit_span">${orderMap.REC_DEALER_NAME }</span>
				</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left">
					<span id="linkMan">${orderMap.LINK_MAN }</span>
				</td>
				<td align="right">联系电话：</td>
				<td align="left">
					<span id="tel">${orderMap.TEL }</span>
				</td>
			</tr>
			<tr>
				<td align="right">经销商备注：</td>
				<td align="left" colspan="3">
					<span id="remark">${orderMap.ORDER_REMARK }</span>
				</td>
			</tr>
	  </table>	
	  
	  
	  <table class="table_list" style="margin-top: 3px;">
	  	<tr>
	        <th colspan=6 align=left>
	        	<img class=nav src="<%=request.getContextPath()%>/img/subNav.gif" />
	        	返利使用记录
	       	</th>
	      </tr>
	    <tbody>
	      <tr class="table_list_th">
	        <th nowrap="nowrap">批售单号</th>
	        <th nowrap="nowrap">使用金额</th>
	        <th nowrap="nowrap">使用时间</th>
	      </tr>
        </tbody>
	    <tbody id="tbody3">
	    	<c:forEach items="${orderRebList }" var="list" varStatus="m">
	    		<tr class="table_list_row2">
	    			<td nowrap="nowrap">${list.ORDER_NO } </td>
	    			<td nowrap="nowrap"> 
	    				<script type="text/javascript">
	    					document.write(amountFormat('${list.USE_AMOUNT}'));
	    				</script>
	    			</td>
	    			<td nowrap="nowrap">${list.USE_TIME } </td>
	    		</tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	  
	  
	  <table class="table_list" style="margin-top: 3px;">
	  	<tr>
	        <th colspan=5 align=left>
	        	<img class=nav src="<%=request.getContextPath()%>/img/subNav.gif" />
	        	审核记录
	       	</th>
	      </tr>
	    <tbody>
	      <tr class="table_list_th">
	        <th nowrap="nowrap">序号</th>
	        <th nowrap="nowrap">审核日期</th>
	        <th nowrap="nowrap">审核人</th>
	        <th nowrap="nowrap">审核结果</th>
	        <th nowrap="nowrap">审核描述</th>
	      </tr>
        </tbody>
	    <tbody id="tbody3">
	    	<c:forEach items="${resultList }" var="list" varStatus="m">
	    		<tr class="table_list_row2">
	    			<td nowrap="nowrap">${m.index + 1 }</td>
	    			<td nowrap="nowrap">${list.CHK_DATE } </td>
	    			<td nowrap="nowrap">${list.NAME } </td>
	    			<td nowrap="nowrap">
	    				<script type="text/javascript">
	    					document.write(getItemValue('${list.CHECK_STATUS}'));
	    				</script>
	    			</td>
	    			<td nowrap="nowrap">${list.CHECK_DESC } </td>
	    		</tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	  <c:if test="${orderMap != null }">
	  		<script>
	  		loadAccountInfo();
	  		</script>
	  </c:if>
</form>
</body>
</html>
