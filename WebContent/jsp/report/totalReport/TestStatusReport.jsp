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
	long tda=0;
	long tsa = 0;
	long tst = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商库存状态</title>
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
	<strong>经销商库存状态&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="8" class="scrollRowThead scrollCR"><strong>经销商库存状态</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<th colspan="8" class="scrollRowThead scrollCR" align="left"><div align="left">日期：${year}年${month}月${day}日</div></th>
	</tr>
	
	<tr class="scrollColThead">
		<th width="5%" class="scrollRowThead scrollCR">大区</th>
		<th  width="5%" class="scrollRowThead scrollCR">省份</th>
		<th  width="20%" class="scrollRowThead scrollCR">经销商</th>
		<th   width="20%" class="scrollRowThead scrollCR">二级经销商</th>
		<th >启票未发</th>
		<th >发运在途</th>
		<th >在库</th>
		<th >合计</th>
	</tr>
	<%
		List<Map<String,Object>> list =(List<Map<String,Object>>) request.getAttribute("list_TestStatus");
		int len = list.size();
		long snba = 0;
		long sda=0;
		long ssa = 0;
		long sst = 0;
		
		
		long snba_org = 0;
		long sda_org  =0;
		long ssa_org = 0;
		long sst_org = 0;
		if(list!=null){
			for(int i=0;i<len;i++){
				long nba = Long.parseLong(list.get(i).get("NODE_BILL_AMOUNT").toString());
				long da = Long.parseLong(list.get(i).get("DEING_AMOUNT").toString());
				long sa = Long.parseLong(list.get(i).get("STOCK_AMOUNT").toString());
				long st = Long.parseLong(list.get(i).get("SUM_TOTAL").toString());
				if(i==0){
					snba_org =nba;
					sda_org = da ;
					ssa_org = sa;
					sst_org =st;
					
					
					snba = nba;
					sda = da;
					ssa = sa;
					sst = st;
					
					tnba = nba;
					tda = da;
					tsa = sa;
					tst = st;
					%>
					<tr>
					<td><%=list.get(i).get("ORG_NAME") == null ?"": list.get(i).get("ORG_NAME").toString()%>&nbsp;</td>
					<td><%=list.get(i).get("REGION_NAME") == null ?"": list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
					<td><%=list.get(i).get("ROOT_DEALER_NAME") == null ?"": list.get(i).get("ROOT_DEALER_NAME").toString()%> &nbsp;</td>
					<td><%=list.get(i).get("DEALER_NAME") == null ?"": list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString()) == 0 ?"": list.get(i).get("NODE_BILL_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString()) == 0 ?"": list.get(i).get("DEING_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString()) == 0 ?"": list.get(i).get("STOCK_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString()) == 0 ?"": list.get(i).get("SUM_TOTAL").toString()%> &nbsp;</td>
					</tr>
					<% 
				}else{
					String befor = list.get(i-1).get("ROOT_DEALER_NAME").toString();
					String after = list.get(i).get("ROOT_DEALER_NAME").toString();
					
					String befor_rdn = list.get(i-1).get("ORG_NAME").toString();
					String after_rdn = list.get(i).get("ORG_NAME").toString();
					if(after.equals(befor)){
						
						snba_org +=nba;
						sda_org += da ;
						ssa_org += sa;
						sst_org += st;
						
						//撤销到此为止
						snba += nba;
						sda += da;
						ssa += sa;
						sst += st;
						
						tnba += nba;
						tda += da;
						tsa += sa;
						tst += st;
						%>
						
						<tr>
						<td><%=list.get(i).get("ORG_NAME") == null ?"": list.get(i).get("ORG_NAME").toString()%>&nbsp;</td>
						<td><%=list.get(i).get("REGION_NAME") == null ?"": list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
						<td><%=list.get(i).get("ROOT_DEALER_NAME") == null ?"": list.get(i).get("ROOT_DEALER_NAME").toString()%> &nbsp;</td>
						<td><%=list.get(i).get("DEALER_NAME") == null ?"": list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
						<td  ><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString()) == 0 ?"": list.get(i).get("NODE_BILL_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString()) == 0 ?"": list.get(i).get("DEING_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString()) == 0 ?"": list.get(i).get("STOCK_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString()) == 0 ?"": list.get(i).get("SUM_TOTAL").toString()%> &nbsp;</td>
						</tr>
						<% 
					}else{
						%>
						<tr>
						<th colspan="4" align="center"><strong>经销商合计</strong>&nbsp;</th>
						<th><%=snba==0?"": snba%> &nbsp;</th>
						<th><%=sda==0?"": sda%> &nbsp;</th>
						<th><%=ssa==0?"": ssa%> &nbsp;</th>
						<th><%=sst==0?"": sst%> &nbsp;</th>
						</tr>
						<% 
						snba = nba;
						sda = da;
						ssa = sa;
						sst = st;
						
						tnba += nba;
						tda += da;
						tsa += sa;
						tst += st;
						
						//snba_org +=nba;
						//sda_org += da ;
						//ssa_org += sa;
						//sst_org +=st;
						//开始
						String before1 = list.get(i-1).get("ORG_NAME").toString();
						String after1 = list.get(i).get("ORG_NAME").toString();
						
						
						if(after1.equals(before1)){
							
							snba_org +=nba;
							sda_org += da ;
							ssa_org += sa;
							sst_org +=st;
						}else{
							%>
							<tr>
							<th colspan="4" align="center"  class="scrollRowThead" ><strong>大区合计</strong>&nbsp;</th>
							<th   class="scrollRowThead"><%=snba_org==0?"": snba_org%> &nbsp;</th>
							<th   class="scrollRowThead"><%=sda_org==0?"": sda_org%> &nbsp;</th>
							<th class="scrollRowThead"><%=ssa_org==0?"": ssa_org%> &nbsp;</th>
							<th  class="scrollRowThead"><%=sst_org==0?"": sst_org%> &nbsp;</th>
							</tr>
							<% 
							snba_org =0;
							sda_org =0;
							ssa_org =0;
							sst_org =0;
							
							snba_org +=nba;
							sda_org += da ;
							ssa_org += sa;
							sst_org +=st;
						}
						//结束
						
						%>
						<tr>
						<td><%=list.get(i).get("ORG_NAME") == null ?"": list.get(i).get("ORG_NAME").toString()%>&nbsp;</td>
						<td><%=list.get(i).get("REGION_NAME") == null ?"": list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
						<td><%=list.get(i).get("ROOT_DEALER_NAME") == null ?"": list.get(i).get("ROOT_DEALER_NAME").toString()%> &nbsp;</td>
						<td><%=list.get(i).get("DEALER_NAME") == null ?"": list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
						<td  ><%=Integer.parseInt(list.get(i).get("NODE_BILL_AMOUNT").toString()) == 0 ?"": list.get(i).get("NODE_BILL_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("DEING_AMOUNT").toString()) == 0 ?"": list.get(i).get("DEING_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("STOCK_AMOUNT").toString()) == 0 ?"": list.get(i).get("STOCK_AMOUNT").toString()%> &nbsp;</td>
					<td  ><%=Integer.parseInt(list.get(i).get("SUM_TOTAL").toString()) == 0 ?"": list.get(i).get("SUM_TOTAL").toString()%> &nbsp;</td>
						</tr>
						<%
					}
					if(i==len-1){
						%>
						<tr>
						<th colspan="4" align="center"><strong>经销商合计</strong>&nbsp;</th>
						<th><%=snba==0?"": snba%> &nbsp;</th>
						<th><%=sda==0?"": sda%> &nbsp;</th>
						<th><%=ssa==0?"": ssa%> &nbsp;</th>
						<th><%=sst==0?"": sst%> &nbsp;</th>
						</tr>
						<% 
						
						%>
						<tr>
						<th colspan="4" align="center" class="scrollRowThead" ><strong>大区合计</strong>&nbsp;</th>
					<th  class="scrollRowThead" ><%=snba_org==0?"": snba_org%> &nbsp;</th>
							<th  class="scrollRowThead" ><%=sda_org==0?"": sda_org%> &nbsp;</th>
							<th class="scrollRowThead"><%=ssa_org==0?"": ssa_org%> &nbsp;</th>
							<th  class="scrollRowThead"><%=sst_org==0?"": sst_org%> &nbsp;</th>
						</tr>
						<% 
						
					}
				}
			}
		}
	%>
	
	
						<tr>
						<th colspan="4" align="center"  class="scrollRowThead"><strong>合计</strong>&nbsp;</th>
						<th class="scrollRowThead" ><%=tnba==0?"": tnba%> &nbsp;</th>
						<th  class="scrollRowThead"><%=tda==0?"": tda%> &nbsp;</th>
						<th class="scrollRowThead" ><%=tsa==0?"": tsa%> &nbsp;</th>
						<th  class="scrollRowThead"><%=tst==0?"": tst%> &nbsp;</th>
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
