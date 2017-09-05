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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核&gt; 集团客户代交车审核</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="reqVer" id="reqVer" value="${ver}"></input>
<input type="hidden" name="orderId" id="orderId" value="${map.ORDER_ID}"></input>
<input type="hidden" name="reqId" id="reqId" value="${reqId}"></input>
<input type="hidden" name="orderType" id="orderType" value="${map.ORDER_TYPE}"></input>
<input type="hidden" name="AREA_ID" id="AREA_ID" value="${map.AREA_ID}"></input>
<input type="hidden" name="amounttype" id="amounttype" value="${map.TYPE_NAME}"></input>
	<table class="table_query">
		<tr>
			<td align="center">业务范围：</td>
			<td align="center">订单号码：</td>
			<td align="center">发运申请单号：</td>
			<td align="center">资金类型：</td>
			<td align="center">可用余额：</td>
			<td align="center">订单总价：</td>
		</tr>
		<tr>
			<td align="center">${map.AREA_NAME}</td>
			<td align="center">${map.ORDER_NO}</td>
			<td align="center">${map.DLVRY_REQ_NO}</td>
			<td align="center">${map.TYPE_NAME}<input type="hidden" name="account" id="account" value="${map.AVAILABLE_AMOUNT}"/></td>
			<td align="center" id="availableAccount"></td>
			<td align="center" id="totailPrice"></td>
		</tr>
	</table>
	<br>
	<table class="table_list">
		<tr align="center" class="tabletitle">
			<th>车系</th>
			<th>物料编号</th>
			<th>物料名称</th>
			<c:if test="${map.ORDER_TYPE==10201003}">
				<th>订做批次号</th>
			</c:if>
			<th>数量</th>
			<th>单价</th>
			<th>金额</th>
			<th>折扣率 %</th>
			<th>折扣后单价</th>
			<th>折扣额</th>
		</tr>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
		<input type="hidden" name="orderDetailId" id="orderDetailId" value="${list.ORDER_DETAIL_ID}"></input>
		<input type="hidden" name="detailId" id="detailId" value="${list.DETAIL_ID}"></input>
			<tr align="center" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td><c:out value="${list.GROUP_NAME}"/></td>
				<td><c:out value="${list.MATERIAL_CODE}"/></td>
				<td><c:out value="${list.MATERIAL_NAME}"/></td>
				<c:if test="${map.ORDER_TYPE==10201003}">
					<td>
						<c:out value="${list.PATCH_NO}"/>
					</td>
				</c:if>
				<td><c:out value="${list.REQ_AMOUNT}"/><input type="hidden" name="reqAmount" value="${list.REQ_AMOUNT}"/></td>
				<td><c:out value="${list.SINGLE_PRICE}"/></td>
				<td><c:out value="${list.PRICE}"/><input type="hidden" name="prices" value="${list.PRICE}"/></td>
				<td><c:out value="${list.DISCOUNT_RATE}"/></td>
				<td><c:out value="${list.DISCOUNT_S_PRICE}"/></td>
				<td><c:out value="${list.DISCOUNT_PRICE}"/><input type="hidden" name="discountPrices" value="${list.DISCOUNT_PRICE}"/></td>
			</tr>
		</c:forEach> 
		<tr align="center" class="table_list_row2">
			<td></td>
			<td></td>
			<c:if test="${map.ORDER_TYPE==10201003}">
				<td></td>
			</c:if>
			<td align="right"><strong>总计：</strong></td>
			<td id="accounts"></td>
			<td id=""></td>
			<td id="price"></td>
			<td id=""></td>
			<td id=""></td>
			<td id="discountPrice"></td>
		</tr>
	</table>
	<br>
	<table class="table_query">
		<tr>
			<td align="right">集团客户名称：</td>
			<td align="left"><c:out value="${map.FLEET_NAME}"/></td>
		</tr>
		<tr>
			<td align="right">运送地点：</td>
			<td align="left"><c:out value="${map.FLEET_ADDRESS}"/></td>
		</tr>
		<tr>
			<td align="right">代交车经销商：</td>
			<td align="left">
			    <input type="text"  name="dealerCode" id="dealerCode"size="15" value="" datatype="0,is_noquotation,15" readonly="readonly"/>
				<input name="button2" type="button" class="mini_btn" onclick="test();" value="..." />
				<input name="button3" type="button" class="cssbutton" onclick="toClear();" value="清除" />
			</td>
		</tr>
		<tr>
			<td align="right" width="15%">运送方式：</td>
			<td align="left">
				<script type="text/javascript">
			      	genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
			    </script>
			</td>
		</tr>
		<tbody id="addTr" style="display:none">
			<tr>
				<td align="right">经销商地址：</td>
				<td align="left">
					<select name="addressId" id="addressId" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);"></select>
				</td>
			</tr>
			<tr>
				<td align="right">收车单位：</td>
				<td align="left" id="receiveOrg">
				</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td align="left">
					<input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
			</tr>
			<tr>
				<td align="right">联系电话：</td>
				<td align="left">
					<input name="tel" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
			</tr>
		</tbody>
		<c:if test="${map.ORDER_TYPE==10201003}">
			<tr>
				<td align="right">改装说明：</td>
				<td align="left"><c:out value="${map.REFIT_REMARK}"/></td>
			</tr>
		</c:if>
		<tr>
			<td align="right">付款信息备注：</td>
			<td align="left"><c:out value="${map.PAY_REMARK}"/></td>
		</tr>
		<tr>
			<td align="right">备注说明：</td>
			<td align="left"><c:out value="${map.ORDER_REMARK}"/></td>
		</tr>
		<tr>
			<td></td>
			<td align="left">
				<input type="hidden" name="reqId" id="reqId" value="${reqId}">
				<input type="hidden" name="ver" id="ver" value="${ver}">
				<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck();" value="确定"/>
				<input type="button" name="button3" class="cssbutton" onclick="parent._hide();" value="返回"/>
				<input type="button" id="queryBtn2" name="cancelButton" class="cssbutton" onclick="toCancelCheck();" value="订单取消"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
