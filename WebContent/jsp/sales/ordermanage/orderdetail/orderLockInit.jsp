<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单锁定</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
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

function getAvailableAmount() {
	
	var dealerId = ${dealerId } ;
	var fundTypeId = document.getElementById("typeId").value ;
	if(fundTypeId==null||"null"==fundTypeId||""==fundTypeId){
		MyAlert("ERP和DMS账户类型不匹配!!!");
		document.getElementById("id1save").disabled=true;
	}else{
		document.getElementById("id1save").disabled=false;
	}
	var url = "<%= contextPath%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json" ;
	
	makeCall(url, showAvailableAmount, {dealerId:dealerId, fundTypeId:fundTypeId}) ;
}

function showAvailableAmount(json) {
	document.getElementById("accountId").value = json.accountId ;
	document.getElementById("accountAmount").innerText= amountFormat(json.returnValue);
	document.getElementById("accountCold").innerText= amountFormat(json.returnValue2);
	document.getElementById("accountTotal").innerText= amountFormat(json.returnValue1);
	document.getElementById("availableAmount").value = json.returnValue ;
	var obj3 = document.getElementById("credit_amount");//信用额度
	obj3.innerHTML = amountFormat(json.returnValue3);	
	var obj4 = document.getElementById("fine_amount");//账户余额-信用额度
	obj4.innerHTML = amountFormat(json.returnValue4);
}

	
</script>
</head>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;订单锁定</div>
<body onload="loadcalendar();initOrderWeek();executeQuery();">
	<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>  
			<td align="right" nowrap="nowrap">销售订单号：</td>
			<td align="left" nowrap="nowrap">
				<input name="orderNo" id="orderNo" style="width:220px;"/>
				<select name="orderYearWeek" id="orderYearWeek" style="display:none;" onchange="showData(this.value); ">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
				<span id="data_start" class="innerHTMLStrong"></span> 
				<span id="data_end" class="innerHTMLStrong"></span>
			</td>
			<td align="right" nowrap="nowrap">提报起止时间：</td>
			<td align="left" nowrap="nowrap">
			<div align="left">
       		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   			</div>
				<input type="hidden" name="areaId" id="areaId"/>
				<input type="hidden" name="dealerId" id="dealerId"/>
			</td>
		</tr>
		<tr>
	     	<td align="right" nowrap="nowrap" >选择经销商：</td>
			<td align="left" nowrap="nowrap">
				<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="" id="dealerCodes"/>
			    <input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
			</td>
			<td align="right" nowrap="nowrap" > 物料代码：</td>
			<td align="left" nowrap="nowrap"><input type="text" name="materialCode" id="materialCode" style="width:200px;"/></td> 
	    </tr> 
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><input name="queryBtn" type=button class="cssbutton" onclick="executeQuery();" value="查询"></td>
			<td align="right" nowrap><input value="10" style="width:30px;" id="pageSize" name="pageSize" datatype="0,isDigit,3"/></td>
		</tr>   
	</table>
<br/>
	<table class="table_query" id="table1">
		<tr>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td></td>
			<td><input type="hidden" id="is_same_order" name="is_same_order"   value="1"/></td>
			<td></td>
			<td align = "center" >
				<input type="hidden" name="Amounts" id="Amounts" value=""/>
				<input type="hidden" name="detailIds" id="detailIds" value=""/>
				<input type="hidden" name="priceListIds" id="priceListIds" value=""/>
				<input type="hidden" name="orderIds" id="orderIds" value=""/>
				<input type="hidden" name="areaIds" id="areaIds" value=""/>
				<input type="hidden" name="cAmounts" id="cAmounts" value=""/>
				<input type="hidden" name="materialIds" id="materialIds" value=""/>
				<input type="hidden" name="singlePrices" id="singlePrices" value=""/>
				<input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
				<input type="hidden" id="DISCOUNT_rate_" name="DISCOUNT_rate_" value="" />
				<input type="hidden" id="DISCOUNT_s_price_" name="DISCOUNT_s_price_" value="" />
				<input type="hidden" id="DISCOUNT_price_" name="DISCOUNT_price_" value="" />
				<input type="hidden" id="orderPriceSums" name="orderPriceSums" value="" />
				<input type="hidden" name="ratePara" value="${ratePara}" />
				<input type="hidden" name="orderId" id="orderId" value=""/>
			</td>
		</tr>
	</table>
    <input type="hidden" value="${curDealerId}" id="dealerIds" name="dealerIds"/>
	<div style="overflow:scroll">
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
   <!--分页 end --> 
   </div>
	<table>
		<c:forEach items="${list}" var="list" varStatus="vstatus">
			<tr>
				<input type="hidden" name="priceListId" value="${list.PRICTLIST_ID}" />
			</tr>
		</c:forEach>
	</table>
