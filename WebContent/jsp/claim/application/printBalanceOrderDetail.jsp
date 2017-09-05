<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结算汇总单打印</title>
<% String contextPath = request.getContextPath(); %>
<%
	TtAsWrGatherBalancePO gatherPO = (TtAsWrGatherBalancePO)request.getAttribute("balancePO");
    List<Map<String,Object>> detailList = (List<Map<String,Object>>)request.getAttribute("orderDetail");
    TmDealerPO dealerPO = (TmDealerPO)request.getAttribute("dealerPO");
%>
</head>
<body>
<br/>
<center><strong><font size="4">结算汇总单</font></strong></center>
<br/>
<table class="tabp2" align="center" width="750">
	<tr>
		<td class="tdp">生产基地</td>
		<td colspan="4" class="tdpright">
			<script type="text/javascript">
				document.write(getItemValue(<%=CommonUtils.checkNull(gatherPO.getYieldly())%>));
			</script>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="tdp">序号</td>
		<td class="tdp">单位编码</td>
		<td class="tdp">单位名称</td>
		<td class="tdp">结算单起止日期</td>
		<td class="tdpright">申报金额</td>
	</tr>
	<% if(detailList!=null && detailList.size()>0){
		  for(int i=0;i<detailList.size();i++){
		    Map<String,Object> detailMap = (Map<String,Object>)detailList.get(i);%>
		    <tr>
		    	<td class="tdp"><%=i+1%>&nbsp;</td>
		    	<td class="tdp"><%=CommonUtils.getDataFromMap(detailMap,"DEALER_CODE")%>&nbsp;</td>
		    	<td class="tdp"><%=CommonUtils.getDataFromMap(detailMap,"DEALER_NAME")%>&nbsp;</td>
		    	<td class="tdp"><%=CommonUtils.getDataFromMap(detailMap,"F_START_DATE")%>-<%=CommonUtils.getDataFromMap(detailMap,"F_END_DATE")%>&nbsp;</td>
		    	<td class="tdpright"><%=CommonUtils.getDataFromMap(detailMap,"AMOUNT_SUM")%>&nbsp;</td>
		    </tr>
	<%}} %>
</table>
<br/>
<hr align="center" width="700px"/>
<table width="700px" align="center">
     <tr>
		<td align="left">申报单位：<%=CommonUtils.checkNull(dealerPO.getDealerName())%></td>
		<td align="left">报表日期：<%=CommonUtils.checkNull(request.getAttribute("printDate")) %></td>
 	</tr>
</table>

<br/>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}  
	    
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	}
</script> 

</body>
</html>