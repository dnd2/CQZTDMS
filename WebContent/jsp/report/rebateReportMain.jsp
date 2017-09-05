<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<script type="text/javascript">
	var Options = {
		cells  : 3
	}
</script>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>返利报表显示页面</title>
</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 报表管理 &gt; 配件报表 &gt; 本部销售报表
            &gt; 返利报表
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr>
                <td align="right" id="Data1">日期：</td>
                <td id="Data2">
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
                <td align="right" id="year1" style="display: none">年份：</td>
                <td id="year2" style="display: none">
                    <select name='year' id="year" class="table_query_label">
                        <%
                            String year = (String) request.getAttribute("year");
                            if (null == year || "".equals(year)) {
                                year = "0";
                            }
                            int y = Integer.parseInt(year);
                        %>
                        <option value="<%=y-1 %>"><%=y -1 %>
                        <option selected value="<%=y %>"><%=y %>
                        </option>
                        <option value="<%=y+1 %>"><%=y + 1 %>
                        </option>
                    </select>
                </td>
                <td align="right">选择大区：</td>
                <td>
                    <input type="text" id="orgCode" name="orgCode" value="" class="middle_txt"/>
                    <input name="obtn" id="obtn" class="mini_btn" type="button" value="&hellip;"
                           onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
                    <input class="mini_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
                </td>
                <td align="right">服务商：</td>
                <td><input type="text" class="middle_txt" name="dealerCode" id="dealerCode" size="17" value=""/>
                    <input name="button2" type="button" class="mini_btn"
                           onclick="showOrgDealer('dealerCode', '', 'true', '', 'false', '', '','');" value="..."/>
                    <input type="button" class="mini_btn" onclick="clrTxt('dealerCode');" value="清 空" id="clrBtn"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    汇总：<input type="radio" name="Query" value="1" checked onclick="showDtl(this)"/>
                    明细：<input type="radio" name="Query" value="2" onclick="showDtl(this)"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/> &nbsp;
                    <input class="normal_btn" type="button" value="导 出" onclick="expExcel();"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" style="color: red;text-align: center;font-weight: bold">提示:完成金额=上传订单金额-退货金额-切换件金额</td>
            </tr>
        </table>
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
    </div>
</form>
</body>

