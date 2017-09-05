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
	var t, isup = false, ischange = false;
	
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
		document.getElementById("REGION_NAME").focus();
		var scan = document.getElementById("TOTAL_ORDER_ID");
		var scanValue = scan.value;

		var userAreaId = document.getElementById("userAreaId").value;//用户产地
		if(userAreaId!=null && userAreaId!=" " && userAreaId!=""){
			var arrAid=userAreaId.split(",");
			//判断属于哪个产地（不同产地根据不同代码规则）
			if(arrAid.length!=1){
				MyAlertForFun('用户产地包括多个，无法识别入库条码',focusNoInput);
				document.getElementById('TOTAL_ORDER_ID').value="";
			}else{
				//入库条形码规则按照217长度截取(景德镇的代码规则)
				if(isup && ischange && scanValue != '')
				{
					// 车型,内部型号,发动机号,VIN号,装配状态,生产日期和下线日期,合格证号,流水号,备注,生产单号,站点,定制码,选装代码,颜色码;
					var erp_model,erp_package,fdjNo,vin,erp_name,productD,hegezheng_code,sdNumber,remark,productNo,orgId,dzCode,xzCode,coCode;	
					if(userAreaId==<%=Constant.areaIdJZD%>){//景德镇217规则--------------------------------------------------
					  if(scanValue.length>=197){
							erp_model=getProductInfo(scanValue, 0, 15);	// 车型
							erp_package=getProductInfo(scanValue, 15, 23);	// 内部型号
							fdjNo = getProductInfo(scanValue, 23, 63);//发动机号
							vin = getProductInfo(scanValue, 63, 83);	// VIN号
							erp_name=getProductInfo(scanValue, 83, 143);	// 装配状态
							productDate = getProductInfo(scanValue, 143, 153);	// 生产日期和下线日期
							hegezheng_code = getProductInfo(scanValue, 153, 173);//合格证号
							sdNumber = getProductInfo(scanValue, 173, 185);//流水号
							remark = getProductInfo(scanValue, 185, 197);//备注
							productNo = getProductInfo(scanValue, 197, scanValue.length);	// 生产单号
						}else{
							MyAlertForFun('入库条码信息错误',focusNoInput);
							document.getElementById('TOTAL_ORDER_ID').value="";
							return;
						}
					}else if(userAreaId==<%=Constant.areaIdJJ%>){//九江规则-----------------------------------------------
						if(scanValue.length>=199){
							erp_model=getProductInfo(scanValue, 0, 15);	// 车型
							erp_package=getProductInfo(scanValue, 15, 23);	// 内部型号
							fdjNo = getProductInfo(scanValue, 23, 63);//发动机号
							vin = getProductInfo(scanValue, 63, 83);	// VIN号
							erp_name=getProductInfo(scanValue, 83, 143);	// 装配状态
							productDate = getProductInfo(scanValue, 143, 153);	// 生产日期和下线日期
							hegezheng_code = getProductInfo(scanValue, 153, 173);//合格证号
							sdNumber = getProductInfo(scanValue, 173, 185);//流水号
							remark = getProductInfo(scanValue, 185, 197);//备注
							productNo = getProductInfo(getProductInfo(scanValue, 199, scanValue.length),0,getProductInfo(scanValue, 199, scanValue.length).length-1);	// 生产单号
							if(productNo==""){
								MyAlertForFun('该入库信息无生产批售单号，无法解析！',focusNoInput);
								document.getElementById('TOTAL_ORDER_ID').value="";
								return;
								}
							orgId=getProductInfo(scanValue, 197, 199);//站点
						}else{
							MyAlertForFun('入库条码信息错误',focusNoInput);
							document.getElementById('TOTAL_ORDER_ID').value="";
							return;
					   }
					}else if(userAreaId==<%=Constant.areaIdHF%>){//合肥规则------------------------------------------------
						if(scanValue.split("\n").length>13){
							var rowL=getProductInfoRow(scanValue,3);
							var erps=rowL.split("-");
							if(erps.length==2){
								erp_model=erps[0].trim();	// 车型
								erp_package=erps[1].trim();	// 内部型号
							}
							fdjNo = getProductInfoRow(scanValue,7);//发动机号
							vin = getProductInfoRow(scanValue,2);	// VIN号
							erp_name="";	// 装配状态
							productDate = getProductInfoRow(scanValue,8);	// 生产日期和下线日期
							if(productDate.length>7){
								productDate = productDate.substr(0, 4)+"-"+productDate.substr(4, 2)+"-"+productDate.substr(6, 2);
							}
							dzCode=getProductInfoRow(scanValue,5);//定制码
							xzCode=getProductInfoRow(scanValue,4);//选装码
							orgId=getProductInfoRow(scanValue,9);//站点
							coCode=getProductInfoRow(scanValue,6);//颜色码
							hegezheng_code = "";//合格证号
							sdNumber ="";//流水号
							remark = "";//备注
							productNo = getProductInfoRow(scanValue,10);	// 生产单号*/
						}else{
							MyAlertForFun('入库条码信息错误',focusNoInput);
							document.getElementById('TOTAL_ORDER_ID').value="";
							return;
					   }
					}
					document.getElementById("TOTAL_ORDER_NO").value=productNo;
					document.getElementById("VIN").value = vin;
					document.getElementById("ENGINE_NO").value = fdjNo;
					document.getElementById("PRODUCT_DATE").value = productDate;
					document.getElementById("OFFLINE_DATE").value = productDate;
					document.getElementById("SD_NUMBER").value =sdNumber;
					document.getElementById("HEGEZHENG_CODE").value = hegezheng_code;
					document.getElementById("erp_model").value = erp_model;
					document.getElementById("erp_package").value = erp_package;
					document.getElementById("erp_name").value = erp_name;
					document.getElementById("remark").value = remark;
					makeCall("<%=contextPath%>/common/AjaxSelectAction/getServerTime.json", function(json){
						if(!json.Exception) {
							document.getElementById("ORG_STORAGE_DATE").value = json.sysdate;
						}
					},null);
					
					if(vin != false) 
					{
						//if(userAreaId==<%=Constant.areaIdJJ%>){//九江专用
							
						//	var VIN = document.getElementById("VIN").value;
						//	var ERP_MODEL = document.getElementById("erp_model").value;
						//	var ERP_PACKAGE = document.getElementById("erp_package").value;
						//	var ERP_NAME = document.getElementById("erp_name").value;
						//	makeCall(g_webAppName + '/sales/storage/storagemanage/ReceivingStorage/receivingStorageMainJJ.json',addReceivingBack,{VIN : VIN,ERP_MODEL:ERP_MODEL,ERP_PACKAGE:ERP_PACKAGE,ERP_NAME:ERP_NAME,orgId:orgId}); 
						//}else 
						if(userAreaId==<%=Constant.areaIdHF%>){//合肥专用
							var orderNO = document.getElementById("TOTAL_ORDER_NO").value;
							var url = "<%=contextPath%>/sales/planmanage/ProductPlan/ProductPlanSearch/getPlanDetailWithStorage.json";
							makeCall(url, function(json){
								if(json.Exception) 
								{
									MyAlertForFun(json.Exception.message, formReset);
								}
								else 
								{
									var VIN = document.getElementById("VIN").value;
									var ERP_MODEL = document.getElementById("erp_model").value;
									var ERP_PACKAGE = document.getElementById("erp_package").value;
									var TOTAL_ORDER_ID = document.getElementById("TOTAL_ORDER_NO").value;
									makeCall('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/receivingStorageMainHF.json',addReceivingBack,{VIN : VIN,ERP_MODEL:ERP_MODEL,ERP_PACKAGE:ERP_PACKAGE,dzCode:dzCode,xzCode:xzCode,TOTAL_ORDER_ID : TOTAL_ORDER_ID,coCode:coCode}); 
								}
							} ,{orderNO : productNo}); 
						}else if(userAreaId==<%=Constant.areaIdJZD%> || userAreaId==<%=Constant.areaIdJJ%>){
							var orderNO = document.getElementById("TOTAL_ORDER_NO").value;
							var url = "<%=contextPath%>/sales/planmanage/ProductPlan/ProductPlanSearch/getPlanDetailWithStorage.json";
							makeCall(url, function(json){
								if(json.Exception) 
								{
									MyAlertForFun(json.Exception.message, formReset);
								}
								else 
								{
									document.getElementById("MATERIAL_CODE").value = json.info[0].MATERIAL_CODE;
									document.getElementById("REGION_NAME").value = json.info[0].REGION_NAME;
									var TOTAL_ORDER_ID = document.getElementById("TOTAL_ORDER_NO").value;
									var VIN = document.getElementById("VIN").value;
									var ERP_MODEL = document.getElementById("erp_model").value;
									var ERP_PACKAGE = document.getElementById("erp_package").value;
									var ERP_NAME = document.getElementById("erp_name").value;
									makeCall('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/receivingStorageMain.json',addReceivingBack,{MATERIAL_CODE : json.info[0].MATERIAL_CODE, TOTAL_ORDER_ID : TOTAL_ORDER_ID, VIN : VIN,ERP_MODEL:ERP_MODEL,ERP_PACKAGE:ERP_PACKAGE,ERP_NAME:ERP_NAME}); 
								}
							} ,{orderNO : productNo}); 
						}
					} 
					else 
					{
						MyAlertForFun('无效VIN号',focusNoInput);
						document.getElementById('TOTAL_ORDER_ID').value="";
					}
				}
		  }

		}else{
			MyAlertForFun('数据加载错误或用户未分配产地',focusNoInput);
			document.getElementById('TOTAL_ORDER_ID').value="";
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
		var YIELDLY=document.getElementById("YIELDLY");//产地ID（隐藏域）
		var SERIES_ID=document.getElementById("SERIES_ID");//车系ID（隐藏域）
		var MODEL_ID=document.getElementById("MODEL_ID");//车型ID（隐藏域）
		var PACKAGE_ID=document.getElementById("PACKAGE_ID");//配置ID（隐藏域）
		var MATERIAL_ID=document.getElementById("MATERIAL_ID");//物料ID（隐藏域）
		var SIT_CODE=document.getElementById("SIT_CODE");//库位码（隐藏域）
		var isLastPosition=document.getElementById("isLastPosition");//后台处理用（隐藏域）
		var COLOR_NAME=document.getElementById("COLOR_NAME");//颜色（隐藏域）
		var MODEL_YEAR=document.getElementById("MODEL_YEAR");//年型（隐藏域）
		var PLAN_DETAIL_ID=document.getElementById("PLAN_DETAIL_ID");//计划详细ID（隐藏域）	
		var YIELDLY_NAME_SHOW=document.getElementById("YIELDLY_NAME_SHOW");//产地NAME（显示）
		var MODEL_CODE_SHOW=document.getElementById("MODEL_CODE_SHOW");//车型（显示）
		var MODEL_NAME_SHOW=document.getElementById("MODEL_NAME_SHOW");//车型名称（显示）
		var PACKAGE_CODE_SHOW=document.getElementById("PACKAGE_CODE_SHOW");//配置代码（显示）
		var PACKAGE_NAME_SHOW=document.getElementById("PACKAGE_NAME_SHOW");//配置名称（显示）
		var MATERIAL_NAME_SHOW=document.getElementById("MATERIAL_NAME_SHOW");//物料NAME（显示）		
		var SIT_CODE_SHOW=document.getElementById("SIT_CODE_SHOW");//库位码（显示）
		var AREA_NAME_SHOW=document.getElementById("AREA_NAME");//库区（显示）
		var ROAD_NAME_SHOW=document.getElementById("ROAD_NAME");//库道（显示）
		var SIT_NAME_SHOW=document.getElementById("SIT_NAME");//库位（显示）
		var ORG_ID=document.getElementById("ORG_ID");//库位（隐藏）
		//*************************************************获取页面属性END******************************************************//

		if(json.returnValue==1)
		{
			//*******************************************隐藏域Start**************************************************//
			YIELDLY.value=json.resultMap.YIELDLY;
			SERIES_ID.value=json.resultMap.SERIES_ID;
			MODEL_ID.value=json.resultMap.MODEL_ID;
			PACKAGE_ID.value=json.resultMap.PACKAGE_ID;
			MATERIAL_ID.value=json.resultMap.MATERIAL_ID;		
			isLastPosition.value=json.resultMap.isLastPosition;//后台用
			COLOR_NAME.value=json.resultMap.COLOR_NAME;	
			MODEL_YEAR.value=json.resultMap.MODEL_YEAR;	
			PLAN_DETAIL_ID.value=json.resultMap.PLAN_DETAIL_ID;	//计划详细ID
			//*******************************************隐藏域END***************************************************//
			//*******************************************显示域Start*************************************************//
			YIELDLY_NAME_SHOW.value=json.resultMap.YIELDLY_NAME;
			MODEL_CODE_SHOW.value=json.resultMap.MODEL_CODE;
			MODEL_NAME_SHOW.value=json.resultMap.MODEL_NAME;
			PACKAGE_CODE_SHOW.value=json.resultMap.PACKAGE_CODE;
			PACKAGE_NAME_SHOW.value=json.resultMap.PACKAGE_NAME;
			MATERIAL_NAME_SHOW.value=json.resultMap.MATERIAL_NAME;	
			ORG_ID.value=json.resultMap.ORG_ID;		
			
			if(isAuto=="SD"){
				document.getElementById("incoming").disabled = "";
				//SIT_CODE.value
			}else{
				SIT_CODE_SHOW.innerHTML=json.resultMap.SIT_CODE;
				AREA_NAME_SHOW.value=json.resultMap.AREA_ID;
				ROAD_NAME_SHOW.value=json.resultMap.ROAD_ID;
				SIT_NAME_SHOW.value=json.resultMap.SIT_ID;
				SIT_CODE_SHOW.value=json.resultMap.SIT_CODE;
				SIT_CODE.value=json.resultMap.SIT_CODE;	
				document.getElementById("incoming").disabled = "disabled";
				//设置库区，库位值
				doInitCombo("","",json.resultMap.ROAD_ID,json.resultMap.ROAD_NAME,json.resultMap.SIT_ID,json.resultMap.SIT_NAME);
				
				makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/getHisVehicle.json", function(json){
					if (json.returnValue == 1) 
					{//退回车需确认后才能入库
						addReset(true,json.retreatdes);
					}else{//不是退回车不需确认
						addReset(false,'');
					}
				},'fm'); 
			}
			//*******************************************显示域END***************************************************//
		
			
		}
		else if(json.returnValue==2){
			MyAlertForFun("处理失败，无法找到该生产批售单号", formReset); 
		}
		else if(json.returnValue==3){
			MyAlertForFun("处理失败，已超过该订单的最大计划数，无法添加",formReset); 
		}
		else if(json.returnValue==4){
			MyAlertForFun("无产地",formReset); 
		}
		else if(json.returnValue==5){
			MyAlertForFun("无符合的空库道",formReset); 
		}
		else if(json.returnValue==14){
			MyAlertForFun("该VIN号已入库",formReset); 
		}
		else if(json.returnValue==20){
			MyAlertForFun("必须同物料才能入库",formReset); 
		}
		else if(json.returnValue==93){
			MyAlertForFun("无法找到该物料",formReset); 
		}
		else if(json.returnValue==29){
			MyAlertForFun("处理失败",formReset); 
		}
		else if(json.returnValue==90){
			MyAlertForFun("站点解析错误",formReset); 
		}
		else if(json.returnValue==94){
			MyAlertForFun("无法找到装配状态",formReset); 
		}
		else if(json.returnValue==95){
			MyAlertForFun("无法找到新的颜色代码",formReset); 
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

		document.getElementById("incoming").disabled = "";
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
		var hgzNo=document.getElementById("hgzNo");//合格证扫描验证
		if(hgzNo.value == "") {
			MyAlert("合格证扫描信息不能为空");
			document.getElementById("hgzNo").focus();
			return ;
		}
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/saveReceivingStorage.json", function(json){
			
			if (json.returnValue == 1) 
			{
				document.getElementById("incoming").disabled = "disabled";//确定入库按钮不可用
				document.getElementById("printSitCode").disabled = "";//打印按钮可用
				vinCode = document.getElementById("VIN").value;
				document.getElementById("perCode").value=json.perCode;
				document.getElementById("vinLast").value = vinCode;
				document.getElementById("prVin").value=vinCode;
				var sitCode=getSitCode();
				printcode(vinCode,sitCode);
				formReset();
			} 
			else if (json.returnValue == 2) 
			{
				MyAlertForFun("操作失败！该库位已有车辆",formReset);
			} 
			else if (json.returnValue == 3) 
			{
				MyAlertForFun("操作失败!该VIN号已入库",formReset);
			} 
			else 
			{
				MyAlertForFun("操作失败!请联系系统管理员!",formReset);
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
		document.getElementById("TOTAL_ORDER_NO").value = "";
		document.getElementById("MATERIAL_ID").value = "";//物料ID（隐藏域）
		document.getElementById("ORG_ID").value = "";
		document.getElementById("VIN").value = "";
		document.getElementById("YIELDLY").value = "";//产地ID（隐藏域）
		document.getElementById("SERIES_ID").value = "";//车系ID（隐藏域）
		document.getElementById("MODEL_ID").value = "";//车型ID（隐藏域）
		document.getElementById("PACKAGE_ID").value = "";//配置ID（隐藏域）
		
		document.getElementById("SIT_CODE").value = "";//库位码（隐藏域）
		document.getElementById("isLastPosition").value = "";//后台处理用（隐藏域）
		document.getElementById("COLOR_NAME").value = "";//颜色（隐藏域）
		document.getElementById("MODEL_YEAR").value = "";//年型（隐藏域）
		document.getElementById("PLAN_DETAIL_ID").value = "";//计划详细ID（隐藏域）
		document.getElementById("YIELDLY_NAME_SHOW").value = "";//产地NAME（显示）
		document.getElementById("MODEL_CODE_SHOW").value = "";//车型（显示）
		document.getElementById("MODEL_NAME_SHOW").value = "";//车型名称（显示）
		document.getElementById("PACKAGE_CODE_SHOW").value = "";//配置代码（显示）
		document.getElementById("PACKAGE_NAME_SHOW").value = "";//配置名称（显示）
		document.getElementById("MATERIAL_NAME_SHOW").value = "";//物料NAME（显示）
		document.getElementById("SIT_CODE_SHOW").innerHTML = "";//库位码（显示）

		document.getElementById("SD_NUMBER").value = "";//流水号
		document.getElementById("HEGEZHENG_CODE").value = "";//合格证
		document.getElementById("REGION_NAME").value = "";//省份
		
		document.getElementById("erp_model").value = "";
		document.getElementById("erp_package").value = "";
		document.getElementById("erp_package").value = "";
		document.getElementById("remark").value = "";
		document.getElementById("incoming").disabled = "disabled";
		document.getElementById("AREA_NAME").value = "";
		document.getElementById("ROAD_NAME").value = "";
		document.getElementById("SIT_NAME").value = "";
		document.getElementById("ORG_STORAGE_DATE").value = "";
		document.getElementById("perCode").value = "";
		document.getElementById("hgzNo").value = "";//合格证扫描设置为空
		
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
	//合格证扫描调用方法
	function onHgz(){
		var openReturn=openCOM();
		if(openReturn=='00' || openReturn==00){
			if(typeof(TUReader.Inventory())=='undefined'){
				MyAlert("合格证扫描控件未设置或设置错误！");
				return;
			}
			if(TUReader.Inventory()!="" && TUReader.Inventory()!=null){
				hgzProcess();
			}else{
				MyAlert("扫描件放置错误或该扫描件已破损或连接读取设备有误,读取失败！");
			}
		}else{
			MyAlert("打开接口失败！请检查COM端口是否为COM6或者驱动是否正常安装");
		}
	}
	//合格证获取值
	function hgzProcess() {
		document.all("hgzNo").value= TUReader.Inventory();
		closeCOM();
		document.getElementById("REGION_NAME").focus();
	}
</script>
<OBJECT style="display:none;" ID="tecPrint"  CLASSID="CLSID:82139C0A-F89F-4EB4-9DDF-38798CBF53B6"
CODEBASE="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/tecPrinter.CAB#version=1,0,0,0">
</OBJECT>
<OBJECT style="display:none;" id=TUReader codebase="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/UReaderProj.CAB#version=1,0,0,2" classid="clsid:220AC733-63C2-4B1E-AC07-EF0B5147BE17">
</OBJECT>
</head>
<body onload="focusNoInput()" onkeydown="focusNoInput()">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> 接车入库
		</div>
		<form method="post" name="fm" id="fm">
			<table class="table_edit" id="table_edit">
				<col width="100" />
				<col />
				<col width="100" />
				<col />
				<tr>
					<td colspan="6" class="table_query_4Col_input" style="text-align: center"><span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;先扫合格证RFID芯片,再扫入库条码</span></td>
				</tr>
				<tr>
					<td  class="right">入库方式：</td>
					<td  align="left"  colspan="5">
						<input type="hidden" id="autoValue" value="ZD" />
						<input type="button" class="normal_btn" id="autoType" onclick="changeAutoType()" value="自动"  title="点击切换入库方式{手动或自动}"/> 
						&nbsp;&nbsp;&nbsp;&nbsp;
					<textarea rows="5" cols="80" id="TOTAL_ORDER_ID" name="TOTAL_ORDER_ID" onkeydown="doKeyDown()" onchange="doInputChange()"></textarea>
					</td>
				</tr>
				<tr class="csstr" align="center">
					<td class="right">生产单号：</td>
					<td align="left" class="table_query_2Col_input" nowrap="nowrap">
						<input type="text" maxlength="20"  name="TOTAL_ORDER_NO" id="TOTAL_ORDER_NO" class="middle_txt"  readonly="readonly"/>
					</td>
					<td class="right">物料代码：</td>
					<td align="left" class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="MATERIAL_CODE" name="MATERIAL_CODE" class="middle_txt"  readonly="readonly"  />
					</td>
					<td>接车员</td>
					<td></td>
					
				</tr>
				<tr class="csstr" align="center">
					<td class="right">产地：</td>
					<td align="left" class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="YIELDLY_NAME_SHOW" readonly="readonly" class="middle_txt"/>
						<input type="hidden" id="PROCESS_TYPE" name="PROCESS_TYPE" class="middle_txt" value="自动入库" /> 
						<input type="hidden" id="userAreaId" name="userAreaId" class="middle_txt" value="${userAreaId } "  /><!-- 用户产地 -->
						<input type="hidden" id="YIELDLY" name="YIELDLY" class="middle_txt" readonly="readonly"  /> 
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
					<td align="left" class="table_query_2Col_input">
						<input type="text" maxlength="20"  class="middle_txt" id="MATERIAL_NAME_SHOW" readonly="readonly"/>
					</td>
					
					<td rowspan="9">
					<input type="hidden" class="middle_txt" id="perCode"/>
					<select name="PER_ID"
						id="PER_ID" class="selectlist"  size="14" style="width: 100px;font-size:20px">
							<!-- <option value="">-请选择-</option> -->
							<c:if test="${list_Pn!=null}">
								<c:forEach items="${list_Pn}" var="list" varStatus="ls">    
									<option  style="font-size: 20px" value="${list.PER_ID }"  <c:if test="${ls.first}">selected="selected"</c:if>>${list.PER_NAME }</option>
								</c:forEach>
							</c:if>
					</select></td>
					<td></td>
				</tr>
				<tr>
					<td class="right">车型：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  class="middle_txt" id="MODEL_CODE_SHOW" readonly="readonly"/>
					</td>
					<td class="right">车型名称：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  class="middle_txt" id="MODEL_NAME_SHOW" readonly="readonly"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td class="right">配置代码：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  class="middle_txt" id="PACKAGE_CODE_SHOW" readonly="readonly"/>
					</td>
					<td class="right">配置名称：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  class="middle_txt" id="PACKAGE_NAME_SHOW" readonly="readonly"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td class="right">VIN：</td>
					<td class="table_query_2Col_input"><input type="text" maxlength="20"  id="VIN"
						name="VIN" class="middle_txt" value="" datatype="0,is_null,17" />
					</td>
					<td class="right">发动机号：</td>
					<td class="table_query_2Col_input"><input type="text" maxlength="20" 
						id="ENGINE_NO" name="ENGINE_NO" class="middle_txt" value=""
						datatype="0,is_null,30" /></td>
					<td colspan="2"></td>
				</tr>
				<tr class="csstr" align="center">
					<td class="right">生产日期：</td>
					<td class="table_query_2Col_input"><input name="PRODUCT_DATE"
						type="text" maxlength="20"  class="middle_txt" id="PRODUCT_DATE"
						readonly="readonly" datatype="0,is_date,10" /> <input
						name="button" type="button" class="time_ico"
						onclick="showcalendar(event, 'PRODUCT_DATE', false);" /></td>
					<td class="right">下线日期：</td>
					<td class="table_query_2Col_input"><input name="OFFLINE_DATE"
						type="text" maxlength="20"  class="middle_txt" id="OFFLINE_DATE"
						readonly="readonly" datatype="0,is_date,10" /> <input
						name="button" type="button" class="time_ico"
						onclick="showcalendar(event, 'OFFLINE_DATE', false);" /></td>
					<td colspan="2"></td>
				</tr>
				<tr class="csstr" align="center">
					<td class="right">入库日期：</td>
					<td class="table_query_2Col_input">
						<input name="ORG_STORAGE_DATE" type="text" maxlength="20"  class="middle_txt" id=ORG_STORAGE_DATE readonly="readonly"  />
					</td>
					<td class="right">合格证号：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="HEGEZHENG_CODE" name="HEGEZHENG_CODE" class="middle_txt" readonly="readonly"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr class="csstr" align="center">
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
					<td class="right">库道：</td>
					<td class="table_query_2Col_input"><select name="ROAD_NAME"
						id="ROAD_NAME" class="u-select" onchange="sitCheckBox('other');">
							<option value="">-请选择-</option>
					</select></td>
					<td colspan="2"></td>
				</tr>
				<tr class="csstr" align="center">
					<td class="right">库位：</td>
					<td class="table_query_2Col_input"><select name="SIT_NAME"
						id="SIT_NAME" class="u-select">
							<option value="">-请选择-</option>
					</select></td>
					<td class="right">库位码：</td>
					<td class="table_query_2Col_input"><span id="SIT_CODE_SHOW"></span>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td class="right">省份：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="REGION_NAME" class="middle_txt" readonly="readonly"/>
					</td>
					<td class="right">流水号：</td>
					<td>
						<input type="text" maxlength="20"  id="SD_NUMBER" name="SD_NUMBER" class="middle_txt" readonly="readonly"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td class="right">已入库车辆：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="vinLast" class="middle_txt" readonly="readonly"/>
					</td>
					<td class="right">合格证RFID芯片系列号：</td>
					<td class="table_query_2Col_input">
						<input type="text" maxlength="20"  id="hgzNo" name="hgzNo" onfocus="onHgz();" class="middle_txt" readonly="readonly"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td align="center" colspan="6">
						<input type="hidden" id="prVin" class="middle_txt" readonly="readonly"/>
						<input type="hidden" id="prSitCode" class="middle_txt" readonly="readonly"/>
						<input type="button" name="button1" id="incoming" class="normal_btn" disabled="disabled"  onclick="saveRe()" value="确定入库" /> 
						<input type="button" id="printSitCode" disabled="disabled" class="normal_btn" value="打印库位码" onclick="disP(document.getElementById('prVin').value,document.getElementById('prSitCode').value)" style="width: 100px;"/> 
						<input type="button" value="重置" class="normal_btn" onclick="formReset();"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>