<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质改索赔明细
</div>
<form name="fm" id="fm">
<input type="hidden" name="id" id="id" value="${po.ID }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query"  style="text-align: center;">
	<!-- 查询条件 -->
		 <tbody id="show" >
		<tr>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
				车系:
			</td>
			<td width="15%" nowrap="true" class="table_query_2Col_label_7Letter">
				${po.CAR_TIE_ID }
			</td>
			 <td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
				车型:
			</td>
			<td width="15%" nowrap="true" class="table_query_2Col_label_7Letter">
				${po.CAR_TYPE_ID }
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
				 底盘号:
			</td>
			<td width="15%" nowrap="true" class="table_query_2Col_label_7Letter">
				${po.VIN }
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
		</tr>
		<tr>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				生产日期:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.RO_CREATE_DATE } 
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				起始修理日期:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.RO_REPAIR_DATE_ONE } 
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				终止修理日期:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.RO_REPAIR_DATE_TWO }
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
		</tr>
		<tr>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				零件号:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.PART_CODE }
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				零件名称:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.PART_NAME } 
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				 故障零件数:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.PART_NUM } 
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
		</tr>
		<tr>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				 部件厂代码:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.MAKER_CODE } 
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				部件厂名称:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.MAKER_NAME }  
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				故障类别代码:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.MAL_CODE }
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
		</tr>
		<tr>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				 故障现象:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.MAL_NAME }
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				创建人:
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				${po.NAME }
			</td>
			<td nowrap="true" class="table_query_2Col_label_7Letter">
				 创建时间 :
			</td>
			<td nowrap="true">
				${po.CREATE_DATE }
			</td>
			<td width="10%" nowrap="true" class="table_query_2Col_label_7Letter">
			</td> 
		</tr>
		</tbody> 
    <tr>
    	<td align="center" colspan="10">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData(${po.id});" >
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="返回"  class="normal_btn" onClick="history.back();" >
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
	var id=document.getElementById("id").value;
	var url = "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/partNumDetai1Data.json?id="+id; 
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "索赔申请单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "零件号", dataIndex: 'PART_CODE', align:'center'},
		{header: "零件名称", dataIndex: 'PART_NAME', align:'center'}
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
	function expotData(id){
		  fm.action="<%=contextPath%>/report/dmsReport/Application/partNumDetai1Data.do?id="+id;
	      fm.submit();
	}
</script>
<!--页面列表 end -->
</html>