<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
List ps=(List)request.getAttribute("ps");
List dateList=(List)request.getAttribute("dateList");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>需求预测上报  </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

<script language="JavaScript">
       function doInit(){
       }
       
       function isSave(){
       		
          if(submitForm('fm')){
      	    MyConfirm("是否确认保存信息?",goSaveRs);
          }
       }
       function goSaveRs(){
       	 $('forcastSave').disabled=true;
       	 var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerRequireForecastSave.do";
       	 var fm = document.getElementById("fm");   
       	 fm.action=url;
       	 fm.submit();
       }
        function showHistoryInfo(groupId){
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/DealerRequireForecastReport/dealerRequireForecastHistoryInit.do?groupId="+groupId,800,500);
       
       }
        
        function onlyNumberAndNopot(obj){
     		obj.value = obj.value.replace(/[^\d]/g,''); 
       }
       
       function checkInputDataformat(id){
   		 var y=document.getElementById(id).value; 
   		 onlyNumberAndNopot(document.getElementById(id));
   	   }
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>需求预测>需求预测上报
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
<h2>需求预测上报 </h2>
<div class="form-body">
 <table class=table_query align=center width="95%">
 	<tr class="tabletitle">
		<td align=left nowrap>预测车型：${modelName }</td>
	</tr>
 </table>
</div>
</div>

 <%
 if(null!=ps&&null!=dateList&&ps.size()>0&&dateList.size()>0){
 %>
 <table class=table_list align=center width="95%">
    <tr>
         <th>配置代码/名称 </th>
         <th>配置名称</th>
         <c:forEach items="${dateList}" var="dateList" varStatus="steps">
              <th>${dateList.YEAR}.${dateList.MONTH}</th>
         </c:forEach>
    </tr>
    <%
        int k=0;
    	for(int j=0;j<ps.size();j++){
    		 Map rsMap=(Map)ps.get(j);
    		 String clazz="";
    		 if(j%2!=0){
    			 clazz="class=\"table_list_row1\"";
    		 }else{
    			 clazz="class=\"table_list_row2\"";
    		 }
    %>
    <tr <%=clazz %>>
    		<td><%=rsMap.get("GROUP_CODE") %></td>
    		<td><%=rsMap.get("GROUP_NAME") %></td>
    		
    		<%
    		   for(int i=0;i<dateList.size();i++){
    			   Map dmap=(Map)dateList.get(i);
    			   String key="D"+i+"";
    			   String id="id"+k;
    			   k++;
    		%>
    			
    		    <td>
    		    	<input type="text" id="<%=id %>" name="<%="ipt"+rsMap.get("GROUP_ID")+"_"+dmap.get("YEAR")+"_"+dmap.get("MONTH") %>" value="<%=rsMap.get(key) %>" size="5" datatype="1,is_digit,4" onkeyup="checkInputDataformat(this.id)"/>
    		    </td>
    		<%	   
    		   }
    		%>
    </tr>
    <%
    }
    %>
    <tr>
        <td class="center" colspan="6">
         <input type="hidden" name="parastr" value="${parastr }" />
         <input type="button" value="保存" class="u-button u-submit" id="forcastSave" onclick="isSave();" />
         <input name="button" type="button" class="u-button u-reset" onclick="history.back();" value="返回" />
       </td>
    </tr>
</table>
<%
 }else{
%>
<table class=table_query align=center width="95%">
 	<tr class="tabletitle">
		<td align=left nowrap>暂无数据</td>
	</tr>
 </table>
<%} %>
</form>
</div>
<p>&nbsp;</p>
</body>
</html>
