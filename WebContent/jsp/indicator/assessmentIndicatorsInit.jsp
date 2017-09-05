<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>考核指标查询</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<script type="text/javascript">
function doInit(){
	_setDate_("startDate", "endDate", "1", "0") ;
	loadcalendar();  //初始化时间控件
}
function clrTxt(value) {
	document.getElementById(value).value = "" ;
}
function query() {
	<%-- document.fm.action='<%=request.getContextPath()%>/indicator/AssessmentIndicatorsAction/indicatorQuery.do';
	document.fm.submit(); --%>
	__extQuery__(1);
}

</script>
</head>
<body>
<div>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  经销商实销管理 &gt; 考核指标管理 &gt; 考核指标查询</div>
	<form id="fm" name="fm" method="post">
			<table class="table_query">
				<tr>
					<td width="20%" align="right">考核日期：</td>
					<td width="20%" align="left">
						<input class="short_txt" readonly="readonly"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
						<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value=" " />至
						<input class="short_txt"  readonly="readonly" type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
						<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value=" " />
					</td>
					<td width="10%">&nbsp;</td>
					<td align="right">经销商公司：</td>
					<td align="left">
						<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
						<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
						<input name="button4" type="button"  class="mini_btn" onclick="showCompanyByOther('dealerCode', 'dealerId', 'true', '${orgId}', '<%=Constant.MSG_TYPE_1 %>')"; value="..." />
						<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="clrTxt('dealerCode');clrTxt('dealerId');"/>
					</td>
				<tr>
					<td colspan="5" align="center">
						<input type="button" class="normal_btn" name="queryBtn" id="queryBtn" value="查 询" onclick="query() ;" />&nbsp;
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/indicator/AssessmentIndicatorsAction/indicatorQuery.json";
	
	var title = null;
	
	var columns = [
	               	
	               	/* {header: '潜客信息及时录入', colspan:4, colspanName:'潜客信息及时录入'}, */
	               	{header: "经销商公司", dataIndex: 'COMPANY_NAME', align: 'center', rowspan:2, colspan:3},
					//{header: "日期", dataIndex: 'UPLOAD_DATE', align:'center', renderer:getDateValue, colspanName:'潜客信息及时录入'},
					{header: "季度潜客数", dataIndex: 'IC001', align:'center' , colspanName:'潜客信息及时录入', renderer: getActualValue},
					{header: "季度实销数", dataIndex: 'ISALE_CAR', align:'center' , renderer: getActualValue},
					{header: "季度实销数/季度潜客数", dataIndex: 'ICS', align:'center', renderer:getPercentValue},
					
					/* {header: '潜客信息及时跟进', colspan:3, rowspan:2,  colspanName:'潜客信息及时跟进'}, */
					{header: "超时跟进潜客数", dataIndex: 'C003', align:'center', colspan:3, colspanName:'潜客信息及时跟进', renderer: getActualValue },
					{header: "应跟进潜客数", dataIndex: 'C002', align:'center', renderer: getActualValue},
					{header: "超时跟进潜客数/应跟进潜客数", dataIndex: 'P1', align:'center', renderer:getPercentValue},
					
					/* {header: '客户关怀及时回访', colspan:3, rowspan:2,  colspanName:'客户关怀及时回访'}, */
					{header: "超时回访客户数", dataIndex: 'S1', align:'center', colspan:3, colspanName:'客户关怀及时回访', renderer: getActualValue},
					{header: "应回访客户数", dataIndex: 'S2', align:'center', renderer: getActualValue},
					{header: "超时回访客户数/应回访客户数", dataIndex: 'P2', align:'center', renderer:getPercentValue},
					
					/* {header: '修后三日跟踪', colspan:3, rowspan:2,  colspanName:'修后三日跟踪'} */
					{header: "超时回访客户数", dataIndex: 'C015', align:'center' , colspan:3,  colspanName:'修后三日跟踪', renderer: getActualValue},
					{header: "应回访客户数", dataIndex: 'C014', align:'center', renderer: getActualValue},
					{header: "超时回访客户数/应回访客户数", dataIndex: 'P3', align:'center', renderer:getPercentValue},
					
					/* {header: '工单未及时录入', colspan:3, rowspan:2,  colspanName:'工单未及时录入' }, */
					{header: "已结算工单数", dataIndex: 'C019', align:'center', colspan:3, colspanName:'工单未及时录入', renderer: getActualValue},
					{header: "索赔单数", dataIndex: 'CLAIM_SHEET_COUNT', align:'center', renderer: getActualValue},
					{header: "索赔单数/已结算工单数", dataIndex: 'P4', align:'center', renderer:getPercentValue},
					
					/* {header: '销售订单及时录入', colspan:3, rowspan:2, colspanName:'销售订单及时录入'}, */
					{header: "季度销售订单数", dataIndex: 'IC017', align:'center' , colspan:3, colspanName:'销售订单及时录入', renderer: getActualValue},
					{header: "季度实销数", dataIndex: 'ISALE_CAR', align:'center', renderer: getActualValue},
					{header: "季度实销数/季度销售订单数", dataIndex: 'ICS2', align:'center', renderer:getPercentValue},
					
					/* {header: '其它', colspan:4, rowspan:2, colspanName:'其它' }, */
					{header: "展厅流量", dataIndex: 'C018', align:'center', colspan:5, colspanName:'其它', renderer: getActualValue},
					{header: "潜客数", dataIndex: 'C001', align:'center', renderer: getActualValue},
					{header: "已回访数(修后)", dataIndex: 'C016', align:'center', renderer: getActualValue},
					{header: "已结算工单数", dataIndex: 'C019', align:'center', renderer: getActualValue},
					{header: "总合", dataIndex: 'TOTAL', align:'center', renderer: getActualValue}
		      ];
	
	function getDateValue(value, date, record) {
		return value.substring(0,10);
	}
	
	function getPercentValue(value, date, record) {
		return (value * 100).toFixed(2) + '%';
	}
	
	function getActualValue(value, data, record) {
		if(value == undefined ||  value == "") {
			return '0';
		} else {
			return value;
		}
	}
		      
	<%-- function myLink(dis_id,metaDate,record){
		var vehicleId = record.data.VEHICLE_ID ;
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + vehicleId + "," + dis_id + ");\">[${opera}]</a>");
    }

	function chkArea(vehicleId, dis_id) {
		document.getElementById('vehicle_id').value = vehicleId ;
		document.getElementById('dis_id').value = dis_id ;
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/infoDetailQuery.do" ;
		fm.action=url;
		fm.submit();
	} --%>
</script>  
</body>
</html>