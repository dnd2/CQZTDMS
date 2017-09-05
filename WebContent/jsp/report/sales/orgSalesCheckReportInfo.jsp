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
<title>大区开票实销统计</title>



<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<style type="text/css"> 
body,table, td, a { 
font:9pt; 
} 

/* 艾春 固定表头和列头 开始*/

/*艾春添加*/
tr.tableHead {
    position:relative;
    text-align: center;
    }

th.tableRHead, td.tableRHead {
    position:relative;
    text-align: center;
    background-color:EEEEEE;
    }
/*艾春添加*/

/*div外框(艾春修改)*/
.scrollDiv {
	height:480px;
	border: 1px solid #EEEEEE;
	overflow: scroll;
	cursor: default;
	width: 100%; 
}

/*行头列头背景(艾春修改)*/
.scrollRowThead,.scrollColThead td,.scrollColThead th,.tableHead td, .tableHead th
{
background-color:EEEEEE;
}

/* 艾春 固定表头和列头 结束*/



/*固定行头样式*/
.scrollRowThead 
{
     position: relative; 
     left: expression(this.parentElement.parentElement.scrollLeft);
     z-index:0;
}

/*固定表头样式*/
.scrollColThead {
     top: expression(this.parentElement.parentElement.scrollTop);
     z-index:2;
}


/*行列交叉的地方*/
.scrollCR {
     z-index:3;
} 

/*行头居中*/
.scrollColThead td,.scrollColThead th
{
     text-align: center ;
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
	<strong>大区开票实销报表查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="tableHead" style="top:expression(document.getElementById('scrollDiv').scrollTop)">
		<th colspan="${2*size+3 }" align="center"><font size="3" color="black">各大区开票实销统计表</font></th>
	</tr>
	<tr class="tableHead" style="top:expression(document.getElementById('scrollDiv').scrollTop)">
		<th>&nbsp;</th>
		<th colspan="${size+1 }">开 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;票</th>
		<th colspan="${size+1 }">实 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;销</th>
	</tr>
	<tr class="tableHead" style="top:expression(document.getElementById('scrollDiv').scrollTop)">
		<th>大&nbsp;区</th>
		<c:forEach items="${seriesList }" var="title">
			<td>${title.GROUP_NAME }</td>
		</c:forEach>
		<Th>合计</Th>
		<c:forEach items="${seriesList }" var="title">
			<td>${title.GROUP_NAME }</td>
		</c:forEach>
		<th>合计</th>
	</tr>
	<% 
			List<Map<String,Object>> result = (List<Map<String,Object>>)request.getAttribute("result");
			List<Map<String,Object>> seriesList = (List<Map<String,Object>>)request.getAttribute("seriesList");
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String,Object> map = result.get(i);
					String root_org_name = map.get("ROOT_ORG_NAME_2").toString();
					String sum_count_1 = map.get("SUM_COUNT_1")==null?"":map.get("SUM_COUNT_1").toString();
					String sum_count_2 = map.get("SUM_COUNT_2")==null?"":map.get("SUM_COUNT_2").toString();
					%>
						<tr>
						<td><%=root_org_name %>&nbsp;</td>
						<%
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME");
							String count = map.get(column+"1")==null?"":map.get(column+"1").toString();
							if(!"".equals(count)){
							%>
								<td><%=count %></td>
							<%	
							}else{
							%>
								<td>&nbsp;</td>
							<%	
							}					
						}
						if(!"".equals(sum_count_1)){
						%>
							<td><%=sum_count_1 %></td>
						<%	
						}else{
						%>
							<td>&nbsp;</td>
						<%	
						}
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME");
							String count = map.get(column+"2")==null?"":map.get(column+"2").toString();
							if(!"".equals(count)){
							%>
								<td><%=count %></td>
							<%	
							}else{
							%>
								<td>&nbsp;</td>
							<%	
							}		
						}
						if(!"".equals(sum_count_2)){
						%>
							<td><%=sum_count_2 %></td>
							</tr>
						<%
						}else{
						%>
							<td>&nbsp;</td>
							</tr>
						<%	
						}					
				}
			}
	%>
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
