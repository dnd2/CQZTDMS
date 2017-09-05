<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>市场活动总结费用申请(经销商)</title>
    <%
        String contextPath = request.getContextPath();
        List dlrSummeryAttachList = (List) request.getAttribute("dlrSummeryAttachList");
    %>

    <script type="text/javascript">
        function isDisplayChngInfo(arg){
            if(arg==13271001){
                document.getElementById("chngInfoTab").style.display="";
            }else if(arg==13271002){
                    document.getElementById("chngInfoTab").style.display="none";
            }
        }
        function doInit() {
            loadcalendar();
            isDisplayChngInfo(${repPO.chngType});
        }
        function goBack() {
            document.fm.action = '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostApply/doInit.do';
            document.fm.submit();
        }
    </script>

</head>
<body>
<div class="navigation">
    <img src="<%=request.getContextPath()%>/img/nav.gif"/>
    &nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动总结费用申请(经销商)
</div>
<form method="post" name="fm" id="fm">
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;上级下发
</div>
<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_list">
    <tr>
        <th>
            经销商编码
        </th>
        <th>
            经销商名称
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
    </tr>
    <tbody>

    <tr class="table_list_row">
        <td>
            ${tdPO.dealerCode}
        </td>
        <td>
            ${tdPO.dealerShortname}
        </td>
        <td>
            <script>
                document.write(getItemValue(${planPO.planType}));
            </script>
        </td>
        <td>
            <input type="text" decimal="0"
                   value="${planPO.projectName}" size="5" readonly="true">
        </td>
        <td>
            <input type="text" value="${executionTimeB}" size="10" readonly="true">
        </td>
        <td>
            <input type="text" value="${executionTimeE}" size="10" readonly="true">
        </td>
        <td>
            <input type="text" size="5" value="${planPO.allCost}" readonly="true">
        </td>
        <td>
            <input type="text" size="5" value="${planPO.companyCost}" readonly="true">
        </td>
        <td>
            <script>
                document.write(getItemValue(${planPO.costType}));
            </script>
        </td>
        <td>
            <input type="text" size="5"  value="${planPO.toPlaceCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5"  value="${planPO.toTelStoreCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5"  value="${planPO.createCardsCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5"  value="${planPO.orderCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5"  value="${planPO.turnCarCount}" readonly="true">
        </td>
    </tr>
    </tbody>
</table>
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;活动总结汇总
</div>
<table  width=100% border="0" align="center"
       cellpadding="1" cellspacing="1" class="table_query">
    <tr>
        <td align="right">
            费用类型：
        </td>
        <td>
            <script>
                document.write(getItemValue(${summeryPO.summeryCostType}));
            </script>
        </td>
        <td width="60%"></td>
    </tr>
</table>
			<table id="chngInfoTab" width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
					<th>
						经销商编码
					</th>
					<th>
						经销商名称
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
						费用处理方式
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
                        实际执行总费用
                    </th>
                    <th>
                        审核支持费用
                    </th>
				<tbody>
					<tr class="table_list_row">
						<td>
                            ${tdPO.dealerCode}
						</td>
						<td>
                            ${tdPO.dealerShortname}
						</td>
						<td>
							<script>
                                    document.write(getItemValue(${summeryPO.planType}));
						    </script>
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_textarea,50"
								value="${summeryPO.projectName}"  size="5"
								id="projectName"
								name="projectName" readonly="true">
						</td>
						<td>
							<input type="text" group="executionTimeB,executionTimeE"
								datatype="2,is_date,10" size="10"
								id="executionTimeB"
								name="executionTimeB" value="${chngExecutionTimeB}"
                                readonly="true" >
						</td>
						<td>
							<input type="text" group="executionTimeB,executionTimeE"
								datatype="2,is_date,10"  size="10"
								id="executionTimeE"
								name="executionTimeE" value="${chngExecutionTimeE}"
                                readonly="true">
						</td>
						<td>
							<script>
                                document.write(getItemValue(${summeryPO.costType}));
						    </script>
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.toPlaceCount}"
								 size="5"
								id="toPlaceCount"
								name="toPlaceCount" readonly="true">
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.toTelStoreCount}"
								 size="5"
								id="toTelStoreCount"
								name="toTelStoreCount" readonly="true">
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.createCardsCount}"
								 size="5"
								id="createCardsCount"
								name="createCardsCount" readonly="true">
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.orderCount}"
								size="5" id="orderCount"
								name="orderCount" readonly="true">
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.turnCarCount}"
								sumtag="turnCarCount" size="5" id="turnCarCount"
								name="turnCarCount" readonly="true">
						</td>
						<td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.applyCost}"
								 size="5" id="applyCost"
								name="applyCost" readonly="true">
						</td>
                        <td>
							<input type="text" decimal="2" datatype="2,is_double,8" value="${summeryPO.submitCost}"
								 size="5" id="submitCost"
								name="submitCost" readonly="true">
						</td>
					</tr>
				</tbody>
			</table>
			<table class="table_query">
                <tr>
                    <td align="right">备注:</td>
                    <td>
                        <textarea cols="50" rows="5" id="remark" name="remark" readonly="true">${summeryPO.remark}</textarea>
                    </td>
                </tr>
            </table>
<!-- 添加附件start -->
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;总结附件
</div>
<table class="table_info" border="0">
    <tr>
        <th>
            附件列表：
            <input type="hidden"/>
        </th>
    </tr>
</table>
<table class="table_info">
    <%
        if (dlrSummeryAttachList != null && dlrSummeryAttachList.size() != 0) {
    %>
    <c:forEach items="${dlrSummeryAttachList}" var="attls">
        <tr class="table_list_row1" id="${attls.FJID}">
            <td>
                <a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
            </td>
        </tr>
    </c:forEach>
    <%
        }
    %>
</table>
<!-- 添加附件end -->

<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;审批记录
</div>
<table  width=100% border="0" align="center" cellpadding="1"
    cellspacing="1" class="table_list">
        <th>
            审核组织
        </th>
        <th>
            审核人
        </th>
        <th>
            审核日期
        </th>
        <th>
            审核结果
        </th>
        <th>
            审核支持费用
        </th>
        <th>
            审核意见
        </th>
        <tbody>
            <c:forEach items="${checkRecords}" var="list">
                <tr class="table_list_row">
                    <td>
                       ${list.ORG_NAME}
                    </td>
                    <td>
                       ${list.NAME}
                    </td>
                    <td>
                       ${list.CHECK_DATE}
                    </td>
                    <td>
                        <script>
                            document.write(getItemValue(${list.CHECK_STATUS}));
                       </script>
                    </td>
                    <td>
                        ${list.SUBMIT_COST}
                    </td>
                    <td>
                        <a href="#" onclick="OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostApply/openCheckDesc.do?chkId='+'${list.CHECK_ID}',600,400)">
                            查看
                        </a>
                    </td>
                </tr>
        </c:forEach>
        </tbody>
</table>
<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_query">
    <tr>
        <td colspan="4" align="center">
            <input type="hidden" name="campaignId" value="${campaignId}"/>
            <input type="hidden" name="spaceId" value="${spaceId}"/>
            <input type="hidden" name="planId" value="${planPO.planId}"/>
            <input type="hidden" name="subm" value=""/>
            <input type="button" class="cssbutton" name="button4"
                   onClick="goBack();" value="返回"/>
        </td>
    </tr>
</table>
</form>
</body>
</html>