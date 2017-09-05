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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报表管理 &gt;了解渠道统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />了解渠道统计</th>
			
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
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/incomingCallChanelExcelQuery.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/queryIncomingCallChanelReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "了解渠道", dataIndex: 'INCOMINGTYPE', align:'center'},
				{header: "投诉", dataIndex: 'CHANEL0', align:'center'},
				{header: "央视",dataIndex: 'CHANEL1',align:'center'},
				{header: "广播", dataIndex: 'CHANEL2', align:'center'},
				{header: "北汽员工", dataIndex: 'CHANEL3', align:'center'},
				{header: "朋友介绍 ", dataIndex: 'CHANEL4', align:'center'},
				{header: "车展", dataIndex: 'CHANEL5', align:'center'},
				{header: "数字营销", dataIndex: 'CHANEL6', align:'center'},
				{header: "实体店", dataIndex: 'CHANEL7', align:'center'},
				{header: "汽车之家", dataIndex: 'CHANEL8', align:'center'},
				{header: "凤凰网", dataIndex: 'CHANEL9', align:'center'},
				{header: "官网", dataIndex: 'CHANEL10', align:'center'},//
				{header: "易车网", dataIndex: 'CHANEL11', align:'center'},
				{header: "汽车网", dataIndex: 'CHANEL12', align:'center'},
				{header: "搜狐", dataIndex: 'CHANEL13', align:'center'},
				{header: "新浪", dataIndex: 'CHANEL14', align:'center'},
				{header: "腾讯", dataIndex: 'CHANEL15', align:'center'},
				{header: "发布会 ", dataIndex: 'CHANEL16', align:'center'},
				{header: "微信 ", dataIndex: 'CHANEL17', align:'center'},
				{header: "太平洋 ", dataIndex: 'CHANEL18', align:'center'},
				{header: "网上超市 ", dataIndex: 'CHANEL19', align:'center'},
				{header: "网易 ", dataIndex: 'CHANEL20', align:'center'},
				{header: "爱卡 ", dataIndex: 'CHANEL21', align:'center'},
				{header: "车友网 ", dataIndex: 'CHANEL22', align:'center'},
				{header: "其它站点 ", dataIndex: 'CHANEL23', align:'center'}
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