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
<title>回访结果统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报告管理 &gt;回访结果统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />回访结果统计</th>
			
			<tr>
				<td align="right" nowrap="true">回访人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td>
				<td align="right" nowrap="true">回访日期：</td>
				<td align="left" nowrap="true">
				<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
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
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/ReturnVisitReportforms/returnVisitReportformsExcel.do';
		fm.submit();
	}
	
	function search(){
		__extQuery__(1);
	}

	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/ReturnVisitReportforms/queryReturnVisitReportforms.json";
				
	var title = null;
	var columns =  [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "分类",dataIndex: 'MODELNAME',align:'center'},
				{header: "回访总数",dataIndex: 'TOTAL',align:'center'},
				{header: "成功回访数量",dataIndex: 'SUCC',align:'center'},
				{header: "不成功回访数量", dataIndex: 'FAILC', align:'center'},
				{header: "回访成功率",dataIndex: 'SUCR',align:'center'},
				{header: "一次成功回访数量",dataIndex: 'ONCESUCC',align:'center'},
				{header: "一次不成功回访数量", dataIndex: 'ONCEFAILC', align:'center'},
				{header: "一次成功回访率", dataIndex: 'ONCESUCR', align:'center'},
				{header: "满意数量", dataIndex: 'SATISC', align:'center'},
				{header: "不满意数量",dataIndex: 'NOSATISC',align:'center'},
				{header: "满意率",dataIndex: 'SATISR',align:'center'},
				{header: "一次满意数量", dataIndex: 'ONCESATISC', align:'center'},
				{header: "一次满意率", dataIndex: 'ONCESATISR', align:'center'},			
				{header: "无人接听数量", dataIndex: 'FAIL1', align:'center'},
				{header: "无人接听率",dataIndex: 'FAIL1R',align:'center'},
				{header: "拒访数量",dataIndex: 'FAIL2',align:'center'},
				{header: "拒访率", dataIndex: 'FAIL2R', align:'center'},
				{header: "空号数量", dataIndex: 'FAIL3', align:'center'},
				{header: "空号率", dataIndex: 'FAIL3R', align:'center'},
				{header: "电话有误数量",dataIndex: 'FAIL4',align:'center'},
				{header: "电话有误率",dataIndex: 'FAIL4R',align:'center'},
				{header: "无法联系数量", dataIndex: 'FAIL5', align:'center'},
				{header: "无法联系率", dataIndex: 'FAIL5R', align:'center'},
				{header: "无法接通数量",dataIndex: 'FAIL6',align:'center'},
				{header: "无法接通率",dataIndex: 'FAIL6R',align:'center'},
				{header: "不成功回访数量", dataIndex: 'FAIL7', align:'center'},
				{header: "不成功回访率", dataIndex: 'FAIL7R', align:'center'}
		      ];
		      

</script>
</body>
</html>