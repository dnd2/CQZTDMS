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
    <title>出库单查询</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstockQuery/partOutstockQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'OUT_ID', renderer: myLink, align: 'center'},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'center', style: 'text-align:left'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "销售单号", dataIndex: 'SOCODE', align: 'center'},
        {header: "出库单号", dataIndex: 'OUT_CODE', align: 'center'},
        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
        {header: "制单人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
        {header: "出库日期", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "拣货日期", dataIndex: 'PICK_ORDER_CREATE_DATE', align: 'center'},
        /*{header: "销售单位", dataIndex: 'SELLER_NAME', align:'center'},*/
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
        {header: "销售金额", dataIndex: 'AMOUNT', align: 'center'},
        {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
        {header: "开票日期", dataIndex: 'BILL_DATE', align: 'center'},
        {header: "发票号", dataIndex: 'BILL_NO', align: 'center'}
        //{header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    ];
    function myLink(value, meta, record) {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>");
    }

    //查看
    function detailOrder(value) {
        //disableAllClEl();
        //window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstockQuery/partOutstockDetail.do?outId=" + value;
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstockQuery/partOutstockDetail.do?outId=" + value + "&flag=" + 1, 900, 500)
    }
    //作废
    function cancelOrder(value) {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstockQuery/cancelOrder.do?outId=" + value;
    }
    function genSelBoxExpMy(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
        var str = "";
        var arr;
        if (expStr.indexOf(",") > 0)
            arr = expStr.split(",");
        else {
            expStr = expStr + ",";
            arr = expStr.split(",");
        }
        str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
        // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
        if (nullFlag && nullFlag == "true") {
            str += " datatype='0,0,0' ";
        }
        // end
        str += " onChange=doCusChange(this.value);> ";
        if (setAll) {
            str += genDefaultOpt();
        }
        for (var i = 0; i < codeData.length; i++) {
            var flag = true;
            for (var j = 0; j < arr.length; j++) {
                if (codeData[i].codeId == arr[j] && codeData[i].codeId !=<%=Constant.CAR_FACTORY_PKG_STATE_01%>) {
                    flag = false;
                }
            }
            if (codeData[i].type == type && flag && codeData[i].codeId !=<%=Constant.CAR_FACTORY_PKG_STATE_01%>) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }
        str += "</select>";

        document.write(str);
    }
    function doQuery() {
        var SstartDate = document.getElementById("SstartDate");
        var SendDate = document.getElementById("SendDate");
        var msg = "";
        if (SstartDate == "") {
            if (SendDate != "") {
                msg += "请填写销售开始时间!"
            }
        } else {
            if (SendDate == "") {
                msg += "请填写销售结束时间!"
            }
        }
        if (msg != "") {
            MyAlert(msg);
            return;
        }
        __extQuery__(1);
    }
    function exportExcel(value) {
        document.getElementById("flag").value = value;
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstockQuery/exportExcel.do";
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
    $(document).ready(function(){
    	/* __extQuery__(1); */
    	enableAllClEl();
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="flag" id="flag"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 > 配件销售管理 >出库单查询</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td width="10%" class="right">出库单号：</td>
	                <td width="20%"><input type="text" class="middle_txt" name="outCode" id="outCode"></td>
	                <td width="10%" class="right">订货单位编码：</td>
	                <td width="20%"><input type="text" class="middle_txt" id="dealerCode" name="dealerCode"></td>
	                <td width="10%" class="right">订货单位：</td>
	                <td width="20%"><input type="text" class="middle_txt" id="dealerName" name="dealerName"></td>
	            </tr>
	            <tr>
	                <%--<td width="10%" class="right">出库单号：</td>
	                <td width="20%"><input type="text" class="middle_txt" name="outCode" id="outCode"></td>--%>
	                <td width="10%" class="right">出库日期：</td>
	                <td width="25%"><input name="SstartDate" type="text" class="short_txt" id="SstartDate"
	                                       value="${old}" style="width:80px;"/>
	                    <input name="button23" type="button" class="time_ico" value=" "/>
	                    至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" value="${now}" style="width:80px;"/>
	                    <input name="button222" type="button" class="time_ico" value=" "/></td>
	                <td width="10%" class="right">出库仓库：</td>
	                <td width="20%">
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                <td width="10%" class="right">订单类型：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "u-select", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">开票日期：</td>
	                <td width="22%"><input name="bSstartDate" type="text" class="short_txt" style="width:80px;" id="bSstartDate" value=""/>
	                    <input name="button23" type="button" class="time_ico" value=" "/>
	                    至
	                    <input name="bSendDate" type="text" class="short_txt" id="bSendDate" value="" style="width:80px;"/>
	                    <input name="button222" type="button" class="time_ico" value=" "/></td>
	                <td width="10%" class="right">发票号：</td>
	                <td width="20%"><input type="text" class="middle_txt" name="billNo" id="billNo"></td>
	                <td width="10%" class="right">是否已开票：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExpMy("state", <%=Constant.IF_TYPE%>, "", true, "u-select", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">拣货日期：</td>
	                <td width="22%"><input name="pSstartDate" type="text" class="short_txt" style="width:80px;" id="pSstartDate" value=""/>
	                    <input name="button23" type="button" class="time_ico" value=" "/>
	                    至
	                    <input name="pSendDate" type="text" class="short_txt" id="pSendDate" value="" style="width:80px;"/>
	                    <input name="button222" type="button" class="time_ico" value=" "/></td>
	                <td width="10%" class="right">销售单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="soCode" id="soCode"/></td>
	                <td width="10%" class="right">拣货单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="pickOrderId" id="pickOrderId"/></td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
	                                                      value="查 询" onclick="doQuery();"/>
	                    <input class="normal_btn" type="button" value="导 出" onclick="exportExcel('1');"/>
	                    <input class="normal_btn" type="button" value="明细导出" onclick="exportExcel('2');"/></td>
	            </tr>
	        </table>
	    </div>    
	    </div>    
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
</form>
</body>
</html>