<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>资金往来明细</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            loadcalendar();  //初始化时间控件
//		getDate();
            __extQuery__(1);
        }

    </script>
</head>
<body>
<form method="post" name="fm" id="fm">
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            配件财务管理 &gt; 账户往来明细
            <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
            <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
            <input type="hidden" name="accountKind" id="ACCOUNT_ID" value="${accountKind }"/>
            <input type="hidden" name="dealerId" id="dealerId" value="${dealerId }"/>
        </div>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <%-- <td width="10%" align="right">服务商名称：</td>
                 <td width="30%">
                     <input class="long_txt" type="text" name="dealerName" id="dealerName" value="${dealerName }"
                            readonly="readonly" disabled="disabled"/>
                 </td>--%>
                <%--  <td width="10%" align="right" >发票号：</td>
                  <td width="20%" ><input class="long_txt" type="text" name="invoiceNO" id="invoiceNO"/></td>
                  <td width="10%" align="right" >资金类型：</td>
                  <td width="20%">
                   <script type="text/javascript">
                     genSelBoxExp("accountKind",<%=Constant.FIXCODE_CURRENCY%>,<%=Constant.FIXCODE_CURRENCY_01%>,true,"short_sel","onchange=__extQuery__(1)","false","");
                   </script>
                  </td>--%>
                <td align="right">日期：</td>
                <td align="left">
                    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" "
                           type="button"/>
                    &nbsp;至&nbsp;
                    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" "
                           type="button"/>
                </td>
                <td align="right">备注：</td>
                <td align="left"><input class="long_txt" type="text" name="remark" id="remark"/></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" value="导出" onclick="exportPartExceptionExcel()"/>
                    <input class="normal_btn" type="button" name="button1" value="关 闭" onclick="_hide();"/>
                </td>
            </tr>
        </table>
    </div>

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/financeManager/dealerAccContactDetailManager/partDealerAccConDetAction/partDealerAccConDetSearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'DETAIL_ID', renderer: getIndex},
//        {header: "服务商编码", dataIndex: 'CHILDORG_CODE'},
//        {header: "服务商名称", dataIndex: 'CHILDORG_NAME'},
//				{header: "发票号", dataIndex: 'INVOICE_NO'},
        {header: "日期", dataIndex: 'CREATE_DATE'},
        {header: "金额(元)", dataIndex: 'AMOUNT', style: 'text-align:right; padding-right:1%;'},
//				{header: "资金类型", dataIndex: 'FIN_TYPE', renderer:getItemValue},
        {header: "往来类型", dataIndex: 'FIN_TYPE'/*, renderer:importAmountType*/},
//				{header: "导入人", dataIndex: 'NAME',style:'text-align:left; padding-left:1%;'},
        {header: "备注", dataIndex: 'REMARK', style: 'text-align:left; padding-left:0.7%;'}

    ];

    //选择服务商
    function sel() {
        var parentOrgId = document.getElementById("parentOrgId").value;
        OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerSelect.do?parentOrgId=' + parentOrgId, 700, 500);
    }

    function exportPartExceptionExcel() {
        document.fm.action = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/exportFINDtl.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
</script>
</body>
</html>