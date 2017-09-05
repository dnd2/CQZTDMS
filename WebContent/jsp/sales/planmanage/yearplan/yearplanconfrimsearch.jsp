<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务确认</title>
<script language="JavaScript">
	$(document).read(function(){
		setDate();	//页面初始化
	});
	function searchSubmit(){
		if(!submitForm('fm')){
			return false;
		}
		var fsm = document.getElementById("fm");
		fsm.action = "<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSearch.do";
    	fsm.submit();
	}
	function subChecked(){
	    var str="";
		MyConfirm("是否执行确认操作?",confirmSubmit);
	}
	function confirmSubmit(){
		var fsm = document.getElementById("fm");
		fsm.action=  "<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSubmint.do";
		fsm.submit();
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
	//•	版本描述默认显示“当前日期”，格式为“YYYY-MM-DD”。
	function setDate(){
		var myDate = new Date();
		var aDate = myDate.Format("yyyy-MM-dd");
		document.getElementById("plan_desc_id").value = aDate;
	}	
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>年度任务>${year}年度任务确认	</div>
<div class="form-panel">
	<h2>年度任务确认	</h2>
	<div class="form-body">
	<form name="fm"  id="fm" method="post" >
		<table class="table_query table_list">
		  <tr>
				    <th><div class="center">经销商代码</div></th>
		            <th><div class="center">经销商名称</div></th>
		            <th><div class="center">车系代码</div></th>
		            <th><div class="center">车系</div></th>
		            <th><div class="center">年度任务</div></th>
		  	   </tr>
		  	<c:forEach items="${list}" var="list" >
				<tr>
					<td><div class="center">${list.DEALER_CODE }</div></td>
					<td><div class="center">${list.DEALER_SHORTNAME }</div></td>
					<td><div class="center">${list.GROUP_CODE }</div></td>
					<td><div class="center">${list.GROUP_NAME }</div></td>
					<td><div class="center">${list.SALE_AMOUNT }</div></td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table class=table_query>
			<tr>
			   <td colspan="6" nowrap="nowrap">
		       	  新版本号:<input type="text" name="plan_ver" size="17" value="${planVer}" class="middle_txt" readonly="true" />      
		       	  版本描述:<input type="text" id="plan_desc_id" name="plan_desc" size="17" value="" class="middle_txt" /> 
		       	  <input type="button" class="u-button u-submit" name="vdcConfirm" value="确认" onclick="subChecked();" />
		       	  <input type="button" class="u-button u-reset"   name="vdcConfirm" value="返回" onclick="history.back();" />
		       	  <input type="hidden" name="year" value="${year }" />   
		       	  <input type="hidden" name="areaId" value="${areaId }" />
		       	  <input type="hidden" name="planType" value="${planType}" />
		       </td>
			</tr>
		</table>
	</form>
	</div>
</div>
</body>
</html>


<%-- <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
		$('fm').action = "<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSearch.do";
    	$("fm").submit();
	}
	function subChecked(){
	    var str="";
		MyConfirm("是否执行确认操作?",confirmSubmit);
	}
	function confirmSubmit(){
		$('fm').action=  "<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/ConfrimYearPlan.do";
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
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>年度目标>${year}年度目标确认	</div>
 <%
     List yearOptions=(List)request.getAttribute("options");
     String arrStr=(String)request.getAttribute("arrStr");
     if(null!=yearOptions&&yearOptions.size()>0){
 %>
<form name="fm" method="post" >
<table class="table_query">
  <tr>
    <td class="table_query_label" align="right" width="45%">选择年份：
      <select name='year'>
        <%
        String selected="";
        for(int i=0;i<yearOptions.size();i++){
        	Map map=(Map)yearOptions.get(i);
        	if(arrStr.equals(map.get("KEYSTR").toString())){
        		selected="selected=\"selected\"";
        	}else{
        		selected="";
        	}
        %>
        <option value="<%=map.get("KEYSTR").toString() %>" <%=selected %>><%=map.get("VALSTR").toString() %></option>
         <%
        }
        %>
      </select></td>
       <td width="10%">&nbsp;</td>
       <td class="table_query_label" >
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
       	  版本描述:<input type="text" name="plan_desc" size="17" value="${curDate}" class="middle_txt" /> 
       	  <input type="button" class="cssbutton"  name="vdcConfirm" value="确认" onclick="subChecked();" />
       	  <input type="hidden" name="year" value="${year }" />     
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
		<font color="red">没有满足条件的数据1</font>
    </td>
  </tr>
</table>
</div>
<%
}
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
 --%>