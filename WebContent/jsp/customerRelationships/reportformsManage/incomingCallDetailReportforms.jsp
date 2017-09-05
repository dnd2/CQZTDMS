<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>咨询类别统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报表管理 &gt;来电明细统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />来电明细统计</th>
			
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td>
				<td align="right" nowrap="true">受理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					<input id="downExcel" name="downExcel" type="button" value="导出Excel" class="normal_btn" onclick="downExcelQuery();" />
        		</td>
			</tr>

		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function downExcelQuery(){
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/incomingCallDetailsExcelQuery.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/queryIncomingCallDetails.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "日期 ",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "方式", dataIndex: 'INCOMING_TYPE', align:'center'},
				{header: "姓名",dataIndex: 'CP_NAME',align:'center'},
				{header: "地址", dataIndex: 'CP_PROXY_AREA', align:'center'},
				{header: "来电号码", dataIndex: 'CP_PHONE', align:'center'},
				{header: "联系方式 ", dataIndex: 'CP_CONTACT', align:'center'},
				{header: "了解渠道", dataIndex: 'CP_CUSTOMER_KNOWN_CHANEL', align:'center'},
				{header: "车架号", dataIndex: 'CP_VIN', align:'center'},
				{header: "行驶里程(KM)", dataIndex: 'CP_MILEAGE', align:'center'},
				{header: "购车商家", dataIndex: 'CP_DEAL_DEALER', align:'center'},
				{header: "来电内容", dataIndex: 'CP_CONTENT', align:'center'},
				{header: "受理人反馈内容", dataIndex: 'CP_SEAT_COMMENT', align:'center'},
				{header: "处理状态", dataIndex: 'CP_STATUS', align:'center'},
				{header: "关注车型", dataIndex: 'CP_MODEL_ID', align:'center'},//
				{header: "来电业务类型", dataIndex: 'CP_BIZ_TYPE', align:'center'},
				{header: "来电客户类型", dataIndex: 'CP_CUSTOMER_TYPE', align:'center'},
				{header: "受理人", dataIndex: 'CREATE_BY', align:'center'},
				{header: "处理来电部门", dataIndex: 'CP_DEAL_ORG', align:'center'},
				{header: "大区联系人", dataIndex: 'CP_ORG_RESPONSER', align:'center'},
				{header: "商家联系人 ", dataIndex: 'CP_DEALER_RESPONSER', align:'center'},
				{header: "最新跟进情况 ", dataIndex: 'CP_UPDATE_CONTENT', align:'center'},
				{header: "最近跟踪人 ", dataIndex: 'UPDATE_BY', align:'center'},
				{header: "最近跟踪时间 ", dataIndex: 'UPDATE_DATE', align:'center'}
		      ];
	function   showMonthFirstDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
		  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
	}     
	function   showMonthLastDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
		  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
		  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
	}  
	 $('dateStart').value=showMonthFirstDay();
	 $('dateEnd').value=showMonthLastDay();
</script>
</body>
</html>