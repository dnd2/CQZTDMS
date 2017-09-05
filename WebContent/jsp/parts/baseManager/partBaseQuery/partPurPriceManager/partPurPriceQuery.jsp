<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>采购价格查询</title>
<script type="text/javascript">
    //autoAlertException();//输出错误信息

    var myPage;
    var url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partPurPriceManager/partPurPriceAction/queryPartBuyPriceInfo.json";

    var title = null;

    var columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:center'},
        {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:center'},
        {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:center'},
        {header: "供应商编码", dataIndex: 'VENDER_CODE',  style: 'text-align:center'},
        {header: "供应商名称", dataIndex: 'VENDER_NAME',  style: 'text-align:center'},
        {header: "采购价格(元)", width: '10%', dataIndex: 'BUY_PRICE',  style: 'text-align:center'},
        {header: "是否暂估", dataIndex: 'IS_GUARD',  style: 'text-align:center', renderer: getItemValueWithSelect},
        // {header: "采购员", dataIndex: 'BUYER_NAME',  style: 'text-align:center'},
        // {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},
        {header: "新增日期", dataIndex: 'CREATE_DATE',  style: 'text-align:center'},
        {header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align:center'},
        {header: "修改人", dataIndex: 'ACNT',  style: 'text-align:center'},
        {header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center', renderer: getItemValue}
    ];

    //是否暂估值转换
    function getItemValueWithSelect(value, meta, record) {
        var guardYes = <%=Constant.IS_GUARD_YES%>;
        if (guardYes == value) {
            return String.format("是");
        }
        else {
            return String.format("否");
        }
    }

    //导出采购价格数据
    function exportPartBuyPriceExcel() {
        fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partPurPriceManager/partPurPriceAction/exportPartBuyPriceExcel.do";
        fm.target = "_self";
        fm.submit();
    }

    $(function(){__extQuery__(1);});
</script>
</head>
<body> 
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息查询 &gt;采购价格查询</div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <input type="hidden" name="partId" id="partId"/>
        <div class="form-panel">
            <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">配件编码：</td>
                        <td><input class="middle_txt" type="text" name="PART_OLDCODE"/></td>
                        <td class="right">配件名称：</td>
                        <td><input class="middle_txt" type="text" name="PART_CNAME"/></td>
                        <td class="right">件号：</td>
                        <td><input class="middle_txt" type="text" name="PART_CODE"/></td>
                    </tr>
                    <tr>
                        <td class="right">供应商名称：</td>
                        <td><input class="middle_txt" type="text" name="VENDER_NAME"/></td>
                        <td class="right">是否有效：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBoxExp("STATE", <%=Constant.STATUS %>, "<%=Constant.STATUS_ENABLE%>", true, "short_sel u-select", "", "false", '');
                            </script>
                        </td>
                        <td class="right">是否暂估：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBoxExp("IS_GUARD", <%=Constant.IS_GUARD%>, "", true, "short_sel u-select", "", "false", '');
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="center" colspan="6">
                            <input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
                            <input class="u-button" type="button" value="重 置" onclick="reset();"/>
                            <input class="u-button" type="button" value="导 出" onclick="exportPartBuyPriceExcel();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

</div>
</body>
</html>