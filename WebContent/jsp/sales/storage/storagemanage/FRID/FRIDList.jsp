<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> RFID维护 </title>
</head>
<body onload="focusNoInput()" onkeydown="focusNoInput()">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> RFID维护
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
		 <tr class="table_list_row2">
		   <td class="right" width="15%">VIN：</td> 
	  		<td align="left">
	  		 <input type="text" maxlength="20"  class="middle_txt" id="VIN" name="VIN" />
	  		 <!-- onkeydown="doKeyDown()" -->
		</td> 
	</tr>
	 <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	 
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
	var url = "<%=contextPath%>/sales/storage/storagemanage/RFIDManage/FRIDQueryAll.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作", dataIndex: 'VIN', align:'center',renderer:myLink},
				{header: "RFID号",dataIndex: 'HGZ_NO',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
				
				
		      ];
	function myLink(value,meta,record){
		var rfid=record.data.HGZ_NO;
		var vehicleId=record.data.VEHICLE_ID;
		var url="<a href='javascript:void(0);' onclick='chkArea(\""+value+"\")'>[RFID更换]</a>";
		if(rfid!=null){
			url+="<a href='javascript:void(0);' onclick='canlFrid(\""+vehicleId+"\")'>[RFID解绑]</a>";
		}
		return String.format(url);
	}
	function chkArea(vin) {
		//var vin=document.getElementById("VIN").value;
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/RFIDManage/FRIDQuery.do?VIN="+vin;
		fm.submit() ;
	}
	function canlFrid(vehicleId){
		MyConfirm("RFID确定解绑?",canlFRID,[vehicleId]);
	}
	function canlFRID(vehicleId){
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/RFIDManage/canlFRID.json?vehicle_id="+vehicleId,canlFRIDBack,'fm','queryBtn'); 
	}
	function canlFRIDBack(json)
	{
		if(json.returnValue == 1)
		{
			MyAlert("解绑成功");
			__extQuery__(1);
		}else if(json.returnValue == 2)
		{
			MyAlert("解绑失败,数据错误，无法找到该VIN号");
		}else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	</script>
<!--页面列表 begin -->
<script type="text/javascript" >
var t;
function doKeyDown() {
	clearTimeout(t);
	t = setTimeout("scanInit()", 800);
}
function scanInit(){
	document.getElementById('VIN').focus();
	var VIN=document.getElementById("VIN").value;
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RFIDManage/FRIDWInit.json";
    makeCall(url,returnBack,{VIN:VIN});	
}
//合格证回调函数
function returnBack(json){
	if(json.returnValue==0){
		chkArea();
	}else if(json.returnValue==1){
		document.getElementById("VIN").value="";
		MyAlert("VIN号错误，无法找到该VIN的车辆信息");
	}else{
		document.getElementById("VIN").value="";
		MyAlert("数据查询失败！");
	}
}
function focusNoInput() { 
	document.getElementById('VIN').focus();
}
</script>
</body>
</html>
