<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件审核明细</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="i_back_id" id="i_back_id" value="" />
 
  <table width="100%" class="table_list">
  		<tr>
	    <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 回运清单明细</th>
	    </tr>
        <tr class="table_list_th">
            <th>序号</th>
            <th>索赔申请单</th>
            <th>VIN</th>
            <th>配件代码</th>
            <th>配件名称</th>
            <th>供应商代码</th>
            <th>回运数</th>
            <th>签收数</th>
            <th>装箱单号</th>
           <!--  <th>库区</th> -->
            <th>是否入库</th>
            <th>扣除说明</th>
       </tr>
       <c:forEach var="detailList" items="${detailList}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.claim_no}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.vin}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.part_code}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.part_name}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.producer_code}"></c:out>
           </td>           
           <td>
               <c:out value="${detailList.return_amount}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.sign_amount}"></c:out>
           </td>
           <td>
              <c:out value='${detailList.box_no}'/>
           </td>
            <td>
             <c:out value='${detailList.in_warhouse_status_name}'/>
           </td>
         <!--   <td>
              <c:out value='${detailList.warehouse_region}'/>
           </td> -->
           <td>
           	  <c:if test="${detailList.deduct_remark == '95061093'}">
           	  		<c:out value='${detailList.deduct_desc}:${detailList.other_remark}'/>
           	  </c:if>
           	  
           	  <c:if test="${detailList.deduct_remark != '95061093'}">
           	  		<c:out value='${detailList.deduct_desc}'/>
           	  </c:if>
              
           </td>
         </tr>
      </c:forEach>  
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
