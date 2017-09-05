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
			List<TtAsWrLabouritemPO> itemLs = (LinkedList<TtAsWrLabouritemPO>) request.getAttribute("itemLs");
			List<TtAsWrPartsitemPO> partLs = (LinkedList<TtAsWrPartsitemPO>) request.getAttribute("partLs");
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
		%>
		<%
			List feeTypeList = (List) request.getAttribute("FEETYPE");//保养费用集合 
			HashMap hm = (HashMap) request.getAttribute("FEE"); //保养费用参数对应的值
			if (hm == null) {
				hm = new HashMap();
			}
			TtAsActivityPO tap = (TtAsActivityPO) request.getAttribute("ACTIVITY");
		%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔申请创建</TITLE>
		<SCRIPT LANGUAGE="JavaScript">
	var myobj;
	var modelId = '<%=tawep.getModelId()%>';
	var itemToDel='';
	var itemToUp='';
	var partToDel='';
	var partToUp='';
	var otherToDel='';
	var otherToUp='';
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
	function confirmUpdate() {
		MyConfirm("是否修改？",confirmUpdate0,[]);
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
		if (document.getElementById("CLAIM_TYPE").value==""||document.getElementById("CLAIM_TYPE").value==null) {
			MyAlert("请选择索赔类型！");
			return false;
		}
		if (document.getElementById("TROUBLE_CODE").value==""||document.getElementById("TROUBLE_CODE").value==null) {
			MyAlert("请选择故障代码！");
			return false;
		}
		if (document.getElementById("DAMAGE_AREA").value==""||document.getElementById("DAMAGE_AREA").value==null) {
			MyAlert("请选择质损区域！");
			return false;
		}
		if (document.getElementById("DAMAGE_TYPE").value==""||document.getElementById("DAMAGE_TYPE").value==null) {
			MyAlert("请选择质损类型！");
			return false;
		}
		if (document.getElementById("DAMAGE_DEGREE").value==""||document.getElementById("DAMAGE_DEGREE").value==null) {
			MyAlert("请选择质损程度！");
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
    	if(document.getElementById('partTable').childNodes.length==0) {
    		MyAlert("一般索赔需要添加至少一个主配件");
    		return false;
    	}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_02%>') {//免费保养
    		clearAllNode(document.getElementById('itemTable'));
    		clearAllNode(document.getElementById('partTable'));
    		clearAllNode(document.getElementById('otherTable'));
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_03%>') {//追加费用
    		clearAllNode(document.getElementById('itemTable'));
    		clearAllNode(document.getElementById('partTable'));
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_04%>') {//重复修理索赔
    	//需要有主要工时，配件
    	if(document.getElementById('itemTable').childNodes.length==0) {
    		MyAlert("重复修理索赔需要添加至少一个主工时");
    		return false;
    	}
    	if(document.getElementById('partTable').childNodes.length==0) {
    		MyAlert("重复修理索赔需要添加至少一个主配件");
    		return false;
    	}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_05%>') {//零件索赔更换
    	//需要有主要工时，配件
    	if(document.getElementById('itemTable').childNodes.length==0) {
    		MyAlert("零件索赔更换需要添加至少一个主工时");
    		return false;
    	}
    	if(document.getElementById('partTable').childNodes.length==0) {
    		MyAlert("零件索赔更换需要添加至少一个主配件");
    		return false;
    	}
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_06%>') {//服务活动
    		clearAllNode(document.getElementById('itemTable'));
    		clearAllNode(document.getElementById('partTable'));
    		clearAllNode(document.getElementById('otherTable'));
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_07%>') {//PDI索赔
    		clearAllNode(document.getElementById('itemTable'));
    		clearAllNode(document.getElementById('partTable'));
    		clearAllNode(document.getElementById('otherTable'));
    	}else if(options[index].value=='<%=Constant.CLA_TYPE_08%>') {//保外索赔
    		clearAllNode(document.getElementById('itemTable'));
    		clearAllNode(document.getElementById('partTable'));
    		clearAllNode(document.getElementById('otherTable'));
    	}
		if(submitForm('fm')){
		var fm = document.getElementById('fm');
		fm.action='<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationUpdate.do?ITEM_DEL='+itemToDel+'&PART_DEL='+partToDel+'&OTHER_DEL='+otherToDel;
		fm.submit();
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
			myobj.cells.item(6).innerHTML = '<td><input type="text" class="short_txt" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" datatype="0,is_yuan" readonly/></td>';
		}else {
			//MyAlert("请输入数量！");
		}
		
	}
	//计算费用
	function countQuantity1(obj) {
	//MyAlert(obj.value);
		var price = myobj.cells.item(5).childNodes[0].value;
		var quantity = obj.value;
		if (quantity!=null&&quantity!=""){
			myobj.cells.item(6).innerHTML = '<input type="text" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" onBlur="javascript:countQuantity(this);"/>';
		}else {
			MyAlert("请输入数量！");
		}
		
	}
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVinForward.do',800,500);
	}
	//获取子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME){
		document.getElementById("VIN").value = VIN;
		document.getElementById("LICENSE_NO").innerHTML = LICENSE_NO;
		document.getElementById("BRAND_NAME").innerHTML = BRAND_NAME;
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("REARAXLE_NO").innerHTML = REARAXLE_NO;
		document.getElementById("GEARBOX_NO").innerHTML = GEARBOX_NO;
		modelId = MODEL_ID;//设置全局变量车型组
	}
	//工时选择
	function setTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue) {
		
		//MyAlert(myobj.cells.item(0).innerHTML);
		if(getRowNo(myobj)==0){
		//clearAllNode(myobj.parentNode,'item');
		//如果是查询出的结果
		if (myobj.cells.item(0).childNodes.length==4) {
		}else {
			myobj.cells.item(0).innerHTML='<input name="isMain2" disabled="true"  type="checkbox" checked="true" onClick="javascript:checkCheckBox(this);"/>';
		}
		//为隐藏域赋值工时的ID
		document.getElementById('timeId').value=labourCode;
		chooseItem(); //联动故障代码下拉框
		//myobj.cells.item(0).innerHTML='<input name="isMain2" disabled="true"  type="checkbox" checked="true" onClick="javascript:checkCheckBox(this);"/>';
		}else {
		if (myobj.cells.item(0).childNodes.length==4) {
		}else {
		myobj.cells.item(0).innerHTML='<input name="isMain2" disabled="true"  type="checkbox"  onClick="javascript:checkCheckBox(this);"/>';
			}
		}
		//MyAlert(myobj.cells.item(1).childNodes[2].value);
		myobj.cells.item(1).innerHTML='<span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="short_txt"   name="WR_LABOURCODE"  value="'+labourCode+'" size="10"/><a href="#"  onclick="selectTime(this);">选择</a>'
		myobj.cells.item(2).innerHTML='<span class="tbwhite"><input type="text" name="WR_LABOURNAME"  value="'+cnDes+'" size="10" readonly/></span>';
		myobj.cells.item(3).innerHTML='<input type="text" name="LABOUR_HOURS" class="short_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(4).innerHTML='<input type="text" name="LABOUR_PRICE" class="short_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/>';
		myobj.cells.item(5).innerHTML='<input type="text" name="LABOUR_AMOUNT" class="short_txt"   value="'+labourQuotiety*parameterValue+'" size="8" maxlength="9" readonly="true"/>';
		
	}
	//配件选择
	function setPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
		if(getRowNo(myobj)==0){
	    	myobj.cells.item(0).innerHTML='<td><input name="PART"  disabled="true" checked="true" type="checkbox" /></td>';
	    }else {
	    	myobj.cells.item(0).innerHTML='<td><input name="PART"  disabled="true" type="checkbox" /></td>';
	    }
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
	    //myobj.cells.item(4).innerHTML =  '<td><input type="text" datatype="0,is_double" decimal="2" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
		//myobj.cells.item(6).innerHTML =  '<td><input type="text" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" readonly readonly/></td>';
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		//myobj.cells.item(2).innerHTML='<input type="text" name="DOWN_PART_CODE" readonly size="10"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="1" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="short_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
	}
	function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode) {
		myobj.cells.item(2).innerHTML='<input type="text" class="short_txt" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true" /><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span>';
	}
	 // 动态生成表格
 	function addRow(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
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
		}
		if (tableId=='partTable'){
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
		}
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input name="ITEM"   type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="short_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0"  value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="short_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="short_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="short_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}else if (tableId == 'partTable') {
			addTable.rows[length].cells[0].innerHTML =  '<td><input name="PART0" type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="short_txt" name="PART_CODE0" datatype="0,is_null"  value="" size="10" id="PART_CODE0" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="short_txt" name="DOWN_PART_CODE" readonly size="10"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME" readonly value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME0" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,is_digit" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="short_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" readonly /></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="text" class="short_txt" name="PRODUCER_CODE0" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME0" id="PRODUCER_NAME" /></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			addTable.rows[length].cells[9].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
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
		//选择工时
		function selectTime(obj) {
			myobj = getRowObj(obj);
			var treeCode = 3;
			var timeId;
			if(getRowNo(myobj)==0){
				treeCode=3;
				timeId='';
				openTime(obj,treeCode,timeId);
				//MyDivConfirm("更改主工时将会随之删除附加工时，确认修改吗？",openTime,[obj,treeCode,timeId]);
			}else {
				treeCode=4;
				timeId = document.getElementById("timeId").value;
				OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
			}
		}
		//修改主项目工时需要删除附加工时
		function openTime(obj,treeCode,timeId){
		var len = obj.parentNode.parentNode.parentNode.childNodes.length;
		for (var i=1;i<len;i++) {
			if(obj.parentNode.parentNode.parentNode.childNodes[1].childNodes[0].childNodes.length==4) {
				itemToDel = itemToDel+","+obj.parentNode.parentNode.parentNode.childNodes[1].childNodes[0].childNodes[0].value;
				//MyAlert(obj.parentNode.parentNode.parentNode.childNodes[1].childNodes[0].childNodes[0].value);
			}
			obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode.parentNode.childNodes[1]);
		}
			parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
		}
		//带出费用名称
		function setOtherName(obj){
			myobj = getRowObj(obj);
			//for (var i in myobj.cells.item(0)) {
			//	MyAlert(i);
			//}
			var options = myobj.cells.item(0).childNodes[0].childNodes[0].options;
			var index = options.selectedIndex;
			var text = options[index].title;
			//MyAlert(text);
			//MyAlert(myobj.cells.item(0).childNodes[0].childNodes[0].options.selectedIndex);
			myobj.cells.item(1).innerHTML='<td><div align="center"><input readonly type="text" name="ITEM_NAME" class="short_txt" value="'+text+'" datatype="0,is_digit_letter_cn,100"  id="ITEM_NAME"/></div></td>';
			//var line =  getRowNo(myobj);
			//MyAlert(line);
		}
		//选择上件
		function selectPartCode(obj){
			myobj = getRowObj(obj);
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
		
		//删除行
		function delItem(obj,name){
		    var tr = this.getRowObj(obj);
		    if(getRowNo(tr)==0) {
		    	MyConfirm("删除主工时将会随之删除附加工时，确认删除吗？",delItems,[tr,name]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//删除行 配件
		function delPartItem(obj,name){
		    var tr = this.getRowObj(obj);
		    if(getRowNo(tr)==0) {
		    	MyConfirm("删除主工时将会随之删除其他配件，确认删除吗？",delItems,[tr,name]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//
		function delItems(tr,name){
		   if(tr != null){
		    clearAllNode(tr.parentNode,name);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
		
		
		//循环删除节点
		function clearAllNode(parentNode,name){
		    while (parentNode.firstChild) {
		      var oldNode = parentNode.removeChild(parentNode.firstChild);
		      //MyAlert(oldNode.childNodes[0].childNodes.length);
		      //MyAlert(oldNode.childNodes[0].innerHTML);
		      if (oldNode.childNodes[0].childNodes.length==4){
		      if (name=='item'){
		      	//MyAlert(oldNode.childNodes[0].childNodes[0].value);
		      	itemToDel = itemToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}else if (name=='part') {
		      	partToDel = partToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}
		      }
		      if (name=='other') {
		      if(oldNode.childNodes[4].childNodes.length==2){
		      	otherToDel = otherToDel+ ","+ oldNode.childNodes[4].childNodes[0].value;
		      }
		      	//otherToDel = 
		      }
		     // MyAlert(itemToDel);
		     // MyAlert(partToDel);
		       oldNode = null;
		     }
		   } 
		//删除行其他项目
		function delItemOther(obj){
		    var tr = this.getRowObj(obj);
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
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
		  	}
		}
		}
		//下拉框修改时赋值
		function assignSelectByObj(obj,value) {
	
		var sel = document.getElementById(name);
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
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
	
</SCRIPT>
	</HEAD>
	<BODY onload="doInit();" onkeydown="keyListnerResp();">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单维护
		</div>

		<form method="post" name="fm" id="fm">
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
							readonly='true'
							value='<%=CommonUtils.checkNull(tawep.getRoNo())%>' />
						-
						<input type='text' name='LINE_NO' id='LINE_NO' class="mini_txt"
							readonly='true'
							value='<%=CommonUtils.checkNull(tawep.getLineNo())%>' />
					</td>
					</tr>
					<tr>
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td>
					<td nowrap="nowrap">
						<input type="text" name="RO_STARTDATE" id="RO_STARTDATE" readonly
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getRoStartdate()))%>'
							datatype="0,is_date,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"/>
							<!-- callFunction="showcalendar(event, 'RO_STARTDATE', false);" -->
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结束时间：
					</td>
					<td>
						<input type="text" name="RO_ENDDATE" id="RO_ENDDATE" readonly
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getRoEnddate()))%>'
							datatype="0,is_date,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"/>
						<!--  	callFunction="showcalendar(event, 'RO_ENDDATE', false);" -->
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程数：
					</td>
					<td>
						<input type='text' name='IN_MILEAGE' id='IN_MILEAGE' datatype="0,is_double" readonly
							value='<%=CommonUtils.checkNull(tawep.getInMileage())%>'
							class="middle_txt" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						<span class="zi">保修开始日期：</span>
					</td>
					<td>
						<input type="text" name="GUARANTEE_DATE" id="GUARANTEE_DATE" readonly
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getGuaranteeDate()))%>'
							datatype="0,is_date,10" hasbtn="true"/>
							<!--  callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" -->
					</td>
					
				</tr>
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						接 待 员：
					</td>
					<td>
						<input type='text' name='SERVE_ADVISOR' id='SERVE_ADVISOR' readonly
							datatype="0,is_digit_letter_cn,10"
							value='<%=CommonUtils.checkNull(tawep.getServeAdvisor())%>'
							class="middle_txt" />
					</td>
				</tr>
				<tr id="activity">
					<td class="table_edit_2Col_label_7Letter">
						<span class="zi">活动编号：</span>
					</td>
					<td>
						<input type='text' name='CAMPAIGN_CODE' id='CAMPAIGN_CODE' readonly
							class="middle_txt" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						是否固定费用：
					</td>
					<td>
						<input type='checkbox' name='RO_NO' id='1410068' readonly='true' disabled="true"
							value='N5510500RO070800018'/>
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
						<input type='text' name='VIN' id='VIN' datatype="0,is_vin" readonly
						value='<%=CommonUtils.checkNull(tawep.getVin())%>'
							class="middle_txt" />
							<!-- style="cursor: pointer;"  onclick="showVIN()"-->
							
						<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					<td class="table_edit_3Col_label_4Letter">
						<span class="zi">牌照号：</span>
					</td>
					<td id="LICENSE_NO"><%=CommonUtils.checkNullEx(tawep.getLicenseNo())%></td>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">发动机号：</span>
					</td>
					<td align="left" id="ENGINE_NO"><%=CommonUtils.checkNullEx(tawep.getEngineNo())%></td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						品牌：
					</td>
					<td id="BRAND_NAME">
						<%=CommonUtils.checkNullEx(tawep.getBrandName())%>
					</td>
					<td class="table_edit_3Col_label_4Letter">
						车系：
					</td>
					<td id="SERIES_NAME"><%=CommonUtils.checkNullEx(tawep.getSeriesName())%>
					</td>
					<td class="table_edit_3Col_label_5Letter">
						车型：
					</td>
					<td id="MODEL_NAME"><%=CommonUtils.checkNullEx(tawep.getModelName())%></td>
				</tr>
				
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">变速箱号：</span>
					</td>
					<td id="GEARBOX_NO"><%=CommonUtils.checkNullEx(tawep.getGearboxNo())%></td>
					<td class="table_edit_3Col_label_4Letter">
						<span class="zi">后桥号：</span>
					</td>
					<td id="REARAXLE_NO"><%=CommonUtils.checkNullEx(tawep.getRearaxleNo())%></td>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">分动器号：</span>
					</td>
					<td id="TRANSFER_NO"><%=CommonUtils.checkNullEx(tawep.getTransferNo())%></td>
				</tr>
				</table>
			
			<table border="0" align="center" cellpadding="0" cellspacing="1"
				class="table_edit">
				<th colspan="4">
					<img class="nav" src="../../../img/subNav.gif" />
					申请内容
				</th>
				<tr>
					<td class="table_edit_2Col_label_4Letter">
						<div align="left">
							索赔类型：
						</div>
					</td>
					<td class="tbwhite">
						<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",false,"short_sel","","true",'');
	       </script>
					</td>
					<td id="blank1"></td>
					<td class="tbwhite" id="blank2">
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_4Letter">
						<div align="left">
							故障代码：
						</div>
					</td>
					<td class="tbwhite" id="myTrouble">
						<script type="text/javascript">
              var seriesList=document.getElementById("list04").value;
    	      var str="";
    	      str += "<select id='TROUBLE_CODE' name='TROUBLE_CODE'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
					</td>
					<td class="table_edit_2Col_label_4Letter">
						质损区域：
					<td class="tbwhite">
						<script type="text/javascript">
              var seriesList=document.getElementById("list02").value;
    	      var str="";
    	      str += "<select id='DAMAGE_AREA' name='DAMAGE_AREA'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_4Letter">
						<div align="left">
							质损类型：
						</div>
					</td>
					<td class="tbwhite">
						<script type="text/javascript">
              var seriesList=document.getElementById("list03").value;
    	      var str="";
    	      str += "<select id='DAMAGE_TYPE' name='DAMAGE_TYPE'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
					</td>
					<td class="table_edit_2Col_label_4Letter">
						<div align="left">
							质损程度：
						</div>
					</td>
					<td class="tbwhite">
						<script type="text/javascript">
              var seriesList=document.getElementById("list01").value;
    	      var str="";
    	      str += "<select id='DAMAGE_DEGREE' name='DAMAGE_DEGREE'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="left">
							故障描述：
						</div>
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='TROUBLE_DESC' id='TROUBLE_DESC'
							datatype="0,is_textarea,100" rows='2' cols='70'><%=tawep.getTroubleDesc() == null ? "" : tawep
					.getTroubleDesc()%></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="left">
							故障原因：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON' datatype="0,is_textarea,100"
							id='TROUBLE_REASON' rows='2' cols='70'><%=tawep.getTroubleReason() == null ? "" : tawep
					.getTroubleReason()%></textarea>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="left">
							维修措施：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='REPAIR_METHOD' datatype="0,is_textarea,100"
							id='REPAIR_METHOD' rows='2' cols='70'><%=tawep.getRepairMethod() == null ? "" : tawep
					.getRepairMethod()%></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="left">
							申请备注：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea datatype="0,is_textarea,100" name='APP_REMARK'
							id='APP_REMARK' rows='2' cols='70'><%=CommonUtils.checkNull(tawep.getRemark()) %></textarea>
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
				<th colspan="7" align="left">
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
						<div align="center">
							作业代码
						</div>
					</td>
					<td>
						工时名称
					</td>
					<td>
						工时定额
					</td>
					<td>
						<div align="center">
							工时单价(元)
						</div>
					</td>
					<td>
						工时金额(元)
					</td>
					<td>
						<input type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('itemTable');"/>
					</td>
				</tr>
				<tbody id="itemTable">
					<%
						for (int i = 0; i < itemLs.size(); i++) {
					%>
					<tr class="table_list_row1">
						<%
							if (i == 0) {
						%>
						<script type="text/javascript">
						//初始化为隐藏于timeId赋值，附加工时可根据主工时添加
	     				document.getElementById('timeId').value='<%=itemLs.get(i).getWrLabourcode()%>';
	     				</script>
						<td>
							<input type="hidden" name="LABOUR_ID"
								value="<%=itemLs.get(i).getLabourId()%>" />
							<input name="ITEM" disabled="true" checked="true" type="checkbox" />
						</td>
						<%
							} else {
						%>
						<td>
							<input type="hidden" name="LABOUR_ID"
								value="<%=itemLs.get(i).getLabourId()%>" />
							<input name="ITEM" disabled="true" type="checkbox" />
						</td>
						<%
							}
						%>
						<td>
							<span class="tbwhite"><a href="#" onclick="selectTime();"></a>
							</span>
							<input type="text" class="short_txt" name="WR_LABOURCODE"
								value="<%=CommonUtils
								.checkNull(itemLs.get(i).getWrLabourcode())%>"
								size="10" readonly datatype="0,is_null" id="WR_LABOURCODES<%=i %>"/>
							<a href="#" onclick="selectTime(this);delItemIds(this,'update');">选择</a>
						</td>
						<td>
							<span class="tbwhite"><input type="text"
									name="WR_LABOURNAME"
									value="<%=CommonUtils
								.checkNull(itemLs.get(i).getWrLabourname())%>"
									size="10" readonly />
							</span>
						</td>
						<td>
							<input type="text" name="LABOUR_HOURS" class="short_txt"
								value="<%=CommonUtils.checkNull(itemLs.get(i)
										.getLabourHours())%>"
								size="8" maxlength="11" id="LABOUR_HOURS" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_PRICE" class="short_txt"
								value="<%=CommonUtils.checkNull(itemLs.get(i)
										.getLabourPrice())%>"
								size="8" maxlength="11" id="LABOUR_PRICE" readonly />
						</td>
						<td>
							<input type="text" name="LABOUR_AMOUNT" id="LABOUR_AMOUNTS<%=i %>" class="short_txt"
								value="<%=CommonUtils.checkNull(itemLs.get(i).getLabourAmount())%>"
								size="8" maxlength="9" datatype="0,is_double" decimal="2" readonly />
						</td>
						<td>
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="javascript:delItem(this,'item');delItemIds(this,'del');" />
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>



			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="10" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					索赔配件
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						<div align="center">
							标志
						</div>
					</td>
					<td>
						<div align="center">
							换上件代码
						</div>
					</td>
					<td>
						<div align="center">
							换下件代码
						</div>
					</td>
					<td>
						换上件名称
					</td>
					<td>
						换上件数量(个)
					</td>
					<td>
						<div align="center">
							单价(元)
						</div>
					</td>
					<td>
						金额(元)
					</td>
					<td>
						<div align="center">
							生产商代码
						</div>
					</td>
					<td>
						故障描述
					</td>
					<td>
						<input type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('partTable');"/>
					</td>
				</tr>
				<tbody id="partTable">
					<%
						for (int i = 0; i < partLs.size(); i++) {
					%>
					<tr class="table_list_row1">
						<%
							if (i == 0) {
						%>
						<td>
							<input type="hidden" name="PART_ID"
								value="<%=partLs.get(i).getPartId()%>" />
							<input name="PART" type="checkbox" disabled="true" checked="true"
								onClick="javascript:checkCheckBox(this);" />
						</td>
						<%
							} else {
						%>
						<td>
							<input type="hidden" name="PART_ID"
								value="<%=partLs.get(i).getPartId()%>" />
							<input name="PART" type="checkbox" disabled="true"
								onClick="javascript:checkCheckBox(this);" />
						</td>
						<%
							}
						%>
						<td>
							<input type="text" name="PART_CODE" class="short_txt"
								value="<%=CommonUtils.checkNull(partLs.get(i).getPartCode())%>"
								size="10" id="PART_CODE" readonly />
							<span class="tbwhite"><a href="#"
								onClick="javascript:selectPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<input type="text" name="DOWN_PART_CODE" class="short_txt"
								value="<%=CommonUtils
								.checkNull(partLs.get(i).getDownPartCode())%>"
								size="10" readonly/>
							<span class="tbwhite"><input type="hidden"
									name="DOWN_PART_NAME" readonly
									value="<%=CommonUtils
								.checkNull(partLs.get(i).getDownPartName())%>"
									size="10" maxlength="13" readonly />
								<input type="hidden" name="s" value="1" size="10" maxlength="13"
									readonly /><a href="#"
								onClick="javascript:selectDownPartCode(this)">选择</a>
							</span>
						</td>
						<td>
							<span class="tbwhite"><input type="text" name="PART_NAME" class="short_txt"
									readonly
									value="<%=CommonUtils.checkNull(partLs.get(i).getPartName())%>"
									id="PART_NAME" name="PART_SN3" size="10" />
							</span>
						</td>
						<td>
							<input type="text"  class="short_txt" datatype="0,is_digit"  size="10" blurback = "true"
								name="QUANTITY" id="QUANTITYS<%=i%>"
								value="<%=(partLs.get(i).getQuantity().intValue())%>"
								maxlength="20" />
						</td>
						<td>
							<input type="text" class="short_txt" name="PRICE"
								value="<%=CommonUtils.checkNull(partLs.get(i).getPrice())%>"
								size="10" maxlength="11" id="PRICE" readOnly = "true" />
						</td>
						<td>
							<input type="text" class="short_txt" name="AMOUNT" id="AMOUNT" size="10"
								value="<%=CommonUtils.checkNull(partLs.get(i)
										.getAmount())%>"
								maxlength="9"  readonly />
						</td>
						<td>
							<input type="text" class="short_txt" name="PRODUCER_CODE" id="PRODUCER_CODE"
								value="<%=CommonUtils
								.checkNull(partLs.get(i).getProducerCode())%>" readonly/>
							<input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME"
								value="<%=CommonUtils
								.checkNull(partLs.get(i).getProducerName())%>" />
						</td>
						<td>
							<input type="text" class="short_txt" name="REMARK" id="REMARK"
								value="<%=CommonUtils.checkNull(partLs.get(i)
										.getRemark())%>"
								size="10" maxlength="13" />
						</td>
						<td>
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="javascript:delPartItem(this,'part');delPartIds(this,'del');" />
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>

			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_edit">
				<th colspan="8">
					<img class="nav" src="../../../img/subNav.gif" />
					其他项目
				</th>
				<tr align="center">
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
										class="short_txt" datatype="0,is_digit_letter_cn,100" />
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
								<span class="tbwhite"><input type="text"
										name="ITEM_REMARK" id="ITEM_REMARK"
										datatype="1,is_digit_letter_cn,100" class="middle_txt"
										value="<%=CommonUtils.checkNull(otherLs.get(i).getRemark())%>" />
								</span>
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
			<table id='feeTableId' class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<%
					if (feeTypeList != null) {
						for (int i = 0; i < feeTypeList.size(); i++) {
							HashMap temp = (HashMap) feeTypeList.get(i);
				%>
				<th><%=temp.get("CODE_DESC")%></th>
				<%
					}
					}
				%>
				<tr class="table_list_row1">
					<%
						if (feeTypeList != null) {
							for (int i = 0; i < feeTypeList.size(); i++) {
								HashMap temp = (HashMap) feeTypeList.get(i);
					%>

					<td>
						<input name="<%=temp.get("CODE_DESC")%>" type="hidden" value='' />
						<input name="<%=temp.get("CODE_ID")%>"
							id="<%=temp.get("CODE_ID")%>" type="text"
							value='<%=hm.get((Object) temp.get("CODE_ID")) == null ? ""
									: hm.get((Object) temp.get("CODE_ID"))%>'
							readonly="true" class="short_txt" />
					</td>
					<%
						}
						}
					%>
				</tr>
			</table>
			<table id='activityTableId' class="table_edit">
				<tr>
					<td>
						配件费用(元)：
					</td>
					<td>
						<%=CommonUtils.checkNull(tap.getPartFee())%>
					</td>
					<td>
						工时费用(元)：
					</td>
					<td>
						<%=CommonUtils.checkNull(tap.getWorktimeFee())%>
					</td>
				</tr>
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
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
    }
  
    //选择索赔类型，隐藏显示表格
 function getTypeChangeStyle(obj) {
    	if(obj=='<%=Constant.CLA_TYPE_01%>') {//一般索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_02%>') {//免费保养
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('feeTableId').style.display='';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_03%>') {//追加费用
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_04%>') {//重复修理索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_05%>') {//零件索赔更换
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_06%>') {//服务活动
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='';
    		document.getElementById("activity").style.display='';
    	}else if(obj=='<%=Constant.CLA_TYPE_07%>') {//PDI索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_08%>') {//保外索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else {
    	document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('feeTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}
    }
    //加载工时，得到第一项即主工时的值
    function chooseItem1() {
        var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode1,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode1(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
    	//为故障代码下拉框赋值
    	assignSelect("TROUBLE_CODE","<%=tawep.getTroubleCode()%>");
    }
    
    //为下拉框赋值
	assignSelect("CLAIM_TYPE","<%=tawep.getClaimType()%>");
	assignSelect("DAMAGE_AREA","<%=tawep.getDamageArea()%>");
	assignSelect("DAMAGE_TYPE","<%=tawep.getDamageType()%>");
	assignSelect("DAMAGE_DEGREE","<%=tawep.getDamageDegree()%>");
	
	chooseItem1();
	var options =  document.getElementById("CLAIM_TYPE").options;
   	var index = options.selectedIndex;
   	var myvalue = options[index].value;
	getTypeChangeStyle(myvalue);
	//这里给下拉框动态赋一个ONCLICK事件
	var obj = document.getElementById("CLAIM_TYPE");
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	
   	function blurBack(obj)
   	{
   	   countQuantity(document.getElementById(obj));
   	}
</script>
	</BODY>
</html>
