<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动相关查询</title>
<% String contextPath = request.getContextPath();%>
<script>
var arrayobj = new Array();
function doInit() {
	__extQuery__(1);
}
</script>
</head>

<body>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" />&nbsp; 售后服务管理>服务活动管理>服务活动相关查询
</div>
<form name="fm" id="fm">
  	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>	
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">活动编号：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text"  datatype="1,is_noquotation,30" id="activityCode" name="activityCode"/>
		</td>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">活动名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="activity_name" name="activity_name"/>
		</td>
	</tr>
	<tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" />
    		<input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
    		<input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/>
    	</td>
    </tr>
    </table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="javascript">
 	var myPage;
	var url = "<%=contextPath%>/claim/serviceActivity/"
	          +"ServiceActivityPlanAnalyse/serviceActivityPlanAnalyseQueryName.json";
       
	var title = null;
	var columns = [
				{header:'序号',renderer:getIndex,align:'center'},
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
				{header: "活动开始日期 ",dataIndex: 'STARTDATE' ,align:'center'},
				{header: "活动结束日期",dataIndex: 'ENDDATE' ,align:'center'},
				{header: "选择", dataIndex: '', align:'center',renderer:mySelect}
		      ];
		      
	
function mySelect(value,metaDate,record){
    
    return String.format("<input type='radio' name='rd' onclick='showNo(\""+record.data.ACTIVITY_ID+"\", \""+record.data.ACTIVITY_CODE+"\",\""+record.data.ACTIVITY_NAME+"\")'/>");
}	
function showNo(activity_id,activity_code,activity_name) {
	parent._hide();
	goTo(activity_id,activity_code,activity_name);
} 

//刷新父页面
function goTo(activity_id,activity_code,activity_name){
	if (parent.$('inIframe')) {
	    
	    parentContainer.showName(activity_id,activity_code,activity_name);
	} else {
		parent.showName(activity_id,activity_code,activity_name);
	}
}
function requery() {
	$('activityCode').value="";
	$('activity_name').value="";
}
</script>
</body>

</html>
