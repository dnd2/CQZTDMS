<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<%
	String contextPath = request.getContextPath();
	String detail_id = (String)request.getAttribute("detail_id");			 	//订单明细id
	String order_amount = (String)request.getAttribute("order_amount");	        //提报数量
	String material_id = (String)request.getAttribute("material_id");		 	//物料id
	String group_id = (String)request.getAttribute("group_id");					//物料组id
	String series_id = (String)request.getAttribute("series_id");				//车系id
%>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;结算中心管理 &gt; 经销商订单管理&gt; 经销商订单审核</div>
<form method="post" name="fm" id="fm">
<table class="table_query" align=center>
	<tr class="tabletitle">
		<th colspan="10" align="left">资源选择</th>
	</tr>
	<tr class=tabletitle>
		<td width="8%" align=right>&nbsp;</td>
		<td width="12%" align=left>&nbsp;</td>
		<td width="13%" align=right>车辆位置：</td>
		<td width="20%" align=left>
			<label> 
				<select id="warehouse_id" name="warehouse_id" class="short_sel"  >
						<option value="">--请选择--</option>
  						<c:forEach var="w_List" items="${w_List }">
  							<option value="${w_List.WAREHOUSE_ID }">${w_List.WAREHOUSE_NAME }</option>
  						</c:forEach>			
  				</select>
			</label>
		</td>
		<td width="11%" align=left>&nbsp;<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
		<td width="12%" align=left>&nbsp;</td>
		<td width="11%" align="right">&nbsp;</td>
		<td width="13%" colspan="3" align=left>
		<input type="hidden" name="material_id" value="<%=material_id %>" />
		<input type="hidden" name="group_id" value="<%=group_id %>" />
		<input type="hidden" name="series_id" value="<%=series_id %>" />
		<input type="hidden" name="order_id" value="${order_id }" />
		</td>
	</tr>
</table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="form1" id="form1">
<table class="table_query" width="85%" align="center">
	<tr class="table_list_row2">
		<td align="center">
			<input type="button" name="button1" class="cssbutton" onclick="toSaveCheck();" value="确定" /> 
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
	var url = "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/getVehicleList.json?COMMAND=1";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\")' />全选", width:'6%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "车辆位置", dataIndex: 'VEHICLE_AREA', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'}
		      ];
		      
	function myCheckBox(value,metaDate,record){
		var data = record.data;
		var is_match = data.IS_MATCH;
		if(0==is_match){
			return String.format("<input type='checkbox' name='vehicleIds' checked='checked' value='" + value + "' />");
		}else{
			return String.format("<input type='checkbox' name='vehicleIds' value='" + value + "' />");
		}
	}
	//保存校验
	function toSaveCheck(){
		document.getElementById("button1").disabled = "disabled" ;
		var checkNO = 0;								//用户选择的车辆数量
		var order_amount = '<%=order_amount%>';	        //提报数量
		var vehicleid=" ";
		var vehicleIds = document.getElementsByName("vehicleIds");
		
		for(var i=0;i<vehicleIds.length;i++){
			if(vehicleIds[i].checked){
				checkNO = checkNO+1;
				vehicleid =vehicleid+vehicleIds[i].value+",";
			}
		}
		if(checkNO > order_amount){
			MyDivAlert("选择车辆数不能大于提报数量,提报数量为：" + order_amount);
			return;
		}
		//MyDivConfirm("确认提交？",putForword,[vehicleid]);
		var order_id = document.getElementById("order_id").value;
		var material_id = '<%=material_id%>';
	 	makeNomalFormCall('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/orderDetailCheck.json?order_id='+order_id+'&vehicleid='+vehicleid+'&material_id='+material_id,showResult,'fm');
	}
	//审核保存提交
	function putForword(vehicleid){
		//var detail_id = '<%=detail_id%>';
		var order_id = document.getElementById("order_id").value;
		var material_id = '<%=material_id%>';
	 	makeNomalFormCall('<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/orderDetailCheck.json?order_id='+order_id+'&vehicleid='+vehicleid+'&material_id='+material_id,showResult,'fm');
	}

	
	function showResult(json){
		parent.$('inIframe').contentWindow.queryOrderInfo();
	 	parent._hide();
	}
</script>
</body>
</html>