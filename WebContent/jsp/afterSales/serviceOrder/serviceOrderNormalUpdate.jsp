﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!-- 日历 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<!-- 附件 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<title>维修工单登记-新增</title>
<script>
//页面参数
var tempLabourId = "";//工时字符串，用于重复工时验证
var tempPartId = "";//配件字符串，用于重复配件验证
//弹窗样式
var alertCss = {type: 1,
		        skin:'layui-layer-rim',
		        area: ['420px','240px'],
		        title:'操作提示', 
		        shade: true, 
		        content:''}
//页面初始化	
function doInit(){
  //loadcalendar();
  //设置初始值
  getVehicleInfo();
  
}
//维修工单保存
function serviceOrderSave(){
  //开始验证
  //基本信息验证对象
  //var partFareRate = document.getElementById("partFareRate").value;//经销商加价率
  var repairType = document.getElementById("repairType");//维修类型
  var arrivalDate = document.getElementById("arrivalDate");//进站时间
  var mileage = document.getElementById("mileage");//进站里程数
  var repairDateBegin = document.getElementById("repairDateBegin");//维修开始时间
  var repairDateEnd = document.getElementById("repairDateEnd");//维修结束时间
  var receptionistMan = document.getElementById("receptionistMan");//接待员
  //车辆信息验证对象
  var vinValid = document.getElementById("vinValid");//是否有效VIN
  var engineNo = document.getElementById("engineNo");//发动机号
  var engineNoComp = document.getElementById("engineNoComp");//车厂发动机号
  var mileageComp = document.getElementById("mileageComp");//车厂里程数
  var licenseNo = document.getElementById("licenseNo");//车厂里程数
  var purchasedDate = document.getElementById("purchasedDate");//购车日期
  //送修人信息验证对象
  var delivererManName = document.getElementById("delivererManName");//送修人姓名
  var delivererManPhone = document.getElementById("delivererManPhone");//送修人电话
  //维修项目验证对象
  var labourIds = document.getElementsByName("labourId");//工时ID
  var labourCodes = document.getElementsByName("labourCode");//工时代码
  var cnDess = document.getElementsByName("cnDes");//工时名称
  var labourHours = document.getElementsByName("labourHour");//工时定额
  var labourPrices = document.getElementsByName("labourPrice");//工时单价
  var labourPriceTotals = document.getElementsByName("labourPriceTotal");//工时金额(元)
  var labourPaymentMethods = document.getElementsByName("labourPaymentMethod");//工时付费方式
  
  var labourCodeStr = "";//工时代码字符串,用于验证所有工时都被至少一个配件选中
  var labourPayCodeStr = "";//自费工时代码字符串，用于验证工时是自费的，对应的配件必须全是自费的，否则不允许保存
  var labourClaimCodeStr = "";//索赔工时代码字符串，用于验证工时有索赔的，配件如果全是自费，则不允许保存
  //维修配件验证对象
  var isThreeGuarantees = document.getElementsByName("isThreeGuarantee");//是否三包
  var isThreeGuaranteeCheckboxs = document.getElementsByName("isThreeGuaranteeCheckbox");//是否三包
  var partIds = document.getElementsByName("partId");//新件ID
  var partCodes = document.getElementsByName("partCode");//新件代码
  var partCnames = document.getElementsByName("partCname");//新件名称
  var partWarTypes = document.getElementsByName("partWarType");//新件三包类型
  var partNums = document.getElementsByName("partNum");//新件数量
  var partPrices = document.getElementsByName("partPrice");//单价
  var partPriceTotals = document.getElementsByName("partPriceTotal");//金额(元)
  var failureModeCodes = document.getElementsByName("failureModeCode");//失效模式
  var partPaymentMethods = document.getElementsByName("partPaymentMethod");//配件付费方式
  var isMainParts = document.getElementsByName("isMainPart");//是否主因件
  var relationMainParts = document.getElementsByName("relationMainPart");//关联主因件
  var relationLabours = document.getElementsByName("relationLabour");//关联工时
  
  var isMainPartStr = "";//主因件字符串，用于验证至少有一个主因件
  var noMainPartStr = "";//无主因件的次因件字符串，用于验证次因件必须关联一个主因件
  var isMainPayPartStr = "";//自费主因件字符串，用于验证主因件为自费的，关联的次因件也必须为自费
  var relationMainClaimPartStr = "";//索赔次因件关联的主因件
  //申请内容验证对象
  var faultDesc = document.getElementById("faultDesc");//故障描述
  var faultReason = document.getElementById("faultReason");//故障原因
  var repairMethod = document.getElementById("repairMethod");//维修措施
  var remark = document.getElementById("remark");//申请备注
  
  //提示信息
  var msg = "";
  var count = 0;
  /**
  if(partFareRate==""){
	    count++;
	    msg = msg + "<div>" + count + "、经销商加价率为空,请联系车厂修改!</div>\n";
  }
  **/
  //基本信息验证
  if(repairType.value==""){
    count++;
    msg = msg + "<div>" + count + "、请选择维修类型!</div>\n";
    //repairType.style.border = "solid 1px #FF0000";
  }else{
    //repairType.style.border = "solid 1px #000000";
  }
  if(arrivalDate.value==""){
    count++;
    msg = msg + "<div>" + count + "、请选择进站时间!</div>\n";
  }
  if(mileage.value==""){
    count++;
    msg = msg + "<div>" + count + "、进站里程数不能为空!</div>\n";
  }else{
    if(isNaN(mileage.value)){
      count++;
      msg = msg + "<div>" + count + "、进站里程数必须为数字!</div>\n";
    }
  }
  if(repairDateBegin.value==""){
    count++;
    msg = msg + "<div>" + count + "、请选择维修开始时间!</div>\n";
  }
  if(repairDateEnd.value==""){
    count++;
    msg = msg + "<div>" + count + "、请选择维修结束时间!</div>\n";
  }
  if(repairDateBegin.value!=""&&repairDateEnd.value!=""){
    if(repairDateBegin.value>repairDateEnd.value){
   	  count++;
      msg = msg + "<div>" + count + "、维修结束时间必须大于等于维修开始时间!</div>\n";
    }
  }
  if(receptionistMan.value==""){
    count++;
    msg = msg + "<div>" + count + "、接待员不能为空!</div>\n";
  }
  //车辆信息验证
  if(vinValid.value=="false"){
    count++;
  	msg = msg + "<div>" + count + "、请先输入有效车辆VIN码!</div>\n";
  }else{
    if(engineNo.value==""){
      count++;
      msg = msg + "<div>" + count + "、发动机号不能为空!</div>\n";
	}else{
	  if(engineNo.value!=engineNoComp.value){
	    count++;
	    msg = msg + "<div>" + count + "、发动机号与车厂记录发动机号不匹配!</div>\n";
	  }
	}
	if(licenseNo.value==""){
	  count++;
	  msg = msg + "<div>" + count + "、牌照号不能为空!</div>\n";
	}
	if(purchasedDate.value==""){
      count++;
      msg = msg + "<div>" + count + "、该车辆购车时间为空,无法做一般维修!</div>\n";
	}
    if(parseFloat(mileage.value)<parseFloat(mileageComp.value)){
      count++;
      msg = msg + "<div>" + count + "、进站里程数异常，小于车厂记录里程数!</div>\n";
    }
  }
  //送修人信息验证
  if(delivererManName.value==""){
    count++;
    msg = msg + "<div>" + count + "、送修人姓名不能为空!</div>\n";
  }
  if(delivererManPhone.value==""){
    count++;
    msg = msg + "<div>" + count + "、送修人电话不能为空!</div>\n";
  }
  //维修项目验证
  if(labourIds.length<=1){
    count++;
    msg = msg + "<div>" + count + "、维修项目不能为空!</div>\n";
  }else{
	//给验证字符串赋值
    for(var i=1;i<labourIds.length;i++){
      labourCodeStr += "," + labourCodes[i].value;
      if(labourPaymentMethods[i].value=="11801001"){//工时付费方式：自费
        labourPayCodeStr += "," + labourCodes[i].value;
      }
      if(labourPaymentMethods[i].value=="11801002"){//工时付费方式：索赔
        labourClaimCodeStr += "," + labourCodes[i].value;
      }
    }
  }
  //维修配件验证
  if(partIds.length<=1){
    count++;
    msg = msg + "<div>" + count + "、维修配件不能为空!</div>\n";
  }else{
    for(var i=1;i<partIds.length;i++){
      if(partNums[i].value==""){
   	    count++;
   	    msg = msg + "<div>" + count + "、维修配件异常,新件数量不能为空!</div>\n";
      }else if(!isPositive(partNums[i].value)){
   	  	count++;
   	    msg = msg + "<div>" + count + "、维修配件异常,新件数量必须为正整数!</div>\n";
      }
      if(failureModeCodes[i].value==""){
   	    count++;
   	    msg = msg + "<div>" + count + "、维修配件异常,失效模式不能为空!</div>\n";
      }
      if(isMainParts[i].value=="10041001"){//是主因件
    	//主因件字符串赋值
        isMainPartStr += "," + partIds[i].value;
        if(partPaymentMethods[i].value=="11801001"){//自费主因件
          isMainPayPartStr += "," + partIds[i].value;
   	    }
      }else if(isMainParts[i].value=="10041002"){//非主因件
   	    if(relationMainParts[i].value==""){
   	      //count++;
   	      //msg = msg + "<div>" + count + "、维修配件异常,非主因件关联主因件不能为空!</div>\n";
   	      noMainPartStr = "维修配件异常,非主因件关联主因件不能为空!";
        }else{
          if(partPaymentMethods[i].value=="11801002"){//索赔次因件关联的主因件
            relationMainClaimPartStr += "," + relationMainParts[i].value;
          }
        }
      }
      if(relationLabours[i].value==""){
   	    count++;
   	    msg = msg + "<div>" + count + "、维修配件异常,关联工时不能为空!</div>\n";
   	    //如果有配件未选择工时则直接清空工时代码字符串字符串
   	    labourCodeStr = "";
   	    labourClaimCodeStr = "";
      }else{
    	//去除已选工时代码
        labourCodeStr = labourCodeStr.replace(","+relationLabours[i].options[relationLabours[i].selectedIndex].text,"");
    	//配件关联工时在付费工时代码字符串中
    	if(labourPayCodeStr.indexOf(relationLabours[i].options[relationLabours[i].selectedIndex].text)>-1){
    	  if(partPaymentMethods[i].value=="11801002"){//关联工时为付费而配件选择了索赔
   		    count++;
   	        msg = msg + "<div>" + count + "、维修配件异常,配件关联工时为自费,配件也必须为自费!</div>\n";
    	  }
    	}
    	//配件关联工时在索赔工时代码字符串中
    	if(labourClaimCodeStr.indexOf(relationLabours[i].options[relationLabours[i].selectedIndex].text)>-1){
      	  if(partPaymentMethods[i].value=="11801002"){//关联工时为索赔而配件也选择了索赔
      		//去除索赔配件对应索赔工时代码
            labourClaimCodeStr = labourClaimCodeStr.replace(","+relationLabours[i].options[relationLabours[i].selectedIndex].text,"");
      	  }
      	}
      }
    }
    //如果主因件字符串为空,则提示必须有一个主因件
    if(isMainPartStr==""){
   	  count++;
      msg = msg + "<div>" + count + "、维修配件异常,必须有一个配件是主因件!</div>\n";
    }else{
      if(noMainPartStr!=""){
	    count++;
        msg = msg + "<div>" + count + "、"+noMainPartStr+"</div>\n";
      }
    }
    //如果工时代码字符串不为空，则表示有工时未被任意维修配件关联
    if(labourCodeStr!=""){
      count++;
      msg = msg + "<div>" + count + "、维修配件异常,维修配件未关联工时"+labourCodeStr.substr(1)+"!</div>\n";
    }
    //如果索赔工时代码字符串不为空，则表示有索赔工时未配任意索赔配件关联
    if(labourClaimCodeStr!=""){
      count++;
      msg = msg + "<div>" + count + "、维修配件异常,索赔工时"+labourClaimCodeStr.substr(1)+"未关联索赔配件!</div>\n";
    }
    //如果主因件为自费的，关联的次因件也必须为自费
    for(var i=0;i<isMainPayPartStr.split(",");i++){
      for(var j=0;j<relationMainClaimPartStr.split(",");j++){
        if(isMainPayPartStr.split(",")[i]==relationMainClaimPartStr.split(",")[j]){
       	  count++;
          msg = msg + "<div>" + count + "、维修配件异常,索赔次因件必须关联索赔主因件!</div>\n";
        }
      }
    }
  }
  //申请内容验证
  if(faultDesc.value==""){
    count++;
    msg = msg + "<div>" + count + "、请填写故障描述!</div>\n";
  }
  if(faultReason.value==""){
    count++;
    msg = msg + "<div>" + count + "、请填写故障原因!</div>\n";
  }
  if(repairMethod.value==""){
    count++;
    msg = msg + "<div>" + count + "、请填写维修措施!</div>\n";
  }
  if(remark.value==""){
    count++;
    msg = msg + "<div>" + count + "、请填写申请备注!</div>\n";
  }
  
  //if(purchasedDate.value=="--/--") purchasedDate = "";
  
  if(msg == ""){
	MyConfirm("确认保存?",saveServiceOrderInfo);
  }else{
	if(msg.indexOf("6、")>-1){
		msg = msg.substr(0,msg.indexOf("<div>6、")) + "<div>&nbsp;&nbsp;&nbsp;......</div>";
	}
	alertCss.content = msg;
	layer.open(alertCss);
    //MyAlert(msg);
    return;
  }
}
function saveServiceOrderInfo(){
  var url =  "<%=contextPath%>/afterSales/ServiceOrderAction/serviceOrderSave.json";
  makeNomalFormCall(url,saveServiceOrderResult,'fm');
}
function saveServiceOrderResult(json){
  //alert();
  if(json.code == "succ"){
    MyAlertForFun("保存成功！",toServiceOrderListUrl);
  }else{
    MyAlert(json.msg);
  }
}
function toServiceOrderListUrl(){
  window.location.href="<%=contextPath%>/afterSales/ServiceOrderAction/serviceOrderList.do";
}
//改变维修类型-失效
/**
function changeRepairType(){
  //清空维修项目
  clearProject();
  //清空维修配件
  clearPart();
  //清空申请费用
  clearApply();
}
**/
//改变进站时间
function changeArrivalDate(date){
  //alert(date);
  //清空维修项目
  clearProject();
  //清空维修配件
  clearPart();
  //清空申请费用
  clearApply();
}
//改变进站里程数
function changeMileage(){
  //清空维修项目
  clearProject();
  //清空维修配件
  clearPart();
  //清空申请费用
  clearApply();
}
//根据VIN获取对应车辆信息
function getVehicleInfo(){
  //var vin = document.getElementById("vin");
  var url = "<%=contextPath%>/afterSales/ServiceOrderAction/getVehicleInfo.json";
  makeNomalFormCall(url,setVehicleInfo,'fm');
}
//设置车辆信息
function setVehicleInfo(json){
	//alert(json.map.LICENSE_NO);
	if(json.code=="succ"){
		//车辆信息
		document.getElementById("licenseNo").innerHTML = json.map.LICENSE_NO==null?"":json.map.LICENSE_NO;
		document.getElementById("engineNo").innerHTML = json.map.ENGINE_NO==null?"":json.map.ENGINE_NO;
		document.getElementById("brandName").innerHTML = json.map.BRAND_NAME==null?"":json.map.BRAND_NAME;
		document.getElementById("seriesName").innerHTML = json.map.SERIES_NAME==null?"":json.map.SERIES_NAME;
		document.getElementById("modelName").innerHTML = json.map.MODEL_NAME==null?"":json.map.MODEL_NAME;
		document.getElementById("areaName").innerHTML = json.map.AREA_NAME==null?"":json.map.AREA_NAME;
		document.getElementById("purchasedDate").value = json.map.PURCHASED_DATE==null?"":json.map.PURCHASED_DATE;
		document.getElementById("_purchasedDate").innerHTML = json.map.PURCHASED_DATE==null?"":json.map.PURCHASED_DATE;
		document.getElementById("productDate").innerHTML = json.map.PRODUCT_DATE==null?"":json.map.PRODUCT_DATE;
		document.getElementById("packageName").innerHTML = json.map.PACKAGE_NAME==null?"":json.map.PACKAGE_NAME;
		document.getElementById("ctmName").innerHTML = json.map.CTM_NAME==null?"":json.map.CTM_NAME;
		document.getElementById("ruleName").innerHTML = json.map.RULE_NAME==null?"":json.map.RULE_NAME;
		//车型组ID，用于维修项目参数
		document.getElementById("wrgroupId").value = json.map.WRGROUP_ID==null?"":json.map.WRGROUP_ID;
		//三包规则ID,用于维修配件参数
		document.getElementById("ruleId").value = json.map.RULE_ID==null?"":json.map.RULE_ID;
		//车型ID，用于维修配件参数,暂时没用
		document.getElementById("modelId").value = json.map.MODEL_ID==null?"":json.map.MODEL_ID;
		//验证字段
		document.getElementById("vinValid").value = "true";
		document.getElementById("mileageComp").value = json.map.MILEAGE==null?"0":json.map.MILEAGE;
		document.getElementById("engineNoComp").value = json.map.ENGINE_NO==null?"":json.map.ENGINE_NO;
	}else{
		MyAlert(json.msg);
		//车辆信息
		document.getElementById("licenseNo").innerHTML = "--/--";
		document.getElementById("engineNo").innerHTML = "--/--";
		document.getElementById("brandName").innerHTML = "--/--";
		document.getElementById("seriesName").innerHTML = "--/--";
		document.getElementById("modelName").innerHTML = "--/--";
		document.getElementById("areaName").innerHTML = "--/--";
		document.getElementById("purchasedDate").value = "";
		document.getElementById("_purchasedDate").innerHTML = "--/--";
		document.getElementById("productDate").innerHTML = "--/--";
		document.getElementById("packageName").innerHTML = "--/--";
		document.getElementById("ctmName").innerHTML = "--/--";
		document.getElementById("ruleName").innerHTML = "--/--";
		//车型组ID，用于维修项目参数
		document.getElementById("wrgroupId").value = "";
		//三包规则ID,用于维修配件参数
		document.getElementById("ruleId").value = "";
		//车型ID，用于维修配件参数,暂时没用
		document.getElementById("modelId").value = "";
		//验证字段
		document.getElementById("vinValid").value = "false";
		document.getElementById("mileageComp").value = "0";
		document.getElementById("engineNoComp").value = "";
	}
	
}

