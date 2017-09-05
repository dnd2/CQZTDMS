<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-车辆信息</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body onload="__extQuery__(1);">
<form id="fm" name="fm" id="fm">
<input type="hidden" id="activityId" name="activityId" value="<%=request.getAttribute("activityId")%>"/>
	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理
	
		<table class="table_edit">
			<tr>
				<td height="36" width="20%" align="right">
					VIN：
				</td>
				<td align="left" width="40%">
					<input type="text" class="middle_txt" name="vin"/>
				</td>
				<td width="30%" align="center">
					<input type="button" class="normal_btn" value="查询" onclick="__extQuery__(1)"/>
				</td>
			</tr>
		</table>
	</div>
 <!--分页开始 -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 结束 --> 
</form>
<!-- 页面列表 开始 -->
		<script type="text/javascript" >
			var myPage;
		
			var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityVehicleListInfo.json";
						
			var title = null;
		
			var columns = [
						{header: "VIN", dataIndex: 'VIN', align:'center'},
						{header: "责任经销商代码",dataIndex: 'DEALER_CODE' ,align:'center'},
						{header: "责任经销商名称 ",dataIndex: 'DEALER_SHORTNAME' ,align:'center'},
						{header: "客户名称",dataIndex: 'CUSTOMER_NAME' ,align:'center'},
						{header: "客户电话",dataIndex: 'LINKMAN_MOBILE' ,align:'center'},
						{header: "客户地址",dataIndex: 'CUSTOMER_ADDRESS' ,align:'center'},
						{header: "邮编",dataIndex: 'POSTAL_CODE' ,align:'center'},
						{header: "联系人",dataIndex: 'LINKMAN' ,align:'center'},
						{header: "联系人区号",dataIndex: 'LINKMAN_ZONE_NUM' ,align:'center'},
						{header: "联系人办公室电话",dataIndex: 'LINKMAN_OFFICE_PHONE' ,align:'center'},
						{header: "联系人住宅电话",dataIndex: 'LINKMAN_FAMILY_PHONE' ,align:'center'},
						{header: "EMAIL",dataIndex: 'EMAIL' ,align:'center'},
						{header: "省",dataIndex: 'PROVINCE' ,align:'center'},
						{header: "地区",dataIndex: 'AREA' ,align:'center'},
						{header: "县市",dataIndex: 'TOWN' ,align:'center'},
						{header: "车牌",dataIndex: 'LINCENSE_TAG' ,align:'center'},
						{header: "销售状态",dataIndex: 'SALE_STATUS' ,align:'center',renderer:getItemValue},
						{header: "维修状态",dataIndex: 'REPAIR_STATUS' ,align:'center',renderer:getItemValue}
				      ];
		</script>
<!-- 页面列表结束 -->
<table class="table_query">
	<tr>
		<td align="center"> 
			<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();"/>
		</td>
	</tr>
</table>
</body>
</html>