<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购订单查询</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
        当前位置：配件管理&gt; 采购订单管理&gt; 采购订单查询
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="wareHouses" id="wareHouses" value="${wareHouses }"/>
        <table class="table_query" bordercolor="#DAE0EE">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">采购订单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE"/></td>
                <td width="10%" align="right">验收单号：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" name="CHECK_CODE"/></td>

                <td width="10%" align="right">入库单号：</td>
                <td width="30%"><input class="middle_txt" type="text" name="IN_CODE"/></td>
            </tr>
            <tr>
                <td width="10%" class="table_query_right" align="right">库房：</td>
                <td width="20%">
                    <select id="WH_ID" name="WH_ID" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:forEach items="${wareHouses}" var="wareHouse">
                            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                        </c:forEach>
                    </select>
                </td>
                <td width="10%" class="table_query_right" align="right">配件种类：</td>
                <td width="20%">
                    <script type="text/javascript">
                        genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                <td width="10%" class="table_query_right" align="right">供应商：</td>
                <td width="30%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                    <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                </td>
            </tr>
            <tr>
                <td width="10%" class="table_query_right" align="right">配件编码：</td>
                <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
                <td width="10%" class="table_query_right" align="right">配件名称：</td>
                <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>

                <!-- <td width="10%" class="table_query_right" align="right">制单时间：</td>
                <td width="30%">
                    <div align="left">
                        <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t1,t2">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't1', false);"/>
                        至
                        <input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t1,t2">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't2', false);"/>
                    </div>
                </td> -->
            </tr>

            <tr>
                <td width="10%" class="table_query_right" align="right">入库时间：</td>
                <td width="22%">
                    <div align="left">
                        <input name="inBeginTime" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t3,t4">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't3', false);"/>
                        至
                        <input name="inEndTime" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t3,t4">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't4', false);"/>
                    </div>
                </td>
                <td width="10%" class="table_query_right" align="right">结算时间：</td>
                <td width="22%">
                    <div align="left">
                        <input name="balanceBeginTime" id="t5" value="" type="text" class="short_txt"
                               datatype="1,is_date,10" group="t5,t6">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't5', false);"/>
                        至
                        <input name="balanceEndTime" id="t6" value="" type="text" class="short_txt"
                               datatype="1,is_date,10" group="t5,t6">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't6', false);"/>
                    </div>
                </td>

                <td width="10%" class="table_query_right" align="right">验货时间：</td>
                <td width="26%">
                    <div align="left">
                        <input name="checkBeginTime" id="t7" value="" type="text" class="short_txt"
                               datatype="1,is_date,10" group="t7,t8">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't7', false);"/>
                        至
                        <input name="checkEndTime" id="t8" value="" type="text" class="short_txt"
                               datatype="1,is_date,10" group="t7,t8">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't8', false);"/>
                    </div>
                </td>
            </tr>

            <tr>
                <td align="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
                                                      onclick="__extQuery__(1);" value="查询"/>
                    &nbsp;<input name="button" type="button" class="normal_btn" onclick="expPurchaseOrderInDiffExcel();"
                                 value="导出"/>&nbsp;&nbsp;    </td>
            </tr>
        </table>

        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInDiffQuery/queryPurchaseOrderInDiffInfo.json";
        var title = null;
        var columns = [
            {header: "序号", renderer: getIndex},
            {header: "采购订单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
            {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left'},
            {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align:left'},
            {header: "采购员", dataIndex: 'BUYER', align: 'center'},
            {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "订购数量", dataIndex: 'BUY_QTY', style: 'text-align:center'},
            {header: "已验货数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center'},
            {header: "入库差异数量", dataIndex: 'INDIFF_QTY', align: 'center'},
            {header: "已结算数量", dataIndex: 'BALANCE_QTY', align: 'center'},
            {header: "结算单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
            {header: "结算金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
            {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "验货日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
            {header: "入库日期", dataIndex: 'IN_DATE', align: 'center', renderer: formatDate},
            {header: "结算日期", dataIndex: 'BALANCE_DATE', align: 'center', renderer: formatDate}
        ];

        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        function clearInput() {//清空选定供应商
            document.getElementById("VENDER_ID").value = '';
            document.getElementById("VENDER_NAME").value = '';
        }

        //导出
        function expPurchaseOrderInDiffExcel() {
            fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInDiffQuery/expPurchaseOrderInDiffExcel.do";
            fm.target = "_self";
            fm.submit();
        }
    </script>
</div>
</body>
</html>