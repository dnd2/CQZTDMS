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
    <title>装箱单打印</title>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />", align: 'center', dataIndex: 'PICK_ORDER_ID', renderer: checkLink},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getActionLink},
        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        /*{header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},*/
        /* {header: "拣货单打印", dataIndex: 'PICK_ORDER_PRINT_NUM', align: 'center', renderer: getPrint},*/
//        {header: "装箱单已打印完", dataIndex: 'PKG_PRINT_NUM', align: 'center', renderer: getPrint2},
        {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
        {header: "总金额", dataIndex: 'TOTALMONEY', align: 'center', style: 'text-align:right'},
        {header: "打印日期", dataIndex: 'PKG_PRINT_DATE', align: 'center'},
        {header: "打印次数", dataIndex: 'PKG_PRINT_NUM', align: 'center'},
//        {header: "合并人", dataIndex: 'CREATEBYNAME', align: 'center'},
        {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
//        {id: 'backupAction', header: "补打操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getBackupActionLink}
    ];
    function getActionLink(value, meta, record) {
        var pkgPrintNum = record.data.PKG_PRINT_NUM;//装箱单打印次数
        var printpkgNum = record.data.PRINTPKG;//已打印箱子数量
        var noprintpkgNum = record.data.NOPRINTPKG;//未打印箱子数量
        var totoalpkgNum = record.data.TOTOALPKG;//总共箱子次数数量
        var pkgview = "<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>";
        var pkgDtlPrint = "<a href=\"#\" onclick='selectPkgNo(\"" + value + "\",\"" + record.data.PKG_PRINT_NUM + "\")'>[打印装箱单]</a>";
        var pkgDtlPrint2 = "<a href=\"#\" onclick='selectPkgNo2(\"" + value + "\",\"" + record.data.PKG_PRINT_NUM + "\")'>[补打]</a>";
        var pkgDtlView = "<a href=\"#\" onclick='viewPkgDtl(\"" + value + "\")'>[查看装箱明细]</a>";
        var expPkgDtl= "<a href=\"#\" onclick='exportExcel(\"" + value + "\")'>[导出装箱明细]</a>";
        if(pkgPrintNum > 0){
            return String.format(pkgDtlView+expPkgDtl + pkgDtlPrint2);
        }else{
            return String.format(pkgDtlPrint +pkgDtlView +expPkgDtl);
        }
    }
    function exportExcel(pickOrderId){
        document.getElementById("pickOrderId2").value =  pickOrderId;
        var url_t = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/exportPkgDtlToExcel.do";
        document.fm.action=url_t;
        document.fm.submit();
    }
    function checkLink(value, meta, record) {
        return String.format("<input  name='cb' type='checkbox'  value='" + value + "' />");
    }
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
            <%-- if (MyConfirm("您已经打印过" + count + "次拣货单?是否继续打印?")) {
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
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/pkgNoSelect.do?pickOrderId=" + pickOrderId + "&flag=1", 800, 450, '打印装箱单');
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
        /*if ("" != document.getElementById("SendDate").value) {
            if ("" == document.getElementById("SstartDate").value) {
                msg += "请选择合并开始时间!</br>";
            }
        }
        if ("" != document.getElementById("SstartDate").value) {
            if ("" == document.getElementById("SendDate").value) {
                msg += "请选择合并结束时间!</br>";
            }
        }
*/
        if (msg != "") {
            MyAlert(msg);
            return;
        }
        __extQuery__(1);
    }

    function initCondition() {
        if ($('#condition_pickOrderId')[0]) {
            $('#pickOrderId')[0].value = $('#condition_pickOrderId')[0].value;
        }
        if ($('#condition_dealerName')[0]) {
            $('#dealerName')[0].value = $('#condition_dealerName')[0].value;
        }
        if ($('#condition_dealerCode')[0]) {
            $('#dealerCode')[0].value = $('#condition_dealerCode')[0].value;
        }
        if ($('#condition_whId')[0]) {
            $('#whId')[0].value = $('#condition_whId')[0].value;
        }
        if ($('#condition_startDate')[0]) {
            $('#SstartDate')[0].value = $('#condition_startDate')[0].value == "" ? $('#SstartDate')[0].value : $('#condition_startDate')[0].value;
        }
        if ($('#condition_endDate')[0]) {
            $('#SendDate')[0].value = $('#condition_endDate')[0].value == "" ? $('#SendDate')[0].value : $('#condition_endDate')[0].value;
        }
        /*if($('#condition_printFlag')[0]){
         $('#printFlag')[0].value = $('#condition_printFlag')[0].value;
         }*/
        if ($('#condition_transFlag')[0]) {
            $('#TransFlag')[0].value = $('#condition_transFlag')[0].value == "" ? $('#TransFlag')[0].value : $('#condition_transFlag')[0].value;
        }
        /*if($('#condition_transType')[0]){
         $('#TRANS_TYPE')[0].value = $('#condition_transType')[0].value;
         }*/
        /* if ($('#condition_isPkg')[0]) {
         $('#IsPkg')[0].value = $('#condition_isPkg')[0].value;
         }*/
    }
    function viewPkgDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlInit.do?pickOrderID=" + value, 600, 500);
    }
 
    function printprintPrintInfo() {
        window.showModalDialog("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=pickPrint", "", 'edge: Raised; center: Yes; help: Yes; resizable: Yes; status: No;dialogHeight:768px;dialogWidth:1024px;toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
    }
    jQuery(function () {
        jQuery('#printPrintInfo').css('width', '100px');
        jQuery(document).on('click', '#printPrintInfo', function () {
            printprintPrintInfo();
        });
    })
    function ckAll(obj) {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].disabled) {
                continue;
            }
            cb[i].checked = obj.checked;
        }
    }
    function batchPrint(){
    	var rsFlag = false;
    	var mt = document.getElementById("myTable");
    	if(mt == null){
    		MyAlert("请选择需要批量打印装箱单！");
    		return;
    	}
        for (var i = 1; i < mt.rows.length; i++) {
            var flag = mt.rows[i].cells[1].firstChild.checked;
        	if(flag){
        		rsFlag = true;
            }
        }
        if(rsFlag){
			var url2 = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPkg/opPkgDtlPrintHtmlBatch.do";
			document.fm.action = url2;
			document.fm.target = "_blank";
			document.fm.submit();
        }else{
			MyAlert("请选择需要批量打印装箱单！");
        }
    }
    function closeWindow(){
        window.returnValue = "refresh";
        window.close();
    }
    $(document).ready(function(){
    	initCondition();
    	__extQuery__(1);
    });
