<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtAsWrOldOutDetailBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
   List<TtAsWrOldOutDetailBean> listBean = (List)request.getAttribute("listBean");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件出库明细索赔单明细</div>
 <form method="post" name ="fm" id="fm">
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>索赔单号</th>
            <th>车型</th>
            <th>配件代码</th>
            <th>配件名称</th>
            <th>经销商代码</th>
            <th>经销商名称</th>
            <th>库存数量</th>
            <th>出库数量</th>
       </tr>
       <c:forEach var="listBean" items="${listBean}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.claimNo}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.modelName}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.partCode}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.partName}"></c:out>
           </td>
             <td>
               <c:out value="${listBean.dealerCode}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.dealerName}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.allAmount}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.outAmount}"></c:out>
           </td>
         </tr>
      </c:forEach>  
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
         <input type="button" onclick="_hide()" class="normal_btn" style="width=8%" value="关闭"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
	function goClaimDetail(code){
	var outNo = document.getElementById("outNo").value;
	var codes = document.getElementById("code").value;
	   OpenHtmlWindow("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/claimDetail.do?partCode="
				+ code+"&code="+codes+"&outNo="+outNo ,800,500);
   }

</script>
</body>
</html>
