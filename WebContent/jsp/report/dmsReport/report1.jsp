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
		cells  : 3
	}
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData1.do";
       fm.submit();
	}
	
	function showCarModel(){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showCarType.do',800,500);
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;单车索赔金额报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr >
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">索赔类型：</td>
		<td width="15%">
			<select class="short_sel" name="claim_type">
				<option value="">--请选择--</option>
				<option value="10661001">正常维修</option>
				<option value="10661006">厂家活动</option>
				<option value="10661007">售前维修</option>
				<option value="10661009">外派服务</option>
				<option value="10661010">特殊服务</option>
				<option value="10661012">索赔急件</option>
			</select>
			
		</td>
		<td width="15%"></td>
	</tr>
	
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车型：</td>
		<td width="10%">
				<input type="text" id="cars" onclick="showCarModel()" class="middle_txt" name="cars" readonly="readonly">
		<input type="hidden" id="carModel"  name="carModel">
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true" >商家代码：</td>
		<td width="15%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">活动名称：</td>
      	<td width="15%" nowrap="true">
			<input type="text" readonly="readonly" name="activity_name" id="activity_name" onclick="showActivity();" class="middle_txt"/>
			<input type="hidden" name="activity_code" id="activity_code"/>
          	<input type="button" class="normal_btn" value="清除" onclick="wrapOut1();"/>      	
        </td>
		<td width="15%"></td>
	</tr>
	<tr >
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">供应商代码：</td>
      	<td width="15%">
      	<input name="producer_code" type="text" id="producer_code"  maxlength="50" class="middle_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">零件代码：</td>
      	<td width="15%">
      		<input name="part_code" type="text" id="part_code"  maxlength="50"  class="middle_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">作业代码：</td>
		<td width="15%">
			<input name="labour_code" type="text" id="labour_code"  maxlength="50"  class="middle_txt"/>
		</td>
		<td width="15%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum()" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
    <tr class="table_list_row1">
		<td >工时总费：<span id="a1"></span></td>
		<td >材料总费：<span id="a2"></span></td>
		<td >其他费用：<span id="a3"></span></td>
		<td >保养总费：<span id="a4"></span></td>
		<td >辅料总费：<span id="a5"></span></td>
		<td >补偿总费：<span id="a6"></span></td>
		<td ><font color="red">结算总费：<span id="a7"></span></font></td>
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
		document.getElementById("a5").innerHTML = '';
		document.getElementById("a6").innerHTML = '';
		document.getElementById("a7").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/report/dmsReport/Application/report1Sum.json",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.BALANCE_LABOUR_AMOUNT == null ? '0' : json.valueMap.BALANCE_LABOUR_AMOUNT;//工时费
			document.getElementById("a2").innerHTML = json.valueMap.BALANCE_PART_AMOUNT == null ? '0' : json.valueMap.BALANCE_PART_AMOUNT;
			document.getElementById("a3").innerHTML = json.valueMap.BALANCE_NETITEM_AMOUNT == null ? '0' : json.valueMap.BALANCE_NETITEM_AMOUNT;
			document.getElementById("a4").innerHTML = json.valueMap.FREE_M_PRICE == null ? '0' : json.valueMap.FREE_M_PRICE;
			document.getElementById("a5").innerHTML = json.valueMap.ACCESSORIES_PRICE == null ? '0' : json.valueMap.ACCESSORIES_PRICE;
			document.getElementById("a6").innerHTML = json.valueMap.COMPENSATION_MONEY == null ? '0' : json.valueMap.COMPENSATION_MONEY;
			document.getElementById("a7").innerHTML = json.valueMap.BALANCE_AMOUNT == null ? '0' : json.valueMap.BALANCE_AMOUNT;
		},'fm');
		__extQuery__(1);
	}
	
	
	
	
	
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/report1.json?query=true";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
		{header: "商家代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
		{header: "车型", dataIndex: 'MODEL', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "申报时间", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
		{header: "工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
		{header: "材料费", dataIndex: 'PART_AMOUNT', align:'center'},
		{header: "外出费", dataIndex: 'NETITEM_AMOUNT', align:'center'},
		{header: "保养费", dataIndex: 'FREE_M_PRICE', align:'center'},
		{header: "辅料费", dataIndex: 'ACCESSORIES_PRICE', align:'center'},
		{header: "补偿费", dataIndex: 'COMPENSATION_MONEY', align:'center'},
		{header: "结算金额", dataIndex: 'BALANCE_AMOUNT', align:'center'},
		{header: "服务活动编码", dataIndex: 'CAMPAIGN_CODE', align:'center'}
	];
	checkSum();
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
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	
	function showActivity(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/activityName.jsp',800,460);
	}
	function myCheck(activity_id,activity_code,activity_name){
		$('activity_code').value=activity_code;
		$('activity_name').value=activity_name;
	}
	function wrapOut1(){
		$('activity_code').value="";
		$('activity_name').value="";
	}
	
</script>
<!--页面列表 end -->
</html>