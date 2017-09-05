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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件扫描出库补录</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	   <input type="hidden" name="stockIds" id="stockIds" value="${stockId}" />
    <TABLE class="table_query">
       <tr>
         <td class="table_query_2Col_label_5Letter">序号</td>
        <td class="table_query_2Col_label_5Letter">出库单号 </td>
         <td class="table_query_2Col_label_5Letter">出库类型 </td>
          <td class="table_query_2Col_label_5Letter">零件名称 </td>
           <td class="table_query_2Col_label_5Letter">零件名代码</td>
           <td class="table_query_2Col_label_5Letter">件号</td>
           <td class="table_query_2Col_label_5Letter">车型</td>
            <td class="table_query_2Col_label_5Letter">出库数量 </td>
             <td class="table_query_2Col_label_5Letter">时间</td>
             <td class="table_query_2Col_label_5Letter">操作</td>
       </tr>
       <c:set var="Num" value="${1}"/>
        <c:set var="aaa" />
         <c:set var="count" value="${0}"/>
       <c:forEach items="${ls}" var="ls">
       
        <tr>
         <td class="table_query_2Col_label_5Letter">${Num}</td>
        <td class="table_query_2Col_label_5Letter">	${ls.STOCK_NO}  </td>
         <td class="table_query_2Col_label_5Letter">${ls.STOCK_TYPE}</td>
          <td class="table_query_2Col_label_5Letter">${ls.PART_NAME}</td>
           <td class="table_query_2Col_label_5Letter">${ls.PART_CODE}</td>
             <td class="table_query_2Col_label_5Letter">${ls.ERPD_CODE}</td>
               <td class="table_query_2Col_label_5Letter">${ls.SERIES_NAME}</td>
            <td class="table_query_2Col_label_5Letter">${ls.COUNT} </td>
             <td class="table_query_2Col_label_5Letter">${ls.STOCK_DATE}</td>
               <td class="table_query_2Col_label_5Letter"><input type="button" value="明细" onClick="queryDetail('${ls.STOCK_ID}','${ls.PART_CODE}','${ls.PART_NAME}');"></td>
       </tr>
        <c:set var="Num" value="${Num+1}"/>
          <c:set var="count" value="${count+ls.COUNT}"/>
        
        <c:set var="aaa" value="${ls.STOCK_ID}"  />
       </c:forEach>
       <tr>
       <td >总数</td>
       <td>${count}</td>
       </tr>
       <tr><td  >件号：</td>
       <td  ><input type="text" id="erpdCode" name="erpdCode"></input></td>
       <td  >配件代码：</td>
       <td  ><input type="text" id="partCode" name="partCode"></input></td>
       
       </tr>
       <tr>
       <td colspan="7"  align="center" > 
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="查询"  onClick="sel();">
       <input type="hidden" id="stockId" name="stockId" />
       <input type="hidden" id="partCode" name="partCode" />
       <input type="hidden" id="partName" name="partName" />
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="补录"  onClick="Bulu(${aaa});">
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="返回"  onClick="back();">
       </td>
       </tr>
       <tr>
       
          <td class="table_query_2Col_label_6Letter" align="right">供应商代码：</td>
   	<td  align="left">
      	<input class="long_txt" id="SUPPLIER_CODE" name="SUPPLIER_CODE" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showSuppliar('SUPPLIER_CODE','SUPPLIER_ID','false')"/>
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="SUPPLIER_ID" name="SUPPLIER_ID" type="hidden" value="">
         </td>
         <td class="table_query_2Col_label_6Letter" align="right">备注：</td>
         <TD>
         <textarea rows="" cols="" ID="remark" name="remark"></textarea>
         </TD>
         
       </tr>
         <td colspan="7"  align="center" > 
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="生成"  onClick="queDing(${aaa});">

       </td>
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
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningStock.do";
	   fm.method="post";
	   fm.submit();
	  }

  function queryDetail(id,partCode,partName){
	  document.getElementById('stockId').value = id;
	  document.getElementById('partCode').value = partCode;
	  document.getElementById('partName').value = partName;
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBuluDetail.do";
	   fm.method="post";
	   fm.submit();
	  
	  }

  function Bulu(stockId){
	  var stockIds=document.getElementById("stockIds").value;
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addBulu.do?stockId="+stockIds;
	   fm.method="post";
	   fm.submit();
	  }

  function queDing(stockId){
	  var SUPPLIER_CODE=document.getElementById("SUPPLIER_CODE").value;
	  var stockIds=document.getElementById("stockIds").value;
	  if(SUPPLIER_CODE==null||SUPPLIER_CODE==''){
			MyAlert("请选择供应商代码");
			return;
			}

	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addBuluQueDing.do?stockId="+stockIds;
	   fm.method="post";
	   fm.submit();
	  
		
	  }


  function sel(){
	  var stockIds=document.getElementById("stockIds").value;
	  var erpdCode=document.getElementById("erpdCode").value;
	  var partCode=document.getElementById("partCode").value;
	  
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBulu1.do?stockId="+stockIds+"&partCode="+partCode+"&erpdCode="+erpdCode;
	   fm.method="post";
	   fm.submit();
	  }
</script>
</BODY>
</html>