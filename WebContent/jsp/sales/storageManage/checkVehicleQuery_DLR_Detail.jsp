<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.infodms.dms.po.TtInspectionDetailPO"%>

<%
	String contextPath = request.getContextPath();
	Map<String,Object> checkDetail = (Map<String,Object>)request.getAttribute("checkDetail");
	List<TtInspectionDetailPO> detailList = (List<TtInspectionDetailPO>)request.getAttribute("detailList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆验收查询--明细(DLR)</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp; 车辆验收明细查询</div>
<table class="table_query table_list" class="center">
	<tr class="tabletitle">
		<td colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />验收明细</td>
	</tr>
	<tr>
		<td width="20%" class="right">底盘号：</td>
		<td width="30%" align="left">${checkDetail.VIN }</td>
		<td width="20%" class="right">发车日期：</td>
		<td width="30%" align="left">${checkDetail.DELIVERY_DATE }</td>
	</tr>
	<tr>
		<td class="right">车型：</td>
		<td align="left">${checkDetail.GROUP_NAME }</td>
		<td class="right">发动机号：</td>
		<td align="left">${checkDetail.ENGINE_NO }</td>
	</tr>
	<tr>

		<td class="right">颜色：</td>
		<td align="left">${checkDetail.COLOR }</td>
		<td class="right">发车仓库：</td>
		<td align="left">${checkDetail.WAREHOUSE_NAME }</td>
	</tr>
	<tr>
		<td class="right">实际到车日期：</td>
		<td align="left">${checkDetail.ARRIVE_DATE }</td>
		<td class="right">实际到车时间：</td>
		<td align="left">${checkDetail.ARRIVE_TIME }时</td>
	</tr>

	<tr>
		<td class="right">送车交接单号：</td>
		<td align="left">${checkDetail.SENDCAR_ORDER_NUMBER}</td>
		<td class="right"></td>
		<td align="left"></td>
	</tr>
	<tr>
		<td class="right">送车人员：</td>
		<td align="left">${checkDetail.MOTORMAN}</td>
		<td class="right">验收单号：</td>
		<td align="left">${checkDetail.INSPECTION_NO }</td>
	</tr>
	<tr>
		<td class="right">验收人员：</td>
		<td align="left">${checkDetail.INSPECTION_PERSON}</td>
		<td class="right">车辆所在位置：</td>
		<td align="left">${checkDetail.VEHICLE_AREA}</td>
	</tr>
	<tr>
		<td class="right">备注：</td>
		<td colspan="3" align="left">${checkDetail.REMARK }</td>
	</tr>
</table>
<%
 	if(null != detailList && detailList.size()>0){
 %>
<table class=table_list >
	<tr class=tabletitle>
		<td nowrap>损坏部位</td>
		<td nowrap>损坏描述</td>
	</tr>
	<c:forEach items="${detailList}" var="detailList" varStatus="vstatus">
		<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
			<div align="center">${detailList.damagePart }</div>
			</td>
			<td>
			<div align="center">${detailList.damageDesc }</div>
			</td>
		</tr>
	</c:forEach>
</table>
<%
    }
    %>
<table class="table_query" width="85%" align="center" border="0">
	<tr align="center">
		<td><input type="button" class="normal_btn" value="返回"
			onclick="history.back();" /></td>
	</tr>
</table>

</div>
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleQueryDLR.json?COMMAND=1";
	
	var title = null;
	var columns = [
				{header: "车型名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "出车仓库", dataIndex: 'VIN', align:'center'},
				{header: "发车日期", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "验收日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "验收人", dataIndex: 'INSPECTION_PERSON', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink}
		      ];
		      
	function myLink(vehicle_id){
        return String.format(
        		 "<a href=\"<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleQueryDLR_Detail.do?vehicle_id="
                +vehicle_id+"\">[详细]</a>");
    }
	    
 </script>
</body>
</html>