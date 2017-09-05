<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;经销商信息变更&gt;无费用(无旧件)申明
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td align="right" nowrap="true">变更日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="changeTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="changeTime1" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">申明时间段：</td>
		<td align="left" nowrap="true">
			<input type="text" name="change_date" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="change_review_date" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
		</td>
		<td align="right" nowrap="true">申明类型：</td>
        <td>
        	<script type="text/javascript">
				 genSelBoxExp("type",<%=Constant.CHANGE_TYPE%>,"",true,"short_sel","","true",'');
		    </script>
        </td>
	</tr>
	<tr>
		<td align="center" colspan="4"><font color="red">*新增必须选择申明类型</font></td>
	</tr>
    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		<input type="button" name="BtnAdd" id="queryBtn"  value="新增"  class="normal_btn" onClick="addRecord()" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/queryOldChangeInfo.json";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "基地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},
				{header: "变更类型", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "变更时间", dataIndex: 'CHANGE_TIME', align:'center'},
				{header: "申明时间起", dataIndex: 'CHANGE_DATE', align:'center'},
				{header: "申明时间止", dataIndex: 'CHANGE_REVIEW_DATE', align:'center'},
				//{header: "申明人", dataIndex: 'NAME', align:'center'},
				{header: "操作", dataIndex: 'ID', align:'center',renderer:MyLink}
		      ];
		      	
	function MyLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>");
	}
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/changeInfo.do?id="+id;
		fm.submit();
		//OpenHtmlWindow(tarUrl,width,height);
	}
	function addRecord() {
		var type = document.getElementById('type').value;
		if(type=='' || type==null){
			MyAlert("申请类型必须选择!");
			return;
		}
		var url = '<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/changeDateInit.do';
		fm.action = url;
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>