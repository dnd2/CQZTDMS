<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		   //初始化时间控件
	}
</script>
<title>车辆验收查询(DLR)</title>
</head>
<body> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  库存管理 &gt; 出入库管理 &gt; 车辆验收查询</div>
	<form id="fm" name="fm" method="post">
				<div class="form-panel">
				<h2>车辆验收查询</h2>
				<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		<tr>
			<td class="tblopt">
			<div align="right">发车日期：</div>
			</td>
			<td>
				<div align="left" width="20%">
						<input name="deliverystartDate" id="deliverystartDate" value=""  type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('deliverystartDate'), maxDate:'#F{$dp.$D(\'deliveryendDate\')}'})"  style="cursor: pointer;width: 80px;"/>
		            	至
		            	<input name="deliveryendDate" id="deliveryendDate" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('deliveryendDate'), minDate:'#F{$dp.$D(\'deliverystartDate\')}'})" style="cursor: pointer;width: 80px;"/>
            	</div>
			</td>
			<td width="20%" class="tblopt">
				<div align="right">交接单号：</div>
			</td>
			<td><input type="text" name="dlvNo" class="middle_txt" id="dlvNo" /></td>
		</tr>
		<tr>
			<td class="tblopt"  width="20%">
				<div align="right">验收日期：</div>
			</td>
			<td>
				<div align="left">
						<input name="checkstartDate" id="checkstartDate" value=""  type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('checkstartDate'), maxDate:'#F{$dp.$D(\'checkendDate\')}'})"  style="cursor: pointer;width: 80px;"/>
		            	至
		            	<input name="checkendDate" id="checkendDate" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('checkendDate'), minDate:'#F{$dp.$D(\'checkstartDate\')}'})" style="cursor: pointer;width: 80px;"/>
            	</div>	
			</td>
		
				<td width="20%" class="tblopt"><div align="right">VIN：</div></td>
				<td width="39%" >
      				<input type="text" name="vin" cols="18" rows="3" class="middle_txt" id="dlvNo" />
    			</td>
		</tr>
		<tr>
				<td width="20%" class="tblopt"><div class="right"></div></td>
				<td>
				</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="detail_query();" value="查 询" id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="totalQuery();" value="汇总查 询" id="queryBtn" /> 
				</td>
		</tr>
		</table>

		</div>
		</div>
	</form>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >
	var myPage;
	var url;
	var title = null;
	var columns=null;
		      
	function myLink(vehicle_id){
        return String.format(
        		 "<a class=\"u-anchor\" href=\"<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleQueryDLR_Detail.do?vehicle_id="
                +vehicle_id+"\">[查看]</a>");
    }
	function totalQuery(){

	calculateConfig = {bindTableList:"myTable",totalColumns:"MATERIAL_NAME"};
		url = "<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleTotalQueryDLR.json?COMMAND=1";
		columns=[{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "验收数量", dataIndex: 'COUNT', align:'center'}
				];
		__extQuery__(1);
	}
	
	function detail_query(){
		calculateConfig = {};
		 url = "<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleQueryDLR.json?COMMAND=1";
		 columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink},
				{header: "交接单号", dataIndex: 'SENDCAR_ORDER_NUMBER', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "出车仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "发车日期", dataIndex: 'FLATCAR_ASSIGN_DATE', align:'center'},
				{header: "验收日期", dataIndex: 'STORAGE_DATE', align:'center'},
				{header: "验收人", dataIndex: 'INSPECTION_PERSON', align:'center'}
		      ];
		      __extQuery__(1);
	}
 </script>    
</body>
</html>