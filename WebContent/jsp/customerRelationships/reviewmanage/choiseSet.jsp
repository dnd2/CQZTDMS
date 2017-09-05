<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
	__extQuery__(1);
}
	//点击
	function choise(seId,seName){
		parentContainer.setSeat(seId,seName);
	   	_hide();
	}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body  onload="doInit();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;指定回访人查询 
</div>
  <form method="post" name="fm" id="fm">
  <TABLE class=table_query >
 	<tr align="center" >
 	<td>
 	<input name="button" type="button" class="normal_btn" onclick="_hide();"  value="关闭" />
 	</td>
 	</tr>
</TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" ><!--
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/querySetQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"选择", align:'center',sortable:false, dataIndex:'SE_USER_ID',width:'2%',renderer:checkBoxShow},
				{header: "姓名",sortable: false,dataIndex: 'SE_NAME',align:'center'},
				{header: "坐席号",sortable: false,dataIndex: 'SE_SEATS_NO',align:'center'},
				{header: "分机号",sortable: false,dataIndex: 'SE_EXT',align:'center'},
				{header: "坐席级别",sortable: false,dataIndex: 'SE_LEVEL',align:'center',renderer:getItemValue}
		      ];
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='radio' id='"+record.data.SE_USER_ID+"' name='code' onclick='choise(\""+record.data.SE_USER_ID+"\",\""+record.data.SE_NAME+"\");' />");
	}
	
	</script>  