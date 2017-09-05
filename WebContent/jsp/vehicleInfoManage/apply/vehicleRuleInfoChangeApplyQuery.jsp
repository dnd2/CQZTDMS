<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包期信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包期信息变更申请
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin" class="middle_txt"/></td>
      	<td class="table_query_2Col_label_6Letter">车牌号：</td>
      	<td><input name="vehicleNo" type="text" id="vehicleNo"  class="middle_txt"/></td>
      	<td class="table_query_2Col_label_6Letter">申请状态：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.VEHICLE_CHANGE_STATUS%>,"",true,"short_sel","","false",'');
        </script>
      </td>
    </tr>
    <tr>
          <td class="table_query_2Col_label_5Letter">提报日期：</td>
          <td align="left" nowrap="true">
			<input name="beginDate" type="text" class="short_time_txt" id="t1" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't1', false);" />  	
             &nbsp;至&nbsp; <input name="endDate" type="text" class="short_time_txt" id="t2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't2', false);" /> 
		</td>	
    	<td></td>
    	<td></td>
    	<td></td>
    	<td></td>
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		<input type="button" name="BtnAdd" id="queryBtn"  value="新增"  class="normal_btn" onClick="addRecord()" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/vehicleRuleInfoChangeApplyInit.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "当前里程", dataIndex: 'MILEAGE', align:'center'},
				{header: "保修期月份", dataIndex: 'CLAIM_MONTH', align:'center'},
				{header: "保修里程", dataIndex: 'CLAIM_MELIEAGE', align:'center'},
				{header: "更改三包期", dataIndex: 'CLAIM_MONTH_NEW', align:'center'},
				{header: "更改三包里程", dataIndex: 'CLAIM_MELIEAGE_NEW', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{id:'action', header: "操作", sortable: false, dataIndex: 'STATUS', renderer:oper, align:'center'}
		      ];
		      	
	//采购订单详细页面
	function addRecord() {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/ruleChangeApplyAdd.do';
		fm.action = url;
		fm.submit();
	}
	//点击索赔申请
	function oper(value,meta,record) {
		if (value == <%=Constant.VEHICLE_CHANGE_STATUS_01%>||value==<%=Constant.VEHICLE_CHANGE_STATUS_04%>) {
			return String.format("<a href=\"#\" onclick='viewModDetail(\""+record.data.ID+"\",\""+record.data.VIN+"\")'>[修改]</a>");
		} else {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\",\""+record.data.VIN+"\")'>[明细]</a>");
		}
	}
	//详细信息修改页面
	function viewModDetail(id,vin) {
		window.location.href='<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/ruleChangeApplyModify.do?id='+id+'&vin='+vin;
		//fm.method = 'post';
		//fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/ruleChangeApplyModify.do?id='+id;
		//fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id,vin) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/ruleChangeApplyView.do?viewFlag=1&id='+id+'&vin='+vin;
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>