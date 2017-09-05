<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.jatools.db.af"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 

		long tl3m = 0 ;
		long tt6m = 0 ;
		long tt9m = 0 ;
		long tt12m = 0 ;
		long tt2y = 0 ;
		long tm2y = 0 ;
		long tst = 0 ;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商库龄查询</title>
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
	<strong>经销商库龄查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<th colspan="11" class="scrollRowThead scrollCR"><strong>经销商库龄查询</strong></th>
	</tr>
	<tr class="scrollColThead" align="left">
	<th colspan="11" class="scrollRowThead scrollCR" align="left"><div align="left">日期：${year}年${month}月${day}日</div></th>
	</tr>
	
	<tr class="scrollColThead">
		<th  width="5%" class="scrollRowThead scrollCR">大区</th>
		<th  width="5%"  class="scrollRowThead scrollCR">省份</th>
		<th  width="20%" class="scrollRowThead scrollCR">经销商</th>
		<th  width="20%"  class="scrollRowThead scrollCR">二级经销商</th>
		<th  >三个月以下</th>
		<th  >3-6个月</th>
		<th  >6-9个月</th>
		<th  >9-12个月</th>
		<th  >1-2年</th>
		<th  >2年以上</th>
		<th  >合计</th>
	</tr>
	<%
				List<Map<String,Object>> list_d_s = (List<Map<String,Object>> )request.getAttribute("list_DealerStrorge");
				int len = list_d_s.size();
				long sl3m = 0 ;
				long st6m = 0 ;
				long st9m = 0 ;
				long st12m = 0 ;
				long st2y = 0 ;
				long sm2y = 0 ;
				long sst = 0 ;
				
				
				long sl3m_ron = 0 ;
				long st6m_ron = 0 ;
				long st9m_ron = 0 ;
				long st12m_ron = 0 ;
				long st2y_ron = 0 ;
				long sm2y_ron = 0 ;
				long sst_ron = 0 ;
				if(list_d_s!=null){
					for(int i=0;i<len;i++){
						long l3m = Long.parseLong(list_d_s.get(i).get("LESS_3_MONTH").toString());
						long t6m = Long.parseLong(list_d_s.get(i).get("TO_6_MONTH").toString());
						long t9m = Long.parseLong(list_d_s.get(i).get("TO_9_MONTH").toString());
						long t12m = Long.parseLong(list_d_s.get(i).get("TO_12_MONTH").toString());
						long t2y = Long.parseLong(list_d_s.get(i).get("TO_2_YEAR").toString());
						long m2y = Long.parseLong(list_d_s.get(i).get("MORE_2_YEAR").toString());
						long st = Long.parseLong(list_d_s.get(i).get("SUM_TOTAL").toString());
							if(i==0){
								sl3m = l3m;
								st6m = t6m;
								st9m = t9m;
								st12m = t12m;
								st2y = t2y;
								sm2y = m2y;
								sst = st;
								
								tl3m = l3m;
								tt6m = t6m;
								tt9m = t9m;
								tt12m = t12m;
								tt2y = t2y;
								tm2y = m2y;
								tst = st;
								
								sl3m_ron = l3m;
								st6m_ron = t6m;
								st9m_ron = t9m;
								st12m_ron = t12m;
								st2y_ron = t2y;
								sm2y_ron = m2y;
								sst_ron = st;
								%>
								
								<tr>
								<td><%=list_d_s.get(i).get("ROOT_ORG_NAME") == null ?"":list_d_s.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("REGION_NAME")==null ? "":list_d_s.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("ROOT_DEALER_NAME")==null ? "":list_d_s.get(i).get("ROOT_DEALER_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("DEALER_NAME")==null ? "":list_d_s.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("LESS_3_MONTH").toString())==0 ? "":list_d_s.get(i).get("LESS_3_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_6_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_6_MONTH").toString() %> &nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_9_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_9_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_12_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_12_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("TO_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("MORE_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("MORE_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("SUM_TOTAL").toString())==0? "":list_d_s.get(i).get("SUM_TOTAL").toString() %>&nbsp;</td>
								</tr>
						
								<% 
							}else{
								String befor_regionName = list_d_s.get(i-1).get("REGION_NAME").toString();
								String after_regionName = list_d_s.get(i).get("REGION_NAME").toString();
								
								String befor_ron = list_d_s.get(i-1).get("ROOT_ORG_NAME").toString();
								String after_ron = list_d_s.get(i).get("ROOT_ORG_NAME").toString();
								
								if(after_regionName.equals(befor_regionName)&&after_ron.equals(befor_ron)){
									sl3m += l3m;
									st6m += t6m;
									st9m += t9m;
									st12m += t12m;
									st2y += t2y;
									sm2y += m2y;
									sst += st;
									
									tl3m += l3m;
									tt6m += t6m;
									tt9m += t9m;
									tt12m += t12m;
									tt2y += t2y;
									tm2y += m2y;
									tst += st;
									
									
									sl3m_ron += l3m;
									st6m_ron += t6m;
									st9m_ron += t9m;
									st12m_ron += t12m;
									st2y_ron += t2y;
									sm2y_ron += m2y;
									sst_ron += st;
									%>
								<tr>
								<td><%=list_d_s.get(i).get("ROOT_ORG_NAME") == null ?"":list_d_s.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("REGION_NAME")==null ? "":list_d_s.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("ROOT_DEALER_NAME")==null ? "":list_d_s.get(i).get("ROOT_DEALER_NAME").toString() %>&nbsp;</td>
								<td><%=list_d_s.get(i).get("DEALER_NAME")==null ? "":list_d_s.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("LESS_3_MONTH").toString())==0 ? "":list_d_s.get(i).get("LESS_3_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_6_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_6_MONTH").toString() %> &nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_9_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_9_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_12_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_12_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("TO_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("MORE_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("MORE_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("SUM_TOTAL").toString())==0? "":list_d_s.get(i).get("SUM_TOTAL").toString() %>&nbsp;</td>
								</tr>
									<% 
									
								}else{
									%>
									<tr>
									<th  align="center" colspan="4"><strong>省份合计</strong>&nbsp;</th>
									<th><%=sl3m==0?"":sl3m %>&nbsp;</th>
									<th><%=st6m==0?"":st6m %> &nbsp;</th>
									<th><%=st9m ==0?"":st9m%>&nbsp;</th>
									<th><%=st12m ==0?"":st12m%>&nbsp;</th>
									<th><%=st2y ==0?"":st2y%>&nbsp;</th>
									<th><%=sm2y ==0?"":sm2y%>&nbsp;</th>
									<th><%=sst ==0?"":sst%>&nbsp;</th>
									</tr>
									<% 
									sl3m = l3m;
									st6m = t6m;
									st9m = t9m;
									st12m = t12m;
									st2y = t2y;
									sm2y = m2y;
									sst = st;
									
									tl3m += l3m;
									tt6m += t6m;
									tt9m += t9m;
									tt12m += t12m;
									tt2y += t2y;
									tm2y += m2y;
									tst += st;
									
									
									//开始
									String before1 = list_d_s.get(i-1).get("ROOT_ORG_NAME").toString();
									String after1 = list_d_s.get(i).get("ROOT_ORG_NAME").toString();
									if(after1.equals(before1)){
										sl3m_ron += l3m;
										st6m_ron += t6m;
										st9m_ron += t9m;
										st12m_ron += t12m;
										st2y_ron += t2y;
										sm2y_ron += m2y;
										sst_ron += st;
									}else{
										%>
										<tr>
										<th  align="center" colspan="4" class="scrollRowThead"><strong>大区合计</strong>&nbsp;</th>
										<th  class="scrollRowThead"><%=sl3m_ron==0?"":sl3m_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=st6m_ron==0?"":st6m_ron %> &nbsp;</th>
										<th  class="scrollRowThead"><%=st9m_ron==0?"":st9m_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=st12m_ron ==0?"":st12m_ron%>&nbsp;</th>
										<th  class="scrollRowThead"><%=st2y_ron==0?"":st2y_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=sm2y_ron==0?"":sm2y_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=sst_ron ==0?"":sst_ron%>&nbsp;</th>
										</tr>
										<% 
										
										sl3m_ron = 0;
										st6m_ron = 0;
										st9m_ron = 0;
										st12m_ron = 0;
										st2y_ron = 0;
										sm2y_ron = 0;
										sst_ron = 0;
										
										sl3m_ron += l3m;
										st6m_ron += t6m;
										st9m_ron += t9m;
										st12m_ron += t12m;
										st2y_ron += t2y;
										sm2y_ron += m2y;
										sst_ron += st;
									}
									//结束
									
									%>
								<tr>
								<td ><%=list_d_s.get(i).get("ROOT_ORG_NAME") == null ?"":list_d_s.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
								<td ><%=list_d_s.get(i).get("REGION_NAME")==null ? "":list_d_s.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
								<td ><%=list_d_s.get(i).get("ROOT_DEALER_NAME")==null ? "":list_d_s.get(i).get("ROOT_DEALER_NAME").toString() %>&nbsp;</td>
								<td ><%=list_d_s.get(i).get("DEALER_NAME")==null ? "":list_d_s.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("LESS_3_MONTH").toString())==0 ? "":list_d_s.get(i).get("LESS_3_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_6_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_6_MONTH").toString() %> &nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_9_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_9_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_12_MONTH").toString())==0 ? "":list_d_s.get(i).get("TO_12_MONTH").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("TO_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("TO_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("MORE_2_YEAR").toString())==0 ? "":list_d_s.get(i).get("MORE_2_YEAR").toString() %>&nbsp;</td>
								<td  ><%=Integer.parseInt(list_d_s.get(i).get("SUM_TOTAL").toString())==0? "":list_d_s.get(i).get("SUM_TOTAL").toString() %>&nbsp;</td>
								</tr>
									<% 
								}
								if(i==len-1){
									%>
									<tr>
									<th  align="center" colspan="4"><strong>省份合计</strong>&nbsp;</th>
									<th><%=sl3m==0?"":sl3m %>&nbsp;</th>
									<th><%=st6m==0?"":st6m %> &nbsp;</th>
									<th><%=st9m ==0?"":st9m%>&nbsp;</th>
									<th><%=st12m ==0?"":st12m%>&nbsp;</th>
									<th><%=st2y ==0?"":st2y%>&nbsp;</th>
									<th><%=sm2y ==0?"":sm2y%>&nbsp;</th>
									<th><%=sst ==0?"":sst%>&nbsp;</th>
									</tr>
									
									
									<tr>
									<th  align="center" colspan="4" class="scrollRowThead"><strong>大区合计</strong>&nbsp;</th>
									<th  class="scrollRowThead"><%=sl3m_ron==0?"":sl3m_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=st6m_ron==0?"":st6m_ron %> &nbsp;</th>
										<th  class="scrollRowThead"><%=st9m_ron==0?"":st9m_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=st12m_ron ==0?"":st12m_ron%>&nbsp;</th>
										<th  class="scrollRowThead"><%=st2y_ron==0?"":st2y_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=sm2y_ron==0?"":sm2y_ron %>&nbsp;</th>
										<th  class="scrollRowThead"><%=sst_ron ==0?"":sst_ron%>&nbsp;</th>
									</tr>
									<% 
								}
							}
						}
					}
	%>
	
	
								<tr>
								<th  align="center" colspan="4" class="scrollRowThead"><strong>合计</strong>&nbsp;</th>
								<th  class="scrollRowThead"><%=tl3m==0?"":tl3m %>&nbsp;</th>
								<th  class="scrollRowThead"><%=tt6m==0?"":tt6m %> &nbsp;</th>
								<th  class="scrollRowThead"><%=tt9m==0?"":tt9m %>&nbsp;</th>
								<th  class="scrollRowThead"><%=tt12m==0?"":tt12m %>&nbsp;</th>
								<th  class="scrollRowThead"><%=tt2y==0?"":tt2y %>&nbsp;</th>
								<th  class="scrollRowThead"><%=tm2y==0?"":tm2y %>&nbsp;</th>
								<th  class="scrollRowThead"><%=tst==0?"":tst %>&nbsp;</th>
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
