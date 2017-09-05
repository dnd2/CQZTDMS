<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript">
        //获取选择框的值
        function getCode(value) {
            var str = getItemValue(value);

            document.write(str);
        }
        //获取序号
        function getIdx(id) {
            document.write(document.getElementById(id).rows.length - 2);
        }

        //返回
        function goBack() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgInit.do?flag=true';
        }
    </script>
</head>
<body onload="loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置:

        配件管理&gt; 配件销售管理 &gt;配件销售单 &gt;配件销售单查看
    </div>
    <table class="table_query">
        <tr>
            <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 销售单信息</th>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">销售单号:</td>
            <td width="24%">${mainMap.SO_CODE}</td>
            <td class="table_query_2Col_label_5Letter">销售日期:</td>
            <td width="24%">${mainMap.CREATE_DATE}</td>
            <td class="table_query_2Col_label_5Letter">销售制单人:</td>
            <td width="24%">${mainMap.CREATE_BY_NAME}</td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">销售单位:</td>
            <td>${mainMap.SELLER_NAME}</td>
            <td class="table_query_2Col_label_5Letter">订货单位:</td>
            <td>${mainMap.DEALER_NAME}</td>
            <td class="table_query_2Col_label_5Letter">订货制单人:</td>
            <td>${mainMap.BUYER_NAME}</td>
        </tr>
        <tr>
          <%--  <td class="table_query_2Col_label_5Letter">出库仓库:</td>
            <td>
                ${mainMap.WH_NAME}
            </td>
            <td class="table_query_2Col_label_5Letter">接收单位:</td>
            <td>${mainMap.CONSIGNEES}</td>
            <td class="table_query_2Col_label_5Letter">&nbsp;</td>
            <td>&nbsp;</td>--%>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">接收地址:</td>
            <td>${mainMap.ADDR}</td>
            <td class="table_query_2Col_label_5Letter"> 接收人:</td>
            <td>${mainMap.RECEIVER}</td>
            <td class="table_query_2Col_label_5Letter"><span> 接收人电话:</span></td>
            <td>${mainMap.TEL}</td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">邮政编码:</td>
            <td>${mainMap.POST_CODE}</td>
            <td class="table_query_2Col_label_5Letter">到站名称:</td>
            <td>${mainMap.STATION}</td>
            <td class="table_query_2Col_label_5Letter">发运方式:</td>
            <td>
                ${mainMap.TRANS_TYPE}
            </td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">付款方式:</td>
            <td>
                <script type="text/javascript">
                    getCode(${mainMap.PAY_TYPE});
                </script>
            </td>
            <td class="table_query_2Col_label_5Letter">订单类型:</td>
            <td>
                <script type="text/javascript">
                    getCode(${mainMap.ORDER_TYPE});
                </script>
            </td>
            <td class="table_query_2Col_label_5Letter">总金额:</td>
            <td>${mainMap.AMOUNT}</td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">运费支付方式:</td>
            <td>
                <script type="text/javascript">
                    getCode(${mainMap.TRANSPAY_TYPE});
                </script>
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_5Letter">备注:</td>
            <td colspan="5">${mainMap.REMARK}</td>
        </tr>
    </table>

    <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
        <tr>
            <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</th>
        </tr>
        <tr bgcolor="#FFFFCC">
            <td>序号</td>
            <td>件号</td>
            <td>配件编码</td>
            <td>配件名称</td>
            <td>最小包装量</td>
            <td>单位</td>
            <td>采购数量</td>
            <td>销售数量</td>
            <td>销售单价</td>
            <td>销售金额</td>
            <td>当前库存</td>
            <td>备注</td>
        </tr>
        <c:forEach items="${detailList}" var="data">
            <tr class="table_list_row1">
                <td align="center">&nbsp;
                    <script type="text/javascript">
                        getIdx("file");
                    </script>
                </td>
                <td align="center"><c:out value="${data.PART_CODE}"/></td>
                <td align="center"><c:out value="${data.PART_OLDCODE}"/></td>
                <td align="center"><c:out value="${data.PART_CNAME}"/></td>
                <td>&nbsp;<c:out value="${data.MIN_PACKAGE}"/></td>
                <td>&nbsp;<c:out value="${data.UNIT}"/></td>
                <td>&nbsp;<c:out value="${data.BUY_QTY}"/></td>
                <td>&nbsp;<c:out value="${data.SALES_QTY}"/></td>
                <td>&nbsp;<c:out value="${data.BUY_PRICE}"/></td>
                <td>&nbsp;<c:out value="${data.BUY_AMOUNT}"/></td>
                <td>&nbsp;<c:out value="${data.STOCK_QTY}"/></td>
                <td>&nbsp;<c:out value="${data.REMARK}"/></td>
            </tr>
        </c:forEach>
    </table>
    <table id="file1" class="table_list" style="border-bottom:1px solid #DAE0EE">
        <tr>
            <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>单据操作信息</th>
        </tr>
        <tr bgcolor="#FFFFCC">
            <td align="center" width="3%">序号</td>
            <td align="center" width="10%">操作人</td>
            <td align="center" width="13%">操作时间</td>
            <td align="center" width="14%">环节</td>
            <td align="center" width="8%">状态</td>

        </tr>
        <c:forEach items="${historyList}" var="data">
            <tr class="table_list_row1">
                <td align="center">&nbsp;
                    <script type="text/javascript">
                        getIdx("file1");
                    </script>
                </td>
                <td align="center"><c:out value="${data.OPT_NAME}"/></td>
                <td align="center"><c:out value="${data.OPT_DATE}"/></td>
                <td align="center"><c:out value="${data.WHAT}"/></td>
                <td>&nbsp;
                    <script type="text/javascript">
                        getCode(${data.STATUS});
                    </script>
                </td>
            </tr>
        </c:forEach>
    </table>

    <table border="0" class="table_query">
        <tr align="center">
            <td><input class="normal_btn" type="button" value="返 回" onclick="goBack()"/></td>
        </tr>
    </table>
</div>
</body>
</html>

