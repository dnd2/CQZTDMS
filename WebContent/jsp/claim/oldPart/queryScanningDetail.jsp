<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.Map"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件扫描出库明细</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
         <td class="table_query_2Col_label_5Letter">序号</td>
        <td class="table_query_2Col_label_5Letter">出库单号 </td>
         <td class="table_query_2Col_label_5Letter">出库类型 </td>
          <td class="table_query_2Col_label_5Letter">零件名称 </td>
           <td class="table_query_2Col_label_5Letter">零件名代码</td>
             <td class="table_query_2Col_label_5Letter">件号</td>
            <td class="table_query_2Col_label_5Letter">出库数量 </td>
             <td class="table_query_2Col_label_5Letter">出库数时间</td>
       </tr>
       <c:set var="Num" value="${1}"/>
       <c:forEach items="${ls}" var="ls">
       
        <tr>
         <td class="table_query_2Col_label_5Letter">${Num}</td>
        <td class="table_query_2Col_label_5Letter">	${ls.STOCK_NO}  </td>
         <td class="table_query_2Col_label_5Letter">${ls.STOCK_TYPE}</td>
          <td class="table_query_2Col_label_5Letter">${ls.PART_NAME}</td>
          <td class="table_query_2Col_label_5Letter">${ls.PART_CODE}</td>
           <td class="table_query_2Col_label_5Letter">${ls.ERPD_CODE}</td>
            <td class="table_query_2Col_label_5Letter">${ls.COUNT} </td>
             <td class="table_query_2Col_label_5Letter">${ls.STOCK_DATE}</td>
           
       </tr>
        <c:set var="Num" value="${Num+1}"/>
       </c:forEach>
       <tr>
       <td> <input class="normal_btn" type="button" id="addButton" name="addButton" value="返回"  onClick="back();"></td>
       </tr>
  </table>
  
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
  function back(){
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryBatchStock.do";
	   fm.method="post";
	   fm.submit();
	  }
</script>
</BODY>
</html>