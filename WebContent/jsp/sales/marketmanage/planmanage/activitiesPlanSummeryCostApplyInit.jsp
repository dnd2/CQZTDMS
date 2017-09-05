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
<title>市场活动总结费用申请(经销商)</title>
<% String contextPath = request.getContextPath();  %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动总结费用申请(经销商)</div>
<font color="red">1.只有变更申请处于未进行和被驳回状态和车厂审核通过时才可以进行活动总结</font>
<form method="post" name="fm" id="fm">
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
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostApply/doSearch.json";
	var title = null;
	var columns = [
				{header: "活动编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "活动名称", dataIndex: 'CAMPAIGN_NAME', align:'center'},
				{header: "活动主题",dataIndex: 'CAMPAIGN_SUBJECT', align:'center'},
				{header: "活动时间",dataIndex: 'START_DATE',align:'center',renderer:myText},
				{header: "活动方案状态",dataIndex: 'CHECK_STATUS',align:'center',renderer:getItemValue},
				{header: "活动总结状态",dataIndex: 'SUMMERY_STATUS',align:'center',renderer:myGetItemValue},
				{id:'action',header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
    function myGetItemValue(value,meta,record){
           if(value==0){
               return String.format("未进行活动总结");
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
        var str="";
        //活动总结状态处于未进行 和 已保存 和被驳回状态 才有申请链接
        if(record.data.SUMMERY_STATUS==0||record.data.SUMMERY_STATUS==13291001||record.data.SUMMERY_STATUS==13291005||record.data.SUMMERY_STATUS==13291007){
            //变更申请只有处于未进行和被驳回状态和车厂审核通过时才可以进行活动总结
            if(record.data.REQ_STATUS!=11261017&&record.data.REQ_STATUS!=11261018&&record.data.REQ_STATUS!=11261020){
                str=String.format("<a href='#' onclick='makePlan("+record.data.CAMPAIGN_ID+","+record.data.SPACE_ID + ","+record.data.PLAN_ID + ")'>[申请]</a>");
            }
        }
        //未进行和未提报 没有审批记录链接
        if(record.data.SUMMERY_STATUS!=0&&record.data.SUMMERY_STATUS!=13291001){
             str+=String.format("<a href='#' onclick='toCheckRecords("+record.data.CAMPAIGN_ID+","+record.data.SPACE_ID + ","+record.data.PLAN_ID + ")'>[审批记录]</a>");
        }
        return str;
    }
    function makePlan(campaignId,spaceId,planId){
        document.fm.action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostApply/detailPre.do?campaignId='+campaignId+'&spaceId='+spaceId+'&planId='+planId;
        document.fm.submit();
    }
    function toCheckRecords(campaignId,spaceId,planId){
        document.fm.action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostApply/toCheckRecordsPre.do?campaignId='+campaignId+'&spaceId='+spaceId+'&planId='+planId;
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