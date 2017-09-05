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
<title>媒体信息查询</title>
<% String contextPath = request.getContextPath();  %>
</head>
<script type="text/javascript">
<!--
function mediaDownload() {
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesMediaCost/mediaDownLoad.json";
	$('fm').action= url;
	$('fm').submit();
}
//-->
</script>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 媒体信息查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
			<td align="right">选择业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">--请选择--</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" />
			</td>
			<td align="right">大区：</td>
			<td align="left">
				<select id="pageOrgId" name="pageOrgId" class="short_sel">
				<option value="">--请选择--</option>
					<c:forEach items="${orgList}" var="orgList">
						<option value="${orgList.ORG_ID }">${orgList.ORG_NAME }</option>
					</c:forEach>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">车型品牌：</TD>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("model",<%=Constant.MEDIA_MODEL%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td align="right">媒体类型：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("mediaType",<%=Constant.MEDIA_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">费用类型：</td>
			<td align="left">
					<label>
					<input type="hidden" name="isFleet" id="isFleet" value="${isFleet}" />
					<script type="text/javascript">
						if("${isFleet}" == <%=Constant.STATUS_ENABLE%>)
							genSelBoxExp("costType",<%=Constant.COST_TYPE%>,"-1",true,"short_sel",'',"false",'<%=Constant.COST_TYPE_01%>,<%=Constant.COST_TYPE_02%>,<%=Constant.COST_TYPE_03%>,<%=Constant.COST_TYPE_04%>,<%=Constant.COST_TYPE_06%>,<%=Constant.COST_TYPE_07%>');
						else if("${isFleet}" == <%=Constant.STATUS_DISABLE%>)
							genSelBoxExp("costType",<%=Constant.COST_TYPE%>,"-1",true,"short_sel",'',"false",'<%=Constant.COST_TYPE_05%>');
						else 
							genSelBoxExp("costType",<%=Constant.COST_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td align="right">媒体名称：</td>
			<td align="left"><input type="text" class="middle_txt" id="mediaName" name="mediaName" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">省份：</td>
			<td align="left">
					<select id="region" name="region" class="short_sel"></select>
			</td>
			<td align="right">广告时间：</td>
			<td align="left">
				<input class="short_txt"  readonly="readonly" type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt" readonly="readonly"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td width="20%" align="left">
				<input type="hidden" name="areaStr" id="areaStr" value="${areaStr }" />
				<input name="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);getTotal() ;" value="查询">&nbsp;
				<input name="queryBtn" type="button" class="cssbutton" onClick="mediaDownload() ;" value="下载">
			</td>
		</tr>
	</table>
	<br />
	<table class="table_query" align="center" style="display:none" id="total">
		<tr class= "tabletitle">
			<th align = "right" width="10%" colspan="1">
			<label><strong>次数合计：</strong></label>
			</th>
			<th align = "left" colspan="2">
			<font color = "black">
			<strong>
			<span id="count">
			</span>
			</strong>
			</font>
			<label><strong>次</strong></label>
			</th>
		</tr>
		<tr>
			<th align = "right" width="10%" colspan="1">
			<label>
			<strong>
			含税费用合计：
			</strong>
			</label>
			</th>
			<th align = "left" colspan="2">
			<font color = "black">
			<strong>
			<span id="account">
			</span>
			</strong>
			</font>
			<label><strong>元</strong></label>
			</th>
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
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesMediaCost/activitiesSpacePlanQuery.json";
	var title = null;
	var columns = [
				{header: "组织名称",dataIndex: 'ORG_NAME',align:'center'},
				{header: "业务范围",dataIndex: 'AREA_NAME',align:'center'},
				{header: "费用类型", dataIndex: 'COST_TYPE', align:'center', renderer:getItemValue},
				{header: "省份",dataIndex: 'REGION',align:'center', renderer:getRegionName},
				{header: "车型品牌", dataIndex: 'MEDIA_MODEL', align:'center', renderer:getItemValue},
				{header: "宣传主题", dataIndex: 'ADV_SUBJECT', align:'center'},
				{header: "媒体类型",dataIndex: 'MEDIA_TYPE',align:'center', renderer:getItemValue},
				{header: "媒体名称",dataIndex: 'MEDIA_NAME', align:'center'},
				{header: "广告开始日期",dataIndex: 'ADV_DATE',align:'center'},
				{header: "广告结束日期",dataIndex: 'END_DATE',align:'center'},
				{header: "栏目/套装",dataIndex: 'MEDIA_COLUMN',align:'center'},
				{header: "刊发位置/播出时段",dataIndex: 'MEDIA_PUBLISH',align:'center'},
				{header: "规格/版式",dataIndex: 'MEDIA_SIZE',align:'center'},
				{header: "单日频次",dataIndex: 'ITEM_COUNT',align:'center'},
				{header: "总次数",dataIndex: 'TOTAL_COUNT',align:'center'},
				{header: "结算单价 元/次",dataIndex: 'ITEM_PRICE',align:'center'},
				{header: "金额（元）",dataIndex: 'PLAN_COST',align:'center'}
		      ];

	//初始化
    function doInit(){
    	_setDate_("startDate", "endDate", "3", "1") ;
    	__extQuery__(1);
    	loadcalendar();  //初始化时间控件
    	getTotal() ;
    	genLocSel('region','','','','','');
	}

	function getTotal() {
		var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesMediaCost/activitiesSpacePlanQuery.json";
		makeNomalFormCall(url,showForwordValue,'fm','queryBtn');
	}

	function showForwordValue (json) {
		document.getElementById("total").style.display = "inline";
		var count  = json.count;
		var account = json.account;
		
		document.getElementById("count").innerText = count;
		document.getElementById("account").innerText = amountFormat(account) ;
	}
</script>
<!--页面列表 end -->
</body>
</html>