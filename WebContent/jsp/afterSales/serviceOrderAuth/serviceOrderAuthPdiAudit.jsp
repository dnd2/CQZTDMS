﻿﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAuthAction/serviceOrderAuthContentQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
	           {header: "", dataIndex: '',align:'center', style:"width:12px;"},
	           {header: "预授权类型", dataIndex: 'AUTH_CONTENT_TYPE_NAME', align:'center',style:'background-color:#FFFFFF;width:110px'},
	           {header: "预授权说明", dataIndex: 'AUTH_CONTENT_DESC', align:'center',style:'background-color:#FFFFFF;'}
	          ];
//页面初始化	
function doInit(){
  //loadcalendar();
  getVehicleInfo();
  __extQuery__(1);
}
//授权
function authAudit(authAuditStatus){
  var authAuditRemark = document.getElementById("authAuditRemark");
  document.getElementById("authAuditStatus").value = authAuditStatus;
  //提示信息
  if(authAuditStatus==12521003||authAuditStatus==12521004){//授权审核驳回或者授权审核拒绝
    if(authAuditRemark.value==""){
      MyAlert("请填写驳回/拒绝原因!");
      return;
    }
  }
  var msg = "";
  if(authAuditStatus=="12521002"){
	  msg = "预授权审核通过";
  }
  if(authAuditStatus=="12521003"){
	  msg = "预授权审核驳回";
  }
  if(authAuditStatus=="12521004"){
	  msg = "预授权审核拒绝";
  }
  MyConfirm("确认"+msg+"?",authAuditSave);
}
//授权保存
function authAuditSave(){
  var url =  "<%=contextPath%>/afterSales/ServiceOrderAuthAction/serviceOrderAuthAuditSave.json";
  makeNomalFormCall(url,authAuditResult,'fm');
}
//授权结果
function authAuditResult(json){
  //alert();
  if(json.code == "succ"){
    MyAlertForFun("审核成功！",toServiceOrderAuthListUrl);
  }else{
    MyAlert(json.msg);
  }
}
//跳转
function toServiceOrderAuthListUrl(){
  window.location.href="<%=contextPath%>/afterSales/ServiceOrderAuthAction/serviceOrderAuthList.do";
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
		//document.getElementById("purchasedDate").value = json.map.PURCHASED_DATE==null?"":json.map.PURCHASED_DATE;
		document.getElementById("_purchasedDate").innerHTML = json.map.PURCHASED_DATE==null?"":json.map.PURCHASED_DATE;
		document.getElementById("productDate").innerHTML = json.map.PRODUCT_DATE==null?"":json.map.PRODUCT_DATE;
		document.getElementById("packageName").innerHTML = json.map.PACKAGE_NAME==null?"":json.map.PACKAGE_NAME;
		document.getElementById("ctmName").innerHTML = json.map.CTM_NAME==null?"":json.map.CTM_NAME;
		document.getElementById("ruleName").innerHTML = json.map.RULE_NAME==null?"":json.map.RULE_NAME;
	}else{
		MyAlert(json.msg);
		//车辆信息
		document.getElementById("licenseNo").innerHTML = "--/--";
		document.getElementById("engineNo").innerHTML = "--/--";
		document.getElementById("brandName").innerHTML = "--/--";
		document.getElementById("seriesName").innerHTML = "--/--";
		document.getElementById("modelName").innerHTML = "--/--";
		document.getElementById("areaName").innerHTML = "--/--";
		//document.getElementById("purchasedDate").value = "";
		document.getElementById("_purchasedDate").innerHTML = "--/--";
		document.getElementById("productDate").innerHTML = "--/--";
		document.getElementById("packageName").innerHTML = "--/--";
		document.getElementById("ctmName").innerHTML = "--/--";
		document.getElementById("ruleName").innerHTML = "--/--";
	}
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
  <input type="hidden" name="authAuditId" id="authAuditId" value="${authAuditId}"/>
  <input type="hidden" name="serviceOrderId" id="serviceOrderId" value="${serviceOrderId}"/>
  <input type="hidden" name="repairType" id="repairType" value="${serviceOrderMap.REPAIR_TYPE}"/>
  <input type="hidden" name="vin" id="vin" value="${serviceOrderMap.VIN}"/>
  <input type="hidden" name="authAuditStatus" id="authAuditStatus" value=""/>
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
        ${serviceOrderMap.ARRIVAL_DATE}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">进站里程数：</td>
	  <td align="left">
	    ${serviceOrderMap.MILEAGE}
	  </td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修开始时间：</td>
	  <td align="left">
	    ${serviceOrderMap.REPAIR_DATE_BEGIN}
      </td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right">维修结束时间：</td>
	  <td align="left">
        ${serviceOrderMap.REPAIR_DATE_END}
      </td>
      <td class="table_add_2Col_label_5Letter" style="text-align:right">接待员：</td>
	  <td align="left">
        ${serviceOrderMap.RECEPTIONIST_MAN}
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
  <!-- PDI信息 -->
  <div class="form-panel">
  <h2>PDI信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">PDI备注：</td>
	  <td align="left">
	    ${serviceOrderMap.PDI_REMARK}
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
	    ${serviceOrderMap.APPLY_PDI_PRICE}
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
	    ${serviceOrderMap.APPLY_PRICE_TOTAL}
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
  <!-- 预授权历史 -->
  <div class="form-panel">
  <h2>预授权历史</h2>
    <div class="form-body">
      <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
      <tr class="table_list_th">
        <th class="noSort" width="12px">&nbsp;</th>
        <th class="noSort" width="100px">授权级别</th>
        <th class="noSort" width="100px">授权状态</th>
        <th class="noSort">授权说明</th>
	    <th class="noSort" width="80px">授权人</th>
	    <th class="noSort" width="80px">授权时间</th>
	  </tr>
	  <c:if test="${serviceOrderAuthAuditList!=null}">
      <c:forEach items="${serviceOrderAuthAuditList}" var="serviceOrderAuthAuditList">
	  <tr>
	    <td width="12px">&nbsp;</td>
	    <td style="background-color:#FFFFFF;">
          ${serviceOrderAuthAuditList.APPROVAL_LEVEL_NAME}
        </td>
        <td style="background-color:#FFFFFF;">
          ${serviceOrderAuthAuditList.AUTH_AUDIT_STATUS_NAME}
        </td>
        <td style="background-color:#FFFFFF;">
          ${serviceOrderAuthAuditList.AUTH_AUDIT_REMARK}
        </td>
        <td style="background-color:#FFFFFF;">
          ${serviceOrderAuthAuditList.AUTH_AUDIT_BY_NAME}
        </td>
        <td style="background-color:#FFFFFF;">
          ${serviceOrderAuthAuditList.AUTH_AUDIT_DATE}
        </td>
      </tr>
      </c:forEach>
      </c:if>
      </table>
    </div>
  </div>
  <!-- 预授权原因 -->
  <div class="form-panel" style="width:100%">
  <h2>预授权原因</h2>
    <div class="form-body">
    <table style="width:100%;height:100%;">
	<tr>
      <td>
	    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
      </td>
	</tr>
	</table>
    </div>
  </div>
  <!-- 预授权原因 -->
  <!--  
  <div class="form-body">
  <h2>预授权内容</h2>
    <div class="form-body">
      <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
	  <tr class="table_list_th">
	    <th class="noSort" style="width:12px">&nbsp;</th>
	    <th class="noSort" style="width:110px">预授权类型</th>
	    <th class="noSort">预授权内容</th>
	  </tr>
	  <c:if test="${serviceOrderAuthContentList!=null}">
	  <c:forEach items="${serviceOrderAuthContentList}" var="serviceOrderAuthContentList">
	  <tr>
	    <td>&nbsp;</td>
	    <td style="background-color:#FFFFFF;">
	      ${serviceOrderAuthContentList.AUTH_CONTENT_TYPE_NAME}
	    </td>
	    <td style="background-color:#FFFFFF;">
		  ${serviceOrderAuthContentList.AUTH_CONTENT_DESC}
	    </td>
	  </tr>
	  </c:forEach>
	  </c:if>
      </table>
  </div>
  -->
  <!-- 预授权 -->
  <div class="form-panel">
  <h2>预授权审核备注</h2>
    <div class="form-body">
      <table style="width:100%">
	  <tr>
	    <td>
	      <textarea id="authAuditRemark" name="authAuditRemark" class="form-control" rows="2" style="width:400px;display:inline-block;"></textarea>
	    </td>
	  </tr>
	  <tr>
	    <td>
		  <input class="normal_btn" type="button" name="button1" value="授权通过"  onclick="authAudit(12521002);"/>
		  <input class="normal_btn" type="button" name="button1" value="授权驳回"  onclick="authAudit(12521003);"/>
		  <input class="normal_btn" type="button" name="button1" value="授权拒绝"  onclick="authAudit(12521004);"/>
		  <input class="normal_btn" type="button" name="button1" value="返回上页"  onclick="window.history.back();"/>
	    </td>
	  </tr>
	  </table>
    </div>
  </div>
</form>
</div>
</body>
</html>