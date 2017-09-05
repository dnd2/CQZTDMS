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
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>维修配件选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/servicePartQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'PART_ID',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "配件单价", dataIndex: 'SALE_PRICE', align:'center'}
		      ];

function mySelect(value,metaDate,record){
	var isThreeGuarantees = record.data.IS_THREE_GUARANTEES==null?"":record.data.IS_THREE_GUARANTEES;
	var partId = record.data.PART_ID==null?"":record.data.PART_ID;
	var partCode = record.data.PART_CODE==null?"":record.data.PART_CODE;
	var partCname = record.data.PART_CNAME==null?"":record.data.PART_CNAME;
	var partPrice = record.data.SALE_PRICE==null?"0":record.data.SALE_PRICE;
	
	return String.format("<input type='radio' name='rd' onclick='setServiceProjectInfo("
			           + "\""+isThreeGuarantees+"\",\""+partId+"\","
			           + "\""+partCode+"\",\""+partCname+"\",\""+partPrice+"\""
			           + ")' />");
}
function setServiceProjectInfo(isThreeGuarantees,partId,partCode,partCname,partPrice){
  /**
  if(parent.$('inIframe').contentWindow.setServicePartInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partPrice);
    _hide();
  }
  **/
    var idOldPartId= document.getElementById("idOldPartId").value;
	var idOldPartCode= document.getElementById("idOldPartCode").value;
	var idOldPartName= document.getElementById("idOldPartName").value;
  if(__parent().setServicePartInfo==undefined)
	    MyAlert('调用父页面方法出现异常!');
	  else{
		  __parent().setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partPrice,idOldPartId,idOldPartCode,idOldPartName);
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
  <input type="hidden" name="modelId" id="modelId" value="${modelId}" />
  <input type="hidden" name="arrivalDate" id="arrivalDate" value="${arrivalDate}" />
  <input type="hidden" name="mileage" id="mileage" value="${mileage}" />
  <input type="hidden" name="repair" id="repair" value="${repair}" />
  <input type="hidden" name="purchasedDate" id="purchasedDate" value="${purchasedDate}" />
  <input type="hidden" name="partFareRate" id="partFareRate" value="${partFareRate}" />
  <input type="hidden" name="idOldPartId" id="idOldPartId" value="${idOldPartId}" />
  <input type="hidden" name="idOldPartCode" id="idOldPartCode" value="${idOldPartCode}" />
  <input type="hidden" name="idOldPartName" id="idOldPartName" value="${idOldPartName}" />
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