<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>下级经销商周度常规订单启票</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
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
//-->
</script>
</head>
<body>
<form method="post" name="fm" id="fm">
	<%	
		List list = (List)request.getAttribute("list");
		if(list.size()==0||list==null){
	%>
	<div class='pageTips'>没有满足条件的数据</div>
	<%}else{ %>
	<div id="detailDiv" style="overflow:scroll">
	<table class="table_list" width="100%">
		<tr class="cssTable">
			<th align="center" nowrap="nowrap">车系</th>
			<th align="center" nowrap="nowrap">物料编号</th>
			<th align="center" nowrap="nowrap">物料名称</th>
			<th align="center" nowrap="nowrap">资源情况</th>
			<th align="center" nowrap="nowrap">提报数量</th>
			<th align="center" nowrap="nowrap">已启票量</th>
			<th align="center" nowrap="nowrap">已申请量</th>
			<th align="center" nowrap="nowrap">一级未审量</th>
			<th align="center" nowrap="nowrap">本次提交数量</th>
		</tr>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
			<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
				<td>
					${list.SERIES_NAME}
					<input type="hidden" name="detailId" value="${list.DETAIL_ID}"/>
					<input type="hidden" name="orderId" value="${list.ORDER_ID}"/>
					<input type="hidden" name="areaId" value="${list.AREA_ID}"/>
					<input type="hidden" name="materialId" value="${list.MATERIAL_ID}"/>
					<input type="hidden" name="orderAmount" value="${list.ORDER_AMOUNT}"/>
					<input type="hidden" name="callAmount" value="${list.CALL_AMOUNT}"/>
					<input type="hidden" name="reqAmount" value="${list.REQ_AMOUNT}"/>
					<input type="hidden" name="ncAmount" value="${list.NC_AMOUNT}"/>
				</td>
				<td>${list.MATERIAL_CODE}</td>
				<td>${list.MATERIAL_NAME}</td>
				<td>${list.RAMOUNT}</td>
				<td>${list.ORDER_AMOUNT}</td>
				<td>${list.CALL_AMOUNT}</td>
				<td>${list.REQ_AMOUNT}</td>
				<td>${list.NC_AMOUNT}</td>
				<td>
					<input type="text" name="applyAmount" id="applyAmount${list.DETAIL_ID}" datatype="0,is_digit,6"  size="3" value="0" onchange="priceTotal();"></input>
				</td>
			</tr>
		</c:forEach>
		<tr class="table_list_row1">
			<td></td>
			<td></td>
			<td><strong>合计：</strong></td>
			<td></td>
			<td id="heji1"></td><!-- 提报数量合计 -->
			<td id="heji2"></td><!-- 已启票数量合计 -->
			<td id="heji4"></td><!-- 已申请数量合计 -->
			<td id="heji5"></td><!-- 已申请数量合计 -->
			<td id="heji3"></td><!-- 本次提交数量合计 -->
		</tr>
	</table>
	</div>
	<br>
	<table class="table_query" id="table1">
		<tr class= "tabletitle">
			<td align="right" id="a1">资金类型：</td>
			<td align="left" id="a2">
				<select name="typeId" id="typeId" class="short_sel">
				</select>
			</td>
			<td align="right"></td>
			<td align="left"></td>
			<td align="right"></td>
			<td align="left"></td>
			<td align="left"></td>
		</tr>
		<tr class="tabletitle">
      		<td align="right"  nowrap="nowrap">是否代交车：</td>
      		<td colspan="3" align="left">
      		<input type="checkbox" name="isCover" id="isCover" onclick="showFleetInfo();" value="" />
      		</td>
    	</tr>
    	<tr id="cusTr" style="display:none">
			<td align="right" class="cssTable" id="ssss1">选择大客户：</td>
			<td align="left" class="cssTable" id="ssss2">
				<input id="fleetName" name="fleetName" readonly="readonly" type="text" datatype="0,is_noquotation,30" />
				<input id="fleetId" name="fleetId" type="hidden"/>				
				<input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
				<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td align="right" class="cssTable" id="address03">运送地址：</td>
			<td align="left" class="cssTable" id="address04">
				<input id="address" name="address" size="50" type="text" datatype="0,is_noquotation,50"/>
			</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class= "tabletitle" id="tran_id">
			<td align ="right">运输方式：</td>
			<td align ="left" id="trantype" colspan="6">
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"<%=Constant.TRANSPORT_TYPE_02%>",false,"short_sel","onchange='addressHide(this.options[this.options.selectedIndex].value);'","false",'');
					</script>
				</label>
			</td>
		</tr>
		<tbody id="addTr">
			<tr class= "tabletitle">
				<td align ="right" >收货方：</td>
				<td align ="left" colspan="6">
					<select name="receiver" onchange="getAddressList();">
			      	</select>
				</td>
			</tr>
			<tr class= "tabletitle">
				<td align ="right" >运送地址：</td>
				<td align ="left">
					<select name="addressId" onchange="getAddressInfo(this.options[this.options.selectedIndex].value);">
			      	</select>
				</td>
				<td align="right" class="cssTable">收车单位：</td>
				<td align="left" class="cssTable" id="receiveOrg" colspan="4">
				</td>
			</tr>
			<tr class= "tabletitle">
				<td align ="right" >联系人：</td>
				<td align ="left">
					<input name="linkMan" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
				<td align="right" class="cssTable">联系电话：</td>
				<td align="left" class="cssTable" colspan="4">
					<input name="tel" type="text" class="middle_txt" size="30" maxlength="30" />
				</td>
			</tr>
		</tbody>
		<tr>
				<td align="right">备注说明：</td>
				<td align="left" colspan="3"><textarea name="orderRemark" id="orderRemark" cols="60" rows="3" onkeyup="value = value.replace(/#/g, '') ;" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/#/g,''));" onkeydown="if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){} else {limitChars('orderRemark', 50);}" onchange="limitChars('orderRemark', 50)"></textarea><font color="red">建议录入不超过50个汉字!</font></td>
			</tr>
		<tr>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
			<td align ="left">
				<input class="normal_btn" name="queryBtn" type="button" value="启票" onclick="applySubmit();">
				<input type="hidden" name="dealerId" value="${dealerId}"/>
				<input type="hidden" name="firstDlr" value="${firstDlr}"/>
				<input type="hidden" name="reqTotalAmount" value="0"/>
			</td>
			<td align = "left" ><label></label></td>
		</tr>
	</table>
	<%} %>
