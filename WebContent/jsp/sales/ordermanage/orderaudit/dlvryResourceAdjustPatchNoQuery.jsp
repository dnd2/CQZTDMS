<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单资源审核</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 订单资源审核</div>
<form method="post" name="fm" id="fm">
	<table class="table_query">
		<tr>
			<td align="right" width="10%">申请数量：</td>
			<td align="left" width="40%">${reqAmount}</td>
			<td align="right" width="10%">保留数量：</td>
			<td align="left" width="40%">${amount}<input type="hidden" name="initTotalAmount" value="${amount}"> </td>
		</tr>
	</table>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>物料代码</th>
			<th>物料名称</th>
			<th>批次</th>
			<th>原保留数量</th>
			<th>已发运数量</th>
			<th>可用数量</th>
			<th>保留数量</th>
		</tr>
		<c:forEach items="${w_List}" var="list1" varStatus="vstatus">
			<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td>
					${list1.MATERIAL_CODE}
				</td>
				<td>${list1.MATERIAL_NAME}</td>
				<td>${list1.BATCH_NO}</td>
				<td id="initReserve${list1.BATCH_NO}">0</td>
				<td>${list1.DELIVERY_AMOUNT}</td>
				<td id="ava${list1.BATCH_NO}">${list1.AVA_AMOUNT}</td>
				<td>
					<input type="hidden" name="materialId" id="materialId${list1.BATCH_NO}" value="${list1.MATERIAL_ID}">
					<input type="hidden" name="avaAmount" id="avaAmount${list1.BATCH_NO}" value="${list1.AVA_AMOUNT}">
					<input type="hidden" name="initAmount" id="initAmount${list1.BATCH_NO}" value="${list1.AVA_AMOUNT}">
					<input type="hidden" name="deliveryAmount" id="deliveryAmount${list1.BATCH_NO}" value="${list1.DELIVERY_AMOUNT}">
					<input type="hidden" name="batchNo" id="batchNo${list1.BATCH_NO}" value="${list1.BATCH_NO}">
					<input type="text" class="mini_txt" name="reserveAmount" id="reserveAmount${list1.BATCH_NO}" value="0" datatype="1,is_digit,6" onchange="numChange('${list1.BATCH_NO}');">
				</td>
			</tr>
		</c:forEach>
		<tr align="center" class="table_list_row2">
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td>合计</td>
			<td id="reserveTotal">0</td>
		</tr> 
	</table>
