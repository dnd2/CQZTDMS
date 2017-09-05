<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
String contextPath = request.getContextPath(); 
List<Map<String,Object>> list_all = (List<Map<String,Object>>) request.getAttribute("list_detail");
List<Map<String,Object>> list_name = (List<Map<String,Object>>) request.getAttribute("list_series_name");
int len_all = list_all.size();
int len_name = list_name.size();

int[] total_dlr = new int[len_name];
int[] total_region = new int[len_name];
int[] total_org = new int[len_name];
int[] total_all = new int[len_name];


int myTotal = 0 ;

int actColAmount=0;

%>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售汇总表</title>
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
	<strong>销售汇总表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable" >

	<tr class="scrollColThead">
	<td colspan="<%=len_name+5%>" class="scrollRowThead scrollCR juzuo"><strong>销售汇总表</strong></td>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="<%=len_name+5%>" class="scrollRowThead scrollCR" align="left"><div align="left">开始日期：${beginTime}--截止日期${endTime}</div></td>
	</tr>
	
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" width="7%">大区</th>
		<th class="scrollRowThead scrollCR" width="7%">省份</th>
		<th width="24%" class="scrollRowThead scrollCR">一级经销商</th>
		<th width="24%"class="scrollRowThead scrollCR">二级经销商</th>
		<c:if test="${list_series_name!=null}">
			<c:forEach items="${list_series_name}" var="list_sn">
				<th nowrap>${list_sn.SERIES_NAME }</th>
			</c:forEach>
		</c:if>
		<th  nowrap>合计</th>
	</tr>
	
	
