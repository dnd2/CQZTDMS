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
<body onLoad="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;我的客户回访 
</div>
  <form method="post" name="fm" id="fm">
  <TABLE class=table_query>
  <tr>
    <td width="9%" align="right" class="table">客户名称： </td>
    <td width="23%" class="table"><input id="RV_CUS_NAME" style="WIDTH: 120px" name="RV_CUS_NAME" /></td>
    <td width="16%" align="right" class="table">回访类型： </td>
    <td width="14%" class="table">
     <script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	</script>
   </td>
    <td width="15%" align="right" class="table">当前状态 ：</td>
    <td width="23%" class="table">
    <select name="RV_STATUS" class="short_sel" id="RV_STATUS">
	    <option selected="selected" value="">--请选择--</option>
	   <c:forEach var="ql" items="${statusList}">
			<option value="${ql.CODE_ID}" >${ql.CODE_DESC}</option>
	   </c:forEach>
	 </select>   
    </td>
  </tr>
<TBODY>
<TR>
  <td align="right" class="table">回访问卷：</td>
  <td class="table">
  <select name="QR_ID" class="long_sel" id="QR_ID">
    <option selected="selected" value="">--请选择--</option>
   <c:forEach var="ql" items="${questionairList}">
		<option value="${ql.QR_ID}" >${ql.QR_NAME}</option>
   </c:forEach>
  </select></td>
  <td align="right" class="table">&nbsp;</td>
  <td class="table">&nbsp;</td>
  <td align="right" class="table">&nbsp;</td>
  <td class="table"><input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn"  onclick="__extQuery__(1);"/></td>
</TR>
</TBODY></TABLE>
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/ReviewManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "回访类型 ",sortable: false,dataIndex: 'RV_TYPE',align:'center'},
				{header: "生成时间",sortable: false,dataIndex: 'RV_DATE',align:'center'},
				{header: "当前状态",sortable: false,dataIndex: 'RV_STATUS',align:'center'},
				{header: "查看",sortable: false,dataIndex: 'RV_ID',renderer:seeLink ,align:'center'},
				{header: "回访",sortable: false,dataIndex: 'RV_ID',renderer:reviewLink ,align:'center'}
		      ];
	//查看回访超链接	      
	function seeLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='seeReview(\""+record.data.RV_ID+"\")'>查看</a>");
	}
	//修改的超链接设置
	function seeReview(RV_ID){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID=' + RV_ID;
	 	fm.submit();
	}
	//回访超链接	      
	function reviewLink(value,meta,record)
	{
		//MyAlert(record.data.RV_STATUS);
		if(record.data.RV_STATUS=="未回访"||record.data.RV_STATUS=="继续回访"){
  			return String.format("<a href='#' onclick='review(\""+record.data.RV_ID+"\")'>回访</a>");
  		}else
  		{
  			return String.format("<a href='#' onclick='updateReview(\""+record.data.RV_ID+"\")'>修改</a>");
  		}
	}
	//回访的超链接设置
	function review(RV_ID){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/Review.do?RV_ID=' + RV_ID; 
	 	fm.submit();
	}
	
	//修改的超链接设置
	function updateReview(RV_ID){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/updateReview.do?RV_ID=' + RV_ID; 
	 	fm.submit();
	}
</script>  