<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场质量信息申报审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;市场质量信息申报审核
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">填报人：</td>
      	<td><input name="reportName" type="text" id="reportName" class="middle_txt"/></td>
        <td class="table_query_2Col_label_6Letter">用户姓名：</td>
      	<td><input name="ctmName" type="text" id="ctmName" class="middle_txt"/></td>
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">经销商名称：</td>
     	<td><input name="dealerName" type="text" id="dealerName" class="middle_txt"/></td>
		<td class="table_query_2Col_label_5Letter">状态：</td>
     	<td>
     		<script type="text/javascript">
	        	genSelBoxExp("verifyStatus",9553,"",true,"short_sel","","false",'95531001');
	        </script>
		</td>
    </tr>
    <tr>
    	<td align="center" colspan="4">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/queryQualityInfoReportVerifyBydept.json";
	var title = null;
	var columns = [
				{header: "填报人", dataIndex: 'REPORT_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "经销商编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "故障名称", dataIndex: 'FAULT_NAME', align:'center'},
				{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, align:'center',dataIndex: 'QUALITY_REPORT_ID',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95531002){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='verifyDetail(\""+ value +"\")' value='审核'/>");
		}
		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/>");
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/viewQualityInfoReportVerify.do?qualityReportId='+value ;
	}
	function verifyDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/verifyQualityInfoReportBydept.do?qualityReportId='+value ;
	}

      	
</script>
<!--页面列表 end -->
</body>
</html>