<%
	//最外层是一级经销商合计，一次是省份，大区
	if(list_all!=null&&len_all!=0){
		for(int i=0;i<len_all;i++){
			int actCol =0;
			for(int j=0;j<len_name;j++){
				actCol += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString());
		   		
			}
			if(i==0){
				
				actColAmount = actCol;
				%>
				<tr>
					<td > <%=list_all.get(i).get("ORG_NAME")==null?"":list_all.get(i).get("ORG_NAME").toString() %>&nbsp;</td>
					<td > <%=list_all.get(i).get("REGION_NAME")==null?"":list_all.get(i).get("REGION_NAME").toString()%>&nbsp;</td>
					<td > <%=list_all.get(i).get("ROOT_DEALER_NAME")==null?"":list_all.get(i).get("ROOT_DEALER_NAME").toString()%>&nbsp;</td>
					<td > <%=list_all.get(i).get("DEALER_NAME")==null?"":list_all.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
					<%
					for(int j=0;j<len_name;j++){
						total_dlr[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
						total_region[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
						total_org[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
						total_all[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
						
						%>
						<td><%=Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString())==0?"":list_all.get(i).get("ACTAMOUNT"+j).toString() %>&nbsp;
						</td>
						<% 
					}
					%>
					<td id="actCol"><%=actColAmount %></td>
				</tr>
				<%
			}else{
				String before = list_all.get(i-1).get("ROOT_DEALER_NAME").toString();
				String after = list_all.get(i).get("ROOT_DEALER_NAME").toString();
				
				String before11 = list_all.get(i-1).get("REGION_NAME").toString();
				String after12 = list_all.get(i).get("REGION_NAME").toString();
				
				String before21 = list_all.get(i-1).get("ORG_NAME").toString();
				String after22 = list_all.get(i).get("ORG_NAME").toString();
				if(after.equals(before)&&after12.equals(before11)&&after22.equals(before21)){
					
					
					actColAmount += actCol;
					%>
					<tr>
						<td > <%=list_all.get(i).get("ORG_NAME")==null?"":list_all.get(i).get("ORG_NAME").toString() %>&nbsp;</td>
						<td > <%=list_all.get(i).get("REGION_NAME")==null?"":list_all.get(i).get("REGION_NAME").toString()%>&nbsp;</td>
						<td > <%=list_all.get(i).get("ROOT_DEALER_NAME")==null?"":list_all.get(i).get("ROOT_DEALER_NAME").toString()%>&nbsp;</td>
						<td > <%=list_all.get(i).get("DEALER_NAME")==null?"":list_all.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
						<%
						for(int j=0;j<len_name;j++){
							total_dlr[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_region[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_org[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_all[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;

							%>
							<td><%=Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString())==0?"":list_all.get(i).get("ACTAMOUNT"+j).toString() %>&nbsp;
						</td>
							<% 
						}
						%>
						<td id="actCol"><%=actCol %></td>
					</tr>
					<%
				}else{
					%>
					<tr>
					<th colspan="4"><center><STRONG>一级经销商合计</STRONG></center></th>
					<%
					myTotal = 0 ;
					for(int m=0;m<len_name;m++){
						%>
						<th><%=total_dlr[m]==0?"":total_dlr[m] %>&nbsp;</th>
						<%
						
						myTotal += total_dlr[m] ;
						total_dlr[m] = 0 ;
					}
					%>
					<th><%=myTotal %>&nbsp;</th>
					</tr>
					<%
					
					
					
					actColAmount = actCol;
					
					
					//开始1
					String before1 = list_all.get(i-1).get("REGION_NAME").toString();
					String after1 = list_all.get(i).get("REGION_NAME").toString();
					
					String before31 = list_all.get(i-1).get("ORG_NAME").toString();
					String after32 = list_all.get(i).get("ORG_NAME").toString();
					if(after1.equals(before1)&&after32.equals(before31)){
						
						actColAmount += actCol;
						
					}else{
						%>
						<tr>
						<th colspan="4" class="scrollRowThead"><center><STRONG>省份合计</STRONG></center></th>
						<%
						myTotal = 0 ;
						for(int m=0;m<len_name;m++){
							%>
							<th class="scrollRowThead"><%=total_region[m]==0?"":total_region[m] %>&nbsp;</th>
							<%
							myTotal += total_region[m] ;
							total_region[m] = 0 ;
						}
						%>
						<th class="scrollRowThead"><%=myTotal %>&nbsp;</th>
						</tr>
						<%
						
						
						
						actColAmount = actCol;
						
						//开始2
						String before2 = list_all.get(i-1).get("ORG_NAME").toString();
						String after2 = list_all.get(i).get("ORG_NAME").toString();
						if(after2.equals(before2)){
							
							actColAmount += actCol;
							
						}else{
							%>
							<tr>
							<th colspan="4" class="scrollRowThead"><center><STRONG>大区合计</STRONG></center></th>
							<%
							myTotal = 0 ;
							for(int m=0;m<len_name;m++){
								%>
								<th class="scrollRowThead"><%=total_org[m]==0?"":total_org[m] %>&nbsp;</th>
								<%
								myTotal += total_org[m] ;
								total_org[m] = 0 ;
							}
							%>
							<th class="scrollRowThead"><%=myTotal %>&nbsp;</th>
							</tr>
							<%
							
							
							
							actColAmount = actCol;
						}
						//结束2
						
					}
					//结束1
					
					
					%>
					<tr>
						<td > <%=list_all.get(i).get("ORG_NAME")==null?"":list_all.get(i).get("ORG_NAME").toString() %>&nbsp;</td>
						<td > <%=list_all.get(i).get("REGION_NAME")==null?"":list_all.get(i).get("REGION_NAME").toString()%>&nbsp;</td>
						<td > <%=list_all.get(i).get("ROOT_DEALER_NAME")==null?"":list_all.get(i).get("ROOT_DEALER_NAME").toString()%>&nbsp;</td>
						<td> <%=list_all.get(i).get("DEALER_NAME")==null?"":list_all.get(i).get("DEALER_NAME").toString() %>&nbsp;</td>
						<%
						for(int j=0;j<len_name;j++){
							total_dlr[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_region[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_org[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							total_all[j] += Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString()) ;
							
							%>
							<td><%=Integer.parseInt(list_all.get(i).get("ACTAMOUNT"+j).toString())==0?"":list_all.get(i).get("ACTAMOUNT"+j).toString() %>&nbsp;
							<input type="hidden" name="ACTAMOUNT<%=j %>" value="<%=list_all.get(i).get("ACTAMOUNT"+j) %>">
							</td>
							<% 
						}
						%>
						<td id="actCol"><%=actCol %></td>
					</tr>
					<%
					
				}
				if(i==len_all-1){
					%>
					<tr>
					<th colspan="4" ><center><STRONG>一级经销商合计</STRONG></center></th>
					<%
					myTotal = 0 ;
					for(int m=0;m<len_name;m++){
						%>
						<th><%=total_dlr[m]==0?"":total_dlr[m] %>&nbsp;</th>
						<%
						myTotal +=total_dlr[m];
						total_dlr[m] = 0;
					}
					%>
					<th><%=myTotal %>&nbsp;</th>
					</tr>
					<%
					
					%>
					<tr>
					<th colspan="4" class="scrollRowThead"><center><STRONG>省份合计</STRONG></center></th>
					<%
					myTotal = 0;
					for(int m=0;m<len_name;m++){
						%>
						<th class="scrollRowThead"><%=total_region[m]==0?"":total_region[m] %>&nbsp;</th>
						<%
						myTotal +=total_region[m];
						total_region[m] =0;
					}
					%>
					<th class="scrollRowThead"><%=myTotal %>&nbsp;</th>
					</tr>
					<%
					
					%>
					<tr>
					<th colspan="4" class="scrollRowThead"><center><STRONG>大区合计</STRONG></center></th>
					<%
					myTotal =0;
					for(int m=0;m<len_name;m++){
						%>
						<th class="scrollRowThead"><%=total_org[m]==0?"":total_org[m] %>&nbsp;</th>
						<%
						myTotal +=total_org[m];
						total_org[m] =0;
					}
					%>
					<th class="scrollRowThead"><%=myTotal %>&nbsp;</th>
					</tr>
					<%
				}
			}
			
		}
	}
	%>
	
					<tr>
				<th colspan="4" class="scrollRowThead"><center><STRONG>合计</STRONG></center></th>
				<%
				myTotal =0;
				if(total_all.length!=0){
				for(int i=0;i<len_name;i++){
					%>
					<th class="scrollRowThead"><span id="ACTAMOUNT_<%=i %>"><%=total_all[i] ==0?"":total_all[i] %></span>&nbsp;</th>
					<%
					myTotal += total_all[i];
				}
				}
				%>
				<th class="scrollRowThead"><span id="actAll"><%=myTotal%></span>&nbsp;</th>
			</tr>
	
	
	
	
	
</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
<script type="text/javascript">
	function do(){
			MyAlert();
		}
</script>
</body>
</html>
