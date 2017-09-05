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
    <meta http-equiv="keywords" content="配件进销存明细">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件进销存明细</title>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;
        配件出入库明细
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">日期：</td>
                <td width="22%" align="left">
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                           datatype="1,is_date,10" maxlength="10" value="${start}"
                           style="width:65px"
                           group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${end}"
                           style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%" align="left"><input class="middle_txt" type="text" id="partOldcode" name="partOldcode"/>
                <td width="10%" align="right">库房：</td>
                <td width="20%">
                    <select id="WH_ID" name="WH_ID" class="short_sel">
                        <c:forEach items="${wareHouses}" var="wareHouse">
                            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">类型</td>
                <td width="20%" align="left">
                    <select id="selType" name="selType" class="short_sel">
                        <option value="">-请选择-</option>
                        <option value="期初库存">期初库存(0904)</option>
                        <option value="销售出库">销售出库</option>
                        <option value="采购退货出库">采购退货出库</option>
                        <option value="杂项出库">杂项出库</option>
                        <option value="移位出库">移位出库</option>
                        <option value="形态转换出库">形态转换出库</option>
                        <option value="销售退货入库">销售退货入库</option>
                        <option value="杂项入库">杂项入库</option>
                        <option value="接收入库">接收入库</option>
                        <option value="旧件验收入库">旧件验收入库</option>
                        <option value="移位入库">移位入库</option>
                        <option value="形态转换入库">形态转换入库</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导 出"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        jQuery.noConflict();
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partBuySaleStoreReport/PartBuySaleStoreAction/query.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: "PART_OLDCODE", align: 'center'},
            {header: "配件名称", dataIndex: "PART_CNAME", style:'text-align:left'},
            {header: "单号", dataIndex: "CODE", style:'text-align:left'},
            {header: "单据类型", dataIndex: "ORDER_TYPE", align: 'center'},
            {header: "业务员", dataIndex: "NAME", align: 'center'},
            {header: "日期", dataIndex: "CREATE_DATE", align: 'center'},
//            {header: "对方单位", dataIndex: "DEALER_NAME", align: 'center'},
//            {header: "货位", dataIndex: "LOC_NAME", align: 'center'},
            {header: "入库数量", dataIndex: "IN_QTY", align: 'center'},
            {header: "出库数量", dataIndex: "OUT_QTY2", align: 'center'},
//            {header: "计划价", dataIndex: "SALE_PRICE3", align: 'center'},
            {header: "库存数量", dataIndex: "ITEM_QTY", align: 'center'},
            {header: "库存金额", dataIndex: "STOCK_AMOUNT",style:'text-align:right'}
        ];
        jQuery(function () {
            loadcalendar();
            autoAlertException();
            jQuery(document).on('click', '#queryBtn', function () {
                __extQuery__(1);
            })
            jQuery(document).on('click', '#exportBtn', function () {
                fm.action = "<%=contextPath%>/report/partReport/partBuySaleStoreReport/PartBuySaleStoreAction/exportExcel.do";
                fm.submit();
            })
//            __extQuery__(1);
        })

    </script>
</div>
</body>
</html>