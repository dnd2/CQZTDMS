<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件BO单查询</title>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype();' -->
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件销售管理 >
        BO单查询
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td width="10%" class="right">BO单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="BO_CODE" id="BO_CODE"/></td>
	                <td width="10%" class="right">BO生成日期：</td>
	                <td width="25%">
	                    <input name="startDate" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;"/>
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                    至
	                    <input name="endDate" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;"/>
	                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
	                </td>
	                <td width="10%" class="right">订货单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">单位编码：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE"/></td>
	                <td width="10%" class="right">单位名称：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME"/></td>
	                <td width="10%" class="right">销售单位：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="SELLER_NAME" id="SELLER_NAME"/></td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">配件编码：</td>
	                <td width="20%"><input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/></td>
	                <td width="10%" class="right">BO单状态：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("STATE", <%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE%>, "<%=Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td width="10%" class="right">订单类型：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	                <%-- <td width="10%"   class="right">销售产生BO：</td>
	                 <td width="20%">
	                     <script type="text/javascript">
	                         genSelBoxExp("SB", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_YES%>", false, "short_sel", "", "false", '');
	                     </script>
	                 </td>--%>
	            </tr>
	            <c:if test="${salerFlag}">
	                <tr>
	                    <td width="10%" class="right">销售人员：</td>
	                    <td width="20%">
	                        <select name="salerId" id="salerId" class="u-select">
	                            <option value="">--请选择--</option>
	                            <c:forEach items="${salerList}" var="saler">
	                                <c:choose>
	                                    <c:when test="${curUserId eq saler.USER_ID}">
	                                        <option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
	                                    </c:when>
	                                    <c:otherwise>
	                                        <option value="${saler.USER_ID}">${saler.NAME}</option>
	                                    </c:otherwise>
	                                </c:choose>
	                            </c:forEach>
	                        </select>
	                    </td>
	                    <td width="10%" class="right">BO单类型：</td>
	                    <td width="20%">
	                        <select name="boType" id="boType" class="u-select">
	                            <option value="">--请选择--</option>
	                            <option value="1">一般BO</option>
	                            <option value="2">现场BO</option>
	                        </select>
	                    </td>
	                </tr>
	            </c:if>
	
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询"
	                           onclick="__extQuery__(1);"/>
	                    <input name="BtnQuery" id="queryBtn2" class="u-button" type="button" value="汇总查询"
	                           onclick="queryAll();"/>
	                    <input name="BtnQuery" id="queryBtn3" class="u-button" type="button" value="明细查询"
	                           onclick="queryDtl();"/>
	                    <input class="u-button" type="button" value="导 出" onclick="expPartBoExcel();"/>
	                </td>
	            </tr>
	        </table>
	        </div>
	        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoinfo.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'BO_ID', renderer: myLink, align: 'center'},
            {header: "BO单号", dataIndex: 'BO_CODE', style: 'text-align:left', renderer: viewBoOrder},
            {header: "订货单号", dataIndex: 'ORDER_CODE', style: 'text-align:left', renderer: viewOrder},
            {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
//            {header: "销售单位", dataIndex: 'SELLER_NAME', style: 'text-align:left'},
            {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
            {header: "BO产生日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "满足数量", dataIndex: 'SALES_QTY', align: 'center'},
            /*  {header: "订货金额", dataIndex: 'ORDER_AMOUNT', align: 'center'},*/
            {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
            /*   {header: "BO金额", dataIndex: 'BO_AMOUNT', align: 'center'},*/
            {header: "转销售数量", dataIndex: 'TOSAL_QTY', align: 'center'},
            {header: "关闭数量", dataIndex: 'CLOSE_QTY', align: 'center'},
            {header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align: 'center'},
            /*	{header: "BO分配次数", dataIndex: 'BO_COUNT', align:'center'},*/
            {header: "BO单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
        ];

        //设置超链接
        function myLink(value, meta, record) {
            var orderId = record.data.ORDER_ID;
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + orderId + ")'>[查看]</a>");
        }
        //导出
        function expPartBoExcel() {
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/expPartBoExcel.do";
            fm.target = "_self";
            fm.submit();
        }

        function viewBoOrder(value, meta, record) {
            var boId = record.data.BO_ID;
            var orderId = record.data.ORDER_ID;
            var boCode = record.data.BO_CODE;
            return String.format("<a href=\"#\" title='查看BO订单' onclick='view(\"" + boId + "\",\"" + orderId + "\")' >" + boCode + "</a>");
        }

        function view(value, orderId) {
            var buttonFalg = "disabled";
            OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetailInit.do?boId=' + value + '&orderId=' + orderId + '&flag=0&buttonFalg=' + buttonFalg, 1000, 440);
            //window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetailInit.do?boId='+value+'&orderId='+orderId+'&flag='+0;
        }

        function viewOrder(value, meta, record) {
            var ORDER_ID = record.data.ORDER_ID;
            var ORDER_CODE = record.data.ORDER_CODE;
            if (ORDER_CODE != null) {
                return String.format("<a href=\"#\" title='查看订单' onclick='viewOrderDtl(\"" + ORDER_ID + "\",\"" + ORDER_CODE + "\")' >" + ORDER_CODE + "</a>");
            } else {
                return String.format("");
            }
        }

        function viewOrderDtl(ORDER_ID, ORDER_CODE) {
            var buttonFalg = "disabled";//用于判断跳转页面是返回还是关闭
            OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=' + ORDER_ID + '&orderCode=' + ORDER_CODE + '&buttonFalg=' + buttonFalg, 900, 500);
        }

        //汇总查询
        function queryAll() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryAllPartBoInit.do';
        }
        //明细查询
        function queryDtl() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDtlInit.do';
        }

        Date.prototype.format = function (format) {
            var o = {
                "M+": this.getMonth() + 1, //month
                "d+": this.getDate(),    //day
                "h+": this.getHours(),   //hour
                "m+": this.getMinutes(), //minute
                "s+": this.getSeconds(), //second
                "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
                "S": this.getMilliseconds() //millisecond
            }
            if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
                    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)if (new RegExp("(" + k + ")").test(format))
                format = format.replace(RegExp.$1,
                                RegExp.$1.length == 1 ? o[k] :
                                ("00" + o[k]).substr(("" + o[k]).length));
            return format;
        }
    </script>
</div>
</body>
</html>