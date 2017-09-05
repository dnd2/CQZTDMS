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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 常规订单审核</div>
<form method="post" name="fm" id="fm">
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>经销商代码</th>
			<th>经销商名称</th>
			<th>订单号码</th>
			<th>订单周度</th>
			<th>订单类型</th>
			<th>提报数量</th>
			<th>审核数量</th>
			<th>待审核数量</th>
		</tr>
		<tr align="center" class="table_list_row2">
			<td>${map.DEALER_CODE}</td>
			<td>${map.DEALER_NAME}</td>
			<td>${map.ORDER_NO}</td>
			<td>${map.ORDER_YEAR}.${map.ORDER_WEEK}<input type="hidden" name="orderWeek" value="${map.ORDER_WEEK}"/><input type="hidden" name="orderYear" value="${map.ORDER_YEAR}"/></td>
			<td>
				<script type="text/javascript">
					writeItemValue(${map.ORDER_TYPE});
				</script>
			</td>
			<td>${map.ORDER_AMOUNT}<input type="hidden" name="ddddd" id="ddddd" value="${map.ORDER_AMOUNT}"/></td>
			<td>${map.CHECK_AMOUNT}<input type="hidden" name="orderId" value="${map.ORDER_ID}"/></td>
			<td>${map.WAIT_AMOUNT}</td>
		</tr>
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>车系</th>
			<th>物料编号</th>
			<th>物料名称</th>
			<th>颜色</th>
			<th>提报数量</th>
			<th>可用生产计划</th>
			<th>待审核数量</th>
			<th>可用库存</th>
			<th>审核数量</th>
		</tr>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
			<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td><c:out value="${list.SERIES_NAME}"/></td>
				<td><c:out value="${list.MATERIAL_CODE}"/></td>
				<td><c:out value="${list.MATERIAL_NAME}"/></td>
				<td><c:out value="${list.COLOR_NAME}"/></td>
				<td><c:out value="${list.ORDER_AMOUNT}"/><input type="hidden" name="orderAmount" value="<c:out value="${list.ORDER_AMOUNT}"/>"/></td>
				<td><c:out value="${list.AMOUNT}"/><input type="hidden" name="resourceAmount" value="<c:out value="${list.AMOUNT}"/>"/></td>
				<td><c:out value="${list.WAIT_AMOUNT}"/><input type="hidden" name="waitAmount" value="<c:out value="${list.WAIT_AMOUNT}"/>"/></td>
				<td><c:out value="${list.STOCK_AMOUNT}"/><input type="hidden" name="stockAmount" value="<c:out value="${list.STOCK_AMOUNT}"/>"/></td>
				<td>
					<input type="text" name="amount" id="<c:out value="${list.DETAIL_ID}"/>" size="4" value="<c:out value="${list.WAIT_AMOUNT}"/>" onBlur="toChange(this.value,<c:out value="${list.ORDER_AMOUNT}"/>,<c:out value="${list.CHECK_AMOUNT}"/>,<c:out value="${list.AMOUNT}"/>,<c:out value="${list.DETAIL_ID}"/>);"/>
					<input type="hidden" name="detailId" value="<c:out value="${list.DETAIL_ID}"/>"/>
					<input type="hidden" name="checkAmount" value="<c:out value="${list.CHECK_AMOUNT}"/>"/>
					<input type="hidden" name="materialId" value="<c:out value="${list.MATERIAL_ID}"/>"/>
					<input type="hidden" name="ver" value="<c:out value="${list.VER}"/>"/>
				</td>
			</tr>
		</c:forEach> 
		<tr align="center" class="table_list_row2">
			<td></td>
			<td></td>
			<td></td>
			<td align="right"><strong>合计：</strong></td>
			<td id="orderAmounts"></td>
			<td id="resourceAmounts"></td>
			<td id="waitAmounts"></td>
			<td id="stockAmounts"></td>
			<td id="checkAmounts"></td>
		</tr>
	</table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="hidden" name="detailIds" id="detailIds"/>
				<input type="hidden" name="strAmounts" id="strAmounts"/>
				<input type="hidden" name="oldAmounts" id="oldAmounts"/>
				<input type="hidden" name="materialIds" id="materialIds"/>
				<input type="hidden" name="vers" id="vers"/>
				<input type="hidden" name="aaaaa" id="aaaaa"/>
				<input type="hidden" name="bbbbb" id="bbbbb"/>
				<input type="hidden" name="ccccc" id="ccccc"/>
				<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck();" value="保存"/>
				<input type="button" name="button3" class="cssbutton" onclick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" >
	function toChange(value1,value2,value3,value4,value5){
		var allamounts= 0;
		var amount = value1;
		var orderAmount = value2;
		var checkAmount = value3;
		var resourceAmount = value4;
		var detailId = value5;
		var exp = /^(-?[1-9][0-9]*|0)$/;
		if(amount>orderAmount){
			MyDivAlert("审核数量不能大于提报数量,请重新输入！");
			return false;
		}
		if(amount>resourceAmount){
			//MyDivAlert("审核数量不能大于资源数量,请重新输入！");
			//return false;
		}
		if(amount<0){
			MyDivAlert("审核数量不能小于零，请重新输入！");
			return false;
		}
		if(!exp.test(amount)){
			MyDivAlert("审核数量只能为整数，请重新输入！");
			return false;
		}
		var amounts = document.getElementsByName("amount");
		for(var i=0; i<amounts.length; i++){
			allamounts = allamounts + Number(amounts[i].value);
		}
		document.getElementById("checkAmounts").innerText = allamounts;
	}
	
	function sumAll(){
		var orderAmount =0;
		var checkAmount =0;
		var waitAmount =0;
		var resourceAmount =0;
		var stockAmount =0;
		var orderAmounts = document.getElementsByName("orderAmount");
		var checkAmounts = document.getElementsByName("checkAmount");
		var waitAmounts = document.getElementsByName("waitAmount");
		var resourceAmounts = document.getElementsByName("resourceAmount");
		var stockAmounts = document.getElementsByName("stockAmount");
		for(var i=0; i<orderAmounts.length; i++){
			orderAmount = orderAmount + Number(orderAmounts[i].value);
			checkAmount = checkAmount + Number(checkAmounts[i].value);
			waitAmount = waitAmount + Number(waitAmounts[i].value);
			resourceAmount = resourceAmount + Number(resourceAmounts[i].value);
			stockAmount = stockAmount + Number(stockAmounts[i].value);
		}
		document.getElementById("orderAmounts").innerText = orderAmount;
		document.getElementById("checkAmounts").innerText = checkAmount;
		document.getElementById("waitAmounts").innerText = waitAmount;
		document.getElementById("resourceAmounts").innerText = resourceAmount;
		document.getElementById("stockAmounts").innerText = stockAmount;
		document.getElementById("aaaaa").value = checkAmount;
		document.getElementById("bbbbb").value = waitAmount;
	}
	//保存时校验
	function toSaveCheck(){
		var cnt = 0;
		var checkAmounts = '';
		var detailIds ='';
		var vers ='';
		var oldAmounts ='';
		var materialIds ='';
		var oldAmount = document.getElementsByName("checkAmount");
		var checkAmount = document.getElementsByName("amount");
		var materialId = document.getElementsByName("materialId");
		var orderAmount = document.getElementsByName("orderAmount");
		var detailId = document.getElementsByName("detailId");
		var resourceAmount = document.getElementsByName("resourceAmount");
		var ver = document.getElementsByName("ver");
		for(var i=0 ;i< detailId.length; i++){
			cnt = cnt+Number(checkAmount[i].value);
			if(Number(checkAmount[i].value) > Number(orderAmount[i].value)){
				MyDivAlert("审核数量不能大于提报数量！");
				return false;
			}
			if(Number(checkAmount[i].value) < 0){
				MyDivAlert("审核数量不能小于零！");
				return false;
			}
			if(Number(checkAmount[i].value)>Number(resourceAmount[i].value)){
				//MyDivAlert("审核数量大于可用资源数量，请重新输入！");
	            //return false;
			}
			checkAmounts = checkAmount[i].value+','+checkAmounts;
			oldAmounts = oldAmount[i].value + ',' + oldAmounts;
			detailIds = detailId[i].value + ',' + detailIds;
			vers = ver[i].value + ',' + vers;
			materialIds = materialId[i].value + ',' + materialIds;
		}
		if(cnt==0){
			MyDivAlert("本次审核数量不能全部为零！");
            return;
		}
		document.getElementById("ccccc").value = cnt;
		document.getElementById("strAmounts").value=checkAmounts;
		document.getElementById("oldAmounts").value=oldAmounts;
		document.getElementById("detailIds").value=detailIds;
		document.getElementById("vers").value=vers;
		document.getElementById("oldAmounts").value=oldAmounts;
		document.getElementById("materialIds").value=materialIds;
		MyDivConfirm("确认保存？",putForword);
	}
	//审核保存提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailCheck.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			var rowObj = parent.$('inIframe').contentWindow.rowObj;
			var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
			var checkAmount = document.getElementById("checkAmounts").innerText;
			var waitAmount = document.getElementById("ddddd").value-checkAmount;
			rowObj.cells[7].childNodes[0].innerText= checkAmount;
			rowObj.cells[8].childNodes[0].innerText= waitAmount;
			//rowObj.cells[9].childNodes[0].value=Number(rowObj.cells[9].childNodes[0].value)+1;
			parent._hide();
			MyAlert("保存成功！");
		}else if(json.returnValue == '2'){
			parent._hide();
			MyAlert("数据已被修改，操作失败！");
		}else{
			MyDivAlert("保存失败！请联系系统管理员！");
		}
	}
	function toBack(){
		parent._hide();
	}
	function doInit(){
		sumAll();
	}
</script>
<!--页面列表 end -->
</body>
</html>