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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量部门审批
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<!-- <tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">填报人：</td>
      	<td><input name="reportname" type="text" id="reportname" class="middle_txt"/></td>
        <td class="table_query_2Col_label_6Letter" nowrap="true">用户姓名：</td>
      	<td><input name="username" type="text" id="username" class="middle_txt"/></td>
    </tr> -->
    <tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">VIN：</td>
      	<td><input name="vin" type="text" id="vin" maxlength="30" class="middle_txt"/></td>
        <td align="right" nowrap="nowrap" >上报日期：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		  <input class="short_txt" id="reportDate1" name="reportDate1" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="reportDate1,reportDate2"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'reportDate1', false);" type="button"/>
                    		至
                    <input class="short_txt" id="reportDate2" name="reportDate2" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="reportDate1,reportDate2"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'reportDate2', false);" type="button"/>
            	</div>
            </td>
    </tr>
    <tr>
    	<td class="table_query_2Col_label_5Letter" nowrap="true">经销商编码：</td>
     	<td>
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
	 			<input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');"/>&nbsp;
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="true">经销商名称：</td>
     	<td><input name="dealerName" type="text" id="dealername"  maxlength="25" class="middle_txt"/></td>
   </tr>
    <tr>
     	<td class="table_query_2Col_label_5Letter" nowrap="true">审批状态：</td>
     	<td>
     		<script type="text/javascript">
        		genSelBoxExp("status",<%=Constant.Quality_Verify%>,"<%=Constant.Quality_Verify_03%>",true,"short_sel","","false",'<%=Constant.Quality_Verify_01%>');
       		</script>
     	</td>
     	<td class="table_query_2Col_label_5Letter" nowrap="true">质量审核人：</td>
     	<td>
     		<input class="middle_txt" id="zlaudit"  name="zlaudit" type="text" maxlength="15"/>
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/qualityDeptInfo.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{id:'action', header: "操作", sortable: false, align:'center',dataIndex: 'QUALITY_ID',renderer:myLink},
		{header: "填报人", dataIndex: 'REPORT_NAME', align:'center'},
		{header: "联系电话", dataIndex: 'CONTACT_TYPE', align:'center'},
		{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
		{header: "经销商编码", dataIndex: 'DEALERCODE', align:'center'},
		{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "车型", dataIndex: 'CAR_TYPE', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "故障件代码", dataIndex: 'FIRST_PROBLEM_CODE', align:'center'},
		{header: "故障件名称", dataIndex: 'FIRST_PROBLEM_NAME', align:'center'},
		{header: "审核人", dataIndex: 'NEXT_AUDIT_NAME', align:'center',renderer:myaudit},
		{header: "审核时间", dataIndex: 'NEXT_AUDIT_DATE', align:'center',renderer:myauditdate},
		{header: "填报日期", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "上报日期", dataIndex: 'REPORT_DATE_BTN', align:'center'},
		{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue}
	];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95531003 && record.data.ISEND==null){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='audit(\""+ value +"\")' value='回复'/>");
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
	//详细页面
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/viewQualityInfoReport2.do?qualityid='+value ;
	}
	//审批
	function audit(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditByQualitydept.do?qualityid='+value ;
	}
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>