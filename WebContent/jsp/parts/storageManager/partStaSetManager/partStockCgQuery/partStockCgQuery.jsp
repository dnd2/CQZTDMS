<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<% String contextPath = request.getContextPath(); %>
<title>库存状态变更查询 </title>
<script type="text/javascript">
$(function(){searchOrdInfos("detail");});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgQueryAction/partStockCgQuerySearch.json";
var title = null;
var columns = null;
//查询
function searchOrdInfos(type) {
    document.getElementById("searchType").value = type;
    var nObj1 = document.getElementById("normal_tr1");
    var nObj2 = document.getElementById("normal_tr2");
    var dObj1 = document.getElementById("detail_tr1");
    var dObj2 = document.getElementById("detail_tr2");
    var dObj3 = document.getElementById("detail_tr3");
    var searchType = type;
    if ("normal" == searchType) {
        dObj1.style.display = "none";
        dObj2.style.display = "none";
        dObj3.style.display = "none";
        nObj1.style.display = "table-row";
        nObj2.style.display = "table-row";
        columns = [
            {header: "序号", dataIndex: 'CHANGE_ID', renderer: getIndex, align: 'center'},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
            {header: "变更单号", dataIndex: 'CHANGE_CODE', align: 'center'},
            {header: "业务类型", dataIndex: 'CHG_TYPE', align: 'center', renderer: getItemValue},
            {header: "制单单位", dataIndex: 'CHGORG_CNAME'},
            {header: "制单人", dataIndex: 'NAME', align: 'center'},
            {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
            {header: "备注", dataIndex: 'REMARK', style: 'text-align: center;'},
            {header: "完成状态", dataIndex: 'STATE', renderer: getState},
            {header: "仓库", dataIndex: 'WH_CNAME', align: 'center'},
        ];
    }else {
        nObj1.style.display = "none";
        nObj2.style.display = "none";
        dObj1.style.display = "table-row";
        dObj2.style.display = "table-row";
        dObj3.style.display = "table-row";
        columns = [
            {header: "序号", dataIndex: 'DTL_ID', renderer: getIndex, align: 'center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center', style: 'text-align: center;'},
            {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center', style: 'text-align: center;'},
            {header: "件号", dataIndex: 'PART_CODE', align: 'center', style: 'text-align: center;'},
            {header: "仓库", dataIndex: 'WH_CNAME', align: 'center', style: 'text-align: center;'},
            {header: "货位", dataIndex: 'LOC_NAME', align: 'center', style: 'text-align: center;'},
            {header: "批次号", dataIndex: 'BATCH_NO', align: 'center', style: 'text-align: center;'},
            {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center'},
            {header: "业务类型", dataIndex: 'CHANGE_REASON', align: 'center', renderer: getItemValue},
            {header: "处理方式", dataIndex: 'CHANGE_TYPE', align: 'center', renderer: getItemValue},
            {header: "调整数量", dataIndex: 'RETURN_QTY', align: 'center'},
            {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'},
            {header: "备注(原因)", dataIndex: 'REMARK', style: 'text-align: left;'},
            {header: "制单人", dataIndex: 'NAME', style: 'text-align: left;'},
            {header: "已处理数量", dataIndex: 'COLSE_QTY', align: 'center'},
            {header: "可处理数量", dataIndex: 'UNCLS_QTY', align: 'center'},
            {header: "处理日期", dataIndex: 'UPDATE_DATE', align: 'center'},
            {header: "处理原因", dataIndex: 'REMARK2', style: 'text-align: left;'},
            {header: "完成状态", dataIndex: 'STATUS', renderer: getState},
        ];
    }
    __extQuery__(1);
}


//设置超链接
function myLink(value, meta, record) {
    var changeId = record.data.CHANGE_ID;
    return String.format("<a href=\"#\" onclick='viewDetail(\"" + changeId + "\")'>[查看]</a>");
}

function getState(value, meta, record) {
    var unfiState = "1";
    if (unfiState == value) {
        return String.format("未完成");
    }
    return String.format("<font color='red'>已完成</font>");

}

//查看
function viewDetail(parms) {
    btnDisable();
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgQueryAction/viewStockDeatilInint.do?changeId=" + parms;
    document.fm.target = "_self";
    document.fm.submit();
}

//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgQueryAction/exportPartStockStatusExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

//汇总导出
function exportStaSetDetExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgQueryAction/exportStaSetDetExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

</script>
</head>
<body>
<form method="post" name="fm" id="fm">
    <div class="wbox">
        <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
        <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
        <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
        <input type="hidden" name="searchType" id="searchType" value="normal"/>
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>
            &nbsp;当前位置： 配件仓库管理 &gt; 库存状态变更 &gt; 库存状态变更查询
        </div>
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
		<div class="form-body">
        <table class="table_query">
            <tr id="normal_tr1">
                <td class="right">变更单号：</td>
                <td>
                    <input class="middle_txt" type="text" name="changeCode" id="changeCode"/>
                </td>
                <td class="right">制单日期：</td>
                <td>
                    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button"/>
                    &nbsp;至&nbsp;
                    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate"/>
                    <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button"/>
                </td>
                <td class="right">仓库：</td>
                <td>
                    <select name="whId" id="whId" class="u-select" onchange="__extQuery__(1)">
                        <option value="">-请选择-</option>
                        <c:if test="${WHList!=null}">
                            <c:forEach items="${WHList}" var="list">
                                <option value="${list.WH_ID }">${list.WH_CNAME }</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
            </tr>
            <tr id="normal_tr2">
                <td class="right">备注：</td>
                <td>
                    <input class="middle_txt" type="text" name="remark1" id="remark1"/>
                </td>
                <td class="right">完成状态：</td>
                <td>
                    <select name="isFinish1" id="isFinish1" class="u-select" onchange="__extQuery__(1)">
                        <option value="" selected="selected">-请选择-</option>
                        <option value="1">-未完成-</option>
                        <option value="0">-已完成-</option>
                    </select>
                </td>
                <td class="right">业务类型：</td>
                <td>
                    <script type="text/javascript">
                        genSelBoxExp("businessType1", <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>, "", true, "", "onchange='__extQuery__(1)'", "false", "");
                    </script>
                </td>
            </tr>
            <tr id="detail_tr1">
                <td class="right">配件编码：</td>
                <td>
                    <input class="middle_txt" type="text" name="partOldcode" id="partOldcode"/>
                </td>
                <td class="right">配件名称：</td>
                <td>
                    <input class="middle_txt" type="text" name="partCname" id="partCname"/>
                </td>
                <td class="right">备注：</td>
                <td>
                    <input class="middle_txt" type="text" name="remark2" id="remark2"/>
                </td>
            </tr>
            <tr id="detail_tr2">
                <td class="right">处理日期：</td>
                <td>
                    <input id="handleSDate" class="short_txt" name="handleSDate" datatype="1,is_date,10" maxlength="10"
                           group="handleSDate,handleEDate" style="width:80px;"/>
                    <input class="time_ico" onclick="showcalendar(event, 'handleSDate', false);" value=" "
                           type="button"/>
                    &nbsp;至&nbsp;
                    <input id="handleEDate" class="short_txt" name="handleEDate" datatype="1,is_date,10" maxlength="10"
                           group="handleSDate,handleEDate" style="width:80px;"/>
                    <input class="time_ico" onclick="showcalendar(event, 'handleEDate', false);" value=" "
                           type="button"/>
                </td>
                <td class="right">业务类型：</td>
                <td>
                    <script type="text/javascript">
                        genSelBoxExp("businessType2", <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>, "", true, "", "onchange='__extQuery__(1)'", "false", "");
                    </script>
                </td>
                <td class="right">处理类型：</td>
                <td>
                    <script type="text/javascript">
                        genSelBoxExp("changeType", <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE%>, "", true, "", "onchange='__extQuery__(1)'", "false", "");
                    </script>
                </td>
            </tr>
            <tr id="detail_tr3">
                <td class="right">完成状态：</td>
                <td>
                    <select name="isFinish2" id="isFinish2" class="u-select" onchange="__extQuery__(1)">
                        <option value="" selected="selected">-请选择-</option>
                        <option value="1">-未完成-</option>
                        <option value="0">-已完成-</option>
                    </select>
                </td>
                <td class="right">制单人：</td>
                <td>
                    <input class="middle_txt" type="text" name="maker" id="maker" value=""/>
                </td>
                <td class="right"></td>
                <td></td>
            </tr>
            <tr>
                <td class="center" colspan="6">
                    <input class="u-button" type="button" value="明细查询" name="BtnQuery" id="queryDtlBtn"  onclick="searchOrdInfos('detail')"/>
                    <input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchOrdInfos('normal')"/>
                    <input class="u-button" type="reset" value="重 置" onclick="reset()"/>
                    <input class="u-button" type="button" value="明细导出" onclick="exportStaSetDetExcel()"/>
                    <input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()"/>
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
</body>
</html>