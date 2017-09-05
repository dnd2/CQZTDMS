<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
	List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("winterDetail");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单上报</TITLE>


</HEAD>
<BODY>

<div class="navigation"><img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;冬季保养单详细信息</div>
    <TABLE  class="table_query">
    <tr>
    	<td>
    		保养单详细信息
    	</td>
    </tr>
    <tr>
    	<td class="table_query_2Col_label_6Letter"> 冬季保养单号：</td>
    	<td><%=list.get(0).get("ID") %></td>
    	<td class="table_query_2Col_label_6Letter"> 创建人：</td>
    	<td><%=list.get(0).get("CREATE_BY") %></td>
    	<td></td>
    	<td></td>
    </tr>
    <tr>
    	<td class="table_query_2Col_label_6Letter"> 冬季保养补助：</td>
    	<td><%=list.get(0).get("AMOUNT") %></td>
    	<td class="table_query_2Col_label_6Letter"> 创建时间：</td>
    	<td><%=list.get(0).get("CREATE_DATE").toString().substring(0,19) %></td>
    	<c:forEach items="${winterDetail}" var="st">
    	<c:set var="group_name" value="${st.GROUP_NAME}"></c:set>
    	</c:forEach>
    	<c:if test="${group_name!=null}">
    	<td class="table_query_2Col_label_6Letter">车型：</td>
    	<td> ${group_name }</td>
    	</c:if>
    </tr>
    <tr>
    	<td class="table_query_2Col_label_6Letter"> 保养开始时间：</td>
    	<td><%=list.get(0).get("START_DATE").toString().substring(0,19) %></td>
    	<td class="table_query_2Col_label_6Letter"> 保养结束时间：</td>
    	<td><%=list.get(0).get("END_DATE").toString().substring(0,19) %></td>
    	<td>
    </tr>
     <tr>
    	<td>
    		相关经销商信息
    	</td>
    </tr>
  </table>
  <table class="table_list">
    			<tr class="table_list_th">
    				<th>
    					大区
    				</th>
    				<th>
    					省份
    				</th>
    				<th>
    					经销商代码
    				</th>
    				<th>
    					经销商名称
    				</th>
    			</tr>
    		<% for(int i=0;i<list.size();i++) {
    			Map<String, Object> map = list.get(i);
    		%>
    			<tr>
    				<td>
    					<%=map.get("ROOT_ORG_NAME") %>
    				</td>
    				<td>
    					<%=map.get("ORG_NAME") %>
    				</td>
    				<td>
    					<%=map.get("DEALER_CODE") %>
    				</td>
    				<td>
    					<%=map.get("DEALER_NAME") %>
    				</td>
    			</tr>
    		<%
				}
						
			%>
    		</table>
</BODY>
</html>