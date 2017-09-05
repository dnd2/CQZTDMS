<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Date" %>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt;回访进度查询
</div>
<form method="post" name="fm" id="fm">
<TABLE class=table_query>
  <TBODY>
    <tr class="">
      <td width="15%" align="right" nowrap="nowrap">回访日期：</td>
      <td width="27%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
 至 
<input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <td width="12%" align="right">回访人：</td>
      <td width="16%"><input  id="RV_ASS_USER" class="Wdate" style="WIDTH: 120px" name="RV_ASS_USER" /></td>
      <td width="15%" align="right">&nbsp;</td>
      <td width="15%"><span class="table">
        <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" align="right" value="查询" />
        </span></td>
    </tr>
    </TBODY>
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewScheduleQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "工号",sortable: false,dataIndex: 'SE_SEATS_NO',align:'center'}, 
				{header: "姓名 ",sortable: false,dataIndex: 'RV_ASS_USER',align:'center'},
				{header: "已分配回访",sortable: false,dataIndex: 'TYPE0',align:'center'},
				{header: "已完成回访",sortable: false,dataIndex: 'TYPE3',align:'center'},
				{header: "尚未回访",sortable: false,dataIndex: 'TYPE1',align:'center'},
				{header: "继续回访",sortable: false,dataIndex: 'TYPE2',align:'center'},
				{header: "回访总量",sortable: false,dataIndex: 'TYPE4',align:'center'}
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
  		return String.format("<a href='#' onclick='review(\""+record.data.RV_ID+"\")'>回访</a>");
	}
	//修改的超链接设置
	function review(RV_ID){
	
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/Review.do?RV_ID=' + RV_ID; 
	 	fm.submit();
	}
</script>  