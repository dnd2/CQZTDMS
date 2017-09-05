<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin" class="middle_txt"/></td>
      	<td class="table_query_2Col_label_6Letter">车牌号：</td>
      	<td><input name="vehicleNo" type="text" id="vehicleNo"  class="middle_txt"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">申请类型：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("changeType",<%=Constant.VEHICLE_CHANGE_TYPE%>,"",true,"short_sel","","false",'');
        </script>			
      </td>
     
    </tr>
    <tr>
    	<td align="center" colspan="5">
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicleChangeInfo.json";
	var title = null;
	var columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车牌号", dataIndex: 'VEHICLE_NO', align:'center'},
				{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车主姓名", dataIndex: 'CTM_NAME', align:'center'},
				{header: "购车时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "当前里程", dataIndex: 'MILEAGE', align:'center'},
				{header: "保养次数", dataIndex: 'FREE_TIMES', align:'center'},
				{header: "申请类型", dataIndex: 'APPLY_TYPE', align:'center', renderer:getItemValue},
				{header: "修改后数据", dataIndex: 'APPLY_DATA', align:'center'},
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
		if (value == 13131001) {
		    return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>");
			//return String.format("<a href=\"#\" onclick='viewModDetail(\""+record.data.ID+"\")'>[修改]</a>");
		} else {
			MyAlert('页面异常！');
		}
	}
	//详细信息修改页面
	function viewModDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/modForward.do?id='+id;
		fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicleChangeInfoById.do?viewFlag=1&id='+id;
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>