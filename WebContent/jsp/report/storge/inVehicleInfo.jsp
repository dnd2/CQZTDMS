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
<title>入库报表</title>
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
height:100%;
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
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;&nbsp;&nbsp;&nbsp;
	<strong>入库报表
	<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</strong>
</div>

<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
		<th colspan="3" align="center"><font size="3" color="black">入库报表</font></th>
	</tr>
	<tr class="scrollColThead">
		<th>车辆型号</th>
		<th>内部型号</th>
		<th>台数合计</th>
	</tr>
	<%
	List<Map<String,Object>> valueMapList = (List<Map<String,Object>>)request.getAttribute("valueMapList");
		for(int i=0;i<valueMapList.size();i++){
			Map<String,Object> vm=valueMapList.get(i);
			%>
			<tr>	
			<%
					if(vm.get("ERP_PACKAGE").toString().equals("-")){
				%>
						<td bgcolor="#EEEEEE" colspan="2"><font size="3" color="black">&nbsp;<%=vm.get("ERP_MODEL") %></font></td>
						<td bgcolor="#EEEEEE">&nbsp;<%=vm.get("VEH_COUNT") %></td>	
			
				<%	
					}else if(i==valueMapList.size()-1){
				%>
						<td bgcolor="#EEEEEE" colspan="2"><font size="3" color="black">&nbsp;<%=vm.get("ERP_PACKAGE") %></font></td>
						<td bgcolor="#EEEEEE">&nbsp;<%=vm.get("VEH_COUNT") %></td>	
				<%
					}else{
						%>
						<td>&nbsp;<%=vm.get("ERP_MODEL") %></td>
						<td>&nbsp;<%=vm.get("ERP_PACKAGE")%></td>
						<td>&nbsp;<%=vm.get("VEH_COUNT") %></td>	
						<%
					}
				%>
					
				</tr>
				<%
		}
	%>	
	</table>

</body>
</html>
