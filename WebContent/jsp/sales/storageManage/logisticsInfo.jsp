<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>

<title>车辆物流查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  实效信息管理 &gt; 车辆管理 &gt; 车辆物流列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="vin" name="vin" value="${vin}" />
		<table class="table_query" border="0">
			<tr>
			
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" style="display:none;" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<br/>
		<br/>
		<br/>
		<center>
			<input type="button" class="normal_btn" onclick="closeWindow();" value="关  闭 " id="queryBtn" /> 
		</center>
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/CheckVehicle/getLogisticsList.json?COMMAND=1";
	var title = null;
	var columns = [
			//	{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'SERIES_ID',renderer:myLink},
				{header: "车架号", dataIndex: 'VIN', align:'center'},
				{header: "时间", dataIndex: 'ADDRESS_DATE', align:'center'},
				{header: "物流地点", dataIndex: 'ADDRESS', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick='submit_(\""+data.SERIES_ID+"\",\""+data.SERIES_NAME+"\");' />";
    }

	function submit_(intent_id,series_name){
		var textId = "${textId}";
		var textName = "${textName}";
		if (parent.document.getElementById('inIframe')) {
			_hide();
			if(textId!=null&&textId!="") {
				parent.document.getElementById('inIframe').contentWindow.showInfo(intent_id,series_name,textId,textName);
			} else {
				parent.document.getElementById('inIframe').contentWindow.showInfo(intent_id,series_name);
			}
		}else {
			parent._hide();
			parent.showInfo(intent_id,series_name);
		}

	}
	//关闭当前窗口
	function closeWindow(){
		_hide();
	}
</script>    
</body>
</html>