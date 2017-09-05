<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商产品套餐明细</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：系统管理 &gt; 产品维护 &gt; 经销商产品套餐明细</div>
 <form method="post" name ="fm" id="fm">
  <table class="table_edit">
    <tr>
      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr bgcolor="F3F4F8">
      <td align="right">套餐代码：</td>
       <td>${map.PACKAGE_CODE }</td>
       <td align="right">套餐名称：</td>
       <td>
          ${map.PACKAGE_NAME }         
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
     <td align="right">省份：</td>
       <td>
          ${map.REGION_NAME }
       </td>
       <td align="right">状态：</td>
       <c:if test="${map.STATUS==10011001 }">
       	<td>有效</td>
       </c:if>
       <c:if test="${map.STATUS==10011002 }">
       	<td>无效</td>
       </c:if>
     </tr>
  </table>
  <table width="100%" class="table_list">
  		<tr>
	    <th colspan="3" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 物料明细</th>
	    </tr>
        <tr class="table_list_th">
            <th>序号</th>
            <th>物料组代码</th>
            <th>物料组名称</th>
       </tr>
       <%
  		List detailMat=(List)request.getAttribute("detailMat");
    	for(int i=0;i<detailMat.size();i++)
    	{
    		Map map=(Map)detailMat.get(i);
    		if(i+1>=4) break;
    		int col=i%2;
    		if(col==0) col=1;
    		else col=2;
  		%>
		<tr class="table_list_row<%=col %>">
			<td ><%=i+1 %></td>
			<td  ><%=map.get("GROUP_CODE") %></td>
			<td  ><%=map.get("GROUP_NAME") %></td>
		</tr>
		<%}%>
    </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
         <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>
