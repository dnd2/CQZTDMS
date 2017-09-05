<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />


<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>运单打印</title>
<% 
String contextPath = request.getContextPath(); 
Map<String,Object> valueMap = (Map<String,Object>)request.getAttribute("valueMap");
List<Map<String,Object>> valueList = (List<Map<String,Object>>)request.getAttribute("valueList");

%>
<script type="text/javascript">
function formatMoney(dataStr){
	var str = formatCurrency(dataStr);
	document.write(str);
}
</script>
</head>
<body>
<br/>
<center><strong><font size="4">重庆市嘉陵川江汽车制造有限公司-运费结算单</font></strong></center>
<br/>
<table class="tabp2" align="center" width="900px" border="0">
<tr align="center"><td>结算车队：</td><td>${bal_info.LOGI_FULL_NAME }</td>
	<td>结算年度：</td><td>${bal_info.YEAR }</td>
	<td>结算月份：</td><td>${bal_info.MONTH }</td>
	<td>结算单号：</td><td>${bal_info.BAL_NO }</td>
</tr>
</table>
<table class="tabp2" width="900px" border="1" align="center">
	<tr align="center">
		<td><strong>序号</strong></td>
		<td><strong>日期</strong></td>
		<td><strong>发运单号</strong></td>
		<td><strong>发往单位</strong></td>
		<td><strong>车系名称</strong></td>
		<td><strong>数量</strong></td>
		<td><strong>运送里程</strong></td>
		<td><strong>里程单价</strong></td>
		<td><strong>结算金额</strong></td>
		<td><strong>备注</strong></td>
	</tr>
	<c:forEach items="${list }" var="po">
		<tr align="center">
			<td>${po.ROW_NUM }</td>
			<td>${po.CREATE_DATE }</td>
			<td>${po.BILL_NO }</td>
			<td>${po.DEALER_NAME }</td>
			<td>${po.GROUP_NAME  }</td>
			<td>${po.COUNT  }</td>
			<td>${po.DISTANCE }</td>
			<td><script>
				formatMoney(${po.SEND_FARE });
			</script></td>
			<td><script>
				formatMoney(${po.AMOUNT });
			</script></td>
			<td>${po.REGION_NAME }</td>
		</tr>
	</c:forEach>
</table>
<table class="tabp2" width="900px" border="1" align="center">
	<tr align="center">
		<td><strong>车系</strong></td>
		<td colspan="2"><strong>车数小计</strong></td>
		<td colspan="2"><strong>常规运费</strong></td>
		<td><strong>非常规运费</strong></td>
		<td><strong>附加运费</strong></td>
		<td colspan="2"><strong>运费合计</strong></td>
	</tr>
	<c:forEach items="${series_list }" var="po">
		<tr align="center">
			<td >${po.GROUP_NAME }</td>
			<td colspan="2">${po.VEHICLE_NUM }</td>
			<td colspan="2">${po.BAL_AMOUNT }</td>
			<td>0</td>
			<td>${po.FJ_AMOUNT }</td>
			<td colspan="2">${po.BAL_AMOUNT }</td>
		</tr>
	</c:forEach>
		<tr align="center">
			<td>合计</td>
			<td colspan="2">${vehicle_num }</td>
			<td colspan="2">${sum }</td>
			<td>0</td>
			<td>${fj_sum }</td>
			<td colspan="2">${hj_sum }</td>
		</tr>
</table>
<table class="tabp2" width="850px" border="0" align="center">
	<tr >
		<td>&nbsp;</td>
		<Td colspan="4"><font  style="font-weight: bold;font-size: 15px;" color="black">甲方审核:</font></Td>
		<Td colspan="4" class="table_query_4Col_input" style="text-align: center"><font style="font-weight: bold;font-size: 15px;" color="black">乙方审核:</font></Td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<Td colspan="4">&nbsp;</Td>
		<Td colspan="4">&nbsp;</Td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<Td colspan="4">经办人：</Td>
		<Td colspan="4">&nbsp;</Td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<Td colspan="4">&nbsp;</Td>
		<Td colspan="4">&nbsp;</Td>
	</tr>
	<tr >
		<td>&nbsp;</td>
		<Td colspan="4">财务审核:</Td>
		<Td colspan="4" class="table_query_4Col_input" style="text-align: center">经办人:</Td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<Td colspan="4">&nbsp;</Td>
		<Td colspan="4">&nbsp;</Td>
	</tr>
	<tr >
		<td>&nbsp;</td>
		<Td colspan="4">业务审核:</Td>
		<Td colspan="4" class="table_query_4Col_input" style="text-align: center"></Td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<Td colspan="4">&nbsp;</Td>
		<Td colspan="4">&nbsp;</Td>
	</tr>
	<tr >
		<td>&nbsp;</td>
		<Td colspan="4">物流处处长审核：</Td>
		<Td colspan="4" class="table_query_4Col_input" style="text-align: center">盖章：</Td>
	</tr>
	<br/>
</table>
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