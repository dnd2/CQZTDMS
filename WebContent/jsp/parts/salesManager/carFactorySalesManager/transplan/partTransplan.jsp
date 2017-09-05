<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<title>配件发运计划</title>
<script language="javascript">
    var myObjArr = [];
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/partOutstockQuery.json";
    var title = null;
    var columns = null;
    function query(type) {
        document.getElementById("queryType").value = type;
        if (type == "normal") {
            columns = [
                {header: "序号", align: 'center', renderer: getIndex},
                {
                    header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />",
                    dataIndex: 'PICK_ORDER_ID',
                    align: 'center',
                    renderer: chkbox
                },
//            {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: myLink},
//                 {header: "整车计划", dataIndex: 'VSO_FLAG', align: 'center',renderer:showZCPlan},
                {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
                {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
                {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
                {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
                {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                {header: "备注", dataIndex: 'REMARK2', style: 'text-align:left'},
//		        {header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},
                {header: "总金额", dataIndex: 'TOTALMONEY', align: 'center', style: 'text-align:right'},
                {header: "装箱完成日期", dataIndex: 'PKG_OVER_DATE', align: 'center'},
//		        {header: "合并人", dataIndex: 'CREATEBYNAME', align: 'center'},
                {header: "仓库", dataIndex: 'WH_NAME', align: 'center'},
                {header: "现场BO件数", dataIndex: 'XC_FLAG', align: 'center'}
//		        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}

            ];
            document.getElementById('TransDiv').style.display = "none";
            document.getElementById('isTr').style.display = "none";
        } else if (type == "cancel") {
            columns = [
                {header: "序号", align: 'center', renderer: getIndex},
                {
                    header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />",
                    align: 'center',
                    dataIndex: 'TRPLAN_ID',
                    renderer: checkLink
                },
                /* {
                    id: 'action',
                    header: "操作",
                    sortable: false,
                    dataIndex: 'PICK_ORDER_ID',
                    align: 'center',
                    renderer: myCancel
                }, */
                {header: "发运计划号", dataIndex: 'TRPLAN_CODE', align: 'center'},
                {header: "拣货单号", dataIndex: 'PICK_ORDERIDS', align: 'center'},
                {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
                {header: "订货单位", dataIndex: 'DEALER_NAME', style:'text-align:left'},
//		        {header: "销售单位", dataIndex: 'SELLER_NAME', align: 'center'},
                {header: "箱号", dataIndex: 'PKG_NO', align: 'center'},
                {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
                {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
                {header: "备注", dataIndex: 'REMARK2', align: 'center'},
                {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
            ];
            document.getElementById('TransDiv').style.display = "";
            document.getElementById('isTr').style.display = "";
        }
        __extQuery__(1);
    }
    function myLink(value, meta, record) {
        var text = "<a href=\"#\" onclick='selectPkgNo(\"" + value + "\")'>[生成发运计划]</a>";
        return String.format(text);

    }
    function myCancel(value, meta, record) {
        var pkgNos = record.data.PKG_NO;
        var pickOrderId = record.data.PICK_ORDER_ID;
        var trplanId = record.data.TRPLAN_ID;
        var outId = record.data.OUT_ID;
        var text = "<a href='#' onclick='cancelOrder(\"" + trplanId + "\",\"" + pickOrderId + "\",\"" + pkgNos + "\")'>[取消]</a>";
        if (outId == '0') {
            return String.format(text);
        } else {
            return String.format("");
        }

    }
    function chkbox(value, meta, record) {
        var dealerId = record.data.DEALER_ID;
        var orderType = record.data.ORDER_TYPE;
        var xcFlag = record.data.XC_FLAG;
        var val = value + "," + dealerId + "," + orderType + "," + xcFlag;
        return String.format("<input id='ck' name='ck' type='checkbox' onclick='ck(this)' value='" + val + "' />");

    }
    function printOrder(trplanId, pickOrderId, pkgNos) {
        var url3 = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransPlan.do?pickOrderId=" + pickOrderId + "&trplanId=" + trplanId
        window.open(url3, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    }
    
    function cancelOrder(trplanId, pickOrderId, pkgNos) {
        MyConfirm("确定取消", cancel, [trplanId, pickOrderId, pkgNos])
    }
    function cancel(value) {
    	var trplanId=value[0];
    	var pickOrderId=value[1];
    	var pkgNos=value[2];
        var url2 = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransPlan/cancelTransPlan.json";
        var params = "trplanId=" + trplanId + "&pickOrderId=" + pickOrderId + "&pkgNos=" + pkgNos;
        makeCall(url2, getResult, params);
    }
    //生成对象
    function createObj(loc, soId) {
        for (var i = 0; i < loc.length; i++) {
            var obj = new Object();
            obj.soId = soId;
            obj.loc_id = loc[i].LOC_ID;
            obj.loc_name = loc[i].LOC_NAME;
            myObjArr.push(obj);
        }
    }
    //查看
    function detailOrder(value) {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/detailOrder.do?pickOrderId=" + value;
    }
    //打印链接
    function printPkgOrder(id) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, 1100, 400);
    }
    //发运计划
    function selectPkgNo() {
        var ck = document.getElementsByName('ck');
        var cn = 0;
        var dealerId = "";
        var pickIds = "";
        var orderType = "";
        var xcFlag = "";
        for (var i = 0; i < ck.length; i++) {
            if (ck[i].checked) {
                dealerId = ck[i].value.split(",")[1];
                orderType = ck[i].value.split(",")[2];
                xcFlag = ck[i].value.split(",")[3];
                if(xcFlag != "0"){
                    MyAlert("拣货单【"+(ck[i].value.split(",")[0])+"】存在现场BO,需确认后才可以进行后续操作!");
                    return;
                }
                cn++;
            }
        }
        for (var i = 0; i < ck.length; i++) {
            if (ck[i].checked) {
                if (dealerId != ck[i].value.split(",")[1]) {
                    MyAlert("请选择相同经销商商做合并发运!");
                    return;
                } else {
                   if (orderType != ck[i].value.split(",")[2]) {
                        MyAlert("发运时订单类型必须一致!");
                        return;
                    } else {
                        pickIds += ck[i].value.split(",")[0] + ",";
                    }  
                }
            }
        }
        if (cn == 0) {
            MyAlert("请选择需要发运的拣货单!");
            return;
        }
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/pkgNoSelect.do?pickOrderId=" + pickIds, 1000, 450);

    }

    //使所有同类型的标签DISABLED掉
    function eleControl(flag) {
        var inputTags = document.getElementsByTagName('input');
        var selTags = document.getElementsByTagName('select');
        var aTags = document.getElementsByTagName('a');
        for (var i = 0; i < inputTags.length; i++) {
            inputTags[i].disabled = flag;
        }
        for (var i = 0; i < selTags.length; i++) {
            selTags[i].disabled = flag;
        }
        for (var i = 0; i < aTags.length; i++) {
            aTags[i].disabled = flag;
        }
    }

    function doQuery() {
        var msg = "";
        if ("" != document.getElementById("SendDate").value) {
            if ("" == document.getElementById("SstartDate").value) {
                msg += "请选择合并开始时间!</br>";
            }
        }
        if ("" != document.getElementById("SstartDate").value) {
            if ("" == document.getElementById("SendDate").value) {
                msg += "请选择合并结束时间!</br>";
            }
        }

        if (msg != "") {
            MyAlert(msg);
            return;
        }
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/amount.json";
        makeNomalFormCall(url, getFreightResult, 'fm');

        __extQuery__(1);
    }

    function getFreightResult(json) {
        var DCK_AMOUNT = json.DCK_AMOUNT == null ? 0 : json.DCK_AMOUNT;
        var YCK_AMOUNT = json.YCK_AMOUNT == null ? 0 : json.YCK_AMOUNT;
        $("#in_amount")[0].value = DCK_AMOUNT;
        $("#out_amount")[0].value = YCK_AMOUNT;
    }

    function exportExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/exportExcel.do";
        fm.submit();
    }

    function disableAllA() {
        var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = true;
        }
    }

    function enableAllA() {
        var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = false;
        }
    }
    function disableAllBtn() {
        var inputArr = document.getElementsByTagName("input");
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = true;
            }
        }
    }
    function enableAllBtn() {
        var inputArr = document.getElementsByTagName("input");
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = false;
            }
        }
    }
    function disableAllClEl() {
        disableAllA();
        disableAllBtn();
    }
    function enableAllClEl() {
        enableAllBtn();
        enableAllA();
    }
    function detailOrder1(value, code) {
        var SstartDate = $('#SstartDate')[0].value;
        var SendDate = $('#SendDate')[0].value;
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/viewOutRepo.do", 800, 400);
    }
    function printTranPlan() {
    	window.open("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartTransPlan/init.do?flag=pickPrint");
    } 
    
    function ckAll(obj) {
        var cb = document.getElementsByName("ck");
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].disabled) {
                continue;
            }
            cb[i].checked = obj.checked;
        }
    }
    function checkLink(value, meta, record) {
        return String.format("<input id='ck' name='ck' type='checkbox'  value='" + value + "' />");
    }
    function modifyTransType() {
        var ck = document.getElementsByName('ck');
        var cn = 0;

        var transType = $("#transType")[0].value;
        var transportOrg = $("#transportOrg")[0].value;

        if (!transType || !transportOrg) {
            MyAlert("请选择发运方式和承运物流!");
            return;
        }
        for (var i = 0; i < ck.length; i++) {
            if (ck[i].checked) {
                cn++;
            }
        }
        if (cn == 0) {
            MyAlert("请选择要修改的记录!");
            return;
        }
        MyConfirm("确定修改?",confirmResult);
    }
    
    function confirmResult(){
    	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/updatePartTransInfo.json";
        makeNomalFormCall(url, getResult, 'fm');
    }

    function getResult(json) {
        if (json.successMsg) {
            MyAlert(json.successMsg);
        } else if (json.errorMsg) {
            MyAlert(json.errorMsg);
        }
        query("cancel");
    }
    //出库
    function pkgPart(pickOrderId, pkgNos) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/partOutstock.do?pickOrderId=" + pickOrderId + "&pkgNos=" + pkgNos;
    }

    function showZCPlan(value, meta, record) {
        var view = "<a href=\"#\" onclick='showDetail(\"" + record.data.DEALER_ID + "\")'>[有]</a>";
        if (record.data.VSO_FLAG == "有") {
            return String.format(view);
        } else {
            return String.format("无");
        }
    }
    function showDetail(dealerId) {
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partQueryZCPlanInit.do?dealerId=' + dealerId, 950, 500);
    }

    $(document).ready(function(){
    	query('normal');
    	enableAllClEl(); 
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="queryType" id="queryType" value="normal"/>
    <input type="hidden" name="queryFlag" id="queryFlag" value="1"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;配件发运计划
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">拣货单号：</td>
	                <td width="21%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                <td class="right">订货单位编码：</td>
	                <td width="24%"><input type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                </td>
	                <td class="right">订货单位：</td>
	                <td width="24%"><input type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	            </tr>
	            <tr>
	                <td class="right">订单号：</td>
	                <td width="21%"><input class="middle_txt" type="text" id="orderCode" name="orderCode"/></td>
	                <td class="right">完成装箱日期：</td>
	                <td width="26%"><input name="SstartDate" type="text" class="short_txt" id="SstartDate" style="width:80px;"
	                                       value="${old}"/>
	                    <input name="button" value=" " type="button" class="time_ico" 
	                           onclick="showcalendar(event, 'SstartDate', false);"/>
	                   	 至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" style="width:80px;" value="${now}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate', false);"/></td>
	
	                <%-- <td class="right"><span class="right">发运方式</span>：</td>
	                 <td width="24%">
	                     <select name="TRANS_TYPE" id="TRANS_TYPE" class="short_sel">
	                         <option value="">-请选择-</option>
	                         <c:if test="${transList!=null}">
	                             <c:forEach items="${transList}" var="list">
	                                 <option value="${list.fixValue }">${list.fixName }</option>
	                             </c:forEach>
	                         </c:if>
	                     </select>
	                 </td>--%>
	
	                <td class="right">出库仓库：</td>
	                <td width="24%">
	                    <select name="whId" id="whId" class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr id="isTr" style="display: none">
	                <td class="right">发运计划号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="TransNo" name="TransNo"/></td>
	                <td class="right">是否已发运：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBox("isOut", <%=Constant.IF_TYPE%>, <%=Constant.IF_TYPE_NO%>, true, "", "onchange='__extQuery__(1)'", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="query('normal');"/>
	                    &nbsp;
	                    <input name="createBtn" id="createBtn" class="normal_btn" type="button" value="生成发运计划"
	                           onclick="selectPkgNo();"/>
	                   <!--  &nbsp;
	                    <input name="cancelBtn" id="cancelBtn" class="normal_btn" type="button" value="运单调整"
	                           onclick="query('cancel');"/> -->
	                    &nbsp;
	                    <input class="normal_btn" type="button" value="打印发运单" name="printPrintInfo"
	                           id="printPrintInfo" onclick="printTranPlan();"/>
	                </td>
	            </tr>
	        </table>
	        </div>
	    </div>    
        <table border="0" class="table_query" id="TransDiv" style="display: none">
            <th colspan="6" width="100%"></th>
            <tr>
                <td align="left">发运方式：
                    <select name="transType" id="transType" class="u-select">
                        <option value="">--请选择</option>
                        <c:forEach items="${listf}" var="obj">
<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
								<option value="${obj.TV_ID}">${obj.TV_NAME}</option>
                        </c:forEach>
                    </select>
                    	承运物流：
                    <select name="transportOrg" id="transportOrg" class="u-select">
                        <option value="">--请选择</option>
                        <c:forEach items="${listc}" var="obj">
<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
								<option value="${obj.LOGI_CODE}">${obj.LOGI_FULL_NAME}</option>
                        </c:forEach>
                    </select>
                    <input type="button" class="normal_btn" name="sava" id="save" onclick="modifyTransType();"
                           value="修改"/>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</html>
</form>
</body>
</html>