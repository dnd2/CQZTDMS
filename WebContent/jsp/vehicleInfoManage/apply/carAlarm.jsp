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
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;预警车辆及维修信息
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

<form method="post" name="fm1" id="fm1">
	<input name="VIN" id="VIN" type="hidden">
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/queryCarAlarm.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "维修历史", sortable: false, align:'center',dataIndex: 'VIN',renderer:myLink},
		{header: "预警明细", sortable: false, align:'center',dataIndex: 'VIN',renderer:myLink1},
//		{header: "三包判定", sortable: false, align:'center',dataIndex: 'VIN',renderer:myLink1},
		{header: "VIN", dataIndex: 'VIN', align:'center'}
	];
	function myLink(value,meta,record){       
						//              		    	/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin;
		return String.format("<a target='_blank' href='<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN="+value+"'>维修历史</a>");
	}
	function myLink1(value,meta,record){       
		return String.format("<a target='_blank' href='<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN="+value+"&flag=1'>预警明细</a>");
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
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditByTechnicalDept.do?qualityid='+value ;
	}
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	
</script>
<!--页面列表 end -->
</html>