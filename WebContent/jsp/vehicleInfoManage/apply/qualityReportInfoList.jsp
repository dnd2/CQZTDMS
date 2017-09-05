<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>品质情报上报</title>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/qualityReportInfo.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,class:'center',renderer:getIndex},
		{id:'action', header: "操作", sortable: false, class:'center',dataIndex: 'QUALITY_ID',renderer:myLink},
		{header: "填报人", dataIndex: 'REPORT_NAME', class:'center'},
		{header: "联系电话", dataIndex: 'CONTACT_TYPE', class:'center'},
		{header: "经销商编码", dataIndex: 'DEALERCODE', class:'center'},
		{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', class:'center'},
		{header: "车型", dataIndex: 'CAR_TYPE', class:'center'},
		{header: "VIN", dataIndex: 'VIN', class:'center'},
		{header: "故障件代码", dataIndex: 'FIRST_PROBLEM_CODE', class:'center'},
		{header: "故障件名称", dataIndex: 'FIRST_PROBLEM_NAME', class:'center'},
		{header: "状态", dataIndex: 'VERIFY_STATUS', class:'center',renderer:getItemValue},
		{header: "审核人", dataIndex: 'AUDIT_NAME', class:'center',renderer:myaudit},
		{header: "审核时间", dataIndex: 'AUDIT_DATE', class:'center',renderer:myauditdate},
		{header: "填报日期", dataIndex: 'REPORT_DATE', class:'center'}
	];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95531001  || record.data.VERIFY_STATUS == 95531004){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='updateQuality(\""+ value +"\")' value='修改'/><input name='delBtn' type='button' class='normal_btn' onclick='delCheck(\""+ value +"\")' value='删除'/><input name='detailBtn' type='button' class='normal_btn' onclick='printDetail(\""+ value +"\")' value='打印'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='printDetail(\""+ value +"\")' value='打印'/>");
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
	
	function add(){
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/saveOrupdateInfoReportInit.do' ;
	}


	function updateQuality(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/updateQualityInfoReport.do?type=update&qualityid='+value ;
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/viewQualityInfoReport.do?qualityid='+value ;
	}
	function printDetail(value){		
		var tarUrl ='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/printQualityInfoReport.do?qualityid='+value ;
		window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}
	var qualityid = null;
	function delCheck(del){
		qualityid = del;
		MyConfirm("是否确认删除？",delsubmit,"");
	}

	function delsubmit(){
		makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/delQualityInfoReport.json?qualityid='+qualityid,delBack,'fm','');
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
		window.location.href= "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/qualityReportInfoInit.do";
	}
	$(document).ready(function(){
		__extQuery__(1);
		loadcalendar();
	});
</script>
</head>
<body >
<div class="wbox">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量信息查询
</div>
<form name="fm" id="fm">
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
<!-- 查询条件 begin -->
<table  class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td class="right">VIN：</td>
      	<td class="left" ><input name="vin" type="text" id="vin" maxlength="30" class="middle_txt"/></td>
        <td class="right">用户姓名：</td>
      	<td class="left" ><input name="username" type="text" id="username" class="middle_txt" maxlength="25"/></td>
      	<td class="right">填报人：</td>
      	<td class="left" ><input name="reportname" type="text" id="reportname" class="middle_txt" maxlength="25"/></td>
    </tr>
    <tr>
    	<td class="right">审批状态：</td>
     	<td class="left" >
     		<script type="text/javascript">
  					genSelBoxExp("status",<%=Constant.Quality_Verify%>,"",true,"","","false",'');
  			</script>
     	</td>
     	<td class="right">审核人：</td>
     	<td class="left" >
     		<input class="middle_txt" id="jsaudit"  name="jsaudit" type="text" maxlength="15"/>
     	</td>
        <td class="right" >填报日期：</td>
        <td class="left" >
            <input name="reportDate1" type="text" class="middle_txt" id="reportDate1" /> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'reportDate1', false);" />  	
             &nbsp;至&nbsp; 
             <input name="reportDate2" type="text" class="middle_txt" id="reportDate2" /> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'reportDate2', false);" /> 
         </td>
    </tr>
    <tr>
    	<td colspan="11" class="center">
    		<input type="button" name="btnQuery" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1)" >
    		<input type="button" name="btnAdd" id="btnAdd" onclick="add();" value="新增"  class="u-button u-submit">
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
</body>
</html>