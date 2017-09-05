<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> freeMaintaimHisList = (List<Map<String,Object>>)request.getAttribute("freeMaintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
   List<Map<String,Object>> maintaimHisList = (List<Map<String,Object>>)request.getAttribute("maintaimHisList"); 
%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>维修记录</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body>
<table width="100%">
		<tr><td>
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;OEM维修记录</div>
		</td></tr>
		<tr>
		   <td>
        		<table class="table_list">
		        <tr class="table_list_th"><th>维修记录</th></tr>
				<tr class="table_list_th">
					<th>序号</th>
					<th>索赔单号码</th>
					<th>维修日期</th>
					<th>经销商名称</th>
					<th>授权人</th>
					<th>审核时间</th>
					<th>授权结果</th>
					<th>行驶里程</th>
					<th>作业代码</th>
					<th>工时名称</th>
					<th>配件代码</th>
					<th>配件名称</th>
					<th>故障描述</th>
					<th>故障原因</th>
					<th>维修措施</th>
					<th>故障名称</th>
					<th>工时费</th>
					<th>材料费</th>
					<th>总申报金额</th>
					<th>状态</th>
				            </tr>
				<% if(maintaimHisList!=null && maintaimHisList.size()>0) { 
					for(int i=0;i<maintaimHisList.size();i++){ 
					Map<String,Object> tempMap = maintaimHisList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CLAIM_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_STARTDATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"NAME")%>&nbsp;</td>
						<td><% String s = CommonUtils.getDataFromMap(tempMap,"AUDITING_DATE").toString();
						       out.print(s.substring(0,s.length()-2));%>&nbsp;</td>
						<td><script>
							writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"IS_AGREE")%>)
						</script></td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"IN_MILEAGE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"WR_LABOURCODE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"WR_LABOURNAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"PART_CODE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"PART_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TROUBLE_DESC")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TROUBLE_REASON")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPAIR_METHOD")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TROUBLE_CODE_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"LABOUR_AMOUNT")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"PART_AMOUNT")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPAIR_TOTAL")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue('<%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>')&nbsp;
							</script>
						</td>
					</tr>
				<% } 
				}%>
			</table>
		</td>
		</tr>
		<tr><td>
			<table class="table_list">
			    <tr class="table_list_th"><th>保养记录</th></tr>
				<tr class="table_list_th">
					<th>序号</th>
					<th>经销商代码</th>
					<th>经销商名称</th>
					<th>工单号码</th>
					<th>日期</th>
					<th>单据类型</th>
					<th>工单状态</th>
					<th>预授权状态</th>
					<th>行驶里程</th>
					<th>保养次数</th>
					<th>变更记录</th>
					<th>废弃记录</th>
					<th style="display:none">保养费用</th>
				</tr>
				<% if(freeMaintaimHisList!=null && freeMaintaimHisList.size()>0) { 
					for(int i=0;i<freeMaintaimHisList.size();i++){ 
					Map<String,Object> tempMap = freeMaintaimHisList.get(i);	
				%>
					<tr class="<%if((i%2)==0){%>table_list_row1<%}else{ %>table_list_row2<%} %>">
						<td><%=i+1%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_CODE")%>&nbsp;</td>						
						<td><%=CommonUtils.getDataFromMap(tempMap,"DEALER_NAME")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"RO_CREATE_DATE")%>&nbsp;</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"REPAIR_TYPE_CODE")%>) ;
							</script>
						</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"RO_STATUS")%>) ;
							</script>
						</td>
						<td>
							<script>
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"REPORT_STATUS")%>);
							</script>
						</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"TOTAL_MILEAGE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FREE_TIMES")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"CHANAGE_TYPE")%>);
							</script>
						</td>
						<td>
							<% if("1".equals(CommonUtils.getDataFromMap(tempMap,"DISTORY"))){%>
								已废弃
							<%} %>
						</td>
						<td style="display:none">
							<% if("1".equals(CommonUtils.getDataFromMap(tempMap,"ISFREE"))) {%>
								<%=CommonUtils.getDataFromMap(tempMap,"FREEAMOUNT")%>&nbsp;
							<% } else {%>
								自费
							<% } %>
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
</body>
</html>