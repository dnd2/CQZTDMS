<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="jxl.write.WritableCellFormat" %>
<%@ page import="jxl.format.Alignment" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
String contextPath = request.getContextPath(); 
List<Map<String,Object>> list = (List<Map<String,Object>>) request.getAttribute("list_DealerCarStrorge");
int len = list.size();




int myTotal = 0 ;

int actColAmount=0;

%>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品库存状态</title>
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
	background-color:#EEEEEE;
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
	<strong>产品库存状态&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
	<div id="scrollDiv" class="scrollDiv" > 
	<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable" >

	<tr class="scrollColThead">
	<td colspan="13" class="scrollRowThead scrollCR juzuo"><strong>产品库存状态</strong></td>
	</tr>
	
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" width="7%">车型车系</th>
		<th class="scrollRowThead scrollCR" width="7%">车型编码</th>
		<th width="24%" class="scrollRowThead scrollCR">三个月以下</th>
		<th width="24%"class="scrollRowThead scrollCR">3-6个月</th>
			<th class="scrollRowThead scrollCR" width="7%">6-9个月</th>
		<th class="scrollRowThead scrollCR" width="7%">9-12个月</th>
		<th width="24%" class="scrollRowThead scrollCR">1-2年</th>
		<th width="24%"class="scrollRowThead scrollCR">2年以上</th>
		<th  nowrap>合计</th>		
	</tr>
	<%
	
	WritableCellFormat wcf = new WritableCellFormat();
	wcf.setAlignment(Alignment.CENTRE);
	
	int y = 0;
	
	long tl3m = 0 ;
	long tt6m = 0 ;
	long tt9m = 0 ;
	long tt12m = 0 ;
	long tt2y = 0 ;
	long tm2y = 0 ;
	long tst = 0 ;
	if(list!=null){
		long sl3m = 0 ;
		long st6m = 0 ;
		long st9m = 0 ;
		long st12m = 0 ;
		long st2y = 0 ;
		long sm2y = 0 ;
		long sst = 0 ;
		for (int i = 0; i < len; i++) {
			
			long l3m = Long.parseLong(list.get(i).get("LESS_3_MONTH").toString()) ;
			long t6m = Long.parseLong(list.get(i).get("TO_6_MONTH").toString()) ;
			long t9m = Long.parseLong(list.get(i).get("TO_9_MONTH").toString()) ;
			long t12m = Long.parseLong(list.get(i).get("TO_12_MONTH").toString()) ;
			long t2y = Long.parseLong(list.get(i).get("TO_2_YEAR").toString()) ;
			long m2y = Long.parseLong(list.get(i).get("MORE_2_YEAR").toString()) ;
			long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString()) ;
			
			if(i==0){
				tl3m = l3m ;
				tt6m = t6m ;
				tt9m = t9m ;
				tt12m = t12m ;
				tt2y = t2y ;
				tm2y = m2y ;
				tst = st ;
				
				sl3m = l3m ;
				st6m = t6m ;
				st9m = t9m ;
				st12m = t12m ;
				st2y = t2y ;
				sm2y = m2y ;
				sst = st ;
				
				
				++y;%>
				<tr>
			<td>  <%=list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString()%>&nbsp;</td>
			<td>   <%=list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>
			<td >   <%=Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()%>&nbsp;</td>
			<td > 	 <%=Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get("TO_9_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get("TO_12_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_2_YEAR").toString()) ==0 ?"":list.get(i).get("TO_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString()) ==0 ?"":list.get(i).get("MORE_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()%>&nbsp;</td>
		</tr>
		<% 
			}else{
				
				String before =list.get(i-1).get("MODEL_CODE")==null ?"":list.get(i-1).get("MODEL_CODE").toString();
				String after = list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString();
				if(after.equals(before)){
					tl3m += l3m ;
					tt6m += t6m ;
					tt9m += t9m ;
					tt12m += t12m ;
					tt2y += t2y ;
					tm2y += m2y ;
					tst += st ;
					
					sl3m += l3m ;
					st6m += t6m ;
					st9m += t9m ;
					st12m += t12m ;
					st2y += t2y ;
					sm2y += m2y ;
					sst += st ;
					
					++y;%>
					<tr>
						<td > <%=list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString() %>&nbsp;</td>
					<td > 	<%= list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>
						<td >   <%=Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()%>&nbsp;</td>
		<td > 	 <%=Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get("TO_9_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get("TO_12_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_2_YEAR").toString()) ==0 ?"":list.get(i).get("TO_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString()) ==0 ?"":list.get(i).get("MORE_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()%>&nbsp;</td>
		</tr>
					<% 
				}else{
					++y;
			%>	
				<tr>
				<th  colspan="2">车型合计</th>
				<th> <%=sl3m==0?"":sl3m%>&nbsp;</th>
				<th> <%=st6m==0?"":st6m%>&nbsp;</th>
				<th> <%=st9m==0?"":st9m%>&nbsp;</th>
				<th> <%=st12m==0?"":st12m%>&nbsp;</th>
				<th> <%=st2y==0?"":st2y%>&nbsp;</th>
				<th> <%=sm2y==0?"":sm2y%>&nbsp;</th>
				<th> <%=sst==0?"":sst%>&nbsp;</th>
				</tr>
					<%
					tl3m += l3m ;
					tt6m += t6m ;
					tt9m += t9m ;
					tt12m += t12m ;
					tt2y += t2y ;
					tm2y += m2y ;
					tst += st ;
					
					sl3m = l3m ;
					st6m = t6m ;
					st9m = t9m ;
					st12m = t12m ;
					st2y = t2y ;
					sm2y = m2y ;
					sst = st ;
					
					++y;%>
					<tr>
					<td > <%=list.get(i).get("SERIES_NAME")==null ?"":list.get(i).get("SERIES_NAME").toString() %>&nbsp;</td>
				<td > 	<%= list.get(i).get("MODEL_CODE")==null ?"":list.get(i).get("MODEL_CODE").toString()%>&nbsp;</td>
					<td >   <%=Integer.parseInt(list.get(i).get("LESS_3_MONTH").toString())==0 ?"":list.get(i).get("LESS_3_MONTH").toString()%>&nbsp;</td>
			<td > 	 <%=Integer.parseInt(list.get(i).get("TO_6_MONTH").toString())==0 ?"":list.get(i).get("TO_6_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_9_MONTH").toString())==0 ?"":list.get(i).get("TO_9_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_12_MONTH").toString())==0 ?"":list.get(i).get("TO_12_MONTH").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("TO_2_YEAR").toString()) ==0 ?"":list.get(i).get("TO_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("MORE_2_YEAR").toString()) ==0 ?"":list.get(i).get("MORE_2_YEAR").toString()%>&nbsp;</td>
			<td >  <%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString())==0 ?"":list.get(i).get("SUM_TOTAL").toString()%>&nbsp;</td>
			</tr>
				<% 
				}
				if(i==len-1){
					++y;
					%>	
					<tr>
					<th colspan="2">车型合计</th>
					<th> <%=	sl3m==0?"":sl3m%>	&nbsp;</th>
					<th> <%=st6m==0?"":st6m%>&nbsp;	</th>
					<th> <%=st9m==0?"":st9m%>&nbsp;	</th>
					<th> <%=st12m==0?"":st12m%>	&nbsp;</th>
					<th> <%=st2y==0?"":st2y%>&nbsp;	</th>
					<th> <%=sm2y==0?"":sm2y%>&nbsp;	</th>
					<th> <%=	sst==0?"":sst%>	&nbsp;</th>
					</tr>
						<%
				}
			}
		}
	}
	++y;
	%>	
		
	<tr>
		<th colspan="2" class="scrollRowThead">合计</th>
		<th class="scrollRowThead"> <%=tl3m==0?"":tl3m%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tl3m==0?"":tl3m%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tt6m==0?"":tt6m%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tt12m==0?"":tt12m%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tt2y==0?"":tt2y%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tm2y==0?"":tm2y%>&nbsp;</th>
		<th class="scrollRowThead"> <%=tst==0?"":tst%>&nbsp;</th>
	</tr>
	
	
	</table>
	</div>
</body>
</html>