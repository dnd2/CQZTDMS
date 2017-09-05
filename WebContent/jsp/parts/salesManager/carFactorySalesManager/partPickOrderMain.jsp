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
<title>装箱单管理</title>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getActionLink},
//         {header: "整车计划", dataIndex: 'VSO_FLAG', align: 'center',renderer:showZCPlan},
        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: xcBo},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        /*{header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},*/
        /* {header: "拣货单打印", dataIndex: 'PICK_ORDER_PRINT_NUM', align: 'center', renderer: getPrint},*/
//        {header: "装箱单已打印完", dataIndex: 'PKG_PRINT_NUM', align: 'center', renderer: getPrint2},
        {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
        {header: "总金额", dataIndex: 'TOTALMONEY', align: 'center', style: 'text-align:right'},
        {header: "拣货单生成日期", dataIndex: 'PICK_ORDER_CREATE_DATE', align: 'center'},
        {header: "合并人", dataIndex: 'CREATEBYNAME', align: 'center'},
        {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
        {header: "现场BO标志", dataIndex: 'XC_FLAG', align: 'center'}
//        {id: 'backupAction', header: "补打操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getBackupActionLink}
    ];
    function getActionLink(value, meta, record) {
        var pickPrintNum = record.data.PICK_ORDER_PRINT_NUM;//捡配单打印次数
        var pkgPrintNum = record.data.PKG_PRINT_NUM;//装箱单打印次数
        
        if (record.data.STATE ==<%=Constant.CAR_FACTORY_PKG_STATE_01%> || record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='pkgPart(\"" + value + "\")'>[装箱]</a>");
        }
        if (record.data.SELLER_ID !=<%=Constant.OEM_ACTIVITIES%> && record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='pkgPart(\"" + value + "\")'>[装箱]</a>");
        }
        var pkgDtlPrint = "<a href=\"#\" onclick='selectPkgNo(\"" + value + "\",\"" + record.data.PKG_PRINT_NUM + "\")'>[打印装箱单]</a>";
//    var pkgDtlPrint2 = "<a href=\"#\" onclick='selectPkgNo2(\"" + value + "\",\"" + record.data.PKG_PRINT_NUM + "\")'>[补打]</a>";
        var pkgDtlView = "<a href=\"#\" onclick='viewPkgDtl(\"" + value + "\")'>[装箱明细]</a>";
        var pkgView = "<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>";
        var noPkgDtlView = "<a href=\"#\" onclick='viewNonePkgDtl(\"" + value + "\")'>[未装箱明细]</a>";
//         var expPkgDtl = "<a href=\"#\" onclick='exportExcel(\"" + value + "\")'>[导出明细]</a>";
        var expPkgDtl = "";
        if (pkgPrintNum == 0) {
            return String.format(pkgView + pkgDtlView + noPkgDtlView);
        }else {
            return String.format(pkgView + pkgDtlView + noPkgDtlView + expPkgDtl);
        }
    }
    //导出明细---导出装箱明细
    function exportExcel(pickOrderId) {
        var url_t = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/exportPkgDtlToExcel.do?pickOrderId=" + pickOrderId;
        document.fm.action = url_t;
        document.fm.target = "_self";
        document.fm.submit();
    }
    function xcBo(value, meta, record) {
        var xcflag = record.data.XC_FLAG;
        if (xcflag == 'Y') {
            return String.format("<span style='font-size:15px;background-color: red' onload='MyAlert(1232);'>" + value + "</span>");
        } else {
            return String.format(value);
        }
    }
    //未装箱明细查询
    function viewNonePkgDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partNonePkgDtlInit.do?pickOrderID=" + value, 800, 450);
    }
    //查看明细
    function detailOrder(id) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderDetail.do?pickOrderId=" + id;
    }
    //装箱
    function pkgPart(pickOrderId) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do?pickOrderId=" + pickOrderId;
    }
    //运单
    function printTransOrder(pickOrderId) {
        window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransOrder.do?pickOrderId=" + pickOrderId, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    }
    //拣货单
    function printPickOrder(id, count) {
        if (count > 1) {
        	MyConfirm("您已经打印过" + count + "次拣货单?是否继续打印?",confirmResult,[id]);
           <%--  if (MyConfirm("您已经打印过" + count + "次拣货单?是否继续打印?")) {
                window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
            } --%>
            __extQuery__(1);
            return;
        }
        window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
        __extQuery__(1);
    }

    function confirmResult(id){
    	window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    }

    function selectPkgNo(pickOrderId, count) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/pkgNoSelect.do?pickOrderId=" + pickOrderId + "&flag=1", 800, 450);
    }
    
    function selectPkgNo2(pickOrderId, count) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/pkgNoSelect2.do?pickOrderId=" + pickOrderId + "&flag=1", 800, 450);
    }
    //打印装箱清单
    function pkgDtlPrint(id, pkgNos) {
        window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPkgDtlPrintHtml.do?pickOrderId=" + id + "&pkgNos=" + pkgNos, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
        __extQuery__(1);
    }
    function getPrint(num) {
        if (num > 0) {
            return "是";
        }
        return "否";
    }
    function getPrint2(num) {
        if (num > 0) {
            return "是";
        }
        return "否";
    }
    function doQuery() {
        var msg = "";
       /* if ("" != document.getElementById("SendDate").value) {
            if ("" == document.getElementById("SstartDate").value) {
                msg += "请选择合并开始时间!</br>";
            }
        }
        if ("" != document.getElementById("SstartDate").value) {
            if ("" == document.getElementById("SendDate").value) {
                msg += "请选择合并结束时间!</br>";
            }
        }*/

        if (msg != "") {
            MyAlert(msg);
            return;
        }
        __extQuery__(1);
    }
    function getDate() {
        var dateS = "";
        var dateE = "";
        var myDate = new Date();
        var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
        var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
        if (moth < 10) {
            if (0 < moth) {
                moth = "0" + moth;
            }else {
                year = myDate.getFullYear() - 1;
                moth = moth + 12;
                if (moth < 10) {
                    moth = "0" + moth;
                }
            }
        }
        var day = myDate.getDate();       //获取当前日(1-31)
        if (day < 10) {
            day = "0" + day;
        }
        dateS = year + "-" + moth + "-" + day;
        moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
        if (moth < 10) {
            moth = "0" + moth;
        }
        dateE = myDate.getFullYear() + "-" + moth + "-" + day;
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

    function initCondition() {
        if ($('#condition_isPkg')[0]) {
            $('#IsPkg')[0].value = $('#condition_isPkg')[0].value == "" ? $('#IsPkg')[0].value : $('#condition_isPkg')[0].value;
        }
    }
    function viewPkgDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlInit.do?pickOrderID=" + value, 600, 500);
    }
    
    function printPkgOrder() {
    	window.open("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=pkgPrint");
    }

