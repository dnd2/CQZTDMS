<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
	String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/storageUtil/storageUtil.js"></script>
<title>接车 入库</title>
<style type="text/css">
table.table_edit {
	
}

table.table_edit td {
	height: 25px;
}

table.table_edit td.table_query_2Col_input {
	padding-left: 5px;
}
</style>
<script type="text/javascript">
	var t, isup = false, ischange = false;var isAuto = "ZD";
	
	function doKeyDown() {

		clearTimeout(t);
	
		isup = true;
		t = setTimeout("scanInit()", 1200);
	}
	
	function doInputChange() {
		ischange = true;
	}
	function scanInit() 
	{   
		document.getElementById("VIN").value = document.getElementById("TOTAL_ORDER_ID").value;//车型ID（隐藏域）
		var vin = document.getElementById("VIN").value.replace(/(^\s*)/, "");
		if(vin != "") {
			var autoValue = document.getElementById("autoValue").value;
			var inStoreType = document.getElementById("inStoreType").value;
			if(autoValue != "ZD" && inStoreType == "1") {
				MyAlert("试制试验区只能自动入库!");
				return;
			}
			var inWay = "";
			var isAuto=document.getElementById("autoValue").value;//获取入库方式
			if(isAuto=="SD"){
				inWay = "SD";
			}else{
				inWay = "ZD";
			}
			var arrAid= document.getElementById("userAreaId").value;
			var yieldly = arrAid.split(",")[0];
		    var url = '<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/readyRecieveVehichle.json';
		    makeCall(url,addReceivingBack,{VIN:vin,YIELDLY:yieldly,IN_STORE_TYPE:inStoreType,inWay:inWay});
		} else {
			document.getElementById("TOTAL_ORDER_ID").value = "";
		}
	}

	// 获取入库车辆条码信息（根据开始位置，结束位置截取区间的值）
	function getProductInfo(scanString,start, end) {
		if(scanString != null) {
			return scanString.substring(start,end).trim();
		} else {
			return false;
		}
	}
	// 获取入库车辆条码信息（根据开始位置，截取某个长度的值）
	function getProductInfoBylen(scanString,start,len) {//根据长度添加
		if(scanString != null) {
			return scanString.substr(start,len).trim();
		} else {
			return false;
		}
	}
	// 获取入库车辆条码信息（获取字符串某一行）
	function getProductInfoRow(scanString,rowNum) {
		if(scanString != null) {
			var val=scanString.split("\n");
			return val[rowNum-1].trim();
		} else {
			return false;
		}
	}
	function addReceivingBack(json){
		var isAuto=document.getElementById("autoValue").value;//获取入库方式
		//*************************************************获取页面属性Start****************************************************//
		var YIELDLY_NAME_SHOW=document.getElementById("YIELDLY_NAME_SHOW");//产地NAME（显示）
		var SERIES_NAME_SHOW=document.getElementById("SERIES_NAME_SHOW");//车系（显示）
		var MODEL_CODE_SHOW=document.getElementById("MODEL_CODE_SHOW");//车型（显示）
		var MODEL_NAME_SHOW=document.getElementById("MODEL_NAME_SHOW");//车型名称（显示）
		var MATERIAL_NAME_SHOW=document.getElementById("MATERIAL_NAME_SHOW");//物料NAME（显示）		
		var MATERIAL_CODE_SHOW = document.getElementById("MATERIAL_CODE_SHOW");//物料NAME（显示）	
		var SIT_CODE_SHOW=document.getElementById("SIT_CODE_SHOW");//库位码（显示）
		var AREA_NAME_SHOW=document.getElementById("AREA_NAME");//库区（显示）
		var ROAD_NAME_SHOW=document.getElementById("ROAD_NAME");//库道（显示）
		var SIT_NAME_SHOW=document.getElementById("SIT_NAME");//库位（显示）
		
		var ENGINE_NO=document.getElementById("ENGINE_NO");//发动机号
		var HGZHENG_CODE=document.getElementById("HGZHENG_CODE");//库位（显示）
		var ORG_STORAGE_CODE=document.getElementById("ORG_STORAGE_CODE");//库位（显示）
		//*************************************************获取页面属性END******************************************************//
		if(json.returnValue==1)
		{
			//*******************************************隐藏域Start**************************************************//
			YIELDLY_NAME_SHOW.innerHTML=json.resultMap.AREA_NAME;
			SERIES_NAME_SHOW.innerHTML=json.resultMap.SERIES_NAME;//车系（显示）
			MODEL_CODE_SHOW.innerHTML=json.resultMap.MODEL_CODE;
		    MODEL_NAME_SHOW.innerHTML=json.resultMap.MODEL_NAME;
			PACKAGE_CODE_SHOW.innerHTML=json.resultMap.PACKAGE_CODE;
			PACKAGE_NAME_SHOW.innerHTML=json.resultMap.PACKAGE_NAME;
			MATERIAL_NAME_SHOW.innerHTML=json.resultMap.MATERIAL_NAME;	
			MATERIAL_CODE_SHOW.innerHTML=json.resultMap.MATERIAL_CODE;
			SERIES_NAME_SHOW.innerHTML =  json.resultMap.SERIES_NAME;
			SIT_CODE_SHOW.innerHTML=json.SIT_CODE;

			ENGINE_NO.innerHTML=json.resultMap.ENGINE_NO;
			HGZHENG_CODE.innerHTML=json.resultMap.HEGEZHENG_CODE;
			ORG_STORAGE_CODE.innerHTML=json.resultMap.ORG_STORAGE_DATE;
			
			if(isAuto=="SD"){
				document.getElementById("incoming").disabled = "";
				//SIT_CODE.value
			}else{
				var inStoreType = document.getElementById("inStoreType").value;
			    if(inStoreType == "1") {
			    	document.getElementById("sySitCode").value = json.SIT_CODE;
			    	document.getElementById("sySitName").value = json.SIT_ID;
			    	makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/getHisVehicle.json", function(json){
						if (json.returnValue == 1) 
						{//退回车不能使用试制试验区
							MyConfirm("请注意该车为退回车,退回原因:<br/>"+json.retreatdes+"<br/>是否进入试制试验区?",syInStore,"",formReset);
							//MyAlert("退回车不能进入试制试验区!");
							//formReset();
						}else{//不是退回车
							syInStore();
						}
					},'fm');
			    } else {
			    	AREA_NAME_SHOW.value = json.AREA_NAME;
					ROAD_NAME_SHOW.value = json.ROAD_NAME;
					SIT_NAME_SHOW.value =  json.SIT_NAME;
					document.getElementById("incoming").disabled = "disabled";
					//设置库区，库位值
					doInitCombo(json.AREA_ID,json.AREA_NAME,json.ROAD_ID,json.ROAD_NAME,json.SIT_ID,json.SIT_NAME);
					makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/getHisVehicle.json", function(json){
						if (json.returnValue == 1) 
						{//退回车需确认后才能入库
							addReset(true,json.retreatdes);
						}else{//不是退回车不需确认
							addReset(false,'');
						}
					},'fm');
			    }
			}
			//*******************************************显示域END***************************************************//
		
			
		}
		else if(json.returnValue==2){
			MyAlertForFun("处理失败，该VIN对应的车辆已经入过库了", formReset); 
		}
		else if(json.returnValue==3){
			MyAlertForFun("处理失败，无法找到该VIN号",formReset); 
		}
		else if(json.returnValue==4){
			MyAlertForFun("无法找到该VIN号",formReset); 
		}
		
		/* else if(json.returnValue==50){
			MyAlert("此车入库在一个新开辟的道：" + json.ROAD_NAME); 
		} */
		else if(json.returnValue==99){
			MyAlertForFun("系统错误，请与管理员联系",formReset); 
		} else {
			if(json.Exception.message != null && json.Exception.message != "") {
				MyAlertForFun(json.Exception.message,formReset);
			}
		}
		
	}
	function addReset(isReset,retreatdes){
		var isAuto=document.getElementById("autoValue").value;//获取入库方式
			if(isReset){
				var infoStorage = "该车为退回车！退回原因为：{"+retreatdes+"},是否重新入库？";
				if(confirm(infoStorage)){
					if(isAuto=="SD"){
						saveReceiving();
					}else{
						addInfo();
					}
				}else{
					formReset();
				}
			}else{
				if(isAuto=="SD"){
					saveReceiving();
				}else{
					addInfo();
				}
			}
	
	}
	function focusNoInput() { 
		document.getElementById('TOTAL_ORDER_ID').focus();
	}
	function getSelectValue(objSelect, objItemValue) {   

		if(!objSelect) {MyAlert('下拉组件无效!'); return;}
		if(!objItemValue && objItemValue != '0') {MyAlert('输入值无效');}
		
	    for (var i = 0; i < objSelect.options.length; i++) {        
	        if (objSelect.options[i].value == objItemValue) {        
	        	objSelect.options[i].selected = true;       
	        	return objSelect.options[i].innerHTML;
	        }        
	    }        
	} 
	
	function addInfo()
	{ 	
		var aname=document.getElementById("AREA_NAME");
		var rname=document.getElementById("ROAD_NAME");
		var sname=document.getElementById("SIT_NAME");
		
		if(aname.value == "") {
			MyAlertForFun("库区不能为空", formReset); isup = false; return ;
		}
		
		if(rname.value == "") {
			MyAlertForFun("库道不能为空", formReset); isup = false; return ;
		}
		
		if(sname.value == "") {
			MyAlertForFun("库位不能为空", formReset); isup = false; return ;
		}

		
		if(getSelectValue(sname,sname.value) == '01') 
		{
			var infoStorage = "是否直接入库?<br/>入库位置：" + getSelectValue(aname,aname.value) + "&nbsp;"+getSelectValue(rname,rname.value)+"号库道&nbsp;"+getSelectValue(sname,sname.value)+"号库位";
			MyConfirm(infoStorage,saveReceiving,"",canler); 
		} 
		else 
		{
			saveReceiving();
		}
	}
	//清除库道位
	function canler(){
		document.getElementById("AREA_NAME").value="";
		document.getElementById("ROAD_NAME").value="";
		document.getElementById("SIT_NAME").value="";
		document.getElementById("sySitCode").value="";
		document.getElementById("sySitName").value="";
	}
	function saveRe(){
		document.getElementById("PROCESS_TYPE").value="手动入库";
		var isAuto=document.getElementById("autoValue").value;//获取入库方式
		if(isAuto=="SD"){//判断是否是手动入库(调用是否是退回车的方法)
			var SIT_CODE=document.getElementById("SIT_CODE");//库位码（隐藏域）
			var aobj = document.getElementById("AREA_NAME"); //selectid
			var aindex = aobj.selectedIndex; // 选中索引
			var atext = aobj.options[aindex].text; // 选中文本	
			var robj = document.getElementById("ROAD_NAME"); //selectid
			var rindex = robj.selectedIndex; // 选中索引
			var rtext = robj.options[rindex].text; // 选中文本	
			var sobj = document.getElementById("SIT_NAME"); //selectid
			var sindex = sobj.selectedIndex; // 选中索引
			var stext = sobj.options[sindex].text; // 选中文本	
			var a=atext.length<2?"0"+atext:atext;	
			var b=rtext.length<2?"0"+rtext:rtext;	
			var c=stext.length<2?"0"+stext:stext;	
				SIT_CODE.value=a+"-"+b+"-"+c
			makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/getHisVehicle.json", function(json){
				if (json.returnValue == 1) 
				{//退回车需确认后才能入库
					addReset(true,json.retreatdes);
				}else{//不是退回车不需确认
					addReset(false,'');
				}
			},'fm'); 
		}else{
			saveReceiving();
		}

	}
	function saveReceiving()
	{ 
		var SIT_CODE=document.getElementById("SIT_CODE");//库位码（隐藏域）
		var aname=document.getElementById("AREA_NAME");
		var rname=document.getElementById("ROAD_NAME");
		var sname=document.getElementById("SIT_NAME");
		if(aname.value == "") {
			MyAlert("库区不能为空"); isup = false; return ;
		}
		if(rname.value == "") {
			MyAlert("库道不能为空"); isup = false; return ;
		}
		if(sname.value == "") {
			MyAlert("库位不能为空"); isup = false; return ;
		}
		var aobj = document.getElementById("AREA_NAME"); //selectid
		var aindex = aobj.selectedIndex; // 选中索引
		var atext = aobj.options[aindex].text; // 选中文本	
		var robj = document.getElementById("ROAD_NAME"); //selectid
		var rindex = robj.selectedIndex; // 选中索引
		var rtext = robj.options[rindex].text; // 选中文本	
		var sobj = document.getElementById("SIT_NAME"); //selectid
		var sindex = sobj.selectedIndex; // 选中索引
		var stext = sobj.options[sindex].text; // 选中文本	
		var a=atext.length<2?"0"+atext:atext;	
		var b=rtext.length<2?"0"+rtext:rtext;	
		var c=stext.length<2?"0"+stext:stext;	
			SIT_CODE.value=a+"-"+b+"-"+c
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/saveVehStorage.json", function(json){
			
			if (json.returnValue == 1) {
				document.getElementById("TOTAL_ORDER_ID").value = '';
				focusNoInput();
			} 
			else if (json.returnValue == 15) 
			{
				MyAlertForFun("操作失败！该库位已有车辆",formReset);
			} 
			else if (json.returnValue == 2) 
			{
				MyAlertForFun("操作失败!该VIN号不存在",formReset);
			}else if(json.returnValue == 5){
				MyAlertForFun("没有可用的库区和库道",formReset);
			}
		
		},'fm'); 
	}
	
	//试验车辆入库
	function syInStore(){
			makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/saveVehStorage.json", function(json){
			if (json.returnValue == 1) {
				document.getElementById("TOTAL_ORDER_ID").value = '';
				focusNoInput();
			} 
			else if (json.returnValue == 15) 
			{
				MyAlertForFun("操作失败！该库位已有车辆",formReset);
			} 
			else if (json.returnValue == 2) 
			{
				MyAlertForFun("操作失败!该VIN号不存在",formReset);
			}else if(json.returnValue == 5){
				MyAlertForFun("没有可用的库区和库道",formReset);
			}
		
		},'fm'); 
	}
	
	//打印库位码
	function printcode(vin,sitcode){	
		try {
			tecPrint.printCode(vin,sitcode,'0060','0038','0060','0165','0040','0120','0040','0170'); 
		}catch(e) {
			MyAlert('打印异常,请检查条码打印机');
		}
	}
	function getSitCode(){
		var aobj = document.getElementById("AREA_NAME"); //selectid
		var aindex = aobj.selectedIndex; // 选中索引
		var atext = aobj.options[aindex].text; // 选中文本	
		var robj = document.getElementById("ROAD_NAME"); //selectid
		var rindex = robj.selectedIndex; // 选中索引
		var rtext = robj.options[rindex].text; // 选中文本	
		var sobj = document.getElementById("SIT_NAME"); //selectid
		var sindex = sobj.selectedIndex; // 选中索引
		var stext = sobj.options[sindex].text; // 选中文本	
		var perId=document.getElementById("perCode").value;//接车员
		var a=atext.length<2?"0"+atext:atext;	
		var b=rtext.length<2?"0"+rtext:rtext;	
		var c=stext.length<2?"0"+stext:stext;	
		var sitCode=a+"-"+b+"-"+c;
			sitCode+="*"+perId;
		document.getElementById("prSitCode").value=sitCode;//打印条码按钮用
		return sitCode;
	}
	var vinCode;

	function formReset()
	{

		document.getElementById("TOTAL_ORDER_ID").value = "";
		document.getElementById("VIN").value = "";
		document.getElementById("SERIES_NAME_SHOW").innerHTML = "";
		document.getElementById("YIELDLY_NAME_SHOW").innerHTML = "";//产地NAME（显示）
		document.getElementById("MODEL_CODE_SHOW").innerHTML = "";//车型（显示）
		document.getElementById("MODEL_NAME_SHOW").innerHTML = "";//车型名称（显示）
		document.getElementById("PACKAGE_CODE_SHOW").innerHTML = "";//配置代码（显示）
		document.getElementById("PACKAGE_NAME_SHOW").innerHTML = "";//配置名称（显示）
		document.getElementById("MATERIAL_NAME_SHOW").innerHTML = "";//物料NAME（显示）
		document.getElementById("SIT_CODE_SHOW").innerHTML = "";//库位码（显示）
		document.getElementById("MATERIAL_CODE_SHOW").innerHTML = "";
	 	document.getElementById("REGION_NAME").value = "";//省份

		document.getElementById("SIT_CODE").value = "";
		canler();
		isup = false, ischange = false;
		focusNoInput();
	}
	
	function doInit() {
		//日期控件初始化
		document.body.style.height = parent.document.body.clientHeight + "px";
		
	}
	function changeAutoType(){
		var t=document.getElementById("autoValue");
		var t2=document.getElementById("autoType");
		if(t.value=="ZD"){
			t.value="SD";
			t2.value="手动";
		}else{
			t.value="ZD";
			t2.value="自动";
			
		}
	}
	function disP(vin,sitcode){
		printcode(vin,sitcode);
		document.getElementById("printSitCode").disabled = "disabled";
	}
	function openCOM(){
		//if(closeCOM()=="00" || closeCOM()==00){
			closeCOM();
		//}
		return TUReader.Open(6);//打开6,代表COM接口
	}
	function closeCOM(){
			return TUReader.Close(); //关闭
	}
	
	function openImportVeh (){
		window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/openImportInit.do";
	}
