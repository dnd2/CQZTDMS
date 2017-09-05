<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>紧急调件备注增加页面</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
</head>
<script type="text/javascript">
   function addsure(){
	 var remark =$("#remark").val().trim();
	 var balance_oder =$("#balance_oder").val();
	 var url =  "<%=contextPath%>/InvoiceAction/addRemarkPayment.json?type=addsure&&balance_oder="+balance_oder;
	 makeNomalFormCall1(url,function showFunc(json){
       if(json.succ=="1"){
          MyAlert("提示：操作成功！");
          _hide();
         }else{
          MyAlert("提示：操作失败！");
         }
	 },"fm");
   }
</script>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;结算开票管理 &gt;添加备注</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
      <input type="hidden" name="canAdd" id="canAdd" value="${canAdd }" />
       <input type="hidden" name="dealerLevel" id="dealerLevel" value="${dealerLevel }" />
      <input type="hidden" name="report_id" id="report_id" value="" />
     <input type="hidden" name="dtlIds" id="dtlIds" value="2012042543012287" />
      <input type="hidden" name="msg" id="msg" value="${msg }" />
      <input type="hidden" name="balance_oder" id="balance_oder" value="${balance_oder }" />
            
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>

<br>
<script type="text/javascript">
   function doInit(){
	   loadcalendar();
   }
	    function  returnapply(){
              window.location.href="<%=contextPath%>/OldReturnAction/oldPartApplyList.do";
        }
</script>
  <div style="margin-bottom: 5px;">添加备注:</div>
  <div>
     <textarea rows="10" cols="80" id="remark" name="remark">${remark }</textarea>
  </div>
   <br/>
   <br/>
   <div style="margin-left: auto;margin-right: auto;" align="center">
 <input type="button" value="&nbsp;&nbsp;确&nbsp;&nbsp;定&nbsp;&nbsp;" onclick="addsure();" style="width: 60px;"  />
 &nbsp;&nbsp;
 <input type="button" value="&nbsp;&nbsp;返&nbsp;&nbsp;回&nbsp;&nbsp;" onclick="_hide();" style="width: 60px;" />
   </div>
</form> 
</body>
</html>