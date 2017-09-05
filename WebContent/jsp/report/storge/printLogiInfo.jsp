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
	String fileName = new String("物流综合实时报表".getBytes("GBK"),"ISO8859-1");
	response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物流综合实时报表</title>
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
	<tr class="scrollColThead">
		<th colspan="21" align="center"><font size="3" color="black">物流综合实时报表</font></th>
	</tr>
	<tr style="background-color:EEEEEE;">
		<th colspan="21" style="text-align: right">统计时间：<font size="2" color="red">${endDate}</font></th>
	</tr>
	<tr class="scrollColThead">
		<th>产地</th>
		<th>车型</th>
		<th>入库/日</th>
		<th>分派/日</th>
		<th>组板/日</th>
		<th>出库/日</th>
		<th>运输/日</th>
		<th>月末遗留</th>
		<th>入库/月</th>
		<th>分派/月</th>
		<th>组板/月</th>
		<th>出库/月</th>
		<th>运输/月</th>
		<th>未组板</th>
		<th>年末遗留</th>
		<th>入库/年</th>
		<th>分派/年</th>
		<th>组板/年</th>
		<th>出库/年</th>
		<th>运输/年</th>
		<th>实时库存</th>
	</tr>
	<%
	List<Map<String,Object>> result = (List<Map<String,Object>>)request.getAttribute("result");
	List<Map<String,Object>> seriesList = (List<Map<String,Object>>)request.getAttribute("seriesList");
	//List<Map<String,Object>> historyList = (List<Map<String,Object>>)request.getAttribute("historyList");
	int jdzcount=(Integer)request.getAttribute("jdzcount");
	int jjcount=(Integer)request.getAttribute("jjcount");
	int hfcount=(Integer)request.getAttribute("hfcount");
	String year=(String)request.getAttribute("year");
	int r1=0,r2=0,r3=0,r4=0,r5=0,r6=0,r7=0,r8=0,r9=0,r10=0,r11=0,r12=0,r13=0,r14=0,r15=0,r16=0,r17=0,r18=0,r19=0;//小计
	int rsum1=0,rsum2=0,rsum3=0,rsum4=0,rsum5=0,rsum6=0,rsum7=0,rsum8=0,rsum9=0,rsum10=0,rsum11=0,rsum12=0,rsum13=0,rsum14=0,rsum15=0,rsum16=0,rsum17=0,rsum18=0,rsum19=0;//合计
		for(int i=0;i<seriesList.size();i++){
			Map<String,Object> se=seriesList.get(i);
			Map<String,Object> re=result.get(i);
			//Map<String,Object> his=historyList.get(i);
			r1+=Integer.parseInt(re.get("STORAGE_DAY_COUNT").toString());
			r2+=Integer.parseInt(re.get("ORDER_DAY_COUNT").toString());
			r3+=Integer.parseInt(re.get("BO_DAY_COUNT").toString());
			r4+=Integer.parseInt(re.get("OUT_DAY_COUNT").toString());
			r5+=Integer.parseInt(re.get("SEND_DAY_COUNT").toString());
			r6+=Integer.parseInt(re.get("BO_BMONTH_COUNT").toString());
			r7+=Integer.parseInt(re.get("STORAGE_MONTH_COUNT").toString());
			r8+=Integer.parseInt(re.get("ORDER_MONTH_COUNT").toString());
			r9+=Integer.parseInt(re.get("BO_MONTH_COUNT").toString());
			r10+=Integer.parseInt(re.get("OUT_MONTH_COUNT").toString());
			r11+=Integer.parseInt(re.get("SEND_MONTH_COUNT").toString());
			r12+=Integer.parseInt(re.get("NOT_BO_COUNT").toString());
			r13+=Integer.parseInt(re.get("BO_BYEAR_COUNT").toString());
			r14+=Integer.parseInt(re.get("STORAGE_YEAR_COUNT").toString());
			r15+=Integer.parseInt(re.get("ORDER_YEAR_COUNT").toString());
			r16+=Integer.parseInt(re.get("BO_YEAR_COUNT").toString());
			r17+=Integer.parseInt(re.get("OUT_YEAR_COUNT").toString());
			r18+=Integer.parseInt(re.get("SEND_YEAR_COUNT").toString());
			r19+=Integer.parseInt(re.get("STORAGE_THIS_COUNT").toString());
			if(year.equals("2013")){//历史数据
				r13+=Integer.parseInt(se.get("BEFORE_YEAR_COUNT").toString());	
				r15+=Integer.parseInt(se.get("ASS_COUNT").toString());	
				r16+=Integer.parseInt(se.get("BO_COUNT").toString());	
				r17+=Integer.parseInt(se.get("OUT_COUNT").toString());	
				r18+=Integer.parseInt(se.get("SEND_COUNT").toString());	
			}
			%>
			<tr>
				<td bgcolor="#EEEEEE" ><%=se.get("AREA_NAME") %></td>	
				<td bgcolor="#EEEEEE"><%=se.get("GROUP_NAME") %></td>
				<td><%=re.get("STORAGE_DAY_COUNT") %></td>
				<td><%=re.get("ORDER_DAY_COUNT") %></td>	
				<td><%=re.get("BO_DAY_COUNT") %></td>
				<td><%=re.get("OUT_DAY_COUNT") %></td>	
				<td><%=re.get("SEND_DAY_COUNT") %></td>
				<td><%=re.get("BO_BMONTH_COUNT") %></td>	
				<td><%=re.get("STORAGE_MONTH_COUNT") %></td>
				<td><%=re.get("ORDER_MONTH_COUNT") %></td>
				<td><%=re.get("BO_MONTH_COUNT") %></td>	
				<td><%=re.get("OUT_MONTH_COUNT") %></td>	
				<td><%=re.get("SEND_MONTH_COUNT") %></td>
				<td><%=re.get("NOT_BO_COUNT") %></td>	
				<td><%=year.equals("2013")?Integer.parseInt(re.get("BO_BYEAR_COUNT").toString())+Integer.parseInt(se.get("BEFORE_YEAR_COUNT").toString()):Integer.parseInt(re.get("BO_BYEAR_COUNT").toString()) %></td>
				<td><%=re.get("STORAGE_YEAR_COUNT") %></td>
				<td><%=year.equals("2013")?Integer.parseInt(re.get("ORDER_YEAR_COUNT").toString())+Integer.parseInt(se.get("ASS_COUNT").toString()):Integer.parseInt(re.get("ORDER_YEAR_COUNT").toString()) %></td>	
				<td><%=year.equals("2013")?Integer.parseInt(re.get("BO_YEAR_COUNT").toString())+Integer.parseInt(se.get("BO_COUNT").toString()):Integer.parseInt(re.get("BO_YEAR_COUNT").toString()) %></td>	
				<td><%=year.equals("2013")?Integer.parseInt(re.get("OUT_YEAR_COUNT").toString())+Integer.parseInt(se.get("OUT_COUNT").toString()):Integer.parseInt(re.get("OUT_YEAR_COUNT").toString()) %></td>	
				<td><%=year.equals("2013")?Integer.parseInt(re.get("SEND_YEAR_COUNT").toString())+Integer.parseInt(se.get("SEND_COUNT").toString()):Integer.parseInt(re.get("SEND_YEAR_COUNT").toString()) %></td>
				<td><%=re.get("STORAGE_THIS_COUNT") %></td>
			</tr>
			<% 
			if(i+1==jdzcount){
				
				%>
				<tr>
					<td bgcolor="#EEEEEE" colspan="2" ><b>小计</b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r1 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r2 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r3 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r4 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r5 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r6 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r7 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r8 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r9 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r10 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r11 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r12 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r13 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r14 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r15 %></b></td>
					<td bgcolor="#EEEEEE" ><b><%=r16 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r17 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r18 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r19 %></b></td>				
				</tr>
				<%
				rsum1+=r1;rsum2+=r2;rsum3+=r3;rsum4+=r4;rsum5+=r5;rsum6+=r6;rsum7+=r7;rsum8+=r8;rsum9+=r9;rsum10+=r10;rsum11+=r11;rsum12+=r12;rsum13+=r13;rsum14+=r14;rsum15+=r15;rsum16+=r16;rsum17+=r17;rsum18+=r18;rsum19+=r19;//合计
				r1=0;r2=0;r3=0;r4=0;r5=0;r6=0;r7=0;r8=0;r9=0;r10=0;r11=0;r12=0;r13=0;r14=0;r15=0;r16=0;r17=0;r18=0;r19=0;//初始化
			}
			if(i+1==jjcount+jdzcount){
				
				%>
				<tr>
					<td bgcolor="#EEEEEE" colspan="2" ><b>小计</b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r1 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r2 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r3 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r4 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r5 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r6 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r7 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r8 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r9 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r10 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r11 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r12 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r13 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r14 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r15 %></b></td>
					<td bgcolor="#EEEEEE" ><b><%=r16 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r17 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r18 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r19 %></b></td>	
				</tr>
				<%
				rsum1+=r1;rsum2+=r2;rsum3+=r3;rsum4+=r4;rsum5+=r5;rsum6+=r6;rsum7+=r7;rsum8+=r8;rsum9+=r9;rsum10+=r10;rsum11+=r11;rsum12+=r12;rsum13+=r13;rsum14+=r14;rsum15+=r15;rsum16+=r16;rsum17+=r17;rsum18+=r18;rsum19+=r19;//合计
				r1=0;r2=0;r3=0;r4=0;r5=0;r6=0;r7=0;r8=0;r9=0;r10=0;r11=0;r12=0;r13=0;r14=0;r15=0;r16=0;r17=0;r18=0;r19=0;//初始化
			}
			if(i+1==hfcount+jdzcount+jjcount){
				
				%>
				<tr>
					<td bgcolor="#EEEEEE" colspan="2" ><b>小计</b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r1 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r2 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r3 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r4 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r5 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r6 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r7 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r8 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r9 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r10 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r11 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r12 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r13 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r14 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r15 %></b></td>
					<td bgcolor="#EEEEEE" ><b><%=r16 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r17 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r18 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=r19 %></b></td>	
				</tr>
				<%
				rsum1+=r1;rsum2+=r2;rsum3+=r3;rsum4+=r4;rsum5+=r5;rsum6+=r6;rsum7+=r7;rsum8+=r8;rsum9+=r9;rsum10+=r10;rsum11+=r11;rsum12+=r12;rsum13+=r13;rsum14+=r14;rsum15+=r15;rsum16+=r16;rsum17+=r17;rsum18+=r18;rsum19+=r19;//合计
			}
			
		}
	%>	
	<tr>
	<td colspan="2" bgcolor="#EEEEEE"><b>总计</b></td>
					<td bgcolor="#EEEEEE" ><b><%=rsum1 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum2 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum3 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum4 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum5 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum6 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum7 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum8 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum9 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum10 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum11 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum12 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum13 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum14 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum15 %></b></td>
					<td bgcolor="#EEEEEE" ><b><%=rsum16 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum17 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum18 %></b></td>	
					<td bgcolor="#EEEEEE" ><b><%=rsum19 %></b></td>
	</tr>
	</table>

</body>
</html>