</script>
<!--<OBJECT style="display:none;" ID="tecPrint"  CLASSID="CLSID:82139C0A-F89F-4EB4-9DDF-38798CBF53B6"-->
<!--CODEBASE="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/tecPrinter.CAB#version=1,0,0,0">-->
<!--</OBJECT>-->
<OBJECT style="display:none;" id=TUReader codebase="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/UReaderProj.CAB#version=1,0,0,2" classid="clsid:220AC733-63C2-4B1E-AC07-EF0B5147BE17">
</OBJECT>
</head>
<body onload="focusNoInput()" onkeydown="focusNoInput()">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> 接车入库
		</div>
		<form method="post" name="fm" id="fm">
		    <input type="hidden" name="sySitCode" id="sySitCode" />
		    <input type="hidden" name="sySitName" id="sySitName" />
			<table class="table_edit" id="table_edit">
				<col width="100" />
				<col />
				<col width="100" />
				<col />
				<tr>
					<td class="right">入库类型：</td>
					<td align="left"><select name="inStoreType"><option value="0">普通区</option><option value="1">试制试验区</option></select></td>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="6" class="table_query_4Col_input" style="text-align: center"><span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;扫入库条码</span></td>
				</tr>
				<tr>
					<td  class="right">入库方式：</td>
					<td  align="left"  colspan="5">
						<input type="hidden" id="autoValue" value="ZD" />
						<input type="button" id="import" class="normal_btn" <c:if test="${userZhen !=201406202059 }">disabled title="导入功能已经禁用,请使用扫描入库"</c:if> value="导入" onclick="openImportVeh();" />  	
						<input type="button" class="normal_btn" id="autoType" onclick="changeAutoType()" value="自动"  title="点击切换入库方式{手动或自动}"/> 
						&nbsp;&nbsp;&nbsp;&nbsp;
					<textarea rows="5" cols="80" id="TOTAL_ORDER_ID" name="TOTAL_ORDER_ID" onkeydown="doKeyDown()" onchange="doInputChange()"></textarea>
					</td>
				</tr>
				
					<tr class="csstr" align="center">
					<td class="right">VIN：</td>
					<td class="table_query_2Col_input"><input type="text" maxlength="20"  id="VIN"
						name="VIN" class="middle_txt" value="" datatype="0,is_null,17" />
					</td>
					<td class="right">库区：</td>
					<td class="table_query_2Col_input"><select name="AREA_NAME"
						id="AREA_NAME" class="u-select" onchange="roadCheckBox('other');" >
							<option value="">-请选择-</option>
							<c:if test="${list_An!=null}">
								<c:forEach items="${list_An}" var="list">
									<option value="${list.AREA_ID }">${list.AREA_NAME }</option>
								</c:forEach>
							</c:if>
					</select></td>
					<td>接车员</td>
				</tr>
				
				<tr class="csstr" align="center">
				   <td class="right">库道：</td>
					<td class="table_query_2Col_input"><select name="ROAD_NAME"
						id="ROAD_NAME" class="u-select" onchange="sitCheckBox('other');">
							<option value="">-请选择-</option>
					</select></td>
					<td class="right">库位：</td>
					<td class="table_query_2Col_input"><select name="SIT_NAME"
						id="SIT_NAME" class="u-select">
							<option value="">-请选择-</option>
					</select></td>
					<td rowspan="6">
					<input type="hidden" class="middle_txt" id="perCode"/>
					<select name="PER_ID"
						id="PER_ID" class="selectlist"  size="12" style="width: 100px;font-size:20px">
							<!-- <option value="">-请选择-</option> -->
							<c:if test="${list_Pn!=null}">
								<c:forEach items="${list_Pn}" var="list" varStatus="ls">    
									<option  style="font-size: 20px" value="${list.PER_ID }"  <c:if test="${ls.first}">selected="selected"</c:if>>${list.PER_NAME }</option>
								</c:forEach>
							</c:if>
					</select></td>
				</tr>
				
				<tr class="csstr" align="center">
					<td class="right">车系：</td>
					<td align="left" class="table_query_2Col_input" nowrap="nowrap"><span id="SERIES_NAME_SHOW"></span>
					</td>
					<td class="right">物料代码：</td>
					<td align="left" class="table_query_2Col_input"><span id="MATERIAL_CODE_SHOW">
					</td>
				</tr>
				<tr class="csstr" align="center">
					<td class="right">产地：</td>
					<td align="left" class="table_query_2Col_input"><span id="YIELDLY_NAME_SHOW"></span>
						<input type="hidden" id="PROCESS_TYPE" name="PROCESS_TYPE" class="middle_txt" value="自动入库" /> 
						<input type="hidden" id="userAreaId" name="userAreaId" class="middle_txt" value="${userAreaId } "  /><!-- 用户产地 -->
						<input type="hidden" id="SERIES_ID" name="SERIES_ID" class="middle_txt" /> 
						<input type="hidden" id="MODEL_ID" name="MODEL_ID" class="middle_txt" value="" /> 
						<input type="hidden" id="PACKAGE_ID" name="PACKAGE_ID" class="middle_txt" /> 
						<input type="hidden" id="MATERIAL_ID" name="MATERIAL_ID" class="middle_txt" value="" /> 
						<input type="hidden" id="SIT_CODE" name="SIT_CODE" class="middle_txt" /> 
						<input type="hidden" id="GEARBOX_NO" name="GEARBOX_NO" class="middle_txt"/>
						<input type="hidden" id="REARAXLE_NO" name="REARAXLE_NO" class="middle_txt"/> 
						<input type="hidden" id="TRANSFER_NO" name="TRANSFER_NO" class="middle_txt"/>
						<input type="hidden" id="isLastPosition" name="isLastPosition" class="middle_txt" /> 
						<input type="hidden" id="ORG_ID" name="ORG_ID" class="middle_txt" /> 
						<!-- 判断用 --> 
						<input type="hidden" id="COLOR_NAME" name="COLOR_NAME" class="middle_txt" />
						<input type="hidden" id="MODEL_YEAR" name="MODEL_YEAR" class="middle_txt" value="" /> 
						<input type="hidden" id="PLAN_DETAIL_ID" name="PLAN_DETAIL_ID" class="middle_txt" />
						 
						<input type="hidden" id="erp_model" name="erp_model" class="middle_txt" />
						<input type="hidden" id="erp_package" name="erp_package" class="middle_txt" value="" /> 
						<input type="hidden" id="erp_name" name="erp_name" class="middle_txt" /> 
						<input type="hidden" id="remark" name="remark" class="middle_txt" /> 
					</td>
					<td class="right">物料名称：</td>
					<td align="left" class="table_query_2Col_input"><span id="MATERIAL_NAME_SHOW"></span>
					</td>
					
				</tr>
				<tr>
					<td class="right">车型：</td>
					<td class="table_query_2Col_input"><span id="MODEL_CODE_SHOW"></span>
					</td>
					<td class="right">车型名称：</td>
					<td class="table_query_2Col_input"><span id= "MODEL_NAME_SHOW"></span>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td class="right">配置代码：</td>
					<td class="table_query_2Col_input"><span id="PACKAGE_CODE_SHOW"></span>
					</td>
					<td class="right">配置名称：</td>
					<td class="table_query_2Col_input"><span id="PACKAGE_NAME_SHOW"></span>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
				   <td class="right">发动机号：</td>
					<td class="table_query_2Col_input"><span id="ENGINE_NO"></span>
					</td>
					 <td class="right">合格证号：</td>
					<td class="table_query_2Col_input"><span id="HGZHENG_CODE"></span>
					</td>
				</tr>
				<tr>
				<td class="right">入库日期：</td>
					<td class="table_query_2Col_input"><span id="ORG_STORAGE_CODE"></span> </td>
					<td class="right">库位码：</td>
					<td class="table_query_2Col_input"><span id="SIT_CODE_SHOW"></span> </td>
					</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td align="center" colspan="6">
						<input type="hidden" id="prVin" class="middle_txt" readonly="readonly"/>
						<input type="hidden" id="prSitCode" class="middle_txt" readonly="readonly"/>
						<input type="button" name="button1" id="incoming"  class="normal_btn" disabled="disabled"  onclick="saveRe()" value="确定入库" /> 
						<input type="button" value="重置" class="normal_btn" onclick="formReset();"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>