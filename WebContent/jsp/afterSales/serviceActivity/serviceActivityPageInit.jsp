<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/jquery/jquery-1.8.0.min.js"></script> --%>
<!-- <script type="text/javascript" >
	var $J = jQuery.noConflict();
</script> -->
<script type="text/javascript">

</script>

<title>服务活动管理</title>
</head>
<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务&gt;服务活动管理
		&gt;服务活动管理
	</div>
<form id="fm" name="fm" method="post">
<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
<table class="table_query">
<tr>
	<td style="text-align:right">活动编号： </td>
	<td align="left">
		<input id="activityCode" class="middle_txt" name="activityCode" >
	 </td>
	<td style="text-align:right">活动名称：</td>
	<td align="left">
		<input class="middle_txt" name="activityName" id="activityName"> 
	</td>
</tr>
<tr>
	<td  style="text-align:right">活动类型： </td>
	<td  align="left">
		<script type="text/javascript">
			genSelBoxExp("activityType", <%=Constant.SERVICEACTIVITY_TYPE_NEW%>, "", true, "", "", "false", '');
		</script>
	 </td>
	<td style="text-align:right">活动状态：</td>
	<td align="left">
		<script type="text/javascript">
			genSelBoxExp("activityStatus", <%=Constant.SERVICEACTIVITY_STATUS_NEW%>, "", true, "", "", "false", '');
		</script>
	</td>
</tr>
	<tr>
		<td colspan="4" style="text-align:center">
			<input class="normal_btn" onclick="__extQuery__(1);" value="查询" name="button" type="button" name="queryBtn">  
			<input class="normal_btn" type="button" name="button_add"  id="button_add" value="新增" onClick="addForm();"/>
		</td>
	</tr>
</table>
</div>
</div>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end -->
</form>
</div>
</body>
<script type="text/javascript">
var myPage;
//查询路径
var url = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityQuery.json";
var title = null;
function doInit(){
	__extQuery__(1);
}

var columns = [ 
          {header : "序号",align : 'center',renderer : getIndex}, 
          {id : 'action',header : "操作",dataIndex : 'ACTIVITY_ID',renderer:myLink}, 
          {header : "活动编号",dataIndex : 'ACTIVITY_CODE', style:"text-align: center;"}, 
          {header : "活动名称",dataIndex : 'ACTIVITY_NAME'}, 
          {header : "活动类型",dataIndex : 'ACTIVITY_TYPE',renderer : getItemValue}, 
          {header : "活动开始时间",dataIndex : 'ACTIVITY_STRATE_DATE', style:"text-align: center;"},
          {header : "活动结束时间",dataIndex : 'ACTIVITY_END_DATE', style:"text-align: center;"}, 
          {header : "活动状态",dataIndex : 'ACTIVITY_STATUS',renderer : getItemValue},
          {header : "发布人",dataIndex : 'RELEASE_BY'}, 
          {header : "发布时间",dataIndex : 'RELEASE_DATE'}
         ];
         
function myLink(value, meta, record) {
		 if(record.data.ACTIVITY_STATUS == 96291001){
			return String.format("<a class='u-anchor' href='#' onclick='check("+value+")' id=''>[查看]</a>" +
					"<a href='#' onclick='updateData(\""+ value +"\",\""+ record.data.ACTIVITY_TYPE +"\")' id=''>[修改]</a>" +
					"<a href='#' onclick='deleteData("+value+")' id=''>[删除]</a>" +
					"<a href='#' onclick='ChangeStatus("+value+")' id=''>[发布]</a>");
		}else{
			return String.format("<a href='#' onclick='check("+value+")' id=''>[查看]</a>");
		} 
		
} 
/**
* 查看
* @param value
*/
function check(value){
	var id ="?type=1&id="+value;
	<%-- window.location.href = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityUpdate.do"+id; --%>
	
	var url = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityUpdate.do"+id;
	OpenHtmlWindow(url, 850, 600, '服务活动管理信息查看');
}
/**
* 修改
* @param value
*/
function updateData(value,type){
	var id ="?type="+type+"&id="+value;
	window.location.href = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityUpdate.do"+id;
}
function deleteData(value){
	
}

/**
* 删除
* @param id
*/
function deleteData(value){
	MyConfirm("确定要删除吗？",deleteDataDo,[value]);
}
function deleteDataDo(value){
	sendAjax("<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/deleteData.json?id="+value,deleteDataResult,"fm");
}
function deleteDataResult(obj){
	 if(obj.msg=="01"){
		   MyAlert("删除成功！");
		   doInit();
	   }else{
		   MyAlert("删除失败");
	   }
}

/**
* 提交
* @param id
*/
function ChangeStatus(value){
	MyConfirm("确定要发布吗？",ChangeStatusDo,[value]);
}
function ChangeStatusDo(value){
	sendAjax("<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/changeStatus.json?id="+value,ChangeStatusResult,"fm");
}
function ChangeStatusResult(obj){
	if(obj.msg=="01"){
		   MyAlert("发布成功！");
		   doInit();
	   }else{
		   MyAlert("发布失败");
	   }
}


/**
* 新增用户按钮事件
*/
/* function addForm(){
	window.location.href = toEditOrCheckUrl;
}
 */
/**
* 新增用户按钮事件
*/
function addForm(){
	OpenHtmlWindow('<%=contextPath%>/jsp/afterSales/serviceActivity/serviceActivityCheck.jsp',600,250);
}
 
function getActivityInfo(str){
	fm.action = '<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityDetail.do?flag='+str;
	fm.submit();
} 
</script>
</html>