<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物流未组板发运统计报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
		todayDate("endDate","yyyy-MM-dd HH:mm");
   		loadcalendar();  //初始化时间控件
	}
	
	function txtClr(v1,v2,v3){
		document.getElementById(v1).value = "";
		document.getElementById(v2).value = "";
		document.getElementById(v3).value = "";
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;物流未组板发运统计报表</div>
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
  <table class="table_query">
  		<tr>
  		<td align="right" nowrap="true">选择截止时间：</td>
		<td align="left" nowrap="true">
			<!-- <input name="startDate" type="text" class="short_time_txt" id="startDate" readonly="readonly" /> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'startDate', false);" /> 
             &nbsp;至&nbsp;  -->
             <input name="endDate" type="text" class="time_txt" id="endDate" readonly="readonly" /> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'endDate', true);" />  
		</td>
	  	</tr>
	  	<tr>
	            <td align="center" colspan="2">
	            	<input class="normal_btn" type="reset" id="queryBtn" name="button1" value="重置"/>&nbsp;&nbsp;&nbsp;&nbsp;
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="firstQuery();"/>
	            	<input name="normal_btn" type=button class="cssbutton" onclick="exportMsg();" value="下载"/>
	            </td>
          </tr>
         
  </table>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
function firstQuery(){
	var fm = document.getElementById("fm");
		fm.target="_blank";
		fm.action = "<%=contextPath%>/report/storge/NotBoardReport/notBoardReportInfo.do";
		fm.submit();
}
function exportMsg(){
	var fm = document.getElementById("fm");
		fm.target="_blank";
		fm.action = "<%=contextPath%>/report/storge/NotBoardReport/printBoardReportInfo.do";
		fm.submit();
}
</script>
<!--页面列表 end -->

</body>
</html>