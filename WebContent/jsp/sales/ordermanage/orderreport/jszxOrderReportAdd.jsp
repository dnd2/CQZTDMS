<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>补充订单提报</title>
<script type="text/javascript"><!--
//--------------------------------------------------------------------------------
//属性申明
var aAddressName = new Array() ;
var aAddressId = new Array() ;

//获取控件左绝对位置
function getAbsoluteLeft(value) {
	var oObj = document.getElementById(value)
	var iLeft = oObj.offsetLeft            
	while(oObj.offsetParent != null) { 
		var oParent = oObj.offsetParent ;
		iLeft += oParent.offsetLeft ;
		oObj = oParent ;
	}
	return iLeft ;
}

//获取控件上绝对位置
function getAbsoluteTop(value) {
	var oObj = document.getElementById(value) ;
	var iTop = oObj.offsetTop ;
	while(oObj.offsetParent != null)
	{  
		var oParent = oObj.offsetParent ;
		iTop += oParent.offsetTop ;  // Add parent top position
		oObj = oParent ;
	}
	return iTop ;
}

//获取控件宽度
function getElementWidth(value) {
	var oObj = document.getElementById(value) ;
	return oObj.offsetHeight ;
}

//设置div控件绝对位置
function setDivLocation(setValue, getValue) {
	var oSet = document.getElementById(setValue) ;
	
	oSet.style.left = getAbsoluteLeft(getValue) ;
	oSet.style.top = parseFloat(getAbsoluteTop(getValue)) + parseFloat(getElementWidth(getValue)) ;
}

function setDivNone() {
	document.getElementById("addressDiv").style.display = "none" ;
}

function setDivInline() {
	document.getElementById("addressDiv").style.display = "inline" ;
}

function changeTableColor_Over(value) {
	document.getElementById(value).style.backgroundColor = "#B0C4DE" ;
}

function changeTableColor_Out(value) {
	document.getElementById(value).style.backgroundColor = "#FFFAF0" ;
}

//将数据插入控件
function insertAddress(iValue) {
	document.getElementById("addressName").value = document.getElementById("s" + iValue).innerHTML ;
	document.getElementById("addressId").value = document.getElementById("ha" + iValue).value ;
}

//将数据插入控件
function insertAddress_New(value1) {
	document.getElementById("addressId").value = value1 ;
}

function clickinsertDiv(value) {
	if(value == "　　　　") {
		document.getElementById("addressName").value = "" ;
	} else {
		insertDiv(document.getElementById("addressName").value) ;
	}
}

//将数据插入层
function insertDiv(value) {
	var iLen = aAddressName.length ;
	var flag = false ;
	var str = "" ;

	str += "<table cellpadding=\"3\" cellspacing=\"0\" bgColor=\"#FFFAF0\">" ;

	for (var i=0; i<iLen; i++) {
		if(aAddressName[i] == value.lTrim()) {
			insertAddress_New(aAddressId[i]) ;

			flag = true ;
		} 

		if(compareValue(aAddressName[i], value.lTrim())) {
			str += "<tr id=\"t" + i + "\" onMouseOver=\"changeTableColor_Over(this.id) ;\" onMouseOut=\"changeTableColor_Out(this.id);\">" ;
			str += "<td onClick=\"insertAddress(" + i + ") ;\">" ;
			str += "<pre><span id=\"s" + i + "\">" + aAddressName[i] + "</span></pre>";
			str += "<input type=\"hidden\" id=\"ha" + i + "\" value=\"" + aAddressId[i] + "\" />" ;
			str += "</td>" ;
			str += "</tr>" ;
		}
	}
	
	str += "</table>" ;
	
	if(!flag) {
		insertAddress_New("") ;
	}
	
	document.getElementById("addressDiv").innerHTML = str ;
	if(str.length == 65) {
		setDivNone() ;
	} else {
		setDivLocation('addressDiv', 'addressName') ;
		setDivInline() ;
	}
}

