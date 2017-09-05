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
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
var Options = {
		cells  : 2
	}
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData5.do";
       fm.submit();
	}
	function showCarModel(){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showCarType.do',800,500);
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;导出索赔单明细报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<!-- 查询条件 -->
	<tr>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">索赔类型：</td>
		<td width="15%">
		    <script type="text/javascript">
		       genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661008,10661010,10661011,10661012,10661013,10661002,10661003,10661004,10661005');
		    </script>
		</td>
	</tr>
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车型：</td>
		<td width="10%">
		<input type="text" id="cars" onclick="showCarModel()" class="middle_txt" name="cars" readonly="readonly">
		<input type="hidden" id="carModel"  name="carModel">
		</td>
		<td width="15%" class="table_query_2Col_label_6Letter" nowrap="true">
		   经销商名称：
		</td>
		<td width="15%" class="table_query_2Col_label_6Letter" nowrap="true">
		  <input type="text" name="dealer_name" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		</td>
		<td width="15%"class="table_query_2Col_label_6Letter" nowrap="true">
		经销商代码：
		</td>
		<td width="15%"class="table_query_2Col_label_6Letter" nowrap="true">
		   <input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" value="${dealerCode }" readonly="readonly"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			<input class="middle_txt" id="dealerId"  name="dealerId" value="${dealerId}" type="hidden" />
			<input class="middle_txt" id="poseId"  name="poseId" value="${poseId}" type="hidden"/>
		</td>
	</tr>
	<tr>
	   <td width="15%" class="table_query_2Col_label_6Letter" nowrap="true">供应商代码:</td>
	   <td >
	     <input type="text" name="producer_code" id="producer_code"  class="middle_txt"/>
	   </td>
	    <td width="15%" class="table_query_2Col_label_5Letter" nowrap="true">零件代码:</td>
	   <td >
	     <input type="text" name="part_code" id="part_code"  class="middle_txt"/>
	   </td>
	   <td width="15%" class="table_query_2Col_label_6Letter" nowrap="true">
		   大区：
		</td>
		<td width="15%"  >
	     <input type="text" name="org_name" id="org_name"  class="middle_txt"/>
		</td>
	</tr>
	 <tr>
	 <td  class="table_query_2Col_label_6Letter" >换下配件名称：</td>
	 <td>
	    <input type="text" name="part_name" id="part_name" class="middle_txt"/>
	 </td>
		<td class="table_query_2Col_label_4Letter" nowrap="true">VIN：</td>
		<td width="15%">
		 <textarea name="vin"  rows="3" cols="18"></textarea>
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="query();" >
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
	var url = "<%=contextPath%>/report/dmsReport/Application/report5.json?query=true";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "区域", dataIndex: 'ORG_NAME', align:'center'},
		{header: "索赔申请单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
		{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
		{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
		{header: "工单开始时间", dataIndex: 'RO_STARTDATE', align:'center'},
		{header: "车辆生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
		{header: "配件结算数量", dataIndex: 'PARTS_COUNT', align:'center'},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
		{header: "车辆购车日期", dataIndex: 'PURCHASED_DATE', align:'center'},
		{header: "行驶里程", dataIndex: 'IN_MILEAGE', align:'center'},
		{header: "故障描述", dataIndex: 'TROUBLE_DESC', align:'center'},
		{header: "故障原因", dataIndex: 'TROUBLE_REASON', align:'center'},
		{header: "维修措施", dataIndex: 'REPAIR_METHOD', align:'center'},
		{header: "换下配件名称", dataIndex: 'DOWN_PART_NAME', align:'center'},
		{header: "换上配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
		{header: "活动编号", dataIndex: 'CAM_CODE', align:'center'}
	];
	function myLink(value,meta,record){
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var roNo = record.data.RO_NO;
		var ID = record.data.ID;
		var claimNo = record.data.CLAIM_NO;
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
	}
	//清空经销商框
	function clearInput(){
		$('dealerCode').value="";
		$('dealerId').value="";
	}
	function query(){
		var beginTime =document.getElementById("beginTime").value;
		if(beginTime==""){
          MyAlert("必须填写开始时间!");
          return;
		}
		__extQuery__(1);
	}
</script>
<!--页面列表 end -->
</html>