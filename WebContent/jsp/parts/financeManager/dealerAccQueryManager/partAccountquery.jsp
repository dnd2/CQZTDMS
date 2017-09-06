<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>账户余额查询</title>
<script language="javascript" type="text/javascript">
        function doInit() {
            __extQuery__(1);
        }
    </script>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerAccQuerySearch.json";

    var title = null;
    var columns = null;

    if ("3" != "${userRole}") {
        columns = [
            {header: "序号", dataIndex: 'SEQUENCE_ID', renderer: getIndex},
            {header: "服务商编码", dataIndex: 'DEALER_CODE'},
            {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left; '},
            {header: "销售单位编码", dataIndex: 'PARENTORG_CODE', style: 'text-align:center;'},
            {header: "销售单位名称", dataIndex: 'PARENTORG_NAME', style: 'text-align:left; '},
            {header: "资金类型", dataIndex: 'ACCOUNT_KIND', renderer: getItemValue},
            {header: "款项类型", dataIndex: 'ACCOUNT_PURPOSE', renderer: getItemValue},
            {header: "总可用余额", dataIndex: 'USEABLEACCOUNT', style: 'text-align:right;', renderer: getAccountKyDetail},
//            {header: "已扣款金额", dataIndex: 'PREEMPTIONVALUE', style: 'text-align:right; ', renderer: getPreemptionDetail},
            {header: "现金可用余额", dataIndex: 'CASH_KY', style: 'text-align:right;'},
            {header: "信用额度", dataIndex: 'CREDIT_LIMIT', style: 'text-align:right; '},
            {header: "现金账户余额", dataIndex: 'ACCOUNT_SUM', style: 'text-align:right; ', renderer: getAccountDetail},
            {header: "已开票金额", dataIndex: 'ACCOUNT_INVO', style: 'text-align:right; ', renderer: getInvoiceDetail}
        ];
    }
    else {
        columns = [
            {header: "序号", dataIndex: 'SEQUENCE_ID', renderer: getIndex},
//					{header: "服务商编码", dataIndex: 'DEALER_CODE',style:'text-align:left; '},
//					{header: "服务商名称", dataIndex: 'DEALER_NAME',style:'text-align:left; '},
            {header: "销售单位编码", dataIndex: 'PARENTORG_CODE', style: 'text-align:center; '},
            {header: "销售单位名称", dataIndex: 'PARENTORG_NAME', style: 'text-align:left; '},
            {header: "资金类型", dataIndex: 'ACCOUNT_KIND', renderer: getItemValue},
             {header: "款项类型", dataIndex: 'ACCOUNT_PURPOSE', renderer: getItemValue},
            {header: "总可用余额", dataIndex: 'USEABLEACCOUNT', style: 'text-align:right;', renderer: getAccountKyDetail},
//            {header: "已扣款金额(元)", dataIndex: 'PREEMPTIONVALUE', style: 'text-align:right; ', renderer: getPreemptionDetail},
//            {header: "现金可用余额(元)", dataIndex: 'CASH_KY', style: 'text-align:right;'},
            {header: "信用额度", dataIndex: 'CREDIT_LIMIT', style: 'text-align:right; '},
//            {header: "现金账户余额(元)", dataIndex: 'ACCOUNT_SUM', style: 'text-align:right; ', renderer: getAccountDetail},
            {header: "已开票金额(元)", dataIndex: 'ACCOUNT_INVO', style: 'text-align:right; ', renderer: getInvoiceDetail}
        ];
    }

//返回余额链接
function getAccountDetail(value, meta, record) {
    return String.format("<a href=\"#\" onclick='showAccountDetail(\"" + record.data.CHILDORG_ID + "\",\"" + record.data.PARENTORG_ID + "\",\"" + record.data.ACCOUNT_PURPOSE + "\")'>" + record.data.ACCOUNT_SUM + "</a>");
}

//返回占用金额额链接
function getPreemptionDetail(value, meta, record) {
    return String.format("<a href=\"#\" onclick='showDetail(\"" + record.data.CHILDORG_ID + "\",\"" + record.data.PARENTORG_ID + "\",\"" + record.data.ACCOUNT_ID + "\")'>" + record.data.PREEMPTIONVALUE + "</a>");
}
//返回已开票金额额链接 //需修改 add by yuan 201306018
function getInvoiceDetail(value, meta, record) {
    return String.format("<a href=\"#\" onclick='showInvDetail(\"" + record.data.CHILDORG_ID + "\",\"" + record.data.PARENTORG_ID + "\",\"" + record.data.ACCOUNT_ID + "\")'>" + record.data.ACCOUNT_INVO + "</a>");
}
//返回可用余额明细
function getAccountKyDetail(value, meta, record) {
    return String.format("<a style='color:red;font-weight:bold' href=\"#\" onclick='showKyDetail(\"" + record.data.CHILDORG_ID + "\",\"" + record.data.PARENTORG_ID + "\",\"" + record.data.ACCOUNT_ID + "\")'>" + record.data.USEABLEACCOUNT + "</a>");
}
//显示余额往来明细
function showAccountDetail(dealerId, parentId, accountKind) {
    OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccContactDetailManager/partDealerAccConDetAction/dalerAccConDetInit.do?parentOrgId=' + parentId + '&dealerId=' + dealerId + "&accountKind=" + accountKind, 950, 500);
}

//显示预扣金额详情
function showDetail(dealerId, parentId, accountId) {
    OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerPreemptionDetail.do?parentOrgId=' + parentId + '&dealerId=' + dealerId + "&ACCOUNT_ID=" + accountId, 950, 500);
}

//显示开票金额详情
function showInvDetail(dealerId, parentId, accountId) {
    OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerInvAccDetail2.do?parentOrgId=' + parentId + '&dealerId=' + dealerId + "&ACCOUNT_ID=" + accountId, 950, 500);
}
//显示可用明细
function showKyDetail(dealerId, parentId, accountId) {
    OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerKyDetail2.do?parentOrgId=' + parentId + '&dealerId=' + dealerId + "&ACCOUNT_ID=" + accountId, 950, 500);
}

//选择服务商
function sel() {
    var parentOrgId = document.getElementById("parentOrgId").value;
    OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerSelect.do?parentOrgId=' + parentOrgId, 700, 500);
}

//下载
function exportPartExceptionExcel() {
    document.fm.action = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/exportPartDealerAccountExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

function setPartCode(dealerCodes) {
    document.getElementById("dealerCode").value = dealerCodes;
}

//千分格式
function addKannma(number) {
    var num = number + "";
    num = num.replace(new RegExp(",", "g"), "");
    // 正负号处理
    var symble = "";
    if (/^([-+]).*$/.test(num)) {
        symble = num.replace(/^([-+]).*$/, "$1");
        num = num.replace(/^([-+])(.*)$/, "$2");
    }

    if (/^[0-9]+(\.[0-9]+)?$/.test(num)) {
        var num = num.replace(new RegExp("^[0]+", "g"), "");
        if (/^\./.test(num)) {
            num = "0" + num;
        }

        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/, "$1");
        var integer = num.replace(/^([0-9]+)(\.[0-9]+)?$/, "$1");

        var re = /(\d+)(\d{3})/;

        while (re.test(integer)) {
            integer = integer.replace(re, "$1,$2");
        }
        return symble + integer + decimal;

    } else {
        return number;
    }
}
function clrTxt(txtId) {
    document.getElementById(txtId).value = "";
}
function txtClr(txtId) {
    document.getElementById(txtId).value = "";
}
</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件财务管理 &gt; 服务商账户查询
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="childOrgId" id="childOrgId" value="${childOrgId }" />
				<input type="hidden" name="childOrgCode" id="childOrgCode" value="${childOrgCode }" />
				<input type="hidden" name="userRole" id="userRole" value="${userRole }" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<c:if test="${normalDlr ne userRole}">
							<tr>
								<td class="right">服务商：</td>
								<td>
									<input type="text" class="middle_txt" name="dealerName" size="15" value="" id="dealerName" />
									<input type="hidden" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode" />
									<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');" value="..." />
									<input type="button" class="u-button" onclick="txtClr('dealerCode');txtClr('dealerName');" value="清 空" id="clrBtn" />
								</td>
								<td class="right">款项类型：</td>
								<td>
									<script type="text/javascript">
                            			genSelBoxExp("accountPurpose", '<%=Constant.PART_ACCOUNT_PURPOSE_TYPE%>', '<%=Constant.PART_ACCOUNT_PURPOSE_TYPE_01%>', true, "", "onchange=__extQuery__(1)", "false", "");
									</script>
								</td>
								<td class="right">资金类型：</td>
								<td>
									<script type="text/javascript">
										genSelBoxExp("accountKind", <%=Constant.FIXCODE_CURRENCY%>, <%=Constant.FIXCODE_CURRENCY_01%>, true, "", "onchange=__extQuery__(1)", "false", "");
									</script>
								</td>
							</tr>
						</c:if>
						<tr>
							<td class="center" colspan="6">
								<c:if test="${normalDlr ne userRole}">
									<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
									<input class="u-button" type="reset" value="重 置" \>
								</c:if>
								<input class="u-button" type="button" value="导出" onclick="exportPartExceptionExcel()" />
							</td>
						</tr>
						<tr>
							<td style="color: red" align="center" colspan="6">提示:点击总可用余额下面的具体数字即可看到往来发生明细.如有异议请联系业务或财务!</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>