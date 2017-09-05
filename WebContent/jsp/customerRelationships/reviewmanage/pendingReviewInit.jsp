<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;我的客户回访 
</div>
  <form method="post" name="fm" id="fm">
  <TABLE class=table_query>
  <td width="189" align="right" class="">客户名称： </td>
    <td width="153" class=""><input id="RV_CUS_NAME" style="WIDTH: 120px" name="RV_CUS_NAME" /></td>
    <td width="165" align="right" class="">回访类型： </td>
    <td width="319" class="">
     <script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	 </script>
    </td>
    <td width="456" class="table"><input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn"  onclick="__extQuery__(1);"/></td>
  
<TBODY>
</TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/pendingReviewQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "回访类型 ",sortable: false,dataIndex: 'RV_TYPE',align:'center'},
				{header: "生成时间",sortable: false,dataIndex: 'RV_DATE',align:'center'},
				{header: "备注",sortable: false,dataIndex: 'RD_CONTENT',align:'center'},
				{header: "查看",sortable: false,dataIndex: 'RV_ID',renderer:myHandler ,align:'center'},
				{header: "批示",sortable: false,dataIndex: 'RV_ID',renderer:reviewLink ,align:'center'}
		      ];
		      
	//查看回访超链接		      
  	function myHandler(value,meta,record){
		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID='+record.data.RV_ID+'">[查看]</a>' ;
	}
	//回访超链接	      
	function reviewLink(value,meta,record)
	{
  		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/Review.do?page=pend&RV_ID='+record.data.RV_ID+'">[回访]</a>' ;
	}
	
</script>  