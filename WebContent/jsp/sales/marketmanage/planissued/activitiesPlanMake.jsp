<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动方案下发</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动意向维护(车厂)</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->

	<table class="table_query" >
		<tr>
			<td align="right">活动编号：</td>
			<td align="left">
				<input name="campaignNo" class="middle_txt" type="text" id="campaignNo" maxlength="25">
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
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button2" type=button class="cssbutton" onClick="toAdd();" value="新增">
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
	var url = "<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/activitiesPlanMakeQuery.json";
	var title = null;
	var columns = [
				{header: "活动编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "活动名称",dataIndex: 'CAMPAIGN_NAME',align:'center'},
				{header: "活动主题",dataIndex: 'CAMPAIGN_SUBJECT',align:'center'},
				{header: "活动状态",dataIndex: 'CAMPAIGN_STATUS',align:'center',renderer:getItemValue},
				{header: "活动时间",dataIndex: 'START_DATE',align:'center',renderer:myText},
				{id:'action',header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
    //设置超链接
    function myLink(value,meta,record){
        if(record.data.CAMPAIGN_STATUS==13261001){
    	    return String.format("<a href='#' onclick='toUpdate("+ value +")'>[修改]</a><a href='#' onclick='toCancel("+ value +")'>[删除]</a><a href='#' onclick='assignPlan("+ value +")'>[下发]</a>");
        }else{
            return String.format("<a href='#' onclick='toDetail("+ value +")'>[查看]</a>");
        }

    }

    function toDetail(value){
		$('fm').action= '<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/activitiesPlanMakeDetail.do?campaignId='+value;
	 	$('fm').submit();
    }

    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
 	//新增
	function toAdd(){
		$('fm').action= '<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/activitiesPlanMakeAddInit.do';
	 	$('fm').submit();
	}
	
	//下发ACTION链接
	function assignPlan(value){
		 MyConfirm("确认下发？",confirmAssign,[value]);
	}
	
	//确认下发
	function confirmAssign(value){
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/assignPlan.json?campaignId='+value,showResult,'fm');
	}
	
	//回调函数
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/activitiesPlanMakeInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	//修改连接
	function toUpdate(value){
		$('fm').action= '<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/updatePlanInit.do?&campaignId='+value;
	 	$('fm').submit();
	}
	
	
	//删除连接
	function toCancel(value){
	    MyConfirm("是否确认删除？",confirmDel,[value]);
	}
	
	//确认删除
	function confirmDel(value){
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/deletePlan.json?campaignId='+value,showResult,'fm');
	}
	
    //初始化
    function doInit(){
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>