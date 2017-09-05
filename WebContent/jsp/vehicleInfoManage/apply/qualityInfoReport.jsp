<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场质量信息申报</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;市场质量信息申报
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">填报人：</td>
      	<td><input name="reportName" type="text" id="reportName" class="middle_txt"/></td>
        <td class="table_query_2Col_label_6Letter">用户姓名：</td>
      	<td><input name="ctmName" type="text" id="ctmName" class="middle_txt"/></td>
		<td class="table_query_2Col_label_5Letter">经销商名称：</td>
     	<td><input name="dealerName" type="text" id="dealerName" class="middle_txt"/></td>
    </tr>

    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		<input type="button" name="BtnAdd" id="queryBtn"  value="新增"  class="normal_btn" onClick="addReport()" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/queryQualityInfoReport.json";
	var title = null;
	var columns = [
				{header: "填报人", dataIndex: 'REPORT_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "经销商编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "用户姓名", dataIndex: 'CTM_NAME', align:'center'},
				{header: "用户电话", dataIndex: 'CTM_PHONE', align:'center'},
				{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, align:'center',dataIndex: 'QUALITY_REPORT_ID',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95531001 || record.data.VERIFY_STATUS == 95531006 || record.data.VERIFY_STATUS == 95531004){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='updateQuality(\""+ value +"\")' value='修改'/><input name='delBtn' type='button' class='normal_btn' onclick='delCheck(\""+ value +"\")' value='删除'/><input name='detailBtn' type='button' class='normal_btn' onclick='printDetail(\""+ value +"\")' value='打印'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='printDetail(\""+ value +"\")' value='打印'/>");
		}
	}
	function updateQuality(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/updateQualityInfoReport.do?qualityReportId='+value ;
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/viewQualityInfoReport.do?qualityReportId='+value ;
	}
	function printDetail(value){		
		var tarUrl ='<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/printQualityInfoReport.do?qualityReportId='+value ;
		window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}
	
	//申请PIN查询VIN
	function addReport(){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/updateQualityInfoReport.do";
	}
	var delValue = null;
	function delCheck(del){
		delValue = del;
		MyConfirm("是否确认删除？",delsubmit,"");
	}

	function delsubmit(){
		makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/delQualityInfoReport.json?ids='+delValue,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success=='true'){
			document.getElementById("delBtn").disabled = true;
			MyAlertForFun("删除成功",sendPage);
		}else{
			MyAlert("删除失败!请联系管理员");
			document.getElementById("delBtn").disabled = false;
		}
	}
		
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/qualityInfoReportInit.do";
	}
</script>
<!--页面列表 end -->
</body>
</html>