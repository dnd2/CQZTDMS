<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="java.util.*"%>
    <%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>市场活动反馈内容打印</title>
		<%
			String contextPath = request.getContextPath();
			List provinceAttachList = (List) request.getAttribute("provinceAttachList");
			List areaAttachList = (List) request.getAttribute("areaAttachList");
		%>
	</head>
	<body>
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />
			&nbsp;当前位置： 市场活动管理 &gt; 市场活动管理 &gt; 市场活动查询
		</div>
		<form method="post" name="fm" id="fm">
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
					
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动名称：
					</td>
					<td>
					${ttcp.activityName}
						
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动类型：
					</td>
					<td colspan="4">
					<script type="text/javascript">writeItemValue(${ttcp.activityType})</script>
					</td>
					<tr>
					<td class="table_query_2Col_label_6Letter">
						负责人：
					</td>
					<td>
					${ttcp.chargeMan}
					</td>
					<td class="table_query_2Col_label_6Letter">
						月份：
					</td>
					<td colspan="4">
					${ttcp.activityMonth} 
					</td>
				</tr>
			
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动车型：
					</td>
					<td>
					${serialName}
					</td>
					<td class="table_query_2Col_label_6Letter">
						财务处理方式：
					</td>
					<td colspan="4">
					<script type="text/javascript">writeItemValue(${ttcp.costType} )</script>
					</td>
				</tr>
				
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动总费用预估(含税、万元)：
					</td>
					<td>
					${ttcp.totalPre}
					</td>
					<td class="table_query_2Col_label_5Letter">
						公司支持费用(万元)：
					</td>
					<td>
					${ttcp.totalSupport}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动开始日期：
					</td>
					<td>
					${startDate}
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动结束日期：
					</td>
					<td>
					${endDate}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动主题：
					</td>
					<td colspan="4">
					${ttcp.activityTheme}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						项目执行方：
					</td>
					<td colspan="4">
					${makerCode}
					</td>
					</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动备注：
					</td>
					<td colspan="4">
					${ttcp.activityRemark}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						议价金额(含税、万元)：
					</td>
					<td>
						${ttcp.suggestMoney}
					</td>
				<td class="table_query_2Col_label_6Letter">
						支持比例：
					</td>
					<td>
					${ttcp.supportRatio}
					</td>
				</tr>
			</table>
			
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;目标
			</div>
			<c:forEach items="${list1}" var="item">
			<table class="table_query" align="center" border="0" cellpadding="1"
				cellspacing="1" width="100%" id="dealerTable1">
				<tbody>

					<tr class="table_list_row2">
						<td align="center" width="10%">
							项目
						</td>
						<td align="center" width="20%">
							目标
						</td>
						<td align="center" width="20%">
							实际
						</td>
						<td align="center" width="10%">
							完成率
						</td>
						<td align="center" width="20%">
							原因分析
						</td>
						<td align="center" width="20%">
							改进措施
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							首客
						</td>
						<td align="center" id="aim1">
							${ttcp.totalFclient}
						</td>
						<td align="center">
							${ttcp.actTotalFclient}
						</td>
						<td align="center" id="rate1">
							${item.FRATE}
						</td>
						<td align="center" rowspan="4">
							${ttcp.reasonAnaly}
						</td>
						<td align="center" rowspan="4">
							${ttcp.suggestTip}
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							混客
						</td>
						<td align="center" id="aim2">
							${ttcp.totalMclient}
						</td>
						<td align="center">
							${ttcp.actTotalMclient}
						</td>
						<td align="center" id="rate2">
							${item.MXRATE}
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							建卡
						</td>
						<td align="center" id="aim3">
							${ttcp.totalAimcard}
						</td>
						<td align="center">
							${ttcp.actTotalAimcard}
						</td>
						<td align="center" id="rate3">
							${item.CARDRATE}
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							订单
						</td>
						<td align="center" id="aim4">
							${ttcp.totalAimorder}
						</td>
						<td align="center">
							${ttcp.actTotalAimorder}
						</td>
						<td align="center" id="rate4">
							${item.ORDERRATE}
						</td>
					</tr>
				</tbody>
			</table>
				</c:forEach >
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;活动范围
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
				<tr>
					<th colspan="15" align="left">
						<input type="hidden" name="orgId" size="15" value="" id="orgId"
							class="middle_txt" datatype="1,is_noquotation,500" />
						<input type="hidden" name="dealerCode" size="15" value=""
							id="dealerCode" class="middle_txt" datatype="1,is_noquotation,500" />
					</th>
				<tr>
					<th>
						经销商编码
					</th>
					<th>
						经销商名称
					</th>
					<th>
						首客目标
					</th>
					<th>
						实际首客
					</th>
                    <th>
						混客目标
					</th>
					 <th>
						实际混客
					</th>
					<th>
						建卡目标
					</th>
					<th>
						实际建卡
					</th>
					<th>
						订单目标
					</th>
					<th>
						实际订单
					</th>
				</tr>
				<tbody id="dealerTable">
					<c:forEach items="${list2}" var="item">
					<tr class="table_list_row" id="${item.DEALER_ID}">
						<td>
							${item.DEALER_CODE}
						</td>
						<td>
							${item.DEALER_SHORTNAME}
						</td>
						<td>
						${item.FCLIENT}
						</td>
						<td>
						${item.ACT_FCLIENT}
						</td>
						<td>
						${item.MIXCLIENT}
						</td>
						<td>
								${item.ACT_MIXCLIENT}
						</td>
						<td>
						${item.AIMCARD}
						</td>
						<td>
						${item.ACT_AIMCARD}
						</td>
						<td>
						${item.AIMORDER}
						</td>
						<td>
							${item.ACT_AIMORDER}
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div>
                <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
                &nbsp;审核记录
            </div>
			    <table class="table_list">
		<tr align="center" class="tabletitle">
			<th>审核日期</th>
			<th>审核单位</th>
			<th>审核人</th>
			<th>审核结果</th>
			<th>审核描述</th>
		</tr>
		<c:forEach items="${list3}" var="list3" varStatus="vstatus1">
			<tr align="center" class="table_list_row1">
				<td><c:out value="${list3.CHECK_DATE}"/></td>
				<td><c:out value="${list3.ORG_NAME}"/></td>
				<td><c:out value="${list3.USER_NAME}"/></td>
				<td><c:out value="${list3.CHECK_STATUS}"/></td>
				<td><c:out value="${list3.CHECK_DESC}"/></td>
			</tr>
		</c:forEach> 
	</table>
            <!-- 添加附件start -->
            <div>
                <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
                &nbsp;活动附件
            </div>
            <table class="table_info" border="0" id="file">
                <tr>
                    <th>
                        附件列表：
                        <input type="hidden" id="fjids" name="fjids" />
