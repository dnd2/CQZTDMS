

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<script type="text/javascript">
//选择经销商(1) 接收单位(2)地址(3)
function selSales(inputId,inputCode,inputName,inputLinkMan,inputTel,inputPostCode,inputStation,dealerId,type){
		if(!inputId){ inputId = null;}
		if(!inputCode){ inputCode = null;}
		if(!inputName){ inputName = null;}
		if(!inputLinkMan){ inputLinkMan = null;}
		if(!inputTel){ inputTel = null;}
		if(!inputPostCode){ inputPostCode = null;}
		if(!inputStation){ inputStation = null;}
		if(!dealerId){ dealerId = null;}
		if(!type){ type = null;}
	    OpenHtmlWindow(g_webAppName+'/jsp/parts/salesManager/carFactorySalesManager/selSales.jsp?dealerId='+dealerId+'&type='+type+'&inputName='+inputName+'&inputId='+inputId+'&inputCode='+inputCode+'&inputLinkMan='+inputLinkMan+'&inputTel='+inputTel+'&inputPostCode='+inputPostCode+'&inputStation='+inputStation,730,390);
}
//获取序号
function getIdx(){
	document.write(document.getElementById("file").rows.length-2);
}
//获取CHECKBOX
function getCb(partId,boLineId){
	document.write("<input type='checkbox' id='cell_"+(document.getElementById("file").rows.length-3)+"' name='cb' checked onclick='countAll()' value='"+partId+"' style='display:none'/>");
}

function chkPart1(){
	var cks = document.getElementsByName('cb');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckAll").checked = flag;
	countAllMoney();
}

//计算所有CHECK的金额
function countAll(){
var flag = true;
	var amountCount=parseFloat(0);
		var cb = document.getElementsByName("cb");
		for(var i=0 ;i<cb.length;i++){
			if(""!=cb[i].value&&null!=document.getElementById("buyAmount_"+cb[i].value)&&""!=document.getElementById("buyAmount_"+cb[i].value).value){
				//只有CHECKED的才能计算
				if(cb[i].checked){
					amountCount = (parseFloat(unFormatNum(amountCount))+parseFloat(unFormatNum(document.getElementById("buyAmount_"+cb[i].value).value))).toFixed(2);
				}
			}
			if(!cb[i].checked){
				flag =false;	
			}
		}
		document.getElementById("ckAll").checked=flag;
		document.getElementById("Amount").value=formatNum(amountCount);
}
function MyAlert(info){
		 var owner = getTopWinRef();
		 try{
		  _dialogInit();
		  var height=200 ;
		  if(info.split('</br>').length>=6){
		 	height = height + (info.split('</br>').length-6)*10;
		  }
		  if(info.split('</br>').length>6){   //如果有6个就会过长
			  var infoR ="";
		  	  var infoArr = info.split('</br>');
		  	  for(var i=0;i<6;i++){
		  		infoR +=  infoArr[i]+"</br>";
		  	  }
		  	  infoR +="......</br>";
		  	  info = infoR;
		  }
		  owner.getElementById('dialog_content_div').innerHTML='\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
		  owner.getElementById('dialog_alert_info').innerHTML=info;
		  owner.getElementById('dialog_alert_button').onclick=_hide;
		  
		   _setSize(300,height);
		    
		  _show();
		 }catch(e){
		  MyAlert('MyAlert : '+ e.name+'='+e.message);
		 }finally{
		  owner=null;
		 }
	}
function countMoney(obj,price,partId){
	price = unFormatNum(price);
	//获取折扣率
	var discount = document.getElementById("DISCOUNT").value;
	if(discount==null||discount==""){
		discount = 1;
	}
	if(obj){
		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		var value = obj.value;
		var len = tbl.rows[idx].cells.length;
		var money = (parseFloat(price)*parseFloat(value)*discount).toFixed(2);
		tbl.rows[idx].cells[len-2].innerHTML = createCellsInnerHtml(("buyAmount_"+partId),("buyAmount_"+partId),"","","hidden",formatNum(money),false,false,"");
		
		var amountCount=parseFloat(0);
		var cb = document.getElementsByName("cb");
		for(var i=0 ;i<cb.length;i++){
			if(""!=cb[i].value&&null!=document.getElementById("buyAmount_"+cb[i].value)&&""!=document.getElementById("buyAmount_"+cb[i].value).value){
				//只有CHECKED的才能计算
				if(cb[i].checked){
					amountCount = (parseFloat(amountCount)+parseFloat(unFormatNum(document.getElementById("buyAmount_"+cb[i].value).value))).toFixed(2);
				}
			}
		}
		document.getElementById("Amount").value=formatNum(amountCount);
	}
}