</form>
<form  name="form1" id="form1">
	<table class="table_query" width="85%" align="center">
		<tr class="table_list_row2">
			<td align="center">
				<input type="button" name="button1" class="cssbutton" onclick="toSave();" value="确定" /> 
				<input type="button" name="button2" class="cssbutton" onclick="parent._hide();;" value="关闭" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function doInit(){
		var patchNo1 = '${initNo}';
		if(patchNo1 != "" && patchNo1.length !=0){
			var array = patchNo1.split("/");
			for(var i=0; i<array.length; i++){
				var tempStr = array[i];
				if(tempStr != ""){
					var tempArray = tempStr.split("-");
					var bathNo = tempArray[0];
					var count = tempArray[1];
					var obj1 = "avaAmount"+bathNo;
					var obj2 = "ava"+bathNo;
					var obj3 = "initAmount"+bathNo;
					if(document.getElementById(obj1)) {
						document.getElementById(obj2).innerHTML = parseInt(document.getElementById(obj1).value,10)+ parseInt(count,10);
						document.getElementById(obj1).value = parseInt(document.getElementById(obj1).value,10) + parseInt(count,10);
						document.getElementById(obj3).value = document.getElementById(obj1).value;
					}
				}
			} 
		}
		
		var patchNo2 = '${batchNo}';
		if(patchNo2 != "" && patchNo2.length !=0){
			var array = patchNo2.split("/");
			for(var i=0; i<array.length; i++){
				var tempStr = array[i];
				if(tempStr != ""){
					var tempArray = tempStr.split("-");
					var bathNo = tempArray[0];
					var count = tempArray[1];
					var obj1 = "avaAmount"+bathNo;
					var obj2 = "ava"+bathNo;
					var obj3 = "reserveAmount"+bathNo;
					var obj4 = "initReserve"+bathNo;
					if(document.getElementById(obj1)) {
						document.getElementById(obj2).innerHTML = parseInt(document.getElementById(obj1).value,10) - parseInt(count,10);
						document.getElementById(obj1).value = parseInt(document.getElementById(obj1).value,10) - parseInt(count,10);
						document.getElementById(obj3).value = parseInt(count,10);
						document.getElementById(obj4).innerHTML = parseInt(count,10);
					}
				}
			} 
		}
		
		totalAmount();
	}
	
	function toSave(){
		if(submitForm('fm')){
			var returnStr1 = "";
			var returnStr2 = "";
			var amountTotal = 0;
			var batchNo = document.getElementsByName("batchNo");
			var reserveAmount = document.getElementsByName("reserveAmount");
			var avaAmount = document.getElementsByName("avaAmount");
			var deliveryAmount = document.getElementsByName("deliveryAmount");
			for(var i=0 ;i< reserveAmount.length; i++){
				if(reserveAmount[i].value > 0 && avaAmount[i].value < 0){
					MyDivAlert("可用库存不能小于0");
					return false;
				}
				
				if(parseInt(reserveAmount[i].value) < parseInt(deliveryAmount[i].value)){
					MyDivAlert("资源保留数量不能小于已发运数量");
					return false;
				}
				var amount = reserveAmount[i].value;
				if(parseInt(amount,10)>0){
					returnStr1 += batchNo[i].value+"-"+reserveAmount[i].value+"/";
					returnStr2 += batchNo[i].value+"/";
					amountTotal += parseInt(amount,10);
				}
			}
			var initTotalAmount = document.getElementById("initTotalAmount").value;
			if(parseInt(initTotalAmount,10) != parseInt(amountTotal,10)){
				MyDivAlert("调整后的保留数量应该与原保留数量相等");
				return false;
			}
			
			parent.$('inIframe').contentWindow.document.getElementById('reserveAmount${materalId}').value = amountTotal;
			parent.$('inIframe').contentWindow.document.getElementById('batchNo${materalId}').value = returnStr1;
			parent.$('inIframe').contentWindow.document.getElementById('batch${materalId}').innerHTML = returnStr1;
			var avaObj = parent.$('inIframe').contentWindow.document.getElementById('avaPrice${materalId}');
			var stockObj = parent.$('inIframe').contentWindow.document.getElementById('stock${materalId}');
			avaObj.innerHTML = parseInt(avaObj.innerHTML,10)+parseInt('${amount}',10)-parseInt(amountTotal,10);
			stockObj.innerHTML = parseInt(stockObj.innerHTML,10)+parseInt('${amount}',10)-parseInt(amountTotal,10);
			parent.$('inIframe').contentWindow.priceTotal();
		 	parent._hide();
	 	}
	}
	
	function numChange(batchNo){
		var objId1 = "initAmount"+batchNo;
		var objId2 = "reserveAmount"+batchNo;
		var objId3 = "ava"+batchNo;
		var objId4 = "avaAmount"+batchNo;
		var initValue = document.getElementById(objId1).value;
		var reserveValue = document.getElementById(objId2).value;
		document.getElementById(objId3).innerHTML = parseInt(initValue, 10) - parseInt(reserveValue, 10);
		document.getElementById(objId4).value = parseInt(initValue, 10) - parseInt(reserveValue, 10);
		totalAmount();
	}
	
	function totalAmount(){
		var reserveAmount = document.getElementsByName("reserveAmount");
		var totalAmount = 0;
		for(var i=0; i<reserveAmount.length; i++){
			totalAmount += parseInt(reserveAmount[i].value, 10);
		}
		document.getElementById('reserveTotal').innerHTML = totalAmount;
	}
</script>
</body>
</html>