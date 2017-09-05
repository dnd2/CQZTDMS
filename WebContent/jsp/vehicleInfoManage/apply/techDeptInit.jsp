<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>技术部门</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;技术部门部（车厂）
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
	</tr><tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">用户名称：</td>
     	<td><input name="dealername" type="text" id="dealername" class="middle_txt"/></td>
		<td class="table_query_2Col_label_5Letter" nowrap="true">审批状态：</td>
     	<td>
     		<script type="text/javascript">
        		genSelBoxExp("status",<%=Constant.BARCODE_APPLY_STATUS%>,"",true,"short_sel","","false",'');
       		</script>
     	</td>
    </tr>

    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		<input type="button" name="btnAdd" id="btnAdd" onclick="add();" value="新增"  class="normal_btn">
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/data1.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
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
		{header: "状态", dataIndex: 'VERIFY_STATUS', align:'center',renderer:getItemValue},
		{id:'action', header: "操作", sortable: false, align:'center',dataIndex: 'QUALITY_ID',renderer:myLink}
	];
	function myLink(value,meta,record){
		if(record.data.VERIFY_STATUS == 95451001 || record.data.VERIFY_STATUS == 95451003){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='详细'/><input name='detailBtn' type='button' class='normal_btn' onclick='update1(\""+ value +"\")' value='修改'/><input name='delBtn' type='button' class='normal_btn' onclick='delCheck(\""+ value +"\")' value='删除'/><input name='detailBtn' type='button' class='normal_btn' onclick='printDetail(\""+ value +"\")' value='打印'/>");
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
		window.location.href = "<%=contextPath%>/jsp/vehicleInfoManage/apply/saveOrupdateTechnicalDept.jsp";
	}


	function update1(value){	
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/updateQualityInfoReport1.do?qualityid='+value ;
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/viewQualityInfoReport4.do?qualityid='+value ;
	}
	function printDetail(value){		
		var tarUrl ='<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/printQualityInfoReport1.do?qualityid='+value ;
		window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}
	var qualityid = null;
	function delCheck(del){
		qualityid = del;
		MyConfirm("是否确认删除？",delsubmit,"");
	}

	function delsubmit(){
		makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/delQualityInfoReport1.json?qualityid='+qualityid,delBack,'fm','');
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
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/techDeptInit.do";
	}
</script>
<!--页面列表 end -->
</html>