</form>
<script type="text/javascript" >
	//初始化
	function doInit(){
		getFund();
		getReceiverList();
		priceTotal() ;
		var list = "<%=request.getAttribute("list")%>";
		if(list.substring(1,list.length-1)){
			document.getElementById("table1").style.display = "";
		}else{
			document.getElementById("table1").style.display = "none";
		}
	}
	
	// 合计价格
	function priceTotal(){
		var orderTotal = 0;
		var callTotal = 0;
		var applyTotal = 0;
		var reqTotal = 0;
		var ncTotal = 0;
		
		var orderAmount = document.getElementsByName("orderAmount");
		var callAmount = document.getElementsByName("callAmount");
		var applyAmount = document.getElementsByName("applyAmount");
		var reqAmount = document.getElementsByName("reqAmount");
		var ncAmount = document.getElementsByName("ncAmount");
		
		for(var i=0; i<orderAmount.length; i++){
			var order = parseInt(orderAmount[i].value, 10);
			var call = parseInt(callAmount[i].value, 10);
			var apply = parseInt(applyAmount[i].value, 10);
			var req = parseInt(reqAmount[i].value, 10);
			var nc = parseInt(ncAmount[i].value, 10);
			
			orderTotal += order;
			callTotal += call;
			applyTotal += apply;
			reqTotal += req ;
			ncTotal += nc ;
		}
		
		document.getElementById("heji1").innerHTML = orderTotal;//提报数量合计
		document.getElementById("heji2").innerHTML = callTotal;//已启票数量合计
		document.getElementById("heji3").innerHTML = applyTotal;//本次提交数量合计
		document.getElementById("heji4").innerHTML = reqTotal;//本次申请数量合计
		document.getElementById("heji5").innerHTML = ncTotal;//本次申请数量合计
		document.getElementById("reqTotalAmount").value = applyTotal;
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
			addressHide(document.getElementById("transportType").value);
			document.getElementById("isCover").value = 0;
		}
	}
	
	function getFund(){
		var dealerId = ${firstDlr};
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
		makeCall(url,showFund,{dealerId:dealerId});
	}
	
	function showFund(json){
		showFundTypeList(json);
	}
	
	//资金类型列表显示
	function showFundTypeList(json){
		var obj = document.getElementById("typeId");
		obj.options.length = 0;
		for(var i=0;i<json.fundTypeList.length;i++){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
		}
	}
	
	//获得收货方列表
	function getReceiverList(){	
		var dealerId = ${dealerId};
		if(document.getElementsByName("areaId").length > 0) {
			var areaId = document.getElementsByName("areaId")[0].value ;
		
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getReceiverList.json";
			makeCall(url,showReceiverList,{dealerId:dealerId, areaId:areaId});
		} 
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
	
	//获得地址列表
	function getAddressList(){	
		var dealerId = document.getElementById("receiver").value;
		var areaId = document.getElementsByName("areaId")[0].value ;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAddressList.json";
		makeCall(url,showAddressList,{dealerId:dealerId,areaId:areaId});
	}	
	
	function showAddressList(json){
		var obj = document.getElementById("addressId");
		obj.options.length = 0;
		for(var i=0;i<json.addressList.length;i++){
			obj.options[i]=new Option(json.addressList[i].ADDRESS, json.addressList[i].ID + "");
		}
		
		getAddressInfo(document.getElementById("addressId").value);
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
	
	//根据运输方式隐藏地址列表
	function addressHide(arg){
		var obj1 = document.getElementById("addTr");
		if(arg == '<%=Constant.TRANSPORT_TYPE_02%>') {
			obj1.style.display = "inline";
		}
		
		else{
			obj1.style.display = "none";
		}
	}
	
	//申请提醒
	function applySubmit(){
		if(submitForm('fm')){
			if(!testLen(document.getElementById('orderRemark').value)) {
				MyAlert('备注录入已超过50个汉字的最大限！') ; 
				return false ;
			}
			
			var reqTotalAmount = document.getElementById("reqTotalAmount").value;
			if(reqTotalAmount==0){
				MyAlert("申请数量不能为零！");
	            return;
			}
			
			var detailId = document.getElementsByName("detailId");
			var orderAmount = document.getElementsByName("orderAmount");
			var callAmount = document.getElementsByName("callAmount");
			var applyAmount = document.getElementsByName("applyAmount");
			var reqAmount = document.getElementsByName("reqAmount");
			var ncAmount = document.getElementsByName("ncAmount");

			for(var i=0; i<detailId.length; i++){
				if(parseInt(applyAmount[i].value) > 0  &&  parseInt(applyAmount[i].value) > parseInt(orderAmount[i].value) - parseInt(callAmount[i].value) - parseInt(ncAmount[i].value)){
					MyAlert("本次提报数量不能大于提报数量减去已启票量和一级未审量之和！");
					return;
				}
			}
			MyConfirm("确认申请？",putForword);
		}
	}
	//申请发运
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderreport/LowWeekGeneralOrderCall/applySubmit.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("申请成功！");
			window.parent.toQuery();
		} else if(json.returnValue == '2') {
			window.parent.MyAlert("申请失败：物料" + json.metStr + "允许启票数量已超出！");
			window.parent.toQuery();
		} else{
			MyAlert("申请失败！请联系系统管理员！");
		}
	}
	//清除按钮
  	function toClear(){
		document.getElementById("fleetName").value="";
		document.getElementById("fleetId").value="";
  	}
    //大用户弹出
  	function showFleet(){
  		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
  	}
</script>
</body>
</html>