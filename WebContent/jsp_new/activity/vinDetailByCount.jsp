<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/vinDetailByCount.json?query=true";
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "是否维修", dataIndex: 'IS_REPAIR', align:'center'}
	];
	
	function doQuery(){
	   
	  __extQuery__(1);
	}
	function exportToexcel(){
	   fm.action="<%=contextPath%>/ActivityAction/exportToexcel.do";
       fm.submit();
	}
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN明细展示
</div>
<form name="fm" id="fm">
<!-- 查询条件 end -->
  <input type="hidden" class="middle_txt" name="activity_code" id="activity_code" value="${activity_code }" maxlength="30"/>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
  <tr>
    <td width="12.5%"></td>
    <td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">是否维修：</td>
    <td width="15%">
       <select class="short_sel" id="is_repair" name="is_repair">
         <option value="">-请选择-</option>
         <option value="0">是</option>
         <option value="1">否</option>
       </select>
    </td>
    <td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">VIN：</td>
    <td width="15%" nowrap="true">
      <input name="vin" type="text" id="vin" maxlength="100" class="middle_txt"/>
    </td>
    <td width="12.5%"></td>
  </tr>
  <tr>
  <td align="center" colspan="6">
     <input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onclick="doQuery();"/>
      &nbsp;&nbsp;&nbsp;
     <input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
      &nbsp;&nbsp;&nbsp;
     <input type="button" value="导出" class="normal_btn" onclick="exportToexcel();"/>
  </td>
    
  </tr>
  <td width="12.5%"></td>
  </table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>