//判断一个字符串中是否包括另一个字符串
function compareValue(value1, value2) {
	if(value1.search(value2) == -1) {
		return false ;
	} else {
		return true ;
	}
}

//删除字符串左空格
String.prototype.lTrim = function() {
	return this.replace(/(^\s*)/g,"") ;
}
//--------------------------------------------------------------------------------

function doInit(){
	getDealerLevel(document.getElementById("areaId").value);
	var choice_code = document.getElementById("orderYearWeek").value;
	showData(choice_code);
	getReceiverList();
	getPriceList();//获得价格类型列表
}

function getDealerLevel(arg){
	document.getElementById("area").value = arg;
	getFundTypeList();
	getPriceList();//获得价格类型列表
	getReceiverList();
}

//控制不能跨业务范围提报
function disableArea(){
	var rowsnum = document.getElementById("tbody1").rows.length;
	if(rowsnum != 0){
		document.getElementById('areaId').disabled = "disabled";
	}
	else{
		document.getElementById('areaId').disabled = "";
	}
}

//获得地址列表
function getAddressList(){	
	var dealerId = document.getElementById("receiver").value;
	var areaId = document.getElementById("area").value.split("|")[0] ;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
	makeCall(url,showAddressList,{dealerId:dealerId,areaId:areaId});
}	

function showAddressList(json){
	// var obj = document.getElementById("deliveryAddress");
	// obj.options.length = 0;
	aAddressName = new Array() ;
	aAddressId = new Array() ;
	for(var i=0;i<json.addressList.length;i++){
		aAddressName[i] = json.addressList[i].ADDRESS ; 
		aAddressId[i] = json.addressList[i].ID ;
		// obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
	}
	document.getElementById("addressName").value = "　　　　" ;
	
	// getAddressInfo(document.getElementById("deliveryAddress").value);
}

//获得联系人信息
function getAddressInfo(arg){
	var addressId = arg;
	var oldAddress = document.getElementById("oldAddress") ;
	
	if(addressId != "" && addressId != oldAddress.value) {
		oldAddress.value = addressId ;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressInfo.json";
		makeCall(url,showAddressInfo,{addressId:addressId});
	}
	
	if(!addressId) {
		oldAddress.value = "" ;
		document.getElementById("linkMan").value = "" ;
		document.getElementById("tel").value = "" ;
		document.getElementById("receiveOrg").innerHTML = "" ;
	}
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

//获得资金类型列表
function getFundTypeList(){
	var arg = document.getElementById("area").value;
	var dealerId = arg.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showJszxFundTypeList.json";
	makeCall(url,showFundTypeList,{dealerId:dealerId});
}

//资金类型列表显示
function showFundTypeList(json){
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
	}
}

// 新增产品
function addMaterial(){	
	var priceId = document.getElementById("priceId").value;
	var materialCode = document.getElementById("materialCode").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/addMaterial.json";
	makeCall(url,addRow,{materialCode:materialCode,priceId:priceId}); 
}

// 删除产品
function delMaterial(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	totalPrice();
  	disableArea();
  	_setSelDisabled_("tbody1", 0) ;
}

function addRow(json){	
	if(parseFloat(json.info.PRICE, 10) < <%=Constant.MATERIAL_PRICE_MAX%>){
		var timeValue = json.info.MATERIAL_ID;
		var newRow = document.getElementById("tbody1").insertRow();
		newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = json.info.SERIES_NAME;
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = json.info.MATERIAL_CODE;
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = json.info.MATERIAL_NAME;
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = json.info.RESOURCE_AMOUNT;
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount"+timeValue+"' type='text' class='SearchInput' datatype='0,is_digit,6' value='0' size='2' maxlength='6' onchange='totalPrice();'><font color='red'>*</font>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<span id='price"+timeValue+"'>"+amountFormat(json.info.PRICE)+"</span><input id='singlePrice"+timeValue+"' type='hidden' name='singlePrice"+timeValue+"' value='"+json.info.PRICE+"'>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<span id='hejiPrice"+timeValue+"'>0</span>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId"+timeValue+"' value='"+json.info.MATERIAL_ID+"'>";
	}	
	else{
		MyAlert("该款物料尚未维护价格，不能添加！");
		return false;
	}
	disableArea();
	_setSelDisabled_("tbody1", 0) ;
}

