<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <title>配件发运计划-发运计划单</title>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="whId" id="whId" value="${mainMap.WH_ID}"/>
    <input type="hidden" name="boxIds" id="boxIds" value="${boxIds}"/>
    <input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderIds}"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt; 配件发运计划 &gt; 发运计划单
        </div>
        <div class="form-panel">
		     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件销售信息</h2>
		     <div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td width="10%" class="right">拣货单号：</td>
	                <td width="20%" colspan="5">${pickOrderIds}</td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">拣货日期：</td>
	                <td width="20%"><fmt:formatDate value="${mainMap.PICK_ORDER_CREATE_DATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                <td width="10%" class="right">制单人：</td>
	                <td width="20%">${CREATE_BY_NAME}</td>
	                <td width="10%" class="right">销售单位：</td>
	                <td width="20%">
	                    ${mainMap.SELLER_NAME}
	                    <input type="hidden" name="SELLER_ID" value="${mainMap.SELLER_ID}"/>
	                    <input type="hidden" name="SELLER_CODE" value="${mainMap.SELLER_CODE}"/>
	                    <input type="hidden" name="SELLER_NAME" value="${mainMap.SELLER_NAME}"/>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">订货单位：</td>
	                <td width="20%">
	                    ${mainMap.DEALER_NAME}
	                    <input type="hidden" name="DEALER_ID" value="${mainMap.DEALER_ID}"/>
	                    <input type="hidden" name="DEALER_CODE" value="${mainMap.DEALER_CODE}"/>
	                    <input type="hidden" name="DEALER_NAME" value="${mainMap.DEALER_NAME}"/>
	                </td>
	                <td width="10%" class="right">订单类型：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        document.write(getItemValue(${mainMap.ORDER_TYPE}));
	                    </script>
	                    <input type="hidden" name="ORDER_TYPE" value="${mmainMapainMap.ORDER_TYPE}"/>
	                </td>
	                <td width="10%" class="right"></td>
	                <td width="20%"></td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">收货地址：</td>
	                <td colspan="5">
	                    <select id="addrId" name="addrId" style="width: 350px"  class="u-select">
	                        <%--<option value="${mainMap.ADDR_ID}">${mainMap.ADDR}</option>--%>
	                        <%--<c:if test="${salerFlag}">--%>
	                        <c:forEach items="${addrList}" var="addrList">
	                            <c:choose>
	                                <c:when test="${mainMap.ADDR_ID eq addrList.ADDR_ID}">
	                                    <option selected="selected" value="${addrList.ADDR_ID}">${addrList.ADDR}</option>
	                                </c:when>
	                                <c:otherwise>
	                                    <option value="${addrList.ADDR_ID}">${addrList.ADDR}</option>
	                                </c:otherwise>
	                            </c:choose>
	                        </c:forEach>
	                        <%--</c:if>--%>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">发运方式：</td>
	                <td width="20%">
	                    <select name="transType" id="transType" class="u-select">
	                        <option value="">--请选择--</option>
	                        <c:forEach items="${listf}" var="obj">
	<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
									<option value="${obj.TV_ID}">${obj.TV_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                <td width="10%" class="right">承运物流：</td>
	                <td width="20%">
	                    <select name="transportOrg" id="transportOrg"  class="u-select">
	                        <option value="">--请选择--</option>
	                        <c:forEach items="${listc}" var="obj">
	<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
									<option value="${obj.LOGI_CODE}">${obj.LOGI_FULL_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                <td width="10%" class="right">箱数：</td>
	                <td width="20%" style="color: red;font-weight: bold;font-size: 15px">
	                    ${mainMap.boxAllNo}
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">箱号：</td>
	                <td colspan="5" class="left" style="text-align: left">
	                    <textarea name="pkgNos" cols="100" rows="8" readonly>${pkgNos}</textarea>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">备注：</td>
	                <td colspan="5" class="left">
	                    <textarea name="remark" cols="100" rows="4">${mainMap.REMARK}</textarea>
	                </td>
	            </tr>
	            <tr>
                <td  class="center"  colspan="6">
                    <input class="u-button" type="button" value="生成运单" onclick="createPartOutstockOrderConfirm();"/>
<!--                     <input class="u-button" type="button" value="生成运单并打印" onclick="createPartOutstockOrderConfirm(1);"/> -->
                    <input class="u-button" type="button" value="返 回" onclick="goBack()"/>
                </td>
            </tr>
	        </table>
	    </div>
	    </div>    
    </div>
</form>
</body>
<script language="javascript">
    function createPartOutstockOrderConfirm(flag) {
        var printFlag = flag == '1' ? '1' : '0';
        if ($("#transType")[0].value == "") {
            MyAlert("发运方式必填!");
            return;
        }
        if ($("#transportOrg")[0].value == "") {
            MyAlert("承运物流必填!");
            return;
        }
        MyConfirm("确定生成配件发运计划单?", createPartOutstockOrder, [printFlag]);
    }
    function createPartOutstockOrder(printFlag) {
    	btnDisable();
        var url = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransPlan/createPartOutstockOrder.json?printFlag=" + printFlag;
        makeNomalFormCall(url, getResult, 'fm');
    }
    function getResult(jsonObj) {
    	btnEnable();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            var printFlag = jsonObj.printFlag;
            if (success) {
                MyAlert(success);
                if (printFlag == "1") {
                	//弹出打印页面
                    printprintPrintInfo();
                } else {
                    window.location.href = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransPlan/init.do";
                }
            } else if (error) {
                MyAlert(error);
            }/*  else if (exceptions) {
                MyAlert(exceptions.message);
            } */
        }
    }
    //返回
    function goBack() {
        window.location.href = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransPlan/init.do";
    }
    //获取选择框的值
    function getCode2(value) {
        var str = getItemValue(value);
        document.write(str);
    }
   
    function printprintPrintInfo() {
		window.open("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartTransPlan/init.do?flag=pickPrint");
    }
</script>
</html>