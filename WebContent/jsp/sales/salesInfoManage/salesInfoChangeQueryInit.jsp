<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>
<script type="text/javascript">
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	var myPage;
	var url = "<%=contextPath%>/sales/salesInfoManage/SalesInfoChangeQuery/salesInfoChangeQueryList.json?COMMAND=1";
	var title = null;
	var columns = [
		{id:'action',header: "操作", wclass:'center',idth:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink},
				{header: "客户名称", dataIndex: 'C_NAME', renderer:mySelect,class:'center'},
				{header: "客户类型", dataIndex: 'CTM_TYPE', class:'center',renderer:getItemValue},
				{header: "是否集团客户", dataIndex: 'IS_FLEET', class:'center',renderer:getItemValue},
				{header: "主要联系电话", dataIndex: 'MAIN_PHONE', class:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', class:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', class:'center'},
				{header: "VIN", dataIndex: 'VIN', class:'center'},
				{header: "实销时间", dataIndex: 'SALES_DATE', class:'center'},
				{header: "状态", dataIndex: 'STATUS', class:'center',renderer:getItemValue}
				
		      ];
	function mySelect(value,meta,record){
		var data = record.data;
	  	return String.format("<a href=\"#\" onclick='customerInfo(\""+data.ORDER_ID+"\",\""+data.S_ID+"\",\""+data.IS_FLEET+"\",\""+data.VEHICLE_ID+"\")';>"+value+"</a>");
	}
	function myLink(vehicle_id,meta,record){
		var data = record.data;
	    return String.format("<a href=\"<%=contextPath%>/sales/salesInfoManage/SalesInfoChangeCheck/checkDetailInfo.do?vehicleId="+data.VEHICLE_ID+"&isFleet="+data.IS_FLEET+"&ctm_edit_id="+data.CTM_EDIT_ID+"&log_id="+data.LOG_ID+"\" class='u-anchor'>[查看]</a>");
	}
	function customerInfo(order_id,s_id,is_fleet,vehicle_id){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/customerInfoQuery.do?order_id='+order_id+'&s_id='+s_id+'&is_fleet='+is_fleet+'&vehicle_id='+vehicle_id,800,600);
	}
	function changeFleet(value){
		var yes = '<%=yes%>';
		if(value == yes || value == ""){
			document.getElementById("fleet_name_id1").style.display = "table-cell";
			document.getElementById("fleet_name_id2").style.display = "table-cell";
			document.getElementById("contract_no1").style.display = "table-cell";
			document.getElementById("contract_no2").style.display = "table-cell";
		}else{
			document.getElementById("fleet_name_id1").style.display = "none";
			document.getElementById("fleet_name_id2").style.display = "none";
			document.getElementById("contract_no1").style.display = "none";
			document.getElementById("contract_no2").style.display = "none";
		}
	}
</script>
<title>实销信息更改查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 实销信息管理 &gt; 实销信息更改查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>实销信息更改查询</h2>
		<div class="form-body">
			<table class="table_query"  class="center">
				<c:if test="${returnValue==2}">
			<tr>
			<td  colspan="6">
	<!--		<font color="red">注意事项:</font>-->
			</td>
		</tr>
	<!--		<tr>-->
	<!--			<td  colspan="6"><font color="red">1、实销变更和实销退车审核流程：实销信息变更——由区域销售主管在dms系统的“实销信息变更审核”中进行审核,非质量问题实销退车申请--dms系统做“实销退车申请”后，将区域经理、大区经理签字后的工单照片、错误发票、正确发票照片交售后服务部陈力审核,因质量问题实销退车申请--dms系统做“实销退车申请”后，将现场工程师签字的工单照片交售后服务部陈力审核。</font></td>-->
	<!--		</tr>-->
	<!--		<tr>-->
	<!--			<td  colspan="6"><font color="red">2、实销变更和实销退车适用范围：客户名称变更、大客户错录成一般零售、vin号变更、客户退换车必须走实销退车申请流程，其他信息变更走实销变更申请流程。</font></td>-->
	<!--		</tr>-->
			</c:if>
			    <tr>
			      <td class="right" width="12%">客户类型：</td>
			      <td> 
			      	<label>
						<script type="text/javascript">
							genSelBoxExp("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",true,"",'',"false",'');
						</script>
					</label>
			      </td>
			      <td class="right" width="12%">客户名称：</td>
			      <td>
			        	<input id="customer_name" name="customer_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
			      </td>
			      <td width="13%" class="right">VIN：</td>
			      <td rowspan="2"><textarea name="vin" rows="3" cols="18" ></textarea></td>
			    </tr>
			    <tr>
			      <td class="right">客户电话：</td>
			      <td >
			        <input id="customer_phone" name="customer_phone" type="text" class="middle_txt"  datatype="1,is_digit,15" size="20" maxlength="60" />
			      </td>
			     
			      <td class="right">选择物料组：</td>
			      <td>
						<input type="text" class="middle_txt" name="materialCode" id="materialCode" size="10" value=""  onclick="showMaterialGroup('materialCode','','true','','true');"/>
	       				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" /> 
				  </td>
			      <td class="right"><p>&nbsp;</p></td>
			    </tr>
			    <tr style="display: none;">
			      <td class="right">是否集团客户：</td>
			      <td>
			      	<label>
						<script type="text/javascript">
							genSelBoxExp("is_fleet",<%=Constant.IF_TYPE%>,"",true,"short_sel","onchange='changeFleet(this.value)'","false",'');
						</script>
					</label>
			      </td>
			      <td class="right" id="fleet_name_id1">集团客户名称：</td>
			      <td id="fleet_name_id2">
			        <input id="fleet_name" name="fleet_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
			      </td>
			      
			      <td class="right" id="contract_no1">集团客户合同：</td>
			      <td id="contract_no2">
			        <input id="contract_no" name="contract_no" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
			      </td>
			    </tr>
			    <tr>
			      <td class="right">上报日期：</td>
			      <td>
						<div class="left">
		            		<input name="startDate" id="t1" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
		            		&nbsp;至&nbsp;
		            		<input name="endDate" id="t2" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
	            		</div>	
					</td>
			     <td class="right">选择经销商：</td>
				<td>
					<input type="text" class="middle_txt" name="dealerCode" id="dealerCode" size="15" value="" onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
					<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
				</td>
				<td class="right">状态：</td>
			    <td>
			    	<label>
						<script type="text/javascript">
							genSelBoxExp("checkStatus",<%=Constant.SALES_INFO_CHANGE_STATUS%>,"",true,"",'',"false",'');
						</script>
					</label>
			    	</td>
			    </tr>
			    <tr>
			    <td class="right" style="display: none;">选择业务范围：</td>
			    <td style="display: none;"><select name="areaId" class="u-select">
						<c:forEach items="${areaList}" var="po">
							<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
						</c:forEach>
					</select>
					<input type="hidden" name="dealerId" id="dealerId" />
					<input type="hidden" name="area_id" id="area_id" value="" /></td>
			    	<td colspan="4" class="right">
			    		<input name="queryBtn" type="button"  class="u-button u-query"  onclick="__extQuery__(1);" value="查询" />
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