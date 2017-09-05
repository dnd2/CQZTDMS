<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="com.infodms.dms.common.Constant"%>
	<%@ page import="java.util.*;"%>
	<%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>活动执行方案提报</title>
		<%
			String contextPath = request.getContextPath();
			List attachList = (List) request.getAttribute("attachList");
		%>

		<script type="text/javascript"
			src="getJs.do?fileName=activitiesPlanAreaCheckDetail"></script>

	</head>
	<body>
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />
			&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 活动执行方案提报(区域)
		</div>
		<form method="post" name="fm" id="fm">
			<input type="hidden" value="${amountList1}" id="amountList1"
				name="amountList1"></input>
			<input type="hidden" value="${amountList2}" id="amountList2"
				name="amountList2"></input>
			<input type="hidden" value="${amountList3}" id="amountList3"
				name="amountList3"></input>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;业务范围
			</div>
			<table class="table_query" align="center">
				<tr class="tabletitle">
					<th align="right" width="10%">
						选择业务范围：
					</th>
					<th align="left">
						<select name="areaId__A" disabled="disabled">
							<c:forEach items="${areaList}" var="po">
								<c:if test="${po.AREA_ID == areaId}">
									<option value="${po.AREA_ID}">
										${po.AREA_NAME}
									</option>
								</c:if>
							</c:forEach>
						</select>
						<input type="hidden" name="areaId" id="area_id" value="${areaId }" />
					</th>
				</tr>
			</table>

			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;方案信息
			</div>
			<table id="table1" width=100% border="0" align="center"
				cellpadding="1" cellspacing="1" class="table_query">
				<tr>
					<th colspan="5" align="left">
						&nbsp;方案录入
					</th>
				</tr>
				<tr>
					<td align="right">
						品牌：
					</td>
					<td align="left">
						<select name="groupId" id="groupId" readonly="readonly"
							class="short_sel" onchange="toClear();">
							<c:forEach items="${list}" var="list">
								<option value="${list.GROUP_ID}"
									<c:if test="${list.GROUP_ID==map2.GROUP_ID}">selected</c:if>>
									${list.GROUP_NAME}
								</option>
							</c:forEach>
						</select>
					</td>
					<td align="right">
						活动车型：
					</td>
					<td align="left">
						<input type="text" name="campaignModel" id="campaignModel"
							readonly="readonly" datatype="0,is_null,1000" size="30"
							value="${modelName}" />
					</td>
					<td width="20%"></td>
				</tr>
				<tr>
					<td align="right">
						车厂方案编号：
					</td>
					<td align="left">
						<input type="text" name="campaignNo" id="campaignNo"
							value="${map2.CAMPAIGN_NO}" datatype="0,is_digit_letter,17"
							readonly="readonly" />
					</td>
					<td align="right">
						方案名称：
					</td>
					<td align="left">
						<input type="text" name="campaignName" id="campaignName"
							readonly="readonly" value="${map2.CAMPAIGN_NAME}"
							datatype="0,is_textarea,100" size="30" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right">
						计划开始日期：
					</td>
					<td align="left">
						<input class="short_txt" type="text" readonly="readonly"
							id="startDate" name="startDate" value="${map2.START_DATE}"
							group="startDate,endDate" datatype="0,is_date,10" />
					</td>
					<td align="right">
						计划结束日期：
					</td>
					<td align="left">
						<input class="short_txt" type="text" readonly="readonly"
							id="endDate" name="endDate" value="${map2.END_DATE}"
							group="startDate,endDate" datatype="0,is_date,10" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right">
						活动主题：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignSubject" id="campaignSubject"
							readonly="readonly" value="${map2.CAMPAIGN_SUBJECT}"
							datatype="1,is_textarea,100" size="90" />
					</td>
				</tr>
				<tr>
					<td align="right">
						活动对象：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignObject" id="campaignObject"
							readonly="readonly" value="${map2.CAMPAIGN_OBJECT}" size="90"
							datatype="1,is_textarea,100">
					</td>
				</tr>
				<tr>
					<td align="right">
						活动目的：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignPurpose" id="campaignPurpose"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${map2.CAMPAIGN_PURPOSE}"></input>
					</td>
				</tr>
				<tr>
					<td align="right">
						活动要求：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignNeed" id="campaignNeed"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${map2.CAMPAIGN_NEED}"></input>
					</td>
				</tr>
				<tr>
					<td align="right">
						活动主要内容：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignDesc" id="campaignDesc"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${map2.CAMPAIGN_DESC}"></input>
					</td>
				</tr>
			</table>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;执行方案信息
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
				<tr>
					<th colspan="5" align="left">
						&nbsp;目标录入
					</th>
				</tr>
				<tr>
					<td align="center">
						项目
					</td>
					<td align="center">
						目标
					</td>
				</tr>
				<tr class="table_list_row2">
					<td align="center">
						来电来店数
					</td>
					<td align="center">
						<input type="text" name="callsHousesCntTgt" id="callsHousesCntTgt"
							readonly="readonly" datatype=”1,is_digit,6”
							value="${map2.TO_TEL_STORE_COUNT}" />
						<font color="red">*</font>
					</td>
				</tr>
				<tr class="table_list_row2">
					<td align="center">
						建卡数
					</td>
					<td align="center">
						<input type="text" name="reserveCntTgt" id="reserveCntTgt"
							readonly="readonly" datatype=”1,is_digit,6”
							value="${map2.CREATE_CARDS_COUNT}" />
						<font color="red">*</font>
					</td>
				</tr>
				<tr class="table_list_row2">
					<td align="center">
						订单数
					</td>
					<td align="center">
						<input type="text" name="orderCntTgt" id="orderCntTgt"
							readonly="readonly" datatype=”1,is_digit,6”
							value="${map2.ORDER_COUNT}" />
						<font color="red">*</font>
					</td>
				</tr>
				<tr class="table_list_row2">
					<td align="center">
						交车数
					</td>
					<td align="center">
						<input type="text" name="deliveryCntTgt" id="deliveryCntTgt"
							readonly="readonly" datatype=”1,is_digit,6”
							value="${map2.TURN_CAR_COUNT}" />
						<font color="red">*</font>
					</td>
				</tr>
			</table>
			<!-- 添加附件start -->
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;方案附件
			</div>
			<table class="table_info" border="0" id="file">
				<tr>
					<th>
						附件列表：
						<input type="hidden" id="fjids" name="fjids" />
						<span> <input type="button" class="cssbutton"
								onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="2"><jsp:include
							page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
			</table>
			<table id="attachTab" class="table_info">
				<%
					if (attachList != null && attachList.size() != 0) {
				%>
				<c:forEach items="${attachList}" var="attls">
					<tr class="table_list_row1" id="${attls.FJID}">
						<td>
							<a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
						</td>
						<td>
							<input type=button onclick="delAttach('${attls.FJID}')"
								class="normal_btn" value="删 除" />
							<input type="hidden" name="delAttachs" id="delAttachs" value="" />
						</td>
					</tr>
				</c:forEach>
				<%
					}
				%>
			</table>
			<!-- 添加附件end -->
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;活动方案下发范围
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
				<tr>
					<th colspan="14" align="left">
						活动方案下发范围
						<input type="hidden" name="orgId" size="15" value="" id="orgId"
							class="middle_txt" datatype="1,is_noquotation,500" />
						<input type="hidden" name="orgCode" size="15" value=""
							id="orgCode" class="middle_txt" datatype="1,is_noquotation,500" />
						<input name="orgbu" id="orgbu" type="button" class="cssbutton"
							onclick="showAllOrgs('orgId','orgCode','true','','${areaId}')"
							value="新增" />
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
						活动形式
					</th>
					<th>
						项目名称
					</th>
					<th colspan="2">
						执行时间
					</th>
					<th>
						总费用
					</th>
					<th>
						公司支持
					</th>
					<th>
						费用处理方式
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
					<c:forEach items="${list2}" var="item">
					<tr class="table_list_row" id="${item.ORG_ID}">
						<td>
							${item.ORG_CODE}
						</td>
						<td>
							${item.ORG_NAME}
							<input type="hidden" value="${item.ORG_ID}"
								id="${item.ORG_ID}" name="dlrIds">
						</td>
						<td>
							<script>
						    		genSelBoxExp("planType" + ${item.ORG_ID}, 1160, "${item.PLAN_TYPE}", true, "", '', "false", '')
						    </script>
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_textarea,50"
								value="${item.PROJECT_NAME}" sumtag="projectName" size="5"
								id="projectName${item.ORG_ID}"
								name="projectName${item.ORG_ID}">
						</td>
						<td>
							<input type="text" group="executionTimeB,executionTimeE"
								datatype="0,is_date,10" sumtag="executionTimeB" size="10"
								id="executionTimeB${item.ORG_ID}"
								name="executionTimeB${item.ORG_ID}" value="${item.EXECUTION_TIME_B}">
							<input type="button" value="&nbsp;"
								onclick="showcalendar(event, 'executionTimeB${item.ORG_ID}', false);"
								class="time_ico">
						</td>
						<td>
							<input type="text" group="executionTimeB,executionTimeE"
								datatype="0,is_date,10" sumtag="executionTimeE" size="10"
								id="executionTimeE${item.ORG_ID}"
								name="executionTimeE${item.ORG_ID}" value="${item.EXECUTION_TIME_E}">
							<input type="button" value="&nbsp;"
								onclick="showcalendar(event, 'executionTimeE${item.ORG_ID}', false);"
								class="time_ico">
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.ALL_COST}"
								sumtag="allCost" size="5" id="allCost${item.ORG_ID}"
								name="allCost${item.ORG_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.COMPANY_COST}"
								sumtag="companyCost" size="5" id="companyCost${item.ORG_ID}"
								name="companyCost${item.ORG_ID}">
						</td>
						<td>
							<script>
						    		genSelBoxExp("costType" + ${item.ORG_ID}, 3004, "${item.COST_TYPE}", true, "", '', "false", '')
						    </script>
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.TO_TEL_STORE_COUNT}"
								sumtag="toTelStoreCount" size="5"
								id="toTelStoreCount${item.ORG_ID}"
								name="toTelStoreCount${item.ORG_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.CREATE_CARDS_COUNT}"
								sumtag="createCardsCount" size="5"
								id="createCardsCount${item.ORG_ID}"
								name="createCardsCount${item.ORG_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.ORDER_COUNT}"
								sumtag="orderCount" size="5" id="orderCount${item.ORG_ID}"
								name="orderCount${item.ORG_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="0,is_double,8" value="${item.TURN_CAR_COUNT}"
								sumtag="turnCarCount" size="5" id="turnCarCount${item.ORG_ID}"
								name="turnCarCount${item.ORG_ID}">
						</td>
						<td>
							<input type="button" value="删除"
								onclick="delRow(${item.ORG_ID})" class="normal_btn">
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;申请操作
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
				<tr>
					<td align="right">
						申请意见：
					</td>
					<td colspan="4" align="left">
						<textarea name="remark" id="remark" datatype="0,is_textarea,300"
							rows="4" cols="70">${map2.REMARK}</textarea>
					</td>
				</tr>
			</table>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
				<tr>
					<td colspan="4" align="center">
						<input type="hidden" name="campaignId" value="${map2.CAMPAIGN_ID}" />
						<input type="hidden" name="executeId" value="${map2.EXECUTE_ID}" />
						
						<input type="hidden" name="subm" id="subm" value="" />
						<input type="button" class="cssbutton" name="button6"
							onClick="userValidate('1');" value="通过" />
						<input type="button" class="cssbutton" name="button5"
							onClick="userValidate('0');" value="驳回" />
						<input type="button" class="cssbutton" name="button4"
							onClick="toBack();" value="返回" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>