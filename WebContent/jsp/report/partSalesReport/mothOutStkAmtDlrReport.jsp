<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>进销存报表(服务商)</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件报表&gt;进销存报表(服务商)
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <input type="hidden" name="oemOrgId" id="oemOrgId"/>
        <input type="hidden" name="parentOrgId" id="parentOrgId"/>
        <input type="hidden" name="oemFlag" id="oemFlag" value="${oemFlag}"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td align="right">配件编码：</td>
                <td><input name="partOldcode" type="text" class="middle_txt" id="partOldcode"/></td>
                <td align="right">配件名称：</td>
                <td><input name="partCname" type="text" class="middle_txt" id="partCname"/></td>
                <td align="right">日期：</td>
                <td>
                    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" value="${old}" readonly/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" "
                           type="button"/>
                    至
                    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" value="${now}" readonly/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" "
                           type="button"/>
                </td>
            </tr>
            <tr>
                <td align="right">服务商代码：</td>
                <td>
                    <input class="middle_txt" type="text" name="dealerCode" id="dealerCode"/>
                </td>
                <td align="right">服务商名称：</td>
                <td>
                    <input class="middle_txt" type="text" id="dealerName" name="dealerName" value=""/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showChildOrg('','dealerName','dealerId','','');"/>
                    <input type="hidden" id="dealerId" name="dealerId" value=""/>
                    <INPUT class="mini_btn" onclick="clearInput();" value=清除 type=button name=clrBtn>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="query();"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expMothStkAmtDlrExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/mothOutStockAmtDlrAction/mothOutStockAmtSearch.json";
        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "服务商代码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
            {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
//            {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "期初数量", dataIndex: 'QC_QTY'},
            {header: "入库数量", dataIndex: 'IN_QTY'},
            {header: "入库金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
            {header: "出库数量", dataIndex: 'OUT_QTY'},
            {header: "出库金额", dataIndex: 'OUT_AMOUNT', style: 'text-align:right'},
            {header: "期末数量", dataIndex: 'QM_QTY'},
            {header: "期末金额", dataIndex: 'QM_AMOUNT', style: 'text-align:right'}
        ];

        jQuery(function () {
            if (jQuery('#oemFlag').val() ==<%=Constant.IF_TYPE_NO%>) {
                jQuery('#dealerCode').parent().parent().hide();
                jQuery('#dealerCode').val(${parentOrgCode});
                jQuery('#dealerName').val(${companyName });
            }
        })

        //导出
        function expMothStkAmtDlrExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/mothOutStockAmtDlrAction/exportMothOutStockAmtExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        function clearInput() {
            //清空选定供应商
            document.getElementById("dealerName").value = '';
            document.getElementById("dealerId").value = '';
        }

        function showChildOrg(RETURN_DEALER, childorgName, childorgId, WH_ID, WH_NAME) {
            document.getElementById("dealerCode").value = '';
            if (!RETURN_DEALER) {
                RETURN_DEALER = null;
            }
            if (!childorgName) {
                childorgName = null;
            }
            if (!childorgId) {
                childorgId = null;
            }
            if (!WH_ID) {
                WH_ID = null;
            }
            if (!WH_NAME) {
                WH_NAME = null;
            }
            OpenHtmlWindow("<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/returnDealerSelect.jsp?RETURN_DEALER=" + RETURN_DEALER + "&childorgName=" + childorgName + "&childorgId=" + childorgId + "&WH_ID=" + WH_ID + "&WH_NAME=" + WH_NAME, 730, 390);
        }

        function query() {
            var dealerCode = document.getElementById("dealerCode").value;
            var dealerName = document.getElementById("dealerName").value;
            if (jQuery('#oemFlag').val() == '<%=Constant.IF_TYPE_YES%>') {
                if (dealerCode == "" && dealerName == "") {
                    MyAlert("由于数据量比较大,请至少选择一个服务商!");
                    return;
                } else {
                    __extQuery__(1)
                }

            } else {
                __extQuery__(1);
            }
        }
    </script>
</div>
</body>
</html>