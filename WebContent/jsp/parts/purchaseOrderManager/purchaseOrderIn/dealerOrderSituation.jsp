<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
    <title>销售报表</title>
    <style type="text/css">
        .mystyle {
            background-color: #F3F4F8;
            border: none;
            width: 65px;
            color: red;
        }
    </style>
    <script language="JavaScript">
    var Options = {
    		cells  : 3
    	}
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件销售报表&gt;销售报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">日期：</td>
                <td width="25%">
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10" maxlength="10"
                           value="${old}" style="width:65px"
                           group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${now}"
                           style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
                <td width="10%" align="right">服务站代码：</td>
                <td width="20%">
                    <input type="text" class="middle_txt" id="dealer_code" name="dealer_code"/>
                </td>
                <td width="10%" align="right">服务站名称：</td>
                <td width="20%">
                    <input type="text" class="middle_txt" id="dealer_name" name="dealer_name"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" style="font-size: 12px;color: red;text-align: center">
                    <ol>
                        <li>此处的查询日期为自然月日期</li>
                        <li>出库信息和订货信息统计发生日期内的常规、紧急、特殊、切换订单数据</li>
                        <li>切换件金额、特殊定件金额为出库金额</li>
                    </ol>
                </td>
            </tr>
         <%--   <tr>
                <td colspan="6" align="center">
                    总出库品种:<input type="text" id="PARTCNT" name="PARTCNT" value="0" class="mystyle" readonly/>&nbsp;
                    总出库数量:<input type="text" id="PARTQTY" name="PARTQTY" value="0" class="mystyle" readonly/>&nbsp;
                    总订货金额:<input type="text" id="ORDER_AMOUNT" name="ORDER_AMOUNT" value="0" class="mystyle" readonly/>&nbsp;
                    总缺件金额:<input type="text" id="BO_AMOUNT" name="BO_AMOUNT" value="0" class="mystyle" readonly/>&nbsp;
                    总退货金额:<input type="text" id="RETURN_AMOUNT" name="RETURN_AMOUNT" value="0" class="mystyle"
                                 readonly/>&nbsp;
                    总切换件金额:<input type="text" id="QH_AMOUNT" name="QH_AMOUNT" value="0" class="mystyle" readonly/>&nbsp;
                    总特殊定件金额:<input type="text" id="TS_AMOUNT" name="TS_AMOUNT" value="0" class="mystyle" readonly/>&nbsp;
                    总实际销售金额:<input type="text" id="AMOUNT" name="AMOUNT" value="0" class="mystyle" readonly/>&nbsp;
                </td>
            </tr>--%>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryDealerOrderSituation.json";

        var title = null;

        var columns = [];
//        var calculateConfig = {totalColumns:"DEALER_NAME"};

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
           /* $('PARTCNT').value = json.PARTCNT;
            $('PARTQTY').value = json.PARTQTY;
            $('ORDER_AMOUNT').value = json.ORDER_AMOUNT;
            $('BO_AMOUNT').value = json.BO_AMOUNT;
            $('RETURN_AMOUNT').value = json.RETURN_AMOUNT;
            $('QH_AMOUNT').value = json.QH_AMOUNT;
            $('TS_AMOUNT').QH_AMOUNT = json.TS_AMOUNT;
            $('AMOUNT').value = json.AMOUNT;*/
            //生成数据集
            if (ps.records != null) {
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {header: "服务商编码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
                    {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                    {header: "出库品种", dataIndex: 'PARTCNT', align: 'center'},
                    {header: "出库数量", dataIndex: 'PARTQTY', align: 'center'},
                    {header: "订货金额", dataIndex: 'ORDER_AMOUNT', style: 'text-align:right'},
                    {header: "缺件金额", dataIndex: 'BO_AMOUNT', style: 'text-align:right'},
                    {header: "退货金额", dataIndex: 'RETURN_AMOUNT', style: 'text-align:right'},
                    {header: "切换件金额", dataIndex: 'QH_AMOUNT', style: 'text-align:right'},
                    {header: "特殊定件金额", dataIndex: 'TS_AMOUNT', style: 'text-align:right'},
                    {header: "实际销售金额", dataIndex: 'AMOUNT', style: 'text-align:right'}
                ];

                len = columns.length;

                $("_page").hide();
                $('myGrid').show();
                new createGrid(title, columns, $("myGrid"), ps).load();
                //分页
                var myTable = $('myTable');
                var tmpRow = myTable.insertRow(myTable.rows.length);
                tmpRow.insertCell(0).innerHTML="<TR class=table_list_row1><TD</TD>";
                tmpRow.insertCell(1).innerHTML="<TD></TD>";
                tmpRow.insertCell(2).innerHTML="<TD >合计</TD>";
                tmpRow.insertCell(3).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.PARTCNT+"</TD>";
                tmpRow.insertCell(4).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.PARTQTY+"</TD>";
                tmpRow.insertCell(5).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.ORDER_AMOUNT+"</TD>";
                tmpRow.insertCell(6).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.BO_AMOUNT+"</TD>";
                tmpRow.insertCell(7).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.RETURN_AMOUNT+"</TD>";
                tmpRow.insertCell(8).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.QH_AMOUNT+"</TD>";
                tmpRow.insertCell(9).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.TS_AMOUNT+"</TD>";
                tmpRow.insertCell(10).innerHTML="<TD style='TEXT-ALIGN: right'>"+json.AMOUNT+"</TD></TR>";
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
        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        //导出
        function expPurOrderInExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expDealerOrderSituationExcel.do";
            fm.target = "_self";
            fm.submit();
        }

    </script>
</div>
</body>
</html>