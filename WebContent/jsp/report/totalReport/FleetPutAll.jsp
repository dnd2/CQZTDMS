<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 

		long tsc = 0;
		long tic = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>大客户信息报备汇总表</title>
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
     /* white-space: nowrap; */
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>大客户信息报备汇总表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
	<td colspan="14" class="scrollRowThead scrollCR"><strong>大客户信息报备汇总表</strong></td>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="14" class="scrollRowThead scrollCR" align="left"><div align="left">备案起止时间：${auditBeginTime}--${auditEndTime}  审批起止时间：${submitBeginTime}--${submitEndTime}</div></td>
	</tr>
	
	<tr class="scrollColThead">
		<th nowrap class="scrollRowThead scrollCR">大区</th>
		<th nowrap class="scrollRowThead scrollCR">省份</th>
		<th nowrap class="scrollRowThead scrollCR">服务中心名称</th>
		<th nowrap>备案时间</th>
		<th nowrap>审批时间</th>
		<th nowrap>大客户名称</th>
		<th nowrap>联系人</th>
		<th nowrap>电话</th>
		<th nowrap>客户类型</th>
		<th nowrap>车系</th>
		<th nowrap>备案数</th>
		<th nowrap>合同审批数</th>
		<th nowrap>享受政策支持</th>
		<th nowrap>备注</th>
		
	</tr>
	<%
		List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("list_FleetPutAll");
		int len = list.size();
		long ssc = 0;
		long sic = 0;
		
		
		long ssc_rn = 0;
		long sic_rn = 0;
		
		
		long ssc_ron = 0;
		long sic_ron = 0;
		if(list!=null){
			for(int i=0;i<len;i++){
				long sc = Long.parseLong(list.get(i).get("SERIES_COUNT").toString());
				long ic = Long.parseLong(list.get(i).get("INTENT_COUNT").toString());
				if(i==0){
					ssc = sc;
					sic = ic;
					
					tsc = sc;
					tic = ic;
					
					ssc_rn = sc;
					sic_rn = ic;
					
					ssc_ron = sc;
					sic_ron = ic;
					
					%>
					<tr>
					<td nowrap><%=list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()%> &nbsp;</td>
					</tr>
					<% 
				}else{
					String before = list.get(i-1).get("DEALER_NAME").toString();
					String after = list.get(i).get("DEALER_NAME").toString();
					
					String before11 = list.get(i-1).get("REGION_NAME").toString();
					String after12 = list.get(i).get("REGION_NAME").toString();
					
					String before21 = list.get(i-1).get("ROOT_ORG_NAME").toString();
					String after22 = list.get(i).get("ROOT_ORG_NAME").toString();
					if(after.equals(before)&&after12.equals(before11)&&after22.equals(before21)){
						ssc += sc;
						sic += ic;
						
						tsc += sc;
						tic += ic;
						
						ssc_rn += sc;
						sic_rn += ic;
						
						ssc_ron += sc;
						sic_ron += ic;
						%>
						<tr>
						<td nowrap ><%=list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
						<td nowrap ><%=list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
						<td nowrap ><%=list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()%> &nbsp;</td>
						</tr>
						<%
					}else{
						%>
						<tr>
						<th nowrap align="center" colspan="3"><strong>经销商合计</strong>&nbsp;</th>
						<th nowrap colspan="7">&nbsp;</th>
						<th nowrap><%=ssc==0?"":ssc%>&nbsp;</th>
						<th nowrap><%=sic==0?"":sic%>&nbsp;</th>
						<th colspan="2" nowrap>&nbsp;</th>
						</tr>
						<%
						ssc = sc;
						sic = ic;
						
						tsc += sc;
						tic += ic;
						
						
						//开始1
						String before1 = list.get(i-1).get("REGION_NAME").toString();
						String after1 = list.get(i).get("REGION_NAME").toString();
						
						
						String before31 = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after32 = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after1.equals(before1)&&after32.equals(before31)){
							ssc_rn += sc;
							sic_rn += ic;
							
							ssc_ron += sc;
							sic_ron += ic;
							
							
						}else{
							%>
							<tr>
							<th nowrap align="center" colspan="3" class="scrollRowThead"><strong>省份合计</strong>&nbsp;</th>
							<th nowrap colspan="7" class="scrollRowThead">&nbsp;</th>
							<th nowrap class="scrollRowThead"><%=ssc_rn==0?"":ssc_rn%>&nbsp;</th>
							<th nowrap class="scrollRowThead"><%=sic_rn==0?"":sic_rn%>&nbsp;</th>
							<th colspan="2" nowrap class="scrollRowThead">&nbsp;</th>
							</tr>
							<%
							
							
							ssc_rn = 0;
							sic_rn = 0;
							
							ssc_rn += sc;
							sic_rn += ic;
							
							//ssc_ron += sc;
							//sic_ron += ic;
							
							
							
							
							//开始2
							String before2 = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after2 = list.get(i).get("ROOT_ORG_NAME").toString();
							
							if(after2.equals(before2)){
								ssc_ron += sc;
								sic_ron += ic;
							}else{
								%>
								<tr>
								<th nowrap align="center" colspan="3" class="scrollRowThead"><strong>大区合计</strong>&nbsp;</th>
								<th nowrap colspan="7" class="scrollRowThead">&nbsp;</th>
								<th nowrap class="scrollRowThead"><%=ssc_ron==0?"":ssc_ron%>&nbsp;</th>
								<th nowrap class="scrollRowThead"><%=sic_ron==0?"":sic_ron%>&nbsp;</th>
								<th colspan="2" class="scrollRowThead" nowrap>&nbsp;</th>
								</tr>
								<%
								
								ssc_ron = 0;
								sic_ron = 0;
								
								
								ssc_ron += sc;
								sic_ron += ic;
							}
							//结束2
						}
						//结束1
						%>
						<tr>
						<td nowrap ><%=list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
						<td nowrap ><%=list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()%> &nbsp;</td>
						</tr>
						<%
					}
					if(i==len-1){
						%>
						<tr>
						<th nowrap align="center" colspan="3" ><strong>经销商合计</strong>&nbsp;</th>
						<th nowrap colspan="7">&nbsp;</th>
						<th nowrap><%=ssc==0?"":ssc%>&nbsp;</th>
						<th nowrap><%=sic==0?"":sic%>&nbsp;</th>
						<th colspan="2" nowrap>&nbsp;</th>
						</tr>
						<%
						
						%>
						<tr>
						<th nowrap align="center" colspan="3" class="scrollRowThead"><strong>省份合计</strong>&nbsp;</th>
						<th nowrap colspan="7" class="scrollRowThead">&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=ssc_rn==0?"":ssc_rn%>&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=sic_rn==0?"":sic_rn%>&nbsp;</th>
						<th colspan="2" nowrap class="scrollRowThead">&nbsp;</th>
						</tr>
						<%
						
						%>
						<tr>
						<th nowrap align="center" colspan="3" class="scrollRowThead "><strong>大区合计</strong>&nbsp;</th>
						<th nowrap colspan="7" class="scrollRowThead">&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=ssc_ron==0?"":ssc_ron%>&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=sic_ron==0?"":sic_ron%>&nbsp;</th>
						<th colspan="2" class="scrollRowThead" nowrap>&nbsp;</th>
						</tr>
						<%
					}
				}
			}
		}
	%>
	
	
						<tr>
						<th nowrap align="center" colspan="3" class="scrollRowThead "><strong>合计</strong>&nbsp;</th>
						<th nowrap colspan="7" class="scrollRowThead">&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=tsc==0?"":tsc%>&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=tic==0?"":tic%>&nbsp;</th>
						<th colspan="2" nowrap class="scrollRowThead">&nbsp;</th>
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
