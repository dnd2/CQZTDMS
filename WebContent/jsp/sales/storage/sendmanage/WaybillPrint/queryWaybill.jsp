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
<title>运单查看</title>
<% 
String contextPath = request.getContextPath(); 
Map<String,Object> valueMap = (Map<String,Object>)request.getAttribute("valueMap");
List<Map<String,Object>> valueList = (List<Map<String,Object>>)request.getAttribute("valueList");

%>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 运单查看</div>
<table class="tab_viewsep"  align="center"  border="0">
<tr><td width="15%" class="right">发运日期：</td><td width="15%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BILL_CRT_DATE")) %></td>
	<td width="15%" class="right">抵达日期：</td><td width="15%">${expectArriveDate }</td>
	<td width="10%" class="right">险种：</td><td width="5%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CODE_DESC")) %></td>
	<td width="10%" class="right">保单号：</td><td width="5%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("POLICY_NO")) %></td>
	<td width="10%" class="right">运单号：</td><td width="10%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BILL_NO")) %></td>
</tr>
</table>
<table class="tab_viewsep"  align="center" border="1">
<tr><td class="right" width="12%">收车经销商：</td><td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("DEALER_NAME")) %></td>
	<td class="right" width="8%">联系人：</td><td><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("LINK_MAN")) %></td>
	<td class="right" width="8%">电话：</td><td width="12%"><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("PHONE")) %></td>
</tr>
<tr><td class="right" width="12%">交车地址：</td><td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("ADDRESS")) %></td>
	<td class="right" width="8%">地点：</td><td><%=valueMap==null?"          ":CommonUtils.checkNull(valueMap.get("REGION_NAME")) %></td>
	<td class="right" width="8%">手机：</td><td width="12%"><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("TEL")) %></td>
</tr>
<tr><td class="right"  width="12%">组板号：</td><td colspan="5"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BONO")) %></td>
</tr>
</table>
<table class="tab_viewsep"  align="center" border="1">
<tr>
<th align="center">序号</th>
<th align="center">发运申请号</th>
<th align="center">车型</th>
<th align="center">配置</th>
<th align="center">发动机</th>
<th align="center">颜色</th>
<th align="center">VIN号</th>
<th align="center">发票号</th>
<!-- 
<td align="center" >承运车队</td>
<td align="center" >承运车号</td>
-->
</tr>
<% if(valueList!=null && valueList.size()>0){
		  for(int i=0;i<valueList.size();i++){
		    Map<String,Object> veMap = (Map<String,Object>)valueList.get(i);%>
		    <tr><td align="center" ><%=i+1%>&nbsp;</td>
		    <td align="center" ><a href='javascript:void(0);' onclick='viewOrderInfo("<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=<%=CommonUtils.checkNull(veMap.get("ORDER_ID"))%>");'><%=CommonUtils.checkNull(veMap.get("ORDER_NO"))%></a>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("MODEL_NAME"))%>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("PACKAGE_NAME"))%></td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("ENGINE_NO"))%>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("COLOR_NAME"))%>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("VIN"))%>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("INVOICE_NO"))%>&nbsp;</td>
		  <!--   <td align="center" ><%=CommonUtils.checkNull(veMap.get("CAR_TEAM"))%>&nbsp;</td>
		    <td align="center" ><%=CommonUtils.checkNull(veMap.get("CAR_NO"))%>&nbsp;</td>  -->
		    </tr>
	<%}} %>

</table>
<table  align="center" class="tab_viewsep"  border="1">
     <tr>
		<td class="right"  width="12%">车队经办人：</td>
		<td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("NAME")) %></td>
		<td class="right"  width="12%">承运车队：</td>
		<td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CAR_TEAM")) %></td>
		<td class="right"  width="12%">运输车号：</td>
		<td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CAR_NO")) %></td>
		<td class="right"  width="12%">总数：</td>
		<td><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("VEH_NUM")) %><font color="red">(<%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("VEH_NUM_01")) %>)</font></td>
 	</tr>
 	<tr>
 		<td class="right"  width="12%" colspan="2">驾驶员姓名：</td>
		<td colspan="2"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("DRIVER_NAME")) %></td>
		<td class="right"  width="12%" colspan="2">驾驶员电话：</td>
		<td colspan="2"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("DRIVER_TEL")) %></td>
 	</tr>
 	<tr>
		<td class="right" rowspan="2"  width="12%">说明备注：</td>
		<td  colspan="5" rowspan="2" align="center"><%=Constant.WAYBILL_PRINT_REMARK%></td>
		<td class="right"  width="12%">收车人及单位：</td>
		<td width="12%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("INSPECTION_PERSON")) %>,<%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("DEALER_NAME")) %></td>
 	</tr>
 	<tr>
		<td class="right"  width="12%">收车日期：</td>
		<td width="12%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("STORAGE_DATE")) %></td>
 	</tr>
</table>
<br/>
<table  align="center"   border="0">
     <tr align="center">
		<td colspan="8">
			<input type="button" class="normal_btn" value="关闭" onclick="_hide()">
		</td>
 	</tr>
</table>
</div>
<script type="text/javascript">
function viewOrderInfo(url)
{
	window.open(url);
}
</script>
</body>
</html>