<!--                        <span> <input type="button" class="cssbutton"-->
<!--                                onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>-->
<!--                    </th>-->
                </tr>
                <tr>
                    <td width="100%" colspan="2"><jsp:include
                            page="${contextPath}/uploadDiv.jsp" /></td>
                </tr>
            </table>
            <table id="attachTab" class="table_info">
                <%
                    if (provinceAttachList != null && provinceAttachList.size() != 0) {
                %>
                <c:forEach items="${provinceAttachList}" var="attls">
                    <tr class="table_list_row1" id="${attls.FJID}">
                        <td>
                            <a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
                        </td>
                        <td>
<!--                            <input type=button onclick="delAttach('${attls.FJID}')"-->
<!--                                class="normal_btn" value="删 除" />-->
<!--                            <input type="hidden" name="delAttachs" id="delAttachs" value="" />-->
                        </td>
                    </tr>
                </c:forEach>
                <%
                    }
                %>
            </table>
            <!-- 添加附件end -->
		<div>&nbsp;</div>
	<table width=99% border="0" align="center" cellpadding="1" cellspacing="1"  style="background-color: white;" class="table_query" >
		<tr align="center">
			<td >
			<input class="cssbutton   noprint" name="button2" type="button" onclick="history.back();"  value ="返回" />
			</td>
		</tr>
	</table>
		</form>
	</body>
</html>