<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>

<title>实销信息查询OEM</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 实销信息管理 &gt; 实销信息查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" width="95%" align="center">
		<!-- <tr>
			<td  colspan="6"><font color="red">注意事项:</font></td>
		</tr>
		<tr>
			<td  colspan="6"><font color="red">1、实销变更和实销退车审核流程：实销信息变更——由区域销售主管在dms系统的“实销信息变更审核”中进行审核,非质量问题实销退车申请--dms系统做“实销退车申请”后，将区域经理、大区经理签字后的工单照片、错误发票、正确发票照片交售后服务部陈力审核,因质量问题实销退车申请--dms系统做“实销退车申请”后，将现场工程师签字的工单照片交售后服务部陈力审核。</font></td>
		</tr>
		<tr>
			<td  colspan="6"><font color="red">2、实销变更和实销退车适用范围：客户名称变更、大客户错录成一般零售、vin号变更、客户退换车必须走实销退车申请流程，其他信息变更走实销变更申请流程。</font></td>
		</tr> -->
		    <tr>
		      <td align="right" width="12%">客户类型：</td>
		      <td> 
		      	<label>
					<script type="text/javascript">
						genSelBoxExp("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
				</label>
		      </td>
		      <td align="right" width="12%">客户名称：</td>
		      <td>
		        	<input id="customer_name" name="customer_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
		      </td>
		      <td width="13%" align="right">VIN：</td>
		      <td  rowspan="2"><textarea name="vin" rows="3" cols="18"></textarea></td>
		    </tr>
		    <tr>
		      <td align="right">客户电话：</td>
		      <td >
		        <input id="customer_phone" name="customer_phone" type="text" class="middle_txt" datatype="1,is_digit,15" size="20" maxlength="60" />
		      </td>
		     
		      <td align="right">选择物料组：</td>
		      <td>
					<input type="text" class="short_txt" name="materialCode" id="materialCode" size="10" value=""  />
       				<input type="button" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','','true','');" />
       				 <input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" /> 
			  </td>
		      <td align="right"><p>&nbsp;</p></td>
		    </tr>
		    <tr>
		      <td align="right">是否集团客户：</td>
		      <td>
		      	<label>
					<script type="text/javascript">
						genSelBoxExp("is_fleet",<%=Constant.IF_TYPE%>,"",true,"short_sel","onchange='changeFleet(this.value)'","false",'');
					</script>
				</label>
		      </td>
		      <td align="right" id="fleet_name_id1">集团客户名称：</td>
		      <td id="fleet_name_id2">
		        <input id="fleet_name" name="fleet_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
		      </td>
		      
		      <td align="right" id="contract_no1">集团客户合同：</td>
		      <td id="contract_no2">
		        <input id="contract_no" name="contract_no" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
		      </td>
		    </tr>
		    <tr>
		      <td align="right">上报日期：</td>
		      <td> 
					<div align="left">
	            		<input name="startDate" id="t1" value="${date }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
	            		&nbsp;至&nbsp;
	            		<input name="endDate" id="t2" value="${date }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
            		</div>	
				</td>
				<td align="right">选择区域：</td>
				 <td>
            <input type="text" class="short_txt" id="orgCode" name="orgCode" value="" size="15" />
			<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
			<input class="normal_btn" type="button" value="清空" onclick="txtClr('orgCode');"/>
		 </td>
		 <td align="right">选择经销商：</td>
		<td align="left">
			<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
      		<input name="dealerName" type="text" id="dealerName" class="short_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
		</td>
		     
			
		    </tr>
		    <tr>
		    	<td align='right'>审核状态(呼叫中心):</td>
		    	<td>
		    		<script type="text/javascript">
						genSelBoxExp("callcenterCheckStatus",<%=Constant.CALLCENTER_CHECK_STATUS%>,"",true,"short_sel","","false",'');
					</script>
		    	</td>
		    	
		    	<td align='right'>车辆性质:</td>
		    	<td>
		    		<script type="text/javascript">
						genSelBoxExp("SERVICEACTIVITY_CHARACTOR",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,"",true,"short_sel","","false",'');
					</script>
		    	</td>
		    </tr>
		     <tr>
		      <td align="right">&nbsp;</td>
		      <td align="left">&nbsp;
		      	<input type="hidden" name="comm" id="comm" value="1"/>
		      </td>
		     
		      <td align="right">&nbsp;</td>
		      <td align="right">&nbsp;</td>
		      <td align="right">&nbsp;</td>
		      <td align="right">&nbsp;</td>
		    </tr>
		    
		    <tr> 
		    <td align="right" style="display: none;">选择产地：</td>
		    <td style="display: none;">
		    	<select name="areaId" class="short_sel">
		    		<option value="">-请选择-</option>
						<c:forEach items="${areaList}" var="po">
							<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
						</c:forEach>
				</select>
			</td>
		    <td colspan="6" align="center">
		    	<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />&nbsp;&nbsp;&nbsp;
		    	<!-- <input name="button2" type=button class="cssbutton" onclick="checks();" value="审核" />-->
		    	</td>
		    </tr>
  	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/salesInfoCheckQuery.json";
	
	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'ORDER_ID', align:'left', renderer: getIndex},
				//{id:'action',header: "<input type='checkbox' name='check' id='check' onclick='checkAll();' />",dataIndex:'ORDER_ID', align:'left',renderer:setCheck},
				{header: "区域", dataIndex: 'ORG_NAME', align:'left'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'left'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'left'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'left'},
				{header: "客户名称", dataIndex: 'CTM_NAME', renderer:mySelect,align:'left'},
				{header: "审核状态(呼叫中心)", dataIndex: 'CALLCENTER_CHECK_STATUS_DESC',align:'left'},
				//{header: "是否二手车置换", dataIndex: 'IS_SECOND', align:'center',renderer:getItemValue},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "是否集团客户", dataIndex: 'IS_FLEET', align:'center',renderer:getItemValue},
				{header: "集团客户名称", dataIndex: 'FLEET_NAME', align:'center'},
				{header: "主要联系电话", dataIndex: 'MAIN_PHONE', align:'left'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'left'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'left'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'left'},
				{header: "配置名称", dataIndex: 'PACKAGE_NAME', align:'left'},
				{header: "颜色名称", dataIndex: 'COLOR_NAME', align:'left'},
				/* {header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'left'}, */
				{header: "VIN", dataIndex: 'VIN', align:'left'},
			
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'left'},
				{header: "车辆性质", dataIndex: 'CAR_CHARACTOR', align:'center',renderer:getItemValue},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "实销时间", dataIndex: 'SALES_DATE', align:'center'},
			
				{header: "付款方式", dataIndex: 'PAYMENT', align:'left',renderer:getItemValue},
				{header: "按揭类型", dataIndex: 'MORTGAGE_TYPE', align:'left',renderer:getItemValue},
				{header: "销售价格", dataIndex: 'PRICE', align:'left'},
				{header: "首付比例", dataIndex: 'SHOUFU_RATIO', align:'left',renderer:myLink},
				{header: "贷款方式", dataIndex: 'LOANS_TYPE', align:'left',renderer:getItemValue},
				{header: "贷款年限", dataIndex: 'LOANS_YEAR', align:'left'}
		      ];
	function setCheck(value,meta,record){
		return "<input type='checkbox' name='checkbox' value='"+record.data.ORDER_ID+","+record.data.CALLCENTER_CHECK_STATUS+"' />";
	}
	
	function mySelect(value,meta,record){
		var data = record.data;
	  	return String.format("<a href=\"#\" onclick='customerInfo(\""+data.CTM_ID+"\",\""+data.VEHICLE_ID+"\",\""+data.ORDER_ID+"\",\""+data.CALLCENTER_CHECK_STATUS+"\",1)';>"+value+"</a>");
	}
	function customerInfo(ctm_id,vehicle_id,order_id,CHECK_STATUS,comm){
		//MyAlert(CHECK_STATUS);
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/customerInfoQuery.do?ctm_id='+ctm_id+'&vehicle_id='+vehicle_id+'&order_id='+order_id+'&check_status='+CHECK_STATUS+'&comm='+comm,700,500);
	}
	function changeFleet(value){
		var yes = '<%=yes%>';
		if(value == yes || value == ""){
			document.getElementById("fleet_name_id1").style.display = "inline";
			document.getElementById("fleet_name_id2").style.display = "inline";
			document.getElementById("contract_no1").style.display = "inline";
			document.getElementById("contract_no2").style.display = "inline";
		}else{
			document.getElementById("fleet_name_id1").style.display = "none";
			document.getElementById("fleet_name_id2").style.display = "none";
			document.getElementById("contract_no1").style.display = "none";
			document.getElementById("contract_no2").style.display = "none";
		}
	}
	function myLink(value){
		if(value==""||value=="0"){
			return String.format("");
			}
		else{
			return String.format(value+"%");
			}}
	
	
	//批量审核
	function checks(){
		var checkbox = document.getElementsByName("checkbox");
		var checkIds = "";
		for(var i = 0;i < checkbox.length;i++){
			if(checkbox[i].checked){
				var values =  checkbox[i].value.split(",");
				if(values[1] == <%=Constant.CALLCENTER_CHECK_STATUS_01 %>) {
					checkIds += values[0]+",";
				} else {
					MyAlert("只能操作待审核的数据!");
					return;
				}
			}
		}
		if(checkIds.lastIndexOf(",") == checkIds.length - 1){
			checkIds = checkIds.substring(0, checkIds.length - 1);
		}
		if(checkIds == "" || checkIds.length <= 0){
			MyAlert("请选择审核的数据!");
			return;
		}
		OpenHtmlWindow('<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/salesInfoCheck.do?comm=1&ids='+checkIds,800,300);
	}
	
	function checkAll(){
		var isCheck = $("check").checked;
		var checkbox = document.getElementsByName("checkbox");
		for(var i = 0;i < checkbox.length;i++){
			if(isCheck){
				checkbox[i].checked = true;
			}else{
				checkbox[i].checked = false;
			}
		}
	}
	
	function queryInfo(){
		__extQuery__(1);
	}
	
	function changeValue(value,meta,record){
		var data = record.data;//callcenterCheckStatus
		if(data.CALLCENTER_CHECK_STATUS == <%=Constant.CALLCENTER_CHECK_STATUS_01 %>){
			return String.format("<a href='#' onclick='checkAudit(\""+data.ORDER_ID+"\")'>审核</a>");
		} else if(data.CALLCENTER_CHECK_STATUS == <%=Constant.CALLCENTER_CHECK_STATUS_02 %>){
			return String.format("<a href='#' onclick='updateInfo(\""+data.VEHICLE_ID+"\")'>修改</a>");
		} else {
			return String.format("-");
		}
	}
	
	//审核
	function checkAudit(orderId){
		OpenHtmlWindow('<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/salesInfoCheck.do?comm=1&ids='+orderId,800,300);
	}
	
	//修改
	function updateInfo(vehicleId){
		fm.action = "<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/updateSalesInfo.do?vehicle_id="+vehicleId;
		fm.submit();
	}
</script>    
</body>
</html>