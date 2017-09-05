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
<title>服务活动选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceActivityQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'LABOUR_ID',renderer:mySelect,align:'center'},
				{header: "活动编码", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "活动类型", dataIndex: 'ACTIVITY_TYPE_NAME', align:'center'}
		      ];

function mySelect(value,metaDate,record){
  var activityId = record.data.ACTIVITY_ID==null?"":record.data.ACTIVITY_ID;
  var activityCode = record.data.ACTIVITY_CODE==null?"":record.data.ACTIVITY_CODE;
  var activityName = record.data.ACTIVITY_NAME==null?"":record.data.ACTIVITY_NAME;
  var activityType = record.data.ACTIVITY_TYPE==null?"":record.data.ACTIVITY_TYPE;
  var activityTypeName = record.data.ACTIVITY_TYPE_NAME==null?"":record.data.ACTIVITY_TYPE_NAME;
  var activityDiscount = record.data.ACTIVITY_DISCOUNT==null?"":record.data.ACTIVITY_DISCOUNT;
  var activityMoney = record.data.ACTIVITY_MONEY==null?"":record.data.ACTIVITY_MONEY;
  var activityNum = record.data.ACTIVITY_NUM==null?"":record.data.ACTIVITY_NUM;
  var faultDesc = record.data.FAULT_DESC==null?"":record.data.FAULT_DESC;
  var faultReason = record.data.FAULT_REASON==null?"":record.data.FAULT_REASON;
  var repairMethod = record.data.REPAIR_METHOD==null?"":record.data.REPAIR_METHOD;
  
  return String.format("<input type='radio' name='rd' onclick='setServiceActivityInfo("
			         + "\""+activityId+"\",\""+activityCode+"\","+"\""+activityName+"\","
			         + "\""+activityType+"\",\""+activityTypeName+"\",\""+activityDiscount+"\","
			         + "\""+activityMoney+"\",\""+activityNum+"\",\""+faultDesc+"\","
			         + "\""+faultReason+"\",\""+repairMethod+"\""
			         + ")' />");
}
function setServiceActivityInfo(activityId,activityCode,activityName,activityType,activityTypeName,
		                        activityDiscount,activityMoney,activityNum,faultDesc,faultReason,
		                        repairMethod){
  /**
  if(parent.$('inIframe').contentWindow.setServiceProjectInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice);
    _hide();
  }
  **/
  if(__parent().setServiceActivityInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setServiceActivityInfo(activityId,activityCode,activityName,activityType,activityTypeName,
    		                          activityDiscount,activityMoney,activityNum,faultDesc,faultReason,
    			                      repairMethod);
    _hide();
  }
}
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function serviceActivityQuery(){
  __extQuery__(1);
}
//关闭
function serviceActivityClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：服务活动选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <input type="hidden" name="vin" id="vin" value="${vin}" />
  <input type="hidden" name="mileage" id="mileage" value="${mileage}" />
  <input type="hidden" name="purchasedDate" id="purchasedDate" value="${purchasedDate}" />
  <input type="hidden" name="wrgroupId" id="wrgroupId" value="${wrgroupId}" />
  <input type="hidden" name="activityType" id="activityType" value="${activityType}" />
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td class="right">活动代码：</td>
    <td>
      <input id="activityCode" name="activityCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td class="right">活动名称：</td>
    <td>
      <input id="activityName" name="activityName" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="right">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="serviceActivityQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="serviceActivityClose();" value="关 闭" />
    </td>
  </tr>
  </table>
  </div>
  </div>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>