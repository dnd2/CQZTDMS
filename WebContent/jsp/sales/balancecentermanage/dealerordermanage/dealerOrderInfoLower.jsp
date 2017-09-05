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
	//初始化
	function doInit(){
		sum();
	}
</script>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp; 经销商订单审核</div>
<form method="post" name="fm" id="fm">
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
		<td>
			${orderInfo.NO_CHECK_AMOUNT }
			<input type="hidden" id="order_id" name="order_id" value="${orderInfo.ORDER_ID }">
			<input type="hidden" id="orderNO" name="orderNO" value="${orderInfo.ORDER_NO }">
		</td>
	</tr>
</table>
<br>
<table class="table_list" id="activeTable">
	<tr align="center" class="tabletitle">
		<th>车系</th>
		<th>物料编号</th>
		<th>物料名称</th>
		<th>原始提报数量</th>
		<th>已满足数量</th>
		<th>待审核数量</th>
		<th>库存数量</th>  
		<th>已采购数量</th>
		<th>采购数量</th>  
		<th>操作</th>
	</tr>
	<c:forEach items="${orderDetailList}" var="orderDetailList" varStatus="vstatus">
		<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
			${orderDetailList.SERIES_NAME}
			<input type='hidden' id='materialId${orderDetailList.MATERIAL_ID}' name='materialId${orderDetailList.MATERIAL_ID}' value='${orderDetailList.MATERIAL_ID}'>
			<input type="hidden"  name="detail_id" value="${orderDetailList.DETAIL_ID }" />
			</td>
			<td>${orderDetailList.MATERIAL_CODE}<input type="hidden" name="material_id" value="${orderDetailList.MATERIAL_ID}" /></td>
			<td>${orderDetailList.MATERIAL_NAME}</td>
			<td>${orderDetailList.ORDER_AMOUNT}<input type="hidden" name="orderAmount" value="${orderDetailList.ORDER_AMOUNT}" /></td>
			<td>${orderDetailList.CHECK_AMOUNT}<input type="hidden" name="checkAmount" value="${orderDetailList.CHECK_AMOUNT}" /></td>
			<td>${orderDetailList.NO_CHECK_AMOUNT}<input type="hidden" name="nocheckAmount" value="${orderDetailList.NO_CHECK_AMOUNT}" /></td>
			<td>${orderDetailList.RESOURCE_AMOUNT}<input type="hidden" name="resourceAmount" value="${orderDetailList.RESOURCE_AMOUNT}" /></td>
			<td>${orderDetailList.APPLYED_AMOUNT}<input type="hidden" name="applyedAmount" value="${orderDetailList.APPLYED_AMOUNT}" /></td>
			<td><input type="text" name="buyNO" id="buyNO${orderDetailList.MATERIAL_ID}" value="" class="mini_txt" datatype="1,is_digit,6" onchange="changeBuyNO(this.value);" /></td>
			<td>[<a href="#" onclick="toCheckDetail('${orderDetailList.DETAIL_ID}','${orderDetailList.ORDER_AMOUNT}','${orderDetailList.MATERIAL_ID}');">库存审核</a>]</td>
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
		<td id="applyedAmounts"></td><!-- 已采购合计 -->
		<td id="buyNOs"></td>
		<td>
			<input type="hidden" id="nocheckAmounts_" name="nocheckAmounts_" value="" />
			<input type="hidden" id="applyedAmounts_" name="applyedAmounts_" value="" />
		</td>
	</tr>
</table>
<br>
<table class="table_query" align="center" border="0">
    <tr class= "tabletitle">
      <td align = "right" width="15%">业务范围：</td>
      <td align="left" >
        ${orderInfo.AREA_NAME }
        <input type="hidden" name="area_id" value="${orderInfo.AREA_ID }" />
        <input type="hidden" name="area" value="">
      </td>
    </tr>
  </table>
