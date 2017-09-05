<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 
		long tnba = 0;
		long tda =0;
		long tsa = 0;
		long tst = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车型库存状态表</title>
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
     /*   white-space: nowrap; */
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>车型库存状态表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="6" class="scrollRowThead scrollCR"><strong>车型库存状态表</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<th colspan="6" class="scrollRowThead scrollCR" align="left"><div align="left">日期：${year}年${month}月${day}日</div></th>
	</tr>
	
	
	<tr class="scrollColThead">
		<th nowrap class="scrollRowThead scrollCR">车型系列</th>
		<th nowrap class="scrollRowThead scrollCR">车型编码</th>
		<th nowrap>启票未发</th>
		<th nowrap>发运在途</th>
		<th nowrap>在库</th>
		<th nowrap>合计</th>
	</tr>
	<%
		List<Map<String,Object>> list = (List<Map<String,Object>>) request.getAttribute("list_Strage2Statu");
		int len = list.size();
		
		
		long snba = 0;
		long sda =0;
		long ssa = 0;
		long sst = 0;
		
		for(int i=0;i<len;i++){
			long nba = Long.parseLong(list.get(i).get("NODE_BILL_AMOUNT").toString());
			long da = Long.parseLong(list.get(i).get("DEING_AMOUNT").toString());
			long sa = Long.parseLong(list.get(i).get("STOCK_AMOUNT").toString());
			long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString());
			if(i==0){
				snba = nba;
				sda = da;
				ssa = sa;
				sst = st;
				
				tnba = nba ;
				tda = da;
				tsa = sa;
				tst = st;
				%>
			<tr>
		<td nowrap><%=list.get(i).get("SERIES_NAME")==null ?"": list.get(i).get("SERIES_NAME").toString()%>&nbsp;</td>
			<td nowrap><%=list.get(i).get("MODEL_CODE") ==null ?"": list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0?"":list.get(i).get("NODE_BILL_AMOUNT")%>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0?"":list.get(i).get("DEING_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0?"":list.get(i).get("STOCK_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0?"":list.get(i).get("SUM_TOTAL") %>&nbsp;</td>
			<tr>
				<%
			} else{
				String befor_group_code = list.get(i-1).get("SERIES_NAME")==null ?"":list.get(i-1).get("SERIES_NAME").toString();
				String after_group_code = list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString();
				if(after_group_code.equals(befor_group_code)){
					snba += nba;
					sda += da;
					ssa += sa;
					sst += st;
					
					tnba += nba ;
					tda += da;
					tsa += sa;
					tst += st;
					%>
			<tr>
			<td nowrap><%=list.get(i).get("SERIES_NAME")==null ?"": list.get(i).get("SERIES_NAME").toString()%>&nbsp;</td>
			<td nowrap><%=list.get(i).get("MODEL_CODE") ==null ?"": list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0?"":list.get(i).get("NODE_BILL_AMOUNT")%>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0?"":list.get(i).get("DEING_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0?"":list.get(i).get("STOCK_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0?"":list.get(i).get("SUM_TOTAL") %>&nbsp;</td>
			<tr>
					<%
				}else{
					
					%>
					<tr>
					<th nowrap colspan="2" align="center"><strong>车型合计</strong>&nbsp;</th>
					<th nowrap><%=snba==0?"": snba%>&nbsp;</th>
					<th nowrap><%=sda==0?"": sda%>&nbsp;</th>
					<th nowrap><%=ssa==0?"": ssa%>&nbsp;</th>
					<th nowrap><%=sst==0?"": sst%>&nbsp;</th>
					<tr>
					<% 
					snba = nba;
					sda = da;
					ssa = sa;
					sst = st;
					
					tnba += nba ;
					tda += da;
					tsa += sa;
					tst += st;
					%>
					<tr>
		<td nowrap><%=list.get(i).get("SERIES_NAME")==null ?"": list.get(i).get("SERIES_NAME").toString()%>&nbsp;</td>
			<td nowrap><%=list.get(i).get("MODEL_CODE") ==null ?"": list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>			
		<td nowrap><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString())==0?"":list.get(i).get("NODE_BILL_AMOUNT")%>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString())==0?"":list.get(i).get("DEING_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString())==0?"":list.get(i).get("STOCK_AMOUNT") %>&nbsp;</td>
			<td nowrap><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0?"":list.get(i).get("SUM_TOTAL") %>&nbsp;</td>
			<tr>
					<%
					
				}
				if(i==len-1){

					%>
					<tr>
					<th nowrap colspan="2" align="center"><strong>车型合计</strong>&nbsp;</th>
					<th nowrap><%=snba==0?"": snba%>&nbsp;</th>
					<th nowrap><%=sda==0?"": sda%>&nbsp;</th>
					<th nowrap><%=ssa==0?"": ssa%>&nbsp;</th>
					<th nowrap><%=sst==0?"": sst%>&nbsp;</th>
					<tr>
					<% 
				}
			}
		}
	%>
			<tr>
			<th nowrap colspan="2" align="center" class="scrollRowThead"><strong>合计</strong>&nbsp;</th>
			<th nowrap class="scrollRowThead"><%=tnba==0?"": tnba%>&nbsp;</th>
			<th nowrap class="scrollRowThead"><%=tda==0?"": tda%>&nbsp;</th>
			<th nowrap class="scrollRowThead"><%=tsa==0?"": tsa%>&nbsp;</th>
			<th nowrap class="scrollRowThead"><%=tst==0?"": tst%>&nbsp;</th>
			<tr>
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