//取消校验
function toCancelCheck(){
	MyDivConfirm("确认取消？",cancelForword);
}


//取消提交
function cancelForword(){
	makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/FleetOrderCheck/FleetOrderBackCheckQuery.json",showCancelValue,'fm');
}

function showCancelValue(json){
	if(json.returnValue == '1'){
		top.frames["inIframe"].__extQuery__(1) ;
		parent._hide();
		MyAlert("取消成功！");
	}else{
		MyAlert("数据已被修改,取消失败！");
	}
}
	//初始化
	function doInit(){
		sumAll();
	}
	function sumAll(){
		var amount = 0;
		var price = 0;
		var discountPrice = 0;
		var account=document.getElementById("account").value;
		var reqAmount = document.getElementsByName("reqAmount");
		var prices = document.getElementsByName("prices");
		var discountPrices = document.getElementsByName("discountPrices");
		for(var i = 0; i< reqAmount.length; i++){
			amount = Number(amount)+Number(reqAmount[i].value);
			price = Number(price)+Number(prices[i].value);
			discountPrice = Number(discountPrice)+Number(discountPrices[i].value);
		}
		document.getElementById("accounts").innerText= amount;
		document.getElementById("price").innerText= amountFormat(price);
		document.getElementById("discountPrice").innerText= amountFormat(discountPrice);
		document.getElementById("totailPrice").innerText= amountFormat(parseFloat(price,10)-parseFloat(discountPrice,10));
		document.getElementById("availableAccount").innerText = amountFormat(account);
	}
	//保存校验
	function toSaveCheck(){
		var dealerCode = document.getElementById("dealerCode").value;
		if(!dealerCode){
			MyDivAlert("代交车经销商不能为空!");
		}
		MyDivConfirm("确认保存？",putForword);
	}
	//审核保存提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/FleetOrderCheck/fleetOrderCheckSave.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			top.frames["inIframe"].__extQuery__(1) ;
			parent._hide();
			MyAlert("操作成功！")
		}else if(json.returnValue == '2'){
			parent._hide();
			MyAlert("数据已被修改,操作失败！")
		}else{
			MyDivAlert("操作失败！请联系系统管理员！");
		}
	}
	function test()
	{
		var areaId=${map.AREA_ID};
		var url = "<%=contextPath%>/dialog/showOrgDealer.jsp?&INPUTCODE=dealerCode&ISMULTI=false&AREA_ID="+areaId+"&ISALLLEVEL=true";
		var win = window.showModalDialog(url,window,"dialogWidth=730px;dialogHeight=400px");
	}
	function modalDialogWinBeforeClose()
	{
		var dealerCode = document.getElementById("dealerCode").value;
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/FleetOrderCheck/dealerAddressQuery.json?&dealerCode="+dealerCode,showAddressList,'fm','queryBtn');
		
	}
	//根据运输方式隐藏地址列表
	function addressHide(arg){
		var obj1 = document.getElementById("addTr");
		var obj2 = document.getElementById("dealerCode");
		if(arg == '<%=Constant.TRANSPORT_TYPE_02%>' && obj2.value != "") {
			obj1.style.display = "inline";
		}
		else{
			obj1.style.display = "none";
		}
	}
	function showAddressList(json){
		var list =json.list;
		var deliveryType = document.getElementById("deliveryType").value;
		if(deliveryType == "<%=Constant.TRANSPORT_TYPE_02%>"){
			document.getElementById("addTr").style.display = "inline";
		}
		var selectList = document.getElementById("addressId");
		selectList.options.length = 0;
		if(list.size()>0){
			for(var i = 0; i < list.size(); i++){
				var option = document.createElement("option");
				option.value=list[i].ADDRESS_ID;
				option.text=list[i].ADDRESS;
				selectList.add(option);
			}
		}
		getAddressInfo(document.getElementById("addressId").value);
	}
	function toClear(){
		document.getElementById("dealerCode").value="";
	}
	//获得联系人信息
	function getAddressInfo(arg){
		var addressId = arg;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressInfo.json";
		makeCall(url,showAddressInfo,{addressId:addressId});
	}	
	function showAddressInfo(json){
		var obj1 = document.getElementById("linkMan");
		var obj2 = document.getElementById("tel");
		var obj3 = document.getElementById("receiveOrg");
		if(!json.info.LINK_MAN) {
			obj1.value = '' ;
		}
		if(!json.info.TEL) {
			obj2.value = '' ;
		}
		if(!json.info.RECEIVE_ORG) {
			obj3.innerHTML = '' ;
		}
		if(json.info.LINK_MAN != null && json.info.LINK_MAN != "null"){
			obj1.value = json.info.LINK_MAN;
		}
		if(json.info.TEL != null && json.info.TEL != "null"){
			obj2.value = json.info.TEL;
		}
		if(json.info.RECEIVE_ORG != null && json.info.RECEIVE_ORG != "null"){
			obj3.innerHTML = json.info.RECEIVE_ORG;
		}
	}
</script>
</body>
</html>