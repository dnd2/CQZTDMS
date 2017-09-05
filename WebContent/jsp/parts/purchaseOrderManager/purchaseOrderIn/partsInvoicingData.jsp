<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件进销存数据统计</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：报表管理&gt;配件报表&gt;本部仓储报表&gt;配件进销存报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">日期：</td>
                <td width="22%" align="left">
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                           datatype="1,is_date,10" maxlength="10" value=""
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
                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>

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
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="query(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center" style="color: red;font-weight: bold">
                    提示:系统默认期初日期为起始日期减1天，如起始日期2014-10-10，期初日期为2014-10-09。期初日期请选择2014年8月20日以后，以前数据有部分不准确。
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryPartsInvoicingData.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "期初数量", dataIndex: 'QC_QTY', style: 'text-align:center'},
            {header: "入库数量", dataIndex: 'IN_QTY', style: 'text-align:center', renderer: inView},
            {header: "出库数量", dataIndex: 'OUT_QTY', style: 'text-align:center', renderer: outView},
            {header: "末期数量", dataIndex: 'JS_QTY', style: 'text-align:center'},
            {header: "当前库存数量", dataIndex: 'ITEM_QTY', align: 'center'}
//            {header: "差异", dataIndex: 'CY', style: 'text-align:center'}
        ];

        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        //导出
        function expPurOrderInExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expPartsInvoicingDataExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        //入库明细查询
        function inView(value, meta, record) {
            var inPage = "inPage";
            var partId = record.data.PART_ID;
            var sDate = document.getElementById("SCREATE_DATE").value;
            var eDate = document.getElementById("ECREATE_DATE").value;
            var wh_id = document.getElementById('WH_ID').value;
            if (sDate == "" || eDate == "") {
                MyAlert("必须指定要查询的期初和期末日期!");
                return;
            }
            return String.format("<a href=\"#\" onclick='showInDtl(\"" + partId + "\",\"" + wh_id + "\",\"" + '' + "\",\"" + inPage + "\",\"" + sDate + "\",\"" + eDate + "\")'>[<span style='color: red;font-weight: bold'>" + value + "</span>]</a>");
        }
        //入库查询弹出页面
        function showInDtl(partId, whId, locId, viewPage, sDate, eDate) {
            OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetInit.do?partId=' + partId + '&whId=' + whId + '&locId=' + locId + '&viewPage=' + viewPage + '&sDate=' + sDate + '&eDate=' + eDate, 950, 500);
        }

        function outView(value, meta, record) {
            var outPage = "outPage";
            var partId = record.data.PART_ID;
            var sDate = document.getElementById("SCREATE_DATE").value;
            var eDate = document.getElementById("ECREATE_DATE").value;
            var wh_id = document.getElementById('WH_ID').value;
            if (sDate == "" || eDate == "") {
                MyAlert("必须指定要查询的期初和期末日期!");
                return;
            }
            return String.format("<a href=\"#\" onclick='showInDtl(\"" + partId + "\",\"" + wh_id + "\",\"" + '' + "\",\"" + outPage + "\",\"" + sDate + "\",\"" + eDate + "\")'>[<span style='color: red;font-weight: bold'>" + value + "</span>]</a>");
        }

        function query() {
            var sDate = document.getElementById("SCREATE_DATE").value;
            var eDate = document.getElementById("ECREATE_DATE").value;
            if (sDate == "" || eDate == "") {
                MyAlert("必须指定要查询的期初和期末日期!");
                return;
            }
            __extQuery__(1);
        }
        MyAlert(ne)
    </script>
</div>
</body>
</html>