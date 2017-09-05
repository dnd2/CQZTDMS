<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>技术支持部审批</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body onload="__extQuery__(1)">
<div class="wbox">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量信息查询
</div>
<form name="fm" id="fm">
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <tr>
		<td class="right">VIN：</td>
      	<td><input name="vin" type="text" id="vin" maxlength="30" class="middle_txt"/></td>
      	<td class="right" nowrap="nowrap" >上报日期：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		  <input class="middle_txt" id="reportDate1" name="reportDate1" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="reportDate1,reportDate2" style="width:80px;"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'reportDate1', false);" type="button"/>
                    		至
                    <input class="middle_txt" id="reportDate2" name="reportDate2" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="reportDate1,reportDate2" style="width:80px;"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'reportDate2', false);" type="button"/>
            	</div>
            </td>
    </tr>
     <tr>
     	<td class="right">经销商编码：</td>
     	<td>
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
	 			<input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');"/>&nbsp;
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td class="right">经销商名称：</td>
     	<td><input name="dealerName" type="text" id="dealername" maxlength="25" class="middle_txt"/></td>
    </tr>
     <tr>
     	<td class="right">审批状态：</td>
     	<td>
     		<script type="text/javascript">
        		genSelBoxExp("status",<%=Constant.Quality_Verify%>,"",true,"","","false",'<%=Constant.Quality_Verify_01%>');
       		</script>
     	</td>
     	<td class="right">技术审核人：</td>
     	<td>
     		<input class="middle_txt" id="jsaudit"  name="jsaudit" type="text" maxlength="15"/>
     	</td>
    </tr>
     <tr>
     	<td class="right">故障件代码：</td>
     	<td>
     		<input class="middle_txt" id="FIRST_PROBLEM_CODE"  name="FIRST_PROBLEM_CODE" type="text" maxlength="15"/>
     	</td>
     	<td class="right">故障件名称：</td>
     	<td>
     		<input class="middle_txt" id="FIRST_PROBLEM_NAME"  name="FIRST_PROBLEM_NAME" type="text" maxlength="15"/>
     	</td>
    </tr>
     <tr>
     	<td class="right">审核时间：</td>
     	<td>
     		  <input class="middle_txt" id="audit_date_start" name="audit_date_start" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="audit_date_start,audit_date_end" style="width:80px;"/>
               <input class="time_ico" value=" " onclick="showcalendar(event, 'audit_date_start', false);" type="button"/>
                                至
                <input class="middle_txt" id="audit_date_end" name="audit_date_end" readonly="readonly" datatype="1,is_date,10"
                           maxlength="10" group="audit_date_start,audit_date_end"  style="width:80px;"/>
               <input class="time_ico" value=" " onclick="showcalendar(event, 'audit_date_end', false);" type="button"/>
     	</td>
     	<td class="right"></td>
     	<td>
     	</td>
    </tr>

    <tr>
    	<td class="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1)" >
    		 &nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn"  value="导出"  class="normal_btn" onClick="exportToexcel();" >
    	</td>
    </tr>
</table>
</div>
</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</div>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/technicalSupportDeptInfo.json";
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
		{header: "主题", dataIndex: 'THEME', align:'center'},
		{header: "审核人", dataIndex: 'AUDIT_NAME', align:'center',renderer:myaudit},
		{header: "审核时间", dataIndex: 'AUDIT_DATE', align:'center',renderer:myauditdate},
		{header: "填报日期", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "上报日期", dataIndex: 'REPORT_DATE_BTN', align:'center'},
		{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue}
	];
	function myLink(value,meta,record){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/>");
		
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
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/viewQualityInfoReport.do?qualityid='+value ;
	}
	//审批
	function audit(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditByTechnicalDept.do?qualityid='+value ;
	}
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function exportToexcel(){
		 fm.action="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/exportToexceltechnical.do";
	     fm.submit();
	}
</script>
<!--页面列表 end -->
</html>