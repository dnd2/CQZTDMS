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
<title>维修配件选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAction/servicePartQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'PART_ID',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "配件单价", dataIndex: 'PART_PRICE', align:'center'}
		      ];

function mySelect(value,metaDate,record){
	var isThreeGuarantees = record.data.IS_THREE_GUARANTEES==null?"":record.data.IS_THREE_GUARANTEES;
	var partId = record.data.PART_ID==null?"":record.data.PART_ID;
	var partCode = record.data.PART_CODE==null?"":record.data.PART_CODE;
	var partCname = record.data.PART_CNAME==null?"":record.data.PART_CNAME;
	var partWarType = record.data.PART_WAR_TYPE==null?"":record.data.PART_WAR_TYPE;
	var salePrice1 = record.data.SALE_PRICE1==null?"":record.data.SALE_PRICE1;//配件实际调拨价
	var partPrice = record.data.PART_PRICE==null?"0":record.data.PART_PRICE;//单价
	var partNumMax = record.data.PART_NUM_MAX==null?"0":record.data.PART_NUM_MAX;//配件数量
	
	return String.format("<input type='radio' name='rd' onclick='setServicePartInfo("
			           + "\""+isThreeGuarantees+"\",\""+partId+"\",\""+partCode+"\","
			           + "\""+partCname+"\",\""+partWarType+"\",\""+salePrice1+"\","
			           + "\""+partPrice+"\",\""+partNumMax+"\""
			           + ")' />");
}
function setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partWarType,salePrice1,partPrice,partNumMax){
  /**
  if(parent.$('inIframe').contentWindow.setServicePartInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partPrice);
    _hide();
  }
  **/
  if(__parent().setServicePartInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partWarType,salePrice1,partPrice,partNumMax);
    _hide();
  }
}
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function servicePartQuery(){
  __extQuery__(1);
}
//关闭
function servicePartClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：维修配件选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <input type="hidden" name="repairType" id="repairType" value="${repairType}" />
  <input type="hidden" name="ruleId" id="ruleId" value="${ruleId}" />
  <input type="hidden" name="modelId" id="modelId" value="${modelId}" />
  <input type="hidden" name="arrivalDate" id="arrivalDate" value="${arrivalDate}" />
  <input type="hidden" name="repairDateBegin" id="repairDateBegin" value="${repairDateBegin}" />
  <input type="hidden" name="mileage" id="mileage" value="${mileage}" />
  <input type="hidden" name="vin" id="vin" value="${vin}" />
  <input type="hidden" name="purchasedDate" id="purchasedDate" value="${purchasedDate}" />
  <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}" />
  <input type="hidden" name="_isThreeGuarantee" id="_isThreeGuarantee" value="${_isThreeGuarantee}"/>

  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td style="text-align:right">配件代码：</td>
    <td>
      <input id="partCode" name="partCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td style="text-align:right">配件名称：</td>
    <td>
      <input id="partCname" name="partCname" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="txtright">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="servicePartQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="servicePartClose();" value="关 闭" />
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