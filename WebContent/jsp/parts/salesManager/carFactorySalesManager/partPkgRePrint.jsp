<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>发运标签打印</title>
</head>
<script language="javascript" for="document" event="onkeydown">
    if (event.keyCode == 13) {
        var pkgNo = document.getElementById("pkgNo").value;
        if (pkgNo == "") {
        	MyAlert("请输入装箱单号");
        } else {
            var getFreightUrl = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/existCheck.json?pkgNo=' + pkgNo + '';
            makeNomalFormCall(getFreightUrl, getFreightResult, 'fm');
            __extQuery__(1);
        }

    }
</script>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/query.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PKG_NO', renderer: myLink, align: 'center'},
        {header: "经销商编码", dataIndex: 'CONSIGNEES_CODE', align: 'center'},
        {header: "经销商名称", dataIndex: 'CONSIGNEES', style: 'text-align:left'},
        {header: "地址", dataIndex: 'ADDR', style: 'text-align:left'},
        {header: "出库单号", dataIndex: 'OUT_CODE', style: 'text-align:left'},
        {header: "发运单号", dataIndex: 'TRANS_CODE', style: 'text-align:left'},
        {header: "装箱单号", dataIndex: 'PKG_NO', style: 'text-align:left'},
        {header: "发货方式", dataIndex: 'TRANS_TYPE', style: 'text-align:left'},
        {header: "发货仓储", dataIndex: 'WH_NAME', style: 'text-align:left'}
    ];
    function myLink(value, meta, record) {
        var dealerId = record.data.CONSIGNEES_ID;
        var dealerCode = record.data.CONSIGNEES_CODE;
        return String.format("<a href=\"#\" onclick='printOrder(\"" + dealerCode + "\",\"" + dealerId + "\",\"" + value + "\")'>[打印]</a>");
    }
    function printOrder(dealerCode , dealerId, pkgNo) {
        var url_3 ='<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/partConfig.do?dealerCode=' + dealerCode + '&dealerId=' + dealerId + '&pkgNo=' + pkgNo;
        //var param = "height="+window.screen.availHeight+",width="+window.screen.availWidth+",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=yes, status=yes'";
        //window.open(url_3,'发运标签打印',param);
        OpenHtmlWindow(url_3, 500, 300);
    }

    function printOrderForBtn() {
        var pkgNo = document.getElementById("pkgNo").value;
        if (pkgNo == "") {
            MyAlert("装箱单号不能为空!");
            return;
        }
        var getFreightUrl = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/existCheck.json?pkgNo=' + pkgNo + '';
        makeNomalFormCall(getFreightUrl, getFreightResult, 'fm');
        __extQuery__(1);

    }
    function doPrint(start, end, sellerId, dealerId, addrId) {
        var url_1 = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartPkgRePrint/printQxt.do?start=" + start + "&end=" + end + "&orgId=" + sellerId + "&dealerId=" + dealerId + "&addrId=" + addrId;
        var param = "height=" + window.screen.availHeight + ",width=" + window.screen.availWidth + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=yes, status=yes'";
        window.open(url_1, "发运标签打印", param);
    }

    function getFreightResult(jsonObj) {
        if (jsonObj.error) {
            MyAlert(jsonObj.error);
        } else {
            OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/partConfig.do?pkgNo=' + jsonObj.list[0].PKG_NO, 500, 300);
        }
    }
    /* function getFreightResult1(jsonObj) {
        if (jsonObj.error) {
            MyAlert(jsonObj.error);
        } else {
            document.getElementById("dealerName").value = jsonObj.list[0].DEALER_NAME;
        }
    } */
    function viewDlrPkgNoDtlxx() {
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/dlrPKginit.do', 800, 500);
    }
</script>
</head>
<body onload="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理&gt;总部销售管理&gt;去向唛头打印
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td width="10%" align="right">装箱单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pkgNo" name="pkgNo"/></td>
	                <td>
	                    <input name="BtnPrint" id="BtnPrint" class="u-button" type="button"
	                           value="打 印" onclick="printOrderForBtn();"/>
	                    <input name="BtnQuery" id="queryBtn" class="u-button" type="button"
	                           value="查 询" onclick="__extQuery__(1);"/>
	                   <!--  <input name="BtnQuery" id="queryBtn" class="u-button" type="button"
	                           value="查询有效箱号" onclick="viewDlrPkgNoDtlxx();"/> -->
	                </td>
	            </tr>
	        </table>
	        </div>
	     </div>   
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>
</html>