<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>批发申请</title>
	</head>
	<body onunload='javascript:destoryPrototype();'
		onload="changeFleet()">
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置： 整车销售 &gt; 库存管理 &gt; 批发申请
			</div>
			<form id="fm" name="fm" method="post">
				<input type="hidden" name="curPage" id="curPage" value="1" />
				<input type="hidden" id="dlrId" name="dlrId" value="" />
				<table class="table_query" border="0">


					<!--					<tr><td  colspan="3"><font color="red">1、不同大区之间经销商批发，需要车厂审核</font></td></tr>-->
					<tr>
						<td colspan="3">
							<font color="red">1、不同省系经销商批发，需要申请的省系先审核，然后接收的省系再审核</font>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<font color="red">2、同一省系经销商批发，需要省系片区审核</font>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<font color="red">3、一、二级经销商之间批发，不需要审核</font>
						</td>
					</tr>
					<tr>
						<td class="table_query_3Col_input">
							<input type="button" class="normal_btn"
								onclick="chooseVehicleDispatchInit(1);" value="  添  加  "
								id="queryBtn_add" />
						</td>
					</tr>
				</table>
				<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
				<div id="myGrid">
					<table style="border-bottom: 1px solid #DAE0EE" class="table_list"
						id="myTable">
						<tbody>
							<tr class="table_list_th">
								<th class="noSort" style="text-align: center;">

								</th>
								<th class="noSort">
									VIN
								</th>
								<th class="noSort">
									车辆业务范围
								</th>
								<th class="noSort">
									发动机号
								</th>
								<th class="noSort">
									物料代码
								</th>
								<th class="noSort">
									物料名称
								</th>
								<th class="noSort">
									验收日期
								</th>
								<th class="noSort">
									库存天数
								</th>
								<th class="noSort">
									操作
								</th>
							</tr>

						</tbody>
					</table>
				</div>
			</form>
		</div>
		<form name="form1" id="form1">
			<input type="hidden" name="dealerLevel" id="dealerLevel"
				value="${dealerLevel}" />
			<table class="table_query" width="85%" align="center" border="0"
				id="roll">
				<tr>
					<td align="left" width="45%">
						<strong><font color="red">“新位置”</font> </strong>:

						<select id="warehouseId" name="warehouseId">

						</select>
					</td>
					<td align="left">
						<input class=normal_btn type='button' name='saveResButton'
							onclick='dispatch();' value="保  存" />
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">

	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
		   
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='vehicleIds' value='" + value + "' />");
	}
	
	function changeFleet() {
    	var o = new ShowWare();
    	o.tagName = 'warehouseId';
    	o.init();
    }
	
	function rowDelete(value) {
	 var oTr = document.getElementById("tr" + value) ;
	 oTr.parentNode.removeChild(oTr) ;
	}
	
	function chooseVehicleDispatchInit() {
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleDispatch/chooseVehicleDispatchInit.do',850,500);
	}
	
	function dispatch(){
		var vehicleIds = document.getElementsByName("vehicleIds");
		if(vehicleIds.length > 0){
			MyConfirm("是否提交?",dispatchAction);
		}else{
			MyAlert("请选择车辆信息!");
		}
	}

	function dispatchAction(){
		var vehicleIds_ = document.getElementsByName("vehicleIds");
		var warehouseId = document.getElementById("warehouseId").value;
		var vehicleIds="";//批发车辆
		if(vehicleIds_.length){
			for(var i=0; i<vehicleIds_.length; i++){
				vehicleIds = vehicleIds+vehicleIds_[i].value;
				if(i<vehicleIds_.length-1){
					vehicleIds = vehicleIds + ","
				}
			}
		}
	
		makeNomalFormCall('/dms/sales/storageManage/VehicleDispatch/vehicleShiftLibrarySubmit.json?vehicleIds='+vehicleIds+'&warehouseId='+warehouseId,showResult,'form1');
	}
	function showResult(json){
		if(json.returnValue == '1'){
			 window.location.href = '/dms/sales/storageManage/VehicleDispatch/VehicleShiftLibraryInit.do';
		}else{
			MyAlert("申请失败！请联系系统管理员！");
		}
	}
	
</script>
	</body>
</html>