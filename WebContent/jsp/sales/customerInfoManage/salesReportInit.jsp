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
<title>实销信息上报</title>
<script type="text/javascript">
<!--
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
  var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/reportVehicleQuery.json?COMMAND=1";
	var title = null;
	var columns = [
		{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'VEHICLE_ID',renderer:myLink},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "配置", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "验收入库日期", dataIndex: 'STORAGE_DATE', align:'center'}
				
		      ];
	function myLink(vehicle_id){
        return String.format(
        		 "<a href=\"#\" class='u-anchor' onclick=\"chkArea(" + vehicle_id + ");\">[实销上报]</a>");
    }

	function chkArea(vehicle_id) {
		document.getElementById('vehicle_id').value = vehicle_id ;
		var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/chkArea.json" ;
		makeCall(url, answer, {vehicleId: vehicle_id}) ;
	}

	function answer(json) {
		var flag = json.vFlag ;
		if(flag == "0" && ${oemFlag == 0}) { 
			MyAlert("<strong><font color=\"red\">车辆业务范围与经销商业务范围不一致，为避免操作后的数据错误，请联系业务人员进行修改后再进行操作！</font><strong>") ;
			return ;
		} 
		var fsm = document.getElementById("fm");
		fsm.action= "<%=contextPath%>/sales/customerInfoManage/SalesReport/toReport.do" ;
		fsm.submit();
	}
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>实销信息上报</h2>
		<div class="form-body">
			<table class="table_query">
			<c:if test="${oemFlag == 0}">
			<tr><td colspan="3"><c:if test="${returnValue1==2}">
			<pre><strong><font color="red">各经销商注意:
	 在做车辆实销上报的时候,如果发现车辆库房和实际车辆的库房不一致或者单位不对是,必须按以下要求进行:
	 1、车辆库房和实际车辆的库房不一致,(比如河北的车在重庆的库房),请立即去做车辆位置变更后,库房位置变更正确后在录入实销上报.
	 2、发现车辆显示没有库房的(必须是系统已经入过库的车辆),请在整车销售 > 经销商库存管理 > 仓库维护,先做库房维护,再做车辆位置变更后,库房位置变更正确后在录入实销上报.
	 3、如发现车辆所在单位和本单位不符，不能做车辆位置变更的，请立即与ERP管理片管员联系处理。</font></strong>
	 </pre></c:if>
	 </td></tr>
			</c:if>
				<tr>
					<td width="20%" class="tblopt right">物料组选择：</td>
					<td>
						<input type="text" class="middle_txt" id="materialCode" name="materialCode"  value="" onclick="showMaterialGroup('materialCode','materialName','true','','true')" />
						<input type="hidden" name="materialName" size="20" id="materialName" value="" />
	       				<input type="button" class="u-button" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" /> 
					</td>
					<td></td>
				</tr>
				<tr>
					<td width="20%" class="tblopt right">VIN：</td>
					<td width="39%" >
	      				<textarea id="vin" name="vin" cols="18" rows="2" class="form-control" style="width:150px;"></textarea>
	    			</td>
					<td>
						<input type="hidden" name="vehicle_id" id="vehicle_id" />
						<input type="button" class="u-button u-query" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
					</td>
				</tr>
			</table>
		</div>
		</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
</body>
</html>