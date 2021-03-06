<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
	String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/storageUtil/storageUtil.js"></script>
<title>车辆退回生产线查询</title>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>车辆退回生产线查询
	</div>
	<form name="fm" method="post" id="fm">
		<!-- 查询条件 begin -->
		<table class="table_query" id="subtab">
			<tr class="csstr" align="center">
				<td class="right" width="15%"><input type="radio" checked="checked" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
				<td align="left">
					<input type="text" maxlength="20" class="middle_txt" name="groupCode" size="15" value="" id="groupCode" />
					<input name="button1" id="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','');" value="..." />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');" />
				</td>
				<td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(2);" />选择物料：</td>
				<td align="left">
					<input type="text" maxlength="20" class="middle_txt" name="materialCode" size="15" value="" id="materialCode" />
					<input name="button2" id="button2" type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." disabled="disabled" />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');" />
				</td>
			</tr>
			<tr class="csstr" align="center">
				<td class="right" width="15%">产地：</td>
				<td align="left">
					<select name="YIELDLY" id="YIELDLY" class="u-select">
						<option value="">--请选择--</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
							</c:forEach>
						</c:if>
					</select>
				</td>		
				<td class="right" nowrap="true">VIN：</td>
				<td align="left"><input type="text" name="VIN" id="VIN" maxlength="20" class="middle_txt"/></td>	
			</tr>
			<tr class="csstr" align="center">				
				<td class="right">库区：</td>
				<td class="table_query_2Col_input">
					<select name="AREA_NAME" id="AREA_NAME" class="u-select" onchange="roadCheckBox();">
						<option value="">-请选择-</option>
						<c:if test="${list_An!=null}">
							<c:forEach items="${list_An}" var="list">
								<option value="${list.AREA_ID }">${list.AREA_NAME }</option>
							</c:forEach>
						</c:if>
					</select>
				</td>		
				<td class="right">退回类型：</td>
				<td align="left">
					<script type="text/javascript">
							genSelBoxExp("retreatType",<%=Constant.RETREAT_TYPE%>,"",true,"u-select","","false",'');
					</script>
				</td>
			</tr>
			<tr class="csstr" align="center">				
				<td class="right">库道：</td>
				<td class="table_query_2Col_input">
					<select name="ROAD_NAME" id="ROAD_NAME" class="u-select" onchange="sitCheckBox();">
						<option value="">-请选择-</option>
					</select>
				</td>
				<td class="right" nowrap="true">下线日期：</td>
				<td align="left" nowrap="true">
					<input name="OFFLINE_STARTDATE" type="text" maxlength="20" class="middle_txt" id="OFFLINE_STARTDATE" readonly="readonly" />
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_STARTDATE', false);" /> &nbsp;至&nbsp;
					<input name="OFFLINE_ENDDATE" type="text" maxlength="20" class="middle_txt" id="OFFLINE_ENDDATE" readonly="readonly" />
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OFFLINE_ENDDATE', false);" />
				</td>		
			</tr>
			<tr class="csstr" align="center">
				<td class="right">库位：</td>
				<td class="table_query_2Col_input">
					<select name="SIT_NAME" id="SIT_NAME" class="u-select">
						<option value="">-请选择-</option>
					</select>
				</td>			
				<td class="right" nowrap="true">退回日期：</td>
				<td align="left" nowrap="true">
					<input name="RETREAT_STARTDATE" type="text" maxlength="20" class="middle_txt" id="RETREAT_STARTDATE" readonly="readonly" />
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RETREAT_STARTDATE', false);" /> &nbsp;至&nbsp;
					<input name="RETREAT_ENDDATE" type="text" maxlength="20" class="middle_txt" id="RETREAT_ENDDATE" readonly="readonly" />
					<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RETREAT_ENDDATE', false);" />
				</td>
			</tr>
			<tr align="center">
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">					
					<input type="button" id="queryBtn" class="normal_btn" value="查询" onclick="__extQuery__(1);" />
					<input type="button" id="queryBtn1" class="normal_btn" value="导出" onclick="dataExport();" />
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
	<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleRetreat/vehicleRetreatQueryData.json?type=0";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "退回类型",dataIndex: 'RETREAT_TYPE',align:'center'},
				{header: "退回人",dataIndex: 'USER_NAME',align:'center'},
				{header: "退回日期",dataIndex: 'RETREAT_DATE', align:'center'},
				{header: "退回原因",dataIndex: 'RETREATDES',style:'text-align:left;'},
				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center',renderer:myVin},
				{header: "库区",dataIndex: 'AREA_NAME',align:'center'},
				{header: "库道",dataIndex: 'ROAD_NAME',align:'center'},
				{header: "库位",dataIndex: 'SIT_NAME',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "下线日期",dataIndex: 'OFFLINE_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化		
	}
	function customerFunc(){
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		if(arrayObj.length>0){//大于0表表示有数据，备注显示
			document.getElementById("tab_remark").style.display="";
			document.getElementById("tab_remark1").style.display="";
		}else{
			document.getElementById("tab_remark").style.display="none";
			document.getElementById("tab_remark1").style.display="none";
		}
	}
	function myVin(value,meta,record){
	  	return String.format("<a href='javascript:void(0);' onclick='vinInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vinInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storage/storagemanage/ReceivingStorage/queryVinDetail.do?vin='+ value, 600, 300);
	}
	//清空数据
	function clrTxt(txtId) {
		document.getElementById(txtId).value = "";
	}
	//全选checkbox
	function myCheckBox(value, metaDate, record) {
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' />");
	}
	function toChangeMaterial(parm) {
		if (parm == 1) {
			document.getElementById("button1").disabled = "";
			document.getElementById("button2").disabled = "disabled";
		} else {
			document.getElementById("button1").disabled = "disabled";
			document.getElementById("button2").disabled = "";
		}
	}
	//数据导出
	function dataExport() {
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/VehicleRetreat/vehicleRetreatQueryData.json?type=1";
		fm.submit();
	}

	</script>
</body>
</html>