// 物料组树
function materialShow(){
	if(!_getTip_()) {
		return false ;
	}
	var ids = "";//已选中的物料id
	var myForm = document.getElementById("fm");
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=10 && obj.id.substring(0,10)=="materialId"){
			ids += obj.value + ",";
		}   
	} 	
	ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
	var areaObj = document.getElementById("area");
	var areaId = areaObj.value.split("|")[0];	
	
	var productId = "" ;
	
	if(document.getElementById("_productId_")) {
		productId = document.getElementById("_productId_").value ;
	}
	
	showMaterialByAreaId('materialCode','','false',areaId,ids, productId);
}

// 合计价格
function totalPrice(){
	var myForm = document.getElementById("fm");
	var totalAmount = 0;
	var totalPrice = 0;
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=6 && obj.id.substring(0,6)=="amount"){
			var subStr = obj.id.substring(6,obj.id.length);
			var value1 = parseInt(obj.value, 10);
			var value2 = parseFloat(document.getElementById("singlePrice"+subStr).value, 10);
			var heji = value1*value2;
			document.getElementById("hejiPrice"+subStr).innerHTML = amountFormat(heji);
			totalAmount += value1;
			totalPrice += heji;
		}  
	}  
	document.getElementById("totalAmount").innerHTML = totalAmount;
	document.getElementById("totalAmount_").value = totalAmount;
	document.getElementById("totalPrice").innerHTML = amountFormat(totalPrice);
	document.getElementById("total").value = totalPrice;
}
function contentTrim(str){
	if(str){
		str = str.replace(/(^\s*)|(\s*$)/g, "");
		return str;
	}else{
		return "";
	} 
}
function confirmAdd(arg){
	var bIsJSZX = document.getElementById("IsJSZX").value ;
	
	if(!bIsJSZX) {
		MyAlert('请选择"是否结算中心库存车启票"!') ; 
		return false ;
	}
	
	if(!_getTip_()) {
		return false ;
	}
	if(document.getElementById('addressId').value == "" && !document.getElementById("isCover").checked && document.getElementById("deliveryType").value == <%=Constant.TRANSPORT_TYPE_02%>) {
		MyAlert('运送地址填写有误！') ; 
		return false ;
	}
	if(!testLen(document.getElementById('orderRemark').value)) {
		MyAlert('备注录入已超过50个汉字的最大限！') ; 
		return false ;
	}
	if(submitForm('fm')){
		document.getElementById("submitType").value = arg;
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=6 && obj.id.substring(0,6) == "amount"){
				var amount = parseInt(obj.value, 10);
				if(amount < 1){
					MyAlert("物料数量不能小于1！");
					return false;
				}
				var subStr = obj.id.substring(6, obj.id.length);
				var singlePrice = document.getElementById("singlePrice"+subStr).value;
				if(parseInt(singlePrice, 10)==0){
					MyAlert("未维护价格物料不能提报！");
					return false;
				}
			}   
		} 
		var totalAmount = parseInt(document.getElementById("totalAmount").innerHTML, 10);
		if(totalAmount < 1){
			MyAlert("所选物料总数量不能小于1！");
			return false;
		}
		var isCover = document.getElementById("isCover").value;
		if("0"==isCover+"" || !isCover){
			if(document.getElementById("deliveryType").value == '<%=Constant.TRANSPORT_TYPE_02%>' && document.getElementById("deliveryAddress").value == ''){
				MyAlert("运送地址不能为空！");
				return false;
			}
		}
		var orderRemark = document.getElementById("orderRemark").value;
		if(contentTrim(orderRemark).length>350){
			MyAlert("备注说明内容过多，请重新填写!");
			return;
		}
		var payRemark = document.getElementById("payRemark").value;
		if(contentTrim(payRemark).length>150){
			MyAlert("付款信息备注内容过多，请重新填写!");
			return;
		}
		var b = new Object();
		b = document.getElementById("baocun");
		var a = new Object();
		a = document.getElementById("tibao");
		a.disabled=true;
		b.disabled=true;
		if(confirm("是否确认保存?")){
			orderAdd();
		}
		else{
			a.disabled=false;
			b.disabled=false;
		}
	}
}

