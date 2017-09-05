<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>常规订单免责初审</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	//免责审核
	function updateRespond(flag){
		var order_id=$("orderId").value;
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/respondFirst.json?order_id="+order_id+"&flag="+flag;
		makeFormCall(url, resResult, "fm") ;
	}
	//免责审核回调函数
	function resResult(json){
		if(json.flag=='1'){
			var week=$("week").value;
			MyAlert("操作成功！！！");
			location.href="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/doRespondFirstInit.do?week="+week;
		}else{
			MyAlert("未知错误，请联系管理员！！！")
		}
	}
</script>
</head>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;常规订单免责初审</div>
<body onload="loadcalendar();initOrderWeek();executeQuery();">
	<form method="post" name="fm" id="fm">
		<input type="hidden" name="dealerId" id="dealerId"/>
		<input type="hidden" name=orderId id="orderId" value="${orderId}"/>
		<input type="hidden" name=week id="week" value="${week}"/>
		<div id="detailDiv" style="overflow:scroll">
	<table class="table_list" width="100%">
		<tr class="cssTable">
			<th align="center" nowrap>经销商代码</th>
			<th align="center" nowrap>经销商名称</th>
			<th align="center" nowrap>车系</th>
			<th align="center" nowrap>销售订单号</th>
			<th align="center" nowrap>物料编号</th>
			<th align="center" width="25%">物料名称</th>
			<th align="center" nowrap>颜色</th>
<!--			<th align="center" nowrap>单价</th>-->
			<th align="center" nowrap>提报数量</th>
			<th align="center" nowrap>已审核数量</th>
			<th align="center" nowrap>已申请数量</th>
			<th align="center" nowrap>免责数量</th>
<!--			<th nowrap="nowrap">折扣率%</th>-->
<!--      		<th nowrap="nowrap">折扣后单价</th>-->
<!--      		<th nowrap="nowrap">折扣额</th>-->
<!--			<th align="center" nowrap>价格合计</th>-->
		</tr>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
			<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td>
					<c:out value="${list.DEALER_CODE}"/>
				</td>
				<td>
					<c:out value="${list.DEALER_SHORTNAME}"/>
					
				</td>
				<td>
					<c:out value="${list.SERIES_NAME}"/>
					<input type="hidden" name="detailId" value="<c:out value="${list.DETAIL_ID}"/>"/>
				</td>
				<td>
					<c:out value="${list.ORDER_NO}"/>
					<input type="hidden" name="orderId" value="<c:out value="${list.ORDER_ID}"/>"/>
				</td>
				<td><c:out value="${list.MATERIAL_CODE}"/><input type="hidden" name="materialId" value="<c:out value="${list.MATERIAL_ID}"/>"/></td>
				<td><c:out value="${list.MATERIAL_NAME}"/></td>
				<td><c:out value="${list.COLOR_NAME}"/></td>
<!--				<td id="priceTd${list.DETAIL_ID}">-->
<!--					<c:out value="${list.SINGLE_PRICE}"/>-->
<!--					<input type="hidden" name="singlePrice" value="<c:out value="${list.SINGLE_PRICE}" />" id="singlePrice${list.DETAIL_ID}"/>-->
<!--					-->
<!--				</td>-->
				
				<td>
					<c:out value="${list.ORDER_AMOUNT}"/>
					<input type="hidden" name="orderAmount" value="<c:out value="${list.ORDER_AMOUNT}"/>"/>
				</td>
				<td>
					<c:out value="${list.CHECK_AMOUNT}"/>
					<input type="hidden" name="checkAmount" value="<c:out value="${list.CHECK_AMOUNT}"/>"/>
				</td>
				<td>
					<c:out value="${list.CALL_AMOUNT}"/>
					<input type="hidden" name="callAmount" value="<c:out value="${list.CALL_AMOUNT}"/>"/>
				</td>
				<td>
					<input type="text" name="respAmount" readonly id="applyAmount<c:out value="${list.DETAIL_ID}"/>" datatype="0,is_digit,6"  size="3" value="${list.RESPOND_AMOUNT}"
					onchange="toChangeNum(document.getElementById('singlePrice${list.DETAIL_ID}').value,<c:out value="${list.DETAIL_ID}"/>,<c:out value="${list.CHECK_AMOUNT}"/>,<c:out value="${list.CALL_AMOUNT}"/>,this.value);" 
				     ></input>
				</td>
				<!-- 折扣率 -->
<!--				<td>-->
<!--					<input type='text' id='discount_rate${list.DETAIL_ID}' name='discount_rate'  class='SearchInput' datatype="0,is_double,6" decimal="2" size='2' maxlength='2' value='0' onchange="priceOnBlue(this);discountRateChange(${list.DETAIL_ID},this.value,document.getElementById('tempPrice${list.DETAIL_ID}').value,document.getElementById('applyAmount${list.DETAIL_ID}').value);" />%-->
<!--				</td>-->
				<!-- 折扣后单价 -->
<!--				<td>-->
<!--					<span id="discount_s_price_${list.DETAIL_ID}"></span>-->
<!--					<input type="hidden" id="discount_s_price${list.DETAIL_ID}" name="discount_s_price${list.DETAIL_ID}" value="" />-->
<!--					<input type="hidden" id="dis_s_price${list.DETAIL_ID}" name="dis_s_price" value="" />-->
<!--				</td>-->
				<!-- 折扣额 -->
<!--				<td>-->
<!--					<span id="discount_price_${list.DETAIL_ID}"></span>-->
<!--					<input type="hidden" id="discount_price${list.DETAIL_ID}" name="discount_price${list.DETAIL_ID}" value="" />-->
<!--					<input type="hidden" id="dis_price${list.DETAIL_ID}" name="dis_price" value="" />-->
<!--				</td>-->
				
<!--				<td>-->
<!--					<span id="<c:out value="${list.DETAIL_ID}"/>">0.00</span>-->
<!--					<input type="hidden" id="orderPriceSum${list.DETAIL_ID}" name="orderPriceSum" value="" />	-->
<!--					<input type="hidden" name="priceListId" value="${list.PRICTLIST_ID}" />-->
<!--				</td>-->
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4">&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td colspan="5">&nbsp;</td>
		</tr>
		</table>
		</div>
		<table class="table_list" width="100%">
		<tr style="width:100%;">
			<td align="left" colspan="">&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<td align="left" style="width:35%">&nbsp;&nbsp;&nbsp;</td>
			<td align="left" style="width:10%"><input name="updateBtn" type=button class="cssbutton" onClick="updateRespond(1);" value="通过"></td>
			<td align="left" style="width:10%"><input name="updateBtn" type=button class="cssbutton" onClick="updateRespond(0);" value="驳回"></td>
			<td align="left" style="width:10%"><input name="cancelBtn" type=button class="cssbutton" onClick="history.back();" value="返回"></td>
			<td align="left" style="width:35%" >&nbsp;&nbsp;&nbsp;</td>
		</tr>
		</table>
</form>
</body>
</html>