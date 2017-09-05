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
<title>市场活动变更/取消申请(经销商)</title>
<% String contextPath = request.getContextPath();  %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动变更/取消申请(经销商)</div>
<form method="post" name="fm" id="fm">
    <font color="red">1.未进行变更申请,变更申请处于已保存,省系审核驳回,事业部审核驳回,车厂审核驳回,
        车厂审核通过状态下才可以进行申请,
        其中变更申请处于已保存状态下进行申请是修改本次申请记录,其余状态进行申请是再一次申请
        <br>
        2.变更申请类型为活动取消,并且车厂审核已通过,则不能再次进行变更申请
        <br>
        3.活动总结处于待审核状态和审核通过状态下不可进行变更申请
    </font>
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
		<tr>
			<td align="right">活动编号：</td>
			<td align="left">
				<input name="campaignNo" type="text" class="middle_txt" id="campaignNo" maxlength="25">
			</td>
			<td align="right">活动名称：</td>
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
			<td align="right">活动时间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td></td>
			<td width="20%" align="left">
				<input name="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
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
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanChngApply/doSearch.json";
	var title = null;
	var columns = [
				{header: "活动编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "活动名称", dataIndex: 'CAMPAIGN_NAME', align:'center'},
				{header: "活动主题",dataIndex: 'CAMPAIGN_SUBJECT', align:'center'},
				{header: "活动时间",dataIndex: 'START_DATE',align:'center',renderer:myText},
				{header: "活动方案状态",dataIndex: 'CHECK_STATUS',align:'center',renderer:getItemValue},
				{header: "变更申请状态",dataIndex: 'REQ_STATUS',align:'center',renderer:myGetItemValue},
				{id:'action',header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
    function myGetItemValue(value,meta,record){
         if(value==0){
             return String.format("未进行变更申请");
         }else{
             return getItemValue(value);
         }
    }
    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
    //设置超链接
    function myLink(value,meta,record){
        return String.format("<a href='#' onclick='makePlan("+record.data.CAMPAIGN_ID+","+record.data.SPACE_ID + ","+record.data.PLAN_ID + ","+record.data.REQ_STATUS+ ")'>[申请]</a>");

    }
    function makePlan(campaignId,spaceId,planId,reqStatus){
        document.fm.action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanChngApply/detailPre.do?campaignId='+campaignId+'&spaceId='+spaceId+'&planId='+planId+'&reqStatus='+reqStatus;
        document.fm.submit();
    }
    //初始化
    function doInit(){
    	//_setDate_("startDate", "endDate", "3", "1") ;
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>