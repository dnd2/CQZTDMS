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
<title>维修类型选择</title>
<script>
function setServiceRepairType(){
  /**
  if(parent.$('inIframe').contentWindow.setServicePartInfo==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    parent.$('inIframe').contentWindow.setServicePartInfo(isThreeGuarantees,partId,partCode,partCname,partPrice);
    _hide();
  }
  **/
  var repairType = document.getElementById("repairType");//维修类型
  var repairTypeId = repairType.value;
  var repairTypeName = repairType.options[repairType.selectedIndex].text;//维修类型名称
  if(__parent().setServiceRepairType==undefined)
    MyAlert('调用父页面方法出现异常!');
  else{
    __parent().setServiceRepairType(repairTypeId,repairTypeName);
    _hide();
  }
}
//页面初始化	
function doInit(){
	//loadcalendar();
	//__extQuery__(1);
}
//关闭
function serviceRepairTypeClose(){
  parent._hide();
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：维修类型选择</div>
<form id="fm" name="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td style="text-align:center;">维修类型：
        <script type="text/javascript"> 
          genSelBoxExp("repairType",<%=Constant.REPAIR_TYPE%>,"","true","u-select","onchange=setServiceRepairType()","false",'');
        </script>
    </td>
  </tr>
  </table>
  </div>
  </div>
</form>
</div>
</body>
</html>