//计算总金额
function countAllMoney(){
	var amountCount=parseFloat(0);
	var cb = document.getElementsByName("cb");
	for(var i=0 ;i<cb.length;i++){
		if(""!=cb[i].value&&null!=document.getElementById("buyAmount_"+cb[i].value)&&""!=document.getElementById("buyAmount_"+cb[i].value).value){
			//只有CHECKED的才能计算
			if(cb[i].checked){
				amountCount = (parseFloat(amountCount)+parseFloat(unFormatNum(document.getElementById("buyAmount_"+cb[i].value).value))).toFixed(2);
			}
		}
	}
	document.getElementById("Amount").value=formatNum(amountCount);
}

//生成CELL中的HTML
function createCellsInnerHtml(id,name,onchangeEvent,style,type,value,trHFlag,trEFlag,hiddenHtml){
	var tdStrHead=trHFlag?'<tr><td align="center" nowrap>':'<td align="center" nowrap>';
	var tdStrEnd=trEFlag?'</td></TR>':'</td>';
	var inputHtml="";
	if(type=='button'){
		inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
	}
	if(type=='text'){
		onchangeEvent=onchangeEvent==''?'':('onchange="'+onchangeEvent+'"');
		inputHtml = '<input  type="text" class="short_txt"  id="'+id+'" name="'+name+'" '+onchangeEvent+' />';
	}
	if(type=='hidden'){
		inputHtml = '<input  type="hidden"  id="'+id+'" name="'+name+'" value="'+value+'" />'+value;
	}
	if(type=='checkbox'){
		inputHtml = '<input  type="checkbox"   id="'+id+'" name="cb" checked="true" value="'+value+'" />';
	}
	if(type=='idx'){
		inputHtml = value;
	}
	if(hiddenHtml!=""){
		inputHtml+=hiddenHtml;
	}
	return tdStrHead+inputHtml+tdStrEnd;
}
function repToFactoryConfirm(){
	MyConfirm("确定提交给车厂?",repToFactory,[]);
}
function repToFactory(){
	disableAllBtn();
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/repToFactory.json?";	
	sendAjax(url,getResult,'fm');
}
function createSalesOrderConfirm(){
	var msg = "";
	var uncheckedId = "";
	if(""==document.getElementById("wh_id").value){
		msg +="请选择出库仓库!</br>";
	}
	if(""==document.getElementById("RCV_ORG").value){
		msg +="请选择接收单位!</br>";
	}
	if(""==document.getElementById("ADDR").value){
		msg +="请选择接收地址!</br>";
	}
	if(""==document.getElementById("RECEIVER").value){
		msg +="接收人不能为空!</br>";
	}
	if(""==document.getElementById("TEL").value){
		msg +="接收人电话不能为空!</br>";
	}
	if(""==document.getElementById("POST_CODE").value){
		msg +="邮政编码不能为空!</br>";
	}
	if(""==document.getElementById("STATION").value){
		msg +="到站名称不能为空!</br>";
	}
	if(""==document.getElementById("TRANS_TYPE").value){
		msg +="请选择发运方式!</br>";
	}
	if(""==document.getElementById("PAY_TYPE").value){
		msg +="请选择付款方式!</br>";
	}
	if(""==document.getElementById("ORDER_TYPE").value){
		msg +="请选择订单类型!</br>";
	}
	if(""==document.getElementById("transpayType").value){
		msg +="请选择运费支付方式!</br>";
	}
	var selFlag = false;
	var cb = document.getElementsByName("cb");
		for(var i =0 ;i<cb.length;i++){
			if(cb[i].checked){
				selFlag = true;
				var salQty = document.getElementById("saleQty_"+cb[i].value).value;
				//需要校验销售量是否填写
				if(salQty==""||salQty=="0"){
						msg += "请填写第"+(document.getElementById("saleQty_"+cb[i].value).parentElement.parentElement.rowIndex-1)+"行的销售数量!</br>";
				}else{

					var pattern1 = /^[1-9][0-9]*$/;
		            if (!pattern1.exec(salQty)) {
		            	msg += "第"+(document.getElementById("saleQty_"+cb[i].value).parentElement.parentElement.rowIndex-1)+"行的销售数量必须是正整数!</br>";
		            }
					
					if(parseFloat(salQty)>parseFloat(document.getElementById("buyQty_"+cb[i].value).value)){
						msg += "第"+(document.getElementById("saleQty_"+cb[i].value).parentElement.parentElement.rowIndex-1)+"行的销售数量不得大于订货数量!</br>";
					}
					
					if(parseFloat(salQty)>parseFloat(document.getElementById("stockQty_"+cb[i].value).value)){
						msg += "第"+(document.getElementById("saleQty_"+cb[i].value).parentElement.parentElement.rowIndex-1)+"行的销售数量不得大于当前库存数量!</br>";
					}
					
					//var  mod =  document.getElementById("saleQty_"+cb[i].value).value%document.getElementById("minPackage_"+cb[i].value).value;
	                
	                if('${flag}'=='1'){
	                	var  vender =  document.getElementById("vender_"+cb[i].value).value;
	                	if(vender==""){
	                		msg += "第"+ (i + 1)+"行的的供应商不能为空!</br>";
	                	}
	                }
	                
				}
				if(document.getElementById("ORDER_TYPE").value!=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>&&parseFloat(document.getElementById("stockQty_"+cb[i].value).value)<=0){
					msg += "第"+(document.getElementById("saleQty_"+cb[i].value).parentElement.parentElement.rowIndex-1)+"行配件的库存不足!</br>";
				}
			}else{
				uncheckedId += ","+cb[i].value;
				document.getElementById("saleQty_"+cb[i].value).value = 0;
				cb[i].disabled=true;
			}
		}
		if(!selFlag){
			msg += "请选择配件!</br>";
		}
	if(msg!=""){
		MyAlert(msg);
		enableCb();
		return;
	}
	MyConfirm("确定生成销售单?",createSaleOrder,[uncheckedId]);
	enableCb();
}