function orderAdd(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/JszxOrderReport/jszxOrderReoprtAdd.json',showResult,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/JszxOrderReport/jszxOrderReoprtQueryPre.do';
	}else{
		MyAlert("提交失败！");
	}
}

//清除按钮
function toClear(){
	document.getElementById("fleetName").value = "";
	document.getElementById("fleetId").value = "";
}

//大用户弹出
function showFleet(){
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
}

//获得收货方列表
function getReceiverList(){	
	var areaObj = document.getElementById("area");
	var areaId = areaObj.value.split("|")[0];
	var dealerId = areaObj.value.split("|")[1];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
	makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
}	

//显示收货方列表
function showReceiverList(json){
	var obj = document.getElementById("receiver");
	obj.options.length = 0;
	for(var i=0;i<json.receiverList.length;i++){
		obj.options[i] = new Option(json.receiverList[i].DEALER_NAME, json.receiverList[i].DEALER_ID + "");
	}
	getAddressList();//获得发运地址列表
}

//是否代交车
function showFleetInfo(){
	if(document.getElementById("isCover").checked){
		document.getElementById("cusTr").style.display = "inline";
		document.getElementById("addTr").style.display = "none";
		document.getElementById("tran_id").style.display = "none";
		document.getElementById("isCover").value = 1;
	}else{
		document.getElementById("cusTr").style.display = "none";
		document.getElementById("addTr").style.display = "inline";
		document.getElementById("tran_id").style.display = "inline";
		addressHide(document.getElementById("deliveryType").value);
		document.getElementById("isCover").value = 0;
	}
}
function showData(choice_code){
	var data_start = "";
	var data_end = "";
	<c:forEach items="${dateList}" var="list">
		var code = "${list.code}";
		if(choice_code+"" == code+""){
			data_start = "${list.date_start}";
			data_end = "${list.date_end}";
		}
	</c:forEach>
	if(data_start){
		document.getElementById("data_start").innerHTML = data_start+"  至  ";
		document.getElementById("data_end").innerHTML = data_end;
	}
}

//获得价格类型列表
function getPriceList(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
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
	for(var i=0;i<json.priceList.length;i++){
		var id = json.priceList[i].MATERIAL_ID;
		var price = json.priceList[i].PRICE;
		
		var obj1 = document.getElementById("price" + id);
		var obj2 = document.getElementById("singlePrice" + id);
		
		if(price > <%=Constant.MATERIAL_PRICE_MAX%>){
			price = 0;
			obj1.innerHTML = "价格未维护";
		}
		else{
			obj1.innerHTML = amountFormat(price);
		}
		obj2.value = price;
		calculate(price,id);
	}
	isShowOtherPriceReason();
	totalPrice();
}

