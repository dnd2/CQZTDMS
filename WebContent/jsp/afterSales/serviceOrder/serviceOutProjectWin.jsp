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
<title>外出项目选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceOutProjectQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'FEE_ID',renderer:mySelect,align:'center'},
				{header: "项目代码", dataIndex: 'FEE_CODE', align:'center'},
				{header: "项目名称", dataIndex: 'FEE_NAME', align:'center'}
		      ];

function mySelect(value,metaDate,record){
	var feeId = record.data.FEE_ID==null?"":record.data.FEE_ID;
	var feeCode = record.data.FEE_CODE==null?"":record.data.FEE_CODE;
	var feeName = record.data.FEE_NAME==null?"":record.data.FEE_NAME;
	
	return String.format("<input type='radio' name='rd' onclick='setServiceOutProjectInfo("
			           + "\""+feeId+"\",\""+feeCode+"\",\""+feeName+"\""
			           + ")' />");
}
function setServiceOutProjectInfo(feeId,feeCode,feeName){
  /**
  if(parent.$('inIframe').contentWindow.setServicePartInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partPrice);
    _hide();
  }
  **/
  if(__parent().setServiceOutProjectInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setServiceOutProjectInfo(feeId,feeCode,feeName);
    _hide();
  }
}
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function serviceOutProjectQuery(){
  __extQuery__(1);
}
//关闭
function serviceOutProjectClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：维修配件选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td style="text-align:right">项目代码：</td>
    <td>
      <input id="feeCode" name="feeCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td style="text-align:right">项目名称：</td>
    <td>
      <input id="feeName" name="feeName" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="txtright">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="serviceOutProjectQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="serviceOutProjectClose();" value="关 闭" />
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