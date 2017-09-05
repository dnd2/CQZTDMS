<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 
	int tba = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团客户实销表</title>
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
     position: relative; 
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
     text-align: center;
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>集团客户实销表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
		<td colspan="7" align="center">集团客户实销表</td>
	</tr>
	<tr class="scrollColThead">
		<td colspan="7" align="left"><div align="left">起止时间：${startTime}--${endTime}</div></td>
	</tr>
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" nowrap>区域事业部</th>
		<th class="scrollRowThead scrollCR" nowrap>省份</th>
		<th class="scrollRowThead scrollCR" nowrap>经销商名称</th>
		<th class="scrollRowThead scrollCR" nowrap>集团客户名称</th>
		<th class="scrollRowThead scrollCR" nowrap>物料名称</th>
		<th class="scrollRowThead scrollCR" nowrap>生产基地</th>
		<th class="scrollRowThead scrollCR" nowrap>数量</th>
	</tr>
	
		<%
	List<Map<String,Object>> list =(List<Map<String,Object>>)request.getAttribute("list_report");
	int len = list.size();
	if(list!=null&&len!=0){
		int sba = 0;
		int sba_rn = 0;
		for(int i=0;i<len;i++){
			int ba = Integer.parseInt(list.get(i).get("ACTAMOUNT").toString());
			if(i==0){
				sba = ba;
				sba_rn = ba;
				tba = ba;
				%>
				<tr>
				<td ><%=list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
				<td ><%=list.get(i).get("REGION_NAME")==null?"":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
				<td ><%=list.get(i).get("DEALER_NAME")==null?"":list.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
				<td ><%=list.get(i).get("FLEET_NAME")==null?"":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
				<td ><%=list.get(i).get("MATERIAL_NAME")==null?"":list.get(i).get("MATERIAL_NAME").toString() %>&nbsp;</td>
				<td ><%=list.get(i).get("CODE_DESC")==null?"":list.get(i).get("CODE_DESC").toString() %>&nbsp;</td>
				<td><%=list.get(i).get("ACTAMOUNT")==null?"":list.get(i).get("ACTAMOUNT").toString() %>&nbsp;</td>
				</tr>
				<%
			}else{
				String before = list.get(i-1).get("REGION_NAME").toString();
				String after = list.get(i).get("REGION_NAME").toString();
				
				String before_rn = list.get(i-1).get("ROOT_ORG_NAME").toString();
				String after_rn = list.get(i).get("ROOT_ORG_NAME").toString();
				if(after.equals(before)&&after_rn.equals(before_rn)){
					sba +=ba;
					sba_rn += ba;
					tba +=ba;
					%>
					<tr>
					<td ><%=list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
					<td ><%=list.get(i).get("REGION_NAME")==null?"":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
					<td ><%=list.get(i).get("DEALER_NAME")==null?"":list.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
					<td ><%=list.get(i).get("FLEET_NAME")==null?"":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
					<td ><%=list.get(i).get("MATERIAL_NAME")==null?"":list.get(i).get("MATERIAL_NAME").toString() %>&nbsp;</td>
					<td ><%=list.get(i).get("CODE_DESC")==null?"":list.get(i).get("CODE_DESC").toString() %>&nbsp;</td>
					<td><%=list.get(i).get("ACTAMOUNT")==null?"":list.get(i).get("ACTAMOUNT").toString() %>&nbsp;</td>
					</tr>
				<%
				}else{
					%>
					<tr>
					<th  colspan="6"><center><strong>省份合计</strong></center></th>
					<th ><%=sba %></th>
					</tr>
					<%
					sba = ba ;
					tba +=ba;
					
					//开始
					String before_on = list.get(i-1).get("ROOT_ORG_NAME").toString();
					String after_on = list.get(i).get("ROOT_ORG_NAME").toString();
					
					if(after_on.equals(before_on)){
						sba_rn +=ba;
					}else{
						%>
						<tr>
						<th class="scrollRowThead" colspan="6"><center><strong>大区合计</strong></center></th>
						<th class="scrollRowThead"><%=sba_rn %></th>
						</tr>
						<%
						sba_rn =0;
						sba_rn +=ba;
					}
					//结束
					%>
					<tr>
						<td ><%=list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
						<td ><%=list.get(i).get("REGION_NAME")==null?"":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
						<td ><%=list.get(i).get("DEALER_NAME")==null?"":list.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
						<td ><%=list.get(i).get("FLEET_NAME")==null?"":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
						<td ><%=list.get(i).get("MATERIAL_NAME")==null?"":list.get(i).get("MATERIAL_NAME").toString() %>&nbsp;</td>
						<td ><%=list.get(i).get("CODE_DESC")==null?"":list.get(i).get("CODE_DESC").toString() %>&nbsp;</td>
						<td><%=list.get(i).get("ACTAMOUNT")==null?"":list.get(i).get("ACTAMOUNT").toString() %>&nbsp;</td>
					</tr>
				<%
					
				}
				if(i==len-1){
					%>
					<tr>
					<th  colspan="6"><center><strong>省份合计</strong></center></th>
					<th ><%=sba %></th>
					</tr>
					
					
					<tr>
					<th class="scrollRowThead" colspan="6"><center><strong>大区合计</strong></center></th>
					<th class="scrollRowThead"><%=sba_rn %></th>
					</tr>
					<%
				}
			}
		}
	}
	%>
					<tr>
					<th class="scrollRowThead" colspan="6"><center><strong>合计</strong></center></th>
					<th class="scrollRowThead"><%=tba %></th>
					</tr>
					
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
