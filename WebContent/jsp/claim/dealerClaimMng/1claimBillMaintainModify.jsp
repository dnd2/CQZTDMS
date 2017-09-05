<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrLabouritemPO"%>
<%@page import="com.infodms.dms.po.TtAsWrPartsitemPO"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.TtAsActivityPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>

<%
	String contextPath = request.getContextPath();
%>
<%
			/** 格式化金钱时保留的小数位数 */
			int minFractionDigits = 2;
    		/** 当格式化金钱为空时，默认返回值 */
    		String defaultValue = "0";

			TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO) request.getAttribute("application");
			List<ClaimListBean> itemLs = (LinkedList<ClaimListBean>) request.getAttribute("itemLs");
			List<ClaimListBean> partLs = (LinkedList<ClaimListBean>) request.getAttribute("partLs");
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
		%>
		<%
			//List feeTypeList = (List) request.getAttribute("FEETYPE");//保养费用集合 
			List<TtAsWrGamefeePO> feeTypeList = (List<TtAsWrGamefeePO>) request.getAttribute("FEE"); //保养费用参数对应的值
			String maintainFeeStr = "";
			String maintainfeeOrderStr = "";
			if (feeTypeList!=null){
				if (feeTypeList.size()>0) {
					for(int i=0;i<feeTypeList.size();i++) {
						maintainFeeStr += ","+feeTypeList.get(i).getManintainFee();
						maintainfeeOrderStr += ","+feeTypeList.get(i).getMaintainfeeOrder();
					}
				}
			}
			//if (hm == null) {
			//	hm = new HashMap();
			//}
			TtAsActivityPO tap = (TtAsActivityPO) request.getAttribute("ACTIVITY");
		%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔申请创建</TITLE>
		<SCRIPT LANGUAGE="JavaScript">
	var first=false; //第一次进入页面为FALSE
	var cloMainTime=1; //关闭主工时选择页面
	var cloTime=1; //关闭附属工时选择页面
	var cloMainPart=1; //关闭主要配件选择页面
	var cloPart=1; //关闭附加配件选择页面
	var myobj;
	var modelId = '<%=tawep.getModelId()%>';
	var purchasedDate = '<%=tawep.getPurchasedDate()%>';
	var mFeeOrder='<%=maintainfeeOrderStr%>'; 
	var mFee='<%=maintainFeeStr%>';
	var arr3 = mFeeOrder.split(','); //免费保养次数
	var arr2 = mFee.split(','); //免费保养费用
	var itemToDel='';
	var itemToUp='';
	var partToDel='';
	var partToUp='';
	var otherToDel='';
	var otherToUp='';
	var random=0;
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
	function confirmUpdate() {
	var appArr = document.getElementsByName('appTime');
		for (var i=0;i<appArr.length;i++) {
			if (appArr[i].value=='') {
				MyAlert("添加的主配件必须选择一个索赔工时!");
				return false;
			}
		}
	if (document.getElementById("CLAIM_TYPE").value==""||document.getElementById("CLAIM_TYPE").value==null) {
			MyAlert("请选择索赔类型！");
			return false;
		}
		
		var options =  document.getElementById("CLAIM_TYPE").options;
    	var index = options.selectedIndex;
    	if(options[index].value=='<%=Constant.CLA_TYPE_01%>') {//一般索赔
    	//需要有主要工时，配件
    	if(document.getElementById('itemTable').childNodes.length==0) {
    		MyAlert("一般索赔需要添加至少一个主工时");
    		return false;
    	}
    	//if(document.getElementById('partTable').childNodes.length==0) {
    	//	MyAlert("一般索赔需要添加至少一个主配件");
    	//	return false;
    	//}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_02%>') {//免费保养
    		clearAllNode(document.getElementById('itemTable'),'item');
    		clearAllNode(document.getElementById('partTable'),'part');
    		clearAllNode(document.getElementById('otherTable'),'other');
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_03%>') {//追加费用
    		clearAllNode(document.getElementById('itemTable'),'item');
    		clearAllNode(document.getElementById('partTable'),'part');
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_04%>') {//重复修理索赔
    	//需要有主要工时，配件
    	if(document.getElementById('itemTable').childNodes.length==0) {
    		MyAlert("重复修理索赔需要添加至少一个主工时");
    		return false;
    	}
    	//if(document.getElementById('partTable').childNodes.length==0) {
    	//	MyAlert("重复修理索赔需要添加至少一个主配件");
    	//	return false;
    	//}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_05%>') {//零件索赔更换
    	//需要有主要工时，配件
    	if(document.getElementById('itemTable').childNodes.length==0) {
    		MyAlert("零件索赔更换需要添加至少一个主工时");
    		return false;
    	}
    	//if(document.getElementById('partTable').childNodes.length==0) {
    	//	MyAlert("零件索赔更换需要添加至少一个主配件");
    	//	return false;
    	//}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_06%>') {//服务活动
    		if (document.getElementById("IS_FIX")=='1') {
    		clearAllNode(document.getElementById('itemTable'),'item');
    		clearAllNode(document.getElementById('partTable'),'part');
    		clearAllNode(document.getElementById('otherTable'),'other');
    		}else {
    			
    		}
    	}
		if(submitForm('fm')){
		if (document.getElementById("RO_NO").value!='<%=CommonUtils.checkNull(tawep.getRoNo())%>'&&document.getElementById("LINE_NO").value!='<%=CommonUtils.checkNull(tawep.getRoNo())%>') {
			verDupRo();
		}else {
			MyConfirm("是否修改？",confirmUpdate0,[]);
		}
		}
	}
	function keyListnerResp(){
	   if((typeof window.event)!= 'undefined'){
		   var type = event.srcElement.type;   
	       var code = event.keyCode;
	       if(type=='text'||type=='textarea'){
	    	   event.returnValue=true;
	       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
	         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
	            event.returnValue=false;       
	         }
	       }
	   }
	}
	function confirmUpdate0() {
		var fm = document.getElementById('fm');
		fm.action='<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationUpdate.do?ITEM_DEL='+itemToDel+'&PART_DEL='+partToDel+'&OTHER_DEL='+otherToDel;
		fm.submit();
	}
	//校验工单和行号在索赔单表中是否有重复
	function verDupRo() {
		var roNo = document.getElementById("RO_NO").value;
		var lineNo = document.getElementById("LINE_NO").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/verifyDupRo.json';
    	makeCall(url,verDupRoBack,{roNo:roNo,lineNo:lineNo});
	}
	//回调函数
	function verDupRoBack(json) {
    	var last=json.dup;
    	if (last){
    		MyAlert("该工单号和行号已经在索赔申请单中存在！");
    	}else {
    		MyConfirm("是否修改？",confirmUpdate0,[]);
    	}
    }
	function doInit()
	{
	   	loadcalendar();
	}
	//计算费用
	function countQuantity(obj) {
		myobj = obj.parentNode.parentNode;
		var price = myobj.cells.item(5).childNodes[0].value;
		var quantity = obj.value;
		if (quantity!=null&&quantity!=""){
			myobj.cells.item(6).innerHTML = '<td><input type="text" class="little_txt" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" datatype="0,is_yuan,10" readonly/></td>';
			document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
			sumAll();
		}else {
			//MyAlert("请输入数量！");
		}
		
	}
	//计算费用(工时)
	function countQuantityLabour(obj) {
		myobj = obj.parentNode.parentNode
		var parameterValue = myobj.cells.item(4).childNodes[0].value;
		var labourQuotiety = obj.value;
		if (labourQuotiety!=null&&labourQuotiety!=""){
			myobj.cells.item(5).innerHTML = '<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" ="true" readonly/></td>';
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			sumAll();
		}else {
			//MyAlert("请输入数量！");
		}
	}
	//计算工时，配件和附加的费用
	function countFee() {
		document.getElementById("ADD_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS0"));
		document.getElementById("ADD_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
		document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
		document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
		document.getElementById("OTHER_AMOUNT").innerText = sumArr(document.getElementsByName("ITEM_AMOUNT"));
		sumAll(); 
	}
	//计算费用
	function countQuantity1(obj) {
	//MyAlert(obj.value);
		var price = myobj.cells.item(5).childNodes[0].value;
		var quantity = obj.value;
		if (quantity!=null&&quantity!=""){
			myobj.cells.item(6).innerHTML = '<input type="text" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" onBlur="javascript:countQuantity(this);"/>';
			document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
		}else {
			MyAlert("请输入数量！");
		}
		
	}
	//增加或删除工时时刷新配件列表中的索赔工时下拉框
	function refreshAppTimeCombo() {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//取得配件列表
		var partTable = document.getElementById('partTable');
		if (partTable!=null&&partTable.rows.length!=0) {
			for (var i=0;i<partTable.rows.length;i++) {
				if (partTable.rows[i].childNodes[11].childNodes.length==3) {
					partTable.rows[i].cells[10].innerHTML=innerHTML;
				}
			}
		}
		//MyAlert(partTable.rows[0].childNodes[10].childNodes.length);
	}
	
	//配件工时下拉框
	function genAppTimeCombo(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		//MyAlert(obj.parentNode.innerHTML);
		obj.parentNode.innerHTML=innerHTML;
	}
	//配件工时下拉框
	function genAppTimeComboStr(id) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  id="'+id+'" name="appTime">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		return innerHTML;
	}
	//点新增时配件工时下拉框
	function genAppTimeCombo1(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		obj.cells[10].innerHTML=innerHTML;
	}
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVinForward.do',800,500);
	}
	//获取子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME,PURCHASED_DATE,TRANSFER_NO,BRAND_CODE,SERIES_CODE,MODEL_CODE,YIELDLY){
		document.getElementById("VIN").value = VIN;
		document.getElementById("LICENSE_NO").value = LICENSE_NO;
		document.getElementById("BRAND_NAME").innerHTML = BRAND_NAME;
		//MyAlert(document.getElementById("brand").value);
		//document.getElementById("BRAND_NAME").innerHTML = '<select onchange="brandOnchange(this)">'+document.getElementById("brand").value+'</select>';
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		//document.getElementById("SERIES_NAME").innerHTML = '<select onchange="seriesOnchange(this)">'+document.getElementById("series").value+'</select>';
		
		//document.getElementById("MODEL_NAME").innerHTML = '<select>'+document.getElementById("model").value+'</select>';
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		document.getElementById("REARAXLE_NO").innerHTML = REARAXLE_NO;
		document.getElementById("GEARBOX_NO").innerHTML = GEARBOX_NO;
		document.getElementById("TRANSFER_NO").innerHTML = TRANSFER_NO;
		
		document.getElementById("BRAND_NAME0").value = BRAND_NAME;
		document.getElementById("SERIES_NAME0").value = SERIES_NAME;
		document.getElementById("MODEL_NAME0").value = MODEL_NAME;
		document.getElementById("BRAND_CODE0").value = BRAND_CODE;
		document.getElementById("SERIES_CODE0").value = SERIES_CODE;
		document.getElementById("MODEL_CODE0").value = MODEL_CODE;
		document.getElementById("ENGINE_NO0").value = ENGINE_NO;
		document.getElementById("REARAXLE_NO0").value = REARAXLE_NO;
		document.getElementById("GEARBOX_NO0").value = GEARBOX_NO;
		document.getElementById("TRANSFER_NO0").value = TRANSFER_NO;
		document.getElementById("GUARANTEE_DATE").value=formatDate(PURCHASED_DATE);
		assignSelect("YIELDLY",YIELDLY);
		modelId=MODEL_ID;
		purchasedDate=PURCHASED_DATE;
		//freeOnchange(modelId);
		freeOnchangeText(modelId);
		
	}
	//校验工单和行号在索赔单表中是否有重复
	function oneVIN() {
		var vin = document.getElementById("VIN").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json';
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin)) {
			if (vin!=null&&vin!='') {
	    		makeCall(url,oneVINBack,{vinParent:vin});
	    	}
		}else {
			//document.getElementById("VIN").value ='' ;
			document.getElementById("LICENSE_NO").value = '' ;
			document.getElementById("BRAND_NAME").innerHTML = '' ;
			document.getElementById("SERIES_NAME").innerHTML = '' ;
			document.getElementById("MODEL_NAME").innerHTML = '' ;
			document.getElementById("ENGINE_NO").value = '';
			document.getElementById("REARAXLE_NO").innerHTML = '' ;
			document.getElementById("GEARBOX_NO").innerHTML = '' ;
			document.getElementById("TRANSFER_NO").innerHTML = '' ;
			
			document.getElementById("BRAND_NAME0").value = '' ;
			document.getElementById("SERIES_NAME0").value = '' ;
			document.getElementById("MODEL_NAME0").value = '' ;
			document.getElementById("BRAND_CODE0").value = '' ;
			document.getElementById("SERIES_CODE0").value = '';
			document.getElementById("MODEL_CODE0").value = '' ;
			document.getElementById("ENGINE_NO0").value = '' ;
			document.getElementById("REARAXLE_NO0").value = '' ;
			document.getElementById("GEARBOX_NO0").value = '' ;
			document.getElementById("TRANSFER_NO0").value = '' ;
			document.getElementById("GUARANTEE_DATE").value='';
			assignSelect("YIELDLY",'');
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//回调函数
	function oneVINBack(json) {
    	var last=json.ps.records;
    	var size=last.length;
    	var record;
    	if (size>0) {
    		record = last[0];
    		document.getElementById("VIN").value =getNull(record.vin) ;
			document.getElementById("LICENSE_NO").value = getNull(record.licenseNo) ;
			document.getElementById("BRAND_NAME").innerHTML = getNull(record.brandName) ;
			document.getElementById("SERIES_NAME").innerHTML = getNull(record.seriesName) ;
			document.getElementById("MODEL_NAME").innerHTML = getNull(record.modelName) ;
			document.getElementById("ENGINE_NO").value = getNull(record.engineNo) ;
			document.getElementById("REARAXLE_NO").innerHTML = getNull(record.rearaxleNo) ;
			document.getElementById("GEARBOX_NO").innerHTML = getNull(record.gearboxNo) ;
			document.getElementById("TRANSFER_NO").innerHTML = getNull(record.transferNo) ;
			
			document.getElementById("BRAND_NAME0").value = getNull(record.brandName) ;
			document.getElementById("SERIES_NAME0").value = getNull(record.seriesName) ;
			document.getElementById("MODEL_NAME0").value = getNull(record.modelName) ;
			document.getElementById("BRAND_CODE0").value = getNull(record.brandCode) ;
			document.getElementById("SERIES_CODE0").value = getNull(record.seriesCode) ;
			document.getElementById("MODEL_CODE0").value = getNull(record.modelCode) ;
			document.getElementById("ENGINE_NO0").value = getNull(record.engineNo) ;
			document.getElementById("REARAXLE_NO0").value = getNull(record.rearaxleNo) ;
			document.getElementById("GEARBOX_NO0").value = getNull(record.gearboxNo) ;
			document.getElementById("TRANSFER_NO0").value = getNull(record.transferNo) ;
			document.getElementById("GUARANTEE_DATE").value=formatDate(getNull(record.purchasedDate) );
			assignSelect("YIELDLY",record.yieldly);
			modelId=record.modelId;
			purchasedDate=PURCHASED_DATE;
			freeOnchangeText(record.modelId);
    	}
    }
	//时间格式化
	Date.prototype.format = function(format) {   
    	var o = {   
			     "M+" : this.getMonth()+1, //month   
			     "d+" : this.getDate(),    //day   
			     "h+" : this.getHours(),   //hour   
			     "m+" : this.getMinutes(), //minute   
			     "s+" : this.getSeconds(), //second   
			     "q+" : Math.floor((this.getMonth()+3)/3), //quarter   
			     "S" : this.getMilliseconds() //millisecond   
   				}   
	   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,   
	     (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	   for(var k in o)if(new RegExp("("+ k +")").test(format))   
	     format = format.replace(RegExp.$1,   
	       RegExp.$1.length==1 ? o[k] :    
	         ("00"+ o[k]).substr((""+ o[k]).length));   
	   return format;   
	}  
	//格式化时间为YYYY-MM-DD
	function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//添加事件
	function AttachEvent(target, eventName, handler, argsObject)
	{
    var eventHandler = handler;
    if(argsObject)
    {
        eventHander = function(e)
        {
            handler.call(argsObject, e);
        }
    }
    if(window.attachEvent)//IE
        target.attachEvent("on" + eventName, eventHander );
    else//FF
        target.addEventListener(eventName, eventHander, false);
	}
	//乘法运算
	function accMul(arg1,arg2) { 
	　　var m=0,s1=arg1.toString(),s2=arg2.toString(); 
	　　try{m+=s1.split(".")[1].length}catch(e){} 
	　　try{m+=s2.split(".")[1].length}catch(e){} 
	　　return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
　　} 
	//加法运算
	function accAdd(arg1,arg2){ 
	   //var arg1 = args1.toString().replace("\,","");
	   //var arg2 = args2.toString().replace("\,","");
	   //MyAlert(arg1);
	   //MyAlert(arg2);
	　　var r1,r2,m; 
	　　try{r1=arg1.toString().split(".")[1].length}catch( e){r1=0} 
	　　try{r2=arg2.toString().split(".")[1].length}catch( e){r2=0} 
	　　m=Math.pow(10,Math.max(r1,r2)) 
	　　return (arg1*m+arg2*m)/m;
　　} 
	//免费保养联动
	function freeOnchange(modelId) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeFree.json';
    	makeCall(url,changeFree,{MODEL_ID:modelId,PURCHASED_DATE:purchasedDate});
	}
	//免费活动下拉框联动
	function changeFree(json) {
    	var last=json.changeFree;
     	last = "<select id='FREE_M_AMOUNT' name='FREE_M_AMOUNT' onchange='setFee(this)'>"+last+"</select>";
     	document.getElementById("feeTableId").innerHTML=last;
     	var obj0 = document.getElementById("FREE_M_AMOUNT");
    	if(obj0){
   		AttachEvent(obj0,'onchange',setFee,obj0);//
   		}
    }
     //免费保养联动
	function freeOnchangeText(modelId) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeFreeText.json';
    	makeCall(url,changeFreeText,{MODEL_ID:modelId,PURCHASED_DATE:purchasedDate});
	}
	//免费保养联动回调函数
	function changeFreeText(json) {
    	var last=json.changeFree;
    	if (last!=''&&last!=null) {
    		var arr1 = last.split("*");
    		arr2 = arr1[0].split(','); //免费保养费用
    		arr3 = arr1[1].split(','); //免费保养次数
    	}
    	if(obj0){
   		AttachEvent(obj0,'onchange',setFee,obj0);//
   		}
    }
    //累加数组和
    function sumArr(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i].value==""?0:arr[i].value;
    		sum=accAdd(sum , parseFloat(tmp));
    	}
		return sum;	    	
    }
    //累加数组和
    function sumArr1(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i]==""?0:arr[i];
    		sum=accAdd(sum , parseFloat(tmp));
    	}
		return sum;	    	
    }
    //累加其他项目费用和
    function sumItem() {
    	document.getElementById("OTHER_AMOUNT").innerText = sumArr(document.getElementsByName("ITEM_AMOUNT"));
    	sumAll();
    }
    
    //计算申请金额，总金额
    function sumAll() {
    	var baseLabour = document.getElementById('BASE_LABOUR').innerText; //基本工时
    	var baseLabourAmount = document.getElementById('BASE_LABOUR_AMOUNT').innerText; //基本工时金额
    	baseLabourAmount = baseLabourAmount.replace(",","");
    	var addLabour = document.getElementById('ADD_LABOUR').innerText; //附加工时
    	var addLabourAmount = document.getElementById('ADD_LABOUR_AMOUNT').innerText; //附加工时金额
    	addLabourAmount = addLabourAmount.replace(",","");
    	var allPartAmount = document.getElementById('ALL_PART_AMOUNT').innerText;//配件金额
    	allPartAmount = allPartAmount.replace(",","");
    	var allLabourAmount = document.getElementById('ALL_LABOUR_AMOUNT').innerText;//工时金额
    	allLabourAmount = allLabourAmount.replace(",","");
    	var otherAmount = document.getElementById('OTHER_AMOUNT').innerText;//其他费用金额
    	otherAmount = otherAmount.replace(",","");
    	var gameAmount = document.getElementById('GAME_AMOUNT').innerText;//免费保养金额
    	gameAmount = gameAmount.replace(",","");
    	var activityAmount = document.getElementById('ACTIVITY_AMOUNT').innerText;//服务活动金额
    	activityAmount = activityAmount.replace(",","");
    	var applyAmount = document.getElementById('APPLY_AMOUNT').innerText;//申请金额
    	applyAmount = applyAmount.replace(",","");
    	var tax = document.getElementById('TAX').innerText;//税额
    	var arr=new Array();
		//arr.push(baseLabour);
		//arr.push(baseLabourAmount);
		//arr.push(addLabour);
		//arr.push(addLabourAmount);
		arr.push(allPartAmount);
		arr.push(allLabourAmount);
		arr.push(otherAmount);
		arr.push(gameAmount);
		arr.push(activityAmount);
		//arr.push(baseLabour);
		
    	document.getElementById('APPLY_AMOUNT').innerText = sumArr1(arr);
    	applyAmount = document.getElementById('APPLY_AMOUNT').innerText;
    	//arr.push(applyAmount);
    	arr.push(tax);
    	document.getElementById('ALL_AMOUNT').innerText = sumArr1(arr);
    	
    	
    }
	//工时选择
	function setMainTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue,fore,isSpec) {
		var table = myobj.parentNode;
		var length= table.childNodes.length;
		var flag=0;
		//判断是否添加了重复的主工时
		for (var i = 0;i<length;i++) {
			if (table.childNodes[i].childNodes[10].childNodes.length==3) {
				if(labourCode==table.childNodes[i].childNodes[1].childNodes[0].value){
					cloMainTime=0;
					MyAlert("该主工时已经存在，不可添加！");
					flag=1;
					break;
				}
			}
		}
		if (flag==0){
			cloMainTime=1;
			//未被授权
			if(fore==0) {
				myobj.cells.item(0).innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE"  type="checkbox" disabled="true" /></td>';				
			//已授权
			}else {
				myobj.cells.item(0).innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE" checked type="checkbox" disabled="true" /></td>';
			}
			if(isSpec==0) {
			chooseItem(labourCode);
			myobj.cells.item(1).innerHTML='<td><input type="text" class="short_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><input name="MAIN_ITEM"  type="hidden" value="on"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
			myobj.cells.item(2).innerHTML='<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME"  value="'+cnDes+'" size="10" readonly/></span></td>';
			myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
			myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
			myobj.cells.item(5).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" readonly="true"/></td>';
			myobj.cells.item(10).innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\''+labourCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
			}else {
				random++;
				chooseItem(labourCode);
				myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="short_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
				myobj.cells.item(2).innerHTML='<td><input type="text" name="WR_LABOURNAME" datatype="0,is_digit_letter_cn,100" value="'+cnDes+'" size="10" /></td>';
				setMustStyle([myobj.cells.item(2).childNodes[0]]);
				myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt" datatype="0,is_double,6" decimal="2"  value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE'+random+'" onblur="countQuantityLabour(this);" /></td>';
				setMustStyle([myobj.cells.item(3).childNodes[0]]);
				myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				myobj.cells.item(5).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" ="true" readonly/></td>';
				myobj.cells.item(10).innerHTML =  '<td><input type="button"  style="display:none"  class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\''+labourCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
			}
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"))+sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
			sumAll();
			refreshAppTimeCombo();
			
		}
	}
	//工时选择(附加工时name加0)
	function setTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue) {
	 	var table = myobj.parentNode;
	    var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    var line = getRowNo(myobj); //取得当前行在表格中的行数
	    var strline = 0;
	    var endline = length;
	    var flag = 0;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		strline = i;
				break;
			}
	    }
	    for (var i=line;i<length;i++) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		endline = i;
				break;
			}
	    }
	    for (var i=strline+1;i<endline;i++) {
	    	if (table.childNodes[i].childNodes[1].childNodes[1].value==labourCode) {
	    		cloTime=0;
	    		flag=1;
	    		MyAlert('不能在一个主工时下添加同一个附属工时');
	    		break;
	    	}
	    }
	    if (flag==0) {
	    cloTime=1;
	    for (var i=line;i>=0;i--) {
	    
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		//MyAlert(table.childNodes[i].childNodes[1].childNodes[2].value);
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_ML_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[0].value+'"/><input name="ITEM" disabled="true"  type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
				break;
			}
	    }
		myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="short_txt"   name="WR_LABOURCODE0" readonly value="'+labourCode+'" size="10"/><a href="#"  onclick="selectTime(this);">选择</a></td>'
		myobj.cells.item(2).innerHTML='<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0"  value="'+cnDes+'" size="10" readonly/></span></td>';
		myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
		myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
		myobj.cells.item(5).innerHTML='<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" readonly="true"/></td>';
		document.getElementById("ADD_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS0"));
		document.getElementById("ADD_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
		document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
		sumAll();
		}
	}
	/*
	//主配件选择
	function setMainPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
		var table = myobj.parentNode;
			var length= table.childNodes.length;
			var flag=0;
			//判断是否添加了重复的主工时
			for (var i = 0;i<length;i++) {
				if (table.childNodes[i].childNodes[10].childNodes.length==3) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[0].value){
						cloMainPart=0;
						MyAlert("该主配件已经存在，不可添加！");
						flag=1;
						break;
					}
				}
			}
			if (flag==0) {
			cloMainPart=1;
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
		myobj.cells.item(10).innerHTML='<td><input type="button" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
		}
	}
	//附加配件选择
	function setPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
	    var table = myobj.parentNode;
	    var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    var line = getRowNo(myobj); //取得当前行在表格中的行数
	    var flag=0;
			//判断是否添加了重复的主工时
			for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[0].value){
						cloPart=0;
						MyAlert("附加配件不可与主配件代码重复！");
						flag=1;
						
					}
					break;
				}
				
			}
			if (flag==0) {
			cloPart=1;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_MP_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[0].value+'"/><input name="PART0" type="checkbox" disabled onClick="javascript:checkCheckBox(this);"/></td>';
	    		break;
			}
	    }
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
		}
	}
	*/
	//主配件选择
	function setMainPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName,fore) {
		var table = myobj.parentNode;
			var length= table.childNodes.length;
			var flag=0;
			//判断是否添加了重复的主工时
			for (var i = 0;i<length;i++) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[0].value){
						cloMainPart=0;
						MyAlert("该配件已经存在，不可添加！");
						flag=1;
						break;
					}
			}
			if (flag==0) {
			cloMainPart=1;
			if(fore==0) {
				myobj.cells.item(0).innerHTML =  '<td><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/><input name="PART_IS_FORE"  type="checkbox" disabled="true" /></td>';				
			//已授权
			}else {
				myobj.cells.item(0).innerHTML =  '<td><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/><input name="PART_IS_FORE"  checked type="checkbox" disabled="true" /></td>';
			}
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="text" name="PRODUCER_NAME" class="short_txt" id="PRODUCER_NAME" value="'+supplierName+'"/>';
		myobj.cells.item(8).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE1" readonly value="'+supplierCode+'" id="PRODUCER_CODE1" /><input type="text" name="PRODUCER_NAME1" readonly id="PRODUCER_NAME1" class="short_txt" value="'+supplierName+'"/>';
		myobj.cells.item(11).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
		}
	}
	//附加配件选择
	function setPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
	    var table = myobj.parentNode;
	    var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    var line = getRowNo(myobj); //取得当前行在表格中的行数
	    var flag=0;
			//判断是否添加了重复的主工时
			for (var i=0;i<length;i++) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[0].value){
						cloPart=0;
						MyAlert("该配件已经存在，不可添加！");
						flag=1;
					}
					//break;
			}
			if (flag==0) {
			cloPart=1;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_MP_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[0].value+'"/><input name="PART0" type="checkbox" disabled onClick="javascript:checkCheckBox(this);"/></td>';
	    		break;
			}
	    }
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="text" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
		}
	}
	function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
		myobj.cells.item(2).innerHTML='<input type="text" class="short_txt" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true" /><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span>';
		myobj.cells.item(8).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE1" readonly value="'+supplierCode+'" id="PRODUCER_CODE1" /><input type="text" name="PRODUCER_NAME1" id="PRODUCER_NAME1" readonly class="short_txt" value="'+supplierName+'"/>';
	}
	 // 动态生成表格
 	function addRow(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		if (tableId=='itemTable'||tableId=='partTable'){
			insertRow.insertCell(6);
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
		}
		if (tableId=='partTable') {
			insertRow.insertCell(11);
		}
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE"  type="checkbox" disabled="true" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectMainTime();"></a></span><input type="text" class="short_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0"  value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="short_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="short_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="short_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><select id="TROUBLE_CODE'+length+'" name="TROUBLE_CODE" class="min_sel">'+document.getElementById('list04').value+'</select></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="DAMAGE_AREA'+length+'" name="DAMAGE_AREA" class="min_sel">'+document.getElementById('list02').value+'</select></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><select id="DAMAGE_TYPE'+length+'" name="DAMAGE_TYPE" class="min_sel">'+document.getElementById('list03').value+'</select></td>';
			addTable.rows[length].cells[9].innerHTML =  '<td><select id="DAMAGE_DEGREE'+length+'" name="DAMAGE_DEGREE" class="min_sel">'+document.getElementById('list01').value+'</select></td>';
			addTable.rows[length].cells[10].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
		}else if (tableId == 'partTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden"  disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/><input name="PART_IS_FORE"  type="checkbox" disabled="true" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="short_txt" name="PART_CODE0" datatype="0,is_null"  value="" size="10" id="PART_CODE0" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="short_txt" name="DOWN_PART_CODE" readonly size="10"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME" readonly value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME0" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,isDigit" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" readonly /></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="hidden" class="short_txt" name="PRODUCER_CODE0" id="PRODUCER_CODE" /><input type="text" class="short_txt" name="PRODUCER_NAME0" id="PRODUCER_NAME" /></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><input type="hidden" class="short_txt" name="PRODUCER_CODE1" id="PRODUCER_CODE1" /><input type="text" class="short_txt" name="PRODUCER_NAME1" id="PRODUCER_NAME1" /></td>';
			addTable.rows[length].cells[9].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			addTable.rows[length].cells[10].innerHTML = '<td><select name="appTime" class="min_sel" ><option value="">索赔工时列表</option></select><input type="hidden" name="PART" value="on"/></td>';
			addTable.rows[length].cells[11].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
		//这个不加0，不用通过弹出页面带值（其他项目）
		}else if (tableId == 'otherTable') {
			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="ITEM_CODE" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+length+'" readonly class="short_txt" datatype="0,is_digit_letter_cn,100"/></span></div></td>';
			setMustStyle([document.getElementById("ITEM_NAME"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><div align="center"><input type="text" onblur="sumItem();" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+length+'"  datatype="0,is_yuan"  class="short_txt"/></div></td>';
			setMustStyle([document.getElementById("ITEM_AMOUNT"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" datatype="1,is_digit_letter_cn,100" class="middle_txt"  /></span></div></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
			}
			return addTable.rows[length];
		}
		 // 动态生成表格
 	function addPlusRow(tableId,obj,code){
 	//MyAlert(getRowNo(obj.parentNode));
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		//var length = rows.length;
		var length = getRowNo(obj.parentNode)+1;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		if (tableId=='otherTable'){
			insertRow.className = "table_edit";
		}
		//insertRow.id = dataId;
		
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		if (tableId=='itemTable'||tableId=='partTable'){
			insertRow.insertCell(5);
			insertRow.insertCell(6);
		//}
		//if (tableId=='partTable'){
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
		}
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="hidden" name="B_ML_CODE" value="'+code+'"/><input name="ITEM" disabled="true"  type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="short_txt"  id="WR_LABOURCODE0'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE0"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0"  value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			//addTable.rows[length].cells[6].innerHTML = '<td><input type="checkbox"/></td>';
			//addTable.rows[length].cells[7].innerHTML = '<td><input type="checkbox"/></td>';
			//addTable.rows[length].cells[8].innerHTML = '<td><input type="checkbox"/></td>';
			//addTable.rows[length].cells[9].innerHTML = '<td></td>';
			addTable.rows[length].cells[10].innerHTML =  '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
		}else if (tableId == 'partTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="hidden" name="B_MP_CODE" value="'+code+'"/><input name="PART0" type="checkbox" disabled onClick="javascript:checkCheckBox(this);"/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="short_txt" name="PART_CODE0" datatype="0,is_null"  value="" size="10" id="PART_CODE0" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="short_txt" name="DOWN_PART_CODE" readonly size="10"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME" readonly value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME0" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,isDigit" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" readonly /></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="text" class="short_txt" name="PRODUCER_CODE0" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME0" id="PRODUCER_NAME" /></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			addTable.rows[length].cells[9].innerHTML =  '<td><input type="hidden" name="PART" value="off"/><input type="hidden" name="appTime" value="" /></td>';
			addTable.rows[length].cells[10].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
		//这个不加0，不用通过弹出页面带值（其他项目）
		}else if (tableId == 'otherTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="ITEM_CODE" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+length+'" readonly class="short_txt" datatype="0,is_digit_letter_cn,100"/></span></div></td>';
			setMustStyle([document.getElementById("ITEM_NAME"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><div align="center"><input type="text" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+length+'"  datatype="0,is_yuan"  class="short_txt"/></div></td>';
			setMustStyle([document.getElementById("ITEM_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" datatype="1,is_digit_letter_cn,100" class="middle_txt"  /></span></div></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
			}
		
		}
		//选择主工时
		function selectMainTime(obj) {
			//MyAlert(document.getElementById('timeId').value);
			myobj = getRowObj(obj);
			var treeCode = 3;
			var timeId;
			treeCode=3;
			timeId='';
			openTime(obj,treeCode,timeId);
		}
		//选择附加工时
		function selectTime(obj) {
			//MyAlert(document.getElementById('timeId').value);
			myobj = getRowObj(obj);
			var table = myobj.parentNode;
	    	var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    	var line = getRowNo(myobj); //取得当前行在表格中的行数
			var treeCode = 3;
			var timeId;
			treeCode=4;
			for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		timeId = table.childNodes[i].childNodes[1].childNodes[0].value;
				break;
				}
	    	}
			//timeId = document.getElementById("timeId").value;
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
		}
		//修改主项目工时需要删除附加工时
		function openTime(obj,treeCode,timeId){
		var tr = obj.parentNode.parentNode;
		delPlusItems(tr);
			parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMainTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
		}
		//带出费用名称
		function setOtherName(obj){
			myobj = getRowObj(obj);
			//for (var i in myobj.cells.item(0)) {
			//	MyAlert(i);
			//}
			var options = myobj.cells.item(1).childNodes[0].childNodes[0].options;
			var index = options.selectedIndex;
			var text = options[index].title;
			changeOtherFore(options[index].value);
			//MyAlert(text);
			//MyAlert(myobj.cells.item(0).childNodes[0].childNodes[0].options.selectedIndex);
			myobj.cells.item(2).innerHTML='<td><div align="center"><input readonly type="text" name="ITEM_NAME" class="short_txt" value="'+text+'" datatype="0,is_digit_letter_cn,100"  id="ITEM_NAME"/></div></td>';
			//var line =  getRowNo(myobj);
			//MyAlert(line);
		}
		//其他项目选择下拉框 ：是否授权联动
		function changeOtherFore(itemCode){
			var roNo = document.getElementById("RO_NO").value;
			var vin = document.getElementById("VIN").value;
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeOtherFore.json';
	    	makeCall(url,changeOtherForeBack,{itemCode:itemCode,roNo:roNo,vin:vin});
		}
		function changeOtherForeBack(json){
			var count = json.count;
			if (count!=null&&count!=0) {
				myobj.cells.item(0).innerHTML='<input type="checkbox"  name="OTHER_IS_FORE" disabled checked / >';
			}else {
				myobj.cells.item(0).innerHTML='<input type="checkbox"  name="OTHER_IS_FORE" disabled / >';
			}
		}
		//选择主上件
		function selectMainPartCode(obj){
			myobj = getRowObj(obj);
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMainPartCodeForward.do?GROUP_ID='+modelId,800,500);
		}
		//选择上件
		function selectPartCode(obj){
			myobj = getRowObj(obj);//取得行对象
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectPartCodeForward.do?GROUP_ID='+modelId,800,500);
		}
		//选择下件
		function selectDownPartCode(obj){
			myobj = getRowObj(obj);
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectDownPartCodeForward.do?GROUP_ID='+modelId,800,500);
		}
		//得到行对象
		function getRowObj(obj)
		{
		   var i = 0;
		   while(obj.tagName.toLowerCase() != "tr"){
		    obj = obj.parentNode;
		    if(obj.tagName.toLowerCase() == "table")
		  return null;
		   }
		   return obj;
		}
		
		//根据得到的行对象得到所在的行数
		function getRowNo(obj){
		   var trObj = getRowObj(obj); 
		   var trArr = trObj.parentNode.children;
		   var ret;
		 for(var trNo= 0; trNo < trArr.length; trNo++){
		  if(trObj == trObj.parentNode.children[trNo]){
		  		ret = trNo;
		  		break;
		  }
		 }
		 return ret;
		}
		
		//删除行 工时
		function delItem(obj,name){
		//MyAlert(obj.parentNode.parentNode.childNodes.length);
		    var tr = this.getRowObj(obj);
		    //MyAlert(tr.childNodes[10].childNodes[2].value);
		    //MyAlert(tr.childNodes[10].childNodes[3].value);
		    //MyAlert("length="+tr.childNodes[10].childNodes.length);
		    if(tr.childNodes[10].childNodes.length==3) {
		    	MyConfirm("删除主工时将会随之删除附加工时，是否删除？",delItems,[tr,name]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    countFee();
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//删除行 配件
		function delPartItem(obj,name){
		     var tr = this.getRowObj(obj);
		     //MyAlert(obj.parentNode.parentNode.childNodes.length);
		    //MyAlert("length="+tr.childNodes[10].childNodes.length);
		    //if(obj.parentNode.parentNode.childNodes.length==3) {
		    if(tr.childNodes[11].childNodes.length==3) {
		    	MyConfirm("删除主配件将会随之删除附加配件，是否删除？",delPartItems,[tr,name]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//删除工时
		function delItems(tr,name){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes[10].childNodes.length==3) {
					endRo=i;
					//MyAlert(endRo);
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo;i<endRo;i++) {
				//MyAlert(trObj.childNodes[roNo].cells[0].childNodes[0].value);
				if (trObj.childNodes[roNo].cells[0].childNodes.length==6) {
					if (name=='item'){
						itemToDel += ','+trObj.childNodes[roNo].cells[0].childNodes[0].value;				
					}else if (name=='part'){
						partToDel += ','+trObj.childNodes[roNo].cells[0].childNodes[0].value;
					}
				}
				var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
				oldNode = null;
			}
			countFee();
			refreshAppTimeCombo();
		   //if(tr != null){
		   // clearAllNode(tr.parentNode);
		 //  }else{
		 //   throw new Error("the given object is not contained by the table");
		  // }
		}
		//删除配件
		function delPartItems(tr){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes[11].childNodes.length==3) {
					endRo=i;
					//MyAlert(endRo);
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo;i<endRo;i++) {
				//MyAlert(i);
				//MyAlert(roNo);
				//MyAlert(tr.parentNode.childNodes[roNo].innerHTML);
				var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
				oldNode = null;
			}
			countFee();
			refreshAppTimeCombo();
		   //if(tr != null){
		   // clearAllNode(tr.parentNode);
		 //  }else{
		 //   throw new Error("the given object is not contained by the table");
		  // }
		}
		function delPlusItems(tr){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes.length==11) {
					endRo=i;
					//MyAlert(endRo);
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo+1;i<endRo;i++) {
				var oldNode = trObj.removeChild(trObj.childNodes[roNo+1]);
				oldNode = null;
			}
		}
		
		
		//循环删除节点
		function clearAllNode(parentNode,name){
		    while (parentNode.firstChild) {
		      var oldNode = parentNode.removeChild(parentNode.firstChild);
		      //MyAlert(oldNode.childNodes[0].childNodes.length);
		      //MyAlert(oldNode.childNodes[0].innerHTML);
		      if (oldNode.childNodes[0].childNodes.length==6){
		      if (name=='item'){
		      	//MyAlert(oldNode.childNodes[0].childNodes[0].value);
		      	itemToDel = itemToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}else if (name=='part') {
		      	partToDel = partToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}
		      }
		      if (name=='other') {
		      
		      //MyAlert(oldNode.childNodes[4].innerHTML);
		      if(oldNode.childNodes[4].childNodes[0].childNodes.length==4){
		      	otherToDel = otherToDel+ ","+ oldNode.childNodes[4].childNodes[0].childNodes[0].value;
		      }
		      
		      	//otherToDel = 
		      }
		     // MyAlert(itemToDel);
		     // MyAlert(partToDel);
		       oldNode = null;
		     }
		    // MyAlert(itemToDel);
		  //   MyAlert(partToDel);
		     //MyAlert(otherToDel);
		   } 
		//删除行其他项目
		function delItemOther(obj){
		    var tr = this.getRowObj(obj);
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    countFee();
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
		//删除或更新行时累加删除ID 工时
		function delItemIds(obj,type) {
			var tr = this.getRowObj(obj);
			//MyAlert(tr.childNodes[0].childNodes[0].value);
			if(type=="del"){
				itemToDel = itemToDel +","+ tr.childNodes[0].childNodes[0].value  ;
			}else if (type=="update") {
				itemToUp = itemToUp +","+ tr.childNodes[0].childNodes[0].value;
			}
			//MyAlert(tr.childNodes[0].childNodes[0].value);
		}
		//删除或更新行时累加删除ID 配件
		function delPartIds(obj,type) {
			var tr = this.getRowObj(obj);
			//MyAlert(tr.childNodes[0].childNodes[0].value);
			if(type=="del"){
				partToDel = partToDel +","+ tr.childNodes[0].childNodes[0].value  ;
			}else if (type=="update") {
				partToUp =  partToUp +","+ tr.childNodes[0].childNodes[0].value;
			}
			//MyAlert(tr.childNodes[0].childNodes[0].value);
		}
		//删除或更新行时累加删除ID 其他项目
		function delOtherIds(obj,type) {
			var tr = this.getRowObj(obj);
			if(type=="del"){
				otherToDel = otherToDel +","+ tr.childNodes[4].childNodes[0].childNodes[0].value;
			}else if (type=="update") {
				otherToUp =  otherToUp +","+ tr.childNodes[4].childNodes[0].childNodes[0].value;
			}
			//MyAlert(tr.childNodes[4].childNodes[0].childNodes[0].value);
		}
		//下拉框修改时赋值
		function assignSelect(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
		//免费保养选择次数下拉框赋值
		function assignSelectTime(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].innerText) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
		//服务活动下拉框赋值
		function assignSelectActivity(name,value) {
			var sel = document.getElementById(name);
			var option = sel.options;
			var optionLength = option.length;
			for (var i = 0;i<optionLength;i++) {
				var arr=option[i].value.split(",");
			  	if (value == arr[0]) {
			  		sel.selectedIndex = i;
			  		break;
			  	}
			}
		}
		//下拉框修改时赋值
		function assignSelectByObj(sel,value) {
		MyAlert(sel.options.length);
		//var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  	}
		}
		}
		//删除行:其他项目
		function delItemOther(obj){
		    var tr = this.getRowObj(obj);
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    countFee();
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
		//设置费用INPUT框
		function setFee(object) {
			//MyAlert(object.selectedIndex);
			var sum = 0;
			var options = object.options;
			for (var i=0;i<=object.selectedIndex;i++) {
				sum += parseFloat(options[i].value);
			}
			document.getElementById("fee").value=sum;
		}
		//设置费用INPUT框
		function setFee0(object) {
			var sum = 0;
			var flag = 0;
			for (var i = 0;i<arr3.length;i++) {
				if (object.value==arr3[i]){
					flag=1;
					document.getElementById("fee").value=arr2[i];
					document.getElementById("GAME_AMOUNT").innerText=arr2[i];
					break;
				}
			}
			if (flag==0) {
				document.getElementById("fee").value=='0';
				document.getElementById("GAME_AMOUNT").innerText='0';
			}
			sumAll();
		}
		//设定固定费用
		function setCheckbox(object) {
			var arr = object.value.split(",");
			document.getElementById("CAMPAIGN_CODE").value=arr[0];
			if (arr[1]=='undefined'||arr[1]=='') {
				tmp=0;
			}else {
				tmp = parseFloat(arr[1]);
			}
			
			sumAll();
			document.getElementById("CAMPAIGN_FEE").value=tmp;
			document.getElementById("IS_FIX").value=arr[2];
			if (arr[2]==1) { //1是固定费用
				document.getElementById("IS_FIX0").checked=true;
				//是固定费用需要隐藏掉工时和配件列表
				clearAllNode(document.getElementById('itemTable'),'item');
				clearAllNode(document.getElementById('partTable'),'part');
				clearAllNode(document.getElementById('otherTable'),'other');
				document.getElementById('itemTableId').style.display='none';
    			document.getElementById('partTableId').style.display='none';
    			document.getElementById('otherTableId').style.display='none';
    			document.getElementById("BASE_LABOUR").innerText ='0.00';
				document.getElementById("BASE_LABOUR_AMOUNT").innerText ='0.00';
				document.getElementById("ALL_LABOUR_AMOUNT").innerText ='0.00';
				document.getElementById("ALL_PART_AMOUNT").innerText ='0.00';
    			document.getElementById("ACTIVITY_AMOUNT").innerText = tmp;
    			sumFunc();
			}else {
				document.getElementById("IS_FIX0").checked=false;
				clearAllNode(document.getElementById('itemTable'),'item');
				clearAllNode(document.getElementById('partTable'),'part');
				clearAllNode(document.getElementById('otherTable'),'other');
				document.getElementById('itemTableId').style.display='';
    			document.getElementById('partTableId').style.display='';
    			document.getElementById('otherTableId').style.display='';
    			document.getElementById("ACTIVITY_AMOUNT").innerText ='0.00';
    			changeRepair(arr[0]);
				changePart(arr[0]);
				changeOther(arr[0]);
				//document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
				//document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
				//document.getElementById("ALL_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"))+sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
				//document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
				//sumAll();
				//SAN20100727003143
			}
		}
		//服务活动累计计算功能
		function sumFunc() {
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
			document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
			sumAll();
		}
		//
		function setRepairTable () {
			var addTable = document.getElementById('itemTable');
			MyAlert(addTable);
			var len  = addTable.rows.length;
			for (var i=0;i<len;i++) {
				myobj=addTable.rows[i];
				MyAlert(addTable.rows[i].cells.item(1).childNodes[2].value);
				//联动故障代码
				chooseItem2(addTable.rows[i].cells.item(1).childNodes[2].value);
				MyAlert(5);
			}
		}
		//
		function setRepairTrouble (objec) {
			myobj = objec;
			//联动故障代码
			chooseItem2(objec.parentNode.parentNode.childNodes[1].childNodes[2].value);
		}
		var objValue; 
		function fillCode(objec) {
			myobj = objec;
			objValue = objec.value;
			//联动故障代码
			chooseItem3(objec.parentNode.parentNode.childNodes[1].childNodes[2].value);
			
			
		}
		
		//服务活动下拉框联动工时表
		function changeRepair(code) {
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityRepairs.json';
	    	makeCall(url,dynaRepair,{CODE:code});
    	}
    	//服务活动下拉框联动配件表
    	function changePart(code) {
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityParts.json';
	    	makeCall(url,dynaPart,{CODE:code});
    	}
    	//服务活动下拉框联动其他项目表
    	function changeOther(code) {
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityOthers.json';
	    	makeCall(url,dynaOther,{CODE:code});
    	}
    	//回调
		function dynaRepair(json) {
    		var last=json.activityRepairs;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('itemTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				insertRow.insertCell(6);
				insertRow.insertCell(7);
				insertRow.insertCell(8);
				insertRow.insertCell(9);
				insertRow.insertCell(10);
				
				addTable.rows[i].cells[0].innerHTML =  '<td><input name="ITEM"  checked type="checkbox" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="short_txt"  id="WR_LABOURCODE'+i+'" name="WR_LABOURCODE" readonly value="'+last[i].itemCode+'" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
				setMustStyle([document.getElementById("WR_LABOURCODE"+i)]);
				addTable.rows[i].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME"  value="'+last[i].itemName+'" size="10" readonly/></span></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+last[i].normalLabor+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+last[i].parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+last[i].sum+'" size="8" maxlength="9" readonly="true"/></td>';
				addTable.rows[i].cells[6].innerHTML =  '<td><select id="ITEM_CODE'+i+'" class="min_sel" onclick="setRepairTrouble(this)" name="TROUBLE_CODE">'+document.getElementById('list04').value+'</select></td>';
				
				addTable.rows[i].cells[7].innerHTML =  '<td><select id="DAMAGE_AREA'+i+'" class="min_sel" name="DAMAGE_AREA">'+document.getElementById('list02').value+'</select></td>';
				addTable.rows[i].cells[8].innerHTML =  '<td><select id="DAMAGE_TYPE'+i+'"  class="min_sel" name="DAMAGE_TYPE">'+document.getElementById('list03').value+'</select></td>';
				addTable.rows[i].cells[9].innerHTML =  '<td><select id="DAMAGE_DEGREE'+i+'" class="min_sel" name="DAMAGE_DEGREE">'+document.getElementById('list01').value+'</select></td>';
				addTable.rows[i].cells[10].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\''+last[i].itemCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
			}
			sumFunc();
		}
		//回调
		function dynaPart(json) {
			var last=json.activityParts;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('partTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				insertRow.insertCell(6);
				insertRow.insertCell(7);
				insertRow.insertCell(8);
				insertRow.insertCell(9);
				insertRow.insertCell(10);
				insertRow.insertCell(11);
		
				addTable.rows[i].cells[0].innerHTML =  '<td><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="checkbox" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/></td>';
				addTable.rows[i].cells[1].innerHTML =  '<input type="text" class="short_txt" name="PART_CODE"   value="'+last[i].partNo+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
				addTable.rows[i].cells[2].innerHTML =  '<td><input type="text" name="DOWN_PART_CODE" value="'+last[i].partNo+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"   size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
				addTable.rows[i].cells[3].innerHTML =  '<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+last[i].partName+'" id="PART_NAME"  size="10"/></span>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,isDigit" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+i+'" value="'+last[i].partQuantity+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
				setMustStyle([document.getElementById("QUANTITY"+i)]);
				var supplierCode = last[i].supplierCode==null?"":last[i].supplierCode;
				var supplierName = last[i].supplierName==null?"":last[i].supplierName;
				addTable.rows[i].cells[5].innerHTML =  '<input type="text" class="little_txt" name="PRICE" value="'+last[i].partPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
				addTable.rows[i].cells[6].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT" id="AMOUNT" value="'+last[i].partAmount+'" size="10" datatype="0,is_money,10"  maxlength="9" readonly /></td>';
				addTable.rows[i].cells[7].innerHTML =  '<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
				addTable.rows[i].cells[8].innerHTML =  '<input type="text" class="short_txt" name="PRODUCER_CODE1" readonly value="'+supplierCode+'" id="PRODUCER_CODE1" /><input type="hidden" name="PRODUCER_NAME1" id="PRODUCER_NAME1" value="'+supplierName+'"/>';
				addTable.rows[i].cells[9].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
				addTable.rows[i].cells[10].innerHTML =  '<td><select name="appTime" onclick="refreshAppTimeCombo();" class="min_sel" ><option value="">索赔工时列表</option></select><input type="hidden" name="PART" value="on"/></td>';
				addTable.rows[i].cells[11].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+last[i].partNo+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			}
			//refreshAppTimeCombo();
			sumFunc();
		}
		//回调
		function dynaOther(json) {
			var last=json.activityOthers;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('otherTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="OTHER_CODE'+i+'" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
				assignSelect('OTHER_CODE'+i,last[i].itemCode);
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly class="short_txt" value="'+last[i].itemDesc+'" datatype="0,is_digit_letter_cn,100"/></span></div></td>';
				setMustStyle([document.getElementById("ITEM_NAME"+i)]);
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="text" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'" onblur="sumItem();"   datatype="0,is_yuan"  class="short_txt"/></div></td>';
				setMustStyle([document.getElementById("ITEM_AMOUNT"+i)]);
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" datatype="1,is_digit_letter_cn,100" class="middle_txt"  /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<div align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div>';
			}
			sumFunc();
		}
		//清空费用
	    function zeroAllFee() {
	    	document.getElementById('BASE_LABOUR').innerText='0.00'; //基本工时
	    	document.getElementById('BASE_LABOUR_AMOUNT').innerText='0.00'; //基本工时金额
	    	document.getElementById('ADD_LABOUR').innerText='0.00'; //附加工时
	    	document.getElementById('ADD_LABOUR_AMOUNT').innerText='0.00'; //附加工时金额
	    	document.getElementById('ALL_PART_AMOUNT').innerText='0.00';//配件金额
	    	document.getElementById('ALL_LABOUR_AMOUNT').innerText='0.00';//工时金额
	    	document.getElementById('OTHER_AMOUNT').innerText='0.00';//其他费用金额
	    	document.getElementById('GAME_AMOUNT').innerText='0.00';//免费保养金额
	    	document.getElementById('ACTIVITY_AMOUNT').innerText='0.00';//服务活动金额
	    	document.getElementById('APPLY_AMOUNT').innerText='0.00';//申请金额
	    	document.getElementById('TAX').innerText='0.00';//税额
	    	document.getElementById('ALL_AMOUNT').innerText ='0.00'; //总费用
	    }
	
</SCRIPT>
	</HEAD>
	<BODY onload="doInit();" onkeydown="keyListnerResp();">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单维护
		</div>

		<form method="post" name="fm" id="fm">
			<input type="hidden" name="attIds" id="attIds" value=""/><!-- 删除附件隐藏 -->
			<input type="hidden" name="BRAND_NAME0" id="BRAND_NAME0" value=""/>
			<input type="hidden" name="SERIES_NAME0" id="SERIES_NAME0" value=""/>
			<input type="hidden" name="MODEL_NAME0" id="MODEL_NAME0" value=""/>
			<input type="hidden" name="ENGINE_NO0" id="ENGINE_NO0" value=""/>
			<input type="hidden" name="REARAXLE_NO0" id="REARAXLE_NO0" value=""/>
			<input type="hidden" name="GEARBOX_NO0" id="GEARBOX_NO0" value=""/>
			<input type="hidden" name="TRANSFER_NO0" id="TRANSFER_NO0" value=""/>
			<input type="hidden" name="BRAND_CODE0" id="BRAND_CODE0" value=""/>
			<input type="hidden" name="SERIES_CODE0" id="SERIES_CODE0" value=""/>
			<input type="hidden" name="MODEL_CODE0" id="MODEL_CODE0" value=""/>
			<input type="hidden" name="ID" value="<%=id%>" />
			<input type="hidden" name="timeId" id="timeId" />
			<input type="hidden" name="list01" id="list01"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_01")%>" />
			<input type="hidden" name="list02" id="list02"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_02")%>" />
			<input type="hidden" name="list03" id="list03"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_03")%>" />
			<input type="hidden" name="list04" id="list04"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_04")%>" />
			<input type="hidden" name="OTHERFEE" id="OTHERFEE"
				value="<%=request.getAttribute("OTHERFEE")%>" />
				<input type="hidden" name="ACTIVITYCOMBO0" id="ACTIVITYCOMBO0"
				value="<%= request.getAttribute("ACTIVITYCOMBO")%>" />
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						经销商代码：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getDealerCode())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						经销商名称：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getDealerName())%>
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索赔申请单号：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getClaimNo())%>
					</td>
					<td  style="table_edit_2Col_label_7Letter">
						维修工单号-行号：
					</td>
					<td align="left">
						<input type='text' name='RO_NO' id='RO_NO' class="middle_txt"
							readonly='true' datatype="0,is_digit_letter,19"
							value='<%=CommonUtils.checkNull(tawep.getRoNo())%>' />
						-
						<input type='text' name='LINE_NO' id='LINE_NO' class="mini_txt"
							readonly='true' datatype="0,is_digit,2"
							value='<%=CommonUtils.checkNull(tawep.getLineNo())%>' />
					</td>
					</tr>
					<tr>
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td>
					<td nowrap="nowrap">
						<input type="text" name="RO_STARTDATE" id="RO_STARTDATE" class="short_txt"
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getRoStartdate()))%>'
							datatype="0,is_date_now,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"
							callFunction="showcalendar(event, 'RO_STARTDATE', false);" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结束时间：
					</td>
					<td>
						<input type="text" name="RO_ENDDATE" id="RO_ENDDATE" class="short_txt"
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getRoEnddate()))%>'
							datatype="0,is_date_now,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"
						callFunction="showcalendar(event, 'RO_ENDDATE', false);" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程数：
					</td>
					<td>
						<input type='text' name='IN_MILEAGE' id='IN_MILEAGE' datatype="0,is_double" 
							value='<%=CommonUtils.checkNull(tawep.getInMileage())%>'
							class="middle_txt" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						接 待 员：
					</td>
					<td>
						<input type='text' name='SERVE_ADVISOR' id='SERVE_ADVISOR' 
							datatype="0,is_digit_letter_cn,10"
							value='<%=CommonUtils.checkNull(tawep.getServeAdvisor())%>'
							class="middle_txt" />
					</td>
					
				</tr>
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						经销商电话：
					</td>
					<td>
						<%=request.getAttribute("phone")%>
					</td>
				</tr>
				
				</table>
				<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td class="table_edit_3Col_label_5Letter" >
						VIN：
					</td>
					<td align="left">
						<input type='text' name='VIN' id='VIN' datatype="0,is_vin" 
						value='<%=CommonUtils.checkNull(tawep.getVin())%>'
							class="middle_txt" 
							 blurback="true"/>
							
						<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">牌照号：</span>
					</td>
					<td>
					<input type="text" value="<%=CommonUtils.checkNull(tawep.getLicenseNo())%>" name="LICENSE_NO" id="LICENSE_NO"
							class="short_txt" /></td>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">发动机号：</span>
					</td>
					<td align="left" >
					<input type="text" id="ENGINE_NO" name="ENGINE_NO" value="<%=CommonUtils.checkNull(tawep.getEngineNo())%>" class="short_txt"/></td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						品牌：
					</td>
					<td id="BRAND_NAME">
						<%=CommonUtils.checkNullEx(tawep.getBrandName())%>
					</td>
					<td class="table_edit_3Col_label_7Letter">
						车系：
					</td>
					<td id="SERIES_NAME"><%=CommonUtils.checkNull(tawep.getSeriesName())%>
					</td>
					<td class="table_edit_3Col_label_5Letter">
						车型：
					</td>
					<td id="MODEL_NAME"><%=CommonUtils.checkNull(tawep.getModelName())%></td>
				</tr>
				
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">变速箱号：</span>
					</td>
					<td id="GEARBOX_NO"><%=CommonUtils.checkNull(tawep.getGearboxNo())%></td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">后桥号：</span>
					</td>
					<td id="REARAXLE_NO"><%=CommonUtils.checkNull(tawep.getRearaxleNo())%></td>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">分动器号：</span>
					</td>
					<td id="TRANSFER_NO"><%=CommonUtils.checkNull(tawep.getTransferNo())%></td>
				</tr>
				<tr>
				<td class="table_edit_3Col_label_5Letter">
						<span class="zi">产地：</span>
					</td>
					<td>
					<script type="text/javascript">
            		 genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",false,"min_sel","","false",'');
           			assignSelect("YIELDLY","<%=CommonUtils.checkNull(tawep.getYieldly())%>");
           			</script> 
					</td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">保修开始日期：</span>
					</td>
					<td>
						<input type="text" name="GUARANTEE_DATE" id="GUARANTEE_DATE"  class="short_txt"
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getGuaranteeDate()))%>'
							datatype="0,is_date_now,10" hasbtn="true"
							 callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" />
					</td>
				</tr>
				</table>
			<table border="0" align="center" cellpadding="0" cellspacing="1"
				class="table_edit">
				<th colspan="8">
					<img class="nav" src="../../../img/subNav.gif" />
					申请内容
				</th>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							索赔类型：
					</td>
					<td class="tbwhite">
						<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",false,"short_sel","","true",'');
	       </script>
					</td>
					<td id="feeTableId" class="table_edit_2Col_label_5Letter">
					保养次数：
					</td>
					<td id="feeId">
					<input type="text" name="FREE_M_AMOUNT" id="FREE_M_AMOUNT"  value="<%=tawep.getFreeMAmount() %>" datatype="0,is_digit" blurback = "true"/>
					<input type="hidden" name="FREE_M_PRICE" value="<%=tawep.getFreeMPrice() %>" id="fee" />
					</td>
					<td id="activityTableId" class="table_edit_2Col_label_4Letter">
						活动编号：</td>
					<td id="activityTableId0"><select  id="ACTIVITYCOMBO" name="ACTIVITYCOMBO" onchange="setCheckbox(this);">
							<script type="text/javascript">
							var tec = document.getElementById('ACTIVITYCOMBO0').value;
							document.write(tec);
							assignSelectActivity('ACTIVITYCOMBO','<%=tawep.getCampaignCode()%>');
							</script>
						</select>
						</td>
						<td id="activityTableId1">
						是否固定费用：
						<%if(1==tawep.getIsFixing()) { %>
						<input type="checkbox" id="IS_FIX0" checked name="IS_FIX0" disabled/>
						<%}else { %>
						<input type="checkbox" id="IS_FIX0" name="IS_FIX0" disabled/>
						<%} %>
						<input type="hidden" id="IS_FIX" name="IS_FIX" value="" />
						<input type="hidden" id="CAMPAIGN_FEE" name="CAMPAIGN_FEE" value="" />
						<input type="hidden" id="CAMPAIGN_CODE" name="CAMPAIGN_CODE" value="" />
						</td>
				</tr>
				
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							故障描述：
					</td>
					<td class="tbwhite" >
						<textarea name='TROUBLE_DESC' id='TROUBLE_DESC' 
							datatype="0,is_textarea,100" rows='2' cols='28'><%=tawep.getTroubleDesc() == null ? "" : tawep
					.getTroubleDesc()%></textarea>
					</td>
				
					<td class="table_edit_2Col_label_5Letter">
							故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON' datatype="1,is_textarea,100"
							id='TROUBLE_REASON' rows='2' cols='28'><%=tawep.getTroubleReason() == null ? "" : tawep
					.getTroubleReason()%></textarea>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							维修措施：
					</td>
					<td  class="tbwhite">
						<textarea name='REPAIR_METHOD' datatype="1,is_textarea,100"
							id='REPAIR_METHOD' rows='2' cols='28'><%=tawep.getRepairMethod() == null ? "" : tawep
					.getRepairMethod()%></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
							申请备注：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea datatype="1,is_textarea,100" name='APP_REMARK'
							id='APP_REMARK' rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getRemark()) %></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					</td>
				</tr>
			</table>

			<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					索赔维修项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						<div align="center">
							标志
						</div>
					</td>
					<td>
						作业代码
					</td>
					<td>
						工时名称
					</td>
					<td>
						工时定额
					</td>
					<td>
						工时单价
					</td>
					<td>
						工时金额(元)
					</td>
					<td>
						故障代码
					</td>
					<td>
						质损区域
					</td>
					<td>
						质损类型
					</td>
					<td>
						质损程度
					</td>
					<td>
						<input type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('itemTable');" />
					</td>
				</tr>
				<tbody id="itemTable">
					<%
						for (int i = 0; i < itemLs.size(); i++) {
					%>
					<tr class="table_list_row1">
						<%
							ClaimListBean clb = itemLs.get(i);
							TtAsWrLabouritemPO tl = (TtAsWrLabouritemPO)clb.getMain();
						%>
						<td>
							<input type="hidden" name="LABOUR_ID"
								value="<%=tl.getLabourId()%>" />
							<input name="ITEM" disabled="true" checked="true" type="checkbox" />
							<input type="hidden" name="TEMP"/>
						</td>
						
						<td>
							<input type="text" class="short_txt" name="WR_LABOURCODE"
								value="<%=CommonUtils
								.checkNull(tl.getWrLabourcode())%>"
								size="10" readonly datatype="0,is_null" id="WR_LABOURCODES<%=i %>"/>
							<a href="#" onclick="selectMainTime(this);delItemIds(this,'update');">选择</a>
						</td>
						<td>
							<span class="tbwhite"><input type="text"
									name="WR_LABOURNAME"
									value="<%=CommonUtils
								.checkNull(tl.getWrLabourname())%>"
									size="10" readonly />
							</span>
						</td>
						<td>
							<input type="text" name="LABOUR_HOURS" class="little_txt"
								value="<%=CommonUtils.checkNull(tl
										.getLabourHours())%>"
								size="8" maxlength="11" id="LABOUR_HOURS" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_PRICE" class="little_txt"
								value="<%=CommonUtils.checkNull(tl
										.getLabourPrice())%>"
								size="8" maxlength="11" id="LABOUR_PRICE" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_AMOUNT" id="LABOUR_AMOUNTS<%=i %>" class="little_txt"
								value="<%=CommonUtils.checkNull(tl.getLabourAmount())%>"
								size="8" maxlength="9" datatype="0,is_double" decimal="2" readonly />
						</td>
						<td>
						<select  id="TROUBLE_CODE<%=i%>"  name="TROUBLE_CODE" class="min_sel">
							<script type="text/javascript">
							var tec = document.getElementById('list04').value;
							document.write(tec);
							assignSelect('TROUBLE_CODE'+'<%=i%>','<%=tl.getTroubleCode()%>');
							</script>
						</select>
						</td>
						<td>
						<select  id="DAMAGE_AREA<%=i%>" name="DAMAGE_AREA" class="min_sel">
							<script type="text/javascript">
							var tec = document.getElementById('list02').value;
							document.write(tec);
							assignSelect('DAMAGE_AREA'+'<%=i%>','<%=tl.getDamageArea()%>');
							</script>
						</select>
						</td>
						<td>
						<select  id="DAMAGE_TYPE<%=i%>" name="DAMAGE_TYPE" class="min_sel">
							<script type="text/javascript">
							var tec = document.getElementById('list03').value;
							document.write(tec);
							assignSelect('DAMAGE_TYPE'+'<%=i%>','<%=tl.getDamageType()%>');
							</script>
						</select>
						</td>
						<td>
						<select  id="DAMAGE_DEGREE<%=i%>" name="DAMAGE_DEGREE" class="min_sel">
							<script type="text/javascript">
							var tec = document.getElementById('list01').value;
							document.write(tec);
							assignSelect('DAMAGE_DEGREE'+'<%=i%>','<%=tl.getDamageDegree()%>');
							</script>
						</select>
						</td>
						<td>
						<div>
						<input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="addPlusRow('itemTable',this,'<%=tl.getWrLabourcode()%>');"></input>
						<input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItem(this.parentNode,'item');delItemIds(this,'del');"></input>
						</div>
						<div>
						</div>
						<div>
						</div>
						</td>
						</tr>
						<% 
							List<Object> tls = clb.getAdditional();
							if (tls!=null) {
								if (tls.size()>0){
								for (int j=0;j<tls.size();j++) {
								TtAsWrLabouritemPO talptm = (TtAsWrLabouritemPO)tls.get(j);
						%>	
						
						<tr class="table_list_row1">
							<td>
							<input type="hidden" name="LABOUR_ID0"
								value="<%=talptm.getLabourId()%>" />
							<input type="hidden" name="B_ML_CODE" value="<%=talptm.getBMlCode() %>"/>
							<input name="ITEM" disabled="true"  type="checkbox" />
						</td>
						
						<td>
							<span class="tbwhite"><a href="#" onclick="selectTime();"></a>
							</span>
							<input type="text" class="short_txt" name="WR_LABOURCODE0"
								value="<%=CommonUtils
								.checkNull(talptm.getWrLabourcode())%>"
								size="10" readonly datatype="0,is_null" id="WR_LABOURCODES0<%=i %>"/>
							<a href="#" onclick="selectTime(this);delItemIds(this,'update');">选择</a>
						</td>
						<td>
							<span class="tbwhite"><input type="text"
									name="WR_LABOURNAME0"
									value="<%=CommonUtils
								.checkNull(talptm.getWrLabourname())%>"
									size="10" readonly />
							</span>
						</td>
						<td>
							<input type="text" name="LABOUR_HOURS0" class="little_txt"
								value="<%=CommonUtils.checkNull(talptm
										.getLabourHours())%>"
								size="8" maxlength="11" id="LABOUR_HOURS0" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_PRICE0" class="little_txt"
								value="<%=CommonUtils.checkNull(talptm
										.getLabourPrice())%>"
								size="8" maxlength="11" id="LABOUR_PRICE0" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_AMOUNT0" id="LABOUR_AMOUNTS0<%=i %>" class="little_txt"
								value="<%=CommonUtils.checkNull(talptm.getLabourAmount())%>"
								size="8" maxlength="9" datatype="0,is_double" decimal="2" readonly />
						</td>
						<td>
						</td>
							<td>
						</td>
							<td>
						</td>
							<td>
						</td>
						<td>
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="javascript:delItem(this,'item');delItemIds(this,'del');" />
						</td>
						</tr>
						<%
						}
						}
						}
						}
						%>
						
					<%
					%>
				</tbody>
			</table>



			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="12" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					索赔配件
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						标志
					</td>
					<td>
						换上件代码
					</td>
					<td>
						换下件代码
					</td>
					<td>
						换上件名称
					</td>
					<td>
						换上件数量
					</td>
					<td>
						单价
					</td>
					<td>
						金额(元)
					</td>
					<td>
						换上件供应商名称
					</td>
					<td>
						换下件供应商名称
					</td>
					<td>
						故障描述
					</td>
					<td>
						索赔工时
					</td>
					<td>
						<input type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:genAppTimeCombo1(addRow('partTable'));" />
					</td>
				</tr>
				<tbody id="partTable">
					<%
						for (int i = 0; i < partLs.size(); i++) {
					%>
					<%
							ClaimListBean clb = partLs.get(i);
							TtAsWrPartsitemPO tl = (TtAsWrPartsitemPO)clb.getMain();
						%>
					<tr class="table_list_row1">
						
						<td>
							<input type="hidden" name="PART_ID"
								value="<%=tl.getPartId()%>" />
							<input name="PART" type="checkbox" disabled="true" checked
								onClick="javascript:checkCheckBox(this);" />
							<input type="hidden" name="B_MP_CODE" value=""/>
						</td>
						<td>
							<input type="text" name="PART_CODE" class="short_txt"
								value="<%=CommonUtils.checkNull(tl.getPartCode())%>"
								size="10" id="PART_CODE" readonly />
							<span class="tbwhite"><a href="#"
								onClick="javascript:selectPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<input type="text" name="DOWN_PART_CODE" class="short_txt"
								value="<%=CommonUtils
								.checkNull(tl.getDownPartCode())%>"
								size="10" readonly/>
							<span class="tbwhite"><input type="hidden"
									name="DOWN_PART_NAME" readonly
									value="<%=CommonUtils
								.checkNull(tl.getDownPartName())%>"
									size="10" maxlength="13" readonly />
								<input type="hidden" name="s" value="1" size="10" maxlength="13"
									readonly /><a href="#"
								onClick="javascript:selectDownPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<span class="tbwhite"><input type="text" name="PART_NAME" class="short_txt"
									readonly
									value="<%=CommonUtils.checkNull(tl.getPartName())%>"
									id="PART_NAME" name="PART_SN3" size="10" />
							</span>
						</td>
						<td>
							<input type="text"  class="short_txt" datatype="0,isDigit"  size="10" blurback = "true"
								name="QUANTITY" id="QUANTITYS<%=i%>"
								value="<%=(tl.getQuantity().intValue())%>"
								maxlength="20" />
						</td>
						<td>
							<input type="text" class="little_txt" name="PRICE"
								value="<%=CommonUtils.checkNull(tl.getPrice())%>"
								size="10" maxlength="11" id="PRICE" readOnly = "true" />
						</td>
						<td>
							<input type="text" class="little_txt" name="AMOUNT" id="AMOUNT" size="10"
								value="<%=CommonUtils.checkNull(tl
										.getAmount())%>"
								maxlength="9"  readonly />
						</td>
						<td>
							<input type="text" class="short_txt" name="PRODUCER_CODE" id="PRODUCER_CODE"
								value="<%=CommonUtils
								.checkNull(tl.getProducerCode())%>" readonly/>
							<input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME"
								value="<%=CommonUtils
								.checkNull(tl.getProducerName())%>" />
						</td>
						<td>
							<input type="text" class="short_txt" name="PRODUCER_CODE1" id="PRODUCER_CODE1"
								value="<%=CommonUtils
								.checkNull(tl.getDownProductCode())%>" readonly/>
							<input type="hidden" name="PRODUCER_NAME1" id="PRODUCER_NAME1"
								value="<%=CommonUtils
								.checkNull(tl.getDownProductName())%>" />
						</td>
						<td>
							<input type="text" class="short_txt" name="REMARK" id="REMARK"
								value="<%=CommonUtils.checkNull(tl
										.getRemark())%>"
								size="10" maxlength="13" />
						</td>
						<td >
						<script type="text/javascript">
						document.write(genAppTimeComboStr("COMBO<%=i%>"));
						assignSelect('COMBO<%=i%>','<%=tl.getWrLabourcode()%>');
						</script>
						</td>
						<td>
						<div>
						<input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow('partTable',this,'<%=tl.getPartCode() %>');"/>
						<input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,'part');delPartIds(this,'del');"/>
						</div>
						<div>
						</div>
						<div>
						</div>
						</td>
					</tr>
					<% 
							List<Object> tls = clb.getAdditional();
							if (tls!=null) {
								if (tls.size()>0){
								for (int j=0;j<tls.size();j++) {
								TtAsWrPartsitemPO talptm = (TtAsWrPartsitemPO)tls.get(j);
						%>	
						<tr class="table_list_row1">
						<td>
							<input type="hidden" name="PART_ID"
								value="<%=talptm.getPartId()%>" />
							<input name="PART0" type="checkbox" disabled="true"
								onClick="javascript:checkCheckBox(this);" />
								<input type="hidden" name="B_MP_CODE" value="<%=talptm.getBMpCode() %>"/>
						</td>
						<td>
							<input type="text" name="PART_CODE" class="short_txt"
								value="<%=CommonUtils.checkNull(talptm.getPartCode())%>"
								size="10" id="PART_CODE" readonly />
							<span class="tbwhite"><a href="#"
								onClick="javascript:selectPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<input type="text" name="DOWN_PART_CODE" class="short_txt"
								value="<%=CommonUtils
								.checkNull(talptm.getDownPartCode())%>"
								size="10" readonly/>
							<span class="tbwhite"><input type="hidden"
									name="DOWN_PART_NAME" readonly
									value="<%=CommonUtils
								.checkNull(talptm.getDownPartName())%>"
									size="10" maxlength="13" readonly />
								<input type="hidden" name="s" value="1" size="10" maxlength="13"
									readonly /><a href="#"
								onClick="javascript:selectDownPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<span class="tbwhite"><input type="text" name="PART_NAME" class="short_txt"
									readonly
									value="<%=CommonUtils.checkNull(talptm.getPartName())%>"
									id="PART_NAME" name="PART_SN3" size="10" />
							</span>
						</td>
						<td>
							<input type="text"  class="short_txt" datatype="0,isDigit"  size="10" blurback = "true"
								name="QUANTITY" id="QUANTITYS0<%=i%>"
								value="<%=(talptm.getQuantity().intValue())%>"
								maxlength="20" />
						</td>
						<td>
							<input type="text" class="little_txt" name="PRICE"
								value="<%=CommonUtils.checkNull(talptm.getPrice())%>"
								size="10" maxlength="11" id="PRICE" readOnly = "true" />
						</td>
						<td>
							<input type="text" class="little_txt" name="AMOUNT" id="AMOUNT" size="10"
								value="<%=CommonUtils.checkNull(talptm
										.getAmount())%>"
								maxlength="9"  readonly />
						</td>
						<td>
							<input type="hidden" class="short_txt" name="PRODUCER_CODE" id="PRODUCER_CODE"
								value="<%=CommonUtils
								.checkNull(talptm.getProducerCode())%>" readonly/>
							<input type="text" name="PRODUCER_NAME" id="PRODUCER_NAME"
								value="<%=CommonUtils
								.checkNull(talptm.getProducerName())%>" />
						</td>
						<td>
							<input type="hidden" class="short_txt" name="PRODUCER_CODE1" id="PRODUCER_CODE1"
								value="<%=CommonUtils
								.checkNull(talptm.getDownProductCode())%>" readonly/>
							<input type="text" name="PRODUCER_NAME1" id="PRODUCER_NAME1"
								value="<%=CommonUtils
								.checkNull(talptm.getDownProductName())%>" />
						</td>
						<td>
							<input type="text" class="short_txt" name="REMARK" id="REMARK"
								value="<%=CommonUtils.checkNull(talptm
										.getRemark())%>"
								size="10" maxlength="13" />
						</td>
						<td>
						<input type="hidden" name="PART" value="off"/>
						<input type="hidden" name="appTime" value="" />
						</td>
						<td>
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="javascript:delPartItem(this,'part');delPartIds(this,'del');" />
						</td>
					</tr>
					<%
					}}}
						}
					%>
				</tbody>
			</table>

			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<th colspan="8" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					其他项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						<div align="center">
							是否授权
						</div>
					</td>
					<td>
						<div align="center">
							项目代码
						</div>
					</td>
					<td>
						<div align="center">
							项目名称
						</div>
					</td>
					<td>
						<div align="center">
							金额(元)
						</div>
					</td>
					<td>
						备注
					</td>
					<td>
						<input type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('otherTable');"/>
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {
					%>
					<tr class="table_list_row1" >
						<td>
						</td>
						<td>
							<div align="center">
								<select onchange="setOtherName(this);" id="ITEM_CODE<%=i%>"
									name="ITEM_CODE">
									<script type="text/javascript">var tec = document.getElementById('OTHERFEE').value;document.write(tec);assignSelect('ITEM_CODE'+'<%=i%>','<%=otherLs.get(i).getItemCode()%>');</script>
								</select>
							</div>
						</td>
						<td>
							<div align="center">
								<span class="tbwhite"><input type="text" name="ITEM_NAME" readonly
										id="ITEM_NAME" value="<%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%>"
										class="short_txt" maxlength="20" />
								</span>
							</div>
						</td>
						<td>
							<div align="center">
								<input type="text" name="ITEM_AMOUNT" id="ITEM_AMOUNT"
									value="<%=CommonUtils.checkNull(otherLs.get(i).getAmount())%>"
									datatype="0,is_yuan" class="short_txt" />
							</div>
						</td>
						<td>
							<div align="center">
								<input type="text"
										name="ITEM_REMARK" id="ITEM_REMARK"
										datatype="1,is_digit_letter_cn,100" class="middle_txt"
										value="<%=CommonUtils.checkNull(otherLs.get(i).getRemark())%>" />
								
							</div>
						</td>
						<td>
						<div align="center">
							<input type="hidden" name="OTHER_ID"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getNetitemId())%>" />
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="javascript:delItemOther(this);delOtherIds(this,'del');" />
								</div>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			<TABLE class="table_edit">
						<tr>
              <th colspan="10"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
              申请费用</th>
            </tr>
            </TABLE>
			<table class="table_edit">
            <tr>
              <td class="table_edit_4Col_label_7Letter" nowrap="nowrap" >基本工时：</td>
              <td  id="BASE_LABOUR"><%=tawep.getLabourHours() %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >基本工时金额：</td>
              <td  nowrap="nowrap" id="BASE_LABOUR_AMOUNT" ><%=CommonUtils.formatPrice(tawep.getStdLabourAmount()) %></td>
              <td  class="table_edit_4Col_label_7Letter" nowrap="nowrap" >附加工时：</td>
              <td id="ADD_LABOUR"><%=tawep.getReinforceHours() %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >附加工时金额：</td>
              <td id="ADD_LABOUR_AMOUNT"><%=CommonUtils.formatPrice(tawep.getReinLabourAmount()) %></td>
            </tr>
            <tr>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >配件金额：</td>
              <td id="ALL_PART_AMOUNT"><%=CommonUtils.formatPrice(tawep.getPartAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >工时金额：</td>
              <td id="ALL_LABOUR_AMOUNT"><%=CommonUtils.formatPrice(tawep.getLabourAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >其它费用金额：</td>
              <td id="OTHER_AMOUNT"><%=CommonUtils.formatPrice(tawep.getNetitemAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >保养金额：</td>
              <td id="GAME_AMOUNT"><%=CommonUtils.formatPrice(tawep.getFreeMPrice()) %></td>
            </tr>
            <tr>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >服务活动金额：</td>
              <td id="ACTIVITY_AMOUNT"><%=CommonUtils.formatPrice(tawep.getCampaignFee()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >索赔申请金额：</td>
              <td id="APPLY_AMOUNT"><%=CommonUtils.formatPrice(tawep.getRepairTotal()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >税额：</td>
              <td id="TAX"><%=CommonUtils.formatPrice(tawep.getTaxSum()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >总金额：</td>
              <td id="ALL_AMOUNT"><%=CommonUtils.formatPrice(tawep.getGrossCredit()) %></td>
            </tr>
          </table>   
            <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
					<th><span align="left"><input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    			<%} %>
 			</table>
				
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td align="center">
						<input type="button" onClick="confirmUpdate()" class="normal_btn"
							style="" value="确定" />
						&nbsp;&nbsp;
						<input type="button" onClick="goBack();"
							class="normal_btn" style="" value="返回" />
					</td>
				</tr>
			</table>

		</form>
		<script type="text/javascript">
getTypeChangeStyle('<%=tawep.getClaimType()%>');
	//选择工时联动故障代码
	function chooseItem() {
        var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
    }
    //选择工时联动故障代码(产生服务活动后，点击故障代码下拉框)
	function chooseItem2(itemId) {
        //var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode2,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode2(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
    	myobj.parentNode.innerHTML = last;
    }
    //选择工时联动故障代码(产生服务活动后，点击故障代码下拉框)
	function chooseItem3(itemId) {
        //var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode3,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode3(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
     	MyAlert(myobj.options.length);
    	myobj.parentNode.innerHTML = last;
    	MyAlert(myobj.parentNode.innerHTML);
    	MyAlert(myobj.options.length);
    	assignSelectByObj(myobj,objValue);
    }
    
    //选择索赔类型，隐藏显示表格
 function getTypeChangeStyle(obj) {
		 if (first) {
		 	document.getElementById('ACTIVITYCOMBO').selectedIndex=0;
		 	zeroAllFee();
  			clearAllNode(document.getElementById('itemTable'),'item');
			clearAllNode(document.getElementById('partTable'),'part');
			clearAllNode(document.getElementById('otherTable'),'other');
		 }
    	if(obj=='<%=Constant.CLA_TYPE_01%>') {//一般索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_02%>') {//免费保养
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('feeTableId').style.display='';
    		document.getElementById('feeId').style.display='';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_03%>') {//追加费用
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_04%>') {//重复修理索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_05%>') {//零件索赔更换
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_06%>') {//服务活动
    		if ('1'=='<%=tawep.getIsFixing()%>') { //是固定费用
	    		document.getElementById('itemTableId').style.display='none';
	    		document.getElementById('partTableId').style.display='none';
	    		document.getElementById('otherTableId').style.display='none';
    		}else {
	    		document.getElementById('itemTableId').style.display='';
	    		document.getElementById('partTableId').style.display='';
	    		document.getElementById('otherTableId').style.display='';
    		}
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='';
    		document.getElementById('activityTableId0').style.display='';
    		document.getElementById('activityTableId1').style.display='';
    		//document.getElementById("activity").style.display='';
    	}else if(obj=='<%=Constant.CLA_TYPE_07%>') {//PDI索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_08%>') {//保外索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}else {
    	document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		//document.getElementById("activity").style.display='none';
    	}
    }
    //加载工时，得到第一项即主工时的值
    function chooseItem1() {
        var itemId = document.getElementById('timeId').value;
        var url0 = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url0,changeTroubleCode1,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode1(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
    	//为故障代码下拉框赋值
    	assignSelect("TROUBLE_CODE","<%=tawep.getTroubleCode()%>");
    }
    
    //为下拉框赋值
	assignSelect("CLAIM_TYPE","<%=tawep.getClaimType()%>");
	//assignSelect("DAMAGE_AREA","<%=tawep.getDamageArea()%>");
	//assignSelect("DAMAGE_TYPE","<%=tawep.getDamageType()%>");
	//assignSelect("DAMAGE_DEGREE","<%=tawep.getDamageDegree()%>");
	
	//chooseItem1();
	var options =  document.getElementById("CLAIM_TYPE").options;
   	var index = options.selectedIndex;
   	var myvalue = options[index].value;
	getTypeChangeStyle(myvalue);
	first=true;
	//这里给下拉框动态赋一个ONCLICK事件
	var obj = document.getElementById("CLAIM_TYPE");
	//MyAlert(obj);
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	
   	function blurBack(obj){
   		if (obj=="VIN") {
   			oneVIN();
   		}else {
   		if (obj!='FREE_M_AMOUNT') {
   	   		countQuantity(document.getElementById(obj));
   	   	}
   	   	
   	   	setFee0(document.getElementById('FREE_M_AMOUNT'));
   	   	}
   	}
   	 function delUploadFile(obj){
   	 document.getElementById("attIds").value += ","+obj.parentElement.parentElement.cells.item(0).childNodes[1].value;
   	 //MyAlert(obj.parentElement.parentElement.cells.item(0).childNodes[1].value);
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('fileUploadTab');
		tbl.deleteRow(idx);
	}
</script>
	</BODY>
</html>
