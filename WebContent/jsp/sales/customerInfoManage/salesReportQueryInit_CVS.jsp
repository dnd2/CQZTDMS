<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
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
</script>


<title>实销信息查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" width="100%" align="center">
		    <tr>
		      <td align="right" width="12%">客户类型：</td>
		      <td> 
		      	<label>
					<script type="text/javascript">
						genSelBoxExp("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
				</label>
		      </td>
		      <td align="right"  width="12%">客户名称：</td>
		      <td>
		        	<input id="customer_name" name="customer_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="30" />
		      </td>
		      <td width="13%" align="right">VIN：</td>
		      <td rowspan="2"><textarea name="vin" rows="3" cols="18"></textarea></td>
		    </tr>
		    <tr>
		      <td align="right">客户电话：</td>
		      <td >
		        <input id="customer_phone" name="customer_phone" type="text" class="middle_txt" datatype="1,is_digit,15" size="15" maxlength="15" />
		      </td>
		     
		      <td align="right">选择物料组：</td>
		      <td>
					<input type="text" name="materialCode" id="materialCode" size="10" value=""  />
					<input type="hidden" name="materialName" size="20" id="materialName" value="" />
       				<input type="button" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','materialName','true','');" />
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
		        <input id="fleet_name" name="fleet_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="30" maxlength="30" />
		      </td>
		      
		      <td align="right" id="contract_no1">集团客户合同：</td>
		      <td id="contract_no2">
		        <input id="contract_no" name="contract_no" type="text" class="middle_txt" datatype="1,is_textarea,20" size="20" maxlength="20" />
		      </td>
		    </tr>
		    <tr>
		      <td align="right">上报日期：</td>
		      <td colspan="2">
					<div align="left">
	            		<input name="startDate" id="t1" value="${date }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" />
	            		<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />
	            		&nbsp;至&nbsp;
	            		<input name="endDate" id="t2" value="${date }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  />
	            		<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
            		</div>	
				</td>
		      <td><!--选择业务范围：<select name="areaId" class="short_sel">
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" />--></td>
		      <td>
		      </td>
		      <td>
		      <input name="button" type="button" id="queryBtn" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
		      <input name="button2" type=button class="cssbutton" onClick="detailCusterDownload();" value="实销下载">
		      </td>
		    </tr>
  	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
<script type="text/javascript">
function detailCusterDownload(){
	$('fm').action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/cusDealerDownLoad_CVS.json";
	$('fm').submit();
}
	var myPage;
	
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryReportInfo.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "客户名称", dataIndex: 'C_NAME', renderer:mySelect,align:'center'},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "是否集团客户", dataIndex: 'IS_FLEET', align:'center',renderer:getItemValue},
				{header: "销售顾问", dataIndex: 'SALES_CON_NAME', align:'center'},
				{header: "主要联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "实销时间", dataIndex: 'SALES_DATE', align:'center'}
		      ];
	function mySelect(value,meta,record){
		var data = record.data;
	  	return String.format("<a href=\"#\" onclick='customerInfo(\""+data.CTM_ID+"\",\""+data.VEHICLE_ID+"\")';>"+value+"</a>");
	}
	function customerInfo(ctm_id,vehicle_id){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/customerInfoQuery.do?ctm_id='+ctm_id+'&vehicle_id='+vehicle_id,700,500);
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
</script>    
</body>
</html>