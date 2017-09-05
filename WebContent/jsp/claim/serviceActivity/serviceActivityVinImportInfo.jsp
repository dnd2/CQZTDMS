<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-VIN导入清单</title>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript">
//功能：导入数据;
//描述：信息到服务活动业务主表中[TT_AS_ACTIVITY_VEHICLE];
function checkedConfirm(){
	MyDivConfirm("确认导入正确有效信息?",ImportVehicle);
}
function ImportVehicle(){
	 var activityId=document.getElementById("activityId").value;
	 var flag=document.getElementById("flag").value;
	 fm.action="<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportVehicle.do?activityId="+activityId+"&flag="+flag;
	 fm.submit();
}
</script>
</head>

<body onload="__extQuery__(1);">
<form id="fm" name="fm">
	 <input type="hidden" name="flag" id="flag" value="<%=request.getAttribute("flag") %>"/>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
	<table class="table_list" style="border-bottom: 1px solid #DAE0EE">
	<tr>
		<td>	
		   <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>" />
		</td>
	</tr>
	</table>
<br/>
 <!--分页开始 -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 结束 --> 
</form>
<!-- 页面列表 开始 -->
		<script type="text/javascript" >
		    var activityId=document.getElementById("activityId").value;
			var myPage;
		
			var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportQuery.json?activityId="+activityId;
						
			var title = null;
		
			var columns = [
						{header: "VIN", dataIndex: 'VIN', align:'center'}
				      ];
		</script>
<!-- 页面列表结束 -->
<table class="table_query">
	<tr>
	    <td align="center"> <input class="normal_btn" type="button" name="button" value="确定"  onclick="checkedConfirm();"/></td>
		<td align="center"> <input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();"/></td>
	</tr>
</table>
</body>
</html>
