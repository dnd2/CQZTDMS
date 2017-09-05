<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimOldPartOutPreListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0">   
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
 //  List<ClaimOldPartOutPreListBean> listBean = (List)request.getAttribute("listBean");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件库存明细</div>
 <form method="post" name ="fm" id="fm">
 <input name="partCode" id="partCode" value="${partCode}" type="hidden"/>
 <input name="supCode" id="supCode" value="${supCode}"  type="hidden"/>
 <input name="yieldly" id="yieldly" value="${yieldly}"  type="hidden"/>
 <input name="main" id="main" value="${main}"  type="hidden"/>
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>配件名称</th>
            <th>索赔单号</th>
             <th>经销商代码</th>
            <th>经销商名称</th>
             <th>供应商代码</th>
            <th>供应商名称</th>
            <th>VIN</th>
            <th>车型</th>
            <th>库存</th>
            <th>回运类型</th>
            <th>索赔类型</th>
            <th>切换件</th>
       </tr>
         <c:forEach var="listBean" items="${listBean}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
            <td>
               <c:out value="${listBean.part_name}"></c:out>
           </td>
           <td>
          <a href='#' onClick='claimDetail(${listBean.id});'>[<c:out value="${listBean.claim_no}"></c:out>]</a>
           </td>
             <td>
               <c:out value="${listBean.dealer_code}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.dealer_name}"></c:out>
           </td>
             <td>
               <c:out value="${listBean.supply_code}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.supply_name}"></c:out>
           </td>
            <td>
               <c:out value="${listBean.vin}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.model_name}"></c:out>
           </td>
         
           <td>
           <c:out value="${listBean.all_amount}"></c:out>
           </td>
            <td>
          	<script type="text/javascript">
	              		document.write(getItemValue('${listBean.returnType}'))
	       	</script>
           </td>
            <td>
          	<script type="text/javascript">
	              		document.write(getItemValue('${listBean.claimType}'))
	       	</script>
           </td>
           <td>
           <c:out value="${listBean.is_qhj}"></c:out>
           </td>
         </tr>
      </c:forEach>  
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
       <input type="button" onclick="exportData();" class="normal_btn" style="width=8%" value="导出"/>
         <input type="button" onclick="window.close();" class="normal_btn" style="width=8%" value="关闭"/>
         
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
function claimDetail(id){
		fm.action="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="+id+"&type=99";
		fm.submit();
 }
 function exportData(){
	 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/exportClaimDetail3.do";
	 fm.submit();
 }

</script>
</body>
</html>
