<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户跟进信息</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息&gt; 集团客户跟进信息维护</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">客户名称：</td>
			<td align="left">
				<input name="fleetName" type="text" class="middle_txt" id="fleetName">
			</td>
			<td align="right">客户类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">报备日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">跟进日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t3" name="followStartDate" datatype="1,is_date,10" group="t3,t4"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't3', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t4" name="followEndDate" datatype="1,is_date,10" group="t3,t4" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't4', false);" value="&nbsp;" />
			</td>
			<td width="20%" align="left"><input name="button2" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFollow/fleetFollowforqueryList.json";
	var title = null;
	var columns = [
				{header: "申请单位",dataIndex: 'COMPANY_NAME',align:'center'},
				{header: "客户名称",dataIndex: 'FLEET_NAME',align:'center',renderer:myView},
				{header: "大客户代码",dataIndex: 'FLEET_CODE',align:'center'},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "区域",dataIndex: 'REGION',align:'center',renderer:getRegionName},
				{header: "主要联系人",dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话",dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "操作", dataIndex: 'FLEET_ID', align:'center',renderer:myLink}
		      ];
    //设置超链接
    function myLink(value,meta,record){
    	return String.format("<a href='#' onclick='searchServiceInfo("+ value +")'>[详细]</a>");
    }
 	//明细链接
	function searchServiceInfo(value1){
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFollow/fleetFollowInfo.do?&fleetId='+value1;
	 	$('fm').submit();
	}
	//设置超链接
	function myView(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.FLEET_ID+"\")'>"+ value +"</a>");
	}
	//详细页面
	function sel(value){
		
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetInfoDetailQuery.do?fleetId='+value,800,500);
	}
    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>