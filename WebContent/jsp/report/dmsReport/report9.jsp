<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData9.do";
       fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;品质情报率大区统计报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%" nowrap="true"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">大区：</td>
      	<td width="15%" nowrap="true">
      		<input name="root_org_name" type="text" id="root_org_name"  class="middle_txt" maxlength="30"/>
      	</td>
       <td align="right" nowrap="true">开始时间：</td>
		<td align="left" nowrap="true" width="25%">
		  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10" readonly="readonly"
                 maxlength="10" group="startDate,endDate" onclick="calendar();"/>
             	 结束时间
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
                 maxlength="10" group="startDate,endDate"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/report/dmsReport/Application/report9.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
		{header: "总分", dataIndex: 'COUNT_SCORE', align:'center'},
		{header: "速度得分", dataIndex: 'IN_TIME_SCORE', align:'center'},
		{header: "质量得分", dataIndex: 'ADD_QUALITY_SCORE', align:'center'},
		{header: "数量得分", dataIndex: 'ADD_NUMBER_SCORE', align:'center'},
		{header: "品质情报数量", dataIndex: 'COUNT_NUM', align:'center'},
		{header: "保修数量", dataIndex: 'APP_NUM', align:'center'},
		{header: "品质情报上报率", dataIndex: 'PERCENT', align:'center'}
	];
</script>
<!--页面列表 end -->
</html>