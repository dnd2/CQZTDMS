<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后报表</title>
<script type="text/javascript">

	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/ysqDetailExport.do";
       fm.submit();
	}
	function showActivity(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/activityName.jsp',800,460);
	}
	function myCheck(activity_id,activity_code,activity_name){
		
		$('activity_id').value=activity_id;
		$('activity_code').value=activity_code;
		$('activity_name').value=activity_name;
	}
	function wrapOut1(){
		$('activity_code').value="";
		$('activity_name').value="";
	}
	
	
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;预授权明细报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		
	    <td width="10%" class="table_query_2Col_label_4Letter" nowrap="true">大区：</td>
		<td width="10%" nowrap="true">
		          <select id="__large_org" name="__large_org" class="short_sel" >
						<option value="">--请选择--</option>
						<c:forEach var="org" items="${orglist}">
							<option value="${org.ORG_NAME}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
						</c:forEach>
					</select>
		</td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="20%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="10%"class="table_query_2Col_label_6Letter" nowrap="true">故障件代码：</td>
		<td width="10%" nowrap="true">
           <input name="part_code" id="part_code" class="middle_txt" >
        </td>
	</tr>
	<tr>
      <td  nowrap="true" class="table_query_2Col_label_5Letter">上报时间：</td>
		<td  nowrap="true" width="25%">
		  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10" readonly="readonly"
                 maxlength="10" group="startDate,endDate" onclick="calendar();"/>
             	 至
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
                 maxlength="10" group="startDate,endDate"/>
		</td>
		<td  nowrap="true" class="table_query_2Col_label_5Letter">审核时间：</td>
		<td  nowrap="true" width="25%">
		  <input class="short_txt" id="startAuditDate" name="startAuditDate" datatype="1,is_date,10" readonly="readonly"
                 maxlength="10" group="startAuditDate,endAuditDate" onclick="calendar();"/>
             	 至
          <input class="short_txt" id="endAuditDate" name="endAuditDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
                 maxlength="10" group="startAuditDate,endAuditDate"/>
		</td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">故障件名称：</td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">
           <input name="part_name" id="part_name" class="middle_txt" >
        </td>
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	
	function checkSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/report/dmsReport/Application/report3Sum.json",function(json){
		document.getElementById("a1").innerHTML = json.valueMap.COUNTALL == null ? '0' : json.valueMap.COUNTALL;//工时费
		document.getElementById("a2").innerHTML = json.valueMap.ACHIVENUM == null ? '0' : json.valueMap.ACHIVENUM;
		document.getElementById("a3").innerHTML = json.valueMap.NOTACHIVENUM == null ? '0' : json.valueMap.NOTACHIVENUM;
		document.getElementById("a4").innerHTML = json.valueMap.PERCENTALL == null ? '0' : json.valueMap.PERCENTALL;
		},'fm');
		__extQuery__(1);
	}
	
	
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/ysqDetailReport.json?query=query";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "服务商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
		{header: "预授权单号", dataIndex: 'YSQ_NO', align:'center'},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE_NAME', align:'center'},
		{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
		{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
		{header: "车辆用途", dataIndex: 'CAR_USE_DESC', align:'center'},
		{header: "颜色", dataIndex: 'COLOR', align:'center'},
		{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
		{header: "购车日期", dataIndex: 'INVOICE_DATE', align:'center'},
		{header: "里程", dataIndex: 'MILEAGE', align:'center'},
		{header: "故障描述", dataIndex: 'TROUBLE_REASON', align:'center'},
		{header: "原因分析及结果", dataIndex: 'TROUBLE_DESC', align:'center'},
		{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
		{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
		{header: "最大预估金额", dataIndex: 'MAX_ESTIMATE', align:'center'},
		{header: "是否回运", dataIndex: 'IS_RETURN_NAME', align:'center'},
		{header: "补偿申请费用", dataIndex: 'COM_APPLY', align:'center'},
		{header: "补偿审核费用", dataIndex: 'COM_PASS', align:'center'},
		{header: "补偿申请备注", dataIndex: 'COM_REMARK', align:'center'},
		{header: "背车申请费用", dataIndex: 'BC_APPLY', align:'center'},
		{header: "背车审核费用", dataIndex: 'BC_PASS', align:'center'},
		{header: "背车申请备注", dataIndex: 'BC_REMARK', align:'center'},
		{header: "退回次数", dataIndex: 'REPORT_NUM', align:'center'},
		{header: "审核日期", dataIndex: 'CREATE_DATE', align:'center'}
	];
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>