//新增工时
function serviceProjectAdd(){
  var repairType = document.getElementById("repairType").value;//维修类型
  var vinValid = document.getElementById("vinValid").value;//是否有效VIN
  var wrgroupId = document.getElementById("wrgroupId").value;//车型组ID
  
  //提示信息
  var msg = "";//"<div>新增维修项目异常</div>\n";
  var count = 0;
  if(repairType==""){
    count++;
    msg = msg + "<div>" + count + "、请先选择维修类型!</div>\n";
  }
  if(vinValid=="false"){
    count++;
  	msg = msg + "<div>" + count + "、请先输入有效车辆VIN码!</div>\n";
  }else{
    if(wrgroupId==""){
      count++;
      msg = msg + "<div>" + count + "、该车辆车型组信息为空,请先通知车厂维护该信息!</div>\n";
    }
  }
  if(msg==""){
	  var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceProjectWin.do?wrgroupId="+wrgroupId;
	  OpenHtmlWindow(url,900,500);
  }else{
	  MyAlert(msg);
  }
}
//新增工时-设置
function setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice){
  if(tempLabourId.indexOf(labourId)>-1){
    MyAlert("该工时已存在,请选择其他工时!");
  }else{
    var pos = tableRowAdd("serviceProjectTable");
    var labourIds = document.getElementsByName("labourId");
    var labourCodes = document.getElementsByName("labourCode");
    var cnDess = document.getElementsByName("cnDes");
    var labourHours = document.getElementsByName("labourHour");
    var labourPrices = document.getElementsByName("labourPrice");
    var labourPriceTotals = document.getElementsByName("labourPriceTotal");
    var labourPriceTotal = parseFloat(labourHour)*parseFloat(labourPrice);
    
    var relationLabours = document.getElementsByName("relationLabour");
    
    labourIds[pos].value = labourId;
    labourCodes[pos].value = labourCode;
    cnDess[pos].value = cnDes;
    labourHours[pos].value = labourHour;
    labourPrices[pos].value = labourPrice;
    labourPriceTotals[pos].value = labourPriceTotal;
    //写入工时字符串
    tempLabourId += labourId + ",";
    //设置配件关联工时
    for(var i=0;i<relationLabours.length;i++){
      relationLabours[i].options.add(new Option(labourCode,labourId));
    }
    //设置 申请费用-工时数量
    setApplyLabourHour(labourHour);
    //设置 申请费用-工时金额
    setApplyLabourPrice(labourPriceTotal);
  }
  
  //alert(labourId);
  //alert(pos);	
}
//删除工时
function serviceProjectDel(tableId,obj){
  var dels = document.getElementsByName("projectDel");
  var labourIds = document.getElementsByName("labourId");
  var labourHours = document.getElementsByName("labourHour");
  var labourPriceTotals = document.getElementsByName("labourPriceTotal");
  var relationLabours = document.getElementsByName("relationLabour");
  var labourId;
  var labourHour;
  var labourPriceTotal;
  var relationLabourOptions;
  //删除对应工时字符串
  for(var i=1;i<dels.length;i++){
    if(dels[i]==obj){
      labourId = labourIds[i].value;
      labourHour = labourHours[i].value;
      labourPriceTotal = labourPriceTotals[i].value;
      tempLabourId = tempLabourId.replace(labourId+",","");
    }
  }
  //删除配件的关联工时下拉值
  for(var i=0;i<relationLabours.length;i++){
    relationLabourOptions = relationLabours[i].options;
    for(j=0;j<relationLabourOptions.length;j++){
      if(relationLabourOptions[j].value==labourId){
        relationLabourOptions.remove(j); 
      }
    }
  }
  //设置 申请费用-工时数量
  setApplyLabourHour(-labourHour);
  //设置 申请费用-工时金额
  setApplyLabourPrice(-labourPriceTotal);
  //删除工时列
  tableRowDel(tableId,obj);
}
//新增配件
function servicePartAdd(){
  var repairType = document.getElementById("repairType").value;//维修类型
  var ruleId = document.getElementById("ruleId").value;//三包规则ID
  var arrivalDate = document.getElementById("arrivalDate").value;//进站时间
  var mileage = document.getElementById("mileage").value;//进站里程数
  var purchasedDate = document.getElementById("purchasedDate").value;//购车日期
  var vinValid = document.getElementById("vinValid").value;//是否有效VIN
  var mileageComp = document.getElementById("mileageComp").value;//车厂里程数
  var partFareRate = document.getElementById("partFareRate").value;//经销商加价率
  
  //提示信息
  var msg = "";//"<div>新增维修项目异常</div>\n";
  var count = 0;
  if(partFareRate==""){
	    count++;
	    msg = msg + "<div>" + count + "、经销商加价率为空,请联系车厂修改!</div>\n";
  }
  if(repairType==""){
	    count++;
	    msg = msg + "<div>" + count + "、请先选择维修类型!</div>\n";
  }
  if(arrivalDate==""){
    count++;
    msg = msg + "<div>" + count + "、请先选择进站时间!</div>\n";
  }
  if(mileage==""){
    count++;
    msg = msg + "<div>" + count + "、请先输入进站里程数!</div>\n";
  }else{
    if(isNaN(mileage)){
      count++;
      msg = msg + "<div>" + count + "、进站里程数必须为数字!</div>\n";
    }
  }
  if(vinValid=="false"){
    count++;
  	msg = msg + "<div>" + count + "、请先输入有效车辆VIN码!</div>\n";
  }else{
	if(purchasedDate==""){
      count++;
      msg = msg + "<div>" + count + "、该车辆购车时间为空,无法做一般维修!</div>\n";
	}
    if(!isNaN(mileage)&&parseFloat(mileage)<parseFloat(mileageComp)){
      count++;
      msg = msg + "<div>" + count + "、进站里程数异常，小于前次进站里程数!</div>\n";
    }
    if(ruleId==""){
      count++;
      msg = msg + "<div>" + count + "、三包规则为空，需先由车厂维护三包规则!</div>\n";
    }
  }
  
  //if(purchasedDate=="--/--") purchasedDate = "";
  repairType
  if(msg == ""){
    var url = "<%=contextPath%>/afterSales/ServiceOrderAction/servicePartWin.do?&repairType="+repairType
    		+ "&ruleId="+ruleId+"&arrivalDate="+arrivalDate+"&mileage="+mileage
    		+ "&purchasedDate="+purchasedDate+"&partFareRate="+partFareRate;
    OpenHtmlWindow(url,900,500);
  }else{
    MyAlert(msg);
    return;
  }
}
//新增配件-设置
function setServicePartInfo(isThreeGuarantee,partId,partCode,partCname,partWarType,salePrice1,partPrice){
  if(tempPartId.indexOf(partId)>-1){
    MyAlert("该配件已存在,请选择其他配件!");
  }else{
    var pos = tableRowAdd("servicePartTable");
    var isThreeGuarantees = document.getElementsByName("isThreeGuarantee");
    var isThreeGuaranteeCheckboxs = document.getElementsByName("isThreeGuaranteeCheckbox");//是否三包
    var partIds = document.getElementsByName("partId");
    var partCodes = document.getElementsByName("partCode");
    var partCnames = document.getElementsByName("partCname");
    var partWarTypes = document.getElementsByName("partWarType");
    var salePrice1s = document.getElementsByName("salePrice1");
    var partPrices = document.getElementsByName("partPrice");
    var partPaymentMethods = document.getElementsByName("partPaymentMethod");
    //alert(partCode);
    //labourIds[pos].value = labourId;
    isThreeGuarantees[pos].value = isThreeGuarantee;
    if(isThreeGuarantee=="10041001"){
      isThreeGuaranteeCheckboxs[pos].checked = true;//设置是否三包
      //设置付费方式
      var partPaymentMethod = partPaymentMethods[pos];
      for(var i=0;i<partPaymentMethod.length;i++){
        if(partPaymentMethod.options[i].value=="11801002"){
          partPaymentMethod.options[i].selected = true;
	    }
	  }
    } 
    partIds[pos].value = partId;
    partCodes[pos].value = partCode;
    partCnames[pos].value = partCname;
    partWarTypes[pos].value = partWarType;
    salePrice1s[pos].value = salePrice1;
    partPrices[pos].value = partPrice;
    //设置失效模式
    getFailureMode(partCode);
    //写入配件字符串
    tempPartId += partId + ",";
  }
}
//删除配件
function servicePartDel(tableId,obj){
  var dels = document.getElementsByName("partDel");
  var partIds = document.getElementsByName("partId");
  var partNums = document.getElementsByName("partNum");
  var partPriceTotals = document.getElementsByName("partPriceTotal");//金额
  var isMainParts = document.getElementsByName("isMainPart");//是否主因件
  var partId;
  var partNum = 0;
  var partPriceTotal = 0;
  //删除配件字符串
  for(var i=1;i<dels.length;i++){
    if(dels[i]==obj){//待删除的配件
      partId = partIds[i].value;
      tempPartId = tempPartId.replace(partId+",","");
      //如果配件是主因件
      if(isMainParts[i].value=="10041001"){
        //调用删除关联主因件方法
        relationMainPartDel(i,partId);
      }
    }else{//非待删除配件
      if(partNums[i].value!=""){
        partNum = Number(partNum) + Number(partNums[i].value);
        partPriceTotal = parseFloat(partPriceTotal) + parseFloat(partPriceTotals[i].value);
      }
    }
  }
  //设置 申请费用-配件数量
  setApplyPartNum(partNum);
  //设置 申请费用-配件金额
  setApplyPartPrice(partPriceTotal);
  //删除行
  tableRowDel(tableId,obj);
}
//设置新件数量
function setPartNum(obj){
  var partNums = document.getElementsByName("partNum");
  var partPrices = document.getElementsByName("partPrice");
  var partPriceTotals = document.getElementsByName("partPriceTotal");
  var partNum = 0;
  var partPriceTotal = 0;
  for(var i=1;i<partNums.length;i++){
    if(obj==partNums[i]){
    	if(!isPositive(partNums[i].value)){
    	  partNums[i].value = "";
    	  partPriceTotals[i].value = "";
    	  MyAlert("新件数量必须为大于0的正整数!");
    	}else{
          partPriceTotals[i].value = parseFloat(partNums[i].value)*parseFloat(partPrices[i].value);
    	}
    }
    if(partNums[i].value!=""){
      partNum = Number(partNum) + Number(partNums[i].value);
      partPriceTotal = parseFloat(partPriceTotal) + parseFloat(partPriceTotals[i].value);
    }
  }
  //设置 申请费用-配件数量
  setApplyPartNum(partNum);
  //设置 申请费用-配件金额
  setApplyPartPrice(partPriceTotal);
  //MyAlert("新件数量必须为大于0的正整数!");
}
//获取新件失效模式
function getFailureMode(partCode){
  var url = "<%=contextPath%>/afterSales/ServiceOrderAction/getFailureMode.json?partCode="+partCode;
  makeNomalFormCall(url,setFailureMode,'fm');
}
//设置新件失效模式
function setFailureMode(json){
  var curTable = document.getElementById("servicePartTable");
  var index = curTable.rows.length - 1;
  var failureModeCodes = document.getElementsByName("failureModeCode");//失效模式
  if(json.code=="succ"){
	var list = json.failureModeList;
    if(list!=null){
      for(var i=0;i<list.length;i++){
        failureModeCodes[index].options.add(new Option(list[i].FAILURE_MODE_NAME,list[i].FAILURE_MODE_CODE));
      }
    }
  }else{
    MyAlert(json.msg);
  }
}
//选择是否主因件
function choiceIsMainPart(obj){
  var isMainParts = document.getElementsByName("isMainPart");//是否主因件
  var partIds = document.getElementsByName("partId");//主因件ID
  var partCodes = document.getElementsByName("partCode");//主因件CODE
  var relationMainParts = document.getElementsByName("relationMainPart");//关联主因件
  for(var i=1;i<isMainParts.length;i++){
    if(isMainParts[i]==obj){
      //alert(isMainParts[i].value);
      if(isMainParts[i].value=="10041001"){
        relationMainPartAdd(i,partIds[i].value,partCodes[i].value);
      }else if(isMainParts[i].value=="10041002"){
        relationMainPartDel(i,partIds[i].value);
      }
    }
  }
}
//新增关联主因件
function relationMainPartAdd(pos,partId,partCode){
  var isMainParts = document.getElementsByName("isMainPart");//是否主因件
  var relationMainParts = document.getElementsByName("relationMainPart");//关联主因件
  for(var i=0;i<relationMainParts.length;i++){
    if(i==pos){
      relationMainParts[i].options.length = 0; 
      relationMainParts[i].options.add(new Option("请选择",""));
    }else{
      if(isMainParts[i].value=="10041002"){//非主因件才增加
        relationMainParts[i].options.add(new Option(partCode,partId));
      }
    }
  }
}
//删除关联主因件
function relationMainPartDel(pos,partId){
  var relationMainParts = document.getElementsByName("relationMainPart");//关联主因件
  var options;
  //移出其他配件关联主因件对应配件下拉框值
  for(var i=0;i<relationMainParts.length;i++){
    if(i!=pos){
      options = relationMainParts[i].options;
      for(var j=0;j<options.length;j++){
        if(options[j].value==partId){
          options.remove(j); 
        }
      }
    }
  }
  //设置当前配件关联主因件
  relationMainParts[pos].options.length = 0; 
  for(var i=0;i<relationMainParts[0].options.length;i++){
    relationMainParts[pos].options.add(new Option(relationMainParts[0].options[i].text,relationMainParts[0].options[i].value));
  }
}
//设置 申请费用-工时数量
function setApplyLabourHour(labourHour){
  var applyLabourHour = document.getElementById("applyLabourHour");
  var applyLabourHourSpan = document.getElementById("applyLabourHourSpan");
  applyLabourHour.value = (parseFloat(applyLabourHour.value) + parseFloat(labourHour)).toFixed(2);//保留2位小数
  applyLabourHourSpan.innerHTML = applyLabourHour.value;
}
//设置 申请费用-工时金额
function setApplyLabourPrice(labourPriceTotal){
  var applyLabourPrice = document.getElementById("applyLabourPrice");
  var applyLabourPriceSpan = document.getElementById("applyLabourPriceSpan");
  applyLabourPrice.value = (parseFloat(applyLabourPrice.value) + parseFloat(labourPriceTotal)).toFixed(2);//保留2位小数
  applyLabourPriceSpan.innerHTML = applyLabourPrice.value;
  //设置 申请费用-总金额
  setApplyPriceTotal();
}
//设置 申请费用-配件数量
function setApplyPartNum(partNum){
  var applyPartNum = document.getElementById("applyPartNum");
  var applyPartNumSpan = document.getElementById("applyPartNumSpan");
  applyPartNum.value = (parseFloat(partNum));//
  applyPartNumSpan.innerHTML = applyPartNum.value;
}
//设置 申请费用-配件金额
function setApplyPartPrice(partPriceTotal){
  var applyPartPrice = document.getElementById("applyPartPrice");
  var applyPartPriceSpan = document.getElementById("applyPartPriceSpan");
  applyPartPrice.value = (parseFloat(partPriceTotal)).toFixed(2);//保留2位小数
  applyPartPriceSpan.innerHTML = applyPartPrice.value;
  //设置 申请费用-总金额
  setApplyPriceTotal();
}
//设置 申请费用-总金额
function setApplyPriceTotal(){
  var applyLabourPrice = document.getElementById("applyLabourPrice");//工时金额
  var applyPartPrice = document.getElementById("applyPartPrice");//配件金额
  var applyPriceTotal = document.getElementById("applyPriceTotal");
  var applyPriceTotalSpan = document.getElementById("applyPriceTotalSpan");
  applyPriceTotal.value = (parseFloat(applyLabourPrice.value)+parseFloat(applyPartPrice.value)).toFixed(2);//保留2位小数
  applyPriceTotalSpan.innerHTML = applyPriceTotal.value;
}
//由于维修类型、进站时间、进站里程数、VIN变动,清空维修项目、维修配件、申请费用
function clearProject(){
  var serviceProjectTable = document.getElementById("serviceProjectTable");
  for(var i=serviceProjectTable.rows.length-1;i>0;i--){
    serviceProjectTable.removeChild(serviceProjectTable.rows[i]);
  }
  tempLabourId = "";
}
function clearPart(){
  var servicePartTable = document.getElementById("servicePartTable");
  var relationMainParts = document.getElementsByName("relationMainPart");
  var relationLabours = document.getElementsByName("relationLabour");//关联工时
  //清空配件
  for(var i=servicePartTable.rows.length-1;i>0;i--){
    servicePartTable.removeChild(servicePartTable.rows[i]);
  }
  //清空配件模板关联主因件
  relationMainParts[0].options.length = 0; 
  relationMainParts[0].options.add(new Option("请选择",""));
  //清空配件模板关联工时
  relationLabours[0].options.length = 0; 
  relationLabours[0].options.add(new Option("请选择",""));
  tempPartId = "";
}
function clearApply(){
  //重新计算数量及费用
  var labourHours = document.getElementsByName("labourHour");//工时定额
  var labourPriceTotals = document.getElementsByName("labourPriceTotal");//工时金额
  var partNums = document.getElementsByName("partNum");//新件数量
  var partPriceTotals = document.getElementsByName("partPriceTotal");//新件金额
  
  var labourHour = 0;
  var labourPriceTotal = 0;
  var partNum = 0;
  var partPriceTotal = 0;
  var priceTotal = 0;//总金额
  
  for(var i=0;i<labourHours.length;i++){
    if(labourHours[i].value!=""){
      labourHour = labourHour + parseFloat(labourHours[i].value);
    }
  }
  for(var i=0;i<labourPriceTotals.length;i++){
    if(labourPriceTotals[i].value!=""){
      labourPriceTotal = labourPriceTotal + parseFloat(labourPriceTotals[i].value);
    }
  }
  for(var i=0;i<partNums.length;i++){
    if(partNums[i].value!=""){
      partNum = partNum + Number(partNums[i].value);
    }
  }
  for(var i=0;i<partPriceTotals.length;i++){
    if(partPriceTotals[i].value!=""){
      partPriceTotal = partPriceTotal + parseFloat(partPriceTotals[i].value);
    }
  }
  priceTotal = labourPriceTotal + partPriceTotal;
  
  var applyLabourHour = document.getElementById("applyLabourHour");
  var applyLabourHourSpan = document.getElementById("applyLabourHourSpan");
  var applyLabourPrice = document.getElementById("applyLabourPrice");
  var applyLabourPriceSpan = document.getElementById("applyLabourPriceSpan");
  var applyPartNum = document.getElementById("applyPartNum");
  var applyPartNumSpan = document.getElementById("applyPartNumSpan");
  var applyPartPrice = document.getElementById("applyPartPrice");
  var applyPartPriceSpan = document.getElementById("applyPartPriceSpan");
  
  var applyPriceTotal = document.getElementById("applyPriceTotal");
  var applyPriceTotalSpan = document.getElementById("applyPriceTotalSpan");
  
  applyLabourHour.value = labourHour.toFixed(2);//保留2位小数
  applyLabourHourSpan.innerHTML = labourHour.toFixed(2);//保留2位小数
  applyLabourPrice.value = labourPriceTotal.toFixed(2);//保留2位小数
  applyLabourPriceSpan.innerHTML = labourPriceTotal.toFixed(2);//保留2位小数
  applyPartNum.value = partNum;//保留2位小数
  applyPartNumSpan.innerHTML = partNum;//保留2位小数
  applyPartPrice.value = partPriceTotal.toFixed(2);//保留2位小数
  applyPartPriceSpan.innerHTML = partPriceTotal.toFixed(2);//保留2位小数
  
  applyPriceTotal.value = priceTotal.toFixed(2);//保留2位小数
  applyPriceTotalSpan.innerHTML = priceTotal.toFixed(2);//保留2位小数
  
}
//公用方法
//采用复制行的方式新增行,返回当前表最后行的序号
function tableRowAdd(tableId){
  try{
    var curTable = document.getElementById(tableId);
    var curRow = curTable.rows[0];
    var newRow = curRow.cloneNode(true);
    newRow.style.display = "";
    curTable.appendChild(newRow);
  }catch(err){
	  alert(err.description);
  }
  //返回当前表最后行的序号
  return curTable.rows.length-1;
}
//删除行
function tableRowDel(tableId,obj){
  try{
    var curTable = document.getElementById(tableId);
    var curRow = obj.parentNode.parentNode;
    
    curTable.removeChild(curRow);
  }catch(err){
	  alert(err.description);
  }
}
//大于0的正整数
function isPositive(value){
  var reg = /^[1-9]\d*$/;
  if (value!="") {
    if (reg.test(value)) {
      return true;
    }
  }
  return false;
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修登记&gt;维修工单登记-新增</div>
<form method="post" name="fm" id="fm">
  <!-- 隐藏域 -->
  <input type="hidden" name="serviceOrderId" id="serviceOrderId" value="${serviceOrderId}"/>
  <input type="hidden" name="repairType" id="repairType" value="${serviceOrderMap.REPAIR_TYPE}"/>
  <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}"/><!-- 经销商加价率 -->
  <input type="hidden" name="wrgroupId" id="wrgroupId" value=""/>
  <input type="hidden" name="ruleId" id="ruleId" value=""/>
  <input type="hidden" name="modelId" id="modelId" value=""/>
  <input type="hidden" name="vin" id="vin" value="${serviceOrderMap.VIN}"/>
  <input type="hidden" name="vinValid" id="vinValid" value="false"/>
  <input type="hidden" name="mileageComp" id="mileageComp" value="0"/>
  <input type="hidden" name="engineNoComp" id="engineNoComp" value=""/>
  <input type="hidden" name="purchasedDate" id="purchasedDate" value=""/>
  <input type="hidden" name="lifeCycle" id="lifeCycle" value="10321004"/><!-- 实销 -->
  <!-- 基本信息 -->
  <div class="form-panel">
  <h2>基本信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商代码：</td>
	  <td align="left" style="width:245px">${serviceOrderMap.DEALER_CODE}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商名称：</td>
	  <td align="left" style="width:245px">${serviceOrderMap.DEALER_NAME}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商电话：</td>
	  <td align="left" style="width:245px">${serviceOrderMap.DEALER_PHONE}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">维修类型：</td>
	  <td align="left">
	    ${serviceOrderMap.REPAIR_TYPE_NAME}
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站时间：</td>
	  <td align="left">
        <input id="arrivalDate" name="arrivalDate" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${serviceOrderMap.ARRIVAL_DATE}"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站里程数：</td>
	  <td align="left">
	    <input id="mileage" name="mileage" maxlength="30" type="text" class="middle_txt" value="${serviceOrderMap.MILEAGE}" onchange="changeMileage()"/>
	    <font style="color:red">*</font>
	  </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修开始时间：</td>
	  <td align="left">
	    <input id="repairDateBegin" name="repairDateBegin" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${serviceOrderMap.REPAIR_DATE_BEGIN}"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修结束时间：</td>
	  <td align="left">
        <input id="repairDateEnd" name="repairDateEnd" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${serviceOrderMap.REPAIR_DATE_END}"/>
        <font style="color:red">*</font>
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">接待员：</td>
	  <td align="left">
        <input id="receptionistMan" name="receptionistMan" maxlength="30" value="${serviceOrderMap.RECEPTIONIST_MAN}" type="text" class="middle_txt"/>
        <font style="color:red">*</font>
      </td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 车辆信息 -->
  <div class="form-panel">
  <h2>车辆信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">VIN：</td>
	  <td align="left" style="width:245px">
	    ${serviceOrderMap.VIN}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">发动机号：</td>
	  <td align="left" style="width:245px">
        <span id="engineNo">--/--</span>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">牌照号：</td>
	  <td align="left" style="width:245px">
	    <span id="licenseNo">--/--</span>
      </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">品牌：</td>
	  <td align="left"><span id="brandName">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车系：</td>
	  <td align="left"><span id="seriesName">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车型：</td>
	  <td align="left"><span id="modelName">--/--</span></td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">产地：</td>
	  <td align="left"><span id="areaName">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">购车日期：</td>
	  <td align="left"><span id="_purchasedDate">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">生产日期：</td>
	  <td align="left"><span id="productDate">--/--</span></td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配置：</td>
	  <td align="left"><span id="packageName">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">车主姓名：</td>
	  <td align="left"><span id="ctmName">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">三包规则名称：</td>
	  <td align="left"><span id="ruleName">--/--</span></td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 送修人信息 -->
  <div class="form-panel">
  <h2>送修人信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">送修人姓名：</td>
	  <td align="left" style="width:245px">
	    <input id="delivererManName" name="delivererManName" maxlength="30" value="${serviceOrderMap.DELIVERER_MAN_NAME}" type="text" class="middle_txt"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">送修人电话：</td>
	  <td align="left" style="width:245px">
        <input id="delivererManPhone" name="delivererManPhone" maxlength="30" value="${serviceOrderMap.DELIVERER_MAN_PHONE}" type="text" class="middle_txt" />
        <font style="color:red">*</font>
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px">&nbsp;</td>
	  <td align="left" style="width:245px">&nbsp;</td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 维修项目 -->
  <div class="form-panel">
  <h2>维修项目</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <tr class="table_list_th">
      <th class="noSort" style="width:12px">&nbsp;</th>
      <th class="noSort">工时代码</th>
	  <th class="noSort">工时名称</th>
	  <th class="noSort">工时定额</th>
	  <th class="noSort">工时单价</th>
	  <th class="noSort">工时金额(元)</th>
	  <th class="noSort">付费方式</th>
	  <th class="noSort">
	    <a id="itemBtn" type="button" class="normal_btn" value="新增" name="button422" onClick="serviceProjectAdd();" />新增</a>
	  </th>
	</tr>
	<tbody id="serviceProjectTable">
	<tr class="table_list_row1" style="display:none">
	  <td style="width:12px">&nbsp;</td>
      <td style="background-color:#FFFFFF;width:120px;">
        <input type="hidden" name="labourId" id="labourId" value=""/>
        <input id="labourCode" name="labourCode" maxlength="30" value="" type="text" class="middle_txt" style="width:80px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="cnDes" name="cnDes" maxlength="30" value="" type="text" class="middle_txt" style="width:220px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
	    <input id="labourHour" name="labourHour" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="labourPrice" name="labourPrice" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="labourPriceTotal" name="labourPriceTotal" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <select id="labourPaymentMethod" name="labourPaymentMethod" class="u-select" style="width:40px">
          <option value="11801001">自费</option>
          <option value="11801002">索赔</option>
        </select>
        <font style="color:red">*</font>
	  </td>
	  <td style="background-color:#FFFFFF;">
	    <a href="#" id="projectDel" name="projectDel" class="u-anchor" onClick="serviceProjectDel('serviceProjectTable',this);">删除</a>
	  </td>
	</tr>
	<c:if test="${serviceOrderProjectList!=null}">
	  <c:forEach items="${serviceOrderProjectList}" var="serviceOrderProjectList">
	  <tr class="table_list_row1">
	  <td style="width:12px">&nbsp;</td>
      <td style="background-color:#FFFFFF;width:120px;">
        <input type="hidden" name="labourId" id="labourId" value="${serviceOrderProjectList.LABOUR_ID}"/>
        <input id="labourCode" name="labourCode" maxlength="30" value="${serviceOrderProjectList.LABOUR_CODE}" type="text" class="middle_txt" style="width:80px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="cnDes" name="cnDes" maxlength="30" value="${serviceOrderProjectList.CN_DES}" type="text" class="middle_txt" style="width:220px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
	    <input id="labourHour" name="labourHour" maxlength="30" value="${serviceOrderProjectList.LABOUR_HOUR}" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        ${serviceOrderProjectList.LABOUR_PRICE}
      </td>
	  <td style="background-color:#FFFFFF;">
        ${serviceOrderProjectList.LABOUR_PRICE_TOTAL}
      </td>
	  <td style="background-color:#FFFFFF;">
	    ${serviceOrderProjectList.LABOUR_PAYMENT_METHOD_NAME}
	  </td>
	  <td style="background-color:#FFFFFF;">
	    <a href="#" id="projectDel" name="projectDel" class="u-anchor" onClick="serviceProjectDel('serviceProjectTable',this);">删除</a>
	  </td>
	  </tr>
	  </c:forEach>
	</c:if>
	</tbody>
    </table>
    </div>
  </div>
  <!-- 维修配件 -->
  <div class="form-panel">
  <h2>维修配件</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <tr class="table_list_th">
      <th class="noSort" style="width:12px">&nbsp;</th>
      <th class="noSort">是否三包</th>
      <th class="noSort">新件代码</th>
	  <th class="noSort">新件名称</th>
	  <th class="noSort">数量</th>
	  <th class="noSort">单价</th>
	  <th class="noSort">金额(元)</th>
	  <th class="noSort">失效模式</th>
	  <th class="noSort">付费方式</th>
	  <th class="noSort">是否主因件</th>
	  <th class="noSort">关联主因件</th>
	  <th class="noSort">关联工时</th>
	  <th class="noSort">
	    <a id="itemBtn" type="button" class="normal_btn" value="新增" name="button422" onClick="servicePartAdd();" />新增</a>
	  </th>
	</tr>
	<tbody id="servicePartTable">
	  <tr class="table_list_row1" style="display:none">
	  <td style="width:12px">&nbsp;</td>
	  <td style="background-color:#FFFFFF;">
	    <input type="hidden" id="isThreeGuarantee" name="isThreeGuarantee" value=""/>
        <input type="checkbox" id="isThreeGuaranteeCheckbox" name="isThreeGuaranteeCheckbox" onclick="return false"/>
      </td>
      <td style="background-color:#FFFFFF;width:120px;">
        <input type="hidden" name="partId" id="partId" value=""/>
        <input type="hidden" name="partWarType" id="partWarType" value=""/>
        <input id="partCode" name="partCode" maxlength="30" value="" type="text" class="middle_txt" style="width:80px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="partCname" name="partCname" maxlength="30" value="" type="text" class="middle_txt" style="width:80px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <input id="partNum" name="partNum" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" onchange="setPartNum(this)"/>
        <font style="color:red">*</font>
      </td>
	  <td style="background-color:#FFFFFF;">
	    <input type="hidden" name="salePrice1" id="salePrice1" value=""/>
        <input id="partPrice" name="partPrice" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
        <input id="partPriceTotal" name="partPriceTotal" maxlength="30" value="" type="text" class="middle_txt" style="width:40px" readonly/>
      </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <select id="failureModeCode" name="failureModeCode" class="u-select" style="width:80px">
          <option value="">请选择</option>
        </select>
        <font style="color:red">*</font>
	  </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <select id="partPaymentMethod" name="partPaymentMethod" class="u-select" style="width:40px">
          <option value="11801001">自费</option>
          <option value="11801002">索赔</option>
        </select>
        <font style="color:red">*</font>
	  </td>
      <td style="background-color:#FFFFFF;">
        &nbsp;
	    <select id="isMainPart" name="isMainPart" class="u-select" style="width:80px" onchange="choiceIsMainPart(this)">
          <option value="10041001">是</option>
          <option value="10041002" selected>否</option>
        </select>
        <font style="color:red">*</font>
	  </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <select id="relationMainPart" name="relationMainPart" class="u-select" style="width:80px">
          <option value="">请选择</option>
        </select>
        <font style="color:red">*</font>
	  </td>
	  <td style="background-color:#FFFFFF;">
	    &nbsp;
	    <select id="relationLabour" name="relationLabour" class="u-select" style="width:80px">
          <option value="">请选择</option>
        </select>
        <font style="color:red">*</font>
	  </td>
	  <td style="background-color:#FFFFFF;">
	    <a href="#" id="partDel" name="partDel" class="u-anchor" onClick="servicePartDel('servicePartTable',this);">删除</a>
	  </td>
	  </tr>
	</tbody>
    </table>
    </div>
  </div>
  <!-- 申请内容 -->
  <div class="form-panel">
  <h2>申请内容</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">故障描述：</td>
	  <td align="left">
	    <textarea id="faultDesc" name="faultDesc" class="form-control" rows="3" style="width:300px;display:inline-block"></textarea>
	    <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">故障原因：</td>
	  <td align="left">
        <textarea id="faultReason" name="faultReason" class="form-control" rows="3" style="width:300px;display:inline-block"></textarea>
        <font style="color:red">*</font>
      </td>
    </tr>
    <tr>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">维修措施：</td>
	  <td align="left">
	    <textarea id="repairMethod" name="repairMethod" class="form-control" rows="3" style="width:300px;display:inline-block"></textarea>
	    <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">申请备注：</td>
	  <td align="left">
        <textarea id="remark" name="remark" rows="3" class="form-control" style="width:300px;display:inline-block"></textarea>
        <font style="color:red">*</font>
      </td>
	</tr>
    </table>
    </div>
  </div>
  <!-- 申请费用 -->
  <div class="form-panel">
  <h2>申请费用</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时数量：</td>
	  <td align="left">
	    <input type="hidden" id="applyLabourHour" name="applyLabourHour" value="0.00"/>
	    <span id="applyLabourHourSpan">0.00</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyLabourPrice" name="applyLabourPrice" value="0.00"/>
	    <span id="applyLabourPriceSpan">0.00</span>
	  </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">配件数量：</td>
	  <td align="left">
	    <input type="hidden" id="applyPartNum" name="applyPartNum" value="0"/>
	    <span id="applyPartNumSpan">0</span>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配件金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPartPrice" name="applyPartPrice" value="0.00"/>
	    <span id="applyPartPriceSpan">0.00</span>
	  </td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">外出金额：</td>
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">服务活动金额：</td>
	  <td align="left"><span id="">--/--</span></td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">首保金额：</td>
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">PDI金额：</td>
	  <td align="left"><span id="">--/--</span></td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">总金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPriceTotal" name="applyPriceTotal" value="0.00"/>
	    <span id="applyPriceTotalSpan">0.00</span>
	  </td>
    </tr>
    </table>
    </div>
  </div>
  <!-- 附件信息 -->
  <div class="form-panel">
  <h2>附件信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right"></td>
	  <td align="left">
	    
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right"></td>
	  <td align="left">
        
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right"></td>
	  <td align="left">
	    
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right"></td>
	  <td align="left">
        
      </td>
    </tr>
    </table>
    </div>
  </div>
  <!-- 按钮 -->
  <table border="0" cellspacing="0" cellpadding="0" class="table_query">
    <tr><td colspan="6">&nbsp;</td></tr>
	<tr>
		<td colspan="2">&nbsp;</td>
		<td colspan="2" align=center>
			<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;
            <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;
			<input type="button" onClick="serviceOrderSave();" id="save_but" class="normal_btn" style="" value="确定" />&nbsp;
			<input type="button" onClick="window.history.back();" class="normal_btn" style="" value="返回" />
		</td>
		<td colspan="2">
		  <input class="normal_btn" type="button" id="three_package_set_btn" value="配件三包判定" onclick="threePackageSet();"/>
		  <input class="normal_btn" type="button" id="three_package_set_btn" value="预警检查" onclick="threePackageSet();"/>
		  <input class="normal_btn" type="button" id="three_package_set_btn" value="预警信息历史查询" onclick="threePackageSet();"/>
		</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
  </table>
  
  
</form>
</div>
</body>
</html>