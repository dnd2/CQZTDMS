<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>热线抽查管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;热线抽查管理
</div>
  <form method="post" name="fm" id="fm">
  <TABLE class=table_query>
	  <tr>
	    <td width="9%" align="right" class="table">经销商CODE： </td>
	    <td width="23%" class="table"><input id="dealerCode" name="dealerCode" style="WIDTH: 120px"/></td>
	    <td width="16%" align="right" class="table">经销商名称： </td>
	    <td width="14%" class="table">
	    	<input id="dealerName" name="dealerName" style="WIDTH: 120px" />
	   </td>
	  </tr>
	  <tr>
	  	<td class="table"><input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn"  onclick="__extQuery__(1);"/></td>
	  	<td class="table"><input class="normal_btn" type="button" value="新增" name="add" id="add"  onclick="add();"/></td>
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
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/hotSpotQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'}, 
				{header: "经销商名称 ",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},
				{header: "联系电话",sortable: false,dataIndex: 'PHONE',align:'center'},
				{header: "抽查人",sortable: false,dataIndex: 'SPOT_USER_NAME',align:'center'},
				{header: "抽查结果",sortable: false,dataIndex: 'SPOT_RESULT',align:'center'},
				{header: "无人接听次数",sortable: false,dataIndex: 'SPOT_TIMES',align:'center'},
				{header: "是否满意",sortable: false,dataIndex: 'SPOT_SATISFACTION',align:'center'},
				{header: "抽查时间",sortable: false,dataIndex: 'SPOT_DATE',align:'center'},
				{header: "操作",sortable: false,dataIndex: 'RV_ID',renderer:manage ,align:'center'}
		      ];
	 
	function manage(value,meta,record)
	{
  		return String.format("<a href='#' onclick='seeReview(\""+record.data.RV_ID+"\")'>查看</a>");
	}
	
	function seeReview(RV_ID){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID=' + RV_ID;
	 	fm.submit();
	}
</script>  