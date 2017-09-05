<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务商结算跟踪报表</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
<div id="loader" style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后索赔报表&gt;服务商结算跟踪报表</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>" /> 
<table class="table_query">
	<tr>
		<td width="15%" align="right">服务站代码：</td>
		<td width="30%" align="left">
			<input type="text" name="code" class="middle_txt" id="code"/>
		</td>
		<td width="15%" align="right">服务站名称：</td>
		<td width="30%" align="left">
			<input type="text" name="name" class="middle_txt" id="name"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">结算单号：</td>
		<td width="30%" align="left">
			<input type="text" name="balanceNo" class="middle_txt" id="balanceNo"/>
		</td>
		<td width="15%" align="right">生产基地：</td>
		<td width="30%" align="left">
			<script type="text/javascript">
	      		genSelBox("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true","");
	      	</script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">大区：</td>
		<td width="30%" align="left">
			<select class="short_sel" name="area">
            	<option value="">-请选择-</option>
	            <c:if test="${areaList!=null}">
					<c:forEach items="${areaList}" var="list1">
						<option value="${list1.ORG_ID}">${list1.ORG_NAME}</option>
					</c:forEach>
				</c:if>
			</select>
		</td>
		<td width="15%" align="right">省份：</td>
		<td width="30%" align="left">
			<select class="short_sel" id="province" name="province"></select>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">结算起止时间：</td>
		<td width="30%" align="left">
			<input type="text" name="bDate" id="bDate"
             type="text" class="short_txt" 
             datatype="1,is_date,10" group="bDate,eDate" 
             hasbtn="true" callFunction="showcalendar(event, 'bDate', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="eDate" id="eDate" 
 			type="text" class="short_txt" datatype="1,is_date,10" 
 			group="bDate,eDate" 
 			hasbtn="true" callFunction="showcalendar(event, 'eDate', false);"/>
		</td>
		<td align="center" colspan="2">
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询" onclick="__extQuery__(1)" />&nbsp;
		    <input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载" onclick="exportExcel()" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /></form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/report/jcafterservicereport/BalanceTracking/query4WC.json";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "一级服务中心代码", dataIndex: 'ROOT_DEALER_CODE', align:'center'},
				{header: "一级服务中心名称", dataIndex: 'ROOT_DEALER_NAME', align:'center'},
				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "厂家", dataIndex: 'YIELDLY', align:'center'},
				{header: "结算单号", dataIndex: 'BALANCE_NO', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "维修起止日期", dataIndex: 'RO_DATE', align:'center'},
				{header: "收单时间", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "收单人", dataIndex: 'SIGN_PERSON', align:'center'},
				{header: "提交复核时间", dataIndex: 'REAUDIT_DATE', align:'center'},
				{header: "结算员", dataIndex: 'REAUDIT_PERSON', align:'center'},
				{header: "开票通知单时间", dataIndex: 'FANCE_DATE', align:'center'},
				{header: "通知发出人", dataIndex: 'FANCE_PERSON', align:'center'},
				{header: "收票时间", dataIndex: 'GET_DATE', align:'center'},
				{header: "收票人", dataIndex: 'GET_PERSON', align:'center'},
				{header: "财务挂账时间", dataIndex: 'CREDIT_DATE', align:'center'},
				{header: "挂账人", dataIndex: 'CREDIT_PERSON', align:'center'},
				{header: "索赔申请金额", dataIndex: 'APPLY_AMOUNT', align:'center'},
				{header: "开票金额", dataIndex: 'NOTE_AMOUNT', align:'center'}
		      ];
		            
	function exportExcel(){
		fm.action = "<%=contextPath%>/report/jcafterservicereport/BalanceTracking/excel4WC.do";
		fm.submit();
	}
	
	loadcalendar();
	genLocSel('province','','','','','');	
</script>
</body>
</html>