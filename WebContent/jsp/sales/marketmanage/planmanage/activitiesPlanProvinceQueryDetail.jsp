<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>市场活动方案查询（省份）修改（查询方案）</title>
    <%
        String contextPath = request.getContextPath();
        List provinceAttachList = (List) request.getAttribute("provinceAttachList");
        List areaAttachList = (List) request.getAttribute("areaAttachList");
    %>

    <script type="text/javascript">
        function doInit() {
            loadcalendar();
        }
        function goBack() {
            document.fm.action = '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanProvinceQuery/doInit.do';
            document.fm.submit();
        }
    </script>

</head>
<body>
<div class="navigation">
    <img src="<%=request.getContextPath()%>/img/nav.gif"/>
    &nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动方案查询(省系)
</div>
<form method="post" name="fm" id="fm">
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;活动信息
</div>
<table id="table1" width=100% border="0" align="center"
       cellpadding="1" cellspacing="1" class="table_query">
    <tr>
        <td align="right">
            活动车型：
        </td>
        <td align="left">
            <input type="text" name="campaignModel" id="campaignModel"
                   readonly="readonly" datatype="0,is_null,1000" size="30"
                   value="${modelName}"/>
        </td>
        <td width="20%"></td>
    </tr>
    <tr>
        <td align="right">
            车厂活动编号：
        </td>
        <td align="left">
            <input type="text" name="campaignNo" id="campaignNo"
                   value="${ttCampaignPO.campaignNo}" datatype="0,is_digit_letter,17"
                   readonly="readonly"/>
        </td>
        <td align="right">
            活动名称：
        </td>
        <td align="left">
            <input type="text" name="campaignName" id="campaignName"
                   readonly="readonly" value="${ttCampaignPO.campaignName}"
                   datatype="0,is_textarea,100" size="30"/>
        </td>
        <td></td>
    </tr>
    <tr>
        <td align="right">
            活动开始日期：
        </td>
        <td align="left">
            <input class="short_txt" type="text" readonly="readonly"
                   id="startDate" name="startDate" value="${startDate}"
                   group="startDate,endDate" datatype="0,is_date,10"/>
        </td>
        <td align="right">
            活动结束日期：
        </td>
        <td align="left">
            <input class="short_txt" type="text" readonly="readonly"
                   id="endDate" name="endDate" value="${endDate}"
                   group="startDate,endDate" datatype="0,is_date,10"/>
        </td>
        <td></td>
    </tr>
    <tr>
        <td align="right">
            活动主题：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="campaignSubject" id="campaignSubject"
                   readonly="readonly" value="${ttCampaignPO.campaignSubject}"
                   datatype="1,is_textarea,100" size="90"/>
        </td>
    </tr>
    <tr>
        <td align="right">
            活动对象：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="campaignObject" id="campaignObject"
                   readonly="readonly" value="${ttCampaignPO.campaignObject}" size="90"
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
                   value="${ttCampaignPO.campaignPurpose}">
        </td>
    </tr>
    <tr>
        <td align="right">
            活动要求：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="campaignNeed" id="campaignNeed"
                   readonly="readonly" datatype="1,is_textarea,1000" size="70"
                   value="${ttCampaignPO.campaignNeed}">
        </td>
    </tr>
    <tr>
        <td align="right">
            活动主要内容：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="campaignDesc" id="campaignDesc"
                   readonly="readonly" datatype="1,is_textarea,1000" size="70"
                   value="${ttCampaignPO.campaignDesc}">
        </td>
    </tr>
</table>
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
    &nbsp;事业部附件
