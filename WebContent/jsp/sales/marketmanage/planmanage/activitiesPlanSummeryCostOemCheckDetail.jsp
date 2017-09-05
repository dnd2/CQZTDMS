<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>市场活动总结费用审核(车厂)</title>
    <%
        String contextPath = request.getContextPath();
        List dlrSummeryAttachList = (List) request.getAttribute("dlrSummeryAttachList");
    %>

    <script type="text/javascript">
        function doInit() {
            loadcalendar();
        }
        function goBack() {
            document.fm.action = '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostOemCheck/doInit.do';
            document.fm.submit();
        }
        function toValidate(arg){

            var m=document.getElementById("checkDesc").value;
            if (m.length==0) {
                MyAlert("审核意见不能为空!");
                return false;
            }
            if(m.length>200){
                MyAlert("审核意见不能超过200个字符!");
                return false;
            }

                var title='';
                if(arg==1){
                   title='确认审核通过?';
                   var obj=document.getElementById("submitCost");
                   obj.setAttribute("datatype","2,is_double,8")
                   obj.setAttribute("decemial","2");
                }else if(arg==0){
                   title='确认审核驳回?';
                   var obj=document.getElementById("submitCost");
                   obj.removeAttribute("datatype");
                   obj.removeAttribute("decemial");
                }
                if (submitForm('fm')) {
                    MyConfirm(title, toConfirm, [arg]); // 0表示为保存,1表示为提报
                }
        }
//操作提交
        function toConfirm(value){
            document.getElementById('subm').value = value;
            makeNomalFormCall(g_webAppName + '/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostOemCheck/doMod.json', showResult, 'fm');
        }
//回调方法
        function showResult(json){
            if (json.returnValue == '1') {
                window.parent.MyAlert("操作成功！");
                $('fm').action = g_webAppName + '/sales/marketmanage/planmanage/ActivitiesPlanSummeryCostOemCheck/doInit.do';
                $('fm').submit();
            }
            else {
                MyAlert("操作失败！请联系系统管理员！");
            }
        }

    </script>

</head>
<body>
<div class="navigation">
    <img src="<%=request.getContextPath()%>/img/nav.gif"/>
    &nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动总结费用审核(车厂)
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
    &nbsp;活动费用汇总
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
						实际来场客流数
					</th>
					<th>
						实际来电来店数
					</th>
					<th>
						实际建卡数
					</th>
					<th>
						实际订单数
					</th>
					<th>
						实际交车数
					</th>
                    <th>
                        实际执行总费用
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
                 <tr>
                    <td align="right">审核支持费用:</td>
                    <td>
                        <input type="text" id="submitCost" name="submitCost" decimal="2" datatype="2,is_double,8">
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
    &nbsp;审核意见
</div>
<table class="table_query">
    <tr>
        <td align="right">审核意见:</td>
        <td>
            <textarea cols="50" rows="5" id="checkDesc" name="checkDesc"  datatype="0,is_textarea,200"></textarea>
        </td>
    </tr>
</table>

<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_query">
    <tr>
        <td colspan="4" align="center">
            <input type="hidden" name="campaignId" value="${campaignId}"/>
            <input type="hidden" name="spaceId" value="${spaceId}"/>
            <input type="hidden" name="planId" value="${planPO.planId}"/>
            <input type="hidden" name="subm" value=""/>
            <input type="hidden" name="sumId" value="${summeryPO.sumId}"/>
            <c:choose>
                <c:when test="${summeryPO.status==13291003}">

                    <input type="button" class="cssbutton" name="saveBtn"
                           onClick="toValidate('1');" value="审核通过"/>
                    <input type="button" class="cssbutton" name="repBtn"
                           onClick="toValidate('0');" value="审核驳回"/>
                </c:when>
                <c:otherwise>
                    <script>
                        window.attachEvent("onload",displaySubmitCost);
                        function displaySubmitCost(){
                            var submitCost=document.getElementById("submitCost");
                            submitCost.value='${summeryPO.submitCost}';
                            submitCost.setAttribute("readOnly","readonly");
                            var checkDesc=document.getElementById("checkDesc");
                            checkDesc.value='${checkDesc}';
                            checkDesc.setAttribute("readOnly","readonly");
                        }
                    </script>
                </c:otherwise>
            </c:choose>
            <input type="button" class="cssbutton" name="button4"
                   onClick="goBack();" value="返回"/>
        </td>
    </tr>
</table>
</form>
</body>
</html>