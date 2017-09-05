<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%-- <jsp:include page="${contextPath}/common/jsp_head_new.jsp" /> --%>
<%@ page import="com.infodms.dms.exception.BizException" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.common.ErrorCodeConstant" %>
<%@ page import="com.infodms.dms.common.RightMessageConstant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionData.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/HashMap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/FormValidation.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/default.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/DialogManager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateFormatter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/msgformat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/textBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/kindeditor/kindeditor.js" />
<script type="text/javascript" >
    var globalContextPath ='<%=(request.getContextPath())%>';
    var g_webAppName = '<%=(request.getContextPath())%>';   //全局webcontent应用名，避免js使用"<%=request.getContextPath()%>"
    var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";   //全局图片路径
	function autoAlertException(){
		var str = null;
		<%
		Throwable exception = ActionContext.getContext().getException();
			if(exception!=null&& (exception instanceof BizException)){
				BizException biz = (BizException)exception;
				if(biz.getType()!=ErrorCodeConstant.SELF_DEAL_WITH_CODE){
		%>
					if(<%=biz.getErrCode()!=null %>){
						str = strAppend(str,"错误代码"+'<%=biz.getErrCode()%>'+":</br>");
					}
					if(<%=biz.getMessage()!=null %>){
						str = strAppend(str,'<%=biz.getMessage() %>');
					}
					MyAlert(str);
					return false;
			<%
			}}
			%>
				return true;
			
	}
</script>
<head>
<% 
	String contextPath = request.getContextPath(); 
	List boardList = (List)request.getAttribute("boardList");	
	List boardListDay = (List)request.getAttribute("boardListDay");
	List newBoardList = (List)request.getAttribute("newBoardList");
%>
<title>销售日报表</title>
<script type="text/javascript">
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	
	function chkForm() {
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DayReport/saleDayReportByParam.do";
		$('fm').submit();
	}
	
	function initTable() {
		/* document.getElementById('scrollDiv').style.width =  document.getElementById('dataTable').offsetWidth+16; */
	}
	
	function displaySwitch() {
		var queryConditionTable = document.getElementById('queryConditionTable');
		if(queryConditionTable.style.display == "none") {
			queryConditionTable.style.display = "inline";
			document.getElementById('queryBtn').value = "隐藏查询";
		} else {
			queryConditionTable.style.display = "none";
			document.getElementById('queryBtn').value = "显示查询";
		}
	}
	
	
</script>
<style type="text/css">

	body {
		font-size: 12px;
	}
	
	.table_list_row2{
		background-color:#F7F7F7;
		border: 1px solid #DAE0EE;
		white-space:    nowrap;
		font-size: 13px;
	}

	/*固定行头样式*/
	.scrollRowThead 
	{
	     position: relative; 
	     left: expression(this.parentElement.parentElement.parentElement.scrollLeft);
	     z-index:0;
	}
	
	/*固定表头样式*/
	.scrollColThead {
	     position: relative; 
	     top: expression(this.parentElement.parentElement.parentElement.scrollTop);
	     z-index:2;
	}
	
	/*行列交叉的地方*/
	.scrollCR {
	     z-index:3;
	} 
	 
	/*div外框*/
	.scrollDiv {
		height:500px;
		clear: both;
		border: 1px solid #EEEEEE;
		overflow: scroll;
		white-space:nowrap;
		width: 100%;
	}
	
	/*行头居中*/
	.scrollColThead td,.scrollColThead th
	{
	     text-align: center ;
	}
	
	/*行头列头背景*/
	.scrollRowThead,.scrollColThead td,.scrollColThead th
	{
		background-color:EEEEEE;
	}
	
	/*表格的线*/
	.scrolltable
	{
		border-bottom:1px solid #CCCCCC; 
		border-right:1px solid #CCCCCC; 
		border-collapse:separate;
		empty-cells:show;
	}
	
	/*单元格的线等*/
	.scrollTable th
	{
		 border-left:1px solid #CCCCCC;
		 border-top:1px solid #CCCCCC;
	     border-collapse:separate;
	     padding:5px;
	     empty-cells:show;
	     text-align: center;
	     
	     background-color:#DAE0EE;
		 font-weight:bold;
		 color:#416C9B;
		 
		 white-space:nowrap;
		 
		 /*font-weight:bold;
		 white-space:nowrap;*/
	}
	
	.scrolltable td {
	 	 padding:5px;
	     empty-cells:show;
	     text-align: center; 
	} 
	
	.scrollTr td{
		 border-left:1px solid #CCCCCC;
		 border-top:1px solid #CCCCCC;
	     border-collapse:separate;
	     padding:5px;
	     empty-cells:show;
	 	 text-align: center;
	 	 white-space:nowrap; 
	}
	
	.scrollTr th {
		 white-space:nowrap; 
	}
	
</style>
</head>

<body onload="initTable()">
<div class="navigation" style="width: 100%"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>销售日报表</strong>
	
	<div id="controlDiv" style="float: right;" align="right">
		<table border="0" align="center" class="table_list">
			<tr>
				<td>
					<input type="button" id="queryBtn" class="cssbutton" onclick="displaySwitch()" value="显示查询" />
					<input name="button2" type=button class="cssbutton" onClick="refresh();" value="刷新">
					<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
				</td>
			</tr>
		</table>
		<br/>
	</div>
</div>	
<form method="post" name="fm" id="fm" style="width: 100%;">


<table id="queryConditionTable" class="table_query" align="center" style="display: none;">

	 <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>

	<tr>
        <td>&nbsp;</td>
		<td width="30%" align="left" nowrap="nowrap">
			<label>选择大&nbsp;&nbsp;&nbsp;区:</label>
			<input type="text" id="orgCode" style="width:150px;height:16px;" name="orgCode" value="${orgCode}" size="15" class="middle_txt" readonly="readonly" />
			<input name="orgBtn" id="orgBtn"  class="mini_btn" type="button" onclick="showOrg('orgCode','orgId' ,'true', 'true');" value="..."/>
			<input type="hidden" id="orgId" name="orgId" value="${orgId}" readonly="readonly">
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('orgId');"/>
        </td>
		<td width="30%" align="left" nowrap="nowrap">
			<label>选择省份:</label>
			<input type="text" id="regionCode" style="width:150px;height: 16px;" name="regionCode" value="${regionCode}" size="15" class="middle_txt" readonly="readonly" />
			<input name="regionBtn" id="regionBtn"  class="mini_btn" type="button" onclick="showRegion111('regionCode','regionId' ,'true');chkTxt('regionCode');" value="..."/>
			<input type="hidden" id="regionId" name="regionId" value="${regionId }" readonly="readonly">
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('regionCode');clrTxt('regionId')"/>
		</td>
		<td>&nbsp;</td>
	</tr>
	
	<tr>
	    <td>&nbsp;</td>
	    <td align="left" nowrap="nowrap" width="30%">
	    	<label>选择物料组:</label>
	      	<input type="text" class="middle_txt" style="width:150px;height: 16px;" name="groupCode" size="15"  value="${groupCode}" id="groupCode" readonly="readonly" />
			<input name="groupBtn" type="button" class="mini_btn" onclick="showMaterialGroupByReport('groupCode','groupId','true',2,'');chkTxt('groupCode');" value="..." />
			<input type="hidden" id="groupId" name="groupId" value="${groupId}" readonly="readonly">
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('groupCode');clrTxt('groupId')"/>
	    </td>
		<td width="30%" align="left" nowrap="nowrap">
			<label>业务范围:</label>
			<select name="areaId" id="areaId" class="short_sel" style="width:150px;">
				<option value="-1">-请选择-</option>
				<c:forEach items="${areaList}" var="list">
					<option value="${list.AREA_ID}" <c:if test="${list.AREA_ID == area_id}">selected="selected"</c:if>>
						<c:out value="${list.AREA_NAME}"/>
					</option>
				</c:forEach>
			</select><input type="hidden" name="area" id="area"/>
		</td>
		<td>&nbsp;</td>
    </tr> 
    
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
    
    <tr>
    	<td colspan="4" align="center"><input type="button" class="cssbutton" name="submitBtn" value="查询" onclick="chkForm()"/></td>
    </tr>
    
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
    
   
    
    
</table>

<br/>
<div>
	<table border="0" align="center" class="table_query">
	 <tr>
    	<td width="20%">统计截止日期:${endTime}</td>
    	<td width="20%">统计起始日期:${startTime}</td>
    	<td width="20%">本月已执行天数:${excluted}天</td>
    	<td width="20%">本月时间进度:<fmt:formatNumber type="percent" value="${schedule}"></fmt:formatNumber></td>
    </tr>
	</table>
</div>

<div id="scrollDiv" class="scrollDiv"> 

