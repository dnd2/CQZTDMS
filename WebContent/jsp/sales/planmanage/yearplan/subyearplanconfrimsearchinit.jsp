<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contentPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contentPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=contentPath %>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/calendar.js"></script>
<title>年度目标确认</title>
<script language="JavaScript" src="<%=contentPath %>/js/ut.js"></script>

<script language="JavaScript">
<!--

	function searchSubmit(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=contentPath %>/sales/planmanage/YearTarget/SubYearTargetConfirm/yearTargetConfirmSearch.do";
    	$("fm").submit();
	}
//-->
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度目标>年度目标确认
	</div>
<form name="fm" method="post" >
<table class="table_query">
 <tr>
    <td align="right" width="40%">业务范围： </td>
    <td>
      <select name="buss_area">
	       <c:forEach items="${areaBusList}" var="areaBusList" >
	       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
		   </c:forEach>
      </select>
    </td>
  </tr>  
  <tr>
       <td align="right" width="40%"> 计划类型： </td>
       <td>
         <script type="text/javascript">
		   genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,"",false,"short_sel","","false",'');
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
</form>
</div>
</body>
</html>
