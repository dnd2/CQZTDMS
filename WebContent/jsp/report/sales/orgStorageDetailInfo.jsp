<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%
	String contextPath = request.getContextPath(); 
	
	

 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>各大区库存报表</title>



<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<style type="text/css"> 
body,table, td, a { 
font:9pt; 
} 

/*固定行头样式*/
.scrollRowThead 
{
     position: relative; 
     left: expression(this.parentElement.parentElement.parentElement.scrollLeft);
     z-index:0;
}

/*固定表头样式*/
.scrollColThead {
     top: expression(this.parentElement.parentElement.parentElement.scrollTop);
     z-index:2;
}

/*行列交叉的地方*/
.scrollCR {
     z-index:3;
} 
 
/*div外框*/
.scrollDiv {
height:480px;
clear: both; 
border: 1px solid #EEEEEE;
OVERFLOW: scroll;
width: 100%; 
}

/*行头居中*/
.scrollColThead td,.scrollColThead th
{
     text-align: center ;
}

/*行头列头背景*/
.scrollRowThead,.scrollColThead td,.scrollColThead th
{
background-color:EEEEEE;
}

/*表格的线*/
.scrolltable
{
border-bottom:1px solid #CCCCCC; 
border-right:1px solid #CCCCCC; 
}

/*单元格的线等*/
.scrolltable td,.scrollTable th
{
     border-left: 1px solid #CCCCCC; 
     border-top: 1px solid #CCCCCC; 
     padding: 5px; 
     white-space: nowrap;
     text-align: center;
}
.juzuo td
{
line-height: 1px solid #CCCCCC;
height: 1px solid #CCCCCC;
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>各大区库存统计查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
		<th colspan="${size }" align="center">各大区库存统计表</th>
	</tr>
	<tr>
		<c:forEach items="${s_m }" var="po">
			<td colspan="${po.value }">${po.key }</td>
		</c:forEach>
	</tr>
	<tr class="scrollColThead">
	<c:forEach items="${title_list}" var="title">
		<td>${title }</td>
	</c:forEach>
	</tr>
	<% List<String> title_list = (List<String>)request.getAttribute("title_list");
		List<String> column_list = (List<String>)request.getAttribute("column_list");
	   List<Map<String,Object>> result = (List<Map<String,Object>>)request.getAttribute("result"); 
	   
	   for(int i = 0 ; i <result.size() ; i++){
		   Map<String,Object> map = result.get(i);
		   %><tr>
		   		<% 
		   			String count = "";
		   			for(int j = 0 ; j <column_list.size();j++){
		   				String title = column_list.get(j);
		   				if(j==0){
		   					count = map.get("ORG_NAME")==null?"0":map.get("ORG_NAME").toString();
		   				}else{
		   					count = map.get(title)==null?"0":map.get(title).toString();
		   				}			
		   				%>
		   				<td>
		   					<%=count %>
		   				</td>
		   				<%
		   			}
		   		%>
		   </tr><%
	   }%>
</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
</body>
</html>
