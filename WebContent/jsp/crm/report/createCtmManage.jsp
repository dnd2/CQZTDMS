<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.jatools.db.af"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DecimalFormat"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 		
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增潜客管理报表</title>
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
height:650px;
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
<script type="text/javascript">
function getDealerLists(dealer_id){
//MyAlert(dealer_id);
	var startDate=document.getElementById("startDate").value;
	var endDate=document.getElementById("endDate").value;
	var dealerCode=document.getElementById("dealerCode").value;
	var seriesId=document.getElementById("seriesId").value;
	var parameter="?dealer_id="+dealer_id+"&startDate="+startDate+"&endDate="+endDate+"&seriesId="+seriesId;
	//MyAlert('<%=contextPath%>/crm/report/CreateCtmManage/getDealerList.do'+parameter);
	var openUrl='<%=contextPath%>/crm/report/CreateCtmManage/getDealerList.do'+parameter;
	window.open (openUrl, "_blank");
}

</script>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>新增潜客管理报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
		<input type="hidden" id="dealerCode" value="${dealerCode}" />
		<input type="hidden" id="startDate" value="${startDate}" />
		<input type="hidden" id="endDate" value="${endDate}" />
		<input type="hidden" id="seriesId" value="${seriesId}" />
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	 
	<tr class="scrollColThead">
		<th  nowrap  class="scrollRowThead scrollCR" >大区</th>
		<th  nowrap  class="scrollRowThead scrollCR" >省份</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商代码"  >经销商代码</th>
		<th  nowrap class="scrollRowThead scrollCR" title="经销商简称"  >经销商名称</th>
		<th  nowrap class="scrollRowThead scrollCR"  >客户总数</th>
		<th  nowrap class="scrollRowThead scrollCR"  >战败客户</th>
		<th  nowrap class="scrollRowThead scrollCR"  >占比</th>
		<th  nowrap class="scrollRowThead scrollCR"  >失效客户</th>
		<th  nowrap class="scrollRowThead scrollCR"  >占比</th>
		<th  nowrap class="scrollRowThead scrollCR"  >保有客户</th>
		<th  nowrap class="scrollRowThead scrollCR"  >占比</th>
		<th  nowrap class="scrollRowThead scrollCR"  >有望客户</th>
		<th  nowrap class="scrollRowThead scrollCR"  >占比</th>
	</tr>
	<% List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("ctmManageList"); 
	int total_account=0;
	int ZBKH_ACCOUNT=0;
	int SXKH_ACCOUNT=0;
	int BYKH_ACCOUNT=0;
	int YWKH_ACCOUNT=0;
	String ZBKH_RATE="0";
	String SXKH_RATE="0";
	String BYKH_RATE="0";
	String YWKH_RATE="0";
	float f_ZBKH_RATE=0;
	float f_SXKH_RATE=0;
	float f_BYKH_RATE=0;
	float f_YWKH_RATE=0;
	DecimalFormat df = new DecimalFormat("0.00");//格式化小数
	 %>
		<% for(int i=0;i<list.size();i++){ %>
		<tr >
			<td nowrap ><%=list.get(i).get("ROOT_ORG_NAME")%>&nbsp; </td>
			<td nowrap ><%=list.get(i).get("PQ_ORG_NAME")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("DEALER_CODE")%>&nbsp;</td>
			<td nowrap >
			<%
			if(!"".equals(list.get(i).get("DEALER_ID"))&&list.get(i).get("DEALER_ID")!=null){
			%>
				<a href="#" onclick="getDealerLists('<%=list.get(i).get("DEALER_ID")%>')"><%=list.get(i).get("DEALER_SHORTNAME")%>&nbsp;</a>
			<% 
			}else{
			%>
			
				<%=list.get(i).get("DEALER_SHORTNAME")%>&nbsp;
			 
			 <%
			}
			 %>
					    
				
						
				
			</td>
			<td nowrap ><%=list.get(i).get("TOTAL_ACCOUNT")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("ZBKH_ACCOUNT")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("ZBKH_RATE")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("SXKH_ACCOUNT")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("SXKH_RATE")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("BYKH_ACCOUNT")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("BYKH_RATE")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("YWKH_ACCOUNT")%>&nbsp;</td>
			<td nowrap ><%=list.get(i).get("YWKH_RATE")%>&nbsp;</td>
		</tr>
		
  <%}
 		
   %>
  
</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr>
	<td colspan="4">
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td>
	</tr>
</table>
</body>
</html>
