<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件货位详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
$(function(){__extQuery__(1);});
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationQuery.json";
var title = null;
var columns = [
				{header: "配件编码", dataIndex: 'PART_OLDCODE'},
				{header: "配件名称", dataIndex: 'PART_CNAME'},
				{header: "件号", dataIndex: 'PART_CODE'},
				{header: "货位", dataIndex: 'LOC_NAME'},
				{header: "附属货位", dataIndex: 'SUB_LOC'},
				{header: "库管员", dataIndex: 'WH_MAN', style: 'text-align:center'},
				{header: "包装尺寸", dataIndex:'PKG_SIZE', style: 'text-align:left'},
				{header: "最小包装量", dataIndex:'OEM_MIN_PKG'},
				{header: "整包发运量", dataIndex:'MIN_PKG'},
				{header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'}
	      	  ];

function searchInfo()
{
	__extQuery__(1);
}
</script> 
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询 &gt; 货位详情</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="partId" id="partId" value="${partId }"/>
  <input type="hidden" name="WH_ID" id="WH_ID" value="${whId }"/>
  <input type="hidden" name="searchType" id="searchType" value="normal"/>
  <br/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
  <br/>
  <table class="table_query">
      <tr>
        <td colspan="6" class="center">
	  	  <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
        </td>
      </tr>       
  </table>
</form>
</body>
</html>