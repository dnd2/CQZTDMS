<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二手车置换申请</title>
<script type="text/javascript">
<!--
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 二手车置换申请</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_edit">
		<tr>
		<td colspan="3" align="center"><font color="red" size="2">流程提示：对于长安车以旧换新，和报废直接做二手车置换申请。对于非长安车做以旧换新做新增处理申请。</font> </td>
		</tr>
		</table>
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">VIN：</div></td>
				<td width="39%" >
      				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="hidden" name="vehicle_id" id="vehicle_id" />
					<input type="hidden" name="dealer_id" id="dealer_id" />
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "事业部 ", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "实销日期", dataIndex: 'SALES_DATE', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'VEHICLE_ID',renderer:myLink}
		      ];
		      
	function myLink(vehicle_id,metaDate,record){
		var dealerId = record.data.DEALER_ID ;
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + vehicle_id + "," + dealerId + ");\">[二手车置换申请]</a>&nbsp;<a href=\"#\" onclick=\"chkArea2(" + vehicle_id + "," + dealerId + ");\">[非长安汽车申请]</a>");        		 
    }

	function chkArea(vehicle_id, dealerId) {
		document.getElementById('vehicle_id').value = vehicle_id ;
		document.getElementById('dealer_id').value = dealerId ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementCarInfo.do" ;
		$('fm').action=url;
		$('fm').submit();
		//makeCall(url, answer, {vehicleId: vehicle_id}) ;
	}
	function chkArea2(vehicle_id, dealerId) {
		document.getElementById('vehicle_id').value = vehicle_id ;
		document.getElementById('dealer_id').value = dealerId ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementCarInfo2.do" ;
		$('fm').action=url;
		$('fm').submit();
	}
	
	function carAdd(){
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementInsertCarInit.do";
		$('fm').submit();
	}

	function answer(json) {
		var flag = json.vFlag ;

		if(flag == "0" && ${oemFlag == 0}) {  // 微车控制
			MyAlert("<strong><font color=\"red\">车辆业务范围与经销商业务范围不一致，为避免操作后的数据错误，请联系业务人员进行修改后再进行操作！</font><strong>") ;

			return ;
		} 
		
		$('fm').action= "<%=contextPath%>/sales/customerInfoManage/SalesReport/toReport.do" ;
		$('fm').submit();
	}
</script>    
</body>
</html>