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
  //申请内容验证对象
  var pdiRemark = document.getElementById("pdiRemark");//PDI备注
  
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
	/**
	if(purchasedDate.value==""){
      count++;
      msg = msg + "<div>" + count + "、该车辆购车时间为空,无法做一般维修!</div>\n";
	}
	**/
    if(parseFloat(mileage.value)<parseFloat(mileageComp.value)){
      count++;
      msg = msg + "<div>" + count + "、进站里程数异常，小于车厂记录里程数!</div>\n";
    }
  }
  
  //if(purchasedDate.value=="--/--") purchasedDate = "";
  
  if(msg == ""){
	MyConfirm("确认保存?",saveServiceOrderInfo);
  }else{
	if(msg.indexOf("<div>6、")>-1){
		msg = msg.substr(0,msg.indexOf("6、")) + "<div>&nbsp;&nbsp;&nbsp;......</div>";
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
		document.getElementById("licenseNo").value = json.map.LICENSE_NO==null?"":json.map.LICENSE_NO;
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
		//设置 申请费用-PDI金额
		var newCarFee = (json.map.NEW_CAR_FEE==null?"":json.map.NEW_CAR_FEE).toFixed(2);//保留2位小数
		document.getElementById("applyPdiPrice").value = newCarFee;
		document.getElementById("applyPdiPriceSpan").innerHTML = newCarFee;
		//验证字段
		document.getElementById("vinValid").value = "true";
		document.getElementById("mileageComp").value = json.map.MILEAGE==null?"0":json.map.MILEAGE;
		document.getElementById("engineNoComp").value = json.map.ENGINE_NO==null?"":json.map.ENGINE_NO;
	}else{
		MyAlert(json.msg);
		//车辆信息
		document.getElementById("vin").value = "";
		document.getElementById("licenseNo").value = "";
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
		//设置 申请费用-PDI金额
		document.getElementById("applyPdiPrice").value = "0.00";
		document.getElementById("applyPdiPriceSpan").innerHTML = "0.00";
		//验证字段
		document.getElementById("vinValid").value = "false";
		document.getElementById("mileageComp").value = "0";
		document.getElementById("engineNoComp").value = "";
	}
	//设置 申请费用-总金额
    setApplyPriceTotal();
}
//设置 申请费用-PDI金额
/**
function setApplyPdiPrice(pdiPrice){
  var applyPdiPrice = document.getElementById("applyPdiPrice");
  var applyPdiPriceSpan = document.getElementById("applyPdiPriceSpan");
  applyPdiPrice.value = parseFloat(pdiPrice).toFixed(2);//保留2位小数
  applyPdiPriceSpan.innerHTML = applyPdiPrice.value;
}
**/
//设置 申请费用-总金额
function setApplyPriceTotal(){
  var applyPdiPrice = document.getElementById("applyPdiPrice");//PDI金额
  var applyPriceTotal = document.getElementById("applyPriceTotal");
  var applyPriceTotalSpan = document.getElementById("applyPriceTotalSpan");
  applyPriceTotal.value = parseFloat(applyPdiPrice.value).toFixed(2);//保留2位小数
  applyPriceTotalSpan.innerHTML = applyPriceTotal.value;
}
//公用方法
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
  <input type="hidden" name="repairType" id="repairType" value="${repairType}"/>
  <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}"/><!-- 经销商加价率 -->
  <input type="hidden" name="wrgroupId" id="wrgroupId" value=""/>
  <input type="hidden" name="ruleId" id="ruleId" value=""/>
  <input type="hidden" name="modelId" id="modelId" value=""/>
  <input type="hidden" name="vinValid" id="vinValid" value="false"/>
  <input type="hidden" name="mileageComp" id="mileageComp" value="0"/>
  <input type="hidden" name="engineNoComp" id="engineNoComp" value=""/>
  <input type="hidden" name="purchasedDate" id="purchasedDate" value=""/>
  <input type="hidden" name="lifeCycle" id="lifeCycle" value="10321003"/><!-- 经销商库存 -->
  <input type="hidden" name="isPdi" id="isPdi" value="10041002"/><!-- 非PDI -->
  <!-- 基本信息 -->
  <div class="form-panel">
  <h2>基本信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商代码：</td>
	  <td align="left" style="width:245px">${loginDealerCode}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商名称：</td>
	  <td align="left" style="width:245px">${loginDealerName}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商电话：</td>
	  <td align="left" style="width:245px">${loginDealerPhone}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">维修类型：</td>
	  <td align="left">
	    <input id="repairTypeName" name="repairTypeName" maxlength="30" type="text" class="middle_txt" value="${repairTypeName}" readonly/>
        <font style="color:red">*</font>
	  </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站时间：</td>
	  <td align="left">
        <input id="arrivalDate" name="arrivalDate" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="2014-06-27 15:56"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站里程数：</td>
	  <td align="left">
	    <input id="mileage" name="mileage" maxlength="30" type="text" class="middle_txt" value="8888"/>
	    <font style="color:red">*</font>
	  </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修开始时间：</td>
	  <td align="left">
	    <input id="repairDateBegin" name="repairDateBegin" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修结束时间：</td>
	  <td align="left">
        <input id="repairDateEnd" name="repairDateEnd" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
        <font style="color:red">*</font>
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">接待员：</td>
	  <td align="left">
        <input id="receptionistMan" name="receptionistMan" maxlength="30" value="" type="text" class="middle_txt" />
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
	    <input id="vin" name="vin" maxlength="30" value="" type="text" class="middle_txt" onchange="getVehicleInfo()"/>
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">发动机号：</td>
	  <td align="left" style="width:245px">
        <input id="engineNo" name="engineNo" maxlength="30" value="" type="text" class="middle_txt" />
        <font style="color:red">*</font>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">牌照号：</td>
	  <td align="left" style="width:245px">
	    <input id="licenseNo" name="licenseNo" maxlength="30" value="" type="text" class="middle_txt" />
        <font style="color:red">*</font>
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
  <!-- PDI信息 -->
  <div class="form-panel">
  <h2>PDI信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">PDI备注：</td>
	  <td align="left">
	    <textarea id="pdiRemark" name="pdiRemark" class="form-control" rows="3" style="width:300px;display:inline-block"></textarea>
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
    </tr>
    <tr>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">&nbsp;</td>
	  <td align="left">&nbsp;</td>
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
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">工时金额：</td>
	  <td align="left"><span id="">--/--</span></td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">配件数量：</td>
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">配件金额：</td>
	  <td align="left"><span id="">--/--</span></td>
    </tr>
    <tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">外出金额：</td>
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">服务活动金额：</td>
	  <td align="left"><span id="">--/--</span></td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">首保金额：</td>
	  <td align="left"><span id="">--/--</span></td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">PDI金额：</td>
	  <td align="left">
	    <input type="hidden" id="applyPdiPrice" name="applyPdiPrice" value="0.00"/>
	    <span id="applyPdiPriceSpan">0.00</span>
	  </td>
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
		  <input class="normal_btn" type="button" id="three_package_set_btn" value="预警检查" onclick="alert(document.getElementById('ruleId').value);"/>
		  <input class="normal_btn" type="button" id="three_package_set_btn" value="预警信息历史查询" onclick="threePackageSet();"/>
		</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
  </table>
  
  
</form>
</div>
</body>
</html>