function createSaleOrder(uncheckedId){
	disableAllBtn();
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/createSaleOrder.json?uncheckedId="+uncheckedId+"&isAll=1";	
	sendAjax(url,getResult,'fm');
	
}
function getResult(jsonObj){
	enableAllBtn();
	var cb = document.getElementsByName("cb");
	for(var i =0 ;i<cb.length;i++){
		cb[i].disabled=false;
	}
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    var len = jsonObj.len;
	    if(success){
	    	MyAlert(success);
	    	if(len==0){
	    		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do';
	    	}else{
	    		$("fm").action = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderAllInit.do';
	        	$("fm").submit();
	    	}
	    }else if(error){
	    	var len = $("len").value;
	    	MyAlert(error);
	    	if(len==1){
	    		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do';
	    	}else{
	    		$("fm").action = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderAllInit.do';
	        	$("fm").submit();
	    	}
	    }else if(exceptions){
	    	var len = $("len").value;
	    	MyAlert(exceptions.message);
	    	if(len==1){
	    		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do';
	    	}else{
	    		$("fm").action = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderAllInit.do';
	        	$("fm").submit();
	    	}
		}
	}
}


function goNextBo(){
	if(confirm("不处理当前bo单,处理下一个bo单?")){
		var dIds = $("dIds").value;
		var wIds = $("wIds").value;
		var ids = $("ids").value;
		var oIds = $("oIds").value;
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderChkInit.do?dIds='+dIds+'&wIds='+wIds+'&ids='+ids+'&oIds='+oIds;
	}
}

