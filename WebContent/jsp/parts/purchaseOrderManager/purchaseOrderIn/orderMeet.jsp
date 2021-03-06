<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>订单满足率</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件销售报表&gt;订单满足率
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="center" colspan="6">订货日期：
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
            </tr>
            <tr id="dtl" style="display: none">
                <td width="10%" align="right">订单号：</td>
                <td><input type="text" name="orderCode" id="orderCode" value="" class="middle_txt"/></td>
                <td width="10%" align="right">流水号：</td>
                <td><input type="text" name="soCode" id="soCode" value="" class="middle_txt"/></td>
                <td width="10%" align="right">配件编码：</td>
                <td><input type="text" name="partOldCode" id="partOldCode" value="" class="middle_txt"/></td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    汇总：<input type="radio" name="Query" value="1" checked onclick="showDtl(this)"/>
                    未满足明细：<input type="radio" name="Query" value="2" onclick="showDtl(this)"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryOrderMeet.json";

        var title = null;

        var columns = [];

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
            //生成数据集
            if (ps.records != null) {
                var chk = 0;
                var chkObjs = document.getElementsByName("Query");
                for (var i = 0; i < chkObjs.length; i++) {
                    if (chkObjs[i].checked) {
                        chk = chkObjs[i].value;
                        break;
                    }
                }
                if (chk == 1) {//
                    columns = [
                        {header: "序号", align: 'center', renderer: getIndex},
                        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center'},
                        {header: "订货品种", dataIndex: 'DH_PARTCNT', align: 'center'},
                        {header: "发运品种", dataIndex: 'FY_PARTCNT', align: 'center'},
                        {header: "品种满足率(一次)", dataIndex: 'PARTRATE', align: 'center'},
                        {header: "订货数量", dataIndex: 'DH_QTYSUM', align: 'center'},
                        {header: "发运数量", dataIndex: 'FY_QTYSUM', align: 'center'},
                        {header: "数量满足率(多次)", dataIndex: 'QTYRATE', align: 'center'}
                    ];

                } else {//明细
                    columns = [
                        {header: "序号", align: 'center', renderer: getIndex},
                        {header: "服务商编码", dataIndex: 'DEALER_CODE', align: 'center'},
                        {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                        {header: "订单类型", dataIndex: 'ORDER_TYPE1', align: 'center'},
                        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
                        {header: "流水号", dataIndex: 'SO_CODE', align: 'center'},
                        {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
                        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
                        {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
                        {header: "发运数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'}
                    ];
                }
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
        //格式化日期
        function formatDate(value, meta, record) {
            var output = value.substr(0, 10);
            return output;
        }

        //导出
        function expPurOrderInExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expOrderMeetExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        //隐藏查询条件
        function showDtl(obj) {
            if (obj.value == '1') {
//                document.getElementById("dtl").style.display = "none";
            } else {
//                document.getElementById("dtl").style.display = "";
            }
//            delObj("myGrid");
//            delObj("myPage");
            __extQuery__(1);
        }
        function delObj(obj) {
            var ob = document.getElementById(obj);
            ob.innerHTML = "";
            ob.style.display = "none";
        }
    </script>
</div>
</body>
</html>