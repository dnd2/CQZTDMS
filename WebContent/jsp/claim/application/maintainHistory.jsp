<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> maintaimHisList = (List<Map<String,Object>>)request.getAttribute("maintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>维修历史[VIN:<%=vin%>]</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<script type="text/javascript">
function roDetail(id,ro_create_date,roNo){
	//OpenHtmlWindow('<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=5&ID='+id,800,500);
	//新分单时间
	var str ='2015-05-25 19:30:00';
	var st = str.replace(/-/g,"/");
	var date = (new Date(st)).getTime();  
	var st1 =ro_create_date.replace(/-/g,"/");
	var date1 = (new Date(st1)).getTime();
	if(date>date1){
		fm.action="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&flags=1&&type=5&ID="+id;
		fm.submit();
	}else{
		fm.action="<%=contextPath%>/OrderAction/orderView.do?roNo="+roNo+"&id="+id;
		fm.submit();
	}
}
</script>
<body>
 <form method="post" name ="fm" id="fm">
	<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;维修历史</div>
		</td></tr>
		<tr><td>
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>工单号</th>
					<th>VIN</th>
					<th>经销商名称</th>
					<th>车主名称</th>
					<th>进厂里程</th>
					<th>送修人</th>
					<th>维修类型</th>
					<th>开工单时间</th>
					<th>工单结算时间</th>
					<th>故障原因</th>
					<th>故障描述</th>
					<th>维修措施</th>
					<th>索赔单号</th>
					<th>索赔单上报时间</th>
					<th>索赔单上报人姓名</th>
					<th>工单状态</th>
					<th>索赔单状态</th>
				</tr>
				<% if(maintaimHisList!=null && maintaimHisList.size()>0) { 
					for(int i=0;i<maintaimHisList.size();i++){ 
					Map<String,Object> tempMap = maintaimHisList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td> [<a href='#' onclick='roDetail("<%=CommonUtils.getDataFromMap(tempMap,"ID")%>","<%=CommonUtils.getDataFromMap(tempMap,"RO_CREATE_DATE")%>","<%=CommonUtils.getDataFromMap(tempMap,"RO_NO")%>");' ><%=CommonUtils.getDataFromMap(tempMap,"RO_NO")%></a>] </td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"VIN")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"OWNER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"IN_MILEAGE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DELIVERER")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue('<%=CommonUtils.getDataFromMap(tempMap,"REPAIR_TYPE_CODE")%>')&nbsp;
							</script>
						</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_CREATE_DATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FOR_BALANCE_TIME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TROUBLE_DESC")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TROUBLE_REASON")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPAIR_METHOD")%>&nbsp;</td>
						
						<td><%=CommonUtils.getDataFromMap(tempMap,"CLAIM_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPORT_DATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPORTER")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue('<%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>')&nbsp;
							</script>
							
						</td>
						<td>
							<script>
								writeItemValue('<%=CommonUtils.getDataFromMap(tempMap,"APP_STATUS")%>')&nbsp;
							</script>
							
						</td>
					</tr>
				<% } 
				}%>
			</table>
		</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td align="center">
				<input type="button" name="closeBtn" class="normal_btn" value="关闭" onclick="window.close();"/>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>