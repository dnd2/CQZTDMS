<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%> 
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String invoiceId = (String)request.getAttribute("invoiceId");
	String companyName = (String)request.getAttribute("companyName");
	String companyId = (String)request.getAttribute("companyId");
	String companyShortname = (String)request.getAttribute("companyShortname");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">


<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style type="text/css">   
.table_edit,.table_edit td {   
    border:1px solid #cccccc;   
    border-collapse:collapse;  
}   
</style>

<script type="text/javascript">
	function doInit(){
	
	}

	var lineNum = 0;
	var delLineNume = 0;
	var rebateMoney = 0;
	var  materialAmountInfoList = new Array();
	
	var url = "<%=contextPath %>/sales/accountsmanage/InvoiceManage/invoiceAddSave.json";
	//返回
	function toGoBack() {
		window.location = "<%=contextPath%>/sales/accountsmanage/InvoiceManage/invoiceManageInit.do";
	}
	
	function saleOrderValueBack(json){
		if(json!=null&&json!=''&&json.dealerInfo!=null&&json.dealerInfo!=''){
			document.getElementById("dealerId").value = json.dealerInfo[0].DEALER_ID;
			document.getElementById("dealerName").value = json.dealerInfo[0].DEALER_NAME;		
			document.getElementById("dealerNameShow").innerHTML  = json.dealerInfo[0].DEALER_NAME;
			document.getElementById("accountsType").value = json.accountTypeInfo[0].TYPE_ID;
			document.getElementById("accountsTypeName").value = json.accountTypeInfo[0].TYPE_NAME;
			document.getElementById("accountsTypeNameShow").innerHTML = json.accountTypeInfo[0].TYPE_NAME;
			rebateMoney = json.rebateMoney[0].REBATE_AMOUNT;
			for(var i=0;i<json.amountInfo.length;i++){
				var materialObject = {};
				materialObject.id = json.amountInfo[i].MATERIAL_ID;
				materialObject.req_amount = json.amountInfo[i].RESERVE_AMOUNT; 
				materialObject.bill_amount = json.amountInfo[i].BILL_AMOUNT
				materialAmountInfoList.push(materialObject);
			}
		}	
		else{
			document.getElementById("dealerId").value = "";
			document.getElementById("dealerName").value = "";		
			document.getElementById("dealerNameShow").innerHTML  = "";
			document.getElementById("accountsType").value = "";
			document.getElementById("accountsTypeName").value = "";
			document.getElementById("accountsTypeNameShow").innerHTML="";
			rebateMoney = 0;
			materialAmountInfoList =[];
		}
	}
	
	//销售订单编号失去焦点
	function saleOrderNoBlur(id){
		var orderUrl = "<%=contextPath %>/sales/accountsmanage/InvoiceManage/getBillInfoByInvoiceId.json";
		var saleOrderValue = document.getElementById("saleOrderNo").value;	
		makeCall(orderUrl,saleOrderValueBack,{saleOrderValue:saleOrderValue});
	}
	
	
	function invoiceAdd(){  		
		var invoiceAddLineSum = document.getElementById("invoiceAddLineSum");
		invoiceAddLineSum.value=++lineNum+"";
		invoiceMsgAddOneLine();
	}
	
	 function isSave(){
		 var purchaseInvoiceNo = document.getElementById("purchaseInvoiceNo").value;
		 var billDate = document.getElementById("billDate").value;
		// var receiveDeptName = document.getElementById("dealerName").value;
		 var saleOrderNo = document.getElementById("saleOrderNo").value;
		// var totalAmount = document.getElementById("totalAmount").value;
		
		 
		 if(purchaseInvoiceNo==null||purchaseInvoiceNo==''){
			 MyAlert("采购发票号不能为空");
			 return;
		 }
		// if(receiveDeptName==null||receiveDeptName==''){
		//	 MyAlert("收票单位名称未填");
		//	 return;
		// }
		 
		 if(saleOrderNo==null||saleOrderNo==''){
			 MyAlert("销售订单编号不能为空");
			 return;
		 }
		 
		 if(billDate==null||billDate==''){
			 MyAlert("开票日期不能为空");
			 return;
		 }
		 
		 
		// if(totalAmount==null||totalAmount==''){
		//	 MyAlert("合计金额未填");
		//	 return;
		// }
		// if(accountType==null||accountType==''){
		//	 MyAlert("账户类型未填");
		//	 return;
		// }
		 if(lineNum==0){
			 MyAlert("未添加采购产品信息");
			 return;
		 }
		 var line=0;
		 for(var i=0;i<lineNum;i++){
			 line = i+1;
			 var productCodeInput = document.getElementById("productCode"+line);
			 if(productCodeInput==null||productCodeInput==undefined){
				 continue;
			 }
			 var productCode = document.getElementById("productCode"+line).value;	
			 if(productCode==null||productCode==''){
				 MyAlert("产品编码不能为空");
				 return;
			 }
			 var amount = document.getElementById("amount"+line).value;
			 if(amount==null||amount==''){
				 MyAlert("数量不能为空");
				 return;
			 }
			 var taxPrice = document.getElementById("taxPrice"+line).value;
			 if(taxPrice==null||taxPrice==''){
				 MyAlert("含税单价不能为空");
				 return;
			 }
			 var taxTotalSum = document.getElementById("taxTotalSum"+line).value;
			 if(taxTotalSum==null||taxTotalSum==''){
				 MyAlert("含税金额不能为空");
				 return;
			 }
			 
			 var taxRate = document.getElementById("taxRate"+line).value;
			 if(taxRate==null||taxRate==''){
				 MyAlert("税率不能为空");
				 return;
			 }
			 
			 var taxSum = document.getElementById("taxSum"+line).value;
			 if(taxSum==null||taxSum==''){
				 MyAlert("税额不能为空");
				 return;
			 }			 
			 var reqAmount = document.getElementById("materialReqAmount"+line).value;			 
			 var billAmount = document.getElementById("materialBillAmount"+line).value;			 
			 var canAmount = reqAmount - billAmount;//可以开多少数最		
			 if(amount>canAmount){
				 MyAlert("产品\'"+productCode+"\'只能开"+canAmount+"张发票");
				 return;			 
			  }			 
			 if(taxRate>100){
				 MyAlert("税率不能大于100%");
				 return;
			 }
		 }
		 
         if(submitForm('fm')){
     	    MyConfirm("是否确认保存信息?",addSave);
         }
      }
	 
	function saveBack(json){
			goBack();	
	}
	
	function goBack(){
		window.location = "<%=contextPath%>/sales/accountsmanage/InvoiceManage/invoiceManageInit.do";
	}
	
	function addSave() {
			  
		document.getElementById("savebtn").disabled=true;
		sendAjax(url,saveBack,'fm');
	////	fm.action = "<%=contextPath %>/sales/accountsmanage/InvoiceManage/invoiceAddSave.do";
	//	fm.submit();
		//makeCall(url,saveBack,'');
	}
	
	/*
	*选择产品编码
	*/
	function selectProductNum(lineNum){
		var orderNo = document.getElementById("saleOrderNo").value;	
		//showInVoiceInfo('2017072317019494',lineNum);
		showInVoiceInfo(orderNo,lineNum);
	}
	
	/*
	 * 销售订单编号
	 * */
	function showInVoiceInfo(invoiceNum,lineNum)
	{
		if(!invoiceNum){ 
			MyAlert("销售订单编号为空");
		}
		OpenHtmlWindow(g_webAppName+'/dialog/showInVoiceInfo.jsp?invoiceNum='+invoiceNum+"&lineNum="+lineNum,800,500);
	}
	
	//控制反利值
	function setFLInputValue(line){		
		document.getElementById("amount"+line).value=1;
		var taxPrice = document.getElementById("taxPrice"+line).value;
		document.getElementById("taxTotalSum"+line).value = taxPrice; 
		document.getElementById("taxRate"+line).value = 0; 
		document.getElementById("taxSum"+line).value = 0; 
	}
	
	//含税单价点击事件
	function taxPriceBlur(line){
		var productCode =document.getElementById("productCode"+line);
		if(productCode==null||productCode==''){
			return;
		}
		var productCodeValue = document.getElementById("productCode"+line).value;	
		if(productCodeValue=='FL'){
			setFLInputValue(line)
		}
		var total = 0;
		for(var i=0;i<lineNum;i++){
			var num = i+1;
			var taxPriceTr = document.getElementById("taxPrice"+num);
			if(taxPriceTr==null||taxPriceTr==undefined){
				continue ;
			}
			var taxPrice=document.getElementById("taxPrice"+num).value;//paraFloat
			var amount=document.getElementById("amount"+num).value; 
			if(amount==''){
				amount = 0;
			}
			var taxPriceSum = taxPrice*amount;
			total = taxPriceSum+total;
			document.getElementById("taxTotalSum"+num).value = taxPriceSum; 
			taxRateBlur(num);
		}		
		document.getElementById("totalAmount").value = total;
		document.getElementById("totalAmountShow").innerHTML =total;
	}
	
	//taxRate 税率输入
	function taxRateBlur(line){
		var productCodeValue = document.getElementById("productCode"+line).value;	
		if(productCodeValue=='FL'){
			setFLInputValue(line)
		}
		else{
			var taxRate = document.getElementById("taxRate"+line).value;
			var taxTotalSum = document.getElementById("taxTotalSum"+line).value;
			if(taxRate!=null){
				if(taxRate==""){
					document.getElementById("taxSum"+line).value = '';
				}
				else{
					if(taxRate>100){
						document.getElementById("taxSum"+line).value = '';
						document.getElementById("taxRate"+line).value = '100';
						taxRate =100;
						MyAlert("税率不能大于100");
					}				
					document.getElementById("taxSum"+line).value = Math.round((taxTotalSum*(taxRate/100))*100)/100;
					
				}
			}
		}
	}
	
	//添加一行
	function invoiceMsgAddOneLine(){
		var orderNo = document.getElementById("saleOrderNo").value;	
		if(orderNo==null||orderNo==''){
			MyAlert("销售订单编号未填");
			return;
		}
		document.getElementById("ctm_table_id_2").style.display ='';
		var tbody2 = document.getElementById("ctm_table_id_2_tbody");
		var tr = document.createElement("tr");
		tr.id = "trTeam"+lineNum;
	//	var td1 = document.createElement("td");
	//	td1.setAttribute('width',"5%") ;
	//	td1.innerHTML = '<input id="rowNo'+lineNum+'" name="rowNo'+lineNum+'" value='+lineNum+' type="text"  style="TEXT-ALIGN: center;border:0"  readonly />';
  	//	tr.appendChild(td1);  	
  		
  		
  		var td2 = document.createElement("td");//产品编码
		td2.setAttribute('width',"20%") ;
		td2.innerHTML ='<input id="productCode'+lineNum+'" name="productCode'+lineNum+'" value="" type="text"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  onclick="selectProductNum('+lineNum+');"/>';
		tr.appendChild(td2); //将节点td加入tr
  		
		var td3 = document.createElement("td"); //产品名字
		td3.setAttribute('width',"20%") ;
		td3.innerHTML ='<input id="productName'+lineNum+'" name="productName'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" readonly="readonly" />';
		tr.appendChild(td3); //将节点td加入tr
		
		var td4 = document.createElement("td"); //数量
		td4.setAttribute('width',"5%") ;
		td4.innerHTML ='<input id="amount'+lineNum+'" name="amount'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" onkeyup="checkInputDataformat2(this.id)" onBlur ="taxPriceBlur('+lineNum+')" />'; // onBlur ="taxPriceBlur('+lineNum+')"
		tr.appendChild(td4); //将节点td加入tr
		
		
		var td5 = document.createElement("td");//含税单价
		td5.setAttribute('width',"15%") ;
		td5.innerHTML ='<input id="taxPrice'+lineNum+'" name="taxPrice'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" onkeyup="checkTaxPriceInputChang('+lineNum+')" onBlur ="taxPriceBlur('+lineNum+')"/>';
		tr.appendChild(td5); //将节点td加入tr
		
		
		var td6 = document.createElement("td"); //含税金额
		td6.setAttribute('width',"15%") ;
		td6.innerHTML ='<input id="taxTotalSum'+lineNum+'" name="taxTotalSum'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" onkeyup="muchInputChang(this.id)" readonly="readonly" />';
		tr.appendChild(td6); //将节点td加入tr
		
		
		var td7 = document.createElement("td");//税率
		td7.setAttribute('width',"5%") ;
		td7.innerHTML ='<input id="taxRate'+lineNum+'" name="taxRate'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" onkeyup="muchInputChang(this.id)" onBlur="taxRateBlur('+lineNum+')"/>';
		tr.appendChild(td7); //将节点td加入tr
		
		
		
		var td8 = document.createElement("td");//税额
		td8.setAttribute('width',"20%") ;
		td8.innerHTML ='<input id="taxSum'+lineNum+'" name="taxSum'+lineNum+'" value="" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50"  onkeyup="muchInputChang(this.id)" readonly="readonly" />';
		tr.appendChild(td8); //将节点td加入tr
  		
		
		var td9 = document.createElement("td"); //删除
		td9.setAttribute('width',"5%") ;
		td9.innerHTML = '<input type="button" value="删除" class="normal_btn" onclick="deletLine('+lineNum+');"/><input id="materialId'+lineNum+'" name="productCode'+lineNum+'" value="" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  /><input id="materialReqAmount'+lineNum+'" name="productCode'+lineNum+'" value="0" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  /><input id="materialBillAmount'+lineNum+'" name="productCode'+lineNum+'" value="0" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  />';
	  	tr.appendChild(td9);  
	  	
	  	/* var td10 = document.createElement("td");//物料ID
		td10.setAttribute('width',"20%") ;
		td10.innerHTML ='<input id="materialId'+lineNum+'" name="productCode'+lineNum+'" value="" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  />';
		tr.appendChild(td10); //将节点td加入tr
		
		var td11 = document.createElement("td");//物料允许数量
		td11.setAttribute('width',"20%") ;
		td11.innerHTML ='<input id="materialReqAmount'+lineNum+'" name="productCode'+lineNum+'" value="0" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  />';
		tr.appendChild(td11); //将节点td加入tr
		
		var td12 = document.createElement("td");//物料已经开票数量
		td12.setAttribute('width',"20%") ;
		td12.innerHTML ='<input id="materialBillAmount'+lineNum+'" name="productCode'+lineNum+'" value="0" type="hidden"  data-value="'+lineNum+'" style="TEXT-ALIGN: center;border:0" maxlength="50" readonly  />';
		tr.appendChild(td12); //将节点td加入tr */
		
		
		tbody2.appendChild(tr);
	}
	
	//删除一行
	function deletLine(line){
		var tbody2 = document.getElementById("ctm_table_id_2_tbody");
		var chils = tbody2.lastChild; 
		var test = "trTeam"+line;
		var tr2 = document.getElementById(test).innerText='';
		taxPriceBlur(line);//删除一行重新计算	
		//var tr = $("#trTeam1");
		//$("#"+tesetId).remove();
		alert(document.getElementById("totalAmount").value)
		delLineNume++;
		if(delLineNume==lineNum){//删除行等于添加的行
			document.getElementById("ctm_table_id_2").style.display ='none';
			//lineNum = 0;
		}
	}
	
	///return "<input type='radio' onclick='singleSelect("+value+",\""+record.data.dealerCode+"\",\""+record.data.dealerShortname+"\")' name='de' id='"+value+"' />";

	//显示意向车型
	function showInfo(line,material_code,material_name,materialId){
		
		//MyAlert("lineNum=="+lineNum+"  material_code=="+material_code+" material_name=="+material_name+" materialId=="+materialId);

		for(var i=0;i<lineNum;i++){
			var num = i+1;
			var product = document.getElementById("productCode"+num);
			if(product==null||product==undefined){
				continue ;				
			}
			var productCode = document.getElementById("productCode"+num).value;
			if(material_code==productCode){
				MyAlert("产品'"+material_code+"'不能重复添加");
				return;
			}
		}
		document.getElementById("productCode"+line).value = material_code;
		document.getElementById("productName"+line).value = material_name;	
		document.getElementById("materialId"+line).value = materialId;
		setAmount(materialId,line);
		if(material_code!='FL'){
			document.getElementById("amount"+line).removeAttribute('readonly');
			document.getElementById("taxPrice"+line).removeAttribute('readonly');
			document.getElementById("taxRate"+line).removeAttribute('readonly');
		}
		else{
			document.getElementById("amount"+line).setAttribute('readonly','readonly');
			document.getElementById("taxPrice"+line).setAttribute('readonly','readonly');
			document.getElementById("taxRate"+line).setAttribute('readonly','readonly');
			document.getElementById("amount"+line).value=1;
			document.getElementById("taxRate"+line).value=0;
			document.getElementById("taxSum"+line).value=0;
			document.getElementById("taxPrice"+line).value = rebateMoney;
			document.getElementById("taxTotalSum"+line).value =rebateMoney;
			taxPriceBlur(line);
		}
	}
	//数量设备
    function setAmount(materialId,lineNum){
		for(var i=0;i<materialAmountInfoList.length;i++){
			var info  = materialAmountInfoList[i];		
			if(info.id==materialId){
				if(info.req_amount==null||info.req_amount==''){
					document.getElementById("materialReqAmount"+lineNum).value = 0;
				}
				else{
					document.getElementById("materialReqAmount"+lineNum).value = info.req_amount;
				}
				if(info.bill_amount==null||info.bill_amount==''){
					document.getElementById("materialBillAmount"+lineNum).value = 0;
				}
				else{
					document.getElementById("materialBillAmount"+lineNum).value = info.bill_amount;
				}
			}
		}
    }
	
	function checkTaxPriceInputChang(line){
		
		var productCodeValue = document.getElementById("productCode"+line).value;	
		if(productCodeValue=='FL'){
			setFLInputValue(line);
			var y=document.getElementById('taxPrice'+line); 
			onlyNumberAndNegative(y);
		}
		else{
			muchInputChang('taxPrice'+line);
		}
		//$("#"+tesetId).remove();
		checkFirstIsZero('taxPrice'+line);
	}
	
	function muchInputChang(id){
		var y=document.getElementById(id).value; 
		//c=y.toLowerCase();
		//document.getElementById(id).value=y.replace(/[^\d.]/g,''); 
		onlyNumber(document.getElementById(id));	
		checkFirstIsZero(id);
	}
	
	function checkInputDataformat(id){
		var y=document.getElementById(id).value; 
		onlyNumberAndNopot(document.getElementById(id));
		
	}
	
	function purchaseInvoiceNoDataformat(id){
		var y=document.getElementById(id).value; 
		if(y==null||y==''){
			return;
		}
		var length = y.length-1;
		var ch = y.charAt(length);
		if(ch>="a"&&ch<="z"){
			return;
		}
		else if(ch>="A"&&ch<="Z"){
			return;
		}
		else if(ch>=0&&ch<=9){
			return;
		}
		else{
			document.getElementById(id).value = y.substring(0,length);
		}
	}
	
	function checkInputDataformat2(id){
		var y=document.getElementById(id).value; 
		onlyNumberAndNopot(document.getElementById(id));
		checkFirstIsZero(id);		
	}
	
	function checkFirstIsZero(id){
		//检查数据前面是否有0的情况，有0去掉0
		y=document.getElementById(id).value; 
		if(y!=null&&y!=''){
			var length = y.length;
			var i=0;
			for(i=0;i<length-1;i++){
				var t = y.charAt(i);
				if(y.charAt(i)!='0'){
					break;	
				}
			}
			if(i>0){
				document.getElementById(id).value = y.substring(i,length) ;
			}
		}
	}
	
	function onlyNumberAndNopot(obj){
		obj.value = obj.value.replace(/[^\d]/g,''); 
	}
	
	function onlyNumberAndNegative(obj){
		//得到第一个字符是否为负号
		var t = obj.value.charAt(0); 
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,''); 
		//必须保证第一个为数字而不是. 
		obj.value = obj.value.replace(/^\./g,''); 
		//保证只有出现一个.而没有多个. 
		obj.value = obj.value.replace(/\.{2,}/g,'.'); 
		//保证.只出现一次，而不能出现两次以上 
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		var strs= new Array(); //定义一数组 
	    strs= obj.value.split(".");	

		if(strs.length>1){
			if(strs[1].length>2){
				var last = strs[1].substring(0,2) ;
				obj.value =strs[0]+"."+last;
			}
		}
		
		//如果第一位是负号，则允许添加
		if(t == '-'){
			obj.value = '-'+obj.value;
		}
	}
	
	function onlyNumber(obj){
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,''); 
		//必须保证第一个为数字而不是. 
		obj.value = obj.value.replace(/^\./g,''); 
		//保证只有出现一个.而没有多个. 
		obj.value = obj.value.replace(/\.{2,}/g,'.'); 
		//保证.只出现一次，而不能出现两次以上 
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		var strs= new Array(); //定义一数组 
	    strs= obj.value.split(".");	

		if(strs.length>1){
			if(strs[1].length>2){
				var last = strs[1].substring(0,2) ;
				obj.value =strs[0]+"."+last;
			}
		}				
	}
	
	</script>
	
	
