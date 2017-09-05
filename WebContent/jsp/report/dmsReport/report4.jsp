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
	function expotData(){
	//新分单时间
	var str ='2015-05-25 19:30:00';
	var st = str.replace(/-/g,"/");
	var date = (new Date(st)).getTime();  
	   
	var start=document.getElementById("beginTime").value;
	var end=document.getElementById("endTime").value;
	
	var startdate=(new Date(start.replace(/-/g,"/"))).getTime();
	var enddate=(new Date(end.replace(/-/g,"/"))).getTime();
	if(""==start){
		MyAlert("提示:必须选择开始结束时间!");
        return;
	}else if(""==end){
		MyAlert("提示:必须选择开始结束时间!");
        return;
	}else if (startdate>=date &&enddate<date) {
		MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
        return;
	}else if (startdate<date &&enddate>=date) {
		MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
        return;
	}else{
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData4.do";
       fm.submit();
	}
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;索赔结算清单报表<span style="color: red;">(开始结束时间必须是同时满足2015-05-25之前或者之后,否则不能查询)</span>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
		<td width="15%"><input name="claim_no" type="text" id="claim_no"  maxlength="30" class="middle_txt"/></td>
		<td width="15%"></td>
	</tr>
	<tr>
		<td width="10%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配件代码：</td>
      	<td width="15%"><input name="part_code" type="text" id="part_code"  class="middle_txt" maxlength="30"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">供应商代码：</td>
      	<td width="15%"><input name="down_part_code" type="text" id="down_part_code" class="middle_txt" maxlength="30"/></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="15%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="15%"></td>
	</tr>
	<tr>
		<td width="10%"></td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">索赔类型：</td>
		<td width="15%">
		    <script type="text/javascript">
		       genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661008,10661011,10661012,10661013,10661002,10661003,10661004,10661005');
		    </script>
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">申请状态：</td>
		<td width="15%">
		    <script type="text/javascript">
		       genSelBoxExp("status",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10791001,10791009,10791010,10791011,10791012,10791014,10791015,10791016');
		         
		    </script>
		</td>
		<td width="15%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum(1)" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
     <tr class="table_list_row1" >
		<td align="right">工时总费：<span id="a1"></span></td>
		<td align="right">材料总费：<span id="a2"></span></td>
		<td align="right">其他总费：<span id="a3"></span></td>
		<td align="right">辅料总费：<span id="a4"></span></td>
		<td align="right">补偿总费：<span id="a5"></span></td>
		<td align="right">合计总数：<span id="a6"></span></td>
		<td align="right"><font color="red">活动总费：<span id="a7"></span></font></td>
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
		//新分单时间
		var str ='2015-05-25 19:30:00';
		var st = str.replace(/-/g,"/");
		var date = (new Date(st)).getTime();  
		   
		var start=document.getElementById("beginTime").value;
		var end=document.getElementById("endTime").value;
		
		var startdate=(new Date(start.replace(/-/g,"/"))).getTime();
		var enddate=(new Date(end.replace(/-/g,"/"))).getTime();
		if(""==start){
			MyAlert("提示:必须选择开始结束时间!");
	        return;
		}else if(""==end){
			MyAlert("提示:必须选择开始结束时间!");
	        return;
		}else if (startdate>=date &&enddate<date) {
			MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
	        return;
		}else if (startdate<date &&enddate>=date) {
			MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
	        return;
		}else{
			__extQuery__(1);
		}

		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		document.getElementById("a5").innerHTML = '';
		document.getElementById("a6").innerHTML = '';
		document.getElementById("a7").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/report/dmsReport/Application/report4Sum.json",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.BALANCE_AMOUNT2 == null ? '0' : json.valueMap.BALANCE_AMOUNT2;//工时费
			document.getElementById("a2").innerHTML = json.valueMap.BALANCE_AMOUNT1 == null ? '0' : json.valueMap.BALANCE_AMOUNT1;
			document.getElementById("a3").innerHTML = json.valueMap.NETITEM_AMOUNT == null ? '0' : json.valueMap.NETITEM_AMOUNT;
			document.getElementById("a4").innerHTML = json.valueMap.ACCESSORIES_PRICE == null ? '0' : json.valueMap.ACCESSORIES_PRICE;
			document.getElementById("a5").innerHTML = json.valueMap.COMPENSATION_MONEY == null ? '0' : json.valueMap.COMPENSATION_MONEY;
			document.getElementById("a6").innerHTML = json.valueMap.COUNTALL == null ? '0' : json.valueMap.COUNTALL;
			document.getElementById("a7").innerHTML = json.valueMap.ACTIVITY_AMOUNT == null ? '0' : json.valueMap.ACTIVITY_AMOUNT;
		},'fm');
	}
	
	
	
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/report4.json?query=true";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "商家代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "月份", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
		{header: "维修日期", dataIndex: 'RO_STARTDATE', align:'center'},
		{header: "配件编码", dataIndex: 'PART_CODE', align:'center'},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "原因分析", dataIndex: 'REMARK', align:'center'},
		{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
		{header: "工时费", dataIndex: 'BALANCE_AMOUNT2', align:'center'},
		{header: "材料费", dataIndex: 'BALANCE_AMOUNT1', align:'center'},
		{header: "其他费", dataIndex: 'NETITEM_AMOUNT', align:'center'},
		{header: "辅料费", dataIndex: 'ACCESSORIES_PRICE', align:'center'},
		{header: "补偿费", dataIndex: 'COMPENSATION_MONEY', align:'center'},
		{header: "费用合计", dataIndex: 'COUNTALL', align:'center'},
		{header: "活动编号", dataIndex: 'CAM_CODE', align:'center'},
		{header: "活动费用", dataIndex: 'ACTIVITY_AMOUNT', align:'center'}
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
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
</script>
<!--页面列表 end -->
</html>