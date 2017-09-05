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
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团客户报备报表</title>
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
     white-space: nowrap;
     text-align: center;
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>集团客户报备报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
	<td colspan="7" class="scrollRowThead scrollCR"><strong>集团客户报备报表</strong></td>
	<td colspan="12">&nbsp;</td>
	</tr>
	<tr class="scrollColThead" align="left">
	<td colspan="19" class="scrollRowThead scrollCR" align="left"><div align="left">提报日期：${checkDate1}--${checkDate2}</div></td>
	</tr>
	
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" nowrap>组织</th>
		<th class="scrollRowThead scrollCR" nowrap>省</th>
		<th class="scrollRowThead scrollCR" nowrap>提报单位</th>
		<th nowrap>提报日期</th>
		<th nowrap>客户名称</th>
		<th nowrap>客户类型</th>
		<th nowrap>主营业务</th>
		<th nowrap>邮编 </th>
		<th nowrap>详细地址</th>
		<th nowrap>主联系人</th>
		<th nowrap>职务</th>
		<th nowrap>电话</th>
		<th nowrap>车系</th>
		<th nowrap>数量</th>
		<th nowrap>备注</th>
		<th nowrap>确认状态</th>
		<th nowrap>确认说明</th>
		<th nowrap>确认人</th>
		<th nowrap>确认时间</th>
	</tr>
	<%
		List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("list_FleetPrepareReport");
		int len = list.size();
		long ssc = 0;
		
		long ssc_ron = 0;
		if(list!=null){
			for(int i=0;i<len;i++){
				long sc = Long.parseLong(list.get(i).get("SERIES_COUNT").toString());
				
				if(i==0){
					ssc = sc;
					
					ssc_ron = sc;
					tsc = sc;
					%>
					<tr>
					<td  nowrap><%=list.get(i).get("ROOT_ORG_NAME")==null ? "":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
					<td  nowrap><%=list.get(i).get("COMPANY_SHORTNAME")==null ? "":list.get(i).get("COMPANY_SHORTNAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ? "":list.get(i).get("SUBMIT_DATE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_NAME")==null ? "":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ? "":list.get(i).get("FLEET_TYPE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_BUSINESS")==null ? "":list.get(i).get("MAIN_BUSINESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ZIP_CODE")==null ? "":list.get(i).get("ZIP_CODE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ADDRESS")==null ? "":list.get(i).get("ADDRESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ? "":list.get(i).get("MAIN_LINKMAN").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_JOB")==null ? "":list.get(i).get("MAIN_JOB").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ? "":list.get(i).get("MAIN_PHONE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUP_NAME")==null ? "":list.get(i).get("GROUP_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SERIES_COUNT")==null ? "":list.get(i).get("SERIES_COUNT").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("REQ_REMARK")==null ? "":list.get(i).get("REQ_REMARK").toString() %>&nbsp;</td> 
					<td nowrap><%=list.get(i).get("STATUS")==null ? "":list.get(i).get("STATUS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_REMARK")==null ? "":list.get(i).get("AUDIT_REMARK").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("NAME")==null ? "":list.get(i).get("NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ? "":list.get(i).get("AUDIT_DATE").toString() %>&nbsp;</td>
					</tr>
					<% 
				}else{
					String befor = list.get(i-1).get("REGION_NAME").toString();
					String after = list.get(i).get("REGION_NAME").toString();
					
					
					String befor11 = list.get(i-1).get("ROOT_ORG_NAME").toString();
					String after12 = list.get(i).get("ROOT_ORG_NAME").toString();
					if(after.equals(befor)&&after12.equals(befor11)){
						ssc += sc;
						ssc_ron += sc;
						tsc +=sc;
						%>
					<tr>
					<td  nowrap><%=list.get(i).get("ROOT_ORG_NAME")==null ? "":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
					<td  nowrap><%=list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
					<td  nowrap><%=list.get(i).get("COMPANY_SHORTNAME")==null ? "":list.get(i).get("COMPANY_SHORTNAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ? "":list.get(i).get("SUBMIT_DATE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_NAME")==null ? "":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ? "":list.get(i).get("FLEET_TYPE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_BUSINESS")==null ? "":list.get(i).get("MAIN_BUSINESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ZIP_CODE")==null ? "":list.get(i).get("ZIP_CODE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ADDRESS")==null ? "":list.get(i).get("ADDRESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ? "":list.get(i).get("MAIN_LINKMAN").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_JOB")==null ? "":list.get(i).get("MAIN_JOB").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ? "":list.get(i).get("MAIN_PHONE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUP_NAME")==null ? "":list.get(i).get("GROUP_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SERIES_COUNT")==null ? "":list.get(i).get("SERIES_COUNT").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("REQ_REMARK")==null ? "":list.get(i).get("REQ_REMARK").toString() %>&nbsp;</td> 
					<td nowrap><%=list.get(i).get("STATUS")==null ? "":list.get(i).get("STATUS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_REMARK")==null ? "":list.get(i).get("AUDIT_REMARK").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("NAME")==null ? "":list.get(i).get("NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ? "":list.get(i).get("AUDIT_DATE").toString() %>&nbsp;</td>
					</tr>
						<% 
					}else{
						%>
						<tr>
						<th nowrap  colspan="3" align="center"><strong>省份合计</strong>&nbsp;</th>
						<th nowrap colspan="10">&nbsp;</th>
						<th nowrap><%=ssc %>&nbsp;</th>
						<th nowrap colspan="5">&nbsp;</th>
						</tr>
						<% 
						ssc = sc;
						tsc +=sc;
						
						
						//开始
						String befor_ron = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after_ron = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after_ron.equals(befor_ron)){
							ssc_ron += sc;
						}else{
							%>
							<tr>
							<th nowrap class="scrollRowThead" colspan="3" align="center"><strong>大区合计</strong>&nbsp;</th>
							<th nowrap class="scrollRowThead" colspan="10">&nbsp;</th>
							<th nowrap class="scrollRowThead"><%=ssc_ron %>&nbsp;</th>
							<th nowrap class="scrollRowThead" colspan="5">&nbsp;</th>
							</tr>
							<% 
							ssc_ron = 0;
							ssc_ron += sc;
						}
						//结束
						%>
					<tr>
					<td nowrap><%=list.get(i).get("ROOT_ORG_NAME")==null ? "":list.get(i).get("ROOT_ORG_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("REGION_NAME")==null ? "":list.get(i).get("REGION_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("COMPANY_SHORTNAME")==null ? "":list.get(i).get("COMPANY_SHORTNAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SUBMIT_DATE")==null ? "":list.get(i).get("SUBMIT_DATE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_NAME")==null ? "":list.get(i).get("FLEET_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("FLEET_TYPE")==null ? "":list.get(i).get("FLEET_TYPE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_BUSINESS")==null ? "":list.get(i).get("MAIN_BUSINESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ZIP_CODE")==null ? "":list.get(i).get("ZIP_CODE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("ADDRESS")==null ? "":list.get(i).get("ADDRESS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_LINKMAN")==null ? "":list.get(i).get("MAIN_LINKMAN").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_JOB")==null ? "":list.get(i).get("MAIN_JOB").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("MAIN_PHONE")==null ? "":list.get(i).get("MAIN_PHONE").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("GROUP_NAME")==null ? "":list.get(i).get("GROUP_NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("SERIES_COUNT")==null ? "":list.get(i).get("SERIES_COUNT").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("REQ_REMARK")==null ? "":list.get(i).get("REQ_REMARK").toString() %>&nbsp;</td> 
					<td nowrap><%=list.get(i).get("STATUS")==null ? "":list.get(i).get("STATUS").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_REMARK")==null ? "":list.get(i).get("AUDIT_REMARK").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("NAME")==null ? "":list.get(i).get("NAME").toString() %>&nbsp;</td>
					<td nowrap><%=list.get(i).get("AUDIT_DATE")==null ? "":list.get(i).get("AUDIT_DATE").toString() %>&nbsp;</td>
					</tr>
						<% 
					}
					if(i==len-1){
						%>
						<tr>
						<th nowrap colspan="3" align="center"><strong>省份合计</strong>&nbsp;</th>
						<th nowrap colspan="10">&nbsp;</th>
						<th nowrap><%=ssc %>&nbsp;</th>
						<th nowrap colspan="5">&nbsp;</th>
						</tr>
						<% 
						
						%>
						<tr>
						<th nowrap class="scrollRowThead" colspan="3" align="center"><strong>大区合计</strong>&nbsp;</th>
						<th nowrap class="scrollRowThead" colspan="10">&nbsp;</th>
						<th class="scrollRowThead" nowrap><%=ssc_ron %>&nbsp;</th>
						<th  class="scrollRowThead" nowrap colspan="5">&nbsp;</th>
						</tr>
						<% 
					}
				}
			}
		}
 	%>
 	
 	
 	
 						<tr>
 						<th nowrap class="scrollRowThead" colspan="3" align="center"><strong>合计</strong>&nbsp;</th>
						<th nowrap class="scrollRowThead" colspan="10">&nbsp;</th>
						<th nowrap class="scrollRowThead"><%=tsc==0 ? "":tsc %>&nbsp;</th>
						<th nowrap class="scrollRowThead" colspan="5">&nbsp;</th>
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
