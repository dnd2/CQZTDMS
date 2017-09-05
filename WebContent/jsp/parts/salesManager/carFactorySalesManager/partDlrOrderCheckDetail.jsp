<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
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
        document.write(document.getElementById(id).rows.length - 1);
    }
    //返回
    function goBack() {
    	_hide();
    	__parent().doQuery(1);
    }

    //zhumingwei 2013-09-16
    //关闭
    function goClose() {
        _hide();
    }

    //明细下载
    function exportDetl() {
        document.fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/exportOrderExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
</script>
</head>
<body>
<div class="wbox page-purchase-order">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 > 配件采购管理 &gt; 配件订单审核 > 采购订单查看</div>
    
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="orderId" id="orderId" value="${orderId }"/>
        <input type="hidden" name="orderCode" id="orderCode" value="${mainMap.ORDER_CODE}"/>
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>采购订单信息</h2>
            <div class="form-body">
                <table class="table_query" border="0" cellSpacing=1 cellPadding=1 width="100%">
                    <tr>
                        <td width="10%" class="right f-bold">订单号：</td>
                        <td width="20%">${mainMap.ORDER_CODE}</td>
                        <td width="10%" class="right f-bold">订货单位：</td>
                        <td width="20%">${mainMap.DEALER_NAME}</td>
                        <td width="10%" class="right f-bold">制单人：</td>
                        <td width="20%">${mainMap.NAME}</td>
                    </tr>
                    <tr>
                        <td width="10%" class="right f-bold">制单日期：</td>
                        <td width="20%">${mainMap.CREATE_DATE}</td>
                        <td width="10%" class="right f-bold">销售单位：</td>
                        <td width="20%">${mainMap.SELLER_NAME}</td>
                        <td width="10%" class="right f-bold">接收单位：</td>
                        <td width="20%">${mainMap.RCV_ORG}</td>
                    </tr>
                    <tr>
                        <td width="10%" class="right f-bold">接收地址：</td>
                        <td colspan="5">${mainMap.ADDR}</td>
                    </tr>
                    <tr>
                        <td width="10%" class="right f-bold"> 接收人：</td>
                        <td width="20%">${mainMap.RECEIVER}</td>
                        <td width="10%" class="right f-bold">接收人电话：</td>
                        <td width="20%">${mainMap.TEL}</td>
                        <td width="10%" class="right f-bold">发运方式：</td>
                        <td width="20%">
                            ${mainMap.TRANS_TYPE}
                    </tr>
                <%--  <tr>
                        <td width="10%" class="right">接收人手机：</td>
                        <td width="20%">${mainMap.MOBILE_PHONE}</td>
                        <td width="10%" class="right">邮政编码：</td>
                        <td width="20%">${mainMap.POST_CODE}</td>
                        <td width="10%" class="right">到站名称：</td>
                        <td>${mainMap.STATION}</td>
                        </td>
                    </tr>--%>
                    <tr>
                        <td width="10%" class="right f-bold">付款方式：</td>
                        <td width="20%">
                            <script type="text/javascript">
                                getCode(${mainMap.PAY_TYPE});
                            </script>
                        </td>
                        <td width="10%" class="right f-bold">订单类型：</td>
                        <td width="20%">
                            <script type="text/javascript">
                                getCode(${mainMap.ORDER_TYPE});
                            </script>
                        </td>
                        <td width="10%" class="right f-bold">订单总金额：</td>
                        <td width="20%">${mainMap.YX_AMOUNT}</td>
                    </tr>
                    <%--<tr>
                        <td width="10%" class="right f-bold">配件金额：</td>
                        <td width="20%">
                            <script type="text/javascript">
                                document.write((${mainMap.AMOUNT}-${mainMap.FREIGHT}).toFixed(2));
                            </script>
                        </td>--%>
                    <%--  <td width="10%" class="right f-bold">运费金额：</td>
                        <td width="20%">${mainMap.FREIGHT}</td>
                    </tr>--%>
                    <tr>
                        <td width="10%" class="right f-bold">备注：</td>
                        <td colspan="5">${mainMap.REMARK}</td>
                    </tr>
                </table>
            </div>
        </div>
        
        <table id="file1" class="table_list">
            <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
            <tr>
                <th>序号</th>
                <th>配件编码</th>
                <th>配件名称</th>
                <th>最小包装量</th>
                <th>单位</th>
                <th>订货数量</th>
                <th>有效数量</th>
                <th style="color: red">关闭数量</th>
                <th>订货单价</th>
                <th>有效订货金额</th>
                <c:if test="${showFlag eq '1'}">
                   <th>当前可用库存</th>
                </c:if>
                <th>备注</th>
            </tr>
            <c:forEach items="${detailList}" var="data">
                <tr class="table_list_row1">
                    <td class="center">
                        <script type="text/javascript">getIdx("file1");</script>
                    </td>
                    <td style="text-align: left"><c:out value="${data.PART_OLDCODE}"/></td>
                    <td style="text-align: left"><c:out value="${data.PART_CNAME}"/></td>
                    <!--  <td class="center"><c:out value="${data.PART_CODE}" /></td> -->
                    <td>&nbsp;<c:out value="${data.MIN_PACKAGE}"/></td>
                    <td>&nbsp;<c:out value="${data.UNIT}"/></td>
                    <td>&nbsp;<c:out value="${data.BUY_QTY}"/></td>
                    <td>&nbsp;<c:out value="${data.YX_QTY}"/></td>
                    <td style="color: red">&nbsp;<c:out value="${data.CLOSE_QTY}"/></td>
                    <td style="text-align: right">&nbsp;<c:out value="${data.BUY_PRICE}"/></td>
                    <td style="text-align: right">&nbsp;<c:out value="${data.YX_AMOUNT}"/></td>
                    <c:if test="${showFlag eq '1'}">
                        <td>&nbsp;<c:out value="${data.STOCK_QTY}"/></td>
                    </c:if>
                    <td>&nbsp;<c:out value="${data.REMARK}"/></td>
                </tr>
            </c:forEach>
        </table>
        <table id="file2" class="table_list">
            <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>单据操作信息</caption>
            <tr>
                <th class="center" width="3%">序号</th>
                <th class="center" width="10%">操作人</th>
                <th class="center" width="13%">操作时间</th>
                <th class="center" width="14%">环节</th>
                <th class="center" width="8%">状态</th>
            </tr>
            <c:forEach items="${historyList}" var="data">
                <tr class="table_list_row1">
                    <td class="center">
                       <script type="text/javascript">getIdx("file2");</script>
                    </td>
                    <td class="center"><c:out value="${data.OPT_NAME}"/></td>
                    <td class="center"><c:out value="${data.OPT_DATE}"/></td>
                    <td class="center"><c:out value="${data.WHAT}"/></td>
                    <td>&nbsp;
                        <script type="text/javascript">getCode(${data.STATUS}); </script>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <table border="0" width="100%" >
            <tr>
                <td align="center">
                    <c:choose>
                        <c:when test="${'disabled' eq buttonFalg}">
                            <input class="u-button" type="button" value="明细下载" onclick="exportDetl()"/>
                            <input class="u-button u-cancel" type="button" value="关 闭" onclick="goClose()"/>
                        </c:when>
                        <c:otherwise>
                            <input class="u-button u-cancel" type="button" value="返 回" onclick="goBack()"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </form>
</div>    

</body>
</html>

