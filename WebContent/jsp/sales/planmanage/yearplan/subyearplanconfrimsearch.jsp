<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contextPath=request.getContextPath();
String[][] tableArr=(String[][])request.getAttribute("tableArr");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>年度目标确认</title>

<script language="JavaScript">
	function searchSubmit(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=contextPath %>/sales/planmanage/YearTarget/SubYearTargetConfirm/yearTargetConfirmSearch.do";
    	$("fm").submit();
	}
	function subChecked(){
	    var str="";
		MyConfirm("是否执行确认操作?",confirmSubmit);
	}
	function confirmSubmit(){
		$('fm').action=  "<%=contextPath %>/sales/planmanage/YearTarget/SubYearTargetConfirm/ConfrimYearPlan.do";
		$('fm').submit();
	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/PunishmentApplyCarMantain/getOrderIdInfo.do?orderId='+value,800,500);
	}
</script>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>年度目标${year}年度目标确认	</div>
<form name="fm" method="post" >
<table class="table_query">
 <tr>
    <td align="right" width="40%"> 业务范围： </td>
    <td>
      <select name="buss_area">
	       <c:forEach items="${areaBusList}" var="areaBusList" >
	       <c:choose>
	       	    <c:when test="${areaBusList.AREA_ID == areaId}">
	       		    <option value="${areaBusList.AREA_ID }" selected="selected">${areaBusList.AREA_NAME }</option>
	       		</c:when>
	       		<c:otherwise>
					<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
				</c:otherwise>
			</c:choose>
		   </c:forEach>
      </select>
    </td>
  </tr>  
  <tr>
        <td align="right" width="40%">计划类型： </td>
        <td>
           <script type="text/javascript">
				genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,${planType},false,"short_sel","","false",'');
			</script>
        </td>
     </tr>
  <tr>
    <td align="right" width="40%">选择年份：</td>
    <td>
      <select name='year'>
        <%
        	String yearOptions=(String)request.getAttribute("options");
            out.println(yearOptions);
        %>
      </select>
    </td>
  </tr>
  <tr>
         <td colspan="11" align="center">
	       <input type="button" class="cssbutton"  name="vdcConfirm" value="查询" onclick="searchSubmit();" />
	     </td>
  </tr>
</table>
<br>
<%
if(tableArr.length>0){
%>
<table class="table_list">
  <tr>
<%
	for(int i=0;i<tableArr[0].length;i++){
%>
        <th><%=tableArr[0][i] %></th>
<%		
	}
%>
  </tr>

<%
    for(int i=1;i<tableArr.length;i++){
%>
  <tr <%if(i%2==0){out.println("class='table_list_row1'");}else{ out.println("class='table_list_row2'");} %>>
    	<%
    	   for(int j=0;j<tableArr[i].length;j++){
    		   %>
    		   <td><%=tableArr[i][j] %></td>
    		   <%
    	   }
    	%>
    </tr>
<%
    }
%>  
</table>
<br>
<table class=table_query>
	<tr>
	   <td colspan="6" nowrap="nowrap">
       	  新版本号:<input type="text" name="plan_ver" size="17" value="${maxVer}" class="middle_txt" readonly="true" />      
       	  版本描述:<input type="text" name="plan_desc" size="17" value="<%=new Date().toLocaleString().substring(0,9) %>" class="middle_txt" /> 
       	  <input type="button" class="cssbutton"  name="vdcConfirm" value="确认" onclick="subChecked();" />
       	  <input type="hidden" name="year" value="${year }" />   
       	  <input type="hidden" name="areaId" value="${areaId }" />
       	  <input type="hidden" name="planType" value="${planType }" />  
       </td>
	</tr>
</table>
<%
}else{
%>
<div class="wbox">
<table class="table_query">
  <tr>
    <td align="center">
		<font color="red">没有满足条件的数据</font>
    </td>
  </tr>
</table>
</div>
<%
}
%>
</form>
</body>
</html>