function enableCb(){
	var cb = document.getElementsByName("cb");
	for(var i =0 ;i<cb.length;i++){
			cb[i].disabled = false;
	}
}
function getPartQty(){
    if($("wh_id").value == ""){
        MyAlert("请选择库房");
        return;
    }
    var whId = $("wh_id").value;
    var partId ="";
    var cb = document.getElementsByName("cb");
    for( var i = 0 ; i < cb.length; i++){
       partId +=","+cb[i].value;
    }
	getPartItemStock(whId,partId);
}
function getPartItemStock(whId,partId){
	disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getPartItemStock.json?whId="+whId+"&partId="+partId;
    sendAjax(url,getPartResult,'fm');
}
function getPartResult(jsonObj){
    if(jsonObj!=null||jsonObj.list!=null){
    	var reAr = jsonObj.list;
    	for(var i=0;i<reAr.length;i++){
    			var partId = reAr[i].PART_ID;
	    		var qty = reAr[i].NORMAL_QTY;
	    		document.getElementById("stockQty_"+partId).value=qty;
	    }
    }else{
        var cb = document.getElementsByName("cb");
        for( var i = 0 ; i < cb.length; i++){
            document.getElementById("stockQty_"+cb[i].value).value=0;
        }
    }
    enableAllBtn();
}


function goBackToBo(){
	//var ids = $("ids").value;
	//var whId = $("whId").value;
	//var orderId = $("orderId").value;
	//var dealerId = $("dealerId").value;
	//window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderInit.do?ids='+ids+'&orderId='+orderId+'&whId='+whId+'&dealerId='+dealerId;
	window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do';
}

function initAccount(){
		if(${mainMap.accountFlag}){
			var td1 = document.getElementById("accountTd1");
			var td2 = document.getElementById("accountTd2");
			var td3 = document.getElementById("accountTd3");
			var td4 = document.getElementById("accountTd4");
			var td5 = document.getElementById("accountTd5");
			var td6 = document.getElementById("accountTd6");
			var str1 = '<td>账户余额：</td>'		;
			var str2 = '<td><input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="normal_txt" value="${mainMap.accountSumNow}" readonly /></td>';
			var str3 = '<td>可用金额：</td>';
			var str4 ='<td><input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="${mainMap.accountKyNow}" class="normal_txt" readonly /></td>';
			var str5 ='<td>冻结金额：</td>';
			var str6 ='<td><input id="DAmount"  name="DAmount" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.accountDjNow}" class="normal_txt" readonly /></td>';
			td1.innerHTML=str1;
			td2.innerHTML=str2;
			td3.innerHTML=str3;
			td4.innerHTML=str4;
			td5.innerHTML=str5;
			td6.innerHTML=str6;
            document.getElementById("accountId").value= ${mainMap.accountId};
		}
}
function ckAllBox(obj){
	var cb = document.getElementsByName("cb");
	for(var i=0;i<cb.length;i++){
		cb[i].checked = obj.checked;
	}
	countAllMoney();
}
function disableAllBtn(){
		var inputArr = document.getElementsByTagName("input");
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="button"){
				inputArr[i].disabled=true;
			}
		}
	}
function enableAllBtn(){
	var inputArr = document.getElementsByTagName("input");
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].type=="button"){
			inputArr[i].disabled=false;
		}
	}
}

function formatNum(str){
	 var len = str.length;
	 var step=3;
     var splitor=",";
     var decPart = ".";
     if((str+"").indexOf(".")>-1){
    	 var strArr = str.split(".");
    	 str = strArr[0];
    	 decPart += strArr[1];
     }
        if(len > step) {
            var l1 = len%step,l2 = parseInt(len/step),arr = [],first = str.substr(0, l1);
            if(first != '') {
                arr.push(first);
            };
            for(var i=0; i<l2 ; i++) {
                arr.push(str.substr(l1 + i*step, step));    
            };
            str = arr.join(splitor);
            str = str.substr(0, str.length-1);
        }; 
        if(decPart != "."){
        	str+=decPart;
        }
　　return str;
}
function unFormatNum(str){
	str=str+"";
	if(str.indexOf(",")>-1){
		str = str.replace(/\,/g,"");
	}
		return str;
}
function doInit(){
	document.getElementById("TRANS_TYPE").value="${mainMap.TRANS_TYPE}";
	if(${flag==2}){
		//将所有BUTTON disable 除了  goBack  和sub
		var inputArr = document.getElementsByTagName("input");
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="button"){
				if(inputArr[i].id=="goBack"||inputArr[i].id=="sub"){
					continue;
				}
				inputArr[i].disabled=true;
			}
		}
		//将所有TEXT disable
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="text"){
				inputArr[i].disabled=true;
			}
		}
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="checkbox"){
				inputArr[i].disabled=true;
			}
		}
		//将所有TEXT select
		var selArr = document.getElementsByTagName("select");
		for(var i=0;i<selArr.length;i++){
			selArr[i].disabled=true;
		}
	}
}

