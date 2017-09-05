<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%
	String contextPath = request.getContextPath(); 
response.setContentType("application/vnd.ms-excel"); 
String fileName = new String("物流未组板发运统计报表".getBytes("GBK"),"ISO8859-1");
response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物流未组板发运统计表</title>
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



<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="tableHead" >
		<th colspan="${jdzcount+jjcount+hfcount+7 }" align="center"><font size="3" color="black">物流未组板发运统计报表</font></th>
	</tr>
	<tr class="tableHead" >
		<th colspan="${jdzcount+jjcount+hfcount+7 }" style="text-align: right">统计时间：<font size="2" color="red">${endDate}</font></th>
	</tr>
	<tr class="tableHead" >
		<th>省份</th>
		<th>经销商</th>
		<th>分派时间</th>
		<th colspan="${jdzcount+1 }"> 景德镇</th>
		<th colspan="${jjcount+1 }">九江</th>
		<th colspan="${hfcount+1 }">合肥</th>
		<th>合计</th>
	</tr>
	<tr class="tableHead" >
		<th>&nbsp;</th>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
		<%
		List<Map<String,Object>> result = (List<Map<String,Object>>)request.getAttribute("result");
		List<Map<String,Object>> seriesList = (List<Map<String,Object>>)request.getAttribute("seriesList");
		for(int bt = 0 ; bt < seriesList.size() ; bt ++){
		%>
			<td> <%=seriesList.get(bt).get("GROUP_NAME").toString() %></td>
			<%
			if(!seriesList.get(bt).get("AREA_ID").toString().equals(bt+1==seriesList.size()?"-1":seriesList.get(bt+1).get("AREA_ID").toString())){
				%>
				<td><h4>小计</h4></td>
				<%
			}
			%>
		<%
		}
		%>
		<th>&nbsp;</th>
	</tr>
	<% 	
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String,Object> map = result.get(i);
					if(map.get("REGION_NAME").toString().equals("-1")){
						%>
						<tr class="tableHead" >
						<th colspan="3">合计</th>
						<%
						int zxj=0;
						int jdz_xj_sum=0,jj_xj_sum=0,hf_xj_sum=0;
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME");
							String count = map.get(column)==null?"0":map.get(column).toString();
							zxj+=Integer.parseInt(count);
							if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
								jdz_xj_sum+=Integer.parseInt(count);
							}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
								jj_xj_sum+=Integer.parseInt(count);
							}else{
								hf_xj_sum+=Integer.parseInt(count);
							}
							%>
								<th><%=count %></th>
							<%
							if(!seriesList.get(j).get("AREA_ID").toString().equals(j+1==seriesList.size()?"-1":seriesList.get(j+1).get("AREA_ID").toString())){
								if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
									%>
									<th><%=jdz_xj_sum %></th>
									<%
								}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
									%>
									<th><%=jj_xj_sum %></th>
									<%
								}else{
									%>
									<th><%=hf_xj_sum %></th>
									<%
								}
							}
						}
						%>
						<th><%=zxj %></th>
						</tr>
					<%	
					}
					else if(map.get("REGION_NAME").toString().equals("经销商小计") || map.get("REGION_NAME").toString().equals("省份小计")){
						%>
						<tr>
						<th style="background-color: #EEEEEE"><%=map.get("REGION_NAME").toString() %></th>
						<th colspan="2" style="background-color: #EEEEEE"><%=map.get("DEALER_NAME").toString() %></th>
						<%
						int zxj=0;
						int jdz_xj_sum=0,jj_xj_sum=0,hf_xj_sum=0;
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME");
							String count = map.get(column)==null?"0":map.get(column).toString();
							zxj+=Integer.parseInt(count);
							if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
								jdz_xj_sum+=Integer.parseInt(count);
							}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
								jj_xj_sum+=Integer.parseInt(count);
							}else{
								hf_xj_sum+=Integer.parseInt(count);
							}
							%>
								<th style="background-color: #EEEEEE"><%=count %></th>
							<%
							if(!seriesList.get(j).get("AREA_ID").toString().equals(j+1==seriesList.size()?"-1":seriesList.get(j+1).get("AREA_ID").toString())){
								if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
									%>
									<th style="background-color: #EEEEEE"><%=jdz_xj_sum %></th>
									<%
								}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
									%>
									<th style="background-color: #EEEEEE"><%=jj_xj_sum %></th>
									<%
								}else{
									%>
									<th style="background-color: #EEEEEE"><%=hf_xj_sum %></th>
									<%
								}
							}
						}
						%>
						<th style="background-color: #EEEEEE"><%=zxj %></th>
						</tr>
					<%	
					}else{
					%>
						<tr>
						<td style="background-color: #EEEEEE"><%=map.get("REGION_NAME").toString() %></td>
						<td style="background-color: #EEEEEE"><%=map.get("DEALER_NAME").toString() %></td>
						<td style="background-color: #EEEEEE"><%=map.get("ASS_DATE").toString() %></td>
						<%
						int xj=0;
						int jdz_xj_only=0,jj_xj_only=0,hf_xj_only=0;
						for(int j = 0 ; j < seriesList.size() ; j ++){
							String column = (String)seriesList.get(j).get("GROUP_NAME");
							String count = map.get(column)==null?"0":map.get(column).toString();
							xj+=Integer.parseInt(count);
							if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
								jdz_xj_only+=Integer.parseInt(count);
							}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
								jj_xj_only+=Integer.parseInt(count);
							}else{
								hf_xj_only+=Integer.parseInt(count);
							}
							%>
								<td><%=count %></td>
							<%
							if(!seriesList.get(j).get("AREA_ID").toString().equals(j+1==seriesList.size()?"-1":seriesList.get(j+1).get("AREA_ID").toString())){
								if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdJZD)){
									%>
									<th style="background-color: #EEEEEE"><%=jdz_xj_only %></th>
									<%
								}else if(seriesList.get(j).get("AREA_ID").toString().equals(Constant.areaIdHF)){
									%>
									<th style="background-color: #EEEEEE"><%=jj_xj_only %></th>
									<%
								}else{
									%>
									<th style="background-color: #EEEEEE"><%=hf_xj_only %></th>
									<%
								}
							}
						}
						%>
						<th style="background-color: #EEEEEE"><%=xj %></th>
						</tr>
					<%
					}
				}
			}
	%>
	</table>
</body>
</html>
