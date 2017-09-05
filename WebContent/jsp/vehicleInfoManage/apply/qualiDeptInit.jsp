<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质量部门</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量部审批（车厂）
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">填报人：</td>
      	<td><input name="reportname" type="text" id="reportname" class="middle_txt"/></td>
        <td class="table_query_2Col_label_6Letter" nowrap="true">用户姓名：</td>
      	<td><input name="username" type="text" id="username" class="middle_txt"/></td>
      </tr> 
      <tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">用户名称：</td>
     	<td><input name="dealername" type="text" id="dealername" class="middle_txt"/></td>
		<td class="table_query_2Col_label_5Letter" nowrap="true">审批状态：</td>
     	<td>
     		<script type="text/javascript">
        		genSelBoxExp("status",<%=Constant.BARCODE_APPLY_STATUS%>,"",true,"short_sel","","false",'<%=Constant.BARCODE_APPLY_STATUS_01%>');
       		</script>
     	</td>
    </tr>

    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/data2.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{id:'action', header: "操作", sortable: false, align:'center',dataIndex: 'QUALITY_ID',renderer:myLink},
		{header: "填报人", dataIndex: 'REPORT_NAME', align:'center'},
		{header: "联系电话", dataIndex: 'CONTACT_TYPE', align:'center'},
		{header: "用户名称", dataIndex: 'DEALER_NAME', align:'center'},
		{header: "车型", dataIndex: 'CAR_TYPE', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "故障件代码", dataIndex: 'FIRST_PROBLEM_CODE', align:'center'},
		{header: "故障件名称", dataIndex: 'FIRST_PROBLEM_NAME', align:'center'},
		{header: "审核人", dataIndex: 'AUDIT_NAME', align:'center',renderer:myaudit},
		{header: "审核时间", dataIndex: 'AUDIT_DATE', align:'center',renderer:myauditdate},
		{header: "填报日期", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue}
	];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95451002){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='audit(\""+ value +"\")' value='审批'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/>");
		}
	}
	function myauditdate(value,meta,record){
		if(value==null || value==""){
			return String.format("--");
		}else{
			return String.format(value);
		}
	}
	function myaudit(value,meta,record){
		if(value==null || value==""){
			return String.format("--");
		}else{
			return String.format(value);
		}
	}
	
	function audit(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/audit1.do?qualityid='+value ;
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/viewQualityInfoReport4.do?qualityid='+value ;
	}
	
</script>
<!--页面列表 end -->
</html>