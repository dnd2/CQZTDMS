<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "xhtml11.dtd">
<html>
<%String contextPath = request.getContextPath();%>
<head>
<title>Highcharts Demo Gallery</title>
<script type="text/javascript">

    function getYearSelect(id, name, scope, value) {
        var date = new Date();
        var year = date.getFullYear();    //获取完整的年份
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:55px;'>";
        //str += "<option selected value=''>-请选择-</option>";
        for (var i = (year - scope); i <= (year + scope); i++) {
            if (value == "") {
                if (i == year) {
                    str += "<option  selected value =" + i + ">" + i + "</option >";
                } else {
                    str += "<option   value =" + i + ">" + i + "</option >";
                }
            } else {
                str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
            }
        }
        str += "</select> 年";
        document.write(str);
    }
    function getQuarterSeasonSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth();//获取当前月
        var tmp;
        if (month < 3) {
            tmp = 1;
        } else if (month < 6) {
            tmp = 2;
        } else if (month < 9) {
            tmp = 3;
        } else {
            tmp = 4;
        }
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;'  onchange='changeMon(this);'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 4; i++) {
            if (value == "") {
                str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                /*if (i == tmp) {
                 str += "<option selected value =" + i + ">第" + convertToChinese(i) + "</option >";
                 } else {
                 str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                 }*/
            }
        }
        str += "</select> 季度";
        document.write(str);
    }
    function getMonThSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth() + 1;
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;' onchange='changeSeason(this);'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 12; i++) {
            if (value == "") {
                str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                /*if (i == month) {
                 str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                 } else {
                 str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                 }*/
            }
        }
        str += "</select> 月";
        document.write(str);
    }
    function convertToChinese(num) {
        var N = [
            "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
        ];
        var str = num.toString();
        var len = num.toString().length;
        var C_Num = [];
        for (var i = 0; i < len; i++) {
            C_Num.push(N[str.charAt(i)]);
        }
        return C_Num.join('');
    }
    function changeMon(obj) {
        if (obj.value) {
            $("MYMONTH").value = "";
        }
    }
    function changeSeason(obj) {
        if (obj.value) {
            $("MYSEASON").value = "";
        }
    }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>

<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/highcharts.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/exporting.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/scripts.js"></script>
<%--<script type="text/javascript">
    var example = 'combo', theme = 'default';
</script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/gray.js"></script>--%>
<script type="text/javascript">
    jQuery.noConflict();
    var myPage;
    var url = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesReport/querypartStockAnalysisInit.json";
    var title = null;
    var columns = [

    ];
    var chart;
    (function ($) { // encapsulate jQuery
        $(document).ready(function () {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container',
                    type: 'line',
                   /* marginRight: 100,*/
                    marginBottom: 25
                },
                title: {
                    text: '配件进销存分析',
                    x: -20, //center
                    style:{
                        color: '#3E576F',
                        fontSize: '20px'
                    }
                },
                subtitle: {
                    x: -20
                },
                xAxis: {
                    categories: []
                },
                yAxis: {
                    title: {
                        text: '数量'
                    },
                    min: 0,
                    plotLines: [
                        {
                            value: 0,
                            width: 1,
                            color: '#a6c96a'
                        }
                    ]
                },
                tooltip: {
                    formatter: function () {
                        return '<b>' + this.series.name + '</b><br/>' +
                                this.x + ': ' + this.y + '';
                    }
                },
               /* legend: {
                    layout: 'vertical',
                    align: 'cen',
                    verticalAlign: 'top',
                    x: -10,
                    y: 100,
                    borderWidth: 0
                },*/
                exporting: {
                    enabled: false
                },

                series: [
                    {
                        name: '进货',
                        data: [],
                        color: '#436EEE'
                    },
                    {
                        name: '销售',
                        data: [],
                        color: '#EE0000'
                    },
                    {
                        name: '库存',
                        data: [],
                        color:'#00CD00'
                    }
                ]
            });
        });

    })(jQuery);

   /* function requestData() {
        $.ajax({
            url: "http://127.0.0.1:8080/cfma/DMSReportServiceOfJson",
            type: "POST",
            dataType: "json",
            success: function (data) {
                chart.xAxis[0].setCategories(data.Data.CATEGORIES);
                chart.series[0].setData(data.Data.OEM_STOCK);
                chart.series[1].setData(data.Data.OEM_WS_TARGET);
                chart.series[2].setData(data.Data.OEM_WS_Y_ACC);
                chart.series[3].setData(data.Data.OEM_DS_TARGET);
                chart.series[4].setData(data.Data.OEM_DS_Y_ACC);
                chart.series[5].setData(data.Data.DLR_WS_TARGET);
                chart.series[6].setData(data.Data.DLR_WS_Y_ACC);
                chart.series[7].setData([ 3000, 2670, 3333, 6303, 3303, 6000, 7888, 8888 ]);
                chart.redraw();
                setTimeout(requestData, 5000);
            },
            cache: false
        });
    }*/
    function callBack(json) {
        var ps;
        //设置对应数据
        chart.setTitle(null, { text: '配件编码：'+json.PartOldcode });
        chart.xAxis[0].setCategories(json.Date);
        chart.series[0].setData(json.In);
        chart.series[1].setData(json.Out);
        chart.series[2].setData(json.Stock);
    }
    function query() {
        if (jQuery("#PARTOLDCODE").val() == "") {
            MyAlert("配件编码必填!");
            return false;
        }
        __extQuery__(1);
    }
    function showPartInfo(inputOldCode,isMulti){
        if(!inputOldCode){
            inputOldCode = null;
        }
        OpenHtmlWindow("<%=contextPath%>/jsp/report/partSalesReport/partOffSelect.jsp?INPUTOLDCODE="+inputOldCode+"&ISMULTI="+isMulti+"&FLAG=1",850,400);
    }
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部销售报表&gt;销售退货报表(本部)
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="25%">
                <%--<td width="25%"><input name="PARTOLDCODE" type="text" class="long_txt" id="PARTOLDCODE" value="A34-00511"/></td>--%>
                <input class="middle_txt" type="text"  id="PARTOLDCODE" name="PARTOLDCODE" datatype="0,is_null"/>
                <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo('PARTOLDCODE','false')"/>
                </td>
                <td width="10%" align="right">查询日期：</td>
                <td width="20%">
                    <script type="text/javascript">
                        getYearSelect("MYYEAR", "MYYEAR", 1, '');
                    </script>
                    <%-- <script type="text/javascript">
                         getQuarterSeasonSelect("MYSEASON", "MYSEASON", '');
                     </script>--%>
                    <script type="text/javascript">
                        getMonThSelect("MYMONTH", "MYMONTH", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="query();"/>
                </td>
            </tr>
        </table>
        <div id="container"
             style="position: relative; left: 5px; width: 1024px; height: 350px; margin: 0 auto;"></div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
    <script type="text/javascript">
        Highcharts.theme = { colors: ['#4572A7'] };// prevent errors in default theme
        var highchartsOptions = Highcharts.getOptions();
    </script>

</div>
</body>
</html>

