<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/ActivityVinManage.json?query=true";//url查询
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
		{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
		{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
		{header: "主题名称", dataIndex: 'SUBJECT_NAME', align:'center'},
		{header: "活动状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
		{id:'action',header: "VIN操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:activityVin,align:'center'}
	];
	function activityVin(value,meta,record){
		return String.format("<a href='<%=contextPath%>/ActivityAction/ActivityVinData.do?activity_id="+value+"'/>[管理]</a>");
	}
	function showsubjectId(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/subjectName.jsp',800,460);
	}
	function myRadio(subject_id,subject_no,subject_name){
		$('subjectId').value=subject_id;
		$('subjectName').value=subject_name;
	}
	function wrapOut(){
		$('subjectId').value="";
		$('subjectName').value="";
	}
	function showActivity(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/activityName.jsp',800,460);
	}
	function myCheck(activity_id,activity_code,activity_name){
		$('activity_code').value=activity_code;
		$('activity_name').value=activity_name;
	}
	function wrapOut1(){
		$('activity_code').value="";
		$('activity_name').value="";
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN管理
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">活动名称：</td>
      	<td width="15%" nowrap="true">
			<input type="text" readonly="readonly" name="activity_name" id="activity_name" class="long_txt"/>
			<input type="hidden" name="activity_code" id="activity_code"/>
			<input type="button" class="mini_btn" value="..." onclick="showActivity();"/>
          	<input type="button" class="normal_btn" value="清除" onclick="wrapOut1();"/>      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">活动主題：</td>
      	<td width="15%" nowrap="true">
	      	<input type="text" readonly="readonly" name="subjectName" id="subjectName" class="long_txt"/>
			<input type="hidden" name="subjectId" id="subjectId"/>
			<input type="button" class="mini_btn" value="..." onclick="showsubjectId();"/>
          	<input type="button" class="normal_btn" value="清除" onclick="wrapOut();"/>
	   	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">活动状态：</td>
		<td width="15%" nowrap="true">
			<script type="text/javascript">
         	genSelBoxExp("status",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","false",'');
         	 </script>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
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
</html>