</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
			
<title>发票管理</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 财务管理&gt; 发票管理 &gt; 发票管理</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 

<div id="invoiceInfoId">
<div id="customerInfoId">

	<table class="table_query table_list" class="center" id="ctm_table_id">

		<tr>		
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>采购发票信息</th>
		</tr>
		
		<tr>
			<td class="right" id="tcmtd">单据编码：</td>
			<td  class="left">
				<%=invoiceId%>
				<input id="invoiceId" name="invoiceId" value="<%=invoiceId%>" type="hidden" readonly size="40"  style="border:0" />
			</td>
			<td class="right" id="tcmtd">采购发票号：</td>
			<td  class="left">
				<input id="purchaseInvoiceNo" name="purchaseInvoiceNo" class="middle_txt" value="" type="text"  size="40"   maxlength="30" style="border:0" onkeyup="purchaseInvoiceNoDataformat(this.id)"/> 
				<font color="red">*</font>
			</td>	
					
		</tr>
		
		<tr>
			<td  class="right">开票单位名称：</td>
			<td  class="left">
				<%=companyName%>
				<input id="billDeptName" name="billDeptName" value="<%=companyName%>" class="middle_txt" type="hidden" readonly size="40"  style="border:0" />
				<input id="billDeptId" name="billDeptId" value="<%=companyId%>" type="hidden" readonly size="40"  style="border:0" />
				<input id="billDeptShortName" name="billDeptShortName" value="<%=companyShortname%>" type="hidden" readonly size="40"  style="border:0" />
			</td>
			<td  class="right">收票单位名称：</td>
			<td  class="left">
				<div id="dealerNameShow"> </div>
		    	<input type="hidden"  name="dealerId" size="15"  id="dealerId" readonly="readonly"/>
           		<input id="dealerName" name="dealerName"  type="hidden" class="middle_txt" readonly size="40"  style="border:0" readonly="readonly" />	  				  			
			</td>
															
		</tr>
			
		<tr>
			<td  class="right">销售订单编号：</td>
			<td  class="left">
				<input id="saleOrderNo" name="saleOrderNo" class="middle_txt" value="" type="text"    size="40"   maxlength="70" style="border:0" onkeyup="checkInputDataformat(this.id)" onBlur ="saleOrderNoBlur()"/>
				<font color="red">*</font>
			</td>	
			<td  class="right" id="sextd">开票日期：</td>
			<td class="left">
				<input name="billDate"  id="billDate" value="${date }" class="middle_txt" type="text" class="middle_txt"  style="width:110px" onFocus="WdatePicker({el:$dp.$('billDate'), maxDate:'#F{$dp.$D(\'payDate1\')}'})" />
		      			<input  id="payDate1"  type="hidden"   />
		      			<font color="red">*</font>
			</td>	
					
		</tr>	
		<tr>
		<td  class="right">合计金额：</td>
			<td  class="left">
				<div id="totalAmountShow"></div>
				<input id="totalAmount" name="totalAmount" value="" class="middle_txt" type="hidden"  size="40"  maxlength="30" style="border:0" onkeyup="muchInputChang(this.id)" readonly="readonly"/>
			</td>
			<td  class="right">账户类型：</td>
			<td  class="left">
				 <div id ='accountsTypeNameShow'></div>
				 <input id="accountsType" name="accountsType" value="" type="hidden"    size="40"   maxlength="70" style="border:0" readonly="readonly" />
				 <input id="accountsTypeName" name="accountsTypeName" value="" type="hidden"  class="middle_txt"   size="40"   maxlength="70" style="border:0" readonly="readonly" />
			</td>			
		</tr>
		<tr>
			<td  class="right">备注：</td>
			<td  class="left">
				<textarea name='remark' id='remark'	 rows='2' cols='30'  style="border:0" ></textarea>
			</td>
		</tr>
 </table>
 <table>
	<tr>
	<td>
	<input type="button" style="float: left;margin-top: 0px;" value="添加" class="normal_btn" onclick="invoiceAdd();"/>
	</td>
	</tr>
</table>
<table class="table_edit" class="center" id="ctm_table_id_2" style="display:none;">
	<tbody id="ctm_table_id_2_tbody">
	<input type="hidden" id="invoiceAddLineSum" name="invoiceAddLineSum" value="0" />
	<%-- <tr>
		
		<th colspan="8" ><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>采购产品信息 </th>		 
	</tr> --%>
	<tr>		
		<th class="center" >产品编码</th>
		<th class="center" >产品名称</th>
		<th class="center" >数量</th>
		<th class="center">含税单价</th>
		<th class="center" >含税金额</th>
		<th class="center">税率%</th>
		<th class="center" >税额</th>
		<th class="center" >删除</th>
	</tr>
	
</tbody></table>

</div>
<table class="table_query" id="submitTable">
	<tbody>
		<td class="center" colspan="6">
		<td>		
			<input type="button" id ='savebtn' value="确定" class="u-button u-submit" onclick="isSave();"/>
			<input type="button" value="返 回" class="u-button u-reset" onclick="toGoBack();"/> 
		</td>
	</tr>
</tbody></table>
</form>
</div>


</body>
</html>