</script>
</head>
<body onload="loadcalendar();initAccount();doInit();countMoney();">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input name="dealerId" id="dealerId" type="hidden"  value="${mainMap.DEALER_ID}" />
<input name="dealerCode" id="dealerCode" type="hidden"  value="${mainMap.DEALER_CODE}" />
<input name="dealerName" id="dealerName" type="hidden"  value="${mainMap.DEALER_NAME}" />
<input name="sellerId" id="sellerId" type="hidden"  value="${mainMap.SELLER_ID}" />
<input name="sellerCode" id="sellerCode" type="hidden"  value="${mainMap.SELLER_CODE}" />
<input name="sellerName" id="sellerName" type="hidden"  value="${mainMap.SELLER_NAME}" />
<input name="buyerId" id="buyerId" type="hidden"  value="${mainMap.BUYER_ID}" />
<input name="buyerName" id="buyerName" type="hidden"  value="${mainMap.BUYER_NAME}" />
<input name="whId" id="whId" type="hidden"  value="${whId}" />
<input name="ALLBO_PARAMS" id="ALLBO_PARAMS" type="hidden"  value="${ALLBO_PARAMS}" />
<input name="BO_PARAMS" id="BO_PARAMS" type="hidden"  value="${BO_PARAMS}" />
<input name="ids" id="ids" type="hidden"  value="${boIdsStr}" />
<input name="len" id="len" type="hidden"  value="${len}" />
<input name="accountId" id="accountId" type="hidden" />
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:
    
    配件管理 &gt;配件销售管理  &gt;BO单处理 &gt;BO单转销售单</div>
  <table id="queryT"class="table_add" border="0"
         style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
         cellSpacing=1 cellPadding=1 width="100%">
    <tr>
      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 配件销售信息</th>
    </tr>
    <tr>
      <td  class="table_add_3Col_label_6Letter" rowspan>销售日期:</td>
      <td >${mainMap.CREATE_DATE}</td>
      <td  class="table_add_3Col_label_6Letter" rowspan>销售制单人:</td>
      <td >${mainMap.saleName}</td>
      <td  class="table_add_3Col_label_6Letter" rowspan>&nbsp;</td>
      <td >&nbsp;</td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter">销售单位:</td>
      <td>${mainMap.SELLER_NAME}</td>
      <td class="table_add_3Col_label_6Letter">订货单位:</td>
      <td >${mainMap.DEALER_NAME}</td>
      <td class="table_add_3Col_label_6Letter">采购制单人:</td>
      <td >${mainMap.NAME}</td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter">采购日期:</td>
      <td>${mainMap.CREATE_DATE}</td>
      <td class="table_add_3Col_label_6Letter">出库仓库:</td>
      <td>
      ${warehouseDefinePO.whName}
      <input type="hidden" name="wh_id" id = "wh_id" value="${warehouseDefinePO.whId}"/>
      </td>
      <td class="table_add_3Col_label_6Letter">接收单位:</td>
      <td><input name="RCV_ORG" class="middle_txt" id="RCV_ORG" type="text" value="${mainMap.RCV_ORG}" size="20" readonly="readonly" />
	    <input name="RCV_CODE" class="middle_txt" id="RCV_CODE" type="hidden" value="${mainMap.RCV_CODE}" size="20" readonly="readonly" />
	    <input name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden" value="${mainMap.RCV_ORGID}" size="20" readonly="readonly" />
  		<input name='dlbtn2' id='dlbtn2' class='mini_btn'  type='button' value='...'  onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${mainMap.DEALER_ID},'2')"/>
       </td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter">接收地址:</td>
      <td colspan="3"><input name="ADDR" class="maxlong_txt" id="ADDR" type="text" size="20" value="${mainMap.ADDR}" readonly="readonly" />
	    <input name="ADDR_ID" class="middle_txt" id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden" size="20" readonly="readonly" />
	    <input name='dlbtn2' id='dlbtn2' class='mini_btn'  type='button' value='...'  onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',${mainMap.DEALER_ID},'3')"/>
      </td>
      <td   align="right">接收人：</td>
      <td><input id="RECEIVER" name="RECEIVER" type="text" value="${mainMap.RECEIVER}" class="normal_txt"/>
        <font color="RED">*</font></td>
    </tr>
      <tr>
      <td class="table_add_3Col_label_6Letter"  ><span   align="right">接收人电话：</span></td>
      <td ><input id="TEL" name="TEL" type="text" class="normal_txt" readonly value="${mainMap.TEL}" />
        <font color="RED">*</font></td>
      <td class="table_add_3Col_label_6Letter" >邮政编码：</td>
      <td ><input id="POST_CODE" name="POST_CODE" type="text" class="normal_txt" value="${mainMap.POST_CODE=='null'?'':mainMap.POST_CODE}" readonly />
        <font color="RED">*</font></td>
      <td   align="right">到站名称：</td>
      <td><input id="STATION" name="STATION" type="text" class="normal_txt" value="${mainMap.STATION}" readonly />
        <font color="RED">*</font></td>
      </tr>
      <tr>
      <td class="table_add_3Col_label_6Letter"  >发运方式：</td>
      <td >
     <select name="TRANS_TYPE" id="TRANS_TYPE" class="short_sel">
                <option value="">-请选择-</option>
                <c:if test="${transList!=null}">
                    <c:forEach items="${transList}" var="list">
                       <option value="${list.fixValue }" selected>${list.fixName }</option>
                    </c:forEach>
                </c:if>
            </select>
            <font color="RED">*</font>
      <td class="table_add_3Col_label_6Letter">付款方式:</td>
      <td> 
      <script type="text/javascript">
			genSelBoxExp("PAY_TYPE",<%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>,<%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>,true,"short_sel","","false",'');
	  </script>
        <font color="RED">*</font></td>
      <td class="table_add_3Col_label_6Letter">订单类型:</td>
      <td><script type="text/javascript">
			genSelBoxExp("ORDER_TYPE1",<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>,${orderType},true,"short_sel",'disabled="disabled"',"false",'');
	  </script>
	  <input type="hidden" name="ORDER_TYPE" id="ORDER_TYPE" value="${orderType}"/>
	  </td>
          </tr>
      <tr>
      <td class="table_add_3Col_label_6Letter">总金额:</td>
      <td><input id="Amount" name="Amount" type="text" style="border:0px;background-color:#F3F4F8;" value="0.00"  class="normal_txt" readonly /><font color="blue">元</font></td>

      <td class="table_add_3Col_label_6Letter">运费支付方式:</td>
      <td>
      <script type="text/javascript">
			genSelBoxExp("transpayType",<%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS%>,<%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>,true,"short_sel","","false",'');
	  </script>

      </td>
      <td class="table_add_3Col_label_6Letter">折扣：</td>
      <td><input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.discount}"  name="DISCOUNT" id="DISCOUNT" />
      <font color="RED">*</font></td>
    </tr>
    <tr >
    	<td class="table_add_3Col_label_6Letter" id="accountTd1">
    	</td>
    	<td  id="accountTd2" align="left">
    	</td>
    	<td class="table_add_3Col_label_6Letter" id="accountTd3">
    	</td>
    	<td  id="accountTd4">
    	</td>
    	<td class="table_add_3Col_label_6Letter" id="accountTd5">
    	</td>
    	<td  id="accountTd6">
    	</td>
    </tr>
    <tr >
    	<td class="table_add_3Col_label_6Letter" >
    		服务商联系人:
    	</td>
    	<td align="left">
    		${phone}
    	</td>
    	<td class="table_add_3Col_label_6Letter" >
    		服务商电话:
    	</td>
    	<td >
    	${linkMan}
    	</td>
    	<td class="table_add_3Col_label_6Letter" >
    	</td>
    	<td >
    	</td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter">订单备注:</td>
      <td colspan="5"><textarea name="REMARK" id="remark" readonly cols="50" rows="3">${mainMap.REMARK}</textarea></td>
    </tr>
      <tr>
      <td class="table_add_3Col_label_6Letter">备注:</td>
      <td colspan="5"><textarea id="REMARK2" name="REMARK2" cols="50" rows="3">${mainMap.REMARK}</textarea></td>
	  </tr>
  </table>
 <table id="file" class="table_list" border="0"  style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
       cellSpacing=1 cellPadding=1 width="100%">
    <tr>
        <th colspan="14" align="left" ><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span class="checked"
                                                                                                         style="text-align:left">
                      </span></th>
    </tr>
    <tr bgcolor="#FFFFCC">
   
      <td >
      <input type="checkbox" checked onclick="ckAllBox(this)" id="ckAll" name="ckAll"/>
      </td>
      <td >序号</td>
      <td >件号</td>
      <td >配件编码</td>
      <td >配件名称</td>
      <td >单位</td>
      <td >当前库存</td>
      <td >订货数量</td>
      <c:if test="${flag==1}">
      	<td >供应商</td>
      </c:if>
      <td >销售数量<font color="RED">*</font></td>
      <td >销售单价<font color="blue">(元)</font></td>
      <td >销售金额<font color="blue">(元)</font></td>
      <td >备注</td>
    </tr>
    <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
     		  <td align="center">
			    <input  type="checkbox" value="${data.PART_ID}" name="cb" checked="checked" onclick="chkPart1();"/>
			  </td>
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx();
				</script>
   			  </td>
   			  <td align="center"><c:out value="${data.PART_CODE}" /></td>
		      <td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td align="center"><c:out value="${data.PART_CNAME}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<input id="stockQty_${data.PART_ID}" name="stockQty_${data.PART_ID}" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="${data.NORMAL_QTY}" /></td>
		      <td>&nbsp;<input id="buyQty_${data.PART_ID}" name="buyQty_${data.PART_ID}" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="${data.BO_ODDQTY}" /></td>
		      <c:if test="${flag==1}">
		      <td>&nbsp;
				<select  name="vender_${data.PART_ID}" id = "vender_${data.PART_ID}" style="width:150px;" >
                    <option selected value=''>-请选择-</option>
		      		<c:forEach items="${data.venderList}" var="vender" >
		      			<option  value="${vender.VENDER_ID}">${vender.VENDER_NAME}</option>
				   	</c:forEach>
      			</select>
		      </td>		
		      </c:if>
		      <c:if test="${data.NORMAL_QTY>=data.BO_ODDQTY}">
		      <td>&nbsp;<input id="saleQty_${data.PART_ID}" name="saleQty_${data.PART_ID}" type="text" class="short_txt" value="${data.BO_ODDQTY}" style="background-color: #ffff80" onchange="countMoney(this,'${data.BUY_PRICE}',${data.PART_ID})"></td>
		      </c:if>
		      <c:if test="${data.NORMAL_QTY<data.BO_ODDQTY}">
		      <td>&nbsp;<input id="saleQty_${data.PART_ID}" name="saleQty_${data.PART_ID}" type="text" class="short_txt" value="${data.NORMAL_QTY}" style="background-color: #ffff80" onchange="countMoney(this,'${data.BUY_PRICE}',${data.PART_ID})"></td>
		      </c:if>																												
			  <td>&nbsp;<c:out value="${data.BUY_PRICE}"/>
			  <input type="hidden" name="BUY_PRICE_${data.PART_ID}" value="${data.BUY_PRICE}"/> </td>
		      <td>&nbsp;</td>
		      <td>&nbsp;<input id="orderCode_${data.PART_ID}" name="orderCode_${data.PART_ID}" type="text" value="${data.code}" readonly style="width:200px;border:0px;background-color:#F3F4F8;text-align:left;"></td>
		      <script type="text/javascript">
	      		countMoney(document.getElementById("saleQty_"+${data.PART_ID}),'${data.BUY_PRICE}',${data.PART_ID});
	      	  </script>
		 </tr>
	</c:forEach>
  </table>
  <table border="0" class="table_query">
    <tr align="center">
      <td>
       <c:if test="${flag!=2}">
         <input class="long_btn" type="button" value="生成销售单" onclick="createSalesOrderConfirm();"/>
       </c:if> 
       <c:if test="${flag==2}">
         <input class="long_btn" type="button" name="sub" id="sub" value="提交给车厂" onclick="repToFactoryConfirm();"/>
       </c:if> 
        &nbsp;&nbsp;
         <input class="normal_btn" type="button" value="返 回" name="goBack" id="goBack" onclick="goBackToBo();"/>
      </td>
    </tr>
  </table>
</div>
</form>
</body>
</html>