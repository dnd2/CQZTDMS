<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购入库确认</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
        当前位置：配件管理&gt;采购计划管理&gt;采购入库确认
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="dtlQuery">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">验收单号:</td>
                <td width="20%" align="left">
                    <INPUT class="middle_txt" type="text" id="CHECK_CODE2" name="CHECK_CODE2"></td>
                <td width="10%" align="right">配件编码:</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/>
                </td>
                <td width="10%" align="right">配件名称:</td>
                <td width="20%" align="left">
                    <input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/>
                </td>
            </tr>
                <tr>
                    <td width="10%" class="table_query_right" align="right">验收日期:</td>
                    <td width="20%" align="left">
                        <input name="beginTime2" id="t5" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t5,t6">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't5', false);"/>
                        至
                        <input name="endTime2" id="t6" value="" type="text" class="short_txt" datatype="1,is_date,10"
                               group="t5,t6">
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 't6', false);"/>
                    </td>
                    <td width="10%" class="table_query_right" align="right">订单来源：</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        genSelBoxExp("ORDER_ORIGIN_TYPE1", <%=Constant.ORDER_ORIGIN_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                </tr>
            <tr>
                <td align="center" colspan="6">

                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input name="PrintBtn" id="PrintBtn" class="normal_btn" type="button" value="打印"
                           onclick="printPurDtlOrder();"/>
                    <input name="backBtn" id="backBtn" class="normal_btn" type="button" value="返回"
                           onclick="myBack();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkInfo.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "<input type='checkbox' name='checkAll' id='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'PO_ID', renderer: myCheckBox},
            {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left'},
            {header: "采购单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
            {header: "订单来源", dataIndex: 'ORIGIN_TYPE', align: 'center', renderer: getItemValue},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
            {header: "配件种类", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "验货数量", dataIndex: 'CHECK_QTY', align: 'center'},
            {header: "待入库数量", dataIndex: 'SPAREIN_QTY', align: 'center'},
            {header: "已入库数量", dataIndex: 'IN_QTY', align: 'center'},
            {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
            {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
            {header: "预计到货日期", dataIndex: 'FORECAST_DATE', align: 'center', renderer: formatDate},
            {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
        ];

        var len = columns.length;
        //设置超链接  begin
        
        //全选checkbox
        function myCheckBox(value, meta, record) {
            return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>");
        }

        function myBack() {
            window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/purchaseOrderChkQueryInit.do";
        }
        
        function chkPart() {
            var cks = document.getElementsByName('ids');
            var flag = true;
            for (var i = 0; i < cks.length; i++) {
                var obj = cks[i];
                if (!obj.checked) {
                    flag = false;
                }
            }
            document.getElementById("checkAll").checked = flag;
        }
        //打印
        function printPurDtlOrder() {
        	var ids = '';
        	var chk = document.getElementsByName("ids");
            var l = chk.length;
            var cnt = 0;
            for (var i = 0; i < l; i++) {
                if (chk[i].checked) {
                	ids += chk[i].value+',';
                    cnt++;
                }
            }
            if (cnt == 0) {
                MyAlert("请选择要打印的配件！");
                return;
            }

            window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opDtlPrintHtml.do?ids="+ids,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
        }
        
        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

    </script>
</div>
</body>
</html>