<table class="table_query">
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
		<input id="tel" name="tel" type="text" class="middle_txt" size="30" maxlength="11" value="${orderInfo.TEL }" datatype="1,is_digit,20" />
		</td>
	</tr>
	<tr>
		<td align="right">改装说明：</td>
		<td align="left"><font style="color: red">${orderInfo.REFIT_REMARK }</font></td>
	</tr>
	<tr>
		<td align="right">备注说明：</td>
		<td align="left">${orderInfo.ORDER_REMARK }</td>
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
		<input class='cssbutton'  name="add22" type="button" onclick="checkSubmit();" value ='审核' />
		<input class='cssbutton'  name="add22" type="button" onclick="checkClose();" value ='关闭' />
		<input class='cssbutton'  name="add232" type="button" onclick="history.back();" value ='返回' />
		<input type="hidden" id="k_dealerId" name="k_dealerId" value="${orderInfo.DEALER_ID }" />
		<input type="hidden" id="d_dealerId" name="d_dealerId" value="${orderInfo.D_DEALER_ID }" />
		<input type="hidden" id="caigouAllNumber" name="caigouAllNumber" value="" />
	 </td>
   </tr>
</table>

	
</form>

<script type="text/javascript">
	function changeBuyNO(value){
		var buyNO = document.getElementsByName("buyNO");					//采购数量
		var buyNOs = 0;
		for(var i=0;i<buyNO.length; i++){
			buyNOs = Number(buyNOs) + Number(buyNO[i].value);
		}
		document.getElementById("buyNOs").innerHTML = buyNOs;
		document.getElementById("caigouAllNumber").value = buyNOs;
	}
	
    function sum(){
		var orderAmount = document.getElementsByName("orderAmount");		//原始提报数量
		var checkAmount = document.getElementsByName("checkAmount");		//已满足数量
		var nocheckAmount = document.getElementsByName("nocheckAmount");	//待审核数量
		var resourceAmount = document.getElementsByName("resourceAmount");	//库存数量
		var applyedAmount = document.getElementsByName("applyedAmount");	//已采购数量
		
		var orderAmounts = 0;
		var checkAmounts = 0;
		var nocheckAmounts = 0;
		var resourceAmounts = 0;
		var applyedAmounts = 0;
		for(var i=0;i<orderAmount.length; i++){
			orderAmounts = Number(orderAmounts) + Number(orderAmount[i].value);
			checkAmounts = Number(checkAmounts) + Number(checkAmount[i].value);
			nocheckAmounts = Number(nocheckAmounts) + Number(nocheckAmount[i].value);
			resourceAmounts = Number(resourceAmounts) + Number(resourceAmount[i].value);
			applyedAmounts = Number(applyedAmounts) + Number(applyedAmount[i].value);
		}
		document.getElementById("orderAmounts").innerHTML = orderAmounts;
		document.getElementById("checkAmounts").innerHTML = checkAmounts;
		document.getElementById("nocheckAmounts").innerHTML = nocheckAmounts;
		document.getElementById("resourceAmounts").innerHTML = resourceAmounts;
		document.getElementById("nocheckAmounts_").value = nocheckAmounts;

		document.getElementById("applyedAmounts").innerHTML = applyedAmounts;
		document.getElementById("applyedAmounts_").value = applyedAmounts;
    }
	
	//detail_id: 订单明细id；no_check_amount：待审核数量；material_id：物料id;
	function toCheckDetail(detail_id,order_amount,material_id){
		var order_id = document.getElementById("order_id").value;
		OpenHtmlWindow('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toCheckDetail.do?detail_id='+detail_id+'&order_amount='+order_amount+'&material_id='+material_id+'&order_id='+order_id,700,500);
	}
	function queryOrderInfo(){
		var areaId = '<%=request.getAttribute("areaId") %>';
		var order_id = document.getElementById("order_id").value;
		showDealerInfo(order_id,areaId);
	}
	function showDealerInfo(order_id,areaId){
		$('fm').action= '<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/toCheck.do?order_id='+order_id+'&areaId='+areaId;
		$('fm').submit();
	}
	//保存校验
	function checkSubmit(){
		if(submitForm('fm')){
			var nocheckAmounts = document.getElementsByName("nocheckAmount");	//待审核数量
			var applyedAmounts = document.getElementsByName("applyedAmount");	//已采购数量
			var buyNOs = document.getElementsByName("buyNO");					//采购数量
			//已采购数量+采购数量  不能大于 “待审核数量”
			for(var i=0;i<nocheckAmounts.length;i++){
				if(Number(applyedAmounts[i].value) + Number(buyNOs[i].value) - Number(nocheckAmounts[i].value) >0){
					MyAlert("已采购数量+采购数量  不能大于 “待审核数量”");
					return;
				}
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
	}
	function showResult(json){
		if(json.returnValue == '2'){
			MyAlert("提交失败！可用余额不足！");
		}else{
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
</script>
</body>
</html>