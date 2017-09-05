<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动方案下发</title>
<% String contextPath = request.getContextPath(); 
   List  executePlans = (List)request.getAttribute("executePlans");
   List  attachList   = (List)request.getAttribute("attachList");
%>
<script type="text/javascript" src="getJs.do?fileName=activitiesPlanMakeUpdate"></script>

</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动意向维护(车厂)</div>
<form method="post" name="fm" id="fm">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;活动信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">活动车型：</td>
			<td>
						<input type="hidden" class="middle_txt" name="campaignModel"
							id="campaignModel" readonly="readonly" datatype="0,is_null,1000"
							size="30" />
                        <input type="text" class="middle_txt" name="groupName"
							id="groupName" datatype="0,is_null,1000" readonly="readonly"
							size="30" value="${compaignMap.GROUPNAME}"/>
				<input type="hidden" name="modelId" id="modelId" value="<c:out value="${compaignMap.MODELID}"/>"/>
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialCarType_market('groupCode','','true','')" value="..." />
				<input type="button" name="clearbutton" id="clearbutton" class="cssbutton" value="清除" onClick="toClear('modelId','campaignModel','groupName');"/>
				<input type="hidden" name="area_id" id="area_id" value="" />
			</td>
			<td class="table_query_2Col_label_6Letter"></td>
			<td>
				
			</td>
			
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动编号：</td>
			<td>
				<input type="text" name="campaignNo" id="campaignNo" value="<c:out value="${compaignMap.CAMPAIGN_NO}"/>" datatype="0,is_digit_letter,30" readonly="readonly"/>
			</td>
			<td class="table_query_2Col_label_6Letter">活动名称：</td>
			<td>
				<input type="text" name="campaignName" id="campaignName" value="<c:out value="${compaignMap.CAMPAIGN_NAME}"/>" datatype="0,is_digit_letter_cn,50" size="30" maxlength="50"/>
			</td>
			
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动开始日期：</td>
			<td>
				<input class="short_txt"  type="text" id="startDate" name="startDate" value="<c:out value="${compaignMap.START_DATE}"/>" group="startDate,endDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
			</td>
			<td class="table_query_2Col_label_6Letter">活动结束日期：</td>
			<td>
				<input class="short_txt"  type="text" id="endDate" name="endDate" value="<c:out value="${compaignMap.END_DATE}"/>" group="startDate,endDate" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动主题：</td>
			<td colspan="4">
				<input type="text" name="campaignSubject" id="campaignSubject" value="<c:out value="${compaignMap.CAMPAIGN_SUBJECT}"/>" datatype="1,is_digit_letter_cn,50" size="90" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动对象：</td>
			<td colspan="4">
				<input type="text" name="campaignObject" id="campaignObject" value="<c:out value="${compaignMap.CAMPAIGN_OBJECT}"/>" size="90" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动目的：</td>
			<td colspan="4">
				<textarea name="campaignPurpose" id="campaignPurpose"  rows="4" cols="70"><c:out value="${compaignMap.CAMPAIGN_PURPOSE}"/></textarea>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动要求：</td>
			<td colspan="4">
				<textarea name="campaignNeed" id="campaignNeed" rows="4" cols="70"><c:out value="${compaignMap.CAMPAIGN_NEED}"/></textarea>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">活动主要内容：</td>
			<td colspan="4">
				<textarea name="campaignDesc" id="campaignDesc" rows="4" cols="70"><c:out value="${compaignMap.CAMPAIGN_DESC}"/></textarea>
			</td>
		</tr>
	</table>
	<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;目标
			</div>
			<table class="table_query" align="center" border="0" cellpadding="1"
				cellspacing="1" width="100%">
				<tbody>
					<tr class="table_list_row2">
						<td align="center">
							项目
						</td>
						<td align="center">
							目标
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							来场客流数
						</td>
						<td align="center">
							<input name="toPlaceCount" id="toPlaceCount" type="text" value="${compaignMap.TO_PLACE_COUNT}" datatype="0,is_double,8" decimal="0" readonly="readonly"/>
						</td>
					</tr>
                    <tr class="table_list_row2">
						<td align="center">
							来电来店数
						</td>
						<td align="center">
							<input name="toTelStoreCount" id="toTelStoreCount" type="text" value="${compaignMap.TO_TEL_STORE_COUNT}" datatype="0,is_double,8" decimal="0" readonly="readonly"/>
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							建卡数
						</td>
						<td align="center">
							<input name="createCardsCount" id="createCardsCount" type="text" value="${compaignMap.CREATE_CARDS_COUNT}" datatype="0,is_double,8" decimal="0" readonly="readonly"/>
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							订单数
						</td>
						<td align="center">
							<input name="orderCount" id="orderCount" type="text" value="${compaignMap.ORDER_COUNT}" datatype="0,is_double,8" decimal="0" readonly="readonly"/>
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							交车数
						</td>
						<td align="center">
							<input name="turnCarCount" id="turnCarCount" type="text" value="${compaignMap.TURN_CAR_COUNT}" datatype="0,is_double,8" decimal="0" readonly="readonly"/>
						</td>
					</tr>
				</tbody>
			</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;活动范围</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_list">
		<tr>
			<th colspan="8" align="left">
				<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,500" />
			<input type="hidden"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,500" />
		<input name="orgbu"  id="orgbu" type="button" class="cssbutton" onclick="showAllOrgs2('orgCode','orgId','true','','')" value="新增" />
			</th>
		</tr>
		<tr>
			<th>
				区域编码
			</th>
			<th>
				区域
			</th>
			<th>
				来场客流数
			</th>
            <th>
				来电来店数
			</th>
			<th>
				建卡数
			</th>
			<th>
				订单数
			</th>
			<th>
				交车数
			</th>
			<th>
				操作
			</th>
		</tr>
		<tbody id="dealerTable">
		<% if(executePlans!=null&&executePlans.size()!=0){ %>
    	<c:forEach items="${executePlans}" var="epList">
       		<tr class="table_list_row" id="<c:out value="${epList.ORG_ID}"/>">
            	<td><c:out value="${epList.ORG_CODE}"/></td>
            	<td><c:out value="${epList.ORG_NAME}"/><input type="hidden" name="dlrIds" value="${epList.ORG_ID}"></input></td>
	            <td><input onchange='calCount(this)' name="toPlaceCount${epList.ORG_ID}" id="toPlaceCount${epList.ORG_ID}" size="5" type="text" value="${epList.TO_PLACE_COUNT}" datatype="0,is_double,8" decimal="0"/></td>
	            <td><input onchange='calCount(this)' name="toTelStoreCount${epList.ORG_ID}" id="toTelStoreCount${epList.ORG_ID}" size="5" type="text" value="${epList.TO_TEL_STORE_COUNT}" datatype="0,is_double,8" decimal="0"/></td>
	            <td><input onchange='calCount(this)' name="createCardsCount${epList.ORG_ID}" id="createCardsCount${epList.ORG_ID}" size="5" type="text" value="${epList.CREATE_CARDS_COUNT}" datatype="0,is_double,8" decimal="0"/></td>
	            <td><input onchange='calCount(this)' name="orderCount${epList.ORG_ID}" id="orderCount${epList.ORG_ID}" size="5" type="text" value="${epList.ORDER_COUNT}" datatype="0,is_double,8" decimal="0"/></td>
	            <td><input onchange='calCount(this)' name="turnCarCount${epList.ORG_ID}" id="turnCarCount${epList.ORG_ID}" size="5" type="text" value="${epList.TURN_CAR_COUNT}" datatype="0,is_double,8" decimal="0"/></td>
	            
	            <td><input type='button' class='normal_btn' onclick='delRow(<c:out value="${epList.ORG_ID}"/>);calCount(this)' value='删除'/></td>
           	 	<td id="1"></td>
           	 	
        	</tr>
    	</c:forEach>
       <%} %>
    	</tbody>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;附件</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">
		<tr>
	        <th colspan="3" align="left">附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<table id="attachTab" class="table_info">
  		<% if(attachList!=null&&attachList.size()!=0){ %>
  		<c:forEach items="${attachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" id="campaignId" value="${campaignId}"/>
				<input type="hidden" name="delDlrIds" id="delDlrIds" value=""/>
				<input type="hidden" name="delAttachs" id="delAttachs" value=""/>
				<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
				<input type="button" class="cssbutton" name="saveBtn" onClick="saveModify();" value="保存"/>
				<input type="button" class="cssbutton" name="assignBtn" onClick="toAssign();" value="下发"/>
				<input type="button" class="cssbutton" name="backBtn" onClick="history.back();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>