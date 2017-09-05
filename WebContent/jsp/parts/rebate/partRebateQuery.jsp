<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件返利控制表</title>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt财务管理&gt;返利关联&gt;配件返利控制表
    </div>
    <table class="table_query">
        <input type="hidden" id="queryType" name="queryType" value="0"/>
        <input type="hidden" id="type" name="type" value="0"/>
        <input type="hidden" id="flag" name="flag" value="1"/>
        <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
            <td align="right">年-季度：</td>
            <td align="left">
                <select name="year" id="year" class="min_sel">
                    <%
                        String year = (String) request.getAttribute("curYear");
                        if (null == year || "".equals(year)) {
                            year = "0";
                        }
                        int y = Integer.parseInt(year);
                    %>
                    <option selected value="">-请选择-</option>
                    <option value="<%=y-1 %>"><%=y - 1 %>
                    </option>
                    <option value="<%=y %>"><%=y %>
                    </option>
                </select>
                <select name="month" id="month" class="min_sel">
                    <option value="">-请选择-</option>
                    <%
                        for (int i = 1; i <= 4; i++) {
                    %>
                    <option value="<%=i %>"><%=i %>
                            <%
                        }
                    %>
                </select>
                至
                <select name="month2" id="month2" class="min_sel">
                    <option value="">-请选择-</option>
                    <%
                        for (int i = 1; i <= 4; i++) {
                    %>
                    <option value="<%=i %>"><%=i %>
                            <%
                        }
                    %>
                </select>
            </td>
            <td align="right">选择服务商：</td>
            <td>
                <input type="text" class="middle_txt" name="dealerName" size="15" value="" id="dealerName"/>
                <input type="hidden" class="middle_txt" name="dealerCode" size="15" value=""
                       id="dealerCode"/>
                <input name="button2" type="button" class="mini_btn"
                       onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');"
                       value="..."/>
                <input type="button" class="mini_btn" onclick="clrTxt('dealerCode');clrTxt('dealerName');"
                       value="清 空" id="clrBtn"/>
            </td>
        </tr>
        <tr align="center">
            <td colSpan='6'>
                <input class="normal_btn" onclick="__extQuery__(1);" value=查询 type="button" name="queryBtn"
                       id="queryBtn"/>
                <input class="normal_btn" value="导出" type="button" name="queryBtn2" onclick="expExcel();"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</div>
<script type="text/javascript">
    autoAlertException();//输出错误信息
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/financeManager/dealerRateManager/RebateManager/rebateQuery.json";
    var title = null;
    var columns = [];
    function callBack(json) {
        var ps;
        var yf;
        var cd;
        var m1;
        var m2;
        var cols = 0;
        //设置对应数据
        if (Object.keys(json).length > 0) {
            keys = Object.keys(json);
            for (var i = 0; i < keys.length; i++) {
                if (keys[i] == "yfMap") {
                    yf = json[keys[i]];
                }
                if (keys[i] == "cdMap") {
                    cd = json[keys[i]];
                }
                if (keys[i] == "m1") {
                    m1 = json[keys[i]];
                }
                if (keys[i] == "m2") {
                    m2 = json[keys[i]];
                }
                if (keys[i] == "ps") {
                    ps = json[keys[i]];
                }
            }
        }
        //生成数据集
        if (ps.records != null) {
            columns = [
                {header: "序号", align: 'center', renderer: getIndex},
                {header: "省份", dataIndex: 'REGION_NAME', align: 'left'},
                {header: "服务商代码", dataIndex: 'DEALER_CODE', align: 'left'},
                {header: "服务商名称", dataIndex: 'DEALER_NAME', align: 'center', style: 'text-align:left'},
                {header: "返利类型", dataIndex: 'FIN_RETURN_NAME', align: 'center', style: 'text-align:left'},
                {header: "未兑现金额", dataIndex: 'WD_AMOUNT', align: 'center', style: 'text-align:right'},
                {header: "应返小计", dataIndex: 'YF_AMOUNT', align: 'center', style: 'text-align:right'}
            ];
            if (Object.keys(yf).length > 0) {
                var yfkeys = Object.keys(yf);
                cols = yfkeys.length;
                for (var i = 0; i < yfkeys.length; i++) {
                    columns.push({
                        header: yf[yfkeys[i]],
                        dataIndex: yfkeys[i],
                        align: 'center',
                        style: 'text-align:right'
                    });
                }
            }
            columns.push({header: "兑现小计", dataIndex: 'CD_AMOUNT', align: 'center', style: 'text-align:right'});
            if (Object.keys(cd).length > 0) {
                var cdkeys = Object.keys(cd);
                for (var i = 0; i < cdkeys.length; i++) {
                    columns.push({
                        header: cd[cdkeys[i]],
                        dataIndex: cdkeys[i],
                        align: 'center',
                        style: 'text-align:right'
                    });
                }
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
            tmpRow.insertCell(2).innerHTML = "<TD></TD>";
            tmpRow.insertCell(3).innerHTML = "<TD></TD>";
            tmpRow.insertCell(4).innerHTML = "<TD>总计</TD>";
            tmpRow.insertCell(5).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.SWD_AMOUNT + "</TD>";
            tmpRow.insertCell(6).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.SYF_AMOUNT + "</TD>";
            if (Object.keys(yf).length > 0) {
                for (var i = 0; i <= yfkeys.length - 1; i++) {
                    tmpRow.insertCell(6 + (i + 1)).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval('json.SYFM' + yfkeys[i].substr(3)) + "</TD>";
                }
            }
            if (cols == 0) {
                tmpRow.insertCell(7).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.SCD_AMOUNT + "</TD>";
            } else {
                tmpRow.insertCell(7 + cols).innerHTML = "<TD style='TEXT-ALIGN: right'>" + json.SCD_AMOUNT + "</TD>";
            }
            if (Object.keys(cd).length > 0) {
                for (var i = 0; i <= cdkeys.length - 1; i++) {
                    tmpRow.insertCell(8 + i + cols).innerHTML = "<TD style='TEXT-ALIGN: right'>" + eval('json.SCDM' + cdkeys[i].substr(3)) + "</TD>";
                }
            }
            myPage = new showPages("myPage", ps, url);
            myPage.printHtml();
        }
        else {
            $("_page").show();
            $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
            $("myPage").innerHTML = "";
            removeGird('myGrid');
            $('myGrid').hide();
            hiddenDocObject(1);
        }
    }
    //清空
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }

    //下载
    function expExcel() {
        fm.action = "<%=contextPath%>/parts/financeManager/dealerRateManager/RebateManager/expExcel.do";
        fm.target = "_self";
        fm.submit();
    }

</script>
</body>
</html>