<table id="dataTable" border="0" cellpadding="3" cellspacing="0" align="center" class="scrollTable">

	<tr class="scrollColThead">
		<th colspan="2" class="scrollRowThead scrollCR"><font style="font-size: 17px">汇总</font></th>
		<th colspan="7" class="scrollRowThead scrollCR"><font style="font-size: 17px">零售</font></th>
		<th colspan="4" class="scrollRowThead scrollCR"><font style="font-size: 17px">经销商库存</font></th>
		<th colspan="7" class="scrollRowThead scrollCR"><font style="font-size: 17px">启票</font></th>
	</tr>
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">销售分区</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">服务中心</font></th>
		
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">今日</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">本月累计</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月目标</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月完成率</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月同比</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">年累计</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">年同比</font></th>
		
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">在途</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">在库</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">审核未打单</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">可供销售</font></th>
		
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">今日</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月累计</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月目标</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月完成率</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">月同比</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">年累计</font></th>
		<th class="scrollRowThead scrollCR"><font style="font-size: 13px">年同比</font></th>
	</tr>
	
	<%  java.text.DecimalFormat   df = new   java.text.DecimalFormat("#"); %>
	
	<!-- 省份合计 -->
	<% 
		Map<String, BigDecimal> regionTotal = new LinkedHashMap<String, BigDecimal>();
	
		//零售
		regionTotal.put("ACT_MONTH_F", BigDecimal.ZERO);
		regionTotal.put("ACT_YEAR_F", BigDecimal.ZERO);
		regionTotal.put("ACT_DAY", BigDecimal.ZERO);
		regionTotal.put("ACT_PLAN", BigDecimal.ZERO);
		regionTotal.put("ACT_MONTH", BigDecimal.ZERO);
		regionTotal.put("ACT_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		regionTotal.put("ACT_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		regionTotal.put("ACT_YEAR", BigDecimal.ZERO);
		regionTotal.put("ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO); //年度累计同比
		
		//服务中心可供销售车辆 
		regionTotal.put("DLVRY_AMOUT", BigDecimal.ZERO);
		regionTotal.put("EXISTS_AMOUNT", BigDecimal.ZERO);
		regionTotal.put("RESERVER_AMOUNT", BigDecimal.ZERO);
		regionTotal.put("AVAILABLE_AMOUNT", BigDecimal.ZERO); //可供销售车辆
		
		//启票
		regionTotal.put("BILL_MONTH_F", BigDecimal.ZERO);
		regionTotal.put("BILL_YEAR_F", BigDecimal.ZERO);
		regionTotal.put("BILL_DAY", BigDecimal.ZERO);
		regionTotal.put("BILL_MONTH", BigDecimal.ZERO);
		regionTotal.put("BILL_PLAN", BigDecimal.ZERO);
		regionTotal.put("BILL_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		regionTotal.put("BILL_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		regionTotal.put("BILL_YEAR", BigDecimal.ZERO);
		regionTotal.put("BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO);
		request.setAttribute("regionTotal", regionTotal);
	%>
	
	<!-- 大区合计 -->
	<% 
		Map<String, BigDecimal> orgTotal = new LinkedHashMap<String, BigDecimal>();
		//零售
		orgTotal.put("ACT_MONTH_F", BigDecimal.ZERO);
		orgTotal.put("ACT_YEAR_F", BigDecimal.ZERO);
		orgTotal.put("ACT_DAY", BigDecimal.ZERO);
		orgTotal.put("ACT_PLAN", BigDecimal.ZERO);
		orgTotal.put("ACT_MONTH", BigDecimal.ZERO);
		orgTotal.put("ACT_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		orgTotal.put("ACT_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		orgTotal.put("ACT_YEAR", BigDecimal.ZERO);
		orgTotal.put("ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO); //年度累计同比
		
		//服务中心可供销售车辆 
		orgTotal.put("DLVRY_AMOUT", BigDecimal.ZERO);
		orgTotal.put("EXISTS_AMOUNT", BigDecimal.ZERO);
		orgTotal.put("RESERVER_AMOUNT", BigDecimal.ZERO);
		orgTotal.put("AVAILABLE_AMOUNT", BigDecimal.ZERO); //可供销售车辆
		
		//启票
		orgTotal.put("BILL_MONTH_F", BigDecimal.ZERO);
		orgTotal.put("BILL_YEAR_F", BigDecimal.ZERO);
		orgTotal.put("BILL_DAY", BigDecimal.ZERO);
		orgTotal.put("BILL_MONTH", BigDecimal.ZERO);
		orgTotal.put("BILL_PLAN", BigDecimal.ZERO);
		orgTotal.put("BILL_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		orgTotal.put("BILL_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		orgTotal.put("BILL_YEAR", BigDecimal.ZERO);
		orgTotal.put("BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO);
		request.setAttribute("orgTotal", orgTotal);
	%>
	
	<!-- 总合计 -->
	<% 
		Map<String, BigDecimal> total = new LinkedHashMap<String, BigDecimal>();
		//零售
		total.put("ACT_MONTH_F", BigDecimal.ZERO);
		total.put("ACT_YEAR_F", BigDecimal.ZERO);
		total.put("ACT_DAY", BigDecimal.ZERO);
		total.put("ACT_PLAN", BigDecimal.ZERO);
		total.put("ACT_MONTH", BigDecimal.ZERO);
		total.put("ACT_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		total.put("ACT_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		total.put("ACT_YEAR", BigDecimal.ZERO);
		total.put("ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO); //年度累计同比
		
		//服务中心可供销售车辆 
		total.put("DLVRY_AMOUT", BigDecimal.ZERO);
		total.put("EXISTS_AMOUNT", BigDecimal.ZERO);
		total.put("RESERVER_AMOUNT", BigDecimal.ZERO);
		total.put("AVAILABLE_AMOUNT", BigDecimal.ZERO); //可供销售车辆
		
		//启票
		total.put("BILL_MONTH_F", BigDecimal.ZERO);
		total.put("BILL_YEAR_F", BigDecimal.ZERO);
		total.put("BILL_DAY", BigDecimal.ZERO);
		total.put("BILL_MONTH", BigDecimal.ZERO);
		total.put("BILL_PLAN", BigDecimal.ZERO);
		total.put("BILL_MONTH_COMPLETION_RATE", BigDecimal.ZERO); //月度完成率
		total.put("BILL_MONTH_YEAR_ON_YEAR", BigDecimal.ZERO); //月度同比
		total.put("BILL_YEAR", BigDecimal.ZERO);
		total.put("BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP", BigDecimal.ZERO);
		request.setAttribute("total", total);
	%>
	
	<c:forEach items="${ps}" var="item" varStatus="status">
		
		<c:choose>
			<c:when test="${status.first == true || ps[status.index-1].ORG_NAME == item.ORG_NAME && ps[status.index-1].REGION_NAME == item.REGION_NAME}">
			
				<c:set target="${regionTotal}" property="ACT_MONTH_F" value="${regionTotal.ACT_MONTH_F + item.ACT_MONTH_F}" />
				<c:set target="${regionTotal}" property="ACT_YEAR_F" value="${regionTotal.ACT_YEAR_F + item.ACT_YEAR_F}" />
				<c:set target="${regionTotal}" property="ACT_DAY" value="${regionTotal.ACT_DAY + item.ACT_DAY}" />
				<c:set target="${regionTotal}" property="ACT_PLAN" value="${regionTotal.ACT_PLAN + item.ACT_PLAN}" />
				<c:set target="${regionTotal}" property="ACT_MONTH" value="${regionTotal.ACT_MONTH + item.ACT_MONTH}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${regionTotal.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${regionTotal.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_YEAR" value="${regionTotal.ACT_YEAR + item.ACT_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${regionTotal.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.AATU}" />
				<c:set target="${regionTotal}" property="DLVRY_AMOUT" value="${regionTotal.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
				<c:set target="${regionTotal}" property="EXISTS_AMOUNT" value="${regionTotal.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
				<c:set target="${regionTotal}" property="RESERVER_AMOUNT" value="${regionTotal.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
				<c:set target="${regionTotal}" property="AVAILABLE_AMOUNT" value="${regionTotal.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_F" value="${regionTotal.BILL_MONTH_F + item.BILL_MONTH_F}" />
				<c:set target="${regionTotal}" property="BILL_YEAR_F" value="${regionTotal.BILL_YEAR_F + item.BILL_YEAR_F}" />
				<c:set target="${regionTotal}" property="BILL_DAY" value="${regionTotal.BILL_DAY + item.BILL_DAY}" />
				<c:set target="${regionTotal}" property="BILL_MONTH" value="${regionTotal.BILL_MONTH + item.BILL_MONTH}" />
				<c:set target="${regionTotal}" property="BILL_PLAN" value="${regionTotal.BILL_PLAN + item.BILL_PLAN}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${regionTotal.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${regionTotal.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_YEAR" value="${regionTotal.BILL_YEAR + item.BILL_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${regionTotal.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BATU}" />
				
				
				<c:set target="${orgTotal}" property="ACT_MONTH_F" value="${orgTotal.ACT_MONTH_F + item.ACT_MONTH_F}" />
				<c:set target="${orgTotal}" property="ACT_YEAR_F" value="${orgTotal.ACT_YEAR_F + item.ACT_YEAR_F}" />
				<c:set target="${orgTotal}" property="ACT_DAY" value="${orgTotal.ACT_DAY + item.ACT_DAY}" />
				<c:set target="${orgTotal}" property="ACT_PLAN" value="${orgTotal.ACT_PLAN + item.ACT_PLAN}" />
				<c:set target="${orgTotal}" property="ACT_MONTH" value="${orgTotal.ACT_MONTH + item.ACT_MONTH}" />
				<c:set target="${orgTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${orgTotal.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${orgTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${orgTotal.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${orgTotal}" property="ACT_YEAR" value="${orgTotal.ACT_YEAR + item.ACT_YEAR}" />
				<c:set target="${orgTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${orgTotal.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.AATU}" />
				<c:set target="${orgTotal}" property="DLVRY_AMOUT" value="${orgTotal.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
				<c:set target="${orgTotal}" property="EXISTS_AMOUNT" value="${orgTotal.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
				<c:set target="${orgTotal}" property="RESERVER_AMOUNT" value="${orgTotal.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
				<c:set target="${orgTotal}" property="AVAILABLE_AMOUNT" value="${orgTotal.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_F" value="${orgTotal.BILL_MONTH_F + item.BILL_MONTH_F}" />
				<c:set target="${orgTotal}" property="BILL_YEAR_F" value="${orgTotal.BILL_YEAR_F + item.BILL_YEAR_F}" />
				<c:set target="${orgTotal}" property="BILL_DAY" value="${orgTotal.BILL_DAY + item.BILL_DAY}" />
				<c:set target="${orgTotal}" property="BILL_MONTH" value="${orgTotal.BILL_MONTH + item.BILL_MONTH}" />
				<c:set target="${orgTotal}" property="BILL_PLAN" value="${orgTotal.BILL_PLAN + item.BILL_PLAN}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${orgTotal.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${orgTotal.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${orgTotal}" property="BILL_YEAR" value="${orgTotal.BILL_YEAR + item.BILL_YEAR}" />
				<c:set target="${orgTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${orgTotal.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BATU}" />
				
				<c:set target="${total}" property="ACT_MONTH_F" value="${total.ACT_MONTH_F + item.ACT_MONTH_F}" />
				<c:set target="${total}" property="ACT_YEAR_F" value="${total.ACT_YEAR_F + item.ACT_YEAR_F}" />
				<c:set target="${total}" property="ACT_DAY" value="${total.ACT_DAY + item.ACT_DAY}" />
				<c:set target="${total}" property="ACT_PLAN" value="${total.ACT_PLAN + item.ACT_PLAN}" />
				<c:set target="${total}" property="ACT_MONTH" value="${total.ACT_MONTH + item.ACT_MONTH}" />
				<c:set target="${total}" property="ACT_MONTH_COMPLETION_RATE" value="${total.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${total}" property="ACT_MONTH_YEAR_ON_YEAR" value="${total.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${total}" property="ACT_YEAR" value="${total.ACT_YEAR + item.ACT_YEAR}" />
				<c:set target="${total}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.AATU}" />
				<c:set target="${total}" property="DLVRY_AMOUT" value="${total.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
				<c:set target="${total}" property="EXISTS_AMOUNT" value="${total.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
				<c:set target="${total}" property="RESERVER_AMOUNT" value="${total.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
				<c:set target="${total}" property="AVAILABLE_AMOUNT" value="${total.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
				<c:set target="${total}" property="BILL_MONTH_F" value="${total.BILL_MONTH_F + item.BILL_MONTH_F}" />
				<c:set target="${total}" property="BILL_YEAR_F" value="${total.BILL_YEAR_F + item.BILL_YEAR_F}" />
				<c:set target="${total}" property="BILL_DAY" value="${total.BILL_DAY + item.BILL_DAY}" />
				<c:set target="${total}" property="BILL_MONTH" value="${total.BILL_MONTH + item.BILL_MONTH}" />
				<c:set target="${total}" property="BILL_PLAN" value="${total.BILL_PLAN + item.BILL_PLAN}" />
				<c:set target="${total}" property="BILL_MONTH_COMPLETION_RATE" value="${total.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${total}" property="BILL_MONTH_YEAR_ON_YEAR" value="${total.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${total}" property="BILL_YEAR" value="${total.BILL_YEAR + item.BILL_YEAR}" />
				<c:set target="${total}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BATU}" />
				
				<tr class="scrollTr">
					<td>
						${item.REGION_NAME}
					</td>
					<td>
						${item.DEALER_SHORTNAME}
					</td>
					
					<td>
						<c:if test="${item.ACT_DAY != 0}">
							${item.ACT_DAY}
						</c:if>
						<c:if test="${item.ACT_DAY == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH != 0}">
							${item.ACT_MONTH}
						</c:if>
						<c:if test="${item.ACT_MONTH == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_PLAN != 0}">
							${item.ACT_PLAN}
						</c:if>
						<c:if test="${item.ACT_PLAN == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH_COMPLETION_RATE != 0}">
							<fmt:formatNumber type="percent" value="${item.ACT_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.ACT_MONTH_COMPLETION_RATE == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.ACT_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_YEAR != 0}">
							${item.ACT_YEAR}
						</c:if>
						<c:if test="${item.ACT_YEAR == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.AATU-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.AATU-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.AATU-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					
					<td>
						<c:if test="${item.DLVRY_AMOUT != 0}">
							${item.DLVRY_AMOUT}
						</c:if>
						<c:if test="${item.DLVRY_AMOUT == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.EXISTS_AMOUNT != 0}">
							${item.EXISTS_AMOUNT}
						</c:if>
						<c:if test="${item.EXISTS_AMOUNT == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.RESERVER_AMOUNT != 0}">
							${item.RESERVER_AMOUNT}
						</c:if>
						<c:if test="${item.RESERVER_AMOUNT == 0}">
							&nbsp;
						</c:if>		
					</td>
					<td>
						<c:if test="${item.AVAILABLE_AMOUNT != 0}">
							${item.AVAILABLE_AMOUNT}
						</c:if>
						<c:if test="${item.AVAILABLE_AMOUNT == 0}">
							&nbsp;
						</c:if>
					</td>
					
					<td>
						<c:if test="${item.BILL_DAY != 0 }">
							${item.BILL_DAY}
						</c:if>	
						<c:if test="${item.BILL_DAY == 0 }">
							&nbsp;
						</c:if>	
					</td>
					<td>
						<c:if test="${item.BILL_MONTH != 0}">
							${item.BILL_MONTH}
						</c:if>
						<c:if test="${item.BILL_MONTH == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_PLAN != 0}">
							${item.BILL_PLAN}
						</c:if>
						<c:if test="${item.BILL_PLAN == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_MONTH_COMPLETION_RATE != 0}">
							<fmt:formatNumber type="percent" value="${item.BILL_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BILL_MONTH_COMPLETION_RATE == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.BILL_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_YEAR != 0}">
							${item.BILL_YEAR}
						</c:if>
						<c:if test="${item.BILL_YEAR == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BATU-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.BATU-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BATU-1 == 0}">
							&nbsp;
						</c:if>
					</td>
				</tr>
				
				<c:choose>
					<c:when test="${status.last == true}">
					
						<tr class="scrollTr" style="font-weight: bold;">
							<td>${ps[status.index-1].REGION_NAME}</td>
							<td>合计</td>
							
							<td>${regionTotal.ACT_DAY}</td>
							<td>${regionTotal.ACT_MONTH}</td>
							<td>${regionTotal.ACT_PLAN}</td>
							<td>
								<c:if test="${regionTotal.ACT_PLAN != 0}">
									<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</td>
							<td>
								<c:if test="${regionTotal.ACT_MONTH_F != 0}">
									<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</td>
							<td>${regionTotal.ACT_YEAR}</td>
							<td>
								<c:if test="${regionTotal.ACT_YEAR_F != 0}">
									<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</td>
							
							<td>${regionTotal.DLVRY_AMOUT}</td>
							<td>${regionTotal.EXISTS_AMOUNT}</td>
							<td>${regionTotal.RESERVER_AMOUNT}</td>
							<td>${regionTotal.AVAILABLE_AMOUNT}</td>
							
							<td>${regionTotal.BILL_DAY}</td>
							<td>${regionTotal.BILL_MONTH}</td>
							<td>${regionTotal.BILL_PLAN}</td>
							<td>
								<c:if test="${regionTotal.BILL_PLAN != 0}">
									<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</td>
							<td>
								<c:if test="${regionTotal.BILL_MONTH_F != 0}">
									<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</td>
							<td>${regionTotal.BILL_YEAR}</td>
							<td>
								<c:if test="${regionTotal.BILL_YEAR_F != 0}">
									<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</td>
						</tr>
					
						<tr class="table_list_row2">
							<th>${ps[status.index-1].ORG_NAME}</th>
							<th>合计</th>
							
							<th>${orgTotal.ACT_DAY}</th>
							<th>${orgTotal.ACT_MONTH}</th>
							<th>${orgTotal.ACT_PLAN}</th>
							<th>
								<c:if test="${orgTotal.ACT_PLAN != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.ACT_MONTH_F != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.ACT_YEAR}</th>
							<th>
								<c:if test="${orgTotal.ACT_YEAR_F != 0}">
									<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
							
							<th>${orgTotal.DLVRY_AMOUT}</th>
							<th>${orgTotal.EXISTS_AMOUNT}</th>
							<th>${orgTotal.RESERVER_AMOUNT}</th>
							<th>${orgTotal.AVAILABLE_AMOUNT}</th>
							
							<th>${orgTotal.BILL_DAY}</th>
							<th>${orgTotal.BILL_MONTH}</th>
							<th>${orgTotal.BILL_PLAN}</th>
							<th>
								<c:if test="${orgTotal.BILL_PLAN != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.BILL_MONTH_F != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.BILL_YEAR}</th>
							<th>
								<c:if test="${orgTotal.BILL_YEAR_F != 0}">
									<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
						</tr>
					
						<tr class="table_list_row2">
							<th colspan="2">总计</th>
							
							<th>${total.ACT_DAY}</th>
							<th>${total.ACT_MONTH}</th>
							<th>${total.ACT_PLAN}</th>
							<th>
								<c:if test="${total.ACT_PLAN != 0}">
									<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${total.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${total.ACT_MONTH_F != 0}">
									<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${total.ACT_YEAR}</th>
							<th>
								<c:if test="${total.ACT_YEAR_F != 0}">
									<%= df.format(total.get("ACT_YEAR").doubleValue()*100 / total.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
							
							<th>${total.DLVRY_AMOUT}</th>
							<th>${total.EXISTS_AMOUNT}</th>
							<th>${total.RESERVER_AMOUNT}</th>
							<th>${total.AVAILABLE_AMOUNT}</th>
							
							<th>${total.BILL_DAY}</th>
							<th>${total.BILL_MONTH}</th>
							<th>${total.BILL_PLAN}</th>
							<th>
								<c:if test="${total.BILL_PLAN != 0}">
									<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${total.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${total.BILL_MONTH_F != 0}">
									<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${total.BILL_YEAR}</th>
							<th>
								<c:if test="${total.BILL_YEAR_F != 0}">
									<%= df.format(total.get("BILL_YEAR").doubleValue()*100 / total.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
						</tr>
					</c:when>
				</c:choose>
			</c:when>
			
			<c:when test="${ps[status.index-1].REGION_NAME != item.REGION_NAME}">
				<tr class="scrollTr" style="font-weight: bold;">
					<td>
						${ps[status.index-1].REGION_NAME}
					</td>
					<td>合计</td>
					
					<td>${regionTotal.ACT_DAY}</td>
					<td>${regionTotal.ACT_MONTH}</td>
					<td>${regionTotal.ACT_PLAN}</td>
					<td>
						<c:if test="${regionTotal.ACT_PLAN != 0}">
							<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_PLAN == 0 }">
							0.00%
						</c:if>
					</td>
					<td>
						<c:if test="${regionTotal.ACT_MONTH_F != 0}">
							<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_MONTH_F == 0}">
							0.00%
						</c:if>
					</td>
					<td>${regionTotal.ACT_YEAR}</td>
					<td>
						<c:if test="${regionTotal.ACT_YEAR_F != 0}">
							<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_YEAR_F == 0}">
							0.00%
						</c:if>
					</td>
					
					<td>${regionTotal.DLVRY_AMOUT}</td>
					<td>${regionTotal.EXISTS_AMOUNT}</td>
					<td>${regionTotal.RESERVER_AMOUNT}</td>
					<td>${regionTotal.AVAILABLE_AMOUNT}</td>
					
					<td>${regionTotal.BILL_DAY}</td>
					<td>${regionTotal.BILL_MONTH}</td>
					<td>${regionTotal.BILL_PLAN}</td>
					<td>
						<c:if test="${regionTotal.BILL_PLAN != 0}">
							<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_PLAN == 0}">
							0.00%
						</c:if>
					</td>
					<td>
						<c:if test="${regionTotal.BILL_MONTH_F != 0}">
							<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_MONTH_F == 0}">
							0.00%
						</c:if>
					</td>
					<td>${regionTotal.BILL_YEAR}</td>
					<td>
						<c:if test="${regionTotal.BILL_YEAR_F != 0}">
							<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_YEAR_F == 0}">
							0.00%
						</c:if>
					</td>
				</tr>
				
				<!-- 初始化省份 -->
				<c:set target="${regionTotal}" property="ACT_MONTH_F" value="${item.ACT_MONTH_F}" />
				<c:set target="${regionTotal}" property="ACT_YEAR_F" value="${item.ACT_YEAR_F}" />
				<c:set target="${regionTotal}" property="ACT_DAY" value="${item.ACT_DAY}" />
				<c:set target="${regionTotal}" property="ACT_PLAN" value="${item.ACT_PLAN}" />
				<c:set target="${regionTotal}" property="ACT_MONTH" value="${item.ACT_MONTH}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_YEAR" value="${item.ACT_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.AATU}" />
				<c:set target="${regionTotal}" property="DLVRY_AMOUT" value="${item.DLVRY_AMOUT}" />
				<c:set target="${regionTotal}" property="EXISTS_AMOUNT" value="${item.EXISTS_AMOUNT}" />
				<c:set target="${regionTotal}" property="RESERVER_AMOUNT" value="${item.RESERVER_AMOUNT}" />
				<c:set target="${regionTotal}" property="AVAILABLE_AMOUNT" value="${item.AVAILABLE_AMOUNT}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_F" value="${item.BILL_MONTH_F}" />
				<c:set target="${regionTotal}" property="BILL_YEAR_F" value="${item.BILL_YEAR_F}" />
				<c:set target="${regionTotal}" property="BILL_DAY" value="${item.BILL_DAY}" />
				<c:set target="${regionTotal}" property="BILL_MONTH" value="${item.BILL_MONTH}" />
				<c:set target="${regionTotal}" property="BILL_PLAN" value="${item.BILL_PLAN}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_YEAR" value="${item.BILL_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.BATU}" />
				
				<c:choose>
					<c:when test="${ps[status.index-1].ORG_NAME != item.ORG_NAME}">
						<tr class="table_list_row2">
							<th>
								${ps[status.index-1].ORG_NAME}
							</th>
							<th>合计</th>
							
							<th>${orgTotal.ACT_DAY}</th>
							<th>${orgTotal.ACT_MONTH}</th>
							<th>${orgTotal.ACT_PLAN}</th>
							<th>
								<c:if test="${orgTotal.ACT_PLAN != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.ACT_MONTH_F != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.ACT_YEAR}</th>
							<th>
								<c:if test="${orgTotal.ACT_YEAR_F != 0}">
									<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
							
							<th>${orgTotal.DLVRY_AMOUT}</th>
							<th>${orgTotal.EXISTS_AMOUNT}</th>
							<th>${orgTotal.RESERVER_AMOUNT}</th>
							<th>${orgTotal.AVAILABLE_AMOUNT}</th>
							
							<th>${orgTotal.BILL_DAY}</th>
							<th>${orgTotal.BILL_MONTH}</th>
							<th>${orgTotal.BILL_PLAN}</th>
							<th>
								<c:if test="${orgTotal.BILL_PLAN != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.BILL_MONTH_F != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.BILL_YEAR}</th>
							<th>
								<c:if test="${orgTotal.BILL_YEAR_F != 0}">
									<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
						</tr>
						
						<!-- 初始化大区 -->
						<c:set target="${orgTotal}" property="ACT_MONTH_F" value="${item.ACT_MONTH_F}" />
						<c:set target="${orgTotal}" property="ACT_YEAR_F" value="${item.ACT_YEAR_F}" />
						<c:set target="${orgTotal}" property="ACT_DAY" value="${item.ACT_DAY}" />
						<c:set target="${orgTotal}" property="ACT_PLAN" value="${item.ACT_PLAN}" />
						<c:set target="${orgTotal}" property="ACT_MONTH" value="${item.ACT_MONTH}" />
						<c:set target="${orgTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${item.ACT_MONTH_COMPLETION_RATE}" />
						<c:set target="${orgTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${item.ACT_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${orgTotal}" property="ACT_YEAR" value="${item.ACT_YEAR}" />
						<c:set target="${orgTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						<c:set target="${orgTotal}" property="DLVRY_AMOUT" value="${item.DLVRY_AMOUT}" />
						<c:set target="${orgTotal}" property="EXISTS_AMOUNT" value="${item.EXISTS_AMOUNT}" />
						<c:set target="${orgTotal}" property="RESERVER_AMOUNT" value="${item.RESERVER_AMOUNT}" />
						<c:set target="${orgTotal}" property="AVAILABLE_AMOUNT" value="${item.AVAILABLE_AMOUNT}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_F" value="${item.BILL_MONTH_F}" />
						<c:set target="${orgTotal}" property="BILL_YEAR_F" value="${item.BILL_YEAR_F}" />
						<c:set target="${orgTotal}" property="BILL_DAY" value="${item.BILL_DAY}" />
						<c:set target="${orgTotal}" property="BILL_MONTH" value="${item.BILL_MONTH}" />
						<c:set target="${orgTotal}" property="BILL_PLAN" value="${item.BILL_PLAN}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${item.BILL_MONTH_COMPLETION_RATE}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${item.BILL_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${orgTotal}" property="BILL_YEAR" value="${item.BILL_YEAR}" />
						<c:set target="${orgTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						
						<!-- 累加总计 -->
						<c:set target="${total}" property="ACT_MONTH_F" value="${total.ACT_MONTH_F + item.ACT_MONTH_F}" />
						<c:set target="${total}" property="ACT_YEAR_F" value="${total.ACT_YEAR_F + item.ACT_YEAR_F}" />
						<c:set target="${total}" property="ACT_DAY" value="${total.ACT_DAY + item.ACT_DAY}" />
						<c:set target="${total}" property="ACT_PLAN" value="${total.ACT_PLAN + item.ACT_PLAN}" />
						<c:set target="${total}" property="ACT_MONTH" value="${total.ACT_MONTH + item.ACT_MONTH}" />
						<c:set target="${total}" property="ACT_MONTH_COMPLETION_RATE" value="${total.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
						<c:set target="${total}" property="ACT_MONTH_YEAR_ON_YEAR" value="${total.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${total}" property="ACT_YEAR" value="${total.ACT_YEAR + item.ACT_YEAR}" />
						<c:set target="${total}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						<c:set target="${total}" property="DLVRY_AMOUT" value="${total.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
						<c:set target="${total}" property="EXISTS_AMOUNT" value="${total.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
						<c:set target="${total}" property="RESERVER_AMOUNT" value="${total.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
						<c:set target="${total}" property="AVAILABLE_AMOUNT" value="${total.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
						<c:set target="${total}" property="BILL_MONTH_F" value="${total.BILL_MONTH_F + item.BILL_MONTH_F}" />
						<c:set target="${total}" property="BILL_YEAR_F" value="${total.BILL_YEAR_F + item.BILL_YEAR_F}" />
						<c:set target="${total}" property="BILL_DAY" value="${total.BILL_DAY + item.BILL_DAY}" />
						<c:set target="${total}" property="BILL_MONTH" value="${total.BILL_MONTH + item.BILL_MONTH}" />
						<c:set target="${total}" property="BILL_PLAN" value="${total.BILL_PLAN + item.BILL_PLAN}" />
						<c:set target="${total}" property="BILL_MONTH_COMPLETION_RATE" value="${total.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
						<c:set target="${total}" property="BILL_MONTH_YEAR_ON_YEAR" value="${total.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${total}" property="BILL_YEAR" value="${total.BILL_YEAR + item.BILL_YEAR}" />
						<c:set target="${total}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						
						<tr class="scrollTr">
							<td>
								${item.REGION_NAME}
							</td>
							<td>
								${item.DEALER_SHORTNAME}
							</td>
							
							<td>
								<c:if test="${item.ACT_DAY != 0}">
									${item.ACT_DAY}
								</c:if>
								<c:if test="${item.ACT_DAY == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH != 0}">
									${item.ACT_MONTH}
								</c:if>
								<c:if test="${item.ACT_MONTH == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_PLAN != 0}">
									${item.ACT_PLAN}
								</c:if>
								<c:if test="${item.ACT_PLAN == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH_COMPLETION_RATE != 0}">
									<fmt:formatNumber type="percent" value="${item.ACT_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.ACT_MONTH_COMPLETION_RATE == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.ACT_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_YEAR != 0}">
									${item.ACT_YEAR}
								</c:if>
								<c:if test="${item.ACT_YEAR == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.AATU-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.AATU-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.AATU-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							
							<td>
								<c:if test="${item.DLVRY_AMOUT != 0}">
									${item.DLVRY_AMOUT}
								</c:if>
								<c:if test="${item.DLVRY_AMOUT == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.EXISTS_AMOUNT != 0}">
									${item.EXISTS_AMOUNT}
								</c:if>
								<c:if test="${item.EXISTS_AMOUNT == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.RESERVER_AMOUNT != 0}">
									${item.RESERVER_AMOUNT}
								</c:if>	
								<c:if test="${item.RESERVER_AMOUNT == 0}">
									&nbsp;
								</c:if>	
							</td>
							<td>
								<c:if test="${item.AVAILABLE_AMOUNT != 0}">
									${item.AVAILABLE_AMOUNT}
								</c:if>
								<c:if test="${item.AVAILABLE_AMOUNT == 0}">
									&nbsp;
								</c:if>
							</td>
							
							<td>
								<c:if test="${item.BILL_DAY != 0}">
									${item.BILL_DAY}
								</c:if>	
								<c:if test="${item.BILL_DAY == 0}">
									&nbsp;
								</c:if>	
							</td>
							<td>
								<c:if test="${item.BILL_MONTH != 0}">
									${item.BILL_MONTH}
								</c:if>
								<c:if test="${item.BILL_MONTH == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_PLAN != 0}">
									${item.BILL_PLAN}
								</c:if>
								<c:if test="${item.BILL_PLAN == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_MONTH_COMPLETION_RATE != 0}">
									<fmt:formatNumber type="percent" value="${item.BILL_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BILL_MONTH_COMPLETION_RATE == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.BILL_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_YEAR != 0}">
									${item.BILL_YEAR}
								</c:if>
								<c:if test="${item.BILL_YEAR == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BATU-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.BATU-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BATU-1 == 0}">
									&nbsp;
								</c:if>
							</td>
						</tr>
						<!-- 大区和省份都不同,并且是最后一条记录时统计合计和总计 -->
						<c:if test="${status.last == true}">
							<tr class="scrollTr" style="font-weight: bold;">
								<td>${item.REGION_NAME}</td>
								<td>合计</td>
								
								<td>${regionTotal.ACT_DAY}</td>
								<td>${regionTotal.ACT_MONTH}</td>
								<td>${regionTotal.ACT_PLAN}</td>
								<td>
									<c:if test="${regionTotal.ACT_PLAN != 0}">
										<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</td>
								<td>
									<c:if test="${regionTotal.ACT_MONTH_F != 0}">
										<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</td>
								<td>${regionTotal.ACT_YEAR}</td>
								<td>
									<c:if test="${regionTotal.ACT_YEAR_F != 0}">
										<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</td>
								
								<td>${regionTotal.DLVRY_AMOUT}</td>
								<td>${regionTotal.EXISTS_AMOUNT}</td>
								<td>${regionTotal.RESERVER_AMOUNT}</td>
								<td>${regionTotal.AVAILABLE_AMOUNT}</td>
								
								<td>${regionTotal.BILL_DAY}</td>
								<td>${regionTotal.BILL_MONTH}</td>
								<td>${regionTotal.BILL_PLAN}</td>
								<td>
									<c:if test="${regionTotal.BILL_PLAN != 0}">
										<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</td>
								<td>
									<c:if test="${regionTotal.BILL_MONTH_F != 0}">
										<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</td>
								<td>${regionTotal.BILL_YEAR}</td>
								<td>
									<c:if test="${regionTotal.BILL_YEAR_F != 0}">
										<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</td>
							</tr>
						
							<tr class="table_list_row2">
								<th>${item.ORG_NAME}</th>
								<th>合计</th>
								
								<th>${orgTotal.ACT_DAY}</th>
								<th>${orgTotal.ACT_MONTH}</th>
								<th>${orgTotal.ACT_PLAN}</th>
								<th>
									<c:if test="${orgTotal.ACT_PLAN != 0}">
										<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${orgTotal.ACT_MONTH_F != 0}">
										<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${orgTotal.ACT_YEAR}</th>
								<th>
									<c:if test="${orgTotal.ACT_YEAR_F != 0}">
										<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
								
								<th>${orgTotal.DLVRY_AMOUT}</th>
								<th>${orgTotal.EXISTS_AMOUNT}</th>
								<th>${orgTotal.RESERVER_AMOUNT}</th>
								<th>${orgTotal.AVAILABLE_AMOUNT}</th>
								
								<th>${orgTotal.BILL_DAY}</th>
								<th>${orgTotal.BILL_MONTH}</th>
								<th>${orgTotal.BILL_PLAN}</th>
								<th>
									<c:if test="${orgTotal.BILL_PLAN != 0}">
										<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${orgTotal.BILL_MONTH_F != 0}">
										<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${orgTotal.BILL_YEAR}</th>
								<th>
									<c:if test="${orgTotal.BILL_YEAR_F != 0}">
										<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
							</tr>
						
							<tr class="table_list_row2">
								<th colspan="2">总计</th>
								
								<th>${total.ACT_DAY}</th>
								<th>${total.ACT_MONTH}</th>
								<th>${total.ACT_PLAN}</th>
								<th>
									<c:if test="${total.ACT_PLAN != 0}">
										<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${total.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${total.ACT_MONTH_F != 0}">
										<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${total.ACT_YEAR}</th>
								<th>
									<c:if test="${total.ACT_YEAR_F != 0}">
										<%= df.format(total.get("ACT_YEAR").doubleValue()*100 / total.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
								
								<th>${total.DLVRY_AMOUT}</th>
								<th>${total.EXISTS_AMOUNT}</th>
								<th>${total.RESERVER_AMOUNT}</th>
								<th>${total.AVAILABLE_AMOUNT}</th>
								
								<th>${total.BILL_DAY}</th>
								<th>${total.BILL_MONTH}</th>
								<th>${total.BILL_PLAN}</th>
								<th>
									<c:if test="${total.BILL_PLAN != 0}">
										<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${total.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${total.BILL_MONTH_F != 0}">
										<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${total.BILL_YEAR}</th>
								<th>
									<c:if test="${total.BILL_YEAR_F != 0}">
										<%= df.format(total.get("BILL_YEAR").doubleValue()*100 / total.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
							</tr>							
						</c:if>
						
					</c:when>
					
					<c:otherwise>
						<tr class="scrollTr">
							<td>
								${item.REGION_NAME}
							</td>
							<td>
								${item.DEALER_SHORTNAME}
							</td>
							
							<td>
								<c:if test="${item.ACT_DAY != 0}">
									${item.ACT_DAY}
								</c:if>
								<c:if test="${item.ACT_DAY == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH != 0}">
									${item.ACT_MONTH}
								</c:if>
								<c:if test="${item.ACT_MONTH == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_PLAN != 0}">
									${item.ACT_PLAN}
								</c:if>
								<c:if test="${item.ACT_PLAN == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH_COMPLETION_RATE != 0}">
									<fmt:formatNumber type="percent" value="${item.ACT_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.ACT_MONTH_COMPLETION_RATE == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.ACT_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.ACT_YEAR != 0}">
									${item.ACT_YEAR}
								</c:if>
								<c:if test="${item.ACT_YEAR == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.AATU-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.AATU-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.AATU-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							
							<td>
								<c:if test="${item.DLVRY_AMOUT != 0}">
									${item.DLVRY_AMOUT}
								</c:if>
								<c:if test="${item.DLVRY_AMOUT == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.EXISTS_AMOUNT != 0}">
									${item.EXISTS_AMOUNT}
								</c:if>
								<c:if test="${item.EXISTS_AMOUNT == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.RESERVER_AMOUNT != 0}">
									${item.RESERVER_AMOUNT}
								</c:if>	
								<c:if test="${item.RESERVER_AMOUNT == 0}">
									&nbsp;
								</c:if>	
							</td>
							<td>
								<c:if test="${item.AVAILABLE_AMOUNT != 0}">
									${item.AVAILABLE_AMOUNT}
								</c:if>
								<c:if test="${item.AVAILABLE_AMOUNT == 0}">
									&nbsp;
								</c:if>
							</td>
							
							<td>
								<c:if test="${item.BILL_DAY != 0}">
									${item.BILL_DAY}
								</c:if>	
								<c:if test="${item.BILL_DAY == 0}">
									&nbsp;
								</c:if>	
							</td>
							<td>
								<c:if test="${item.BILL_MONTH != 0}">
									${item.BILL_MONTH}
								</c:if>
								<c:if test="${item.BILL_MONTH == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_PLAN != 0}">
									${item.BILL_PLAN}
								</c:if>
								<c:if test="${item.BILL_PLAN == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_MONTH_COMPLETION_RATE != 0}">
									<fmt:formatNumber type="percent" value="${item.BILL_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BILL_MONTH_COMPLETION_RATE == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.BILL_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BILL_YEAR != 0}">
									${item.BILL_YEAR}
								</c:if>
								<c:if test="${item.BILL_YEAR == 0}">
									&nbsp;
								</c:if>
							</td>
							<td>
								<c:if test="${item.BATU-1 != 0}">
									<fmt:formatNumber type="percent" value="${item.BATU-1}" pattern="#0%"></fmt:formatNumber>
								</c:if>
								<c:if test="${item.BATU-1 == 0}">
									&nbsp;
								</c:if>
							</td>
						</tr>
						
						<!-- 累加大区 -->
						<c:set target="${orgTotal}" property="ACT_MONTH_F" value="${orgTotal.ACT_MONTH_F + item.ACT_MONTH_F}" />
						<c:set target="${orgTotal}" property="ACT_YEAR_F" value="${orgTotal.ACT_YEAR_F + item.ACT_YEAR_F}" />
						<c:set target="${orgTotal}" property="ACT_DAY" value="${orgTotal.ACT_DAY + item.ACT_DAY}" />
						<c:set target="${orgTotal}" property="ACT_PLAN" value="${orgTotal.ACT_PLAN + item.ACT_PLAN}" />
						<c:set target="${orgTotal}" property="ACT_MONTH" value="${orgTotal.ACT_MONTH + item.ACT_MONTH}" />
						<c:set target="${orgTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${orgTotal.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
						<c:set target="${orgTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${orgTotal.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${orgTotal}" property="ACT_YEAR" value="${orgTotal.ACT_YEAR + item.ACT_YEAR}" />
						<c:set target="${orgTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${orgTotal.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						<c:set target="${orgTotal}" property="DLVRY_AMOUT" value="${orgTotal.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
						<c:set target="${orgTotal}" property="EXISTS_AMOUNT" value="${orgTotal.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
						<c:set target="${orgTotal}" property="RESERVER_AMOUNT" value="${orgTotal.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
						<c:set target="${orgTotal}" property="AVAILABLE_AMOUNT" value="${orgTotal.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_F" value="${orgTotal.BILL_MONTH_F + item.BILL_MONTH_F}" />
						<c:set target="${orgTotal}" property="BILL_YEAR_F" value="${orgTotal.BILL_YEAR_F + item.BILL_YEAR_F}" />
						<c:set target="${orgTotal}" property="BILL_DAY" value="${orgTotal.BILL_DAY + item.BILL_DAY}" />
						<c:set target="${orgTotal}" property="BILL_MONTH" value="${orgTotal.BILL_MONTH + item.BILL_MONTH}" />
						<c:set target="${orgTotal}" property="BILL_PLAN" value="${orgTotal.BILL_PLAN + item.BILL_PLAN}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${orgTotal.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
						<c:set target="${orgTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${orgTotal.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${orgTotal}" property="BILL_YEAR" value="${orgTotal.BILL_YEAR + item.BILL_YEAR}" />
						<c:set target="${orgTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${orgTotal.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						
						<!-- 累加总计 -->
						<c:set target="${total}" property="ACT_MONTH_F" value="${total.ACT_MONTH_F + item.ACT_MONTH_F}" />
						<c:set target="${total}" property="ACT_YEAR_F" value="${total.ACT_YEAR_F + item.ACT_YEAR_F}" />
						<c:set target="${total}" property="ACT_DAY" value="${total.ACT_DAY + item.ACT_DAY}" />
						<c:set target="${total}" property="ACT_PLAN" value="${total.ACT_PLAN + item.ACT_PLAN}" />
						<c:set target="${total}" property="ACT_MONTH" value="${total.ACT_MONTH + item.ACT_MONTH}" />
						<c:set target="${total}" property="ACT_MONTH_COMPLETION_RATE" value="${total.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
						<c:set target="${total}" property="ACT_MONTH_YEAR_ON_YEAR" value="${total.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${total}" property="ACT_YEAR" value="${total.ACT_YEAR + item.ACT_YEAR}" />
						<c:set target="${total}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						<c:set target="${total}" property="DLVRY_AMOUT" value="${total.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
						<c:set target="${total}" property="EXISTS_AMOUNT" value="${total.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
						<c:set target="${total}" property="RESERVER_AMOUNT" value="${total.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
						<c:set target="${total}" property="AVAILABLE_AMOUNT" value="${total.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
						<c:set target="${total}" property="BILL_MONTH_F" value="${total.BILL_MONTH_F + item.BILL_MONTH_F}" />
						<c:set target="${total}" property="BILL_YEAR_F" value="${total.BILL_YEAR_F + item.BILL_YEAR_F}" />
						<c:set target="${total}" property="BILL_DAY" value="${total.BILL_DAY + item.BILL_DAY}" />
						<c:set target="${total}" property="BILL_MONTH" value="${total.BILL_MONTH + item.BILL_MONTH}" />
						<c:set target="${total}" property="BILL_PLAN" value="${total.BILL_PLAN + item.BILL_PLAN}" />
						<c:set target="${total}" property="BILL_MONTH_COMPLETION_RATE" value="${total.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
						<c:set target="${total}" property="BILL_MONTH_YEAR_ON_YEAR" value="${total.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
						<c:set target="${total}" property="BILL_YEAR" value="${total.BILL_YEAR + item.BILL_YEAR}" />
						<c:set target="${total}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						
						<c:if test="${status.last == true}">
						
							<tr class="scrollTr" style="font-weight: bold;">
								<td>${item.REGION_NAME}</td>
								<%-- <th>${ps[status.index-1].USER_NAME}</th> --%>
								<td>合计</td>
								
								<td>${regionTotal.ACT_DAY}</td>
								<td>${regionTotal.ACT_MONTH}</td>
								<td>${regionTotal.ACT_PLAN}</td>
								<td>
									<c:if test="${regionTotal.ACT_PLAN != 0}">
										<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</td>
								<td>
									<c:if test="${regionTotal.ACT_MONTH_F != 0}">
										<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</td>
								<td>${regionTotal.ACT_YEAR}</td>
								<td>
									<c:if test="${regionTotal.ACT_YEAR_F != 0}">
										<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</td>
								
								<td>${regionTotal.DLVRY_AMOUT}</td>
								<td>${regionTotal.EXISTS_AMOUNT}</td>
								<td>${regionTotal.RESERVER_AMOUNT}</td>
								<td>${regionTotal.AVAILABLE_AMOUNT}</td>
								
								<td>${regionTotal.BILL_DAY}</td>
								<td>${regionTotal.BILL_MONTH}</td>
								<td>${regionTotal.BILL_PLAN}</td>
								<td>
									<c:if test="${regionTotal.BILL_PLAN != 0}">
										<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</td>
								<td>
									<c:if test="${regionTotal.BILL_MONTH_F != 0}">
										<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</td>
								<td>${regionTotal.BILL_YEAR}</td>
								<td>
									<c:if test="${regionTotal.BILL_YEAR_F != 0}">
										<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${regionTotal.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</td>
							</tr>
						
							<tr class="table_list_row2">
								<th>${item.ORG_NAME}</th>
								<th>合计</th>
								
								<th>${orgTotal.ACT_DAY}</th>
								<th>${orgTotal.ACT_MONTH}</th>
								<th>${orgTotal.ACT_PLAN}</th>
								<th>
									<c:if test="${orgTotal.ACT_PLAN != 0}">
										<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${orgTotal.ACT_MONTH_F != 0}">
										<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${orgTotal.ACT_YEAR}</th>
								<th>
									<c:if test="${orgTotal.ACT_YEAR_F != 0}">
										<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
								
								<th>${orgTotal.DLVRY_AMOUT}</th>
								<th>${orgTotal.EXISTS_AMOUNT}</th>
								<th>${orgTotal.RESERVER_AMOUNT}</th>
								<th>${orgTotal.AVAILABLE_AMOUNT}</th>
								
								<th>${orgTotal.BILL_DAY}</th>
								<th>${orgTotal.BILL_MONTH}</th>
								<th>${orgTotal.BILL_PLAN}</th>
								<th>
									<c:if test="${orgTotal.BILL_PLAN != 0}">
										<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${orgTotal.BILL_MONTH_F != 0}">
										<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${orgTotal.BILL_YEAR}</th>
								<th>
									<c:if test="${orgTotal.BILL_YEAR_F != 0}">
										<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${orgTotal.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
							</tr>
							
							<tr class="table_list_row2">
								<th colspan="2">总计</th>
								
								<th>${total.ACT_DAY}</th>
								<th>${total.ACT_MONTH}</th>
								<th>${total.ACT_PLAN}</th>
								<th>
									<c:if test="${total.ACT_PLAN != 0}">
										<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${total.ACT_PLAN == 0 }">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${total.ACT_MONTH_F != 0}">
										<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.ACT_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${total.ACT_YEAR}</th>
								<th>
									<c:if test="${total.ACT_YEAR_F != 0}">
										<%= df.format(total.get("ACT_YEAR").doubleValue()*100 / total.get("ACT_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.ACT_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
								
								<th>${total.DLVRY_AMOUT}</th>
								<th>${total.EXISTS_AMOUNT}</th>
								<th>${total.RESERVER_AMOUNT}</th>
								<th>${total.AVAILABLE_AMOUNT}</th>
								
								<th>${total.BILL_DAY}</th>
								<th>${total.BILL_MONTH}</th>
								<th>${total.BILL_PLAN}</th>
								<th>	
									<c:if test="${total.BILL_PLAN != 0}">
										<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_PLAN").doubleValue()) %>%
									</c:if>
									<c:if test="${total.BILL_PLAN == 0}">
										0.00%
									</c:if>
								</th>
								<th>
									<c:if test="${total.BILL_MONTH_F != 0}">
										<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_MONTH_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.BILL_MONTH_F == 0}">
										0.00%
									</c:if>
								</th>
								<th>${total.BILL_YEAR}</th>
								<th>
									<c:if test="${total.BILL_YEAR_F != 0}">
										<%= df.format(total.get("BILL_YEAR").doubleValue()*100 / total.get("BILL_YEAR_F").doubleValue() -100) %>%
									</c:if>
									<c:if test="${total.BILL_YEAR_F == 0}">
										0.00%
									</c:if>
								</th>
							</tr>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:when>
			
			<c:when test="${ps[status.index-1].REGION_NAME == item.REGION_NAME && ps[status.index-1].ORG_NAME != item.ORG_NAME}">
				
				<!-- 省份合计 -->
				<tr class="scrollTr" style="font-weight: bold;">
					
					<td>
						${ps[status.index-1].REGION_NAME}
					</td>
					<td>合计</td>
					
					<td>${regionTotal.ACT_DAY}</td>
					<td>${regionTotal.ACT_MONTH}</td>
					<td>${regionTotal.ACT_PLAN}</td>
					<td>
						<c:if test="${regionTotal.ACT_PLAN != 0}">
							<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_PLAN == 0 }">
							0.00%
						</c:if>
					</td>
					<td>
						<c:if test="${regionTotal.ACT_MONTH_F != 0}">
							<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_MONTH_F == 0}">
							0.00%
						</c:if>
					</td>
					<td>${regionTotal.ACT_YEAR}</td>
					<td>
						<c:if test="${regionTotal.ACT_YEAR_F != 0}">
							<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.ACT_YEAR_F == 0}">
							0.00%
						</c:if>
					</td>
					
					<td>${regionTotal.DLVRY_AMOUT}</td>
					<td>${regionTotal.EXISTS_AMOUNT}</td>
					<td>${regionTotal.RESERVER_AMOUNT}</td>
					<td>${regionTotal.AVAILABLE_AMOUNT}</td>
					
					<td>${regionTotal.BILL_DAY}</td>
					<td>${regionTotal.BILL_MONTH}</td>
					<td>${regionTotal.BILL_PLAN}</td>
					<td>
						<c:if test="${regionTotal.BILL_PLAN != 0}">
							<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_PLAN == 0}">
							0.00%
						</c:if>
					</td>
					<td>
						<c:if test="${regionTotal.BILL_MONTH_F != 0}">
							<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_MONTH_F == 0}">
							0.00%
						</c:if>
					</td>
					<td>${regionTotal.BILL_YEAR}</td>
					<td>
						<c:if test="${regionTotal.BILL_YEAR_F != 0}">
							<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${regionTotal.BILL_YEAR_F == 0}">
							0.00%
						</c:if>
					</td>
				</tr>
				
				<!-- 大区合计 -->
				<tr class="table_list_row2">
					<th>
						${ps[status.index-1].ORG_NAME}
					</th>
					<th>合计</th>
					
					<th>${orgTotal.ACT_DAY}</th>
					<th>${orgTotal.ACT_MONTH}</th>
					<th>${orgTotal.ACT_PLAN}</th>
					<th>
						<c:if test="${orgTotal.ACT_PLAN != 0}">
							<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${orgTotal.ACT_PLAN == 0 }">
							0.00%
						</c:if>
					</th>
					<th>
						<c:if test="${orgTotal.ACT_MONTH_F != 0}">
							<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${orgTotal.ACT_MONTH_F == 0}">
							0.00%
						</c:if>
					</th>
					<th>${orgTotal.ACT_YEAR}</th>
					<th>
						<c:if test="${orgTotal.ACT_YEAR_F != 0}">
							<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${orgTotal.ACT_YEAR_F == 0}">
							0.00%
						</c:if>
					</th>
					
					<th>${orgTotal.DLVRY_AMOUT}</th>
					<th>${orgTotal.EXISTS_AMOUNT}</th>
					<th>${orgTotal.RESERVER_AMOUNT}</th>
					<th>${orgTotal.AVAILABLE_AMOUNT}</th>
					
					<th>${orgTotal.BILL_DAY}</th>
					<th>${orgTotal.BILL_MONTH}</th>
					<th>${orgTotal.BILL_PLAN}</th>
					<th>
						<c:if test="${orgTotal.BILL_PLAN != 0}">
							<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
						</c:if>
						<c:if test="${orgTotal.BILL_PLAN == 0}">
							0.00%
						</c:if>
					</th>
					<th>
						<c:if test="${orgTotal.BILL_MONTH_F != 0}">
							<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${orgTotal.BILL_MONTH_F == 0}">
							0.00%
						</c:if>
					</th>
					<th>${orgTotal.BILL_YEAR}</th>
					<th>
						<c:if test="${orgTotal.BILL_YEAR_F != 0}">
							<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
						</c:if>
						<c:if test="${orgTotal.BILL_YEAR_F == 0}">
							0.00%
						</c:if>
					</th>
				</tr>
				
				<!-- 显示记录 -->
				<tr class="scrollTr">
					<td>
						${item.REGION_NAME}
					</td>
					<td>
						${item.DEALER_SHORTNAME}
					</td>
					
					<td>
						<c:if test="${item.ACT_DAY != 0}">
							${item.ACT_DAY}
						</c:if>
						<c:if test="${item.ACT_DAY == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH != 0}">
							${item.ACT_MONTH}
						</c:if>
						<c:if test="${item.ACT_MONTH == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_PLAN != 0}">
							${item.ACT_PLAN}
						</c:if>
						<c:if test="${item.ACT_PLAN == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH_COMPLETION_RATE != 0}">
							<fmt:formatNumber type="percent" value="${item.ACT_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.ACT_MONTH_COMPLETION_RATE == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.ACT_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.ACT_MONTH_YEAR_ON_YEAR-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.ACT_YEAR != 0}">
							${item.ACT_YEAR}
						</c:if>
						<c:if test="${item.ACT_YEAR == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.AATU-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.AATU-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.AATU-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					
					<td>
						<c:if test="${item.DLVRY_AMOUT != 0}">
							${item.DLVRY_AMOUT}
						</c:if>
						<c:if test="${item.DLVRY_AMOUT == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.EXISTS_AMOUNT != 0}">
							${item.EXISTS_AMOUNT}
						</c:if>
						<c:if test="${item.EXISTS_AMOUNT == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.RESERVER_AMOUNT != 0}">
							${item.RESERVER_AMOUNT}
						</c:if>
						<c:if test="${item.RESERVER_AMOUNT == 0}">
							&nbsp;
						</c:if>		
					</td>
					<td>
						<c:if test="${item.AVAILABLE_AMOUNT != 0}">
							${item.AVAILABLE_AMOUNT}
						</c:if>
						<c:if test="${item.AVAILABLE_AMOUNT == 0}">
							&nbsp;
						</c:if>
					</td>
					
					<td>
						<c:if test="${item.BILL_DAY != 0 }">
							${item.BILL_DAY}
						</c:if>	
						<c:if test="${item.BILL_DAY == 0 }">
							&nbsp;
						</c:if>	
					</td>
					<td>
						<c:if test="${item.BILL_MONTH != 0}">
							${item.BILL_MONTH}
						</c:if>
						<c:if test="${item.BILL_MONTH == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_PLAN != 0}">
							${item.BILL_PLAN}
						</c:if>
						<c:if test="${item.BILL_PLAN == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_MONTH_COMPLETION_RATE != 0}">
							<fmt:formatNumber type="percent" value="${item.BILL_MONTH_COMPLETION_RATE}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BILL_MONTH_COMPLETION_RATE == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.BILL_MONTH_YEAR_ON_YEAR-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BILL_MONTH_YEAR_ON_YEAR-1 == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BILL_YEAR != 0}">
							${item.BILL_YEAR}
						</c:if>
						<c:if test="${item.BILL_YEAR == 0}">
							&nbsp;
						</c:if>
					</td>
					<td>
						<c:if test="${item.BATU-1 != 0}">
							<fmt:formatNumber type="percent" value="${item.BATU-1}" pattern="#0%"></fmt:formatNumber>
						</c:if>
						<c:if test="${item.BATU-1 == 0}">
							&nbsp;
						</c:if>
					</td>
				</tr>
				
				<!-- 初始化省份 -->
				<c:set target="${regionTotal}" property="ACT_MONTH_F" value="${item.ACT_MONTH_F}" />
				<c:set target="${regionTotal}" property="ACT_YEAR_F" value="${item.ACT_YEAR_F}" />
				<c:set target="${regionTotal}" property="ACT_DAY" value="${item.ACT_DAY}" />
				<c:set target="${regionTotal}" property="ACT_PLAN" value="${item.ACT_PLAN}" />
				<c:set target="${regionTotal}" property="ACT_MONTH" value="${item.ACT_MONTH}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_YEAR" value="${item.ACT_YEAR}" />
				<c:set target="${regionTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
				<c:set target="${regionTotal}" property="DLVRY_AMOUT" value="${item.DLVRY_AMOUT}" />
				<c:set target="${regionTotal}" property="EXISTS_AMOUNT" value="${item.EXISTS_AMOUNT}" />
				<c:set target="${regionTotal}" property="RESERVER_AMOUNT" value="${item.RESERVER_AMOUNT}" />
				<c:set target="${regionTotal}" property="AVAILABLE_AMOUNT" value="${item.AVAILABLE_AMOUNT}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_F" value="${item.BILL_MONTH_F}" />
				<c:set target="${regionTotal}" property="BILL_YEAR_F" value="${item.BILL_YEAR_F}" />
				<c:set target="${regionTotal}" property="BILL_DAY" value="${item.BILL_DAY}" />
				<c:set target="${regionTotal}" property="BILL_MONTH" value="${item.BILL_MONTH}" />
				<c:set target="${regionTotal}" property="BILL_PLAN" value="${item.BILL_PLAN}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${regionTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_YEAR" value="${item.BILL_YEAR}" />
				<c:set target="${regionTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
						
				<!-- 初始化大区 -->
				<c:set target="${orgTotal}" property="ACT_MONTH_F" value="${item.ACT_MONTH_F}" />
				<c:set target="${orgTotal}" property="ACT_YEAR_F" value="${item.ACT_YEAR_F}" />
				<c:set target="${orgTotal}" property="ACT_DAY" value="${item.ACT_DAY}" />
				<c:set target="${orgTotal}" property="ACT_PLAN" value="${item.ACT_PLAN}" />
				<c:set target="${orgTotal}" property="ACT_MONTH" value="${item.ACT_MONTH}" />
				<c:set target="${orgTotal}" property="ACT_MONTH_COMPLETION_RATE" value="${item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${orgTotal}" property="ACT_MONTH_YEAR_ON_YEAR" value="${item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${orgTotal}" property="ACT_YEAR" value="${item.ACT_YEAR}" />
				<c:set target="${orgTotal}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
				<c:set target="${orgTotal}" property="DLVRY_AMOUT" value="${item.DLVRY_AMOUT}" />
				<c:set target="${orgTotal}" property="EXISTS_AMOUNT" value="${item.EXISTS_AMOUNT}" />
				<c:set target="${orgTotal}" property="RESERVER_AMOUNT" value="${item.RESERVER_AMOUNT}" />
				<c:set target="${orgTotal}" property="AVAILABLE_AMOUNT" value="${item.AVAILABLE_AMOUNT}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_F" value="${item.BILL_MONTH_F}" />
				<c:set target="${orgTotal}" property="BILL_YEAR_F" value="${item.BILL_YEAR_F}" />
				<c:set target="${orgTotal}" property="BILL_DAY" value="${item.BILL_DAY}" />
				<c:set target="${orgTotal}" property="BILL_MONTH" value="${item.BILL_MONTH}" />
				<c:set target="${orgTotal}" property="BILL_PLAN" value="${item.BILL_PLAN}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_COMPLETION_RATE" value="${item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${orgTotal}" property="BILL_MONTH_YEAR_ON_YEAR" value="${item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${orgTotal}" property="BILL_YEAR" value="${item.BILL_YEAR}" />
				<c:set target="${orgTotal}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
				
				<!-- 累加总计 -->
				<c:set target="${total}" property="ACT_MONTH_F" value="${total.ACT_MONTH_F + item.ACT_MONTH_F}" />
				<c:set target="${total}" property="ACT_YEAR_F" value="${total.ACT_YEAR_F + item.ACT_YEAR_F}" />
				<c:set target="${total}" property="ACT_DAY" value="${total.ACT_DAY + item.ACT_DAY}" />
				<c:set target="${total}" property="ACT_PLAN" value="${total.ACT_PLAN + item.ACT_PLAN}" />
				<c:set target="${total}" property="ACT_MONTH" value="${total.ACT_MONTH + item.ACT_MONTH}" />
				<c:set target="${total}" property="ACT_MONTH_COMPLETION_RATE" value="${total.ACT_MONTH_COMPLETION_RATE + item.ACT_MONTH_COMPLETION_RATE}" />
				<c:set target="${total}" property="ACT_MONTH_YEAR_ON_YEAR" value="${total.ACT_MONTH_YEAR_ON_YEAR + item.ACT_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${total}" property="ACT_YEAR" value="${total.ACT_YEAR + item.ACT_YEAR}" />
				<c:set target="${total}" property="ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.ACT_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
				<c:set target="${total}" property="DLVRY_AMOUT" value="${total.DLVRY_AMOUT + item.DLVRY_AMOUT}" />
				<c:set target="${total}" property="EXISTS_AMOUNT" value="${total.EXISTS_AMOUNT + item.EXISTS_AMOUNT}" />
				<c:set target="${total}" property="RESERVER_AMOUNT" value="${total.RESERVER_AMOUNT + item.RESERVER_AMOUNT}" />
				<c:set target="${total}" property="AVAILABLE_AMOUNT" value="${total.AVAILABLE_AMOUNT + item.AVAILABLE_AMOUNT}" />
				<c:set target="${total}" property="BILL_MONTH_F" value="${total.BILL_MONTH_F + item.BILL_MONTH_F}" />
				<c:set target="${total}" property="BILL_YEAR_F" value="${total.BILL_YEAR_F + item.BILL_YEAR_F}" />
				<c:set target="${total}" property="BILL_DAY" value="${total.BILL_DAY + item.BILL_DAY}" />
				<c:set target="${total}" property="BILL_MONTH" value="${total.BILL_MONTH + item.BILL_MONTH}" />
				<c:set target="${total}" property="BILL_PLAN" value="${total.BILL_PLAN + item.BILL_PLAN}" />
				<c:set target="${total}" property="BILL_MONTH_COMPLETION_RATE" value="${total.BILL_MONTH_COMPLETION_RATE + item.BILL_MONTH_COMPLETION_RATE}" />
				<c:set target="${total}" property="BILL_MONTH_YEAR_ON_YEAR" value="${total.BILL_MONTH_YEAR_ON_YEAR + item.BILL_MONTH_YEAR_ON_YEAR}" />
				<c:set target="${total}" property="BILL_YEAR" value="${total.BILL_YEAR + item.BILL_YEAR}" />
				<c:set target="${total}" property="BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP" value="${total.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP + item.BILL_ANNUAL_ACCUMULATIVE_TOTAL_UP}" />
				
				<c:choose>
					<c:when test="${status.last == true }">
						<!-- 省份合计 -->
						<tr class="scrollTr" style="font-weight: bold;">
							<td>${item.REGION_NAME}</td>
							<td>合计</td>
							
							<td>${regionTotal.ACT_DAY}</td>
							<td>${regionTotal.ACT_MONTH}</td>
							<td>${regionTotal.ACT_PLAN}</td>
							<td>
								<c:if test="${regionTotal.ACT_PLAN != 0}">
									<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</td>
							<td>
								<c:if test="${regionTotal.ACT_MONTH_F != 0}">
									<%= df.format(regionTotal.get("ACT_MONTH").doubleValue()*100 / regionTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</td>
							<td>${regionTotal.ACT_YEAR}</td>
							<td>
								<c:if test="${regionTotal.ACT_YEAR_F != 0}">
									<%= df.format(regionTotal.get("ACT_YEAR").doubleValue()*100 / regionTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</td>
							
							<td>${regionTotal.DLVRY_AMOUT}</td>
							<td>${regionTotal.EXISTS_AMOUNT}</td>
							<td>${regionTotal.RESERVER_AMOUNT}</td>
							<td>${regionTotal.AVAILABLE_AMOUNT}</td>
							
							<td>${regionTotal.BILL_DAY}</td>
							<td>${regionTotal.BILL_MONTH}</td>
							<td>${regionTotal.BILL_PLAN}</td>
							<td>
								<c:if test="${regionTotal.BILL_PLAN != 0}">
									<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</td>
							<td>
								<c:if test="${regionTotal.BILL_MONTH_F != 0}">
									<%= df.format(regionTotal.get("BILL_MONTH").doubleValue()*100 / regionTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</td>
							<td>${regionTotal.BILL_YEAR}</td>
							<td>
								<c:if test="${regionTotal.BILL_YEAR_F != 0}">
									<%= df.format(regionTotal.get("BILL_YEAR").doubleValue()*100 / regionTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${regionTotal.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</td>
						</tr>
						
						<!-- 大区合计 -->
						<tr class="table_list_row2">
							<th>${item.ORG_NAME}</th>
							<th>合计</th>
							
							<th>${orgTotal.ACT_DAY}</th>
							<th>${orgTotal.ACT_MONTH}</th>
							<th>${orgTotal.ACT_PLAN}</th>
							<th>
								<c:if test="${orgTotal.ACT_PLAN != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.ACT_MONTH_F != 0}">
									<%= df.format(orgTotal.get("ACT_MONTH").doubleValue()*100 / orgTotal.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.ACT_YEAR}</th>
							<th>
								<c:if test="${orgTotal.ACT_YEAR_F != 0}">
									<%= df.format(orgTotal.get("ACT_YEAR").doubleValue()*100 / orgTotal.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
							
							<th>${orgTotal.DLVRY_AMOUT}</th>
							<th>${orgTotal.EXISTS_AMOUNT}</th>
							<th>${orgTotal.RESERVER_AMOUNT}</th>
							<th>${orgTotal.AVAILABLE_AMOUNT}</th>
							
							<th>${orgTotal.BILL_DAY}</th>
							<th>${orgTotal.BILL_MONTH}</th>
							<th>${orgTotal.BILL_PLAN}</th>
							<th>
								<c:if test="${orgTotal.BILL_PLAN != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${orgTotal.BILL_MONTH_F != 0}">
									<%= df.format(orgTotal.get("BILL_MONTH").doubleValue()*100 / orgTotal.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${orgTotal.BILL_YEAR}</th>
							<th>
								<c:if test="${orgTotal.BILL_YEAR_F != 0}">
									<%= df.format(orgTotal.get("BILL_YEAR").doubleValue()*100 / orgTotal.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${orgTotal.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
						</tr>
						<tr class="table_list_row2">
							<th colspan="2">总计</th>
							
							<th>${total.ACT_DAY}</th>
							<th>${total.ACT_MONTH}</th>
							<th>${total.ACT_PLAN}</th>
							<th>
								<c:if test="${total.ACT_PLAN != 0}">
									<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${total.ACT_PLAN == 0 }">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${total.ACT_MONTH_F != 0}">
									<%= df.format(total.get("ACT_MONTH").doubleValue()*100 / total.get("ACT_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.ACT_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${total.ACT_YEAR}</th>
							<th>
								<c:if test="${total.ACT_YEAR_F != 0}">
									<%= df.format(total.get("ACT_YEAR").doubleValue()*100 / total.get("ACT_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.ACT_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
							
							<th>${total.DLVRY_AMOUT}</th>
							<th>${total.EXISTS_AMOUNT}</th>
							<th>${total.RESERVER_AMOUNT}</th>
							<th>${total.AVAILABLE_AMOUNT}</th>
							
							<th>${total.BILL_DAY}</th>
							<th>${total.BILL_MONTH}</th>
							<th>${total.BILL_PLAN}</th>
							<th>	
								<c:if test="${total.BILL_PLAN != 0}">
									<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_PLAN").doubleValue()) %>%
								</c:if>
								<c:if test="${total.BILL_PLAN == 0}">
									0.00%
								</c:if>
							</th>
							<th>
								<c:if test="${total.BILL_MONTH_F != 0}">
									<%= df.format(total.get("BILL_MONTH").doubleValue()*100 / total.get("BILL_MONTH_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.BILL_MONTH_F == 0}">
									0.00%
								</c:if>
							</th>
							<th>${total.BILL_YEAR}</th>
							<th>
								<c:if test="${total.BILL_YEAR_F != 0}">
									<%= df.format(total.get("BILL_YEAR").doubleValue()*100 / total.get("BILL_YEAR_F").doubleValue() -100) %>%
								</c:if>
								<c:if test="${total.BILL_YEAR_F == 0}">
									0.00%
								</c:if>
							</th>
						</tr>
					</c:when>
				</c:choose>
			</c:when>
			
			
		</c:choose>
	</c:forEach>
</table>
<br>
</div>
</form> 
<script type="text/javascript" >
	function refresh(){
		$('fm').action="<%= contextPath %>/sales/ordermanage/orderquery/DayReport/saleDayReportInit.do";
		$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
