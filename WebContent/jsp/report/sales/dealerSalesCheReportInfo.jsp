<%@page import="javax.print.DocFlavor.STRING"%>
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
<title>经销商开票实销统计</title>



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
	<strong>经销商开票实销查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="tableHead" style="top:expression(document.getElementById('scrollDiv').scrollTop)">
		<th colspan="3" class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)">
		<th colspan="${size+1 }">开 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;票</th>
		<th colspan="${size+1 }">实 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;销</th>
	</tr>
	<tr class="tableHead" style="top:expression(document.getElementById('scrollDiv').scrollTop)">
		<th class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)">大区</th>
		<th class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)">省份</th>
		<th class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)">经销商名称</th>
		<c:forEach items="${seriesList }" var="title">
			<td>${title.GROUP_NAME }</td>
		</c:forEach>
		<td>合计</td>
		<c:forEach items="${seriesList }" var="title">
			<td>${title.GROUP_NAME }</td>
		</c:forEach>
		<td>合计</td>
	</tr>
	<% 
			List<Map<String,Object>> result = (List<Map<String,Object>>)request.getAttribute("result");
			List<Map<String,Object>> seriesList = (List<Map<String,Object>>)request.getAttribute("seriesList");
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String,Object> map = result.get(i);
					String root_org_name = map.get("ROOT_ORG_NAME")==null?"":map.get("ROOT_ORG_NAME").toString();
					String org_name = map.get("ORG_NAME")==null?"":map.get("ORG_NAME").toString();
					String dealerCode = map.get("DEALER_CODE")==null?"":map.get("DEALER_CODE").toString();
					String dealerName = map.get("DEALER_NAME")==null?"合计":map.get("DEALER_NAME").toString();
					String sum_count_1 = map.get("SUM_COUNT_1")==null?"":map.get("SUM_COUNT_1").toString();
					String sum_count_2 = map.get("SUM_COUNT_2")==null?"":map.get("SUM_COUNT_2").toString();
					%>
						<tr>
						<td class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)"><%=root_org_name %>&nbsp;</td>
						<td class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)"><%=org_name %>&nbsp;</td>
						<td class="tableRHead" style="left:expression(document.getElementById('scrollDiv').scrollLeft)"><%=dealerName %></td>
						<%
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME")+"1";
							String count = map.get(column)==null?"":map.get(column).toString();
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
							String column = (String)seriesList.get(j).get("GROUP_NAME")+"2";
							String count = map.get(column)==null?"":map.get(column).toString();
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
