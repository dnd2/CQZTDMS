<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库单管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;移库单明细</div>
<form name="fm">
  <table class="table_query">
    <tr class="cssTable" >
    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_2col_label_6Letter">发运仓库：</td>
    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
    	<c:out value="${map.FROM_NAME}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_query_2Col_label_6Letter">目的仓库：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
    	<c:out value="${map.TO_NAME}"/>
    </td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap" class="table_info_2col_label_6Letter">发运状态：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <script type="text/javascript">
      	 	writeItemValue(<c:out value="${map.STATUS}"/>)
      	 </script>
      </td>
      <td align="right" valign="top" nowrap="nowrap" class="table_query_2Col_label_6Letter">发运日期：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	  <c:out value="${map.STO_DATE}"/>
      </td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap" class="table_info_2col_label_6Letter">ERP订单号：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	   <c:out value="${map.ERP_ORDER_NO}"/>
      </td>
      <%
      		Map cmap = (Map)request.getAttribute("map");
      %>
      <td align="right" valign="top" nowrap="nowrap" class="table_query_2Col_label_6Letter" >处理失败原因：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input" title="<%=cmap.get("REFIT_REMARK")==null?"":cmap.get("REFIT_REMARK")%>">
      	       <%
      	       		
          			if(cmap.get("ERP_MSG")!=null){
          				if(String.valueOf(cmap.get("ERP_MSG")).length()<=6){
          		%>
          			<%=cmap.get("ERP_MSG")%>
          		<%
          				}
          		%>
          		<%
          				if(String.valueOf(cmap.get("ERP_MSG")).length()>10){
          					String s = String.valueOf(cmap.get("ERP_MSG"));
          					s = s.substring(0,6);
          		%>
          			<%=s%>...
          		<%
          				}
          			}
          		%>
      </td>
    </tr>
</table>
<table class="table_list" id="table1" >
    <tr align="center" class="cssTable" >
      <th width="5%" nowrap="nowrap">行号</th>
      <th width="24%" nowrap="nowrap">物料编号</th>
      <th width="31%" nowrap="nowrap">物料名称</th>
      <th width="19%" nowrap="nowrap">对应批次</th>
      <th width="21%" nowrap="nowrap">移库数量</th>
    </tr>
	<%
		List list = (List) request.getAttribute("list");
		for(int i=0;i<list.size();i++){
			Map ab = (Map)list.get(i);
	%>
		<tr align="center" class="table_list_row2" class="<%if(i%2 != 0){ %>table_list_row1<%}else{ %>table_list_row2<%} %>">
			<td><%=ab.get("NUM") %></td>
			<td><%=ab.get("MATERIAL_CODE") %></td>
			<td><%=ab.get("MATERIAL_NAME") %></td>
			<td><%=ab.get("BATCH_NO") %></td>
			<td><%=ab.get("AMOUNT") %></td>
		</tr>
	<%
		}
	%>
  </table>
  <table class="table_query">
    <tr class="cssTable" >
      <td width="15%" align="center"><input class="normal_btn"  name="add232" type="button" onclick="_hide();" value="关闭" /></td>
    </tr>
  </table>
  <br>
</form>
</body>
</html>
