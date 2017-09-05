<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件退赔单</title>
<% 
   String contextPath = request.getContextPath();
	String supName=request.getParameter("supName");
   if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(supName)){
	   supName = new String(supName.getBytes("ISO-8859-1"),"UTF-8");
   }
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;关联件明细</div>
 <form method="post" name ="fm" id="fm">
  <table class="table_list" width="100%">
     <tr >
       <td align="center" class="tdp" colspan="1">供应商代码</td>
       <td align="center" class="tdp" colspan="2">${supCode }</td>
       <td align="center" class="tdp" colspan="1">供应商名称</td>
       <td align="center" class="tdp" colspan="2">${supName }</td>
     </tr>
        <tr >
            <td align="center" class="tdp">序号</td>
            <td align="center" class="tdp">索赔单号</td>
            <td align="center" class="tdp">零部件代码</td>
            <td align="center" class="tdp">零部件名称</td>
            <td align="center" class="tdp">数量</td>
            <td align="center" class="tdp">是否主因件</td>
       </tr>
       <c:set var="pageSize"  value="10000" />
     <c:forEach var="dList" items="${list}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	    <td class="tdp" align="center"  >${dList.CLAIM_NO}</td>
	     <td class="tdp" align="center"  >${dList.PART_CODE}</td>
	  	<td class="tdp" align="center"  >  ${dList.PART_NAME} </td>
	    <td class="tdp" align="center" >${dList.SIGN_AMOUNT}</td>
	    <c:if test="${dList.IS_MAIN_CODE==94001001}">
	    <td class="tdp" align="center" >是</td>
	    </c:if>
	     <c:if test="${dList.IS_MAIN_CODE==94001002}">
	    <td class="tdp" align="center" >否</td>
	    </c:if>
	 </tr>
  </c:forEach>
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
          <input type="button"  onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
</script>
</body>
</html>
