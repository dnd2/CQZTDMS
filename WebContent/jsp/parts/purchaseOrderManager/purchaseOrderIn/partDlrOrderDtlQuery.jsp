<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <meta http-equiv="keywords" content="服务站订货明细">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>服务站订货明细</title>
    <style type="text/css">
        .mystyle {
            background-color:#F3F4F8;
            border: none;
            color: red;
        }
    </style>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部销售报表&gt;服务站订货明细</div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right" id="deId">服务站编码：</td>
                <td align="left" nowrap="true" id="deId1">
                    <input class="middle_txt" id="dealerCode" value="${orgCodes}" name="dealerCode" type="text"/>
                    <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;"
                           onclick="showOrgDealer('dealerCode','','true','',true,'','10771002','');" value="..."/>
                    <input name="clrBtn" type="button" class="mini_btn" onclick="clrTxt('dealerCode');" value="清除"/>
                    <input type="hidden" name="DEALER_IDS" id="DEALER_IDS" value=""/>
                </td>
                <td width="10%" align="right">服务站名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="dealerName" name="dealerName"/>
                </td>
                <td align="right">选择大区：</td>
                <td>
                    <input type="text" id="orgCode" name="orgCode" value="" class="middle_txt"/>
                    <input name="obtn" id="obtn" class="mini_btn" type="button" value="&hellip;"
                           onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
                    <input class="mini_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">订货日期：</td>
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
                <td width="20%">
                    <input class="middle_txt" type="text" id="partOldCode" name="partOldCode"/>
                </td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="partCname" name="partCnaem"/>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">订单号：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="orderCode" name="orderCode"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导 出"/>
                    <div style="color: red;font-size: 15px">注：因逻辑运算较复杂，每5分钟刷新数据</div>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    总订货数量:<input type="text" id="buyQty" name="buyQty" value="0" class="mystyle" readonly/>&nbsp;
                    总订货金额:<input type="text" id="buyAmount" name="buyAmount" value="0"  class="mystyle" readonly/>&nbsp;
                    总出库数量:<input type="text" id="outQty" name="outQty" value="0"  class="mystyle" readonly/>&nbsp;
                    总出库金额:<input type="text" id="outAmount" name="outAmount" value="0" class="mystyle" readonly/>&nbsp;
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        jQuery.noConflict();
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/dlrOrderDtlQuery.json";
        var title = null;
        var columns = [
        ];
        function callBack(json) {
            var ps;
            //设置对应数据
            if (Object.keys(json).length > 0) {
                keys = Object.keys(json);
                for (var i = 0; i < keys.length; i++) {
                    if (keys[i] == "ps") {
                        ps = json[keys[i]];
                        break;
                    }
                }
            }
            $('buyQty').value = json.buyQty;
            $('buyAmount').value = json.buyAmount;
            $('outQty').value = json.outQty;
            $('outAmount').value = json.outAmount;
            //生成数据集
            if (ps.records != null) {
                    columns = [
                        {header: "序号", align: 'center', renderer: getIndex},
                        {header: "服务站编码", dataIndex: "DEALER_CODE", align: 'center'},
                        {header: "服务站名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
                        {header: "订货日期", dataIndex: "SUBMIT_DATE", align: 'center'},
                        {header: "订单号", dataIndex: "ORDER_CODE", align: 'center'},
                        {header: "流水号", dataIndex: "SO_CODE", align: 'center'},
                        {header: "配件编码", dataIndex: "PART_OLDCODE", style: 'text-align:left'},
                        {header: "配件名称", dataIndex: "PART_CNAME", style: 'text-align:left'},
                        {header: "订货数量", dataIndex: "BUY_QTY", align: 'center'},
                        {header: "发货数量", dataIndex: "OUTSTOCK_QTY", align: 'center'},
                        {header: "销售单价", dataIndex: "BUY_PRICE", style: 'text-align:right'},
                        {header: "订货金额", dataIndex: "ORDER_AMOUNT", style: 'text-align:right'},
                        {header: "发货金额", dataIndex: "SALES_AMOUNT", style: 'text-align:right'}
                    ];

                len = columns.length;

                $("_page").hide();
                $('myGrid').show();
                new createGrid(title, columns, $("myGrid"), ps).load();
                //分页
                myPage = new showPages("myPage", ps, url);
                myPage.printHtml();
            } else {
                $("_page").show();
                $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
                $("myPage").innerHTML = "";
                removeGird('myGrid');
                $('myGrid').hide();
                hiddenDocObject(1);
            }
        }
        jQuery(function () {
            loadcalendar();
            autoAlertException();
            jQuery(document).on('click', '#queryBtn', function () {
                __extQuery__(1);
            })
            jQuery(document).on('click', '#exportBtn', function () {
                fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expdlrOrderDtl.do";
                fm.submit();
            })

//            __extQuery__(1);
        })

        function clrTxt(value) {
            document.getElementById(value).value = "";
        }

    </script>
</div>
</body>
</html>