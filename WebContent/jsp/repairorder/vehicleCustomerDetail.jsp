<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
%>
<% List<Map<String,Object>> freeMaintaimHisList = (List<Map<String,Object>>)request.getAttribute("freeMaintaimHisList"); 
   String vin = (String)request.getAttribute("VIN");
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单维护</TITLE>

</HEAD>
<body>

		<div class="navigation">
			<img src="../../img/nav.gif" />
			&nbsp;售后服务管理 >车辆信息管理 >车辆车主信息明细
		</div>

		<form method="post" name="fm" id="fm">						
		
			<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td  align="left">
						VIN：
					</td>
					<td align="left">
						${detailMap.VIN }
					</td>
					<td cla align="left">
						<span class="zi">牌照号：</span>
					</td>
					<td>
					${detailMap.LICENSE_NO }
					</td>
					<td  align="left">
						<span class="zi">发动机号：</span>
					</td>
					<td align="left" >
					${detailMap.ENGINE_NO }
					</td>
				</tr>
				<tr>
					<td align="left">
						车系：
					</td>
					<td id="BRAND_NAME">
						${detailMap.CX_NAME }
					</td>
					<td  align="left">
						车型配置：
					</td>
					<td id="SERIES_NAME">${detailMap.MODEL_NAME }
					</td>
					<td class="table_edit_3Col_label_5Letter">
						
					</td>
					<td id="MODEL_NAME"></td>
				</tr>
				<tr>
					<td  align="left">
							<span class="zi">产地：</span>
						</td>
						<td>
						${detailMap.AREA_NAME }
						</td>
						<td  align="left">
							购车日期：
						</td>
						<td>
							${detailMap.SALES_DATE }
						</td>
						<td align='left'>
							生产日期:
						</td>
						<td>
							${detailMap.PRODUCT_DATE }
						</td>
				</tr>
				<tr>
					<td  align="left">
							行驶里程:
						</td>
						<td>	
							${detailMap.MILEAGE }		
						</td>
						<td  align="left">
							<span class="zi">保养次数</span>
						</td>
						<td>
							${detailMap.FREE_TIMES }		
						</td>
						<td  align="left">
							三包策略编码:
						</td>
						<td>	
							${detailMap.GAME_CODE }			
						</td>
				</tr>
		
				<th colspan="6">
					<img class="nav" src="../../img/subNav.gif" />
					车主信息
				</th>
				<tr>
				<td align="left" >是否退车：</td>
				<td  style="color: red;" colspan="3">
				<c:if test="${detailMap.IS_RETURN == 10041002}">未退车</c:if>
				<c:if test="${detailMap.IS_RETURN == 10041001}">已退车</c:if>
				</td>
				</tr>
				<c:if test="${detailMap.IS_RETURN == 10041002}">
				<tr>
					<td  align="left">
						车主名称:
					</td>
					<td align="left">
						${detailMap.CUS_NAME }			
					</td>
					<td class="zi">
						<span class="table_edit_3Col_label_5Letter">详细地址:</span>
					</td>
					<td align="left" >
						${detailMap.ADDRESS }
					</td>
					<td class="zi">
						<span class="zi">&nbsp;</span>
					</td>
					<td align="left" >
						&nbsp;
					</td>					
				</tr>
				<tr>
					<td  align="left">
						省:
					</td>
					<td>
						<script>writeRegionName(${detailMap.PROVINCE })</script>
					</td>
					<td  align="left">
						城市:
					</td>
					<td align="left" >
						<script>writeRegionName(${detailMap.CITY })</script>
					</td>
					<td class="zi">
						<span class="zi">&nbsp;</span>
					</td>
					<td align="left" >
						&nbsp;
					</td>
				</tr>
				</c:if>
				
			</table>	
			
			<table class="table_list">
				<tr class="table_list_th">
					<th>序号</th>
					<th>经销商代码</th>
					<th>经销商名称</th>
					<th>工单号码</th>
					<th>开工单日期</th>
					<th>单据类型</th>
					<th>工单状态</th>
					<th>行驶里程</th>
					<th>保养次数</th>
					<th>预授权单号</th>
					<th>预授权审核人</th>
					<th>审核状态</th>
					<th>审核时间</th>
					<th>索赔单号</th>
					<th>索赔单上报时间</th>
					<th>索赔单号状态</th>
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
						<td><%=CommonUtils.getDataFromMap(tempMap,"IN_MILEAGE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FREE_TIMES")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"FO_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"AUDIT_PERSON")%>&nbsp;</td>
						<td>
							<script>
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"AUDIT_RESULT")%>);
							</script>
						</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"AUDIT_DATE")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"CLAIM_NO")%>&nbsp;</td>
						<td><%=CommonUtils.getDataFromMap(tempMap,"REPORT_DATE")%>&nbsp;</td>
						<td>
							<script type="text/javascript">
								writeItemValue(<%=CommonUtils.getDataFromMap(tempMap,"STATUS")%>) ;
							</script>
						</td>
					</tr>
				<% } 
				}%>
			</table> 
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td align="center">
					   
						<input type="button" onClick="_hide();"  class="normal_btn" id="backId" style="" value="关闭" />
					</td>
				</tr>
			</table>
			

		</form>
		
	</BODY>

</html>