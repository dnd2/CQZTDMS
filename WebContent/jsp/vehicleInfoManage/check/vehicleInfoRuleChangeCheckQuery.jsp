<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆三包信息变更审核
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<input type="hidden" id="checkQueryStatus" name="checkQueryStatus" value="1"/><!-- 车辆信息变更审核查询页面标志 -->
	<tr>
		<td align="right">VIN：</td>
      	<td><input name="vin" type="text" id="vin" class="middle_txt"/></td>
      	<td align="right">车牌号：</td>
      	<td><input name="vehicleNo" type="text" id="vehicleNo"  class="middle_txt"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">提报日期：</td>
      <td align="left" nowrap="true">
			<input name="beginDate" type="text" class="short_time_txt" id="t1" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't1', false);" />  	
             &nbsp;至&nbsp; <input name="endDate" type="text" class="short_time_txt" id="t2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't2', false);" /> 
		</td>	
      	<td align="right">申请状态：</td>
      	<td>
      		<script type="text/javascript">
        		genSelBoxExp("status",<%=Constant.VEHICLE_CHANGE_STATUS%>,"<%=Constant.VEHICLE_CHANGE_STATUS_02%>",true,"short_sel","","false",'<%=Constant.VEHICLE_CHANGE_STATUS_01%>');
        	</script>
      	</td>
    </tr>
    <tr>
    	<td nowrap="nowrap" align="right">提报经销商：</td>
        <td nowrap="nowrap">
        	<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" readonly="readonly" />
            <input class="mini_btn" type="button" id="dealerBtn" value="&hellip;"onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');"/> 
           	<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           	<input class="normal_btn" type="button" value="清空" onclick="clrDlr('dealerId', 'dealerCode');"/>
        </td>
        <td align="right" nowrap="nowrap" >&nbsp;</td>
        <td nowrap="nowrap">
			&nbsp;
        </td>
	</tr>
	<tr>

    </tr>
    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="doSearch();" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/queryVehicleRuleChangeInfo.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				//{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center',renderer:formatDate},
				//{header: "经销商电话", dataIndex: 'PHONE', align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				//{header: "当前里程", dataIndex: 'MILEAGE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_CODE', align:'center'},
				/*{header: "原三包期", dataIndex: 'CLAIM_MONTH', align:'center'},
				{header: "原三包里程", dataIndex: 'CLAIM_MELIEAGE', align:'center'},
				{header: "申请三包期", dataIndex: 'CLAIM_MONTH_NEW', align:'center'},
				{header: "申请三包里程", dataIndex: 'CLAIM_MELIEAGE_NEW', align:'center'},*/
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "提报时间", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, dataIndex: 'STATUS', renderer:oper, align:'center'}
		      ];
			
	//采购订单详细页面
	function addRecord() {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/applyAdd.do';
		fm.action = url;
		fm.submit();
	}
	//点击索赔申请
	function oper(value,meta,record) {
		if (value == <%=Constant.VEHICLE_CHANGE_STATUS_02%>) {
			return String.format("<a href=\"#\" onclick='viewModDetail(\""+record.data.ID+"\",\""+record.data.VIN+"\",\""+record.data.PART_CODE+"\")'>[审核]</a>");
		} else {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\",\""+record.data.VIN+"\",\""+record.data.PART_CODE+"\")'>[明细]</a>");
		}
	}
	//详细信息审核页面
	function viewModDetail(id,vin,partCode) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/queryVehicleRuleChangeInfoDetail.do?viewFlag=0&changeId='+id+'&vin='+vin+'&partCode='+partCode;
		fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id,vin,partCode) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/queryVehicleRuleChangeDetailView.do?viewFlag=1&changeId='+id+'&vin='+vin+'&partCode='+partCode;
		fm.submit();
	}
	//清空经销商
	function clrDlr(dealerId, dealerCode) {
		document.getElementById(dealerId).value="";
		document.getElementById(dealerCode).value="";
	}
	//格式化时间为YYYY-MM-DD
	   function formatDate(value,meta,record) {
		 if (value==""||value==null) {
			return "";
		 }else {
			return value.substr(0,10);
		 }
	   }
	//查询
	function doSearch(){
		if($('status').value=='<%=Constant.VEHICLE_CHANGE_STATUS_03%>'||$('status').value=='<%=Constant.VEHICLE_CHANGE_STATUS_04%>'){
			columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				//{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center',renderer:formatDate},
				//{header: "经销商电话", dataIndex: 'PHONE', align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				//{header: "当前里程", dataIndex: 'MILEAGE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_CODE', align:'center'},
				/*{header: "原三包期", dataIndex: 'CLAIM_MONTH', align:'center'},
				{header: "原三包里程", dataIndex: 'CLAIM_MELIEAGE', align:'center'},
				{header: "申请三包期", dataIndex: 'CLAIM_MONTH_NEW', align:'center'},
				{header: "申请三包里程", dataIndex: 'CLAIM_MELIEAGE_NEW', align:'center'},*/
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "提报时间", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, dataIndex: 'STATUS', renderer:oper, align:'center'}
		      ];
		}else{
			 columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				//{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center',renderer:formatDate},
				//{header: "经销商电话", dataIndex: 'PHONE', align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				//{header: "当前里程", dataIndex: 'MILEAGE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_CODE', align:'center'},
				/*{header: "原三包期", dataIndex: 'CLAIM_MONTH', align:'center'},
				{header: "原三包里程", dataIndex: 'CLAIM_MELIEAGE', align:'center'},
				{header: "申请三包期", dataIndex: 'CLAIM_MONTH_NEW', align:'center'},
				{header: "申请三包里程", dataIndex: 'CLAIM_MELIEAGE_NEW', align:'center'},*/
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "提报时间", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, dataIndex: 'STATUS', renderer:oper, align:'center'}
		      ];
		}  
		__extQuery__(1);    
	}
</script>
<!--页面列表 end -->
</body>
</html>