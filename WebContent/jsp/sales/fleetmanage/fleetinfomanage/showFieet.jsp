<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户报备确认</title>
<script type="text/javascript">
	function doInit(){
   		__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户信息管理 &gt;集团客户是否报备确认</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_5Letter">客户名称：</td>
		    <td>
		    	<input type="text"  class="middle_txt" name="val1"  id="val1" datatype="1,is_name,30" onblur="removeFid()" value="<c:out value="${fname}"/>"/>
		    	<input type="hidden" id="val2" name="val2" value="<c:out value="${fid}"/>">
		    </td>
          </tr>
           <tr>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td align="right"><input name="searchBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" /></td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;</td>
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
    //查询路径
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoAppConfirm/showFieet.json";
				
	var title = null;

	var columns = [
				{header: "报备经销商", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
				{header: "集团客户名称", dataIndex: 'FLEET_NAME', align:'center'},
				{header: "主要联系人", dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "需求车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "需求数量", dataIndex: 'SERIES_COUNT', align:'center'},
				{header: "报备时间", dataIndex: 'SUBMIT_DATE', align:'center'},
				{header: "报备状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];
		      
//设置超链接  begin     
	
	function removeFid()
	{
		document.getElementById("val2").value == "";
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
