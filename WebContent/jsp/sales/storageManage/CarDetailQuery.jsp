<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>详细车籍查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：车厂库存管理 &gt; 详细车籍查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td class="right">底盘号（VIN）：</td>
			<td>
				<textarea  id="vin" name="vin" cols="18" rows="3"></textarea> 
	  		</td>
	  		<td class="right">
	  			发动机号 ：
	  		</td>
			<td>
				<input  id="engineNo" name="engineNo" /> 
			</td>
		</tr>
		<tr>
	  		<td colspan="2" align="center"><span style="color: red">请输入一行或多行完整的17位车辆底盘号(VIN)</span></td>
		</tr>
		<tr>
			<td></td>
			<td align="center">
				<input name="button2" type=button class="normal_btn" onClick="mySubmit();" value="查询">
			</td>
			<td align="center">
				<input name="button2" type=button class="normal_btn" onClick="getMyDownLoad();" value="下载">
			</td>
			<td></td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//获取字符串长度 中文以两个字符计算
String.prototype.len=function(){   
    return this.replace(/[^\x00-\xff]/g,"**").trim().length;   
}   
function mySubmit(){
	var myLength=document.getElementById("vin");
	var engineNoValue = document.getElementById("engineNo").value.trim();
	if( engineNoValue != '') {
		__extQuery__(1);
	} else {
		if(document.getElementById("vin").value==''){
			MyAlert('VIN底盘号输入不能为空！');
		}else if(myLength.value.len()<17){
			MyAlert('请输入完整的VIN！');
		}else if(myLength.value.len()>17000){
			MyAlert('查询不能超过1000辆车！');
		}else{
			__extQuery__(1);
		}
	}
}
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storageManage/CarInfo/vhclDetailQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称", width:'10%', dataIndex: 'DEALER_NAME',align:'center'},
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{id:'action',header: "车辆VIN号", dataIndex: 'VIN', align:'center',renderer:myLink},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "合格证号", dataIndex: 'HEGEZHENG_CODE', align:'center'},
				{header: "车辆价格", dataIndex: 'VHCL_PRICE', align:'center'},
				{header: "当前节点", dataIndex: 'NODE_CODE', align:'center',renderer:getItemValue},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "车辆状态", dataIndex: 'LOCK_STATUS', align:'center',renderer:getItemValue}
		      ];
	//设置超链接  begin    
	//超链接设置
	if(${myValue==1} && document.getElementById("vin").value==''){
			MyAlert('车辆VIN不能为空！');
		}
	
	function myLink(value,meta,record){
		var data = record.data;
  		return String.format("<a href='#' onclick='getDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function getDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
	function getMyDownLoad(){
			document.getElementById('fm').action= "<%=contextPath%>/sales/storageManage/CarInfo/vhclDetailQueryLoad.json";
			document.getElementById('fm').submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>