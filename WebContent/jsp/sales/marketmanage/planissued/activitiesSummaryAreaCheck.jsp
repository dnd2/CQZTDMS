<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动总结审批</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动总结审批&gt; 活动总结审批</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
		<td align="right">选择业务范围：</td>
			<td align="left">
				<select name="areaId" class="short_sel">
					<option value="">---请选择---</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" 

/>
			</td>
			</tr>
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
			<td align="right">活动时间：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt" readonly="readonly" type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td align="right">代理商：</td>
			<td>
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" value=""  id="dealerCode"/>
				<input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true', '${orgId}');"/>
				<input class="cssbutton" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
			</td>
			<td width="20%" align="left">
			    <input name="campaignId" id="campaignId" type="hidden" value="">
			    <input name="executeId" id="executeId" type="hidden" value="">
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
	var url = "<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSummaryAreaCheck/activitiesSummaryAreaCheckQuery.json";
	var title = null;
	var columns = [
				{header: "方案编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "方案名称", dataIndex: 'CAMPAIGN_NAME', align:'center'},
				{header: "活动类别",dataIndex: 'CAMPAIGN_TYPE',align:'center',renderer:getItemValue},
				// {header: "代理商代码",dataIndex: 'DEALER_CODE', align:'center'},
				{header: "代理商名称",dataIndex: 'DEALER_NAME', align:'center'},
				// {header: "活动主题",dataIndex: 'CAMPAIGN_SUBJECT', align:'center'},
				{header: "提报人",dataIndex: 'NAME', align:'center'},
				{header: "上报时间",dataIndex: 'SUBMITS_DATE',align:'center'},
				{id:'action',header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
    //设置超链接
    function myLink(value,meta,record){
    	return String.format("<a href='#' onclick='toCheckDetial("+record.data.CAMPAIGN_ID+","+record.data.EXECUTE_ID+")'>[审批]</a>");
    }
    //取消链接
    function toCheckDetial(value1,value2){
    	document.getElementById("campaignId").value= value1;
    	document.getElementById("executeId").value= value2;
    	$('fm').action= '<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSummaryAreaCheck/showActivitiesSummarySubCheckDetial.do';
	 	$('fm').submit();
    }
 	//新增链接
	function toAdd(){
		$('fm').action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanReport/activitiesPlanReportAddInit.do';
	 	$('fm').submit();
	}
    //初始化
    function doInit(){
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
	   	document.getElementById(txtId).value="";
	}
</script>
<!--页面列表 end -->
</body>
</html>