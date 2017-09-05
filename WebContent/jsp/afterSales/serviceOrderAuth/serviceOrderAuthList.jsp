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
<!-- 日历类 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<title>维修工单登记</title>
<script>
var myPage;

var url = "<%=contextPath%>/afterSales/ServiceOrderAuthAction/serviceOrderAuthQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
	           {header: "序号", align:'center', renderer:getIndex, width:'7%'},
	           {header: "操作", id:'action', sortable: false, dataIndex: 'AUTH_AUDIT_ID', renderer:myLink},
	           {header: "工单代码", dataIndex: 'SERVICE_ORDER_CODE', align:'center'},//, orderCol:"SERVICE_ORDER_CODE"
	           {header: "维修类型", dataIndex: 'REPAIR_TYPE_NAME', align:'center'},
	           {header: "车牌号", dataIndex: 'LICENSE_NO', align:'center'},
	           {header: "VIN", dataIndex: 'VIN', align:'center'},
	           {header: "经销商", dataIndex: 'DEALER_NAME', align:'center'},
	           {header: "预授权审核状态", dataIndex: 'AUTH_AUDIT_STATUS_NAME', align:'center'},
	           {header: "审核人", dataIndex: 'AUTH_AUDIT_BY_NAME', align:'center'},
	           {header: "审核时间", dataIndex: 'AUTH_AUDIT_DATE', align:'center'},
	           {header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
	          ];

//$(document).ready(function(){__extQuery__(1);});
//页面初始化	
function doInit(){
  //loadcalendar();
  __extQuery__(1);
}
//查询
function serviceOrderQuery(){
  __extQuery__(1);
}
function myLink(value,meta,record){
  var authAuditId = record.data.AUTH_AUDIT_ID;
  var serviceOrderId = record.data.SERVICE_ORDER_ID;
  var repairType = record.data.REPAIR_TYPE;
  var authAuditStatus = record.data.AUTH_AUDIT_STATUS;
  var str = "";
  if(authAuditStatus=="12521001"){//待审核
    str += "<a href=\"###\" onclick=\"serviceOrderAuthAudit("+authAuditId+","+serviceOrderId+","+repairType+",2); \">[授权审核]</a>";
  }
  str += "<a href=\"###\" onclick=\"serviceOrderAuthShow("+authAuditId+","+serviceOrderId+","+repairType+",1); \">[查看]</a>";
  return String.format(str);
}
//授权审核
function serviceOrderAuthAudit(authAuditId,serviceOrderId,repairType,operationType){
  document.getElementById('operationType').value = operationType;
  document.getElementById('authAuditId').value = authAuditId;
  document.getElementById('serviceOrderId').value = serviceOrderId;
  document.getElementById('repairType').value = repairType;
  var url = "<%=contextPath %>/afterSales/ServiceOrderAuthAction/serviceOrderAuthShow.do";
  var fm = document.getElementById("fm");
  fm.action = url;
  fm.submit();
}
//查看
function serviceOrderAuthShow(authAuditId,serviceOrderId,repairType,operationType){
  //document.getElementById('serviceOrderId').value = serviceOrderId;
  var url = "<%=contextPath %>/afterSales/ServiceOrderAuthAction/serviceOrderAuthShow.do?serviceOrderId="+serviceOrderId
          + "&repairType="+repairType+"&operationType="+operationType;
  window.location.href = url;
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt;索赔预授权&gt;索赔预授权列表</div>
<form id="fm" name="fm">
  <!-- 隐藏域 -->
  <input type="hidden" name="operationType" id="operationType" value=""/>
  <input type="hidden" name="authAuditId" id="authAuditId" value=""/>
  <input type="hidden" name="serviceOrderId" id="serviceOrderId" value=""/>
  <input type="hidden" name="repairType" id="repairType" value=""/>
  <input type="hidden" name="curPage" id="curPage" value="1"/>
  <div class="form-panel">
  <%-- <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>--%>
  <div class="form-body">
  <table class="table_query" border="0">
  <tr>
    <td class="right">工单号：</td>
    <td>
      <input id="serviceOrderCode" name="serviceOrderCode" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td class="right">VIN：</td>
    <td>
      <input id="vin" name="vin" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
    <td class="right">车牌号：</td>
    <td>
      <input id="licenseNo" name="licenseNo" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
  </tr>
  <tr>
    <td class="right">经销商：</td>
    <td>
      <input type="hidden" name="dealerId" id="dealerId" value=""/>
      <input type="hidden" name="dealerCode" id="dealerCode" value=""/>
      <input type="text" class="middle_txt" name="dealerName" id="dealerName" onclick="showOrgDealer('dealerCode','dealerId','true','','','','','dealerName','经销商选择')"/>
      <input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerId','dealerCode','dealerName');"/>
    </td>
    <td class="right">维修类型：</td>
    <td>
      <script type="text/javascript"> 
        genSelBoxExp("repairType",<%=Constant.REPAIR_TYPE%>,"","true","u-select","","false",'');
      </script>
    </td>
    <td class="right">预授权状态：</td>
    <td>
	  <script type="text/javascript"> 
	    genSelBoxExp("authAuditStatus",<%=Constant.AUTH_AUDIT_STATUS%>,"","true","u-select","","false",'');
	  </script>
    </td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="serviceOrderQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input type="reset" class="normal_btn" value="重 置"/> &nbsp; 
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