<script language="javascript">
    autoAlertException();//输出错误信息
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/report/partReport/partRebateReport/RebateReport/query.json";
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
            if (chk == 1) {
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {header: "服务商代码", dataIndex: "DEALER_CODE", align: 'center'},
                    {header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
                    {header: "任务金额", dataIndex: "TASK_AMOUNT", style: 'text-align:right'},
                    {header: "上传订单金额", dataIndex: "PART_AMOUNT", style: 'text-align:right'},
                    {header: "退货金额", dataIndex: "RETURN_AMOUNT", style: 'text-align:right'},
                    {header: "切换件金额", dataIndex: "QH_AMOUNT", style: 'text-align:right'},
                    {header: "完成金额", dataIndex: "CP_AMOUNT", style: 'text-align:right'},
                    {header: "完成率", dataIndex: "CP_RATIO", style: 'text-align:right'},
                    {header: "返利金额", dataIndex: "FL_AMOUNT", style: 'text-align:right'}
                ];
            } else {
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {header: "服务商代码", dataIndex: "DEALER_CODE", align: 'center'},
                    {header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
                    {header: "1月任务", dataIndex: "YM_TK_01", style: 'text-align:right'},
                    {header: "1月完成金额", dataIndex: "YM_CP_01", style: 'text-align:right'},
                    {header: "1月完成率", dataIndex: "YM_RT_01", style: 'text-align:center'},
                    {header: "2月任务", dataIndex: "YM_TK_02", style: 'text-align:right'},
                    {header: "2月完成金额", dataIndex: "YM_CP_02", style: 'text-align:right'},
                    {header: "2月完成率", dataIndex: "YM_RT_02", style: 'text-align:center'},
                    {header: "3月任务", dataIndex: "YM_TK_03", style: 'text-align:right'},
                    {header: "3月完成金额", dataIndex: "YM_CP_03", style: 'text-align:right'},
                    {header: "3月完成率", dataIndex: "YM_RT_03", style: 'text-align:center'},
                    {header: "4月任务", dataIndex: "YM_TK_04", style: 'text-align:right'},
                    {header: "4月完成金额", dataIndex: "YM_CP_04", style: 'text-align:right'},
                    {header: "4月完成率", dataIndex: "YM_RT_04", style: 'text-align:center'},
                    {header: "5月任务", dataIndex: "YM_TK_05", style: 'text-align:right'},
                    {header: "5月完成金额", dataIndex: "YM_CP_05", style: 'text-align:right'},
                    {header: "5月完成率", dataIndex: "YM_RT_05", style: 'text-align:center'},
                    {header: "6月任务", dataIndex: "YM_TK_06", style: 'text-align:right'},
                    {header: "6月完成金额", dataIndex: "YM_CP_06", style: 'text-align:right'},
                    {header: "6月完成率", dataIndex: "YM_RT_06", style: 'text-align:center'},
                    {header: "7月任务", dataIndex: "YM_TK_07", style: 'text-align:right'},
                    {header: "7月完成金额", dataIndex: "YM_CP_07", style: 'text-align:right'},
                    {header: "7月完成率", dataIndex: "YM_RT_07", style: 'text-align:center'},
                    {header: "8月任务", dataIndex: "YM_TK_08", style: 'text-align:right'},
                    {header: "8月完成金额", dataIndex: "YM_CP_08", style: 'text-align:right'},
                    {header: "8月完成率", dataIndex: "YM_RT_08", style: 'text-align:center'},
                    {header: "9月任务", dataIndex: "YM_TK_09", style: 'text-align:right'},
                    {header: "9月完成金额", dataIndex: "YM_CP_09", style: 'text-align:right'},
                    {header: "9月完成率", dataIndex: "YM_RT_09", style: 'text-align:center'},
                    {header: "10月任务", dataIndex: "YM_TK_10", style: 'text-align:right'},
                    {header: "10月完成金额", dataIndex: "YM_CP_10", style: 'text-align:right'},
                    {header: "10月完成率", dataIndex: "YM_RT_10", style: 'text-align:center'},
                    {header: "11月任务", dataIndex: "YM_TK_11", style: 'text-align:right'},
                    {header: "11月完成金额", dataIndex: "YM_CP_11", style: 'text-align:right'},
                    {header: "11月完成率", dataIndex: "YM_RT_11", style: 'text-align:center'},
                    {header: "12月任务", dataIndex: "YM_TK_12", style: 'text-align:right'},
                    {header: "12月完成金额", dataIndex: "YM_CP_12", style: 'text-align:right'},
                    {header: "12月完成率", dataIndex: "YM_RT_12", style: 'text-align:center'},
                    {header: "合件任务", dataIndex: "TK_SUM", style: 'text-align:right'},
                    {header: "合计完成金额", dataIndex: "CP_SUM", style: 'text-align:right'},
                    {header: "合计完成率", dataIndex: "SUM_RT", style: 'text-align:center'}
                ];
            }

            len = columns.length;

            $("_page").hide();
            $('myGrid').show();
            new createGrid(title, columns, $("myGrid"), ps).load();
            //分页
            var myTable = $('myTable');
            var tmpRow = myTable.insertRow(myTable.rows.length);
            tmpRow.insertCell(0).innerHTML = "<TR class=table_list_row1><TD</TD>";
            tmpRow.insertCell(1).innerHTML = "<TD></TD>";
            tmpRow.insertCell(2).innerHTML = "<TD >合计</TD>";
            if (chk == 1) {
                tmpRow.insertCell(3).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.TASK_AMOUNT + "</TD>";
                tmpRow.insertCell(4).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.PART_AMOUNT + "</TD>";
                tmpRow.insertCell(5).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.RETURN_AMOUNT + "</TD>";
                tmpRow.insertCell(6).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.QH_AMOUNT + "</TD>";
                tmpRow.insertCell(7).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.CP_AMOUNT + "</TD>";
                tmpRow.insertCell(8).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.CP_RATIO + "</TD>";
                tmpRow.insertCell(9).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.FL_AMOUNT + "</TD>";
            } else {
                for (var i = 1; i <= 12; i++) {
                    var mth = i;
                    if (i < 10) {
                        mth = "0" + i;
                    }
                    tmpRow.insertCell(i * 3).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.YM_TK_" + mth) + "</TD>";
                    tmpRow.insertCell((i * 3) + 1).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.YM_CP_" + mth) + "</TD>";
                    tmpRow.insertCell((i * 3) + 2).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.YM_RT_" + mth) + "</TD>";
                }
                tmpRow.insertCell(39).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.TK_SUM") + "</TD>";
                tmpRow.insertCell(40).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.CP_SUM") + "</TD>";
                tmpRow.insertCell(41).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval("json.SUM_RT") + "</TD>";
            }
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

    function doInit() {
        loadcalendar();//时间初始化
        //initCondition();
        __extQuery__(1);
    }

    //导出
    function expExcel() {
        fm.action = "<%=contextPath%>/report/partReport/partRebateReport/RebateReport/expExcel.do";
        fm.target = "_self";
        fm.submit();
    }

    function showDtl(obj) {
        if (obj.value == '2') {
            document.getElementById("year1").style.display = "";
            document.getElementById("year2").style.display = "";
            document.getElementById("Data1").style.display = "none";
            document.getElementById("Data2").style.display = "none";
        } else {
            document.getElementById("year1").style.display = "none";
            document.getElementById("year2").style.display = "none";
            document.getElementById("Data1").style.display = "";
            document.getElementById("Data2").style.display = "";
        }
        __extQuery__(1);
    }

    //清空
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }

    function createMonthList() {
        var year = ${year};

    }
</script>

</html>