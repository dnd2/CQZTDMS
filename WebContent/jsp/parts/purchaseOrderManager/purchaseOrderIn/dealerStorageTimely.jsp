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
    <title>服务商入库及时率</title>
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
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;配件销售报表&gt;服务商入库及时率
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">发运日期(出库)：</td>
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
                <td width="10%" align="right">服务商编码：</td>
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
                    汇总：<input type="radio" name="Query" value="1" checked onclick="__extQuery__(1)"/>
                    明细：<input type="radio" name="Query" value="2" onclick="__extQuery__(1)"/>
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
        var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryDealerStorageTimely.json";
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
                        {header: "服务商编码", dataIndex: 'DEALER_CODE', align: 'center'},
                        {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                        {header: "发出订单数量", dataIndex: 'TRCNT', align: 'center'},
                        {header: "及时入库订单数量", dataIndex: 'DLRCNT', align: 'center'},
                        {header: "及时率", dataIndex: 'WLRATE', align: 'center'}
                    ];

                } else {//明细
                    columns = [
                        {header: "序号", align: 'center', renderer: getIndex},
                        {header: "服务商编码", dataIndex: 'DEALER_CODE', align: 'center'},
                        {header: "服务商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
                        {header: "流水号", dataIndex: 'SO_CODE', align: 'center'},
                        {header: "提交时间", dataIndex: 'SO_DATE', align: 'center'},
                        {header: "发运时间(出库)", dataIndex: 'CREATE_DATE', align: 'center'},
                        {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
                        {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
                        {header: "约定天数", dataIndex: 'ARR_DATE', align: 'center'},
                        {header: "到货时间", dataIndex: 'ARRIVE_DAYS', align: 'center'},
                        {header: "实际到货时间", dataIndex: 'ARR_DATE2', align: 'center'},
                        {header: "验收时间", dataIndex: 'IN_DATE', align: 'center'},
                        {header: "要求验收时间", dataIndex: 'XX2', align: 'center'},
                        {header: "及时与否", dataIndex: 'DLR', align: 'center', renderer: isJs}
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

        function isJs(value, meta, record) {
            if (value > 0) {
                return '及时';
            } else {
                return '不及时';
            }
        }
        //导出
        function expPurOrderInExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expDealerStorageTimelyExcel.do";
            fm.target = "_self";
            fm.submit();
        }

    </script>
</div>
</body>
</html>