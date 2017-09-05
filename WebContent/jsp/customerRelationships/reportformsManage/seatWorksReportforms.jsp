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
<title>坐席工作量统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报告管理 &gt;坐席工作量统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />坐席工作量统计</th>
			
			<tr>
				<td align="right" nowrap="true">坐席：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td>
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
				<td align="right" nowrap="true">排班日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="paiStart" name="paiStart" datatype="1,is_date,10"
                           maxlength="10" group="paiStart,paiEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'paiStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="paiEnd" name="paiEnd" datatype="1,is_date,10"
                           maxlength="10" group="paiStart,paiEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'paiEnd', false);" type="button"/>
				</td>			
			</tr>
			
			<tr>
				<td colspan="6" align="center">
					<input align="right" class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="search();" />
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
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/SeatWorksReportforms/seatWorksReportformsExcel.do';
		fm.submit();
	}
	
	function search(){
		var paiStart = document.getElementById('paiStart').value;
		var paiEnd = document.getElementById('paiEnd').value;
		 if((paiStart.length > 0 && paiEnd.length == 0 ) || (paiStart.length == 0 && paiEnd.length > 0 )  )
		 {
		 	MyAlert('排班时间要选择必须同时选！');
		 }else
		 {
		 		__extQuery__(1);
		 }
	}
	
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/SeatWorksReportforms/querySeatWorksReportforms.json";
				
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "工号",dataIndex: 'ACC',align:'center'},
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "白班投诉电话", dataIndex: 'COMCM', align:'center'},
				{header: "晚班投诉电话", dataIndex: 'COMCN', align:'center'},
				{header: "9、10月投诉电话", dataIndex: 'COMCB', align:'center'},
				{header: "回访班投诉电话", dataIndex: 'COMCC', align:'center'},
				{header: "白班咨询电话", dataIndex: 'COUCM', align:'center'},
				{header: "晚班咨询电话", dataIndex: 'COUCN', align:'center'},
				{header: "9、10月咨询电话", dataIndex: 'COUCB', align:'center'},
				{header: "回访班咨询电话", dataIndex: 'COUCC', align:'center'},
				{header: "'1333'抽查回访（成功）",dataIndex: 'RVDS',align:'center'},
				{header: "'1333'抽查回访（不成功）", dataIndex: 'RVEF', align:'center'},
				{header: "客户关怀回访（成功）",dataIndex: 'RVFS',align:'center'},
				{header: "客户关怀回访（不成功）", dataIndex: 'RVGF', align:'center'},
				{header: "市场调查回访（成功）",dataIndex: 'RVHS',align:'center'},
				{header: "市场调查回访（不成功）", dataIndex: 'RVIF', align:'center'},
				{header: "服务站满意度回访（成功）",dataIndex: 'RVJS',align:'center'},
				{header: "服务站满意度回访（不成功）", dataIndex: 'RVKF', align:'center'}
		      ];
	
		      

</script>
</body>
</html>