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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>实销信息查询OEM</title>
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/sales/salesInfoManage/SalesInfoQuery/salesInfoQuery.json?COMMAND=1";
var title = null;
var columns = [
			{header: "区域", dataIndex: 'ORG_NAME', align:'left'},
			{header: "省份", dataIndex: 'REGION_NAME', align:'left'},
			{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'left'},
			{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'left'},
			{header: "客户名称", dataIndex: 'CTM_NAME', renderer:mySelect,align:'left'},
			{header: "联系人", dataIndex: 'LINK_MAN', align:'left'},
			{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
			//{header: "是否集团客户", dataIndex: 'IS_FLEET', align:'center',renderer:getItemValue},
			//{header: "集团客户名称", dataIndex: 'FLEET_NAME', align:'center'},
			{header: "主要联系电话", dataIndex: 'MAIN_PHONE', align:'left'},
			{header: "车系名称", dataIndex: 'VS', align:'left'},
			{header: "车型名称", dataIndex: 'VT', align:'left'},
			{header: "状态名称", dataIndex: 'SN', align:'left'},
			{header: "颜色", dataIndex: 'COLOR', align:'left'},
			{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'left'},
			/* {header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'left'}, */
			{header: "VIN", dataIndex: 'VIN', align:'left'},
			{header: "发动机号", dataIndex: 'ENGINE_NO', align:'left'},
			{header: "车辆性质", dataIndex: 'CAR_CHARACTOR', align:'center',renderer:getItemValue},
			{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
			{header: "实销时间", dataIndex: 'SALES_DATE', align:'center'},
			{header: "购置方式", dataIndex: 'PAYMENT', align:'left'},
			{header: "按揭类型", dataIndex: 'MORTGAGE_TYPE', align:'left'},
			{header: "销售价格", dataIndex: 'PRICE', align:'left'},
			//{header: "首付比例", dataIndex: 'SHOUFU_RATIO', align:'left',renderer:myLink},
			//{header: "贷款方式", dataIndex: 'LOANS_TYPE', align:'left'},
			{header: "贷款年限", dataIndex: 'LOANS_YEAR', align:'left'},
			{header: "贷款金额", dataIndex: 'MONEY', align:'left'}
			//{header: "情报/老客户姓名", dataIndex: 'OLD_CUSTOMER_NAME', align:'center'},
			//{header: "情报/老客户电话", dataIndex: 'OLD_TELEPHONE', align:'center'},
			//{header: "老客户车架号", dataIndex: 'OLD_VEHICLE_ID', align:'center'}
	      ];
function mySelect(value,meta,record){
	var data = record.data;
  	return String.format("<a href=\"#\" onclick='customerInfo(\""+data.ORDER_ID+"\",\""+data.CTM_ID+"\",\""+data.VEHICLE_ID+"\")';>"+value+"</a>");
}
function customerInfo(order_id,ctm_id,vehicle_id){
	OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/customerInfoQuery.do?order_id='+order_id+'&ctm_id='+ctm_id+'&vehicle_id='+vehicle_id,700,500);
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
	}
}
function detailCusterDownload(){
	var fsm = document.getElementById("fm");
	fsm.action= "<%=request.getContextPath()%>/sales/salesInfoManage/SalesInfoQuery/cusDownLoad.json";
	fsm.submit();
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 实销信息管理 &gt; 实销信息查询</div>
	<form id="fm" name="fm"  method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
			<h2>实销信息查询</h2>
			<div class="form-body">	
				<table class="table_query"  class="center">
				    <tr>
				      <td class="right" width="12%">客户类型：</td>
				      <td> 
				      	<label>
							<script type="text/javascript">
								genSelBoxExp("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",true,"",'',"false",'');
							</script>
						</label>
				      </td>
				      <td class="right"  width="12%">客户名称：</td>
				      <td>
				        	<input id="customer_name" name="customer_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
				      </td>
				      <td width="13%" class="right">VIN：</td>
				      <td  rowspan="2"><textarea name="vin" rows="3" cols="18"  class="form-control" style="width:150px;"></textarea></td>
				    </tr>
				    <tr>
				      <td class="right">客户电话：</td>
				      <td >
				        <input id="customer_phone" name="customer_phone" type="text" class="middle_txt" datatype="1,is_digit,15" size="20" maxlength="60" />
				      </td>
				     
				      <td class="right">选择物料组：</td>
				      <td>
							<input type="text" class="short_txt" name="materialCode" id="materialCode" onclick="showMaterialGroup('materialCode','','true','','true');" size="10" value=""  />
		       				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" /> 
					  </td>
				      <td class="right"><p>&nbsp;</p></td>
				    </tr>
				    <tr>
				      <td class="right">上报日期：</td>
				      <td> 
						<div class="left">
		            		<input name="startDate"  id="t1" value="${date }" type="text" class="short_txt"   onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})" style="cursor: pointer;width: 80px;"/>
		            		至
		            		<input name="endDate"  id="t2" value="${date }" type="text" class="short_txt"   onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
		           		</div>	
					 </td>
				     <td class="right">选择经销商：</td>
					 <td>
						<input type="text" class="short_txt" name="dealerCode" id="dealerCode" size="15" value="" onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" /><!-- onclick="showCompany('<%=contextPath %>')" -->
						<!--<c:if test="${dutyType!=10431005}">
							<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button"  value="..." 
								onclick="showOrgDealer('dealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
						</c:if> -->
						<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
					</td>
					<td class="right">
				      	购置方式：
				      </td>
				      <td>
					      <select id="payment" name="payment"  class="u-select">
						      <option value=" ">--请选择--</option>
						      <option  value="10361002" title="一次性付款">一次性付款</option><option value="10361003" title="按揭">按揭</option>
						      <option value="10361004" title="二手车置换">二手车置换</option>
						      <option value="10361005" title="二手车置换转按揭">二手车置换转按揭</option>
					      </select>
				      </td>	
				    </tr>
				    <tr> 
					<td class="right">联系地址：</td>
			        <td>
			      	<input type="text" name="linkAddr"  id="linkAddr"  class="middle_txt" datatype="1,is_textarea,30" size="30" maxlength="30"/>
			        </td>
			        <td class="right">车辆性质：</td>
			        <td colspan="3">
			    		<script type="text/javascript">
								genSelBoxExp("vchlPro",1072,'',true,"",'',"false",'');
						</script>
					</td>
				    </tr>
				    <tr>
				    <td colspan="6"  class="center">
				    	<input  type="button" class="u-button u-query"  onclick="__extQuery__(1);" value="查询" />&nbsp;&nbsp;&nbsp;
				    	<input  type="button"  class="u-button u-submit" onclick="detailCusterDownload();" value="实销下载" /></td>
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