$(document).ready(function(){
	getDate();
	initCondition();
	__extQuery__(1);

});
</script>
</head>
<body onafterprint="MyAlert('complete')">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="condition_pickOrderId" name="condition_pickOrderId" value="${condition.pickOrderId}" type="hidden"/>
    <input id="condition_dealerName" name="condition_dealerName" value="${condition.dealerName}" type="hidden"/>
    <input id="condition_dealerCode" name="condition_dealerCode" value="${condition.dealerCode}" type="hidden"/>
    <input id="condition_whId" name="condition_whId" value="${condition.whId}" type="hidden"/>
    <input id="condition_startDate" name="condition_startDate" value="${condition.startDate}" type="hidden"/>
    <input id="condition_endDate" name="condition_endDate" value="${condition.endDate}" type="hidden"/>
    <input id="condition_printFlag" name="condition_printFlag" value="${condition.printFlag}" type="hidden"/>
    <input id="condition_transFlag" name="condition_transFlag" value="${condition.transFlag}" type="hidden"/>
    <input id="condition_transType" name="condition_transType" value="${condition.transType}" type="hidden"/>
    <input id="condition_isPkg" name="condition_isPkg" value="${condition.isPkg}" type="hidden"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 > 配件销售管理 >装箱单管理</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right"><span class="right">订单号</span>：</td>
	                <td ><input type="text" id="orderCode" name="orderCode" class="middle_txt">
	                <td class="right">订货单位编码：</td>
	                <td ><input type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                <td class="right">订货单位：</td>
	                <td ><input type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	                <%--<td class="right"><span class="right">发运方式</span>：</td>
	                <td >
	                    <select name="TRANS_TYPE" id="TRANS_TYPE"  class="u-select">
	                        <option value="">-请选择-</option>
	                        <c:if test="${transList!=null}">
	                            <c:forEach items="${transList}" var="list">
	                                <option value="${list.fixValue }">${list.fixName }</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                </td>--%>
	
	            </tr>
	            <tr>
	                <td class="right">销售单号：</td>
	                <td ><input class="middle_txt" type="text" id="soCode" name="soCode"/></td>
	                <td class="right">拣货单号：</td>
	                <td ><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                <td class="right">拣货单打印：</td>
	                <td >
	                    <script type="text/javascript">
	                        genSelBoxExp("printFlag", <%=Constant.PART_BASE_FLAG%>, "<%=Constant.PART_BASE_FLAG_YES%>", true, "", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">拣货单生成日期：</td>
	                <td >
	                	<input name="SstartDate" type="text" class="short_txt" id="SstartDate" 
	                			style="width:80px" value="${old}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SstartDate', false);"/>
	                    	至
	                    <input name="SendDate" id="SendDate" type="text" class="short_txt" id="SendDate" 
	                    		style="width:80px"  value="${now}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate', false);"/>
	                </td>
	                <%-- <td class="right">装箱单已打印完：</td>
	                 <td >
	                     <script type="text/javascript">
	                         genSelBoxExp("TransFlag", <%=Constant.PART_BASE_FLAG%>, "", true, "", "", "false", '');
	                     </script>
	                 </td>--%>
	                <td class="right">是否已装箱：</td>
	                <td >
	                    <script type="text/javascript">
	                        genSelBoxExp("IsPkg", "<%=Constant.PART_BASE_FLAG%>", "<%=Constant.PART_BASE_FLAG_NO%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">出库仓库：</td>
	                <td >
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
	                           onclick="doQuery();"/>
	                    &nbsp;<input class="normal_btn" type="button" value="打印装箱单" name="printPrintInfo"
	                                 id="printPrintInfo" onclick="printPkgOrder();"/>
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