</form>
<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = null;
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "销售订单号",dataIndex: 'ORDER_NO',align:'center',renderer:orderLink},
				{header: "车系",dataIndex: 'ORDER_NO',align:'center',renderer:seriesLink},
				{header: "物料编码",dataIndex: 'ORDER_NO',align:'center',renderer:matCodeLink},
				{header: "物料名称",dataIndex: 'ORDER_NO',align:'center',renderer:matNameLink},
				{header: "颜色",dataIndex: 'ORDER_NO',align:'center',renderer:matColorLink},
				//{header: "单价",dataIndex: 'ORDER_NO',align:'center',renderer:priceLink},
				{header: "提报数量",dataIndex: 'ORDER_NO',align:'center',renderer:orderAmountLink},
				{header: "已审核数量",dataIndex: 'ORDER_NO',align:'center',renderer:checkAmountLink},
				{header: "已申请数量",dataIndex: 'ORDER_NO',align:'center',renderer:callAmountLink},
				{header: "资源数量",dataIndex: 'AVA_STOCK',align:'center'},
				//{header: "价格合计",dataIndex: 'ORDER_NO',align:'center',renderer:priceTotalLink},
				{header: "操作",dataIndex: 'ORDER_NO',align:'center',renderer:opLink}
		      ];
	function opLink(value,meta,record){
		var str='';
		var isLock=record.data.IS_LOCK;
		if(isLock=='10041002'){
			str+="<a href='#' onclick='lockInit("+record.data.DETAIL_ID+",10041001)'>[锁定资源]</a>";
		}else{
			str+="<a href='#' onclick='lockInit("+record.data.DETAIL_ID+",10041002)'>[解锁资源]</a>";
		}
		return String.format(str);
	}
	
	function lockInit(order_detail_id,isLock){
		var url="<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/lockUpdate.json?orderDetailId="+order_detail_id+"&isLock="+isLock;
		makeFormCall(url, lockResult, "fm") ;
	}
	
	function lockResult(json){
		if(json.flag=='1'){
			MyAlert("操作成功！！！");
			__extQuery__(1);
		}else{
			MyAlert("操作失败！！！");
		}
	
	}
	function executeQuery(){
		var orderYearWeek = document.getElementById("orderYearWeek").value;
		var areaId = document.getElementById("areaId").value;
		var dealerId = document.getElementById("dealerId").value;
		var orderNO = document.getElementById("orderNO").value;
		url = "<%=contextPath%>/sales/ordermanage/orderdetail/OrderOperation/lockDetailQuery.json?&orderYearWeek="+orderYearWeek+"&areaId="+areaId+"&dealerId="+dealerId+"&orderNO="+orderNO;
		__extQuery__(1);
	}
	function orderLink(value,meta,record){
			var str='';
			str+=record.data.ORDER_NO;
			str+='<input type="hidden" name="orderId" value="'+record.data.ORDER_ID+'"/>';
			str+='<input type="hidden" name="allArea" value="'+record.data.AREA_ID+'"/>'
			return String.format(str);
	}
	function seriesLink(value,meta,record){
			var str='';
			str+=record.data.SERIES_NAME;
			str+='<input type="hidden" name="detailId" value="'+record.data.DETAIL_ID+'"/>';
			return String.format(str);
	}
	function matCodeLink(value,meta,record){
			var str='';
			str+=record.data.MATERIAL_CODE;
			str+='<input type="hidden" name="materialId" value="'+record.data.MATERIAL_ID+'"/>';
			return String.format(str);
	}
	function matNameLink(value,meta,record){
			var str='';
			str+=record.data.MATERIAL_NAME;
			return String.format(str);
	}
	function matColorLink(value,meta,record){
			var str='';
			str+=record.data.COLOR_NAME;
			return String.format(str);
	}
	
	function priceLink(value,meta,record){
			var str='';
			str+=record.data.SINGLE_PRICE;
			str+='<input type="hidden" name="singlePrice" value="'+record.data.SINGLE_PRICE+'" id="singlePrice'+record.data.DETAIL_ID+'"/>';
			return String.format(str);
	}
	function orderAmountLink(value,meta,record){
			var str='';
			str+=record.data.ORDER_AMOUNT;
			str+='<input type="hidden" name="orderAmount" value="'+record.data.ORDER_AMOUNT+'"/>';
			return String.format(str);
	}
	function checkAmountLink(value,meta,record){
			var str='';
			str+=record.data.CHECK_AMOUNT;
			str+='<input type="hidden" name="checkAmount" value="'+record.data.CHECK_AMOUNT+'"/>';
			return String.format(str);
	}
	function callAmountLink(value,meta,record){
			var str='';
			str+=record.data.CALL_AMOUNT;
			str+='<input type="hidden" name="callAmount" value="'+record.data.CALL_AMOUNT+'"/>';
			return String.format(str);
	
	}
	
	function applyAmountLink(value,meta,record){
	    var str='';
		 str+='<input type="text" name="applyAmount" id="applyAmount'+record.data.DETAIL_ID+'" datatype="0,is_digit,6"  size="3" value="'+record.data.APPLY_AMOUNT+'"';
		 str+='onchange="'+"toChangeNum("+record.data.SINGLE_PRICE+","+record.data.DETAIL_ID+","+record.data.CHECK_AMOUNT+","+record.data.CALL_AMOUNT+",$(this).value);"+'" />'
		// str+='<input type="hidden" name="detailId" id="detailId'+record.data.DETAIL_ID+'" value="'+record.data.DETAIL_ID+'">';
		 str+='<input type="hidden" name="tempPrice" id="tempPrice'+record.data.DETAIL_ID+'" value="'+record.data.DETAIL_ID+'">';
		return String.format(str);
	}
	function priceTotalLink(value,meta,record){
			var str='';
			str+='<span id="'+record.data.DETAIL_ID+'">'+record.data.TOTAL_PRICE+'</span>';
			str+='<input type="hidden" id="orderPriceSum'+record.data.DETAIL_ID+'" name="orderPriceSum" value="" />';
			return String.format(str);
	}
