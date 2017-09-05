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
<title>汇总统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报表管理 &gt;投诉咨询汇总统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉咨询汇总统计</th>
			
			<tr>
				<!-- <td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td> -->
				<td align="right" nowrap="true">日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td align="left">
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
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/incomingCallDayReportExcel.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/incomingCallDayReportQuery.json";
				
	var title = null;
	//onload="__extQuery__(1)"
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "潜在用户(来电)", dataIndex: 'CHANEL0', align:'center'},
				{header: "潜在商家",dataIndex: 'CHANEL1',align:'center'},
				{header: "已购车用户", dataIndex: 'CHANEL2', align:'center'},
				{header: "其他咨询", dataIndex: 'CHANEL3', align:'center'},
				{header: "投诉  ", dataIndex: 'NUM1', align:'center'},
				{header: "合计", dataIndex: 'SUM1', align:'center'},
				{header: "S2", dataIndex: 'MODEL0', align:'center'},
				{header: "S3", dataIndex: 'MODEL1', align:'q'},
				{header: "不确定", dataIndex: 'MODEL2', align:'center'},//来电咨询 
				{header: "潜在用户(在线咨询)", dataIndex: 'CHANELW', align:'center'},
				{header: "潜在商家", dataIndex: 'CHANELX', align:'center'},//
				//{header: "已购车用户(在线咨询)", dataIndex: 'CHANELY', align:'center'},
				{header: "其他", dataIndex: 'CHANELZ', align:'center'},
				{header: "合计", dataIndex: 'SUMW', align:'center'},
				{header: "S2", dataIndex: 'MODELW', align:'center'},
				{header: "S3", dataIndex: 'MODELX', align:'center'},
				{header: "不确定", dataIndex: 'MODELY', align:'center'},
				{header: "潜在用户(数字营销)", dataIndex: 'CHANELA', align:'center'},
				{header: "潜在商家 ", dataIndex: 'CHANELB', align:'center'},
				//{header: "已购车用户(数字营销) ", dataIndex: 'CHANEL17', align:'center'},
				{header: "合计 ", dataIndex: 'SUMA', align:'center'},
				{header: "S2 ", dataIndex: 'MODELA', align:'center'},
				{header: "S3 ", dataIndex: 'MODELB', align:'center'},
				{header: "不确定", dataIndex: 'MODELC', align:'center'},
				{header: "当日数量总计(总计) ", dataIndex: 'SUMDAY', align:'center'},
				{header: "潜在用户 ", dataIndex: 'SUMQK', align:'center'},
				{header: "潜在商家 ", dataIndex: 'SUMQS', align:'center'},
				{header: "已购车用户 ", dataIndex: 'SUMCUS', align:'center'},
				{header: "其他咨询 ", dataIndex: 'SUMINNER', align:'center'},
				{header: "投诉 ", dataIndex: 'SUMCP', align:'center'},
				{header: "S2", dataIndex: 'SUMS2', align:'center'},
				{header: "S3", dataIndex: 'SUMS3', align:'center'},
				{header: "不确定", dataIndex: 'SUMS23', align:'center'}
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