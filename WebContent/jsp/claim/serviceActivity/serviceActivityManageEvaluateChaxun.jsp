<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动总结查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估
	</div>
  <form method="post" name="fm" id="fm">
        <table width="100%" class="tab_list">
          <tr>
            <td>服务站代码</td>   
            <td>服务站名称</td> 
            <td>申请金额</td> 
            <td>评估金额</td> 
            <td>评估等级</td> 
            <td>操作</td> 
          </tr> 
          <% int i = 1; %>
          <c:forEach var="Evaluate" items="${Evaluate}">
          <tr>
            <td>${Evaluate.DEALER_CODE}</td>   
            <td>${Evaluate.DEALER_NAME}</td> 
            <td>${Evaluate.REPAIR_TOTAL}</td> 
            <td>${Evaluate.EVALUATE_AMOUNT}</td> 
            <td> 
             <script type='text/javascript'>
		       var proCode=getItemValue('${Evaluate.EVALUATE_RES}');
		       document.write(proCode) ;
		     </script>
			</td> 
            <td><a href="#" onclick="infor(${Evaluate.DEALER_ID},${Evaluate.SUBJECT_ID})">明细</a></td> 
           </tr>
          </c:forEach>
          <tr>
            <td colspan="6">
            	 <INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back>
            </td>
          </tr>
        </table>
       <input type="hidden" name="day" id="day" value="${day}" >
        <input type="hidden" name="iconunt" value="<%= i %>" >
</form>
 <br/>
 <script type="text/javascript" >
 function infor(DEALER_ID,SUBJECT_ID)
   {
		var tarUrl = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityManageSummaryEvaluateInfor.do?DEALER_ID="+DEALER_ID+"&SUBJECT_ID="+SUBJECT_ID;
		var width=200;
		var height=200;
		var screenW = window.screen.width-700;	
		var screenH = document.viewport.getHeight()-200;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		
		OpenHtmlWindow(tarUrl,width,height);
   		
   }
 </script>
</body>
</html>