</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">
		<tr>
	        <th colspan="3" align="left">附件列表：<input type="hidden"/>
			</th>
		</tr>

  		<table class="table_info">
  		<% if(areaAttachList!=null&&areaAttachList.size()!=0){ %>
  		<c:forEach items="${areaAttachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
    </table>
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;目标
</div>
<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_list">
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
            ${toPO.orgCode}
        </td>
        <td>
            ${toPO.orgName}
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
            <input type="text" size="5" id="toPlaceCount" value="${planPO.toPlaceCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5" id="toTelStoreCount" value="${planPO.toTelStoreCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5" id="createCardsCount" value="${planPO.createCardsCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5" id="orderCount" value="${planPO.orderCount}" readonly="true">
        </td>
        <td>
            <input type="text" size="5" id="turnCarCount" value="${planPO.turnCarCount}" readonly="true">
        </td>
    </tr>
    </tbody>
</table>
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;活动范围
</div>
<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_list">
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
    <tbody id="dealerTable">
    <c:forEach items="${list2}" var="item">
        <tr class="table_list_row"
            id="${item.DEALER_ID}">
            <td>
                    ${item.DEALER_CODE}
            </td>
            <td>

                    ${item.DEALER_SHORTNAME}
                <input type="hidden"
                       value="${item.DEALER_ID}"

                       id="${item.DEALER_ID}" name="dlrIds">
            </td>
            <td>
                <script>
                    document.write
                            (getItemValue(${item.PLAN_TYPE}));
                </script>
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_textarea,50"

                       value="${item.PROJECT_NAME}" sumtag="projectName" size="5"

                       id="projectName${item.DEALER_ID}"

                       name="projectName${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"

                       datatype="1,is_date,10" sumtag="executionTimeB" size="10"

                       id="executionTimeB${item.DEALER_ID}"

                       name="executionTimeB${item.DEALER_ID}" value="${item.EXECUTION_TIME_B}"
                       readonly="true">
            </td>
            <td>
                <input type="text"

                       datatype="1,is_date,10" sumtag="executionTimeE" size="10"

                       id="executionTimeE${item.DEALER_ID}"

                       name="executionTimeE${item.DEALER_ID}" value="${item.EXECUTION_TIME_E}"
                       readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.ALL_COST}"

                       sumtag="allCost" size="5" id="allCost${item.DEALER_ID}"

                       name="allCost${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.COMPANY_COST}"

                       sumtag="companyCost" size="5" id="companyCost${item.DEALER_ID}"

                       name="companyCost${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <script>
                    document.write(getItemValue
                            (${item.COST_TYPE}));
                </script>
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.TO_PLACE_COUNT}"

                       sumtag="toPlaceCount" size="5"

                       id="toPlaceCount${item.DEALER_ID}"

                       name="toPlaceCount${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.TO_TEL_STORE_COUNT}"

                       sumtag="toTelStoreCount" size="5"

                       id="toTelStoreCount${item.DEALER_ID}"

                       name="toTelStoreCount${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.CREATE_CARDS_COUNT}"

                       sumtag="createCardsCount" size="5"

                       id="createCardsCount${item.DEALER_ID}"

                       name="createCardsCount${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.ORDER_COUNT}"

                       sumtag="orderCount" size="5" id="orderCount${item.DEALER_ID}"

                       name="orderCount${item.DEALER_ID}" readonly="true">
            </td>
            <td>
                <input type="text"
                       decimal="0" datatype="1,is_double,8" value="${item.TURN_CAR_COUNT}"

                       sumtag="turnCarCount" size="5" id="turnCarCount${item.DEALER_ID}"

                       name="turnCarCount${item.DEALER_ID}" readonly="true">
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<!-- 添加附件start -->
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;省系附件
</div>
<table class="table_info" border="0" id="file">
    <tr>
        <th>
            附件列表：
            <input type="hidden" id="fjids" name="fjids"/>
        </th>
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
        </tr>
    </c:forEach>
    <%
        }
    %>
</table>
<!-- 添加附件end -->
<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_query">
    <tr>
        <td colspan="4" align="center">
            <input type="hidden" name="campaignId" value="${campaignId}"/>
            <input type="hidden" name="dealerIds" value=""/>
            <input type="hidden" name="subm" value=""/>
            <input type="hidden" name="spaceId" value="${spaceId}"/>
            <input type="hidden" name="planId" value="${planPO.planId}"/>
            <input type="button" class="cssbutton" name="button4"
                   onClick="goBack();" value="返回"/>
        </td>
    </tr>
</table>
</form>
</body>
</html>