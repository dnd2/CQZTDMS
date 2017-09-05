<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <script type="text/javascript">
        //获取选择框的值
        function getCode(value) {
            var str = getItemValue(value);
            document.write(str);
        }
        function clickCheckBox() {
            var flag = true;
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if (!cb[i].checked) {
                    flag = false;
                }
            }
            document.getElementById("cbAll").checked = flag;

        }
        function checkAll(obj) {
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                cb[i].checked = obj.checked;
            }
        }
        function createTransOrderConfirm() {
            if (!validateData()) {
                return;
            }
            MyConfirm("确定生成出运单?", createTransOrder, [])
        }
        function createTransOrder() {
            disableAllClEl();
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/createTransOrder.json?";
            sendAjax(url, getResult, 'fm');
        }
        function validateData() {
            var msg = "";
            if (document.getElementById("transportOrg").value == "") {
                msg += "请填写托运单位!</br>"
            }
            if (document.getElementById("linkman").value == "") {
                msg += "请填写托运负责人!</br>"
            }
            if (document.getElementById("tel2").value == "") {
                msg += "请填写托运负责人电话!</br>"
            }
            if (msg != "") {
                MyAlert(msg);
                return false;
            }
            return true;
        }
        function disabledCb() {
            var cbs = document.getElementsByName("cb");
            for (var i = 0; i < cbs.length; i++) {
                cbs[i].disabled = (!cbs[i].checked);
            }
        }
        function unDisabledCb() {
            var cbs = document.getElementsByName("cb");
            for (var i = 0; i < cbs.length; i++) {
                cbs[i].disabled = false;
            }
        }
        function getResult(jsonObj) {
            enableAllClEl();
            if (jsonObj != null) {
                var success = jsonObj.success;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (success) {
                    MyAlert(success);
                    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/partTransInit.do";
                } else if (error) {
                    unDisabledCb();
                    MyAlert(error);
                } else if (exceptions) {
                    unDisabledCb();
                    MyAlert(exceptions.message);
                }
            }
        }
        //返回
        function goBack() {
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/partTransInit.do";
        }
        //获取序号
        function getIdx() {
            document.write(document.getElementById("file").rows.length - 2);
        }
        function disableAllA() {
            var inputArr = document.getElementsByTagName("a");
            for (var i = 0; i < inputArr.length; i++) {
                inputArr[i].disabled = true;
            }
        }

        function enableAllA() {

            var inputArr = document.getElementsByTagName("a");
            for (var i = 0; i < inputArr.length; i++) {
                inputArr[i].disabled = false;
            }
        }
        function disableAllBtn() {
            var inputArr = document.getElementsByTagName("input");
            for (var i = 0; i < inputArr.length; i++) {
                if (inputArr[i].type == "button") {
                    inputArr[i].disabled = true;
                }
            }
        }
        function enableAllBtn() {
            var inputArr = document.getElementsByTagName("input");
            for (var i = 0; i < inputArr.length; i++) {
                if (inputArr[i].type == "button") {
                    inputArr[i].disabled = false;
                }
            }
        }
        function disableAllClEl() {
            disableAllA();
            disableAllBtn();
        }
        function enableAllClEl() {
            enableAllBtn();
            enableAllA();
        }
    </script>
</head>