// 判断是否显示使用其他价格原因
function isShowOtherPriceReason(){	
	var areaObj = document.getElementById("area");
	var dealerId = areaObj.value.split("|")[1];
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
--></script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 补充订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align="center">
    <tr class= "tabletitle">
      <td align = "right" >业务范围：</td>
      <td align = "left" >
      	<select name="areaId" onchange="getDealerLevel(this.options[this.options.selectedIndex].value);">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
        <input type="hidden" name="area" value="">
      </td>
      <td id="_productControl_">
      	<script type="text/javascript">
      		productStart("<%=request.getContextPath()%>", '', false, true) ;
      	</script>
      </td>
      <td align = "right"><span id="span1">选择资金类型：</span></td>
      <td align = "left">
      	<span id="span2">
	      	<select name="fundType">
	      	</select>
      	</span>
      </td>
      <td align="left"></td>
      <td align="right"></td>
      <td align="left"></td>
    </tr>
    <tr class = "tabletitle">
      <td align = "right" >价格类型：</td>
      <td align = "left" colspan="7">
      	<select name="priceId" onchange="priceTypeChange();">
        </select>
      </td>
    </tr>
    <tr id="otherTr" class = "tabletitle">
      <td align = "right">使用其他价格原因：</td>
      <td align = "left" colspan="7">
      	<input name="otherPriceReason" type="text" class="long_txt"/>
      </td>
    </tr>
    <tr  class= "tabletitle">
	    <td align="right" nowrap>订单周度：   </td>
	    <td  class="table_query_2Col_input" nowrap>
		    <select name="orderWeek" id="orderYearWeek" onchange="showData(this.value);">
		      <c:forEach items="${dateList}" var="po"> 
		      <c:choose>   
    				<c:when test="${po.code==myWeek}" >   
					<option value="${po.code}" selected="selected">${po.name}</option>
					</c:when>
					<c:otherwise>  
							<option value="${po.code}">${po.name}</option> 
					</c:otherwise>
					</c:choose>
			  </c:forEach>
	        </select>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end" class="innerHTMLStrong"></span>
        </td>
	    <td  align=left nowrap>&nbsp;</td>
    </tr> 
  </table>
  <TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable >
      <th nowrap="nowrap">车系</th>
      <th nowrap="nowrap">物料编号</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">资源情况</th>
      <th nowrap="nowrap">数量</th>
      <th nowrap="nowrap" id="singleTdTitle">单价</th>
      <th nowrap="nowrap" id="totalTdTitle">合计</th>
      <th nowrap="nowrap">操作</th>
    </tr>
    <tbody id="tbody1"></tbody>
     <tr class="table_list_row1">
      <td nowrap="nowrap"  >&nbsp;</td>
      <td nowrap="nowrap" >&nbsp;</td>
      <td align="right" nowrap="nowrap"  ><strong>总计： </strong></td>
      <td align="center" nowrap="nowrap" >&nbsp;</td>
      <td align="center" nowrap="nowrap" id="totalAmount">0</td>
      <td align="center" nowrap="nowrap"></td>
      <td align="center" nowrap="nowrap" id="totalPrice"></td>
      <td nowrap="nowrap" >
      	<input type="hidden" id="totalAmount_" name="totalAmount_" value="" />
      </td>
    </tr>
  </table>	
  <table class="table_query">
    <tr class="cssTable" >
      <td width="100%" align="left">
	      <input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
	      <input class='cssbutton' name="add22" type="button" onclick="materialShow();" value ='新增产品' />
          &nbsp;
      </td>
    </tr>
  </table>
  <p>&nbsp;</p>
  <TABLE class=table_query>
    <tr>
      <th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 订单说明
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
      <th align="right"   nowrap="nowrap">&nbsp;</th>
    </tr>
    <tr class="cssTable">
      <td width="12%" align="right" valign="top" nowrap="nowrap">是否结算中心库存车启票：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<select id="isJSZX" name="isJSZX">
			<option value="">请选择</option>
			<option value="0">否</option>
			<option value="1">是</option>
		</select><font color="red"> *</font>
      </td>
    </tr>
    <tr class="cssTable">
      <td width="12%" align="right" valign="top" nowrap="nowrap">是否代交车：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap">
      	<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      </td>
    </tr>
    <tr class="cssTable" id="cusTr" style="display:none">
      <td align="right" nowrap="nowrap">选择集团客户：</td>
      <td align="left" nowrap="nowrap">
	      <input id="fleetName" name="fleetName" type="text" datatype="0,is_noquotation,30"/>
		  <input id="fleetId" name="fleetId" type="hidden"/>				
		  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
		  <input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
	  </td>
      <td align="right" nowrap="nowrap">集团客户运送地址：</td>
      <td align="left" nowrap="nowrap">
	      <input type="text" id="fleetAddress" name="fleetAddress" size="50" datatype="0,is_noquotation,50"/>
	  </td>
    </tr>
   
    <tr class="cssTable" id="tran_id">
      <td align="right"   nowrap="nowrap">运输方式： </td>
      <td colspan="3" align="left" nowrap="nowrap">
	      <script type="text/javascript">
	      	genSelBoxExp("deliveryType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'<%=Constant.TRANSPORT_TYPE_03%>');
	      </script>
      </td>
    </tr>
    <tbody id="addTr">
	    <tr class="cssTable">
	      <td align="right" nowrap="nowrap">收货方：</td>
	      <td align="left" nowrap="nowrap">
		      <select name="receiver" onchange="getAddressList();">
		      </select>
		  </td>
	      <td colspan="2" align="left" nowrap="nowrap">&nbsp;</td>
	    </tr>
	    <tr class="cssTable" style="display:inline">
	      <td align="right" nowrap="nowrap">运送地址：</td>
	      <td align="left" nowrap="nowrap">
	      	<input type="text" name="addressName" id="addressName" onclick="clickinsertDiv(this.value);" onpropertychange="insertDiv(this.value);" onblur="setTimeout('setDivNone()', 150) ;" class="long_txt" /><font color="red">*</font>
  			<input type="hidden" name="deliveryAddress" id="addressId" onpropertychange="getAddressInfo(this.value)" />
		      <!--<select name="deliveryAddress" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
		      </select>
		  --></td>
	      <td align="right" valign="top" nowrap="nowrap">收车单位：</td>
	      <td align="left" valign="top" nowrap="nowrap" id="receiveOrg"></td>
	    </tr>
	    <tr class="cssTable" >
	      <td align="right" valign="top" nowrap="nowrap">联系人：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
	      <td align="left" valign="top" nowrap="nowrap"><input name="tel" type="text" class="middle_txt" size="30" maxlength="30" /></td>
	    </tr>
    </tbody>
    <TR class=cssTable id="payRemarkTR">
      <TD height="66" align="right"  nowrap>付款信息备注：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea id="payRemark" name="payRemark" rows="3" cols="80"></textarea></TD>
    </TR>
    <TR class=cssTable >
      <TD height="66" align="right"  nowrap>备注说明：</TD>
	  <TD colspan="3" align="left"  nowrap><textarea id="orderRemark" name="orderRemark" rows="3" cols="80" onkeyup="value = value.replace(/#/g, '') ;" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onchange="limitChars('orderRemark', 50)"></textarea></TD>
    </TR>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
      	  <input type="hidden" name="year" value="${year}">
      	  <input type="hidden" name="week" value="${week}">
      	  <input type="hidden" name="isCheck" value="${isCheck}">
      	  <input type="hidden" name="submitType" value="">
      	  <input type="hidden" name="total" value="0">
      	  <input type="hidden" name="oldAddress" id="oldAddress" />
	      <input class="cssbutton" name="baocun" type="button"  value="保存" onClick="confirmAdd('1');">
	      <input class="cssbutton" name="tibao" type="button"  value="提报" onClick="confirmAdd('2');">
	      <input class="cssbutton" name="shangbao2" type="button" value="返回" onclick="history.back();" />
      </td>
    </tr>
 </table>
  <BR>
  <div id="addressDiv" style="display: none; border: solid #808080 1px; position: absolute"></div>
</form>
</body>
</html>
