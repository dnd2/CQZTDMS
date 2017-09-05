<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>计划员与仓库维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
$(function() {
	__extQuery__(1);
});
var myPage;
var url = "<%=request.getContextPath()%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerSelect.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'USER_ID',align:'center',renderer:myLink},
		//	{header: "用户ID",sortable: false,dataIndex: 'USER_ID',align:'center'},
			{header: "用户名称",sortable: false,dataIndex: 'NAME',align:'center'}
	      ];
function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.USER_ID+"' name='"+record.data.USER_ID+"' value='"+record.data.NAME+"'/>"+
			"<input type='radio'  value='"+record.data.USER_ID+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('#plannerID').val(obj.value);
	$('#selectedName').val(document.getElementById(obj.value).value);
	parentDocument.getElementById('plannerID').value = obj.value;
   	parentDocument.getElementById('selectedName').value = document.getElementById(obj.value).value;	
	btnDisable();
	_hide();
}
function returnBefore()
{   var plannerId = 'plannerID';
    var Name = 'selectedName';
	var userId = document.getElementById("plannerID").value;
	var selectedName = document.getElementById("selectedName").value;
	var parentDocument = __parent().document;
	if(userId && userId.length > 0)
		parentDocument.getElementById(plannerId).value = userId;
	if(selectedName && selectedName.length > 0)
	   	parentDocument.getElementById(Name).value = selectedName;	
}
</script> 
</head>
<body onbeforeunload="returnBefore();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 计划员与仓库维护 &gt; 计划员选择</div>
<form name='fm' id='fm'>
<input id="plannerID" name="plannerID" type="hidden" />
<input id="selectedName" name="selectedName" type="hidden" />
	<table class="table_query">
		<tr>            
	        <td class="right" style="width: 80px;">计划员：</td>            
	        <td>
				<input  class="middle_txt" id="plannerName"  name="plannerName" type="text" datatype="1,is_null,20"/>
	        </td>
		</tr>
		<tr>
			<td colspan="2" class="center">
        	    <input class="u-button u-query" type="button" name="BtnQuery" id="queryBtn" value="查 询"  onclick="__extQuery__(1)"/>
				<input class="u-button u-cancel" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
        	</td>
		</tr>       
	</table>
 	<br/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

</form>
</div>
</body>
</html>