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

var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceOrderQuery.json";
	  
//设置表格标题
var title = null;

//设置列名属性
var columns = [
	           {header: "序号", align:'center', renderer:getIndex, width:'7%'},
	           {header: "操作", id:'action', sortable: false, dataIndex: 'SERVICE_ORDER_ID', renderer:myLink},
	           {header: "工单代码", dataIndex: 'SERVICE_ORDER_CODE', align:'center'},//, orderCol:"SERVICE_ORDER_CODE"
	           {header: "维修类型", dataIndex: 'REPAIR_TYPE_NAME', align:'center'},
	           {header: "车牌号", dataIndex: 'LICENSE_NO', align:'center'},
	           {header: "VIN", dataIndex: 'VIN', align:'center'},
	           {header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
	           {header: "车主", dataIndex: 'CTM_NAME', align:'center'},
	           {header: "工单创建时间", dataIndex: 'CREATE_DATE', align:'center'},
	           {header: "进站里程数", dataIndex: 'MILEAGE', align:'center'},
	           {header: "工单状态", dataIndex: 'STATUS_NAME', align:'center'}
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
  var serviceOrderId = record.data.SERVICE_ORDER_ID;
  var repairType = record.data.REPAIR_TYPE;
  var isCanClaim = record.data.IS_CAN_CLAIM;
  var activityType = record.data.ACTIVITY_TYPE;
  var status = record.data.STATUS;
  var str = "";
  if(status=="12501001"||status=="12501005"){//未上报或者审核驳回
    str += "<a href=\"###\" onclick=\"serviceOrderSub("+serviceOrderId+","+repairType+","+isCanClaim+","+activityType+"); \">[上报]</a>"
         + "<a href=\"###\" onclick=\"serviceOrderUpdate("+serviceOrderId+","+repairType+",2); \">[修改]</a>";
  }else if(status=="12501002"||status=="12501004"){
    str += "<a href=\"###\" onclick=\"serviceOrderSettlement("+serviceOrderId+"); \">[结算]</a>";
  }
  str += "<a href=\"###\" onclick=\"serviceOrderShow("+serviceOrderId+","+repairType+",1); \">[查看]</a>";
       //+ "<a href=\"###\" onclick=\"fmUpdate("+value+"); \">[条码查看]</a>" ;
  return String.format(str);
}
//新增
function serviceOrderAdd(){
  /**
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderAdd.do";
  var fm = document.getElementById("fm");
  fm.action = url;
  fm.submit();
  **/
  serviceRepairTypeWin();
}
//维修类型选择
function serviceRepairTypeWin(){
  var repairType = document.getElementById("repairType").value;//维修类型
  var url = "<%=contextPath%>/afterSales/ServiceOrderAction/serviceRepairTypeWin.do?repairType="+repairType;
  OpenHtmlWindow(url,500,200);
  return;
}
//新增跳转
function setServiceRepairType(repairType){
  //var repairType = document.getElementById("repairType");//维修类型
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderAdd.do?repairType="+repairType;
  window.location.href = url;
}
//上报
function serviceOrderSub(serviceOrderId,repairType,isCanClaim,activityType) {
  //document.getElementById('serviceOrderId').value = serviceOrderId;
  //document.getElementById('repairTypeId').value = repairTypeId;
  MyConfirm("确定要上报吗？", serviceOrderSubSave, ["?serviceOrderId="+serviceOrderId+
		                                        "&repairType="+repairType+
		                                        "&isCanClaim="+isCanClaim+
		                                        "&activityType="+activityType
		                                        ]);
}
//上报保存
function serviceOrderSubSave(value) {
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderSubSave.json"+value;
  makeNomalFormCall(url,showResult,'fm');
}
//修改
function serviceOrderUpdate(serviceOrderId,repairType,operationType){
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderShow.do?serviceOrderId="+serviceOrderId
          + "&repairType="+repairType+"&operationType="+operationType;
  window.location.href = url;
}
//结算
function serviceOrderSettlement(serviceOrderId) {
  document.getElementById('serviceOrderId').value = serviceOrderId;
  MyConfirm("确定要结算吗？", serviceOrderSettlementSave, []);
}
//结算保存
function serviceOrderSettlementSave() {
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderSettlementSave.json";
  makeNomalFormCall(url,showResult,'fm');
}
//显示结果
function showResult(obj) {
  if (obj.code == 'succ') {
    MyAlert(obj.msg);
    __extQuery__(1);
  }else if(obj.code == 'fail') {
    MyAlert(obj.msg);
  }
}
//查看
function serviceOrderShow(serviceOrderId,repairType,operationType){
  //document.getElementById('serviceOrderId').value = serviceOrderId;
  var url = "<%=contextPath %>/afterSales/ServiceOrderAction/serviceOrderShow.do?serviceOrderId="+serviceOrderId
          + "&repairType="+repairType+"&operationType="+operationType;
  window.location.href = url;
}
//编辑
function serviceOrderEdit(){
	
}
//废弃
function serviceOrderDel(){
	
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt;维修登记 &gt;维修工单登记</div>
<form id="fm" name="fm">
  <!-- 隐藏域 -->
  <input type="hidden" name="serviceOrderId" id="serviceOrderId" value=""/>
  <input type="hidden" name="repairTypeId" id="repairTypeId" value=""/>
  <input type="hidden" name="repairTypeName" id="repairTypeName" value=""/>
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
    <td class="right">车牌号：</td>
    <td>
      <input id="licenseNo" name="licenseNo" maxlength="20" value=""  type="text" class="middle_txt" />
    </td>
    <td class="right">VIN：</td>
    <td>
      <input id="vin" name="vin" maxlength="30" value="" type="text" class="middle_txt" />
    </td>
  </tr>
  <tr>
    <td class="right">维修类型：</td>
    <td>
      <script type="text/javascript"> 
        genSelBoxExp("repairType",<%=Constant.REPAIR_TYPE%>,"","true","u-select","","false",'');
      </script>
    </td>
  <td class="right">工单状态：</td>
    <td>
	  <script type="text/javascript"> 
	    genSelBoxExp("status",<%=Constant.SERVICE_ORDER_STATUS%>,"","true","u-select","","false",'');
	  </script>
    </td>
    <td class="right">工单创建时间：</td>
    <td>
      <input id="createDateBegin" name="createDateBegin" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	     至
	  <input id="createDateEnd" name="createDateEnd" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
    </td>
  </tr>
  <tr>
    <td colspan="6" style="text-align: center">
      <input name="queryBtn" type="button" class="normal_btn" onclick="serviceOrderQuery();" value="查 询" id="queryBtn" /> &nbsp; 
	  <input type="reset" class="normal_btn" value="重 置"/> &nbsp; 
	  <input name="button2" type="button" class="normal_btn" onclick="serviceOrderAdd();" value="新 增" />
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