<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做审核 > 订做车需求价格核定</div>
<form method="POST" name="fm" id="fm">
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">需求数量</th>
			<th nowrap="nowrap">预计交付周期</th>
			<th nowrap="nowrap">订做批次号</th>
			<th nowrap="nowrap">价格</th>
			<th nowrap="nowrap">价格变动</th>
			<th nowrap="nowrap">变动后价格</th>
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.AMOUNT}<input type="hidden" name="amo" id="amo${po.DTL_ID}" value="${po.AMOUNT}"></td>
		      <td align="center">${po.EXPECTED_PERIOD}天</td>
		      <td align="center">${po.BATCH_NO}</td>
		      <td align="center">
		      <span id="pri${po.DTL_ID}" >
		      <c:if test="${po.PRICE > MaxMoney}">
		      		价格未维护
		      	</c:if>
		      	<c:if test="${po.PRICE <= MaxMoney}">
		      		${po.PRICE} 
		      	</c:if>
		      </span>
		      <c:if test="${po.PRICE > MaxMoney}">
		      		 <input type="hidden" name="salePrice" id="pr${po.DTL_ID}" value="价格未维护"/>
		      	</c:if>
		      	<c:if test="${po.PRICE <= MaxMoney}">
		      		 <input type="hidden" name="salePrice" id="pr${po.DTL_ID}" value="${po.PRICE}"/>
		      	</c:if>
		     </td>
		      <td align="center">
		      <input type="text" class="short_txt" name="price" id="price${po.DTL_ID}" type="1,is_digit,12" size="12" onchange="chgPrice('pr'+${po.DTL_ID},'price'+${po.DTL_ID},'lab'+${po.DTL_ID},'amo'+${po.DTL_ID}, ${po.DTL_ID}) ;"/>
		      <input type="hidden" name="dealerId" id="delaerId" value="${parentDlrId}">
		      <input type="hidden" name="reqId" id="reqId" value="${reqId }">
		      <input type="hidden" name="total" id="total${po.DTL_ID}" value="" />
		      <input type="hidden" name="detailId" value="${po.DTL_ID}"/>
		      <input type='hidden' id='materialId' name='materialId' value='${po.MATERIAL_ID}'></td>
		      <td align="center"><label id="lab${po.DTL_ID}">
		      <c:if test="${po.PRICE > MaxMoney}">
		      		价格未维护
		      	</c:if>
		      	<c:if test="${po.PRICE <= MaxMoney}">
		      	</c:if>
		      </label></td>
		    </tr>
    	</c:forEach>
	</table>
	<br>	
	<c:if test="${attachList!=null}">
	<br>
	<table id="attachTab" class="table_info">
		<tr>
	        <th colspan="2">附件列表：<input type="hidden" id="fjids" name="fjids"/>
			</th>
		</tr>
	  		<c:forEach items="${attachList}" var="attls">
			    <tr class="table_list_row1" id="${attls.FJID}">
			    <td colspan="2"><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
			    </tr>
			</c:forEach>
	</table>
	</c:if>
	<br />
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">日期</th>
			<th nowrap="nowrap">单位</th>
			<th nowrap="nowrap">操作人</th>
			<th nowrap="nowrap">审核结果</th>
			<th nowrap="nowrap">审核描述</th>
		</tr>
    	<c:forEach items="${checkList}" var="checkList">
    		<tr class="table_list_row2">
		      <td align="center">${checkList.CHECK_DATE}</td>
		      <td align="center">${checkList.ORG_NAME}</td>
		      <td align="center">${checkList.NAME}</td>
		      <td align="center">${checkList.CHECK_STATUS}</td>
		      <td align="center">${checkList.CHECK_DESC}</td>
		    </tr>
    	</c:forEach>
	</table>
	<br />
	<table class=table_query>
		<tr class=cssTable>
			<td width="8%" align="right">资金类型：</td>
			<td align="left" nowrap>
				<select name="fundType" class="long_sel" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
	      		</select>
			</td>
			<td width="8%" align="right">可用余额：</td>
			<td align="left" id="availableAmount" nowrap></td>
		</tr>
		<tr class=cssTable>
			<td width="8%" align="right">预付款：</td>
			<td align="left" colspan="3"  nowrap>
				<input id="preAmount" name="preAmount" type="text" class="short_txt" size="10" maxlength="30" value="0" datatype='0,isMoney,10' style="text-align:right"/>
			</td>
		</tr>
		<tr class = "tabletitle">
     	 <td align = "right" >价格类型：</td>
     	 <td align = "left" colspan="7">
      		<select id="priceId" name="priceId" onChange="isShowPrice();">
       	 </select>
     	 </td>
    	</tr>
		<tr class=cssTable>
			<td width="8%" align="right">集团客户：</td>
			<td width="50%" colspan="3" align="left"  nowrap>${fleetName}</td>
		</tr>
		<tr class=cssTable>
			<td width="8%" align="right">改装说明：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark1" id="remark1" rows="4" cols="50" disabled="disabled"><c:out value="${remark}"/></textarea></td>
		</tr>
		<!--<tr class=cssTable>
			<td width="8%" align="right">产品审核描述：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark2" id="remark2" rows="4" cols="50" disabled="disabled"><c:out value="${remark2}"/></textarea></td>
		</tr>
		--><tr class=cssTable>
			<td width="8%" align="right">审核描述：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark" id="remark" rows="4" cols="50"></textarea></td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="detailIds" id="detailIds"/>
				<input type="hidden" name="prices" id="prices"/>
				<input type="hidden" name="salePrices" id="salePrices"/>
				<input type="hidden" name="flag" id="flag"/>
				<input type="hidden" name="reqId" id="reqId" value="${reqId}"/>
				<input type="hidden" name="dealerId" id="dealerId" value="${parentDlrId}"/>
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd('0');" value="核价完成" id="queryBtn1" />
				<input type="button" name="button2" class="cssbutton" onclick="confirmAdd('1');" value="驳回" id="queryBtn2" /> 
				<input type="button" name="button3" class="cssbutton" onclick="toBack();" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">

	function doInit(){
		getFundTypeList();
		getPriceList();//获得价格类型列表
	}

	//获得价格类型列表
	function getPriceList(){	
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getPriceList.json";
		makeCall(url,showPriceList,{dealerId:${parentDlrId}});
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
		isShowPrice() ;
		// priceTypeChange();
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
			var obj2 = document.getElementById("lab" + id);
			var obj3 = document.getElementById("pr" + id);
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




	
	
	function chgPrice(value1,value2,value3,value4,value5) {
		var total = 'total' + value5 ;
		
		if (document.getElementById(value1).value == '价格未维护') {
			document.getElementById(value3).innerHTML = '价格未维护' ;

			return false ;
		}
		var chgPrice = parseFloat(document.getElementById(value1).value) + parseFloat(document.getElementById(value2).value) ;
		
		if(chgPrice) {
			document.getElementById(value3).innerHTML = chgPrice ;
			document.getElementById(total).value = chgPrice * parseFloat(document.getElementById(value4).value);
		}
		else {
			document.getElementById(value3).innerHTML = '' ;
		}
	}




	
	//提交校验
	function confirmAdd(value){
		var prices = '';
		var detailIds ='';
		var salePrices='';
		var price = document.getElementsByName("price");
		var salePrice = document.getElementsByName("salePrice");
		var detailId = document.getElementsByName("detailId");
		for(var i=0 ;i< detailId.length; i++){
			if(value==0){
				if(salePrice[i].value == '价格未维护') {
					MyDivAlert("价格未维护，不能核价！");
					return false;
				}
				if(!price[i].value){
					MyDivAlert("请输入变动价格！");
					return false;
				}
			}
			salePrices = salePrice[i].value + ',' + salePrices;
			prices = price[i].value + ',' + prices;
			detailIds = detailId[i].value + ',' + detailIds;
		}
		if(value==1&&document.getElementById("remark").value.trim()==""){
			MyDivAlert("请输入审核描述！");
			return false;
		}
		document.getElementById("prices").value=prices;
		document.getElementById("salePrices").value=salePrices;
		document.getElementById("detailIds").value=detailIds;
		document.getElementById("flag").value = value;
		MyDivConfirm("确认提交？",toAdd);
	}
	//提交
	function toAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedPriceCheck/specialNeedPriceDetailCheck.json',showResult,'fm');
	}
	//返回
	function toBack(){
		parent._hide();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			try
			{
				var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
				var tab = parent.$('inIframe').contentWindow.tabObj;
				tab.rows(rowIndex).removeNode(true);
			}catch(e){}
			parent._hide();
			parent.MyAlert("操作成功！");
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	

	function isShowPrice() {
		var reqId = document.getElementById("reqId").value;
		var priceId = document.getElementById("priceId").value ;
		var dtlIds = document.getElementsByName('detailId') ;
		
		for (var i=0; i<dtlIds.length; i++) {
			var str = reqId+','+priceId +','+dtlIds[i].value;
			var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedPriceCheck/specialNeedPriceDetailCheckInitA.json";
			makeCall(url,showPrice,{str:str});
		}
	}
	function showPrice(json) {
		var pri = 'pri' + json.delId ;
		var pr = 'pr' + json.delId ;
		var price = 'price' + json.delId ;
		var lab = 'lab' + json.delId ;
		var amo = 'amo' + json.delId ;
		var sNaNPrice = '价格未维护' ;

		if(json.priceA > <%= Constant.MATERIAL_PRICE_MAX %>) {
			document.getElementById(pri).innerText = sNaNPrice ;
			document.getElementById(lab).innerText = sNaNPrice ;
			document.getElementById(pr).innerText = sNaNPrice ;

			return false ;
		} else {
			document.getElementById(pri).innerText = json.priceA ;
			document.getElementById(pr).innerText = json.priceA ;
		}
		chgPrice(pr, price, lab, amo, json.delId) ;
	}
	// 获取预付款金额
	/*function getPreAmount() {
		var totals = document.getElementsByName('total') ;
		var fTotalAmount = 0 ;

		for(var i=0; i<totals.length; i++) {
			fTotalAmount += parseFloat(totals[i].value) ;
		}

		document.getElementById('preAmount').value = fTotalAmount ;
	}*/
	
	function getFundTypeList(){
		var dealerId = document.getElementById("dealerId").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFundTypeList.json";
		makeCall(url,showFundTypeList,{dealerId:dealerId});
	}
	
	//资金类型列表显示
	function showFundTypeList(json){
		var obj = document.getElementById("fundType");
		obj.options.length = 0;
		for(var i=0;i<json.fundTypeList.length;i++){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + "");
		}
		
		getAvailableAmount(document.getElementById("fundType").value);
	}
	
	function getAvailableAmount(arg){
		var dealerId = document.getElementById("dealerId").value;
		var fundTypeId = arg;
		
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
		makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
	}
	
	function showAvailableAmount(json){
		var obj = document.getElementById("availableAmount");
		obj.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
	}
</script>
</body>
</html>