<body onload="loadcalendar();enableAllClEl()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <input type="hidden" name="outId" id="outId" value="${mainMap.OUT_ID}"/>

        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:

            配件管理 &gt; 配件销售管理 &gt;发运单生成
        </div>
        <table class="table_query" cellSpacing="3px">
            <th colspan="7" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 采购订单信息</th>
            <tr>
            	
                <td   align="right">销售日期:</td>
                <td width="24%"><fmt:formatDate value="${mainMap.SALE_DATE}" type="date"/></td>
                <td   align="right">销售制单人:</td>
                <td width="24%">${mainMap.CREATE_BY_NAME}</td>
                <td   align="right">&nbsp;</td>
                <td width="24%">&nbsp;</td>
            </tr>
            <tr>
            	
                <td   align="right">销售单位:</td>
                <td width="24%">${mainMap.SELLER_NAME}</td>
                <td   align="right">订货单位:</td>
                <td width="24%">${mainMap.DEALER_NAME}</td>
                <td   align="right">采购制单人:</td>
                <td width="24%">${mainMap.orderCreateByName}</td>
            </tr>
            <tr>
            	
                <td   align="right">订货日期:</td>
                <td width="24%">${mainMap.orderCreateDate} </td>
                <td   align="right">出库仓库:</td>
                <td width="24%">${mainMap.WH_NAME}</td>
                <td   align="right">接收单位:</td>
                <td width="24%">${mainMap.CONSIGNEES}</td>
            </tr>
            <tr>
            	
                <td   align="right">接收地址:</td>
                <td width="24%">${mainMap.ADDR}</td>
                <td   align="right">接收人:</td>
                <td width="24%">${mainMap.RECEIVER}</td>
                <td   align="right">接收人电话:</td>
                <td width="24%">${mainMap.TEL}</td>
            </tr>
            <tr>
            	
                <td   align="right">邮政编码:</td>
                <td width="24%">${mainMap.POST_CODE}</td>
                <td   align="right">到站名称:</td>
                <td width="24%">${mainMap.STATION}</td>
                <td   align="right">发运方式:</td>
                <td width="24%">
                    ${mainMap.TRANS_TYPE}
                </td>
            </tr>
            <tr>
            	
                <td   align="right">付款方式:</td>
                <td width="24%">
                    <script type="text/javascript">
                        getCode(${mainMap.PAY_TYPE});
                    </script>
                </td>
                <td   align="right">订单类型:</td>
                <td width="24%">
                    <script type="text/javascript">
                        getCode(${mainMap.ORDER_TYPE});
                    </script>
                </td>
                <td   align="right">运费支付方式:</td>
                <td width="24%">
                    <script type="text/javascript">
                        getCode(${mainMap.TRANSPAY_TYPE});
                    </script>
                </td>
            </tr>
            <tr>
            	
                <td   align="right">托运单位:</td>
                <td width="24%"><input class="middle_txt" type="text" name="transportOrg" id="transportOrg"/></td>
                <td   align="right">托运负责人:</td>
                <td width="24%"><input class="middle_txt" type="text" name="linkman" id="linkman"/></td>
                <td   align="right">托运负责人电话:</td>
                <td width="24%"><input class="middle_txt" type="text" name="tel2" id="tel2"/></td>
            </tr>
            <tr>
            	
                <td   align="right">备注:</td>
                <td colspan="6">
                    <textarea name="textarea" cols="120" rows="4"></textarea>
                </td>
            </tr>
        </table>
        <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
            <tr>
                <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</th>
            </tr>
            <tr bgcolor="#FFFFCC">
                <td align="center" style="display:none" width="2%"><input type="checkbox" onclick="checkAll(this)" id="cbAll" name="cbAll"
                                                     checked/></td>
                <td align="center" width="3%">序号</td>
                <td align="center" width="9%">件号</td>
                <td align="center" width="11%">配件编码</td>
                <td align="center" width="9%">配件名称</td>
                <td align="center" width="7%">最小包装量</td>
                <td width="6%" align="center">当前库存</td>
                <td width="7%" align="center">数量</td>
                <td align="center" width="6%">单位</td>
                <td align="center" width="6%">出库货位</td>
                <%--<td align="center" width="8%">包装方式</td>
                --%>
                <td align="center" width="12%">备注</td>
            </tr>
            <c:forEach items="${detailList}" var="data">
            <tr class="table_list_row1">
                <td style="display:none" align="center">&nbsp;
                    <input type="checkbox" id="cb" name="cb" value="${data.OUTLINE_ID}" onclick="clickCheckBox()"
                           checked/>
                </td>
                <td align="center">&nbsp;
                    <script type="text/javascript">
                        getIdx();
                    </script>
                </td>
                <td align="center"><c:out value="${data.PART_CODE}"/></td>
                <td align="center"><c:out value="${data.PART_OLDCODE}"/></td>
                <td align="center"><c:out value="${data.PART_CNAME}"/></td>
                <td>&nbsp;<c:out value="${data.MIN_PACKAGE}"/></td>
                <td>&nbsp;<c:out value="${data.NORMAL_QTY_NOW}"/></td>
                <td>&nbsp;<c:out value="${data.OUTSTOCK_QTY}"/></td>

                <td>&nbsp;<c:out value="${data.UNIT}"/></td>
                <td>&nbsp;<c:out value="${data.LOC_NAME}"/></td>
                    <%--<td>&nbsp;
                        <script type="text/javascript">
                          getCode(${data.PKG_TYPE});
                        </script>
                    </td>
                    --%>
                <td>&nbsp;<c:out value="${data.REMARK}"/></td>
            </tr>
            </c:forEach>
            <table border="0" class="table_query">
                <tr align="center">
                    <td><input class="long_btn" type="button" value="生成发运单" onclick="createTransOrderConfirm();"/>
                        <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/></td>
                </tr>
            </table>
    </div>
</form>
</body>
</html>
