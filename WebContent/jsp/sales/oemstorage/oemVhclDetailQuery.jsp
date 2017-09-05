<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
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
function search(){
	if($('areaId').value==''&&$('dealerCode').value==''&&$('groupCode').value==''&&$('vin').value==''&&$('enginNo').value==''){
		MyAlert('由于数据量大，请至少输入一个条件查询！');
		return;
	}else{
		__extQuery__(1);
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：车厂库存管理 &gt; 详细车籍查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td></td>
			<td align="right"> 选择业务范围：</td>
		    <td align="left">
		    	<select name="areaId" class="short_sel" id="areaId">
		    		<option value="">-请选择-</option>
					<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">经销商：</td>
			<td>
				<input type="text" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true')" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">物料组：</td>
			<td>
				<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('groupCode');" value="清 空" id="clrBtn" />
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">底盘号（VIN）：</td>
			<td>
				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
	  		</td>
	  		<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">发动机号：</td>
			<td>
				<input type="text" class="middle_txt" id="enginNo" name="enginNo" />&nbsp;&nbsp;<font color="red">需录入完整发动机号</font>
	  		</td>
	  		<td></td>
		</tr>
		<tr>
			<td align="right"></TD>
			<td></TD>
			<td align="center"><input name="queryBtn" type=button class="cssbutton" onClick="search();" value="查询">
			<input name="button2" type=button class="cssbutton" onClick="getMyDownLoad();" value="下载">
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
	
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/oemstorage/OemVhclDetailQuery/vhclDetailQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称", width:'10%', dataIndex: 'DEALER_NAME',align:'center'},
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
				{id:'action',header: "车辆VIN号", dataIndex: 'VIN', align:'center',renderer:myLink},
				{header: "车辆价格", dataIndex: 'VHCL_PRICE', align:'center'},
				{header: "当前节点", dataIndex: 'NODE_CODE', align:'center',renderer:getItemValue},
				{header: "库存状态", dataIndex: 'LIFE_CYCLE', align:'center',renderer:getItemValue},
				{header: "车辆状态", dataIndex: 'LOCK_STATUS', align:'center',renderer:getItemValue}
		      ];
	//设置超链接  begin    
	//超链接设置
	function myLink(value,meta,record){
		var data = record.data;
  		return String.format("<a href='#' onclick='getDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function getDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
	function getMyDownLoad(){
		$('fm').action= "<%=contextPath%>/sales/oemstorage/OemVhclDetailQuery/vhclLoadDetailQuery.json";
		$('fm').submit();
}
</script>
<!--页面列表 end -->
</body>
</html>