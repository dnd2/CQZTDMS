<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动方案管理</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理&gt; 活动信息查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">方案编号：</td>
			<td align="left">
				<input name="campaignNo" type="text" class="middle_txt" id="campaignNo" maxlength="25">
			</td>
			<td align="right">方案名称：</td>
			<td align="left">
				<input name="campaignName" type="text" class="middle_txt" id="campaignName" maxlength="25">
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="campaignSubject" id="campaignSubject" maxlength="25"/>
			</td>
			<td align="right">活动类别：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("campaignType",<%=Constant.CAMPAIGN_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">费用类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("costType",<%=Constant.COST_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td align="right">活动方案状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("checkStatus",<%=Constant.CAMPAIGN_CHECK_STATUS%>,"-1",true,"short_sel",'',"false",'<%=Constant.CAMPAIGN_CHECK_STATUS_01%>,<%=Constant.CAMPAIGN_CHECK_STATUS_12%>');
					</script>
				</label>
			</td>
			<td width="20%" align="left"></td>
		</tr>
		<tr>
			<td align="right">活动时间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td align="right"></td>
			<td align="left"></td>
			<td width="20%" align="left">
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
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
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSearch/activitiesPlanSearch.json";
	var title = null;
	var columns = [
				{header: "方案编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "方案名称", dataIndex: 'CAMPAIGN_NAME', align:'center'},
				{header: "费用类型",dataIndex: 'PRICE_TYPE',align:'center',renderer:getItemValue},
				{header: "活动类别",dataIndex: 'CAMPAIGN_TYPE',align:'center',renderer:getItemValue},
				{header: "活动主题",dataIndex: 'CAMPAIGN_SUBJECT', align:'center'},
				{header: "提报时间",dataIndex: 'SUBMITS_DATE',align:'center'},
				{header: "活动方案状态",dataIndex: 'CHECK_STATUS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
    //设置超链接
    function myLink(value,meta,record){
    	return String.format("<a href='#' onclick='searchServiceInfo("+record.data.CAMPAIGN_ID+","+record.data.EXECUTE_ID+")'>[查看]</a>");
    }
    //明细链接
    function searchServiceInfo(value1,value2){
    	$('fm').action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSearch/activitiesPlanSearchDetail.do?&campaignId='+value1+'&executeId='+value2;
	 	$('fm').submit();
    }
    //回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("操作成功！");
			__extQuery__(1);
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
    //初始化
    function doInit(){
    	_setDate_("startDate", "endDate", "3", "1") ;
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>