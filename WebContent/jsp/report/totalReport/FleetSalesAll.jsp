<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 

	long tsa = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>大客户信息实销汇总表</title>
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
	<strong>大客户信息实销汇总表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
	<td colspan="17" class="scrollRowThead scrollCR"><strong>大客户信息实销汇总表</strong></td>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="17" class="scrollRowThead scrollCR" align="left"><div align="left">交车起止时间：${insertBeginTime}--${insertEndTime}</div></td>
	</tr>
	
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" nowrap>大区</th>
		<th class="scrollRowThead scrollCR" nowrap>省份</th>
		<th class="scrollRowThead scrollCR" nowrap>服务中心名称</th>
		<th nowrap>交库日期</th>
		<th nowrap>客户名称</th>
		<th nowrap>联系人</th>
		<th nowrap>电话</th>
		<th nowrap>客户类型</th>
		<th nowrap>车系</th>
		<th nowrap>车型</th>
		<th nowrap>实际交车数</th>
		<th nowrap>不合格资料数</th>
		<th nowrap>享受政策支持</th>
		<th nowrap>单价</th>
		<th nowrap>兑现折让总额</th>
		<th nowrap>审核状态</th>
		<th nowrap>备注</th>
	
	</tr>
	
	<%
	//得到action中的集合
		List<Map<String,Object>> list = (List<Map<String,Object>>) request.getAttribute("list_FleetSalesAll");
		int len = list.size();
		long ssa = 0;
		
		long ssa_rn =0;
		
		long ssa_ron =0;
		if(list!=null){
			for(int i=0;i<len;i++){
				long sa = Long.parseLong(list.get(i).get("SALESAMOUNT").toString());
				if(i==0){
					ssa = sa;
					ssa_rn = sa;
					ssa_ron = sa;
					tsa = sa;
					%>
					<tr>
					<td  nowrap><%=list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
					<td  nowrap><%=list.get(i).get("REGION_NAME").toString() %> &nbsp;</td>
					<td  nowrap><%=list.get(i).get("DEALER_NAME").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("CONSIGNATION_DATE").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_NAME").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_LINKMAN").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_PHONE").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_TYPE").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUP_NAME") == null ?  "" : list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUPMODEL").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("SALESAMOUNT").toString().equals("0") ? "" : list.get(i).get("SALESAMOUNT").toString() %> &nbsp;</td>
					<td nowrap>&nbsp;</td>
					<td nowrap><%=list.get(i).get("INTENT_POINT").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("PRICE").toString() %> &nbsp;</td>
					<td nowrap>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_STATUS").toString() %> &nbsp;</td>
					<td nowrap><%=list.get(i).get("REQ_REMARK") == null ?  "" : list.get(i).get("REQ_REMARK").toString() %> &nbsp;</td> 
					</tr>
					<% 
				}else{
					String before_da = list.get(i-1).get("DEALER_NAME").toString();
					String after_da = list.get(i).get("DEALER_NAME").toString();
					
					String before_da11 = list.get(i-1).get("REGION_NAME").toString();
					String after_da12 = list.get(i).get("REGION_NAME").toString();
					
					String before_da21 = list.get(i-1).get("ROOT_ORG_NAME").toString();
					String after_da22 = list.get(i).get("ROOT_ORG_NAME").toString();
					if(after_da.equals(before_da)&&after_da12.equals(before_da11)&&after_da22.equals(before_da21)){
						ssa += sa;
						ssa_rn += sa;
						ssa_ron += sa;
						
						tsa += sa;
						%>
						<tr>
						<td  nowrap><%=list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
						<td  nowrap><%=list.get(i).get("REGION_NAME").toString() %> &nbsp;</td>
						<td  nowrap><%=list.get(i).get("DEALER_NAME").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("CONSIGNATION_DATE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_NAME").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_LINKMAN").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_PHONE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_TYPE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUP_NAME") == null ?  "" : list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUPMODEL").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("SALESAMOUNT").toString().equals("0") ? "" :  list.get(i).get("SALESAMOUNT").toString() %> &nbsp;</td>
						<td nowrap>&nbsp;</td>
						<td nowrap><%=list.get(i).get("INTENT_POINT")==null ? "":list.get(i).get("INTENT_POINT").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("PRICE").toString() %> &nbsp;</td>
						<td nowrap>&nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_STATUS") == null ? "":list.get(i).get("FLEET_STATUS").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("REQ_REMARK") == null ?  "" : list.get(i).get("REQ_REMARK").toString() %> &nbsp;</td> 
						</tr>
						<%
					}else{
						%>
						<tr>
						<th  colspan="3" align="center" nowrap><strong>经销商合计</strong> &nbsp;</th>
						<th nowrap colspan="7"> &nbsp;</th>
						<th nowrap align="center"><%=ssa%> &nbsp;</th>
						<th nowrap colspan="6"> &nbsp;</th>
						</tr>
						<% 
						ssa =sa;
						tsa += sa;
						//开始1
						String before_rn = list.get(i-1).get("REGION_NAME").toString();
						String after_rn = list.get(i).get("REGION_NAME").toString();
						
						String before_rn31 = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after_rn32 = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after_rn.equals(before_rn)&&after_rn32.equals(before_rn31)){
							ssa_rn += sa;
							ssa_ron += sa;
						}else{
							%>
							<tr>
							<th  colspan="3" align="center" nowrap class="scrollRowThead"><strong>省份合计</strong> &nbsp;</th>
							<th nowrap colspan="7" class="scrollRowThead"> &nbsp;</th>
							<th nowrap align="center" class="scrollRowThead"><%=ssa_rn%> &nbsp;</th>
							<th nowrap colspan="6" class="scrollRowThead"> &nbsp;</th>
							</tr>
							<% 
							ssa_rn = 0;
							
							ssa_rn += sa;
							//开始2
							String before_ron = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after_ron = list.get(i).get("ROOT_ORG_NAME").toString();
							if(after_ron.equals(before_ron)){
								ssa_ron += sa;
							}else{
								%>
								<tr>
								<th class="scrollRowThead" colspan="3" align="center" nowrap><strong>大区合计</strong> &nbsp;</th>
								<th class="scrollRowThead" nowrap colspan="7"> &nbsp;</th>
								<th class="scrollRowThead" nowrap align="center"><%=ssa_ron%> &nbsp;</th>
								<th class="scrollRowThead" nowrap colspan="6"> &nbsp;</th>
								</tr>
								<% 
								
								ssa_ron = 0;
								ssa_ron += sa;
							}
							//结束2
						}
						//结束1
						%>
						<tr>
						<td nowrap><%=list.get(i).get("ROOT_ORG_NAME").toString() %> &nbsp;</td>
						<td  nowrap><%=list.get(i).get("REGION_NAME").toString() %> &nbsp;</td>
						<td  nowrap><%=list.get(i).get("DEALER_NAME").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("CONSIGNATION_DATE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_NAME").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_LINKMAN").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("MAIN_PHONE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_TYPE").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUP_NAME") == null ?  "" : list.get(i).get("GROUP_NAME").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("GROUPMODEL").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("SALESAMOUNT").toString().equals("0") ? "" : list.get(i).get("SALESAMOUNT").toString() %> &nbsp;</td>
						<td nowrap>&nbsp;</td>
						<td nowrap><%=list.get(i).get("INTENT_POINT").toString() %> &nbsp;</td>
						<td nowrap><%=list.get(i).get("PRICE").toString() %> &nbsp;</td>
						<td nowrap>&nbsp;</td>
						<td nowrap><%=list.get(i).get("FLEET_STATUS") == null ? "":list.get(i).get("FLEET_STATUS").toString()%> &nbsp;</td>
						<td nowrap><%=list.get(i).get("REQ_REMARK") == null ?  "" : list.get(i).get("REQ_REMARK").toString() %> &nbsp;</td> 
						</tr>
						<% 
					}
					if(i==len-1){
						%>
						<tr>
						<th colspan="3" align="center" nowrap><strong>经销商合计</strong> &nbsp;</th>
						<th nowrap colspan="7"> &nbsp;</th>
						<th nowrap align="center"><%=ssa%> &nbsp;</th>
						<th nowrap colspan="6"> &nbsp;</th>
						</tr>
						<% 
						
						%>
						<tr>
						<th colspan="3" align="center" nowrap class="scrollRowThead"><strong>省份合计</strong> &nbsp;</th>
						<th nowrap colspan="7" class="scrollRowThead"> &nbsp;</th>
						<th nowrap align="center" class="scrollRowThead"><%=ssa_rn%> &nbsp;</th>
						<th nowrap colspan="6" class="scrollRowThead"> &nbsp;</th>
						</tr>
						<% 
						
						%>
						<tr>
						<th class="scrollRowThead" colspan="3" align="center" nowrap><strong>大区合计</strong> &nbsp;</th>
						<th class="scrollRowThead" nowrap colspan="7"> &nbsp;</th>
						<th class="scrollRowThead" nowrap align="center"><%=ssa_ron%> &nbsp;</th>
						<th class="scrollRowThead" nowrap colspan="6"> &nbsp;</th>
						</tr>
						<% 
					}
				}
			}
		}
	%>
	
	
	
						<tr>
						<th class="scrollRowThead" colspan="3" align="center" nowrap><strong>总计实销数量</strong> &nbsp;</th>
						<th class="scrollRowThead" nowrap colspan="7"> &nbsp;</th>
						<th class="scrollRowThead" nowrap  align="center"><strong><%=tsa%> </strong>&nbsp;</th>
						<th class="scrollRowThead" nowrap colspan="6"> &nbsp;</th>
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
