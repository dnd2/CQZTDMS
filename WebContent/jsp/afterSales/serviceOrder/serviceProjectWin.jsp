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
<title>维修项目选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceProjectQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'LABOUR_ID',renderer:mySelect,align:'center'},
				{header: "车型组名称", dataIndex: 'WRGROUP_NAME', align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'CN_DES', align:'center'},
				{header: "工时数", dataIndex: 'LABOUR_QUOTIETY', align:'center'},
				{header: "工时定额", dataIndex: 'LABOUR_HOUR', align:'center'},
				//{header: "附加工时数", dataIndex: 'engineNo', align:'center'},
				{header: "工时单价", dataIndex: 'LABOUR_PRICE', align:'center'}
				//{header: "预授权", dataIndex: 'fore', align:'center',renderer:getItemValue}
				//{header: "特殊工时", dataIndex: 'approvalLevel', align:'center'}
		      ];

function mySelect(value,metaDate,record){
  var labourId = record.data.LABOUR_ID==null?"":record.data.LABOUR_ID;
  var labourCode = record.data.LABOUR_CODE==null?"":record.data.LABOUR_CODE;
  var cnDes = record.data.CN_DES==null?"":record.data.CN_DES;
  var labourHour = record.data.LABOUR_HOUR==null?"":record.data.LABOUR_HOUR;
  var labourPrice = record.data.LABOUR_PRICE==null?"":record.data.LABOUR_PRICE;
  
  return String.format("<input type='radio' name='rd' onclick='setServiceProjectInfo("
			         + "\""+labourId+"\",\""+labourCode+"\","
			         + "\""+cnDes+"\",\""+labourHour+"\",\""+labourPrice+"\""
			         + ")' />");
}
function setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice){
  /**
  if(parent.$('inIframe').contentWindow.setServiceProjectInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice);
    _hide();
  }
  **/
  if(__parent().setServiceProjectInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice);
    _hide();
  }
}
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function serviceProjectQuery(){
  __extQuery__(1);
}
//关闭
function serviceProjectClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：维修项目选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <input type="hidden" name="wrgroupId" id="wrgroupId" value="${wrgroupId}" />
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td class="right">工时代码：</td>
    <td>
      <input id="labourCode" name="labourCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td class="right">工时名称：</td>
    <td>
      <input id="cnDes" name="cnDes" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="right">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="serviceProjectQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="serviceProjectClose();" value="关 闭" />
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