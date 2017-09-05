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
<title>责任供应商选择</title>
<script>
var myPage;

var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/producerInfoQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'VENDER_ID',renderer:mySelect,align:'center'},
				{header: "供应商编码", dataIndex: 'VENDER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'}
		      ];

function mySelect(value,metaDate,record){
  var id = document.getElementById("id").value;
  var venderId = record.data.VENDER_ID==null?"":record.data.VENDER_ID;
  var venderCode = record.data.VENDER_CODE==null?"":record.data.VENDER_CODE;
  var venderName = record.data.VENDER_NAME==null?"":record.data.VENDER_NAME;
  
  var claimId = document.getElementById("claimId").value;
  var partId = document.getElementById("partId").value;
  var isMainCode = document.getElementById("isMainCode").value;
  
  return String.format("<input type='radio' name='rd' onclick='setProducerInfo("
			         + "\""+id+"\",\""+venderCode+"\",\""+venderName+"\","
			         + "\""+claimId+"\",\""+partId+"\",\""+isMainCode+"\""
			         + ")' />");
}
function setProducerInfo(id,venderCode,venderName,claimId,partId,isMainCode){
  /**
  if(parent.$('inIframe').contentWindow.setServiceProjectInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServiceProjectInfo(labourId,labourCode,cnDes,labourHour,labourPrice);
    _hide();
  }
  **/
  if(__parent().setProducerInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setProducerInfo(id,venderCode,venderName,claimId,partId,isMainCode);
    _hide();
  }
}
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function producerInfoQuery(){
  __extQuery__(1);
}
//关闭
function producerInfoClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：责任供应商选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <input type="hidden" name="id" id="id" value="${id}" />
  <input type="hidden" name="claimId" id="claimId" value="${claimId}" />
  <input type="hidden" name="partId" id="partId" value="${partId}" />
  <input type="hidden" name="isMainCode" id="isMainCode" value="${isMainCode}" />
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td class="right">责任供应商编码：</td>
    <td>
      <input id="venderCode" name="venderCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td class="right">责任供应商名称：</td>
    <td>
      <input id="venderName" name="venderName" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="right">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="producerInfoQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="producerInfoClose();" value="关 闭" />
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