</script>
<script type="text/javascript" >
	function clrTxt(dealerCodes){
		document.getElementById(dealerCodes).value="";
	}

  	//初始化
	function initOrderWeek(){
			var choice_code = document.getElementById("orderYearWeek").value;
			showData(choice_code);
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
	//判断选择的是否是同一个订单数据
	
	function judgeIfSameOrder(obj){
		//得到所有选择的值，判断值是否有重复
		var allBox=document.getElementsByName("checkOrder");
		var temp=null;
		for(var i=0;i<allBox.length;i++){
			 if(allBox[i].checked){
			 	temp=allBox[i].value;
			 	$("orderId").value=temp;
			 	break;
			 }
		}
		for(var i=0;i<allBox.length;i++){
			if(allBox[i].checked&&temp!=allBox[i].value){
				document.getElementById("is_same_order").value="2";
				break;
			}else{
				document.getElementById("is_same_order").value="1";
				
			}
		}
	}
	
	function setSelectValue(selectBox){
			var cnt = 0;
			var applyAmount = '';
			var detailId = '';
			var materialId = '';
			var singlePrice = '';
			var orderId = '';
			var areaId = '';
			var callAmount ='';
			var orderPriceSum = '';     //订单价格合计
			var Amounts = document.getElementsByName("applyAmount");
			var singlePrices = document.getElementsByName("singlePrice");
			var areaIds = document.getElementsByName("allArea");
			var detailIds = document.getElementsByName("detailId");
			var materialIds = document.getElementsByName("materialId");
			var orderIds = document.getElementsByName("orderId");
			var callAmounts = document.getElementsByName("callAmount");
			var orderPriceSums = document.getElementsByName("orderPriceSum");
			var priceListIds = document.getElementsByName("priceListId");
			var priceListId='';
			for(var i=0 ;i< Amounts.length; i++){
				var flag=judgeArrayContain(selectBox,detailIds[i].value);//id包含在数组中就加入值，否则不加
				if(flag){
					cnt = cnt+Amounts[i].value;
					applyAmount = Amounts[i].value + ',' + applyAmount;
					singlePrice = singlePrices[i].value + ',' + singlePrice;
					detailId = detailIds[i].value + ',' + detailId;
					materialId = materialIds[i].value + ',' + materialId;
					orderId = orderIds[i].value + ',' + orderId;
					areaId = areaIds[i].value + ',' + areaId;
					callAmount = callAmounts[i].value + ',' +callAmount;
					orderPriceSum = orderPriceSums[i].value+','+orderPriceSum;			//订单价格合计
					priceListId=priceListIds[i].value+','+priceListId;
				}
			}
			document.getElementById("Amounts").value = applyAmount;
			document.getElementById("detailIds").value = detailId;
			document.getElementById("materialIds").value = materialId;
			document.getElementById("singlePrices").value = singlePrice;
			document.getElementById("orderIds").value = orderId;
			document.getElementById("areaIds").value = areaId;
			document.getElementById("cAmounts").value = callAmount;
			document.getElementById("orderPriceSums").value = orderPriceSum;
			document.getElementById("priceListIds").value = priceListId;
			if(cnt==0){
				MyAlert("申请数量不能为零！");
	            return;
			}
	}
	//判断数组中是否包含某个值
	function judgeArrayContain(a,value){
		var flag=false;
		for(var i=0;i<a.length;i++){
			if(a[i]==value){
				flag=true;
				break;
			}
		}
		return flag;
	}
	//点击提报申请的时候弹出申请选择的数据
	
	function applySubmit(){
		if(!submitForm('fm')){
			return;
		}
		var sameValue=document.getElementById("is_same_order").value;
		var flag=false;
		var allBox=document.getElementsByName("checkOrder");
		var temp=null;
		var selectBox=new Array();
		for(var i=0;i<allBox.length;i++){
			 if(allBox[i].checked){
			 	selectBox[selectBox.length]=allBox[i].getAttribute("detailId");
			 	flag=true;
			 }
		}
		setSelectValue(selectBox);
		if(!flag){
			MyAlert("请选择其中一条进行操作！！！")
			return ;
		}
		if(sameValue=="2"){
			MyAlert("请选择同一个订单操作！！！");
			return;
		}
		var orderId=$("orderId").value;
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/delivery/DeliveryApply/queryDealerDelvInit.do?orderId='+orderId,900,400);
	}
	//本次申请数量与价格合计联动
	function toChangeNum(value1,value2,value3,value4,value5){
		var price = Number(value1);
		var detailId = Number(value2);
		var checkAmount = Number(value3);
		var callAmount = Number(value4);
		var applyAmount = Number(value5);
		if(price == 0){
			MyAlert("价格未维护，不能申请！");
			document.getElementById("applyAmount"+detailId).value="0";
			document.getElementById("applyAmount"+detailId).focus();
		}

		if(applyAmount>(checkAmount-callAmount)){
			MyAlert("本次申请数量过大，请重新输入！");
			document.getElementById("applyAmount"+detailId).value="0";
			document.getElementById("applyAmount"+detailId).focus();
		}else{
			var sumAccount = applyAmount*price;
			<c:forEach items="${list}" var="list">
	        var id = <c:out value="${list.DETAIL_ID}"/>
	        if(detailId==Number(id)){
	       	 document.getElementById(detailId).innerText=amountFormat(sumAccount);
	       	 document.getElementById("orderPriceSum"+detailId).value=sumAccount;
	        }
	        </c:forEach>
	       // var discount_rate = document.getElementById("discount_rate"+detailId).value;
	      	 var  discount_rate=0;
	        var single_Price = document.getElementById("tempPrice"+detailId).value;
	        var applyAmount = document.getElementById("applyAmount"+detailId).value;
	        discountRateChange(detailId,discount_rate,single_Price,applyAmount)
		}
	}
	function discountRateChange(detail_id,discount_rate,single_Price,applyAmount){
		var price_sum = 0;		 //价格合计
		price_sum = parseFloat(single_Price)*parseFloat(applyAmount);
		document.getElementById("totailAccount").value = round(price_sum,2);
		document.getElementById(detail_id).innerHTML = amountFormat(round(price_sum,2));
		document.getElementById("orderPriceSum"+detail_id).value=round(price_sum,2);
		changeAllAccount();
	}
	function round(number,fractionDigits){   
		   with(Math){   
		       return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
		    }   
	}
	
</script>
</body>
</html>