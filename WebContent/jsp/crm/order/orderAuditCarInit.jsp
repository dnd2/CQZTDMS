<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<title>订单查询</title>
</head>
<body onunload='javascript:destoryPrototype();  '
	onload="__extQuery__(1);loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置： 整车销售 &gt; 潜客管理	&gt;订单管理 &gt;订车/退车记录查询
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" id="curPaths" value="<%=contextPath%>" /> 
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" /> 
			<input type="hidden" id="userDealerId" name="userDealerId" value="${userDealerId}" /> 
			<input type="hidden" id="adviser" name="adviser" value="${adviser}" />
			<table class="table_query" border="0">
				<tr >
					<td class="table_query_3Col_label_6Letter">
						客户名称：
					</td>
					<td>
						<input type="text" name="customer_name" id="customer_name" />
					</td>
					<c:if test="${dutyType==10431005}">	
		
					<td align="left" class="table_query_3Col_label_6Letter">
						客户手机：
					</td>
					
					<td align="left" nowrap>
						<input type="text" name="telephone" id="telephone" />
					</td>
					</c:if>	
					<td align="left" nowrap>
						订单日期：
					</td>
					<td align="left" nowrap>
						<input name="orderDate" type="text" id="orderDate" class="short_txt" value="${today}" readonly /> 
						   <input class="time_ico"	type="button" onClick="showcalendar(event, 'orderDate', false);" />
						到： <input name="orderDateEnd" type="text" id="orderDateEnd" class="short_txt"  value="${today}" readonly /> 
						   <input class="time_ico"	type="button" onClick="showcalendar(event, 'orderDateEnd', false);" />
					</td>
					<td class="table_query_3Col_label_6Letter"></td>
					<td>
					</td>
					<!-- 
					<td class="table_query_3Col_label_6Letter">转介绍类型：</td>
					<td>
						<select id="be_status" name="be_status" style="width:125px;">
				      			<option value="">--请选择--</option>
				      			<option value="10001">老客户转介绍</option>
				      			<option value="10002">情报转介绍</option>
				      			<option value="10003">老客户+情报转介绍</option>
				      	</select>
					</td>
				 -->
				</tr>
				<tr>
				<td class="table_query_3Col_label_6Letter">订单状态：</td>
					<td>
						<select id="order_status" name="order_status" style="width:100px;">
				      			<option value="">--请选择--</option>
				      			<option value="1">下单</option>
				      			<option value="0">退单</option>
				      	</select>
				</td>
				<td align="right">车系：</td>
					<td align="left">
              		<input id="seriesId" name="seriesId" type="text"  size="13"  readonly="readonly" value=""/> 
					<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="toSeriesList('seriesId','','true', '${orgId}')" value="..." />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('seriesId');"/>
				</td>	
		<c:if test="${dutyType!=10431005}">	
			<td align="right" nowrap>选择经销商：</td>
              <td align="left">
                  <input type="text" name="dealerCode" size="20" value="" id="dealerCode"/>
                  <%-- <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealerBase('dealerCode','','true', '${orgId}','','')" value="..." />   --%>             
                   <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
                  		onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '', '');"  value="..."/>     
                  <input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
              </td>
		</c:if>			
					<!-- 
					<td class="table_query_3Col_label_6Letter">
					VIN：
					</td>
					<td>
						<input type="text" name="vin" id="vin" />
					</td>
					 -->
					 <!-- 
					<c:if test="${adviserLogon=='yes' }">
					<td class="table_query_3Col_label_6Letter"></td>
					<td></td>
					</c:if>
					<c:if test="${adviserLogon=='no' }">
					<td class="table_query_3Col_label_6Letter">顾问：</td>
					<td>
						<select id="adviserId" name="adviserId" style="width:130px;">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
					</c:if>
					 -->
					<!--  
					<c:if test="${managerLogon=='yes' }">
					<td class="table_query_3Col_label_6Letter" >分组：</td>
					<td>
						<select id="groupId" name="groupId" style="width:100px;">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
					</c:if>
					<c:if test="${managerLogon=='no' }">
					<td class="table_query_3Col_label_6Letter"></td>
					<td></td>
					</c:if>
					-->
				</tr>
				<tr>
					<td colspan="7" align="center">
					  <input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
					  
					  <input type="button" class="cssbutton" name="orderDetailDownloadBtn" id="orderDetailDownloadBtn" onclick="orderDetailDownload()" value="下  载" />
					
					</td>
					 
					<td align="right"  >
						页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3" />
					</td>
				</tr>
			</table>
			<!--分页部分  -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
	<script type="text/javascript"> 
	var myPage;
	var url = "<%=contextPath%>/crm/order/OrderManage/orderAuditCarFindQuery.json";
	var title = null;
	var columns =null;
	var type=${dutyType };
	if(type!="10431005"){	
	 columns = [
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'PQ_ORG_NAME', align:'center'},
				{header: "代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				//{header: "订单编号", dataIndex: 'ORDER_ID', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				//{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				//{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "数量", dataIndex: 'ORDERNUM', align:'center'},
				{header: "订单日期", dataIndex: 'ORDER_DATE', align:'center'},
				{header: "订单状态", dataIndex: 'TASK_STATUS', align:'center'},
				{header: "顾问", dataIndex: 'ADVISER', align:'center'}
			
				//{header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink}
		          ];
	}else{
		columns = [
					{header: "代码", dataIndex: 'DEALER_CODE', align:'center'},
					{header: "简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
					{header: "订单编号", dataIndex: 'ORDER_ID', align:'center'},
					{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
					{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
					{header: "车型", dataIndex: 'SERIES_NAME', align:'center'},
					//{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
					{header: "数量", dataIndex: 'ORDERNUM', align:'center'},
					{header: "订单日期", dataIndex: 'ORDER_DATE', align:'center'},
					{header: "订单状态", dataIndex: 'TASK_STATUS', align:'center'},
					{header: "顾问", dataIndex: 'ADVISER', align:'center'}
				
					//{header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink}
			          ];
		}
	function myLink(value,meta,record){
		if(document.getElementById("userDealerId").value==null || document.getElementById("userDealerId").value=="" || document.getElementById("adviser").value==null || document.getElementById("adviser").value=="" || record.data.ORDER_STATUS =="修改锁定" || record.data.ORDER_STATUS =="退单锁定" || record.data.ORDER_STATUS =="退单" || record.data.ORDER_STATUS =="完成交车") {
			return String.format("<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkDetailUrl(\""+record.data.ORDER_ID+"\")'>[详情]</a>/<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='printDetailUrl(\""+record.data.ORDER_ID+"\")'>[打印]</a>");
		} else {
			return String.format("<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkDetailUrl(\""+record.data.ORDER_ID+"\")'>[详情]</a><a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='checkUpdateUrl(\""+record.data.ORDER_ID+"\")'>[修改/退单申请]</a>/<a href=\"#\" id=\"ORDER_ID\" name=\"ORDER_ID\" onclick='printDetailUrl(\""+record.data.ORDER_ID+"\")'>[打印]</a>");
		}
	}   
	function checkDetailUrl(orderId){
		window.location.href="<%=contextPath%>/crm/order/OrderManage/orderDetailInit.do?orderId="+orderId;
	}
	function checkUpdateUrl(orderId){
		window.location.href="<%=contextPath%>/crm/order/OrderManage/orderUpdateInit.do?orderId="+orderId;
	}
	function printDetailUrl(orderId){
		$('fm').action= "<%=contextPath%>/crm/order/OrderManage/printDetailInit.do?orderId="+orderId
	 	$('fm').target="_blank";
	 	$('fm').submit();
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
	function toSeriesList(inputCode ,isMulti ,orgId)
	    {
	    	if(!inputCode){ inputCode = null;}
	    	if(!isMulti){ isMulti = null;}
	    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showSeriesCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
	    }
	    
function orderDetailDownload(){
	$('fm').action= "<%=request.getContextPath()%>/crm/order/OrderManage/orderAuditDownload.json";
	$('fm').submit();
}
	    
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
</body>
</html>