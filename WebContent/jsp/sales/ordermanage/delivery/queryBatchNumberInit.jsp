<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令下达</title>
<% 
	String contextPath = request.getContextPath(); 
	List warehouseList = (List)request.getAttribute("warehouseList");
%>
<script type="text/javascript">
	function doInit(){
		//__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;发运指令下达</div>
<form method="post" name="fm" id="fm">
	<table class="table_query" border="0">
		<tr align="center">
			<td width="25%" align="right">仓库&nbsp;</td>
			<td width="25%" align="left">
				<select name="warehouse_id" id="warehouse_id">
					<option value="">--请选择--</option>
					<%
						if(null != warehouseList && warehouseList.size()>0){
							for(int i=0;i<warehouseList.size();i++){
								Map  map = (Map) warehouseList.get(i);
					%>
						<option value="<%=map.get("WAREHOUSE_ID") %>"><%=map.get("WAREHOUSE_NAME") %></option>
					<%		
							}
						}
					%>
				</select>
			</td>
			<td width="50%" align="left">
				<input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input type="hidden" id="materialId" name="materialId" value="${materialId }" />
			</td>
		</tr>
	</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderDeliveryCommand/queryBatchNumberList.json";
	var title = null;
	var columns = [
	            {id:'action',header: "选择", width:'8%',dataIndex: 'SPECIAL_BATCH_NO',renderer:mySelect},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "批次号", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				{header: "数量", dataIndex: 'VEHICLE_NUMBER', align:'center'}
		      ];
    function mySelect(value,metaDate,record){
    	var data = record.data;
    	return String.format("<input type='radio' name='batch_no'  onclick='deli_batch_no(\""+data.SPECIAL_BATCH_NO+"\",\""+data.MATERIAL_ID+"\");' />");
    }
	
	function deli_batch_no(value,material_id){
		if(value && "null" != value+""){
			var b_number = "batchNumber"+material_id;
			parent.$('inIframe').contentWindow.$(b_number).value = value;
		}
		parent._hide();
	}
	
</script>
<!--页面列表 end -->
</body>
</html>