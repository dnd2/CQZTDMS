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

  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  
  	  <table style="widows: 90%" align="center">
  	   <tr >
  	      <c:forEach items="${ls2}" var="ls2">
    <td colspan="6" align="center"><font size="5">三包件索赔出库单(${jw}${ls2.CODE_DESC}旧件库)</font> </td>
    </c:forEach>
    
    </tr>
  	  </table>
  	  
    <TABLE align="center" border="1" cellpadding="0" cellspacing="0" style="height: 90%;widows: 90%">
    
   
      <c:forEach items="${ls1}" var="ls1">
    <tr>
    <td colspan="2" align="center">出库时间：</td><td align="center">${ls1.STOCK_DATE}  </td>
    <td colspan="2" align="center">出库单号：</td><td align="center">${ls1.STOCK_NO}  </td>
    </tr>
      <tr>
    <td colspan="2" align="center">供应商名称：</td><td align="center">${ls1.SUPPLIER_NAME}  </td>
    <td colspan="2" align="center">供应商代码：</td><td align="center">${ls1.SUPPLIER_CODE}  </td>
    </tr>
    </c:forEach>
       <tr>
         <td align="center">序号</td>
       
          <td  align="center">零件名称 </td>
             <td  align="center">车系 </td>
             
            <td  align="center">出库数</td>        
              <td align="center" colspan="2">件号</td>   
            
       </tr>
       <c:set var="Num" value="${1}"/>
       
        <c:set var="aaa" />
        <c:set var="count" value="${0}"/>
       <c:forEach items="${ls}" var="ls">
       
        <tr>
         <td  align="center">${Num}</td>
     
          <td  align="center">${ls.PART_NAME}</td>
           <td  align="center">${ls.SERIES_NAME}</td>
         
          <td  align="center">${ls.COUNT}</td>
               <td  align="center" colspan="2">${ls.ERPD_CODE}</td>
             
       </tr>
        <c:set var="Num" value="${Num+1}"/>
         <c:set var="aaa" value="${ls.STOCK_ID}" />
          <c:set var="count" value="${count+ls.COUNT}"/>
       </c:forEach>
       
       <tr>
        <td  align="center" colspan="3">合计 </td><td align="center">${count}</td><td></td><td></td>
       </tr>
       
      
        <tr>
        <td  align="center" colspan="5">备注 <td></td></td>
       </tr>
       
       <tr>
         <c:forEach items="${ls1}" var="ls1">
          <td  align="center" colspan="5">&nbsp;${ls1.REMARK} <BR/></td>
         </c:forEach>
       
       </tr>
      <tr style="height: 40px">
      <td colspan="2" align="center"> 库房管理员</td><td>&nbsp;</td>
       <td colspan="2" align="center"> 签字时间</td><td>&nbsp;</td>
      </tr>
       <tr style="height: 40px">
      <td colspan="2" align="center"> 供应商代表</td><td>&nbsp;</td>
       <td colspan="2" align="center"> 签字时间</td><td>&nbsp;</td>
      </tr>
       <tr style="height: 40px" >
      <td colspan="2" align="center"> 分管主任</td><td>&nbsp;</td>
       <td colspan="2" align="center"> 签字时间</td><td>&nbsp;</td>
      </tr>
       <tr style="height: 40px">
      <td colspan="3" align="center"> 批准：</td>
       <td colspan="3" align="center"> 客户部盖章：</td>
      </tr>
      <tr>
     
       <td colspan="6" align="left"> 注：三包件出库索赔单一式四联，第一联白色，客户部留寸；第二联绿色，报质量部；第三联红色，交供应商；第四联黄色，交财务结算。</td>
      </tr>
      <tr>
      
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
function print(){
	    
		  
		  window.print();   
		  
	  }
</script>
</BODY>
</html>