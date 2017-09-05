<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TcCodePO"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请创建</title>
<SCRIPT LANGUAGE="JavaScript">
	var myobj;
	var modelId='';
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
	function confirmAdd() {
		MyConfirm("是否添加？",confirmAdd0,[]);
	}
	function confirmAdd0() {
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
		fm.action='<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationInsert.do';
		fm.submit();
		}
	}
	function doInit()
	{
	   	loadcalendar();
	}
	//计算费用
	function countQuantity(obj) {
		myobj = obj.parentNode.parentNode
		var price = myobj.cells.item(5).childNodes[0].value;
		var quantity = obj.value;
		if (quantity!=null&&quantity!=""){
			myobj.cells.item(6).innerHTML = '<td><input type="text" class="short_txt" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" readonly="true"/></td>';
		}else {
			//MyAlert("请输入数量！");
		}
		
	}
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVinForward.do',800,500);
	}
	//获取子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME){
		document.getElementById("VIN").value = VIN;
		//document.getElementById("LICENSE_NO").innerHTML = LICENSE_NO;
		document.getElementById("BRAND_NAME").innerHTML = BRAND_NAME;
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("REARAXLE_NO").innerHTML = REARAXLE_NO;
		document.getElementById("GEARBOX_NO").innerHTML = GEARBOX_NO;
		modelId=MODEL_ID;
	}
	//工时选择
	function setTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue) {
		if(getRowNo(myobj)==0){
		//为隐藏域赋值工时的ID
		document.getElementById('timeId').value=labourCode;
		chooseItem(); //联动故障代码下拉框
		myobj.cells.item(0).innerHTML='<input name="isMain2" disabled="true"  type="checkbox" checked="true" onClick="javascript:checkCheckBox(this);"/>';
		}else {
		myobj.cells.item(0).innerHTML='<input name="isMain2" disabled="true"  type="checkbox"  onClick="javascript:checkCheckBox(this);"/>';
		}
		myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="short_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><a href="#"  onclick="selectTime(this);">选择</a></td>'
		myobj.cells.item(2).innerHTML='<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME"  value="'+cnDes+'" size="10" readonly/></span></td>';
		myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="short_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
		myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="short_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
		myobj.cells.item(5).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="short_txt" readonly  value="'+labourQuotiety*parameterValue+'" size="8" maxlength="9" readonly="true"/></td>';
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
		myobj.cells.item(1).innerHTML='<input type="text" class="short_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="short_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="hidden" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
	}
	//换下件选择
	function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode) {
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
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
			//MyAlert(document.getElementById('timeId').value);
			myobj = getRowObj(obj);
			var treeCode = 3;
			var timeId;
			if(getRowNo(myobj)==0){
				treeCode=3;
				timeId='';
				openTime(obj,treeCode,timeId);
			}else {
				treeCode=4;
				timeId = document.getElementById("timeId").value;
				OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
				//timeId=myobj.parentNode.children[0].cells.item(1).innerText;
			}
			
			//getTime(getRowObj(obj).cells.item(0).innerHTML);
			
			
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
			var options = myobj.cells.item(0).childNodes[0].childNodes[0].options;
			var index = options.selectedIndex;
			var text = options[index].title;
			myobj.cells.item(1).innerHTML='<td><div align="center"><input type="text" readonly name="ITEM_NAME" value="'+text+'" id="ITEM_NAME"/></div></td>';
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
		
		//function delItem(obj) {
		//	MyConfirm("删除主工时将会随之删除附加工时，确认删除吗？",delItems,[obj]);
		//}
		//删除行
		function delItem(obj){
		    var tr = this.getRowObj(obj);
		    if(getRowNo(tr)==0) {
		    	MyConfirm("删除主工时将会随之删除附加工时，确认删除吗？",delItems,[tr]);
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
		function delItems(tr){
		   if(tr != null){
		    clearAllNode(tr.parentNode);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
		
		
		//循环删除节点
		function clearAllNode(parentNode){
		    while (parentNode.firstChild) {
		      var oldNode = parentNode.removeChild(parentNode.firstChild);
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
	
</SCRIPT>
	</HEAD>
	<BODY onload="doInit()">

		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单维护
		</div>

		<form method="post" name="fm" id="fm">
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
					<td align="left">
						<%=request.getAttribute("dealerCode") %>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						经销商名称：
					</td>
					<td align="left">
						<%=request.getAttribute("dealerName") %>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						维修工单号-行号：
					</td>
					<td align="left">
						<input type='text' name='RO_NO' id='RO_NO' class="middle_txt"
							datatype="0,is_digit_letter,19" value='' />
						-
						<input type='text' name='LINE_NO' id='LINE_NO' class="mini_txt"
							datatype="0,is_digit" value='' />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td>
					<td nowrap="nowrap">
						<input type="text" name="RO_STARTDATE" id="RO_STARTDATE" class="short_txt"
							datatype="0,is_date,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"
							callFunction="showcalendar(event, 'RO_STARTDATE', false);" />
					</td>
					
				</tr>
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						工单结束时间：
					</td>
					<td>
						<input type="text" name="RO_ENDDATE" id="RO_ENDDATE" class="short_txt"
							datatype="0,is_date,10" group="RO_STARTDATE,RO_ENDDATE"
							hasbtn="true"
							callFunction="showcalendar(event, 'RO_ENDDATE', false);" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程数：
					</td>
					<td>
						<input type='text' name='IN_MILEAGE' id='IN_MILEAGE'
							class="middle_txt" datatype="0,is_double,10" />
					</td>
					</tr>
					<tr>
					<td class="table_edit_2Col_label_7Letter">
						<span class="zi">保修开始日期：</span>
					</td>
					<td>
						<input type="text" name="GUARANTEE_DATE" id="GUARANTEE_DATE" class="short_txt"
							datatype="0,is_date,10" hasbtn="true"
							callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						接 待 员：
					</td>
					<td>
						<input type='text' name='SERVE_ADVISOR' id='SERVE_ADVISOR'
							datatype="0,is_digit_letter_cn,10" class="middle_txt" />
					</td>
				</tr>
				<tr id="activity">
					<td class="table_edit_3Col_label_7Letter" >
						<span class="zi">活动编号：</span>
					</td>
					<td>
						<input type='text' name='CAMPAIGN_CODE' id='CAMPAIGN_CODE'
							class="middle_txt" />
					</td>
					<td class="table_edit_3Col_label_6Letter">
						是否固定费用：
					</td>
					<td>
						<input type='checkbox' name='RO_NO' id='1410068' readonly='true'
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
					<td class="table_edit_3Col_label_7Letter">
						VIN：
					</td>
					<td >
						<input type='text' name='VIN' id='VIN' onclick="showVIN()"
							style="cursor: pointer;" class="middle_txt" />
							<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">牌照号：</span>
					</td>
					<td >
					<input type="text" value="" name="LICENSE_NO" id="LICENSE_NO" class="short_txt" />
					<!--  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
					</td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">发动机号：</span>
					</td>
					<td align="left" id="ENGINE_NO">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_7Letter">
						品牌：
					</td>
					<td align="left" id="BRAND_NAME">
						
					</td>
					<td class="table_edit_3Col_label_6Letter">
						车系：
					</td>
					<td id="SERIES_NAME">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td class="table_edit_3Col_label_6Letter">
						车型：
					</td>
					<td id="MODEL_NAME">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">变速箱号：</span>
					</td>
					<td id="GEARBOX_NO"></td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">后桥号：</span>
					</td>
					<td id="REARAXLE_NO"></td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">分动器号：</span>
					</td>
					<td id="TRANSFER_NO"></td>
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
						<div align="right">
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
						<div align="right">
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
						<div align="right">
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
						<div align="right">
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
						<div align="right">
							故障描述：
						</div>
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='TROUBLE_DESC' datatype="0,is_textarea,100"
							id='TROUBLE_DESC' rows='2' cols='70'></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="right">
							故障原因：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON' id='TROUBLE_REASON'
							datatype="0,is_textarea,100" rows='2' cols='70'></textarea>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="right">
							维修措施：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='REPAIR_METHOD' datatype="0,is_textarea,100"
							id='REPAIR_METHOD' rows='2' cols='70'></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_1Col_label_4Letter">
						<div align="right">
							申请备注：
						</div>
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='APP_REMARK' id='APP_REMARK'
							datatype="0,is_textarea,100" rows='2' cols='70'></textarea>
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
							工时单价
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
						换上件数量
					</td>
					<td>
						<div align="center">
							单价
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
				</tbody>
			</table>

			<table id='otherTableId' border="0" align="center" cellpadding="0"
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
 			</table>

			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td colspan="6" align=center>
						<input type="button" onClick="confirmAdd();" class="normal_btn"
							style="" value="确定" />
						&nbsp;&nbsp;
						<input type="button" onClick="goBack();"
							class="normal_btn" style="" value="返回" />
					</td>
				</tr>
			</table>
			<script type="text/javascript">
    function chooseItem() {
        var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode,{ITEM_ID:itemId});
    }
    function changeTroubleCode(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
    }
    var obj = document.getElementById("CLAIM_TYPE");
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	//根据索赔类型变换样式
   function getTypeChangeStyle(obj) {
    	if(obj=='<%=Constant.CLA_TYPE_01%>') {//一般索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_02%>') {//免费保养
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_03%>') {//追加费用
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_04%>') {//重复修理索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_05%>') {//零件索赔更换
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_06%>') {//服务活动
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='';
    		document.getElementById("activity").style.display='';
    	}else if(obj=='<%=Constant.CLA_TYPE_07%>') {//PDI索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else if(obj=='<%=Constant.CLA_TYPE_08%>') {//保外索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}else {
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('activityTableId').style.display='none';
    		document.getElementById("activity").style.display='none';
    	}
    }
    var options =  document.getElementById("CLAIM_TYPE").options;
   	var index = options.selectedIndex;
   	var myvalue = options[index].value;
	getTypeChangeStyle(myvalue);
    //MyAlert(document.getElementById("CLAIM_TYPE").options.selectedIndex);
    </script>
			<!-- 资料显示区结束 -->

		</form>
	</body>
</html>