</script>
</head>
<body class="u-main" onafterprint="MyAlert('complete')">
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
    <input id="pickOrderId2" name="pickOrderId2"  type="hidden"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;装箱单管理
            &gt;装箱单打印
        </div>
		<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">订单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="orderCode" name="orderCode"/></td>
	                <td class="right">订货单位编码：</td>
	                <td width="24%"><input type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                <td class="right">订货单位：</td>
	                <td width="24%"><input type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	                <%--<td class="right"><span class="right">发运方式</span>：</td>
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
	
	            </tr>
	            <tr>
	                <td class="right">拣货单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                <td class="right">拣货单打印：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBoxExp("printFlag", <%=Constant.PART_BASE_FLAG%>, "<%=Constant.PART_BASE_FLAG_YES%>", true, "", "", "false", '');
	                    </script>
	                </td>
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
	            <tr>
	                <td class="right">拣货日期：</td>
	                <td width="24%"><input name="SstartDate" type="text" class="short_txt"  style="width:80px;" id="SstartDate"
	                                       value="${old}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SstartDate', false);"/>
	                   	 至
	                    <input name="SendDate" id="SendDate" type="text" class="short_txt"  style="width:80px;" id="SendDate"
	                           value="${now}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate', false);"/></td>
	                <td class="right">已打印完装箱单：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBoxExp("TransFlag", <%=Constant.PART_BASE_FLAG%>, "<%=Constant.PART_BASE_FLAG_NO%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right" style="display: none">是否装箱：</td>
	                <td width="24%" style="display: none">
	                    <script type="text/javascript">
	                        genSelBoxExp("IsPkg", <%=Constant.PART_BASE_FLAG%>, <%=Constant.PART_BASE_FLAG_YES%>, true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">打印日期：</td>
	                <td width="24%"><input name="SstartDate1" type="text" class="short_txt" style="width:80px;" id="SstartDate1"
	                                       value=""/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SstartDate1', false);"/>
	                    	至
	                    <input name="SendDate1" id="SendDate1" type="text" class="short_txt"  style="width:80px;"
	                           value=""/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate1', false);"/></td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="doQuery();"/>
	                    &nbsp; <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="批量打印" onclick="batchPrint();"/>
	                    &nbsp; <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="关 闭" onclick="closeWindow();"/>
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